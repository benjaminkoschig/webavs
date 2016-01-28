package globaz.cygnus.db.decisions;

import globaz.cygnus.db.dossiers.RFDossier;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.prestation.db.demandes.PRDemande;

/**
 * 
 * @author dma
 */
public class RFDecisionJointTiersManagerSummary extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdTiers = "";
    private transient String fromClause = null;

    public RFDecisionJointTiersManagerSummary() {
        super();
        wantCallMethodBeforeFind(true);
    }

    // @Override
    // protected String _getFields(BStatement statement) {
    // StringBuffer fields = new StringBuffer();
    // fields.append(RFDecision.FIELDNAME_MONTANT_TOTAL_RFM);
    // fields.append(',');
    // fields.append(RFDecision.FIELDNAME_DATE_VALIDATION);
    // fields.append(',');
    // fields.append(RFDecision.FIELDNAME_DATE_DEBUT_RETRO);
    // fields.append(',');
    // fields.append(RFDecision.FIELDNAME_DATE_FIN_RETRO);
    // fields.append(',');
    // fields.append(RFDecision.FIELDNAME_MONTANT_TOTAL_RFM);
    // fields.append(',');
    // return fields.toString();
    // }
    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(RFDecisionJointTiersSummary.createFromClause(_getCollection()));
            fromClause = from.toString();
        }
        return fromClause;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        StringBuffer sqlOrder = new StringBuffer();
        return sqlOrder.toString();
    }

    /**
     * Redéfinition de la méthode _getWhere du parent afin de générer le WHERE de la requête en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();
        String schema = _getCollection();

        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String egal = " = ";

        if (!JadeStringUtil.isEmpty(forIdTiers)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(schema);
            sqlWhere.append(PRDemande.TABLE_NAME);
            sqlWhere.append(".");
            sqlWhere.append(PRDemande.FIELDNAME_IDTIERS);
            sqlWhere.append(egal);
            sqlWhere.append(forIdTiers);
            sqlWhere.append(" AND ");
            sqlWhere.append(schema);
            sqlWhere.append(RFDecision.TABLE_NAME);
            sqlWhere.append(".");
            sqlWhere.append(RFDecision.FIELDNAME_DATE_VALIDATION);
            sqlWhere.append(egal);
            sqlWhere.append(" (select max( ");
            sqlWhere.append("decisionMax.");
            sqlWhere.append(RFDecision.FIELDNAME_DATE_VALIDATION);
            sqlWhere.append(" ) from ");
            sqlWhere.append(schema);
            sqlWhere.append(RFDecision.TABLE_NAME);
            sqlWhere.append(" as decisionMax ");
            sqlWhere.append(innerJoin);
            sqlWhere.append(schema);
            sqlWhere.append(RFAssDossierDecision.TABLE_NAME);
            sqlWhere.append(" as assDec ");
            sqlWhere.append(on);
            sqlWhere.append(" assDec.");
            sqlWhere.append(RFAssDossierDecision.FIELDNAME_ID_DECISION);
            sqlWhere.append(egal);
            sqlWhere.append("decisionMax.");
            sqlWhere.append(RFDecision.FIELDNAME_ID_DECISION);

            // jointure entre la table association dossiers-décisions et la table
            // dossiers
            sqlWhere.append(innerJoin);
            sqlWhere.append(schema);
            sqlWhere.append(RFDossier.TABLE_NAME);
            sqlWhere.append(" as dossie ");
            sqlWhere.append(on);
            sqlWhere.append(" dossie.");
            sqlWhere.append(RFDossier.FIELDNAME_ID_DOSSIER);
            sqlWhere.append(egal);
            sqlWhere.append(" assDec.");
            sqlWhere.append(RFAssDossierDecision.FIELDNAME_ID_DOSSIER);

            // jointure entre la table des dossiers et la table des demandes
            // prestation
            sqlWhere.append(innerJoin);
            sqlWhere.append(schema);
            sqlWhere.append(PRDemande.TABLE_NAME);
            sqlWhere.append(" as demand ");
            sqlWhere.append(on);
            sqlWhere.append(" demand.");
            sqlWhere.append(PRDemande.FIELDNAME_IDDEMANDE);
            sqlWhere.append(egal);
            sqlWhere.append(" dossie.");
            sqlWhere.append(RFDossier.FIELDNAME_ID_PRDEM);
            sqlWhere.append(" where ");
            sqlWhere.append(" demand.");
            sqlWhere.append(PRDemande.FIELDNAME_IDTIERS);
            sqlWhere.append(" = ");
            sqlWhere.append(forIdTiers);
            sqlWhere.append(" )");
        }

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFDecisionJointTiersSummary();
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getFromClause() {
        return fromClause;
    }

    @Override
    public String getOrderByDefaut() {
        return null;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}
