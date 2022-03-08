package contacts.javafx.model;

import contacts.javafx.data.Compte;
import javafx.collections.ObservableList;


public interface IModelCompte {

	ObservableList<Compte> getListe();

	Compte getCourant();

	Compte getSelection();

	void setSelection(Compte selection);

	void actualiserListe();

	void actualiserCourant();

	void validerMiseAJour();

	void supprimer(Compte compte);

}