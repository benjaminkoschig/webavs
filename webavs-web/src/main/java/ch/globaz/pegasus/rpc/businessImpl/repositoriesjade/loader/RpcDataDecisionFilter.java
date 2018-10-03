package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.rpc.domaine.RpcData;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionRequerantConjoint;

/**
 * Le but est de filter les decisions multiple d'un mois pour retourner, selon les règles établies, une seule decision
 * agregeant les données des autres
 */
public class RpcDataDecisionFilter {

    public static void filtre(RpcData rpcData) {
        //filtreNonRequerant(rpcData);
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
    
    static void filtreNonRequerant(RpcData rpcData) {
        boolean hasRequerant = false;
        List<RpcDecisionRequerantConjoint> decisionsToRemove = new ArrayList<>();
        for(RpcDecisionRequerantConjoint decision : rpcData.getRpcDecisionRequerantConjoints()) {
            if(!RoleMembreFamille.REQUERANT.equals(decision.getRequerant().getPca().getRoleBeneficiaire())) {
                decisionsToRemove.add(decision);
            } else {
                hasRequerant = true;
            }
        }
        if(hasRequerant) {
            for(RpcDecisionRequerantConjoint decision :decisionsToRemove) {
                rpcData.getRpcDecisionRequerantConjoints().remove(decision);
            }
        }
    }
    

    static void keepLast(RpcData rpcData) {
        sortOnDateFrom(rpcData);
        RpcDecisionRequerantConjoint last = rpcData.getRpcDecisionRequerantConjoints().get(rpcData.getRpcDecisionRequerantConjoints().size()-1);
        last = resolveConjoint(last, rpcData);
        rpcData.setDemande(last.getDemande());
        rpcData.getRpcDecisionRequerantConjoints().clear();
        rpcData.add(last);
    }

    static void keepCurrent(RpcData rpcData, Date resolvedMinValidFrom) {
        RpcDecisionRequerantConjoint rpcDeci = rpcData.resolveCurrent();
        rpcData.setDemande(rpcDeci.getDemande());
        rpcDeci.getRequerant().getDecision().setDateDebut(resolvedMinValidFrom);
        rpcDeci.getRequerant().getPca().setDateDebut(resolvedMinValidFrom);
        if (rpcDeci.hasConjoint()) {
            rpcDeci.getConjoint().getDecision().setDateDebut(resolvedMinValidFrom);
            rpcDeci.getConjoint().getPca().setDateDebut(resolvedMinValidFrom);
        }
        RpcDecisionRequerantConjoint rpcDecif = resolveConjoint(rpcDeci, rpcData);
        rpcData.getRpcDecisionRequerantConjoints().clear();
        rpcData.add(rpcDecif);
    }

    static void keepLastPositiv(RpcData rpcData, Date resolvedMinValidFrom, Date resolvedMaxValidTo) {
        RpcDecisionRequerantConjoint rpcDeci = resolveLastOctroy(rpcData);
        rpcData.setDemande(rpcDeci.getDemande());
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
        RpcDecisionRequerantConjoint rpcDecif = resolveConjoint(rpcDeci, rpcData);
        rpcData.getRpcDecisionRequerantConjoints().clear();
        rpcData.add(rpcDecif);
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
    
    /////////////////////////////////////////
    //////       PARTIE CONJOINT    /////////
    /////////////////////////////////////////
    
    // S180426_009 - RPC - Pi-066 - S'il n'y a pas de décision conjoint dans la décision du requérant choisi mais qu'il en existe bien une dans les précédentes il faut fusionner les 2
       
    private static RpcDecisionRequerantConjoint resolveConjoint(RpcDecisionRequerantConjoint rpcDeci, RpcData rpcData) {
        if(!rpcDeci.hasConjoint() && hasOneDecisionConjoint(rpcData)) {
            List<RpcDecisionRequerantConjoint> decisionsConjoint = filtreListConjoint(rpcData.getRpcDecisionRequerantConjoints());
            if (isOnlyNegativeConjoint(decisionsConjoint)) {
                RpcDecisionRequerantConjoint current = keepLastConjoint(decisionsConjoint);
                return new RpcDecisionRequerantConjoint(rpcDeci, current.getConjointDatas());
            } else if(hasCurrentConjoint(rpcData)) {
                RpcDecisionRequerantConjoint current = keepCurrentConjoint(rpcData, resolveMinValidFromConjoint(decisionsConjoint));
                return new RpcDecisionRequerantConjoint(rpcDeci, current.getConjointDatas());
            } else {
                RpcDecisionRequerantConjoint current = keepLastPositiveConjoint(decisionsConjoint, resolveMinValidFromConjoint(decisionsConjoint),
                        resolveMaxValidToConjoint(decisionsConjoint));
                return new RpcDecisionRequerantConjoint(rpcDeci, current.getConjointDatas());
            }
        }
        return rpcDeci;
    }
    
    private static boolean isOnlyNegativeConjoint(List<RpcDecisionRequerantConjoint> decisions) {
        for (RpcDecisionRequerantConjoint rpcDecisionRequerantConjoint : decisions) {
            if (!rpcDecisionRequerantConjoint.getConjoint().getPca().getEtatCalcul().isRefus()) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean hasCurrentConjoint(RpcData rpcData) {
        for(RpcDecisionRequerantConjoint decision: rpcData.getRpcDecisionRequerantConjoints()) {
            if(decision.isCurrentConjoint()) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean hasOneDecisionConjoint(RpcData rpcData) {
        for(RpcDecisionRequerantConjoint decision: rpcData.getRpcDecisionRequerantConjoints()) {
            if(decision.hasConjoint()) {
                return true;
            }
        }
        return false;
    }
    
    private static RpcDecisionRequerantConjoint keepCurrentConjoint(RpcData rpcData, Date resolvedMinValidFrom) {
        RpcDecisionRequerantConjoint rpcDeci = rpcData.resolveCurrentConjoint();
        rpcDeci.getConjoint().getDecision().setDateDebut(resolvedMinValidFrom);
        rpcDeci.getConjoint().getPca().setDateDebut(resolvedMinValidFrom);
        return rpcDeci;
    }
    
    private static Date resolveMaxValidToConjoint(List<RpcDecisionRequerantConjoint> decisions) {
        Date maxValidTo = null;
        for (RpcDecisionRequerantConjoint rpcDecisionRequerantConjoint : decisions) {
            if (rpcDecisionRequerantConjoint.getConjoint().getPca().getEtatCalcul().isOctroieOuOctroiePartiel()
                    && (maxValidTo == null || rpcDecisionRequerantConjoint.getConjoint().getDecision().getDateFin()
                            .after(maxValidTo))) {
                maxValidTo = rpcDecisionRequerantConjoint.getConjoint().getDecision().getDateFin();
            }
        }
        return maxValidTo;
    }
    
    private static Date resolveMinValidFromConjoint(List<RpcDecisionRequerantConjoint> decisions) {
        Date minValidFrom = null;
        for (RpcDecisionRequerantConjoint rpcDecisionRequerantConjoint : decisions) {
            if (rpcDecisionRequerantConjoint.getConjoint().getPca().getEtatCalcul().isOctroieOuOctroiePartiel()
                    && (minValidFrom == null || rpcDecisionRequerantConjoint.getConjoint().getDecision()
                            .getDateDebut().before(minValidFrom))) {
                minValidFrom = rpcDecisionRequerantConjoint.getConjoint().getDecision().getDateDebut();
            }
        }
        return minValidFrom;
    }
    
    private static RpcDecisionRequerantConjoint keepLastConjoint(List<RpcDecisionRequerantConjoint> listConjoint) {
        sortOnDateFromConjoint(listConjoint);
        return listConjoint.get(listConjoint.size()-1);
    }

    
    private static RpcDecisionRequerantConjoint keepLastPositiveConjoint(List<RpcDecisionRequerantConjoint> listConjoint, Date resolvedMinValidFrom, Date resolvedMaxValidTo) {
        RpcDecisionRequerantConjoint rpcDeci = resolveLastOctroyConjoint(listConjoint);
        rpcDeci.getConjoint().getDecision().setDateDebut(resolvedMinValidFrom);
        rpcDeci.getConjoint().getPca().setDateDebut(resolvedMinValidFrom);
        if (resolvedMaxValidTo != null) {
            rpcDeci.getConjoint().getDecision().setDateFin(resolvedMaxValidTo);
            rpcDeci.getConjoint().getPca().setDateFin(resolvedMaxValidTo);
        }
        return rpcDeci;
    }
    
    private static RpcDecisionRequerantConjoint resolveLastOctroyConjoint(List<RpcDecisionRequerantConjoint> listConjoint) {
        sortOnDateFromConjoint(listConjoint);
        RpcDecisionRequerantConjoint rpcDeci = null;
        for (RpcDecisionRequerantConjoint rpcDecisionRequerantConjoint : listConjoint) {
            if (rpcDecisionRequerantConjoint.getConjoint().getPca().getEtatCalcul().isOctroieOuOctroiePartiel()) {
                rpcDeci = rpcDecisionRequerantConjoint;
            }
        }
        return rpcDeci;
    }
    
    private static List<RpcDecisionRequerantConjoint> filtreListConjoint(List<RpcDecisionRequerantConjoint> rpcDecisionRequerantConjoints) {
        List<RpcDecisionRequerantConjoint> rpcDecisionConjoint = new ArrayList<>();
        for(RpcDecisionRequerantConjoint decision:rpcDecisionRequerantConjoints) {
            if(decision.hasConjoint()) {
                rpcDecisionConjoint.add(decision);
            }
        }
        return rpcDecisionConjoint;
    }
    
    private static void sortOnDateFromConjoint(List<RpcDecisionRequerantConjoint> rpcDecisionRequerantConjoints) {
        Collections.sort(rpcDecisionRequerantConjoints, new Comparator<RpcDecisionRequerantConjoint>() {
            @Override
            public int compare(RpcDecisionRequerantConjoint deci1, RpcDecisionRequerantConjoint deci2) {

                return deci1.getConjoint().getDecision().getDateDebut()
                        .compareTo(deci2.getConjoint().getDecision().getDateDebut());
            }
        });
    }

}
