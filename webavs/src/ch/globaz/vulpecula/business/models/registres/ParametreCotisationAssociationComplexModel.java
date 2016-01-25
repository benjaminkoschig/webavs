/**
 *
 */
package ch.globaz.vulpecula.business.models.registres;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.vulpecula.business.models.association.CotisationAssociationProfessionnelleSimpleModel;

public class ParametreCotisationAssociationComplexModel extends JadeComplexModel {
    private ParametreCotisationAssociationSimpleModel parametreCotisationAssociationSimpleModel;
    private CotisationAssociationProfessionnelleSimpleModel cotisationAssociationProfessionnelleSimpleModel;
    private AdministrationComplexModel administrationComplexModel;

    private static final long serialVersionUID = 1L;

    public ParametreCotisationAssociationComplexModel() {
        parametreCotisationAssociationSimpleModel = new ParametreCotisationAssociationSimpleModel();
        cotisationAssociationProfessionnelleSimpleModel = new CotisationAssociationProfessionnelleSimpleModel();
        administrationComplexModel = new AdministrationComplexModel();
    }

    public ParametreCotisationAssociationSimpleModel getParametreCotisationAssociationSimpleModel() {
        return parametreCotisationAssociationSimpleModel;
    }

    public void setParametreCotisationAssociationSimpleModel(
            ParametreCotisationAssociationSimpleModel parametreCotisationAssociationSimpleModel) {
        this.parametreCotisationAssociationSimpleModel = parametreCotisationAssociationSimpleModel;
    }

    public CotisationAssociationProfessionnelleSimpleModel getCotisationAssociationProfessionnelleSimpleModel() {
        return cotisationAssociationProfessionnelleSimpleModel;
    }

    public void setCotisationAssociationProfessionnelleSimpleModel(
            CotisationAssociationProfessionnelleSimpleModel cotisationAssociationProfessionnelleSimpleModel) {
        this.cotisationAssociationProfessionnelleSimpleModel = cotisationAssociationProfessionnelleSimpleModel;
    }

    public AdministrationComplexModel getAdministrationComplexModel() {
        return administrationComplexModel;
    }

    public void setAdministrationComplexModel(AdministrationComplexModel administrationComplexModel) {
        this.administrationComplexModel = administrationComplexModel;
    }

    @Override
    public String getId() {
        return parametreCotisationAssociationSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return parametreCotisationAssociationSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        parametreCotisationAssociationSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        parametreCotisationAssociationSimpleModel.setSpy(spy);
    }
}
