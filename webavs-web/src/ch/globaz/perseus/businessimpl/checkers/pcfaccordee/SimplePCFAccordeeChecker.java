package ch.globaz.perseus.businessimpl.checkers.pcfaccordee;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.exceptions.models.pcfaccordee.PCFAccordeeException;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.pcfaccordee.SimplePCFAccordee;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;

/**
 * @author vyj
 */
public class SimplePCFAccordeeChecker extends PerseusAbstractChecker {

    /**
     * @param simplePCFAccordee
     */
    public static void checkForCreate(SimplePCFAccordee simplePCFAccordee) {
        SimplePCFAccordeeChecker.checkMandatory(simplePCFAccordee);
    }

    /**
     * @param simplePCFAccordee
     */
    public static void checkForDelete(SimplePCFAccordee simplePCFAccordee) throws JadePersistenceException,
            PCFAccordeeException {
        // Check qu'il n'existe pas de décision
        DecisionSearchModel decisionSearchModel = new DecisionSearchModel();
        decisionSearchModel.setForIdDemande(simplePCFAccordee.getIdDemande());
        try {
            if (PerseusServiceLocator.getDecisionService().count(decisionSearchModel) > 0) {
                JadeThread.logError(SimplePCFAccordeeChecker.class.getName(), "perseus.pcfaccordee.decisions.existe");
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PCFAccordeeException("Unable to check demande, service not available", e);
        } catch (DecisionException e) {
            throw new PCFAccordeeException("DecisionException exception during demande check : " + e.toString(), e);
        }
    }

    /**
     * @param simplePCFAccordee
     */
    public static void checkForUpdate(SimplePCFAccordee simplePCFAccordee) {
        SimplePCFAccordeeChecker.checkMandatory(simplePCFAccordee);
    }

    /**
     * @param simplePCFAccordee
     */
    private static void checkMandatory(SimplePCFAccordee simplePCFAccordee) {
        if (JadeStringUtil.isEmpty(simplePCFAccordee.getIdDemande())) {
            JadeThread.logError(SimplePCFAccordeeChecker.class.getName(),
                    "perseus.pcfaccordee.simplepcfaccordee.idDemande.mandatory");
        }
        if (JadeStringUtil.isEmpty(simplePCFAccordee.getMontant())) {
            JadeThread.logError(SimplePCFAccordeeChecker.class.getName(),
                    "perseus.pcfaccordee.simplepcfaccordee.montant.mandatory");
        }
        if (JadeStringUtil.isEmpty(simplePCFAccordee.getExcedantRevenu())) {
            JadeThread.logError(SimplePCFAccordeeChecker.class.getName(),
                    "perseus.pcfaccordee.simplepcfaccordee.excedantrevenu.mandatory");
        }
        if (JadeStringUtil.isEmpty(simplePCFAccordee.getDateCalcul())) {
            JadeThread.logError(SimplePCFAccordeeChecker.class.getName(),
                    "perseus.pcfaccordee.simplepcfaccordee.datecalcul.mandatory");
        }
    }
}
