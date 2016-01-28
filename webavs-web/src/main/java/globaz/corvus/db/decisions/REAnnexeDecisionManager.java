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
public class REAnnexeDecisionManager extends PRAbstractManager implements BIGenericManager<REAnnexeDecision> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDecision = "";

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(getForIdDecision())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(REAnnexeDecision.FIELDNAME_ID_DECISION);
            whereClause.append("=");
            whereClause.append(this._dbWriteNumeric(statement.getTransaction(), getForIdDecision()));
        }

        return whereClause.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REAnnexeDecision();
    }

    @Override
    public List<REAnnexeDecision> getContainerAsList() {
        List<REAnnexeDecision> list = new ArrayList<REAnnexeDecision>();
        for (int i = 0; i < size(); i++) {
            list.add((REAnnexeDecision) get(i));
        }
        return list;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    @Override
    public String getOrderByDefaut() {
        return REAnnexeDecision.FIELDNAME_ID_DECISION_ANNEXE;
    }

    public void setForIdDecision(String string) {
        forIdDecision = string;
    }
}
