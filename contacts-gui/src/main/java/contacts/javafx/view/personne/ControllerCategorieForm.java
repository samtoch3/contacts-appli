package contacts.javafx.view.personne;

import javax.inject.Inject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import contacts.javafx.data.Categorie;
import contacts.javafx.model.IModelCategorie;
import contacts.javafx.view.EnumView;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.util.converter.IntegerStringConverter;
import jfox.javafx.view.Controller;
import jfox.javafx.view.IManagerGui;


@Component
@Scope( "prototype" )
public class ControllerCategorieForm extends Controller {

	
	// Composants de la vue
	
	@FXML
	private TextField			textFieldId;
	@FXML
	private TextField			textFieldLibelle;


	// Autres champs
	
	@Inject
	private IManagerGui			managerGui;
	@Inject
	private IModelCategorie		modelCategorie;


	// Initialisation du Controller

	@FXML
	private void initialize() {

		// Data binding
		
		Categorie courant = modelCategorie.getCourant();
		textFieldId.textProperty().bindBidirectional( courant.idProperty(), new IntegerStringConverter()  );
		textFieldLibelle.textProperty().bindBidirectional( courant.libelleProperty()  );
	}
	
	
	public void refresh() {
		modelCategorie.actualiserCourant();
	}
	
	
	// Actions
	
	@FXML
	private void doAnnuler() {
		managerGui.showView( EnumView.CategorieListe );
	}
	
	@FXML
	private void doValider() {
		modelCategorie.validerMiseAJour();
		managerGui.showView( EnumView.CategorieListe );
	}

}
