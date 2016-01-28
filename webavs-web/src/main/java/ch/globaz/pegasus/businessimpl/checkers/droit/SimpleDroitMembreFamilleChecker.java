package ch.globaz.pegasus.businessimpl.checkers.droit;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.hera.business.exceptions.models.MembreFamilleException;
import ch.globaz.hera.business.models.famille.MembreFamilleSearch;
import ch.globaz.hera.business.services.HeraServiceLocator;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneesPersonnellesException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.DroitSearch;
import ch.globaz.pegasus.business.models.droit.SimpleDonneesPersonnellesSearch;
import ch.globaz.pegasus.business.models.droit.SimpleDroitMembreFamille;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public abstract class SimpleDroitMembreFamilleChecker extends PegasusAbstractChecker {
    /**
     * @param droitMembreFamille
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws DroitException
     */
    public static void checkForCreate(SimpleDroitMembreFamille droitMembreFamille) throws DroitException,
            JadePersistenceException, JadeNoBusinessLogSessionError {
        SimpleDroitMembreFamilleChecker.checkMandatory(droitMembreFamille);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleDroitMembreFamilleChecker.checkIntegrity(droitMembreFamille);
        }
    }

    /**
     * @param droitMembreFamille
     */
    public static void checkForDelete(SimpleDroitMembreFamille droitMembreFamille) {
    }

    /**
     * @param droitMembreFamille
     */
    public static void checkForUpdate(SimpleDroitMembreFamille droitMembreFamille) {
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * <li>Le droit doit exister</li> <li>Le membre de famille doit exister dans la SF</li> <li>Les données personnelles
     * doivent exister</li>
     * 
     * @param droitMembreFamille
     * @throws DroitException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleDroitMembreFamille droitMembreFamille) throws DroitException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        // vérifie que l'id du droit existe
        DroitSearch dSearch = new DroitSearch();
        dSearch.setForIdDroit(droitMembreFamille.getIdDroit());
        try {
            if (PegasusImplServiceLocator.getDroitBusinessService().count(dSearch) < 1) {
                JadeThread.logError(droitMembreFamille.getClass().getName(),
                        "pegasus.droitMembreFamille.droit.integrity");

            }
        } catch (DroitException e1) {
            throw new DroitException("Unable to check droitMembreFamille", e1);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Unable to check droitMembreFamille", e);
        }

        // vérifie que l'id des donnees personnelles existe
        SimpleDonneesPersonnellesSearch sdpSearch = new SimpleDonneesPersonnellesSearch();
        sdpSearch.setForIdDonneesPersonnelles(droitMembreFamille.getIdDonneesPersonnelles());
        try {
            if (PegasusImplServiceLocator.getSimpleDonneesPersonnellesService().count(sdpSearch) < 1) {
                JadeThread.logError(droitMembreFamille.getClass().getName(),
                        "pegasus.droitMembreFamille.donneesPersonelles.integrity");

            }
        } catch (DonneesPersonnellesException e1) {
            throw new DroitException("Unable to check droitMembreFamille", e1);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Unable to check droitMembreFamille", e);
        }

        // vérifier que l'id du membre de famille existe dans la SF
        MembreFamilleSearch mfSearch = new MembreFamilleSearch();
        mfSearch.setForIdMembreFamille(droitMembreFamille.getIdMembreFamilleSF());
        try {
            if (HeraServiceLocator.getMembreFamilleService().count(mfSearch) < 1) {
                JadeThread.logError(droitMembreFamille.getClass().getName(),
                        "pegasus.droitMembreFamille.donneesPersonelles.integrity");

            }
        } catch (MembreFamilleException e1) {
            throw new DroitException("Unable to check droitMembreFamille", e1);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DroitException("Unable to check droitMembreFamille", e);
        }

    }

    /**
     * Verifiaction des donnees obligatoires:
     * 
     * <li>Un droitMembreFamille doit avoir une reference sur un droit</li> <li>
     * Un droitMembreFamille doit avoir une reference sur un membreFamilleSF</li> <li>Un droitMembreFamille doit avoir
     * une reference sur des donneesPersonnelles</li>
     * 
     * @param droitMembreFamille
     */
    private static void checkMandatory(SimpleDroitMembreFamille droitMembreFamille) {
        // Vérifie que le droitMembreFamille ait une reference sur un droit
        if (JadeStringUtil.isEmpty(droitMembreFamille.getIdDroit())) {
            JadeThread
                    .logError(droitMembreFamille.getClass().getName(), "pegasus.droitMembreFamille.iddroit.mandatory");
        }
        // Vérifie que le droitMembreFamille ait une reference sur un
        // membreFamilleSF
        if (JadeStringUtil.isEmpty(droitMembreFamille.getIdMembreFamilleSF())) {
            JadeThread.logError(droitMembreFamille.getClass().getName(),
                    "pegasus.droitMembreFamille.idmembrefamillesf.mandatory");
        }
        // Vérifie que le droitMembreFamille ait une reference sur des
        // donneesPersonnelles
        if (JadeStringUtil.isEmpty(droitMembreFamille.getIdDonneesPersonnelles())) {
            JadeThread.logError(droitMembreFamille.getClass().getName(),
                    "pegasus.droitMembreFamille.iddonneespersonnelles.mandatory");
        }
    }
}
