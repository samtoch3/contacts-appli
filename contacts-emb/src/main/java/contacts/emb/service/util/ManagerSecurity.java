package contacts.emb.service.util;

import java.io.Serializable;

import javax.inject.Named;

import org.springframework.stereotype.Component;

import contacts.commun.dto.DtoCompte;
import contacts.commun.dto.Roles;
import contacts.commun.exception.ExceptionPermission;


@Component
@SuppressWarnings("serial")
public class ManagerSecurity implements IManagerSecurity, Serializable {
	
	
	// Champs
	
	@Named
	protected DtoCompte compteActif;

	
	// Getters & Setters
	
	@Override
	public void setCompteActif( DtoCompte compteActif ) {
		this.compteActif = compteActif;
	}
	

	// Actions 
	
	@Override
	public int getIdCompteActif() {
		return compteActif.getId();
	}

	// Vérifie que le compte connecté a le rôle utilisateur (ou à défaut administrateur)
	@Override
	public void verifierAutorisationUtilisateurOuAdmin() throws ExceptionPermission {
		if ( 
				compteActif == null
				|| (
						! compteActif.isInRole( Roles.UTILISATEUR )
						&& ! compteActif.isInRole( Roles.ADMINISTRATEUR ) 
				)
			) {
			throw new ExceptionPermission();
		}
	}

	// Vérifie que le compte connecté a le rôle administrateur
	@Override
	public void verifierAutorisationAdmin() throws ExceptionPermission {
		if ( 
				compteActif == null
				|| ! compteActif.isInRole( Roles.ADMINISTRATEUR )
			) {
			throw new ExceptionPermission();
		}
	}

	// Vérifie que le compte connecte, soit a le rôle administrateur
	// soit a comme identifiant celui passé en paramètre
	@Override
	public void verifierAutorisationAdminOuCompteActif( int idCompte ) throws ExceptionPermission {
		if ( 
				compteActif == null
				|| ( 
						! compteActif.isInRole( Roles.ADMINISTRATEUR )
						&& compteActif.getId() != idCompte
				)
			) {
			throw new ExceptionPermission();
		}
	}

}
