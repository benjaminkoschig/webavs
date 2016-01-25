package ch.globaz.vulpecula.business.models.qualification;

import globaz.jade.persistence.model.JadeSimpleModel;

public class ConventionQualificationSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = -2957856222343839954L;

    private String id;
    private String idConvention;
    private String ouvrierEmploye;
    private String personnel;
    private String qualification;

    @Override
    public String getId() {
        return id;
    }

    public String getIdConvention() {
        return idConvention;
    }

    public String getOuvrierEmploye() {
        return ouvrierEmploye;
    }

    public String getPersonnel() {
        return personnel;
    }

    public String getQualification() {
        return qualification;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void setIdConvention(String idConvention) {
        this.idConvention = idConvention;
    }

    public void setOuvrierEmploye(String ouvrierEmploye) {
        this.ouvrierEmploye = ouvrierEmploye;
    }

    public void setPersonnel(String personnel) {
        this.personnel = personnel;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }
}
