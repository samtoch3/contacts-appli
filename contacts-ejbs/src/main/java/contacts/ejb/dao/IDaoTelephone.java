package contacts.ejb.dao;

import java.util.List;

import contacts.ejb.data.Personne;
import contacts.ejb.data.Telephone;


public interface IDaoTelephone {

	void insererPourPersonne(Personne personne);

	void modifierPourPersonne(Personne personne);

	void supprimerPourPersonne(int idPersonne);

	List<Telephone> listerPourPersonne( Personne personne );

}
