package ch.globaz.vulpecula.business.models.qualification;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author JPA
 * 
 */
public class QualificationSearchComplexModel extends JadeSearchComplexModel {
    private String forIdConvention = "";
    private String forIdConventionQualification = "";

    public String getForIdConvention() {
        return forIdConvention;
    }

    public String getForIdConventionQualification() {
        return forIdConventionQualification;
    }

    public void setForIdConvention(String forIdConvention) {
        this.forIdConvention = forIdConvention;
    }

    public void setForIdConventionQualification(String forIdConventionQualification) {
        this.forIdConventionQualification = forIdConventionQualification;
    }

    @Override
    public Class<QualificationComplexModel> whichModelClass() {
        return QualificationComplexModel.class;
    }
}
