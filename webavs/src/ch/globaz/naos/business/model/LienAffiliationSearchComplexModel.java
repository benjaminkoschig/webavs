package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class LienAffiliationSearchComplexModel extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forChildNumeroAffilie;
    private String forDateLien;
    private String forParentNumeroAffilie;
    private String forTypeLien;

    public String getForChildNumeroAffilie() {
        return forChildNumeroAffilie;
    }

    public String getForDateLien() {
        return forDateLien;
    }

    public String getForParentNumeroAffilie() {
        return forParentNumeroAffilie;
    }

    public String getForTypeLien() {
        return forTypeLien;
    }

    public void setForChildNumeroAffilie(String forChildNumeroAffilie) {
        this.forChildNumeroAffilie = forChildNumeroAffilie;
    }

    public void setForDateLien(String forDateLien) {
        this.forDateLien = forDateLien;
    }

    public void setForParentNumeroAffilie(String forParentNumeroAffilie) {
        this.forParentNumeroAffilie = forParentNumeroAffilie;
    }

    public void setForTypeLien(String forTypeLien) {
        this.forTypeLien = forTypeLien;
    }

    @Override
    public Class whichModelClass() {
        return LienAffiliationComplexModel.class;
    }

}
