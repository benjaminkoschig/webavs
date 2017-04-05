package ch.globaz.amal.business.models.annoncesedexco;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class ComplexAnnonceSedexCODebiteursAssuresSearch extends JadeSearchComplexModel {
    private static final long serialVersionUID = 5867414265875259866L;
    private String forIdSedexCO = null;

    @Override
    public Class whichModelClass() {
        return ComplexAnnonceSedexCODebiteursAssures.class;
    }

    public String getForIdSedexCO() {
        return forIdSedexCO;
    }

    public void setForIdSedexCO(String forIdSedexCO) {
        this.forIdSedexCO = forIdSedexCO;
    }
}
