package contacts.javafx.model.mock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import contacts.commun.dto.Roles;
import contacts.javafx.data.Categorie;
import contacts.javafx.data.Compte;
import contacts.javafx.data.Personne;
import contacts.javafx.data.Telephone;


@Component
public class Donnees {

	
    // Champs 

    private final Map<Integer, Compte>  	mapComptes 		= new HashMap<>();
	private final Map<Integer, Categorie>	mapCategories 	= new HashMap<>();
	private final Map<Integer, Personne>	mapPersonnes	= new HashMap<>();

	private int 	prochainIdCompte;
	private int 	prochainIdCategorie;
	private int 	prochainIdPersonne;
	private int 	prochainIdTelephone;

	
	// Getters
    
	public Map<Integer, Compte> getMapComptes() {
		return mapComptes;
	}

	public Map<Integer, Categorie> getMapCategories() {
		return mapCategories;
	}

	public Map<Integer, Personne> getMapPersonnes() {
		return mapPersonnes;
	}
	
	public int getProchainIdCompte() {
		return prochainIdCompte++;
	}
	
	public int getProchainIdCategorie() {
		return prochainIdCategorie++;
	}
	
	public int getProchainIdPersonne() {
		return prochainIdPersonne++;
	}
	
	public int getProchainIdTelephone() {
		return prochainIdTelephone++;
	}
	
	
	// Constructeur
	
	public Donnees() {
		initialiserDonnees();
	}
	
	
	// Méthodes auxiliaires

	private void initialiserDonnees() {
		
		
		// Comptes
		
		Compte compte;
		compte = new Compte( 1, "geek", "geek", "geek@3il.fr" );
		compte.getRoles().add( Roles.ADMINISTRATEUR  );
		compte.getRoles().add( Roles.UTILISATEUR  );
		mapComptes.put( compte.getId(), compte );
		
		compte = new Compte(2, "chef", "chef", "chef@3il.fr");
		compte.getRoles().add( Roles.UTILISATEUR  );
		mapComptes.put( compte.getId(), compte );
		
		compte = new Compte( 3, "job", "job", "job@3il.fr" );
		compte.getRoles().add( Roles.UTILISATEUR  );
		mapComptes.put( compte.getId(), compte );
		
		compte = new Compte(4, "_" + this.getClass().getPackage().getName(), "xxx", "xxx@3il.fr");
		compte.getRoles().add( Roles.UTILISATEUR  );
		mapComptes.put( compte.getId(), compte );
		
		prochainIdCompte = Collections.max( mapComptes.keySet()) + 1;
		
		
		// Catégories
		
		Categorie categorie;
		categorie =  new Categorie( 1, "Ecrivains" );
		mapCategories.put( categorie.getId(), categorie );
		categorie =  new Categorie( 2, "Peintres" );
		mapCategories.put( categorie.getId(), categorie );
		
		prochainIdCategorie = Collections.max( mapCategories.keySet()) + 1;
		
		
		// Personnes
		
		Categorie categorie1 = mapCategories.get(1);
		
		Personne personne;
		
		personne = new Personne( 1, "VALERY", "Paul", categorie1 );
		personne.getTelephones().add(new Telephone(31, "Portable", "06 33 33 33 33"));
		personne.getTelephones().add(new Telephone(32, "Domicile", "05 55 33 33 33"));
		personne.getTelephones().add(new Telephone(33, "Travail", "05 55 99 33 33"));
		mapPersonnes.put(personne.getId(), personne);
		
		personne = new Personne( 2, "FRANCE", "Anatole", categorie1 );
		personne.getTelephones().add(new Telephone(11, "Portable", "06 11 11 11 11"));
		personne.getTelephones().add(new Telephone(12, "Domicile", "05 55 11 11 11"));
		personne.getTelephones().add(new Telephone(13, "Travail", "05 55 99 11 11"));
		mapPersonnes.put(personne.getId(), personne);
		
		personne = new Personne( 3, "DAUDET", "Alphonse", categorie1 );
		personne.getTelephones().add(new Telephone(21, "Portable", "06 22 22 22 22"));
		personne.getTelephones().add(new Telephone(22, "Domicile", "05 55 22 22 22"));
		personne.getTelephones().add(new Telephone(23, "Travail", "05 55 99 22 22"));
		mapPersonnes.put(personne.getId(), personne);
		
		prochainIdPersonne = Collections.max( mapPersonnes.keySet()) + 1;
		prochainIdTelephone = 101;
	
	}
	
	
}
