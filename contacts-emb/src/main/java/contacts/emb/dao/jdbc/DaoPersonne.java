package contacts.emb.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import contacts.emb.dao.IDaoCategorie;
import contacts.emb.dao.IDaoPersonne;
import contacts.emb.dao.IDaoTelephone;
import contacts.emb.data.Categorie;
import contacts.emb.data.Personne;
import jfox.jdbc.UtilJdbc;


@Component
public class DaoPersonne implements IDaoPersonne {

	
	// Champs

	@Inject
	private DataSource		dataSource;
	@Inject
	private IDaoTelephone	daoTelephone;
	@Inject
	private IDaoCategorie	daoCategorie;

	
	// Actions
	
	@Override
	public int inserer(Personne personne)  {

		Connection			cn		= null;
		PreparedStatement	stmt	= null;
		ResultSet 			rs 		= null;
		String				sql;

		try {
			cn = dataSource.getConnection();

			// Insère le personne
			sql = "INSERT INTO personne ( IdCategorie, Nom, Prenom ) VALUES ( ?, ?, ? )";
			stmt = cn.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS  );
			stmt.setInt(	1, personne.getCategorie().getId() );
			stmt.setString(	2, personne.getNom() );
			stmt.setString(	3, personne.getPrenom() );
			stmt.executeUpdate();

			// Récupère l'identifiant généré par le SGBD
			rs = stmt.getGeneratedKeys();
			rs.next();
			personne.setId( rs.getInt(1) );
	
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			UtilJdbc.close( rs, stmt, cn );
		}

		// Insère les telephones
		daoTelephone.insererPourPersonne( personne );
		
		// Retourne l'identifiant
		return personne.getId();
	}

	
	@Override
	public void modifier(Personne personne)  {

		Connection			cn		= null;
		PreparedStatement	stmt	= null;
		String 				sql;

		try {
			cn = dataSource.getConnection();

			// Modifie le personne
			sql = "UPDATE personne SET IdCategorie = ?, Nom = ?, Prenom = ? WHERE IdPersonne =  ?";
			stmt = cn.prepareStatement( sql );
			stmt.setInt(	1, personne.getCategorie().getId() );
			stmt.setString(	2, personne.getNom() );
			stmt.setString(	3, personne.getPrenom() );
			stmt.setInt(	4, personne.getId() );
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			UtilJdbc.close( stmt, cn );
		}

		// Modifie les telephones
		daoTelephone.modifierPourPersonne( personne );
	}

	
	@Override
	public void supprimer(int idPersonne)  {

		Connection			cn		= null;
		PreparedStatement	stmt	= null;
		String 				sql;

		// Supprime les telephones
		daoTelephone.supprimerPourPersonne( idPersonne );

		try {
			cn = dataSource.getConnection();

			// Supprime le personne
			sql = "DELETE FROM personne WHERE IdPersonne = ? ";
			stmt = cn.prepareStatement(sql);
			stmt.setInt( 1, idPersonne );
			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			UtilJdbc.close( stmt, cn );
		}
	}

	
	@Override
	public Personne retrouver(int idPersonne)  {

		Connection			cn		= null;
		PreparedStatement	stmt	= null;
		ResultSet 			rs 		= null;
		String				sql;

		try {
			cn = dataSource.getConnection();

			sql = "SELECT * FROM personne WHERE IdPersonne = ?";
            stmt = cn.prepareStatement(sql);
            stmt.setInt(1, idPersonne);
            rs = stmt.executeQuery();

            if ( rs.next() ) {
                return construirePersonne(rs, null );
            } else {
            	return null;
            }
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			UtilJdbc.close( rs, stmt, cn );
		}
	}

	
	@Override
	public List<Personne> listerTout()   {

		Connection			cn		= null;
		PreparedStatement	stmt	= null;
		ResultSet 			rs 		= null;
		String				sql;

		try {
			cn = dataSource.getConnection();

			sql = "SELECT * FROM personne ORDER BY Nom, Prenom";
			stmt = cn.prepareStatement(sql);
			rs = stmt.executeQuery();

//			List<Personne> personnes = new ArrayList<>();
//			Map<Integer, Categorie> mapCategorie = new HashMap<>();
//			Categorie categorie;
//			Personne personne;
//			while (rs.next()) {
//				categorie = mapCategorie.get( rs.getInt("IdCategorie") );
//				personne = construirePersonne(rs, categorie);
//				personnes.add( personne );
//				if ( categorie == null ) {
//					categorie = personne.getCategorie();
//					mapCategorie.put( categorie.getId(), categorie );
//				}
//			}
			
			List<Personne> personnes = new ArrayList<>();
			Map<Integer, Categorie> mapCategorie = new HashMap<>();
			while (rs.next()) {
				personnes.add( construirePersonne(rs, mapCategorie) );
			}
			return personnes;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			UtilJdbc.close( rs, stmt, cn );
		}
	}

    
    @Override
    public int compterPourCategorie(int idCategorie) {
    	
		Connection			cn		= null;
		PreparedStatement	stmt 	= null;
		ResultSet 			rs		= null;

		try {
			cn = dataSource.getConnection();
            String sql = "SELECT COUNT(*) FROM personne WHERE IdCategorie = ?";
            stmt = cn.prepareStatement( sql );
            stmt.setInt( 1, idCategorie );
            rs = stmt.executeQuery();

            rs.next();
            return rs.getInt( 1 );

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			UtilJdbc.close( rs, stmt, cn );
		}
    }
	
	
	// Méthodes auxiliaires
	
	private Personne construirePersonne( ResultSet rs, Map<Integer, Categorie> mapCategorie ) throws SQLException {
		Categorie categorie = null;
		if ( mapCategorie != null ) {
			categorie = mapCategorie.get( rs.getInt("IdCategorie") );
		}
		if ( categorie == null ) {
			categorie = daoCategorie.retrouver( rs.getInt( "idCategorie" ) );
			if ( mapCategorie != null ) {
				mapCategorie.put( categorie.getId(), categorie );
			}
		}
		Personne personne = new Personne();
		personne.setId(rs.getInt( "IdPersonne" ));
		personne.setCategorie( categorie );
		personne.setNom(rs.getString( "Nom" ));
		personne.setPrenom(rs.getString( "Prenom" ));
		personne.setTelephones( daoTelephone.listerPourPersonne( personne ) );
		return personne;
	}
	
}
