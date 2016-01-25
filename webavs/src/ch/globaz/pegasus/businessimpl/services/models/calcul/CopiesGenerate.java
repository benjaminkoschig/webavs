package ch.globaz.pegasus.businessimpl.services.models.calcul;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadeCloneModelException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.calcul.CalculPcaReplace;
import ch.globaz.pegasus.business.models.calcul.CalculPcaReplaceSearch;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.services.models.calcul.CalculPersistanceService;

// @formatter:off
/**
 * Le but de cette class est de générer les pca qui doivent être copié. On copie une pca lors qu'une nouvelle pca est
 * créer et quelle commence après une ancienne pca . Aucune copie ne sera générer si on est dans une version initial du
 * droit Ex: [Copie v2][ v2] [ V1 ] Sans date de fin: [Copie v2] [ V2] [ V1 Sans copie: [V2] [V1][V1]
 * 
 * @author dma
 */
public class CopiesGenerate {

	private CalculPersistanceService calculPersistanceService;
	private Set<String> controlCopieCreated = new HashSet<String>();
	private boolean hasMoreCopieHasExpected = false;

	public CopiesGenerate(CalculPersistanceService calculPersistanceService) {
		if (calculPersistanceService == null) {
			throw new IllegalArgumentException("Unable to CopiesGenerate, the calculPersistanceService is null!");
		}
		this.calculPersistanceService = calculPersistanceService;
	}

	private CalculPcaReplace clonePca(JadeAbstractModel model) throws DroitException {
		try {
			model = JadePersistenceUtil.clone(model);
		} catch (JadeCloneModelException e) {
			throw new DroitException("Unable to clone the mode pca for create the copie", e);
		}
		CalculPcaReplace pca = ((CalculPcaReplace) model);
		return pca;
	}

	private void detectIfhasMoreCopieHasExpected(String idTiers, String dateFCloturePca) {
		String key = idTiers + "_" + dateFCloturePca;
		if (!controlCopieCreated.contains(key)) {
			controlCopieCreated.add(key);
		} else {
			hasMoreCopieHasExpected = true;
		}
	}

	/**
	 * Génération des copies
	 * 
	 * @param pcaReplaced
	 * @param dateDebut
	 *            (Date au format jj.mm.yyyy)
	 * @param droit
	 * @return
	 * @throws DroitException
	 */
	public List<CalculPcaReplace> generate(CalculPcaReplaceSearch pcaReplaced, String dateDebutNewPCa,
			SimpleVersionDroit droit) throws DroitException {

		if (droit == null) {
			throw new IllegalArgumentException("Unable to generate, the  droit is null!");
		}

		if (JadeStringUtil.isBlankOrZero(dateDebutNewPCa)) {
			throw new IllegalArgumentException("Unable to generate, the  dateDebut is null!");
		}

		if (pcaReplaced == null) {
			throw new IllegalArgumentException("Unable to generate, the pcaReplaced is null!");
		}

		List<CalculPcaReplace> pcas = new ArrayList<CalculPcaReplace>();
		// on recherche les pca du droit précédent avec une date de debut antérieur à la date de dbut de la plage de
		// calcul
		if (isVersionDroitNotInitial(droit)) {
			// Si résultats copies des pca avec fermeture des périodes ouvertes
			String dateDebuNewPca = dateDebutNewPCa.substring(3);

			for (JadeAbstractModel model : pcaReplaced.getSearchResults()) {

				String idPCa = model.getId();
				CalculPcaReplace pca = clonePca(model);
				String dateDebutPcaReplaced = pca.getSimplePCAccordee().getDateDebut();
				String dateFinPcaReplaced = pca.getSimplePCAccordee().getDateFin();
				if ((JadeDateUtil.isDateMonthYearBefore(dateDebutPcaReplaced, dateDebuNewPca) && !dateDebuNewPca
						.equals(dateDebutPcaReplaced))
						&& (JadeDateUtil.isDateMonthYearAfter(dateFinPcaReplaced, dateDebuNewPca)
								|| dateDebuNewPca.equals(dateFinPcaReplaced) || JadeStringUtil
									.isBlankOrZero(dateFinPcaReplaced))) {

					String dateFCloturePca = JadeDateUtil.addMonths(dateDebutNewPCa, -1).substring(3);
					pca.getSimplePCAccordee().setDateFin(dateFCloturePca);
					pca.setId(idPCa);
					pca.getSimplePCAccordee().setIdVersionDroit(droit.getIdVersionDroit());
					pcas.add(pca);
					detectIfhasMoreCopieHasExpected(pca.getSimplePrestationsAccordees().getIdTiersBeneficiaire(),
							dateFCloturePca);
				}
			}
		}
		return pcas;
	}

	/**
	 * @param pcaReplaced
	 * @param dateDebut
	 *            (Date au format jj.mm.yyyy)
	 * @param droit
	 * @return
	 * @throws DroitException
	 */
	public List<CalculPcaReplace> generateAndPersist(CalculPcaReplaceSearch pcaReplaced, String dateDebut,
			SimpleVersionDroit droit) throws PCAccordeeException, DroitException,
			JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
		List<CalculPcaReplace> list = generate(pcaReplaced, dateDebut, droit);
		persist(list);
		return list;
	}

	public boolean getHasMoreCopieHasExpected() {
		return hasMoreCopieHasExpected;
	}

	private boolean isVersionDroitNotInitial(SimpleVersionDroit droit) {
		Integer noVersion = Integer.parseInt(droit.getNoVersion());
		return noVersion > 1;
	}

	void persist(List<CalculPcaReplace> pcas) throws PCAccordeeException, JadeApplicationServiceNotAvailableException,
			JadePersistenceException, JadeApplicationException {
		for (CalculPcaReplace pca : pcas) {
			calculPersistanceService.sauvePCAccordeeToCopie(pca);
		}
	}

}
