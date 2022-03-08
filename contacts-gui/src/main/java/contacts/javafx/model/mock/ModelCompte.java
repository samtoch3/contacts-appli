package contacts.javafx.model.mock;


import java.util.Comparator;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import contacts.javafx.data.Compte;
import contacts.javafx.data.mapper.IMapperGui;
import contacts.javafx.model.IModelCompte;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfox.exception.ExceptionValidation;
import jfox.javafx.util.UtilFX;


@Component
public class ModelCompte implements IModelCompte {
	
	
	// Données pour les vues 
	
	private final ObservableList<Compte> liste = FXCollections.observableArrayList();
	
	private final Compte		courant = new Compte();
	
	
	// Objet sélectionné

	private Compte				selection;
	
	
	// Autres champs
    @Inject
	private Donnees				donnees;
    @Inject
	private IMapperGui			mapper;

	
	// Getters & Setters
	
	@Override
	public ObservableList<Compte> getListe() {
		return liste;
	}

	@Override
	public Compte getCourant() {
		return courant;
	}
	
	@Override
	public Compte getSelection() {
		return selection;
	}
	
	@Override
	public void setSelection( Compte selection ) {
		if ( selection == null ) {
			this.selection = new Compte();
		} else {
			this.selection = selection;
		}
	}
	
	
	// Actions
	
	@Override
	public void actualiserListe() {

		liste.setAll( donnees.getMapComptes().values() );
		
		// Trie la liste
		FXCollections.sort( liste,
	            (Comparator<Compte>) ( item1, item2) -> {
	                return ( item1.pseudoProperty().get().toUpperCase().compareTo(item2.pseudoProperty().get().toUpperCase()));
			});
 	}

	
	@Override
	public void actualiserCourant() {
		mapper.update( courant, selection );
	}
	
	
	@Override
	public void validerMiseAJour()   {

		// Vérifie la validité des données
		
		StringBuilder message = new StringBuilder();
		
		if( courant.getPseudo() == null || courant.getPseudo().isEmpty() ) {
			message.append( "\nLe pseudo ne doit pas être vide." );
		} else 	if ( courant.getPseudo().length() < 3 ) {
			message.append( "\nLe pseudo est trop court : 3 mini." );
		} else  if ( courant.getPseudo().length()> 25 ) {
			message.append( "\nLe pseudo est trop long : 25 maxi." );
//		} else 	if ( ! daoCompte.verifierUnicitePseudo( courant.getPseudo(), courant.getId() ) ) {
//			message.append( "\nLe pseudo " + courant.getPseudo() + " est déjà utilisé." );
		}
		
		if( courant.getMotDePasse() == null || courant.getMotDePasse().isEmpty() ) {
			message.append( "\nLe mot de passe ne doit pas être vide." );
		} else  if ( courant.getMotDePasse().length()< 3 ) {
			message.append( "\nLe mot de passe est trop court : 3 mini." );
		} else  if ( courant.getMotDePasse().length()> 25 ) {
			message.append( "\nLe mot de passe est trop long : 25 maxi." );
		}
		
		if( courant.getEmail() == null || courant.getEmail().isEmpty() ) {
			message.append( "\nL'adresse e-mail ne doit pas être vide." );
		} else  if ( courant.getEmail().length()> 100 ) {
			message.append( "\nL'adresse e-mail est trop longue : 100 maxi." );
		}
		
		if ( message.length() > 0 ) {
			throw new ExceptionValidation( message.toString().substring(1) );
		}
		
		// Effectue la mise à jour

		if ( selection.getId() == 0 ) {
	    	courant.setId( donnees.getProchainIdCompte() );
			donnees.getMapComptes().put( selection.getId(), selection );
		}
		mapper.update( selection, courant );		
	}
	
	
	@Override
	public void supprimer( Compte item )   {
		donnees.getMapComptes().remove( item.getId() );
		selection = UtilFX.findNext( liste, item );
	}
    
}
