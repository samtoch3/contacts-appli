package contacts.emb.service.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import contacts.commun.dto.DtoCompte;
import contacts.commun.exception.ExceptionValidation;
import contacts.commun.service.IServiceCompte;
import contacts.emb.service.util.IManagerSecurity;
import contacts.emb.service.util.UtilServices;

@Component
public class ServiceCompte implements IServiceCompte {

	
	// Champs

	@Inject
	private Donnees donnees;
	@Inject
	private IManagerSecurity managerSecurite;

	
	// Actions

	@Override
	public int inserer(DtoCompte dtoCompte) throws ExceptionValidation {

		try {
			managerSecurite.verifierAutorisationAdmin();
			verifierValiditeDonnees(dtoCompte);
			dtoCompte.setId( donnees.getProchainIdCompte()) ;
			donnees.getMapComptes().put(dtoCompte.getId(), dtoCompte);
			return dtoCompte.getId();
		} catch (Exception e) {
			throw UtilServices.exceptionValidationOrAnomaly(e);
		}
	}

	@Override
	public void modifier(DtoCompte dtoCompte) throws ExceptionValidation {
		try {
			managerSecurite.verifierAutorisationAdmin();
			verifierValiditeDonnees(dtoCompte);
			donnees.getMapComptes().replace(dtoCompte.getId(), dtoCompte);
		} catch (Exception e) {
			throw UtilServices.exceptionValidationOrAnomaly(e);
		}
	}

	@Override
	public void supprimer(int idCompte) throws ExceptionValidation {
		try {
			managerSecurite.verifierAutorisationAdmin();
			if (managerSecurite.getIdCompteActif() == idCompte) {
				throw new ExceptionValidation(
						"Vous ne pouvez pas supprimer le compte avec lequel vous êtes connecté !");
			}

			donnees.getMapComptes().remove(idCompte);
		} catch (Exception e) {
			throw UtilServices.exceptionValidationOrAnomaly(e);
		}
	}

	@Override
	public DtoCompte retrouver(int idCompte) {
		try {
			managerSecurite.verifierAutorisationAdmin();
			return donnees.getMapComptes().get(idCompte);
		} catch (Exception e) {
			throw UtilServices.exceptionAnomaly(e);
		}
	}

	@Override
	public List<DtoCompte> listerTout() {
		try {
			managerSecurite.verifierAutorisationAdmin();
			return trierParPseudo(new ArrayList<>(donnees.getMapComptes().values()));
		} catch (Exception e) {
			throw UtilServices.exceptionAnomaly(e);
		}
	}

	// Méthodes auxiliaires

	private void verifierValiditeDonnees(DtoCompte dtoCompte) throws ExceptionValidation {

		StringBuilder message = new StringBuilder();

		if (dtoCompte.getPseudo() == null || dtoCompte.getPseudo().isEmpty()) {
			message.append("\nLe pseudo est absent.");
		} else if (dtoCompte.getPseudo().length() < 3) {
			message.append("\nLe pseudo est trop court.");
		} else if (dtoCompte.getPseudo().length() > 25) {
			message.append("\nLe pseudo est trop long.");
		} else {
			boolean pseudoDejaPresent = false;
			for (DtoCompte c : donnees.getMapComptes().values()) {
				if (c.getPseudo().equals(dtoCompte.getPseudo()) && c.getId() != dtoCompte.getId()) {
					pseudoDejaPresent = true;
					break;
				}
			}
			if (pseudoDejaPresent) {
				message.append("\nLe pseudo " + dtoCompte.getPseudo() + " est déjà utilisé.");
			}
		}

		if (dtoCompte.getMotDePasse() == null || dtoCompte.getMotDePasse().isEmpty()) {
			message.append("\nLe mot de passe est absent.");
		} else if (dtoCompte.getMotDePasse().length() < 3) {
			message.append("\nLe mot de passe est trop court.");
		} else if (dtoCompte.getMotDePasse().length() > 25) {
			message.append("\nLe mot de passe est trop long.");
		}

		if (dtoCompte.getEmail() == null || dtoCompte.getEmail().isEmpty()) {
			message.append("\nL'adresse e-mail est absente.");
		}

		if (message.length() > 0) {
			throw new ExceptionValidation(message.toString().substring(1));
		}
	}

	private List<DtoCompte> trierParPseudo(List<DtoCompte> liste) {
		Collections.sort(liste, (Comparator<DtoCompte>) (item1, item2) -> {
			return (item1.getPseudo().toUpperCase().compareTo(item2.getPseudo().toUpperCase()));
		});
		return liste;
	}

}
