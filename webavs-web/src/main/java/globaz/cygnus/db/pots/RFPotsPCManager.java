/*
 * Cr�� le 11 janvier 2009
 */
package globaz.cygnus.db.pots;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * author fha
 */
public class RFPotsPCManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * G�n�ration de la clause from pour la requ�te
     * 
     * @param schema
     * 
     * @return la clause from
     */
    public static final String createFromClause(String schema) {

        StringBuffer fromClauseBuffer = new StringBuffer();

        fromClauseBuffer.append(schema);
        fromClauseBuffer.append(RFPotsPC.TABLE_NAME);

        return fromClauseBuffer.toString();
    }

    private transient String forDateDebut = "";
    private transient String forDateFin = "";
    private transient String forIdPotPC = "";

    private String forOrderBy = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private transient String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe RFPotsPCManager
     */
    public RFPotsPCManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {

            StringBuffer from = new StringBuffer(createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getOrder(BStatement statement) {

        StringBuffer sqlOrder = new StringBuffer();
        sqlOrder.append("FIDDEP");
        return sqlOrder.toString();
    }

    /**
     * Red�finition de la m�thode _getWhere du parent afin de g�n�rer le WHERE de la requ�te en fonction des besoins
     * 
     * @param statement
     */
    @Override
    protected String _getWhere(BStatement statement) {

        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isEmpty(forIdPotPC)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFPotsPC.FIELDNAME_ID_POT_PC);
            sqlWhere.append(" = ");
            sqlWhere.append(_dbWriteNumeric(statement.getTransaction(), forIdPotPC));
        }

        if (!JadeStringUtil.isEmpty(forDateDebut)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(RFPotsPC.FIELDNAME_DATE_DEBUT);
            sqlWhere.append(" <= ");
            sqlWhere.append(_dbWriteDateAMJ(statement.getTransaction(), forDateDebut));
        }

        if (!JadeStringUtil.isEmpty(forDateFin)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ( ");
            }
            sqlWhere.append(RFPotsPC.FIELDNAME_DATE_FIN);
            sqlWhere.append(" >= ");
            sqlWhere.append(_dbWriteDateAMJ(statement.getTransaction(), forDateFin));
            sqlWhere.append(" OR ");
            sqlWhere.append(RFPotsPC.FIELDNAME_DATE_FIN);
            sqlWhere.append(" = 0 )");
        }

        return sqlWhere.toString();
    }

    /**
     * D�finition de l'entit� (RFPotsPC)
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFPotsPC();
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForIdPotPC() {
        return forIdPotPC;
    }

    public String getForOrderBy() {
        return forOrderBy;
    }

    public String getFromClause() {
        return fromClause;
    }

    public String getOrderByDefaut() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForIdPotPC(String forIdPotPC) {
        this.forIdPotPC = forIdPotPC;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}
