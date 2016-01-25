package globaz.hercule.db.controleEmployeur;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.utils.CEUtils;

public class CEImprimerDecisionManager extends BManager implements FWListViewBeanInterface {

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
            StringBuffer from = new StringBuffer(CEImprimerDecision.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        CEUtils.sqlAddCondition(sqlWhere, " IDSOUSTYPE = 227017");

        CEUtils.sqlAddCondition(sqlWhere, "IDCONTROLE <> 0");

        if (getIdPassage().length() != 0) {
            CEUtils.sqlAddCondition(sqlWhere, "IDPASSAGE =" + getIdPassage());
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEImprimerDecision();
    }

    // protected String _getOrder(BStatement statement) {
    // StringBuffer order = new StringBuffer();
    // order.append(_getCollection() + "HEANNOP.RNIANN, TYPEINCO DESC");
    // return order.toString();
    // }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getFromClause() {
        return fromClause;
    }

    public String getIdPassage() {
        return idPassage;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }
}
