package contacts.emb.dao.jpa;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.springframework.stereotype.Component;

import contacts.emb.dao.IDaoCompte;
import contacts.emb.data.Compte;


@Component
public class DaoCompte implements IDaoCompte {

	
	// Champs

	@Inject
	private EntityManager em;
	
	// Actions
	
	@Override
	public int inserer(Compte compte) {
        em.persist(compte);
        em.flush();
        return compte.getId();
	}

	@Override
	public void modifier(Compte compte) {
		em.merge(compte);
	}

	@Override
	public void supprimer(int idCompte) {
		em.remove(retrouver(idCompte));
	}

	@Override
	public Compte retrouver(int idCompte) {
		return em.find(Compte.class, idCompte);
	}

	@Override
	public List<Compte> listerTout() {
		var jpql = "SELECT c FROM Compte c ORDER BY c.pseudo";
		var query = em.createQuery(jpql, Compte.class);
		return query.getResultList();
	}


	@Override
	public Compte validerAuthentification( String pseudo, String motDePasse )  {
		try {
			var jpql = "SELECT c FROM Compte c WHERE c.pseudo=:p AND c.motDePasse=:m";
			var query = em.createQuery(jpql, Compte.class);
			query.setParameter("p", pseudo);
			query.setParameter("m", motDePasse);
			return query.getSingleResult();
		}catch(NoResultException e) {
			return null;
		}
	}


	@Override
	public boolean verifierUnicitePseudo( String pseudo, int idCompte )  {
		var jpql = "SELECT COUNT(c) FROM Compte c WHERE c.pseudo=:p AND c.id<>:idc";
		var query = em.createQuery(jpql, Long.class);
		query.setParameter("p", pseudo);
		query.setParameter("idc", idCompte);
		
		return query.getSingleResult().intValue() == 0;
	}
	
}
