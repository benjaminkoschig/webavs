package ch.globaz.corvus.business.models.rentesaccordees;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleRenteAccordee extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeAnticipation;
    private String anneeMontantRAM;
    private String cleRegroupementDecision;
    private String codeAuxilliaire;
    private String codeCasSpeciaux1;
    private String codeCasSpeciaux2;
    private String codeCasSpeciaux3;
    private String codeCasSpeciaux4;
    private String codeCasSpeciaux5;
    private String codeMutation;
    private String codeRefugie;
    private String codeSurvivantInvalide;
    private String csEtatCivil;
    private String csRelationAuRequerant;
    private String dateDebutAnticipation;
    private String dateFinDroitPrevueEcheance;
    private String dateRevocationAjournement;
    private String dureeAjournement;
    private String idBaseCalcul;
    private String idRenteAccordee;
    private String idTiersBaseCalcul;
    private String idTiersComplementaire1;
    private String idTiersComplementaire2;
    private Boolean isTraitementManuel;
    private String montantReducationAnticipation;
    private String montantRenteOrdiRemplacee;
    private String prescriptionAppliquee;
    private String reductionFauteGrave;
    private String remarques;
    private String supplementAjournement;
    private String supplementVeuvage;
    private String tauxReductionAnticipation;
    private String csGenreDroitApi;

    public SimpleRenteAccordee() {
        super();

        anneeAnticipation = "";
        anneeMontantRAM = "";
        cleRegroupementDecision = "";
        codeAuxilliaire = "";
        codeCasSpeciaux1 = "";
        codeCasSpeciaux2 = "";
        codeCasSpeciaux3 = "";
        codeCasSpeciaux4 = "";
        codeCasSpeciaux5 = "";
        codeMutation = "";
        codeRefugie = "";
        codeSurvivantInvalide = "";
        csEtatCivil = "";
        csRelationAuRequerant = "";
        dateDebutAnticipation = "";
        dateFinDroitPrevueEcheance = "";
        dateRevocationAjournement = "";
        dureeAjournement = "";
        idBaseCalcul = "";
        idRenteAccordee = "";
        idTiersBaseCalcul = "";
        idTiersComplementaire1 = "";
        idTiersComplementaire2 = "";
        isTraitementManuel = Boolean.FALSE;
        montantReducationAnticipation = "";
        montantRenteOrdiRemplacee = "";
        prescriptionAppliquee = "";
        reductionFauteGrave = "";
        remarques = "";
        supplementAjournement = "";
        supplementVeuvage = "";
        tauxReductionAnticipation = "";
        csGenreDroitApi = "";
    }

    public String getAnneeAnticipation() {
        return anneeAnticipation;
    }

    public String getAnneeMontantRAM() {
        return anneeMontantRAM;
    }

    public String getCleRegroupementDecision() {
        return cleRegroupementDecision;
    }

    public String getCodeAuxilliaire() {
        return codeAuxilliaire;
    }

    public String getCodeCasSpeciaux1() {
        return codeCasSpeciaux1;
    }

    public String getCodeCasSpeciaux2() {
        return codeCasSpeciaux2;
    }

    public String getCodeCasSpeciaux3() {
        return codeCasSpeciaux3;
    }

    public String getCodeCasSpeciaux4() {
        return codeCasSpeciaux4;
    }

    public String getCodeCasSpeciaux5() {
        return codeCasSpeciaux5;
    }

    public String getCodeMutation() {
        return codeMutation;
    }

    public String getCodeRefugie() {
        return codeRefugie;
    }

    public String getCodeSurvivantInvalide() {
        return codeSurvivantInvalide;
    }

    public String getCsEtatCivil() {
        return csEtatCivil;
    }

    public String getCsRelationAuRequerant() {
        return csRelationAuRequerant;
    }

    public String getDateDebutAnticipation() {
        return dateDebutAnticipation;
    }

    public String getDateFinDroitPrevueEcheance() {
        return dateFinDroitPrevueEcheance;
    }

    public String getDateRevocationAjournement() {
        return dateRevocationAjournement;
    }

    public String getDureeAjournement() {
        return dureeAjournement;
    }

    @Override
    public String getId() {
        return idRenteAccordee;
    }

    public String getIdBaseCalcul() {
        return idBaseCalcul;
    }

    public String getIdRenteAccordee() {
        return getId();
    }

    public String getIdTiersBaseCalcul() {
        return idTiersBaseCalcul;
    }

    public String getIdTiersComplementaire1() {
        return idTiersComplementaire1;
    }

    public String getIdTiersComplementaire2() {
        return idTiersComplementaire2;
    }

    public Boolean getIsTraitementManuel() {
        return isTraitementManuel;
    }

    public String getMontantReducationAnticipation() {
        return montantReducationAnticipation;
    }

    public String getMontantRenteOrdiRemplacee() {
        return montantRenteOrdiRemplacee;
    }

    public String getPrescriptionAppliquee() {
        return prescriptionAppliquee;
    }

    public String getReductionFauteGrave() {
        return reductionFauteGrave;
    }

    public String getRemarques() {
        return remarques;
    }

    public String getSupplementAjournement() {
        return supplementAjournement;
    }

    public String getSupplementVeuvage() {
        return supplementVeuvage;
    }

    public String getTauxReductionAnticipation() {
        return tauxReductionAnticipation;
    }

    public void setAnneeAnticipation(String anneeAnticipation) {
        this.anneeAnticipation = anneeAnticipation;
    }

    public void setAnneeMontantRAM(String anneeMontantRAM) {
        this.anneeMontantRAM = anneeMontantRAM;
    }

    public void setCleRegroupementDecision(String cleRegroupementDecision) {
        this.cleRegroupementDecision = cleRegroupementDecision;
    }

    public void setCodeAuxilliaire(String codeAuxilliaire) {
        this.codeAuxilliaire = codeAuxilliaire;
    }

    public void setCodeCasSpeciaux1(String codeCasSpeciaux1) {
        this.codeCasSpeciaux1 = codeCasSpeciaux1;
    }

    public void setCodeCasSpeciaux2(String codeCasSpeciaux2) {
        this.codeCasSpeciaux2 = codeCasSpeciaux2;
    }

    public void setCodeCasSpeciaux3(String codeCasSpeciaux3) {
        this.codeCasSpeciaux3 = codeCasSpeciaux3;
    }

    public void setCodeCasSpeciaux4(String codeCasSpeciaux4) {
        this.codeCasSpeciaux4 = codeCasSpeciaux4;
    }

    public void setCodeCasSpeciaux5(String codeCasSpeciaux5) {
        this.codeCasSpeciaux5 = codeCasSpeciaux5;
    }

    public void setCodeMutation(String codeMutation) {
        this.codeMutation = codeMutation;
    }

    public void setCodeRefugie(String codeRefugie) {
        this.codeRefugie = codeRefugie;
    }

    public void setCodeSurvivantInvalide(String codeSurvivantInvalide) {
        this.codeSurvivantInvalide = codeSurvivantInvalide;
    }

    public void setCsEtatCivil(String csEtatCivil) {
        this.csEtatCivil = csEtatCivil;
    }

    public void setCsRelationAuRequerant(String csRelationAuRequerant) {
        this.csRelationAuRequerant = csRelationAuRequerant;
    }

    public void setDateDebutAnticipation(String dateDebutAnticipation) {
        this.dateDebutAnticipation = dateDebutAnticipation;
    }

    public void setDateFinDroitPrevueEcheance(String dateFinDroitPrevueEcheance) {
        this.dateFinDroitPrevueEcheance = dateFinDroitPrevueEcheance;
    }

    public void setDateRevocationAjournement(String dateRevocationAjournement) {
        this.dateRevocationAjournement = dateRevocationAjournement;
    }

    public void setDureeAjournement(String dureeAjournement) {
        this.dureeAjournement = dureeAjournement;
    }

    @Override
    public void setId(String id) {
        idRenteAccordee = id;
    }

    public void setIdBaseCalcul(String idBaseCalcul) {
        this.idBaseCalcul = idBaseCalcul;
    }

    public void setIdRenteAccordee(String idRenteAccordee) {
        setId(idRenteAccordee);
    }

    public void setIdTiersBaseCalcul(String idTiersBaseCalcul) {
        this.idTiersBaseCalcul = idTiersBaseCalcul;
    }

    public void setIdTiersComplementaire1(String idTiersComplementaire1) {
        this.idTiersComplementaire1 = idTiersComplementaire1;
    }

    public void setIdTiersComplementaire2(String idTiersComplementaire2) {
        this.idTiersComplementaire2 = idTiersComplementaire2;
    }

    public void setIsTraitementManuel(Boolean isTraitementManuel) {
        this.isTraitementManuel = isTraitementManuel;
    }

    public void setMontantReducationAnticipation(String montantReducationAnticipation) {
        this.montantReducationAnticipation = montantReducationAnticipation;
    }

    public void setMontantRenteOrdiRemplacee(String montantRenteOrdiRemplacee) {
        this.montantRenteOrdiRemplacee = montantRenteOrdiRemplacee;
    }

    public void setPrescriptionAppliquee(String prescriptionAppliquee) {
        this.prescriptionAppliquee = prescriptionAppliquee;
    }

    public void setReductionFauteGrave(String reductionFauteGrave) {
        this.reductionFauteGrave = reductionFauteGrave;
    }

    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }

    public void setSupplementAjournement(String supplementAjournement) {
        this.supplementAjournement = supplementAjournement;
    }

    public void setSupplementVeuvage(String supplementVeuvage) {
        this.supplementVeuvage = supplementVeuvage;
    }

    public void setTauxReductionAnticipation(String tauxReductionAnticipation) {
        this.tauxReductionAnticipation = tauxReductionAnticipation;
    }

    public String getCsGenreDroitApi() {
        return csGenreDroitApi;
    }

    public void setCsGenreDroitApi(String csGenreDroitApi) {
        this.csGenreDroitApi = csGenreDroitApi;
    }

}
