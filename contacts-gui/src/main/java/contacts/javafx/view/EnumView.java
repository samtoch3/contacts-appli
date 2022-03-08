package contacts.javafx.view;

import jfox.javafx.view.IEnumView;
import jfox.javafx.view.View;


public enum EnumView implements IEnumView {

	
	// Valeurs
	
	Info			( "systeme/ViewInfo.fxml" ),
	Connexion		( "systeme/ViewConnexion.fxml" ),
	CompteListe		( "compte/ViewCompteListe.fxml" ),
	CompteForm		( "compte/ViewCompteForm.fxml" ),
	CategorieListe	( "personne/ViewCategorieListe.fxml" ),
	CategorieForm	( "personne/ViewCategorieForm.fxml" ),
	PersonneListe	( "personne/ViewPersonneListe.fxml" ),
	PersonneForm	( "personne/ViewPersonneForm.fxml" ),
	;

	
	// Champs
	
	private final View	view;

	
	// Constructeurs
	
	EnumView( String path, boolean flagReuse ) {
		view = new View(path, flagReuse);
	}
	
	EnumView( String path ) {
		view = new View(path);
	}

	
	// Getters & setters
	
	@Override
	public View getView() {
		return view;
	}
}
