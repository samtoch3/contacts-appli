package contacts.javafx.model;

import contacts.javafx.data.Personne;
import contacts.javafx.data.Telephone;
import javafx.collections.ObservableList;


public interface IModelPersonne {

	ObservableList<Personne> getListe();

	Personne getCourant();

	Personne getSelection();

	void setSelection(Personne selection);

	void actualiserListe();

	void actualiserCourant();

	void validerMiseAJour();

	void supprimer(Personne personne);

	void ajouterTelephone();

	void supprimerTelephone(Telephone telephone);

}