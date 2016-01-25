package ch.globaz.musca.businessimpl.checkers.module;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.musca.business.models.ModuleModel;
import ch.globaz.musca.businessimpl.checkers.FAAbstractChecker;

public class ModuleModelChecker extends FAAbstractChecker {
    /**
     * V�rifie l'int�grit�e "business" des donn�es
     * 
     * @param moduleModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkBusinessIntegrity(ModuleModel moduleModel) throws JadePersistenceException,
            JadeApplicationException {

        if (FAAbstractChecker.hasError()) {
            return;
        }
        // TODO: implement business rules when create / update service will be implemented

    }

    /**
     * V�rification des codesSystems
     * 
     * @param moduleModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
     * v�rification de l'int�grit� des donn�es
     * 
     * @param moduleModel
     *            Mod�le � valider
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
     * V�rification de l'int�grit� m�tier avant suppression
     * 
     * @param passageModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
     * v�rification des donn�es requises
     * 
     * @param moduleModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
     * validation des donn�es de moduleModel
     * 
     * @param moduleModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(ModuleModel moduleModel) throws JadeApplicationException, JadePersistenceException {
        ModuleModelChecker.checkMandatory(moduleModel);
        ModuleModelChecker.checkDatabaseIntegrity(moduleModel);
        ModuleModelChecker.checkCodesystemIntegrity(moduleModel);
        ModuleModelChecker.checkBusinessIntegrity(moduleModel);
    }

    /**
     * Validation de l'int�grit� des donn�es avant suppression
     * 
     * @param moduleModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validateForDelete(ModuleModel moduleModel) throws JadeApplicationException,
            JadePersistenceException {
        ModuleModelChecker.checkDeleteIntegrity(moduleModel);
    }

}
