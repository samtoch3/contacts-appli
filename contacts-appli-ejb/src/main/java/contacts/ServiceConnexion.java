package contacts;

import java.lang.reflect.UndeclaredThrowableException;

import javax.security.sasl.SaslException;

import org.wildfly.security.auth.AuthenticationException;
import org.wildfly.security.auth.client.AuthenticationConfiguration;
import org.wildfly.security.auth.client.AuthenticationContext;
import org.wildfly.security.auth.client.MatchRule;

import contacts.commun.dto.DtoCompte;
import contacts.commun.exception.ExceptionPermission;
import contacts.commun.service.IServiceConnexion;


public class ServiceConnexion implements IServiceConnexion {

	
    // Champs 

	private final FactoryProxyService	factoryProxyService;

	private IServiceConnexion		serviceConnexionEjb;
	
	
	// Constructeur
	
	public ServiceConnexion( FactoryProxyService factory ) {
		this.factoryProxyService = factory;
	}

    
    // Actions 

	@Override
	public DtoCompte sessionUtilisateurOuvrir( String pseudo, String motDePasse ) {
		AuthenticationConfiguration config = AuthenticationConfiguration.empty()
				.useName( pseudo ).usePassword( motDePasse);
        AuthenticationContext ac = 
        		AuthenticationContext.empty().with( MatchRule.ALL, config );
        factoryProxyService.setAuthenticationContext( ac );
        if ( serviceConnexionEjb == null ) {
			try {
				serviceConnexionEjb = factoryProxyService.createProxy( IServiceConnexion.class );
			} catch (ExceptionPermission e) {
				return null;
			}
		}
		try {
			return serviceConnexionEjb.sessionUtilisateurOuvrir(pseudo, motDePasse);
		} catch (ExceptionPermission e) {
			return null;
		} catch ( UndeclaredThrowableException e) {
			Throwable t = e;
			while (  t.getCause() != null ) {
				t= t.getCause();
			}
			if ( t.getSuppressed().length != 0 ) {
				t = t.getSuppressed()[0];
				if (t.getCause() instanceof SaslException || t.getCause() instanceof AuthenticationException) {
					return null;
				}
			}
			throw runtimeException( t );
		} catch (Exception e) {
			throw runtimeException(e);
		}
	} 

	@Override
	public void sessionUtilisateurFermer() {
        factoryProxyService.setAuthenticationContext( AuthenticationContext.captureCurrent() );
	}

	
	
	// Méthodes auxiliaires
	
	public static RuntimeException runtimeException( Throwable e ) {
		if ( e instanceof ReflectiveOperationException
				&& e.getCause() != null ) {
			e = e.getCause();
		}
		if ( e instanceof RuntimeException ) {
			return (RuntimeException) e;
		} else {
			return new RuntimeException(e);
		}
	}

}
