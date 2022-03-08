package contacts.emb.service.standard;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import contacts.commun.dto.DtoCategorie;
import contacts.commun.exception.ExceptionValidation;
import contacts.commun.service.IServiceCategorie;
import contacts.emb.dao.IDaoCategorie;
import contacts.emb.dao.IDaoPersonne;
import contacts.emb.dao.IManagerTransaction;
import contacts.emb.data.Categorie;
import contacts.emb.data.mapper.IMapperEmb;
import contacts.emb.service.util.IManagerSecurity;
import contacts.emb.service.util.UtilServices;


@Component
public class ServiceCategorie implements IServiceCategorie {

	
	// Champs 

	@Inject
	private IManagerSecurity	managerSecurite;
	@Inject
	private	IManagerTransaction	managerTransaction;
	@Inject
	private IMapperEmb			mapper;
	@Inject
	private IDaoCategorie		daoCategorie;
	@Inject
	private IDaoPersonne		daoPersonne;
	
	
	// Injecteurs
	
	public void setManagerSecurite( IManagerSecurity managerSecurite ) {
		this.managerSecurite = managerSecurite;
	}
	
	public void setManagerTransaction( IManagerTransaction managerTransaction ) {
		this.managerTransaction = managerTransaction;
	}
	
	public void setMapper( IMapperEmb mapper ) {
		this.mapper = mapper;
	}
	
	public void setDaoCategorie( IDaoCategorie daoCategorie ) {
		this.daoCategorie = daoCategorie;
	}
	
	public void setDaoPersonne( IDaoPersonne daoPersonne ) {
		this.daoPersonne = daoPersonne;
	}
	

	// Actions 

	@Override
	public int inserer( DtoCategorie dtoCategorie ) throws ExceptionValidation {
		try {
			
			managerSecurite.verifierAutorisationAdmin();
			verifierValiditeDonnees( dtoCategorie );
	
			managerTransaction.begin();
			int id = daoCategorie.inserer( mapper.map( dtoCategorie ) );
			managerTransaction.commit();
			return id;

		} catch ( Exception e ) {
	    	managerTransaction.rollbackSiApplicable();
			throw UtilServices.exceptionValidationOrAnomaly(e);
		}
	}

	@Override
	public void modifier( DtoCategorie dtoCategorie ) throws ExceptionValidation {
		try  {
		
			managerSecurite.verifierAutorisationAdmin();
			verifierValiditeDonnees( dtoCategorie );
	
			managerTransaction.begin();
			daoCategorie.modifier( mapper.map( dtoCategorie ) );
			managerTransaction.commit();

		} catch ( Exception e ) {
	    	managerTransaction.rollbackSiApplicable();
			throw UtilServices.exceptionValidationOrAnomaly(e);
		}
	}

	@Override
	public void supprimer( int idCategorie ) throws ExceptionValidation {
		try {
			
			managerSecurite.verifierAutorisationAdmin();

            if ( daoPersonne.compterPourCategorie(idCategorie) != 0 ) {
                throw new ExceptionValidation( "La catégorie n'est pas vide" );
            }

			managerTransaction.begin();
			daoCategorie.supprimer(idCategorie);
			managerTransaction.commit();

		} catch ( Exception e ) {
	    	managerTransaction.rollbackSiApplicable();
			throw UtilServices.exceptionValidationOrAnomaly(e);
		}
	}

	@Override
	public DtoCategorie retrouver( int idCategorie ) {
		try {
			
			managerSecurite.verifierAutorisationUtilisateurOuAdmin();
			return mapper.map( daoCategorie.retrouver(idCategorie) );

		} catch ( Exception e ) {
			throw UtilServices.exceptionAnomaly(e);
		}
	}

	@Override
	public List<DtoCategorie> listerTout() {
		try {

			managerSecurite.verifierAutorisationUtilisateurOuAdmin();
			List<DtoCategorie> liste = new ArrayList<>();
			for( Categorie categorie : daoCategorie.listerTout() ) {
				liste.add( mapper.map( categorie ) );
			}
			return liste;

		} catch ( Exception e ) {
			throw UtilServices.exceptionAnomaly(e);
		}
	}

	
	
	// Méthodes auxiliaires
	
	private void verifierValiditeDonnees( DtoCategorie dtoCategorie ) throws ExceptionValidation {
		
		StringBuilder message = new StringBuilder();
		
		if ( dtoCategorie.getLibelle() == null || dtoCategorie.getLibelle().isEmpty() ) {
			message.append( "\nLe libellé est absent." );
		} else 	if ( dtoCategorie.getLibelle().length() < 3 ) {
			message.append( "\nLe libellé est trop court." );
		} else 	if ( dtoCategorie.getLibelle().length() > 25 ) {
			message.append( "\nLe libellé est trop long." );
		}
		
		if ( message.length() > 0 ) {
			throw new ExceptionValidation( message.toString().substring(1) );
		}
	}

}
