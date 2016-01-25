package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Mod�le de base d'un dossier contenant uniquement les cl�s �trang�res. Ce mod�le est utilis� pour �tablir les
 * jointures entre diff�rents mod�les
 * 
 * @author jts
 */
public class DossierFkModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * activit� de l'allocataire (code syst�me acivit�: salari�, ind�pendant, agricole, ....)
     * 
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_ACTIVITE_ALLOC
     */
    private String activiteAllocataire = null;

    /**
     * compl�ment � l'activit� allocataire (code syst�me compl�ment activit� : mission temporaire,...)
     * 
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_COMPLEMENT_ACTIVITE
     */
    private String complementActiviteAllocataire = null;

    /**
     * �tat du dossier (code syst�me: radi�, suspendu, actif, ...)
     * 
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_ETAT
     */
    private String etatDossier = null;
    /**
     * identifiant de l'allocataire li� au dossier
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
     * identifiant du tiers b�n�ficiaire (si autre que allocataire)
     */
    private String idTiersBeneficiaire = null;
    /**
     * identifiant de la caisse du conjoint
     */
    private String idTiersCaisseConjoint = null;
    /**
     * num�ro de l'affili�
     */
    private String numeroAffilie = null;
    /**
     * num�ro de salari� externe
     */

    private String numSalarieExterne = null;
    /**
     * statut du dossier (code syst�me: statut (normal, suppl�tif, ...)
     * 
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_STATUT
     */
    private String statut = null;

    /**
     * Retourne l'activite d'allocataire (salari�, ind�pendant, ...)
     * 
     * @return the activiteAllocataire
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_ACTIVITE_ALLOC
     */
    public String getActiviteAllocataire() {
        return activiteAllocataire;
    }

    /**
     * Retourne l'�tat du dossier (actif, radi�, ...)
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
     * Retourne l'id du tiers b�n�ficiaire. Provient de web@tiers. Utilis� en cas de paiement direct
     * 
     * @return the idTiersBeneficiaire
     */
    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    /**
     * Retourne l'id de la caisse AF du conjoint. Elle est utilis� pour le calcul des droit dans le cas de dossier ADC
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
     * D�finit l'activite d'allocataire (salari�, ind�pendant, ...)
     * 
     * @param ActiviteAllocataire
     *            the activiteAllocataire to set
     */
    public void setActiviteAllocataire(String ActiviteAllocataire) {
        activiteAllocataire = ActiviteAllocataire;
    }

    /**
     * D�finit l'�tat du dossier (actif, radi�, ...)
     * 
     * @param etatDossier
     *            the etatDossier to set
     */
    public void setEtatDossier(String etatDossier) {
        this.etatDossier = etatDossier;
    }

    /**
     * D�finit l'id du dossier
     */
    @Override
    public void setId(String id) {
        idDossier = id;
    }

    /**
     * D�finit l'id de l'allocataire
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
     * D�finit l'id du dossier du conjoint
     * 
     * @param idDossierConjoint
     *            identifiant du dossier du conjoint
     */
    public void setIdDossierConjoint(String idDossierConjoint) {
        this.idDossierConjoint = idDossierConjoint;
    }

    /**
     * D�finit l'id du tiers b�n�ficiaire (web@tiers) Utilis� en cas de paiement direct
     * 
     * @param idTiersBeneficiaire
     *            the idTiersBeneficiaire to set
     */
    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    /**
     * D�finit l'id de la caisse AF du conjoint. Elle est utilis� pour le calcul des droit dans le cas de dossier ADC
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
     * D�finit le statut du dossier
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
