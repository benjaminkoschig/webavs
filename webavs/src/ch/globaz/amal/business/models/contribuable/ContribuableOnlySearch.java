/**
 * 
 */
package ch.globaz.amal.business.models.contribuable;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author CBU
 * 
 */
public class ContribuableOnlySearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean forContribuableActif = null;
    private String forCsSexe = null;
    private String forDateNaissance = null;
    private String forIdContribuable = null;
    private String forIdContribuableDifferent = null;
    private String forIdGestionnaire = null;
    private String forIdTier = null;
    private Boolean forIsContribuable = null;
    private String forNoContribuable = null;
    private String forNoPersonne = null;
    private String forRechercheHisto = null;
    private String forTypeNoPersonne = null;
    private String likeNom = null;
    private String likeNss = null;
    private String likePrenom = null;

    public Boolean getForContribuableActif() {
        return forContribuableActif;
    }

    /**
     * @return the forCsSexe
     */
    public String getForCsSexe() {
        return forCsSexe;
    }

    /**
     * @return the forDateNaissance
     */
    public String getForDateNaissance() {
        return forDateNaissance;
    }

    /**
     * @return the forIdContribuable
     */
    public String getForIdContribuable() {
        return forIdContribuable;
    }

    public String getForIdContribuableDifferent() {
        return forIdContribuableDifferent;
    }

    /**
     * @return the forIdGestionnaire
     */
    public String getForIdGestionnaire() {
        return forIdGestionnaire;
    }

    /**
     * @return the forIdTier
     */
    public String getForIdTier() {
        return forIdTier;
    }

    public Boolean getForIsContribuable() {
        return forIsContribuable;
    }

    /**
     * @return the forNoContribuable
     */
    public String getForNoContribuable() {
        return forNoContribuable;
    }

    /**
     * @return the forNoPersonne
     */
    public String getForNoPersonne() {
        return forNoPersonne;
    }

    public String getForRechercheHisto() {
        return forRechercheHisto;
    }

    /**
     * @return the forTypePersonne
     */
    public String getForTypeNoPersonne() {
        return forTypeNoPersonne;
    }

    /**
     * @return the likeNom
     */
    public String getLikeNom() {
        return likeNom;
    }

    /**
     * @return the likeNss
     */
    public String getLikeNss() {
        return likeNss;
    }

    /**
     * @return the likePrenom
     */
    public String getLikePrenom() {
        return likePrenom;
    }

    /**
     * @return the orderBy
     */
    public String getOrderBy() {
        return getOrderKey();
    }

    public void setForContribuableActif(Boolean forContribuableActif) {
        this.forContribuableActif = forContribuableActif;
    }

    /**
     * @param forCsSexe
     *            the forCsSexe to set
     */
    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    /**
     * @param forDateNaissance
     *            the forDateNaissance to set
     */
    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    /**
     * @param forIdContribuable
     *            the forIdContribuable to set
     */
    public void setForIdContribuable(String forIdContribuable) {
        this.forIdContribuable = forIdContribuable;
    }

    public void setForIdContribuableDifferent(String forIdContribuableDifferent) {
        this.forIdContribuableDifferent = forIdContribuableDifferent;
    }

    /**
     * @param forIdGestionnaire
     *            the forIdGestionnaire to set
     */
    public void setForIdGestionnaire(String forIdGestionnaire) {
        this.forIdGestionnaire = forIdGestionnaire;
    }

    /**
     * @param forIdTier
     *            the forIdTier to set
     */
    public void setForIdTier(String forIdTier) {
        this.forIdTier = forIdTier;
    }

    public void setForIsContribuable(Boolean forIsContribuable) {
        this.forIsContribuable = forIsContribuable;
    }

    /**
     * @param forNoContribuable
     *            the forNoContribuable to set
     */
    public void setForNoContribuable(String forNoContribuable) {
        this.forNoContribuable = forNoContribuable;
    }

    /**
     * @param forNoPersonne
     *            the forNoPersonne to set
     */
    public void setForNoPersonne(String forNoPersonne) {
        this.forNoPersonne = forNoPersonne;
    }

    public void setForRechercheHisto(String forRechercheHisto) {
        this.forRechercheHisto = forRechercheHisto;
    }

    /**
     * @param forTypePersonne
     *            the forTypePersonne to set
     */
    public void setForTypeNoPersonne(String forTypeNoPersonne) {
        this.forTypeNoPersonne = forTypeNoPersonne;
    }

    /**
     * @param likeNom
     *            the likeNom to set
     */
    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom != null ? JadeStringUtil.convertSpecialChars(likeNom).toUpperCase() : null;
    }

    /**
     * @param likeNss
     *            the likeNss to set
     */
    public void setLikeNss(String likeNss) {
        this.likeNss = likeNss;
    }

    /**
     * @param likePrenom
     *            the likePrenom to set
     */
    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom != null ? JadeStringUtil.convertSpecialChars(likePrenom).toUpperCase() : null;
    }

    /**
     * @param orderBy
     *            the orderBy to set
     */
    public void setOrderBy(String orderBy) {
        setOrderKey(orderBy);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return ContribuableOnly.class;
    }

}
