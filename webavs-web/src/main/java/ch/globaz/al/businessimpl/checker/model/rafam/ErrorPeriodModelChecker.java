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
 * classe de validation des donn�es de {@link ch.globaz.al.business.models.rafam.ErreurErreurAnnonceRafamModel}
 * 
 * @author jts
 * 
 */
public abstract class ErrorPeriodModelChecker extends ALAbstractChecker {

    /**
     * V�rifie l'int�grit�e "business" des donn�es
     * 
     * @param model
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected static void checkBusinessIntegrity(ErrorPeriodModel model) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // v�rification de l'existence de l'erreur d'annonce
        ErreurAnnonceRafamSearchModel search = new ErreurAnnonceRafamSearchModel();
        search.setForIdErreurAnnonce(model.getIdErreurAnnonce());
        if (0 == ALServiceLocator.getErreurAnnonceRafamModelService().count(search)) {
            JadeThread.logError(ErrorPeriodModelChecker.class.getName(),
                    "al.rafam.errorPeriodModel.idErreurAnnonce.businessIntegrity.ExistingId");
        }

        // Date d�but doit �tre ant�rieure � la date de fin
        if (!JadeStringUtil.isEmpty(model.getErrorPeriodStart())
                && (!JadeStringUtil.isEmpty(model.getErrorPeriodEnd()) && (JadeDateUtil.isDateAfter(
                        model.getErrorPeriodStart(), model.getErrorPeriodEnd())))) {

            JadeThread.logError(OverlapInformationModelChecker.class.getName(),
                    "al.rafam.errorPeriodModel.errorPeriodStart.businessIntegrity.dateChronology");
        }
    }

    /**
     * v�rification de l'int�grit� des donn�es
     * 
     * @param model
     *            Mod�le � valider
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

        // date d�but
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
     * v�rification des donn�es requises
     * 
     * @param model
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected static void checkMandatory(ErrorPeriodModel model) throws JadeApplicationException,
            JadePersistenceException {

        // IdErreurAnnonce
        if (JadeNumericUtil.isEmptyOrZero(model.getIdErreurAnnonce())) {
            JadeThread.logError(ErrorPeriodModelChecker.class.getName(),
                    "al.rafam.errorPeriodModel.idErreurAnnonce.mandatory");
        }

        // date d�but
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
     * validation des donn�es de droitModel
     * 
     * @param model
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(ErrorPeriodModel model) throws JadeApplicationException, JadePersistenceException {

        ErrorPeriodModelChecker.checkMandatory(model);
        ErrorPeriodModelChecker.checkDatabaseIntegrity(model);
        ErrorPeriodModelChecker.checkBusinessIntegrity(model);
    }

    /**
     * Validation de l'int�grit� des donn�es avant suppression
     * 
     * @param model
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validateForDelete(ErrorPeriodModel model) throws JadeApplicationException,
            JadePersistenceException {
        // NOTHING TO CHECK
    }
}