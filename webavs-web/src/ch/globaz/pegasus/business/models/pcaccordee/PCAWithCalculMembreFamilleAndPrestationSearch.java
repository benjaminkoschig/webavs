package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class PCAWithCalculMembreFamilleAndPrestationSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String WITH_DATE_DE_FIN_IS_NULL = "withDateDeFinIsNull";
    public final static String WITH_PCA_VALIDE_PLAN_CALCUL_RETENU_AND_DATE_VALABLE = "withPCAValidePlanClaculeRetenuAndDateValable";

    private String forCsEtatPcAccordee = null;
    private String forDateValable = null;
    private String forIdPca = null;
    private String forIdTiersMembreFamille = null;
    private String forIdVersionDroit = null;
    private Boolean isPlanRetenu = null;

    public String getForCsEtatPcAccordee() {
        return forCsEtatPcAccordee;
    }

    public String getForDateValable() {
        return forDateValable;
    }

    public String getForIdPca() {
        return forIdPca;
    }

    public String getForIdTiersMembreFamille() {
        return forIdTiersMembreFamille;
    }

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public Boolean getIsPlanRetenu() {
        return isPlanRetenu;
    }

    public void setForCsEtatPcAccordee(String forCsEtatPcAccordee) {
        this.forCsEtatPcAccordee = forCsEtatPcAccordee;
    }

    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
    }

    public void setForIdPca(String forIdPca) {
        this.forIdPca = forIdPca;
    }

    public void setForIdTiersMembreFamille(String forIdTiersMembreFamille) {
        this.forIdTiersMembreFamille = forIdTiersMembreFamille;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    public void setIsPlanRetenu(Boolean isPlanRetenu) {
        this.isPlanRetenu = isPlanRetenu;
    }

    @Override
    public Class<PCAWithCalculMembreFamilleAndPrestation> whichModelClass() {
        return PCAWithCalculMembreFamilleAndPrestation.class;
    }

}
