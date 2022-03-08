package contacts.emb.dao.jpa;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;

import contacts.emb.dao.IDaoPersonne;
import contacts.emb.data.Personne;


@Component
public class DaoPersonne implements IDaoPersonne {
	
	// Champs
	
	@Inject
	private EntityManager em;

	
	// Actions
	
	@Override
	public int inserer(Personne personne) {
		em.persist(personne);
		em.flush();
		return personne.getId();
	}

	@Override
	public void modifier(Personne personne) {
		em.merge(personne);
	}

	@Override
	public void supprimer(int idPersonne) {
		em.remove(retrouver(idPersonne));
	}

	@Override
	public Personne retrouver(int idPersonne) {
		return em.find(Personne.class, idPersonne);
	}

	@Override
	public List<Personne> listerTout() {
		var jpql = " SELECT p FROM  Personne p ORDER BY p.nom, p.prenom";
		var query = em.createQuery(jpql, Personne.class);
		return query.getResultList();
	}


	@Override
	public int compterPourCategorie(int idCategorie) {
		var jpql = "SELECT COUNT(p) FROM Personne p WHERE p.categorie.id = :idcateg";
		var query = em.createQuery(jpql, Long.class);
		query.setParameter("idcateg", idCategorie);
		return query.getSingleResult().intValue();	
	}

}
