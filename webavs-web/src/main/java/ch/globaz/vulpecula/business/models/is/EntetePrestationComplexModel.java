package ch.globaz.vulpecula.business.models.is;

import globaz.jade.persistence.model.JadeComplexModel;

public class EntetePrestationComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = -4307493889847851996L;

    private String idEntete;
    private String idTiers;
    private String idTiersAffilie;
    private String idDossier;
    private String titre;
    private String nom;
    private String prenom;
    private String numeroAffilie;
    private String numAvsActuel;
    private String dateNaissance;
    private String langue;
    private String montantTotal;
    private Boolean retenueImpot;
    private String periodeDe;
    private String periodeA;
    private String typeFacturation;
    private String npa;
    private String localite;
    private String referencePermis;
    private String raisonSociale;
    private String activiteAllocataire;
    private String idJournal;
    private String dateComptableVersement;
    private String numFactureRecap;
    private String spy;

    /**
     * Ces attributs ne sont pas récupérés depuis la base de données, ils sont settés grâce au service d'affiliation.
     */
    private String libelleCaisseAF;
    private String codeCaisseAF;
    // Canton de résidence de l'affilié
    private String cantonResidence;

    public String getIdEntete() {
        return idEntete;
    }

    public void setIdEntete(String idEntete) {
        this.idEntete = idEntete;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public String getNumAvsActuel() {
        return numAvsActuel;
    }

    public void setNumAvsActuel(String numAvsActuel) {
        this.numAvsActuel = numAvsActuel;
    }

    public String getCantonResidence() {
        return cantonResidence;
    }

    public void setCantonResidence(String cantonResidence) {
        this.cantonResidence = cantonResidence;
    }

    public String getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(String montantTotal) {
        this.montantTotal = montantTotal;
    }

    public Boolean getRetenueImpot() {
        return retenueImpot;
    }

    public void setRetenueImpot(Boolean retenueImpot) {
        this.retenueImpot = retenueImpot;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public String getPeriodeDe() {
        return periodeDe;
    }

    public void setPeriodeDe(String periodeDe) {
        this.periodeDe = periodeDe;
    }

    public String getPeriodeA() {
        return periodeA;
    }

    public void setPeriodeA(String periodeA) {
        this.periodeA = periodeA;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }

    @Override
    public String getId() {
        return idEntete;
    }

    @Override
    public void setId(String id) {
        idEntete = id;
    }

    public String getTypeFacturation() {
        return typeFacturation;
    }

    public void setTypeFacturation(String typeFacturation) {
        this.typeFacturation = typeFacturation;
    }

    public String getIdTiersAffilie() {
        return idTiersAffilie;
    }

    public void setIdTiersAffilie(String idTiersAffilie) {
        this.idTiersAffilie = idTiersAffilie;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNpa() {
        return npa;
    }

    public void setNpa(String npa) {
        this.npa = npa;
    }

    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public String getReferencePermis() {
        return referencePermis;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public String getActiviteAllocataire() {
        return activiteAllocataire;
    }

    public void setActiviteAllocataire(String activiteAllocataire) {
        this.activiteAllocataire = activiteAllocataire;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
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

    public void setReferencePermis(String referencePermis) {
        this.referencePermis = referencePermis;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * @return the dateComptableVersement
     */
    public String getDateComptableVersement() {
        return dateComptableVersement;
    }

    /**
     * @param dateComptableVersement the dateComptableVersement to set
     */
    public void setDateComptableVersement(String dateComptableVersement) {
        this.dateComptableVersement = dateComptableVersement;
    }

    /**
     * @return the numFactureRecap
     */
    public String getNumFactureRecap() {
        return numFactureRecap;
    }

    /**
     * @param numFactureRecap the numFactureRecap to set
     */
    public void setNumFactureRecap(String numFactureRecap) {
        this.numFactureRecap = numFactureRecap;
    }

}
