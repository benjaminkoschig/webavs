/**
 * 
 */
package ch.globaz.pegasus.business.models.calcul;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author ECO
 * 
 */
public class CalculVariableMetier extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csTypeVariableMetier = null;
    private String dateDebut = null;
    private String dateFin = null;
    private String fractionDenominateur = null;
    private String fractionNumerateur = null;
    private String Montant = null;
    private String Taux = null;

    /**
     * @return the csTypeVariableMetier
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
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return Montant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return the taux
     */
    public String getTaux() {
        return Taux;
    }

    public String getValue() {
        if (!JadeStringUtil.isBlankOrZero(Montant)) {
            return Montant;
        } else if (!JadeStringUtil.isBlankOrZero(Taux)) {
            return Taux;
        } else if (!JadeStringUtil.isBlankOrZero(fractionNumerateur)
                && !JadeStringUtil.isBlankOrZero(fractionDenominateur)) {
            return Float.toString(Float.valueOf(fractionNumerateur) / Float.valueOf(fractionDenominateur));
        } else {
            return null;
        }
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
        // TODO Auto-generated method stub

    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        Montant = montant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub

    }

    /**
     * @param taux
     *            the taux to set
     */
    public void setTaux(String taux) {
        Taux = taux;
    }

}
