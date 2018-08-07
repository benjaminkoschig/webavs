package ch.globaz.al.business.models.rafam;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Modèle d'une annonce RAFAM
 *
 * @author jts
 *
 */
public class AnnonceRafamModel extends JadeSimpleModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * base légale
     */
    private String baseLegale = null;
    /**
     * Indique si l'annonce a été annulée
     */
    private Boolean canceled = null;
    /**
     * canton
     */
    private String canton = null;
    /**
     * Code du type de remarque
     */
    private String codeRemarque = null;
    /**
     * Code de retour de l'annonce
     */
    private String codeRetour = null;
    /**
     * Code du statut familial
     */
    private String codeStatutFamilial = null;
    /**
     * Code de type d'activité du bénéficiaire
     */
    private String codeTypeActivite = null;
    /**
     * date de création de l'annonce
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
     * date de création de l'annonce
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

    /** Date de réception du retour de l'annonce */
    private String dateReception = null;

    /**
     * date de début du droit
     */
    private String debutDroit = null;

    /**
     * Indique si l'annonce est liée à un employeur délégué
     */
    private Boolean delegated = null;

    /**
     * date d'échéance du droit
     */
    private String echeanceDroit = null;

    /**
     * Etat de l'annonce
     *
     */
    private String etat = null;

    /**
     * évènement déclencheur
     */
    private String evDeclencheur = null;

    /**
     * Code du type d'allocation
     */
    private String genrePrestation = null;

    /**
     * Id de l'allocataire concerné par l'annonce
     */
    private String idAllocataire;

    /**
     * identifiant
     */
    private String idAnnonce = null;

    /**
     * Id du droit concerné par l'annonce
     */
    private String idDroit = null;

    /**
     * Indique si une erreur interne s'est produite
     */
    private Boolean internalError = null;

    /**
     * Message lié à internalError
     */
    private String internalErrorMessage = null;

    /**
     * Référence interne
     */
    private String internalOfficeReference = null;

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
     * prénom de l'enfant
     */
    private String prenomAllocataire = null;

    /**
     * prénom de l'allocataire
     */
    private String prenomEnfant = null;

    /**
     * record Number de l'annonce à la centrale
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

    private String officeIdentifier;
    private String officeBranch;
    private String legalOffice;

    /**
     * pays de domicile de l'enfant (obligatoire depuis xsd 4.0)
     */
    private String codeCentralePaysEnfant;

    /**
     * numéro IDE (ajouté depuis xsd 4.0)
     */
    private String numeroIDE;

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

    public String getDateReception() {
        return dateReception;
    }

    public String getDebutDroit() {
        return debutDroit;
    }

    public Boolean getDelegated() {
        return delegated;
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

    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    public void setDebutDroit(String debutDroit) {
        this.debutDroit = debutDroit;
    }

    public void setDelegated(Boolean delegated) {
        this.delegated = delegated;
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

    public String getLegalOffice() {
        return legalOffice;
    }

    public void setLegalOffice(String legalOffice) {
        this.legalOffice = legalOffice;
    }

    public String getOfficeIdentifier() {
        return officeIdentifier;
    }

    public void setOfficeIdentifier(String officeIdentifier) {
        this.officeIdentifier = officeIdentifier;
    }

    public String getOfficeBranch() {
        return officeBranch;
    }

    public void setOfficeBranch(String officeBranch) {
        this.officeBranch = officeBranch;
    }

    public String getNumeroIDE() {
        return numeroIDE;
    }

    public void setNumeroIDE(String numeroIDE) {
        this.numeroIDE = numeroIDE;
    }

    public String getCodeCentralePaysEnfant() {
        return codeCentralePaysEnfant;
    }

    public void setCodeCentralePaysEnfant(String codeCentralePaysEnfant) {
        this.codeCentralePaysEnfant = codeCentralePaysEnfant;
    }
}
