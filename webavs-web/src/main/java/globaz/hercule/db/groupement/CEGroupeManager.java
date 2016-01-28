package globaz.hercule.db.groupement;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author JMC
 * @since 2 juin 2010
 */
public class CEGroupeManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdGroupe = "";
    private String forLibelle = "";
    private String likeLibelle = "";

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return CEGroupe.FIELD_LIBELLE + " ASC ";
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isEmpty(getForIdGroupe())) {
            CEUtils.sqlAddCondition(sqlWhere, CEGroupe.FIELD_IDGROUPE + " = " + getForIdGroupe());
        }

        if (!JadeStringUtil.isEmpty(getLikeLibelle())) {
            CEUtils.sqlAddCondition(sqlWhere, CEGroupe.FIELD_LIBELLE + " like '" + getLikeLibelle() + "%'");
        }

        if (!JadeStringUtil.isEmpty(getForLibelle())) {
            CEUtils.sqlAddCondition(sqlWhere, CEGroupe.FIELD_LIBELLE + " = '" + getForLibelle() + "'");
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEGroupe();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForIdGroupe() {
        return forIdGroupe;
    }

    public String getForLibelle() {
        return forLibelle;
    }

    public String getLikeLibelle() {
        return likeLibelle;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForIdGroupe(String forIdGroupe) {
        this.forIdGroupe = forIdGroupe;
    }

    public void setForLibelle(String forLibelle) {
        this.forLibelle = forLibelle;
    }

    public void setLikeLibelle(String likeLibelle) {
        this.likeLibelle = likeLibelle;
    }
}
