package globaz.corvus.db.decisions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.List;

/**
 * @author BSC
 */
public class RECopieDecisionManager extends PRAbstractManager implements BIGenericManager<RECopieDecision> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDecision = "";
    private String forIdExterne = "";
    private String forIdTiers = "";

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(getForIdDecision())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RECopieDecision.FIELDNAME_ID_DECISION);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), getForIdDecision()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdTiers())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RECopieDecision.FIELDNAME_ID_TIERS_COPIE);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), getForIdTiers()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdExterne())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RECopieDecision.FIELDNAME_ID_AFFILIE);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), getForIdExterne()));
        }

        return whereClause.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RECopieDecision();
    }

    @Override
    public List<RECopieDecision> getContainerAsList() {
        List<RECopieDecision> list = new ArrayList<RECopieDecision>();
        for (int i = 0; i < size(); i++) {
            list.add((RECopieDecision) get(i));
        }
        return list;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForIdExterne() {
        return forIdExterne;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    @Override
    public String getOrderByDefaut() {
        return RECopieDecision.FIELDNAME_ID_DECISION_COPIE;
    }

    public void setForIdDecision(String string) {
        forIdDecision = string;
    }

    public void setForIdExterne(String forIdExterne) {
        this.forIdExterne = forIdExterne;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }
}
