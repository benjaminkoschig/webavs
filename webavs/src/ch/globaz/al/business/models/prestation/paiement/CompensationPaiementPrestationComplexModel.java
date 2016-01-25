package ch.globaz.al.business.models.prestation.paiement;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * Modèle de base pour la compensation sur facture et le paiement direct de prestations. Elle fournit les colonnes qui
 * sont obligatoirement sélectionnées. Tous les modèles liés au paiement de prestation doivent étendre cette classe
 * 
 * @author jts
 * 
 */
public abstract class CompensationPaiementPrestationComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Type de bonification de l'en-tête
     */
    private String bonificationEntete = null;

    /**
     * Etat du dossier
     */
    private String etatDossier = null;
    /**
     * Date de fin de validité du dossier
     */
    private String finValidite = null;

    /**
     * Id du détail de la prestation
     */
    private String idDetailPrestation = null;

    /**
     * Id de l'en-tête de la prestation
     */
    private String idEntete = null;

    /**
     * Id de la récap
     */
    private String idRecap = null;

    /**
     * Montant du détail de la prestation
     */
    private String montantDetail = null;

    /**
     * fin de la période de la prestation
     */
    private String presPeriodeA = null;

    /**
     * @return the bonificationEntete
     */
    public String getBonificationEntete() {
        return bonificationEntete;
    }

    /**
     * @return the etatDossier
     */
    public String getEtatDossier() {
        return etatDossier;
    }

    /**
     * @return the finValidite
     */
    public String getFinValidite() {
        return finValidite;
    }

    @Override
    public String getId() {
        return idDetailPrestation;
    }

    /**
     * @return the idDetailPrestation
     */
    public String getIdDetailPrestation() {
        return idDetailPrestation;
    }

    /**
     * @return the idEntete
     */
    public String getIdEntete() {
        return idEntete;
    }

    /**
     * @return the idRecap
     */
    public String getIdRecap() {
        return idRecap;
    }

    /**
     * @return the montantDetail
     */
    public String getMontantDetail() {
        return montantDetail;
    }

    /**
     * @return the presPeriodeA
     */
    public String getPresPeriodeA() {
        return presPeriodeA;
    }

    @Override
    public String getSpy() {
        return null;
    }

    /**
     * @param bonificationEntete
     *            the bonificationEntete to set
     */
    public void setBonificationEntete(String bonificationEntete) {
        this.bonificationEntete = bonificationEntete;
    }

    /**
     * @param etatDossier
     *            the etatDossier to set
     */
    public void setEtatDossier(String etatDossier) {
        this.etatDossier = etatDossier;
    }

    /**
     * @param finValidite
     *            the finValidite to set
     */
    public void setFinValidite(String finValidite) {
        this.finValidite = finValidite;
    }

    @Override
    public void setId(String id) {
        // DO NOTHING
    }

    /**
     * @param idDetailPrestation
     *            the idDetailPrestation to set
     */
    public void setIdDetailPrestation(String idDetailPrestation) {
        this.idDetailPrestation = idDetailPrestation;
    }

    /**
     * @param idEntete
     *            the idEntete to set
     */
    public void setIdEntete(String idEntete) {
        this.idEntete = idEntete;
    }

    /**
     * @param idRecap
     *            the idRecap to set
     */
    public void setIdRecap(String idRecap) {
        this.idRecap = idRecap;
    }

    /**
     * @param montantDetail
     *            the montantDetail to set
     */
    public void setMontantDetail(String montantDetail) {
        this.montantDetail = montantDetail;
    }

    /**
     * @param presPeriodeA
     *            the presPeriodeA to set
     */
    public void setPresPeriodeA(String presPeriodeA) {
        this.presPeriodeA = presPeriodeA;
    }

    @Override
    public void setSpy(String spy) {
        // DO NOTHING
    }
}
