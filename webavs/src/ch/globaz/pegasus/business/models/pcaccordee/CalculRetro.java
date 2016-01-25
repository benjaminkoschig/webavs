package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeComplexModel;

public class CalculRetro extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // private String csEtatPC = null;
    // private String csGenrePC = null;
    // private String csRoleBeneficiaire = null
    // private String dateDebut = null;
    // private String idPCAccordee = null;
    // private String dateFin = null;
    private String dateDecision = null;
    private String dateProchainPaiement = null;
    private String idTiersBeneficiaire = null;
    private String idTiersConjoint = null;
    private String montantPCMensuelle = null;
    private String nom = null;
    private String nomConj = null;
    private String noVersion = null;
    private String prenom = null;
    private String prenomConj = null;
    private SimplePCAccordee simplePCAccordee = null;
    private String sousCodePresation = null;

    public CalculRetro() {
        super();
        simplePCAccordee = new SimplePCAccordee();

    }

    public String getCsEtatPC() {
        return simplePCAccordee.getCsEtatPC();
    }

    public String getCsGenrePC() {
        return simplePCAccordee.getCsGenrePC();
    }

    public String getCsRoleBeneficiaire() {
        return simplePCAccordee.getCsRoleBeneficiaire();
    }

    public String getCsTypePC() {
        return simplePCAccordee.getCsTypePC();
    }

    public String getDateDebut() {
        return simplePCAccordee.getDateDebut();
    }

    public String getDateDecision() {
        return dateDecision;
    }

    public String getDateFin() {
        return simplePCAccordee.getDateFin();
    }

    public String getDateProchainPaiement() {
        return dateProchainPaiement;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return getIdPCAccordee();
    }

    public String getIdPCAccordee() {
        return simplePCAccordee.getIdPCAccordee();
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public String getIdTiersConjoint() {
        return idTiersConjoint;
    }

    public String getMontantPCMensuelle() {
        return montantPCMensuelle;
    }

    public String getNom() {
        return nom;
    }

    public String getNomConj() {
        return nomConj;
    }

    public String getNoVersion() {
        return noVersion;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getPrenomConj() {
        return prenomConj;
    }

    public SimplePCAccordee getSimplePCAccordee() {
        return simplePCAccordee;
    }

    public String getSousCodePresation() {
        return sousCodePresation;
    }

    @Override
    public String getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setCsEtatPC(String csEtatPC) {
        simplePCAccordee.setCsEtatPC(csEtatPC);
    }

    public void setCsGenrePC(String csGenrePC) {
        simplePCAccordee.setCsGenrePC(csGenrePC);
    }

    public void setCsRoleBeneficiaire(String csRoleBeneficiaire) {
        simplePCAccordee.setCsRoleBeneficiaire(csRoleBeneficiaire);
    }

    public void setDateDebut(String dateDebut) {
        simplePCAccordee.setDateDebut(dateDebut);
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    public void setDateFin(String dateFin) {
        simplePCAccordee.setDateFin(dateFin);
    }

    public void setDateProchainPaiement(String dateProchainPaiement) {
        this.dateProchainPaiement = dateProchainPaiement;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub

    }

    public void setIdPCAccordee(String idPCAccordee) {
        simplePCAccordee.setIdPCAccordee(idPCAccordee);
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setIdTiersConjoint(String idTiersConjoint) {
        this.idTiersConjoint = idTiersConjoint;
    }

    public void setMontantPCMensuelle(String montantPCMensuelle) {
        this.montantPCMensuelle = montantPCMensuelle;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNomConj(String nomConj) {
        this.nomConj = nomConj;
    }

    public void setNoVersion(String noVersion) {
        this.noVersion = noVersion;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setPrenomConj(String prenomConj) {
        this.prenomConj = prenomConj;
    }

    public void setSimplePCAccordee(SimplePCAccordee simplePCAccordee) {
        this.simplePCAccordee = simplePCAccordee;
    }

    public void setSousCodePresation(String sousCodePresation) {
        this.sousCodePresation = sousCodePresation;
    }

    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "CalculRetro [ dateDebut=" + simplePCAccordee.getDateDebut() + ", dateFin="
                + simplePCAccordee.getDateFin() + ", montantPCMensuelle=" + montantPCMensuelle + ", dateDecision="
                + dateDecision + ", dateProchainPaiement=" + dateProchainPaiement + ", nom=" + nom + ", noVersion="
                + noVersion + ", idPca=" + simplePCAccordee.getIdPCAccordee() + ", sousCodePresation="
                + sousCodePresation + "]";
    }

}
