package contacts.javafx.model.standard;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import contacts.commun.dto.DtoCategorie;
import contacts.commun.service.IServiceCategorie;
import contacts.javafx.data.Categorie;
import contacts.javafx.data.mapper.IMapperGui;
import contacts.javafx.model.IModelCategorie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfox.javafx.util.UtilFX;


@Component
public class ModelCategorie implements IModelCategorie  {
	
	
	// Données observables 
	
	private final ObservableList<Categorie> liste = FXCollections.observableArrayList(); 
	
	private final Categorie	courant = new Categorie();

	
	// Autres champs
	
	private Categorie			selection;

	@Inject
	private IMapperGui			mapper;
    @Inject
	private IServiceCategorie	serviceCategorie;
	
	
	// Getters & Setters
	
	public ObservableList<Categorie> getListe() {
		return liste;
	}
	
	public Categorie getCourant() {
		return courant;
	}
	
	public Categorie getSelection() {
		return selection;
	}
	
	public void setSelection( Categorie selection ) {
		if ( selection == null ) {
			this.selection = new Categorie();
		} else {
			this.selection = selection;
		}
	}
	
	
	// Actions
	
	public void actualiserListe() {
		liste.clear();
		for( DtoCategorie dto : serviceCategorie.listerTout() ) {
			Categorie categorie = mapper.map( dto );
			liste.add( categorie );
		}
 	}

	
	public void actualiserCourant() {
		if ( selection.getId() != null ) {
			selection = mapper.map( serviceCategorie.retrouver( selection.getId() ) );
		}
		mapper.update( courant, selection );
	}
	
	
	public void validerMiseAJour() {
		
		try {
			
			// Effectue la mise à jour

			DtoCategorie dto = mapper.map( courant );
			
			if ( selection.getId() == null ) {
				// Insertion
				selection.setId( serviceCategorie.inserer( dto ) );
			} else {
				// modficiation
				serviceCategorie.modifier( dto );
			}
		} catch ( Exception e) {
			throw UtilFX.runtimeException( e );
		}
	}
	
	
	public void supprimer( Categorie item ) {
		
		try {
			serviceCategorie.supprimer( item.getId() );
			selection = UtilFX.findNext( liste, item );
		} catch ( Exception e) {
			throw UtilFX.runtimeException( e );
		}
	}
	
}
