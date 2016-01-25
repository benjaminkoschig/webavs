package ch.globaz.pegasus.business.models.calcul;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class CalculMembreFamilleSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsRoletMembreFamilleIn = null;
    private String forIdDroit = null;
    private String forIdDroitMembreFamille = null;

    public String getForCsRoletMembreFamilleIn() {
        return forCsRoletMembreFamilleIn;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public String getForIdDroitMembreFamille() {
        return forIdDroitMembreFamille;
    }

    public void setForCsRoletMembreFamilleIn(String forCsRoletMembreFamilleIn) {
        this.forCsRoletMembreFamilleIn = forCsRoletMembreFamilleIn;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForIdDroitMembreFamille(String forIdDroitMembreFamille) {
        this.forIdDroitMembreFamille = forIdDroitMembreFamille;
    }

    @Override
    public Class whichModelClass() {
        return CalculMembreFamille.class;
    }

}
