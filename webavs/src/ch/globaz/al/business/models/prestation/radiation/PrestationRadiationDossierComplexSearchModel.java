package ch.globaz.al.business.models.prestation.radiation;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class PrestationRadiationDossierComplexSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String SEARCH_DERNIERE_PRESTATION = "dernierePrestation";

    /** Recherche selon l'état du dossier */
    private String forEtatDossier = null;
    private String forIdDossier = null;
    private String forPeriodeA = null;

    public String getForEtatDossier() {
        return forEtatDossier;
    }

    public String getForIdDossier() {
        return forIdDossier;
    }

    public String getForPeriodeA() {
        return forPeriodeA;
    }

    public void setForEtatDossier(String forEtatDossier) {
        this.forEtatDossier = forEtatDossier;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    public void setForPeriodeA(String forPeriodeA) {
        this.forPeriodeA = forPeriodeA;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<PrestationRadiationDossierComplexModel> whichModelClass() {
        return PrestationRadiationDossierComplexModel.class;
    }

}
