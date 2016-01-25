package ch.globaz.vulpecula.repositoriesjade.taxationoffice;

import java.util.List;
import ch.globaz.vulpecula.business.models.taxationoffice.LigneTaxationComplexModel;
import ch.globaz.vulpecula.business.models.taxationoffice.LigneTaxationSearchComplexModel;
import ch.globaz.vulpecula.business.models.taxationoffice.LigneTaxationSimpleModel;
import ch.globaz.vulpecula.domain.models.taxationoffice.LigneTaxation;
import ch.globaz.vulpecula.domain.repositories.taxationoffice.LigneTaxationRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.taxationoffice.converters.LigneTaxationConverter;

public class LigneTaxationRepositoryJade extends
        RepositoryJade<LigneTaxation, LigneTaxationComplexModel, LigneTaxationSimpleModel> implements
        LigneTaxationRepository {

    @Override
    public LigneTaxation findById(String id) {
        LigneTaxationSearchComplexModel searchModel = new LigneTaxationSearchComplexModel();
        searchModel.setForId(id);
        return searchAndFetchFirst(searchModel);
    }

    @Override
    public List<LigneTaxation> findByIdTaxationOffice(String idTaxationOffice) {
        LigneTaxationSearchComplexModel searchModel = new LigneTaxationSearchComplexModel();
        searchModel.setForIdTaxationOffice(idTaxationOffice);
        return searchAndFetch(searchModel);
    }

    @Override
    public DomaineConverterJade<LigneTaxation, LigneTaxationComplexModel, LigneTaxationSimpleModel> getConverter() {
        return new LigneTaxationConverter();
    }

}
