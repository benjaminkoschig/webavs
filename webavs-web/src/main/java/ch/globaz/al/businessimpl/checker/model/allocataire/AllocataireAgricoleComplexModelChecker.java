package ch.globaz.al.businessimpl.checker.model.allocataire;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.allocataire.AllocataireAgricoleComplexModel;

/**
 * Checker du mod�le <code>AllocataireAgricoleComplexModel</code>
 * 
 * @author jts
 * 
 * @see ch.globaz.al.business.models.allocataire.AllocataireAgricoleComplexModel
 */
public class AllocataireAgricoleComplexModelChecker extends AllocataireComplexModelChecker {

    /**
     * Validation des donn�es
     * 
     * @param model
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static void validate(AllocataireAgricoleComplexModel model) throws JadePersistenceException,
            JadeApplicationException {
        AllocataireComplexModelChecker.validate(model);
    }
}
