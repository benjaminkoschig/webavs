package ch.globaz.pegasus.business.models.home;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;

public class HomeSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCanton = null;
    private String forIdHome = null;
    private String forIdTiersHome = null;
    private String forNomBatiment = null;
    private String forNumeroIdentification = null;
    private String forTypeAdresse = null;
    private String likeDesignation = null;
    private String likeLocalite = null;
    private String likeNpa = null;
    private String likeNumeroIdentification = null;
    private String notForIdHome = null;

    /**
     * retourne la condition de recherche sur le canton dans lequel se situe un home
     * 
     * @return the forCanton
     */
    public String getForCanton() {
        return forCanton;
    }

    /**
     * @return the forIdHome
     */
    public String getForIdHome() {
        return forIdHome;
    }

    /**
     * @return the forIdTiersHome
     */
    public String getForIdTiersHome() {
        return forIdTiersHome;
    }

    /**
     * @return the forNomBatiment
     */
    public String getForNomBatiment() {
        return forNomBatiment;
    }

    /**
     * retourne la condition de recherche sur le numero d'identification d'un home
     * 
     * @return the forNumeroIdentification
     */
    public String getForNumeroIdentification() {
        return forNumeroIdentification;
    }

    /**
     * @return the forTypeAdresse
     */
    public String getForTypeAdresse() {
        return forTypeAdresse;
    }

    /**
     * retourne la condition de recherche sur la designation d'un home
     * 
     * @return the likeDesignation
     */
    public String getLikeDesignation() {
        return likeDesignation;
    }

    public String getLikeLocalite() {
        return likeLocalite;
    }

    public String getLikeNpa() {
        return likeNpa;
    }

    public String getLikeNumeroIdentification() {
        return likeNumeroIdentification;
    }

    /**
     * @return the notForIdHome
     */
    public String getNotForIdHome() {
        return notForIdHome;
    }

    /**
     * définit la condition de recherche sur le canton dans lequel se situe un home
     * 
     * @param forCanton
     *            the forCanton to set
     */
    public void setForCanton(String forCanton) {
        this.forCanton = forCanton;
    }

    /**
     * @param forIdHome
     *            the forIdHome to set
     */
    public void setForIdHome(String forIdHome) {
        this.forIdHome = forIdHome;
    }

    /**
     * @param forIdTiersHome
     *            the forIdTiersHome to set
     */
    public void setForIdTiersHome(String forIdTiersHome) {
        this.forIdTiersHome = forIdTiersHome;
    }

    /**
     * @param forNomBatiment
     *            the forNomBatiment to set
     */
    public void setForNomBatiment(String forNomBatiment) {
        this.forNomBatiment = forNomBatiment;
    }

    /**
     * définit la condition de recherche sur le numero d'identification d'un home
     * 
     * @param forNumeroIdentification
     *            the forNumeroIdentification to set
     */
    public void setForNumeroIdentification(String forNumeroIdentification) {
        this.forNumeroIdentification = forNumeroIdentification;
    }

    /**
     * @param forTypeAdresse
     *            the forTypeAdresse to set
     */
    public void setForTypeAdresse(String forTypeAdresse) {
        this.forTypeAdresse = forTypeAdresse;
    }

    /**
     * définit la condition de recherche sur la designation d'un home
     * 
     * @param likeDesignation
     *            the likeDesignation to set
     */
    public void setLikeDesignation(String likeDesignation) {
        this.likeDesignation = likeDesignation != null ? "%"
                + JadeStringUtil.convertSpecialChars(likeDesignation).toUpperCase() : null;
    }

    public void setLikeLocalite(String likeLocalite) {
        this.likeLocalite = likeLocalite;
    }

    public void setLikeNpa(String likeNpa) {
        this.likeNpa = likeNpa;
    }

    /**
     * Si Integer.parseInt(npaOrLocalite) provoque un number FormatException, c'est une localite. Sinon c'est un npa.
     * 
     * @param npaOrLocalite
     *            le NPA ou la localite a rechercher
     */
    public void setLikeNpaOrLocalite(String npaOrLocalite) {

        try {
            Integer.parseInt(npaOrLocalite);
            setLikeNpa(npaOrLocalite);

        } catch (NumberFormatException e) {
            // si ce n'est pas un nombre c'est une localite
            setLikeLocalite(npaOrLocalite);
        }
    }

    public void setLikeNumeroIdentification(String likeNumeroIdentification) {
        this.likeNumeroIdentification = likeNumeroIdentification;
    }

    /**
     * @param notForIdHome
     *            the notForIdHome to set
     */
    public void setNotForIdHome(String notForIdHome) {
        this.notForIdHome = notForIdHome;
    }

    @Override
    public Class whichModelClass() {
        return Home.class;
    }

}
