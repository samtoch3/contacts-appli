package contacts.javafx.model.mock;

import java.util.Comparator;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import contacts.javafx.data.Categorie;
import contacts.javafx.data.mapper.IMapperGui;
import contacts.javafx.model.IModelCategorie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfox.exception.ExceptionValidation;
import jfox.javafx.util.UtilFX;


@Component
public class ModelCategorie implements IModelCategorie  {
	
	
	// Données observables 
	
	private final ObservableList<Categorie> liste = FXCollections.observableArrayList(); 
	
	private final Categorie		courant = new Categorie();
	
	
	// Objet sélectionné

	private Categorie			selection;

	
	// Autres champs
	@Inject
	private Donnees				donnees;
	@Inject
	private IMapperGui			mapper;
	
	
	// Getters & Setters
	
	@Override
	public ObservableList<Categorie> getListe() {
		return liste;
	}
	
	@Override
	public Categorie getCourant() {
		return courant;
	}
	
	@Override
	public Categorie getSelection() {
		return selection;
	}
	
	@Override
	public void setSelection( Categorie selection ) {
		if ( selection == null ) {
			this.selection = new Categorie();
		} else {
			this.selection = selection;
		}
	}


	// Actions
	
	@Override
	public void actualiserListe() {

		liste.setAll( donnees.getMapCategories().values() );

		// Trie la liste
		FXCollections.sort( liste,
	            (Comparator<Categorie>) ( item1, item2) -> {
	                return ( item1.getLibelle().toUpperCase().compareTo( item2.getLibelle().toUpperCase() ) );
			});
 	}

	
	@Override
	public void actualiserCourant() {
		mapper.update( courant, selection );
	}
	
	
	@Override
	public void validerMiseAJour() {

		// Vérifie la validité des données
		
		StringBuilder message = new StringBuilder();

		if( courant.getLibelle() == null || courant.getLibelle().isEmpty() ) {
			message.append( "\nLe libellé ne doit pas être vide." );
		} else  if ( courant.getLibelle().length()> 25 ) {
			message.append( "\nLe libellé est trop long : 25 maxi." );
		}
		
		if ( message.length() > 0 ) {
			throw new ExceptionValidation( message.toString().substring(1) );
		}
		
		
		// Effectue la mise à jour
		
		if ( selection.getId() == 0 ) {
			courant.setId( donnees.getProchainIdCategorie() + 1 );
			donnees.getMapCategories().put( selection.getId(), selection );
		}
		mapper.update( selection, courant );		
	}
	
	
	@Override
	public void supprimer( Categorie item )  {

		donnees.getMapCategories().remove( item.getId() );

		// Détermine le nouvel objet courant
		selection = UtilFX.findNext( liste, item );
	}
	
}
