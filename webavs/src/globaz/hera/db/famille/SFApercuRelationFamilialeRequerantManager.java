/*
 * Créé le 8 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.db.famille;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * DOCUMENT ME!
 * </p>
 * 
 * @author jpa
 * 
 *         <p>
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 *         </p>
 */
public class SFApercuRelationFamilialeRequerantManager extends SFMembreFamilleManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdRequerant = new String();
    private String orderBy = null;
    private Boolean wantFamilleRequerant = new Boolean(false);

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer from = new StringBuffer(SFApercuRelationFamilialeRequerant.createFromClause(_getCollection(),
                getForIdRequerant()));

        if ((!JadeStringUtil.isBlankOrZero(getlikeNumeroAvsNNSS()) && !JadeStringUtil.isEmpty(getLikeNumeroAvs()))) {
            from.append(" LEFT JOIN " + _getDbSchema() + "." + SFMembreFamille.TABLE_AVS_HIST + " AS "
                    + ALIAS_TI_AVS_HIST + " ON (" + ALIAL_TI_AVS + "." + SFMembreFamille.FIELD_TI_IDTIERS + " = "
                    + ALIAS_TI_AVS_HIST + "." + SFMembreFamille.FIELD_TI_IDTIERS + ")");
        }

        return from.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        if (orderBy == null) {
            return super._getOrder(statement);
        } else {
            if ((!JadeStringUtil.isBlankOrZero(getlikeNumeroAvsNNSS()) && !JadeStringUtil.isEmpty(getLikeNumeroAvs()))) {
                return orderBy + SECONDARY_ORDER_BY;
            } else {
                return orderBy;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
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
        String sqlWhere = super._getWhere(statement);

        if (!JadeStringUtil.isIntegerEmpty(getForIdRequerant())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += SFRelationFamilialeRequerant.TABLE_NAME + "." + SFRelationFamilialeRequerant.FIELD_IDREQUERANT
                    + " = " + _dbWriteNumeric(statement.getTransaction(), getForIdRequerant());
        }

        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
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
        return new SFApercuRelationFamilialeRequerant();
    }

    /**
     * @return
     */
    public String getForIdRequerant() {
        return forIdRequerant;
    }

    /**
     * @return
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * sans effet sur la requette
     * 
     * @return
     */
    // devrait être mis dans un ListViewBEan
    public Boolean getWantFamilleRequerant() {
        return wantFamilleRequerant;
    }

    /**
     * getter pour l'attribut want famille requerant, sans effet sur la requette
     * 
     * @return la valeur courante de l'attribut want famille requerant
     */
    // devrait être mis dans un ListViewBEan
    public boolean isWantFamilleRequerant() {
        return wantFamilleRequerant.booleanValue();
    }

    /**
     * @param string
     */
    public void setForIdRequerant(String string) {
        forIdRequerant = string;
    }

    /**
     * @param string
     */
    public void setOrderBy(String string) {
        orderBy = string;
    }

    /**
     * Setter sans effet sur la requête
     * 
     * @param boolean1
     */
    // devrait être mis dans un ListViewBEan
    public void setWantFamilleRequerant(Boolean boolean1) {
        wantFamilleRequerant = boolean1;
    }

}
