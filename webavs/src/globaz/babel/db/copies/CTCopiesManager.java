package globaz.babel.db.copies;

import globaz.babel.db.CTAbstractManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class CTCopiesManager extends CTAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdTiersRequerant = "";
    private String fromDateDebut = "";
    private String toDateFin = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isEmpty(forIdTiersRequerant)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(CTCopies.FIELDNAME_ID_TIERS_REQUERANT);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), forIdTiersRequerant));
        }

        if (!JadeStringUtil.isBlankOrZero(fromDateDebut)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(" ( " + CTCopies.FIELDNAME_DATE_DEBUT_COPIES);
            whereClause.append(" <= ");
            whereClause.append(_dbWriteDateAMJ(statement.getTransaction(), fromDateDebut));

            whereClause.append(" OR  (");
            whereClause.append(CTCopies.FIELDNAME_DATE_DEBUT_COPIES);
            whereClause.append(" = 0 OR ");
            whereClause.append(CTCopies.FIELDNAME_DATE_DEBUT_COPIES);
            whereClause.append(" IS NULL )) ");

        }

        if (!JadeStringUtil.isBlankOrZero(toDateFin)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(" ( " + CTCopies.FIELDNAME_DATE_FIN_COPIES);
            whereClause.append(" >= ");
            whereClause.append(_dbWriteDateAMJ(statement.getTransaction(), toDateFin));

            whereClause.append(" OR (");
            whereClause.append(CTCopies.FIELDNAME_DATE_FIN_COPIES);
            whereClause.append(" = 0 OR ");
            whereClause.append(CTCopies.FIELDNAME_DATE_FIN_COPIES);
            whereClause.append(" IS NULL )) ");
        }

        return whereClause.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CTCopies();
    }

    /**
     * getter pour l'attribut for id document
     * 
     * @return la valeur courante de l'attribut for id document
     */
    public String getForIdTiersRequerant() {
        return forIdTiersRequerant;
    }

    public String getFromDateDebut() {
        return fromDateDebut;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.babel.db.CTAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return CTCopies.FIELDNAME_ID_COPIE;
    }

    public String getToDateFin() {
        return toDateFin;
    }

    public void setForIdTiersRequerant(String s) {
        forIdTiersRequerant = s;
    }

    public void setFromDateDebut(String fromDateDebut) {
        this.fromDateDebut = fromDateDebut;
    }

    public void setToDateFin(String toDateFin) {
        this.toDateFin = toDateFin;
    }

}
