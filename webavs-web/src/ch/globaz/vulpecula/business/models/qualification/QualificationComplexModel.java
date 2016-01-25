package ch.globaz.vulpecula.business.models.qualification;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;

/**
 * @author JPA
 * 
 */
public class QualificationComplexModel extends JadeComplexModel {
    private AdministrationComplexModel administrationComplexModel = null;
    private ConventionQualificationSimpleModel conventionQualificationSimpleModel = null;

    public QualificationComplexModel() {
        super();
        administrationComplexModel = new AdministrationComplexModel();
        conventionQualificationSimpleModel = new ConventionQualificationSimpleModel();
    }

    public AdministrationComplexModel getAdministrationComplexModel() {
        return administrationComplexModel;
    }

    public void setAdministrationComplexModel(AdministrationComplexModel administrationComplexModel) {
        this.administrationComplexModel = administrationComplexModel;
    }

    public ConventionQualificationSimpleModel getConventionQualificationSimpleModel() {
        return conventionQualificationSimpleModel;
    }

    public void setConventionQualificationSimpleModel(
            ConventionQualificationSimpleModel conventionQualificationSimpleModel) {
        this.conventionQualificationSimpleModel = conventionQualificationSimpleModel;
    }

    @Override
    public String getId() {
        return conventionQualificationSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return conventionQualificationSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        conventionQualificationSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        conventionQualificationSimpleModel.setSpy(spy);
    }
}
