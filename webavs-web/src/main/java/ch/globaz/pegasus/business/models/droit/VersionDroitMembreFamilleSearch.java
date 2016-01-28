package ch.globaz.pegasus.business.models.droit;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class VersionDroitMembreFamilleSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsRoletMembreFamille = null;
    private String forIdVersionDroit = null;

    public String getForCsRoletMembreFamille() {
        return forCsRoletMembreFamille;
    }

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public void setForCsRoletMembreFamille(String forCsRoletMembreFamille) {
        this.forCsRoletMembreFamille = forCsRoletMembreFamille;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    @Override
    public Class<VersionDroitMembreFamille> whichModelClass() {
        return VersionDroitMembreFamille.class;
    }

}
