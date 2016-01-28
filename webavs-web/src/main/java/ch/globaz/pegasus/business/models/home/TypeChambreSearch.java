package ch.globaz.pegasus.business.models.home;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class TypeChambreSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsCategorie = null;
    private String forDesignation = null;
    private String forHomeTypeAdresse = null;
    private String forIdHome = null;
    private String forIdTiersParticularite = null;
    private String forIdTypeChambre = null;
    private Boolean forIsApiFacturee = null;
    private Boolean forIsParticularite = null;
    private String notForIdTypeChambre = null;

    /**
     * @return the forCsCategorie
     */
    public String getForCsCategorie() {
        return forCsCategorie;
    }

    /**
     * @return the forDesignation
     */
    public String getForDesignation() {
        return forDesignation;
    }

    public String getForHomeTypeAdresse() {
        return forHomeTypeAdresse;
    }

    /**
     * retourne la condition de recherche sur l'id home pour lequel ce type de chambre est valable
     * 
     * @return the forIdHome
     */
    public String getForIdHome() {
        return forIdHome;
    }

    /**
     * @return the forIdTiersParticularite
     */
    public String getForIdTiersParticularite() {
        return forIdTiersParticularite;
    }

    /**
     * @return the forIdTypeChambre
     */
    public String getForIdTypeChambre() {
        return forIdTypeChambre;
    }

    /**
     * @return the forIsApiFacturee
     */
    public Boolean getForIsApiFacturee() {
        return forIsApiFacturee;
    }

    /**
     * @return the forIsParticularite
     */
    public Boolean getForIsParticularite() {
        return forIsParticularite;
    }

    /**
     * @return the notForIdTypeChambre
     */
    public String getNotForIdTypeChambre() {
        return notForIdTypeChambre;
    }

    /**
     * @param forCsCategorie
     *            the forCsCategorie to set
     */
    public void setForCsCategorie(String forCsCategorie) {
        this.forCsCategorie = forCsCategorie;
    }

    /**
     * @param forDesignation
     *            the forDesignation to set
     */
    public void setForDesignation(String forDesignation) {
        this.forDesignation = forDesignation;
    }

    public void setForHomeTypeAdresse(String forHomeTypeAdresse) {
        this.forHomeTypeAdresse = forHomeTypeAdresse;
    }

    /**
     * définit la condition de recherche sur l'id home pour lequel ce type de chambre est valable
     * 
     * @param forIdHome
     *            the forIdHome to set
     */
    public void setForIdHome(String forIdHome) {
        this.forIdHome = forIdHome;
    }

    /**
     * @param forIdTiersParticularite
     *            the forIdTiersParticularite to set
     */
    public void setForIdTiersParticularite(String forIdTiersParticularite) {
        this.forIdTiersParticularite = forIdTiersParticularite;
    }

    /**
     * @param forIdtypeChambre
     *            the forIdtypeChambre to set
     */
    public void setForIdTypeChambre(String forIdTypeChambre) {
        this.forIdTypeChambre = forIdTypeChambre;
    }

    /**
     * @param forIsApiFacturee
     *            the forIsApiFacturee to set
     */
    public void setForIsApiFacturee(Boolean forIsApiFacturee) {
        this.forIsApiFacturee = forIsApiFacturee;
    }

    /**
     * @param forIsParticularite
     *            the forIsParticularite to set
     */
    public void setForIsParticularite(Boolean forIsParticularite) {
        this.forIsParticularite = forIsParticularite;
    }

    /**
     * @param notForIdTypeChambre
     *            the notForIdTypeChambre to set
     */
    public void setNotForIdTypeChambre(String notForIdTypeChambre) {
        this.notForIdTypeChambre = notForIdTypeChambre;
    }

    @Override
    public Class whichModelClass() {
        return TypeChambre.class;
    }

}
