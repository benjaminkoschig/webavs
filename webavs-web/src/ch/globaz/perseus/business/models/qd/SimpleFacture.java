package ch.globaz.perseus.business.models.qd;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Modèle simple d'une facture. Descrit les champs composant une facture dans la base de données.
 * 
 * @author JSI
 * 
 */
public class SimpleFacture extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean acceptationForcee = null;
    private Boolean casDeRigueur = null;
    private String csEtat = null;
    private String csMotif = null;
    private String dateDebutTraitement = null;
    private String dateDecision = null;
    private String dateFacture = null;
    private String dateFinTraitement = null;
    private String datePriseEnCharge = null;
    private String dateReception = null;
    private String dateValidation = null;
    private String excedantRevenuCompense = null;
    private String fournisseur = null;
    private Boolean hygienisteDentaire = null;
    private String idApplicationAdresseCourrier = null;
    private String idApplicationAdressePaiement = null;
    private String idFacture = null;
    private String idGestionnaire = null;
    private String idQD = null;
    private String idTiersAdresseCourrier = null;
    private String idTiersAdressePaiement = null;
    private String libelle = null;
    private String minutesHygieniste = null;
    private String montant = null;
    private String montantDepassant = null;
    private String montantRembourse = null;
    private String motifLibre = null;
    private String numDecision = null;

    private String numRefFacture = null;

    /**
     * @return the acceptationForcee
     */
    public Boolean getAcceptationForcee() {
        return acceptationForcee;
    }

    public Boolean getCasDeRigueur() {
        if (casDeRigueur == null) {
            casDeRigueur = false;
        }
        return casDeRigueur;
    }

    /**
     * @return the csEtat
     */
    public String getCsEtat() {
        return csEtat;
    }

    /**
     * @return the csMotif
     */
    public String getCsMotif() {
        return csMotif;
    }

    /**
     * @return the dateDebutTraitement
     */
    public String getDateDebutTraitement() {
        return dateDebutTraitement;
    }

    /**
     * @return the dateDecision
     */
    public String getDateDecision() {
        return dateDecision;
    }

    /**
     * @return the dateFacture
     */
    public String getDateFacture() {
        return dateFacture;
    }

    /**
     * @return the dateFinTraitement
     */
    public String getDateFinTraitement() {
        return dateFinTraitement;
    }

    public String getDatePriseEnCharge() {
        return datePriseEnCharge;
    }

    /**
     * @return the dateReception
     */
    public String getDateReception() {
        return dateReception;
    }

    /**
     * @return dateValidation
     */
    public String getDateValidation() {
        return dateValidation;
    }

    /**
     * @return the excedantRevenuCompense
     */
    public String getExcedantRevenuCompense() {
        return excedantRevenuCompense;
    }

    /**
     * @return the fournisseur
     */
    public String getFournisseur() {
        return fournisseur;
    }

    public Boolean getHygienisteDentaire() {
        if (hygienisteDentaire == null) {
            hygienisteDentaire = false;
        }
        return hygienisteDentaire;
    }

    @Override
    public String getId() {
        return idFacture;
    }

    public String getIdApplicationAdresseCourrier() {
        return idApplicationAdresseCourrier;
    }

    /**
     * @return the idApplicationAdressePaiement
     */
    public String getIdApplicationAdressePaiement() {
        return idApplicationAdressePaiement;
    }

    /**
     * @return the idFacture
     */
    public String getIdFacture() {
        return idFacture;
    }

    /**
     * @return the idGestionnaire
     */
    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    /**
     * @return the idQD
     */
    public String getIdQD() {
        return idQD;
    }

    public String getIdTiersAdresseCourrier() {
        return idTiersAdresseCourrier;
    }

    /**
     * @return the idTiersAdressePaiement
     */
    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    /**
     * @return the libelle
     */
    public String getLibelle() {
        return libelle;
    }

    public String getMinutesHygieniste() {
        return minutesHygieniste;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @return the montantDepassant
     */
    public String getMontantDepassant() {
        return montantDepassant;
    }

    /**
     * @return the montantRembourse
     */
    public String getMontantRembourse() {
        return montantRembourse;
    }

    /**
     * @return the motifLibre
     */
    public String getMotifLibre() {
        return motifLibre;
    }

    /**
     * @return the numDecision
     */
    public String getNumDecision() {
        return numDecision;
    }

    /**
     * @return the numRefFacture
     */
    public String getNumRefFacture() {
        return numRefFacture;
    }

    /**
     * @param acceptationForcee
     *            the acceptationForcee to set
     */
    public void setAcceptationForcee(Boolean acceptationForcee) {
        this.acceptationForcee = acceptationForcee;
    }

    public void setCasDeRigueur(Boolean casDeRigueur) {
        this.casDeRigueur = casDeRigueur;
    }

    /**
     * @param csEtat
     *            the csEtat to set
     */
    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    /**
     * @param csMotif
     *            the csMotif to set
     */
    public void setCsMotif(String csMotif) {
        this.csMotif = csMotif;
    }

    /**
     * @param dateDebutTraitement
     *            the dateDebutTraitement to set
     */
    public void setDateDebutTraitement(String dateDebutTraitement) {
        this.dateDebutTraitement = dateDebutTraitement;
    }

    /**
     * @param dateDecision
     *            the dateDecision to set
     */
    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    /**
     * @param dateFacture
     *            the dateFacture to set
     */
    public void setDateFacture(String dateFacture) {
        this.dateFacture = dateFacture;
    }

    /**
     * @param dateFinTraitement
     *            the dateFinTraitement to set
     */
    public void setDateFinTraitement(String dateFinTraitement) {
        this.dateFinTraitement = dateFinTraitement;
    }

    public void setDatePriseEnCharge(String datePriseEnCharge) {
        this.datePriseEnCharge = datePriseEnCharge;
    }

    /**
     * @param dateReception
     *            the dateReception to set
     */
    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    /**
     * @param date
     *            de validation the validation date to set
     */
    public void setDateValidation(String value) {
        dateValidation = value;
    }

    /**
     * @param excedantRevenuCompense
     *            the excedantRevenuCompense to set
     */
    public void setExcedantRevenuCompense(String excedantRevenuCompense) {
        this.excedantRevenuCompense = excedantRevenuCompense;
    }

    /**
     * @param fournisseur
     *            the fournisseur to set
     */
    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }

    public void setHygienisteDentaire(Boolean hygienisteDentaire) {
        this.hygienisteDentaire = hygienisteDentaire;
    }

    @Override
    public void setId(String id) {
        idFacture = id;
    }

    public void setIdApplicationAdresseCourrier(String idApplicationAdresseCourrier) {
        this.idApplicationAdresseCourrier = idApplicationAdresseCourrier;
    }

    /**
     * @param idApplicationAdressePaiement
     *            the idApplicationAdressePaiement to set
     */
    public void setIdApplicationAdressePaiement(String idApplicationAdressePaiement) {
        this.idApplicationAdressePaiement = idApplicationAdressePaiement;
    }

    /**
     * @param idFacture
     *            the idFacture to set
     */
    public void setIdFacture(String idFacture) {
        this.idFacture = idFacture;
    }

    /**
     * @param idGestionnaire
     *            the idGestionnaire to set
     */
    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    /**
     * @param idQD
     *            the idQD to set
     */
    public void setIdQD(String idQD) {
        this.idQD = idQD;
    }

    public void setIdTiersAdresseCourrier(String idTiersAdresseCourrier) {
        this.idTiersAdresseCourrier = idTiersAdresseCourrier;
    }

    /**
     * @param idTiersAdressePaiement
     *            the idTiersAdressePaiement to set
     */
    public void setIdTiersAdressePaiement(String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    /**
     * @param libelle
     *            the libelle to set
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setMinutesHygieniste(String minutesHygieniste) {
        this.minutesHygieniste = minutesHygieniste;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * @param montantDepassant
     *            the montantDepassant to set
     */
    public void setMontantDepassant(String montantDepassant) {
        this.montantDepassant = montantDepassant;
    }

    /**
     * @param montantRembourse
     *            the montantRembourse to set
     */
    public void setMontantRembourse(String montantRembourse) {
        this.montantRembourse = montantRembourse;
    }

    /**
     * @param motifLibre
     *            the motifLibre to set
     */
    public void setMotifLibre(String motifLibre) {
        this.motifLibre = motifLibre;
    }

    /**
     * @param numDecision
     *            the numDecision to set
     */
    public void setNumDecision(String numDecision) {
        this.numDecision = numDecision;
    }

    /**
     * @param numRefFacture
     *            the numRefFacture to set
     */
    public void setNumRefFacture(String numRefFacture) {
        this.numRefFacture = numRefFacture;
    }

}
