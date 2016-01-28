package ch.globaz.corvus.business.models.recapinfo;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleInfoRecapSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDate;
    private String forIdLot;
    private String inCode;

    public String getForDate() {
        return forDate;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    public String getInCode() {
        return inCode;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    public void setInCode(String inCode) {
        this.inCode = inCode;
    }

    @Override
    public Class<SimpleInfoRecap> whichModelClass() {
        return SimpleInfoRecap.class;
    }

}
