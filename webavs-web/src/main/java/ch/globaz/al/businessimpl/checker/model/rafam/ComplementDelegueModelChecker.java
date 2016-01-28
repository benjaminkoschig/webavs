package ch.globaz.al.businessimpl.checker.model.rafam;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.rafam.ComplementDelegueModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;

public abstract class ComplementDelegueModelChecker extends ALAbstractChecker {

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
    protected static void checkBusinessIntegrity(ComplementDelegueModel model) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // v�rification de l'existence de l'erreur d'annonce

        // ErreurAnnonceRafamSearchModel search = new ErreurAnnonceRafamSearchModel();
        // search.setForIdErreurAnnonce(model.getIdErreurAnnonce());
        // if (0 == ALServiceLocator.getErreurAnnonceRafamModelService().count(search)) {
        // JadeThread.logError(ErrorPeriodModelChecker.class.getName(),
        // "al.rafam.errorPeriodModel.idErreurAnnonce.businessIntegrity.ExistingId");
        // }

    }

    /**
     * v�rification de l'int�grit� des donn�es
     * 
     * @param model
     *            Mod�le � valider
     */
    protected static void checkDatabaseIntegrity(ComplementDelegueModel model) {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // idComplement
        if (!JadeNumericUtil.isEmptyOrZero(model.getIdComplement())
                && !JadeNumericUtil.isNumericPositif(model.getIdComplement())) {
            JadeThread.logError(ComplementDelegueModelChecker.class.getName(),
                    "al.rafam.complementDelegueModel.idComplement.databaseIntegrity.type");
        }

        // date fin d'activit� b�n�ficiaire
        if (!JadeStringUtil.isEmpty(model.getBeneficiaryEndDate())
                && !JadeDateUtil.isGlobazDate(model.getBeneficiaryEndDate())) {
            JadeThread.logError(ComplementDelegueModelChecker.class.getName(),
                    "al.rafam.complementDelegueModel.beneficiaryEndDate.databaseIntegrity.dateFormat");
        }

        // date d�but d'activit� b�n�ficiaire
        if (!JadeStringUtil.isEmpty(model.getBeneficiaryStartDate())
                && !JadeDateUtil.isGlobazDate(model.getBeneficiaryStartDate())) {
            JadeThread.logError(ComplementDelegueModelChecker.class.getName(),
                    "al.rafam.complementDelegueModel.beneficiaryStartDate.databaseIntegrity.dateFormat");
        }

        // date du message
        if (!JadeStringUtil.isEmpty(model.getMessageDate()) && !JadeDateUtil.isGlobazDate(model.getMessageDate())) {
            JadeThread.logError(ComplementDelegueModelChecker.class.getName(),
                    "al.rafam.complementDelegueModel.messageDate.databaseIntegrity.dateFormat");
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
    protected static void checkMandatory(ComplementDelegueModel model) throws JadeApplicationException,
            JadePersistenceException {

        // recordNumber
        if (JadeNumericUtil.isEmptyOrZero(model.getRecordNumber())) {
            JadeThread.logError(ComplementDelegueModelChecker.class.getName(),
                    "al.rafam.complementDelegueModel.recordNumber.mandatory");
        }

        // // date d�but
        // if (JadeStringUtil.isEmpty(model.getErrorPeriodStart())) {
        // JadeThread.logError(ErrorPeriodModelChecker.class.getName(),
        // "al.rafam.errorPeriodModel.errorPeriodStart.mandatory");
        // }
        //
        // // date fin
        // if (JadeStringUtil.isEmpty(model.getErrorPeriodEnd())) {
        // JadeThread.logError(ErrorPeriodModelChecker.class.getName(),
        // "al.rafam.errorPeriodModel.errorPeriodEnd.mandatory");
        // }

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
    public static void validate(ComplementDelegueModel model) throws JadeApplicationException, JadePersistenceException {

        ComplementDelegueModelChecker.checkMandatory(model);
        ComplementDelegueModelChecker.checkDatabaseIntegrity(model);
        ComplementDelegueModelChecker.checkBusinessIntegrity(model);
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
    public static void validateForDelete(ComplementDelegueModel model) throws JadeApplicationException,
            JadePersistenceException {
        // NOTHING TO CHECK
    }

}
