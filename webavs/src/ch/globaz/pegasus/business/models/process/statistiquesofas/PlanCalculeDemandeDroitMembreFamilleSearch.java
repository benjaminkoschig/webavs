package ch.globaz.pegasus.business.models.process.statistiquesofas;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class PlanCalculeDemandeDroitMembreFamilleSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdPCAccordee = null;
    private Boolean isComprisDansCalcul = null;
    private Boolean isPlanRetenu = null;

    public String getForIdPCAccordee() {
        return forIdPCAccordee;
    }

    public Boolean getIsComprisDansCalcul() {
        return isComprisDansCalcul;
    }

    public Boolean getIsPlanRetenu() {
        return isPlanRetenu;
    }

    public void setForIdPCAccordee(String forIdPCAccordee) {
        this.forIdPCAccordee = forIdPCAccordee;
    }

    public void setIsComprisDansCalcul(Boolean isComprisDansCalcul) {
        this.isComprisDansCalcul = isComprisDansCalcul;
    }

    public void setIsPlanRetenu(Boolean isPlanRetenu) {
        this.isPlanRetenu = isPlanRetenu;
    }

    @Override
    public Class<PlanCalculeDemandeDroitMembreFamille> whichModelClass() {
        return PlanCalculeDemandeDroitMembreFamille.class;
    }

}
