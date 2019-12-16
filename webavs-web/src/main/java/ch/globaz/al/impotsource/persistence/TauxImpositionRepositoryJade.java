package ch.globaz.al.impotsource.persistence;

import ch.globaz.al.impotsource.domain.TauxImpositions;
import ch.globaz.al.impotsource.models.TauxImpositionSearchSimpleModel;
import ch.globaz.al.impotsource.models.TauxImpositionSimpleModel;
import ch.globaz.al.impotsource.domain.TauxImposition;
import ch.globaz.al.impotsource.domain.TypeImposition;
import ch.globaz.common.persistence.DomaineConverterJade;

public class TauxImpositionRepositoryJade extends
        ALRepositoryJade<TauxImposition, TauxImpositionSimpleModel> implements TauxImpositionRepository {
    @Override
    public DomaineConverterJade<TauxImposition, TauxImpositionSimpleModel> getConverter() {
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
