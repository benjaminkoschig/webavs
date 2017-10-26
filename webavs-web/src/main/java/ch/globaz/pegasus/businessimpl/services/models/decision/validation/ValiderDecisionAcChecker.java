package ch.globaz.pegasus.businessimpl.services.models.decision.validation;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.util.JadePersistenceUtil;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import java.util.Map;
import ch.globaz.corvus.business.exceptions.models.RetenuePayementException;
import ch.globaz.corvus.business.exceptions.models.SimpleRetenuePayementException;
import ch.globaz.corvus.business.models.pcaccordee.SimpleRetenuePayement;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;

public class ValiderDecisionAcChecker {

    public static void check(ValiderDecisionAcData data) throws RetenuePayementException,
            JadeApplicationServiceNotAvailableException, SimpleRetenuePayementException, JadePersistenceException,
            DecisionException, PmtMensuelException, JadeNoBusinessLogSessionError {

        // if (!ValiderDecisionAcChecker.checkIfOldDecisionHasReturnToTreat(data.getDecisionsApresCalcul(),

        if (!ValiderDecisionAcChecker.checkIfCoherenceRetenue(data.getDecisionsApresCalcul(),
                data.getRentuesPayements())) {
            throw new DecisionException("pegasus.validationDecision.coherenceRetenue");
        }

        if (!ValiderDecisionAcChecker.checkIfWithoutRetroCoherence(data)) {
            throw new DecisionException("pegasus.validationDecision.retroIncoherence");
        }

        if (ValiderDecisionAcChecker.isDateProchainPaimemtBeforeDateDecision(data.getDecisionsApresCalcul(),
                data.getDateProchainPaiement())) {
            String[] date = { "01." + data.getDateProchainPaiement() };
            JadeThread.logError(ValiderDecisionAcChecker.class.getClass().getName(),
                    "pegasus.validationDecision.dateProchainPaimentBefore", date);
            throw new DecisionException("pegasus.validationDecision.dateProchainPaimentBefore");
        }

        String dateDernierPmt = JadeDateUtil.addMonths("01." + data.getDateProchainPaiement(), -1);
        String dateProchainPmt = "01." + data.getDateProchainPaiement();

        DecisionApresCalcul dcLast = ValiderDecisionAcChecker.resolveLastDecisionRequerant(data);
        String dateDecision = dcLast.getDecisionHeader().getSimpleDecisionHeader().getDateDecision();
        // verifie que la date de décision soit valide
        String[] params = new String[2];

        if (JadeDateUtil.isDateBefore(dateDecision, dateDernierPmt)) {
            params[0] = dateDecision;
            params[1] = dateDernierPmt;
            JadeThread.logError(ValiderDecisionAcChecker.class.getClass().getName(),
                    "pegasus.simpleDecisionHeader.dateDecision.integrity.dateDernierPaiement", params);
        }
        if (!JadeDateUtil.isDateBefore(dateDecision, dateProchainPmt)) {
            params[0] = dateDecision;
            params[1] = dateProchainPmt;
            JadeThread.logError(ValiderDecisionAcChecker.class.getClass().getName(),
                    "pegasus.simpleDecisionHeader.dateDecision.integrity.dateProchainPaiement", params);
        }

        String dateDebutLastPca = dcLast.getPcAccordee().getSimplePrestationsAccordees().getDateDebutDroit();

        if (JadeDateUtil.isDateMonthYearAfter(dateDebutLastPca, data.getDateProchainPaiement())
                && !dateDebutLastPca.equals(data.getDateProchainPaiement())) {
            params[0] = dateDebutLastPca;
            params[1] = data.getDateProchainPaiement();
            JadeThread.logError(ValiderDecisionAcChecker.class.getClass().getName(),
                    "pegasus.simpleDecisionHeader.datePca.integrity.dateProchainPaiement", params);
        }

    }

    /**
     * 
     * Permet verifier que le montant total des retenus ne dépasse pas le montant de la PCA
     * 
     * @param decisionApresCalculSearch
     * @return
     * @throws RetenuePayementException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws SimpleRetenuePayementException
     * @throws JadePersistenceException
     */
    private static Boolean checkIfCoherenceRetenue(List<DecisionApresCalcul> decisions,
            List<SimpleRetenuePayement> retenues) throws RetenuePayementException,
            JadeApplicationServiceNotAvailableException, SimpleRetenuePayementException, JadePersistenceException {

        Map<String, List<DecisionApresCalcul>> mapDecisions = ValiderDecisionAcChecker.groupDecisionsByIdPca(decisions);

        float sommeTotalRetenue = 0;

        for (SimpleRetenuePayement retenuePayement : retenues) {
            // Retenue encore active
            if (retenuePayement.getDateFinRetenue().equalsIgnoreCase("0")) {
                sommeTotalRetenue += new Float(retenuePayement.getMontantRetenuMensuel());
            }
            SimplePlanDeCalcul planDeCalcul = null;
            if (mapDecisions.containsKey(retenuePayement.getIdRenteAccordee())) {
                if (retenuePayement.getIdRenteAccordee().length() > 1) {
                    throw new RetenuePayementException("Too many planClacule was resolved with this ids pca: "
                            + retenuePayement.getIdRenteAccordee());
                }
                planDeCalcul = mapDecisions.get(retenuePayement.getIdRenteAccordee()).get(0).getPlanCalcul();
            } else {
                throw new RetenuePayementException("Unable to resolve the planCalcule with this ids pca: "
                        + retenuePayement.getIdRenteAccordee() + " funded in the retenue");
            }

            if (sommeTotalRetenue > new Float(planDeCalcul.getMontantPCMensuelle())) {
                return false;
            }
        }
        return true;
    }

    /**
     * définis si une décision basé sur un calcul effectué de type sans retro, est bien basée sur des pca non
     * rétroactive. On se base sur la date de prochain paiement
     * 
     * @param dac
     * @return true, si ok, false dans le cas contraire
     * @throws PmtMensuelException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DecisionException
     */
    private static Boolean checkIfWithoutRetroCoherence(ValiderDecisionAcData data)
            throws JadeApplicationServiceNotAvailableException, DecisionException {

        DecisionApresCalcul dc = ValiderDecisionAcChecker.resolveLastDecisionRequerant(data);

        Boolean calculIsRetro = dc.getPcAccordee().getSimplePCAccordee().getIsCalculRetro();
        Boolean retroCoherence = true;

        // Si pas de calcul retro
        if (!calculIsRetro) {
            // Si date de debut pca antérieur a date de prochain paiement, ok
            String datePP = "01." + data.getDateProchainPaiement();
            String ddPCA = "01." + dc.getPcAccordee().getSimplePCAccordee().getDateDebut();

            if (JadeDateUtil.isDateAfter(ddPCA, datePP) || JadeDateUtil.areDatesEquals(ddPCA, datePP)) {
                retroCoherence = true;
            } else {
                retroCoherence = false;
            }
        }

        return retroCoherence;
    }

    private static Map<String, List<DecisionApresCalcul>> groupDecisionsByIdPca(List<DecisionApresCalcul> decisions) {
        Map<String, List<DecisionApresCalcul>> mapDecisions = JadeListUtil.groupBy(decisions,
                new JadeListUtil.Key<DecisionApresCalcul>() {
                    @Override
                    public String exec(DecisionApresCalcul e) {
                        return e.getPcAccordee().getSimplePCAccordee().getIdPCAccordee();
                    }
                });
        return mapDecisions;
    }

    private static boolean isDateProchainPaimemtBeforeDateDecision(List<DecisionApresCalcul> decisionsAc, String datePP)
            throws PmtMensuelException, JadeApplicationServiceNotAvailableException {

        Integer dateMin = null;
        Integer dateTemp = 0;

        for (DecisionApresCalcul dc : decisionsAc) {
            String dateDecision = dc.getDecisionHeader().getSimpleDecisionHeader().getDateDecision();
            dateTemp = Integer.valueOf(JadePersistenceUtil.parseDateToSql(dateDecision));
            if (((dateMin == null) || (dateTemp < dateMin))) {
                dateMin = dateTemp;
            }
        }

        if (dateMin > Integer.valueOf(Integer.valueOf(JadePersistenceUtil.parseDateToSql("01." + datePP)))) {
            return true;
        }
        return false;
    }

    static DecisionApresCalcul resolveLastDecisionRequerant(ValiderDecisionAcData data) throws DecisionException {
        DecisionApresCalcul dc = ValiderDecisionUtils.resolvedLastDecisionRequerant(data.getDecisionsApresCalcul(),
                data.getDateDebut());
        return dc;
    }

    /**
     * 
     * Verifie que la précédente décision n'a pas des retours en warning non traités
     * 
     * @param decisionApresCalculSearch
     * @return
     * @throws RetenuePayementException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws SimpleRetenuePayementException
     * @throws JadePersistenceException
     */
    private static Boolean checkIfOldDecisionHasReturnToTreat(List<DecisionApresCalcul> decisions)
            throws RetenuePayementException, JadeApplicationServiceNotAvailableException,
            SimpleRetenuePayementException, JadePersistenceException {

        return true;
    }
}
