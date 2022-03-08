package contacts.javafx.data;

import java.util.Objects;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Telephone {
	

	// Données observables
	
	private final Property<Integer>	id		= new SimpleObjectProperty<>();
	private final StringProperty	libelle	= new SimpleStringProperty();
	private final StringProperty	numero	= new SimpleStringProperty();
	
	
	// Constructeurs
	
	public Telephone() {
	}
	
	public Telephone( int id, String libelle, String numero ) {
		setId(id);
		setLibelle(libelle);
		setNumero(numero);
	}
	
	
	// Getters & setters
	
	public final Property<Integer> idProperty() {
		return this.id;
	}

	public final Integer getId() {
		return this.idProperty().getValue();
	}

	public final void setId(final int id) {
		this.idProperty().setValue(id);
	}

	
	public final StringProperty libelleProperty() {
		return this.libelle;
	}

	public final java.lang.String getLibelle() {
		return this.libelleProperty().getValue();
	}

	public final void setLibelle(final java.lang.String libelle) {
		this.libelleProperty().setValue(libelle);
	}
	

	public final StringProperty numeroProperty() {
		return this.numero;
	}

	public final java.lang.String getNumero() {
		return this.numeroProperty().getValue();
	}

	public final void setNumero(final java.lang.String numero) {
		this.numeroProperty().setValue(numero);
	}

	
	// toString()
	
	@Override
	public String toString() {
		return getLibelle();
	}
	
	
	// hashCode() & equals()

	@Override
	public int hashCode() {
		return Objects.hash(id.getValue() );
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Telephone other = (Telephone) obj;
		return Objects.equals(id.getValue(), other.id.getValue() );
	}

}
