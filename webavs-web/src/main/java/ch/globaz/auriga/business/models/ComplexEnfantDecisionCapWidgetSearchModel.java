package ch.globaz.auriga.business.models;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class ComplexEnfantDecisionCapWidgetSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDesignation1Like;
    private String forDesignation2Like;
    private String forIdTiersParent;
    private String forNumeroAvsActuel;
    private String forTypeCompositionTiers;

    public String getForDesignation1Like() {
        return forDesignation1Like;
    }

    public String getForDesignation2Like() {
        return forDesignation2Like;
    }

    public String getForIdTiersParent() {
        return forIdTiersParent;
    }

    public String getForNumeroAvsActuel() {
        return forNumeroAvsActuel;
    }

    public String getForTypeCompositionTiers() {
        return forTypeCompositionTiers;
    }

    public void setForDesignation1Like(String forDesignation1Like) {
        this.forDesignation1Like = forDesignation1Like;
    }

    public void setForDesignation2Like(String forDesignation2Like) {
        this.forDesignation2Like = forDesignation2Like;
    }

    public void setForIdTiersParent(String forIdTiersParent) {
        this.forIdTiersParent = forIdTiersParent;
    }

    public void setForNumeroAvsActuel(String forNumeroAvsActuel) {
        this.forNumeroAvsActuel = forNumeroAvsActuel;
    }

    public void setForTypeCompositionTiers(String forTypeCompositionTiers) {
        this.forTypeCompositionTiers = forTypeCompositionTiers;
    }

    @Override
    public Class<ComplexEnfantDecisionCapWidget> whichModelClass() {
        return ComplexEnfantDecisionCapWidget.class;
    }

}
