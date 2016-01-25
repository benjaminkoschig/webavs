/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author SCE
 * 
 *         22 sept. 2010
 */
public class DecisionSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtat = null;
    private String forCsSexe = null;
    private String forDansDernierLot = null;
    private String forDateNaissance = null;
    private String forDepuisDebutDroit = null;
    private String forDepuisValidation = null;
    private String forIdDecision = null;
    private String forIdDossier = null;
    private String forIdTiers = null;
    private String forNoDecision = null;
    private String forNoDroit = null;
    private String forPcAccorde = null;
    private String forPreparePar = null;
    private String forValidePar = null;
    private String likeNom = null;
    private String likeNss = null;
    private String likePrenom = null;

    /**
     * @return the forCsEtat
     */
    public String getForCsEtat() {
        return forCsEtat;
    }

    /**
     * @return the forCsSexe
     */
    public String getForCsSexe() {
        return forCsSexe;
    }

    /**
     * @return the forDansDernierLot
     */
    public String getForDansDernierLot() {
        return forDansDernierLot;
    }

    /**
     * @return the forDateNaissance
     */
    public String getForDateNaissance() {
        return forDateNaissance;
    }

    /**
     * @return the forDepuisDebutDroit
     */
    public String getForDepuisDebutDroit() {
        return forDepuisDebutDroit;
    }

    /**
     * @return the forDepuisValidation
     */
    public String getForDepuisValidation() {
        return forDepuisValidation;
    }

    /**
     * @return the forIdDecision
     */
    public String getForIdDecision() {
        return forIdDecision;
    }

    /**
     * @return the forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * @return the forIdTiers
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    /**
     * @return the forNoDecision
     */
    public String getForNoDecision() {
        return forNoDecision;
    }

    /**
     * @return the forNoDroit
     */
    public String getForNoDroit() {
        return forNoDroit;
    }

    /**
     * @return the forPcAccorde
     */
    public String getForPcAccorde() {
        return forPcAccorde;
    }

    /**
     * @return the forPreparePar
     */
    public String getForPreparePar() {
        return forPreparePar;
    }

    /**
     * @return the forValidePar
     */
    public String getForValidePar() {
        return forValidePar;
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
     * @param forCsEtat
     *            the forCsEtat to set
     */
    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    /**
     * @param forCsSexe
     *            the forCsSexe to set
     */
    public void setForCsSexe(String forCsSexe) {
        this.forCsSexe = forCsSexe;
    }

    /**
     * @param forDansDernierLot
     *            the forDansDernierLot to set
     */
    public void setForDansDernierLot(String forDansDernierLot) {
        this.forDansDernierLot = forDansDernierLot;
    }

    /**
     * @param forDateNaissance
     *            the forDateNaissance to set
     */
    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    /**
     * @param forDepuisDebutDroit
     *            the forDepuisDebutDroit to set
     */
    public void setForDepuisDebutDroit(String forDepuisDebutDroit) {
        this.forDepuisDebutDroit = forDepuisDebutDroit;
    }

    /**
     * @param forDepuisValidation
     *            the forDepuisValidation to set
     */
    public void setForDepuisValidation(String forDepuisValidation) {
        this.forDepuisValidation = forDepuisValidation;
    }

    /**
     * @param forIdDecision
     *            the forIdDecision to set
     */
    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    /**
     * @param forIdDossier
     *            the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    /**
     * @param forIdTiers
     *            the forIdTiers to set
     */
    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    /**
     * @param forNoDecision
     *            the forNoDecision to set
     */
    public void setForNoDecision(String forNoDecision) {
        this.forNoDecision = forNoDecision;
    }

    /**
     * @param forNoDroit
     *            the forNoDroit to set
     */
    public void setForNoDroit(String forNoDroit) {
        this.forNoDroit = forNoDroit;
    }

    /**
     * @param forPcAccorde
     *            the forPcAccorde to set
     */
    public void setForPcAccorde(String forPcAccorde) {
        this.forPcAccorde = forPcAccorde;
    }

    /**
     * @param forPreparePar
     *            the forPreparePar to set
     */
    public void setForPreparePar(String forPreparePar) {
        this.forPreparePar = forPreparePar;
    }

    /**
     * @param forValidePar
     *            the forValidePar to set
     */
    public void setForValidePar(String forValidePar) {
        this.forValidePar = forValidePar;
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
    public Class whichModelClass() {
        // TODO Auto-generated method stub
        return Decision.class;
    }

}
