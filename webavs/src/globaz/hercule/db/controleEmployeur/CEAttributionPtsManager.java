package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

public class CEAttributionPtsManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean forActif = false;
    private String forDateDebut;
    private String forDateFin;
    private String forIdAttributionPts;
    private String forIdControle;
    private String forLastModification;
    private String forLastUser;
    private String forNotIdAttribution;
    private String forNumAffilie;
    private String likeNumAffilie;
    private String likeUser;
    private boolean orderByModification = false;

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + CEAttributionPts.TABLE_CEATTPTS;
    }

    /**
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        if (isOrderByModification()) {
            return " MPAPID DESC ";
        }

        return " MALNAF DESC ";
    }

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        StringBuffer sqlWhere = new StringBuffer();

        // traitement du positionnement
        if (!JadeStringUtil.isBlank(getLikeNumAffilie())) {
            CEUtils.sqlAddCondition(sqlWhere, "MALNAF like '" + getLikeNumAffilie() + "%'");
        }

        if (!JadeStringUtil.isBlank(getForNumAffilie())) {
            CEUtils.sqlAddCondition(sqlWhere, "MALNAF="
                    + _dbWriteString(statement.getTransaction(), getForNumAffilie()));
        }

        if (!JadeStringUtil.isBlankOrZero(getForIdControle())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "MDICON=" + _dbWriteNumeric(statement.getTransaction(), getForIdControle()));
        }

        if (!JadeStringUtil.isBlankOrZero(getForIdAttributionPts())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "MPAPID=" + _dbWriteNumeric(statement.getTransaction(), getForIdAttributionPts()));
        }

        if (!JadeStringUtil.isBlank(getForLastUser())) {
            CEUtils.sqlAddCondition(sqlWhere, "MPLUSR=" + _dbWriteNumeric(statement.getTransaction(), getForLastUser()));
        }

        if (!JadeStringUtil.isBlank(getLikeUser())) {
            CEUtils.sqlAddCondition(sqlWhere, "MPLUSR like '" + getLikeUser() + "%'");
        }

        if (!JadeStringUtil.isBlank(getForLastModification())) {
            CEUtils.sqlAddCondition(sqlWhere, "MPLMOD like '" + getForLastModification() + "%'");
        }

        if (!JadeStringUtil.isEmpty(getForDateDebut())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "MPPEDE = " + _dbWriteDateAMJ(statement.getTransaction(), getForDateDebut()));
        }

        if (!JadeStringUtil.isEmpty(getForDateFin())) {
            CEUtils.sqlAddCondition(sqlWhere, "MPPEFI = "
                    + _dbWriteDateAMJ(statement.getTransaction(), getForDateFin()));
        }

        if (isForActif()) {
            CEUtils.sqlAddCondition(sqlWhere, "CEBAAV = '1'");
        }

        if (!JadeStringUtil.isBlank(getForNotIdAttribution())) {
            CEUtils.sqlAddCondition(sqlWhere,
                    "MPAPID <> " + _dbWriteNumeric(statement.getTransaction(), getForNotIdAttribution()));
        }

        return sqlWhere.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEAttributionPts();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForIdAttributionPts() {
        return forIdAttributionPts;
    }

    public String getForIdControle() {
        return forIdControle;
    }

    public String getForLastModification() {
        return forLastModification;
    }

    public String getForLastUser() {
        return forLastUser;
    }

    public String getForNotIdAttribution() {
        return forNotIdAttribution;
    }

    public String getForNumAffilie() {
        return forNumAffilie;
    }

    public String getLikeNumAffilie() {
        return likeNumAffilie;
    }

    public String getLikeUser() {
        return likeUser;
    }

    public boolean isForActif() {
        return forActif;
    }

    public boolean isOrderByModification() {
        return orderByModification;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForActif(boolean forActif) {
        this.forActif = forActif;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForIdAttributionPts(String forIdAttributionPts) {
        this.forIdAttributionPts = forIdAttributionPts;
    }

    public void setForIdControle(String forIdControle) {
        this.forIdControle = forIdControle;
    }

    public void setForLastModification(String forLastModification) {
        this.forLastModification = forLastModification;
    }

    public void setForLastUser(String forLastUser) {
        this.forLastUser = forLastUser;
    }

    public void setForNotIdAttribution(String forNotIdAttribution) {
        this.forNotIdAttribution = forNotIdAttribution;
    }

    public void setForNumAffilie(String forNumAffilie) {
        this.forNumAffilie = forNumAffilie;
    }

    public void setLikeNumAffilie(String likeNumAffilie) {
        this.likeNumAffilie = likeNumAffilie;
    }

    public void setLikeUser(String likeUser) {
        this.likeUser = likeUser;
    }

    public void setOrderByModification(boolean orderByModification) {
        this.orderByModification = orderByModification;
    }

}
