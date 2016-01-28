/**
 * 
 */
package ch.globaz.amal.businessimpl.checkers.detailfamille;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Arrays;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.exceptions.models.documents.DocumentException;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;
import ch.globaz.amal.business.models.documents.SimpleDocumentSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.checkers.AmalAbstractChecker;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

/**
 * @author DHI
 * 
 */
public class SimpleDetailFamilleChecker extends AmalAbstractChecker {

    /**
     * 
     * @param detailFamille
     * @throws FamilleException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static void checkForCreate(SimpleDetailFamille detailFamille) throws DetailFamilleException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        SimpleDetailFamilleChecker.checkForIntegrityGeneric(detailFamille);
    }

    /**
     * 
     * @param detailFamille
     * @throws FamilleException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DocumentException
     */
    public static void checkForDelete(SimpleDetailFamille detailFamille) throws DetailFamilleException,
            JadePersistenceException, DocumentException, JadeApplicationServiceNotAvailableException {
        SimpleDetailFamilleChecker.checkForHistoriqueEnvois(detailFamille);
        SimpleDetailFamilleChecker.checkForHistoriqueAnnonces(detailFamille);
    }

    /**
     * 
     * @param detailFamille
     * @throws FamilleException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DetailFamilleException
     */
    private static void checkForHistoriqueAnnonces(SimpleDetailFamille detailFamille) throws DetailFamilleException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        // nothing to do right now

    }

    /**
     * 
     * @param detailFamille
     * @throws FamilleException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DetailFamilleException
     * @throws DocumentException
     */
    private static void checkForHistoriqueEnvois(SimpleDetailFamille detailFamille) throws DetailFamilleException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException, DocumentException {

        // check if we sent some documents until now
        // -----------------------------------------------------------------
        SimpleDocumentSearch search = new SimpleDocumentSearch();
        search.setForIdDetailFamille(detailFamille.getIdDetailFamille());
        search = AmalServiceLocator.getDetailFamilleService().search(search);
        if (search.getSearchResults().length > 0) {
            JadeThread.logError(detailFamille.getClass().getName(),
                    "amal.detailFamille.simpleDetailFamille.integrity.HistoriqueEnvoisNotEmpty");
        }

    }

    /**
     * 
     * @param detailFamille
     * @throws FamilleException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DetailFamilleException
     */
    private static void checkForIntegrityGeneric(SimpleDetailFamille detailFamille) throws DetailFamilleException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {

        int anneeHistorique = -1;
        // check if Annee Historique is not empty
        // ----------------------------------------------------------
        if (!JadeStringUtil.isBlankOrZero(detailFamille.getAnneeHistorique())) {
            anneeHistorique = JadeStringUtil.parseInt(detailFamille.getAnneeHistorique(), -1);
            // Check if the annee historique is not here just for fun
            if ((anneeHistorique < 1900) || (anneeHistorique > 2100)) {
                JadeThread.logError(detailFamille.getClass().getName(),
                        "amal.detailFamille.simpleDetailFamille.integrity.AnneeHistoriqueValue");
            }
        } else {
            JadeThread.logError(detailFamille.getClass().getName(),
                    "amal.detailFamille.simpleDetailFamille.integrity.AnneeHistoriqueEmpty");
        }

        // Check if debut droit is not empty
        // ----------------------------------------------------------
        if (JadeStringUtil.isBlankOrZero(detailFamille.getDebutDroit())) {
            JadeThread.logError(detailFamille.getClass().getName(),
                    "amal.detailFamille.simpleDetailFamille.integrity.DebutDroitEmpty");
        } else {
            // check if debut droit is < fin droit
            // ----------------------------------------------------------
            if (!JadeStringUtil.isBlankOrZero(detailFamille.getFinDroit())
                    && !detailFamille.getDebutDroit().equals(detailFamille.getFinDroit())) {
                if (!JadeDateUtil.isDateMonthYearBefore(detailFamille.getDebutDroit(), detailFamille.getFinDroit())) {
                    JadeThread.logError(detailFamille.getClass().getName(),
                            "amal.detailFamille.simpleDetailFamille.integrity.DebutDroitAfterFinDroit");
                }
            }
        }

        // check if we don't have a similar subside for the same start date
        // -----------------------------------------------------------------
        if (detailFamille.getCodeActif()) {
            SimpleDetailFamilleSearch search = new SimpleDetailFamilleSearch();
            search.setForIdFamille(detailFamille.getIdFamille());
            search.setForCodeActif(true);
            search = AmalImplServiceLocator.getSimpleDetailFamilleService().search(search);
            for (java.util.Iterator<JadeAbstractModel> it = Arrays.asList(search.getSearchResults()).iterator(); it
                    .hasNext();) {
                SimpleDetailFamille detail = (SimpleDetailFamille) it.next();

                if (!detail.getIdDetailFamille().equals(detailFamille.getIdDetailFamille())) {
                    if (detail.getDebutDroit().equals(detailFamille.getDebutDroit())) {
                        JadeThread.logError(detailFamille.getClass().getName(),
                                "amal.detailFamille.simpleDetailFamille.integrity.DebutDroitExist");
                    }
                }
            }
        }
    }

    /**
     * 
     * @param detailFamille
     * @throws FamilleException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static void checkForUpdate(SimpleDetailFamille detailFamille) throws DetailFamilleException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        SimpleDetailFamilleChecker.checkForIntegrityGeneric(detailFamille);
    }
}
