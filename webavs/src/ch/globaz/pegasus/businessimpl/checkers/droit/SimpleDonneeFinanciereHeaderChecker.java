package ch.globaz.pegasus.businessimpl.checkers.droit;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.DonneeFinanciereHeaderSearch;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroitSearch;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public abstract class SimpleDonneeFinanciereHeaderChecker extends PegasusAbstractChecker {
    /**
     * vérifie la cohérence des données financières
     * 
     * La date de fin doit être après la date de début
     * 
     * @param donneeFinanciereHeader
     */
    private static void checkConsistency(SimpleDonneeFinanciereHeader donneeFinanciereHeader) {
        if (JadeDateUtil.isDateBefore(donneeFinanciereHeader.getDateFin(), donneeFinanciereHeader.getDateDebut())) {
            JadeThread.logError(donneeFinanciereHeader.getClass().getName(),
                    "pegasus.donneefinanciereheader.dates.consistency");
        }
    }

    /**
     * @param donneeFinanciereHeader
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     */
    public static void checkForCreate(SimpleDonneeFinanciereHeader donneeFinanciereHeader)
            throws DonneeFinanciereException, JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleDonneeFinanciereHeaderChecker.checkMandatory(donneeFinanciereHeader);
        SimpleDonneeFinanciereHeaderChecker.checkConsistency(donneeFinanciereHeader);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleDonneeFinanciereHeaderChecker.checkIntegrity(donneeFinanciereHeader);
        }
        // vérification superpositionperiodes
        SimpleDonneeFinanciereHeaderChecker.checkSuperpositionPeriodes(donneeFinanciereHeader);
    }

    /**
     * @param donneeFinanciereHeader
     */
    public static void checkForDelete(SimpleDonneeFinanciereHeader donneeFinanciereHeader) {
    }

    /**
     * @param donneeFinanciereHeader
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     */
    public static void checkForUpdate(SimpleDonneeFinanciereHeader donneeFinanciereHeader)
            throws DonneeFinanciereException, JadePersistenceException, JadeNoBusinessLogSessionError {
        SimpleDonneeFinanciereHeaderChecker.checkMandatory(donneeFinanciereHeader);
        SimpleDonneeFinanciereHeaderChecker.checkConsistency(donneeFinanciereHeader);
        // vérification superpositionperiodes
        SimpleDonneeFinanciereHeaderChecker.checkSuperpositionPeriodes(donneeFinanciereHeader);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * <li>La version du droit doit exister</li><li>Le droitMembreFamillet doit exister</li>
     * 
     * @param donneeFinanciereHeader
     * @throws DonneeFinanciereException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleDonneeFinanciereHeader donneeFinanciereHeader)
            throws DonneeFinanciereException, JadePersistenceException, JadeNoBusinessLogSessionError {

        // vérifie que l'id de la version du droit existe
        SimpleVersionDroitSearch vdSearch = new SimpleVersionDroitSearch();
        vdSearch.setForIdVersionDroit(donneeFinanciereHeader.getIdVersionDroit());
        try {
            if (PegasusImplServiceLocator.getSimpleVersionDroitService().count(vdSearch) < 1) {
                JadeThread.logError(donneeFinanciereHeader.getClass().getName(),
                        "pegasus.donneefinanciereheader.versiondroit.integrity");
            }

        } catch (DroitException e1) {
            throw new DonneeFinanciereException("Unable to check donneeFinanciereHeader", e1);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneeFinanciereException("Unable to check donneeFinanciereHeader", e);
        }

        // vérifie que l'id du droitMembreFamille existe
        DroitMembreFamilleSearch dmfSearch = new DroitMembreFamilleSearch();
        dmfSearch.setForIdDroitMembreFamille(donneeFinanciereHeader.getIdDroitMembreFamille());
        try {
            if (PegasusImplServiceLocator.getDroitMembreFamilleService().count(dmfSearch) < 1) {
                JadeThread.logError(donneeFinanciereHeader.getClass().getName(),
                        "pegasus.donneefinanciereheader.droitmembrefamille.integrity");
            }
        } catch (DroitException e1) {
            throw new DonneeFinanciereException("Unable to check donneeFinanciereHeader", e1);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneeFinanciereException("Unable to check donneeFinanciereHeader", e);
        }

    }

    /**
     * Verifiaction des donnees obligatoires:
     * 
     * <li>Vérifie que le donneeFinanciereHeader ait une reference sur un droitMembreFamille</li> <li>Vérifie que le
     * donneeFinanciereHeader ait une reference sur une versionDroit</li> <li>Vérifie que le donneeFinanciereHeader ait
     * un idEntity</li> <li>Vérifie que le donneeFinanciereHeader ait un typeDonneeFinanciere</li> <li>Vérifie que le
     * donneeFinanciereHeader ait une dateDebut</li>
     * 
     * @param donneeFinanciereHeader
     */
    private static void checkMandatory(SimpleDonneeFinanciereHeader donneeFinanciereHeader) {

        // Vérifie que le donneeFinanciereHeader ait une reference sur un
        // droitMembreFamille
        if (JadeStringUtil.isEmpty(donneeFinanciereHeader.getIdDroitMembreFamille())) {
            JadeThread.logError(donneeFinanciereHeader.getClass().getName(),
                    "pegasus.donneefinanciereheader.iddroitmembrefamille.mandatory");
        }

        // Vérifie que le donneeFinanciereHeader ait une reference sur une
        // versionDroit
        if (JadeStringUtil.isEmpty(donneeFinanciereHeader.getIdVersionDroit())) {
            JadeThread.logError(donneeFinanciereHeader.getClass().getName(),
                    "pegasus.donneefinanciereheader.idversiondroit.mandatory");
        }

        // Vérifie que le donneeFinanciereHeader ait un idEntity
        if (JadeStringUtil.isEmpty(donneeFinanciereHeader.getIdEntity())) {
            JadeThread.logError(donneeFinanciereHeader.getClass().getName(),
                    "pegasus.donneefinanciereheader.identity.mandatory");
        }

        // Vérifie que le donneeFinanciereHeader ait un typeDonneeFinanciere
        if (JadeStringUtil.isEmpty(donneeFinanciereHeader.getCsTypeDonneeFinanciere())) {
            JadeThread.logError(donneeFinanciereHeader.getClass().getName(),
                    "pegasus.donneefinanciereheader.typedonneefinanciere.mandatory");
        }

        // Vérifie que le donneeFinanciereHeader ait une dateDebut
        if (JadeStringUtil.isEmpty(donneeFinanciereHeader.getDateDebut())) {
            JadeThread.logError(donneeFinanciereHeader.getClass().getName(),
                    "pegasus.donneefinanciereheader.datedebut.mandatory");
        }

    }

    private static void checkSuperpositionPeriodes(SimpleDonneeFinanciereHeader donneeFinanciereHeader)
            throws JadePersistenceException, JadeNoBusinessLogSessionError, DonneeFinanciereException {

        // Récupération de la version droit pour search superposition
        SimpleVersionDroitSearch vdSearch = new SimpleVersionDroitSearch();
        vdSearch.setForIdVersionDroit(donneeFinanciereHeader.getIdVersionDroit());

        if (JadePersistenceManager.search(vdSearch).getSearchResults().length < 0) {
            JadeThread.logError(donneeFinanciereHeader.getClass().getName(),
                    "pegasus.donneefinanciereheader.versiondroit.integrity");
        }

        SimpleVersionDroit vrDroit = (SimpleVersionDroit) vdSearch.getSearchResults()[0];
        // Pour une période pas de superposition possible
        DonneeFinanciereHeaderSearch sdfSearch = new DonneeFinanciereHeaderSearch();
        sdfSearch.setForIdDonneeFinanciereHeader(donneeFinanciereHeader.getIdDonneeFinanciereHeader());
        sdfSearch.setForNumeroVersion(vrDroit.getNoVersion());
        sdfSearch.setForIdEntityGroup(donneeFinanciereHeader.getIdEntityGroup());
        sdfSearch.setForIdEntity(donneeFinanciereHeader.getIdEntity());

        sdfSearch.setForDateDebutCheckPeriode(donneeFinanciereHeader.getDateDebut());
        sdfSearch.setForDateFinCheckPeriode(donneeFinanciereHeader.getDateFin());
        sdfSearch.setWhereKey("checkSuperpositionPeriodes");

        try {
            // Si pas de résultat, pas de superposition ok
            if (PegasusImplServiceLocator.getDonneeFinanciereHeaderService().count(sdfSearch) > 0) {
                JadeThread.logError(donneeFinanciereHeader.getClass().getName(),
                        "pegasus.donneefinanciereheader.superpositionperiodes.integrity");
            }
        } catch (DroitException e1) {
            throw new DonneeFinanciereException("Unable to check donneeFinanciereHeader", e1);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DonneeFinanciereException("Unable to check donneeFinanciereHeader", e);
        }
    }
}
