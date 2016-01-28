package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class LienAffiliationSearchSimpleModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forChildAffiliationId;
    private String forDateLien;
    private String forParentAffiliationId;
    private String forTypeLien;

    public String getForChildAffiliationId() {
        return forChildAffiliationId;
    }

    public String getForDateLien() {
        return forDateLien;
    }

    public String getForParentAffiliationId() {
        return forParentAffiliationId;
    }

    public String getForTypeLien() {
        return forTypeLien;
    }

    public void setForChildAffiliationId(String forChildAffiliationId) {
        this.forChildAffiliationId = forChildAffiliationId;
    }

    public void setForDateLien(String forDateLien) {
        this.forDateLien = forDateLien;
    }

    public void setForParentAffiliationId(String forParentAffiliationId) {
        this.forParentAffiliationId = forParentAffiliationId;
    }

    public void setForTypeLien(String forTypeLien) {
        this.forTypeLien = forTypeLien;
    }

    @Override
    public Class whichModelClass() {
        return LienAffiliationSimpleModel.class;
    }

}
