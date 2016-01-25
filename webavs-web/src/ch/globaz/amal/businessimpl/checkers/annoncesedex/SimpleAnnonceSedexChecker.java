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
     * Contr�le de la cr�ation d'une annonce sedex
     * 
     * @param annonceSedex
     * @throws DetailFamilleException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static void checkForCreate(SimpleAnnonceSedex annonceSedex) throws DetailFamilleException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        // Les �l�ments boolean doivent �tre renseign�s
        SimpleAnnonceSedexChecker.checkMandatory(annonceSedex);
        // Le detail famille en relation doit exister + ...
        SimpleAnnonceSedexChecker.checkIntegrity(annonceSedex);
    }

    /**
     * Contr�le de la suppression d'une annonce Sedex
     * 
     * @param annonceSedex
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static void checkForDelete(SimpleAnnonceSedex annonceSedex)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException {
    }

    /**
     * Contr�le de la mise � jour d'une annonce
     * 
     * @param annonce
     * @throws DetailFamilleException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static void checkForUpdate(SimpleAnnonceSedex annonceSedex) throws DetailFamilleException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        // Les �l�ments boolean doivent �tre renseign�s
        SimpleAnnonceSedexChecker.checkMandatory(annonceSedex);
        // Le detail famille en relation doit exister +...
        SimpleAnnonceSedexChecker.checkIntegrity(annonceSedex);

    }

    /**
     * L'�l�ment annonce doit �tre li� � un detail famille existant et les dates de d�buts doivent �tre avant les dates
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
     * Les �l�ments boolean doivent �tre renseign�s
     * 
     * @param annonce
     */
    private static void checkMandatory(SimpleAnnonceSedex annonceSedex) {
    }

}
