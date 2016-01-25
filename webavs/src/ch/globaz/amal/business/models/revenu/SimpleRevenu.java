package ch.globaz.amal.business.models.revenu;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleRevenu extends JadeSimpleModel implements Cloneable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String CS_TYPE_SOURCIER = "42002601";
    private String anneeTaxation = null;

    private String codeSuspendu = null;
    private String dateAvisTaxation = null;
    private String dateSaisie = null;
    private String dateTraitement = null;
    private String etatCivil = null;
    private String etatCivilLibelle = null;
    private String flagTraitementDoc = null;
    private String idContribuable = null;
    private String idRevenu = null;
    private String idRevenuHistorique = null;
    private Boolean isSourcier = false;
    private String nbEnfants = null;
    private String nbEnfantSuspens = null;
    private String nbJours = null;
    private String noLotAvisTaxation = null;
    private String numeroContribuable = null;
    private String profession = null;
    private String revDetUnique = null;
    private Boolean revDetUniqueOuiNon = null;
    private String typeRevenu = null;
    private String typeSource = null;
    private String typeSourceLibelle = null;
    private String typeTaxation = null;
    private String typeTaxationLibelle = null;

    @Override
    public SimpleRevenu clone() throws CloneNotSupportedException {
        return (SimpleRevenu) super.clone();
    }

    /**
     * @return the anneeTaxation
     */
    public String getAnneeTaxation() {
        return anneeTaxation;
    }

    /**
     * @return the codeSuspendu
     */
    public String getCodeSuspendu() {
        return codeSuspendu;
    }

    /**
     * @return the dateAvisTaxation
     */
    public String getDateAvisTaxation() {
        return dateAvisTaxation;
    }

    /**
     * @return the dateSaisie
     */
    public String getDateSaisie() {
        return dateSaisie;
    }

    /**
     * @return the dateTraitement
     */
    public String getDateTraitement() {
        return dateTraitement;
    }

    /**
     * @return the etatCivil
     */
    public String getEtatCivil() {
        return etatCivil;
    }

    public String getEtatCivilLibelle() {
        return etatCivilLibelle;
    }

    public String getFlagTraitementDoc() {
        return flagTraitementDoc;
    }

    @Override
    public String getId() {
        return idRevenu;
    }

    /**
     * @return the idContribuable
     */
    public String getIdContribuable() {
        return idContribuable;
    }

    public String getIdRevenu() {
        return idRevenu;
    }

    /**
     * @return the idRevenuHistorique
     */
    public String getIdRevenuHistorique() {
        return idRevenuHistorique;
    }

    public Boolean getIsSourcier() {
        return isSourcier;
    }

    /**
     * @return the nbEnfants
     */
    public String getNbEnfants() {
        return nbEnfants;
    }

    /**
     * @return the nbEnfantSuspens
     */
    public String getNbEnfantSuspens() {
        return nbEnfantSuspens;
    }

    /**
     * @return the nbJours
     */
    public String getNbJours() {
        return nbJours;
    }

    /**
     * @return the noLotAvisTaxation
     */
    public String getNoLotAvisTaxation() {
        return noLotAvisTaxation;
    }

    public String getNumeroContribuable() {
        return numeroContribuable;
    }

    /**
     * @return the profession
     */
    public String getProfession() {
        return profession;
    }

    /**
     * @return the revDetUnique
     */
    public String getRevDetUnique() {
        return revDetUnique;
    }

    /**
     * @return the revDetUniqueOuiNon
     */
    public Boolean getRevDetUniqueOuiNon() {
        return revDetUniqueOuiNon;
    }

    public String getTypeRevenu() {
        return typeRevenu;
    }

    /**
     * @return the typeSource
     */
    public String getTypeSource() {
        return typeSource;
    }

    /**
     * @return the typeSourceLibelle
     */
    public String getTypeSourceLibelle() {
        return typeSourceLibelle;
    }

    /**
     * @return the typeTaxation
     */
    public String getTypeTaxation() {
        return typeTaxation;
    }

    public String getTypeTaxationLibelle() {
        return typeTaxationLibelle;
    }

    public Boolean isSourcier() {
        return SimpleRevenu.CS_TYPE_SOURCIER.equals(typeRevenu);
    }

    /**
     * @param anneeTaxation
     *            the anneeTaxation to set
     */
    public void setAnneeTaxation(String anneeTaxation) {
        this.anneeTaxation = anneeTaxation;
    }

    /**
     * @param codeSuspendu
     *            the codeSuspendu to set
     */
    public void setCodeSuspendu(String codeSuspendu) {
        this.codeSuspendu = codeSuspendu;
    }

    /**
     * @param dateAvisTaxation
     *            the dateAvisTaxation to set
     */
    public void setDateAvisTaxation(String dateAvisTaxation) {
        this.dateAvisTaxation = dateAvisTaxation;
    }

    /**
     * @param dateSaisie
     *            the dateSaisie to set
     */
    public void setDateSaisie(String dateSaisie) {
        this.dateSaisie = dateSaisie;
    }

    /**
     * @param dateTraitement
     *            the dateTraitement to set
     */
    public void setDateTraitement(String dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    /**
     * @param etatCivil
     *            the etatCivil to set
     */
    public void setEtatCivil(String etatCivil) {
        this.etatCivil = etatCivil;
    }

    public void setEtatCivilLibelle(String etatCivilLibelle) {
        this.etatCivilLibelle = etatCivilLibelle;
    }

    public void setFlagTraitementDoc(String flagTraitementDoc) {
        this.flagTraitementDoc = flagTraitementDoc;
    }

    @Override
    public void setId(String id) {
        idRevenu = id;
    }

    /**
     * @param idContribuable
     *            the idContribuable to set
     */
    public void setIdContribuable(String idContribuable) {
        this.idContribuable = idContribuable;
    }

    public void setIdRevenu(String idRevenu) {
        this.idRevenu = idRevenu;
    }

    /**
     * @param idRevenuHistorique
     *            the idRevenuHistorique to set
     */
    public void setIdRevenuHistorique(String idRevenuHistorique) {
        this.idRevenuHistorique = idRevenuHistorique;
    }

    public void setIsSourcier(Boolean isSourcier) {
        this.isSourcier = isSourcier;
    }

    /**
     * @param nbEnfants
     *            the nbEnfants to set
     */
    public void setNbEnfants(String nbEnfants) {
        this.nbEnfants = nbEnfants;
    }

    /**
     * @param nbEnfantSuspens
     *            the nbEnfantSuspens to set
     */
    public void setNbEnfantSuspens(String nbEnfantSuspens) {
        this.nbEnfantSuspens = nbEnfantSuspens;
    }

    /**
     * @param nbJours
     *            the nbJours to set
     */
    public void setNbJours(String nbJours) {
        this.nbJours = nbJours;
    }

    /**
     * @param noLotAvisTaxation
     *            the noLotAvisTaxation to set
     */
    public void setNoLotAvisTaxation(String noLotAvisTaxation) {
        this.noLotAvisTaxation = noLotAvisTaxation;
    }

    public void setNumeroContribuable(String numeroContribuable) {
        this.numeroContribuable = numeroContribuable;
    }

    /**
     * @param profession
     *            the profession to set
     */
    public void setProfession(String profession) {
        this.profession = profession;
    }

    /**
     * @param revDetUnique
     *            the revDetUnique to set
     */
    public void setRevDetUnique(String revDetUnique) {
        this.revDetUnique = revDetUnique;
    }

    /**
     * @param revDetUniqueOuiNon
     *            the revDetUniqueOuiNon to set
     */
    public void setRevDetUniqueOuiNon(Boolean revDetUniqueOuiNon) {
        this.revDetUniqueOuiNon = revDetUniqueOuiNon;
    }

    public void setTypeRevenu(String typeRevenu) {
        this.typeRevenu = typeRevenu;
    }

    /**
     * @param typeSource
     *            the typeSource to set
     */
    public void setTypeSource(String typeSource) {
        this.typeSource = typeSource;
    }

    /**
     * @param typeSourceLibelle
     *            the typeSourceLibelle to set
     */
    public void setTypeSourceLibelle(String typeSourceLibelle) {
        this.typeSourceLibelle = typeSourceLibelle;
    }

    /**
     * @param typeTaxation
     *            the typeTaxation to set
     */
    public void setTypeTaxation(String typeTaxation) {
        this.typeTaxation = typeTaxation;
    }

    public void setTypeTaxationLibelle(String typeTaxationLibelle) {
        this.typeTaxationLibelle = typeTaxationLibelle;
    }
}