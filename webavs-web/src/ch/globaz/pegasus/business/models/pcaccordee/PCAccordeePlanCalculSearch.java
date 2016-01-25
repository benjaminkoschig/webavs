package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

public class PCAccordeePlanCalculSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FOR_CURRENT_VERSIONED_ETAT_PLAN_CALCULE_NOT_EQUALS = "forCurrentVersionnedEtatPlanClaculNotEquals";
    public static final String FOR_CURRENT_VERSIONED_FOR_ADAPTATION = "forCurrentPCAforAdaptation";
    public static final String FOR_CURRENT_VERSIONED_WITHOUT_COPIE = "forCurrentVersionedWithoutCopie";
    public static final String FOR_OLD_VERSIONED_PCA_WITH_MONTANT_MENSUELLE = "forOldVersionnedPcaWithMontantMensuelle";
    public static final String FOR_OLD_VERSIONED_PCA_WITH_MONTANT_MENSUELLE_FOR_DECOMPTE = "forOldVersionnedPcaWithMontantMensuelleForDecompte";
    public static final String FOR_OLD_VERSIONNED_PCA_WITHOUT_COPIED = "forOldVersionnedPcaWithoutCopied";
    public static final String FOR_PCA_CONJOINT = "forPcaConjoint";
    private String forCsEtat = null;
    private String forCsEtatPlanCalcul = null;
    private String forDateDebut = null;
    private String forDateFin = null;
    private String forDateValable = null;
    private String forDroitCsMotif = null;
    private String forExcludePcaPartiel = null;
    private String forIdDemande = null;
    private String forIdDossier = null;
    private String forIdDroit = null;
    private String forIdPCAccordee = null;
    private String forIdPcaParent = null;
    private String forIdVersionDroit = null;
    private List<String> forInIdDemande = null;
    private Boolean forIsPlanRetenu = false;
    private List<String> forInIdPcaccordee = null;

    public List<String> getForInIdPcaccordee() {
        return forInIdPcaccordee;
    }

    public void setForInIdPcaccordee(List<String> forInIdPcaccordee) {
        this.forInIdPcaccordee = forInIdPcaccordee;
    }

    private Boolean forIsSupprimee = false;
    private String forNoVersion = null;

    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForCsEtatPlanCalcul() {
        return forCsEtatPlanCalcul;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForDateValable() {
        return forDateValable;
    }

    public String getForDroitCsMotif() {
        return forDroitCsMotif;
    }

    public String getForExcludePcaPartiel() {
        return forExcludePcaPartiel;
    }

    public String getForIdDemande() {
        return forIdDemande;
    }

    public String getForIdDossier() {
        return forIdDossier;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public String getForIdPCAccordee() {
        return forIdPCAccordee;
    }

    public String getForIdPcaParent() {
        return forIdPcaParent;
    }

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public List<String> getForInIdDemande() {
        return forInIdDemande;
    }

    public Boolean getForIsPlanRetenu() {
        return forIsPlanRetenu;
    }

    public Boolean getForIsSupprimee() {
        return forIsSupprimee;
    }

    public String getForNoVersion() {
        return forNoVersion;
    }

    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForCsEtatPlanCalcul(String forCsEtatPlanCalcul) {
        this.forCsEtatPlanCalcul = forCsEtatPlanCalcul;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
    }

    public void setForDroitCsMotif(String forDroitCsMotif) {
        this.forDroitCsMotif = forDroitCsMotif;
    }

    public void setForExcludePcaPartiel(String forExcludePcaPartiel) {
        this.forExcludePcaPartiel = forExcludePcaPartiel;
    }

    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForIdPCAccordee(String forIdPCAccordee) {
        this.forIdPCAccordee = forIdPCAccordee;
    }

    public void setForIdPcaParent(String forIdPcaParent) {
        this.forIdPcaParent = forIdPcaParent;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    public void setForInIdDemande(List<String> forInIdDemande) {
        this.forInIdDemande = forInIdDemande;
    }

    public void setForIsPlanRetenu(Boolean forIsPlanRetenu) {
        this.forIsPlanRetenu = forIsPlanRetenu;
    }

    public void setForIsSupprimee(Boolean forIsSupprimee) {
        this.forIsSupprimee = forIsSupprimee;
    }

    public void setForNoVersion(String forNoVersion) {
        this.forNoVersion = forNoVersion;
    }

    @Override
    public Class<PCAccordeePlanCalcul> whichModelClass() {
        return PCAccordeePlanCalcul.class;
    }

}
