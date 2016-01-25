/*
 * Créé le 15 sept. 05
 */
package globaz.ij.db.prononces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJMesureJointAgentExecutionManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forIdPrononce = "";

    private boolean forIsActiveDuringPeriode = false;
    private transient String fromClause = null;
    private String fromDateDebut = "";

    private String periodeDateDebut = "";
    private String periodeDateFin = "";
    private String toDateFin = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = IJMesureJointAgentExecution.createFromClause(_getCollection());
        }

        return fromClause;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isEmpty(forIdPrononce)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJMesure.FIELDNAME_ID_PRONONCE);
            whereClause.append("=");
            whereClause.append(forIdPrononce);
        }

        if (!JadeStringUtil.isEmpty(fromDateDebut)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append("(");
            whereClause.append(IJMesure.FIELDNAME_DATE_DEBUT);
            whereClause.append(">=");
            whereClause.append(_dbWriteDateAMJ(statement.getTransaction(), fromDateDebut));
            whereClause.append(" OR ");
            whereClause.append(IJMesure.FIELDNAME_DATE_DEBUT);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), "0"));
            whereClause.append(")");
        }

        if (!JadeStringUtil.isEmpty(toDateFin)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append("(");
            whereClause.append(IJMesure.FIELDNAME_DATE_FIN);
            whereClause.append("<=");
            whereClause.append(_dbWriteDateAMJ(statement.getTransaction(), toDateFin));
            whereClause.append(" OR ");
            whereClause.append(IJMesure.FIELDNAME_DATE_FIN);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), "0"));
            whereClause.append(")");
        }

        if (forIsActiveDuringPeriode) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append("(");
            // date de debut dans periode
            whereClause.append("(");
            whereClause.append(IJMesure.FIELDNAME_DATE_DEBUT);
            whereClause.append(">=");
            whereClause.append(_dbWriteDateAMJ(statement.getTransaction(), periodeDateDebut));
            whereClause.append(" AND ");
            whereClause.append(IJMesure.FIELDNAME_DATE_DEBUT);
            whereClause.append("<=");
            whereClause.append(_dbWriteDateAMJ(statement.getTransaction(), periodeDateFin));
            whereClause.append(")");

            whereClause.append(" OR ");
            // date de fin dans periode
            whereClause.append("(");
            whereClause.append(IJMesure.FIELDNAME_DATE_FIN);
            whereClause.append(">=");
            whereClause.append(_dbWriteDateAMJ(statement.getTransaction(), periodeDateDebut));
            whereClause.append(" AND ");
            whereClause.append(IJMesure.FIELDNAME_DATE_FIN);
            whereClause.append("<=");
            whereClause.append(_dbWriteDateAMJ(statement.getTransaction(), periodeDateFin));
            whereClause.append(")");

            whereClause.append(" OR ");
            // periode comprise dans date fin et date debut
            whereClause.append("(");
            whereClause.append("(");
            whereClause.append(IJMesure.FIELDNAME_DATE_DEBUT);
            whereClause.append("<");
            whereClause.append(_dbWriteDateAMJ(statement.getTransaction(), periodeDateDebut));

            whereClause.append(" OR ");

            whereClause.append(IJMesure.FIELDNAME_DATE_DEBUT);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), "0"));
            whereClause.append(")");
            whereClause.append(" AND ");
            whereClause.append("(");
            whereClause.append(IJMesure.FIELDNAME_DATE_FIN);
            whereClause.append(">");
            whereClause.append(_dbWriteDateAMJ(statement.getTransaction(), periodeDateFin));

            whereClause.append(" OR ");

            whereClause.append(IJMesure.FIELDNAME_DATE_FIN);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), "0"));
            whereClause.append(")");
            whereClause.append(")");
            whereClause.append(")");
        }

        return whereClause.toString();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJMesureJointAgentExecution();
    }

    /**
     * getter pour l'attribut for id prononce
     * 
     * @return la valeur courante de l'attribut for id prononce
     */
    public String getForIdPrononce() {
        return forIdPrononce;
    }

    /**
     * @return
     */
    public String getFromDateDebut() {
        return fromDateDebut;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return IJAgentExecution.FIELDNAME_ID_AGENT_EXECUTION;
    }

    /**
     * @return
     */
    public String getToDateFin() {
        return toDateFin;
    }

    /**
     * setter pour l'attribut for id prononce
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdPrononce(String string) {
        forIdPrononce = string;
    }

    /**
     * @param string
     */
    public void setForIsActiveDuringPeriode(String dateDebut, String dateFin) {
        forIsActiveDuringPeriode = true;
        periodeDateDebut = dateDebut;
        periodeDateFin = dateFin;
    }

    /**
     * @param string
     */
    public void setFromDateDebut(String string) {
        fromDateDebut = string;
    }

    /**
     * @param string
     */
    public void setToDateFin(String string) {
        toDateFin = string;
    }

}
