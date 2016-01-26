/**
 * 
 */
package ch.globaz.perseus.business.models.qd;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

/**
 * @author DDE
 * 
 */
public class FactureSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = null;
    private String forCsEtatFacture = null;
    private String forCSTypeQD = null;
    private String forIdDossier = null;
    private String forIdGestionnaire = null;
    private String forIdMembreFamille = null;
    private String forIdQd = null;
    private Boolean forIsHygienisteDentaire = null;
    private String forNumDecision = null;
    private List<String> inCsEtatFacture = null;
    private String likeNom = null;
    private String likeNss = null;
    private String likePrenom = null;
    private String likeDossierNss = null;

    public String getLikeDossierNss() {
        return likeDossierNss;
    }

    public void setLikeDossierNss(String likeDossierNss) {
        this.likeDossierNss = likeDossierNss;
    }

    /**
     * @return the forAnnee
     */
    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * @return the forCsEtatFacture
     */
    public String getForCsEtatFacture() {
        return forCsEtatFacture;
    }

    /**
     * @return the forCSTypeQD
     */
    public String getForCSTypeQD() {
        return forCSTypeQD;
    }

    /**
     * @return the forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * @return the forIdGestionnaire
     */
    public String getForIdGestionnaire() {
        return forIdGestionnaire;
    }

    public String getForIdMembreFamille() {
        return forIdMembreFamille;
    }

    /**
     * @return the forIdQd
     */
    public String getForIdQd() {
        return forIdQd;
    }

    public Boolean getForIsHygienisteDentaire() {
        return forIsHygienisteDentaire;
    }

    /**
     * @return the forNumDecision
     */
    public String getForNumDecision() {
        return forNumDecision;
    }

    public List<String> getInCsEtatFacture() {
        return inCsEtatFacture;
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
     * @param forAnnee
     *            the forAnnee to set
     */
    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    /**
     * @param forCsEtatFacture
     *            the forCsEtatFacture to set
     */
    public void setForCsEtatFacture(String forCsEtatFacture) {
        this.forCsEtatFacture = forCsEtatFacture;
    }

    /**
     * @param forCSTypeQD
     *            the forCSTypeQD to set
     */
    public void setForCSTypeQD(String forCSTypeQD) {
        this.forCSTypeQD = forCSTypeQD;
    }

    /**
     * @param forIdDossier
     *            the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    /**
     * @param forIdGestionnaire
     *            the forIdGestionnaire to set
     */
    public void setForIdGestionnaire(String forIdGestionnaire) {
        this.forIdGestionnaire = forIdGestionnaire;
    }

    public void setForIdMembreFamille(String forIdMembreFamille) {
        this.forIdMembreFamille = forIdMembreFamille;
    }

    /**
     * @param forIdQd
     *            the forIdQd to set
     */
    public void setForIdQd(String forIdQd) {
        this.forIdQd = forIdQd;
    }

    public void setForIsHygienisteDentaire(Boolean forIsHygienisteDentaire) {
        this.forIsHygienisteDentaire = forIsHygienisteDentaire;
    }

    /**
     * @param forNumDecision
     *            the forNumDecision to set
     */
    public void setForNumDecision(String forNumDecision) {
        this.forNumDecision = forNumDecision;
    }

    public void setInCsEtatFacture(List<String> inCsEtatFacture) {
        this.inCsEtatFacture = inCsEtatFacture;
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<Facture> whichModelClass() {
        return Facture.class;
    }

}
