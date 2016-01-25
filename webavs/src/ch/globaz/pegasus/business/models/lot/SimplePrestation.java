/**
 * 
 */
package ch.globaz.pegasus.business.models.lot;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author SCE Modele simple pour les prestations 14 juil. 2010
 */
public class SimplePrestation extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String csEtat = null;

    private String csTypePrestation = null;
    private String dateDebut = null;
    private String dateFin = null;
    private String idCompteAnnexeConjoint;
    private String idCompteAnnexeRequerant;
    private String idLot = null;
    private String idPrestation = null;
    private String idTiersBeneficiaire = null;
    private String idVersionDroit = null;
    private String moisAn = null;
    private String montantTotal = null;

    /**
     * @return the csEtat
     */
    public String getCsEtat() {
        return csEtat;
    }

    public String getCsTypePrestation() {
        return csTypePrestation;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idPrestation;
    }

    public String getIdCompteAnnexeConjoint() {
        return idCompteAnnexeConjoint;
    }

    public String getIdCompteAnnexeRequerant() {
        return idCompteAnnexeRequerant;
    }

    /**
     * @return the idLot
     */
    public String getIdLot() {
        return idLot;
    }

    /**
     * @return the idPrestation
     */
    public String getIdPrestation() {
        return idPrestation;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    /**
     * @return the idVersionDroit
     */
    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    /**
     * @return the moisAn
     */
    public String getMoisAn() {
        return moisAn;
    }

    /**
     * @return the montantTotal
     */
    public String getMontantTotal() {
        return montantTotal;
    }

    /**
     * @param csEtat
     *            the csEtat to set
     */
    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public void setCsTypePrestation(String csTypePrestation) {
        this.csTypePrestation = csTypePrestation;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idPrestation = id;

    }

    public void setIdCompteAnnexeConjoint(String idCompteAnnexeConjoint) {
        this.idCompteAnnexeConjoint = idCompteAnnexeConjoint;
    }

    public void setIdCompteAnnexeRequerant(String idCompteAnnexeRequerant) {
        this.idCompteAnnexeRequerant = idCompteAnnexeRequerant;
    }

    /**
     * @param idLot
     *            the idLot to set
     */
    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    /**
     * @param idPrestation
     *            the idPrestation to set
     */
    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    /**
     * @param idVersionDroit
     *            the idVersionDroit to set
     */
    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    /**
     * @param moisAn
     *            the moisAn to set
     */
    public void setMoisAn(String moisAn) {
        this.moisAn = moisAn;
    }

    /**
     * @param montantTotal
     *            the montantTotal to set
     */
    public void setMontantTotal(String montantTotal) {
        this.montantTotal = montantTotal;
    }

}
