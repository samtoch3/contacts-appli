package contacts.emb.service.standard;

import javax.inject.Inject;
import javax.inject.Named;

import contacts.commun.dto.DtoCompte;
import contacts.commun.service.IServiceConnexion;
import contacts.emb.dao.IDaoCompte;
import contacts.emb.data.mapper.IMapperEmb;
import contacts.emb.service.util.IManagerSecurity;
import contacts.emb.service.util.UtilServices;


@Named
public class ServiceConnexion implements IServiceConnexion {
	
	
	// Champs 

	@Inject
	private IManagerSecurity	managerSecurite;
	@Inject
	private IMapperEmb			mapper;
	@Inject
	private IDaoCompte			daoCompte;
	
	
	// Injecteurs
	
	public void setMapper( IMapperEmb mapper ) {
		this.mapper = mapper;
	}

	public void setDaoCompte( IDaoCompte daoCompte ) {
		this.daoCompte = daoCompte;
	}
	
	public void setManagerSecurite( IManagerSecurity managerSecurite ) {
		this.managerSecurite = managerSecurite;
	}
	
	
	// Actions

	@Override
	public DtoCompte sessionUtilisateurOuvrir( String pseudo, String motDePasse ) {
		try {
			DtoCompte compte = mapper.map( daoCompte.validerAuthentification(pseudo, motDePasse) );
			managerSecurite.setCompteActif( compte );
			return compte;
		} catch ( Exception e ) {
			throw UtilServices.exceptionAnomaly(e);
		}
	}


	@Override
	public void sessionUtilisateurFermer() {
		try {
			managerSecurite.setCompteActif( null );
		} catch ( Exception e ) {
			throw UtilServices.exceptionAnomaly(e);
		}
	}

}
