package globaz.corvus.db.rentesverseesatort;

import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteVerseeATort;
import globaz.corvus.utils.RETiersForJspUtils;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITIPersonneDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;
import java.math.BigDecimal;
import ch.globaz.corvus.domaine.constantes.TypeRenteVerseeATort;
import ch.globaz.pyxis.domaine.NumeroSecuriteSociale;

public final class RERenteVerseeATortJointRenteAccordee extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ALIAS_CODE_PRESTATION_ANCIEN_DROIT = "code_prest_anc_droit";
    public static final String ALIAS_CODE_PRESTATION_NOUVEAU_DROIT = "code_prest_nv_droit";
    public static final String ALIAS_CS_ETAT_ANCIEN_DROIT = "cs_etat_anc_droit";
    public static final String ALIAS_CS_ETAT_NOUVEAU_DROIT = "cs_etat_nv_droit";
    public static final String ALIAS_DATE_DEBUT_ANCIEN_DROIT = "date_debut_anc_droit";
    public static final String ALIAS_DATE_DEBUT_NOUVEAU_DROIT = "date_debut_nv_droit";
    public static final String ALIAS_DATE_FIN_ANCIEN_DROIT = "date_fin_anc_droit";
    public static final String ALIAS_DATE_FIN_NOUVEAU_DROIT = "date_fin_nv_droit";

    public static final String ALIAS_TABLE_PRESTATION_ACCORDEE_ANCIEN_DROIT = "prest_acc_anc_droit";
    public static final String ALIAS_TABLE_PRESTATION_ACCORDEE_NOUVEAU_DROIT = "prest_acc_nv_droit";

    private Integer codePrestationAncienDroit;
    private Integer codePrestationNouveauDroit;
    private Integer csEtatAncienDroit;
    private Integer csEtatNouveauDroit;
    private String dateDebutAncienDroit;
    private String dateDebutNouveauDroit;
    private String dateDebutPaiementPrestationDue;
    private String dateDebutRenteVerseeATort;
    private String dateDeces;
    private String dateFinAncienDroit;
    private String dateFinNouveauDroit;
    private String dateFinPaiementPrestationDue;
    private String dateFinRenteVerseeATort;
    private String dateNaissance;
    private Long idDemandeRente;
    private Long idPrestationDue;
    private Long idRenteAncienDroit;
    private Long idRenteNouveauDroit;
    private Long idRenteVerseeATort;
    private Long idTiersBeneficiaire;
    private BigDecimal montant;
    private BigDecimal montantPrestationDue;
    private String nationalite;
    private String nom;
    private NumeroSecuriteSociale nss;
    private String prenom;
    private boolean saisieManuelle;
    private String sexe;
    private TypeRenteVerseeATort typeRenteVerseeATort;

    public RERenteVerseeATortJointRenteAccordee() {
        super();

        codePrestationAncienDroit = null;
        codePrestationNouveauDroit = null;
        csEtatAncienDroit = null;
        csEtatNouveauDroit = null;
        dateDebutAncienDroit = null;
        dateDebutNouveauDroit = null;
        dateDebutPaiementPrestationDue = null;
        dateDebutRenteVerseeATort = null;
        dateFinAncienDroit = null;
        dateFinNouveauDroit = null;
        dateFinPaiementPrestationDue = null;
        dateFinRenteVerseeATort = null;
        dateNaissance = null;
        idDemandeRente = null;
        idPrestationDue = null;
        idRenteAncienDroit = null;
        idRenteNouveauDroit = null;
        idRenteVerseeATort = null;
        idTiersBeneficiaire = null;
        montant = null;
        montantPrestationDue = null;
        nationalite = null;
        nom = null;
        nss = null;
        prenom = null;
        typeRenteVerseeATort = null;
    }

    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tableRenteVerseeATort = _getCollection() + RERenteVerseeATort.TABLE_RENTE_VERSEE_A_TORT;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonne = _getCollection() + ITIPersonneDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;
        String tablePrestationDue = _getCollection() + REPrestationDue.TABLE_NAME_PRESTATIONS_DUES;

        sql.append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.ID_RENTE_VERSEE_A_TORT).append(",");
        sql.append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.ID_DEMANDE_RENTE).append(",");
        sql.append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.ID_RENTE_ANCIEN_DROIT).append(",");
        sql.append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.ID_RENTE_NOUVEAU_DROIT).append(",");
        sql.append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.MONTANT).append(",");
        sql.append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.DATE_DEBUT).append(",");
        sql.append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.DATE_FIN).append(",");
        sql.append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.TYPE_RENTE_VERSEE_A_TORT).append(",");
        sql.append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.IS_SAISIE_MANUELLE).append(",");

        sql.append(RERenteVerseeATortJointRenteAccordee.ALIAS_TABLE_PRESTATION_ACCORDEE_ANCIEN_DROIT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(" AS ")
                .append(RERenteVerseeATortJointRenteAccordee.ALIAS_CODE_PRESTATION_ANCIEN_DROIT).append(",");
        sql.append(RERenteVerseeATortJointRenteAccordee.ALIAS_TABLE_PRESTATION_ACCORDEE_ANCIEN_DROIT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append(" AS ")
                .append(RERenteVerseeATortJointRenteAccordee.ALIAS_CS_ETAT_ANCIEN_DROIT).append(",");
        sql.append(RERenteVerseeATortJointRenteAccordee.ALIAS_TABLE_PRESTATION_ACCORDEE_ANCIEN_DROIT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(" AS ")
                .append(RERenteVerseeATortJointRenteAccordee.ALIAS_DATE_DEBUT_ANCIEN_DROIT).append(",");
        sql.append(RERenteVerseeATortJointRenteAccordee.ALIAS_TABLE_PRESTATION_ACCORDEE_ANCIEN_DROIT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" AS ")
                .append(RERenteVerseeATortJointRenteAccordee.ALIAS_DATE_FIN_ANCIEN_DROIT).append(",");

        sql.append(RERenteVerseeATortJointRenteAccordee.ALIAS_TABLE_PRESTATION_ACCORDEE_NOUVEAU_DROIT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION).append(" AS ")
                .append(RERenteVerseeATortJointRenteAccordee.ALIAS_CODE_PRESTATION_NOUVEAU_DROIT).append(",");
        sql.append(RERenteVerseeATortJointRenteAccordee.ALIAS_TABLE_PRESTATION_ACCORDEE_NOUVEAU_DROIT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append(" AS ")
                .append(RERenteVerseeATortJointRenteAccordee.ALIAS_CS_ETAT_NOUVEAU_DROIT).append(",");
        sql.append(RERenteVerseeATortJointRenteAccordee.ALIAS_TABLE_PRESTATION_ACCORDEE_NOUVEAU_DROIT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(" AS ")
                .append(RERenteVerseeATortJointRenteAccordee.ALIAS_DATE_DEBUT_NOUVEAU_DROIT).append(",");
        sql.append(RERenteVerseeATortJointRenteAccordee.ALIAS_TABLE_PRESTATION_ACCORDEE_NOUVEAU_DROIT).append(".")
                .append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT).append(" AS ")
                .append(RERenteVerseeATortJointRenteAccordee.ALIAS_DATE_FIN_NOUVEAU_DROIT).append(",");

        sql.append(tablePrestationDue).append(".").append(REPrestationDue.FIELDNAME_ID_PRESTATION_DUE).append(",");
        sql.append(tablePrestationDue).append(".").append(REPrestationDue.FIELDNAME_DATE_DEBUT_PAIEMENT).append(",");
        sql.append(tablePrestationDue).append(".").append(REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT).append(",");
        sql.append(tablePrestationDue).append(".").append(REPrestationDue.FIELDNAME_MONTANT).append(",");

        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_1).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_2).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_PAYS).append(",");

        sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.DATE_NAISSANCE).append(",");
        sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.DATE_DECES).append(",");
        sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.CS_SEXE).append(",");

        sql.append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL);

        return sql.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tableRenteVerseeATort = _getCollection() + RERenteVerseeATort.TABLE_RENTE_VERSEE_A_TORT;
        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tablePersonne = _getCollection() + ITIPersonneDefTable.TABLE_NAME;
        String tablePersonneAvs = _getCollection() + ITIPersonneAvsDefTable.TABLE_NAME;
        String tablePrestationDue = _getCollection() + REPrestationDue.TABLE_NAME_PRESTATIONS_DUES;

        sql.append(tableRenteVerseeATort);

        // il se peut qu'il n'y ait pas de rente pour le nouveau droit dans le cas où
        sql.append(" LEFT OUTER JOIN ").append(tablePrestationAccordee).append(" AS ")
                .append(RERenteVerseeATortJointRenteAccordee.ALIAS_TABLE_PRESTATION_ACCORDEE_NOUVEAU_DROIT);
        sql.append(" ON ").append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.ID_RENTE_NOUVEAU_DROIT)
                .append("=").append(RERenteVerseeATortJointRenteAccordee.ALIAS_TABLE_PRESTATION_ACCORDEE_NOUVEAU_DROIT)
                .append(".").append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        sql.append(" LEFT OUTER JOIN ").append(tablePrestationAccordee).append(" AS ")
                .append(RERenteVerseeATortJointRenteAccordee.ALIAS_TABLE_PRESTATION_ACCORDEE_ANCIEN_DROIT);
        sql.append(" ON ").append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.ID_RENTE_ANCIEN_DROIT)
                .append("=").append(RERenteVerseeATortJointRenteAccordee.ALIAS_TABLE_PRESTATION_ACCORDEE_ANCIEN_DROIT)
                .append(".").append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        sql.append(" LEFT OUTER JOIN ").append(tablePrestationDue);
        sql.append(" ON ").append(RERenteVerseeATortJointRenteAccordee.ALIAS_TABLE_PRESTATION_ACCORDEE_ANCIEN_DROIT)
                .append(".").append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE).append("=")
                .append(tablePrestationDue).append(".").append(REPrestationDue.FIELDNAME_ID_RENTE_ACCORDEE);

        sql.append(" INNER JOIN ").append(tableTiers);
        sql.append(" ON ").append(tableRenteVerseeATort).append(".").append(RERenteVerseeATort.ID_TIERS).append("=")
                .append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);

        sql.append(" INNER JOIN ").append(tablePersonne);
        sql.append(" ON ").append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS).append("=")
                .append(tablePersonne).append(".").append(ITIPersonneDefTable.ID_TIERS);

        sql.append(" INNER JOIN ").append(tablePersonneAvs);
        sql.append(" ON ").append(tablePersonne).append(".").append(ITIPersonneDefTable.ID_TIERS).append("=")
                .append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.ID_TIERS);

        return sql.toString();
    }

    @Override
    protected String _getTableName() {
        return RERenteVerseeATort.TABLE_RENTE_VERSEE_A_TORT;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idDemandeRente = Long.parseLong(statement.dbReadNumeric(RERenteVerseeATort.ID_DEMANDE_RENTE));

        idRenteVerseeATort = Long.parseLong(statement.dbReadNumeric(RERenteVerseeATort.ID_RENTE_VERSEE_A_TORT));
        dateDebutRenteVerseeATort = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadString(RERenteVerseeATort.DATE_DEBUT));
        dateFinRenteVerseeATort = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadString(RERenteVerseeATort.DATE_FIN));
        montant = new BigDecimal(statement.dbReadString(RERenteVerseeATort.MONTANT));
        typeRenteVerseeATort = TypeRenteVerseeATort.parse(statement
                .dbReadNumeric(RERenteVerseeATort.TYPE_RENTE_VERSEE_A_TORT));
        saisieManuelle = statement.dbReadBoolean(RERenteVerseeATort.IS_SAISIE_MANUELLE);

        idTiersBeneficiaire = Long.parseLong(statement.dbReadNumeric(ITITiersDefTable.ID_TIERS));
        nom = statement.dbReadString(ITITiersDefTable.DESIGNATION_1);
        prenom = statement.dbReadString(ITITiersDefTable.DESIGNATION_2);
        nss = new NumeroSecuriteSociale(statement.dbReadString(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL));
        sexe = RETiersForJspUtils.getInstance(getSession()).getLibelleCourtSexe(
                statement.dbReadString(ITIPersonneDefTable.CS_SEXE));
        dateNaissance = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement
                .dbReadString(ITIPersonneDefTable.DATE_NAISSANCE));
        dateDeces = PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(statement
                .dbReadString(ITIPersonneDefTable.DATE_DECES));
        nationalite = RETiersForJspUtils.getInstance(getSession()).getLibellePays(
                statement.dbReadString(ITITiersDefTable.ID_PAYS));

        String idRenteAncienDroit = statement.dbReadNumeric(RERenteVerseeATort.ID_RENTE_ANCIEN_DROIT);
        if (!JadeStringUtil.isBlank(idRenteAncienDroit)) {
            this.idRenteAncienDroit = Long.parseLong(idRenteAncienDroit);
            codePrestationAncienDroit = Integer.parseInt(statement
                    .dbReadString(RERenteVerseeATortJointRenteAccordee.ALIAS_CODE_PRESTATION_ANCIEN_DROIT));
            csEtatAncienDroit = Integer.parseInt(statement
                    .dbReadNumeric(RERenteVerseeATortJointRenteAccordee.ALIAS_CS_ETAT_ANCIEN_DROIT));
            dateDebutAncienDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                    .dbReadString(RERenteVerseeATortJointRenteAccordee.ALIAS_DATE_DEBUT_ANCIEN_DROIT));
            dateFinAncienDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                    .dbReadString(RERenteVerseeATortJointRenteAccordee.ALIAS_DATE_FIN_ANCIEN_DROIT));

            idPrestationDue = Long.parseLong(statement.dbReadNumeric(REPrestationDue.FIELDNAME_ID_PRESTATION_DUE));
            dateDebutPaiementPrestationDue = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                    .dbReadString(REPrestationDue.FIELDNAME_DATE_DEBUT_PAIEMENT));
            dateFinPaiementPrestationDue = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                    .dbReadString(REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT));
            montantPrestationDue = new BigDecimal(statement.dbReadNumeric(REPrestationDue.FIELDNAME_MONTANT));
        }

        String idRenteNouveauDroit = statement.dbReadNumeric(RERenteVerseeATort.ID_RENTE_NOUVEAU_DROIT);
        if (!JadeStringUtil.isBlank(idRenteNouveauDroit)) {
            this.idRenteNouveauDroit = Long.parseLong(idRenteNouveauDroit);
            codePrestationNouveauDroit = Integer.parseInt(statement
                    .dbReadString(RERenteVerseeATortJointRenteAccordee.ALIAS_CODE_PRESTATION_NOUVEAU_DROIT));
            csEtatNouveauDroit = Integer.parseInt(statement
                    .dbReadNumeric(RERenteVerseeATortJointRenteAccordee.ALIAS_CS_ETAT_NOUVEAU_DROIT));
            dateDebutNouveauDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                    .dbReadString(RERenteVerseeATortJointRenteAccordee.ALIAS_DATE_DEBUT_NOUVEAU_DROIT));
            dateFinNouveauDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                    .dbReadString(RERenteVerseeATortJointRenteAccordee.ALIAS_DATE_FIN_NOUVEAU_DROIT));
        }
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public final Integer getCodePrestationAncienDroit() {
        return codePrestationAncienDroit;
    }

    public final Integer getCodePrestationNouveauDroit() {
        return codePrestationNouveauDroit;
    }

    public final Integer getCsEtatAncienDroit() {
        return csEtatAncienDroit;
    }

    public final Integer getCsEtatNouveauDroit() {
        return csEtatNouveauDroit;
    }

    public final String getDateDebutAncienDroit() {
        return dateDebutAncienDroit;
    }

    public final String getDateDebutNouveauDroit() {
        return dateDebutNouveauDroit;
    }

    public final String getDateDebutPaiementPrestationDue() {
        return dateDebutPaiementPrestationDue;
    }

    public final String getDateDebutRenteVerseeATort() {
        return dateDebutRenteVerseeATort;
    }

    public final String getDateDeces() {
        return dateDeces;
    }

    public final String getDateFinAncienDroit() {
        return dateFinAncienDroit;
    }

    public final String getDateFinNouveauDroit() {
        return dateFinNouveauDroit;
    }

    public final String getDateFinPaiementPrestationDue() {
        return dateFinPaiementPrestationDue;
    }

    public final String getDateFinRenteVerseeATort() {
        return dateFinRenteVerseeATort;
    }

    public final String getDateNaissance() {
        return dateNaissance;
    }

    public final Long getIdDemandeRente() {
        return idDemandeRente;
    }

    public final Long getIdPrestationDue() {
        return idPrestationDue;
    }

    public final Long getIdRenteAncienDroit() {
        return idRenteAncienDroit;
    }

    public final Long getIdRenteNouveauDroit() {
        return idRenteNouveauDroit;
    }

    public final Long getIdRenteVerseeATort() {
        return idRenteVerseeATort;
    }

    public final Long getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public final BigDecimal getMontant() {
        return montant;
    }

    public final BigDecimal getMontantPrestationDue() {
        return montantPrestationDue;
    }

    public final String getNationalite() {
        return nationalite;
    }

    public final String getNom() {
        return nom;
    }

    public final NumeroSecuriteSociale getNss() {
        return nss;
    }

    public final String getPrenom() {
        return prenom;
    }

    public final String getSexe() {
        return sexe;
    }

    public final TypeRenteVerseeATort getTypeRenteVerseeATort() {
        return typeRenteVerseeATort;
    }

    public final boolean isSaisieManuelle() {
        return saisieManuelle;
    }

    public final void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public final void setNom(String nom) {
        this.nom = nom;
    }

    public final void setNss(NumeroSecuriteSociale nss) {
        this.nss = nss;
    }

    public final void setSaisieManuelle(boolean saisieManuelle) {
        this.saisieManuelle = saisieManuelle;
    }
}
