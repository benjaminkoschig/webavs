package globaz.pavo.db.upidaily;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

public class CIUpiDailyManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdUpiDaily = new String();
    private String forNomUpiDaily = new String();

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (getForIdUpiDaily().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KTID=" + _dbWriteNumeric(statement.getTransaction(), getForIdUpiDaily());
        }
        if (getForNomUpiDaily().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KTLNOM=" + _dbWriteString(statement.getTransaction(), getForNomUpiDaily());
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CIUpiDaily();
    }

    public String getForIdUpiDaily() {
        return forIdUpiDaily;
    }

    public String getForNomUpiDaily() {
        return forNomUpiDaily;
    }

    public void setForIdUpiDaily(String forIdUpiDaily) {
        this.forIdUpiDaily = forIdUpiDaily;
    }

    public void setForNomUpiDaily(String forNomUpiDaily) {
        this.forNomUpiDaily = forNomUpiDaily;
    }

}
