package globaz.cygnus.process;

import java.util.List;

/**
 * 
 * @author jje
 * 
 */
public class RFImportDemandesCmsData {

    private String anneeQd = "";
    private String codeTraitement = "";
    private String dateDeDebutTraitement = "";
    private String dateDeFinTraitement = "";
    private String dateFacture = "";
    private String gestionnaire = "";
    private boolean hasErrorsEtape3 = false;
    private String idDemandeValiderDecisionStep = "";
    private boolean isDerniereLigne = false;
    private boolean isPremiereLigne = false;
    private String ligne = "";
    // List<String[libelle msg erreur, code erreur]>
    private List<String[]> messagesErreursImportationList = null;
    private String montantDemande = "";
    private String montantPaye = "";
    private String montantDSAS = "";
    private List<String> motifsRefusList = null;
    private String nomPrenomTiersFichierSource = "";
    private String nomTiers = "";
    private String nssBeneficiaire = "";
    private String numDecision = "";
    private String numeroEquipe = "";
    private String numeroLigne = "";
    private String prenomTiers = "";

    public String getAnneeQd() {
        return anneeQd;
    }

    public String getMontantDSAS() {
        return montantDSAS;
    }

    public void setMontantDSAS(String montantDSAS) {
        this.montantDSAS = montantDSAS;
    }

    public String getCodeTraitement() {
        return codeTraitement;
    }

    public String getDateDeDebutTraitement() {
        return dateDeDebutTraitement;
    }

    public String getDateDeFinTraitement() {
        return dateDeFinTraitement;
    }

    public String getDateFacture() {
        return dateFacture;
    }

    public String getGestionnaire() {
        return gestionnaire;
    }

    public String getIdDemandeValiderDecisionStep() {
        return idDemandeValiderDecisionStep;
    }

    public String getLigne() {
        return ligne;
    }

    public List<String[]> getMessagesErreursImportationList() {
        return messagesErreursImportationList;
    }

    public String getMontantDemande() {
        return montantDemande;
    }

    public String getMontantPaye() {
        return montantPaye;
    }

    public List<String> getMotifsRefusList() {
        return motifsRefusList;
    }

    public String getNomPrenomTiersFichierSource() {
        return nomPrenomTiersFichierSource;
    }

    public String getNomTiers() {
        return nomTiers;
    }

    public String getNssBeneficiaire() {
        return nssBeneficiaire;
    }

    public String getNumDecision() {
        return numDecision;
    }

    public String getNumeroEquipe() {
        return numeroEquipe;
    }

    public String getNumeroLigne() {
        return numeroLigne;
    }

    public String getPrenomTiers() {
        return prenomTiers;
    }

    public boolean hasErrorsEtape3() {
        return hasErrorsEtape3;
    }

    public boolean isDerniereLigne() {
        return isDerniereLigne;
    }

    public boolean isPremiereLigne() {
        return isPremiereLigne;
    }

    public void setAnneeQd(String anneeQd) {
        this.anneeQd = anneeQd;
    }

    public void setCodeTraitement(String codeTraitement) {
        this.codeTraitement = codeTraitement;
    }

    public void setDateDeDebutTraitement(String dateDeDebutTraitement) {
        this.dateDeDebutTraitement = dateDeDebutTraitement;
    }

    public void setDateDeFinTraitement(String dateDeFinTraitement) {
        this.dateDeFinTraitement = dateDeFinTraitement;
    }

    public void setDateFacture(String dateFacture) {
        this.dateFacture = dateFacture;
    }

    public void setDerniereLigne(boolean isDerniereLigne) {
        this.isDerniereLigne = isDerniereLigne;
    }

    public void setGestionnaire(String gestionnaire) {
        this.gestionnaire = gestionnaire;
    }

    public void setHasErrorsEtape3(boolean hasErrorsEtape3) {
        this.hasErrorsEtape3 = hasErrorsEtape3;
    }

    public void setIdDemandeValiderDecisionStep(String idDemandeValiderDecisionStep) {
        this.idDemandeValiderDecisionStep = idDemandeValiderDecisionStep;
    }

    public void setLigne(String ligne) {
        this.ligne = ligne;
    }

    public void setMessagesErreursImportationsList(List<String[]> messagesErreursList) {
        messagesErreursImportationList = messagesErreursList;
    }

    public void setMontantDemande(String montantDemande) {
        this.montantDemande = montantDemande;
    }

    public void setMontantPaye(String montantPaye) {
        this.montantPaye = montantPaye;
    }

    public void setMotifsRefusList(List<String> motifsRefusList) {
        this.motifsRefusList = motifsRefusList;
    }

    public void setNomPrenomTiersFichierSource(String nomPrenomTiersFichierSource) {
        this.nomPrenomTiersFichierSource = nomPrenomTiersFichierSource;
    }

    public void setNomTiers(String nomTiers) {
        this.nomTiers = nomTiers;
    }

    public void setNssBeneficiaire(String nssBeneficiaire) {
        this.nssBeneficiaire = nssBeneficiaire;
    }

    public void setNumDecision(String numDecision) {
        this.numDecision = numDecision;
    }

    public void setNumeroEquipe(String numeroEquipe) {
        this.numeroEquipe = numeroEquipe;
    }

    public void setNumeroLigne(String numeroLigne) {
        this.numeroLigne = numeroLigne;
    }

    public void setPremiereLigne(boolean isPremiereLigne) {
        this.isPremiereLigne = isPremiereLigne;
    }

    public void setPrenomTiers(String prenomTiers) {
        this.prenomTiers = prenomTiers;
    }

}
