package ch.globaz.vulpecula.business.models.congepaye;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Mapping de la table PT_CONGE_PAYE
 * 
 * @since WebBMS 0.01.04
 */
public class CongePayeSimpleModel extends JadeSimpleModel {

    private static final long serialVersionUID = 8429347818568912832L;

    private String id;
    private String idPosteTravail;
    private String idPassageFacturation;
    private String etat;
    private String anneeDebut;
    private String anneeFin;
    private String salaireNonDeclare;
    private String dateSalaireNonDeclare;
    private String tauxCP;
    private String beneficiaire;

    private String totalSalaire;
    private String salaireDeclare;
    private String montantNet;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String arg0) {
        id = arg0;
    }

    /**
     * @return the idPosteTravail
     */
    public String getIdPosteTravail() {
        return idPosteTravail;
    }

    /**
     * @param idPosteTravail the idPosteTravail to set
     */
    public void setIdPosteTravail(String idPosteTravail) {
        this.idPosteTravail = idPosteTravail;
    }

    /**
     * @return the etat
     */
    public String getEtat() {
        return etat;
    }

    /**
     * @param etat the etat to set
     */
    public void setEtat(String etat) {
        this.etat = etat;
    }

    /**
     * @return the anneeDebut
     */
    public String getAnneeDebut() {
        return anneeDebut;
    }

    /**
     * @param anneeDebut the anneeDebut to set
     */
    public void setAnneeDebut(String anneeDebut) {
        this.anneeDebut = anneeDebut;
    }

    /**
     * @return the anneeFin
     */
    public String getAnneeFin() {
        return anneeFin;
    }

    /**
     * @param anneeFin the anneeFin to set
     */
    public void setAnneeFin(String anneeFin) {
        this.anneeFin = anneeFin;
    }

    /**
     * @return the salaireNonDeclare
     */
    public String getSalaireNonDeclare() {
        return salaireNonDeclare;
    }

    /**
     * @param salaireNonDeclare the salaireNonDeclare to set
     */
    public void setSalaireNonDeclare(String salaireNonDeclare) {
        this.salaireNonDeclare = salaireNonDeclare;
    }

    /**
     * @return the dateSalaireNonDeclare
     */
    public String getDateSalaireNonDeclare() {
        return dateSalaireNonDeclare;
    }

    /**
     * @param dateSalaireNonDeclare the dateSalaireNonDeclare to set
     */
    public void setDateSalaireNonDeclare(String dateSalaireNonDeclare) {
        this.dateSalaireNonDeclare = dateSalaireNonDeclare;
    }

    /**
     * @return the idPassageFacturation
     */
    public String getIdPassageFacturation() {
        return idPassageFacturation;
    }

    /**
     * @param idPassageFacturation the idPassageFacturation to set
     */
    public void setIdPassageFacturation(String idPassageFacturation) {
        this.idPassageFacturation = idPassageFacturation;
    }

    /**
     * @return the tauxCP
     */
    public String getTauxCP() {
        return tauxCP;
    }

    /**
     * @param tauxCP the tauxCP to set
     */
    public void setTauxCP(String tauxCP) {
        this.tauxCP = tauxCP;
    }

    /**
     * @return the beneficiaire
     */
    public String getBeneficiaire() {
        return beneficiaire;
    }

    /**
     * @param beneficiaire the beneficiaire to set
     */
    public void setBeneficiaire(String beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public String getTotalSalaire() {
        return totalSalaire;
    }

    public void setTotalSalaire(String totalSalaire) {
        this.totalSalaire = totalSalaire;
    }

    public String getSalaireDeclare() {
        return salaireDeclare;
    }

    public void setSalaireDeclare(String salaireDeclare) {
        this.salaireDeclare = salaireDeclare;
    }

    public String getMontantNet() {
        return montantNet;
    }

    public void setMontantNet(String montantNet) {
        this.montantNet = montantNet;
    }

}
