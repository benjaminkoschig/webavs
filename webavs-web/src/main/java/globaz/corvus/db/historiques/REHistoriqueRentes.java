package globaz.corvus.db.historiques;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.prestation.tools.PRDateFormater;

/**
 * Historique des rentes accordées
 * 
 * @author SCR
 */
public class REHistoriqueRentes extends REHistoriqueHeader {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String CONSTANTE_ORDER_BY_DATE = "date_fin_order_by";

    public static final String FIELDNAME_ANNEE_MONTANT_RAM = "WIDAMR";
    public static final String FIELDNAME_ANNEE_NIVEAU = "WILANN";
    public static final String FIELDNAME_CLE_INF_ATTEINTE_FCT = "WILCIA";
    public static final String FIELDNAME_CODE_PRESTATION = "WILCPR";
    public static final String FIELDNAME_CODE_REVENU = "WILCRE";
    public static final String FIELDNAME_CS1 = "WILCS1";
    public static final String FIELDNAME_CS2 = "WILCS2";
    public static final String FIELDNAME_CS3 = "WILCS3";
    public static final String FIELDNAME_CS4 = "WILCS4";
    public static final String FIELDNAME_CS5 = "WILCS5";
    public static final String FIELDNAME_DATE_DEB_ANTICIP = "WIDANT";
    public static final String FIELDNAME_DATE_DEB_DROIT = "WIDDDR";
    public static final String FIELDNAME_DATE_FIN_DROIT = "WIDDFD";
    public static final String FIELDNAME_DATE_REVOC_AJOURN = "WIDRAJ";
    public static final String FIELDNAME_DEGR_INV = "WINDIN";
    public static final String FIELDNAME_DROIT_APPLIQUE = "WILDAP";
    public static final String FIELDNAME_DUREE_AJOURN = "WILDAJ";
    public static final String FIELDNAME_DUREE_COT_AP_73 = "WILDCP";
    public static final String FIELDNAME_DUREE_COT_AV_73 = "WILDCA";
    public static final String FIELDNAME_DUREE_COT_CLS_AGE = "WILDCC";
    public static final String FIELDNAME_DUREE_COT_RAM = "WILDCR";
    public static final String FIELDNAME_DUREE_COTI_ETR_AP_73 = "WIDCEP";
    public static final String FIELDNAME_DUREE_COTI_ETR_AV_73 = "WIDCEA";
    public static final String FIELDNAME_ECHELLE = "WILECH";
    public static final String FIELDNAME_FRACTION_RENTE = "WILFRR";
    public static final String FIELDNAME_QUOTITE_RENTE = "QUOTITE_RENTE";
    public static final String FIELDNAME_ID_HISTORIQUE_RENTES = "WIIHIS";
    public static final String FIELDNAME_ID_RENTE_ACCORDEE = "WIIRA";
    public static final String FIELDNAME_ID_TIERS = "WIITIE";
    public static final String FIELDNAME_IS_INVAL_PRECOCE = "WIBIPR";
    public static final String FIELDNAME_IS_MODIFIE = "WIBMOD";
    public static final String FIELDNAME_IS_PRENDRE_CALCUL_ACOR = "WIBPEC";
    public static final String FIELDNAME_IS_RENTE_AJOURNEE = "WIBAJO";
    public static final String FIELDNAME_IS_REVENU_SPLITTE = "WIBRSP";
    public static final String FIELDNAME_IS_SURV_INVALID = "WIBSIN";
    public static final String FIELDNAME_IS_TRANSFERE = "WIBTRA";
    public static final String FIELDNAME_MOIS_APP_AP_73 = "WINMAP";
    public static final String FIELDNAME_MOIS_APP_AV_73 = "WINMAA";
    public static final String FIELDNAME_MONTANT_BONUS_EDUC = "WIMBTE";
    public static final String FIELDNAME_MONTANT_PREST = "WIMPRE";
    public static final String FIELDNAME_MONTANT_REDUC_ANTICIP = "WIMRAN";
    public static final String FIELDNAME_NBR_ANNEE_ANTICIP = "WINANT";
    public static final String FIELDNAME_NBR_ANNEE_BTA = "WINBTA";
    public static final String FIELDNAME_NBR_ANNEE_BTE = "WINBTE";
    public static final String FIELDNAME_NBR_ANNEE_TRANSIT = "WINBTR";
    public static final String FIELDNAME_NSS_BENEFICIAIRE = "WILNSS";
    public static final String FIELDNAME_OAI = "WILOAI";
    public static final String FIELDNAME_PSPY = "PSPY";
    public static final String FIELDNAME_RAM = "WIMRAM";
    public static final String FIELDNAME_SUPP_CARR = "WIMSUC";
    public static final String FIELDNAME_SUPPL_AJOURN = "WIMSAJ";
    public static final String FIELDNAME_SURV_EV_ASSURE = "WIDSEA";
    public static final String FIELDNAME_CODE_MUTATION = "WILCMU";

    public static final String TABLE_NAME_HISTORIQUE_RENTES = "REHISTR";

    public static final String createFromClause(String schema) {
        StringBuffer fromClause = new StringBuffer();

        fromClause.append(schema);
        fromClause.append(REHistoriqueRentes.TABLE_NAME_HISTORIQUE_RENTES);

        // jointure avec la table
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(REHistoriqueHeader.TABLE_NAME_HISTORIQUE_HEADER);
        fromClause.append(" ON ");
        fromClause.append(REHistoriqueHeader.FIELDNAME_ID_HISTORIQUE);
        fromClause.append("=");
        fromClause.append(REHistoriqueRentes.FIELDNAME_ID_HISTORIQUE_RENTES);

        return fromClause.toString();
    }

    private String anneeMontantRAM = "";
    private String anneeNiveau = "";
    private String cleInfirmiteAtteinteFct = "";
    private String codePrestation = "";
    private String codeRevenu = "";
    private String cs1;
    private String cs2;
    private String cs3;
    private String cs4;
    private String cs5;
    private String dateDebutAnticipation = "";
    private String dateDebutDroit = "";
    private String dateFinDroit = "";
    private String dateRevocationAjournement = "";
    private String degreInvalidite = "";
    private String droitApplique = "";
    private String dureeAjournement = "";
    private String dureeCotAp73;
    private String dureeCotAv73;
    private String dureeCotiClasseAge = "";
    private String dureeCotiEtrangereAp73 = "";
    private String dureeCotiEtrangereAv73 = "";
    private String dureeCotRam = "";
    private String echelle = "";
    private String fractionRente = "";
    private String quotiteRente = "";
    private String idRenteAccordee = "";
    private String idTiers = "";
    private Boolean isInvaliditePrecoce = Boolean.FALSE;
    private Boolean isModifie = Boolean.FALSE;
    private Boolean isPrendreEnCompteCalculAcor = Boolean.FALSE;
    private Boolean isRenteAjournee = Boolean.FALSE;
    private Boolean isRevenuSplitte = Boolean.FALSE;
    private Boolean isSurvivantInvalid = Boolean.FALSE;
    private Boolean isTransfere = Boolean.FALSE;
    private String moisAppointAp73;
    private String moisAppointAv73;
    private String montantBTE = "";
    private String montantPrestation = "";
    private String montantReducAnticipation = "";
    private String nbrAnneeAnticipation = "";
    private String nbrAnneeBTA = "";
    private String nbrAnneeBTE = "";
    private String nbrAnneeBTR = "";
    private String officeAI = "";
    private String ram = "";
    private String supplementAjournement = "";
    private String supplementCarriere = "";
    private String survenanceEvenementAssure = "";
    private String codeMutation = "";

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return REHistoriqueRentes.createFromClause(_getCollection());
    }

    @Override
    protected String _getTableName() {
        return REHistoriqueRentes.TABLE_NAME_HISTORIQUE_RENTES;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        anneeMontantRAM = statement.dbReadNumeric(REHistoriqueRentes.FIELDNAME_ANNEE_MONTANT_RAM);
        anneeNiveau = statement.dbReadString(REHistoriqueRentes.FIELDNAME_ANNEE_NIVEAU);
        cleInfirmiteAtteinteFct = statement.dbReadNumeric(REHistoriqueRentes.FIELDNAME_CLE_INF_ATTEINTE_FCT);
        codePrestation = statement.dbReadString(REHistoriqueRentes.FIELDNAME_CODE_PRESTATION);
        codeRevenu = statement.dbReadString(REHistoriqueRentes.FIELDNAME_CODE_REVENU);
        cs1 = statement.dbReadString(REHistoriqueRentes.FIELDNAME_CS1);
        cs2 = statement.dbReadString(REHistoriqueRentes.FIELDNAME_CS2);
        cs3 = statement.dbReadString(REHistoriqueRentes.FIELDNAME_CS3);
        cs4 = statement.dbReadString(REHistoriqueRentes.FIELDNAME_CS4);
        cs5 = statement.dbReadString(REHistoriqueRentes.FIELDNAME_CS5);
        dateDebutAnticipation = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REHistoriqueRentes.FIELDNAME_DATE_DEB_ANTICIP));
        dateDebutDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REHistoriqueRentes.FIELDNAME_DATE_DEB_DROIT));
        dateFinDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REHistoriqueRentes.FIELDNAME_DATE_FIN_DROIT));
        dateRevocationAjournement = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REHistoriqueRentes.FIELDNAME_DATE_REVOC_AJOURN));
        degreInvalidite = statement.dbReadNumeric(REHistoriqueRentes.FIELDNAME_DEGR_INV);
        droitApplique = statement.dbReadString(REHistoriqueRentes.FIELDNAME_DROIT_APPLIQUE);
        dureeAjournement = statement.dbReadString(REHistoriqueRentes.FIELDNAME_DUREE_AJOURN);
        dureeCotAp73 = statement.dbReadString(REHistoriqueRentes.FIELDNAME_DUREE_COT_AP_73);
        dureeCotAv73 = statement.dbReadString(REHistoriqueRentes.FIELDNAME_DUREE_COT_AV_73);
        dureeCotRam = statement.dbReadString(REHistoriqueRentes.FIELDNAME_DUREE_COT_RAM);
        dureeCotiClasseAge = statement.dbReadString(REHistoriqueRentes.FIELDNAME_DUREE_COT_CLS_AGE);
        dureeCotiEtrangereAp73 = statement.dbReadString(REHistoriqueRentes.FIELDNAME_DUREE_COTI_ETR_AP_73);
        dureeCotiEtrangereAv73 = statement.dbReadString(REHistoriqueRentes.FIELDNAME_DUREE_COTI_ETR_AV_73);
        echelle = statement.dbReadString(REHistoriqueRentes.FIELDNAME_ECHELLE);
        fractionRente = statement.dbReadString(REHistoriqueRentes.FIELDNAME_FRACTION_RENTE);
        quotiteRente = statement.dbReadString(REHistoriqueRentes.FIELDNAME_QUOTITE_RENTE);
        idHistorique = statement.dbReadNumeric(REHistoriqueRentes.FIELDNAME_ID_HISTORIQUE_RENTES);
        idRenteAccordee = statement.dbReadNumeric(REHistoriqueRentes.FIELDNAME_ID_RENTE_ACCORDEE);
        idTiers = statement.dbReadNumeric(REHistoriqueRentes.FIELDNAME_ID_TIERS);
        isInvaliditePrecoce = statement.dbReadBoolean(REHistoriqueRentes.FIELDNAME_IS_INVAL_PRECOCE);
        isModifie = statement.dbReadBoolean(REHistoriqueRentes.FIELDNAME_IS_MODIFIE);
        isPrendreEnCompteCalculAcor = statement.dbReadBoolean(REHistoriqueRentes.FIELDNAME_IS_PRENDRE_CALCUL_ACOR);
        isRenteAjournee = statement.dbReadBoolean(REHistoriqueRentes.FIELDNAME_IS_RENTE_AJOURNEE);
        isRevenuSplitte = statement.dbReadBoolean(REHistoriqueRentes.FIELDNAME_IS_REVENU_SPLITTE);
        isSurvivantInvalid = statement.dbReadBoolean(REHistoriqueRentes.FIELDNAME_IS_SURV_INVALID);
        isTransfere = statement.dbReadBoolean(REHistoriqueRentes.FIELDNAME_IS_TRANSFERE);
        moisAppointAp73 = statement.dbReadNumeric(REHistoriqueRentes.FIELDNAME_MOIS_APP_AP_73);
        moisAppointAv73 = statement.dbReadNumeric(REHistoriqueRentes.FIELDNAME_MOIS_APP_AV_73);
        montantBTE = statement.dbReadNumeric(REHistoriqueRentes.FIELDNAME_MONTANT_BONUS_EDUC);
        montantPrestation = statement.dbReadNumeric(REHistoriqueRentes.FIELDNAME_MONTANT_PREST);
        montantReducAnticipation = statement.dbReadNumeric(REHistoriqueRentes.FIELDNAME_MONTANT_REDUC_ANTICIP);
        nbrAnneeAnticipation = statement.dbReadString(REHistoriqueRentes.FIELDNAME_NBR_ANNEE_ANTICIP);
        nbrAnneeBTA = statement.dbReadString(REHistoriqueRentes.FIELDNAME_NBR_ANNEE_BTA);
        nbrAnneeBTE = statement.dbReadString(REHistoriqueRentes.FIELDNAME_NBR_ANNEE_BTE);
        nbrAnneeBTR = statement.dbReadString(REHistoriqueRentes.FIELDNAME_NBR_ANNEE_TRANSIT);
        officeAI = statement.dbReadString(REHistoriqueRentes.FIELDNAME_OAI);
        ram = statement.dbReadNumeric(REHistoriqueRentes.FIELDNAME_RAM);
        supplementAjournement = statement.dbReadNumeric(REHistoriqueRentes.FIELDNAME_SUPPL_AJOURN);
        supplementCarriere = statement.dbReadNumeric(REHistoriqueRentes.FIELDNAME_SUPP_CARR);
        survenanceEvenementAssure = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REHistoriqueRentes.FIELDNAME_SURV_EV_ASSURE));
        codeMutation = statement.dbReadString(REHistoriqueRentes.FIELDNAME_CODE_MUTATION);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(REHistoriqueRentes.FIELDNAME_ID_HISTORIQUE_RENTES,
                this._dbWriteNumeric(statement.getTransaction(), idHistorique));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        if (_getAction() == BEntity.ACTION_COPY) {
            super._writeProperties(statement);
        } else {
            statement.writeField(REHistoriqueRentes.FIELDNAME_ID_HISTORIQUE_RENTES,
                    this._dbWriteNumeric(statement.getTransaction(), idHistorique, "idHistorique"));
        }

        statement.writeField(REHistoriqueRentes.FIELDNAME_ANNEE_MONTANT_RAM,
                this._dbWriteNumeric(statement.getTransaction(), anneeMontantRAM, "anneeMontantRAM"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_ANNEE_NIVEAU,
                this._dbWriteString(statement.getTransaction(), anneeNiveau, "anneeNiveau"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_CLE_INF_ATTEINTE_FCT,
                this._dbWriteNumeric(statement.getTransaction(), cleInfirmiteAtteinteFct, "cleInfirmiteAtteinteFct"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_CODE_PRESTATION,
                this._dbWriteString(statement.getTransaction(), codePrestation, "codePrestation"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_CODE_REVENU,
                this._dbWriteString(statement.getTransaction(), codeRevenu, "codeRevenu"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_CS1,
                this._dbWriteString(statement.getTransaction(), cs1, "cs1"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_CS2,
                this._dbWriteString(statement.getTransaction(), cs2, "cs2"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_CS3,
                this._dbWriteString(statement.getTransaction(), cs3, "cs3"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_CS4,
                this._dbWriteString(statement.getTransaction(), cs4, "cs4"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_CS5,
                this._dbWriteString(statement.getTransaction(), cs5, "cs5"));
        statement.writeField(
                REHistoriqueRentes.FIELDNAME_DATE_DEB_ANTICIP,
                this._dbWriteNumeric(statement.getTransaction(),
                        PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateDebutAnticipation), "dateDebutAnticipation"));
        statement.writeField(
                REHistoriqueRentes.FIELDNAME_DATE_DEB_DROIT,
                this._dbWriteNumeric(statement.getTransaction(),
                        PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateDebutDroit), "dateDebutDroit"));
        statement.writeField(
                REHistoriqueRentes.FIELDNAME_DATE_FIN_DROIT,
                this._dbWriteNumeric(statement.getTransaction(),
                        PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateFinDroit), "dateFinDroit"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_DATE_REVOC_AJOURN, this._dbWriteNumeric(
                statement.getTransaction(), PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateRevocationAjournement),
                "dateRevocationAjournement"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_DEGR_INV,
                this._dbWriteNumeric(statement.getTransaction(), degreInvalidite, "degreInvalidite"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_DROIT_APPLIQUE,
                this._dbWriteString(statement.getTransaction(), droitApplique, "droitApplique"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_DUREE_AJOURN,
                this._dbWriteString(statement.getTransaction(), dureeAjournement, "dureeAjournement"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_DUREE_COTI_ETR_AP_73,
                this._dbWriteString(statement.getTransaction(), dureeCotiEtrangereAp73, "dureeCotiEtrangereAp73"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_DUREE_COTI_ETR_AV_73,
                this._dbWriteString(statement.getTransaction(), dureeCotiEtrangereAv73, "dureeCotiEtrangereAv73"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_DUREE_COT_AP_73,
                this._dbWriteString(statement.getTransaction(), dureeCotAp73, "dureeCotAp73"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_DUREE_COT_AV_73,
                this._dbWriteString(statement.getTransaction(), dureeCotAv73, "dureeCotAv73"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_DUREE_COT_CLS_AGE,
                this._dbWriteString(statement.getTransaction(), dureeCotiClasseAge, "dureeCotiClasseAge"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_DUREE_COT_RAM,
                this._dbWriteString(statement.getTransaction(), dureeCotRam, "dureeCotRam"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_ECHELLE,
                this._dbWriteString(statement.getTransaction(), echelle, "echelle"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_FRACTION_RENTE,
                this._dbWriteString(statement.getTransaction(), fractionRente, "fractionRente"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_QUOTITE_RENTE,
                this._dbWriteNumeric(statement.getTransaction(), quotiteRente, "quotiteRente"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_ID_RENTE_ACCORDEE,
                this._dbWriteNumeric(statement.getTransaction(), idRenteAccordee, "idRenteAccordee"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_ID_TIERS,
                this._dbWriteNumeric(statement.getTransaction(), idTiers, "idTiers"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_IS_INVAL_PRECOCE,
                this._dbWriteBoolean(statement.getTransaction(), isInvaliditePrecoce, BConstants.DB_TYPE_BOOLEAN_CHAR,
                        "isInvaliditePrecoce"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_IS_MODIFIE, this._dbWriteBoolean(statement.getTransaction(),
                isModifie, BConstants.DB_TYPE_BOOLEAN_CHAR, "isModifie"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_IS_PRENDRE_CALCUL_ACOR, this._dbWriteBoolean(
                statement.getTransaction(), isPrendreEnCompteCalculAcor, BConstants.DB_TYPE_BOOLEAN_CHAR,
                "isPrendreEnCompteCalculAcor "));
        statement.writeField(REHistoriqueRentes.FIELDNAME_IS_RENTE_AJOURNEE, this._dbWriteBoolean(
                statement.getTransaction(), isRenteAjournee, BConstants.DB_TYPE_BOOLEAN_CHAR, "isRenteAjournee"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_IS_REVENU_SPLITTE, this._dbWriteBoolean(
                statement.getTransaction(), isRevenuSplitte, BConstants.DB_TYPE_BOOLEAN_CHAR, "isRevenuSplitte"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_IS_SURV_INVALID, this._dbWriteBoolean(
                statement.getTransaction(), isSurvivantInvalid, BConstants.DB_TYPE_BOOLEAN_CHAR, "isSurvivantInvalid"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_IS_TRANSFERE, this._dbWriteBoolean(
                statement.getTransaction(), isTransfere, BConstants.DB_TYPE_BOOLEAN_CHAR, "isTransfere"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_MOIS_APP_AP_73,
                this._dbWriteNumeric(statement.getTransaction(), moisAppointAp73, "moisAppointAp73"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_MOIS_APP_AV_73,
                this._dbWriteNumeric(statement.getTransaction(), moisAppointAv73, "moisAppointAv73"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_MONTANT_BONUS_EDUC,
                this._dbWriteNumeric(statement.getTransaction(), montantBTE, "montantBTE"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_MONTANT_PREST,
                this._dbWriteNumeric(statement.getTransaction(), montantPrestation, "montantPrestation"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_MONTANT_REDUC_ANTICIP,
                this._dbWriteNumeric(statement.getTransaction(), montantReducAnticipation, "montantReducAnticipation"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_NBR_ANNEE_ANTICIP,
                this._dbWriteString(statement.getTransaction(), nbrAnneeAnticipation, "nbrAnneeAnticipation"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_NBR_ANNEE_BTA,
                this._dbWriteString(statement.getTransaction(), nbrAnneeBTA, "nbrAnneeBTA"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_NBR_ANNEE_BTE,
                this._dbWriteString(statement.getTransaction(), nbrAnneeBTE, "nbrAnneeBTE"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_NBR_ANNEE_TRANSIT,
                this._dbWriteString(statement.getTransaction(), nbrAnneeBTR, "nbrAnneeBTR"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_OAI,
                this._dbWriteString(statement.getTransaction(), officeAI, "officeAI"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_RAM,
                this._dbWriteNumeric(statement.getTransaction(), ram, "ram"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_SUPPL_AJOURN,
                this._dbWriteNumeric(statement.getTransaction(), supplementAjournement, "supplementAjournement"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_SUPP_CARR,
                this._dbWriteNumeric(statement.getTransaction(), supplementCarriere, "supplementCarriere"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_SURV_EV_ASSURE, this._dbWriteNumeric(
                statement.getTransaction(), PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(survenanceEvenementAssure),
                "survenanceEvenementAssure"));
        statement.writeField(REHistoriqueRentes.FIELDNAME_CODE_MUTATION,
                this._dbWriteString(statement.getTransaction(), codeMutation, "codeMutation"));
    }

    public String getAnneeMontantRAM() {
        return anneeMontantRAM;
    }

    public String getAnneeNiveau() {
        return anneeNiveau;
    }

    public String getCleInfirmiteAtteinteFct() {
        return cleInfirmiteAtteinteFct;
    }

    public String getCodePrestation() {
        return codePrestation;
    }

    public String getCodeRevenu() {
        return codeRevenu;
    }

    public String getCs1() {
        return cs1;
    }

    public String getCs2() {
        return cs2;
    }

    public String getCs3() {
        return cs3;
    }

    public String getCs4() {
        return cs4;
    }

    public String getCs5() {
        return cs5;
    }

    public String getDateDebutAnticipation() {
        return dateDebutAnticipation;
    }

    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public String getDateRevocationAjournement() {
        return dateRevocationAjournement;
    }

    public String getDegreInvalidite() {
        return degreInvalidite;
    }

    public String getDroitApplique() {
        return droitApplique;
    }

    public String getDureeAjournement() {
        return dureeAjournement;
    }

    public String getDureeCotAp73() {
        return dureeCotAp73;
    }

    public String getDureeCotAv73() {
        return dureeCotAv73;
    }

    public String getDureeCotiClasseAge() {
        return dureeCotiClasseAge;
    }

    public String getDureeCotiEtrangereAp73() {
        return dureeCotiEtrangereAp73;
    }

    public String getDureeCotiEtrangereAv73() {
        return dureeCotiEtrangereAv73;
    }

    public String getDureeCotRam() {
        return dureeCotRam;
    }

    public String getEchelle() {
        return echelle;
    }

    public String getFractionRente() {
        return fractionRente;
    }

    public String getQuotiteRente() {
        return quotiteRente;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public Boolean getIsInvaliditePrecoce() {
        return isInvaliditePrecoce;
    }

    public Boolean getIsModifie() {
        return isModifie;
    }

    public Boolean getIsPrendreEnCompteCalculAcor() {
        return isPrendreEnCompteCalculAcor;
    }

    public Boolean getIsRenteAjournee() {
        return isRenteAjournee;
    }

    public Boolean getIsRevenuSplitte() {
        return isRevenuSplitte;
    }

    public Boolean getIsSurvivantInvalid() {
        return isSurvivantInvalid;
    }

    public Boolean getIsTransfere() {
        return isTransfere;
    }

    public String getMoisAppointAp73() {
        return moisAppointAp73;
    }

    public String getMoisAppointAv73() {
        return moisAppointAv73;
    }

    public String getMontantBTE() {
        return montantBTE;
    }

    public String getMontantPrestation() {
        return montantPrestation;
    }

    public String getMontantReducAnticipation() {
        return montantReducAnticipation;
    }

    public String getNbrAnneeAnticipation() {
        return nbrAnneeAnticipation;
    }

    public String getNbrAnneeBTA() {
        return nbrAnneeBTA;
    }

    public String getNbrAnneeBTE() {
        return nbrAnneeBTE;
    }

    public String getNbrAnneeBTR() {
        return nbrAnneeBTR;
    }

    public String getOfficeAI() {
        return officeAI;
    }

    public String getRam() {
        return ram;
    }

    public String getSupplementAjournement() {
        return supplementAjournement;
    }

    public String getSupplementCarriere() {
        return supplementCarriere;
    }

    public String getSurvenanceEvenementAssure() {
        return survenanceEvenementAssure;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public void setAnneeMontantRAM(String anneeMontantRAM) {
        this.anneeMontantRAM = anneeMontantRAM;
    }

    public void setAnneeNiveau(String anneeNiveau) {
        this.anneeNiveau = anneeNiveau;
    }

    public void setCleInfirmiteAtteinteFct(String cleInfirmiteAtteinteFct) {
        this.cleInfirmiteAtteinteFct = cleInfirmiteAtteinteFct;
    }

    public void setCodePrestation(String codePrestation) {
        this.codePrestation = codePrestation;
    }

    public void setCodeRevenu(String codeRevenu) {
        this.codeRevenu = codeRevenu;
    }

    public void setCs1(String cs1) {
        this.cs1 = cs1;
    }

    public void setCs2(String cs2) {
        this.cs2 = cs2;
    }

    public void setCs3(String cs3) {
        this.cs3 = cs3;
    }

    public void setCs4(String cs4) {
        this.cs4 = cs4;
    }

    public void setCs5(String cs5) {
        this.cs5 = cs5;
    }

    public void setDateDebutAnticipation(String dateDebutAnticipation) {
        this.dateDebutAnticipation = dateDebutAnticipation;
    }

    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    public void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    public void setDateRevocationAjournement(String dateRevocationAjournement) {
        this.dateRevocationAjournement = dateRevocationAjournement;
    }

    public void setDegreInvalidite(String degreInvalidite) {
        this.degreInvalidite = degreInvalidite;
    }

    public void setDroitApplique(String droitApplique) {
        this.droitApplique = droitApplique;
    }

    public void setDureeAjournement(String dureeAjournement) {
        this.dureeAjournement = dureeAjournement;
    }

    public void setDureeCotAp73(String dureeCotAp73) {
        this.dureeCotAp73 = dureeCotAp73;
    }

    public void setDureeCotAv73(String dureeCotAv73) {
        this.dureeCotAv73 = dureeCotAv73;
    }

    public void setDureeCotiClasseAge(String dureeCotiClasseAge) {
        this.dureeCotiClasseAge = dureeCotiClasseAge;
    }

    public void setDureeCotiEtrangereAp73(String dureeCotiEtrangereAp73) {
        this.dureeCotiEtrangereAp73 = dureeCotiEtrangereAp73;
    }

    public void setDureeCotiEtrangereAv73(String dureeCotiEtrangereAv73) {
        this.dureeCotiEtrangereAv73 = dureeCotiEtrangereAv73;
    }

    public void setDureeCotRam(String dureeCotRam) {
        this.dureeCotRam = dureeCotRam;
    }

    public void setEchelle(String echelle) {
        this.echelle = echelle;
    }

    public void setFractionRente(String fractionRente) {
        this.fractionRente = fractionRente;
    }

    public void setQuotiteRente(String quotiteRente) {
        this.quotiteRente = quotiteRente;
    }

    public void setIdRenteAccordee(String idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIsInvaliditePrecoce(Boolean isInvaliditePrecoce) {
        this.isInvaliditePrecoce = isInvaliditePrecoce;
    }

    public void setIsModifie(Boolean isModifie) {
        this.isModifie = isModifie;
    }

    public void setIsPrendreEnCompteCalculAcor(Boolean isPrendreEnCompteCalculAcor) {
        this.isPrendreEnCompteCalculAcor = isPrendreEnCompteCalculAcor;
    }

    public void setIsRenteAjournee(Boolean isRenteAjournee) {
        this.isRenteAjournee = isRenteAjournee;
    }

    public void setIsRevenuSplitte(Boolean isRevenuSplitte) {
        this.isRevenuSplitte = isRevenuSplitte;
    }

    public void setIsSurvivantInvalid(Boolean isSurvivantInvalid) {
        this.isSurvivantInvalid = isSurvivantInvalid;
    }

    public void setIsTransfere(Boolean isTransfere) {
        this.isTransfere = isTransfere;
    }

    public void setMoisAppointAp73(String moisAppointAp73) {
        this.moisAppointAp73 = moisAppointAp73;
    }

    public void setMoisAppointAv73(String moisAppointAv73) {
        this.moisAppointAv73 = moisAppointAv73;
    }

    public void setMontantBTE(String montantBTE) {
        this.montantBTE = montantBTE;
    }

    public void setMontantPrestation(String montantPrestation) {
        this.montantPrestation = montantPrestation;
    }

    public void setMontantReducAnticipation(String montantReducAnticipation) {
        this.montantReducAnticipation = montantReducAnticipation;
    }

    public void setNbrAnneeAnticipation(String nbrAnneeAnticipation) {
        this.nbrAnneeAnticipation = nbrAnneeAnticipation;
    }

    public void setNbrAnneeBTA(String nbrAnneeBTA) {
        this.nbrAnneeBTA = nbrAnneeBTA;
    }

    public void setNbrAnneeBTE(String nbrAnneeBTE) {
        this.nbrAnneeBTE = nbrAnneeBTE;
    }

    public void setNbrAnneeBTR(String nbrAnneeBTR) {
        this.nbrAnneeBTR = nbrAnneeBTR;
    }

    public void setOfficeAI(String officeAI) {
        this.officeAI = officeAI;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public void setSupplementAjournement(String supplementAjournement) {
        this.supplementAjournement = supplementAjournement;
    }

    public void setSupplementCarriere(String supplementCarriere) {
        this.supplementCarriere = supplementCarriere;
    }

    public void setSurvenanceEvenementAssure(String survenanceEvenementAssure) {
        this.survenanceEvenementAssure = survenanceEvenementAssure;
    }

    public String getCodeMutation() {
        return codeMutation;
    }

    public void setCodeMutation(String codeMutation) {
        this.codeMutation = codeMutation;
    }
}
