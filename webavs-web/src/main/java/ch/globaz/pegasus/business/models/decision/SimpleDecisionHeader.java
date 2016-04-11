/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeSimpleModel;
import ch.globaz.pegasus.business.domaine.decision.EtatDecision;
import ch.globaz.pegasus.business.domaine.decision.TypeDecision;

/**
 * @author SCE Modèle simple pour les header des décisions 14 juil. 2010
 */
public class SimpleDecisionHeader extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String csEtatDecision = null;// etat cs

    private String csGenreDecision = null;
    private String csTypeDecision = null;// type cs decisions
    private String dateDebutDecision = null;
    private String dateDecision = null;// date
    private String dateFinDecision = null;
    private String datePreparation = null;
    private String dateValidation = null;
    private String idDecisionConjoint = null;
    private String idDecisionHeader = null;// id unique
    private String idPrestation = null;
    private String idTiersBeneficiaire = null;// id tiers
    private String idTiersCourrier = null;
    private String noDecision = null;// no dec
    private String preparationPar = null;// personne qui a prepare
    private String validationPar = null;// personne qui a valide

    /**
     * Retourne l'état de la déicision (enregistré, prévalidé, validé)
     * 
     * @return the csEtatDecision
     */
    public String getCsEtatDecision() {
        return csEtatDecision;
    }

    /**
     * Retourne le genre de décision (décision, pro forma)
     * 
     * @return the csGenreDecision
     */
    public String getCsGenreDecision() {
        return csGenreDecision;
    }

    /**
     * Retourne le type de décision (refus SC, suppression, octroi, refus AC, octroi partiel, adaptation)
     * 
     * @return the csTypeDecision
     */
    public String getCsTypeDecision() {
        return csTypeDecision;
    }

    public String getDateDebutDecision() {
        return dateDebutDecision;
    }

    /**
     * @return the dateDecision
     */
    public String getDateDecision() {
        return dateDecision;
    }

    public String getDateFinDecision() {
        return dateFinDecision;
    }

    /**
     * @return the datePreparation
     */
    public String getDatePreparation() {
        return datePreparation;
    }

    /**
     * @return the dateValidation
     */
    public String getDateValidation() {
        return dateValidation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idDecisionHeader;
    }

    public String getIdDecisionConjoint() {
        return idDecisionConjoint;
    }

    /**
     * @return the idDecisionHeader
     */
    public String getIdDecisionHeader() {
        return idDecisionHeader;
    }

    public String getIdPrestation() {
        return idPrestation;
    }

    /**
     * @return the idTiersBeneficiaire
     */
    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    /**
     * @return the idTiersCourrier
     */
    public String getIdTiersCourrier() {
        return idTiersCourrier;
    }

    /**
     * @return the noDecision
     */
    public String getNoDecision() {
        return noDecision;
    }

    /**
     * @return the preparationPar
     */
    public String getPreparationPar() {
        return preparationPar;
    }

    /**
     * @return the validationPar
     */
    public String getValidationPar() {
        return validationPar;
    }

    /**
     * @param csEtatDecision
     *            the csEtatDecision to set
     */
    public void setCsEtatDecision(String csEtatDecision) {
        this.csEtatDecision = csEtatDecision;
    }

    /**
     * @param csGenreDecision
     *            the csGenreDecision to set
     */
    public void setCsGenreDecision(String csGenreDecision) {
        this.csGenreDecision = csGenreDecision;
    }

    /**
     * @param csTypeDecision
     *            the csTypeDecision to set
     */
    public void setCsTypeDecision(String csTypeDecision) {
        this.csTypeDecision = csTypeDecision;
    }

    public void setDateDebutDecision(String dateDebutDecision) {
        this.dateDebutDecision = dateDebutDecision;
    }

    /**
     * @param dateDecision
     *            the dateDecision to set
     */
    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    public void setDateFinDecision(String dateFinDecision) {
        this.dateFinDecision = dateFinDecision;
    }

    /**
     * @param datePreparation
     *            the datePreparation to set
     */
    public void setDatePreparation(String datePreparation) {
        this.datePreparation = datePreparation;
    }

    /**
     * @param dateValidation
     *            the dateValidation to set
     */
    public void setDateValidation(String dateValidation) {
        this.dateValidation = dateValidation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idDecisionHeader = id;

    }

    public void setIdDecisionConjoint(String idDecisionConjoint) {
        this.idDecisionConjoint = idDecisionConjoint;
    }

    /**
     * @param idDecisionHeader
     *            the idDecisionHeader to set
     */
    public void setIdDecisionHeader(String idDecisionHeader) {
        this.idDecisionHeader = idDecisionHeader;
    }

    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    /**
     * @param idTiersBeneficiaire
     *            the idTiersBeneficiaire to set
     */
    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    /**
     * @param idTiersCourrier
     *            the idTiersCourrier to set
     */
    public void setIdTiersCourrier(String idTiersCourrier) {
        this.idTiersCourrier = idTiersCourrier;
    }

    /**
     * @param noDecision
     *            the noDecision to set
     */
    public void setNoDecision(String noDecision) {
        this.noDecision = noDecision;
    }

    /**
     * @param preparationPar
     *            the preparationPar to set
     */
    public void setPreparationPar(String preparationPar) {
        this.preparationPar = preparationPar;
    }

    /**
     * @param validationPar
     *            the validationPar to set
     */
    public void setValidationPar(String validationPar) {
        this.validationPar = validationPar;
    }

    public TypeDecision getType() {
        return TypeDecision.fromValue(csTypeDecision);
    }

    public EtatDecision getEtat() {
        return EtatDecision.fromValue(csEtatDecision);
    }

}
