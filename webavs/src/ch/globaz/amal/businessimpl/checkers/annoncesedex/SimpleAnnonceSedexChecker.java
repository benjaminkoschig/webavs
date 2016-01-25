/**
 * 
 */
package ch.globaz.amal.businessimpl.checkers.annoncesedex;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.businessimpl.checkers.AmalAbstractChecker;

/**
 * @author dhi
 * 
 */
public class SimpleAnnonceSedexChecker extends AmalAbstractChecker {

    /**
     * Contrôle de la création d'une annonce sedex
     * 
     * @param annonceSedex
     * @throws DetailFamilleException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static void checkForCreate(SimpleAnnonceSedex annonceSedex) throws DetailFamilleException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        // Les éléments boolean doivent être renseignés
        SimpleAnnonceSedexChecker.checkMandatory(annonceSedex);
        // Le detail famille en relation doit exister + ...
        SimpleAnnonceSedexChecker.checkIntegrity(annonceSedex);
    }

    /**
     * Contrôle de la suppression d'une annonce Sedex
     * 
     * @param annonceSedex
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static void checkForDelete(SimpleAnnonceSedex annonceSedex)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException {
    }

    /**
     * Contrôle de la mise à jour d'une annonce
     * 
     * @param annonce
     * @throws DetailFamilleException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static void checkForUpdate(SimpleAnnonceSedex annonceSedex) throws DetailFamilleException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        // Les éléments boolean doivent être renseignés
        SimpleAnnonceSedexChecker.checkMandatory(annonceSedex);
        // Le detail famille en relation doit exister +...
        SimpleAnnonceSedexChecker.checkIntegrity(annonceSedex);

    }

    /**
     * L'élément annonce doit être lié à un detail famille existant et les dates de débuts doivent être avant les dates
     * de fin
     * 
     * @param annonceSedex
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DetailFamilleException
     */
    private static void checkIntegrity(SimpleAnnonceSedex annonceSedex) throws DetailFamilleException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
    }

    /**
     * Les éléments boolean doivent être renseignés
     * 
     * @param annonce
     */
    private static void checkMandatory(SimpleAnnonceSedex annonceSedex) {
    }

}
