package ch.globaz.al.businessimpl.checker.model.allocataire;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;

/**
 * Checker du mod�le <code>AllocataireComplexModel</code>
 * 
 * @author jts
 * 
 * @see ch.globaz.al.business.models.allocataire.AllocataireComplexModel
 */
public class AllocataireComplexModelChecker extends ALAbstractChecker {

    /**
     * V�rification des donn�es obligatoires
     * 
     * @param model
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
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
     * Validation des donn�es
     * 
     * @param model
     *            Mod�le � valider
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static void validate(AllocataireComplexModel model) throws JadePersistenceException,
            JadeApplicationException {
        AllocataireComplexModelChecker.checkMandatory(model);
    }
}
