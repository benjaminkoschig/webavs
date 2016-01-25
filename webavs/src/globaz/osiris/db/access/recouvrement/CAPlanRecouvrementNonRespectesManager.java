package globaz.osiris.db.access.recouvrement;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;

/**
 * Représente un container de type PlanRecouvrementNonRespectes.
 * 
 * @author Arnaud Dostes, 24-may-2005
 */
public class CAPlanRecouvrementNonRespectesManager extends CAEcheancePlanManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdEtat = "";
    private String forSelectionRole = "";
    private String orderBy = "";

    /**
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.access.recouvrement.CAEcheancePlanManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return super._getFrom(statement) + " a," + _getCollection() + "CAPLARP b, " + _getCollection()
                + CACompteAnnexe.TABLE_CACPTAP + " c";
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        if (!JadeStringUtil.isBlank(orderBy)) {
            return "a." + orderBy;
        } else {
            return "";
        }
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        setToDateExigibilite(JACalendar.todayJJsMMsAAAA());
        setForDateEffective("0");

        String sqlWhere = super._getWhere(statement)
                + " AND a.IDPLANRECOUVREMENT=b.IDPLANRECOUVREMENT AND b.IDCOMPTEANNEXE=c.IDCOMPTEANNEXE";

        if (sqlWhere.length() != 0) {
            sqlWhere += " AND ";
            sqlWhere += "b.IDETAT=" + this._dbWriteNumeric(statement.getTransaction(), CAPlanRecouvrement.CS_ACTIF);
        }

        if (!JadeStringUtil.isEmpty(getForSelectionRole())) {
            sqlWhere += " AND ";

            if (getForSelectionRole().indexOf(',') != -1) {
                String[] roles = JadeStringUtil.split(getForSelectionRole(), ',', Integer.MAX_VALUE);

                sqlWhere += "c.IDROLE IN (";

                for (int id = 0; id < roles.length; ++id) {
                    if (id > 0) {
                        sqlWhere += ",";
                    }

                    sqlWhere += this._dbWriteNumeric(statement.getTransaction(), roles[id]);
                }

                sqlWhere += ")";
            } else {
                sqlWhere += "c.IDROLE=" + this._dbWriteNumeric(statement.getTransaction(), getForSelectionRole());
            }
        }

        return sqlWhere;
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAPlanRecouvrementNonRespectes();
    }

    /**
     * @return
     */
    public String getForIdEtat() {
        return forIdEtat;
    }

    public String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * @param string
     */
    public void setForIdEtat(String string) {
        forIdEtat = string;
    }

    public void setForSelectionRole(String string) {
        forSelectionRole = string;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setOrderBy(String string) {
        orderBy = string;
    }

}
