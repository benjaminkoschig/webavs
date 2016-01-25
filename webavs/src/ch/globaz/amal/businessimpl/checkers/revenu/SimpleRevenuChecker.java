/**
 * 
 */
package ch.globaz.amal.businessimpl.checkers.revenu;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.SimpleRevenu;
import ch.globaz.amal.businessimpl.checkers.AmalAbstractChecker;

/**
 * @author DHI
 * 
 */
public class SimpleRevenuChecker extends AmalAbstractChecker {
    /**
     * @param dossier
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DemandeException
     */
    public static void checkForCreate(SimpleRevenu simpleRevenu) throws RevenuException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        // SimpleRevenuChecker.checkForIntegrityGeneric(simpleRevenu);
        SimpleRevenuChecker.checkMandatoryGeneric(simpleRevenu);
        if (!AmalAbstractChecker.threadOnError()) {
            SimpleRevenuChecker.checkForIntegrityGeneric(simpleRevenu);
        }

        // SimpleRevenuChecker.checkForIntegrityContribuable(simpleRevenu);
        // SimpleRevenuChecker.checkForIntegritySourceContribuable(simpleRevenu);
    }

    /**
     * @param dossier
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DemandeException
     */
    public static void checkForDelete(SimpleRevenu simpleRevenu) throws RevenuException, JadePersistenceException {
        // nothing to test until now
    }

    /**
     * 
     * @param simpleRevenu
     * @throws RevenuException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private static void checkForIntegrityGeneric(SimpleRevenu simpleRevenu) throws RevenuException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        int anneeHistorique = -1;
        int anneeTaxation = -1;
        // Check if we got a value in AnneeHistorique
        // --------------------------------------------------------------------------
        // if (!JadeStringUtil.isBlankOrZero(simpleRevenu.getAnneeHistorique())) {
        // anneeHistorique = JadeStringUtil.parseInt(simpleRevenu.getAnneeHistorique(), -1);
        // // Check if the annee historique is not here just for fun
        // if ((anneeHistorique < 1900) || (anneeHistorique > 2100)) {
        // JadeThread.logError(simpleRevenu.getClass().getName(),
        // "amal.revenu.simpleRevenu.integrity.AnneeHistoriqueValue");
        // }
        // // Check if we already have some revenu with the same anneHistorique and with a CodeActif=true
        // // ----------------------------------------------------------------------------------------------
        // SimpleRevenuSearch search = new SimpleRevenuSearch();
        // search.setForAnneeHistorique(simpleRevenu.getAnneeHistorique());
        // search.setForIdContribuable(simpleRevenu.getIdContribuable());
        // search = AmalImplServiceLocator.getSimpleRevenuService().search(search);
        // for (java.util.Iterator<JadeAbstractModel> it = Arrays.asList(search.getSearchResults()).iterator(); it
        // .hasNext();) {
        // SimpleRevenu revenu = (SimpleRevenu) it.next();
        // if (!revenu.getIdRevenu().equals(simpleRevenu.getIdRevenu())) {
        // if (revenu.getAnneeHistorique().equals(simpleRevenu.getAnneeHistorique())) {
        // if (revenu.getCodeActif() == true) {
        // JadeThread.logError(simpleRevenu.getClass().getName(),
        // "amal.revenu.simpleRevenu.integrity.SameRevenuExistAndIsActif");
        // break;
        // }
        // }
        // }
        // }
        //
        // } else {
        // JadeThread.logError(simpleRevenu.getClass().getName(),
        // "amal.revenu.simpleRevenu.integrity.AnneeHistoriqueEmpty");
        // }

        // Check if we got a value in AnneeTaxation
        // --------------------------------------------------------------------------
        if (!JadeStringUtil.isBlankOrZero(simpleRevenu.getAnneeTaxation())) {
            anneeTaxation = JadeStringUtil.parseInt(simpleRevenu.getAnneeTaxation(), -1);
            // Check if the annee taxation is not here just for fun
            if ((anneeTaxation < 1900) || (anneeTaxation > 2100)) {
                JadeThread.logError(simpleRevenu.getClass().getName(),
                        "amal.revenu.simpleRevenu.integrity.AnneeTaxationValue");
            }
        }
        // Test désactivé (CBU 04.05.2011)
        // else {
        // JadeThread.logError(simpleRevenu.getClass().getName(),
        // "amal.revenu.simpleRevenu.integrity.AnneeTaxationEmpty");
        // }

        // TEST TO AVOID IN A FIRST TIME
        // Check if the AnneeTaxation = AnneeHistorique - 2
        // --------------------------------------------------------------------------
        /*
         * if (!JadeStringUtil.isBlankOrZero(simpleRevenu.getAnneeTaxation()) &&
         * !JadeStringUtil.isBlankOrZero(simpleRevenu.getAnneeTaxation())) { // check the -2 if (anneeTaxation + 2 !=
         * anneeHistorique) { JadeThread.logError(simpleRevenu.getClass().getName(),
         * "amal.revenu.simpleRevenu.integrity.AnneeHistoriqueMisMatch"); } }
         */

        // Check if the nb jours > 365
        // --------------------------------------------------------------------------
        if (!JadeStringUtil.isBlank(simpleRevenu.getNbJours())) {
            int nbJours = JadeStringUtil.parseInt(simpleRevenu.getNbJours(), -1);
            // Check if the nbJours is not here just for fun
            if ((nbJours < 0) || (nbJours > 367)) {
                JadeThread.logError(simpleRevenu.getClass().getName(), "amal.revenu.simpleRevenu.integrity.NbJours");
            }
        }
        // Test désactivé (CBU 04.05.2011)
        // else {
        // JadeThread.logError(simpleRevenu.getClass().getName(), "amal.revenu.simpleRevenu.integrity.NbJoursEmpty");
        // }

        // Check if the nb enfants > 9
        // --------------------------------------------------------------------------
        if (!JadeStringUtil.isBlank(simpleRevenu.getNbEnfants())) {
            int nbEnfants = JadeStringUtil.parseInt(simpleRevenu.getNbEnfants(), -1);
            // Check if the nbEnfants is not here just for fun
            if ((nbEnfants < 0) || (nbEnfants > 99)) {
                JadeThread.logError(simpleRevenu.getClass().getName(), "amal.revenu.simpleRevenu.integrity.NbEnfants");
            }
        } else {
            simpleRevenu.setNbEnfants("0");
            // Test désactivé (CBU 04.05.2011)
            // JadeThread.logError(simpleRevenu.getClass().getName(),
            // "amal.revenu.simpleRevenu.integrity.NbEnfantsEmpty");
        }

        if (!JadeStringUtil.isBlankOrZero(simpleRevenu.getDateAvisTaxation())
                && !JadeDateUtil.isGlobazDate(simpleRevenu.getDateAvisTaxation())) {
            JadeThread.logError(simpleRevenu.getClass().getName(),
                    "amal.revenu.simpleRevenu.integrity.dateAvisTaxation");
        }

        if (!JadeStringUtil.isBlankOrZero(simpleRevenu.getDateSaisie())
                && !JadeDateUtil.isGlobazDate(simpleRevenu.getDateSaisie())) {
            JadeThread.logError(simpleRevenu.getClass().getName(), "amal.revenu.simpleRevenu.integrity.dateSaisie");
        }

        if (!JadeStringUtil.isBlankOrZero(simpleRevenu.getDateTraitement())
                && !JadeDateUtil.isGlobazDate(simpleRevenu.getDateTraitement())) {
            JadeThread.logError(simpleRevenu.getClass().getName(), "amal.revenu.simpleRevenu.integrity.dateTraitement");
        }
    }

    // /**
    // *
    // * @param simpleRevenu
    // * @throws RevenuException
    // * @throws JadePersistenceException
    // * @throws JadeApplicationServiceNotAvailableException
    // */
    // private static void checkForIntegritySourceContribuable(SimpleRevenu simpleRevenu) throws RevenuException,
    // JadePersistenceException, JadeApplicationServiceNotAvailableException {
    //
    // }
    //
    // /**
    // *
    // * @param simpleRevenu
    // * @throws RevenuException
    // * @throws JadePersistenceException
    // * @throws JadeApplicationServiceNotAvailableException
    // */
    // private static void checkForIntegrityContribuable(SimpleRevenu simpleRevenu) throws RevenuException,
    // JadePersistenceException, JadeApplicationServiceNotAvailableException {
    // SimpleRevenu simpleRevenuToCheck = new SimpleRevenu();
    // simpleRevenuToCheck = AmalImplServiceLocator.getSimpleRevenuService().read(simpleRevenu.getIdRevenu());
    //
    // if (simpleRevenuToCheck.isNew()) {
    // JadeThread.logError(simpleRevenu.getClass().getName(),
    // "amal.revenu.simpleRevenu.integrity.AnneeHistoriqueValue");
    // }
    // }

    /**
     * @param dossier
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DemandeException
     */
    public static void checkForUpdate(SimpleRevenu simpleRevenu) throws RevenuException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        SimpleRevenuChecker.checkForIntegrityGeneric(simpleRevenu);
        // SimpleRevenuChecker.checkForIntegrityStandardContribuable(simpleRevenu);
        // SimpleRevenuChecker.checkForIntegritySourceContribuable(simpleRevenu);
    }

    private static void checkMandatoryGeneric(SimpleRevenu simpleRevenu) {
        if (JadeStringUtil.isBlank(simpleRevenu.getTypeRevenu())) {
            JadeThread.logError(simpleRevenu.getClass().getName(), "amal.revenu.typeRevenu.mandatory");
        }

        if (simpleRevenu.isSourcier() == null) {
            JadeThread.logError(simpleRevenu.getClass().getName(), "amal.revenu.isSourcier.mandatory");
        }

        if (simpleRevenu.getRevDetUniqueOuiNon() == null) {
            JadeThread.logError(simpleRevenu.getClass().getName(), "amal.revenu.revDetUniqueOuiNon.mandatory");
        }
    }
}
