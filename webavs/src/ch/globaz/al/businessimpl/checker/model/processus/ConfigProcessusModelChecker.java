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
 * Classe de validation des donn�es de ProcessusPeriodiqueModel
 * 
 * @author GMO
 * 
 */
public abstract class ConfigProcessusModelChecker extends ALAbstractChecker {
    /**
     * v�rifie l'int�grit� des donn�es relatives aux codes syst�mes , si non respect�e lance un message sur l'int�grit�
     * de(s) donn�e(s)
     * 
     * @param configProcessusModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkCodesystemIntegrity(ConfigProcessusModel configProcessusModel)
            throws JadePersistenceException, JadeApplicationException {
        if (ALAbstractChecker.hasError()) {
            return;
        }

        // processus m�tier
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
     * v�rifie l'int�grit� des donn�es relatives � la base de donn�es , si non respect�e lance un message sur
     * l'int�grit� de(s) donn�e(s)
     * 
     * @param configProcessusModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
     * v�rifie l'obligation des donn�es, si non respect�e lance un message sur l'obligation de(s) donn�e(s)
     * 
     * @param configProcessusModel
     *            Mod�le � valider
     */
    private static void checkMandatory(ConfigProcessusModel configProcessusModel) {
        // code du processus m�tier obligatoire
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
     * validation de l'int�grit� des donn�es et des r�gles m�tier, de l'obligation des donn�es et de l'int�grit� des
     * codes syst�mes
     * 
     * @param configProcessusModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static void validate(ConfigProcessusModel configProcessusModel) throws JadePersistenceException,
            JadeApplicationException {
        ConfigProcessusModelChecker.checkMandatory(configProcessusModel);
        ConfigProcessusModelChecker.checkDatabaseIntegrity(configProcessusModel);
        ConfigProcessusModelChecker.checkCodesystemIntegrity(configProcessusModel);
    }
}
