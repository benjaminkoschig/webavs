package ch.globaz.al.businessimpl.checker.model.adi;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.adi.AdiSaisieComplexModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;

/**
 * Checker du mod�le <code>AdiSaisieComplexModel</code>
 * 
 * @author gmo
 * 
 * @see ch.globaz.al.business.models.adi.AdiSaisieComplexModel
 */
public class AdiSaisieComplexModelChecker extends ALAbstractChecker {
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
    private static void checkBusinessIntegrity(AdiSaisieComplexModel model) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
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
    public static void validate(AdiSaisieComplexModel model) throws JadePersistenceException, JadeApplicationException {
        AdiSaisieComplexModelChecker.checkBusinessIntegrity(model);
    }

}
