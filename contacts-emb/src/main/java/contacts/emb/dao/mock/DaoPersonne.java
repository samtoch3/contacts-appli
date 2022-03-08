package contacts.emb.dao.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import contacts.emb.dao.IDaoPersonne;
import contacts.emb.data.Personne;
import contacts.emb.data.Telephone;


@Component
public class DaoPersonne implements IDaoPersonne {
	
	// Champs
	
	@Inject
	private Donnees		donnees;

	
	// Actions
	
	@Override
	public int inserer(Personne personne) {
		personne.setId( donnees.getProchainIdPersonne() );
		affecterIdTelephones(personne);
		donnees.getMapPersonnes().put( personne.getId(), personne );
		return personne.getId();
	}

	@Override
	public void modifier(Personne personne) {
		affecterIdTelephones(personne);
		donnees.getMapPersonnes().replace( personne.getId(), personne );
	}

	@Override
	public void supprimer(int idPersonne) {
		donnees.getMapPersonnes().remove( idPersonne );
	}

	@Override
	public Personne retrouver(int idPersonne) {
		return donnees.getMapPersonnes().get( idPersonne );
	}

	@Override
	public List<Personne> listerTout() {
		return  trierParNom( new ArrayList<>(donnees.getMapPersonnes().values() ) );
	}


	@Override
	public int compterPourCategorie(int idCategorie) {
		List<Personne> personnes = listerTout();
		int compteur = 0;
		for ( Personne personne : personnes ) {
			if( personne.getCategorie().getId() == idCategorie ) {
				compteur++;
			}
		}
		return compteur;
	}
	
	
	// Méthodes auxiliaires
	
	private void affecterIdTelephones( Personne personne ) {
		for( Telephone t : personne.getTelephones() ) {
			if ( t.getId() == 0 ) {
				t.setId( donnees.getProchainIdTelephone() );
			}
		}
	}
    
    private List<Personne> trierParNom( List<Personne> liste ) {
		Collections.sort( liste,
	            (Comparator<Personne>) ( item1, item2) -> {
	            	int resultat = item1.getNom().toUpperCase().compareTo( item2.getNom().toUpperCase() );
	            	if ( resultat  != 0 ) {
	            		return resultat;
	            	} else {
		                return ( item1.getPrenom().toUpperCase().compareTo( item2.getPrenom().toUpperCase() ) );
	            	}
		});
    	return liste;
    }

}
