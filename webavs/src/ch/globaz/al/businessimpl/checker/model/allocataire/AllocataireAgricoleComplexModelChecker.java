package ch.globaz.al.businessimpl.checker.model.allocataire;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.allocataire.AllocataireAgricoleComplexModel;

/**
 * Checker du modèle <code>AllocataireAgricoleComplexModel</code>
 * 
 * @author jts
 * 
 * @see ch.globaz.al.business.models.allocataire.AllocataireAgricoleComplexModel
 */
public class AllocataireAgricoleComplexModelChecker extends AllocataireComplexModelChecker {

    /**
     * Validation des données
     * 
     * @param model
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static void validate(AllocataireAgricoleComplexModel model) throws JadePersistenceException,
            JadeApplicationException {
        AllocataireComplexModelChecker.validate(model);
    }
}
