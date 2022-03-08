package contacts.javafx.view.compte;

import javax.inject.Inject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import contacts.javafx.data.Compte;
import contacts.javafx.model.IModelCompte;
import contacts.javafx.view.EnumView;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import jfox.javafx.util.UtilFX;
import jfox.javafx.view.Controller;
import jfox.javafx.view.IManagerGui;


@Component
@Scope( "prototype" )
public class ControllerCompteListe extends Controller {
	
	
	// Composants de la vue

	@FXML
	private ListView<Compte>	listView;
	@FXML
	private Button				buttonModifier;
	@FXML
	private Button				buttonSupprimer;
	@FXML
	private Button				buttonMemos;

	
	// Autres champs
	
	@Inject
	private IManagerGui			managerGui;
	@Inject
	private IModelCompte		modelCompte;
	
	
	// Initialisation du Controller

	@FXML
	private void initialize() {
		
		// Configuration de l'objet ListView
		
		// Data binding
		listView.setItems( modelCompte.getListe() );

		// Affichage
		listView.setCellFactory( (list) -> {
		    return new ListCell<Compte>() {
		        @Override
		        protected void updateItem(Compte item, boolean empty) {
		            super.updateItem(item, empty);
		            if (item == null) {
		                setText(null);
		            } else {
		                setText(item.pseudoProperty().getValue() );
		            }
		        }
		    };
		});		

		// Comportement si modificaiton de la séleciton
		listView.getSelectionModel().getSelectedItems().addListener( 
				(ListChangeListener<Compte>) (c) -> {
					 configurerBoutons();					
		});
		configurerBoutons();
	}
	
	public void refresh() {
		modelCompte.actualiserListe();
		UtilFX.selectRow(listView, modelCompte.getSelection() );
		listView.requestFocus();
	}

	
	// Actions

	@FXML
	private void doAjouter() {
		modelCompte.setSelection( null );
		managerGui.showView( EnumView.CompteForm );
	}

	@FXML
	private void doModifier() {
		modelCompte.setSelection( listView.getSelectionModel().getSelectedItem() );
		managerGui.showView( EnumView.CompteForm );
	}

	@FXML
	private void doSupprimer() {
		if ( managerGui.showDialogConfirm( "Confirmez-vous la suppresion ?" ) ) {
			modelCompte.supprimer( listView.getSelectionModel().getSelectedItem() );
			refresh();
		}
	}
	
	
	// Gestion des évènements

	// Clic sur la liste
	@FXML
	private void gererClicSurListe( MouseEvent event ) {
		if (event.getButton().equals(MouseButton.PRIMARY)) {
			if (event.getClickCount() == 2) {
				if ( listView.getSelectionModel().getSelectedIndex() == -1 ) {
					managerGui.showDialogError( "Aucun élément n'est sélectionné dans la liste.");
				} else {
					doModifier();
				}
			}
		}
	}


	// Méthodes auxiliaires
	
	private void configurerBoutons() {
    	if( listView.getSelectionModel().getSelectedItems().isEmpty() ) {
			buttonModifier.setDisable(true);
			buttonSupprimer.setDisable(true);
		} else {
			buttonModifier.setDisable(false);
			buttonSupprimer.setDisable(false);
		}
	}

}
