/*
 * Created on Jul 3, 2006
 * 
 * To change the template for this generated file go to Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 * Comments
 */
package globaz.naos.db.annonceAffilie;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Get a list of Distinct NumAffilite from AFAnnonceAffilie
 * 
 * @author cuva
 * 
 *         To change the template for this generated type comment go to Window&gt;Preferences&gt;Java&gt;Code
 *         Generation&gt;Code and Comments
 */
public class AFAnnonceAffilieDistinctManager extends AFAnnonceAffilieManager implements Cloneable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateAnnonce = "";
    private java.lang.String forEntreDate;
    private boolean useParent = false;

    /**
     * Override from globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement) Select only distinct MAIAFF.
     * 
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     **/
    @Override
    protected String _getFields(BStatement statement) {
        if (isUseParent()) {
            return super._getFields(statement);
        } else {
            return "DISTINCT AA.MAIAFF";
        }
    }

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sb = new StringBuffer(100);
        sb.append(_getCollection() + "AFAPREP AA INNER JOIN ");
        sb.append(_getCollection() + "AFAFFIP A ON(AA.MAIAFF = A.MAIAFF)");
        return sb.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     **/
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer(100);

        if (!JadeStringUtil.isEmpty(getForAffiliationId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("AA.MAIAFF=" + this._dbWriteNumeric(statement.getTransaction(), getForAffiliationId()));
        }

        if (!JadeStringUtil.isEmpty(getForTraitement())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("MPBTRA=" + this._dbWriteNumeric(statement.getTransaction(), getForTraitement()));
        }

        if (!JadeStringUtil.isEmpty(getForDateAnnonce())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append("MPDANN=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDateAnnonce()));
        }
        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getForEntreDate())) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }
            sqlWhere.append(" MADDEB <= " + this._dbWriteDateAMJ(statement.getTransaction(), getForEntreDate())
                    + " AND (MADFIN >= " + this._dbWriteDateAMJ(statement.getTransaction(), getForEntreDate())
                    + " OR MADFIN=0)");
        }

        return sqlWhere.toString();
    }

    /**
     * @see java.lang.Object#clone()
     **/
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * @return
     */
    @Override
    public String getForDateAnnonce() {
        return forDateAnnonce;
    }

    public java.lang.String getForEntreDate() {
        return forEntreDate;
    }

    /**
     * @return
     */
    public boolean isUseParent() {
        return useParent;
    }

    /**
     * @param string
     */
    @Override
    public void setForDateAnnonce(String string) {
        forDateAnnonce = string;
    }

    public void setForEntreDate(java.lang.String newForEntreDate) {
        forEntreDate = newForEntreDate;
    }

    /**
     * @param b
     */
    public void setUseParent(boolean b) {
        useParent = b;
    }

}
