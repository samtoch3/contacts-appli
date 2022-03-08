package contacts.javafx.view.personne;

import javax.inject.Inject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import contacts.javafx.data.Categorie;
import contacts.javafx.data.Personne;
import contacts.javafx.data.Telephone;
import contacts.javafx.model.IModelCategorie;
import contacts.javafx.model.IModelPersonne;
import contacts.javafx.view.EnumView;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.converter.IntegerStringConverter;
import jfox.javafx.control.EditingCell;
import jfox.javafx.view.Controller;
import jfox.javafx.view.IManagerGui;


@Component
@Scope( "prototype" )
public class ControllerPersonneForm extends Controller {
	
	
	// Composants de la vue
	
	@FXML
	private TextField			textFieldId;
	@FXML
	private TextField			textFieldNom;
	@FXML	
	private TextField			textFieldPrenom;
    @FXML
    private ComboBox<Categorie>	comboBoxCategorie;
	@FXML
	private TableView<Telephone>	tableViewTelphones;
	@FXML
	private TableColumn<Telephone, Integer> columnId;
	@FXML
	private TableColumn<Telephone, String> columnLibelle;
	@FXML
	private TableColumn<Telephone, String> columnNumero;

	
	// Autres champs
	@Inject
	private IManagerGui			managerGui;
	@Inject
	private IModelPersonne		modelPersonne;
	@Inject
	private IModelCategorie		modelCategorie;
    
	
	// Initialisation du controller
	
	public void initialize() {
		
		// Champs simples
		Personne courant = modelPersonne.getCourant();
		textFieldId.textProperty().bindBidirectional( courant.idProperty(), new IntegerStringConverter() );
		textFieldNom.textProperty().bindBidirectional( courant.nomProperty() );
		textFieldPrenom.textProperty().bindBidirectional( courant.prenomProperty() );

        
		// Configuration de la combo box

		// Data binding
		comboBoxCategorie.setItems( modelCategorie.getListe() );
        comboBoxCategorie.valueProperty().bindBidirectional( courant.categorieProperty() );
 		
		
		// Configuration du TableView

		// Data binding
		tableViewTelphones.setItems(  modelPersonne.getCourant().getTelephones() );
		
		columnId.setCellValueFactory( t -> t.getValue().idProperty() );
		columnLibelle.setCellValueFactory( t -> t.getValue().libelleProperty() );
		columnNumero.setCellValueFactory( t -> t.getValue().numeroProperty() );

		columnLibelle.setCellFactory(  p -> new EditingCell<>() );
		columnNumero.setCellFactory(  p -> new EditingCell<>() );		
	
	}
	
	
	public void refresh() {
		modelCategorie.actualiserListe();
		modelPersonne.actualiserCourant();
	}
	
	
	// Actions
	
	@FXML
	private void doValider() {
		modelPersonne.validerMiseAJour();
		managerGui.showView( EnumView.PersonneListe );
	}
	
	@FXML
	private void doAnnuler() {
		managerGui.showView( EnumView.PersonneListe );
	}
	
	@FXML
	private void doAjouterTelephone() {
		modelPersonne.ajouterTelephone();
	}
	
	
	@FXML
	private void doiSupprimerTelephone() {
		Telephone telephone = tableViewTelphones.getSelectionModel().getSelectedItem();
		modelPersonne.supprimerTelephone(telephone);
	}
    
}
