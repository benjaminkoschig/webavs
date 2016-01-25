/**
 * 
 */
package ch.globaz.pegasus.business.models.calcul;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author ECO
 * 
 */
public class CalculDonneesDroit extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csTypeDonneeFinanciere = null;
    private String dateDebutDonneeFinanciere = null;
    private String dateDeces = null;
    private String dateFinDonneeFinanciere = null;
    private String dateNaissance = null;
    private String idDroit = null;
    private String idMembreFamille = null;
    private String noVersion = null;
    private String roleFamillePc = null;

    /**
	 * 
	 */
    public CalculDonneesDroit() {
        super();
    }

    public String getCsTypeDonneeFinanciere() {
        return csTypeDonneeFinanciere;
    }

    /**
     * @return the dateDebutDonneeFinanciere
     */
    public String getDateDebutDonneeFinanciere() {
        return dateDebutDonneeFinanciere;
    }

    /**
     * @return the dateDeces
     */
    public String getDateDeces() {
        return dateDeces;
    }

    /**
     * @return the dateFinDonneeFinanciere
     */
    public String getDateFinDonneeFinanciere() {
        return dateFinDonneeFinanciere;
    }

    /**
     * @return the dateNaissance
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idDroit;
    }

    /**
     * @return the idDroit
     */
    public String getIdDroit() {
        return idDroit;
    }

    /**
     * @return the idMembreFamille
     */
    public String getIdMembreFamille() {
        return idMembreFamille;
    }

    /**
     * @return the noVersion
     */
    public String getNoVersion() {
        return noVersion;
    }

    /**
     * @return the roleFamillePc
     */
    public String getRoleFamillePc() {
        return roleFamillePc;
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

    public void setCsTypeDonneeFinanciere(String csTypeDonneeFinanciere) {
        this.csTypeDonneeFinanciere = csTypeDonneeFinanciere;
    }

    /**
     * @param dateDebutDonneeFinanciere
     *            the dateDebutDonneeFinanciere to set
     */
    public void setDateDebutDonneeFinanciere(String dateDebutDonneeFinanciere) {
        this.dateDebutDonneeFinanciere = dateDebutDonneeFinanciere;
    }

    /**
     * @param dateDeces
     *            the dateDeces to set
     */
    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    /**
     * @param dateFinDonneeFinanciere
     *            the dateFinDonneeFinanciere to set
     */
    public void setDateFinDonneeFinanciere(String dateFinDonneeFinanciere) {
        this.dateFinDonneeFinanciere = dateFinDonneeFinanciere;
    }

    /**
     * @param dateNaissance
     *            the dateNaissance to set
     */
    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idDroit = id;
    }

    /**
     * @param idDroit
     *            the idDroit to set
     */
    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    /**
     * @param idMembreFamille
     *            the idMembreFamille to set
     */
    public void setIdMembreFamille(String idMembreFamille) {
        this.idMembreFamille = idMembreFamille;
    }

    /**
     * @param noVersion
     *            the noVersion to set
     */
    public void setNoVersion(String noVersion) {
        this.noVersion = noVersion;
    }

    /**
     * @param roleFamillePc
     *            the roleFamillePc to set
     */
    public void setRoleFamillePc(String roleFamillePc) {
        this.roleFamillePc = roleFamillePc;
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

}
