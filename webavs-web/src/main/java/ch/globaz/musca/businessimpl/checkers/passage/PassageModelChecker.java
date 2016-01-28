package ch.globaz.musca.businessimpl.checkers.passage;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.exceptions.model.droit.ALDroitModelException;
import ch.globaz.musca.business.constantes.FACSPassage;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.musca.businessimpl.checkers.FAAbstractChecker;

public class PassageModelChecker extends FAAbstractChecker {

    /**
     * Vérifie l'intégritée "business" des données
     * 
     * @param passageModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkBusinessIntegrity(PassageModel passageModel) throws JadePersistenceException,
            JadeApplicationException {

        if (FAAbstractChecker.hasError()) {
            return;
        }
        // si le passage n'est pas de type externe, on doit renseigner la date de la période
        if (!FACSPassage.TYPE_FACTU_EXTERNE.equals(passageModel.getTypeFacturation())
                && JadeStringUtil.isBlankOrZero(passageModel.getDatePeriode())) {
            JadeThread.logError(PassageModelChecker.class.getName(),
                    "fa.passage.passageModel.typeFacturation.businessIntegrity.mandatoryIfNoExterne");
        }

        // si le passage est de type externe, on ne doit pas renseigner la date de la période
        if (FACSPassage.TYPE_FACTU_EXTERNE.equals(passageModel.getTypeFacturation())
                && !JadeStringUtil.isBlankOrZero(passageModel.getDatePeriode())) {
            JadeThread.logError(PassageModelChecker.class.getName(),
                    "fa.passage.passageModel.typeFacturation.businessIntegrity.noPeriodeIfExterne");
        }

    }

    /**
     * Vérification des codesSystems
     * 
     * @param passageModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkCodesystemIntegrity(PassageModel passageModel) throws JadeApplicationException,
            JadePersistenceException {
        if (FAAbstractChecker.hasError()) {
            return;
        }

        try {

            // type de facturation
            if (!JadeCodesSystemsUtil.checkCodeSystemType(FACSPassage.GROUP_TYPE_FACTU,
                    passageModel.getTypeFacturation())) {
                JadeThread.logError(PassageModelChecker.class.getName(),
                        "fa.passage.passageModel.typeFacturation.codesystemIntegrity");
            }
            // status du passage
            if (!JadeCodesSystemsUtil.checkCodeSystemType(FACSPassage.GROUP_STATUS, passageModel.getStatus())) {
                JadeThread.logError(PassageModelChecker.class.getName(),
                        "fa.passage.passageModel.status.codesystemIntegrity");
            }

        } catch (Exception e) {
            throw new ALDroitModelException("PassageModelChecker problem during checking codes system integrity", e);
        }

    }

    /**
     * vérification de l'intégrité des données
     * 
     * @param passageModel
     *            Modèle à valider
     */
    private static void checkDatabaseIntegrity(PassageModel passageModel) {

        if (FAAbstractChecker.hasError()) {
            return;
        }

        if (!JadeDateUtil.isGlobazDate(passageModel.getDateFacturation())) {
            JadeThread.logError(PassageModelChecker.class.getName(),
                    "fa.passage.passageModel.dateFacturation.databaseIntegrity.dateFormat");
        }

        if (!JadeDateUtil.isGlobazDate(passageModel.getDatePeriode())) {
            JadeThread.logError(PassageModelChecker.class.getName(),
                    "fa.passage.passageModel.datePeriode.databaseIntegrity.dateFormat");
        }

        passageModel.getLibellePassage();

        if (!JadeNumericUtil.isIntegerPositif(passageModel.getStatus())) {
            JadeThread.logError(PassageModelChecker.class.getName(),
                    "fa.passage.passageModel.status.databaseIntegrity.type");
        }

        if (!JadeNumericUtil.isIntegerPositif(passageModel.getTypeFacturation())) {
            JadeThread.logError(PassageModelChecker.class.getName(),
                    "fa.passage.passageModel.typeFacturation.databaseIntegrity.type");
        }
    }

    /**
     * Vérification de l'intégrité métier avant suppression
     * 
     * @param passageModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDeleteIntegrity(PassageModel passageModel) throws JadePersistenceException,
            JadeApplicationException {
        if (FAAbstractChecker.hasError()) {
            return;
        }
        // on ne peut pas supprimer un passage
        JadeThread.logError(PassageModelChecker.class.getName(),
                "fa.passage.passageModel.idPassage.deleteIntegrity.noDelete");

    }

    /**
     * vérification des données requises
     * 
     * @param passageModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkMandatory(PassageModel passageModel) throws JadeApplicationException,
            JadePersistenceException {

        // date de facturation
        if (JadeStringUtil.isEmpty(passageModel.getDateFacturation())) {
            JadeThread.logError(PassageModelChecker.class.getName(),
                    "fa.passage.passageModel.dateFacturation.mandatory");

        }
        // libelle passage
        if (JadeStringUtil.isEmpty(passageModel.getLibellePassage())) {
            JadeThread
                    .logError(PassageModelChecker.class.getName(), "fa.passage.passageModel.libellePassage.mandatory");

        }
        // status
        if (JadeStringUtil.isEmpty(passageModel.getStatus())) {
            JadeThread.logError(PassageModelChecker.class.getName(), "fa.passage.passageModel.status.mandatory");

        }
        // type de facturation
        if (JadeStringUtil.isEmpty(passageModel.getTypeFacturation())) {
            JadeThread.logError(PassageModelChecker.class.getName(),
                    "fa.passage.passageModel.typeFacturation.mandatory");

        }

    }

    /**
     * validation des données de passageModel
     * 
     * @param passageModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(PassageModel passageModel) throws JadeApplicationException, JadePersistenceException {
        PassageModelChecker.checkMandatory(passageModel);
        PassageModelChecker.checkDatabaseIntegrity(passageModel);
        PassageModelChecker.checkCodesystemIntegrity(passageModel);
        PassageModelChecker.checkBusinessIntegrity(passageModel);
    }

    /**
     * Validation de l'intégrité des données avant suppression
     * 
     * @param passageModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validateForDelete(PassageModel passageModel) throws JadeApplicationException,
            JadePersistenceException {
        PassageModelChecker.checkDeleteIntegrity(passageModel);
    }

}
