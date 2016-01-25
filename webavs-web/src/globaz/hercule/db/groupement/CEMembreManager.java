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
public class CEMembreManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdAffiliation = "";
    private String forIdGroupe = "";

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(getForIdAffiliation())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    _getCollection() + CEMembre.TABLE_CEMEMP + "." + CEMembre.FIELD_IDAFFILIATION + "="
                            + _dbWriteNumeric(statement.getTransaction(), getForIdAffiliation()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdGroupe())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    CEMembre.FIELD_IDGROUPE + "=" + _dbWriteNumeric(statement.getTransaction(), getForIdGroupe()));
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEMembre();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForIdAffiliation() {
        return forIdAffiliation;
    }

    public String getForIdGroupe() {
        return forIdGroupe;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForIdAffiliation(String forIdAffiliation) {
        this.forIdAffiliation = forIdAffiliation;
    }

    public void setForIdGroupe(String forIdGroupe) {
        this.forIdGroupe = forIdGroupe;
    }
}
