/*
 * Créé le 7 sept. 05
 */
package globaz.ij.db.prestations;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJIJCalculeeManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebutPeriode = "";
    private String dateFinPeriode = "";
    private String forIdPrononce = "";
    private String forNoRevision = "";
    private String inGaranti3Revision = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        BTransaction transaction = statement.getTransaction();
        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isEmpty(inGaranti3Revision)) {
            if (whereClause.length() != 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJIJCalculee.FIELDNAME_GARANTIE_R3);
            whereClause.append(" IN (");
            whereClause.append(inGaranti3Revision);
            whereClause.append(") ");
        }

        if (!JadeStringUtil.isBlankOrZero(forNoRevision)) {
            if (whereClause.length() != 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJIJCalculee.FIELDNAME_NO_REVISION);
            whereClause.append("=");
            whereClause.append(_dbWriteString(transaction, forNoRevision));

        }

        if (!JadeStringUtil.isIntegerEmpty(forIdPrononce)) {
            if (whereClause.length() != 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJIJCalculee.FIELDNAME_ID_PRONONCE);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(transaction, forIdPrononce));
        }

        if (!JAUtil.isDateEmpty(dateDebutPeriode)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJIJCalculee.FIELDNAME_DATE_DEBUT_DROIT);
            whereClause.append("<=");
            whereClause.append(_dbWriteDateAMJ(transaction, dateFinPeriode));
            whereClause.append(" AND (");
            whereClause.append(IJIJCalculee.FIELDNAME_DATE_FIN_DROIT);
            whereClause.append("=");
            whereClause.append(_dbWriteDateAMJ(transaction, "0"));
            whereClause.append(" OR ");
            whereClause.append(IJIJCalculee.FIELDNAME_DATE_FIN_DROIT);
            whereClause.append(">=");
            whereClause.append(_dbWriteDateAMJ(transaction, dateDebutPeriode));
            whereClause.append(")");
        }

        return whereClause.toString();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new IJIJCalculee();
    }

    /**
     * getter pour l'attribut for id prononce
     * 
     * @return la valeur courante de l'attribut for id prononce
     */
    public String getForIdPrononce() {
        return forIdPrononce;
    }

    public String getForNoRevision() {
        return forNoRevision;
    }

    /**
     * @deprecated replaced by noRevision
     * @return
     */
    @Deprecated
    public String getInGaranti3Revision() {
        return inGaranti3Revision;
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
        return IJIJCalculee.FIELDNAME_ID_IJ_CALCULEE;
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

    public void setForNoRevision(String forNoRevision) {
        this.forNoRevision = forNoRevision;
    }

    /**
     * setter pour l'attribut for periode
     * 
     * @param dateDebutPeriode
     *            une nouvelle valeur pour cet attribut
     * @param dateFinPeriode
     *            une nouvelle valeur pour cet attribut
     */
    public void setForPeriode(String dateDebutPeriode, String dateFinPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
        this.dateFinPeriode = dateFinPeriode;
    }

    /**
     * @deprecated replaced by noRevision
     * @param string
     */
    @Deprecated
    public void setInGaranti3Revision(String string) {
        inGaranti3Revision = string;
    }

}
