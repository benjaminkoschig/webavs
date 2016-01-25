package ch.globaz.vulpecula.business.models.af;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Modèle d'un droit aux AF
 * 
 * @author jts
 * 
 */
public class DroitModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Indique si l'edition de prestation prend en compte allocataire ou tiers bénéficiaire
     */
    private Boolean attestationAlloc = null;
    /**
     * Date de l'attestation d'étude renseignée par l'utilisateur
     */
    private String dateAttestationEtude = null;
    /**
     * Date de début du droit
     */
    private String debutDroit = null;
    /**
     * Etat du droit (Actif,Suspendu,Inactif+calculRang)
     */
    private String etatDroit = null;
    /**
     * Date échéance du droit forcée par l'utilisateur
     */
    private String finDroitForcee = null;
    /**
     * Indique si un montant à été forcé
     */
    private Boolean force = null;
    /**
     * id du dossier possédant le droit
     */
    private String idDossier = null;

    /**
     * id du droit, clé primaire
     */
    private String idDroit = null;
    /**
     * id de l'enfant lié au droit
     */
    private String idEnfant = null;

    /**
     * id de l'éventuel tiers bénéficiaire du droit
     */
    private String idTiersBeneficiaire = null;
    /**
     * Indique si le droit apparaît ou non sur la liste des échéances
     */
    private Boolean imprimerEcheance = null;

    /**
     * Montant forcé (par l'utilisateur) du droit
     */
    private String montantForce = null;

    /**
     * Motif de la fin du droit(ECH,FET,...)
     */
    private String motifFin = null;
    /**
     * Motif de la réduction du montant du droit
     */
    private String motifReduction = null;
    /**
     * Le statut familial de l'enfant
     */
    private String statutFamilial = null;
    /**
     * supplément actif
     */
    private Boolean supplementActif = null;
    /**
     * Indique si le montant versé est un supplément FNB
     */
    private Boolean supplementFnb = null;
    /**
     * Tarif forcé (par l'utilisateur) appliqué pour le droit
     */
    private String tarifForce = null;
    /**
     * Taux de versement (réduction) du droit
     */
    private String tauxVersement = null;

    /**
     * Type du droit ( ENF,FORM,MEN,...)
     */
    private String typeDroit = null;

    public Boolean getAttestationAlloc() {
        return attestationAlloc;
    }

    public String getDateAttestationEtude() {
        return dateAttestationEtude;
    }

    /**
     * Retourne la date de début de droit
     * 
     * @return the debutDroit
     */
    public String getDebutDroit() {
        return debutDroit;
    }

    /**
     * Retourne l'état du droit (actif, radié, ...)
     * 
     * @return the etatDroit
     */
    public String getEtatDroit() {
        return etatDroit;
    }

    /**
     * Retourne la date de fin de droit indiquée par l'utilisateur
     * 
     * @return the finDroitForcee
     */
    public String getFinDroitForcee() {
        return finDroitForcee;
    }

    public Boolean getForce() {
        return force;
    }

    /**
     * Retourne l'id du droit
     * 
     * @return idDroit
     */
    @Override
    public String getId() {
        return idDroit;
    }

    /**
     * Retourne l'id du dossier auquel le droit est lié
     * 
     * @return the idDossier
     */
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * @return the idDroit
     */
    public String getIdDroit() {
        return idDroit;
    }

    /**
     * Retourne l'id de l'enfant lié au droit (si de type ENF, FORM, NAIS ou ACCE)
     * 
     * @return the idEnfant
     */
    public String getIdEnfant() {
        return idEnfant;
    }

    /**
     * Retourne l'id du tiers bénéficiaire du droit
     * 
     * @return the idTiersBeneficiaire
     */
    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    /**
     * Indique si l'échéance doit être imprimée
     * 
     * @return the imprimerEcheance
     */
    public Boolean getImprimerEcheance() {
        return imprimerEcheance;
    }

    /**
     * @return the montantForcé
     */
    public String getMontantForce() {
        return montantForce;
    }

    /**
     * Retourne le motif de fin de droit (16 ans, fin de formation, ...)
     * 
     * @return the motifFin
     */
    public String getMotifFin() {
        return motifFin;
    }

    /**
     * @return the motifReduction
     */
    public String getMotifReduction() {
        return motifReduction;
    }

    /**
     * @return le statut familial de l'enfant (enfant du mariage actuel, adopté, ...)
     */
    public String getStatutFamilial() {
        return statutFamilial;
    }

    /**
     * Indique si un calcul du supplément est actif pour le droit (utilisé uniquement pour les caisses horlogères)
     * 
     * @return <code>true</code> si le calcul du supplément est actif
     */
    public Boolean getSupplementActif() {
        return supplementActif;
    }

    public Boolean getSupplementFnb() {
        return supplementFnb;
    }

    /**
     * Retourne le tarif forcé par l'utilisateur (si forcé)
     * 
     * @return the tarifForce
     */
    public String getTarifForce() {
        return tarifForce;
    }

    /**
     * Retourne le taux de versement du droit
     * 
     * @return the tauxVersement
     */
    public String getTauxVersement() {
        return tauxVersement;
    }

    /**
     * Retourne le type de droit (ENF, FORM, ...)
     * 
     * @return the typeDroit
     */
    public String getTypeDroit() {
        return typeDroit;
    }

    public void setAttestationAlloc(Boolean attestationAlloc) {
        this.attestationAlloc = attestationAlloc;
    }

    public void setDateAttestationEtude(String dateAttestation) {
        dateAttestationEtude = dateAttestation;
    }

    /**
     * Définit la date de début de droit
     * 
     * @param debutDroit
     *            the debutDroit to set
     */
    public void setDebutDroit(String debutDroit) {
        this.debutDroit = debutDroit;
    }

    /**
     * Définit l'état du droit (actif, radié, ...)
     * 
     * @param etatDroit
     *            the etatDroit to set
     */
    public void setEtatDroit(String etatDroit) {
        this.etatDroit = etatDroit;
    }

    /**
     * Définit la date de fin de droit indiquée par l'utilisateur
     * 
     * @param finDroitForcee
     *            the finDroitForcee to set
     */
    public void setFinDroitForcee(String finDroitForcee) {
        this.finDroitForcee = finDroitForcee;
    }

    public void setForce(Boolean force) {
        this.force = force;
    }

    /**
     * Définit l'id du droit
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */

    @Override
    public void setId(String id) {
        idDroit = id;
    }

    /**
     * @param idDossier
     *            the idDossier to set
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * @param idDroit
     *            the idDroit to set
     */
    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    /**
     * Définit l'id de l'enfant lié au droit (si de type ENF, FORM, NAIS ou ACCE)
     * 
     * @param idEnfant
     *            the idEnfant to set
     */
    public void setIdEnfant(String idEnfant) {
        this.idEnfant = idEnfant;
    }

    /**
     * Définit l'id du tiers bénéficiaire
     * 
     * @param idTiersBeneficiaire
     *            the idTiersBeneficiaire to set
     */
    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    /**
     * Indique si l'échéance doit être imprimée
     * 
     * @param imprimerEcheance
     *            the imprimerEcheance to set
     */
    public void setImprimerEcheance(Boolean imprimerEcheance) {
        this.imprimerEcheance = imprimerEcheance;
    }

    /**
     * Définit le montant forcé
     * 
     * @param montantForce
     *            the montantForce to set
     */
    public void setMontantForce(String montantForce) {
        this.montantForce = montantForce;
    }

    /**
     * Définit le motif de fin de droit (16 ans, fin de formation, ...)
     * 
     * @param motifFin
     *            the motifFin to set
     */
    public void setMotifFin(String motifFin) {
        this.motifFin = motifFin;
    }

    /**
     * @param motifReduction
     *            the motifReduction to set
     */
    public void setMotifReduction(String motifReduction) {
        this.motifReduction = motifReduction;
    }

    /**
     * Définit le statut familial de l'enfant (enfant du mariage actuel, adopté, ...)
     * 
     * @param statutFamilial
     *            the statutFamilial to set
     */
    public void setStatutFamilial(String statutFamilial) {
        this.statutFamilial = statutFamilial;
    }

    /**
     * Définit si un calcul du supplément est actif pour le droit (utilisé uniquement pour les caisses horlogères)
     * 
     * @param supplementActif
     */
    public void setSupplementActif(Boolean supplementActif) {
        this.supplementActif = supplementActif;
    }

    public void setSupplementFnb(Boolean supplementFnb) {
        this.supplementFnb = supplementFnb;
    }

    /**
     * Définit le tarif qui doit être forcé (canton, caisse, ...)
     * 
     * @param tarifForce
     *            the tarifForce to set
     */
    public void setTarifForce(String tarifForce) {
        this.tarifForce = tarifForce;
    }

    /**
     * Définit le taux de réduction du dossier
     * 
     * @param tauxVersement
     *            the tauxVersement to set
     */
    public void setTauxVersement(String tauxVersement) {
        this.tauxVersement = tauxVersement;
    }

    /**
     * Définit le type de droit (ENF, FORM, ...)
     * 
     * @param typeDroit
     *            the typeDroit to set
     */
    public void setTypeDroit(String typeDroit) {
        this.typeDroit = typeDroit;
    }
}