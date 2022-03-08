package contacts.javafx.model.standard;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.stereotype.Component;

import contacts.commun.dto.DtoCompte;
import contacts.commun.exception.ExceptionValidation;
import contacts.commun.service.IServiceConnexion;
import contacts.javafx.data.Compte;
import contacts.javafx.data.mapper.IMapperGui;
import contacts.javafx.model.IModelConnexion;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfox.javafx.util.UtilFX;


@Component
public class ModelConnexion implements IModelConnexion {
	
	// Logger
	private static final Logger logger = Logger.getLogger( ModelConnexion.class.getName() );
	
	
	// Données observables 
	
	// Vue connexion
	private final Compte		courant = new Compte();

	// Compte connecté
	private final ObjectProperty<Compte>	compteActif = new SimpleObjectProperty<>();

	
	// Autres champs
	@Inject
	private IMapperGui			mapper;
	@Inject
	private IServiceConnexion	serviceConnexion;
	

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
	public Compte getCompteActif() {
		return compteActif.get();
	}
	
	
	// Initialisation
	
	@PostConstruct
	public void init() {
		courant.setPseudo( "geek" );
		courant.setMotDePasse( "geek" );
	}
	
	
	// Actions


	@Override
	public void ouvrirSessionUtilisateur() {
		
		try {

			DtoCompte dto = serviceConnexion.sessionUtilisateurOuvrir(
					courant.pseudoProperty().get(), courant.motDePasseProperty().get() );
			
			// Message de log
			String logMessage;
			if( dto == null ) {
				logMessage = "Pseudo ou mot de passe invalide : " + courant.pseudoProperty().get() + " / " + courant.motDePasseProperty().get();
			} else {
				logMessage = "\n    Utilisateur connecté : " + dto.getId() +  " " + dto.getPseudo();
				logMessage += "\n    Roles :";
				for( String role : dto.getRoles() ) {
					logMessage += " " + role;
				}
			}
			logger.log( Level.CONFIG, logMessage );
			
			if( dto == null ) {
				throw new ExceptionValidation( "Pseudo ou mot de passe invalide." );
			} else {
				 Platform.runLater( () -> compteActif.set( mapper.map( dto ) ) );
			}
		} catch ( Exception e) {
			throw UtilFX.runtimeException( e );
		}
	}
	

	@Override
	public void fermerSessionUtilisateur() {
		serviceConnexion.sessionUtilisateurFermer();
		compteActif.set( null );
	}

}
