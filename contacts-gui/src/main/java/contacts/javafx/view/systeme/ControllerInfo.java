package contacts.javafx.view.systeme;

import javax.inject.Inject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import contacts.javafx.model.IModelInfo;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import jfox.javafx.view.Controller;


@Component
@Scope( "prototype" )
public class ControllerInfo extends Controller {
	
	
	// Composants de la vue
	
	@FXML
	private Label		labelTitre;
	@FXML
	private Label		labelMessage;

	
	// Autres champs
	
	@Inject
	private IModelInfo	modelInfo;
	
	
	// Initialisation
	
	@FXML
	private void initialize() {
		
		// Data binding
		labelTitre.textProperty().bind( modelInfo.titreProperty() );
		labelMessage.textProperty().bind( modelInfo.messageProperty() );
		
	}
	

}
