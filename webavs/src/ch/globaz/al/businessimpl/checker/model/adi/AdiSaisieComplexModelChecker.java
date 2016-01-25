package ch.globaz.al.businessimpl.checker.model.adi;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.adi.AdiSaisieComplexModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;

/**
 * Checker du modèle <code>AdiSaisieComplexModel</code>
 * 
 * @author gmo
 * 
 * @see ch.globaz.al.business.models.adi.AdiSaisieComplexModel
 */
public class AdiSaisieComplexModelChecker extends ALAbstractChecker {
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
    private static void checkBusinessIntegrity(AdiSaisieComplexModel model) throws JadePersistenceException,
            JadeApplicationException {

        if (ALAbstractChecker.hasError()) {
            return;
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
    public static void validate(AdiSaisieComplexModel model) throws JadePersistenceException, JadeApplicationException {
        AdiSaisieComplexModelChecker.checkBusinessIntegrity(model);
    }

}
