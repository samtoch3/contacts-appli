package contacts.emb.service.util;

import contacts.commun.dto.DtoCompte;
import contacts.commun.exception.ExceptionPermission;


public interface IManagerSecurity {

	int		getIdCompteActif();
	
	void 	verifierAutorisationUtilisateurOuAdmin() throws ExceptionPermission;
	
	void 	verifierAutorisationAdmin() throws ExceptionPermission;
	
	void 	verifierAutorisationAdminOuCompteActif( int idCompte ) throws ExceptionPermission;

	void	setCompteActif( DtoCompte compteActif );

}