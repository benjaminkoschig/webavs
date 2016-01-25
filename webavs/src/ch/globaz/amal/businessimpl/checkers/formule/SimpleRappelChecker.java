/**
 * 
 */
package ch.globaz.amal.businessimpl.checkers.formule;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.exceptions.models.formule.RappelException;
import ch.globaz.amal.businessimpl.checkers.AmalAbstractChecker;
import ch.globaz.envoi.business.models.parametrageEnvoi.SimpleRappel;

/**
 * @author LFO
 * 
 */
public class SimpleRappelChecker extends AmalAbstractChecker {

    private static void checkForIntegrityGeneric(SimpleRappel simpleRappel) throws RappelException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {

        /*
         * int anneeHistorique = -1; // check if Annee Historique is not empty //
         * ---------------------------------------------------------- if
         * (!JadeStringUtil.isBlankOrZero(simpleRappel.getIdDefinitionFormule())) { anneeHistorique =
         * JadeStringUtil.parseInt(detailFamille.getAnneeHistorique(), -1); // Check if the annee historique is not here
         * just for fun if ((anneeHistorique < 1900) || (anneeHistorique > 2100)) {
         * JadeThread.logError(detailFamille.getClass().getName(),
         * "amal.detailFamille.simpleDetailFamille.integrity.AnneeHistoriqueValue"); } } else {
         * JadeThread.logError(detailFamille.getClass().getName(),
         * "amal.detailFamille.simpleDetailFamille.integrity.AnneeHistoriqueEmpty"); }
         */

    }

    /**
     * 
     * @param detailFamille
     * @throws FamilleException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static void checkForUpdate(SimpleRappel simpleRappel) throws RappelException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        SimpleRappelChecker.checkForIntegrityGeneric(simpleRappel);
    }

}
