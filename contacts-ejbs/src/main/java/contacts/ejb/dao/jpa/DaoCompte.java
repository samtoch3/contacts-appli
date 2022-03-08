package contacts.ejb.dao.jpa;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import contacts.ejb.dao.IDaoCompte;
import contacts.ejb.data.Compte;


@Stateless
@Local
public class DaoCompte implements IDaoCompte {

	
	// Champs

	@PersistenceContext(unitName="contacts")
	private EntityManager em;
	
	// Actions
	
	@Override
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public int inserer(Compte compte) {
        em.persist(compte);
        em.flush();
        return compte.getId();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void modifier(Compte compte) {
		em.merge(compte);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void supprimer(int idCompte) {
		em.remove(retrouver(idCompte));
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Compte retrouver(int idCompte) {
		return em.find(Compte.class, idCompte);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Compte> listerTout() {
		var jpql = "SELECT c FROM Compte c ORDER BY c.pseudo";
		var query = em.createQuery(jpql, Compte.class);
		return query.getResultList();
	}


	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
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
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean verifierUnicitePseudo( String pseudo, int idCompte )  {
		var jpql = "SELECT COUNT(c) FROM Compte c WHERE c.pseudo=:p AND c.id<>:idc";
		var query = em.createQuery(jpql, Long.class);
		query.setParameter("p", pseudo);
		query.setParameter("idc", idCompte);
		
		return query.getSingleResult().intValue() == 0;
	}
	
}
