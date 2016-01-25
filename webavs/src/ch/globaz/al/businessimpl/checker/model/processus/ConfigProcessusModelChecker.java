package ch.globaz.al.businessimpl.checker.model.processus;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.exceptions.model.allocataire.ALAllocataireModelException;
import ch.globaz.al.business.models.processus.ConfigProcessusModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;

/**
 * 
 * Classe de validation des données de ProcessusPeriodiqueModel
 * 
 * @author GMO
 * 
 */
public abstract class ConfigProcessusModelChecker extends ALAbstractChecker {
    /**
     * vérifie l'intégrité des données relatives aux codes systèmes , si non respectée lance un message sur l'intégrité
     * de(s) donnée(s)
     * 
     * @param configProcessusModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkCodesystemIntegrity(ConfigProcessusModel configProcessusModel)
            throws JadePersistenceException, JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        // processus métier
        try {
            if (!JadeNumericUtil.isEmptyOrZero(configProcessusModel.getBusinessProcessus())
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSProcessus.GROUP_NAME_PROCESSUS_TRAITEMENT,
                            configProcessusModel.getBusinessProcessus())) {
                JadeThread.logError(ConfigProcessusModelChecker.class.getName(),
                        "al.processus.configProcessusModel.businessProcessus.codesystemIntegrity");
            }

            if (!JadeNumericUtil.isEmptyOrZero(configProcessusModel.getTemplate())
                    && !JadeCodesSystemsUtil.checkCodeSystemType(ALCSProcessus.GROUP_NAME_TEMPLATE_CONFIG,
                            configProcessusModel.getTemplate())) {
                JadeThread.logError(ConfigProcessusModelChecker.class.getName(),
                        "al.processus.configProcessusModel.template.codesystemIntegrity");
            }

        } catch (JadeNoBusinessLogSessionError e) {
            throw new ALAllocataireModelException(
                    "ConfigProcessusModelChecker#checkCodesystemIntegrity : unable to check codes system", e);
        } catch (Exception e) {
            throw new ALAllocataireModelException(
                    "ConfigProcessusModelChecker#checkCodesystemIntegrity : unable to check codes system", e);
        }

    }

    /**
     * vérifie l'intégrité des données relatives à la base de données , si non respectée lance un message sur
     * l'intégrité de(s) donnée(s)
     * 
     * @param configProcessusModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDatabaseIntegrity(ConfigProcessusModel configProcessusModel)
            throws JadePersistenceException {

        if (!JadeNumericUtil.isIntegerPositif(configProcessusModel.getBusinessProcessus())) {
            JadeThread.logError(ConfigProcessusModelChecker.class.getName(),
                    "al.processus.configProcessusModel.businessProcessus.databaseIntegrity.type");
        }

        if (!JadeNumericUtil.isIntegerPositif(configProcessusModel.getTemplate())) {
            JadeThread.logError(ConfigProcessusModelChecker.class.getName(),
                    "al.processus.configProcessusModel.template.databaseIntegrity.type");
        }

    }

    /**
     * vérifie l'obligation des données, si non respectée lance un message sur l'obligation de(s) donnée(s)
     * 
     * @param configProcessusModel
     *            Modèle à valider
     */
    private static void checkMandatory(ConfigProcessusModel configProcessusModel) {
        // code du processus métier obligatoire
        if (JadeStringUtil.isEmpty(configProcessusModel.getBusinessProcessus())) {
            JadeThread.logError(ConfigProcessusModelChecker.class.getName(),
                    "al.processus.configProcessusModel.businessProcessus.mandatory");
        }

        // code du template obligatoire
        if (JadeStringUtil.isEmpty(configProcessusModel.getTemplate())) {
            JadeThread.logError(ConfigProcessusModelChecker.class.getName(),
                    "al.processus.configProcessusModel.template.mandatory");
        }
    }

    /**
     * validation de l'intégrité des données et des règles métier, de l'obligation des données et de l'intégrité des
     * codes systèmes
     * 
     * @param configProcessusModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static void validate(ConfigProcessusModel configProcessusModel) throws JadePersistenceException,
            JadeApplicationException {
        ConfigProcessusModelChecker.checkMandatory(configProcessusModel);
        ConfigProcessusModelChecker.checkDatabaseIntegrity(configProcessusModel);
        ConfigProcessusModelChecker.checkCodesystemIntegrity(configProcessusModel);
    }
}
