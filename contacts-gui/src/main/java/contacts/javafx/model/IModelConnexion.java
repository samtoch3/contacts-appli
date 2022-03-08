package contacts.javafx.model;

import contacts.javafx.data.Compte;
import javafx.beans.property.ObjectProperty;


public interface IModelConnexion {

	Compte getCourant();

	ObjectProperty<Compte> compteActifProperty();

	Compte getCompteActif();

	void ouvrirSessionUtilisateur();

	void fermerSessionUtilisateur();

}