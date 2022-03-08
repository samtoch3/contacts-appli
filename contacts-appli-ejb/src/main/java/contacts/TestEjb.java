package contacts;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import contacts.commun.dto.DtoCompte;
import contacts.commun.service.IServiceCompte;

public class TestEjb {

	public static void main(String[] args) throws NamingException {
		InitialContext ic = new InitialContext();
		IServiceCompte serviceCompte = (IServiceCompte) ic.lookup("contacts-ear/contacts-ejbs/ServiceCompte!contacts.commun.service.IServiceCompte");
		for( DtoCompte compte : serviceCompte.listerTout() ) {
		System.out.println( compte.getMotDePasse() );
		}
		ic.close();

	}

}
