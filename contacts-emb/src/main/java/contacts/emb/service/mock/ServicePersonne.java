package contacts.emb.service.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import contacts.commun.dto.DtoPersonne;
import contacts.commun.dto.DtoTelephone;
import contacts.commun.exception.ExceptionValidation;
import contacts.commun.service.IServicePersonne;
import contacts.emb.service.util.IManagerSecurity;
import contacts.emb.service.util.UtilServices;


@Component
public class ServicePersonne implements IServicePersonne {

	
	// Champs 

	@Inject
	private Donnees						donnees;
	@Inject
	private IManagerSecurity			managerSecurite;
	

	// Actions 

	@Override
	public int inserer( DtoPersonne dtoPersonne ) throws ExceptionValidation {
		try {
			managerSecurite.verifierAutorisationUtilisateurOuAdmin();
			verifierValiditeDonnees( dtoPersonne );
			dtoPersonne.setId( donnees.getProchainIdPersonne() );
        	affecterIdTelephones(dtoPersonne);
			donnees.getMapPersonnes().put( dtoPersonne.getId(), dtoPersonne );
			return dtoPersonne.getId();
		} catch ( Exception e ) {
			throw UtilServices.exceptionValidationOrAnomaly(e);
		}
	}

	@Override
	public void modifier( DtoPersonne dtoPersonne ) throws ExceptionValidation {
		try {
			managerSecurite.verifierAutorisationUtilisateurOuAdmin();
			verifierValiditeDonnees( dtoPersonne );
        	affecterIdTelephones(dtoPersonne);
        	donnees.getMapPersonnes().replace( dtoPersonne.getId(), dtoPersonne );
		} catch ( Exception e ) {
			throw UtilServices.exceptionValidationOrAnomaly(e);
		}
	}

	@Override
	public void supprimer( int idPersonne ) throws ExceptionValidation  {
		try {
			managerSecurite.verifierAutorisationUtilisateurOuAdmin();
			donnees.getMapPersonnes().remove( idPersonne );
		} catch ( Exception e ) {
			throw UtilServices.exceptionValidationOrAnomaly(e);
		}
	}

	@Override
	public DtoPersonne retrouver( int idPersonne ) {
		try {
			managerSecurite.verifierAutorisationUtilisateurOuAdmin();
			return donnees.getMapPersonnes().get( idPersonne );
		} catch ( Exception e ) {
			throw UtilServices.exceptionAnomaly(e);
		}
	}

	@Override
	public List<DtoPersonne> listerTout() {
		try {
			managerSecurite.verifierAutorisationUtilisateurOuAdmin();
			return trierParNom( new ArrayList<>(donnees.getMapPersonnes().values()) );
		} catch ( Exception e ) {
			throw UtilServices.exceptionAnomaly(e);
		}
	}
	
	
	// Méthodes auxiliaires

	private void verifierValiditeDonnees( DtoPersonne dtoPersonne ) throws ExceptionValidation {
		
		StringBuilder message = new StringBuilder();
		
		if ( dtoPersonne.getNom() == null || dtoPersonne.getNom().isEmpty() ) {
			message.append( "\nLe nom est absent." );
		} else 	if ( dtoPersonne.getNom().length() > 25 ) {
			message.append( "\nLe nom est trop long." );
		}

		if ( dtoPersonne.getPrenom() == null || dtoPersonne.getPrenom().isEmpty() ) {
			message.append( "\nLe prénom est absent." );
		} else 	if ( dtoPersonne.getPrenom().length() > 25 ) {
			message.append( "\nLe prénom est trop long." );
		}
		
		for( DtoTelephone telephoe : dtoPersonne.getTelephones() ) {
			if ( telephoe.getLibelle() == null || telephoe.getLibelle().isEmpty() ) {
				message.append( "\nLlibellé absent pour le téléphone : " + telephoe.getNumero() );
			} else 	if ( telephoe.getLibelle().length() > 25 ) {
				message.append( "\nLe libellé du téléphone est trop lon : " + telephoe.getLibelle() );
			}
			
			if ( telephoe.getNumero() == null || telephoe.getNumero().isEmpty() ) {
				message.append( "\nNuméro absent pour le téléphone : " + telephoe.getLibelle() );
			} else 	if ( telephoe.getNumero().length() > 25 ) {
				message.append( "\nLe numéro du téléphone est trop lon : " + telephoe.getNumero() );
			}
		}
		
		if ( message.length() > 0 ) {
			throw new ExceptionValidation( message.toString().substring(1) );
		}
	}
    
	private void affecterIdTelephones( DtoPersonne personne ) {
		for( DtoTelephone t : personne.getTelephones() ) {
			if ( t.getId() == 0 ) {
				t.setId( donnees.getProchainIdTelephone() );
			}
		}
	}
    
    private List<DtoPersonne> trierParNom( List<DtoPersonne> liste ) {
		Collections.sort( liste,
	            (Comparator<DtoPersonne>) ( item1, item2) -> {
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
