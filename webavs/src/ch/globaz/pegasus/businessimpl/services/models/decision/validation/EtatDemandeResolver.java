package ch.globaz.pegasus.businessimpl.services.models.decision.validation;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;

public class EtatDemandeResolver {

    static boolean hasSameDateFin(DecisionApresCalcul decisionRequerant, DecisionApresCalcul decisionConjoint) {
        return decisionRequerant.getDecisionHeader().getSimpleDecisionHeader().getDateDebutDecision()
                .equals(decisionConjoint.getDecisionHeader().getSimpleDecisionHeader().getDateDebutDecision());
    }

    private static boolean isDecisionConjointCoupleSeparer(DecisionApresCalcul decisionMostRecentConjoint) {
        return IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(decisionMostRecentConjoint.getPcAccordee()
                .getSimplePCAccordee().getCsRoleBeneficiaire());
    }

    static DecisionApresCalcul resolveDecionsMostFavorable(DecisionApresCalcul requerant, DecisionApresCalcul conjoint) {
        if (conjoint != null) {
            String csEtatRequerant = requerant.getPlanCalcul().getEtatPC();
            String csEtatConjoint = conjoint.getPlanCalcul().getEtatPC();
            if (!csEtatRequerant.equals(csEtatConjoint)) {
                int pointRequerant = EtatDemandeResolver.resolvePoidDecsion(csEtatRequerant);
                int pointConjoint = EtatDemandeResolver.resolvePoidDecsion(csEtatConjoint);
                if (pointConjoint > pointRequerant) {
                    return conjoint;
                }
            }
        }
        return requerant;
    }

    /**
     * @param decisions
     * @param hasOnlyRefus
     *            : Doit indiqué si la demande contient que de pcas en refus
     * @return
     */
    public static String resolvedEtatDemande(List<DecisionApresCalcul> decisions, boolean hasOnlyRefus) {
        return EtatDemandeResolver.resolvedEtatDemande(decisions, hasOnlyRefus, false);
    }

    public static String resolvedEtatDemandeForDevalidation(List<DecisionApresCalcul> decisions, boolean hasOnlyRefus) {
        return EtatDemandeResolver.resolvedEtatDemande(decisions, hasOnlyRefus, true);
    }

    private static String resolvedEtatDemande(List<DecisionApresCalcul> decisions, boolean hasOnlyRefus,
            boolean forDevalidation) {
        if (decisions == null) {
            throw new IllegalArgumentException("Unable to resolvedEtatDemande, the decisions is null!");
        }

        if (decisions.size() == 0) {
            throw new IllegalArgumentException("Unable to resolvedEtatDemande, the decisions is empty!");
        }

        DecisionApresCalcul decisionMostRecent = EtatDemandeResolver.resolveLastDecsion(decisions);
        String csEtatPca = decisionMostRecent.getPlanCalcul().getEtatPC();
        boolean hasDemandeDateFin = !JadeStringUtil.isBlankOrZero(decisionMostRecent.getPcAccordee()
                .getSimplePCAccordee().getDateFin());

        return EtatDemandeResolver.resolveEtatDemandeByEtaPca(csEtatPca, hasOnlyRefus, hasDemandeDateFin,
                forDevalidation);
    }

    // static boolean hasOnlyRefus(List<DecisionApresCalcul> decisions) {
    //
    // for (DecisionApresCalcul dc : decisions) {
    // if (!IPCDecision.CS_TYPE_REFUS_AC.equals(dc.getDecisionHeader().getSimpleDecisionHeader()
    // .getCsTypeDecision())) {
    // return false;
    // }
    // }
    // return true;
    // }

    static boolean hasPcaDateDeFinForce(List<DecisionApresCalcul> decisions) {
        for (DecisionApresCalcul dc : decisions) {
            if (dc.getPcAccordee().getSimplePCAccordee().getIsDateFinForce()) {
                return false;
            }
        }
        return false;
    }

    static String resolveEtatDemandeByEtaPca(String csEtatPca, boolean hasOnlyRefus, boolean hasDateFin,
            boolean forDevalidation) {

        String csEtat = null;
        boolean isRefus = false;

        if (IPCValeursPlanCalcul.STATUS_OCTROI.equals(csEtatPca)
                || IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL.equals(csEtatPca)) {
            csEtat = IPCDemandes.CS_OCTROYE;
        } else if (IPCValeursPlanCalcul.STATUS_REFUS.equals(csEtatPca)) {
            if (!forDevalidation) {
                if (hasOnlyRefus) {
                    csEtat = IPCDemandes.CS_REFUSE;
                } else {
                    csEtat = IPCDemandes.CS_SUPPRIME;
                }
            } else {
                csEtat = IPCDemandes.CS_REOUVERT;
            }
            isRefus = true;
        }

        if (hasDateFin && !isRefus) {
            csEtat = IPCDemandes.CS_SUPPRIME;
        }
        return csEtat;
    }

    static DecisionApresCalcul resolveLastDecsion(List<DecisionApresCalcul> decisions) {
        decisions = EtatDemandeResolver.sortDecsionByDateDateDebutDesc(decisions);
        DecisionApresCalcul decisionMostRecentAndFaborable = decisions.get(0);

        if (decisions.size() > 1) {
            if (EtatDemandeResolver.isDecisionConjointCoupleSeparer(decisions.get(1))
                    && EtatDemandeResolver.hasSameDateFin(decisionMostRecentAndFaborable, decisions.get(1))) {
                decisionMostRecentAndFaborable = EtatDemandeResolver.resolveDecionsMostFavorable(decisions.get(0),
                        decisions.get(1));
            }
        }

        return decisionMostRecentAndFaborable;
    }

    private static int resolvePoidDecsion(String csEtat) {
        int point = 0;
        if (IPCValeursPlanCalcul.STATUS_OCTROI.equals(csEtat)) {
            point = 3;
        } else if (IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL.equals(csEtat)) {
            point = 2;
        } else if (IPCValeursPlanCalcul.STATUS_REFUS.equals(csEtat)) {
            point = 1;
        }
        return point;
    }

    static List<DecisionApresCalcul> sortDecsionByDateDateDebutDesc(List<DecisionApresCalcul> decisions) {
        List<DecisionApresCalcul> copie = new ArrayList<DecisionApresCalcul>(decisions);
        Collections.sort(copie, new Comparator<DecisionApresCalcul>() {
            @Override
            public int compare(DecisionApresCalcul o1, DecisionApresCalcul o2) {
                String date1 = o1.getDecisionHeader().getSimpleDecisionHeader().getDateDebutDecision();
                String date2 = o2.getDecisionHeader().getSimpleDecisionHeader().getDateDebutDecision();
                if ((date1 == null) && (date2 == null)) {
                    return 0;
                } else if ((date1 == null) && (date2 != null)) {
                    return 1;
                } else if ((date1 != null) && (date2 == null)) {
                    return -1;
                }

                if (JadeDateUtil.isDateMonthYearBefore(date1, date2)) {
                    return 1;
                } else if (date1.equals(date2)) {
                    if (IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(o1.getPcAccordee().getSimplePCAccordee()
                            .getCsRoleBeneficiaire())) {
                        return -1;
                    } else if (o1.getPcAccordee().getSimplePCAccordee().getCsRoleBeneficiaire()
                            .equals(o2.getPcAccordee().getSimplePCAccordee().getCsRoleBeneficiaire())) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
                return -1;
            }
        });
        return copie;
    }
}
