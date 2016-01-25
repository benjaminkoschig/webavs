package ch.globaz.pegasus.business.models.variablemetier;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author DMA
 * 
 */
public class SimpleVariableMetier extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csTypeVariableMetier = null;
    private String dateDebut = null;
    private String dateFin = null;
    private String fractionDenominateur = null;
    private String fractionNumerateur = null;
    private String idVariableMetier = null;
    private String montant = null;
    private String taux = null;
    private String typeDeDonnee = null;

    /**
     * @return the idTypeVariableMetier
     */

    public String getCsTypeVariableMetier() {
        return csTypeVariableMetier;
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return the dateFin
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * @return the fractionDenominateur
     */
    public String getFractionDenominateur() {
        return fractionDenominateur;
    }

    /**
     * @return the fractionNumerateur
     */
    public String getFractionNumerateur() {
        return fractionNumerateur;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idVariableMetier;
    }

    /**
     * @return the idVariableMetier
     */
    public String getIdVariableMetier() {
        return idVariableMetier;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @return the taux
     */
    public String getTaux() {
        return taux;
    }

    /**
     * @return the typeDeDonnee
     */
    public String getTypeDeDonnee() {
        return typeDeDonnee;
    }

    /**
     * @param csTypeVariableMetier
     *            the csTypeVariableMetier to set
     */
    public void setCsTypeVariableMetier(String csTypeVariableMetier) {
        this.csTypeVariableMetier = csTypeVariableMetier;
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * @param fractionDenominateur
     *            the fractionDenominateur to set
     */
    public void setFractionDenominateur(String fractionDenominateur) {
        this.fractionDenominateur = fractionDenominateur;
    }

    /**
     * @param fractionNumerateur
     *            the fractionNumerateur to set
     */
    public void setFractionNumerateur(String fractionNumerateur) {
        this.fractionNumerateur = fractionNumerateur;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idVariableMetier = id;
    }

    /**
     * @param idVariableMetier
     *            the idVariableMetier to set
     */
    public void setIdVariableMetier(String idVariableMetier) {
        this.idVariableMetier = idVariableMetier;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * @param taux
     *            the taux to set
     */
    public void setTaux(String taux) {
        this.taux = taux;
    }

    /**
     * @param typeDeDonnee
     *            the typeDeDonnee to set
     */
    public void setTypeDeDonnee(String typeDeDonnee) {
        this.typeDeDonnee = typeDeDonnee;
    }

}
