/**
 * 
 */
package globaz.apg.db.annonces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author dde
 */
public class APBreakRuleManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDroit = "";

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(forIdDroit)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APBreakRule.FIELDNAME_ID_DROIT + "="
                    + this._dbWriteNumeric(statement.getTransaction(), forIdDroit);
        }

        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APBreakRule();
    }

    /**
     * @return the forIdDroit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * @param forIdDroit
     *            the forIdDroit to set
     */
    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

}
