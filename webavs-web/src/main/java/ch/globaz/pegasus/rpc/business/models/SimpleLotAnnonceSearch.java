package ch.globaz.pegasus.rpc.business.models;

import ch.globaz.common.persistence.DomaineJadeAbstractSearchModel;

public class SimpleLotAnnonceSearch extends DomaineJadeAbstractSearchModel {

    public static final String ORDER_BY_DATEENVOI_DESC = "OrderByDateEnvoiDESC";
    public static final String ORDER_BY_IDLOT_DESC = "OrderByIdLotDESC";
    private String forDate;
    private String forStartDate;
    private String forEndDate;

    public String getForDate() {
        return forDate;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public String getForStartDate() {
        return forStartDate;
    }

    public void setForStartDate(String forStartDate) {
        this.forStartDate = forStartDate;
    }

    public String getForEndDate() {
        return forEndDate;
    }

    public void setForEndDate(String forEndDate) {
        this.forEndDate = forEndDate;
    }

    @Override
    public Class<SimpleLotAnnonce> whichModelClass() {
        return SimpleLotAnnonce.class;
    }

}
