package ch.globaz.vulpecula.repositoriesjade.association.converter;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.vulpecula.business.models.association.AssociationCotisationComplexModel;
import ch.globaz.vulpecula.business.models.association.AssociationCotisationSearchSimpleModel;
import ch.globaz.vulpecula.business.models.association.AssociationCotisationSimpleModel;
import ch.globaz.vulpecula.domain.models.association.AssociationCotisation;
import ch.globaz.vulpecula.domain.models.association.CotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class AssociationCotisationConverter
        implements
        DomaineConverterJade<AssociationCotisation, AssociationCotisationComplexModel, AssociationCotisationSimpleModel> {
    private static final AssociationCotisationConverter INSTANCE = new AssociationCotisationConverter();

    public static AssociationCotisationConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public AssociationCotisation convertToDomain(AssociationCotisationComplexModel associationCotisationComplexModel) {
        CotisationAssociationProfessionnelle cotisationAssociationProfessionnelle = CotisationAssociationProfessionnelleConverter
                .getInstance().convertToDomain(
                        associationCotisationComplexModel.getCotisationAssociationProfessionnelleComplexModel());

        AssociationCotisation associationCotisation = convertToDomain(associationCotisationComplexModel
                .getAssociationCotisationSimpleModel());
        associationCotisation.setCotisationAssociationProfessionnelle(cotisationAssociationProfessionnelle);

        return associationCotisation;
    }

    @Override
    public AssociationCotisationSimpleModel convertToPersistence(AssociationCotisation associationCotisation) {
        AssociationCotisationSimpleModel associationCotisationSimpleModel = new AssociationCotisationSimpleModel();
        associationCotisationSimpleModel.setId(associationCotisation.getId());
        associationCotisationSimpleModel.setIdCotisationAssociationProfessionnelle(associationCotisation
                .getIdCotisationAssociationProfessionnelle());
        associationCotisationSimpleModel.setGenre(associationCotisation.getGenre().getValue());
        associationCotisationSimpleModel.setIdEmployeur(associationCotisation.getIdEmployeur());
        associationCotisationSimpleModel.setPeriodeDebut(associationCotisation.getPeriodeDebutAsValue());
        associationCotisationSimpleModel.setPeriodeFin(associationCotisation.getPeriodeFinAsValue());
        associationCotisationSimpleModel.setForfait(associationCotisation.getForfait().getValue());
        associationCotisationSimpleModel.setFacturer(associationCotisation.getFacturer().getValue());
        associationCotisationSimpleModel.setSpy(associationCotisation.getSpy());
        return associationCotisationSimpleModel;
    }

    @Override
    public AssociationCotisation convertToDomain(AssociationCotisationSimpleModel associationCotisationSimpleModel) {
        AssociationCotisation associationCotisation = new AssociationCotisation();
        associationCotisation.setId(associationCotisationSimpleModel.getId());
        associationCotisation.setGenre(GenreCotisationAssociationProfessionnelle
                .fromValue(associationCotisationSimpleModel.getGenre()));
        associationCotisation.setIdEmployeur(associationCotisationSimpleModel.getIdEmployeur());
        if (!JadeNumericUtil.isEmptyOrZero(associationCotisationSimpleModel.getPeriodeFin())) {
            associationCotisation.setPeriode(new Periode(associationCotisationSimpleModel.getPeriodeDebut(),
                    associationCotisationSimpleModel.getPeriodeFin()));
        } else {
            associationCotisation.setPeriode(new Periode(associationCotisationSimpleModel.getPeriodeDebut(), null));
        }

        if (!JadeStringUtil.isEmpty(associationCotisationSimpleModel.getForfait())) {
            associationCotisation.setForfait(new Montant(associationCotisationSimpleModel.getForfait()));
        }

        // associationCotisation.setFacturer(CategorieFactureAssociationProfessionnelle
        // .fromValue(associationCotisationSimpleModel.getFacturer()));
        associationCotisation.setSpy(associationCotisationSimpleModel.getSpy());

        if (!JadeStringUtil.isBlankOrZero(associationCotisationSimpleModel.getIdCotisationAssociationProfessionnelle())) {
            CotisationAssociationProfessionnelle cotisation = new CotisationAssociationProfessionnelle();
            cotisation.setId(associationCotisationSimpleModel.getIdCotisationAssociationProfessionnelle());
            associationCotisation.setCotisationAssociationProfessionnelle(cotisation);
        }

        return associationCotisation;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new AssociationCotisationSearchSimpleModel();
    }

}
