package ch.globaz.vulpecula.business.models.af;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Mod�le d'un droit aux AF
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
     * Indique si l'edition de prestation prend en compte allocataire ou tiers b�n�ficiaire
     */
    private Boolean attestationAlloc = null;
    /**
     * Date de l'attestation d'�tude renseign�e par l'utilisateur
     */
    private String dateAttestationEtude = null;
    /**
     * Date de d�but du droit
     */
    private String debutDroit = null;
    /**
     * Etat du droit (Actif,Suspendu,Inactif+calculRang)
     */
    private String etatDroit = null;
    /**
     * Date �ch�ance du droit forc�e par l'utilisateur
     */
    private String finDroitForcee = null;
    /**
     * Indique si un montant � �t� forc�
     */
    private Boolean force = null;
    /**
     * id du dossier poss�dant le droit
     */
    private String idDossier = null;

    /**
     * id du droit, cl� primaire
     */
    private String idDroit = null;
    /**
     * id de l'enfant li� au droit
     */
    private String idEnfant = null;

    /**
     * id de l'�ventuel tiers b�n�ficiaire du droit
     */
    private String idTiersBeneficiaire = null;
    /**
     * Indique si le droit appara�t ou non sur la liste des �ch�ances
     */
    private Boolean imprimerEcheance = null;

    /**
     * Montant forc� (par l'utilisateur) du droit
     */
    private String montantForce = null;

    /**
     * Motif de la fin du droit(ECH,FET,...)
     */
    private String motifFin = null;
    /**
     * Motif de la r�duction du montant du droit
     */
    private String motifReduction = null;
    /**
     * Le statut familial de l'enfant
     */
    private String statutFamilial = null;
    /**
     * suppl�ment actif
     */
    private Boolean supplementActif = null;
    /**
     * Indique si le montant vers� est un suppl�ment FNB
     */
    private Boolean supplementFnb = null;
    /**
     * Tarif forc� (par l'utilisateur) appliqu� pour le droit
     */
    private String tarifForce = null;
    /**
     * Taux de versement (r�duction) du droit
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
     * Retourne la date de d�but de droit
     * 
     * @return the debutDroit
     */
    public String getDebutDroit() {
        return debutDroit;
    }

    /**
     * Retourne l'�tat du droit (actif, radi�, ...)
     * 
     * @return the etatDroit
     */
    public String getEtatDroit() {
        return etatDroit;
    }

    /**
     * Retourne la date de fin de droit indiqu�e par l'utilisateur
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
     * Retourne l'id du dossier auquel le droit est li�
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
     * Retourne l'id de l'enfant li� au droit (si de type ENF, FORM, NAIS ou ACCE)
     * 
     * @return the idEnfant
     */
    public String getIdEnfant() {
        return idEnfant;
    }

    /**
     * Retourne l'id du tiers b�n�ficiaire du droit
     * 
     * @return the idTiersBeneficiaire
     */
    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    /**
     * Indique si l'�ch�ance doit �tre imprim�e
     * 
     * @return the imprimerEcheance
     */
    public Boolean getImprimerEcheance() {
        return imprimerEcheance;
    }

    /**
     * @return the montantForc�
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
     * @return le statut familial de l'enfant (enfant du mariage actuel, adopt�, ...)
     */
    public String getStatutFamilial() {
        return statutFamilial;
    }

    /**
     * Indique si un calcul du suppl�ment est actif pour le droit (utilis� uniquement pour les caisses horlog�res)
     * 
     * @return <code>true</code> si le calcul du suppl�ment est actif
     */
    public Boolean getSupplementActif() {
        return supplementActif;
    }

    public Boolean getSupplementFnb() {
        return supplementFnb;
    }

    /**
     * Retourne le tarif forc� par l'utilisateur (si forc�)
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
     * D�finit la date de d�but de droit
     * 
     * @param debutDroit
     *            the debutDroit to set
     */
    public void setDebutDroit(String debutDroit) {
        this.debutDroit = debutDroit;
    }

    /**
     * D�finit l'�tat du droit (actif, radi�, ...)
     * 
     * @param etatDroit
     *            the etatDroit to set
     */
    public void setEtatDroit(String etatDroit) {
        this.etatDroit = etatDroit;
    }

    /**
     * D�finit la date de fin de droit indiqu�e par l'utilisateur
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
     * D�finit l'id du droit
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
     * D�finit l'id de l'enfant li� au droit (si de type ENF, FORM, NAIS ou ACCE)
     * 
     * @param idEnfant
     *            the idEnfant to set
     */
    public void setIdEnfant(String idEnfant) {
        this.idEnfant = idEnfant;
    }

    /**
     * D�finit l'id du tiers b�n�ficiaire
     * 
     * @param idTiersBeneficiaire
     *            the idTiersBeneficiaire to set
     */
    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    /**
     * Indique si l'�ch�ance doit �tre imprim�e
     * 
     * @param imprimerEcheance
     *            the imprimerEcheance to set
     */
    public void setImprimerEcheance(Boolean imprimerEcheance) {
        this.imprimerEcheance = imprimerEcheance;
    }

    /**
     * D�finit le montant forc�
     * 
     * @param montantForce
     *            the montantForce to set
     */
    public void setMontantForce(String montantForce) {
        this.montantForce = montantForce;
    }

    /**
     * D�finit le motif de fin de droit (16 ans, fin de formation, ...)
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
     * D�finit le statut familial de l'enfant (enfant du mariage actuel, adopt�, ...)
     * 
     * @param statutFamilial
     *            the statutFamilial to set
     */
    public void setStatutFamilial(String statutFamilial) {
        this.statutFamilial = statutFamilial;
    }

    /**
     * D�finit si un calcul du suppl�ment est actif pour le droit (utilis� uniquement pour les caisses horlog�res)
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
     * D�finit le tarif qui doit �tre forc� (canton, caisse, ...)
     * 
     * @param tarifForce
     *            the tarifForce to set
     */
    public void setTarifForce(String tarifForce) {
        this.tarifForce = tarifForce;
    }

    /**
     * D�finit le taux de r�duction du dossier
     * 
     * @param tauxVersement
     *            the tauxVersement to set
     */
    public void setTauxVersement(String tauxVersement) {
        this.tauxVersement = tauxVersement;
    }

    /**
     * D�finit le type de droit (ENF, FORM, ...)
     * 
     * @param typeDroit
     *            the typeDroit to set
     */
    public void setTypeDroit(String typeDroit) {
        this.typeDroit = typeDroit;
    }
}