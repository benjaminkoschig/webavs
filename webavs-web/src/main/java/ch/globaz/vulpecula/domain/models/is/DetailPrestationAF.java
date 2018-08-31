package ch.globaz.vulpecula.domain.models.is;

import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.common.Montant;

/**
 * POJO permettant de mapper le contenu d'une requête qui s'occupe de récupérer des prestations groupées par tiers
 * employeur, allocataire et tiers bénéficiaires.
 * La requête permet de définir par sous-totaux quels sont les montants à allouer aux différents tiers bénéficiaire.
 * 
 * @author jwe
 * 
 */
public class DetailPrestationAF implements DomainEntity {

    private Montant montantPrestation;
    private String idTiersBeneficiaire;
    private String nomTiersBeneficiaire;
    private String prenomTiersBeneficiaire;
    private String periodeDebut;
    private String activiteAllocataire;
    private String titreAllocataire;
    private String prenomAllocataire;
    private String nssAllocataire;
    private String dateNaissanceAllocataire;
    private String referencePermis;
    private String nomAllocataire;
    private String periodeFin;
    private String raisonSocialEmployeur;
    private String idTiersAllocataire;
    private String dateComptabilisation;
    private String typeFacturation;
    private String numeroAffilie;
    private String idTiersEmployeur;
    private String idJournalCA;
    private Boolean isImpotSource = false;
    private String langueTiersAllocataire;
    private String numeroDossier;
    private String numeroFacture;
    private String libelleCaisseAF;
    private String codeCaisseAF;
    private String cantonResidence;
    private Montant montantImpotSource;
    private Boolean isBenefEmployeur = false;

    @Override
    public String getId() {
        return null;
    }

    /**
     * Id de la prestation
     */
    @Override
    public void setId(String id) {

    }

    @Override
    public String getSpy() {
        return null;
    }

    @Override
    public void setSpy(String spy) {

    }

    public Montant getMontantPrestation() {
        return montantPrestation;
    }

    public void setMontantPrestation(Montant montantPrestation) {
        this.montantPrestation = montantPrestation;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public String getPeriodeDebut() {
        return periodeDebut;
    }

    public void setPeriodeDebut(String periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public String getActiviteAllocataire() {
        return activiteAllocataire;
    }

    public void setActiviteAllocataire(String activiteAllocataire) {
        this.activiteAllocataire = activiteAllocataire;
    }

    public String getTitreAllocataire() {
        return titreAllocataire;
    }

    public void setTitreAllocataire(String titreAllocataire) {
        this.titreAllocataire = titreAllocataire;
    }

    public String getPrenomAllocataire() {
        return prenomAllocataire;
    }

    public void setPrenomAllocataire(String prenomAllocataire) {
        this.prenomAllocataire = prenomAllocataire;
    }

    public String getNssAllocataire() {
        return nssAllocataire;
    }

    public void setNssAllocataire(String nssAllocataire) {
        this.nssAllocataire = nssAllocataire;
    }

    public String getDateNaissanceAllocataire() {
        return dateNaissanceAllocataire;
    }

    public void setDateNaissanceAllocataire(String dateNaissanceAllocataire) {
        this.dateNaissanceAllocataire = dateNaissanceAllocataire;
    }

    public String getReferencePermis() {
        return referencePermis;
    }

    public void setReferencePermis(String referencePermis) {
        this.referencePermis = referencePermis;
    }

    public String getNomAllocataire() {
        return nomAllocataire;
    }

    public void setNomAllocataire(String nomAllocataire) {
        this.nomAllocataire = nomAllocataire;
    }

    public String getPeriodeFin() {
        return periodeFin;
    }

    public void setPeriodeFin(String periodeFin) {
        this.periodeFin = periodeFin;
    }

    public String getRaisonSocialEmployeur() {
        return raisonSocialEmployeur;
    }

    public void setRaisonSocialEmployeur(String raisonSocialEmployeur) {
        this.raisonSocialEmployeur = raisonSocialEmployeur;
    }

    public String getIdTiersAllocataire() {
        return idTiersAllocataire;
    }

    public void setIdTiersAllocataire(String idTiersAllocataire) {
        this.idTiersAllocataire = idTiersAllocataire;
    }

    public String getDateComptabilisation() {
        return dateComptabilisation;
    }

    public void setDateComptabilisation(String dateComptabilisation) {
        this.dateComptabilisation = dateComptabilisation;
    }

    public String getTypeFacturation() {
        return typeFacturation;
    }

    public void setTypeFacturation(String typeFacturation) {
        this.typeFacturation = typeFacturation;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public String getIdTiersEmployeur() {
        return idTiersEmployeur;
    }

    public void setIdTiersEmployeur(String idTiersEmployeur) {
        this.idTiersEmployeur = idTiersEmployeur;
    }

    public String getIdJournalCA() {
        return idJournalCA;
    }

    public void setIdJournalCA(String idJournalCA) {
        this.idJournalCA = idJournalCA;
    }

    public Boolean isImpotSource() {
        return isImpotSource;
    }

    public void setImpotSource(Boolean isImpotSource) {
        this.isImpotSource = isImpotSource;
    }

    public String getLangueTiersAllocataire() {
        return langueTiersAllocataire;
    }

    public void setLangueTiersAllocataire(String langueTiersAllocataire) {
        this.langueTiersAllocataire = langueTiersAllocataire;
    }

    public String getNumeroDossier() {
        return numeroDossier;
    }

    public void setNumeroDossier(String numeroDossier) {
        this.numeroDossier = numeroDossier;
    }

    public String getNumeroFacture() {
        return numeroFacture;
    }

    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public String getLibelleCaisseAF() {
        return libelleCaisseAF;
    }

    public void setLibelleCaisseAF(String libelleCaisseAF) {
        this.libelleCaisseAF = libelleCaisseAF;
    }

    public String getCodeCaisseAF() {
        return codeCaisseAF;
    }

    public void setCodeCaisseAF(String codeCaisseAF) {
        this.codeCaisseAF = codeCaisseAF;
    }

    public String getCantonResidence() {
        return cantonResidence;
    }

    public void setCantonResidence(String cantonResidence) {
        this.cantonResidence = cantonResidence;
    }

    public Montant getMontantImpotSource() {
        return montantImpotSource;
    }

    public void setMontantImpotSource(Montant montantImpotSource) {
        this.montantImpotSource = montantImpotSource;
    }

    public String getNomTiersBeneficiaire() {
        return nomTiersBeneficiaire;
    }

    public void setNomTiersBeneficiaire(String nomTiersBeneficiaire) {
        this.nomTiersBeneficiaire = nomTiersBeneficiaire;
    }

    public String getPrenomTiersBeneficiaire() {
        return prenomTiersBeneficiaire;
    }

    public void setPrenomTiersBeneficiaire(String prenomTiersBeneficiaire) {
        this.prenomTiersBeneficiaire = prenomTiersBeneficiaire;
    }

    public Boolean isBenefEmployeur() {
        return isBenefEmployeur;
    }

    public void setIsBenefEmployeur(Boolean isBenefEmployeur) {
        this.isBenefEmployeur = isBenefEmployeur;
    }

}
