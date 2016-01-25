package ch.globaz.vulpecula.repositoriesjade.syndicat;

import java.util.List;
import ch.globaz.vulpecula.business.models.syndicat.ParametreSyndicatComplexModel;
import ch.globaz.vulpecula.business.models.syndicat.ParametreSyndicatSearchComplexModel;
import ch.globaz.vulpecula.business.models.syndicat.ParametreSyndicatSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.syndicat.ParametreSyndicat;
import ch.globaz.vulpecula.domain.repositories.syndicat.ParametreSyndicatRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.syndicat.converters.ParametreSyndicatConverter;

public class ParametreSyndicatRepositoryJade extends
        RepositoryJade<ParametreSyndicat, ParametreSyndicatComplexModel, ParametreSyndicatSimpleModel> implements
        ParametreSyndicatRepository {
    @Override
    public ParametreSyndicat findById(String id) {
        ParametreSyndicatSearchComplexModel searchComplexModel = new ParametreSyndicatSearchComplexModel();
        searchComplexModel.setForId(id);
        return searchAndFetchFirst(searchComplexModel);
    }

    @Override
    public List<ParametreSyndicat> findByIdSyndicat(String idSyndicat) {
        return findByIdSyndicat(idSyndicat, null);
    }

    @Override
    public List<ParametreSyndicat> findByIdSyndicat(String idSyndicat, String idCaisseMetier) {
        ParametreSyndicatSearchComplexModel searchComplexModel = new ParametreSyndicatSearchComplexModel();
        searchComplexModel.setForIdSyndicat(idSyndicat);
        searchComplexModel.setForIdCaisseMetier(idCaisseMetier);
        return searchAndFetch(searchComplexModel);
    }

    @Override
    public List<ParametreSyndicat> findForYear(Annee annee) {
        ParametreSyndicatSearchComplexModel searchComplexModel = new ParametreSyndicatSearchComplexModel();
        searchComplexModel.setForAnnee(annee);
        return searchAndFetch(searchComplexModel);
    }

    @Override
    public DomaineConverterJade<ParametreSyndicat, ParametreSyndicatComplexModel, ParametreSyndicatSimpleModel> getConverter() {
        return ParametreSyndicatConverter.getInstance();
    }

}
