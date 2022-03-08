package contacts;

import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;

import contacts.commun.service.IServiceCategorie;
import contacts.commun.service.IServiceCompte;
import contacts.commun.service.IServiceConnexion;
import contacts.commun.service.IServicePersonne;


@Lazy
@ComponentScan(
		basePackages = {
				"contacts.javafx.data.mapper",
				"contacts.javafx.view",
				"contacts.javafx.model.standard",
		},
		lazyInit = true	)
public class Config4Ejb {
	
	
	@Inject @Lazy 
	private FactoryProxyService factory;

	
	@Bean 
	public InitialContext initialContext() throws NamingException {
		return new InitialContext();
	}
	
	@Bean 
	public FactoryProxyService factoryProxyService() {
		return new FactoryProxyService();
	}
	
	@Bean
	public IServiceConnexion serviceConnexion() {
		return new ServiceConnexion(factory);
	}
	
	@Bean
	public IServiceCategorie serviceCategorie() {
		return factory.createProxy( IServiceCategorie.class );
	}
	
	@Bean
	public IServiceCompte serviceCompte() {
		return factory.createProxy( IServiceCompte.class );
	}
	
	@Bean
	public IServicePersonne servicePersonne() {
		return factory.createProxy( IServicePersonne.class );
	}
	
	
}
