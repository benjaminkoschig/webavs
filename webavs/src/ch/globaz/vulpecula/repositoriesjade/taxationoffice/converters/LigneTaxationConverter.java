package ch.globaz.vulpecula.repositoriesjade.taxationoffice.converters;

import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.vulpecula.business.models.taxationoffice.LigneTaxationComplexModel;
import ch.globaz.vulpecula.business.models.taxationoffice.LigneTaxationSearchSimpleModel;
import ch.globaz.vulpecula.business.models.taxationoffice.LigneTaxationSimpleModel;
import ch.globaz.vulpecula.business.models.taxationoffice.TaxationOfficeComplexModel;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.taxationoffice.LigneTaxation;
import ch.globaz.vulpecula.external.models.CotisationComplexModel;
import ch.globaz.vulpecula.external.repositoriesjade.naos.converters.CotisationConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;

public class LigneTaxationConverter implements
        DomaineConverterJade<LigneTaxation, LigneTaxationComplexModel, LigneTaxationSimpleModel> {

    private TaxationOfficeConverter taxationOfficeConverter = new TaxationOfficeConverter();

    @Override
    public LigneTaxation convertToDomain(LigneTaxationComplexModel ligneTaxationComplexModel) {
        LigneTaxationSimpleModel ligneTaxationSimpleModel = ligneTaxationComplexModel.getLigneTaxationSimpleModel();
        CotisationComplexModel cotisationComplexModel = ligneTaxationComplexModel.getCotisationComplexModel();
        TaxationOfficeComplexModel taxationOfficeComplexModel = ligneTaxationComplexModel
                .getTaxationOfficeComplexModel();

        LigneTaxation ligneTaxation = convertToDomain(ligneTaxationSimpleModel);
        ligneTaxation.setTaxationOffice(taxationOfficeConverter.convertToDomain(taxationOfficeComplexModel));
        ligneTaxation.setCotisation(CotisationConverter.convertToDomain(cotisationComplexModel));
        return ligneTaxation;
    }

    @Override
    public LigneTaxationSimpleModel convertToPersistence(LigneTaxation ligneTaxation) {
        LigneTaxationSimpleModel ligneTaxationSimpleModel = new LigneTaxationSimpleModel();
        ligneTaxationSimpleModel.setId(ligneTaxation.getId());
        ligneTaxationSimpleModel.setIdCotisation(ligneTaxation.getIdCotisation());
        ligneTaxationSimpleModel.setIdTaxationOffice(ligneTaxation.getIdTaxationOffice());
        ligneTaxationSimpleModel.setMasse(String.valueOf(ligneTaxation.getMasseValue()));
        ligneTaxationSimpleModel.setTaux(String.valueOf(ligneTaxation.getTauxValue()));
        ligneTaxationSimpleModel.setMontant(String.valueOf(ligneTaxation.getMontantValue()));
        ligneTaxationSimpleModel.setSpy(ligneTaxation.getSpy());
        return ligneTaxationSimpleModel;
    }

    @Override
    public LigneTaxation convertToDomain(LigneTaxationSimpleModel ligneTaxationSimpleModel) {
        LigneTaxation ligneTaxation = new LigneTaxation();
        ligneTaxation.setId(ligneTaxationSimpleModel.getId());
        ligneTaxation.setMasse(new Montant(ligneTaxationSimpleModel.getMasse()));
        ligneTaxation.setTaux(new Taux(ligneTaxationSimpleModel.getTaux()));
        ligneTaxation.setMontant(new Montant(ligneTaxationSimpleModel.getMontant()));
        ligneTaxation.setSpy(ligneTaxationSimpleModel.getSpy());
        return ligneTaxation;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new LigneTaxationSearchSimpleModel();
    }

}
