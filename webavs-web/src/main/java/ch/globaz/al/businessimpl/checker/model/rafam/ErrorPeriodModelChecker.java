package ch.globaz.al.businessimpl.checker.model.rafam;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.rafam.ErreurAnnonceRafamSearchModel;
import ch.globaz.al.business.models.rafam.ErrorPeriodModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;

/**
 * classe de validation des données de {@link ch.globaz.al.business.models.rafam.ErreurErreurAnnonceRafamModel}
 * 
 * @author jts
 * 
 */
public abstract class ErrorPeriodModelChecker extends ALAbstractChecker {

    /**
     * Vérifie l'intégritée "business" des données
     * 
     * @param model
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected static void checkBusinessIntegrity(ErrorPeriodModel model) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // vérification de l'existence de l'erreur d'annonce
        ErreurAnnonceRafamSearchModel search = new ErreurAnnonceRafamSearchModel();
        search.setForIdErreurAnnonce(model.getIdErreurAnnonce());
        if (0 == ALServiceLocator.getErreurAnnonceRafamModelService().count(search)) {
            JadeThread.logError(ErrorPeriodModelChecker.class.getName(),
                    "al.rafam.errorPeriodModel.idErreurAnnonce.businessIntegrity.ExistingId");
        }

        // Date début doit être antérieure à la date de fin
        if (!JadeStringUtil.isEmpty(model.getErrorPeriodStart())
                && (!JadeStringUtil.isEmpty(model.getErrorPeriodEnd()) && (JadeDateUtil.isDateAfter(
                        model.getErrorPeriodStart(), model.getErrorPeriodEnd())))) {

            JadeThread.logError(OverlapInformationModelChecker.class.getName(),
                    "al.rafam.errorPeriodModel.errorPeriodStart.businessIntegrity.dateChronology");
        }
    }

    /**
     * vérification de l'intégrité des données
     * 
     * @param model
     *            Modèle à valider
     */
    protected static void checkDatabaseIntegrity(ErrorPeriodModel model) {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // IdErreurAnnonce
        if (!JadeNumericUtil.isEmptyOrZero(model.getIdErreurAnnonce())
                && !JadeNumericUtil.isNumericPositif(model.getIdErreurAnnonce())) {
            JadeThread.logError(ErrorPeriodModelChecker.class.getName(),
                    "al.rafam.errorPeriodModel.idErreurAnnonce.databaseIntegrity.type");
        }

        // date début
        if (!JadeStringUtil.isEmpty(model.getErrorPeriodStart())
                && !JadeDateUtil.isGlobazDate(model.getErrorPeriodStart())) {
            JadeThread.logError(ErrorPeriodModelChecker.class.getName(),
                    "al.rafam.errorPeriodModel.errorPeriodStart.databaseIntegrity.dateFormat");
        }

        // date fin
        if (!JadeStringUtil.isEmpty(model.getErrorPeriodEnd()) && !JadeDateUtil.isGlobazDate(model.getErrorPeriodEnd())) {
            JadeThread.logError(ErrorPeriodModelChecker.class.getName(),
                    "al.rafam.errorPeriodModel.errorPeriodEnd.databaseIntegrity.dateFormat");
        }
    }

    /**
     * vérification des données requises
     * 
     * @param model
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected static void checkMandatory(ErrorPeriodModel model) throws JadeApplicationException,
            JadePersistenceException {

        // IdErreurAnnonce
        if (JadeNumericUtil.isEmptyOrZero(model.getIdErreurAnnonce())) {
            JadeThread.logError(ErrorPeriodModelChecker.class.getName(),
                    "al.rafam.errorPeriodModel.idErreurAnnonce.mandatory");
        }

        // date début
        if (JadeStringUtil.isEmpty(model.getErrorPeriodStart())) {
            JadeThread.logError(ErrorPeriodModelChecker.class.getName(),
                    "al.rafam.errorPeriodModel.errorPeriodStart.mandatory");
        }

        // date fin
        if (JadeStringUtil.isEmpty(model.getErrorPeriodEnd())) {
            JadeThread.logError(ErrorPeriodModelChecker.class.getName(),
                    "al.rafam.errorPeriodModel.errorPeriodEnd.mandatory");
        }

    }

    /**
     * validation des données de droitModel
     * 
     * @param model
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(ErrorPeriodModel model) throws JadeApplicationException, JadePersistenceException {

        ErrorPeriodModelChecker.checkMandatory(model);
        ErrorPeriodModelChecker.checkDatabaseIntegrity(model);
        ErrorPeriodModelChecker.checkBusinessIntegrity(model);
    }

    /**
     * Validation de l'intégrité des données avant suppression
     * 
     * @param model
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validateForDelete(ErrorPeriodModel model) throws JadeApplicationException,
            JadePersistenceException {
        // NOTHING TO CHECK
    }
}