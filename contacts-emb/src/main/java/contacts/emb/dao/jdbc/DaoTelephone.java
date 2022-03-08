package contacts.emb.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import contacts.emb.dao.IDaoTelephone;
import contacts.emb.data.Personne;
import contacts.emb.data.Telephone;
import jfox.jdbc.UtilJdbc;


@Component
public class DaoTelephone implements IDaoTelephone {

	
	// Champs

	@Inject
	private DataSource		dataSource;

	
	// Actions

	@Override
	public void insererPourPersonne(Personne personne) {

		Connection			cn		= null;
		PreparedStatement	stmt	= null;
		ResultSet 			rs 		= null;
		String				sql;

		try {
			cn = dataSource.getConnection();
			sql = "INSERT INTO telephone ( IdPersonne, Libelle, Numero ) VALUES (?,?,?)";
			stmt = cn.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
			for( Telephone telephone : personne.getTelephones() ) {
				stmt.setInt(	1, personne.getId() );
				stmt.setString(	2, telephone.getLibelle() );
				stmt.setString(	3, telephone.getNumero() );
				stmt.executeUpdate();

				rs = stmt.getGeneratedKeys();
				rs.next();
				telephone.setId( rs.getInt(1) );
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			UtilJdbc.close( rs, stmt, cn );
		}
	}

	@Override
	public void modifierPourPersonne(Personne personne) {

		Connection			cn			= null;
		PreparedStatement	stmtDelete	= null;
		PreparedStatement	stmtInsert	= null;
		PreparedStatement	stmtUpdate	= null;
		ResultSet 			rs 			= null;
		String 				sql;

		try {
			cn = dataSource.getConnection();

			sql = "DELETE FROM telephone WHERE IdTelephone = ?";
			stmtDelete = cn.prepareStatement( sql );
			for ( Telephone telephone : listerPourPersonne(personne) ) {
				if ( ! personne.getTelephones().contains( telephone ) ) {
					stmtDelete.setInt( 1, telephone.getId() );
					stmtDelete.executeUpdate();
				}
			}

			sql = "INSERT INTO telephone (IdPersonne, Libelle, Numero) VALUES (?,?, ?)";
			stmtInsert = cn.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
			sql = "UPDATE telephone SET IdPersonne = ?, Libelle = ?, Numero = ? WHERE IdTelephone = ?";
			stmtUpdate = cn.prepareStatement( sql );
			for( Telephone telephone : personne.getTelephones() ) {
				if ( telephone.getId() == 0 ) {
					stmtInsert.setInt( 1, personne.getId());
					stmtInsert.setString( 2, telephone.getLibelle() );
					stmtInsert.setString( 3, telephone.getNumero() );
					stmtInsert.executeUpdate();
					// Récupère l'identifiant généré par le SGBD
					rs = stmtInsert.getGeneratedKeys();
					rs.next();
					telephone.setId( rs.getInt(1) );
				} else {
					stmtUpdate.setInt( 1, personne.getId());
					stmtUpdate.setString( 2, telephone.getLibelle() );
					stmtUpdate.setString( 3, telephone.getNumero() );
					stmtUpdate.setInt( 4, telephone.getId());
					stmtUpdate.executeUpdate();
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			UtilJdbc.close( rs, stmtDelete, stmtInsert, stmtUpdate, cn );
		}
	}

	@Override
	public void supprimerPourPersonne(int idPersonne) {

		Connection			cn		= null;
		PreparedStatement	stmt	= null;
		String 				sql;

		try {
			cn = dataSource.getConnection();

			// Supprime les telephones
			sql = "DELETE FROM telephone  WHERE IdPersonne = ? ";
			stmt = cn.prepareStatement(sql);
			stmt.setInt( 1, idPersonne );
			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			UtilJdbc.close(  stmt, cn );
		}
	}

	@Override
	public List<Telephone> listerPourPersonne(Personne personne) {

		Connection			cn		= null;
		PreparedStatement	stmt	= null;
		ResultSet 			rs 		= null;
		String				sql;

		try {
			cn = dataSource.getConnection();

			sql = "SELECT * FROM telephone WHERE IdPersonne = ? ORDER BY Libelle";
			stmt = cn.prepareStatement(sql);
			stmt.setInt( 1, personne.getId() );
			rs = stmt.executeQuery();

			List<Telephone> telephones = new ArrayList<>();
			while (rs.next()) {
				telephones.add( construireTelephone( rs, personne ) );
			}
			return telephones;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			UtilJdbc.close( rs, stmt, cn );
		}
	}
	
	
	// Méthodes auxiliaires
	
	private Telephone construireTelephone( ResultSet rs, Personne personne ) throws SQLException {
		Telephone telephone = new Telephone();
		telephone.setId(rs.getInt( "IdTelephone" ));
		telephone.setLibelle(rs.getString( "Libelle" ));
		telephone.setNumero(rs.getString( "Numero" ));
		telephone.setPersonne(personne);
		return telephone;
	}

}
