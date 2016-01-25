/**
 * 
 */
package ch.globaz.pegasus.businessimpl.checkers.decision;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.home.PeriodeServiceEtatException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AllocationImpotentException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.IndemniteJournaliereAiException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.RenteAvsAiException;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionSuppression;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

/**
 * @author SCE
 * 
 *         29 juil. 2010
 */
public class SimpleDecisionSuppressionChecker extends PegasusAbstractChecker {

    /**
     * Validation d'une allocation impotent lors de la creation
     * 
     * @param simpleAllocationImpotent
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws PmtMensuelException
     * @throws AllocationImpotentException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static void checkForCreate(SimpleDecisionSuppression decision) throws DecisionException,
            JadePersistenceException, JadeNoBusinessLogSessionError, PmtMensuelException,
            JadeApplicationServiceNotAvailableException {

        // Check mandatory
        SimpleDecisionSuppressionChecker.checkMandatory(decision);

        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleDecisionSuppressionChecker.checkIntegrity(decision);
        }
    }

    /**
     * Validation lors de l'effacement d'une allocationImpotent A prori inutile, car on ne supprime pas une
     * allocationImpotent, mais on la marque comme supprimé
     * 
     * @param simpleAllocationImpotent
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws AllocationImpotenetException
     * @throws PeriodeServiceEtatException
     */
    public static void checkForDelete(SimpleDecisionSuppression decision) throws DecisionException,
            JadePersistenceException, JadeNoBusinessLogSessionError {
    }

    /**
     * Validation d'une allocationImpotent lors d'une mise a jour
     * 
     * @param allocationImpotent
     * @throws AllocationImpotentException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PmtMensuelException
     */
    public static void checkForUpdate(SimpleDecisionSuppression decision) throws DecisionException,
            JadePersistenceException, JadeNoBusinessLogSessionError, PmtMensuelException,
            JadeApplicationServiceNotAvailableException {
        // Check mandatory
        SimpleDecisionSuppressionChecker.checkMandatory(decision);

        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleDecisionSuppressionChecker.checkIntegrity(decision);
        }
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * 
     * @param simpleAllocationImpotent
     * @throws simpleAllocationImpotentException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PmtMensuelException
     */
    private static void checkIntegrity(SimpleDecisionSuppression decision) throws DecisionException,
            JadePersistenceException, JadeNoBusinessLogSessionError, PmtMensuelException,
            JadeApplicationServiceNotAvailableException {

        // L'id du header doit etre défini
        if (JadeStringUtil.isEmpty(decision.getIdDecisionHeader())) {
            JadeThread.logError(decision.getClass().getName(), "pegasus.simpleDecisionSuppression.header.integrity");
        }
        // L'id de la version du droit doit etre definie
        if (JadeStringUtil.isEmpty(decision.getIdVersionDroit())) {
            JadeThread.logError(decision.getClass().getName(), "pegasus.simpleDecisionSuppression.idDroit.integrity");
        }

    }

    /**
     * Vérification des données obligatoires
     * 
     * @param simpleAllocationImpotent
     * @throws JadeNoBusinessLogSessionError
     * @throws RenteAvsAiException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws IndemniteJournaliereAiException
     */
    private static void checkMandatory(SimpleDecisionSuppression decision) throws DecisionException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        // Le motif doit etre spécifié
        if (JadeStringUtil.isEmpty(decision.getCsMotif())) {
            JadeThread.logError(decision.getClass().getName(), "pegasus.simpleDecisionSuppression.motif.mandatory");
        }
        // La date de suppression doit etre spécifié
        if (JadeStringUtil.isEmpty(decision.getDateSuppression())) {
            JadeThread.logError(decision.getClass().getName(),
                    "pegasus.simpleDecisionSuppression.dateSuppression.mandatory");
        }
        // si le motif a un sous motifs et que celui ci est vide
        if (IPCDecision.CS_MOTIF_SUPPR_WITH_SM_1.equals(decision.getCsMotif())
                || IPCDecision.CS_MOTIF_SUPPR_WITH_SM_2.equals(decision.getCsMotif())) {
            if (JadeStringUtil.isEmpty(decision.getCsSousMotif())) {
                JadeThread.logError(decision.getClass().getName(),
                        "pegasus.simpleDecisionSuppression.sousMotifs.integrity");
            }
        }

    }

    public static void checkParametersForTransfert(String motifTransfert, String idNouvelleCaisse) {

        if ((idNouvelleCaisse == null) || JadeStringUtil.isBlank(idNouvelleCaisse)) {
            JadeThread.logError("Transfert dossier suppression",
                    "pegasus.SimpleDecisionSupression.idNouvelleCaisse.mandatory");
        }

        if ((motifTransfert == null) || JadeStringUtil.isBlank(motifTransfert)) {
            JadeThread.logError("Transfert dossier suppression",
                    "pegasus.SimpleDecisionSupression.csMotifTransfert.mandatory");
        }

    }

}
