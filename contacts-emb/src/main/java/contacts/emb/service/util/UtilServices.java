package contacts.emb.service.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import contacts.commun.exception.ExceptionAnomaly;
import contacts.commun.exception.ExceptionValidation;


public final class UtilServices {


	// Logger
	private static final Logger logger = Logger.getLogger( UtilServices.class.getName() );
	
	
	// Constructeur
	private UtilServices() {
	}
    
    
	// Méthodes utilitaires
	
	public static RuntimeException exceptionAnomaly( Throwable e ) {

    	if ( e instanceof ExceptionAnomaly ) {
    		throw (ExceptionAnomaly) e;
    	} else {
    		if ( e.getClass() == RuntimeException.class && e.getCause() != null ) {
    			e = e.getCause();
    		}
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new ExceptionAnomaly( e );
    	}
    }
    
    
    public static RuntimeException exceptionValidationOrAnomaly( Throwable e ) throws ExceptionValidation {
    	
    	if ( e instanceof ExceptionValidation ) {
    		throw (ExceptionValidation) e;
    	} else {
        	throw exceptionAnomaly( e  );
    	}
    }

}
