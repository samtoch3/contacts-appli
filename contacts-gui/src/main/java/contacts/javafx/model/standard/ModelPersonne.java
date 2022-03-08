package contacts.javafx.model.standard;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import contacts.commun.dto.DtoPersonne;
import contacts.commun.service.IServicePersonne;
import contacts.javafx.data.Personne;
import contacts.javafx.data.Telephone;
import contacts.javafx.data.mapper.IMapperGui;
import contacts.javafx.model.IModelPersonne;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfox.javafx.util.UtilFX;


@Component
public class ModelPersonne implements IModelPersonne {
	
	
	// Données observables 
	
	private final ObservableList<Personne> liste = FXCollections.observableArrayList();
	
	private final Personne		courant = new Personne();
	
	
	// Autres champs

	private Personne			selection;

	@Inject
	private IMapperGui			mapper;
    @Inject
	private IServicePersonne	servicePersonne;
	
	
	// Getters & Setters
	
	public ObservableList<Personne> getListe() {
		return liste;
	}
	
	public Personne getCourant() {
		return courant;
	}
	
	public Personne getSelection() {
		return selection;
	}
	
	public void setSelection( Personne selection ) {
		if ( selection == null ) {
			this.selection = new Personne();
		} else {
			this.selection = selection;
		}
	}

	
	// Actions
	
	public void actualiserListe() {
		liste.clear();
		for( DtoPersonne dto : servicePersonne.listerTout() ) {
			Personne personne = mapper.map( dto );
			liste.add( personne );
		}
	}

	
	public void actualiserCourant() {
		if ( selection.getId() != null ) {
			selection = mapper.map( servicePersonne.retrouver( selection.getId() ) );
		}
		mapper.update( courant, selection );
	}

	
	public void validerMiseAJour() {
		
		try {
			
			// Effectue la mise à jour

			DtoPersonne dto = mapper.map( courant );
			
			if ( selection.getId() == null ) {
				// Insertion
				selection.setId( servicePersonne.inserer( dto ) );
			} else {
				// modficiation
				servicePersonne.modifier( dto );
			}
		} catch ( Exception e) {
			throw UtilFX.runtimeException( e );
		}
	}
	

	public void supprimer( Personne item ) {
		try {
			servicePersonne.supprimer( item.getId() );
			selection = UtilFX.findNext( liste, item );
		} catch ( Exception e) {
			throw UtilFX.runtimeException( e );
		}
	}
	

	public void ajouterTelephone() {
		courant.getTelephones().add( new Telephone() );
	}
	

	public void supprimerTelephone( Telephone telephone )  {
		courant.getTelephones().remove( telephone );
	}
	
}
