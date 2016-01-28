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
public class IJDecisionIJAIManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdPrononce = "";

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isBlankOrZero(getForIdPrononce())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += IJDecisionIJAI.FIELDNAME_ID_PRONONCE + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForIdPrononce());
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJDecisionIJAI();
    }

    public String getForIdPrononce() {
        return forIdPrononce;
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
        return IJDecisionIJAI.FIELDNAME_ID_DECISION + " DESC ";
    }

    public void setForIdPrononce(String forIdPrononce) {
        this.forIdPrononce = forIdPrononce;
    }

}
