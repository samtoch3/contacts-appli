package contacts.javafx.model.mock;

import org.springframework.stereotype.Component;

import contacts.javafx.model.IModelInfo;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


@Component
public class ModelInfo implements IModelInfo {
	
	
	// Données observables 
	
	private final StringProperty	propTitre = new SimpleStringProperty();
	private final StringProperty	propMessage = new SimpleStringProperty();
	

	// Getters
	
	@Override
	public StringProperty titreProperty() {
		return propTitre;
	}
	
	@Override
	public StringProperty messageProperty() {
		return propMessage;
	}
}
