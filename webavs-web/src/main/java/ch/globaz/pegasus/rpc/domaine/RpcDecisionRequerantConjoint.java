package ch.globaz.pegasus.rpc.domaine;

import java.util.List;
import ch.globaz.pegasus.business.domaine.demande.Demande;
import ch.globaz.pegasus.business.domaine.droit.VersionDroit;
import ch.globaz.pegasus.business.domaine.membreFamille.MembreFamilleWithDonneesFinanciere;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariablesMetier;
import ch.globaz.pegasus.business.domaine.pca.PcaDecision;
import ch.globaz.pegasus.business.domaine.pca.PcaSituation;

public class RpcDecisionRequerantConjoint {
    private final Demande demande;
    private final RpcPcaDecisionCalculElementCalcul requerant;
    private final RpcPcaDecisionCalculElementCalcul conjoint;
    private final PcaSituation situation;
    private final MembresFamilleWithDonneesFinanciere membresFamilleWithDonneesFinanciere;

    public RpcDecisionRequerantConjoint(Demande demande, PcaDecision decisionRequerant, RpcCalcul calculRequerant,
            PersonsElementsCalcul personsElementsCalculRequerant, PcaDecision decisionConjoint,
            RpcCalcul calculConjoint, PersonsElementsCalcul personsElementsCalculConjoint,
            List<MembreFamilleWithDonneesFinanciere> membresFamilleWithDonneesFinancieres,
            VariablesMetier variablesMetier) {
        this.demande = demande;
        membresFamilleWithDonneesFinanciere = new MembresFamilleWithDonneesFinanciere(
                membresFamilleWithDonneesFinancieres, variablesMetier);

        requerant = new RpcPcaDecisionCalculElementCalcul(decisionRequerant, calculRequerant,
                personsElementsCalculRequerant);

        conjoint = new RpcPcaDecisionCalculElementCalcul(decisionConjoint, calculConjoint,
                personsElementsCalculConjoint);

        situation = resolveSituation();
    }

    public RpcDecisionRequerantConjoint(Demande demande, PcaDecision decisionRequerant, RpcCalcul calculRequerant,
            List<MembreFamilleWithDonneesFinanciere> membresFamilleWithDonneesFinancieres,
            PersonsElementsCalcul personsElementsCalcul, VariablesMetier variablesMetier) {
        this.demande = demande;
        membresFamilleWithDonneesFinanciere = new MembresFamilleWithDonneesFinanciere(
                membresFamilleWithDonneesFinancieres, variablesMetier);
        requerant = new RpcPcaDecisionCalculElementCalcul(decisionRequerant, calculRequerant, personsElementsCalcul);

        conjoint = null;

        /* Il n'y a pas de pca pour les décisions sans calcul */
        if (!requerant.getPcaDecision().getDecision().getType().isRefusSansCalcul()) {
            situation = resolveSituation();
        } else {
            situation = PcaSituation.INDEFINIT;
        }
    }

    public PcaDecision getRequerant() {
        return requerant.getPcaDecision();
    }

    public PcaDecision getConjoint() {
        return conjoint.getPcaDecision();
    }

    public RpcPcaDecisionCalculElementCalcul getRequerantDatas() {
        return requerant;
    }

    public RpcPcaDecisionCalculElementCalcul getConjointDatas() {
        return conjoint;
    }

    public boolean isCurrent() {
        return hasRequerant() && requerant.getPcaDecision().getPca() != null
                && requerant.getPcaDecision().getPca().hasCurrent();
    }

    public boolean isCoupleSepare() {
        return situation.isCoupleSepare();
    }

    public PcaSituation getSituation() {
        return situation;
    }

    private boolean isEmpty(RpcPcaDecisionCalculElementCalcul rpc) {
        if (rpc == null) {
            return true;
        }
        return false;
    }

    public boolean hasConjoint() {
        return !isEmpty(conjoint);
    }

    public boolean hasRequerant() {
        return !isEmpty(requerant);
    }

    public MembresFamilleWithDonneesFinanciere getMembresFamilleWithDonneesFinanciere() {
        return membresFamilleWithDonneesFinanciere;
    }

    public RpcDecisionAnnonceComplete buildRpcDecisionAnnonceCompleteRequerant(VersionDroit versionDroit) {
        return new RpcDecisionAnnonceComplete(requerant.getPcaDecision(), requerant.getCalcul(), versionDroit,
                membresFamilleWithDonneesFinanciere, requerant.getPersonsElementsCalcul(),
                hasConjoint() ? conjoint.getPcaDecision() : null, demande);
    }

    public RpcDecisionAnnonceComplete buildRpcDecisionAnnonceCompleteConjoint(VersionDroit versionDroit) {
        return new RpcDecisionAnnonceComplete(conjoint.getPcaDecision(), conjoint.getCalcul(), versionDroit,
                membresFamilleWithDonneesFinanciere, conjoint.getPersonsElementsCalcul(), requerant.getPcaDecision(),
                demande);
    }

    PcaSituation resolveSituation() {
        PcaSituation pcaCas = PcaSituation.INDEFINIT;
        if (hasRequerant()) {
            if (hasConjoint()) {
                pcaCas = PcaSituation.resolve(requerant.getPcaDecision().getPca().getGenre(), conjoint.getPcaDecision()
                        .getPca().getGenre(), requerant.getPcaDecision().getPca().getBeneficiaireConjointDom2R()
                        .estInitialisee());
            } else {
                pcaCas = PcaSituation.resolve(requerant.getPcaDecision().getPca().getGenre(), null, requerant
                        .getPcaDecision().getPca().getBeneficiaireConjointDom2R().estInitialisee());
            }
        }
        return pcaCas;
    }

}
