package ch.globaz.al.businessimpl.checker.model.rafam;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;
import ch.globaz.al.businessimpl.checker.ALAbstractChecker;

/**
 * classe de validation des donn�es de {@link ch.globaz.al.business.models.rafam.AnnonceRafamModel}
 * 
 * @author jts
 * 
 */
public abstract class AnnonceRafam69dModelChecker extends ALAbstractChecker {

    /**
     * V�rifie l'int�grit�e "business" des donn�es
     * 
     * @param model
     *            Mod�le � valider
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected static void checkBusinessIntegrity(AnnonceRafamModel model) throws JadePersistenceException,
            JadeApplicationException {
        // DO NOTHING
    }

    /**
     * V�rification des codesSystems
     * 
     * @param model
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected static void checkCodesystemIntegrity(AnnonceRafamModel model) throws JadeApplicationException,
            JadePersistenceException {
        // DO NOTHING
    }

    /**
     * v�rification de l'int�grit� des donn�es
     * 
     * @param model
     *            Mod�le � valider
     */
    protected static void checkDatabaseIntegrity(AnnonceRafamModel model) {
        // DO NOTHING
    }

    /**
     * v�rification des donn�es requises
     * 
     * @param model
     *            Mod�le � valider
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected static void checkMandatory(AnnonceRafamModel model) throws JadeApplicationException,
            JadePersistenceException {
        // DO NOTHING
    }
}
