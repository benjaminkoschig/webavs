package ch.globaz.vulpecula.repositoriesjade.decompte.ebusiness;

import globaz.jade.persistence.model.JadeComplexModel;
import java.util.List;
import ch.globaz.vulpecula.business.models.ebusiness.TravailleurEbuSimpleModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurEbuSearchSimpleModel;
import ch.globaz.vulpecula.domain.models.postetravail.TravailleurEbuDomain;
import ch.globaz.vulpecula.domain.repositories.ebusiness.NouveauTravailleurRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.TravailleurRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.DomaineConverterJade;
import ch.globaz.vulpecula.repositoriesjade.postetravail.converters.TravailleurEbuConverter;

/***
 * Implémentation Jade de {@link TravailleurRepository}
 * 
 */
public class NouveauTravailleurRepositoryJade extends
        RepositoryJade<TravailleurEbuDomain, JadeComplexModel, TravailleurEbuSimpleModel> implements
        NouveauTravailleurRepository {

    @Override
    public List<TravailleurEbuDomain> findAll() {
        TravailleurEbuSearchSimpleModel travailleurSearch = new TravailleurEbuSearchSimpleModel();
        return searchAndFetch(travailleurSearch);
    }

    @Override
    public List<TravailleurEbuDomain> findAllSansQuittance() {
        TravailleurEbuSearchSimpleModel search = new TravailleurEbuSearchSimpleModel();
        search.setWhereKey(TravailleurEbuSearchSimpleModel.WHERE_KEY_SANS_QUITTANCE);
        return searchAndFetch(search);
    }

    @Override
    public TravailleurEbuDomain findById(final String id) {
        TravailleurEbuSearchSimpleModel search = new TravailleurEbuSearchSimpleModel();
        search.setForId(id);
        return searchAndFetchFirst(search);
    }

    @Override
    public TravailleurEbuDomain findByIdSansQuittance(final String id) {
        TravailleurEbuSearchSimpleModel search = new TravailleurEbuSearchSimpleModel();
        search.setWhereKey(TravailleurEbuSearchSimpleModel.WHERE_KEY_SANS_QUITTANCE);
        search.setForId(id);
        return searchAndFetchFirst(search);
    }

    @Override
    public TravailleurEbuDomain findByCorrelationId(final String id) {
        TravailleurEbuSearchSimpleModel search = new TravailleurEbuSearchSimpleModel();
        search.setWhereKey(TravailleurEbuSearchSimpleModel.WHERE_KEY_SANS_QUITTANCE);
        search.setForCorrelationId(id);
        return searchAndFetchFirst(search);
    }

    @Override
    public TravailleurEbuDomain findByCorrelationIdAndPosteCorrelationId(final String correlationId,
            final String posteCorrelationId) {
        TravailleurEbuSearchSimpleModel search = new TravailleurEbuSearchSimpleModel();
        search.setWhereKey(TravailleurEbuSearchSimpleModel.WHERE_KEY_SANS_QUITTANCE);
        search.setForCorrelationId(correlationId);
        search.setForPosteCorrelationId(posteCorrelationId);
        return searchAndFetchFirst(search);
    }

    @Override
    public TravailleurEbuDomain findByPosteCorrelationId(final String posteCorrelationId) {
        TravailleurEbuSearchSimpleModel search = new TravailleurEbuSearchSimpleModel();
        search.setWhereKey(TravailleurEbuSearchSimpleModel.WHERE_KEY_SANS_QUITTANCE);
        search.setForPosteCorrelationId(posteCorrelationId);
        return searchAndFetchFirst(search);
    }

    @Override
    public List<TravailleurEbuDomain> findByIdEmployeur(final String idEmployeur) {
        TravailleurEbuSearchSimpleModel search = new TravailleurEbuSearchSimpleModel();
        search.setForIdEmployeur(idEmployeur);

        return searchAndFetch(search);
    }

    @Override
    public DomaineConverterJade<TravailleurEbuDomain, JadeComplexModel, TravailleurEbuSimpleModel> getConverter() {
        return TravailleurEbuConverter.getInstance();
    }

    @Override
    public TravailleurEbuDomain findByCorrelationIdWithQuittance(String correlationId) {
        TravailleurEbuSearchSimpleModel search = new TravailleurEbuSearchSimpleModel();
        search.setForCorrelationId(correlationId);
        return searchAndFetchFirst(search);
    }

    @Override
    public TravailleurEbuDomain findByCorrelationAndPosteCorrelationWithQuittance(String correlationId,
            String posteCorrelationId) {
        TravailleurEbuSearchSimpleModel search = new TravailleurEbuSearchSimpleModel();
        search.setForCorrelationId(correlationId);
        search.setForPosteCorrelationId(posteCorrelationId);
        return searchAndFetchFirst(search);
    }

    @Override
    public List<TravailleurEbuDomain> findByNSS(String nss) {
        TravailleurEbuSearchSimpleModel travailleurSearch = new TravailleurEbuSearchSimpleModel();
        travailleurSearch.setForNumAvs(nss);
        return searchAndFetch(travailleurSearch);
    }

}
