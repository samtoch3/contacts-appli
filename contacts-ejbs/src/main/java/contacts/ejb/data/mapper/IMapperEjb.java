package contacts.ejb.data.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import contacts.commun.dto.DtoCategorie;
import contacts.commun.dto.DtoCompte;
import contacts.commun.dto.DtoPersonne;
import contacts.commun.dto.DtoTelephone;
import contacts.ejb.data.Categorie;
import contacts.ejb.data.Compte;
import contacts.ejb.data.Personne;
import contacts.ejb.data.Telephone;

 
@Mapper( componentModel = "cdi" )
public interface IMapperEjb {  
	
	static final IMapperEjb INSTANCE = Mappers.getMapper( IMapperEjb.class );
	
	
	// Compte
	Compte map( DtoCompte source );
	DtoCompte map( Compte source );

	
	// Categorie
	Categorie map( DtoCategorie source );
	DtoCategorie map( Categorie source );

	
	// Personne
	Personne map( DtoPersonne source );
	DtoPersonne map( Personne source );
	@Mapping( target="telephones", ignore=true )
	@Mapping( target="categorie", ignore=true )
	DtoPersonne mapMinimal( Personne source );

	
	// Telephone
	@Mapping( target="personne", ignore=true )
	Telephone map( DtoTelephone source );
	DtoTelephone map( Telephone source );
	

	// Méthodes auxiliaires
	
    @AfterMapping
    public default void addBackReference(@MappingTarget Personne target) {
        for (Telephone telephone : target.getTelephones() ) {
        	telephone.setPersonne( target );
        }
    }	
	
}
