package globaz.corvus.db.prestations;

import globaz.corvus.db.lots.RELot;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author PBA
 */
public class REPrestationJointLotManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDecision;
    private String forIdLot;
    private String forIdPrestation;

    public REPrestationJointLotManager() {
        super();

        forIdDecision = "";
        forIdLot = "";
        forIdPrestation = "";
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tablePrestation = _getCollection() + REPrestations.TABLE_NAME_PRESTATION;
        String tableLot = _getCollection() + RELot.TABLE_NAME_LOT;

        if (!JadeStringUtil.isBlankOrZero(forIdPrestation)) {
            sql.append(tablePrestation).append(".").append(REPrestations.FIELDNAME_ID_PRESTATION).append("=")
                    .append(forIdPrestation);
        }

        if (!JadeStringUtil.isBlankOrZero(forIdDecision)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tablePrestation).append(".").append(REPrestations.FIELDNAME_ID_DECISION).append("=")
                    .append(forIdDecision);
        }

        if (!JadeStringUtil.isBlankOrZero(forIdLot)) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            sql.append(tableLot).append(".").append(RELot.FIELDNAME_ID_LOT).append("=").append(forIdLot);
        }

        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REPrestationJointLot();
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    public String getForIdPrestation() {
        return forIdPrestation;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    public void setForIdPrestation(String forIdPrestation) {
        this.forIdPrestation = forIdPrestation;
    }
}
