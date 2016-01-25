package ch.globaz.al.business.models.rafam;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * MODELE A N'UTILISER QUE POUR LA CREATION D'ANNONCE MANQUANTE LORS DU TRAITEMENT D'UN ETAT DU REGISTRE
 * 
 * @author jts
 * @deprecated classe � usage temporaire pour le traitement d'�tat du registre.
 */
@Deprecated
public class AnnonceRafamEtatRegistreModel extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * base l�gale
     */
    private String baseLegale = null;

    /**
     * Indique si l'annonce a �t� annul�e
     */
    private Boolean canceled = null;
    /**
     * canton
     */
    private String canton = null;
    /**
     * Code d'erreur de l'annonce
     * 
     */
    private String codeErreur = null;
    /**
     * Code du type de remarque
     * 
     */
    private String codeRemarque = null;

    /**
     * Code de retour de l'annonce
     * 
     */
    private String codeRetour = null;

    /**
     * Code du statut familial
     */
    private String codeStatutFamilial = null;

    /**
     * Code de type d'activit� du b�n�ficiaire
     * 
     */
    private String codeTypeActivite = null;

    /**
     * date de cr�ation de l'annonce
     */
    private String dateCreation = null;

    /**
     * Date de la mort de l'allocataire
     */
    private String dateMortAllocataire = null;

    /**
     * Date de la mort de l'enfant
     */
    private String dateMortEnfant = null;

    /**
     * date de cr�ation de l'annonce
     */
    private String dateMutation = null;

    /**
     * Date de naissance de l'allocataire
     */
    private String dateNaissanceAllocataire = null;

    /**
     * Date de naissance de l'enfant
     */
    private String dateNaissanceEnfant = null;

    /**
     * date de d�but du droit
     */
    private String debutDroit = null;

    /**
     * Num�ro de l'autre organe concern� en dans le cas d'annonces contradictoire
     */
    private String deliveryOfficeConflict = null;

    /**
     * date d'�ch�ance du droit
     */
    private String echeanceDroit = null;

    /**
     * Etat de l'annonce
     * 
     */
    private String etat = null;

    /**
     * �v�nement d�clencheur
     */
    private String evDeclencheur = null;

    /**
     * Code du type d'allocation
     */
    private String genrePrestation = null;

    /**
     * Id de l'allocataire concern� par l'annonce
     */
    private String idAllocataire;

    /**
     * identifiant
     */
    private String idAnnonce = null;

    /**
     * Id du droit concern� par l'annonce
     */
    private String idDroit = null;

    /**
     * Indique si une erreur interne s'est produite
     */
    private Boolean internalError = null;

    /**
     * Message li� � internalError
     */
    private String internalErrorMessage = null;

    /**
     * R�f�rence interne
     */
    private String internalOfficeReference = null;

    /**
     * minimalStartFlag
     */
    private String minimalStartFlag = null;

    /**
     * Nouveau NSS de l'enfant (dans le cas d'une annonce UPI)
     */
    private String newNssEnfant = null;

    /**
     * nom de l'allocataire
     */
    private String nomAllocataire = null;

    /**
     * nom de l'enfant
     */
    private String nomEnfant = null;

    /**
     * NSS de l'allocataire
     */
    private String nssAllocataire = null;

    /**
     * NSS de l'enfant
     */
    private String nssEnfant = null;

    /**
     * fin de p�riode de chevauchement
     */
    private String overlapPeriodeEnd = null;

    /**
     * d�but de la p�riode de chevauchement
     */
    private String overlapPeriodeStart = null;

    /**
     * pr�nom de l'enfant
     */
    private String prenomAllocataire = null;

    /**
     * pr�nom de l'allocataire
     */
    private String prenomEnfant = null;

    /**
     * record Number de l'annonce � la centrale
     */
    private String recordNumber = null;

    /**
     * sexe de l'allocataire
     */
    private String sexeAllocataire = null;

    /**
     * sexe de l'enfant
     */
    private String sexeEnfant = null;

    /**
     * Type d'annonce
     */
    private String typeAnnonce = null;

    public String getBaseLegale() {
        return baseLegale;
    }

    public Boolean getCanceled() {
        return canceled;
    }

    public String getCanton() {
        return canton;
    }

    public String getCodeErreur() {
        return codeErreur;
    }

    public String getCodeRemarque() {
        return codeRemarque;
    }

    public String getCodeRetour() {
        return codeRetour;
    }

    public String getCodeStatutFamilial() {
        return codeStatutFamilial;
    }

    public String getCodeTypeActivite() {
        return codeTypeActivite;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public String getDateMortAllocataire() {
        return dateMortAllocataire;
    }

    public String getDateMortEnfant() {
        return dateMortEnfant;
    }

    public String getDateMutation() {
        return dateMutation;
    }

    public String getDateNaissanceAllocataire() {
        return dateNaissanceAllocataire;
    }

    public String getDateNaissanceEnfant() {
        return dateNaissanceEnfant;
    }

    public String getDebutDroit() {
        return debutDroit;
    }

    public String getDeliveryOfficeConflict() {
        return deliveryOfficeConflict;
    }

    public String getEcheanceDroit() {
        return echeanceDroit;
    }

    public String getEtat() {
        return etat;
    }

    public String getEvDeclencheur() {
        return evDeclencheur;
    }

    public String getGenrePrestation() {
        return genrePrestation;
    }

    @Override
    public String getId() {
        return idAnnonce;
    }

    public String getIdAllocataire() {
        return idAllocataire;
    }

    public String getIdAnnonce() {
        return idAnnonce;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public Boolean getInternalError() {
        return internalError;
    }

    public String getInternalErrorMessage() {
        return internalErrorMessage;
    }

    public String getInternalOfficeReference() {
        return internalOfficeReference;
    }

    public String getMinimalStartFlag() {
        return minimalStartFlag;
    }

    public String getNewNssEnfant() {
        return newNssEnfant;
    }

    public String getNomAllocataire() {
        return nomAllocataire;
    }

    public String getNomEnfant() {
        return nomEnfant;
    }

    public String getNssAllocataire() {
        return nssAllocataire;
    }

    public String getNssEnfant() {
        return nssEnfant;
    }

    public String getOverlapPeriodeEnd() {
        return overlapPeriodeEnd;
    }

    public String getOverlapPeriodeStart() {
        return overlapPeriodeStart;
    }

    public String getPrenomAllocataire() {
        return prenomAllocataire;
    }

    public String getPrenomEnfant() {
        return prenomEnfant;
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public String getSexeAllocataire() {
        return sexeAllocataire;
    }

    public String getSexeEnfant() {
        return sexeEnfant;
    }

    public String getTypeAnnonce() {
        return typeAnnonce;
    }

    public void setBaseLegale(String baseLegale) {
        this.baseLegale = baseLegale;
    }

    public void setCanceled(Boolean canceled) {
        this.canceled = canceled;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public void setCodeErreur(String codeErreur) {
        this.codeErreur = codeErreur;
    }

    public void setCodeRemarque(String codeRemarque) {
        this.codeRemarque = codeRemarque;
    }

    public void setCodeRetour(String codeRetour) {
        this.codeRetour = codeRetour;
    }

    public void setCodeStatutFamilial(String codeStatutFamilial) {
        this.codeStatutFamilial = codeStatutFamilial;
    }

    public void setCodeTypeActivite(String codeTypeActivite) {
        this.codeTypeActivite = codeTypeActivite;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setDateMortAllocataire(String dateMortAllocataire) {
        this.dateMortAllocataire = dateMortAllocataire;
    }

    public void setDateMortEnfant(String dateMortEnfant) {
        this.dateMortEnfant = dateMortEnfant;
    }

    public void setDateMutation(String dateMutation) {
        this.dateMutation = dateMutation;
    }

    public void setDateNaissanceAllocataire(String dateNaissanceAllocataire) {
        this.dateNaissanceAllocataire = dateNaissanceAllocataire;
    }

    public void setDateNaissanceEnfant(String dateNaissanceEnfant) {
        this.dateNaissanceEnfant = dateNaissanceEnfant;
    }

    public void setDebutDroit(String debutDroit) {
        this.debutDroit = debutDroit;
    }

    public void setDeliveryOfficeConflict(String deliveryOfficeConflict) {
        this.deliveryOfficeConflict = deliveryOfficeConflict;
    }

    public void setEcheanceDroit(String echeanceDroit) {
        this.echeanceDroit = echeanceDroit;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setEvDeclencheur(String evDeclencheur) {
        this.evDeclencheur = evDeclencheur;
    }

    public void setGenrePrestation(String genrePrestation) {
        this.genrePrestation = genrePrestation;
    }

    @Override
    public void setId(String id) {
        idAnnonce = id;
    }

    public void setIdAllocataire(String idAllocataire) {
        this.idAllocataire = idAllocataire;
    }

    public void setIdAnnonce(String idAnnonce) {
        this.idAnnonce = idAnnonce;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setInternalError(Boolean internalError) {
        this.internalError = internalError;
    }

    public void setInternalErrorMessage(String internalErrorMessage) {
        this.internalErrorMessage = internalErrorMessage;
    }

    public void setInternalOfficeReference(String internalOfficeReference) {
        this.internalOfficeReference = internalOfficeReference;
    }

    public void setMinimalStartFlag(String minimalStartFlag) {
        this.minimalStartFlag = minimalStartFlag;
    }

    public void setNewNssEnfant(String newNssEnfant) {
        this.newNssEnfant = newNssEnfant;
    }

    public void setNomAllocataire(String nomAllocataire) {
        this.nomAllocataire = nomAllocataire;
    }

    public void setNomEnfant(String nomEnfant) {
        this.nomEnfant = nomEnfant;
    }

    public void setNssAllocataire(String nssAllocataire) {
        this.nssAllocataire = nssAllocataire;
    }

    public void setNssEnfant(String nssEnfant) {
        this.nssEnfant = nssEnfant;
    }

    public void setOverlapPeriodeEnd(String overlapPeriodeEnd) {
        this.overlapPeriodeEnd = overlapPeriodeEnd;
    }

    public void setOverlapPeriodeStart(String overlapPeriodeStart) {
        this.overlapPeriodeStart = overlapPeriodeStart;
    }

    public void setPrenomAllocataire(String prenomAllocataire) {
        this.prenomAllocataire = prenomAllocataire;
    }

    public void setPrenomEnfant(String prenomEnfant) {
        this.prenomEnfant = prenomEnfant;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public void setSexeAllocataire(String sexeAllocataire) {
        this.sexeAllocataire = sexeAllocataire;
    }

    public void setSexeEnfant(String sexeEnfant) {
        this.sexeEnfant = sexeEnfant;
    }

    public void setTypeAnnonce(String typeAnnonce) {
        this.typeAnnonce = typeAnnonce;
    }
}
