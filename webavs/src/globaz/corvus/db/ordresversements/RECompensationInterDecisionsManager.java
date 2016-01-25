package globaz.corvus.db.ordresversements;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * @author HPE
 */
public class RECompensationInterDecisionsManager extends PRAbstractManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdOV;
    private String forIdOVCompensation;
    private String forIdTiers;

    public RECompensationInterDecisionsManager() {
        super();

        forIdOV = "";
        forIdOVCompensation = "";
        forIdTiers = "";
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        if (!JadeStringUtil.isBlankOrZero(forIdTiers)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(RECompensationInterDecisions.FIELDNAME_ID_TIERS).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdTiers()));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdOVCompensation)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(RECompensationInterDecisions.FIELDNAME_ID_OV_COMPENSATION).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdOVCompensation()));
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdOV)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(RECompensationInterDecisions.FIELDNAME_ID_ORDRE_VERSEMENT).append("=")
                    .append(this._dbWriteNumeric(statement.getTransaction(), getForIdOV()));
        }

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new RECompensationInterDecisions();
    }

    public String getForIdOV() {
        return forIdOV;
    }

    public String getForIdOVCompensation() {
        return forIdOVCompensation;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    @Override
    public String getOrderByDefaut() {
        return RECompensationInterDecisions.FIELDNAME_ID_COMP_INTER_DEC;
    }

    public void setForIdOV(String forIdOV) {
        this.forIdOV = forIdOV;
    }

    public void setForIdOVCompensation(String forIdOVCompensation) {
        this.forIdOVCompensation = forIdOVCompensation;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }
}
