package ch.globaz.vulpecula.business.models.qualification;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author JPA
 * 
 */
public class ConventionQualificationSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = 4426071218871997143L;

    private String forId = "";
    private String forIdConvention = "";
    private String forIdConventionQualification = "";
    private String forCsQualification = "";

    public String getForIdConvention() {
        return forIdConvention;
    }

    public String getForIdConventionQualification() {
        return forIdConventionQualification;
    }

    public void setForIdConvention(final String forIdConvention) {
        this.forIdConvention = forIdConvention;
    }

    public void setForIdConventionQualification(final String forIdConventionQualification) {
        this.forIdConventionQualification = forIdConventionQualification;
    }

    public String getForId() {
        return forId;
    }

    public void setForId(final String forId) {
        this.forId = forId;
    }

    public String getForCsQualification() {
        return forCsQualification;
    }

    public void setForCsQualification(String forCsQualification) {
        this.forCsQualification = forCsQualification;
    }

    @Override
    public Class<ConventionQualificationSimpleModel> whichModelClass() {
        return ConventionQualificationSimpleModel.class;
    }
}
