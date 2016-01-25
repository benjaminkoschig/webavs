package globaz.corvus.db.rentesverseesatort;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteVerseeATort;
import globaz.corvus.db.rentesverseesatort.wrapper.RECalculRentesVerseesATortConverter;
import globaz.corvus.db.rentesverseesatort.wrapper.RECalculRentesVerseesATortWrapper;
import globaz.corvus.utils.RETiersForJspUtils;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITIPersonneDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;

/**
 * <p>
 * Entité permettant de charger toutes les informations nécessaires au calcul des rentes versées à tort par l'utiliraire
 * {@link RERenteVerseeATortUtil}
 * </p>
 * <p>
 * Il est impératif d'utiliser le {@link RECalculRentesVerseesATortManager manager correspondant} afin charger cette
 * entité depuis la base de données, puis l'utiltaire {@link RECalculRentesVerseesATortConverter} afin réarranger les
 * données brutes (inutilisables en l'état) et de les convertir en un {@link RECalculRentesVerseesATortWrapper wrapper
 * de données}
 * </p>
 * 
 * @author PBA
 */
public class RECalculRentesVerseesATortEntity extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ALIAS_CHAMP_CODE_PRESTATION_PRESTATION_ACCORDEE_ANCIEN_DROIT = "cp_anc_droit";
    public static final String ALIAS_CHAMP_CODE_PRESTATION_PRESTATION_ACCORDEE_NOUVEAU_DROIT = "cp_nv_droit";
    public static final String ALIAS_CHAMP_DATE_DEBUT_PRESTATION_ACCORDEE_ANCIEN_DROIT = "dd_anc_droit";
    public static final String ALIAS_CHAMP_DATE_DEBUT_PRESTATION_ACCORDEE_NOUVEAU_DROIT = "dd_nv_droit";
    public static final String ALIAS_CHAMP_DATE_FIN_PRESTATION_ACCORDEE_ANCIEN_DROIT = "df_anc_droit";
    public static final String ALIAS_CHAMP_DATE_FIN_PRESTATION_ACCORDEE_NOUVEAU_DROIT = "df_nv_droit";
    public static final String ALIAS_CHAMP_ID_PRESTATION_ACCORDEE_ANCIEN_DROIT = "id_anc_droit";
    public static final String ALIAS_CHAMP_ID_PRESTATION_ACCORDEE_NOUVEAU_DROIT = "id_nv_droit";
    public static final String ALIAS_CHAMP_ID_TIERS_BENEFICIAIRE_ANCIEN_DROIT = "idt_anc_droit";
    public static final String ALIAS_CHAMP_ID_TIERS_BENEFICIAIRE_NOUVEAU_DROIT = "idt_nv_droit";
    public static final String ALIAS_TABLE_PRESTATION_ACCORDEE_ANCIEN_DROIT = "prest_anc_droit";
    public static final String ALIAS_TABLE_PRESTATION_ACCORDEE_NOUVEAU_DROIT = "prest_nouv_droit";

    private String codePrestationRenteAccordeeAncienDroit;
    private String codePrestationRenteAccordeeNouveauDroit;
    private String dateDebutPaiementPrestationVerseeDueAncienDroit;
    private String dateDebutRenteAccordeeAncienDroit;
    private String dateDebutRenteAccordeeNouveauDroit;
    private String dateDecesTiersBeneficiaire;
    private String dateFinPaiementPrestationVerseeDueAncienDroit;
    private String dateFinRenteAccordeeAncienDroit;
    private String dateFinRenteAccordeeNouveauDroit;
    private String dateNaissanceTiersBeneficiaire;
    private String idDemandeRenteNouveauDroit;
    private String idPrestationDueAncienDroit;
    private String idRenteAccordeeAncienDroit;
    private String idRenteAccordeeNouveauDroit;
    private String idTiersBeneficiaireRenteAccordeeAncienDroit;
    private String idTiersBeneficiaireRenteAccordeeNouveauDroit;
    private String montantPrestationVerseeDueAncienDroit;
    private String nationaliteTiersBeneficiaire;
    private String nomTiersBeneficiaire;
    private String nssTiersBeneficiaire;
    private String prenomTiersBeneficiaire;
    private String sexeTiersBeneficiaire;

    public RECalculRentesVerseesATortEntity() {
        super();

        codePrestationRenteAccordeeAncienDroit = "";
        codePrestationRenteAccordeeNouveauDroit = "";
        dateDebutPaiementPrestationVerseeDueAncienDroit = "";
        dateDebutRenteAccordeeAncienDroit = "";
        dateDebutRenteAccordeeNouveauDroit = "";
        dateDecesTiersBeneficiaire = "";
        dateFinPaiementPrestationVerseeDueAncienDroit = "";
        dateFinRenteAccordeeAncienDroit = "";
        dateFinRenteAccordeeNouveauDroit = "";
        dateNaissanceTiersBeneficiaire = "";
        idDemandeRenteNouveauDroit = "";
        idPrestationDueAncienDroit = "";
        idRenteAccordeeAncienDroit = "";
        idRenteAccordeeNouveauDroit = "";
        idTiersBeneficiaireRenteAccordeeAncienDroit = "";
        idTiersBeneficiaireRenteAccordeeNouveauDroit = "";
        montantPrestationVerseeDueAncienDroit = "";
        nationaliteTiersBeneficiaire = "";
        nomTiersBeneficiaire = "";
        nssTiersBeneficiaire = "";
        prenomTiersBeneficiaire = "";
        sexeTiersBeneficiaire = "";
    }

    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tableDemandeRente = _getCollection() + REDemandeRente.TABLE_NAME_DEMANDE_RENTE;
        String tablePrestationVerseeDue = _getCollection() + REPrestationDue.TABLE_NAME_PRESTATIONS_DUES;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonne = _getCollection() + ITIPersonneDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;
        String tableRenteVerseeATort = _getCollection() + RERenteVerseeATort.TABLE_RENTE_VERSEE_A_TORT;

        sql.append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_NOUVEAU_DROIT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append(" AS ")
                .append(RECalculRentesVerseesATortEntity.ALIAS_CHAMP_ID_PRESTATION_ACCORDEE_NOUVEAU_DROIT).append(",");
        sql.append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_NOUVEAU_DROIT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(" AS ")
                .append(RECalculRentesVerseesATortEntity.ALIAS_CHAMP_CODE_PRESTATION_PRESTATION_ACCORDEE_NOUVEAU_DROIT)
                .append(",");
        sql.append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_NOUVEAU_DROIT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(" AS ")
                .append(RECalculRentesVerseesATortEntity.ALIAS_CHAMP_DATE_DEBUT_PRESTATION_ACCORDEE_NOUVEAU_DROIT)
                .append(",");
        sql.append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_NOUVEAU_DROIT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" AS ")
                .append(RECalculRentesVerseesATortEntity.ALIAS_CHAMP_DATE_FIN_PRESTATION_ACCORDEE_NOUVEAU_DROIT)
                .append(",");
        sql.append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_NOUVEAU_DROIT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append(" AS ")
                .append(RECalculRentesVerseesATortEntity.ALIAS_CHAMP_ID_TIERS_BENEFICIAIRE_NOUVEAU_DROIT).append(",");

        sql.append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_ANCIEN_DROIT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append(" AS ")
                .append(RECalculRentesVerseesATortEntity.ALIAS_CHAMP_ID_PRESTATION_ACCORDEE_ANCIEN_DROIT).append(",");
        sql.append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_ANCIEN_DROIT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(" AS ")
                .append(RECalculRentesVerseesATortEntity.ALIAS_CHAMP_CODE_PRESTATION_PRESTATION_ACCORDEE_ANCIEN_DROIT)
                .append(",");
        sql.append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_ANCIEN_DROIT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(" AS ")
                .append(RECalculRentesVerseesATortEntity.ALIAS_CHAMP_DATE_DEBUT_PRESTATION_ACCORDEE_ANCIEN_DROIT)
                .append(",");
        sql.append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_ANCIEN_DROIT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" AS ")
                .append(RECalculRentesVerseesATortEntity.ALIAS_CHAMP_DATE_FIN_PRESTATION_ACCORDEE_ANCIEN_DROIT)
                .append(",");
        sql.append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_ANCIEN_DROIT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append(" AS ")
                .append(RECalculRentesVerseesATortEntity.ALIAS_CHAMP_ID_TIERS_BENEFICIAIRE_ANCIEN_DROIT).append(",");

        sql.append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE).append(",");

        sql.append(tablePrestationVerseeDue).append(".").append(REPrestationDue.FIELDNAME_ID_PRESTATION_DUE)
                .append(",");
        sql.append(tablePrestationVerseeDue).append(".").append(REPrestationDue.FIELDNAME_MONTANT).append(",");
        sql.append(tablePrestationVerseeDue).append(".").append(REPrestationDue.FIELDNAME_DATE_DEBUT_PAIEMENT)
                .append(",");
        sql.append(tablePrestationVerseeDue).append(".").append(REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT)
                .append(",");

        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_1).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_2).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_PAYS).append(",");

        sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.CS_SEXE).append(",");
        sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.DATE_NAISSANCE).append(",");
        sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.DATE_DECES).append(",");

        sql.append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL).append(",");

        sql.append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.ID_RENTE_VERSEE_A_TORT);

        return sql.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
        String tableRenteAccordee = _getCollection() + RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE;
        String tableBaseCalcul = _getCollection() + REBasesCalcul.TABLE_NAME_BASES_CALCUL;
        String tableDemandeRente = _getCollection() + REDemandeRente.TABLE_NAME_DEMANDE_RENTE;
        String tablePrestationVerseeDue = _getCollection() + REPrestationDue.TABLE_NAME_PRESTATIONS_DUES;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonne = _getCollection() + ITIPersonneDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;
        String tableRenteVerseeATort = _getCollection() + RERenteVerseeATort.TABLE_RENTE_VERSEE_A_TORT;

        sql.append(tablePrestationAccordee).append(" AS ")
                .append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_NOUVEAU_DROIT);

        sql.append(" INNER JOIN ").append(tableRenteAccordee);
        sql.append(" ON ").append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_NOUVEAU_DROIT)
                .append(".").append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append("=")
                .append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);

        sql.append(" INNER JOIN ").append(tableBaseCalcul);
        sql.append(" ON ").append(tableRenteAccordee).append(".").append(RERenteAccordee.FIELDNAME_ID_BASE_CALCUL)
                .append("=").append(tableBaseCalcul).append(".").append(REBasesCalcul.FIELDNAME_ID_BASES_DE_CALCUL);

        sql.append(" INNER JOIN ").append(tableDemandeRente);
        sql.append(" ON ").append(tableBaseCalcul).append(".").append(REBasesCalcul.FIELDNAME_ID_RENTE_CALCULEE)
                .append("=").append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_ID_RENTE_CALCULEE);

        sql.append(" INNER JOIN ").append(tablePrestationAccordee).append(" AS ")
                .append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_ANCIEN_DROIT);
        sql.append(" ON ").append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_NOUVEAU_DROIT)
                .append(".").append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append("=")
                .append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_ANCIEN_DROIT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE);

        sql.append(" INNER JOIN ").append(tablePrestationVerseeDue);
        sql.append(" ON ").append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_ANCIEN_DROIT)
                .append(".").append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append("=")
                .append(tablePrestationVerseeDue).append(".").append(REPrestationDue.FIELDNAME_ID_RENTE_ACCORDEE);

        sql.append(" INNER JOIN ").append(tableTiers);
        sql.append(" ON ").append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_NOUVEAU_DROIT)
                .append(".").append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE).append("=")
                .append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);

        sql.append(" INNER JOIN ").append(tablePersonne);
        sql.append(" ON ").append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS).append("=")
                .append(tablePersonne).append(".").append(ITIPersonneDefTable.ID_TIERS);

        sql.append(" INNER JOIN ").append(tablePersonneAvs);
        sql.append(" ON ").append(tablePersonne).append(".").append(ITIPersonneDefTable.ID_TIERS).append("=")
                .append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.ID_TIERS);

        sql.append(" LEFT OUTER JOIN ").append(tableRenteVerseeATort);
        sql.append(" ON (").append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_NOUVEAU_DROIT)
                .append(".").append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append("=")
                .append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.ID_RENTE_NOUVEAU_DROIT);
        sql.append(" AND ").append(RECalculRentesVerseesATortEntity.ALIAS_TABLE_PRESTATION_ACCORDEE_ANCIEN_DROIT)
                .append(".").append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append("=")
                .append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.ID_RENTE_ANCIEN_DROIT);
        sql.append(" AND ").append(tableDemandeRente).append(".").append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE)
                .append("=").append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.ID_DEMANDE_RENTE);
        sql.append(")");

        return sql.toString();
    }

    @Override
    protected String _getTableName() {
        return REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        codePrestationRenteAccordeeAncienDroit = statement
                .dbReadString(RECalculRentesVerseesATortEntity.ALIAS_CHAMP_CODE_PRESTATION_PRESTATION_ACCORDEE_ANCIEN_DROIT);
        codePrestationRenteAccordeeNouveauDroit = statement
                .dbReadString(RECalculRentesVerseesATortEntity.ALIAS_CHAMP_CODE_PRESTATION_PRESTATION_ACCORDEE_NOUVEAU_DROIT);
        dateDebutPaiementPrestationVerseeDueAncienDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationDue.FIELDNAME_DATE_DEBUT_PAIEMENT));
        dateDebutRenteAccordeeAncienDroit = PRDateFormater
                .convertDate_AAAAMM_to_MMxAAAA(statement
                        .dbReadNumeric(RECalculRentesVerseesATortEntity.ALIAS_CHAMP_DATE_DEBUT_PRESTATION_ACCORDEE_ANCIEN_DROIT));
        dateDebutRenteAccordeeNouveauDroit = PRDateFormater
                .convertDate_AAAAMM_to_MMxAAAA(statement
                        .dbReadNumeric(RECalculRentesVerseesATortEntity.ALIAS_CHAMP_DATE_DEBUT_PRESTATION_ACCORDEE_NOUVEAU_DROIT));
        dateDecesTiersBeneficiaire = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement
                .dbReadString(ITIPersonneDefTable.DATE_DECES));
        dateFinPaiementPrestationVerseeDueAncienDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT));
        dateFinRenteAccordeeAncienDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(RECalculRentesVerseesATortEntity.ALIAS_CHAMP_DATE_FIN_PRESTATION_ACCORDEE_ANCIEN_DROIT));
        dateFinRenteAccordeeNouveauDroit = PRDateFormater
                .convertDate_AAAAMM_to_MMxAAAA(statement
                        .dbReadNumeric(RECalculRentesVerseesATortEntity.ALIAS_CHAMP_DATE_FIN_PRESTATION_ACCORDEE_NOUVEAU_DROIT));
        dateNaissanceTiersBeneficiaire = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement
                .dbReadString(ITIPersonneDefTable.DATE_NAISSANCE));
        idDemandeRenteNouveauDroit = statement.dbReadNumeric(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);
        idPrestationDueAncienDroit = statement.dbReadNumeric(REPrestationDue.FIELDNAME_ID_PRESTATION_DUE);
        idRenteAccordeeAncienDroit = statement
                .dbReadNumeric(RECalculRentesVerseesATortEntity.ALIAS_CHAMP_ID_PRESTATION_ACCORDEE_ANCIEN_DROIT);
        idRenteAccordeeNouveauDroit = statement
                .dbReadNumeric(RECalculRentesVerseesATortEntity.ALIAS_CHAMP_ID_PRESTATION_ACCORDEE_NOUVEAU_DROIT);
        idTiersBeneficiaireRenteAccordeeAncienDroit = statement
                .dbReadNumeric(RECalculRentesVerseesATortEntity.ALIAS_CHAMP_ID_TIERS_BENEFICIAIRE_ANCIEN_DROIT);
        idTiersBeneficiaireRenteAccordeeNouveauDroit = statement
                .dbReadNumeric(RECalculRentesVerseesATortEntity.ALIAS_CHAMP_ID_TIERS_BENEFICIAIRE_NOUVEAU_DROIT);
        montantPrestationVerseeDueAncienDroit = statement.dbReadNumeric(REPrestationDue.FIELDNAME_MONTANT, 2);
        nationaliteTiersBeneficiaire = RETiersForJspUtils.getInstance(getSession()).getLibellePays(
                statement.dbReadNumeric(ITITiersDefTable.ID_PAYS));
        nomTiersBeneficiaire = statement.dbReadString(ITITiersDefTable.DESIGNATION_1);
        nssTiersBeneficiaire = statement.dbReadString(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL);
        prenomTiersBeneficiaire = statement.dbReadString(ITITiersDefTable.DESIGNATION_2);
        sexeTiersBeneficiaire = RETiersForJspUtils.getInstance(getSession()).getLibelleCourtSexe(
                statement.dbReadNumeric(ITIPersonneDefTable.CS_SEXE));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // rien, entité de lecture uniquement
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // rien
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // rien, entité de lecture uniquement
    }

    public String getCodePrestationRenteAccordeeAncienDroit() {
        return codePrestationRenteAccordeeAncienDroit;
    }

    public String getCodePrestationRenteAccordeeNouveauDroit() {
        return codePrestationRenteAccordeeNouveauDroit;
    }

    public String getDateDebutPaiementPrestationVerseeDueAncienDroit() {
        return dateDebutPaiementPrestationVerseeDueAncienDroit;
    }

    public String getDateDebutRenteAccordeeAncienDroit() {
        return dateDebutRenteAccordeeAncienDroit;
    }

    public String getDateDebutRenteAccordeeNouveauDroit() {
        return dateDebutRenteAccordeeNouveauDroit;
    }

    public String getDateDecesTiersBeneficiaire() {
        return dateDecesTiersBeneficiaire;
    }

    public String getDateFinPaiementPrestationVerseeDueAncienDroit() {
        return dateFinPaiementPrestationVerseeDueAncienDroit;
    }

    public String getDateFinRenteAccordeeAncienDroit() {
        return dateFinRenteAccordeeAncienDroit;
    }

    public String getDateFinRenteAccordeeNouveauDroit() {
        return dateFinRenteAccordeeNouveauDroit;
    }

    public String getDateNaissanceTiersBeneficiaire() {
        return dateNaissanceTiersBeneficiaire;
    }

    public String getIdDemandeRenteNouveauDroit() {
        return idDemandeRenteNouveauDroit;
    }

    public String getIdPrestationDueAncienDroit() {
        return idPrestationDueAncienDroit;
    }

    public String getIdRenteAccordeeAncienDroit() {
        return idRenteAccordeeAncienDroit;
    }

    public String getIdRenteAccordeeNouveauDroit() {
        return idRenteAccordeeNouveauDroit;
    }

    public String getIdTiersBeneficiaireRenteAccordeeAncienDroit() {
        return idTiersBeneficiaireRenteAccordeeAncienDroit;
    }

    public String getIdTiersBeneficiaireRenteAccordeeNouveauDroit() {
        return idTiersBeneficiaireRenteAccordeeNouveauDroit;
    }

    public String getMontantPrestationVerseeDueAncienDroit() {
        return montantPrestationVerseeDueAncienDroit;
    }

    public String getNationaliteTiersBeneficiaire() {
        return nationaliteTiersBeneficiaire;
    }

    public String getNomTiersBeneficiaire() {
        return nomTiersBeneficiaire;
    }

    public String getNssTiersBeneficiaire() {
        return nssTiersBeneficiaire;
    }

    public String getPrenomTiersBeneficiaire() {
        return prenomTiersBeneficiaire;
    }

    public String getSexeTiersBeneficiaire() {
        return sexeTiersBeneficiaire;
    }

    public void setCodePrestationRenteAccordeeAncienDroit(String codePrestationRenteAccordeeAncienDroit) {
        this.codePrestationRenteAccordeeAncienDroit = codePrestationRenteAccordeeAncienDroit;
    }

    public void setCodePrestationRenteAccordeeNouveauDroit(String codePrestationRenteAccordeeNouveauDroit) {
        this.codePrestationRenteAccordeeNouveauDroit = codePrestationRenteAccordeeNouveauDroit;
    }

    public void setDateDebutPaiementPrestationVerseeDueAncienDroit(
            String dateDebutPaiementPrestationVerseeDueAncienDroit) {
        this.dateDebutPaiementPrestationVerseeDueAncienDroit = dateDebutPaiementPrestationVerseeDueAncienDroit;
    }

    public void setDateDebutRenteAccordeeAncienDroit(String dateDebutRenteAccordeeAncienDroit) {
        this.dateDebutRenteAccordeeAncienDroit = dateDebutRenteAccordeeAncienDroit;
    }

    public void setDateDebutRenteAccordeeNouveauDroit(String dateDebutRenteAccordeeNouveauDroit) {
        this.dateDebutRenteAccordeeNouveauDroit = dateDebutRenteAccordeeNouveauDroit;
    }

    public void setDateDecesTiersBeneficiaire(String dateDecesTiersBeneficiaire) {
        this.dateDecesTiersBeneficiaire = dateDecesTiersBeneficiaire;
    }

    public void setDateFinPaiementPrestationVerseeDueAncienDroit(String dateFinPaiementPrestationVerseeDueAncienDroit) {
        this.dateFinPaiementPrestationVerseeDueAncienDroit = dateFinPaiementPrestationVerseeDueAncienDroit;
    }

    public void setDateFinRenteAccordeeAncienDroit(String dateFinRenteAccordeeAncienDroit) {
        this.dateFinRenteAccordeeAncienDroit = dateFinRenteAccordeeAncienDroit;
    }

    public void setDateFinRenteAccordeeNouveauDroit(String dateFinRenteAccordeeNouveauDroit) {
        this.dateFinRenteAccordeeNouveauDroit = dateFinRenteAccordeeNouveauDroit;
    }

    public void setDateNaissanceTiersBeneficiaire(String dateNaissanceTiersBeneficiaire) {
        this.dateNaissanceTiersBeneficiaire = dateNaissanceTiersBeneficiaire;
    }

    public void setIdDemandeRenteNouveauDroit(String idDemandeRenteNouveauDroit) {
        this.idDemandeRenteNouveauDroit = idDemandeRenteNouveauDroit;
    }

    public void setIdPrestationDueAncienDroit(String idPrestationDueAncienDroit) {
        this.idPrestationDueAncienDroit = idPrestationDueAncienDroit;
    }

    public void setIdRenteAccordeeAncienDroit(String idRenteAccordeeAncienDroit) {
        this.idRenteAccordeeAncienDroit = idRenteAccordeeAncienDroit;
    }

    public void setIdRenteAccordeeNouveauDroit(String idRenteAccordeeNouveauDroit) {
        this.idRenteAccordeeNouveauDroit = idRenteAccordeeNouveauDroit;
    }

    public void setIdTiersBeneficiaireRenteAccordeeAncienDroit(String idTiersBeneficiaireRenteAccordeeAncienDroit) {
        this.idTiersBeneficiaireRenteAccordeeAncienDroit = idTiersBeneficiaireRenteAccordeeAncienDroit;
    }

    public void setIdTiersBeneficiaireRenteAccordeeNouveauDroit(String idTiersBeneficiaireRenteAccordeeNouveauDroit) {
        this.idTiersBeneficiaireRenteAccordeeNouveauDroit = idTiersBeneficiaireRenteAccordeeNouveauDroit;
    }

    public void setMontantPrestationVerseeDueAncienDroit(String montantPrestationVerseeDueAncienDroit) {
        this.montantPrestationVerseeDueAncienDroit = montantPrestationVerseeDueAncienDroit;
    }

    public void setNationaliteTiersBeneficiaire(String nationaliteTiersBeneficiaire) {
        this.nationaliteTiersBeneficiaire = nationaliteTiersBeneficiaire;
    }

    public void setNomTiersBeneficiaire(String nomTiersBeneficiaire) {
        this.nomTiersBeneficiaire = nomTiersBeneficiaire;
    }

    public void setNssTiersBeneficiaire(String nssTiersBeneficiaire) {
        this.nssTiersBeneficiaire = nssTiersBeneficiaire;
    }

    public void setPrenomTiersBeneficiaire(String prenomTiersBeneficiaire) {
        this.prenomTiersBeneficiaire = prenomTiersBeneficiaire;
    }

    public void setSexeTiersBeneficiaire(String sexeTiersBeneficiaire) {
        this.sexeTiersBeneficiaire = sexeTiersBeneficiaire;
    }
}
