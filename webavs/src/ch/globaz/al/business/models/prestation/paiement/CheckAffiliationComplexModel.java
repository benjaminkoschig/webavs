package ch.globaz.al.business.models.prestation.paiement;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * Modèle utilisé pour la vérification des affiliations lors des compensation sur facture
 * 
 * @author jts
 * 
 */
public class CheckAffiliationComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Activité de l'allocataire
     */
    private String activiteAllocataire = null;
    /**
     * Etat du dossier
     */
    private String etatDossier = null;
    /**
     * Date de fin de validité du dossier
     */
    private String finValidite = null;
    /**
     * identifiant du dossier
     */
    private String idDossier = null;
    /**
     * identifaint de la récap
     */
    private String idRecap = null;

    /**
     * Numéro de l'affilié
     */
    private String numeroAffilie = null;

    /**
     * fin de la période de la récap
     */
    private String periodeA = null;

    /**
     * début de la période de la récap
     */
    private String periodeDe = null;
    private String presPeriodeA = null;

    /**
     * @return the activiteAllocataire
     */
    public String getActiviteAllocataire() {
        return activiteAllocataire;
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
        return null;
    }

    /**
     * @return the idDossier
     */
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * @return the idRecap
     */
    public String getIdRecap() {
        return idRecap;
    }

    /**
     * @return the numeroAffilie
     */
    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    /**
     * @return the periodeA
     */
    public String getPeriodeA() {
        return periodeA;
    }

    /**
     * @return the periodeDe
     */
    public String getPeriodeDe() {
        return periodeDe;
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
     * @param activiteAllocataire
     *            the activiteAllocataire to set
     */
    public void setActiviteAllocataire(String activiteAllocataire) {
        this.activiteAllocataire = activiteAllocataire;
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
     * @param idDossier
     *            the idDossier to set
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * @param idRecap
     *            the idRecap to set
     */
    public void setIdRecap(String idRecap) {
        this.idRecap = idRecap;
    }

    /**
     * @param numeroAffilie
     *            the numeroAffilie to set
     */
    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    /**
     * @param periodeA
     *            the periodeA to set
     */
    public void setPeriodeA(String periodeA) {
        this.periodeA = periodeA;
    }

    /**
     * @param periodeDe
     *            the periodeDe to set
     */
    public void setPeriodeDe(String periodeDe) {
        this.periodeDe = periodeDe;
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
