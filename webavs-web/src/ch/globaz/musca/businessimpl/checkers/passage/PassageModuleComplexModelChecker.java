package ch.globaz.musca.businessimpl.checkers.passage;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.musca.business.models.PassageModuleComplexModel;
import ch.globaz.musca.businessimpl.checkers.FAAbstractChecker;

public class PassageModuleComplexModelChecker extends FAAbstractChecker {
    /**
     * Vérifie l'intégritée "business" des données
     * 
     * @param passageModuleComplexModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private static void checkBusinessIntegrity(PassageModuleComplexModel passageModuleComplexModel)
            throws JadePersistenceException, JadeApplicationException {

        if (FAAbstractChecker.hasError()) {
            return;
        }

        // TODO: implement business rules when create / update service will be implemented

    }

    /**
     * Validation des données
     * 
     * @param passageModuleComplexModel
     *            Modèle à valider
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static void validate(PassageModuleComplexModel passageModuleComplexModel) throws JadeApplicationException,
            JadePersistenceException {
        PassageModuleComplexModelChecker.checkBusinessIntegrity(passageModuleComplexModel);
    }
}
