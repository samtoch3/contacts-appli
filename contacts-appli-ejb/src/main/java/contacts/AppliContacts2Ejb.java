package contacts;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import jfox.javafx.util.UtilFX;
import jfox.javafx.view.ManagerGuiAbstract;


public class AppliContacts2Ejb extends Application {

	
	// Titre de la fenêtre
	private final String TITRE = "Contacts EJB";
	
	
	// Logger
	
	private static final Logger logger = getLogger();

	
	// Champs
	
	private AnnotationConfigApplicationContext	context;
	
	
	// Actions
	
	@Override
	public void init() throws Exception {
		context = new AnnotationConfigApplicationContext();
		context.register( Config4Ejb.class );
		context.refresh();
	}
	
	/**
	 *
	 */
	@Override
	public final void start(Stage stage) {
		
		try {

			// ManagerGui
	    	ManagerGuiAbstract managerGui = context.getBean( ManagerGuiAbstract.class );
	    	managerGui.setFactoryController( context::getBean );
			managerGui.setStage( stage );
			managerGui.configureStage();

	    	
	    	// Trace
	    	
			/*
			 * StringBuilder sbMessage = new StringBuilder(); try { IDaoCompte dao =
			 * context.getBean( IDaoCompte.class ); sbMessage.append(
			 * "\n    Couche DAO     : " ).append( dao.getClass().getPackageName() ); }
			 * catch (NoSuchBeanDefinitionException e) { } try { IServiceCompte service =
			 * context.getBean( IServiceCompte.class ); sbMessage.append(
			 * "\n    Couche Service : " ).append( service.getClass().getPackageName() ); }
			 * catch (NoSuchBeanDefinitionException e) { } try { IModelCompte model =
			 * context.getBean( IModelCompte.class ); sbMessage.append(
			 * "\n    Couche Model   : " ).append( model.getClass().getPackageName() ); }
			 * catch (NoSuchBeanDefinitionException e) { } logger.log(Level.CONFIG,
			 * sbMessage.toString() );
			 */

			
			// Affiche le stage
			stage.setTitle( TITRE );
			stage.show();
			
		} catch(Exception e) {
			UtilFX.unwrapException(e).printStackTrace();
			logger.log( Level.SEVERE, "Echec du démarrage", e );
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setHeaderText( "Impossible de démarrer l'application." );
	        alert.showAndWait();
	        Platform.exit();
		}

	}
	
	@Override
	public final void stop() throws Exception {

		if (context != null ) {
			context.close();
		}
		
		// Message de fermeture
		logger.config( "\n    Fermeture de l'application" );
	}
	

	
	// Méthodes auxiliaires
	
	private static Logger getLogger() {

		try {
			InputStream in = 
				Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/logging.properties");
			LogManager.getLogManager().readConfiguration( in );
			in.close();
		} catch ( Exception e ) {
			e.printStackTrace();
		}		

		return Logger.getLogger( AppliContacts2Ejb.class.getName() );
	}

	
	// Classe interne Main
	
	public static class Main {
		public static void main(String[] args) {
			Application.launch( AppliContacts2Ejb.class, args);
		}
	}

}
