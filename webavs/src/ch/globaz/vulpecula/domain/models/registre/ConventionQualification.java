package ch.globaz.vulpecula.domain.models.registre;

import ch.globaz.vulpecula.domain.models.common.DomainEntity;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;

public class ConventionQualification implements DomainEntity {
    private String id;
    private String idConvention;
    private Qualification qualification;
    private Personnel personnel;
    private TypeQualification typeQualification;
    private String spy;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Qualification getQualification() {
        return qualification;
    }

    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
    }

    public Personnel getPersonnel() {
        return personnel;
    }

    public void setPersonnel(Personnel personnel) {
        this.personnel = personnel;
    }

    public TypeQualification getTypeQualification() {
        return typeQualification;
    }

    public void setTypeQualification(TypeQualification typeQualification) {
        this.typeQualification = typeQualification;
    }

    public String getIdConvention() {
        return idConvention;
    }

    public void setIdConvention(String idConvention) {
        this.idConvention = idConvention;
    }

    @Override
    public String getSpy() {
        return spy;
    }

    @Override
    public void setSpy(String spy) {
        this.spy = spy;
    }
}
