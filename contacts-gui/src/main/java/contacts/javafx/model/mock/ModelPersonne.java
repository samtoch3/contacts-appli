package contacts.javafx.model.mock;

import java.util.Comparator;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import contacts.javafx.data.Personne;
import contacts.javafx.data.Telephone;
import contacts.javafx.data.mapper.IMapperGui;
import contacts.javafx.model.IModelPersonne;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfox.exception.ExceptionValidation;


@Component
public class ModelPersonne implements IModelPersonne {
	
	
	// Données observables 
	
	private final ObservableList<Personne> liste = FXCollections.observableArrayList(); 
	
	private final Personne		courant = new Personne();
	
	
	// Objet sélectionné
	
	private Personne     		selection;
	
	
	// Autres champs
	
    @Inject
	private Donnees				donnees;
    @Inject
	private IMapperGui				mapper;

	
	// Getters & Setters
	
	@Override
	public ObservableList<Personne> getListe() {
		return liste;
	}
	
	@Override
	public Personne getCourant() {
		return courant;
	}
	
	@Override
	public Personne getSelection() {
		return selection;
	}
	
	@Override
	public void setSelection( Personne selection ) {
		if ( selection == null ) {
			this.selection = new Personne();
		} else {
			this.selection = selection;
		}
	}

	
	// Actions
	
	@Override
	public void actualiserListe() {

		liste.setAll( donnees.getMapPersonnes().values() );
		
		// Trie la liste
		FXCollections.sort( liste,
	            (Comparator<Personne>) ( item1, item2) -> {
	                int resultat = item1.getNom().toUpperCase().compareTo(item2.getNom().toUpperCase());
	                return (resultat != 0 ? resultat : item1.getPrenom().toUpperCase().compareTo(item2.getPrenom().toUpperCase()));
			});
	}

	
	@Override
	public void actualiserCourant() {
		mapper.update( courant, selection );
	}

	
	@Override
	public void validerMiseAJour() {
		
		String nom = courant.nomProperty().get();
		String prenom = courant.prenomProperty().get();
		
		StringBuilder message = new StringBuilder();
		if( nom == null || nom.isEmpty() ) {
			message.append( "\nLe nom ne doit pas être vide." );
		} else  if ( nom.length()> 25 ) {
			message.append( "\nLe nom est trop long." );
		}
		if( prenom == null || prenom.isEmpty() ) {
			message.append( "\nLe prénom ne doit pas être vide." );
		} else if ( prenom.length()> 25 ) {
			message.append( "\nLe prénom est trop long." );
		}
		
		if ( message.length() > 0 ) {
			throw new ExceptionValidation( message.toString().substring(1) );
		}
		
		// Affecte les identifiants de téléphones
		for( Telephone t : courant.getTelephones() ) {
			if ( t.getId() == 0 ) {
				t.setId( donnees.getProchainIdTelephone() );
			}
		}
		
		// Test si c'est un ajout ou une modificaiton
		if ( selection.getId() == 0 ) {
			courant.setId( donnees.getProchainIdPersonne() );
			donnees.getMapPersonnes().put( selection.getId(), selection );
		}
		mapper.update( selection, courant );		
		
	}
	
	@Override
	public void supprimer( Personne personne )  {

		donnees.getMapPersonnes().remove( personne.getId() );

		int index = liste.indexOf(personne) + 1;
		if ( index < liste.size() ) {
			selection = liste.get(index);
		}
	}
	
	@Override
	public void ajouterTelephone() {
		courant.getTelephones().add( new Telephone() );
	}
	
	@Override
	public void supprimerTelephone( Telephone telephone )  {
		for ( int i=0; i < courant.getTelephones().size(); ++i ) {
			if ( courant.getTelephones().get(i) == telephone ) {
				courant.getTelephones().remove( i );
				break;
			}
		}
	}
}
