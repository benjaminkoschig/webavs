package ch.globaz.vulpecula.business.models.association;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;

public class CotisationAssociationProfessionnelleComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = 1L;

    private CotisationAssociationProfessionnelleSimpleModel cotisationAssociationProfessionnelleSimpleModel;
    private AdministrationComplexModel administrationComplexModel;

    public CotisationAssociationProfessionnelleComplexModel() {
        cotisationAssociationProfessionnelleSimpleModel = new CotisationAssociationProfessionnelleSimpleModel();
        administrationComplexModel = new AdministrationComplexModel();
    }

    @Override
    public String getId() {
        return cotisationAssociationProfessionnelleSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return cotisationAssociationProfessionnelleSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        cotisationAssociationProfessionnelleSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        cotisationAssociationProfessionnelleSimpleModel.setSpy(spy);
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
}
