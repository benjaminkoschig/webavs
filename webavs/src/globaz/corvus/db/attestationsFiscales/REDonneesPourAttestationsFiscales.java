package globaz.corvus.db.attestationsFiscales;

import globaz.corvus.api.retenues.IRERetenues;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.corvus.db.retenues.RERetenuesPaiement;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITIPersonneDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;

/**
 * <p>
 * Données provenant de la base de données représentant un tiers requérant et d'une rente liée à ce requérant ainsi que
 * le bénéficiaire de cette rente.
 * </p>
 * <p>
 * Le membre requérant sera utilisé pour définir l'adresse de courrier de l'attestation fiscal.
 * </p>
 * <p>
 * Les données doivent être agrégées par une instance de {@link REDonneesPourAttestationsFiscalesManager}
 * <p>
 * 
 * @author PBA
 */
public class REDonneesPourAttestationsFiscales extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ALIAS_PERSONNE_AVS_BASE_CALCUL_NO_AVS = "pers_avs_base_calcul_no_avs";
    public static final String ALIAS_PERSONNE_AVS_BENEFICIAIRE_NO_AVS = "pers_avs_beneficiaire_no_avs";
    public static final String ALIAS_PERSONNE_BASE_CALCUL_CS_SEXE = "pers_base_calcul_cs_sexe";
    public static final String ALIAS_PERSONNE_BASE_CALCUL_DATE_DECES = "pers_base_calcul_date_deces";
    public static final String ALIAS_PERSONNE_BASE_CALCUL_DATE_NAISSANCE = "pers_base_calcul_date_naiss";
    public static final String ALIAS_PERSONNE_BENEFICIAIRE_CS_SEXE = "pers_beneficiaire_cs_sexe";
    public static final String ALIAS_PERSONNE_BENEFICIAIRE_DATE_DECES = "pers_beneficiaire_date_deces";
    public static final String ALIAS_PERSONNE_BENEFICIAIRE_DATE_NAISSANCE = "pers_beneficiaire_date_naiss";
    public static final String ALIAS_TABLE_PERSONNE_AVS_BASE_CALCUL = "pers_avs_base_calcul";
    public static final String ALIAS_TABLE_PERSONNE_AVS_BENEFICIAIRE = "pers_avs_beneficiaire";
    public static final String ALIAS_TABLE_PERSONNE_BASE_CALCUL = "pers_base_calcul";
    public static final String ALIAS_TABLE_PERSONNE_BENEFICIAIRE = "pers_beneficiaire";
    public static final String ALIAS_TABLE_TIERS_BASE_CALCUL = "tiers_base_calcul";
    public static final String ALIAS_TABLE_TIERS_BENEFICIAIRE = "tiers_beneficiaire";
    public static final String ALIAS_TIERS_BASE_CALCUL_CS_LANGUE = "tiers_base_calcul_cs_langue";
    public static final String ALIAS_TIERS_BASE_CALCUL_ID_TIERS = "tiers_base_calcul_id_tiers";
    public static final String ALIAS_TIERS_BASE_CALCUL_NOM = "tiers_base_calcul_nom";
    public static final String ALIAS_TIERS_BASE_CALCUL_PRENOM = "tiers_base_calcul_prenom";
    public static final String ALIAS_TIERS_BENEFICIAIRE_CS_LANGUE = "tiers_beneficiaire_cs_langue";
    public static final String ALIAS_TIERS_BENEFICIAIRE_ID_TIERS = "tiers_beneficiaire_id_tiers";
    public static final String ALIAS_TIERS_BENEFICIAIRE_NOM = "tiers_beneficiaire_nom";
    public static final String ALIAS_TIERS_BENEFICIAIRE_PRENOM = "tiers_benenficiare_prenom";

    private String codePrestation;
    private String csLangueTiersBaseCalcul;
    private String csLangueTiersBeneficiaire;
    private String csSexeTiersBaseCalcul;
    private String csSexeTiersBeneficiaire;
    private String csTypeRetenue;
    private String dateDebutDroit;
    private String dateDebutRetenue;
    private String dateDecesTiersBaseCalcul;
    private String dateDecesTiersBeneficiaire;
    private String dateDecision;
    private String dateFinDroit;
    private String dateFinRetenue;
    private String dateNaissanceTiersBaseCalcul;
    private String dateNaissanceTiersBeneficiaire;
    private String fractionRente;
    private String idRenteAccordee;
    private String idRetenue;
    private String idTiersAdressePaiement;
    private String idTiersBaseCalcul;
    private String idTiersBeneficiaire;
    private Boolean isPrestationBloquee;
    private String montantPrestation;
    private String nomTiersBaseCalcul;
    private String nomTiersBeneficiaire;
    private String numeroAvsTiersBaseCalcul;
    private String numeroAvsTiersBeneficiaire;
    private String prenomTiersBaseCalcul;
    private String prenomTiersBeneficiaire;

    public REDonneesPourAttestationsFiscales() {
        super();

        codePrestation = "";
        csLangueTiersBaseCalcul = "";
        csLangueTiersBeneficiaire = "";
        csSexeTiersBaseCalcul = "";
        csSexeTiersBeneficiaire = "";
        csTypeRetenue = "";
        dateDebutDroit = "";
        dateDebutRetenue = "";
        dateDecesTiersBaseCalcul = "";
        dateDecesTiersBeneficiaire = "";
        dateDecision = "";
        dateFinDroit = "";
        dateFinRetenue = "";
        dateNaissanceTiersBaseCalcul = "";
        dateNaissanceTiersBeneficiaire = "";
        fractionRente = "";
        idRenteAccordee = "";
        idTiersAdressePaiement = "";
        idTiersBaseCalcul = "";
        idTiersBeneficiaire = "";
        isPrestationBloquee = Boolean.FALSE;
        montantPrestation = "";
        nomTiersBaseCalcul = "";
        numeroAvsTiersBaseCalcul = "";
        prenomTiersBaseCalcul = "";
    }

    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tableRenteAccordee = _getCollection() + RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE;
        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
        String tableDecision = _getCollection() + REDecisionEntity.TABLE_NAME_DECISIONS;
        String tableRetenue = _getCollection() + RERetenuesPaiement.TABLE_NAME_RETENUES;
        String tableInfoComptable = _getCollection() + REInformationsComptabilite.TABLE_NAME_INFO_COMPTA;

        sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE).append(",");

        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_FRACTION_RENTE)
                .append(",");

        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_DATE_DECISION).append(",");

        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_TIERS_BASE_CALCUL).append(".")
                .append(ITITiersDefTable.ID_TIERS).append(" AS ")
                .append(REDonneesPourAttestationsFiscales.ALIAS_TIERS_BASE_CALCUL_ID_TIERS).append(",");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_TIERS_BASE_CALCUL).append(".")
                .append(ITITiersDefTable.DESIGNATION_1).append(" AS ")
                .append(REDonneesPourAttestationsFiscales.ALIAS_TIERS_BASE_CALCUL_NOM).append(",");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_TIERS_BASE_CALCUL).append(".")
                .append(ITITiersDefTable.DESIGNATION_2).append(" AS ")
                .append(REDonneesPourAttestationsFiscales.ALIAS_TIERS_BASE_CALCUL_PRENOM).append(",");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_TIERS_BASE_CALCUL).append(".")
                .append(ITITiersDefTable.CS_LANGUE).append(" AS ")
                .append(REDonneesPourAttestationsFiscales.ALIAS_TIERS_BASE_CALCUL_CS_LANGUE).append(",");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_PERSONNE_BASE_CALCUL).append(".")
                .append(ITIPersonneDefTable.CS_SEXE).append(" AS ")
                .append(REDonneesPourAttestationsFiscales.ALIAS_PERSONNE_BASE_CALCUL_CS_SEXE).append(",");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_PERSONNE_BASE_CALCUL).append(".")
                .append(ITIPersonneDefTable.DATE_DECES).append(" AS ")
                .append(REDonneesPourAttestationsFiscales.ALIAS_PERSONNE_BASE_CALCUL_DATE_DECES).append(",");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_PERSONNE_BASE_CALCUL).append(".")
                .append(ITIPersonneDefTable.DATE_NAISSANCE).append(" AS ")
                .append(REDonneesPourAttestationsFiscales.ALIAS_PERSONNE_BASE_CALCUL_DATE_NAISSANCE).append(",");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_PERSONNE_AVS_BASE_CALCUL).append(".")
                .append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL).append(" AS ")
                .append(REDonneesPourAttestationsFiscales.ALIAS_PERSONNE_AVS_BASE_CALCUL_NO_AVS).append(",");

        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".")
                .append(ITITiersDefTable.ID_TIERS).append(" AS ")
                .append(REDonneesPourAttestationsFiscales.ALIAS_TIERS_BENEFICIAIRE_ID_TIERS).append(",");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".")
                .append(ITITiersDefTable.DESIGNATION_1).append(" AS ")
                .append(REDonneesPourAttestationsFiscales.ALIAS_TIERS_BENEFICIAIRE_NOM).append(",");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".")
                .append(ITITiersDefTable.DESIGNATION_2).append(" AS ")
                .append(REDonneesPourAttestationsFiscales.ALIAS_TIERS_BENEFICIAIRE_PRENOM).append(",");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".")
                .append(ITITiersDefTable.CS_LANGUE).append(" AS ")
                .append(REDonneesPourAttestationsFiscales.ALIAS_TIERS_BENEFICIAIRE_CS_LANGUE).append(",");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_PERSONNE_BENEFICIAIRE).append(".")
                .append(ITIPersonneDefTable.CS_SEXE).append(" AS ")
                .append(REDonneesPourAttestationsFiscales.ALIAS_PERSONNE_BENEFICIAIRE_CS_SEXE).append(",");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_PERSONNE_BENEFICIAIRE).append(".")
                .append(ITIPersonneDefTable.DATE_DECES).append(" AS ")
                .append(REDonneesPourAttestationsFiscales.ALIAS_PERSONNE_BENEFICIAIRE_DATE_DECES).append(",");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_PERSONNE_BENEFICIAIRE).append(".")
                .append(ITIPersonneDefTable.DATE_NAISSANCE).append(" AS ")
                .append(REDonneesPourAttestationsFiscales.ALIAS_PERSONNE_BENEFICIAIRE_DATE_NAISSANCE).append(",");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_PERSONNE_AVS_BENEFICIAIRE).append(".")
                .append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL).append(" AS ")
                .append(REDonneesPourAttestationsFiscales.ALIAS_PERSONNE_AVS_BENEFICIAIRE_NO_AVS).append(",");

        sql.append(tableRetenue).append(".").append(RERetenuesPaiement.FIELDNAME_ID_RETENUE).append(",");
        sql.append(tableRetenue).append(".").append(RERetenuesPaiement.FIELDNAME_TYPE_RETENU).append(",");
        sql.append(tableRetenue).append(".").append(RERetenuesPaiement.FIELDNAME_DATE_DEBUT_RETENUE).append(",");
        sql.append(tableRetenue).append(".").append(RERetenuesPaiement.FIELDNAME_DATE_FIN_RETENUE).append(",");

        sql.append(tableInfoComptable).append(".").append(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT);

        return sql.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tableRenteAccordee = _getCollection() + RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE;
        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
        String tableBaseCalcul = _getCollection() + REBasesCalcul.TABLE_NAME_BASES_CALCUL;
        String tableRenteCalculee = _getCollection() + RERenteCalculee.TABLE_NAME_RENTE_CALCULEE;
        String tableDemandeRente = _getCollection() + REDemandeRente.TABLE_NAME_DEMANDE_RENTE;
        String tableDecision = _getCollection() + REDecisionEntity.TABLE_NAME_DECISIONS;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonne = _getCollection() + ITIPersonneDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;
        String tableRetenue = _getCollection() + RERetenuesPaiement.TABLE_NAME_RETENUES;
        String tableInfoComptable = _getCollection() + REInformationsComptabilite.TABLE_NAME_INFO_COMPTA;

        sql.append(tableRenteAccordee);

        sql.append(" INNER JOIN ");
        sql.append(tablePrestationAccordee);
        sql.append(" ON ");
        sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);
        sql.append("=");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        sql.append(" INNER JOIN ");
        sql.append(tableBaseCalcul);
        sql.append(" ON ");
        sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL);
        sql.append("=");
        sql.append(tableBaseCalcul).append(".").append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);

        sql.append(" INNER JOIN ");
        sql.append(tableRenteCalculee);
        sql.append(" ON ");
        sql.append(tableBaseCalcul).append(".").append(REBasesCalcul.FIELDNAME_ID_RENTE_CALCULEE);
        sql.append("=");
        sql.append(tableRenteCalculee).append(".").append(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE);

        sql.append(" INNER JOIN ");
        sql.append(tableDemandeRente);
        sql.append(" ON ");
        sql.append(tableRenteCalculee).append(".").append(RERenteCalculee.FIELDNAME_ID_RENTE_CALCULEE);
        sql.append("=");
        sql.append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_ID_RENTE_CALCULEE);

        sql.append(" INNER JOIN ");
        sql.append(tableDecision);
        sql.append(" ON ");
        sql.append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);
        sql.append("=");
        sql.append(tableDecision).append(".").append(REDecisionEntity.FIELDNAME_ID_DEMANDE_RENTE);

        sql.append(" INNER JOIN ");
        sql.append(tableTiers).append(" AS ").append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_TIERS_BASE_CALCUL);
        sql.append(" ON ");
        sql.append(tableBaseCalcul).append(".").append(REBasesCalcul.FIELDNAME_ID_TIERS_BASE_CALCUL);
        sql.append("=");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_TIERS_BASE_CALCUL).append(".")
                .append(ITITiersDefTable.ID_TIERS);

        sql.append(" INNER JOIN ");
        sql.append(tablePersonne).append(" AS ")
                .append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_PERSONNE_BASE_CALCUL);
        sql.append(" ON ");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_TIERS_BASE_CALCUL).append(".")
                .append(ITITiersDefTable.ID_TIERS);
        sql.append("=");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_PERSONNE_BASE_CALCUL).append(".")
                .append(ITIPersonneDefTable.ID_TIERS);

        sql.append(" INNER JOIN ");
        sql.append(tablePersonneAvs).append(" AS ")
                .append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_PERSONNE_AVS_BASE_CALCUL);
        sql.append(" ON ");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_PERSONNE_BASE_CALCUL).append(".")
                .append(ITIPersonneDefTable.ID_TIERS);
        sql.append("=");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_PERSONNE_AVS_BASE_CALCUL).append(".")
                .append(ITIPersonneAvsDefTable.ID_TIERS);

        sql.append(" INNER JOIN ");
        sql.append(tableTiers).append(" AS ").append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_TIERS_BENEFICIAIRE);
        sql.append(" ON ");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);
        sql.append("=");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".")
                .append(ITITiersDefTable.ID_TIERS);

        sql.append(" INNER JOIN ");
        sql.append(tablePersonne).append(" AS ")
                .append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_PERSONNE_BENEFICIAIRE);
        sql.append(" ON ");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".")
                .append(ITITiersDefTable.ID_TIERS);
        sql.append("=");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_PERSONNE_BENEFICIAIRE).append(".")
                .append(ITIPersonneDefTable.ID_TIERS);

        sql.append(" INNER JOIN ");
        sql.append(tablePersonneAvs).append(" AS ")
                .append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_PERSONNE_AVS_BENEFICIAIRE);
        sql.append(" ON ");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_TIERS_BENEFICIAIRE).append(".")
                .append(ITITiersDefTable.ID_TIERS);
        sql.append("=");
        sql.append(REDonneesPourAttestationsFiscales.ALIAS_TABLE_PERSONNE_AVS_BENEFICIAIRE).append(".")
                .append(ITIPersonneAvsDefTable.ID_TIERS);

        sql.append(" LEFT OUTER JOIN ");
        sql.append(tableRetenue);
        sql.append(" ON (");
        sql.append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE).append("=")
                .append(tableRetenue).append(".").append(RERetenuesPaiement.FIELDNAME_ID_RENTE_ACCORDEE);
        sql.append(" AND ");
        sql.append(tableRetenue).append(".").append(RERetenuesPaiement.FIELDNAME_TYPE_RETENU).append("=")
                .append(IRERetenues.CS_TYPE_IMPOT_SOURCE);
        sql.append(")");

        sql.append(" INNER JOIN ");
        sql.append(tableInfoComptable);
        sql.append(" ON ");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA);
        sql.append("=");
        sql.append(tableInfoComptable).append(".").append(REInformationsComptabilite.FIELDNAME_ID_INFO_COMPTA);

        return sql.toString();
    }

    @Override
    protected String _getTableName() {
        return RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        codePrestation = statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
        csLangueTiersBaseCalcul = statement
                .dbReadString(REDonneesPourAttestationsFiscales.ALIAS_TIERS_BASE_CALCUL_CS_LANGUE);
        csLangueTiersBeneficiaire = statement
                .dbReadString(REDonneesPourAttestationsFiscales.ALIAS_TIERS_BENEFICIAIRE_CS_LANGUE);
        csSexeTiersBaseCalcul = statement
                .dbReadNumeric(REDonneesPourAttestationsFiscales.ALIAS_PERSONNE_BASE_CALCUL_CS_SEXE);
        csSexeTiersBeneficiaire = statement
                .dbReadNumeric(REDonneesPourAttestationsFiscales.ALIAS_PERSONNE_BENEFICIAIRE_CS_SEXE);
        csTypeRetenue = statement.dbReadNumeric(RERetenuesPaiement.FIELDNAME_TYPE_RETENU);
        dateDebutDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT));
        dateDebutRetenue = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(RERetenuesPaiement.FIELDNAME_DATE_DEBUT_RETENUE));
        dateDecesTiersBaseCalcul = statement
                .dbReadDateAMJ(REDonneesPourAttestationsFiscales.ALIAS_PERSONNE_BASE_CALCUL_DATE_DECES);
        dateDecesTiersBeneficiaire = statement
                .dbReadDateAMJ(REDonneesPourAttestationsFiscales.ALIAS_PERSONNE_BENEFICIAIRE_DATE_DECES);
        dateDecision = statement.dbReadDateAMJ(REDecisionEntity.FIELDNAME_DATE_DECISION);
        dateFinDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadString(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT));
        dateFinRetenue = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(RERetenuesPaiement.FIELDNAME_DATE_FIN_RETENUE));
        dateNaissanceTiersBaseCalcul = statement
                .dbReadDateAMJ(REDonneesPourAttestationsFiscales.ALIAS_PERSONNE_BASE_CALCUL_DATE_NAISSANCE);
        dateNaissanceTiersBeneficiaire = statement
                .dbReadDateAMJ(REDonneesPourAttestationsFiscales.ALIAS_PERSONNE_BENEFICIAIRE_DATE_NAISSANCE);
        fractionRente = statement.dbReadString(REPrestationsAccordees.FIELDNAME_FRACTION_RENTE);
        idRenteAccordee = statement.dbReadNumeric(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);
        idRetenue = statement.dbReadNumeric(RERetenuesPaiement.FIELDNAME_ID_RETENUE);
        idTiersAdressePaiement = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT);
        idTiersBaseCalcul = statement.dbReadNumeric(REDonneesPourAttestationsFiscales.ALIAS_TIERS_BASE_CALCUL_ID_TIERS);
        idTiersBeneficiaire = statement
                .dbReadNumeric(REDonneesPourAttestationsFiscales.ALIAS_TIERS_BENEFICIAIRE_ID_TIERS);
        isPrestationBloquee = statement.dbReadBoolean(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE);
        montantPrestation = statement.dbReadString(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION);
        nomTiersBaseCalcul = statement.dbReadString(REDonneesPourAttestationsFiscales.ALIAS_TIERS_BASE_CALCUL_NOM);
        numeroAvsTiersBaseCalcul = statement
                .dbReadString(REDonneesPourAttestationsFiscales.ALIAS_PERSONNE_AVS_BASE_CALCUL_NO_AVS);
        prenomTiersBaseCalcul = statement
                .dbReadString(REDonneesPourAttestationsFiscales.ALIAS_TIERS_BASE_CALCUL_PRENOM);
        nomTiersBeneficiaire = statement.dbReadString(REDonneesPourAttestationsFiscales.ALIAS_TIERS_BENEFICIAIRE_NOM);
        numeroAvsTiersBeneficiaire = statement
                .dbReadString(REDonneesPourAttestationsFiscales.ALIAS_PERSONNE_AVS_BENEFICIAIRE_NO_AVS);
        prenomTiersBeneficiaire = statement
                .dbReadString(REDonneesPourAttestationsFiscales.ALIAS_TIERS_BENEFICIAIRE_PRENOM);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // rien
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE,
                this._dbWriteNumeric(statement.getTransaction(), getIdRenteAccordee(), "idRenteAccordee"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        throw new UnsupportedOperationException("Can't save a composed entity");
    }

    public String getCodePrestation() {
        return codePrestation;
    }

    public String getCsLangueTiersBaseCalcul() {
        return csLangueTiersBaseCalcul;
    }

    public String getCsLangueTiersBeneficiaire() {
        return csLangueTiersBeneficiaire;
    }

    public String getCsSexeTiersBaseCalcul() {
        return csSexeTiersBaseCalcul;
    }

    public String getCsSexeTiersBeneficiaire() {
        return csSexeTiersBeneficiaire;
    }

    public String getCsTypeRetenue() {
        return csTypeRetenue;
    }

    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    public String getDateDebutRetenue() {
        return dateDebutRetenue;
    }

    public String getDateDecesTiersBaseCalcul() {
        return dateDecesTiersBaseCalcul;
    }

    public String getDateDecesTiersBeneficiaire() {
        return dateDecesTiersBeneficiaire;
    }

    public String getDateDecision() {
        return dateDecision;
    }

    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public String getDateFinRetenue() {
        return dateFinRetenue;
    }

    public String getDateNaissanceTiersBaseCalcul() {
        return dateNaissanceTiersBaseCalcul;
    }

    public String getDateNaissanceTiersBeneficiaire() {
        return dateNaissanceTiersBeneficiaire;
    }

    public String getFractionRente() {
        return fractionRente;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public String getIdRetenue() {
        return idRetenue;
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

    public Boolean getIsPrestationBloquee() {
        return isPrestationBloquee;
    }

    public String getMontantPrestation() {
        return montantPrestation;
    }

    public String getNomTiersBaseCalcul() {
        return nomTiersBaseCalcul;
    }

    public String getNomTiersBeneficiaire() {
        return nomTiersBeneficiaire;
    }

    public String getNumeroAvsTiersBaseCalcul() {
        return numeroAvsTiersBaseCalcul;
    }

    public String getNumeroAvsTiersBeneficiaire() {
        return numeroAvsTiersBeneficiaire;
    }

    public String getPrenomTiersBaseCalcul() {
        return prenomTiersBaseCalcul;
    }

    public String getPrenomTiersBeneficiaire() {
        return prenomTiersBeneficiaire;
    }

    public void setCodePrestation(String codePrestation) {
        this.codePrestation = codePrestation;
    }

    public void setCsLangueTiersBaseCalcul(String csLangueTiersBaseCalcul) {
        this.csLangueTiersBaseCalcul = csLangueTiersBaseCalcul;
    }

    public void setCsLangueTiersBeneficiaire(String csLangueTiersBeneficiaire) {
        this.csLangueTiersBeneficiaire = csLangueTiersBeneficiaire;
    }

    public void setCsSexeTiersBaseCalcul(String csSexeTiersBaseCalcul) {
        this.csSexeTiersBaseCalcul = csSexeTiersBaseCalcul;
    }

    public void setCsSexeTiersBeneficiaire(String csSexeTiersBeneficiaire) {
        this.csSexeTiersBeneficiaire = csSexeTiersBeneficiaire;
    }

    public void setCsTypeRetenue(String csTypeRetenue) {
        this.csTypeRetenue = csTypeRetenue;
    }

    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    public void setDateDebutRetenue(String dateDebutRetenue) {
        this.dateDebutRetenue = dateDebutRetenue;
    }

    public void setDateDecesTiersBaseCalcul(String dateDecesTiersBaseCalcul) {
        this.dateDecesTiersBaseCalcul = dateDecesTiersBaseCalcul;
    }

    public void setDateDecesTiersBeneficiaire(String dateDecesTiersBeneficiaire) {
        this.dateDecesTiersBeneficiaire = dateDecesTiersBeneficiaire;
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    public void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    public void setDateFinRetenue(String dateFinRetenue) {
        this.dateFinRetenue = dateFinRetenue;
    }

    public void setDateNaissanceTiersBaseCalcul(String dateNaissanceTiersBaseCalcul) {
        this.dateNaissanceTiersBaseCalcul = dateNaissanceTiersBaseCalcul;
    }

    public void setDateNaissanceTiersBeneficiaire(String dateNaissanceTiersBeneficiaire) {
        this.dateNaissanceTiersBeneficiaire = dateNaissanceTiersBeneficiaire;
    }

    public void setFractionRente(String fractionRente) {
        this.fractionRente = fractionRente;
    }

    public void setIdRenteAccordee(String idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public void setIdRetenue(String idRetenue) {
        this.idRetenue = idRetenue;
    }

    public void setIdTiersAdressePaiement(String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public void setIdTiersBaseCalcul(String idTiersBaseCalcul) {
        this.idTiersBaseCalcul = idTiersBaseCalcul;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setIsPrestationBloquee(Boolean isPrestationBloquee) {
        this.isPrestationBloquee = isPrestationBloquee;
    }

    public void setMontantPrestation(String montantPrestation) {
        this.montantPrestation = montantPrestation;
    }

    public void setNomTiersBaseCalcul(String nomTiersBaseCalcul) {
        this.nomTiersBaseCalcul = nomTiersBaseCalcul;
    }

    public void setNomTiersBeneficiaire(String nomTiersBeneficiaire) {
        this.nomTiersBeneficiaire = nomTiersBeneficiaire;
    }

    public void setNumeroAvsTiersBaseCalcul(String numeroAvsTiersBaseCalcul) {
        this.numeroAvsTiersBaseCalcul = numeroAvsTiersBaseCalcul;
    }

    public void setNumeroAvsTiersBeneficiaire(String numeroAvsTiersBeneficiaire) {
        this.numeroAvsTiersBeneficiaire = numeroAvsTiersBeneficiaire;
    }

    public void setPrenomTiersBaseCalcul(String prenomTiersBaseCalcul) {
        this.prenomTiersBaseCalcul = prenomTiersBaseCalcul;
    }

    public void setPrenomTiersBeneficiaire(String prenomTiersBeneficiaire) {
        this.prenomTiersBeneficiaire = prenomTiersBeneficiaire;
    }
}
