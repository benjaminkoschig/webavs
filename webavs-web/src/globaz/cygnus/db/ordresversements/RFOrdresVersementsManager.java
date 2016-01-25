/*
 * Créé le 10 décembre 2010
 */
package globaz.cygnus.db.ordresversements;

import globaz.cygnus.api.ordresversements.IRFOrdresVersements;
import globaz.cygnus.db.paiement.RFAssDecOv;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author fha
 */
public class RFOrdresVersementsManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDecision = "";
    private String forIdPrestation = "";
    private String forOrderBy = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------
    public RFOrdresVersementsManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {

        StringBuffer fromClauseBuffer = new StringBuffer();
        String schema = _getCollection();

        // Clause sur la table des ordres de versement si idPrestation pas NULL
        if (!JadeStringUtil.isBlank(forIdPrestation)) {

            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFOrdresVersements.TABLE_NAME_ORVER);

        }

        // Clause sur la table associative entre Decision et Ordre de versement si idDecision pas NULL
        if (!JadeStringUtil.isBlank(forIdDecision)) {

            String innerJoin = " INNER JOIN ";
            String on = " ON ";
            String point = ".";
            String egal = "=";

            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFAssDecOv.TABLE_NAME);

            // Jointure entre la table des décision et la table associative RFAssDecOv
            fromClauseBuffer.append(innerJoin);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFOrdresVersements.TABLE_NAME_ORVER);
            fromClauseBuffer.append(on);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFOrdresVersements.TABLE_NAME_ORVER);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(RFOrdresVersements.FIELDNAME_ID_ORDRE_VERSEMENT);
            fromClauseBuffer.append(egal);
            fromClauseBuffer.append(schema);
            fromClauseBuffer.append(RFAssDecOv.TABLE_NAME);
            fromClauseBuffer.append(point);
            fromClauseBuffer.append(RFAssDecOv.FIELDNAME_ID_OV);
        }

        return fromClauseBuffer.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {

        StringBuffer sqlOrder = new StringBuffer();
        if (!JadeStringUtil.isEmpty(forOrderBy)) {
            sqlOrder.append(forOrderBy);
        }
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

        // Requete si le traitement vient des prestations
        if (!JadeStringUtil.isBlank(forIdPrestation)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            /*
             * sqlWhere.append(this._getCollection()); sqlWhere.append(RFOrdresVersements.TABLE_NAME_ORVER);
             * sqlWhere.append(".");
             */
            sqlWhere.append(RFOrdresVersements.FIELDNAME_ID_PRESTATION);
            sqlWhere.append(" = ");
            sqlWhere.append(this._dbWriteNumeric(statement.getTransaction(), forIdPrestation));

            // Requete si le traitement vient des décisions
        } else if (!JadeStringUtil.isBlank(forIdDecision)) {
            if (sqlWhere.length() > 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(RFOrdresVersements.FIELDNAME_TYPE_VERSEMENT);
            sqlWhere.append(" = ");
            sqlWhere.append((this._dbWriteNumeric(statement.getTransaction(), IRFOrdresVersements.CS_TYPE_RESTITUTION)));

            sqlWhere.append(" AND ");

            sqlWhere.append(RFAssDecOv.FIELDNAME_ID_DECISION);
            sqlWhere.append(" = ");
            sqlWhere.append((this._dbWriteNumeric(statement.getTransaction(), forIdDecision)));
        }

        return sqlWhere.toString();
    }

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFOrdresVersements();
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForIdPrestation() {
        return forIdPrestation;
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    @Override
    public String getOrderByDefaut() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForIdPrestation(String forIdPrestation) {
        this.forIdPrestation = forIdPrestation;
    }

}
