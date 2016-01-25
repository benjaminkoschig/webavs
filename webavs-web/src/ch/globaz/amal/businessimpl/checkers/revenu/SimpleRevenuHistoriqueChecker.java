/**
 * 
 */
package ch.globaz.amal.businessimpl.checkers.revenu;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.SimpleRevenuHistorique;
import ch.globaz.amal.business.models.revenu.SimpleRevenuHistoriqueSearch;
import ch.globaz.amal.businessimpl.checkers.AmalAbstractChecker;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

/**
 * Checker for the simple revenu historique model actions
 * 
 * @author dhi
 * 
 */
public class SimpleRevenuHistoriqueChecker extends AmalAbstractChecker {

    /**
     * Contrôle pour la création d'un revenu historique
     * 
     * @param revenuHistorique
     * @throws RevenuException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static void checkForCreate(SimpleRevenuHistorique revenuHistorique) throws RevenuException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleRevenuHistoriqueChecker.checkMandatoryCreation(revenuHistorique);
        if (!AmalAbstractChecker.threadOnError()) {
            SimpleRevenuHistoriqueChecker.checkIntegrityCreation(revenuHistorique);
        }
    }

    /**
     * Contrôle pour la suppression d'un revenu historique
     * 
     * @param revenuHistorique
     */
    public static void checkForDelete(SimpleRevenuHistorique revenuHistorique) {
        JadeThread.logError(revenuHistorique.getClass().getName(), "amal.simpleRevenuHistorique.deleteForbidden");
    }

    /**
     * Contrôle pour la mise à jour d'un revenu historique
     * 
     * @param revenuHistorique
     */
    public static void checkForUpdate(SimpleRevenuHistorique revenuHistorique) {
        SimpleRevenuHistoriqueChecker.checkMandatory(revenuHistorique);
        if (!AmalAbstractChecker.threadOnError()) {
            SimpleRevenuHistoriqueChecker.checkIntegrity(revenuHistorique);
        }
    }

    /**
     * Contrôle d'intégrité générique (les enregistrements selon les ids renseignés doivent exister, contribuable, rev
     * det, taxation)
     * 
     * @param revenuHistorique
     */
    private static void checkIntegrity(SimpleRevenuHistorique revenuHistorique) {
        // TODO : définir les critères d'intégrités
    }

    /**
     * Contrôle d'intégrité à la création (pas d'enregistrement existant pour la même année)
     * 
     * @param revenuHistorique
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws RevenuException
     */
    private static void checkIntegrityCreation(SimpleRevenuHistorique revenuHistorique) throws RevenuException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // Check si un enregistrement est existant pour l'année historique renseigné
        // si oui >> erreur, il s'agit d'une mise à jour avec historisation à appliquer
        SimpleRevenuHistoriqueSearch revenuSearch = new SimpleRevenuHistoriqueSearch();
        revenuSearch.setForAnneeHistorique(revenuHistorique.getAnneeHistorique());
        revenuSearch.setForIdContribuable(revenuHistorique.getIdContribuable());
        revenuSearch = AmalImplServiceLocator.getSimpleRevenuHistoriqueService().search(revenuSearch);
        if ((revenuSearch.getSize() > 0) && !revenuHistorique.getIsRecalcul()) {
            for (int iRevenu = 0; iRevenu < revenuSearch.getSize(); iRevenu++) {
                SimpleRevenuHistorique currentRevenuHistorique = (SimpleRevenuHistorique) revenuSearch
                        .getSearchResults()[0];
                if (currentRevenuHistorique.getCodeActif()) {
                    JadeThread.logError(revenuHistorique.getClass().getName(),
                            "amal.simpleRevenuHistorique.anneeHistorique.integrity");
                    break;
                }
            }
        }
    }

    /**
     * Contrôle des champs obligatoires (année historique, id contribuable, id revenu, id revenu det )
     * 
     * @param revenuHistorique
     */
    private static void checkMandatory(SimpleRevenuHistorique revenuHistorique) {
        if (JadeStringUtil.isEmpty(revenuHistorique.getIdContribuable())) {
            JadeThread.logError(revenuHistorique.getClass().getName(),
                    "amal.simpleRevenuHistorique.idContribuable.mandatory");
        }
        if (JadeStringUtil.isEmpty(revenuHistorique.getAnneeHistorique())) {
            JadeThread.logError(revenuHistorique.getClass().getName(),
                    "amal.simpleRevenuHistorique.anneeHistorique.mandatory");
        }
        if (JadeStringUtil.isEmpty(revenuHistorique.getIdRevenu())) {
            JadeThread
                    .logError(revenuHistorique.getClass().getName(), "amal.simpleRevenuHistorique.idRevenu.mandatory");
        }
        if (JadeStringUtil.isEmpty(revenuHistorique.getIdRevenuDeterminant())) {
            JadeThread.logError(revenuHistorique.getClass().getName(),
                    "amal.simpleRevenuHistorique.idRevenuDeterminant.mandatory");
        }
    }

    /**
     * Contrôle des champs obligatoires pour la création (année historique, id contribuable )
     * 
     * @param revenuHistorique
     */
    private static void checkMandatoryCreation(SimpleRevenuHistorique revenuHistorique) {
        if (JadeStringUtil.isEmpty(revenuHistorique.getIdContribuable())) {
            JadeThread.logError(revenuHistorique.getClass().getName(),
                    "amal.simpleRevenuHistorique.idContribuable.mandatory");
        }
        if (JadeStringUtil.isEmpty(revenuHistorique.getAnneeHistorique())) {
            JadeThread.logError(revenuHistorique.getClass().getName(),
                    "amal.simpleRevenuHistorique.anneeHistorique.mandatory");
        }

    }

}
