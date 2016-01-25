/*
 * Créé le 24 oct. 05
 */
package globaz.ij.db.prestations;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.ij.db.prononces.IJEmployeur;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJRepartitionPaiementsJointEmployeurManager extends IJRepartitionJointPrestationManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdParticularite = "";
    private String fromClause = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        if (fromClause == null) {
            fromClause = IJRepartitionPaiementsJointEmployeur.createFromClause(_getCollection());
        }

        return fromClause;
    }

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
        String sqlWhere = super._getWhere(statement);

        if (!JadeStringUtil.isIntegerEmpty(forIdParticularite)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + IJEmployeur.TABLE_NAME + "." + IJEmployeur.FIELDNAME_ID_PARTICULARITE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdParticularite);
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
        return new IJRepartitionPaiementsJointEmployeur();
    }

    /**
     * getter pour l'attribut for id particulier
     * 
     * @return la valeur courante de l'attribut for id particulier
     */
    public String getForIdParticularite() {
        return forIdParticularite;
    }

    /**
     * setter pour l'attribut for id particulier
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdParticularite(String string) {
        forIdParticularite = string;
    }
}
