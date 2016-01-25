package globaz.naos.db.controleEmployeur;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class AFImprimerDecisionManager extends BManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private transient String fromClause = null;

    private String idPassage = new String();

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */

    @Override
    protected String _getFrom(BStatement statement) {

        if (fromClause == null) {
            StringBuffer from = new StringBuffer(AFImprimerDecision.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        // -- composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        sqlWhere += " IDSOUSTYPE = 227017";

        if (sqlWhere.length() != 0) {
            sqlWhere += " AND ";
        }
        sqlWhere += "IDCONTROLE <> 0";

        if (getIdPassage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDPASSAGE =" + getIdPassage();
        }

        return sqlWhere;

    }

    @Override
    protected BEntity _newEntity() throws Exception {

        return new AFImprimerDecision();
    }

    /*
     * protected String _getOrder(BStatement statement) { StringBuffer order = new StringBuffer();
     * order.append(_getCollection()+"HEANNOP.RNIANN, TYPEINCO DESC"); return order.toString(); }
     */

    public String getFromClause() {
        return fromClause;
    }

    public String getIdPassage() {
        return idPassage;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }
}
