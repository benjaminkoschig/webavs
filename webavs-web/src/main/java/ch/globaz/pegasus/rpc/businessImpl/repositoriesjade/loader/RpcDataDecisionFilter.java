package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.rpc.domaine.RpcData;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionRequerantConjoint;

/**
 * Le but est de filter les decisions multiple d'un mois pour retourner, selon les règles établies, une seule decision
 * agregeant les données des autres
 */
public class RpcDataDecisionFilter {

    public static void filtre(RpcData rpcData) {
        if (rpcData.getRpcDecisionRequerantConjoints().size() > 1) {
            if (isOnlyNegativ(rpcData.getRpcDecisionRequerantConjoints())) {
                keepLast(rpcData);
            } else if (rpcData.hasCurrent()) {
                keepCurrent(rpcData, resolveMinValidFrom(rpcData.getRpcDecisionRequerantConjoints()));
            } else {
                keepLastPositiv(rpcData, resolveMinValidFrom(rpcData.getRpcDecisionRequerantConjoints()),
                        resolveMaxValidTo(rpcData.getRpcDecisionRequerantConjoints()));
            }
        }
    }

    static void keepLast(RpcData rpcData) {
        sortOnDateFrom(rpcData);
        for (int i = 0; i < rpcData.getRpcDecisionRequerantConjoints().size(); i++) {
            rpcData.getRpcDecisionRequerantConjoints().remove(0);
        }
    }

    static void keepCurrent(RpcData rpcData, Date resolvedMinValidFrom) {
        RpcDecisionRequerantConjoint rpcDeci = rpcData.resolveCurrent();
        rpcDeci.getRequerant().getDecision().setDateDebut(resolvedMinValidFrom);
        rpcDeci.getRequerant().getPca().setDateDebut(resolvedMinValidFrom);
        if (rpcDeci.hasConjoint()) {
            rpcDeci.getConjoint().getDecision().setDateDebut(resolvedMinValidFrom);
            rpcDeci.getConjoint().getPca().setDateDebut(resolvedMinValidFrom);
        }
        rpcData.getRpcDecisionRequerantConjoints().clear();
        rpcData.add(rpcDeci);
    }

    static void keepLastPositiv(RpcData rpcData, Date resolvedMinValidFrom, Date resolvedMaxValidTo) {
        RpcDecisionRequerantConjoint rpcDeci = resolveLastOctroy(rpcData);
        rpcDeci.getRequerant().getDecision().setDateDebut(resolvedMinValidFrom);
        rpcDeci.getRequerant().getPca().setDateDebut(resolvedMinValidFrom);
        if (rpcDeci.hasConjoint()) {
            rpcDeci.getConjoint().getDecision().setDateDebut(resolvedMinValidFrom);
            rpcDeci.getConjoint().getPca().setDateDebut(resolvedMinValidFrom);
        }
        if (resolvedMaxValidTo != null) {
            rpcDeci.getRequerant().getDecision().setDateFin(resolvedMaxValidTo);
            rpcDeci.getRequerant().getPca().setDateFin(resolvedMaxValidTo);
            if (rpcDeci.hasConjoint()) {
                rpcDeci.getConjoint().getDecision().setDateFin(resolvedMinValidFrom);
                rpcDeci.getConjoint().getPca().setDateFin(resolvedMinValidFrom);
            }
        }
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

    private static Date resolveMaxValidTo(List<RpcDecisionRequerantConjoint> decisions) {
        Date maxValidTo = null;
        for (RpcDecisionRequerantConjoint rpcDecisionRequerantConjoint : decisions) {
            if (rpcDecisionRequerantConjoint.getRequerant().getPca().getEtatCalcul().isOctroieOuOctroiePartiel()
                    && (maxValidTo == null || rpcDecisionRequerantConjoint.getRequerant().getDecision().getDateFin()
                            .after(maxValidTo))) {
                maxValidTo = rpcDecisionRequerantConjoint.getRequerant().getDecision().getDateFin();
            }
        }
        return maxValidTo;
    }

    private static Date resolveMinValidFrom(List<RpcDecisionRequerantConjoint> decisions) {
        Date minValidFrom = null;
        for (RpcDecisionRequerantConjoint rpcDecisionRequerantConjoint : decisions) {
            if (rpcDecisionRequerantConjoint.getRequerant().getPca().getEtatCalcul().isOctroieOuOctroiePartiel()
                    && (minValidFrom == null || rpcDecisionRequerantConjoint.getRequerant().getDecision()
                            .getDateDebut().before(minValidFrom))) {
                minValidFrom = rpcDecisionRequerantConjoint.getRequerant().getDecision().getDateDebut();
            }
        }
        return minValidFrom;
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
}
