/**
 * 
 */
package ch.globaz.perseus.business.models.rentepont;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author JSI
 * 
 */
public class SimpleFactureRentePont extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean acceptationForcee = null;
    private Boolean casDeRigueur = null;
    private String csEtat = null;
    private String csMotif = null;
    private String csSousTypeSoinRentePont = null;
    private String csTypeSoinRentePont = null;
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
    private String idFactureRentePont = null;
    private String idGestionnaire = null;
    private String idQDRentePont = null;
    private String idTiersAdresseCourrier = null;
    private String idTiersAdressePaiement = null;
    private String libelle = null;
    private String montant = null;
    private String montantDepassant = null;
    private String montantRembourse = null;
    private String motifLibre = null;
    private String numDecision = null;
    private String numRefFacture = null;
    private String idTiersMembreFamille = null;

    public String getIdTiersMembreFamille() {
        return idTiersMembreFamille;
    }

    public void setIdTiersMembreFamille(String idTiersMembreFamille) {
        this.idTiersMembreFamille = idTiersMembreFamille;
    }

    public Boolean getAcceptationForcee() {
        return acceptationForcee;
    }

    public Boolean getCasDeRigueur() {
        if (null == casDeRigueur) {
            casDeRigueur = false;
        }
        return casDeRigueur;
    }

    public String getCsEtat() {
        return csEtat;
    }

    public String getCsMotif() {
        return csMotif;
    }

    public String getCsSousTypeSoinRentePont() {
        return csSousTypeSoinRentePont;
    }

    public String getCsTypeSoinRentePont() {
        return csTypeSoinRentePont;
    }

    public String getDateDebutTraitement() {
        return dateDebutTraitement;
    }

    public String getDateDecision() {
        return dateDecision;
    }

    public String getDateFacture() {
        return dateFacture;
    }

    public String getDateFinTraitement() {
        return dateFinTraitement;
    }

    public String getDatePriseEnCharge() {
        return datePriseEnCharge;
    }

    public String getDateReception() {
        return dateReception;
    }

    public String getDateValidation() {
        return dateValidation;
    }

    public String getExcedantRevenuCompense() {
        return excedantRevenuCompense;
    }

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
        return idFactureRentePont;
    }

    public String getIdApplicationAdresseCourrier() {
        return idApplicationAdresseCourrier;
    }

    public String getIdApplicationAdressePaiement() {
        return idApplicationAdressePaiement;
    }

    public String getIdFactureRentePont() {
        return idFactureRentePont;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdQDRentePont() {
        return idQDRentePont;
    }

    public String getIdTiersAdresseCourrier() {
        return idTiersAdresseCourrier;
    }

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getMontant() {
        return montant;
    }

    public String getMontantDepassant() {
        return montantDepassant;
    }

    public String getMontantRembourse() {
        return montantRembourse;
    }

    public String getMotifLibre() {
        return motifLibre;
    }

    public String getNumDecision() {
        return numDecision;
    }

    public String getNumRefFacture() {
        return numRefFacture;
    }

    public void setAcceptationForcee(Boolean acceptationForcee) {
        this.acceptationForcee = acceptationForcee;
    }

    public void setCasDeRigueur(Boolean casDeRigueur) {
        this.casDeRigueur = casDeRigueur;
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public void setCsMotif(String csMotif) {
        this.csMotif = csMotif;
    }

    public void setCsSousTypeSoinRentePont(String csSousTypeSoinRentePont) {
        this.csSousTypeSoinRentePont = csSousTypeSoinRentePont;
    }

    public void setCsTypeSoinRentePont(String csTypeSoinRentePont) {
        this.csTypeSoinRentePont = csTypeSoinRentePont;
    }

    public void setDateDebutTraitement(String dateDebutTraitement) {
        this.dateDebutTraitement = dateDebutTraitement;
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    public void setDateFacture(String dateFacture) {
        this.dateFacture = dateFacture;
    }

    public void setDateFinTraitement(String dateFinTraitement) {
        this.dateFinTraitement = dateFinTraitement;
    }

    public void setDatePriseEnCharge(String datePriseEnCharge) {
        this.datePriseEnCharge = datePriseEnCharge;
    }

    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    public void setDateValidation(String dateValidation) {
        this.dateValidation = dateValidation;
    }

    public void setExcedantRevenuCompense(String excedantRevenuCompense) {
        this.excedantRevenuCompense = excedantRevenuCompense;
    }

    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }

    public void setHygienisteDentaire(Boolean hygienisteDentaire) {
        this.hygienisteDentaire = hygienisteDentaire;
    }

    @Override
    public void setId(String id) {
        idFactureRentePont = id;
    }

    public void setIdApplicationAdresseCourrier(String idApplicationAdresseCourrier) {
        this.idApplicationAdresseCourrier = idApplicationAdresseCourrier;
    }

    public void setIdApplicationAdressePaiement(String idApplicationAdressePaiement) {
        this.idApplicationAdressePaiement = idApplicationAdressePaiement;
    }

    public void setIdFactureRentePont(String idFactureRentePont) {
        this.idFactureRentePont = idFactureRentePont;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdQDRentePont(String idQDRentePont) {
        this.idQDRentePont = idQDRentePont;
    }

    public void setIdTiersAdresseCourrier(String idTiersAdresseCourrier) {
        this.idTiersAdresseCourrier = idTiersAdresseCourrier;
    }

    public void setIdTiersAdressePaiement(String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setMontantDepassant(String montantDepassant) {
        this.montantDepassant = montantDepassant;
    }

    public void setMontantRembourse(String montantRembourse) {
        this.montantRembourse = montantRembourse;
    }

    public void setMotifLibre(String motifLibre) {
        this.motifLibre = motifLibre;
    }

    public void setNumDecision(String numDecision) {
        this.numDecision = numDecision;
    }

    public void setNumRefFacture(String numRefFacture) {
        this.numRefFacture = numRefFacture;
    }

}
