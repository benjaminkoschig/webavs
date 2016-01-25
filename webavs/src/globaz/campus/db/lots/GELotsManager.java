package globaz.campus.db.lots;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author acr
 */

public class GELotsManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = null;
    private String forCsEtatLot = null;
    private String forIdLot = null;
    private String forIdTiersEcole = null;
    private String forLibelleTraitementLike = null;
    private String fromDateReceptionLot = null;
    private String orderBy = GELots.FIELDNAME_DATE_RECEPTION + " DESC, " + GELots.FIELDNAME_ID_LOT + " DESC ";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + GELots.TABLE_NAME_LOT;
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return orderBy;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        if (!JadeStringUtil.isBlank(getForIdLot())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GELots.FIELDNAME_ID_LOT + "=" + _dbWriteNumeric(statement.getTransaction(), getForIdLot());
        }
        if (!JadeStringUtil.isBlank(getFromDateReceptionLot())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GELots.FIELDNAME_DATE_RECEPTION + ">="
                    + _dbWriteDateAMJ(statement.getTransaction(), getFromDateReceptionLot());
        }
        if (!JadeStringUtil.isBlank(getForCsEtatLot())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GELots.FIELDNAME_ETAT_LOT + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForCsEtatLot());
        }
        if (!JadeStringUtil.isBlank(getForLibelleTraitementLike())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += "UPPER (" + GELots.FIELDNAME_LIBELLE_TRAITEMENT + ") like UPPER ("
                    + _dbWriteString(statement.getTransaction(), "%" + getForLibelleTraitementLike() + "%") + ")";
        }
        if (!JadeStringUtil.isBlank(getForAnnee())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GELots.FIELDNAME_ANNEE + "=" + _dbWriteNumeric(statement.getTransaction(), getForAnnee());
        }
        if (!JadeStringUtil.isBlank(getForIdTiersEcole())) {
            if (!JadeStringUtil.isBlank(sqlWhere)) {
                sqlWhere += " AND ";
            }
            sqlWhere += GELots.FIELDNAME_ID_TIERS_ECOLE + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdTiersEcole());
        }
        return sqlWhere;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new GELots();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForCsEtatLot() {
        return forCsEtatLot;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    public String getForIdTiersEcole() {
        return forIdTiersEcole;
    }

    public String getForLibelleTraitementLike() {
        return forLibelleTraitementLike;
    }

    public String getFromDateReceptionLot() {
        return fromDateReceptionLot;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForCsEtatLot(String forCsEtatLot) {
        this.forCsEtatLot = forCsEtatLot;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    public void setForIdTiersEcole(String forIdTiersEcole) {
        this.forIdTiersEcole = forIdTiersEcole;
    }

    public void setForLibelleTraitementLike(String forLibelleTraitementLike) {
        this.forLibelleTraitementLike = forLibelleTraitementLike;
    }

    public void setFromDateReceptionLot(String fromDateReceptionLot) {
        this.fromDateReceptionLot = fromDateReceptionLot;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

}
