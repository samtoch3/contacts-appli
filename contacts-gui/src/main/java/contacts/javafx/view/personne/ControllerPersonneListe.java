package contacts.javafx.view.personne;

import javax.inject.Inject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import contacts.javafx.data.Personne;
import contacts.javafx.model.IModelPersonne;
import contacts.javafx.view.EnumView;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import jfox.javafx.util.UtilFX;
import jfox.javafx.view.Controller;
import jfox.javafx.view.IManagerGui;


@Component
@Scope( "prototype" )
public class ControllerPersonneListe extends Controller {
	
	
	// Composants de la vue
	
	@FXML
	private ListView<Personne>	listView;
	@FXML
	private Button				buttonModifier;
	@FXML
	private Button				buttonSupprimer;
	
	
	// Autres champs
	
	@Inject
	private IManagerGui			managerGui;
	@Inject
	private IModelPersonne		modelPersonne;
	
	
	// Initialisation du controller

	@FXML
	private void initialize() {
		
		// Data binding
		listView.setItems( modelPersonne.getListe() );
		
		// Configuraiton des boutons
		listView.getSelectionModel().getSelectedItems().addListener( 
		        (ListChangeListener<Personne>) (c) -> {
		        	configurerBoutons();
		});
    	configurerBoutons();
	}
	
	public void refresh() {
		modelPersonne.actualiserListe();
		UtilFX.selectRow(listView, modelPersonne.getSelection() );
		listView.requestFocus();
	}
	
	
	// Actions
	
	@FXML
	private void doAjouter() {
		modelPersonne.setSelection(null);;
		managerGui.showView( EnumView.PersonneForm );
	}
	
	@FXML
	private void doModifier() {
		modelPersonne.setSelection( listView.getSelectionModel().getSelectedItem() );
		managerGui.showView( EnumView.PersonneForm );
	}
	
	@FXML
	private void doSupprimer() {
		if ( managerGui.showDialogConfirm("Etes-vous sûr de voulir supprimer cette personne ?" ) ) {
			modelPersonne.supprimer( listView.getSelectionModel().getSelectedItem() );
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
