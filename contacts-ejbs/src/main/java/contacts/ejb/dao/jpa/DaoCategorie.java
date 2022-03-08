package contacts.ejb.dao.jpa;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import contacts.ejb.dao.IDaoCategorie;
import contacts.ejb.data.Categorie;


@Stateless
@Local
public class DaoCategorie implements IDaoCategorie {

	
	// Champs
	
	@PersistenceContext(unitName="contacts")
	private EntityManager em;
	
	// Actions
	
	@Override
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public int inserer(Categorie categorie) {
		em.persist(categorie);
		em.flush();
		return categorie.getId();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void modifier(Categorie categorie) {
		em.merge(categorie);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void supprimer(int idCategorie) {
		em.remove(retrouver(idCategorie));
		
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Categorie retrouver(int idCategorie) {
		return em.find(Categorie.class, idCategorie);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Categorie> listerTout() {
		em.clear();
		var jpql = "SELECT c FROM Categorie c ORDER BY c.libelle";
		var query = em.createQuery(jpql, Categorie.class);
		return query.getResultList();
	}
	
}
