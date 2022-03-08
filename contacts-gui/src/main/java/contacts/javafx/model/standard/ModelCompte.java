package contacts.javafx.model.standard;


import javax.inject.Inject;

import org.springframework.stereotype.Component;

import contacts.commun.dto.DtoCompte;
import contacts.commun.service.IServiceCompte;
import contacts.javafx.data.Compte;
import contacts.javafx.data.mapper.IMapperGui;
import contacts.javafx.model.IModelCompte;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfox.javafx.util.UtilFX;


@Component
public class ModelCompte implements IModelCompte {
	
	
	// Données observables 
	
	private final ObservableList<Compte> liste = FXCollections.observableArrayList(); 
	
	private final Compte	courant = new Compte();
	
	
	// Autres champs
	
	private Compte			selection;

    @Inject
	private IMapperGui		mapper;
    @Inject
	private IServiceCompte	serviceCompte;
	
	
	// Getters & Setters
	
	public ObservableList<Compte> getListe() {
		return liste;
	}

	public Compte getCourant() {
		return courant;
	}
	
	public Compte getSelection() {
		return selection;
	}
	
	public void setSelection( Compte selection ) {
		if ( selection == null ) {
			this.selection = new Compte();
		} else {
			this.selection = selection;
		}
	}
	
	
	// Actions
	
	public void actualiserListe() {
		liste.clear();
		for( DtoCompte dto : serviceCompte.listerTout() ) {
			Compte compte = mapper.map( dto );
			liste.add( compte );
		}
 	}

	
	public void actualiserCourant() {
		if ( selection.getId() != null ) {
			selection = mapper.map( serviceCompte.retrouver( selection.getId() ) );
		}
		mapper.update( courant, selection );
	}
	
	
	public void validerMiseAJour() {
		
		try {
			
			// Effectue la mise à jour

			DtoCompte dto = mapper.map( courant );
			
			if ( selection.getId() == null ) {
				// Insertion
				selection.setId( serviceCompte.inserer( dto ) );
			} else {
				// modficiation
				serviceCompte.modifier( dto );
			}
		} catch ( Exception e) {
			throw UtilFX.runtimeException( e );
		}
	}
	
	
	public void supprimer( Compte item ) {
		
		try {
			serviceCompte.supprimer( item.getId() );
			selection = UtilFX.findNext( liste, item );
		} catch ( Exception e) {
			throw UtilFX.runtimeException( e );
		}
	}

}
