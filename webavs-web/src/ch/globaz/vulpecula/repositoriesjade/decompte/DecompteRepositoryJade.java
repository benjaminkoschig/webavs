package ch.globaz.vulpecula.repositoriesjade.decompte;

import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import ch.globaz.vulpecula.business.models.decomptes.DecompteComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteSearchComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.PeriodeMensuelle;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.HistoriqueDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.converters.DecompteConverter;

/***
 * Implémentation Jade de {@link DecompteRepository}.
 * 
 */
public class DecompteRepositoryJade extends RepositoryJade<Decompte, DecompteComplexModel, DecompteSimpleModel>
        implements DecompteRepository {

    @Override
    public Decompte findById(final String id) {
        DecompteSearchComplexModel search = new DecompteSearchComplexModel();
        search.setForId(id);
        search.setOrderKey(DecompteSearchComplexModel.ORDER_BY_RAISON_SOCIAL_PERIODE_DEBUT_ASC);
        return searchAndFetchFirst(search);
    }

    @Override
    public List<Decompte> findByIdInWithDependencies(Collection<String> ids) {
        DecompteSearchComplexModel search = new DecompteSearchComplexModel();
        search.setForIds(ids);
        search.setOrderKey(DecompteSearchComplexModel.ORDER_BY_RAISON_SOCIAL_PERIODE_DEBUT_ASC);
        return searchAndFetchDependencies(search);
    }

    @Override
    public List<Decompte> findDecompteByNoDecompte(String noDecompte) {
        DecompteSearchComplexModel search = new DecompteSearchComplexModel();
        search.setForNoDecompte(noDecompte);
        return searchAndFetch(search);
    }

    @Override
    public Decompte findFirstDecompteByNoDecompte(String noDecompte) {
        DecompteSearchComplexModel search = new DecompteSearchComplexModel();
        search.setForNoDecompte(noDecompte);
        return searchAndFetchFirst(search);
    }

    @Override
    public Decompte findByIdWithDependencies(final String idDecompte) {
        Decompte decompte = findById(idDecompte);
        if (decompte == null) {
            return null;
        }
        loadDependencies(Arrays.asList(decompte));
        return decompte;
    }

    @Override
    public List<Decompte> findDecomptesForFacturation() {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setInEtats(EtatDecompte.RECTIFIE, EtatDecompte.VALIDE);
        return searchAndFetch(searchModel);
    }

    @Override
    public List<Decompte> findAll() {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        return searchAndFetchDependencies(searchModel);
    }

    @Override
    public List<Decompte> findByIdEmployeur(final String idEmployeur) {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        return searchAndFetchDependencies(searchModel, LoadOptions.NOT_LOAD_DECOMPTE_SALAIRE_DEPENDENCIES);
    }

    @Override
    public List<Decompte> findByIdEmployeur(final String idEmployeur, Date date) {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForDateDe(date);
        return searchAndFetchDependencies(searchModel, LoadOptions.NOT_LOAD_DECOMPTE_SALAIRE_DEPENDENCIES);
    }

    @Override
    public List<Decompte> findByIdEmployeurWithDependencies(final String idEmployeur) {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        return searchAndFetchDependencies(searchModel, LoadOptions.NOT_LOAD_HISTORIQUE);
    }

    @Override
    public List<Decompte> findWithDependencies(final String idEmployeur, String idDecompte, String numeroDecompte,
            String type) {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        if (!JadeStringUtil.isEmpty(idDecompte)) {
            searchModel.setForIdGreater(idDecompte);
            searchModel.setOrderKey(DecompteSearchComplexModel.ORDER_BY_ID_ASC);
        }
        searchModel.setLikeNoDecompte(numeroDecompte);
        searchModel.setForType(type);
        return searchAndFetchDependencies(searchModel, LoadOptions.NOT_LOAD_HISTORIQUE);
    }

    @Override
    public List<Decompte> findDecomptesForIdPassage(final String idPassage) {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setForIdPassage(idPassage);
        return searchAndFetchDependencies(searchModel);
    }

    @Override
    public DomaineConverterJade<Decompte, DecompteComplexModel, DecompteSimpleModel> getConverter() {
        return new DecompteConverter();
    }

    @Override
    public List<Decompte> findRectificatifs() {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setWantRectifie(true);
        searchModel.setForEtat(EtatDecompte.COMPTABILISE.getValue());
        return searchAndFetch(searchModel);
    }

    @Override
    public List<Decompte> findDecomptesForSommation() {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setInEtats(EtatDecompte.GENERE, EtatDecompte.OUVERT);
        searchModel.setInTypes(TypeDecompte.PERIODIQUE, TypeDecompte.COMPLEMENTAIRE);
        searchModel.setBeforeDateRappel(Date.now());
        searchModel.setDateRappelIsNotZero();
        return searchAndFetch(searchModel);
    }

    @Override
    public List<Decompte> findDecomptesForTaxationOffice() {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setForEtat(EtatDecompte.SOMMATION.getValue());
        searchModel.setBeforeDateRappel(Date.now());
        searchModel.setDateRappelIsNotZero();
        return searchAndFetch(searchModel);
    }

    @Override
    public Decompte findDecomptePrecedent(Decompte decompte) {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setInEtats(EtatDecompte.COMPTABILISE);
        searchModel.setForIdEmployeur(decompte.getIdEmployeur());
        searchModel.setBeforeDate(decompte.getPeriodeDebut());
        searchModel.setOrderKey("periodeDebutDesc");
        return searchAndFetchFirst(searchModel);
    }

    @Override
    public Decompte findDecompteIdentiqueGenere(Employeur employeur, PeriodeMensuelle periodeMensuelle,
            TypeDecompte type) {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setForIdEmployeur(employeur.getId());
        searchModel.setForType(type);
        searchModel.setForPeriode(periodeMensuelle);
        searchModel.setForEtat(EtatDecompte.GENERE);
        return searchAndFetchFirst(searchModel);
    }

    @Override
    public List<Decompte> findBy(String idDecompte, String numeroDecompte, String noAffilie, String idPassage,
            EtatDecompte etatDecompte) {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setForId(idDecompte);
        searchModel.setForNoDecompte(numeroDecompte);
        searchModel.setLikeNoAffilie(noAffilie);
        searchModel.setForIdPassage(idPassage);
        searchModel.setForEtat(etatDecompte);
        return searchAndFetch(searchModel);
    }

    @Override
    public List<Decompte> findBy(String idDecompte, String numeroDecompte, String noAffilie, String idPassage,
            String etatsDecomptesSeparatedByComma) {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setForId(idDecompte);
        searchModel.setForNoDecompte(numeroDecompte);
        searchModel.setLikeNoAffilie(noAffilie);
        searchModel.setForIdPassage(idPassage);
        searchModel.setInEtats(etatsDecomptesSeparatedByComma);
        return searchAndFetch(searchModel);
    }

    private List<Decompte> searchAndFetchDependencies(final JadeSearchComplexModel searchModel, LoadOptions... options) {
        List<Decompte> decomptes = searchAndFetch(searchModel);
        loadDependencies(decomptes, options);
        return decomptes;
    }

    private void loadDependencies(final List<Decompte> decomptes, LoadOptions... options) {
        List<LoadOptions> loadOptions = Arrays.asList(options);

        List<String> ids = new ArrayList<String>();
        for (Decompte decompte : decomptes) {
            ids.add(decompte.getId());
        }

        if (!loadOptions.contains(LoadOptions.NOT_LOAD_DECOMPTE_SALAIRE_DEPENDENCIES)) {
            loadDecomptesSalairesWithDependencies(decomptes, ids);
        } else {
            loadDecomptesSalaires(decomptes, ids);
        }

        if (!loadOptions.contains(LoadOptions.NOT_LOAD_HISTORIQUE)) {
            loadHistoriques(decomptes, ids);
        }
    }

    private void loadHistoriques(final List<Decompte> decomptes, final List<String> ids) {
        List<HistoriqueDecompte> historiques = RepositoryJade.searchByLot(ids,
                new SearchLotExecutor<HistoriqueDecompte>() {
                    @Override
                    public List<HistoriqueDecompte> execute(final List<String> ids) {
                        return VulpeculaRepositoryLocator.getHistoriqueDecompteRepository().findByIdDecompteIn(ids);
                    }
                });

        Map<String, List<HistoriqueDecompte>> map = JadeListUtil.groupBy(historiques,
                new JadeListUtil.Key<HistoriqueDecompte>() {
                    @Override
                    public String exec(final HistoriqueDecompte e) {
                        return e.getDecompte().getId();
                    }
                });

        for (Decompte decompte : decomptes) {
            String id = decompte.getId();
            if (map.containsKey(id)) {
                decompte.setHistoriques(map.get(id));
            }
        }
    }

    private void loadDecomptesSalairesWithDependencies(final List<Decompte> decomptes, final List<String> ids) {
        SearchLotExecutor<DecompteSalaire> executor = new SearchLotExecutor<DecompteSalaire>() {
            @Override
            public List<DecompteSalaire> execute(final List<String> ids) {
                return VulpeculaRepositoryLocator.getDecompteSalaireRepository().findByIdsDecomptesInWithDependencies(
                        ids);
            }
        };
        loadDecompteSalaires(executor, decomptes, ids);
    }

    private void loadDecomptesSalaires(final List<Decompte> decomptes, final List<String> ids) {
        SearchLotExecutor<DecompteSalaire> executor = new SearchLotExecutor<DecompteSalaire>() {
            @Override
            public List<DecompteSalaire> execute(final List<String> ids) {
                return VulpeculaRepositoryLocator.getDecompteSalaireRepository().findByIdsDecomptesIn(ids);
            }
        };
        loadDecompteSalaires(executor, decomptes, ids);
    }

    private void loadDecompteSalaires(SearchLotExecutor<DecompteSalaire> executor, final List<Decompte> decomptes,
            final List<String> ids) {
        List<DecompteSalaire> decomptesSalaires = RepositoryJade.searchByLot(ids, executor);

        Map<String, List<DecompteSalaire>> map = JadeListUtil.groupBy(decomptesSalaires,
                new JadeListUtil.Key<DecompteSalaire>() {
                    @Override
                    public String exec(final DecompteSalaire e) {
                        return e.getDecompte().getId();
                    }
                });

        for (Decompte decompte : decomptes) {
            String id = decompte.getId();
            if (map.containsKey(id)) {
                decompte.setLignes(map.get(id));
            }
        }
    }

    private static enum LoadOptions {
        NOT_LOAD_DECOMPTE_SALAIRE_DEPENDENCIES,
        NOT_LOAD_HISTORIQUE
    }
}
