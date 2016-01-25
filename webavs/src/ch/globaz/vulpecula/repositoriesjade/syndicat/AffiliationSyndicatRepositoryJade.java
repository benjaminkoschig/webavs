package ch.globaz.vulpecula.repositoriesjade.syndicat;

import java.util.List;
import ch.globaz.vulpecula.business.models.syndicat.AffiliationSyndicatComplexModel;
import ch.globaz.vulpecula.business.models.syndicat.AffiliationSyndicatSearchComplexModel;
import ch.globaz.vulpecula.business.models.syndicat.AffiliationSyndicatSimpleModel;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.syndicat.AffiliationSyndicat;
import ch.globaz.vulpecula.domain.repositories.syndicat.AffiliationSyndicatRepository;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.syndicat.converters.AffiliationSyndicatConverter;

public class AffiliationSyndicatRepositoryJade extends
        RepositoryJade<AffiliationSyndicat, AffiliationSyndicatComplexModel, AffiliationSyndicatSimpleModel> implements
        AffiliationSyndicatRepository {

    private static final String SEARCH_FOR_DATE_FIN_IS_NULL = "searchForDateFinIsNull";

    @Override
    public AffiliationSyndicat findById(String id) {
        AffiliationSyndicatSearchComplexModel searchComplexModel = new AffiliationSyndicatSearchComplexModel();
        searchComplexModel.setForId(id);
        return searchAndFetchFirst(searchComplexModel);
    }

    @Override
    public List<AffiliationSyndicat> findByIdTravailleur(String idTravailleur) {
        AffiliationSyndicatSearchComplexModel searchComplexModel = new AffiliationSyndicatSearchComplexModel();
        searchComplexModel.setForIdTravailleur(idTravailleur);
        return searchAndFetch(searchComplexModel);
    }

    @Override
    public DomaineConverterJade<AffiliationSyndicat, AffiliationSyndicatComplexModel, AffiliationSyndicatSimpleModel> getConverter() {
        return AffiliationSyndicatConverter.getInstance();
    }

    @Override
    public List<AffiliationSyndicat> findBySyndicatAnnee(Administration syndicat, Annee annee) {
        AffiliationSyndicatSearchComplexModel searchComplexModel = new AffiliationSyndicatSearchComplexModel();
        searchComplexModel.setForDateDebutBeforeOrEquals(annee.getLastDayOfYear());
        searchComplexModel.setForDateDebutAfterOrEquals(annee.getFirstDayOfYear());
        searchComplexModel.setForIdSyndicat(syndicat.getId());
        return searchAndFetch(searchComplexModel);
    }

    @Override
    public List<AffiliationSyndicat> findByAnnee(String idSyndicat, Annee annee) {
        AffiliationSyndicatSearchComplexModel searchComplexModel = new AffiliationSyndicatSearchComplexModel();
        searchComplexModel.setWhereKey(SEARCH_FOR_DATE_FIN_IS_NULL);
        searchComplexModel.setForDateDebutBeforeOrEquals(annee.getLastDayOfYear());
        searchComplexModel.setForDateFinAfterOrEquals(annee.getFirstDayOfYear());
        searchComplexModel.setForIdSyndicat(idSyndicat);
        searchComplexModel.setForDateFinIsNull();
        return searchAndFetch(searchComplexModel);
    }

    @Override
    public List<AffiliationSyndicat> findByAnnee(Annee annee) {
        return findByAnnee(null, annee);
    }
}
