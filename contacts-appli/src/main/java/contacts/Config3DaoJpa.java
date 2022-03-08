package contacts;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;


@Lazy
@ComponentScan(
		basePackages = {
				"contacts.javafx.data.mapper",
				"contacts.javafx.view",
				"contacts.javafx.model.standard",
				"contacts.emb.data.mapper",
				"contacts.emb.service.util",
				"contacts.emb.service.standard",
				"contacts.emb.dao.jpa",
		},
		lazyInit = true	)
public class Config3DaoJpa {
	
	@Bean @Lazy
	public EntityManagerFactory emf() {
		return Persistence.createEntityManagerFactory( "contacts" );
	}
	
	@Bean @Lazy
	public EntityManager em( EntityManagerFactory emf ) {
		return emf.createEntityManager();
	}
}
