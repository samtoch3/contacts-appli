package contacts.emb.dao.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import contacts.emb.dao.IDaoCategorie;
import contacts.emb.data.Categorie;


@Component
public class DaoCategorie implements IDaoCategorie {

	
	// Champs

	@Inject
	private Donnees		donnees;
	
	
	// Actions
	
	@Override
	public int inserer(Categorie categorie) {
		categorie.setId( donnees.getProchainIdCategorie() );
		donnees.getMapCategories().put( categorie.getId(), categorie );
		return categorie.getId();
	}

	@Override
	public void modifier(Categorie categorie) {
		donnees.getMapCategories().replace( categorie.getId(), categorie );
	}

	@Override
	public void supprimer(int idCategorie) {
		donnees.getMapCategories().remove( idCategorie );
	}

	@Override
	public Categorie retrouver(int idCategorie) {
		return donnees.getMapCategories().get( idCategorie );
	}

	@Override
	public List<Categorie> listerTout() {
		return trierParLibelle( new ArrayList<>(donnees.getMapCategories().values() ) );
	}

	
	// Méthodes auxiliaires
    
    private List<Categorie> trierParLibelle( List<Categorie> liste ) {
		Collections.sort( liste,
	            (Comparator<Categorie>) ( item1, item2) -> {
	                return ( item1.getLibelle().toUpperCase().compareTo( item2.getLibelle().toUpperCase() ) );
			});
    	return liste;
    }
	
}
