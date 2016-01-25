package globaz.hercule.db.couverture;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;

public class CECouvertureEcranManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeCouverture;
    private String forIdAffilie;
    private String forIdCouverture;
    private String forNumAffilie;
    private Boolean isActif = new Boolean(false);
    private String likeNumAffilie;

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return " aff.MALNAF";
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        if (!JadeStringUtil.isIntegerEmpty(getForIdCouverture())) {
            CEUtils.sqlAddCondition(
                    sqlWhere,
                    CECouvertureEcran.FIELD_IDCOUVERTURE + "="
                            + _dbWriteNumeric(statement.getTransaction(), getForIdCouverture()));
        }

        if (!JadeStringUtil.isIntegerEmpty(getForIdAffilie())) {
            CEUtils.sqlAddCondition(
                    sqlWhere,
                    "aff." + CECouvertureEcran.FIELD_IDAFFILIE + "="
                            + _dbWriteNumeric(statement.getTransaction(), getForIdAffilie()));
        }

        if (!JadeStringUtil.isBlankOrZero(getForNumAffilie())) {
            CEUtils.sqlAddCondition(
                    sqlWhere,
                    "aff." + CECouvertureEcran.FIELD_NUMAFFILIE + "="
                            + _dbWriteString(statement.getTransaction(), getForNumAffilie()));
        }

        if (!JadeStringUtil.isBlankOrZero(getLikeNumAffilie())) {
            CEUtils.sqlAddCondition(sqlWhere, "aff." + CECouvertureEcran.FIELD_NUMAFFILIE + " LIKE '%"
                    + getLikeNumAffilie() + "%'");
        }

        if (getIsActif().booleanValue()) {
            CEUtils.sqlAddCondition(sqlWhere, CECouverture.FIELD_COUVERTUREACTIVE + "=" + "'1'");
        }

        if (!JadeStringUtil.isBlankOrZero(getForAnneeCouverture())) {
            CEUtils.sqlAddCondition(
                    sqlWhere,
                    CECouvertureEcran.FIELD_ANNEE + "="
                            + _dbWriteNumeric(statement.getTransaction(), getForAnneeCouverture()));
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CECouvertureEcran();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForAnneeCouverture() {
        return forAnneeCouverture;
    }

    public String getForIdAffilie() {
        return forIdAffilie;
    }

    public String getForIdCouverture() {
        return forIdCouverture;
    }

    public String getForNumAffilie() {
        return forNumAffilie;
    }

    public Boolean getIsActif() {
        return isActif;
    }

    public String getLikeNumAffilie() {
        return likeNumAffilie;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForAnneeCouverture(String forAnneeCouverture) {
        this.forAnneeCouverture = forAnneeCouverture;
    }

    public void setForIdAffilie(String forIdAffilie) {
        this.forIdAffilie = forIdAffilie;
    }

    public void setForIdCouverture(String _forIdCouverture) {
        forIdCouverture = _forIdCouverture;
    }

    public void setForNumAffilie(String forNumAffilie) {
        this.forNumAffilie = forNumAffilie;
    }

    public void setIsActif(Boolean isActif) {
        this.isActif = isActif;
    }

    public void setLikeNumAffilie(String likeNumAffilie) {
        this.likeNumAffilie = likeNumAffilie;
    }

}
