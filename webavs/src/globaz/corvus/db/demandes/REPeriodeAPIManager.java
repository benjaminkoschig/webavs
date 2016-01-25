package globaz.corvus.db.demandes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendarGregorian;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;
import globaz.webavs.common.BIGenericManager;
import java.util.ArrayList;
import java.util.List;

public class REPeriodeAPIManager extends PRAbstractManager implements BIGenericManager<REPeriodeAPI> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDemandeRente = "";
    private String forDateFinInvaliditeInMonthYear = "";

    public String getForDateFinInvaliditeInMonthYear() {
        return forDateFinInvaliditeInMonthYear;
    }

    public void setForDateFinInvaliditeInMonthYear(String forDateFinInvaliditeInMonthYear) {
        this.forDateFinInvaliditeInMonthYear = forDateFinInvaliditeInMonthYear;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isEmpty(getForIdDemandeRente())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPeriodeAPI.FIELDNAME_ID_DEMANDE_RENTE + "="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdDemandeRente());
        }

        if (!JadeStringUtil.isEmpty(getForDateFinInvaliditeInMonthYear())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            String firstDateInMonth = "01." + forDateFinInvaliditeInMonthYear;
            String lastDateInMonth = "";

            try {
                lastDateInMonth = (new JACalendarGregorian()).lastInMonth(firstDateInMonth);
            } catch (Exception e) {
                _addError(statement.getTransaction(), "forDateFinInvaliditeInMonthYear : "
                        + "unable to find lastDateInMonth for the following date : " + firstDateInMonth
                        + " Exception :" + e.getMessage());
            }

            sqlWhere += REPeriodeAPI.FIELDNAME_DATE_FIN_INVALIDITE + ">="
                    + this._dbWriteDateAMJ(statement.getTransaction(), firstDateInMonth) + " AND "
                    + REPeriodeAPI.FIELDNAME_DATE_FIN_INVALIDITE + "<="
                    + this._dbWriteDateAMJ(statement.getTransaction(), lastDateInMonth);
        }

        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new REPeriodeAPI();
    }

    @Override
    public List<REPeriodeAPI> getContainerAsList() {
        List<REPeriodeAPI> list = new ArrayList<REPeriodeAPI>();

        for (int i = 0; i < size(); i++) {
            list.add((REPeriodeAPI) get(i));
        }

        return list;
    }

    public String getForIdDemandeRente() {
        return forIdDemandeRente;
    }

    @Override
    public String getOrderByDefaut() {
        return REPeriodeAPI.FIELDNAME_DATE_DEBUT_INVALIDITE;
    }

    public void setForIdDemandeRente(String string) {
        forIdDemandeRente = string;
    }
}
