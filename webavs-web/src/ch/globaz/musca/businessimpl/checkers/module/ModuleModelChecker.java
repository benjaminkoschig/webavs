package ch.globaz.musca.businessimpl.checkers.module;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.musca.business.models.ModuleModel;
import ch.globaz.musca.businessimpl.checkers.FAAbstractChecker;

public class ModuleModelChecker extends FAAbstractChecker {
    /**
     * Vérifie l'intégritée "business" des données
     * 
     * @param moduleModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkBusinessIntegrity(ModuleModel moduleModel) throws JadePersistenceException,
            JadeApplicationException {

        if (FAAbstractChecker.hasError()) {
            return;
        }
        // TODO: implement business rules when create / update service will be implemented

    }

    /**
     * Vérification des codesSystems
     * 
     * @param moduleModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkCodesystemIntegrity(ModuleModel moduleModel) throws JadeApplicationException,
            JadePersistenceException {
        if (FAAbstractChecker.hasError()) {
            return;
        }

        // TODO: implement business when create / update services will be implemented

    }

    /**
     * vérification de l'intégrité des données
     * 
     * @param moduleModel
     *            Modèle à valider
     */
    private static void checkDatabaseIntegrity(ModuleModel moduleModel) {

        if (FAAbstractChecker.hasError()) {
            return;
        }
        /*
         * if (!JadeDateUtil.isGlobazDate(moduleModel.getDateFacturation())) {
         * JadeThread.logError(PassageModelChecker.class.getName(),
         * "fa.passage.passageModel.dateFacturation.databaseIntegrity.dateFormat");
         * }
         * 
         * 
         * if (!JadeNumericUtil.isIntegerPositif(passageModel.getStatus())) {
         * JadeThread.logError(PassageModelChecker.class.getName(),
         * "fa.passage.passageModel.status.databaseIntegrity.type");
         * }
         */

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
    private static void checkDeleteIntegrity(ModuleModel moduleModel) throws JadePersistenceException,
            JadeApplicationException {
        if (FAAbstractChecker.hasError()) {
            return;
        }
        // TODO implement business delete rules when delete service will be implement

    }

    /**
     * vérification des données requises
     * 
     * @param moduleModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkMandatory(ModuleModel moduleModel) throws JadeApplicationException,
            JadePersistenceException {

        // date de facturation
        /*
         * if (JadeStringUtil.isEmpty(passageModel.getDateFacturation())) {
         * JadeThread.logError(PassageModelChecker.class.getName(),
         * "fa.passage.passageModel.dateFacturation.mandatory");
         * 
         * }
         */

    }

    /**
     * validation des données de moduleModel
     * 
     * @param moduleModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(ModuleModel moduleModel) throws JadeApplicationException, JadePersistenceException {
        ModuleModelChecker.checkMandatory(moduleModel);
        ModuleModelChecker.checkDatabaseIntegrity(moduleModel);
        ModuleModelChecker.checkCodesystemIntegrity(moduleModel);
        ModuleModelChecker.checkBusinessIntegrity(moduleModel);
    }

    /**
     * Validation de l'intégrité des données avant suppression
     * 
     * @param moduleModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validateForDelete(ModuleModel moduleModel) throws JadeApplicationException,
            JadePersistenceException {
        ModuleModelChecker.checkDeleteIntegrity(moduleModel);
    }

}
