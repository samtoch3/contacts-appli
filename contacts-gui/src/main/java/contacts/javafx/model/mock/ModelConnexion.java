package contacts.javafx.model.mock;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.stereotype.Component;

import contacts.javafx.data.Compte;
import contacts.javafx.model.IModelConnexion;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfox.exception.ExceptionValidation;


@Component
public class ModelConnexion implements IModelConnexion {
	
	
	// Logger
	private static final Logger logger = Logger.getLogger( ModelConnexion.class.getName() );
	
	
	// Données observables 
	private final Compte		courant = new Compte();

	// Compte connecté
	private final ObjectProperty<Compte>	compteActif = new SimpleObjectProperty<>();

	
	// Autres champs
    @Inject
	private Donnees				donnees;
	
	
	// Initialisation
	
	@PostConstruct
	public void init() {
		courant.setPseudo( "geek" );
		courant.setMotDePasse( "geek" );
	}
	

	// Getters
	
	@Override
	public Compte getCourant() {
		return courant;
	}
	
	@Override
	public ObjectProperty<Compte> compteActifProperty() {
		return compteActif;
	}
	
	@Override
	public final Compte getCompteActif() {
		return compteActif.get();
	}
	
	
	// Actions


	@Override
	public void ouvrirSessionUtilisateur() {

		Compte compte = null;
		
		for ( Compte item : donnees.getMapComptes().values() ) {
			if ( item.getPseudo().equals(courant.getPseudo() )
					&& item.getMotDePasse().equals( courant.getMotDePasse()) ) {
				compte = item;
			}
		}
		
		// Message de log
		String logMessage;
		if( compte == null ) {
			logMessage = "Pseudo ou mot de passe invalide : " + courant.pseudoProperty().get() + " / " + courant.motDePasseProperty().get();
		} else {
			logMessage = "\n    Utilisateur connecté : " + compte.getId() +  " " + compte.getPseudo();
			logMessage += "\n    Roles :";
			for( String role : compte.getRoles() ) {
				logMessage += " " + role;
			}
		}
		logger.log( Level.CONFIG, logMessage );
		
		if( compte == null ) {
			throw new ExceptionValidation( "Pseudo ou mot de passe invalide." );
		} else {
			final Compte compteFinal = compte;
			Platform.runLater( () -> compteActif.set( compteFinal ) );
		}
	}
	

	@Override
	public void fermerSessionUtilisateur()  {
		compteActif.set( null );
	}

}
