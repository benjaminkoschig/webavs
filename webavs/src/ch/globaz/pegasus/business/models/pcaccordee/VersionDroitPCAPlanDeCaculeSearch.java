package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class VersionDroitPCAPlanDeCaculeSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FOR_DATE_VALABLE_AND_NOT_CS_ETAT_PLAN_CLACULE = "withDateValableAndNotPlanClacule";
    private String forCsEtatPca = null;
    private String forCsEtatPlanDeCalcule = null;
    private String forCsEtatVersionDroit = null;
    private String forDateValable = null;
    private Boolean forIsPlanRetenu = false;

    public String getForCsEtatPca() {
        return forCsEtatPca;
    }

    public String getForCsEtatPlanDeCalcule() {
        return forCsEtatPlanDeCalcule;
    }

    public String getForCsEtatVersionDroit() {
        return forCsEtatVersionDroit;
    }

    public String getForDateValable() {
        return forDateValable;
    }

    public Boolean getForIsPlanRetenu() {
        return forIsPlanRetenu;
    }

    public void setForCsEtatPca(String forCsEtatPca) {
        this.forCsEtatPca = forCsEtatPca;
    }

    public void setForCsEtatPlanDeCalcule(String forCsEtatPlanDeCalcule) {
        this.forCsEtatPlanDeCalcule = forCsEtatPlanDeCalcule;
    }

    public void setForCsEtatVersionDroit(String forCsEtatVersionDroit) {
        this.forCsEtatVersionDroit = forCsEtatVersionDroit;
    }

    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
    }

    public void setForIsPlanRetenu(Boolean forIsPlanRetenu) {
        this.forIsPlanRetenu = forIsPlanRetenu;
    }

    @Override
    public Class<VersionDroitPCAPlanDeCacule> whichModelClass() {
        return VersionDroitPCAPlanDeCacule.class;
    }

}
