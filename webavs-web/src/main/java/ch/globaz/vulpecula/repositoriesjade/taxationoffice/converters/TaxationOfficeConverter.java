package ch.globaz.vulpecula.repositoriesjade.taxationoffice.converters;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteComplexModel;
import ch.globaz.vulpecula.business.models.taxationoffice.TaxationOfficeComplexModel;
import ch.globaz.vulpecula.business.models.taxationoffice.TaxationOfficeSearchSimpleModel;
import ch.globaz.vulpecula.business.models.taxationoffice.TaxationOfficeSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.taxationoffice.EtatTaxation;
import ch.globaz.vulpecula.domain.models.taxationoffice.TaxationOffice;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.converters.DecompteConverter;

public class TaxationOfficeConverter implements
        DomaineConverterJade<TaxationOffice, TaxationOfficeComplexModel, TaxationOfficeSimpleModel> {

    private DecompteConverter decompteConverter = new DecompteConverter();

    @Override
    public TaxationOffice convertToDomain(TaxationOfficeComplexModel taxationOfficeComplexModel) {
        TaxationOfficeSimpleModel taxationOfficeSimpleModel = taxationOfficeComplexModel.getTaxationOfficeSimpleModel();
        DecompteComplexModel decompteComplexModel = taxationOfficeComplexModel.getDecompteComplexModel();

        TaxationOffice taxationOffice = convertToDomain(taxationOfficeSimpleModel);
        taxationOffice.setDecompte(decompteConverter.convertToDomain(decompteComplexModel));

        return taxationOffice;
    }

    @Override
    public TaxationOfficeSimpleModel convertToPersistence(TaxationOffice taxationOffice) {
        TaxationOfficeSimpleModel taxationOfficeSimpleModel = new TaxationOfficeSimpleModel();
        taxationOfficeSimpleModel.setId(taxationOffice.getId());
        taxationOfficeSimpleModel.setIdPassageFacturation(taxationOffice.getIdPassageFacturation());
        taxationOfficeSimpleModel.setIdDecompte(taxationOffice.getIdDecompte());
        taxationOfficeSimpleModel.setEtat(taxationOffice.getEtatValue());
        if (taxationOffice.getDateAnnulation() != null) {
            taxationOfficeSimpleModel.setDateAnnulation(taxationOffice.getDateAnnulation().getSwissValue());
        }
        taxationOfficeSimpleModel.setIdSection(taxationOffice.getIdSection());
        taxationOfficeSimpleModel.setSpy(taxationOffice.getSpy());
        return taxationOfficeSimpleModel;
    }

    @Override
    public TaxationOffice convertToDomain(TaxationOfficeSimpleModel taxationOfficeSimpleModel) {
        TaxationOffice taxationOffice = new TaxationOffice();
        taxationOffice.setId(taxationOfficeSimpleModel.getId());
        taxationOffice.setIdPassageFacturation(taxationOfficeSimpleModel.getIdPassageFacturation());
        taxationOffice.setEtat(EtatTaxation.fromValue(taxationOfficeSimpleModel.getEtat()));
        if (!JadeNumericUtil.isEmptyOrZero(taxationOfficeSimpleModel.getDateAnnulation())) {
            taxationOffice.setDateAnnulation(new Date(taxationOfficeSimpleModel.getDateAnnulation()));
        }
        taxationOffice.setIdSection(taxationOfficeSimpleModel.getIdSection());
        taxationOffice.setSpy(taxationOfficeSimpleModel.getSpy());
        return taxationOffice;
    }

    @Override
    public JadeAbstractSearchModel getSearchSimpleModel() {
        return new TaxationOfficeSearchSimpleModel();
    }

}
