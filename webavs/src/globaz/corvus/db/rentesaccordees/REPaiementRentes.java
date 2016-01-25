/*
 * Créé le 5 nov. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author scr
 */
public class REPaiementRentes extends BEntity {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codePrestation = "";
    private String idAdressePmtAllocNoel = "";
    private String idAdressePmtRente = "";
    private String idAdresseStd = "";

    // private String nom = "";
    // private String prenom = "";
    // private String nss = "";
    private String idCompteAnnexe = "";
    private String idRenteAccordee = "";
    private String idTiersAdressePmt = "";
    private String idTiersBeneficiaire = "";
    private Boolean isAttenteMajBlocage = Boolean.FALSE;
    private Boolean isAttenteMajRetenue = Boolean.FALSE;
    private Boolean isPrestationBloquee = Boolean.FALSE;
    private Boolean isRetenue = Boolean.FALSE;

    private String level = "";
    private String montant = "";
    private String nomTBE = "";

    private String nssTBE = "";
    private String prenomTBE = "";
    private String referencePmt = "";
    private String sousTypeCodePrestation = "";

    // private String libelleEcriture = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return null; // PAS DE TABLES !!!
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idRenteAccordee = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        level = statement.dbReadNumeric(REPaiementRentesManager.FIELDNAME_LEVEL);
        idTiersBeneficiaire = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        idTiersAdressePmt = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT);
        idCompteAnnexe = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_COMPTE_ANNEXE);
        montant = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);
        referencePmt = statement.dbReadString(REPrestationsAccordees.FIELDNAME_REFERENCE_PMT);
        codePrestation = statement.dbReadString(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
        sousTypeCodePrestation = statement.dbReadString(REPrestationsAccordees.FIELDNAME_SOUS_TYPE_GENRE_PRESTATION);
        idAdressePmtRente = statement.dbReadNumeric(REPaiementRentesManager.FIELDNAME_ID_ADR_PMT_RENTE);
        idAdresseStd = statement.dbReadNumeric(REPaiementRentesManager.FIELDNAME_ID_ADR_PMT_STANDARD);

        // TODO valider la récupération de l'id !!!
        idAdressePmtAllocNoel = statement.dbReadNumeric(REPaiementRentesManager.FIELDNAME_ID_ADR_PMT_ALLOCATION_NOEL);

        // nom = statement.dbReadString(REPaiementRentesManager.FIELDNAME_NOM);
        // prenom =
        // statement.dbReadString(REPaiementRentesManager.FIELDNAME_PRENOM);
        // nss =
        // statement.dbReadString(REPaiementRentesManager.FIELDNAME_NUM_AVS);

        isPrestationBloquee = statement.dbReadBoolean(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE);
        isRetenue = statement.dbReadBoolean(REPrestationsAccordees.FIELDNAME_IS_RETENUES);
        nomTBE = statement.dbReadString(REPaiementRentesManager.FIELDNAME_NOM_BP);
        prenomTBE = statement.dbReadString(REPaiementRentesManager.FIELDNAME_PRENOM_BP);
        nssTBE = statement.dbReadString(REPaiementRentesManager.FIELDNAME_NSS_BP);
        isAttenteMajBlocage = statement.dbReadBoolean(REPrestationsAccordees.FIELDNAME_IS_ATTENTE_MAJ_BLOCAGE);
        isAttenteMajRetenue = statement.dbReadBoolean(REPrestationsAccordees.FIELDNAME_IS_ATTENTE_MAJ_RETENUE);

    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        _addError(statement.getTransaction(), "interdit d'ajouter");
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    public String getCodePrestation() {
        return codePrestation;
    }

    /**
     * @return l'id de l'adresse de paiement dans le domaine des allocations de Noël
     */
    public final String getIdAdressePmtAllocNoel() {
        return idAdressePmtAllocNoel;
    }

    public String getIdAdressePmtRente() {
        return idAdressePmtRente;
    }

    public String getIdAdresseStd() {
        return idAdresseStd;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public String getIdTiersAdressePmt() {
        return idTiersAdressePmt;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public Boolean getIsAttenteMajBlocage() {
        return isAttenteMajBlocage;
    }

    public Boolean getIsAttenteMajRetenue() {
        return isAttenteMajRetenue;
    }

    public Boolean getIsPrestationBloquee() {
        return isPrestationBloquee;
    }

    public Boolean getIsRetenue() {
        return isRetenue;
    }

    public String getLevel() {
        return level;
    }

    public String getMontant() {
        return montant;
    }

    // public String getNom() {
    // return nom;
    // }
    //
    // public void setNom(String nom) {
    // this.nom = nom;
    // }
    //
    // public String getPrenom() {
    // return prenom;
    // }
    //
    // public void setPrenom(String prenom) {
    // this.prenom = prenom;
    // }
    //
    // public String getNss() {
    // return nss;
    // }
    //
    // public void setNss(String nss) {
    // this.nss = nss;
    // }

    public String getNomTBE() {
        return nomTBE;
    }

    public String getNssTBE() {
        return nssTBE;
    }

    public String getPrenomTBE() {
        return prenomTBE;
    }

    public String getReferencePmt() {
        return referencePmt;
    }

    /**
     * Retourne le sous-type du genre de prestation.
     * 
     * @return the sousTypeCodePrestation
     */
    public final String getSousTypeCodePrestation() {
        return sousTypeCodePrestation;
    }

    public void setCodePrestation(String codePrestation) {
        this.codePrestation = codePrestation;
    }

    /**
     * Définit l'id de l'adresse de paiement dans le domaine des allocations de Noël
     * 
     * @param idAdressePmtAllocNoel
     *            id de l'adresse de paiement dans le domaine des allocations de Noël
     */
    public final void setIdAdressePmtAllocNoel(String idAdressePmtAllocNoel) {
        this.idAdressePmtAllocNoel = idAdressePmtAllocNoel;
    }

    public void setIdAdressePmtRente(String idAdressePmtRente) {
        this.idAdressePmtRente = idAdressePmtRente;
    }

    public void setIdAdresseStd(String idAdresseStd) {
        this.idAdresseStd = idAdresseStd;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setIdRenteAccordee(String idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public void setIdTiersAdressePmt(String idTiersAdressePmt) {
        this.idTiersAdressePmt = idTiersAdressePmt;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setIsAttenteMajBlocage(Boolean isAttenteMajBlocage) {
        this.isAttenteMajBlocage = isAttenteMajBlocage;
    }

    public void setIsAttenteMajRetenue(Boolean isAttenteMajRetenue) {
        this.isAttenteMajRetenue = isAttenteMajRetenue;
    }

    // public String getLibelleEcriture() {
    // return libelleEcriture;
    // }
    //
    // public void setLibelleEcriture(String libelleEcriture) {
    // this.libelleEcriture = libelleEcriture;
    // }

    public void setIsPrestationBloquee(Boolean isPrestationBloquee) {
        this.isPrestationBloquee = isPrestationBloquee;
    }

    public void setIsRetenue(Boolean isRetenue) {
        this.isRetenue = isRetenue;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setNomTBE(String nomTBE) {
        this.nomTBE = nomTBE;
    }

    public void setNssTBE(String nssTBE) {
        this.nssTBE = nssTBE;
    }

    public void setPrenomTBE(String prenomTBE) {
        this.prenomTBE = prenomTBE;
    }

    public void setReferencePmt(String referencePmt) {
        this.referencePmt = referencePmt;
    }

    /**
     * @param sousTypeCodePrestation
     *            the sousTypeCodePrestation to set
     */
    public final void setSousTypeCodePrestation(String sousTypeCodePrestation) {
        this.sousTypeCodePrestation = sousTypeCodePrestation;
    }

    public String toStringggg() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n\nRente no :" + getIdRenteAccordee()).append("\n");
        sb.append("\t").append("level = ").append(level).append("\n");
        sb.append("\t").append("idTiersBeneficiaire = ").append(idTiersBeneficiaire).append("\n");
        sb.append("\t").append("idTiersAdressePmt = ").append(idTiersAdressePmt).append("\n");
        sb.append("\t").append("idCompteAnnexe = ").append(idCompteAnnexe).append("\n");
        sb.append("\t").append("montant = ").append(montant).append("\n");
        sb.append("\t").append("referencePmt = ").append(referencePmt).append("\n");
        sb.append("\t").append("codePrestation = ").append(codePrestation).append("\n");
        sb.append("\t").append("idAdressePmtRente = ").append(idAdressePmtRente).append("\n");
        sb.append("\t").append("idAdresseStd = ").append(idAdresseStd).append("\n");
        // sb.append("\t").append("nom = ").append(nom).append("\n");
        // sb.append("\t").append("prenom = ").append(prenom).append("\n");
        // sb.append("\t").append("nss = ").append(nss).append("\n");
        sb.append("\t").append("nomTBE = ").append(nomTBE).append("\n");
        sb.append("\t").append("prenomTBE = ").append(prenomTBE).append("\n");
        sb.append("\t").append("nssTBE = ").append(nssTBE).append("\n");

        sb.append("\t").append("isPrestationBloquee = ").append(isPrestationBloquee).append("\n");
        sb.append("\t").append("isRetenue = ").append(isRetenue).append("\n");

        return sb.toString();
    }

}
