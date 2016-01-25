package ch.globaz.osiris.business.model;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author SCO
 */
public class OperationSimpleModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codeDebitCredit;
    private String date;
    private String etat;
    private String idCompte; // Attention : idExterne rubrique !
    private String idRubrique;
    private String idCompteAnnexe;
    private String idJournal;
    private String idOperation;
    private String idSection;
    private String idTypeOperation;
    private String idCaisseProf;
    private String libelle;
    private String montant;
    private String masse;
    private String taux;
    private String annee;

    public String getCodeDebitCredit() {
        return codeDebitCredit;
    }

    public String getDate() {
        return date;
    }

    public String getEtat() {
        return etat;
    }

    @Override
    public String getId() {
        return getIdOperation();
    }

    public String getIdCompte() {
        return idCompte;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public String getIdOperation() {
        return idOperation;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getIdTypeOperation() {
        return idTypeOperation;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getMontant() {
        return montant;
    }

    public void setCodeDebitCredit(String codeDebitCredit) {
        this.codeDebitCredit = codeDebitCredit;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    @Override
    public void setId(String id) {
        setIdOperation(id);
    }

    public void setIdCompte(String idCompte) {
        this.idCompte = idCompte;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public void setIdOperation(String idOperation) {
        this.idOperation = idOperation;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    protected void setIdTypeOperation(String idTypeOperation) {
        this.idTypeOperation = idTypeOperation;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }
    
    /**
     * @return the idCaisseProf
     */
    public String getIdCaisseProf() {
        return idCaisseProf;
    }

    /**
     * @param idCaisseProf the idCaisseProf to set
     */
    public void setIdCaisseProf(String idCaisseProf) {
        this.idCaisseProf = idCaisseProf;
    }

    /**
     * @return the idRubrique
     */
    public String getIdRubrique() {
        return idRubrique;
    }

    /**
     * @param idRubrique the idRubrique to set
     */
    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    /**
     * @return the masse
     */
    public String getMasse() {
        return masse;
    }

    /**
     * @return the taux
     */
    public String getTaux() {
        return taux;
    }

    /**
     * @return the annee
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * @param masse the masse to set
     */
    public void setMasse(String masse) {
        this.masse = masse;
    }

    /**
     * @param taux the taux to set
     */
    public void setTaux(String taux) {
        this.taux = taux;
    }

    /**
     * @param annee the annee to set
     */
    public void setAnnee(String annee) {
        this.annee = annee;
    }
}
