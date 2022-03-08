package contacts.emb.service.mock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import contacts.commun.dto.DtoCategorie;
import contacts.commun.dto.DtoCompte;
import contacts.commun.dto.DtoPersonne;
import contacts.commun.dto.DtoTelephone;
import contacts.commun.dto.Roles;


@Component
public class Donnees {

	
    // Champs 

    private final Map<Integer, DtoCompte>  		mapComptes 		= new HashMap<>();
	private final Map<Integer, DtoCategorie>	mapCategories 	= new HashMap<>();
	private final Map<Integer, DtoPersonne>		mapPersonnes	= new HashMap<>();

	private int 	prochainIdCompte;
	private int 	prochainIdCategorie;
	private int 	prochainIdPersonne;
	private int 	prochainIdTelephone;

	
	// Getters
    
	public Map<Integer, DtoCompte> getMapComptes() {
		return mapComptes;
	}

	public Map<Integer, DtoCategorie> getMapCategories() {
		return mapCategories;
	}

	public Map<Integer, DtoPersonne> getMapPersonnes() {
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
	
	
	// Initialisations

	@PostConstruct
	public void initialiserDonnees() {
		
		
		// Comptes
		
		DtoCompte compte;
		compte = new DtoCompte( 1, "geek", "geek", "geek@3il.fr" );
		compte.getRoles().add( Roles.ADMINISTRATEUR  );
		compte.getRoles().add( Roles.UTILISATEUR  );
		mapComptes.put( compte.getId(), compte );

		compte = new DtoCompte(2, "chef", "chef", "chef@3il.fr");
		compte.getRoles().add( Roles.UTILISATEUR  );
		mapComptes.put( compte.getId(), compte );
		
		compte = new DtoCompte( 3, "job", "job", "job@3il.fr" );
		compte.getRoles().add( Roles.UTILISATEUR  );
		mapComptes.put( compte.getId(), compte );

		compte = new DtoCompte(4, "_" + this.getClass().getPackage().getName(), "xxx", "xxx@3il.fr");
		compte.getRoles().add( Roles.UTILISATEUR  );
		mapComptes.put( compte.getId(), compte );
		
		prochainIdCompte = Collections.max( mapComptes.keySet()) + 1;

		
		// Catégories
	
		DtoCategorie categorie;
		categorie =  new DtoCategorie( 1, "Ecrivains" );
    	mapCategories.put( categorie.getId(), categorie );
    	categorie =  new DtoCategorie( 2, "Peintres" );
    	mapCategories.put( categorie.getId(), categorie );
		
		prochainIdCategorie = Collections.max( mapCategories.keySet()) + 1;

    	
    	// Personnes
    	
    	DtoCategorie categorie1 = mapCategories.get(1);

        DtoPersonne personne;

        personne = new DtoPersonne( 1, "VERLAINE", "Paul", categorie1 );
        personne.getTelephones().add(new DtoTelephone(31, "Portable", "06 33 33 33 33"));
        personne.getTelephones().add(new DtoTelephone(32, "Domicile", "05 55 33 33 33"));
        personne.getTelephones().add(new DtoTelephone(33, "Travail", "05 55 99 33 33"));
        mapPersonnes.put(personne.getId(), personne);

        personne = new DtoPersonne( 2, "HUGO", "Victor", categorie1 );
        personne.getTelephones().add(new DtoTelephone(11, "Portable", "06 11 11 11 11"));
        personne.getTelephones().add(new DtoTelephone(12, "Domicile", "05 55 11 11 11"));
        personne.getTelephones().add(new DtoTelephone(13, "Travail", "05 55 99 11 11"));
        mapPersonnes.put(personne.getId(), personne);

        personne = new DtoPersonne( 3, "TRIOLET", "Elsa", categorie1 );
        personne.getTelephones().add(new DtoTelephone(21, "Portable", "06 22 22 22 22"));
        personne.getTelephones().add(new DtoTelephone(22, "Domicile", "05 55 22 22 22"));
        personne.getTelephones().add(new DtoTelephone(23, "Travail", "05 55 99 22 22"));
        mapPersonnes.put(personne.getId(), personne);
        
		prochainIdPersonne = Collections.max( mapPersonnes.keySet()) + 1;
		prochainIdTelephone = 101;
	
	}
	
	
}
