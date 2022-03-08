package contacts.javafx.data.mapper;

import javax.inject.Named;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import contacts.commun.dto.DtoCategorie;
import contacts.commun.dto.DtoCompte;
import contacts.commun.dto.DtoPersonne;
import contacts.commun.dto.DtoTelephone;
import contacts.javafx.data.Categorie;
import contacts.javafx.data.Compte;
import contacts.javafx.data.Personne;
import contacts.javafx.data.Telephone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
   

@Mapper( uses=IMapperGui.FactoryObsservableList.class, componentModel = "jsr330"  )
public interface IMapperGui { 
	
	IMapperGui INSTANCE = Mappers.getMapper( IMapperGui.class );
	
	
	// Compte
	
	Compte map( DtoCompte source );
	
	DtoCompte map( Compte source );
	
	Compte update( @MappingTarget Compte target,  Compte source );
	
	
	// Categorie
	
	Categorie map( DtoCategorie source );
	
	DtoCategorie map( Categorie source );
	
	Categorie update( @MappingTarget Categorie target, Categorie source );
	
	
	// Personne
	
    Personne map( DtoPersonne source );
	
	@Mapping( source="categorie", target="categorie" )
	@Mapping( source="source.id", target="id" )
	Personne map( Categorie categorie, DtoPersonne source );
	
	DtoPersonne map( Personne source );
	
	@Mapping( target="categorie", expression="java( source.getCategorie() )" )
	Personne update( @MappingTarget Personne target, Personne source );
	
	
	// Telephone
	
    Telephone map( DtoTelephone source );
	
    DtoTelephone map( Telephone source );

    // Méthodes nécessaire pour update( FXPersonne )
    Telephone duplicate( Telephone source );
    ObservableList<Telephone> duplicate( ObservableList<Telephone> source );
    
	
	
    // Classe auxiliaire

    @Named
    public static class FactoryObsservableList {

    	ObservableList<Telephone> createObsListFXTelephone() {
    		return FXCollections.observableArrayList();
    	}

    	ObservableList<String> createObsListString() {
    		return FXCollections.observableArrayList();
    	}

    }
    
}
