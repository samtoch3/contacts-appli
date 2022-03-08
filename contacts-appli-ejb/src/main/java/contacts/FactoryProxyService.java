package contacts;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.AccessException;

import javax.ejb.EJBAccessException;
import javax.ejb.EJBException;
import javax.ejb.NoSuchEJBException;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.sasl.SaslException;

import org.wildfly.security.auth.AuthenticationException;
import org.wildfly.security.auth.client.AuthenticationContext;

import contacts.commun.exception.ExceptionAnomaly;
import contacts.commun.exception.ExceptionPermission;


public class FactoryProxyService {

	
	// Constantes
	private static final String PREFIX = "contacts-ear/contacts-ejbs/";

	
	// Champs
	
	@Inject
	private InitialContext 			ic;

	private AuthenticationContext	ac= AuthenticationContext.captureCurrent();
	
	
	// Getters & Setters
	
	public AuthenticationContext getAuthenticationContext() {
		return ac;
	}
	
	public void setAuthenticationContext( AuthenticationContext ac  ) {
		this.ac = ac;
	}

	
	
	// Actions

	@SuppressWarnings("unchecked")
	public <T> T createProxy( Class<T> type ) {
		
		try {
			
			// Lookup
			String nomJndi = PREFIX + type.getSimpleName().substring(1) + "!" +type.getName();
			T service = (T) ac.runCallable(  () -> ic.lookup( nomJndi )	);

			return (T) Proxy.newProxyInstance(
					type .getClassLoader(),
					new Class[] {type},
					new Handler<T>( service )
					);		

		} catch ( RuntimeException e ) {
			throw e;
		} catch (Exception e) {
			if( e instanceof NamingException && e.getCause()!=null ) {
				Throwable cause = e.getCause();
				if ( cause instanceof SaslException
					|| cause instanceof AuthenticationException 
					|| cause.getCause() instanceof AccessException 
				) {
					throw new ExceptionPermission( cause );
				}
			}
			throw new RuntimeException(e);
		}
		
		
	}

	
	// Types auxiliaires
	
	private class Handler<T> implements InvocationHandler {
		
		// Champs
		private final T service;
		
		// Constructeur
		public Handler( T service ) {
			super();
			if ( service == null ) {
				throw new NullPointerException();
			}
			this.service = service;
		}

		
		// Actions
	    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	    	try {
				return ac.runCallable(  () -> method.invoke( service, args) );
			} catch ( InvocationTargetException e) {
				Throwable cause = e.getCause();
				if ( cause.getSuppressed().length != 0 ) {
					cause = cause.getSuppressed()[0];
					if ( cause.getCause() instanceof AuthenticationException 
							|| cause.getCause() instanceof SaslException ) {
						throw new ExceptionPermission( cause.getCause() );
					}
				}
				if ( cause instanceof EJBAccessException ) {
					throw new ExceptionPermission( cause );
				}
				if ( cause instanceof NoSuchEJBException ) {
					throw cause;
				}
				if ( cause instanceof EJBException ) {
					if ( cause.getCause() instanceof ExceptionAnomaly ) {
						throw cause.getCause();
					} else {
						throw new ExceptionAnomaly( cause );
					}
				}
				throw cause;
			} catch ( Exception e) {
				throw e;
			}
		}
	}
	
}
