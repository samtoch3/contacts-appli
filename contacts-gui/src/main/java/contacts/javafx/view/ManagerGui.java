package contacts.javafx.view;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import contacts.commun.exception.ExceptionAnomaly;
import contacts.commun.exception.ExceptionPermission;
import contacts.commun.exception.ExceptionValidation;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import jfox.javafx.view.ManagerGuiAbstract;
import jfox.javafx.view.View;


@Component
public class ManagerGui extends ManagerGuiAbstract {
	
	
	// Initialisation
	
	@PostConstruct
	public void init() {
		addExceptionAnomaly( ExceptionAnomaly.class );
		addExceptionPermission( ExceptionPermission.class );
		addExceptionValidation( ExceptionValidation.class );
	}

	
	// Actions

	@Override
	public void configureStage()  {
		
		// Choisit la vue à afficher
		showView( EnumView.Connexion);
		
		// Configure le stage
		stage.setWidth(600);
		stage.setHeight(440);
		stage.setMinWidth(400);
		stage.setMinHeight(300);
		stage.getIcons().add(new Image(getClass().getResource("icone.png").toExternalForm()));
		
		// Configuration par défaut pour les boîtes de dialogue
//		typeConfigDialogDefault = ConfigDialog.class;
	}

	
	@Override
	public Scene createScene( View view ) {
		BorderPane paneMenu = new BorderPane( view.getRoot() );
		paneMenu.setTop( (MenuBarAppli) factoryController.call( MenuBarAppli.class ) );
		Scene scene = new Scene( paneMenu );
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		return scene;
	}
	
}