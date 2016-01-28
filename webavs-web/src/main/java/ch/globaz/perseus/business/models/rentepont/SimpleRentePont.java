/**
 * 
 */
package ch.globaz.perseus.business.models.rentepont;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author DDE
 * 
 */
public class SimpleRentePont extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csCaisse = null;
    private String csEtat = null;
    private String dateDebut = null;
    private String dateDecision = null;
    private String dateDepot = null;
    private String dateFin = null;
    private String excedantRevenu = null;
    private String idDomaineApplicatifAdressePaiement = null;
    private String idDossier = null;
    private String idGestionnaire = null;
    private String idRentePont = null;
    private String idSituationFamiliale = null;
    private String idTiersAdressePaiement = null;
    private String montant = null;
    private String montantImpotSource = null;
    private String montantRetroactif = null;
    private Boolean onError = null;
    private String remarques = null;

    /**
     * @return the csCaisse
     */
    public String getCsCaisse() {
        return csCaisse;
    }

    /**
     * @return the csEtat
     */
    public String getCsEtat() {
        return csEtat;
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateDecision() {
        return dateDecision;
    }

    /**
     * @return the dateDepot
     */
    public String getDateDepot() {
        return dateDepot;
    }

    /**
     * @return the dateFin
     */
    public String getDateFin() {
        return dateFin;
    }

    public String getExcedantRevenu() {
        return excedantRevenu;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idRentePont;
    }

    public String getIdDomaineApplicatifAdressePaiement() {
        return idDomaineApplicatifAdressePaiement;
    }

    /**
     * @return the idDossier
     */
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * @return the idGestionnaire
     */
    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    /**
     * @return the idRentePont
     */
    public String getIdRentePont() {
        return idRentePont;
    }

    /**
     * @return the idSituationFamiliale
     */
    public String getIdSituationFamiliale() {
        return idSituationFamiliale;
    }

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @return the montantImpotSource
     */
    public String getMontantImpotSource() {
        return montantImpotSource;
    }

    public String getMontantRetroactif() {
        return montantRetroactif;
    }

    public Boolean getOnError() {
        return onError;
    }

    /**
     * @return the remarques
     */
    public String getRemarques() {
        return remarques;
    }

    /**
     * @param csCaisse
     *            the csCaisse to set
     */
    public void setCsCaisse(String csCaisse) {
        this.csCaisse = csCaisse;
    }

    /**
     * @param csEtat
     *            the csEtat to set
     */
    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    /**
     * @param dateDepot
     *            the dateDepot to set
     */
    public void setDateDepot(String dateDepot) {
        this.dateDepot = dateDepot;
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setExcedantRevenu(String excedantRevenu) {
        this.excedantRevenu = excedantRevenu;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idRentePont = id;
    }

    public void setIdDomaineApplicatifAdressePaiement(String idDomaineApplicatifAdressePaiement) {
        this.idDomaineApplicatifAdressePaiement = idDomaineApplicatifAdressePaiement;
    }

    /**
     * @param idDossier
     *            the idDossier to set
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * @param idGestionnaire
     *            the idGestionnaire to set
     */
    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    /**
     * @param idRentePont
     *            the idRentePont to set
     */
    public void setIdRentePont(String idRentePont) {
        this.idRentePont = idRentePont;
    }

    /**
     * @param idSituationFamiliale
     *            the idSituationFamiliale to set
     */
    public void setIdSituationFamiliale(String idSituationFamiliale) {
        this.idSituationFamiliale = idSituationFamiliale;
    }

    public void setIdTiersAdressePaiement(String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * @param montantImpotSource
     *            the montantImpotSource to set
     */
    public void setMontantImpotSource(String montantImpotSource) {
        this.montantImpotSource = montantImpotSource;
    }

    public void setMontantRetroactif(String montantRetroactif) {
        this.montantRetroactif = montantRetroactif;
    }

    public void setOnError(Boolean onError) {
        this.onError = onError;
    }

    /**
     * @param remarques
     *            the remarques to set
     */
    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }

}
