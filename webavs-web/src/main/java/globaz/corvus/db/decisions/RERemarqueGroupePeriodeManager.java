package globaz.corvus.db.decisions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

public class RERemarqueGroupePeriodeManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateAu = "";
    private String forDatedu = "";
    private String forIdDecision = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(getForIdDecision())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RERemarqueGroupePeriode.FIELDNAME_ID_DECISION);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), getForIdDecision()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForDatedu())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(RERemarqueGroupePeriode.FIELDNAME_DATE_DEPUIS);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), getForDatedu()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForDateAu())) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append("(" + RERemarqueGroupePeriode.FIELDNAME_DATE_AU);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), getForDateAu()));

            whereClause.append(" OR ");

            whereClause.append(RERemarqueGroupePeriode.FIELDNAME_DATE_AU);
            whereClause.append("= 0)");
        }

        return whereClause.toString();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RERemarqueGroupePeriode();
    }

    public String getForDateAu() {
        return forDateAu;
    }

    public String getForDatedu() {
        return forDatedu;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    /**
     * (non-Javadoc).
     * 
     * @return la valeur courante de l'attribut order by defaut
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        return RERemarqueGroupePeriode.FIELDNAME_ID_REM_GROUPE_PERIODE;
    }

    public void setForDateAu(String forDateAu) {
        this.forDateAu = forDateAu;
    }

    public void setForDatedu(String forDatedu) {
        this.forDatedu = forDatedu;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

}
