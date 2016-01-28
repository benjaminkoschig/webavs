/*
 * Créé le 24 oct. 05
 */
package globaz.apg.db.prestation;

import globaz.apg.db.droits.APEmployeur;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APRepartitionPaiementsJointEmployeurManager extends APRepartitionJointPrestationManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdParticularite = "";
    private Boolean forParentOnly;
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
            fromClause = APRepartitionPaiementsJointEmployeur.createFromClause(_getCollection());
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

            sqlWhere += _getCollection() + APEmployeur.TABLE_NAME + "." + APEmployeur.FIELDNAME_ID_PARTICULARITE + "="
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
        return new APRepartitionPaiementsJointEmployeur();
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
     * @return
     */
    public Boolean getForParentOnly() {
        return forParentOnly;
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

    /**
     * @param boolean1
     */
    public void setForParentOnly(Boolean boolean1) {
        forParentOnly = boolean1;
    }

}
