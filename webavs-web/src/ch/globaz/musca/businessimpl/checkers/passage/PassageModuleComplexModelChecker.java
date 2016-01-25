package ch.globaz.musca.businessimpl.checkers.passage;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.musca.business.models.PassageModuleComplexModel;
import ch.globaz.musca.businessimpl.checkers.FAAbstractChecker;

public class PassageModuleComplexModelChecker extends FAAbstractChecker {
    /**
     * V�rifie l'int�grit�e "business" des donn�es
     * 
     * @param passageModuleComplexModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private static void checkBusinessIntegrity(PassageModuleComplexModel passageModuleComplexModel)
            throws JadePersistenceException, JadeApplicationException {

        if (FAAbstractChecker.hasError()) {
            return;
        }

        // TODO: implement business rules when create / update service will be implemented

    }

    /**
     * Validation des donn�es
     * 
     * @param passageModuleComplexModel
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public static void validate(PassageModuleComplexModel passageModuleComplexModel) throws JadeApplicationException,
            JadePersistenceException {
        PassageModuleComplexModelChecker.checkBusinessIntegrity(passageModuleComplexModel);
    }
}
