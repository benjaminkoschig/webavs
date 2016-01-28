/**
 * 
 */
package ch.globaz.amal.business.models.revenu;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author dhi
 * 
 */
public class SimpleRevenuHistorique extends JadeSimpleModel implements Cloneable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeHistorique = null;
    private Boolean codeActif = null;
    private String dateCreation = null;
    private String idContribuable = null;
    private String idRevenu = null;
    private String idRevenuDeterminant = null;
    private String idRevenuHistorique = null;
    private Boolean isRecalcul = false;

    /**
	 * 
	 */
    public SimpleRevenuHistorique() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public SimpleRevenuHistorique clone() throws CloneNotSupportedException {
        return (SimpleRevenuHistorique) super.clone();
    }

    /**
     * @return the anneeHistorique
     */
    public String getAnneeHistorique() {
        return anneeHistorique;
    }

    /**
     * @return the codeActif
     */
    public Boolean getCodeActif() {
        return codeActif;
    }

    /**
     * @return the dateCreation
     */
    public String getDateCreation() {
        return dateCreation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idRevenuHistorique;
    }

    /**
     * @return the idContribuable
     */
    public String getIdContribuable() {
        return idContribuable;
    }

    /**
     * @return the idRevenu
     */
    public String getIdRevenu() {
        return idRevenu;
    }

    /**
     * @return the idRevenuDeterminant
     */
    public String getIdRevenuDeterminant() {
        return idRevenuDeterminant;
    }

    /**
     * @return the idRevenuHistorique
     */
    public String getIdRevenuHistorique() {
        return idRevenuHistorique;
    }

    public Boolean getIsRecalcul() {
        return isRecalcul;
    }

    /**
     * @param anneeHistorique
     *            the anneeHistorique to set
     */
    public void setAnneeHistorique(String anneeHistorique) {
        this.anneeHistorique = anneeHistorique;
    }

    /**
     * @param codeActif
     *            the codeActif to set
     */
    public void setCodeActif(Boolean codeActif) {
        this.codeActif = codeActif;
    }

    /**
     * @param dateCreation
     *            the dateCreation to set
     */
    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idRevenuHistorique = id;
    }

    /**
     * @param idContribuable
     *            the idContribuable to set
     */
    public void setIdContribuable(String idContribuable) {
        this.idContribuable = idContribuable;
    }

    /**
     * @param idRevenu
     *            the idRevenu to set
     */
    public void setIdRevenu(String idRevenu) {
        this.idRevenu = idRevenu;
    }

    /**
     * @param idRevenuDeterminant
     *            the idRevenuDeterminant to set
     */
    public void setIdRevenuDeterminant(String idRevenuDeterminant) {
        this.idRevenuDeterminant = idRevenuDeterminant;
    }

    /**
     * @param idRevenuHistorique
     *            the idRevenuHistorique to set
     */
    public void setIdRevenuHistorique(String idRevenuHistorique) {
        this.idRevenuHistorique = idRevenuHistorique;
    }

    public void setIsRecalcul(Boolean isRecalcul) {
        this.isRecalcul = isRecalcul;
    }

}
