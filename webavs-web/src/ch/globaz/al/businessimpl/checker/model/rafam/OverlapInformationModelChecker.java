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
 * classe de validation des données de {@link ch.globaz.al.business.models.rafam.ErreurErreurAnnonceRafamModel}
 * 
 * @author jts
 * 
 */
public abstract class OverlapInformationModelChecker extends ALAbstractChecker {

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
    protected static void checkBusinessIntegrity(OverlapInformationModel model) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // vérification de l'existence de l'erreur d'annonce
        ErreurAnnonceRafamSearchModel search = new ErreurAnnonceRafamSearchModel();
        search.setForIdErreurAnnonce(model.getIdErreurAnnonce());
        if (0 == ALServiceLocator.getErreurAnnonceRafamModelService().count(search)) {
            JadeThread.logError(ErrorPeriodModelChecker.class.getName(),
                    "al.rafam.overlapInformationModel.idErreurAnnonce.businessIntegrity.ExistingId");
        }

        // Date de chevauchement début doit être antérieure à la date de
        // chevauchement fin
        if (!JadeStringUtil.isEmpty(model.getOverlapPeriodeStart())
                && (!JadeStringUtil.isEmpty(model.getOverlapPeriodeEnd()) && (JadeDateUtil.isDateAfter(
                        model.getOverlapPeriodeStart(), model.getOverlapPeriodeEnd())))) {

            JadeThread.logError(OverlapInformationModelChecker.class.getName(),
                    "al.rafam.overlapInformationModel.overlapPeriodeStart.businessIntegrity.dateChronology");
        }
    }

    /**
     * vérification de l'intégrité des données
     * 
     * @param model
     *            Modèle à valider
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
    public static void validate(OverlapInformationModel model) throws JadeApplicationException,
            JadePersistenceException {

        OverlapInformationModelChecker.checkMandatory(model);
        OverlapInformationModelChecker.checkDatabaseIntegrity(model);
        OverlapInformationModelChecker.checkBusinessIntegrity(model);
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
    public static void validateForDelete(OverlapInformationModel model) throws JadeApplicationException,
            JadePersistenceException {
        // NOTHING TO CHECK
    }
}