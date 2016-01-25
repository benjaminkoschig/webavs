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
 * classe de validation des donn�es de AgricoleModel
 * 
 * @author PTA
 * 
 */
public abstract class AgricoleModelChecker extends ALAbstractChecker {

    /**
     * V�rifie l'int�grit� m�tier des donn�es
     * 
     * @param agricoleModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkBusinessIntegrity(AgricoleModel agricoleModel) throws JadeApplicationException,
            JadePersistenceException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // on v�rifie que l'id de l'allocataire fourni correspond bien � un
        // allocataire existant en base de donn�es
        AllocataireSearchModel as = new AllocataireSearchModel();
        as.setForIdAllocataire(agricoleModel.getIdAllocataire());
        if (0 == ALImplServiceLocator.getAllocataireModelService().count(as)) {
            JadeThread.logError(AgricoleModelChecker.class.getName(),
                    "al.allocataire.agricoleModel.idAllocataire.businessIntegrity.ExistingId");
        }
    }

    /**
     * v�rifie l'int�grit� des donn�es de la base de donn�es de agricoleModel, si non respect�e message sur l'int�grit�
     * de la donn�e
     * 
     * @param agricoleModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
     * v�rifie l'obligation des donn�es de agricoleModel, si non respect�e message sur l'obligation de la donn�e
     * 
     * @param agricoleModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
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
     * valide l'int�grit� et l'obligation des donn�es de AgricoleModel
     * 
     * @param agricoleModel
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public static void validate(AgricoleModel agricoleModel) throws JadeApplicationException, JadePersistenceException {
        AgricoleModelChecker.checkMandatory(agricoleModel);
        AgricoleModelChecker.checkDatabaseIntegrity(agricoleModel);
        AgricoleModelChecker.checkBusinessIntegrity(agricoleModel);
    }
}
