package ch.globaz.pegasus.rpc.domaine;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.domaine.decision.Decision;
import ch.globaz.pegasus.business.domaine.demande.Demande;
import ch.globaz.pegasus.business.domaine.dossier.Dossier;
import ch.globaz.pegasus.business.domaine.droit.VersionDroit;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.domaine.pca.PcaDecisions;
import ch.globaz.pegasus.rpc.businessImpl.converter.ConverterDecisionCause;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pyxis.domaine.PersonneAVS;

public class RpcData {
    private final VersionDroit versionDroit;
    private final Dossier dossier;
    private Demande demande;
    private final List<RpcDecisionRequerantConjoint> rpcDecisionRequerantConjoints = new ArrayList<RpcDecisionRequerantConjoint>();
    private AnnonceCase annonce;

    public RpcData(VersionDroit versionDroit, Dossier dossier, Demande demande) {
        this.versionDroit = versionDroit;
        this.dossier = dossier;
        this.demande = demande;
    }

    public RpcData(Dossier dossier, Demande demande) {
        versionDroit = null;
        this.dossier = dossier;
        this.demande = demande;
    }

    public VersionDroit getVersionDroit() {
        return versionDroit;
    }

    public boolean add(RpcDecisionRequerantConjoint rpcDecisionRequerantConjoint) {
        return rpcDecisionRequerantConjoints.add(rpcDecisionRequerantConjoint);
    }

    public List<RpcDecisionRequerantConjoint> getRpcDecisionRequerantConjoints() {
        return rpcDecisionRequerantConjoints;
    }

    public Dossier getDossier() {
        return dossier;
    }

    public Demande getDemande() {
        return demande;
    }
    
    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public String descriptionHtml() {
        String description = formatPersonne(
                rpcDecisionRequerantConjoints.get(0).getRequerant().getDecision().getTiersBeneficiaire());

        if (hasVersionDroit()) {
            description += " - Num droit:" + versionDroit.getNumero();
        }
        description += " - idDossier:" + getDossier().getId() + ", nb pcaDecisionRC:"
                + rpcDecisionRequerantConjoints.size() + ", Situation:"
                + rpcDecisionRequerantConjoints.get(0).getSituation();

        description += " - Decisions: " + formatDecisions();
        return description;
    }

    public String description() {
        PersonneAVS personne = rpcDecisionRequerantConjoints.get(0).getRequerant().getDecision().getTiersBeneficiaire();
        String description = personne.getNss() + " - " + personne.getNom() + " " + personne.getPrenom() + " / "
                + personne.getDateNaissance();

        if (hasVersionDroit()) {
            description += " - Num droit:" + versionDroit.getNumero();
        }
        description += " - idDossier:" + getDossier().getId() + ", nb pcaDecisionRC:"
                + rpcDecisionRequerantConjoints.size() + ", Situation:"
                + rpcDecisionRequerantConjoints.get(0).getSituation();

        description += " - Decisions: " + formatDecisions();
        return description;
    }

    public boolean isMotifDroitSuppression() {
        return versionDroit != null && versionDroit.getMotif().isSuppression();
    }

    public boolean hasCurrent() {
        if (!isMotifDroitSuppression()) {
            return resolveCurrent() != null;
        }
        return false;
    }

    public RpcDecisionRequerantConjoint resolveCurrent() {
        for (RpcDecisionRequerantConjoint decision : rpcDecisionRequerantConjoints) {
            if (decision.isCurrent()) {
                return decision;
            }
        }
        return null;
    }
    
    public RpcDecisionRequerantConjoint resolveCurrentConjoint() {
        for (RpcDecisionRequerantConjoint decision : rpcDecisionRequerantConjoints) {
            if (decision.isCurrentConjoint()) {
                return decision;
            }
        }
        return null;
    }

    public PcaDecisions resolveDecisionsConjoint() {
        PcaDecisions decisions = new PcaDecisions();
        for (RpcDecisionRequerantConjoint decision : rpcDecisionRequerantConjoints) {
            if (decision.hasConjoint()) {
                decisions.add(decision.getConjoint());
            }
        }
        return decisions;
    }

    public boolean hasVersionDroit() {
        return getVersionDroit() != null;
    }

    public PcaDecisions resolveDecisionsRequerant() {
        PcaDecisions decisions = new PcaDecisions();
        for (RpcDecisionRequerantConjoint decision : rpcDecisionRequerantConjoints) {
            if (decision.hasRequerant()) {
                decisions.add(decision.getRequerant());
            }
        }
        return decisions;
    }

    public boolean isDecisionsSuppressionOrRefusSc() {
        for (RpcDecisionRequerantConjoint pcaDesions : rpcDecisionRequerantConjoints) {
            if (pcaDesions.getRequerant().getDecision().getType().isRefusSansCalcul()) {
                return true;
            }
            if (pcaDesions.getRequerant().getDecision().getType().isSuppression()) {
                return true;
            }
        }
        return false;
    }

    private String formatPersonne(PersonneAVS personneAVS) {
        String format = "<b>" + personneAVS.getNss() + "&nbsp;";

        if (personneAVS.getDateDeces() != null && !personneAVS.getDateDeces().isEmpty()) {
            format += "<span  style=color:red> ( </span><span style=font-family:wingdings>U</span>&nbsp;"
                    + personneAVS.getDateDeces() + "<span  style=color:red> )</span>&nbsp;";
        }
        format += "</b>" + personneAVS.getNom() + " " + personneAVS.getPrenom() + " / "
                + personneAVS.getDateNaissance();
        return format;
    }

    private String formatDecisions() {
        StringBuilder builder = new StringBuilder();
        for (RpcDecisionRequerantConjoint pcaDecisionRC : rpcDecisionRequerantConjoints) {
            builder.append(pcaDecisionRC.getSituation() + "-");
            if (pcaDecisionRC.hasRequerant()) {
                Decision decision = pcaDecisionRC.getRequerant().getDecision();
                builder.append("R[" + decision.getType() + ", " + decision.getDateDebut().getSwissMonthValue() + "-"
                        + (decision.getDateFin() == null ? "" : decision.getDateFin().getSwissMonthValue()) + ", "
                        + decision.getDateDecision() + "]");
            }
            if (pcaDecisionRC.hasConjoint()) {
                Decision decision = pcaDecisionRC.getConjoint().getDecision();
                builder.append(" C[" + decision.getType() + ", " + decision.getDateDebut().getSwissMonthValue() + "-"
                        + (decision.getDateFin() == null ? "" : decision.getDateFin().getSwissMonthValue()) + ", "
                        + decision.getDateDecision() + "]");
            }
        }
        return builder.toString();
    }

    public boolean isSuppressionDecesRequerant() {
        return isCoupleSepare() && isMotifDeces() && isRequerantDecede() && isSansDateDeFin() && isDecisionSuppRetro()
                ? true
                : false;
    }

    /**
     * Couple séparé
     */
    private boolean isCoupleSepare() {
        return rpcDecisionRequerantConjoints.get(0).getSituation().isCoupleSepare();
    }

    /**
     * Décision de suppression prise dans ce mois
     */
    private boolean isMotifDeces() {
        return ConverterDecisionCause.convert(getVersionDroit()).compareTo(BigInteger.valueOf(4)) == 0;
    }

    /**
     * Requérant décédé
     */
    private boolean isRequerantDecede() {
        return rpcDecisionRequerantConjoints.get(0).getRequerant().getDecision().getTiersBeneficiaire()
                .getDateDeces() != null;
    }

    /**
     * Demande sans date de fin
     */
    private boolean isSansDateDeFin() {
        return demande.getFin() == null;
    }

    /**
     * Décision de suppression rétroactive
     */
    private boolean isDecisionSuppRetro() {
        if (rpcDecisionRequerantConjoints.get(0).getRequerant().getDecision().getDateFin() == null) {
            return false;
        }
        return rpcDecisionRequerantConjoints.get(0).getRequerant().getPca().getDateAnnonceComptable()
                .after(rpcDecisionRequerantConjoints.get(0).getRequerant().getDecision().getDateFin());
    }

    public AnnonceCase getAnnonce() {
        if (annonce == null) {
            annonce = new AnnonceCase(this);
        }
        return annonce;
    }

}
