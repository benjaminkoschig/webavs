/*
 * Créé le 10 janv. 07
 */
package globaz.corvus.db.demandes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.db.demandes.PRDemande;

/**
 * @author scr
 */
public class REDemandePrestationJointDemandeRenteManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String ALIAS_DATE_FIN = "DATE_DE_FIN";

    private String forCsEtatDemandeIn = "";
    private String forCsTypeDemande = "";
    private String forDateDebut = "";
    private String forIdDemandeRente = "";
    private String forIdTiersRequerant = "";

    private transient String fromClause = null;

    public REDemandePrestationJointDemandeRenteManager() {
        wantCallMethodBeforeFind(true);
    }

    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder fields = new StringBuilder();
        fields.append(PRDemande.FIELDNAME_IDTIERS).append(", ");
        fields.append(REDemandePrestationJointDemandeRente.FIELDNAME_CS_TYPE_DEMANDE_RENTE).append(", ");
        fields.append(REDemandePrestationJointDemandeRente.FIELDNAME_DATE_RECEPTION).append(", ");
        fields.append(REDemandePrestationJointDemandeRente.FIELDNAME_DATE_DEBUT).append(", ");
        fields.append(REDemandePrestationJointDemandeRente.FIELDNAME_DATE_FIN).append(", ");
        fields.append(REDemandePrestationJointDemandeRente.FIELDNAME_DATE_DEPOT).append(", ");
        fields.append(REDemandePrestationJointDemandeRente.FIELDNAME_DATE_TRAITEMENT).append(", ");
        fields.append(REDemandePrestationJointDemandeRente.FIELDNAME_CS_ETAT).append(", ");
        fields.append(REDemandePrestationJointDemandeRente.FIELDNAME_ID_DEMANDE_RENTE).append(", ");
        fields.append(REDemandePrestationJointDemandeRente.FIELDNAME_ID_RENTE_CALCULEE).append(", ");
        fields.append(REDemandePrestationJointDemandeRente.FIELDNAME_CS_TYPE_CALCUL).append(", ");

        fields.append(" CASE  WHEN ( ");
        fields.append(REDemandePrestationJointDemandeRente.FIELDNAME_DATE_FIN).append("=0");
        fields.append(" OR ");
        fields.append(REDemandePrestationJointDemandeRente.FIELDNAME_DATE_FIN).append(" IS NULL) THEN 99999999 ELSE ");
        fields.append(REDemandePrestationJointDemandeRente.FIELDNAME_DATE_FIN).append(" END AS ")
                .append(REDemandePrestationJointDemandeRenteManager.ALIAS_DATE_FIN);

        return fields.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuilder from = new StringBuilder();

            from.append(getFromClauseFromEntity(_getCollection()));
            fromClause = from.toString();
        }
        return fromClause;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();
        String schema = _getCollection();

        if (!JadeStringUtil.isEmpty(forIdDemandeRente)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema);
            sql.append(REDemandePrestationJointDemandeRente.TABLE_NAME_DEMANDE_RENTE);
            sql.append(".");
            sql.append(REDemandePrestationJointDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);
            sql.append(" = ");
            sql.append(forIdDemandeRente);
        }

        if (!JadeStringUtil.isEmpty(forCsTypeDemande)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema);
            sql.append(REDemandePrestationJointDemandeRente.TABLE_NAME_DEMANDE_RENTE);
            sql.append(".");
            sql.append(REDemandePrestationJointDemandeRente.FIELDNAME_CS_TYPE_DEMANDE_RENTE);
            sql.append("=");
            sql.append(this._dbWriteNumeric(statement.getTransaction(), forCsTypeDemande));
        }

        if (!JadeStringUtil.isEmpty(forCsEtatDemandeIn)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema);
            sql.append(REDemandePrestationJointDemandeRente.TABLE_NAME_DEMANDE_RENTE);
            sql.append(".");
            sql.append(REDemandePrestationJointDemandeRente.FIELDNAME_CS_ETAT);
            sql.append(" IN ( ");
            sql.append(forCsEtatDemandeIn);
            sql.append(" )");
        }

        if (!JadeStringUtil.isEmpty(forDateDebut)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema);
            sql.append(REDemandePrestationJointDemandeRente.TABLE_NAME_DEMANDE_RENTE);
            sql.append(".");
            sql.append(REDemandePrestationJointDemandeRente.FIELDNAME_DATE_DEBUT);
            sql.append(">=");
            sql.append(this._dbWriteDateAMJ(statement.getTransaction(), forDateDebut));
        }

        if (!JadeStringUtil.isEmpty(forIdTiersRequerant)) {
            if (sql.length() != 0) {
                sql.append(" AND ");
            }
            sql.append(schema);
            sql.append(PRDemande.TABLE_NAME);
            sql.append(".");
            sql.append(PRDemande.FIELDNAME_IDTIERS);
            sql.append(" = ");
            sql.append(forIdTiersRequerant);
        }

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REDemandePrestationJointDemandeRente();
    }

    public String getForCsEtatDemandeIn() {
        return forCsEtatDemandeIn;
    }

    public String getForCsType() {
        return forCsTypeDemande;
    }

    public String getForCsTypeDemande() {
        return forCsTypeDemande;
    }

    public String getForIdDemandeRente() {
        return forIdDemandeRente;
    }

    public String getForIdTiersRequerant() {
        return forIdTiersRequerant;
    }

    protected String getFromClauseFromEntity(String schema) {
        return REDemandePrestationJointDemandeRente.createFromClause(_getCollection());
    }

    @Override
    public String getOrderByDefaut() {
        return PRDemande.FIELDNAME_IDDEMANDE;
    }

    public void setForCsEtatDemandeIn(String forCsEtatDemandeIn) {
        this.forCsEtatDemandeIn = forCsEtatDemandeIn;
    }

    public void setForCsTypeDemande(String forCsTypeDemande) {
        this.forCsTypeDemande = forCsTypeDemande;
    }

    public void setForDateDebut(String string) {
        forDateDebut = string;
    }

    public void setForIdDemandeRente(String forIdDemandeRente) {
        this.forIdDemandeRente = forIdDemandeRente;
    }

    public void setForIdTiersRequerant(String forIdTiersRequerant) {
        this.forIdTiersRequerant = forIdTiersRequerant;
    }
}
