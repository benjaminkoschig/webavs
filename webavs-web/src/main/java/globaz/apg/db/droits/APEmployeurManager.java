/*
 * Créé le 23 mai 05
 */
package globaz.apg.db.droits;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APEmployeurManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdAffilie = "";
    private String forIdParticularite = "";
    private String forIdTiers = "";

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
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + APEmployeur.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isEmpty(forIdAffilie)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APEmployeur.FIELDNAME_ID_AFFILIE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdAffilie);
        }

        if (!JadeStringUtil.isEmpty(forIdTiers)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APEmployeur.FIELDNAME_ID_TIERS + "=" + _dbWriteNumeric(statement.getTransaction(), forIdTiers);
        }

        if (!JadeStringUtil.isEmpty(forIdParticularite)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APEmployeur.FIELDNAME_ID_PARTICULARITE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdParticularite);
        }

        return sqlWhere;
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
        return new APEmployeur();
    }

    /**
     * getter pour l'attribut for id affilie
     * 
     * @return la valeur courante de l'attribut for id affilie
     */
    public String getForIdAffilie() {
        return forIdAffilie;
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
     * getter pour l'attribut for id tiers
     * 
     * @return la valeur courante de l'attribut for id tiers
     */
    public String getForIdTiers() {
        return forIdTiers;
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
        return APEmployeur.FIELDNAME_ID_EMPLOYEUR;
    }

    /**
     * setter pour l'attribut for id affilie
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdAffilie(String string) {
        forIdAffilie = string;
    }

    /**
     * setter pour l'attribut for id particulier
     * 
     * @param forIdParticularite
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdParticularite(String forIdParticularite) {
        this.forIdParticularite = forIdParticularite;
    }

    /**
     * setter pour l'attribut for id tiers
     * 
     * @param forIdTiers
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }
}
