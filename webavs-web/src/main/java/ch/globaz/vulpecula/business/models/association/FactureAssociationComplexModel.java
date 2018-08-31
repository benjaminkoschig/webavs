package ch.globaz.vulpecula.business.models.association;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.vulpecula.business.models.employeur.EmployeurComplexModel;
import ch.globaz.vulpecula.external.models.AdministrationComplexModel;

public class FactureAssociationComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = -4622854639297176318L;

    private EnteteFactureAssociationProfessionnelleSimpleModel enteteFactureAssociationProfessionnelleSimpleModel;
    private LigneFactureAssociationProfessionnelleSimpleModel ligneFactureAssociationProfessionnelleSimpleModel;
    private AdministrationComplexModel administrationComplexModel;
    private AdministrationComplexModel administrationParentComplexModel;
    private AssociationCotisationSimpleModel associationCotisationSimpleModel;
    private CotisationAssociationProfessionnelleSimpleModel cotisationAssociationProfessionnelleSimpleModel;
    private PassageModel passageModel;
    private EmployeurComplexModel employeurComplexModel;

    public FactureAssociationComplexModel() {
        administrationParentComplexModel = new AdministrationComplexModel();
        enteteFactureAssociationProfessionnelleSimpleModel = new EnteteFactureAssociationProfessionnelleSimpleModel();
        ligneFactureAssociationProfessionnelleSimpleModel = new LigneFactureAssociationProfessionnelleSimpleModel();
        administrationComplexModel = new AdministrationComplexModel();
        associationCotisationSimpleModel = new AssociationCotisationSimpleModel();
        cotisationAssociationProfessionnelleSimpleModel = new CotisationAssociationProfessionnelleSimpleModel();
        employeurComplexModel = new EmployeurComplexModel();
        passageModel = new PassageModel();
    }

    public EnteteFactureAssociationProfessionnelleSimpleModel getEnteteFactureAssociationProfessionnelleSimpleModel() {
        return enteteFactureAssociationProfessionnelleSimpleModel;
    }

    public void setEnteteFactureAssociationProfessionnelleSimpleModel(
            EnteteFactureAssociationProfessionnelleSimpleModel enteteFactureAssociationProfessionnelleSimpleModel) {
        this.enteteFactureAssociationProfessionnelleSimpleModel = enteteFactureAssociationProfessionnelleSimpleModel;
    }

    public LigneFactureAssociationProfessionnelleSimpleModel getLigneFactureAssociationProfessionnelleSimpleModel() {
        return ligneFactureAssociationProfessionnelleSimpleModel;
    }

    public void setLigneFactureAssociationProfessionnelleSimpleModel(
            LigneFactureAssociationProfessionnelleSimpleModel ligneFactureAssociationProfessionnelleSimpleModel) {
        this.ligneFactureAssociationProfessionnelleSimpleModel = ligneFactureAssociationProfessionnelleSimpleModel;
    }

    public AssociationCotisationSimpleModel getAssociationCotisationSimpleModel() {
        return associationCotisationSimpleModel;
    }

    public void setAssociationCotisationSimpleModel(AssociationCotisationSimpleModel associationCotisationSimpleModel) {
        this.associationCotisationSimpleModel = associationCotisationSimpleModel;
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

    public AdministrationComplexModel getAdministrationParentComplexModel() {
        return administrationParentComplexModel;
    }

    public void setAdministrationParentComplexModel(AdministrationComplexModel administrationParentComplexModel) {
        this.administrationParentComplexModel = administrationParentComplexModel;
    }

    public PassageModel getPassageModel() {
        return passageModel;
    }

    public void setPassageModel(PassageModel passageModel) {
        this.passageModel = passageModel;
    }

    public EmployeurComplexModel getEmployeurComplexModel() {
        return employeurComplexModel;
    }

    public void setEmployeurComplexModel(EmployeurComplexModel employeurComplexModel) {
        this.employeurComplexModel = employeurComplexModel;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getSpy() {
        return null;
    }

    @Override
    public void setId(String id) {
    }

    @Override
    public void setSpy(String spy) {
    }
}
