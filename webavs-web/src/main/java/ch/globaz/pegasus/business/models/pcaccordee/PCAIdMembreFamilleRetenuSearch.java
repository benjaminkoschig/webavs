package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author BSC
 * 
 */
public class PCAIdMembreFamilleRetenuSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdVersionDroit = null;
    private Boolean isPlanRetenu = null;

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public Boolean getIsPlanRetenu() {
        return isPlanRetenu;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    public void setIsPlanRetenu(Boolean isPlanRetenu) {
        this.isPlanRetenu = isPlanRetenu;
    }

    @Override
    public Class<PCAIdMembreFamilleRetenu> whichModelClass() {
        return PCAIdMembreFamilleRetenu.class;
    }

}
