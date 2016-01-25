package globaz.hercule.db.reviseur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

public class CEReviseurManager extends BManager implements Serializable {

    private static final long serialVersionUID = -6424923065533092073L;
    private Boolean findOnlyActif = false;
    private String forIdReviseur;
    private String forVisa;
    private String likeVisa;

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CEReviseur.TABLE_CEREVIP;
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return CEReviseur.FIELD_VISA + " ASC";
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isEmpty(getForIdReviseur())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    CEReviseur.FIELD_IDREVISEUR + " = "
                            + _dbWriteString(statement.getTransaction(), getForIdReviseur()));
        }
        if (!JadeStringUtil.isEmpty(getForVisa())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    CEReviseur.FIELD_VISA + " = " + _dbWriteString(statement.getTransaction(), getForVisa()));
        }
        if (!JadeStringUtil.isEmpty(getLikeVisa())) {
            CEUtils.sqlAddCondition(
                    sqlWhere,
                    "(" + CEReviseur.FIELD_VISA + " LIKE "
                            + _dbWriteString(statement.getTransaction(), getLikeVisa().toLowerCase() + "%") + " OR "
                            + CEReviseur.FIELD_VISA + " LIKE "
                            + _dbWriteString(statement.getTransaction(), getLikeVisa().toUpperCase() + "%") + " OR "
                            + CEReviseur.FIELD_VISA + " LIKE "
                            + _dbWriteString(statement.getTransaction(), getLikeVisa() + "%") + ")");
        }

        if (getFindOnlyActif()) {
            CEUtils.sqlAddCondition(sqlWhere, CEReviseur.FIELD_REVISEURACTIF + " = '1'");
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEReviseur();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public Boolean getFindOnlyActif() {
        return findOnlyActif;
    }

    public String getForIdReviseur() {
        return forIdReviseur;
    }

    public String getForVisa() {
        return forVisa;
    }

    public String getLikeVisa() {
        return likeVisa;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setFindOnlyActif(Boolean findOnlyActif) {
        this.findOnlyActif = findOnlyActif;
    }

    public void setForIdReviseur(String string) {
        forIdReviseur = string;
    }

    public void setForVisa(String string) {
        forVisa = string;
    }

    public void setLikeVisa(String string) {
        likeVisa = string;
    }
}
