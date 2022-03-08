package contacts.emb.dao.jpa;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;

import contacts.emb.dao.IDaoCategorie;
import contacts.emb.data.Categorie;


@Component
public class DaoCategorie implements IDaoCategorie {

	
	// Champs
	
	@Inject
	private EntityManager em;
	
	// Actions
	
	@Override
	public int inserer(Categorie categorie) {
		em.persist(categorie);
		em.flush();
		return categorie.getId();
	}

	@Override
	public void modifier(Categorie categorie) {
		em.merge(categorie);
	}

	@Override
	public void supprimer(int idCategorie) {
		em.remove(retrouver(idCategorie));
		
	}

	@Override
	public Categorie retrouver(int idCategorie) {
		return em.find(Categorie.class, idCategorie);
	}

	@Override
	public List<Categorie> listerTout() {
		em.clear();
		var jpql = "SELECT c FROM Categorie c ORDER BY c.libelle";
		var query = em.createQuery(jpql, Categorie.class);
		return query.getResultList();
	}
	
}
