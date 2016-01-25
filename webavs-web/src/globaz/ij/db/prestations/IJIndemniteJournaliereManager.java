package globaz.ij.db.prestations;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJIndemniteJournaliereManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsTypeIndemnite = "";
    private String forIdIJCalculee = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(forIdIJCalculee)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJIndemniteJournaliere.FIELDNAME_ID_IJ_CALCULEE);
            whereClause.append("=");
            whereClause.append(forIdIJCalculee);
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsTypeIndemnite)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJIndemniteJournaliere.FIELDNAME_CS_TYPE_INDEMNISATION);
            whereClause.append("=");
            whereClause.append(forCsTypeIndemnite);
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
        return new IJIndemniteJournaliere();
    }

    /**
     * getter pour l'attribut for cs type indemnite
     * 
     * @return la valeur courante de l'attribut for cs type indemnite
     */
    public String getForCsTypeIndemnite() {
        return forCsTypeIndemnite;
    }

    /**
     * getter pour l'attribut for id IJIJCalculee
     * 
     * @return la valeur courante de l'attribut for id IJIJCalculee
     */
    public String getForIdIJCalculee() {
        return forIdIJCalculee;
    }

    /**
     * getter pour l'attribut order by defaut
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return IJIndemniteJournaliere.FIELDNAME_ID_INDEMNITE_JOURNALIERE;
    }

    /**
     * setter pour l'attribut for cs type indemnite
     * 
     * @param forCsTypeIndemnite
     *            une nouvelle valeur pour cet attribut
     */
    public void setForCsTypeIndemnite(String forCsTypeIndemnite) {
        this.forCsTypeIndemnite = forCsTypeIndemnite;
    }

    /**
     * setter pour l'attribut for id IJIJCalculee
     * 
     * @param forIdIJCalculee
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdIJCalculee(String forIdIJCalculee) {
        this.forIdIJCalculee = forIdIJCalculee;
    }
}
