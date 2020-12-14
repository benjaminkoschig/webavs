package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSearchComplexModel;

import java.util.List;

public class PCAccordeePlanCalculReformeSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDroit = null;
    private String forNoVersion = null;
    private Boolean forIsPlanRetenu = null;


    public String getForIdDroit() {
        return forIdDroit;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public Boolean getForIsPlanRetenu() {
        return forIsPlanRetenu;
    }

    public void setForIsPlanRetenu(Boolean forIsPlanRetenu) {
        this.forIsPlanRetenu = forIsPlanRetenu;
    }

    public String getForNoVersion() {
        return forNoVersion;
    }

    public void setForNoVersion(String forNoVersion) {
        this.forNoVersion = forNoVersion;
    }

    @Override
    public Class<PCAccordeePlanCalculReforme> whichModelClass() {
        return PCAccordeePlanCalculReforme.class;
    }

}
