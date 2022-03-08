package contacts.emb.dao.jpa;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;

import contacts.emb.dao.IManagerTransaction;


@Component
public class ManagerTransaction implements IManagerTransaction {
	
	
	// Logger
	private static final Logger logger = Logger.getLogger(ManagerTransaction.class.getName());
	
	@Inject
	private EntityManager em;
	
	// Actions

	@Override
	public void begin() {
		em.getTransaction().begin();
		logger.finer("Transaction BEGIN");
	}

	@Override
	public void commit() {
		em.getTransaction().commit();
		logger.finer("Transaction COMMIT");
	}

	@Override
	public void rollback() {
		em.getTransaction().rollback();
		logger.finer("Transaction ROLLBACK");
	}

	@Override
	public void rollbackSiApplicable() {
		if(em.getTransaction().isActive())
			rollback();
	}

}
