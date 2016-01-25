/*
 * Créé le 17 janvier 2012
 */
package globaz.cygnus.db.statistiques;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author MBO
 */
public class RFSousTypeSoinsJointTypeSoinsStatistiquesManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forOrderBy = "";
    private transient String fromClause = null;

    public RFSousTypeSoinsJointTypeSoinsStatistiquesManager() {
        super();
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {

            StringBuffer from = new StringBuffer(
                    RFSousTypeSoinsJointTypeSoinsStatistiques.createFromClause(_getCollection()));
            fromClause = from.toString();
        }

        return fromClause;
    }

    @Override
    protected String _getOrder(BStatement statement) {

        StringBuffer sqlOrder = new StringBuffer();
        if (!JadeStringUtil.isEmpty(forOrderBy)) {
            sqlOrder.append(forOrderBy);
        }
        return sqlOrder.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RFSousTypeSoinsJointTypeSoinsStatistiques();
    }

    public String getForOrderBy() {
        return forOrderBy;
    }

    public String getFromClause() {
        return fromClause;
    }

    public void setForOrderBy(String forOrderBy) {
        this.forOrderBy = forOrderBy;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

}
