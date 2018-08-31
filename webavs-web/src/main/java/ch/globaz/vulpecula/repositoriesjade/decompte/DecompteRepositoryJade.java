package ch.globaz.vulpecula.repositoriesjade.decompte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.business.models.decomptes.DecompteComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteComptableComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteComptableSearchComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteSearchComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteSimpleModel;
import ch.globaz.vulpecula.business.models.decomptes.HistoriqueDecompteComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.HistoriqueDecompteSearchComplexModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.PeriodeMensuelle;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.HistoriqueDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeProvenance;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteRepository;
import ch.globaz.vulpecula.external.exceptions.ViewException;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.converters.DecompteConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.converters.HistoriqueDecompteConverter;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeSearchComplexModel;

/***
 * Implémentation Jade de {@link DecompteRepository}.
 * 
 */
public class DecompteRepositoryJade extends RepositoryJade<Decompte, DecompteComplexModel, DecompteSimpleModel>
        implements DecompteRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(DecompteRepositoryJade.class);

    @Override
    public void delete(final Decompte entity) {
        try {
            VulpeculaServiceLocator.getDecompteService().notifierSynchronisationEbu(entity.getId(),
                    entity.getIdEmployeur(), entity.getType());
        } catch (JadePersistenceException e) {
            LOGGER.error(e.getMessage());
        }
        if (entity.isNotEbusiness()) {
            super.delete(entity);
            return;
        }

        if (!entity.isPeriodique()) {
            super.delete(entity);
            return;
        }

        List<Decompte> liste = findComplementaireByIdEmployeurAndPeriode(entity.getIdEmployeur(), entity.getAnnee());
        if (!liste.isEmpty()) {
            throw new ViewException(SpecificationMessage.DECOMPTE_COMPLEMENTAIRE_EXISTANT);
        }

        super.delete(entity);
    }

    @Override
    public Decompte create(Decompte entity) {
        Decompte decompte = super.create(entity);
        try {
            VulpeculaServiceLocator.getDecompteService().notifierSynchronisationEbu(decompte.getId(),
                    decompte.getIdEmployeur(), decompte.getType());
        } catch (JadePersistenceException e) {
            LOGGER.error(e.getMessage());
        }
        return decompte;
    }

    @Override
    public Decompte update(Decompte entity) {
        // On regarde si il y a changement de status et nécessite une annonce ebusiness
        Decompte ancienDecompte = VulpeculaRepositoryLocator.getDecompteRepository().findById(entity.getId());
        if (!ancienDecompte.getEtat().getValue().equals(entity.getEtat().getValue())) {
            // il y a changement d'état donc on doit annoncer
            try {
                VulpeculaServiceLocator.getDecompteService().notifierSynchronisationEbu(entity.getId(),
                        entity.getIdEmployeur(), entity.getType());
            } catch (JadePersistenceException e) {
                LOGGER.error(e.getMessage());
            }
        }
        return super.update(entity);
    }

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
        return searchAndFetchDependencies(search, LoadOptions.ORDER_BY_TRAVAILLEUR);
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
        DecompteSearchComplexModel search = new DecompteSearchComplexModel();
        search.setForId(idDecompte);
        search.setOrderKey(DecompteSearchComplexModel.ORDER_BY_RAISON_SOCIAL_PERIODE_DEBUT_ASC);
        List<Decompte> listDecompte = searchAndFetchDependencies(search, LoadOptions.ORDER_BY_TRAVAILLEUR);
        if (listDecompte != null && listDecompte.size() > 0) {
            return listDecompte.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Decompte> findDecomptesForFacturation(Date dateLimite) {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setInEtats(EtatDecompte.RECTIFIE, EtatDecompte.VALIDE);
        searchModel.setForDateAu(dateLimite);
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
    public List<Decompte> findByIdEmployeurOrderByPeriodeDebutDesc(String idEmployeur, String idDecompte,
            String numeroDecompte, String type) {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        if (!JadeStringUtil.isEmpty(idDecompte)) {
            searchModel.setForIdGreater(idDecompte);
            searchModel.setOrderKey(DecompteSearchComplexModel.ORDER_BY_PERIODE_DESC);
        }
        searchModel.setLikeNoDecompte(numeroDecompte);
        searchModel.setForType(type);
        return searchAndFetchDependencies(searchModel, LoadOptions.NOT_LOAD_DECOMPTE_SALAIRE_DEPENDENCIES);
    }

    @Override
    public List<Decompte> findByIdEmployeur(final String idEmployeur, String idDecompte, String numeroDecompte,
            String type) {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        if (!JadeStringUtil.isEmpty(idDecompte)) {
            searchModel.setForIdGreater(idDecompte);
            searchModel.setOrderKey(DecompteSearchComplexModel.ORDER_BY_ID_ASC);
        }
        searchModel.setLikeNoDecompte(numeroDecompte);
        searchModel.setForType(type);
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
    public List<Decompte> findByIdEmployeurAndPeriodeWithDependencies(final String idEmployeur, String dateDebut,
            String dateFin) {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForAnneeDebut(dateDebut);
        searchModel.setForAnneeFin(dateFin);
        return searchAndFetchDependencies(searchModel, LoadOptions.NOT_LOAD_HISTORIQUE);
    }

    @Override
    public List<Decompte> findByIdEmployeurAndPeriodeWithDependencies(final String idEmployeur, String dateDebut,
            String dateFin, EtatDecompte etat) {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForAnneeDebut(dateDebut);
        searchModel.setForAnneeFin(dateFin);
        searchModel.setForEtat(etat);
        return searchAndFetchDependencies(searchModel, LoadOptions.NOT_LOAD_HISTORIQUE);
    }

    @Override
    public List<Decompte> findByIdEmployeurAndPeriode(final String idEmployeur, String dateDebut, String dateFin,
            EtatDecompte etat, LoadOptions... options) {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForAnneeDebut(dateDebut);
        searchModel.setForAnneeFin(dateFin);
        searchModel.setInTypes(TypeDecompte.PERIODIQUE, TypeDecompte.SPECIAL_SALAIRE, TypeDecompte.CONTROLE_EMPLOYEUR,
                TypeDecompte.COMPLEMENTAIRE);
        if (etat != null) {
            searchModel.setForEtat(etat);
        }
        return searchAndFetchDependencies(searchModel, options);
    }

    @Override
    public List<Decompte> findByIdEmployeurForPeriode(final String idEmployeur, PeriodeMensuelle periode) {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setFromDateDebut(periode.getPeriodeDebut());
        searchModel.setToDateFin(periode.getPeriodeFin());
        return searchAndFetchDependencies(searchModel);
    }

    @Override
    public List<Decompte> findComplementaireByIdEmployeurAndPeriode(String idEmployeur, Annee annee) {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForType(TypeDecompte.COMPLEMENTAIRE);
        searchModel.setFromDateDebut(annee.getFirstDayOfYear());
        searchModel.setToDateFin(annee.getLastDayOfYear());
        searchModel.setForNotEtat(EtatDecompte.ANNULE);
        return searchAndFetch(searchModel);
    }

    @Override
    public List<Decompte> findComplementaireByIdEmployeurAndPeriodeWithDependencies(String idEmployeur, Annee annee) {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForType(TypeDecompte.COMPLEMENTAIRE);
        searchModel.setFromDateDebut(annee.getFirstDayOfYear());
        searchModel.setToDateFin(annee.getLastDayOfYear());
        searchModel.setForNotEtat(EtatDecompte.ANNULE);
        return searchAndFetchDependencies(searchModel, LoadOptions.NOT_LOAD_HISTORIQUE);
    }

    @Override
    public List<Decompte> findByIdEmployeurAndPeriodeForCPP(final String idEmployeur, String dateDebut, String dateFin,
            EtatDecompte etat, LoadOptions... options) {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle(dateDebut, dateFin);
        searchModel.setForPeriode(periodeMensuelle);
        searchModel.setInTypes(TypeDecompte.PERIODIQUE, TypeDecompte.SPECIAL_SALAIRE, TypeDecompte.CONTROLE_EMPLOYEUR,
                TypeDecompte.CPP);
        if (etat != null) {
            searchModel.setForEtat(etat);
        }
        return searchAndFetchDependencies(searchModel, options);
    }

    @Override
    public List<Decompte> findByIdEmployeurAndPeriodeForCPPComplementaire(final String idEmployeur, String dateDebut,
            String dateFin, EtatDecompte etat, LoadOptions... options) {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForAnneeDebut(dateDebut);
        searchModel.setForAnneeFin(dateFin);
        searchModel.setInTypes(TypeDecompte.COMPLEMENTAIRE);
        if (etat != null) {
            searchModel.setForEtat(etat);
        }
        return searchAndFetchDependencies(searchModel, options);
    }

    @Override
    public List<Decompte> findByIdEmployeurAndPeriodeWithDependenciesOrderByTravailleur(final String idEmployeur,
            String dateDebut, String dateFin) {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForAnneeDebut(dateDebut);
        searchModel.setForAnneeFin(dateFin);
        return searchAndFetchDependencies(searchModel, LoadOptions.ORDER_BY_TRAVAILLEUR);
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
        return searchAndFetchDependencies(searchModel);
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
        searchModel.setForType(TypeDecompte.PERIODIQUE);
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
    public Decompte findDecompteForAnnee(Employeur employeur, Annee annee, TypeDecompte type) {
        PeriodeMensuelle periode = new PeriodeMensuelle(new Date(1, 1, annee.getValue()), new Date(31, 12,
                annee.getValue()));
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setForIdEmployeur(employeur.getId());
        searchModel.setForType(type);
        searchModel.setForPeriode(periode);
        searchModel.setForNotEtat(EtatDecompte.ANNULE);
        return searchAndFetchFirst(searchModel);
    }

    @Override
    public Decompte findDecompteIdentiqueEBusiness(Employeur employeur, PeriodeMensuelle periodeMensuelle,
            TypeDecompte type) {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        searchModel.setForIdEmployeur(employeur.getId());
        searchModel.setForType(type);
        searchModel.setForPeriode(periodeMensuelle);
        searchModel.setForTypeProvenance(TypeProvenance.EBUSINESS);
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

        if (loadOptions.contains(LoadOptions.ORDER_BY_TRAVAILLEUR)) {
            loadDecomptesSalairesWithDependenciesOrderByTravailleur(decomptes, ids);
        } else {

            if (!loadOptions.contains(LoadOptions.NOT_LOAD_DECOMPTE_SALAIRE_DEPENDENCIES)) {
                loadDecomptesSalairesWithDependencies(decomptes, ids);
            } else {
                loadDecomptesSalaires(decomptes, ids);
            }
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

    private void loadDecomptesSalairesWithDependenciesOrderByTravailleur(final List<Decompte> decomptes,
            final List<String> ids) {
        SearchLotExecutor<DecompteSalaire> executor = new SearchLotExecutor<DecompteSalaire>() {
            @Override
            public List<DecompteSalaire> execute(final List<String> ids) {
                return VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                        .findByIdsDecomptesInWithDependenciesOrderByTravailleur(ids);
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

    @Override
    public List<Decompte> findByIdEmployeurForAnneeComptable(final String idEmployeur, Date periodeDebut,
            Date periodeFin) {
        List<Decompte> decomptes = new ArrayList<Decompte>();
        decomptes.addAll(findDecomptesRepriseByIdEmployeur(idEmployeur, periodeDebut, periodeFin));
        decomptes.addAll(findDecomptesByIdEmployeur(idEmployeur, periodeDebut, periodeFin));
        return decomptes;
    }

    /**
     * Retourne les décomptes comptabilisés historiques par rapport à l'année comptable passée en paramètre.
     * 
     * @param idEmployeur String représentant l'id d'un employeur
     * @param anneeComptable Année comptable
     * @return Liste de décomptes
     */
    private List<Decompte> findDecomptesRepriseByIdEmployeur(final String idEmployeur, Date periodeDebut,
            Date periodeFin) {
        HistoriqueDecompteSearchComplexModel searchModel = new HistoriqueDecompteSearchComplexModel();
        searchModel.setForEtat(EtatDecompte.COMPTABILISE.getValue());
        searchModel.setForAnneeDebut(periodeDebut.toString());
        searchModel.setForAnneeFin(periodeFin.toString());
        searchModel.setForIdEmployeur(idEmployeur);

        List<HistoriqueDecompteComplexModel> historiquesComplexModel = RepositoryJade.searchForAndFetch(searchModel);

        List<Decompte> decomptes = new ArrayList<Decompte>();
        for (HistoriqueDecompteComplexModel historiqueDecompteComplexModel : historiquesComplexModel) {
            HistoriqueDecompte historiqueDecompte = HistoriqueDecompteConverter.getInstance().convertToDomain(
                    historiqueDecompteComplexModel);
            if (historiqueDecompte.isReprise()) {
                decomptes.add(historiqueDecompte.getDecompte());
            }
        }
        return decomptes;
    }

    /**
     * Retourne les décomptes comptabilisés par rapport à l'année comptable passée en paramètre.
     * 
     * @param idEmployeur String représentant l'id d'un employeur
     * @param anneeComptable Année comptable
     * @return Liste de décomptes
     */
    private List<Decompte> findDecomptesByIdEmployeur(final String idEmployeur, Date periodeDebut, Date periodeFin) {
        DecompteComptableSearchComplexModel searchModel = new DecompteComptableSearchComplexModel();
        searchModel.setForEtat(EtatDecompte.COMPTABILISE.getValue());
        searchModel.setForDatePassageGreaterOfEquals(periodeDebut.getSwissValue());
        searchModel.setForDatePassageLessOrEquals(periodeFin.getSwissValue());
        searchModel.setForIdEmployeur(idEmployeur);
        List<DecompteComptableComplexModel> decomptesComplexModel = RepositoryJade.searchForAndFetch(searchModel);
        List<Decompte> decomptes = new ArrayList<Decompte>();
        for (DecompteComptableComplexModel decompteComplexModel : decomptesComplexModel) {
            Decompte decompte = DecompteConverter.getInstance().convertToDomain(decompteComplexModel);
            if (!decompte.isReprise()) {
                decomptes.add(decompte);
            }
        }
        return decomptes;
    }

    public static enum LoadOptions {
        NOT_LOAD_DECOMPTE_SALAIRE_DEPENDENCIES,
        NOT_LOAD_HISTORIQUE,
        ORDER_BY_TRAVAILLEUR
    }

    @Override
    public List<Decompte> findOuvertGenereATraiterByIdEmployeur(String idEmployeur, Date date) {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        EtatDecompte[] etats = new EtatDecompte[] { EtatDecompte.OUVERT, EtatDecompte.GENERE, EtatDecompte.A_TRAITER,
                EtatDecompte.SOMMATION };
        searchModel.setInEtats(etats);
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForDateDe(date);
        return searchAndFetchDependencies(searchModel, LoadOptions.NOT_LOAD_DECOMPTE_SALAIRE_DEPENDENCIES);
    }

    @Override
    public List<Decompte> findOuvertGenereATraiterByIdEmployeurPeriode(String idEmployeur, PeriodeMensuelle periode) {
        DecompteSearchComplexModel searchModel = new DecompteSearchComplexModel();
        EtatDecompte[] etats = new EtatDecompte[] { EtatDecompte.OUVERT, EtatDecompte.GENERE, EtatDecompte.A_TRAITER,
                EtatDecompte.SOMMATION };
        searchModel.setInEtats(etats);
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setFromDateDebut(periode.getPeriodeDebut());
        searchModel.setToDateFin(periode.getPeriodeFin());
        return searchAndFetchDependencies(searchModel);
    }
}
