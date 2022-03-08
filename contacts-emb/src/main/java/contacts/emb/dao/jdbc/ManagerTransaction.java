package contacts.emb.dao.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import contacts.emb.dao.IManagerTransaction;


@Component
public class ManagerTransaction implements IManagerTransaction {
	
	
	// Logger
	private static final Logger logger = Logger.getLogger(ManagerTransaction.class.getName());
	
	
	// Champs
	
	@Inject
	private DataSource	dataSource;  
	
	
	// Actions

	@Override
	public void begin() {
		try {
			dataSource.getConnection().setAutoCommit(false);
			logger.finer("Transaction BEGIN");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void commit() {
		try {
			Connection connection = dataSource.getConnection();
			connection.commit();
			connection.setAutoCommit(true);
			logger.finer("Transaction COMMIT");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void rollback() {
		try {
			Connection connecition = dataSource.getConnection();
			connecition.rollback();
			connecition.setAutoCommit(true);
			logger.finer("Transaction ROLLBACK");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void rollbackSiApplicable() {
		try {
			if ( ! dataSource.getConnection().getAutoCommit() ) {
				rollback();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
