package contacts.ejb.dao.jpa;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import contacts.ejb.dao.IDaoPersonne;
import contacts.ejb.data.Personne;


@Stateless
@Local
public class DaoPersonne implements IDaoPersonne {
	
	// Champs
	
	@PersistenceContext(unitName="contacts")
	private EntityManager em;

	
	// Actions
	
	@Override
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public int inserer(Personne personne) {
		em.persist(personne);
		em.flush();
		return personne.getId();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void modifier(Personne personne) {
		em.merge(personne);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void supprimer(int idPersonne) {
		em.remove(retrouver(idPersonne));
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Personne retrouver(int idPersonne) {
		var graph = em.createEntityGraph( Personne.class );
		graph.addAttributeNodes( "categorie" );
		graph.addAttributeNodes( "telephones" );
		var props = new HashMap<String, Object>();
		props.put( "javax.persistence.loadgraph", graph );
		return em.find( Personne.class, idPersonne, props );
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Personne> listerTout() {
		var jpql = " SELECT p FROM  Personne p ORDER BY p.nom, p.prenom";
		var query = em.createQuery(jpql, Personne.class);
		return query.getResultList();
	}


	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int compterPourCategorie(int idCategorie) {
		var jpql = "SELECT COUNT(p) FROM Personne p WHERE p.categorie.id = :idcateg";
		var query = em.createQuery(jpql, Long.class);
		query.setParameter("idcateg", idCategorie);
		return query.getSingleResult().intValue();	
	}

}
