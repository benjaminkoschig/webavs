package ch.globaz.perseus.businessimpl.checkers.dossier;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.exceptions.models.dossier.DossierException;
import ch.globaz.perseus.business.exceptions.models.rentepont.RentePontException;
import ch.globaz.perseus.business.models.demande.DemandeSearchModel;
import ch.globaz.perseus.business.models.dossier.Dossier;
import ch.globaz.perseus.business.models.dossier.DossierSearchModel;
import ch.globaz.perseus.business.models.rentepont.RentePontSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

public abstract class DossierChecker extends PerseusAbstractChecker {
    /**
     * @param dossier
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws DossierException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static void checkForCreate(Dossier dossier) throws DossierException, JadePersistenceException,
            JadeNoBusinessLogSessionError {
        DossierChecker.checkMandatory(dossier);
        if (!PerseusAbstractChecker.threadOnError()) {
            DossierChecker.checkIntegrity(dossier);
        }
    }

    /**
     * @param dossier
     * @throws DossierException
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     */
    public static void checkForDelete(Dossier dossier) throws DossierException, JadePersistenceException,
            JadeNoBusinessLogSessionError {
        // Check qu'il n'existe plus de demandes dans le dossier
        DemandeSearchModel demandeSearchModel = new DemandeSearchModel();
        demandeSearchModel.setForIdDossier(dossier.getId());
        // Check qu'il n'existe plus de rentes-pont dans le dossier
        RentePontSearchModel rentePontSearchModel = new RentePontSearchModel();
        rentePontSearchModel.setForIdDossier(dossier.getId());
        try {
            if (PerseusServiceLocator.getDemandeService().count(demandeSearchModel) > 0) {
                JadeThread.logError(dossier.getClass().getName(), "perseus.dossier.demandes.existe");
            }
            if (PerseusServiceLocator.getRentePontService().count(rentePontSearchModel) > 0) {
                JadeThread.logError(dossier.getClass().getName(), "perseus.dossier.demandes.existe");
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DossierException("Unable to check dossier", e);
        } catch (DemandeException e) {
            throw new DossierException("Demande exception during dossier check : " + e.getMessage());
        } catch (RentePontException e) {
            throw new DossierException("RentePont exception during dossier check : " + e.getMessage());
        }
    }

    /**
     * @param dossier
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws DossierException
     */
    public static void checkForUpdate(Dossier dossier) throws DossierException, JadePersistenceException,
            JadeNoBusinessLogSessionError {
        DossierChecker.checkMandatory(dossier);
    }

    /**
     * @param dossier
     * @throws DossierException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(Dossier dossier) throws DossierException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

        // Vérifie qu'il n'existe pas déjà un dossier avec le même tiers
        DossierSearchModel dSearch = new DossierSearchModel();
        dSearch.setForIdTiers(dossier.getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers());
        try {
            if (PerseusServiceLocator.getDossierService().count(dSearch) > 0) {
                JadeThread.logError(dossier.getClass().getName(), "perseus.dossier.dossierUnique.mandatory");
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DossierException("Unable to check dossier", e);
        }
    }

    /**
     * @param dossier
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DossierException
     */
    private static void checkMandatory(Dossier dossier) throws DossierException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

        if (JadeStringUtil.isEmpty(dossier.getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers())) {
            JadeThread.logError(dossier.getClass().getName(), "perseus.dossier.idTiers.mandatory");
        }
        if (JadeStringUtil.isEmpty(dossier.getDossier().getDateRevision())) {
            JadeThread.logError(DossierChecker.class.getName(), "perseus.dossier.dateRevision.mandatory");
        }
        if (JadeStringUtil.isEmpty(dossier.getDossier().getGestionnaire())) {
            JadeThread.logError(DossierChecker.class.getName(), "perseus.dossier.gestionnaire.mandatory");
        }

    }
}
