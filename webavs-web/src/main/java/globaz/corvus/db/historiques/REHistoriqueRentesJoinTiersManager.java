package globaz.corvus.db.historiques;

import globaz.corvus.vb.historiques.REHistoriqueRentesJoinTiersViewBean;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * 
 * @author SCR
 * 
 */
public class REHistoriqueRentesJoinTiersManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATEDECES = "HPDDEC";
    public static final String FIELDNAME_DATENAISSANCE = "HPDNAI";
    public static final String FIELDNAME_ID_TIERS_TI = "HTITIE";
    public static final String FIELDNAME_NATIONALITE = "HNIPAY";
    public static final String FIELDNAME_NOM = "HTLDE1";
    public static final String FIELDNAME_NOM_FOR_SEARCH = "HTLDU1";
    public static final String FIELDNAME_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_PRENOM = "HTLDE2";
    public static final String FIELDNAME_PRENOM_FOR_SEARCH = "HTLDU2";
    public static final String FIELDNAME_SEXE = "HPTSEX";

    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_AVS_HISTO = "TIHAVSP";
    public static final String TABLE_PERSONNE = "TIPERSP";
    public static final String TABLE_TIERS = "TITIERP";

    public static final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REHistoriqueRentes.TABLE_NAME_HISTORIQUE_RENTES);

        // jointure entre table des historiques rente et table des numeros AVS
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REHistoriqueRentesJoinTiersManager.TABLE_AVS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REHistoriqueRentes.TABLE_NAME_HISTORIQUE_RENTES);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REHistoriqueRentes.FIELDNAME_ID_TIERS);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REHistoriqueRentesJoinTiersManager.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REHistoriqueRentesJoinTiersManager.FIELDNAME_ID_TIERS_TI);

        // jointure entre table table des numeros AVS et table des personnes
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REHistoriqueRentesJoinTiersManager.TABLE_PERSONNE);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REHistoriqueRentesJoinTiersManager.TABLE_AVS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REHistoriqueRentesJoinTiersManager.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REHistoriqueRentesJoinTiersManager.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REHistoriqueRentesJoinTiersManager.FIELDNAME_ID_TIERS_TI);

        // jointure entre table des personnes et table des tiers
        fromClauseBuffer.append(innerJoin);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REHistoriqueRentesJoinTiersManager.TABLE_TIERS);
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REHistoriqueRentesJoinTiersManager.TABLE_PERSONNE);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REHistoriqueRentesJoinTiersManager.FIELDNAME_ID_TIERS_TI);
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REHistoriqueRentesJoinTiersManager.TABLE_TIERS);
        fromClauseBuffer.append(point);
        fromClauseBuffer.append(REHistoriqueRentesJoinTiersManager.FIELDNAME_ID_TIERS_TI);

        // jointure avec la table
        fromClauseBuffer.append(" INNER JOIN ");
        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(REHistoriqueHeader.TABLE_NAME_HISTORIQUE_HEADER);
        fromClauseBuffer.append(" ON ");
        fromClauseBuffer.append(REHistoriqueHeader.FIELDNAME_ID_HISTORIQUE);
        fromClauseBuffer.append("=");
        fromClauseBuffer.append(REHistoriqueRentes.FIELDNAME_ID_HISTORIQUE_RENTES);

        return fromClauseBuffer.toString();
    }

    private String forIdRA = "";

    private String forIdTiersIn = "";
    private Boolean forIsEnvoyerAcor = null;
    private Boolean forIsModifie = null;

    private Boolean forIsRefSurRenteAccordeeRenseignee = null;

    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder fieldsQueryBuilder = new StringBuilder();

        // field pour REHistoriqueRentesJoinTiersViewBean
        fieldsQueryBuilder.append(REHistoriqueRentesJoinTiersViewBean.FIELDNAME_NOM).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentesJoinTiersViewBean.FIELDNAME_PRENOM).append(",");

        // field pour REHistoriqueRentes
        fieldsQueryBuilder.append(_getCollection()).append(REHistoriqueRentes.TABLE_NAME_HISTORIQUE_RENTES).append(".")
                .append(REHistoriqueRentes.FIELDNAME_PSPY).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_ANNEE_MONTANT_RAM).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_ANNEE_NIVEAU).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_CLE_INF_ATTEINTE_FCT).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_CODE_PRESTATION).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_CODE_REVENU).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_CS1).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_CS2).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_CS3).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_CS4).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_CS5).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_DATE_DEB_ANTICIP).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_DATE_DEB_DROIT).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_DATE_FIN_DROIT).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_DATE_REVOC_AJOURN).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_DEGR_INV).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_DROIT_APPLIQUE).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_DUREE_AJOURN).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_DUREE_COT_AP_73).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_DUREE_COT_AV_73).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_DUREE_COT_CLS_AGE).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_DUREE_COT_RAM).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_DUREE_COTI_ETR_AP_73).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_DUREE_COTI_ETR_AV_73).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_ECHELLE).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_FRACTION_RENTE).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_QUOTITE_RENTE).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_ID_HISTORIQUE_RENTES).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_ID_RENTE_ACCORDEE).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_ID_TIERS).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_IS_INVAL_PRECOCE).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_IS_MODIFIE).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_IS_PRENDRE_CALCUL_ACOR).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_IS_RENTE_AJOURNEE).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_IS_REVENU_SPLITTE).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_IS_SURV_INVALID).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_IS_TRANSFERE).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_MOIS_APP_AP_73).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_MOIS_APP_AV_73).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_MONTANT_BONUS_EDUC).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_MONTANT_PREST).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_MONTANT_REDUC_ANTICIP).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_NBR_ANNEE_ANTICIP).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_NBR_ANNEE_BTA).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_NBR_ANNEE_BTE).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_NBR_ANNEE_TRANSIT).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_NSS_BENEFICIAIRE).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_OAI).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_RAM).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_SUPP_CARR).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_SUPPL_AJOURN).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_SURV_EV_ASSURE).append(",");
        fieldsQueryBuilder.append(REHistoriqueRentes.FIELDNAME_CODE_MUTATION).append(",");

        // field pour REHistoriqueHeader
        fieldsQueryBuilder.append(REHistoriqueHeader.FIELDNAME_ID_HISTORIQUE).append(",");

        // variable pour le tri
        fieldsQueryBuilder.append("CASE WHEN (").append(REHistoriqueRentes.FIELDNAME_DATE_FIN_DROIT).append("=0")
                .append(" OR ").append(REHistoriqueRentes.FIELDNAME_DATE_FIN_DROIT).append(" IS NULL) THEN 99999999")
                .append(" ELSE ").append(REHistoriqueRentes.FIELDNAME_DATE_FIN_DROIT).append(" END AS ")
                .append(REHistoriqueRentes.CONSTANTE_ORDER_BY_DATE);

        return fieldsQueryBuilder.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return REHistoriqueRentesJoinTiersManager.createFromClause(_getCollection());
    }

    /**
     * Renvoie la clause WHERE de la requête SQL
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {

        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(forIdRA)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REHistoriqueRentes.FIELDNAME_ID_RENTE_ACCORDEE + " =  "
                    + this._dbWriteNumeric(statement.getTransaction(), forIdRA);
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdTiersIn)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REHistoriqueRentes.FIELDNAME_ID_TIERS + " IN ( " + forIdTiersIn + " ) ";
        }

        if (forIsRefSurRenteAccordeeRenseignee != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            if (forIsRefSurRenteAccordeeRenseignee.booleanValue()) {
                sqlWhere += REHistoriqueRentes.FIELDNAME_ID_RENTE_ACCORDEE + " IS NOT NULL AND "
                        + REHistoriqueRentes.FIELDNAME_ID_RENTE_ACCORDEE + " > 0 ";

            } else {
                sqlWhere += REHistoriqueRentes.FIELDNAME_ID_RENTE_ACCORDEE + " = 0 OR "
                        + REHistoriqueRentes.FIELDNAME_ID_RENTE_ACCORDEE + " IS NULL ";
            }

        }

        if (forIsEnvoyerAcor != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REHistoriqueRentes.FIELDNAME_IS_PRENDRE_CALCUL_ACOR
                    + "="
                    + this._dbWriteBoolean(statement.getTransaction(), forIsEnvoyerAcor,
                            BConstants.DB_TYPE_BOOLEAN_CHAR);

        }

        if (forIsModifie != null) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REHistoriqueRentes.FIELDNAME_IS_MODIFIE + "="
                    + this._dbWriteBoolean(statement.getTransaction(), forIsModifie, BConstants.DB_TYPE_BOOLEAN_CHAR);

        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REHistoriqueRentes();
    }

    public String getForIdRA() {
        return forIdRA;
    }

    public String getForIdTiersIn() {
        return forIdTiersIn;
    }

    public Boolean getForIsEnvoyerAcor() {
        return forIsEnvoyerAcor;
    }

    public Boolean getForIsModifie() {
        return forIsModifie;
    }

    public Boolean getForIsRefSurRenteAccordeeRenseignee() {
        return forIsRefSurRenteAccordeeRenseignee;
    }

    @Override
    public String getOrderByDefaut() {
        return null;
    }

    public void setForIdRA(String forIdRA) {
        this.forIdRA = forIdRA;
    }

    public void setForIdTiersIn(String forIdTiersIn) {
        this.forIdTiersIn = forIdTiersIn;
    }

    public void setForIsEnvoyerAcor(Boolean forIsEnvoyerAcor) {
        this.forIsEnvoyerAcor = forIsEnvoyerAcor;
    }

    public void setForIsModifie(Boolean forIsModifie) {
        this.forIsModifie = forIsModifie;
    }

    public void setForIsRefSurRenteAccordeeRenseignee(Boolean forIsRefSurRenteAccordeeRenseignee) {
        this.forIsRefSurRenteAccordeeRenseignee = forIsRefSurRenteAccordeeRenseignee;
    }
}
