package globaz.hermes.db.gestion;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.pavo.application.CIApplication;

public class HEIrrecouvrableCIManager extends HEOutputAnnonceListViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdLot = "";
    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    private String forMotifIn = "";
    private transient String fromClause = null;

    private boolean isIrrecouvrable = false;

    @Override
    protected String _getFields(BStatement statement) {

        // String sqlFields = super._getFields(statement);
        String sqlFields = "";

        sqlFields = " KANAVS" + ",KALNOM" + ",KADNAI" + ",KATSEX" + ",KAIPAY";

        // TODO Auto-generated method stub
        return sqlFields;
    }

    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(HEIrrecouvrableCI.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        // -- composant de la requete initialises avec les options par defaut
        String sqlWhere = super._getWhere(statement);

        // AJOUT

        if (getForIdLot().length() > 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " RMILOT = " + _dbWriteNumeric(statement.getTransaction(), forIdLot);
        }

        if (isIrrecouvrable()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " KATIRR = " + _dbWriteNumeric(statement.getTransaction(), CIApplication.CODE_IRRECOUVRABLE);
        }
        if (getForMotifIn().length() > 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " RNMOT IN( " + getForMotifIn() + ") ";
        }

        return sqlWhere;

    }

    @Override
    protected BEntity _newEntity() throws Exception {

        return new HEIrrecouvrableCI();
    }

    @Override
    public String getForIdLot() {
        return forIdLot;
    }

    public String getForMotifIn() {
        return forMotifIn;
    }

    public String getFromClause() {
        return fromClause;
    }

    public boolean isIrrecouvrable() {
        return isIrrecouvrable;
    }

    @Override
    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    public void setForMotifIn(String forMotifIn) {
        this.forMotifIn = forMotifIn;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setIrrecouvrable(boolean isIrrecouvrable) {
        this.isIrrecouvrable = isIrrecouvrable;
    }

}
