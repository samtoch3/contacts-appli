package contacts.emb.dao.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import contacts.emb.dao.IDaoCompte;
import contacts.emb.data.Compte;


@Component
public class DaoCompte implements IDaoCompte {

	
	// Champs
	
	@Inject
	private Donnees		donnees;

	
	// Actions
	
	@Override
	public int inserer(Compte compte) {
        compte.setId( donnees.getProchainIdCompte() );
		donnees.getMapComptes().put( compte.getId(), compte );
		return compte.getId();
	}

	@Override
	public void modifier(Compte compte) {
		donnees.getMapComptes().replace( compte.getId(), compte );
	}

	@Override
	public void supprimer(int idCompte) {
		donnees.getMapComptes().remove( idCompte );
	}

	@Override
	public Compte retrouver(int idCompte) {
		return donnees.getMapComptes().get( idCompte );
	}

	@Override
	public List<Compte> listerTout() {
		return trierParPseudo( new ArrayList<>(donnees.getMapComptes().values()) );
	}


	@Override
	public Compte validerAuthentification( String pseudo, String motDePasse )  {
		return testerAuthentification(pseudo, motDePasse);
	}


	@Override
	public boolean verifierUnicitePseudo( String pseudo, int idCompte )  {
		return testerUnicitePseudo(pseudo, idCompte);
	}

	
	// Méthodes auxiliaires
    
    private List<Compte> trierParPseudo( List<Compte> liste ) {
		Collections.sort( liste,
	            (Comparator<Compte>) ( item1, item2) -> {
	                return ( item1.getPseudo().toUpperCase().compareTo( item2.getPseudo().toUpperCase() ) );
			});
    	return liste;
    }


	private Compte testerAuthentification( String pseudo, String motDePasse )  {
		for ( Compte compte : donnees.getMapComptes().values() ) {
			if ( compte.getPseudo().equals(pseudo) ) {
				if ( compte.getMotDePasse().equals(motDePasse) ) {
					return compte;
				}
				break;
			}
		}
		return null;
	}

	
	private boolean testerUnicitePseudo( String pseudo, int idCompte )  {
		for ( Compte compte : donnees.getMapComptes().values() ) {
			if ( compte.getPseudo().equals(pseudo) ) {
				if ( compte.getId() != idCompte  ) {
					return false;
				}
			}
		}
		return true;
	}
	
}
