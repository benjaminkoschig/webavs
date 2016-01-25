package globaz.ij.db.decisions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * 
 * @author SCR
 * 
 */
public class IJCopieDecisionManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDecision = "";

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        if (!JadeStringUtil.isBlankOrZero(getForIdDecision())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += IJCopieDecision.FIELDNAME_ID_DECISION + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForIdDecision());
        }
        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJCopieDecision();
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return IJCopieDecision.FIELDNAME_ID_DECISION + " DESC ";
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }
}
