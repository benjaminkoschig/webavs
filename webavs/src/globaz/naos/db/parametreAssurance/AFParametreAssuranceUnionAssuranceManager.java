/*
 * Created on Jul 14, 2006
 * 
 * To change the template for this generated file go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 * Comments
 */
package globaz.naos.db.parametreAssurance;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.assurance.AFAssurance;

/**
 * @author cuva Created on Jul 14, 2006
 * 
 *         To change the template for this generated type comment go to Window&gt;Preferences&gt;Java&gt;Code
 *         Generation&gt;Code and Comments
 */
public class AFParametreAssuranceUnionAssuranceManager extends AFParametreAssuranceManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forTypeAssurance = null;

    private transient String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    /**
     * Surcharge.
     * 
     * @param statement
     * @return
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     **/
    @Override
    protected String _getFields(BStatement statement) {
        return super._getFields(statement);
    }

    /**
     * Surcharge.
     * 
     * @param statement
     * @return
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     **/
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = AFParametreAssuranceUnionAssurance.createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * Surcharge.
     * 
     * @param statement
     * @return
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     **/
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sbWhere = new StringBuffer(super._getWhere(statement));
        if (!JadeStringUtil.isEmpty(getForTypeAssurance())) {
            if (!JadeStringUtil.isEmpty(sbWhere.toString())) {
                sbWhere.append(" and ");
            }
            sbWhere.append(AFAssurance.FIELD_TYPE_ASSURANCE);
            sbWhere.append("=" + getForTypeAssurance());
        }

        return sbWhere.toString();
    }

    /**
     * Surcharge.
     * 
     * @return
     * @throws java.lang.Exception
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     **/
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFParametreAssuranceUnionAssurance();
    }

    /**
     * Getter for TypeAssurance
     * 
     * @return
     */
    public String getForTypeAssurance() {
        return forTypeAssurance;
    }

    /**
     * Setter for type assurance
     * 
     * @param string
     */
    public void setForTypeAssurance(String string) {
        forTypeAssurance = string;
    }

}
