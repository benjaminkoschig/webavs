package ch.globaz.al.businessimpl.checker.model.allocataire;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;

/**
 * Checker du modèle <code>AllocataireComplexModel</code>
 * 
 * @author jts
 * 
 * @see ch.globaz.al.business.models.allocataire.AllocataireComplexModel
 */
public class AllocataireComplexModelChecker extends ALAbstractChecker {

    /**
     * Vérification des données obligatoires
     * 
     * @param model
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkMandatory(AllocataireComplexModel model) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
        }

        // NSS
        if (JadeStringUtil.isEmpty(model.getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel())) {
            JadeThread.logError(AllocataireModelChecker.class.getName(),
                    "al.allocataire.allocataireComplexModel.nss.mandatory");
        }
    }

    /**
     * Validation des données
     * 
     * @param model
     *            Modèle à valider
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static void validate(AllocataireComplexModel model) throws JadePersistenceException,
            JadeApplicationException {
        AllocataireComplexModelChecker.checkMandatory(model);
    }
}
