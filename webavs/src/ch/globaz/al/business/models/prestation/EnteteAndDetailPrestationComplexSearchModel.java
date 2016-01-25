package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * Modèle de recherche pour les prestations et l'aggrégation de ses détails
 * 
 * @author AGE
 * 
 */
public class EnteteAndDetailPrestationComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Recherche par l'id dossier
     */
    private String forIdDossier = null;

    public String getForIdDossier() {
        return forIdDossier;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    @Override
    public Class whichModelClass() {
        return EnteteAndDetailPrestationComplexModel.class;
    }
}
