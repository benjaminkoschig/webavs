package ch.globaz.vulpecula.external.models;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.naos.business.model.AssuranceSimpleModel;
import ch.globaz.naos.business.model.CotisationSimpleModel;
import ch.globaz.naos.business.model.ParametreAssuranceSimpleModel;

/**
 * @author JPA créé le 30.09.2014
 * 
 *         Complexe Model des cotisations, assurances et paramètres des assurances
 */
public class CotisationParametreComplexModel extends JadeComplexModel {

    private static final long serialVersionUID = -608611053121830671L;
    private CotisationSimpleModel cotisationSimpleModel;
    private AssuranceSimpleModel assuranceSimpleModel;
    private ParametreAssuranceSimpleModel parametreAssuranceSimpleModel;

    public ParametreAssuranceSimpleModel getParametreAssuranceSimpleModel() {
        return parametreAssuranceSimpleModel;
    }

    public void setParametreAssuranceSimpleModel(ParametreAssuranceSimpleModel parametreAssuranceSimpleModel) {
        this.parametreAssuranceSimpleModel = parametreAssuranceSimpleModel;
    }

    public CotisationParametreComplexModel() {
        cotisationSimpleModel = new CotisationSimpleModel();
        assuranceSimpleModel = new AssuranceSimpleModel();
        parametreAssuranceSimpleModel = new ParametreAssuranceSimpleModel();
    }

    @Override
    public String getId() {
        return cotisationSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return cotisationSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        cotisationSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        cotisationSimpleModel.setSpy(spy);
    }

    public CotisationSimpleModel getCotisationSimpleModel() {
        return cotisationSimpleModel;
    }

    public void setCotisationSimpleModel(CotisationSimpleModel cotisationSimpleModel) {
        this.cotisationSimpleModel = cotisationSimpleModel;
    }

    public AssuranceSimpleModel getAssuranceSimpleModel() {
        return assuranceSimpleModel;
    }

    public void setAssuranceSimpleModel(AssuranceSimpleModel assuranceSimpleModel) {
        this.assuranceSimpleModel = assuranceSimpleModel;
    }
}
