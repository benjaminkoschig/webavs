package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Modèle de base d'un dossier contenant uniquement les clés étrangères. Ce modèle est utilisé pour établir les
 * jointures entre différents modèles
 * 
 * @author jts
 */
public class DossierFkModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * activité de l'allocataire (code système acivité: salarié, indépendant, agricole, ....)
     * 
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_ACTIVITE_ALLOC
     */
    private String activiteAllocataire = null;

    /**
     * complément à l'activité allocataire (code système complément activité : mission temporaire,...)
     * 
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_COMPLEMENT_ACTIVITE
     */
    private String complementActiviteAllocataire = null;

    /**
     * état du dossier (code système: radié, suspendu, actif, ...)
     * 
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_ETAT
     */
    private String etatDossier = null;
    /**
     * identifiant de l'allocataire lié au dossier
     */
    private String idAllocataire = null;
    /**
     * identifiant du dossier
     */
    private String idDossier = null;
    /**
     * identifiant du dossier du conjoint
     */
    private String idDossierConjoint = null;
    /**
     * identifiant du tiers bénéficiaire (si autre que allocataire)
     */
    private String idTiersBeneficiaire = null;
    /**
     * identifiant de la caisse du conjoint
     */
    private String idTiersCaisseConjoint = null;
    /**
     * numéro de l'affilié
     */
    private String numeroAffilie = null;
    /**
     * numéro de salarié externe
     */

    private String numSalarieExterne = null;
    /**
     * statut du dossier (code système: statut (normal, supplétif, ...)
     * 
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_STATUT
     */
    private String statut = null;

    /**
     * Retourne l'activite d'allocataire (salarié, indépendant, ...)
     * 
     * @return the activiteAllocataire
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_ACTIVITE_ALLOC
     */
    public String getActiviteAllocataire() {
        return activiteAllocataire;
    }

    /**
     * Retourne l'état du dossier (actif, radié, ...)
     * 
     * @return the etatDossier
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_ETAT
     */
    public String getEtatDossier() {
        return etatDossier;
    }

    /**
     * Retourne l'id du dossier
     * 
     * @return idDossier
     */
    @Override
    public String getId() {
        return idDossier;
    }

    /**
     * Retourne l'id de l'allocataire
     * 
     * @return the idAllocataire
     */
    public String getIdAllocataire() {
        return idAllocataire;
    }

    /**
     * @return the idDossier
     */
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * Retourne l'id du dossier du conjoint (si existant)
     * 
     * @return idDossierConjoint
     */
    public String getIdDossierConjoint() {
        return idDossierConjoint;
    }

    /**
     * Retourne l'id du tiers bénéficiaire. Provient de web@tiers. Utilisé en cas de paiement direct
     * 
     * @return the idTiersBeneficiaire
     */
    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    /**
     * Retourne l'id de la caisse AF du conjoint. Elle est utilisé pour le calcul des droit dans le cas de dossier ADC
     * 
     * @return the idTiersCaisseConjoint
     */
    public String getIdTiersCaisseConjoint() {
        return idTiersCaisseConjoint;
    }

    /**
     * @return the numeroAffilie
     */
    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    /**
     * @return the numSalarieExterne
     */
    public String getNumSalarieExterne() {
        return numSalarieExterne;
    }

    /**
     * Retourne le statut du dossier (N, NP, ...)
     * 
     * @return the statut
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_STATUT
     */
    public String getStatut() {
        return statut;
    }

    /**
     * Définit l'activite d'allocataire (salarié, indépendant, ...)
     * 
     * @param ActiviteAllocataire
     *            the activiteAllocataire to set
     */
    public void setActiviteAllocataire(String ActiviteAllocataire) {
        activiteAllocataire = ActiviteAllocataire;
    }

    /**
     * Définit l'état du dossier (actif, radié, ...)
     * 
     * @param etatDossier
     *            the etatDossier to set
     */
    public void setEtatDossier(String etatDossier) {
        this.etatDossier = etatDossier;
    }

    /**
     * Définit l'id du dossier
     */
    @Override
    public void setId(String id) {
        idDossier = id;
    }

    /**
     * Définit l'id de l'allocataire
     * 
     * @param idAllocataire
     *            the idAllocataire to set
     */
    public void setIdAllocataire(String idAllocataire) {
        this.idAllocataire = idAllocataire;
    }

    /**
     * @param idDossier
     *            the idDossier to set
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * Définit l'id du dossier du conjoint
     * 
     * @param idDossierConjoint
     *            identifiant du dossier du conjoint
     */
    public void setIdDossierConjoint(String idDossierConjoint) {
        this.idDossierConjoint = idDossierConjoint;
    }

    /**
     * Définit l'id du tiers bénéficiaire (web@tiers) Utilisé en cas de paiement direct
     * 
     * @param idTiersBeneficiaire
     *            the idTiersBeneficiaire to set
     */
    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    /**
     * Définit l'id de la caisse AF du conjoint. Elle est utilisé pour le calcul des droit dans le cas de dossier ADC
     * 
     * @param idTiersCaisseConjoint
     *            the idTiersCaisseConjoint to set
     */
    public void setIdTiersCaisseConjoint(String idTiersCaisseConjoint) {
        this.idTiersCaisseConjoint = idTiersCaisseConjoint;
    }

    /**
     * @param numeroAffilie
     *            the numeroAffilie to set
     */
    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    /**
     * @param numSalarieExterne
     *            the numSalarieExterne to set
     */
    public void setNumSalarieExterne(String numSalarieExterne) {
        this.numSalarieExterne = numSalarieExterne;
    }

    /**
     * Définit le statut du dossier
     * 
     * @param statut
     *            the statut to set
     */
    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getComplementActiviteAllocataire() {
        return complementActiviteAllocataire;
    }

    public void setComplementActiviteAllocataire(String complementActiviteAllocataire) {
        this.complementActiviteAllocataire = complementActiviteAllocataire;
    }
}
