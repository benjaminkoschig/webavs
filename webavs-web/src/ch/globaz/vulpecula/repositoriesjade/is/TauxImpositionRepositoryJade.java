package ch.globaz.vulpecula.repositoriesjade.is;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.vulpecula.business.models.is.TauxImpositionSearchSimpleModel;
import ch.globaz.vulpecula.business.models.is.TauxImpositionSimpleModel;
import ch.globaz.vulpecula.domain.models.is.TauxImposition;
import ch.globaz.vulpecula.domain.models.is.TauxImpositions;
import ch.globaz.vulpecula.domain.models.is.TypeImposition;
import ch.globaz.vulpecula.domain.repositories.is.TauxImpositionRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.is.converters.TauxImpositionConverter;

public class TauxImpositionRepositoryJade extends
        RepositoryJade<TauxImposition, JadeComplexModel, TauxImpositionSimpleModel> implements TauxImpositionRepository {
    @Override
    public DomaineConverterJade<TauxImposition, JadeComplexModel, TauxImpositionSimpleModel> getConverter() {
        return new TauxImpositionConverter();
    }

    @Override
    public TauxImpositions findAll() {
        TauxImpositionSearchSimpleModel searchModel = new TauxImpositionSearchSimpleModel();
        return new TauxImpositions(searchAndFetch(searchModel));
    }

    @Override
    public TauxImpositions findAll(TypeImposition typeImposition) {
        TauxImpositionSearchSimpleModel searchModel = new TauxImpositionSearchSimpleModel();
        searchModel.setForTypeImpostiion(typeImposition);
        return new TauxImpositions(searchAndFetch(searchModel));
    }
}
