package contacts.javafx.model;

import contacts.javafx.data.Categorie;
import javafx.collections.ObservableList;


public interface IModelCategorie {

	ObservableList<Categorie> getListe();

	Categorie getCourant();

	Categorie getSelection();

	void setSelection(Categorie selection);

	void actualiserListe();

	void actualiserCourant();

	void validerMiseAJour();

	void supprimer(Categorie item);

}