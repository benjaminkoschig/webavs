package ch.globaz.al.businessimpl.checker.model.rafam;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.rafam.ErreurAnnonceRafamSearchModel;
import ch.globaz.al.business.models.rafam.OverlapInformationModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;

/**
 * classe de validation des donn�es de {@link ch.globaz.al.business.models.rafam.ErreurErreurAnnonceRafamModel}
 * 
 * @author jts
 * 
 */
public abstract class OverlapInformationModelChecker extends ALAbstractChecker {

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
    protected static void checkBusinessIntegrity(OverlapInformationModel model) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // v�rification de l'existence de l'erreur d'annonce
        ErreurAnnonceRafamSearchModel search = new ErreurAnnonceRafamSearchModel();
        search.setForIdErreurAnnonce(model.getIdErreurAnnonce());
        if (0 == ALServiceLocator.getErreurAnnonceRafamModelService().count(search)) {
            JadeThread.logError(ErrorPeriodModelChecker.class.getName(),
                    "al.rafam.overlapInformationModel.idErreurAnnonce.businessIntegrity.ExistingId");
        }

        // Date de chevauchement d�but doit �tre ant�rieure � la date de
        // chevauchement fin
        if (!JadeStringUtil.isEmpty(model.getOverlapPeriodeStart())
                && (!JadeStringUtil.isEmpty(model.getOverlapPeriodeEnd()) && (JadeDateUtil.isDateAfter(
                        model.getOverlapPeriodeStart(), model.getOverlapPeriodeEnd())))) {

            JadeThread.logError(OverlapInformationModelChecker.class.getName(),
                    "al.rafam.overlapInformationModel.overlapPeriodeStart.businessIntegrity.dateChronology");
        }
    }

    /**
     * v�rification de l'int�grit� des donn�es
     * 
     * @param model
     *            Mod�le � valider
     */
    protected static void checkDatabaseIntegrity(OverlapInformationModel model) {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // IdErreurAnnonce
        if (!JadeNumericUtil.isEmptyOrZero(model.getIdErreurAnnonce())
                && !JadeNumericUtil.isNumericPositif(model.getIdErreurAnnonce())) {
            JadeThread.logError(OverlapInformationModelChecker.class.getName(),
                    "al.rafam.overlapInformationModel.idErreurAnnonce.databaseIntegrity.type");
        }

        // OverlapPeriodeStart
        if (!JadeStringUtil.isEmpty(model.getOverlapPeriodeStart())
                && !JadeDateUtil.isGlobazDate(model.getOverlapPeriodeStart())) {
            JadeThread.logError(OverlapInformationModelChecker.class.getName(),
                    "al.rafam.overlapInformationModel.overlapPeriodeStart.databaseIntegrity.dateFormat");
        }

        // OverlapPeriodeStart
        if (!JadeStringUtil.isEmpty(model.getOverlapPeriodeEnd())
                && !JadeDateUtil.isGlobazDate(model.getOverlapPeriodeEnd())) {
            JadeThread.logError(OverlapInformationModelChecker.class.getName(),
                    "al.rafam.overlapInformationModel.overlapPeriodeEnd.databaseIntegrity.dateFormat");
        }

        // MinimalStartFlag
        if (!JadeNumericUtil.isInteger(model.getMinimalStartFlag())) {
            JadeThread.logError(AnnonceRafamModelChecker.class.getName(),
                    "al.rafam.overlapInformationModel.minimalStartFlag.databaseIntegrity.type");
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
    protected static void checkMandatory(OverlapInformationModel model) throws JadeApplicationException,
            JadePersistenceException {

        // IdErreurAnnonce
        if (JadeNumericUtil.isEmptyOrZero(model.getIdErreurAnnonce())) {
            JadeThread.logError(ErrorPeriodModelChecker.class.getName(),
                    "al.rafam.overlapInformationModel.idErreurAnnonce.mandatory");
        }

        // OfficeIdentifier
        if (JadeStringUtil.isEmpty(model.getOfficeIdentifier())) {
            JadeThread.logError(ErrorPeriodModelChecker.class.getName(),
                    "al.rafam.overlapInformationModel.officeIdentifier.mandatory");
        }

        // OverlapPeriodeStar
        if (JadeNumericUtil.isEmptyOrZero(model.getOverlapPeriodeStart())) {
            JadeThread.logError(ErrorPeriodModelChecker.class.getName(),
                    "al.rafam.overlapInformationModel.overlapPeriodeStart.mandatory");
        }

        // OverlapPeriodeEnd
        if (JadeNumericUtil.isEmptyOrZero(model.getOverlapPeriodeEnd())) {
            JadeThread.logError(ErrorPeriodModelChecker.class.getName(),
                    "al.rafam.overlapInformationModel.overlapPeriodeEnd.mandatory");
        }

        // MinimalStartFlag
        if (JadeStringUtil.isBlank(model.getMinimalStartFlag())) {
            JadeThread.logError(ErrorPeriodModelChecker.class.getName(),
                    "al.rafam.overlapInformationModel.minimalStartFlag.mandatory");
        }

        // Insignificance
        if (model.getInsignificance() == null) {
            JadeThread.logError(ErrorPeriodModelChecker.class.getName(),
                    "al.rafam.overlapInformationModel.insignificance.mandatory");
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
    public static void validate(OverlapInformationModel model) throws JadeApplicationException,
            JadePersistenceException {

        OverlapInformationModelChecker.checkMandatory(model);
        OverlapInformationModelChecker.checkDatabaseIntegrity(model);
        OverlapInformationModelChecker.checkBusinessIntegrity(model);
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
    public static void validateForDelete(OverlapInformationModel model) throws JadeApplicationException,
            JadePersistenceException {
        // NOTHING TO CHECK
    }
}