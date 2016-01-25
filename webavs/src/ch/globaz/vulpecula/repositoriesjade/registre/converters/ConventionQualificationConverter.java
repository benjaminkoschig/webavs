package ch.globaz.vulpecula.repositoriesjade.registre.converters;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.vulpecula.business.models.qualification.ConventionQualificationSearchSimpleModel;
import ch.globaz.vulpecula.business.models.qualification.ConventionQualificationSimpleModel;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.domain.models.registre.ConventionQualification;
import ch.globaz.vulpecula.domain.models.registre.Personnel;
import ch.globaz.vulpecula.domain.models.registre.TypeQualification;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

/**
 * Convertisseur d'objet {@link ConventionQualification} <--> {@link ConventionQualificationSimpleModel}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 6 janv. 2014
 * 
 */
public final class ConventionQualificationConverter implements
        DomaineConverterJade<ConventionQualification, JadeComplexModel, ConventionQualificationSimpleModel> {

    private static final ConventionQualificationConverter INSTANCE = new ConventionQualificationConverter();

    public static ConventionQualificationConverter getInstance() {
        return INSTANCE;
    }

    private ConventionQualificationConverter() {

    }

    @Override
    public ConventionQualification convertToDomain(
            final ConventionQualificationSimpleModel conventionQualificationSimpleModel) {
        ConventionQualification conventionQualification = new ConventionQualification();

        conventionQualification.setId(conventionQualificationSimpleModel.getId());
        conventionQualification.setIdConvention(conventionQualificationSimpleModel.getIdConvention());
        conventionQualification.setPersonnel(Personnel.fromValue(conventionQualificationSimpleModel.getPersonnel()));
        conventionQualification.setQualification(Qualification.fromValue(conventionQualificationSimpleModel
                .getQualification()));
        conventionQualification.setTypeQualification(TypeQualification.fromValue(conventionQualificationSimpleModel
                .getOuvrierEmploye()));
        conventionQualification.setSpy(conventionQualificationSimpleModel.getSpy());

        return conventionQualification;
    }

    @Override
    public ConventionQualification convertToDomain(final JadeComplexModel model) {
        return null;
    }

    @Override
    public ConventionQualificationSimpleModel convertToPersistence(final ConventionQualification conventionQualification) {
        ConventionQualificationSimpleModel conventionQualificationSimpleModel = new ConventionQualificationSimpleModel();
        conventionQualificationSimpleModel.setId(conventionQualification.getId());
        conventionQualificationSimpleModel.setIdConvention(conventionQualification.getIdConvention());
        conventionQualificationSimpleModel.setOuvrierEmploye(conventionQualification.getTypeQualification().getValue());
        conventionQualificationSimpleModel.setPersonnel(conventionQualification.getPersonnel().getValue());
        conventionQualificationSimpleModel.setQualification(conventionQualification.getQualification().getValue());
        conventionQualificationSimpleModel.setSpy(conventionQualification.getSpy());
        return conventionQualificationSimpleModel;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new ConventionQualificationSearchSimpleModel();
    }
}
