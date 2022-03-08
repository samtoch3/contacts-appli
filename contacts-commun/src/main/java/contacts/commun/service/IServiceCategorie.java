package contacts.commun.service;

import java.util.List;

import contacts.commun.dto.DtoCategorie;
import contacts.commun.exception.ExceptionValidation;


public interface IServiceCategorie {
	
	int		inserer( DtoCategorie dtoCategorie ) throws ExceptionValidation;

	void	modifier( DtoCategorie dtoCategorie ) throws ExceptionValidation;

	void	supprimer( int idCategorie ) throws ExceptionValidation;

	DtoCategorie	retrouver( int idCategorie );

	List<DtoCategorie>	listerTout();

}
