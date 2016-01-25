package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeComplexModel;

public class AdhesionCotisationAssuranceComplexModel extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AdhesionSimpleModel adhesion;
    private AssuranceSimpleModel assurance;
    private CotisationSimpleModel cotisation;

    public AdhesionCotisationAssuranceComplexModel() {
        adhesion = new AdhesionSimpleModel();
        assurance = new AssuranceSimpleModel();
        cotisation = new CotisationSimpleModel();
    }

    public AdhesionSimpleModel getAdhesion() {
        return adhesion;
    }

    public AssuranceSimpleModel getAssurance() {
        return assurance;
    }

    public CotisationSimpleModel getCotisation() {
        return cotisation;
    }

    @Override
    public String getId() {
        return adhesion.getId();
    }

    @Override
    public String getSpy() {
        return adhesion.getSpy();
    }

    public void setAdhesion(AdhesionSimpleModel adhesion) {
        this.adhesion = adhesion;
    }

    public void setAssurance(AssuranceSimpleModel assurance) {
        this.assurance = assurance;
    }

    public void setCotisation(CotisationSimpleModel cotisation) {
        this.cotisation = cotisation;
    }

    @Override
    public void setId(String id) {
        adhesion.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        adhesion.setSpy(spy);
    }

}
