package ch.globaz.vulpecula.business.models.association;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author JPA
 * 
 */
public class AssociationCotisationComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = 6418597820205013590L;

    private CotisationAssociationProfessionnelleComplexModel cotisationAssociationProfessionnelleComplexModel;
    private AssociationCotisationSimpleModel associationCotisationSimpleModel;

    public AssociationCotisationComplexModel() {
        super();
        cotisationAssociationProfessionnelleComplexModel = new CotisationAssociationProfessionnelleComplexModel();
        associationCotisationSimpleModel = new AssociationCotisationSimpleModel();
    }

    public AssociationCotisationSimpleModel getAssociationCotisationSimpleModel() {
        return associationCotisationSimpleModel;
    }

    public void setAssociationCotisationSimpleModel(AssociationCotisationSimpleModel associationCotisationSimpleModel) {
        this.associationCotisationSimpleModel = associationCotisationSimpleModel;
    }

    public CotisationAssociationProfessionnelleComplexModel getCotisationAssociationProfessionnelleComplexModel() {
        return cotisationAssociationProfessionnelleComplexModel;
    }

    public void setCotisationAssociationProfessionnelleComplexModel(
            CotisationAssociationProfessionnelleComplexModel cotisationAssociationProfessionnelleComplexModel) {
        this.cotisationAssociationProfessionnelleComplexModel = cotisationAssociationProfessionnelleComplexModel;
    }

    @Override
    public String getId() {
        return associationCotisationSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return associationCotisationSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        associationCotisationSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        associationCotisationSimpleModel.setSpy(spy);
    }
}
