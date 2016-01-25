package globaz.hercule.db.couverture;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author SCO
 * @since SCO 1 juin 2010
 */
public class CECouvertureManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdAffilie;
    private String forIdCourveture;
    private String forNumAffilie;
    // cette id couverture
    private Boolean isActif;
    private String notForIdCouverture; // Permet d'exclure l'occurence avec

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + CECouverture.TABLE_CECOUVP;
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(getForIdCourveture())) {
            CEUtils.sqlAddCondition(
                    sqlWhere,
                    CECouverture.FIELD_IDCOUVERTURE + "="
                            + _dbWriteNumeric(statement.getTransaction(), getForIdCourveture()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdAffilie())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    CECouverture.FIELD_IDAFFILIE + "=" + _dbWriteNumeric(statement.getTransaction(), getForIdAffilie()));
        }

        if (!JadeStringUtil.isBlankOrZero(getForNumAffilie())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    CECouverture.FIELD_NUMAFFILIE + "="
                            + _dbWriteString(statement.getTransaction(), getForNumAffilie()));
        }

        if (getIsActif() != null) {
            CEUtils.sqlAddCondition(sqlWhere, CECouverture.FIELD_COUVERTUREACTIVE + "="
                    + (getIsActif().booleanValue() ? "'1'" : "'2'"));
        }

        if (!JadeStringUtil.isBlankOrZero(getNotForIdCouverture())) {
            CEUtils.sqlAddCondition(
                    sqlWhere,
                    CECouverture.FIELD_IDCOUVERTURE + "<>"
                            + _dbWriteNumeric(statement.getTransaction(), getNotForIdCouverture()));
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CECouverture();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForIdAffilie() {
        return forIdAffilie;
    }

    public String getForIdCourveture() {
        return forIdCourveture;
    }

    public String getForNumAffilie() {
        return forNumAffilie;
    }

    public Boolean getIsActif() {
        return isActif;
    }

    public String getNotForIdCouverture() {
        return notForIdCouverture;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForIdAffilie(String forIdAffilie) {
        this.forIdAffilie = forIdAffilie;
    }

    public void setForIdCourveture(String forIdCourveture) {
        this.forIdCourveture = forIdCourveture;
    }

    public void setForNumAffilie(String forNumAffilie) {
        this.forNumAffilie = forNumAffilie;
    }

    public void setIsActif(Boolean isActif) {
        this.isActif = isActif;
    }

    public void setNotForIdCouverture(String notForIdCouverture) {
        this.notForIdCouverture = notForIdCouverture;
    }

}
