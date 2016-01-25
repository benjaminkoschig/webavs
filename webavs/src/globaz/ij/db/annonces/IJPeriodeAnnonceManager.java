/*
 * Créé le 8 sept. 05
 */
package globaz.ij.db.annonces;

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
public class IJPeriodeAnnonceManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdAnnonce = "";

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

        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(forIdAnnonce)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += IJPeriodeAnnonce.FIELDNAME_IDANNONCE + "=" + _dbWriteNumeric(transaction, forIdAnnonce);
        }

        return sqlWhere;
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
        return new IJPeriodeAnnonce();
    }

    /**
     * getter pour l'attribut for id annonce
     * 
     * @return la valeur courante de l'attribut for id annonce
     */
    public String getForIdAnnonce() {
        return forIdAnnonce;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.annonces.PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return IJPeriodeAnnonce.FIELDNAME_IDPERIODEANNONCE;
    }

    /**
     * setter pour l'attribut for id annonce
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdAnnonce(String string) {
        forIdAnnonce = string;
    }
}
