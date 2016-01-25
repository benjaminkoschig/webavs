package globaz.corvus.db.rentesaccordees;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;

public class RERenteAccordeeFamille extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codePrestation;
    private String csEtatRenteAccordee;
    private String dateDebutDroit;
    private String dateFinDroit;
    private String idBaseCalcul;
    private String idDomaineApplicationAdressePaiement;
    private String idRenteAccordee;
    private String idTiersAdressePaiement;
    private String idTiersBaseCalcul;
    private String idTiersBeneficiaire;
    private String montantPrestation;
    private String nomBeneficiaire;
    private String numeroAvsBeneficiaire;
    private String prenomBeneficiaire;
    private boolean prestationBloquee;

    public RERenteAccordeeFamille() {
        super();

        codePrestation = "";
        csEtatRenteAccordee = "";
        dateDebutDroit = "";
        dateFinDroit = "";
        idBaseCalcul = "";
        idDomaineApplicationAdressePaiement = "";
        idRenteAccordee = "";
        idTiersAdressePaiement = "";
        idTiersBaseCalcul = "";
        idTiersBeneficiaire = "";
        montantPrestation = "";
        nomBeneficiaire = "";
        numeroAvsBeneficiaire = "";
        prenomBeneficiaire = "";
        prestationBloquee = false;
    }

    @Override
    protected String _getFields(final BStatement statement) {

        String tableRenteAccordee = _getCollection() + RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE;
        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
        String tableBaseCalcul = _getCollection() + REBasesCalcul.TABLE_NAME_BASES_CALCUL;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;
        String tableInformationComptable = _getCollection() + REInformationsComptabilite.TABLE_NAME_INFO_COMPTA;

        StringBuilder sql = new StringBuilder();

        sql.append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL).append(",");

        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_1).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_2).append(",");

        sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL).append(",");

        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE)
                .append(",");

        sql.append(tableBaseCalcul).append(".").append(REBasesCalcul.FIELDNAME_ID_TIERS_BASE_CALCUL).append(",");

        sql.append(tableInformationComptable).append(".")
                .append(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT).append(",");
        sql.append(tableInformationComptable).append(".")
                .append(REInformationsComptabilite.FIELDNAME_ID_DOMAINE_APPLICATION);

        return sql.toString();
    }

    @Override
    protected String _getFrom(final BStatement statement) {

        String tableRenteAccordee = _getCollection() + RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE;
        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
        String tableBaseCalcul = _getCollection() + REBasesCalcul.TABLE_NAME_BASES_CALCUL;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;
        String tableInformationComptable = _getCollection() + REInformationsComptabilite.TABLE_NAME_INFO_COMPTA;
        String tableEnTeteBlocage = _getCollection() + REEnteteBlocage.TABLE_NAME_ENTETE_BLOCAGE;
        String tableBlocage = _getCollection() + REPrestationAccordeeBloquee.TABLE_NAME_PRESTATION_ACCORDEE_BLOQUEE;

        StringBuilder sql = new StringBuilder();

        sql.append(tableRenteAccordee);

        sql.append(" INNER JOIN ").append(tablePrestationAccordee);
        sql.append(" ON ");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        sql.append("=");
        sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);

        sql.append(" INNER JOIN ").append(tableBaseCalcul);
        sql.append(" ON ");
        sql.append(tableBaseCalcul).append(".").append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);
        sql.append("=");
        sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL);

        sql.append(" INNER JOIN ").append(tableTiers);
        sql.append(" ON ");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);
        sql.append("=");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);

        sql.append(" INNER JOIN ").append(tablePersonneAvs);
        sql.append(" ON ");
        sql.append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.ID_TIERS);
        sql.append("=");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);

        sql.append(" INNER JOIN ").append(tableInformationComptable);
        sql.append(" ON ");
        sql.append(tableInformationComptable).append(".").append(REInformationsComptabilite.FIELDNAME_ID_INFO_COMPTA);
        sql.append("=");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA);

        sql.append(" LEFT OUTER JOIN ").append(tableEnTeteBlocage);
        sql.append(" ON ");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_ENTETE_BLOCAGE);
        sql.append("=");
        sql.append(tableEnTeteBlocage).append(".").append(REEnteteBlocage.FIELDNAME_ID_ENTETE_BLOCAGE);

        return sql.toString();
    }

    @Override
    protected String _getTableName() {
        return RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE;
    }

    @Override
    protected void _readProperties(final BStatement statement) throws Exception {
        codePrestation = statement.dbReadString(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
        csEtatRenteAccordee = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_CS_ETAT);
        dateDebutDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadString(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT));
        dateFinDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadString(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT));
        idBaseCalcul = statement.dbReadNumeric(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL);
        idDomaineApplicationAdressePaiement = statement
                .dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_DOMAINE_APPLICATION);
        idRenteAccordee = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        idTiersAdressePaiement = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT);
        idTiersBaseCalcul = statement.dbReadNumeric(RERenteAccordee.FIELDNAME_ID_TIERS_BASE_CALCUL);
        idTiersBeneficiaire = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        montantPrestation = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);
        nomBeneficiaire = statement.dbReadString(ITITiersDefTable.DESIGNATION_1);
        numeroAvsBeneficiaire = statement.dbReadString(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL);
        prenomBeneficiaire = statement.dbReadString(ITITiersDefTable.DESIGNATION_2);
        prestationBloquee = statement.dbReadBoolean(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE);
    }

    @Override
    protected void _validate(final BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(final BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(final BStatement statement) throws Exception {
    }

    public String getCodePrestation() {
        return codePrestation;
    }

    public String getCsEtatRenteAccordee() {
        return csEtatRenteAccordee;
    }

    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public String getIdBaseCalcul() {
        return idBaseCalcul;
    }

    public String getIdDomaineApplicationAdressePaiement() {
        return idDomaineApplicationAdressePaiement;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public String getIdTiersBaseCalcul() {
        return idTiersBaseCalcul;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public String getMontantPrestation() {
        return montantPrestation;
    }

    public String getNomBeneficiaire() {
        return nomBeneficiaire;
    }

    public String getNumeroAvsBeneficiaire() {
        return numeroAvsBeneficiaire;
    }

    public String getPrenomBeneficiaire() {
        return prenomBeneficiaire;
    }

    public boolean isPrestatationBloquee() {
        return prestationBloquee;
    }

    public void setCodePrestation(final String codePrestation) {
        this.codePrestation = codePrestation;
    }

    public void setCsEtatRenteAccordee(final String csEtatRenteAccordee) {
        this.csEtatRenteAccordee = csEtatRenteAccordee;
    }

    public void setDateDebutDroit(final String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    public void setDateFinDroit(final String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    public void setIdBaseCalcul(final String idBaseCalcul) {
        this.idBaseCalcul = idBaseCalcul;
    }

    public void setIdDomaineApplicationAdressePaiement(final String idDomaineApplicationAdressePaiement) {
        this.idDomaineApplicationAdressePaiement = idDomaineApplicationAdressePaiement;
    }

    public void setIdRenteAccordee(final String idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public void setIdTiersAdressePaiement(final String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public void setIdTiersBaseCalcul(final String idTiersBaseCalcul) {
        this.idTiersBaseCalcul = idTiersBaseCalcul;
    }

    public void setIdTiersBeneficiaire(final String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setMontantPrestation(final String montantPrestation) {
        this.montantPrestation = montantPrestation;
    }

    public void setNomBeneficiaire(final String nomBeneficiaire) {
        this.nomBeneficiaire = nomBeneficiaire;
    }

    public void setNumeroAvsBeneficiaire(final String numeroAvsBeneficiaire) {
        this.numeroAvsBeneficiaire = numeroAvsBeneficiaire;
    }

    public void setPrenomBeneficiaire(final String prenomBeneficiaire) {
        this.prenomBeneficiaire = prenomBeneficiaire;
    }

    public void setPrestationBloquee(final boolean prestationBloquee) {
        this.prestationBloquee = prestationBloquee;
    }
}
