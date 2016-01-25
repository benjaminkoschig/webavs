/*
 * Créé le 8 sept. 05
 */
package globaz.ij.db.prestations;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJCotisationManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdRepartitionPaiements = "";
    private String notForIdCotisation = "";

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

        if (!JadeStringUtil.isIntegerEmpty(forIdRepartitionPaiements)) {
            if (whereClause.length() != 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJCotisation.FIELDNAME_IDREPARTITIONPAIEMENTS);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(transaction, forIdRepartitionPaiements));
        }

        if (!JadeStringUtil.isIntegerEmpty(notForIdCotisation)) {
            if (whereClause.length() != 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(IJCotisation.FIELDNAME_IDCOTISATION);
            whereClause.append("<>");
            whereClause.append(_dbWriteNumeric(transaction, notForIdCotisation));
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
        return new IJCotisation();
    }

    /**
     * getter pour l'attribut for id repartition paiements
     * 
     * @return la valeur courante de l'attribut for id repartition paiements
     */
    public String getForIdRepartitionPaiements() {
        return forIdRepartitionPaiements;
    }

    /**
     * getter pour l'attribut not for id cotisation
     * 
     * @return la valeur courante de l'attribut not for id cotisation
     */
    public String getNotForIdCotisation() {
        return notForIdCotisation;
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
        return IJCotisation.FIELDNAME_IDCOTISATION;
    }

    /**
     * setter pour l'attribut for id repartition paiements
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdRepartitionPaiements(String string) {
        forIdRepartitionPaiements = string;
    }

    /**
     * setter pour l'attribut not for id cotisation
     * 
     * @param notForIdCotisation
     *            une nouvelle valeur pour cet attribut
     */
    public void setNotForIdCotisation(String notForIdCotisation) {
        this.notForIdCotisation = notForIdCotisation;
    }
}
