/**
 * 
 */
package ch.globaz.al.businessimpl.checker.model.allocataire;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.allocataire.AgricoleModel;
import ch.globaz.al.business.models.allocataire.AllocataireSearchModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * classe de validation des données de AgricoleModel
 * 
 * @author PTA
 * 
 */
public abstract class AgricoleModelChecker extends ALAbstractChecker {

    /**
     * Vérifie l'intégrité métier des données
     * 
     * @param agricoleModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkBusinessIntegrity(AgricoleModel agricoleModel) throws JadeApplicationException,
            JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // on vérifie que l'id de l'allocataire fourni correspond bien à un
        // allocataire existant en base de données
        AllocataireSearchModel as = new AllocataireSearchModel();
        as.setForIdAllocataire(agricoleModel.getIdAllocataire());
        if (0 == ALImplServiceLocator.getAllocataireModelService().count(as)) {
            JadeThread.logError(AgricoleModelChecker.class.getName(),
                    "al.allocataire.agricoleModel.idAllocataire.businessIntegrity.ExistingId");
        }
    }

    /**
     * vérifie l'intégrité des données de la base de données de agricoleModel, si non respectée message sur l'intégrité
     * de la donnée
     * 
     * @param agricoleModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private static void checkDatabaseIntegrity(AgricoleModel agricoleModel) throws JadeApplicationException,
            JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // id de l'allocataire
        if (!JadeNumericUtil.isIntegerPositif(agricoleModel.getIdAllocataire())) {
            JadeThread.logError(AgricoleModelChecker.class.getName(),
                    "al.allocataire.agricoleModel.idAllocataire.databaseIntegrity.type");
        }
    }

    /**
     * vérifie l'obligation des données de agricoleModel, si non respectée message sur l'obligation de la donnée
     * 
     * @param agricoleModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkMandatory(AgricoleModel agricoleModel) throws JadeApplicationException {

        // domaine de montagne
        if (null == agricoleModel.getDomaineMontagne()) {
            JadeThread.logError(AgricoleModelChecker.class.getName(),
                    "al.allocataire.agricoleModel.domaineMontagne.mandatory");
        }

        // id de l'allocataire
        if (JadeStringUtil.isEmpty(agricoleModel.getIdAllocataire())) {
            JadeThread.logError(AgricoleModelChecker.class.getName(),
                    "al.allocataire.agricoleModel.idAllocataire.mandatory");
        }
    }

    /**
     * valide l'intégrité et l'obligation des données de AgricoleModel
     * 
     * @param agricoleModel
     *            Modèle à valider
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(AgricoleModel agricoleModel) throws JadeApplicationException, JadePersistenceException {
        AgricoleModelChecker.checkMandatory(agricoleModel);
        AgricoleModelChecker.checkDatabaseIntegrity(agricoleModel);
        AgricoleModelChecker.checkBusinessIntegrity(agricoleModel);
    }
}
