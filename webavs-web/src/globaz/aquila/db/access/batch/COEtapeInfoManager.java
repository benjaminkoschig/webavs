package globaz.aquila.db.access.batch;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class COEtapeInfoManager extends COEtapeInfoConfigManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final long serialVersionUID = -6981555076044900606L;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String forIdEtapeInfoConfig;
    private String forIdHistorique;
    private boolean leftJoin = false;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param statement
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFields(BStatement statement) {
        return COEtapeInfo.createFieldsClause(_getCollection());
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return COEtapeInfo.createFromClause(_getCollection(), leftJoin);
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer where = new StringBuffer(super._getWhere(statement));

        if (!JadeStringUtil.isEmpty(forIdHistorique)) {
            if (where.length() > 0) {
                where.append(" AND ");
            }

            where.append(COEtapeInfo.FNAME_IDHISTORIQUE);
            where.append("=");
            where.append(this._dbWriteNumeric(statement.getTransaction(), forIdHistorique));
        }

        if (!JadeStringUtil.isEmpty(forIdEtapeInfoConfig)) {
            if (where.length() > 0) {
                where.append(" AND ");
            }

            where.append(_getCollection());
            where.append(COEtapeInfo.TABLE_NAME_VALEUR);
            where.append(".");
            where.append(COEtapeInfoConfig.FNAME_IDETAPEINFOCONFIG);
            where.append("=");
            where.append(this._dbWriteNumeric(statement.getTransaction(), forIdEtapeInfoConfig));
        }

        return where.toString();
    }

    /**
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new COEtapeInfo();
    }

    /**
     * getter pour l'attribut for id etape info config.
     * 
     * @return la valeur courante de l'attribut for id etape info config
     */
    public String getForIdEtapeInfoConfig() {
        return forIdEtapeInfoConfig;
    }

    /**
     * getter pour l'attribut for id historique.
     * 
     * @return la valeur courante de l'attribut for id historique
     */
    public String getForIdHistorique() {
        return forIdHistorique;
    }

    public boolean isLeftJoin() {
        return leftJoin;
    }

    /**
     * setter pour l'attribut for id etape info config.
     * 
     * @param forIdEtapeInfoConfig
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdEtapeInfoConfig(String forIdEtapeInfoConfig) {
        this.forIdEtapeInfoConfig = forIdEtapeInfoConfig;
    }

    /**
     * setter pour l'attribut for id historique.
     * 
     * @param forIdHistorique
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdHistorique(String forIdHistorique) {
        this.forIdHistorique = forIdHistorique;
    }

    public void setLeftJoin(boolean b) {
        leftJoin = b;
    }

}
