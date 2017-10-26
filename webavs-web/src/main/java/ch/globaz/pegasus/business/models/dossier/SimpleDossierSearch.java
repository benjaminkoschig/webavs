package ch.globaz.pegasus.business.models.dossier;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleDossierSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDemandePrestation = null;
    private String forIdDossier = null;

    /**
     * @return the forIdDemandePrestation
     */
    public String getForIdDemandePrestation() {
        return forIdDemandePrestation;
    }

    /**
     * @param forIdDemandePrestation
     *            the forIdDemandePrestation to set
     */
    public void setForIdDemandePrestation(String forIdDemandePrestation) {
        this.forIdDemandePrestation = forIdDemandePrestation;
    }

    public String getForIdDossier() {
        return forIdDossier;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleDossier.class;
    }

}
