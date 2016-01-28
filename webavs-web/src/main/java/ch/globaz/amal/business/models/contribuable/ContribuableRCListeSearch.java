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
public class ContribuableRCListeSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean forContribuableActif = null;
    private String forCsSexe = null;
    private String forDateNaissance = null;
    private String forIdContribuable = null;
    private String forIdGestionnaire = null;
    private String forIdTier = null;
    private String forNoContribuable = null;
    private String forNoPersonne = null;
    private String forNumPersonnel = null;
    private String forRechercheFamille = null;
    // private String forRechercheHisto = null;
    private String forRechercheHistorique = null;
    private String forTypeNoPersonne = null;
    private Boolean isContribuable = null;
    private String likeNom = null;
    private String likeNss = null;
    private String likePrenom = null;
    private Boolean searchInTiers = null;

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

    // public String getForRechercheHisto() {
    // return this.forRechercheHisto;
    // }

    public String getForNumPersonnel() {
        return forNumPersonnel;
    }

    public String getForRechercheFamille() {
        return forRechercheFamille;
    }

    public String getForRechercheHistorique() {
        return forRechercheHistorique;
    }

    /**
     * @return the forTypePersonne
     */
    public String getForTypeNoPersonne() {
        return forTypeNoPersonne;
    }

    public Boolean getIsContribuable() {
        return isContribuable;
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

    /**
     * Pas de recherche en db, permet de sélectionner les paramètres de recherche
     * 
     * @return the searchInTiers
     */
    public Boolean getSearchInTiers() {
        return searchInTiers;
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

    /**
     * @param forIdGestionnaire
     *            the forIdGestionnaire to set
     */
    public void setForIdGestionnaire(String forIdGestionnaire) {
        this.forIdGestionnaire = forIdGestionnaire;
    }

    // public void setForRechercheHisto(String forRechercheHisto) {
    // this.forRechercheHisto = forRechercheHisto;
    // }

    /**
     * @param forIdTier
     *            the forIdTier to set
     */
    public void setForIdTier(String forIdTier) {
        this.forIdTier = forIdTier;
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

    public void setForNumPersonnel(String forNumPersonnel) {
        this.forNumPersonnel = forNumPersonnel;
    }

    public void setForRechercheFamille(String forRechercheFamille) {
        this.forRechercheFamille = forRechercheFamille;
    }

    public void setForRechercheHistorique(String forRechercheHistorique) {
        this.forRechercheHistorique = forRechercheHistorique;
    }

    /**
     * @param forTypePersonne
     *            the forTypePersonne to set
     */
    public void setForTypeNoPersonne(String forTypeNoPersonne) {
        this.forTypeNoPersonne = forTypeNoPersonne;
    }

    public void setIsContribuable(Boolean isContribuable) {
        this.isContribuable = isContribuable;
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

    /**
     * Pas de recherche en db, permet de sélectionner les paramètres de recherche
     * 
     * @param searchInTiers
     *            the searchInTiers to set
     */
    public void setSearchInTiers(Boolean searchInTiers) {
        this.searchInTiers = searchInTiers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return ContribuableRCListe.class;
    }

}
