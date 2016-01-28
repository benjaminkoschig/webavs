package ch.globaz.pegasus.business.models.decision;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

public class ListDecisionsSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtat = null;
    private String forCsSexe = null;
    private String forCsTypeDecision = null;
    private String forDansDernierLot = null;
    private String forDateDecision = null;
    private String forDateNaissance = null;
    private String forDemande = null;
    private String forDepuisDebutDroit = null;
    private String forDepuisValidation = null;
    private String forDossier = null;
    private String forDroit = null;
    private String forIdDecision = null;
    private String forIdTiers = null;
    private String forNoDecision = null;
    private List<String> notInCsTypeDecision = null;
    private String forPcAccorde = null;
    private String forPreparePar = null;
    private String forValidePar = null;
    private String forVersionDroitApc = null;

    private String forVersionDroitSup = null;

    private List<String> inDatesValidations = null;

    private String forDateValidationGreaterOrEqual = null;
    private String forDateValidationLessOrEqual = null;
    private String forDateDebutDroit = null;

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

    public String getForCsTypeDecision() {
        return forCsTypeDecision;
    }

    /**
     * @return the forDansDernierLot
     */
    public String getForDansDernierLot() {
        return forDansDernierLot;
    }

    public String getForDateDecision() {
        return forDateDecision;
    }

    /**
     * @return the forDateNaissance
     */
    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public String getForDemande() {
        return forDemande;
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

    public String getForDossier() {
        return forDossier;
    }

    public String getForDroit() {
        return forDroit;
    }

    /**
     * @return the forIdDecision
     */
    public String getForIdDecision() {
        return forIdDecision;
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

    public String getForVersionDroitApc() {
        return forVersionDroitApc;
    }

    public String getForVersionDroitSup() {
        return forVersionDroitSup;
    }

    public List<String> getInDatesValidations() {
        return inDatesValidations;
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
     * 
     * @return the forDateDebutDroit
     */
    public String getForDateDebutDroit() {
        return forDateDebutDroit;
    }

    /**
     * 
     * @param forDateDebutDroit
     *            the forDateDebutDroit to set
     */
    public void setForDateDebutDroit(String forDateDebutDroit) {
        this.forDateDebutDroit = forDateDebutDroit;
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

    public void setForCsTypeDecision(String forCsTypeDecision) {
        this.forCsTypeDecision = forCsTypeDecision;
    }

    /**
     * @param forDansDernierLot
     *            the forDansDernierLot to set
     */
    public void setForDansDernierLot(String forDansDernierLot) {
        this.forDansDernierLot = forDansDernierLot;
    }

    public void setForDateDecision(String forDateDecision) {
        this.forDateDecision = forDateDecision;
    }

    /**
     * @param forDateNaissance
     *            the forDateNaissance to set
     */
    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public void setForDemande(String forDemande) {
        this.forDemande = forDemande;
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

    public void setForDossier(String forDossier) {
        this.forDossier = forDossier;
    }

    public void setForDroit(String forDroit) {
        this.forDroit = forDroit;
    }

    /**
     * @param forIdDecision
     *            the forIdDecision to set
     */
    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
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

    public void setForVersionDroitApc(String forVersionDroitApc) {
        this.forVersionDroitApc = forVersionDroitApc;
    }

    public void setForVersionDroitSup(String forVersionDroitSup) {
        this.forVersionDroitSup = forVersionDroitSup;
    }

    public void setInDatesValidations(List<String> inDatesValidations) {
        this.inDatesValidations = inDatesValidations;
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
        return ListDecisions.class;
    }

    public String getForDateValidationGreaterOrEqual() {
        return forDateValidationGreaterOrEqual;
    }

    public void setForDateValidationGreaterOrEqual(String forDateValidationGreaterOrEqual) {
        this.forDateValidationGreaterOrEqual = forDateValidationGreaterOrEqual;
    }

    public String getForDateValidationLessOrEqual() {
        return forDateValidationLessOrEqual;
    }

    public void setForDateValidationLessOrEqual(String forDateValidationLessOrEqual) {
        this.forDateValidationLessOrEqual = forDateValidationLessOrEqual;
    }

    public List<String> getNotInCsTypeDecision() {
        return notInCsTypeDecision;
    }

    public void setNotInCsTypeDecision(List<String> notInCsTypeDecision) {
        this.notInCsTypeDecision = notInCsTypeDecision;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
