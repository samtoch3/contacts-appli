package contacts.emb.service.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import contacts.commun.dto.DtoCategorie;
import contacts.commun.dto.DtoPersonne;
import contacts.commun.exception.ExceptionValidation;
import contacts.commun.service.IServiceCategorie;
import contacts.commun.service.IServicePersonne;
import contacts.emb.service.util.IManagerSecurity;
import contacts.emb.service.util.UtilServices;


@Component
public class ServiceCategorie implements IServiceCategorie {

	
	// Champs 
	
	@Inject
	private Donnees						donnees;
	@Inject
	private IManagerSecurity			managerSecurite;
	@Inject
	private IServicePersonne			servicePersonne;
	

	// Actions 

	@Override
	public int inserer( DtoCategorie dtoCategorie ) {
		try {
			managerSecurite.verifierAutorisationAdmin();
			verifierValiditeDonnees(dtoCategorie);
			dtoCategorie.setId( donnees.getProchainIdCategorie() );
			donnees.getMapCategories().put( dtoCategorie.getId(), dtoCategorie );
			return dtoCategorie.getId();
		} catch ( Exception e ) {
//			throw UtilServices.exceptionValidationOrAnomaly(e);
			throw UtilServices.exceptionAnomaly(e);
		}
	}

	@Override
	public void modifier( DtoCategorie dtoCategorie ) {
		try {
			managerSecurite.verifierAutorisationAdmin();
			verifierValiditeDonnees(dtoCategorie);
			donnees.getMapCategories().replace( dtoCategorie.getId(), dtoCategorie );
		} catch ( Exception e ) {
//			throw UtilServices.exceptionValidationOrAnomaly(e);
			throw UtilServices.exceptionAnomaly(e);
		}
	}

	@Override
	public void supprimer( int idCategorie ) {
		try {
			managerSecurite.verifierAutorisationAdmin();
			for( DtoPersonne personne : servicePersonne.listerTout() ) {
				if ( personne.getCategorie().getId() == idCategorie ) {
	                throw new ExceptionValidation( "La catégorie n'est pas vide" );
				}
			}
			donnees.getMapCategories().remove( idCategorie );
		} catch ( Exception e ) {
//			throw UtilServices.exceptionValidationOrAnomaly(e);
			throw UtilServices.exceptionAnomaly(e);
		}
	}

	@Override
	public DtoCategorie retrouver( int idCategorie ) {
		try {
			managerSecurite.verifierAutorisationUtilisateurOuAdmin();
			return donnees.getMapCategories().get( idCategorie );
		} catch ( Exception e ) {
			throw UtilServices.exceptionAnomaly(e);
		}
	}

	@Override
	public List<DtoCategorie> listerTout() {
		try {
			managerSecurite.verifierAutorisationUtilisateurOuAdmin();;
			return trierParLibelle( new ArrayList<>(donnees.getMapCategories().values()) );
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
    
	
    private List<DtoCategorie> trierParLibelle( List<DtoCategorie> liste ) {
		Collections.sort( liste,
	            (Comparator<DtoCategorie>) ( item1, item2) -> {
	                return ( item1.getLibelle().toUpperCase().compareTo( item2.getLibelle().toUpperCase() ) );
			});
    	return liste;
    }

}
