package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.business.domaine.decision.TypeDecision;
import ch.globaz.pegasus.business.domaine.demande.EtatDemande;
import ch.globaz.pegasus.business.domaine.droit.EtatDroit;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.domaine.pca.PcaSituation;
import ch.globaz.pegasus.rpc.domaine.RpcData;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionRequerantConjoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Le but est de filter les decisions multiple d'un mois pour retourner, selon les règles établies, une seule decision
 * agregeant les données des autres
 */
public class RpcDataDecisionFilter {

    private static final Logger LOG = LoggerFactory.getLogger(RpcDataDecisionFilter.class);

    public static void filtre(RpcData rpcData) {
        if (rpcData.getRpcDecisionRequerantConjoints().size() > 1) {
            boolean isSupressionConjoint = analyseSuppression(rpcData);
            if (!isSupressionConjoint) {
                mergeDecisions(rpcData);
            }
        }
        // PR-038 : ne pas annoncer des décisions historisé d'octroie alors que la demande est supprimée
        if(EtatDemande.SUPPRIME.equals(rpcData.getDemande().getEtat())
                && EtatDroit.HISTORISE.equals(rpcData.getVersionDroit().getEtat())
                && hasOnlyOctroie(rpcData.getRpcDecisionRequerantConjoints())
                && rpcData.getDemande().getFin() != null
                && rpcData.getDemande().getFin().before(rpcData.getRpcDecisionRequerantConjoints().get(0).getRequerant().getDecision().getDateDebut())) {
            LOG.info("Pas d'annonce pour le droit : "+rpcData.getVersionDroit().getId() + " - demande : "+rpcData.getDemande().getId());
            rpcData.getRpcDecisionRequerantConjoints().clear();
        }
    }

    private static void mergeDecisions(RpcData rpcData) {
        if (isOnlyNegativ(rpcData.getRpcDecisionRequerantConjoints())) {
            keepLast(rpcData);
        } else if (rpcData.hasCurrent()) {
            keepCurrent(rpcData);
        } else {
            keepLastPositiv(rpcData);
        }
    }

    static void filtreNonRequerant(RpcData rpcData) {
        boolean hasRequerant = false;
        List<RpcDecisionRequerantConjoint> decisionsToRemove = new ArrayList<>();
        for (RpcDecisionRequerantConjoint decision : rpcData.getRpcDecisionRequerantConjoints()) {
            if (!RoleMembreFamille.REQUERANT.equals(decision.getRequerant().getPca().getRoleBeneficiaire())) {
                decisionsToRemove.add(decision);
            } else {
                hasRequerant = true;
            }
        }
        if (hasRequerant) {
            for (RpcDecisionRequerantConjoint decision : decisionsToRemove) {
                rpcData.getRpcDecisionRequerantConjoints().remove(decision);
            }
        }
    }


    static void keepLast(RpcData rpcData) {
        sortOnDateFrom(rpcData);
        RpcDecisionRequerantConjoint last = rpcData.getRpcDecisionRequerantConjoints().get(rpcData.getRpcDecisionRequerantConjoints().size() - 1);
        rpcData.setDemande(last.getDemande());
        rpcData.getRpcDecisionRequerantConjoints().clear();
        rpcData.add(last);
    }

    static void keepCurrent(RpcData rpcData) {
        RpcDecisionRequerantConjoint rpcDeci = rpcData.resolveCurrent();
        rpcData.setDemande(rpcDeci.getDemande());
        rpcData.getRpcDecisionRequerantConjoints().clear();
        rpcData.add(rpcDeci);
    }

    static void keepLastPositiv(RpcData rpcData) {
        sortOnDateFrom(rpcData);
        RpcDecisionRequerantConjoint rpcDeci = resolveLastOctroy(rpcData);
        rpcData.setDemande(rpcDeci.getDemande());
        rpcData.getRpcDecisionRequerantConjoints().clear();
        rpcData.add(rpcDeci);
    }

    static boolean isOnlyNegativ(List<RpcDecisionRequerantConjoint> decisions) {
        for (RpcDecisionRequerantConjoint rpcDecisionRequerantConjoint : decisions) {
            if (!rpcDecisionRequerantConjoint.getRequerant().getPca().getEtatCalcul().isRefus()) {
                return false;
            }
        }
        return true;
    }

    static boolean hasOnlyOctroie(List<RpcDecisionRequerantConjoint> decisions) {
        for (RpcDecisionRequerantConjoint rpcDecisionRequerantConjoint : decisions) {
            if (TypeDecision.SUPPRESSION_SANS_CALCUL.equals(rpcDecisionRequerantConjoint.getRequerant().getDecision().getType())) {
                return false;
            }
        }
        return true;
    }

    private static RpcDecisionRequerantConjoint resolveLastOctroy(RpcData rpcData) {
        sortOnDateFrom(rpcData);
        RpcDecisionRequerantConjoint rpcDeci = null;
        for (RpcDecisionRequerantConjoint rpcDecisionRequerantConjoint : rpcData.getRpcDecisionRequerantConjoints()) {
            if (rpcDecisionRequerantConjoint.getRequerant().getPca().getEtatCalcul().isOctroieOuOctroiePartiel()) {
                rpcDeci = rpcDecisionRequerantConjoint;
            }
        }
        return rpcDeci;
    }

    private static void sortOnDateFrom(RpcData rpcData) {
        Collections.sort(rpcData.getRpcDecisionRequerantConjoints(), new Comparator<RpcDecisionRequerantConjoint>() {
            @Override
            public int compare(RpcDecisionRequerantConjoint deci1, RpcDecisionRequerantConjoint deci2) {

                return deci1.getRequerant().getDecision().getDateDebut()
                        .compareTo(deci2.getRequerant().getDecision().getDateDebut());
            }
        });
    }

    private static boolean analyseSuppression(RpcData rpcData) {
        sortOnDateFrom(rpcData);
        RpcDecisionRequerantConjoint splitDecision = null;
        boolean isCoupleASeul = false;
        for (RpcDecisionRequerantConjoint decision : rpcData.getRpcDecisionRequerantConjoints()) {
            if ((TypeDecision.SUPPRESSION_SANS_CALCUL.equals(decision.getRequerant().getDecision().getType())
                    && (decision.getSituation().isCoupleSepare()) || decision.getSituation().isDom2())) {
                splitDecision = decision;
            } else if (splitDecision != null
                && !TypeDecision.SUPPRESSION_SANS_CALCUL.equals(decision.getRequerant().getDecision().getType())
                && (PcaSituation.HOME.equals(decision.getSituation()) || PcaSituation.DOMICILE.equals(decision.getSituation()))) {
                isCoupleASeul = true;
                break;
            }
        }

        if (isCoupleASeul) {
            List<RpcDecisionRequerantConjoint> enCouple = new ArrayList<>(rpcData.getRpcDecisionRequerantConjoints().subList(0, rpcData.getRpcDecisionRequerantConjoints().indexOf(splitDecision) + 1));
            List<RpcDecisionRequerantConjoint> seul = new ArrayList<>(rpcData.getRpcDecisionRequerantConjoints().subList(rpcData.getRpcDecisionRequerantConjoints().indexOf(splitDecision) + 1, rpcData.getRpcDecisionRequerantConjoints().size()));
            RpcDecisionRequerantConjoint finalEnCouple = mergeAndReturnDecision(rpcData, enCouple);
            RpcDecisionRequerantConjoint finalSeul = mergeAndReturnDecision(rpcData, seul);
            rpcData.getRpcDecisionRequerantConjoints().clear();
            rpcData.add(finalEnCouple);
            rpcData.add(finalSeul);
        }

        return isCoupleASeul;
    }

    private static RpcDecisionRequerantConjoint mergeAndReturnDecision(RpcData rpcData, List<RpcDecisionRequerantConjoint> listDecisions) {
        rpcData.getRpcDecisionRequerantConjoints().clear();
        rpcData.getRpcDecisionRequerantConjoints().addAll(listDecisions);
        mergeDecisions(rpcData);
        return rpcData.getRpcDecisionRequerantConjoints().get(0);
    }

}
