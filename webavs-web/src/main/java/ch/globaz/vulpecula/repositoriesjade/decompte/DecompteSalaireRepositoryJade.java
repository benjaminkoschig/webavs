package ch.globaz.vulpecula.repositoriesjade.decompte;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.sql.QueryExecutor;
import ch.globaz.vulpecula.business.models.decomptes.AbsenceComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.AbsenceSearchComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.AbsenceSearchSimpleModel;
import ch.globaz.vulpecula.business.models.decomptes.AbsenceSimpleModel;
import ch.globaz.vulpecula.business.models.decomptes.CodeErreurDecompteSalaireComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.CodeErreurDecompteSalaireSearchComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.CodeErreurDecompteSalaireSearchSimpleModel;
import ch.globaz.vulpecula.business.models.decomptes.CodeErreurDecompteSalaireSimpleModel;
import ch.globaz.vulpecula.business.models.decomptes.CotisationDecompteComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.CotisationDecompteSearchComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.CotisationDecompteSimpleModel;
import ch.globaz.vulpecula.business.models.decomptes.LigneDecompteComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.LigneDecompteSearchComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.LigneDecompteSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.AnneeComptable;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.communicationsalaires.CommunicationSalairesRetaval;
import ch.globaz.vulpecula.domain.models.decompte.Absence;
import ch.globaz.vulpecula.domain.models.decompte.CodeErreurDecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.CotisationDecompte;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.models.postetravail.TravailleurEbuDomain;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.domain.models.registre.ConventionQualification;
import ch.globaz.vulpecula.domain.models.registre.Personnel;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteSalaireRepository;
import ch.globaz.vulpecula.external.models.pyxis.Pays;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.converters.AbsenceConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.converters.CodeErreurDecompteSalaireConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.converters.CotisationDecompteConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.converters.DecompteSalaireConverter;
import ch.globaz.vulpecula.util.DBUtil;
import ch.globaz.vulpecula.ws.bean.StatusAnnonceTravailleurEbu;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.model.JadeSearchComplexModel;

/***
 * Implémentation Jade de {@link DecompteSalaireRepository}
 *
 */
public class DecompteSalaireRepositoryJade
        extends RepositoryJade<DecompteSalaire, LigneDecompteComplexModel, LigneDecompteSimpleModel>
        implements DecompteSalaireRepository {

    private final Logger LOGGER = LoggerFactory.getLogger(DecompteSalaireRepositoryJade.class);

    static enum LoadOptions {
        ABSENCES,
        COTISATIONS,
        NONE,
        CODE_ERREUR
    }

    @Override
    public List<DecompteSalaire> findByIdDecompteWithDependencies(final String idDecompte) {
        LigneDecompteSearchComplexModel searchModel = new LigneDecompteSearchComplexModel();
        searchModel.setForIdDecompte(idDecompte);
        return searchAndLoadDependencies(searchModel);
    }

    @Override
    public List<DecompteSalaire> findWithDependencies(String idDecompte, String idPosteTravail, String nom) {
        LigneDecompteSearchComplexModel searchModel = new LigneDecompteSearchComplexModel();
        searchModel.setForIdDecompte(idDecompte);
        searchModel.setForIdPosteTravail(idPosteTravail);
        searchModel.setForDesignationUpperLike(nom);
        searchModel.setOrderKey(LigneDecompteSearchComplexModel.ORDER_BY_TRAVAILLEUR);
        return searchAndLoadDependencies(searchModel);
    }

    @Override
    public List<DecompteSalaire> findByIdWithCotisations(String idDecompte, String idPosteTravail) {
        LigneDecompteSearchComplexModel searchModel = new LigneDecompteSearchComplexModel();
        searchModel.setForIdDecompte(idDecompte);
        searchModel.setForIdPosteTravail(idPosteTravail);
        List<DecompteSalaire> decompteSalaires = searchAndFetch(searchModel);
        loadCotisations(decompteSalaires);
        return decompteSalaires;
    }

    @Override
    public DecompteSalaire create(DecompteSalaire decompteSalaire) {
        DecompteSalaire decompteSalaire2 = super.create(decompteSalaire);
        saveCotisationsDecompte(decompteSalaire2);
        saveAbsences(decompteSalaire2, false);
        saveCodesErreur(decompteSalaire2, true);
        try {
            VulpeculaServiceLocator.getDecompteService().notifierSynchronisationEbu(
                    decompteSalaire2.getDecompte().getId(), decompteSalaire2.getIdEmployeur(),
                    decompteSalaire2.getDecompte().getType());
        } catch (JadePersistenceException e) {
            LOGGER.error(e.getMessage());
        }
        return decompteSalaire2;
    }

    @Override
    public DecompteSalaire createWithoutCotisations(DecompteSalaire decompteSalaire) {
        DecompteSalaire decompteSalaire2 = super.create(decompteSalaire);
        // saveCotisationsDecompte(decompteSalaire2);
        saveAbsences(decompteSalaire2, false);
        saveCodesErreur(decompteSalaire2, true);
        try {
            VulpeculaServiceLocator.getDecompteService().notifierSynchronisationEbu(
                    decompteSalaire2.getDecompte().getId(), decompteSalaire2.getIdEmployeur(),
                    decompteSalaire2.getDecompte().getType());
        } catch (JadePersistenceException e) {
            LOGGER.error(e.getMessage());
        }
        return decompteSalaire2;
    }

    @Override
    public DecompteSalaire update(DecompteSalaire decompteSalaire) {
        DecompteSalaire decompteSalaire2 = super.update(decompteSalaire);
        saveCotisationsDecompte(decompteSalaire2);
        saveAbsences(decompteSalaire2);
        saveCodesErreur(decompteSalaire2, true);
        // Si c'est un nouveau travailleur et que le poste est quittancé
        if ((!JadeStringUtil.isEmpty(decompteSalaire.getCorrelationId())) && (!decompteSalaire.isaTraiter())) {
            TravailleurEbuDomain travailleur = VulpeculaRepositoryLocator.getNouveauTravailleurRepository()
                    .findByCorrelationId(decompteSalaire.getCorrelationId());
            if (travailleur != null && !travailleur.isTraite()) {
                // travailleur.setTraite(true);
                travailleur.setStatus(StatusAnnonceTravailleurEbu.TRAITE);
                VulpeculaRepositoryLocator.getNouveauTravailleurRepository().update(travailleur);
            }
        }
        try {
            VulpeculaServiceLocator.getDecompteService().notifierSynchronisationEbu(
                    decompteSalaire2.getDecompte().getId(), decompteSalaire2.getIdEmployeur(),
                    decompteSalaire2.getDecompte().getType());
        } catch (JadePersistenceException e) {
            LOGGER.error(e.getMessage());
        }
        return decompteSalaire2;
    }

    @Override
    public DecompteSalaire updateDateAnnonce(DecompteSalaire decompteSalaire) {
        return super.update(decompteSalaire);
    }

    @Override
    public void delete(final DecompteSalaire entity) {
        try {
            DecompteSalaire decompteSalaire = VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                    .findById(entity.getId());
            VulpeculaServiceLocator.getDecompteService().notifierSynchronisationEbu(
                    decompteSalaire.getDecompte().getId(), decompteSalaire.getDecompte().getIdEmployeur(),
                    decompteSalaire.getDecompte().getType());
        } catch (JadePersistenceException e) {
            LOGGER.error(e.getMessage());
        }
        super.delete(entity);
    }

    @Override
    public List<DecompteSalaire> findByIdDecompte(final String idDecompte) {
        LigneDecompteSearchComplexModel searchModel = new LigneDecompteSearchComplexModel();
        searchModel.setForIdDecompte(idDecompte);
        return searchAndFetch(searchModel);
    }

    @Override
    public List<DecompteSalaire> findByNoDecompteAndIdPosteTravail(final String noDecompte,
            final String idPosteTravail) {
        LigneDecompteSearchComplexModel searchModel = new LigneDecompteSearchComplexModel();
        searchModel.setForNumeroDecompte(noDecompte);
        searchModel.setForIdPosteTravail(idPosteTravail);
        return searchAndFetch(searchModel);
    }

    @Override
    public DecompteSalaire findAndfetchFirstByIdDecompte(final String idDecompte) {
        LigneDecompteSearchComplexModel searchModel = new LigneDecompteSearchComplexModel();
        searchModel.setForIdDecompte(idDecompte);
        return searchAndFetchFirst(searchModel);
    }

    @Override
    public List<DecompteSalaire> findByIdsDecomptesInWithDependencies(final List<String> idsDecomptes) {
        LigneDecompteSearchComplexModel searchModel = new LigneDecompteSearchComplexModel();
        searchModel.setForIdsDecomptesIn(idsDecomptes);
        return searchAndLoadDependencies(searchModel);
    }

    @Override
    public List<DecompteSalaire> findByIdsDecomptesInWithDependenciesOrderByTravailleur(
            final List<String> idsDecomptes) {
        LigneDecompteSearchComplexModel searchModel = new LigneDecompteSearchComplexModel();
        searchModel.setForIdsDecomptesIn(idsDecomptes);
        searchModel.setOrderKey(LigneDecompteSearchComplexModel.ORDER_BY_TRAVAILLEUR);
        return searchAndLoadDependencies(searchModel);
    }

    @Override
    public List<DecompteSalaire> findByIdEmployeurAndPeriodeWithDependenciesOrderByTravailleur(String idEmployeur,
            Date periodeDebut, Date periodeFin) {
        LigneDecompteSearchComplexModel searchModel = new LigneDecompteSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForPeriodeDebut(periodeDebut.getSwissValue());
        searchModel.setForPeriodeFin(periodeFin.getSwissValue());
        searchModel.setOrderKey(LigneDecompteSearchComplexModel.ORDER_BY_TRAVAILLEUR);
        return searchAndLoadDependencies(searchModel);
    }

    @Override
    public List<DecompteSalaire> findCPPByIdEmployeurAndPeriodeWithDependenciesOrderByTravailleur(String idEmployeur,
            Annee anneeDebut, Annee anneeFin) {
        LigneDecompteSearchComplexModel searchModel = new LigneDecompteSearchComplexModel();
        searchModel.setForIdEmployeur(idEmployeur);
        searchModel.setForTypeDecompteIn(TypeDecompte.CPP);
        searchModel.setForAnneeCotisationsGreaterOrEquals(anneeDebut);
        searchModel.setForAnneeCotisationsLessOrEquals(anneeFin);
        searchModel.setOrderKey(LigneDecompteSearchComplexModel.ORDER_BY_TRAVAILLEUR);
        return searchAndLoadDependencies(searchModel);
    }

    @Override
    public List<DecompteSalaire> findByIdsDecomptesIn(List<String> ids) {
        LigneDecompteSearchComplexModel searchModel = new LigneDecompteSearchComplexModel();
        searchModel.setForIdsDecomptesIn(ids);
        return searchAndFetch(searchModel);
    }

    @Override
    public List<DecompteSalaire> findByIdPosteTravail(final String idPosteTravail) {
        LigneDecompteSearchComplexModel searchModel = new LigneDecompteSearchComplexModel();
        searchModel.setForIdPosteTravail(idPosteTravail);
        return searchAndLoadDependencies(searchModel);
    }

    @Override
    public List<DecompteSalaire> findByIdPosteTravail(final Collection<String> idPostes, Date dateDebut, Date dateFin) {
        if (idPostes.isEmpty()) {
            return new ArrayList<DecompteSalaire>();
        }

        LigneDecompteSearchComplexModel searchModel = new LigneDecompteSearchComplexModel();
        searchModel.setForIdsPostesTravailIn(idPostes);

        searchModel.setForPeriodeDebut(dateDebut);
        searchModel.setForPeriodeFin(dateFin);
        return searchAndFetch(searchModel);
    }

    @Override
    public List<DecompteSalaire> findByIdPosteTravailOneYearAgo(final String idPosteTravail) {
        LigneDecompteSearchComplexModel searchModel = new LigneDecompteSearchComplexModel();
        searchModel.setForIdPosteTravail(idPosteTravail);
        searchModel.setForPeriodeDebut(Date.now().addYear(-1));
        searchModel.setOrderKey(LigneDecompteSearchComplexModel.DATE_FIN_DECOMPTE_DESC);
        return searchAndLoadDependencies(searchModel);
    }

    @Override
    public List<DecompteSalaire> findLignesDecomptesByIdTravailleur(final String idTravailleur) {
        LigneDecompteSearchComplexModel searchModel = new LigneDecompteSearchComplexModel();
        searchModel.setForIdTravailleur(idTravailleur);
        return searchAndLoadDependencies(searchModel);
    }

    @Override
    public List<DecompteSalaire> findLignesDecomptesSansCotisationsByIdTravailleur(String idTravailleur,
            String idDecompte, String raisonSociale, String numeroDecompte, String type) {
        LigneDecompteSearchComplexModel searchComplexModel = new LigneDecompteSearchComplexModel();
        if (!JadeStringUtil.isEmpty(idDecompte)) {
            searchComplexModel.setForIdDecompteGreater(idDecompte);
            searchComplexModel.setOrderKey(LigneDecompteSearchComplexModel.ID_DECOMPTE_ASC);
        }
        searchComplexModel.setForIdTravailleur(idTravailleur);
        searchComplexModel.setForRaisonSocialeUpperLike(raisonSociale);
        searchComplexModel.setLikeNumeroDecompte(numeroDecompte);
        if (!JadeStringUtil.isEmpty(type)) {
            searchComplexModel.setForTypeDecompteIn(Arrays.asList(type));
        }
        List<DecompteSalaire> salaires = searchAndFetch(searchComplexModel);
        loadDependencies(salaires, LoadOptions.ABSENCES);
        return salaires;
    }

    @Override
    public DecompteSalaire findNextByIdDecompteAndSequence(final String idDecompte, final String currentSequence) {
        LigneDecompteSearchComplexModel searchModel = getLigneDecompteSearchModel(idDecompte, currentSequence,
                SearchDirection.NEXT);
        return searchFirstAndLoadDependencies(searchModel);
    }

    @Override
    public DecompteSalaire findNextAQuittancerByIdDecompteAndSequence(String idDecompte, String currentSequence) {
        LigneDecompteSearchComplexModel searchModel = getLigneDecompteSearchModel(idDecompte, currentSequence,
                SearchDirection.NEXT);
        searchModel.setForToTreat(true);
        return searchFirstAndLoadDependencies(searchModel);
    }

    @Override
    public DecompteSalaire findPreviousByIdDecompteAndSequence(final String idDecompte, final String currentSequence) {
        LigneDecompteSearchComplexModel searchModel = getLigneDecompteSearchModel(idDecompte, currentSequence,
                SearchDirection.PREVIOUS);
        return searchFirstAndLoadDependencies(searchModel);
    }

    @Override
    public DecompteSalaire findById(final String id) {
        LigneDecompteSearchComplexModel search = new LigneDecompteSearchComplexModel();
        search.setForId(id);
        return searchFirstAndLoadDependencies(search);
    }

    @Override
    public DecompteSalaire findByIdWithoutDependencies(final String id) {
        LigneDecompteSearchComplexModel search = new LigneDecompteSearchComplexModel();
        search.setForId(id);
        DecompteSalaire ds = searchAndFetchFirst(search);
        loadDependencies(Arrays.asList(ds), LoadOptions.NONE);
        return ds;
    }

    @Override
    public DecompteSalaire findPrecedentComptabilise(String idPosteTravail, Date dateReception) {
        LigneDecompteSearchComplexModel search = new LigneDecompteSearchComplexModel();
        search.setForIdPosteTravail(idPosteTravail);
        search.setBeforeDateFinDecompte(dateReception);
        search.setForEtatDecompte(EtatDecompte.COMPTABILISE);
        search.setForTypeDecompteIn(TypeDecompte.PERIODIQUE);
        search.setOrderKey(LigneDecompteSearchComplexModel.DATE_FIN_DECOMPTE_DESC);
        return searchAndFetchFirst(search);
    }

    @Override
    public DecompteSalaire findPrecedentValide(String idPosteTravail, Date dateReception) {
        LigneDecompteSearchComplexModel search = new LigneDecompteSearchComplexModel();
        search.setForIdPosteTravail(idPosteTravail);
        search.setBeforeDateFinDecompte(dateReception);
        search.setForEtatDecompteIn(Arrays.asList(EtatDecompte.COMPTABILISE.getValue(),
                EtatDecompte.FACTURATION.getValue(), EtatDecompte.VALIDE.getValue(), EtatDecompte.RECTIFIE.getValue()));
        search.setForTypeDecompteIn(TypeDecompte.PERIODIQUE);
        search.setOrderKey(LigneDecompteSearchComplexModel.DATE_FIN_DECOMPTE_DESC);
        return searchAndFetchFirst(search);
    }

    @Override
    public DomaineConverterJade<DecompteSalaire, LigneDecompteComplexModel, LigneDecompteSimpleModel> getConverter() {
        return new DecompteSalaireConverter();
    }

    @Override
    public boolean isLastDecompteSalaire(final String idDecompte, final String currentSequence) {
        if (currentSequence == null || currentSequence.length() == 0) {
            return true;
        }
        LigneDecompteSearchComplexModel searchModel = getLigneDecompteSearchModel(idDecompte, currentSequence,
                SearchDirection.NEXT);
        return count(searchModel) < 1;
    }

    @Override
    public boolean isLastDecompteSalaireAQuittancer(final String idDecompte, final String currentSequence) {
        if (currentSequence == null || currentSequence.length() == 0) {
            return true;
        }
        LigneDecompteSearchComplexModel searchModel = getLigneDecompteSearchModel(idDecompte, currentSequence,
                SearchDirection.NEXT);
        searchModel.setForToTreat(true);
        return count(searchModel) < 1;
    }

    @Override
    public boolean isFirstDecompteSalaire(final String idDecompte, final String currentSequence) {
        if (currentSequence == null || currentSequence.length() == 0) {
            return true;
        }
        LigneDecompteSearchComplexModel searchModel = getLigneDecompteSearchModel(idDecompte, currentSequence,
                SearchDirection.PREVIOUS);
        return count(searchModel) < 1;
    }

    @Override
    public void saveCotisationsDecompte(DecompteSalaire decompteSalaire) {
        for (CotisationDecompte cotisationDecompte : decompteSalaire.getCotisationsDecompte()) {
            CotisationDecompteSimpleModel cotisationDecompteSimpleModel = CotisationDecompteConverter
                    .convertToPersistence(cotisationDecompte, decompteSalaire);
            try {
                if (JadeStringUtil.isBlankOrZero(cotisationDecompteSimpleModel.getSpy())) {
                    JadePersistenceManager.add(cotisationDecompteSimpleModel);
                } else {
                    JadePersistenceManager.update(cotisationDecompteSimpleModel);
                }
            } catch (JadePersistenceException ex) {
                LOGGER.error("Décompte salaire : " + decompteSalaire.getId() + " pour cotisation : "
                        + cotisationDecompte.toString(), ex);
            }
        }
    }

    @Override
    public void deleteCotisationsDecompte(DecompteSalaire decompteSalaire) {
        try {
            for (CotisationDecompte cotisationDecompte : decompteSalaire.getCotisationsDecompte()) {
                CotisationDecompteSimpleModel cotisationDecompteSimpleModel = CotisationDecompteConverter
                        .convertToPersistence(cotisationDecompte, decompteSalaire);
                JadePersistenceManager.delete(cotisationDecompteSimpleModel);
            }
        } catch (JadePersistenceException ex) {
            ex.printStackTrace();
        }
    }

    private void saveAbsences(DecompteSalaire decompteSalaire) {
        saveAbsences(decompteSalaire, true);
    }

    private void saveAbsences(DecompteSalaire decompteSalaire, boolean withDelete) {
        try {
            AbsenceSearchSimpleModel searchSimpleModel = new AbsenceSearchSimpleModel();
            searchSimpleModel.setForIdDecompteSalaire(decompteSalaire.getId());
            if (withDelete) {
                JadePersistenceManager.delete(searchSimpleModel);
            }
            for (Absence absence : decompteSalaire.getAbsences()) {
                AbsenceSimpleModel absenceSimpleModel = AbsenceConverter.convertToPersistence(absence, decompteSalaire);

                if (JadeStringUtil.isBlankOrZero(absenceSimpleModel.getSpy()) || withDelete) {
                    JadePersistenceManager.add(absenceSimpleModel);
                } else {
                    JadePersistenceManager.update(absenceSimpleModel);
                }
            }
        } catch (JadePersistenceException ex) {
            ex.printStackTrace();
        }
    }

    private void saveCodesErreur(DecompteSalaire decompteSalaire, boolean withDelete) {
        try {
            CodeErreurDecompteSalaireSearchSimpleModel searchSimpleModel = new CodeErreurDecompteSalaireSearchSimpleModel();
            searchSimpleModel.setForIdDecompteSalaire(decompteSalaire.getId());
            if (withDelete) {
                JadePersistenceManager.delete(searchSimpleModel);
            }
            for (CodeErreurDecompteSalaire code : decompteSalaire.getListeCodeErreur()) {
                // On supprimme les codes erreurs précédemment enregistré pour la ligne de décompte
                CodeErreurDecompteSalaireSimpleModel codeSimpleModel = CodeErreurDecompteSalaireConverter
                        .convertToPersistence(code, decompteSalaire);
                JadePersistenceManager.add(codeSimpleModel);
            }
        } catch (JadePersistenceException ex) {
            ex.printStackTrace();
        }
    }

    private DecompteSalaire searchFirstAndLoadDependencies(final JadeSearchComplexModel searchModel) {
        DecompteSalaire decompteSalaire = searchAndFetchFirst(searchModel);
        List<DecompteSalaire> decomptesSalaires = Arrays.asList(decompteSalaire);
        loadDependencies(decomptesSalaires);
        return decomptesSalaires.get(0);
    }

    private List<DecompteSalaire> searchAndLoadDependencies(final JadeSearchComplexModel searchModel) {
        List<DecompteSalaire> decomptesSalaires = searchAndFetch(searchModel);
        loadDependencies(decomptesSalaires);
        return decomptesSalaires;
    }

    void loadDependencies(final List<DecompteSalaire> decomptesSalaires, LoadOptions... options) {
        List<LoadOptions> listOptions = Arrays.asList(options);
        List<String> ids = new ArrayList<String>();
        for (DecompteSalaire decompteSalaire : decomptesSalaires) {
            ids.add(decompteSalaire.getId());
        }

        if (listOptions.isEmpty() || listOptions.contains(LoadOptions.ABSENCES)) {
            loadAbsences(decomptesSalaires, ids);
        }
        if (listOptions.isEmpty() || listOptions.contains(LoadOptions.COTISATIONS)) {
            loadCotisations(decomptesSalaires, ids);
        }
        if (listOptions.isEmpty() || listOptions.contains(LoadOptions.CODE_ERREUR)) {
            loadCodeErreurDecompteSalaire(decomptesSalaires, ids);
        }
    }

    void loadAbsences(final List<DecompteSalaire> decomptesSalaires, final List<String> ids) {

        List<AbsenceComplexModel> absencesComplexModel = RepositoryJade.searchByLot(ids,
                new SearchLotExecutor<AbsenceComplexModel>() {
                    @Override
                    public List<AbsenceComplexModel> execute(final List<String> ids) {
                        AbsenceSearchComplexModel searchModel = new AbsenceSearchComplexModel();
                        searchModel.setInIdLigneDecompte(ids);
                        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                        try {
                            JadePersistenceManager.search(searchModel);
                        } catch (JadePersistenceException ex) {
                            ex.printStackTrace();
                        }
                        List<JadeAbstractModel> models = Arrays.asList(searchModel.getSearchResults());
                        List<AbsenceComplexModel> absences = (List<AbsenceComplexModel>) (List<?>) models;
                        return absences;
                    }
                });

        Map<String, List<AbsenceComplexModel>> map = JadeListUtil.groupBy(absencesComplexModel,
                new JadeListUtil.Key<AbsenceComplexModel>() {
                    @Override
                    public String exec(final AbsenceComplexModel e) {
                        return String.valueOf(e.getAbsenceSimpleModel().getIdLigneDecompte());
                    }
                });

        for (DecompteSalaire decompteSalaire : decomptesSalaires) {
            String idLigneDecompte = String.valueOf(decompteSalaire.getId());
            if (map.containsKey(idLigneDecompte)) {
                List<AbsenceComplexModel> absencesComplex = map.get(idLigneDecompte);
                List<Absence> absences = AbsenceConverter.convertToDomain(absencesComplex, decompteSalaire);
                decompteSalaire.setAbsences(absences);
            }
        }
    }

    void loadCodeErreurDecompteSalaire(final List<DecompteSalaire> decomptesSalaires, final List<String> ids) {
        List<CodeErreurDecompteSalaireComplexModel> codeErreurComplexModel = RepositoryJade.searchByLot(ids,
                new SearchLotExecutor<CodeErreurDecompteSalaireComplexModel>() {
                    @Override
                    public List<CodeErreurDecompteSalaireComplexModel> execute(final List<String> ids) {
                        CodeErreurDecompteSalaireSearchComplexModel searchModel = new CodeErreurDecompteSalaireSearchComplexModel();
                        searchModel.setInIdLigneDecompte(ids);
                        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                        try {
                            JadePersistenceManager.search(searchModel);
                        } catch (JadePersistenceException ex) {
                            JadeLogger.error(ex, ex.getMessage());
                        }
                        List<JadeAbstractModel> models = Arrays.asList(searchModel.getSearchResults());
                        List<CodeErreurDecompteSalaireComplexModel> codeErreurs = (List<CodeErreurDecompteSalaireComplexModel>) (List<?>) models;
                        return codeErreurs;
                    }
                });

        Map<String, List<CodeErreurDecompteSalaireComplexModel>> map = JadeListUtil.groupBy(codeErreurComplexModel,
                new JadeListUtil.Key<CodeErreurDecompteSalaireComplexModel>() {
                    @Override
                    public String exec(final CodeErreurDecompteSalaireComplexModel e) {
                        return String.valueOf(e.getCodeErreurDecompteSalaireSimpleModel().getIdLigneDecompte());
                    }
                });

        for (DecompteSalaire decompteSalaire : decomptesSalaires) {
            String idLigneDecompte = String.valueOf(decompteSalaire.getId());
            if (map.containsKey(idLigneDecompte)) {
                List<CodeErreurDecompteSalaireComplexModel> codeErreurComplex = map.get(idLigneDecompte);
                List<CodeErreurDecompteSalaire> codeErreurs = CodeErreurDecompteSalaireConverter
                        .convertToDomain(codeErreurComplex, decompteSalaire);
                decompteSalaire.setListeCodeErreur(codeErreurs);
            }
        }
    }

    void loadCotisations(final List<DecompteSalaire> decomptesSalaires) {
        List<String> ids = new ArrayList<String>();
        for (DecompteSalaire decompteSalaire : decomptesSalaires) {
            ids.add(decompteSalaire.getId());
        }
        loadCotisations(decomptesSalaires, ids);
    }

    void loadCotisations(final List<DecompteSalaire> decomptesSalaires, final List<String> ids) {
        List<CotisationDecompteComplexModel> cotisationsDecompte = RepositoryJade.searchByLot(ids,
                new SearchLotExecutor<CotisationDecompteComplexModel>() {
                    @Override
                    public List<CotisationDecompteComplexModel> execute(final List<String> ids) {
                        CotisationDecompteSearchComplexModel searchModel = new CotisationDecompteSearchComplexModel();
                        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                        searchModel.setInId(ids);
                        try {
                            JadePersistenceManager.search(searchModel);
                        } catch (JadePersistenceException ex) {
                            ex.printStackTrace();
                        }
                        List<JadeAbstractModel> models = Arrays.asList(searchModel.getSearchResults());
                        List<CotisationDecompteComplexModel> cotisationsDecompte = (List<CotisationDecompteComplexModel>) (List<?>) models;
                        return cotisationsDecompte;
                    }
                });

        Map<String, List<CotisationDecompteComplexModel>> map = JadeListUtil.groupBy(cotisationsDecompte,
                new JadeListUtil.Key<CotisationDecompteComplexModel>() {
                    @Override
                    public String exec(final CotisationDecompteComplexModel e) {
                        return String.valueOf(e.getCotisationDecompteSimpleModel().getIdLigneDecompte());
                    }
                });

        for (DecompteSalaire decompteSalaire : decomptesSalaires) {
            String idLigneDecompte = String.valueOf(decompteSalaire.getId());
            if (map.containsKey(idLigneDecompte)) {
                List<CotisationDecompteComplexModel> cotisationDecompteComplexModels = map.get(idLigneDecompte);
                List<CotisationDecompte> cotisationDecomptes = CotisationDecompteConverter
                        .convertToDomain(cotisationDecompteComplexModels, decompteSalaire);
                decompteSalaire.setCotisationsDecompte(cotisationDecomptes);
            }
        }
    }

    private LigneDecompteSearchComplexModel getLigneDecompteSearchModel(final String idDecompte,
            final String currentSequence, SearchDirection direction) {
        LigneDecompteSearchComplexModel searchModel = new LigneDecompteSearchComplexModel();
        searchModel.setForIdDecompte(idDecompte);
        switch (direction) {
            case PREVIOUS:
                searchModel.setOrderKey(LigneDecompteSearchComplexModel.SEQUENCE_DESC);
                searchModel.setToSequence(currentSequence);
                break;
            case NEXT:
                searchModel.setOrderKey(LigneDecompteSearchComplexModel.SEQUENCE_ASC);
                searchModel.setFromSequence(currentSequence);
                break;
        }
        return searchModel;
    }

    private enum SearchDirection {
        PREVIOUS,
        NEXT;
    }

    @Override
    public List<DecompteSalaire> findBy(String idDecompte, String idPosteTravail, Date periodeDebut, Date periodeFin) {
        LigneDecompteSearchComplexModel searchComplexModel = new LigneDecompteSearchComplexModel();
        searchComplexModel.setForIdDecompte(idDecompte);
        searchComplexModel.setForIdPosteTravail(idPosteTravail);
        searchComplexModel.setForPeriodeDebut(periodeDebut.getSwissValue());
        searchComplexModel.setForPeriodeFin(periodeFin.getSwissValue());
        return searchAndFetch(searchComplexModel);
    }

    @Override
    public Deque<DecompteSalaire> findSalairesPourAnnee(Annee annee, Convention convention, String typeDecompte) {
        String typeDecompteString = "";
        if (JadeStringUtil.isEmpty(typeDecompte)) {
            typeDecompteString = typeDecompteString + TypeDecompte.COMPLEMENTAIRE.getValue() + ",";
            typeDecompteString = typeDecompteString + TypeDecompte.PERIODIQUE.getValue() + ",";
            typeDecompteString = typeDecompteString + TypeDecompte.SPECIAL_SALAIRE.getValue() + ",";
            typeDecompteString = typeDecompteString + TypeDecompte.SPECIAL_CAISSE.getValue() + ",";
            typeDecompteString = typeDecompteString + TypeDecompte.CPP.getValue();
        } else {
            typeDecompteString = typeDecompte;
        }

        List<DecompteSalaire> salaires = findDecompteSalairesPourAnneeByQuery(new AnneeComptable(annee), convention,
                typeDecompteString);

        // loadCotisations(salaires);

        Deque<DecompteSalaire> deque = new ArrayDeque<DecompteSalaire>();
        for (DecompteSalaire decompteSalaire2 : salaires) {
            // On recherche maintenant si le décompte salaire possède de l'AVS, au lieu du poste de travail
            // if (decompteSalaire2.hasTypeAssurance(TypeAssurance.COTISATION_AVS_AI)) {
            deque.add(decompteSalaire2);
            // }
        }
        return deque;
    }

    @Override
    public Deque<DecompteSalaire> findSalairesResorPourAnnee(Annee annee) {
        String typeDecompteString = "";
        typeDecompteString = typeDecompteString + TypeDecompte.COMPLEMENTAIRE.getValue() + ",";
        typeDecompteString = typeDecompteString + TypeDecompte.PERIODIQUE.getValue() + ",";
        typeDecompteString = typeDecompteString + TypeDecompte.SPECIAL_SALAIRE.getValue() + ",";
        typeDecompteString = typeDecompteString + TypeDecompte.SPECIAL_CAISSE.getValue() + ",";
        typeDecompteString = typeDecompteString + TypeDecompte.CPP.getValue() + ",";
        typeDecompteString = typeDecompteString + TypeDecompte.CONTROLE_EMPLOYEUR.getValue();

        List<DecompteSalaire> salaires = findDecompteSalairesResorPourAnneeByQuery(new AnneeComptable(annee),
                typeDecompteString);

        // loadCotisations(salaires);

        Deque<DecompteSalaire> deque = new ArrayDeque<DecompteSalaire>();
        for (DecompteSalaire decompteSalaire2 : salaires) {
            // On recherche maintenant si le décompte salaire possède de l'AVS, au lieu du poste de travail
            // if (decompteSalaire2.hasTypeAssurance(TypeAssurance.COTISATION_AVS_AI)) {
            deque.add(decompteSalaire2);
            // }
        }
        return deque;
    }

    public List<DecompteSalaire> findDecompteSalairesResorPourAnneeByQuery(AnneeComptable annee) {
        String typeDecompteString = TypeDecompte.COMPLEMENTAIRE.getValue() + ",";
        typeDecompteString = typeDecompteString + TypeDecompte.PERIODIQUE.getValue() + ",";
        typeDecompteString = typeDecompteString + TypeDecompte.SPECIAL_SALAIRE.getValue() + ",";
        typeDecompteString = typeDecompteString + TypeDecompte.SPECIAL_CAISSE.getValue() + ",";
        typeDecompteString = typeDecompteString + TypeDecompte.CPP.getValue();

        return findDecompteSalairesResorPourAnneeByQuery(annee, typeDecompteString);
    }

    public List<DecompteSalaire> findDecompteSalairesResorPourAnneeByQuery(AnneeComptable annee, String typeDecompte) {
        List<DecompteSalaire> decompteSalaires = new ArrayList<DecompteSalaire>();
        String query = "SELECT" + " cotisations.MASSE as MASSE," + " cotisations.MASSE_FORCEE as MASSE_FORCEE,"
                + " PT_DECOMPTE_LIGNES1.ID AS PT_DECOMPTE_LIGNES1_ID," + " TIPERSP1.HPDNAI AS TIPERSP1_HPDNAI,"
                + " AFCOTIP1.MEDDEB as AFCOTIP1_MEDDEB, AFCOTIP1.MEDFIN as AFCOTIP1_MEDFIN,"
                + " TIPERSP1.HPTSEX AS TIPERSP1_HPTSEX," + " TITIERP1.HTTLAN AS TITIERP1_HTTLAN,"
                + " PT_DECOMPTES1.DATE_ETABLISSEMENT AS PT_DECOMPTES1_DATE_ETABLISSEMENT,"
                + " PT_POSTES_TRAVAILS1.CS_GENRE_SALAIRE AS PT_POSTES_TRAVAILS1_CS_GENRE_SALAIRE,"
                + " TIADMIP1.HBCADM AS TIADMIP1_HBCADM," + " TITIERP1.HNIPAY AS TITIERP1_HNIPAY,"
                + " PT_TRAVAILLEURS1.ID AS PT_TRAVAILLEURS1_ID," + " AFAFFIP1.MADDEB AS AFAFFIP1_MADDEB,"
                + " AFAFFIP1.MADFIN AS AFAFFIP1_MADFIN,"
                + " PT_DECOMPTES1.ID_PASSAGE_FACTURATION AS PT_DECOMPTES1_ID_PASSAGE_FACTURATION,"
                + " PT_DECOMPTES1.CS_TYPE AS PT_DECOMPTES1_CS_TYPE,"
                + " PT_DECOMPTE_LIGNES1.PERIODE_FIN AS PT_DECOMPTE_LIGNES1_PERIODE_FIN,"
                + " AFAFFIP1.MAIAFF AS AFAFFIP1_MAIAFF," + " AFAFFIP1.MADESL AS AFAFFIP1_MADESL,"
                + " AFAFFIP1.MACONV AS AFAFFIP1_MACONV," + " FAPASSP1.IDTYPEFACTURATION AS FAPASSP1_IDTYPEFACTURATION,"
                + " FAPASSP1.LIBELLEPASSAGE AS FAPASSP1_LIBELLEPASSAGE,"
                + " PT_DECOMPTE_LIGNES1.SALAIRE_TOTAL AS PT_DECOMPTE_LIGNES1_SALAIRE_TOTAL,"
                + " PT_DECOMPTE_LIGNES1.MONTANT_FRANCHISE AS MONTANT_FRANCHISE,"
                + " PT_POSTES_TRAVAILS1.ID AS PT_POSTES_TRAVAILS1_ID,"
                + " PT_POSTES_TRAVAILS1.CS_QUALIFICATION AS PT_POSTES_TRAVAILS1_CS_QUALIFICATION,"
                + " PT_CONV_QUALIFICATIONS1.CS_PERSONNEL AS PT_CONV_QUALIFICATIONS1_CS_PERSONNEL,"
                + " TIPAVSP1.HXNAVS AS TIPAVSP1_HXNAVS," + " PT_DECOMPTE_LIGNES1.PSPY AS PT_DECOMPTE_LIGNES1_PSPY,"
                + " FAPASSP1.DATEFACTURATION AS FAPASSP1_DATEFACTURATION," + " FAPASSP1.STATUS AS FAPASSP1_STATUS,"
                + " PT_DECOMPTES1.ID_PT_EMPLOYEURS AS PT_DECOMPTES1_ID_PT_EMPLOYEURS,"
                + " TITIERP1.HTLDE1 AS TITIERP1_HTLDE1," + " TITIERP1.HTLDE2 AS TITIERP1_HTLDE2,"
                + " AFAFFIP1.MALNAF AS AFAFFIP1_MALNAF," + " FAPASSP1.DATEPERIODE AS FAPASSP1_DATEPERIODE,"
                + " FAPASSP1.IDJOURNAL AS FAPASSP1_IDJOURNAL," + " FAPASSP1.PSPY AS FAPASSP1_PSPY,"
                + " FAPASSP1.IDPASSAGE AS FAPASSP1_IDPASSAGE,"
                + " FAPASSP1.IDPLANFACTURATION AS FAPASSP1_IDPLANFACTURATION,"
                + " PT_HISTORIQUE_DECOMPTES1.DATE AS PT_HISTORIQUE_DECOMPTES1_DATE,"
                + " PT_HISTORIQUE_DECOMPTES1.CS_ETAT AS PT_HISTORIQUE_DECOMPTES1_CS_ETAT,"
                + " PT_DECOMPTE_LIGNES1.PERIODE_DEBUT AS PT_DECOMPTE_LIGNES1_PERIODE_DEBUT,"
                + " PT_DECOMPTES1.ID AS PT_DECOMPTES1_ID" + " FROM SCHEMA.PT_DECOMPTE_LIGNES PT_DECOMPTE_LIGNES1"
                + " INNER JOIN SCHEMA.PT_POSTES_TRAVAILS PT_POSTES_TRAVAILS1 ON" + " ("
                + "    PT_DECOMPTE_LIGNES1.ID_PT_POSTES_TRAVAIL=PT_POSTES_TRAVAILS1.ID" + " )" + " LEFT OUTER"
                + " JOIN SCHEMA.PT_TRAVAILLEURS PT_TRAVAILLEURS1 ON" + " ("
                + "    PT_POSTES_TRAVAILS1.ID_PT_TRAVAILLEURS=PT_TRAVAILLEURS1.ID" + " )" + " LEFT OUTER"
                + " JOIN SCHEMA.TIPAVSP TIPAVSP1 ON" + " (" + "    PT_TRAVAILLEURS1.ID_TITIERP=TIPAVSP1.HTITIE" + " )"
                + " LEFT OUTER" + " JOIN SCHEMA.TIPERSP TIPERSP1 ON" + " ("
                + "    PT_TRAVAILLEURS1.ID_TITIERP=TIPERSP1.HTITIE" + " )" + " LEFT OUTER"
                + " JOIN SCHEMA.TITIERP TITIERP1 ON" + " (" + "    PT_TRAVAILLEURS1.ID_TITIERP=TITIERP1.HTITIE" + " )"
                + " LEFT OUTER" + " JOIN SCHEMA.AFAFFIP AFAFFIP1 ON" + " ("
                + "    PT_POSTES_TRAVAILS1.ID_AFAFFIP=AFAFFIP1.MAIAFF" + " )" + " LEFT OUTER"
                + " JOIN SCHEMA.TITIERP TITIERP2 ON ( AFAFFIP1.HTITIE=TITIERP2.HTITIE ) LEFT OUTER"
                + " JOIN SCHEMA.TIADMIP TIADMIP1 ON ( AFAFFIP1.MACONV=TIADMIP1.HTITIE ) LEFT OUTER"
                + " JOIN SCHEMA.TITIERP TITIERP3 ON ( TIADMIP1.HTITIE=TITIERP3.HTITIE ) LEFT OUTER"
                + " JOIN SCHEMA.PT_EMPLOYEURS PT_EMPLOYEURS1 ON" + " ("
                + "    PT_POSTES_TRAVAILS1.ID_AFAFFIP=PT_EMPLOYEURS1.ID_AFAFFIP" + " )"
                + " INNER JOIN SCHEMA.PT_DECOMPTES PT_DECOMPTES1 ON" + " ("
                + "    PT_DECOMPTE_LIGNES1.ID_PT_DECOMPTES=PT_DECOMPTES1.ID" + " )" + " LEFT OUTER"
                + " JOIN SCHEMA.FAPASSP FAPASSP1 ON" + " ("
                + "    PT_DECOMPTES1.ID_PASSAGE_FACTURATION=FAPASSP1.IDPASSAGE" + " )"

                + " INNER JOIN SCHEMA.PT_COTISATION_DECOMPTES cotisations ON" + " ("
                + " cotisations.ID_PT_DECOMPTE_LIGNES = PT_DECOMPTE_LIGNES1.ID" + " )"
                + " INNER JOIN SCHEMA.AFCOTIP AFCOTIP1 ON" + " (" + " cotisations.ID_AFCOTIP=AFCOTIP1.MEICOT AND "
                + " AFCOTIP1.MBIASS = 237 " + " )"
                + " INNER JOIN SCHEMA.PT_HISTORIQUE_DECOMPTES PT_HISTORIQUE_DECOMPTES1 ON" + " ("
                + "    PT_DECOMPTES1.ID=PT_HISTORIQUE_DECOMPTES1.ID_PT_DECOMPTES"
                + "    AND PT_HISTORIQUE_DECOMPTES1.CS_ETAT=68012007" + " )"

                + " LEFT OUTER JOIN WEBAVSS.PT_CONV_QUALIFICATIONS PT_CONV_QUALIFICATIONS1 ON " + " ( "
                + " PT_CONV_QUALIFICATIONS1.ID_TIADMIP_CONVENTION = TIADMIP1.HTITIE " + " AND "
                + " PT_CONV_QUALIFICATIONS1.CS_QUALIFICATION = PT_POSTES_TRAVAILS1.CS_QUALIFICATION " + " ) "

                + " WHERE" + " (" + "    PT_DECOMPTES1.CS_ETAT=68012007" + "    AND PT_DECOMPTES1.CS_TYPE IN ("
                + typeDecompte + ")" + "    AND" + "    (" + "       (" + "          PT_HISTORIQUE_DECOMPTES1.DATE>="
                + annee.getDateDebut().getValue() + "          AND PT_HISTORIQUE_DECOMPTES1.DATE<="
                + annee.getDateFin().getValue() + "          AND PT_DECOMPTES1.NUMERO_DECOMPTE LIKE '0%'" + "       )"
                + "       OR" + "       (" + "          FAPASSP1.DATEFACTURATION>=" + annee.getDateDebut().getValue()
                + "          AND FAPASSP1.DATEFACTURATION<=" + annee.getDateFin().getValue() + "       )" + "    )"
                + " )" + " ORDER BY PT_TRAVAILLEURS1_ID ASC," + " AFAFFIP1_MAIAFF ASC,"
                + " PT_DECOMPTE_LIGNES1_PERIODE_DEBUT ASC,"
                + " PT_DECOMPTE_LIGNES1_PERIODE_FIN ASC, PT_DECOMPTES1_CS_TYPE ASC ";
        try {
            ArrayList<HashMap<String, Object>> result = DBUtil.executeQuery(query, DecompteRepositoryJade.class);
            for (HashMap<String, Object> value : result) {
                DecompteSalaire decompteSalaire = new DecompteSalaire();
                Decompte decompte = new Decompte();
                PosteTravail posteTravail = new PosteTravail();
                Employeur employeur = new Employeur();
                Travailleur travailleur = new Travailleur();
                travailleur.setDesignation2(String.valueOf(value.get("TITIERP1_HTLDE2")));
                travailleur.setDesignation1(String.valueOf(value.get("TITIERP1_HTLDE1")));
                travailleur.setNumAvsActuel(String.valueOf(value.get("TIPAVSP1_HXNAVS")));
                travailleur.setDateNaissance(String.valueOf(value.get("TIPERSP1_HPDNAI")));
                travailleur.setSexe(String.valueOf(value.get("TIPERSP1_HPTSEX")));
                travailleur.setLangue(String.valueOf(value.get("TITIERP1_HTTLAN")));

                Pays pays = new Pays();
                pays.setCodeIso(String.valueOf(value.get("TITIERP1_HNIPAY")));
                travailleur.setPays(pays);

                employeur.setId(String.valueOf(value.get("PT_DECOMPTES1_ID_PT_EMPLOYEURS")));
                employeur.setAffilieNumero(String.valueOf(value.get("AFAFFIP1_MALNAF")));
                employeur.setRaisonSociale(String.valueOf(value.get("AFAFFIP1_MADESL")));
                employeur.setDateDebut(String.valueOf(value.get("AFAFFIP1_MADDEB")));
                employeur.setDateFin(String.valueOf(value.get("AFAFFIP1_MADFIN")));
                Convention convention = new Convention();
                convention.setCode(String.valueOf(value.get("TIADMIP1_HBCADM")));
                employeur.setConvention(convention);

                posteTravail.setId(String.valueOf(value.get("PT_POSTES_TRAVAILS1_ID")));
                posteTravail.setTypeSalaire(
                        TypeSalaire.fromValue(String.valueOf(value.get("PT_POSTES_TRAVAILS1_CS_GENRE_SALAIRE"))));
                posteTravail.setQualification(
                        Qualification.fromValue(String.valueOf(value.get("PT_POSTES_TRAVAILS1_CS_QUALIFICATION"))));
                List<ConventionQualification> parametresQualification = new ArrayList<ConventionQualification>();
                ConventionQualification conventionQualification = new ConventionQualification();
                conventionQualification.setPersonnel(
                        Personnel.fromValue(String.valueOf(value.get("PT_CONV_QUALIFICATIONS1_CS_PERSONNEL"))));
                parametresQualification.add(conventionQualification);
                posteTravail.setParametresQualifications(parametresQualification);
                List<AdhesionCotisationPosteTravail> cotisations = new ArrayList<AdhesionCotisationPosteTravail>();
                AdhesionCotisationPosteTravail cotisation = new AdhesionCotisationPosteTravail();
                Date dateDebutCotisation = null;
                Date dateFinCotisation = null;
                if (!JadeStringUtil.isBlankOrZero(String.valueOf(value.get("AFCOTIP1_MEDDEB")))) {
                    dateDebutCotisation = new Date(String.valueOf(value.get("AFCOTIP1_MEDDEB")));
                }
                if (!JadeStringUtil.isBlankOrZero(String.valueOf(value.get("AFCOTIP1_MEDFIN")))) {
                    dateFinCotisation = new Date(String.valueOf(value.get("AFCOTIP1_MEDFIN")));
                }
                cotisation.setPeriode(new Periode(dateDebutCotisation, dateFinCotisation));
                cotisations.add(cotisation);
                posteTravail.setAdhesionsCotisations(cotisations);

                decompteSalaire
                        .setSalaireTotal(new Montant(String.valueOf(value.get("PT_DECOMPTE_LIGNES1_SALAIRE_TOTAL"))));
                decompteSalaire.setId(String.valueOf(value.get("PT_DECOMPTE_LIGNES1_ID")));
                travailleur.setId(String.valueOf(value.get("PT_TRAVAILLEURS1_ID")));
                decompte.setId(String.valueOf(value.get("PT_DECOMPTES1_ID")));
                decompte.setType(TypeDecompte.fromValue(String.valueOf(value.get("PT_DECOMPTES1_CS_TYPE"))));
                String dateDebut = String.valueOf(value.get("PT_DECOMPTE_LIGNES1_PERIODE_DEBUT"));
                String dateFin = String.valueOf(value.get("PT_DECOMPTE_LIGNES1_PERIODE_FIN"));
                decompteSalaire.setPeriode(new Periode(dateDebut, dateFin));
                String masseForcee = String.valueOf(value.get("MASSE_FORCEE"));
                if (masseForcee.equals("1")) {
                    decompteSalaire.setSalaireTotal(new Montant(String.valueOf(value.get("MASSE"))));
                }

                decompteSalaire.setDecompte(decompte);
                posteTravail.setEmployeur(employeur);
                posteTravail.setTravailleur(travailleur);
                decompteSalaire.setPosteTravail(posteTravail);
                decompteSalaires.add(decompteSalaire);
            }

        } catch (JadePersistenceException e) {
            logger.error(e.getMessage());
        }
        return decompteSalaires;
    }

    public List<DecompteSalaire> findDecompteSalairesPourAnneeByQuery(AnneeComptable annee, Convention convention,
            String typeDecompte) {
        List<DecompteSalaire> decompteSalaires = new ArrayList<DecompteSalaire>();
        String query = "SELECT" + " cotisations.MASSE as MASSE," + " cotisations.MASSE_FORCEE as MASSE_FORCEE,"
                + " PT_DECOMPTE_LIGNES1.ID AS PT_DECOMPTE_LIGNES1_ID,"
                + " PT_DECOMPTES1.DATE_ETABLISSEMENT AS PT_DECOMPTES1_DATE_ETABLISSEMENT,"
                + " PT_TRAVAILLEURS1.ID AS PT_TRAVAILLEURS1_ID,"
                + " PT_DECOMPTES1.ID_PASSAGE_FACTURATION AS PT_DECOMPTES1_ID_PASSAGE_FACTURATION,"
                + " PT_DECOMPTE_LIGNES1.PERIODE_FIN AS PT_DECOMPTE_LIGNES1_PERIODE_FIN,"
                + " AFAFFIP1.MAIAFF AS AFAFFIP1_MAIAFF," + " FAPASSP1.IDTYPEFACTURATION AS FAPASSP1_IDTYPEFACTURATION,"
                + " FAPASSP1.LIBELLEPASSAGE AS FAPASSP1_LIBELLEPASSAGE,"
                + " PT_DECOMPTE_LIGNES1.SALAIRE_TOTAL AS PT_DECOMPTE_LIGNES1_SALAIRE_TOTAL,"
                + " PT_DECOMPTE_LIGNES1.MONTANT_FRANCHISE AS MONTANT_FRANCHISE,"
                + " PT_POSTES_TRAVAILS1.ID AS PT_POSTES_TRAVAILS1_ID," + " TIPAVSP1.HXNAVS AS TIPAVSP1_HXNAVS,"
                + " PT_DECOMPTE_LIGNES1.PSPY AS PT_DECOMPTE_LIGNES1_PSPY,"
                + " FAPASSP1.DATEFACTURATION AS FAPASSP1_DATEFACTURATION," + " FAPASSP1.STATUS AS FAPASSP1_STATUS,"
                + " PT_DECOMPTES1.ID_PT_EMPLOYEURS AS PT_DECOMPTES1_ID_PT_EMPLOYEURS,"
                + " TITIERP1.HTLDE1 AS TITIERP1_HTLDE1," + " TITIERP1.HTLDE2 AS TITIERP1_HTLDE2,"
                + " AFAFFIP1.MALNAF AS AFAFFIP1_MALNAF," + " FAPASSP1.DATEPERIODE AS FAPASSP1_DATEPERIODE,"
                + " FAPASSP1.IDJOURNAL AS FAPASSP1_IDJOURNAL," + " FAPASSP1.PSPY AS FAPASSP1_PSPY,"
                + " FAPASSP1.IDPASSAGE AS FAPASSP1_IDPASSAGE,"
                + " FAPASSP1.IDPLANFACTURATION AS FAPASSP1_IDPLANFACTURATION,"
                + " PT_DECOMPTE_LIGNES1.PERIODE_DEBUT AS PT_DECOMPTE_LIGNES1_PERIODE_DEBUT,"
                + " PT_DECOMPTES1.ID AS PT_DECOMPTES1_ID" + " FROM SCHEMA.PT_DECOMPTE_LIGNES PT_DECOMPTE_LIGNES1"
                + " INNER JOIN SCHEMA.PT_POSTES_TRAVAILS PT_POSTES_TRAVAILS1 ON" + " ("
                + "    PT_DECOMPTE_LIGNES1.ID_PT_POSTES_TRAVAIL=PT_POSTES_TRAVAILS1.ID" + " )" + " LEFT OUTER"
                + " JOIN SCHEMA.PT_TRAVAILLEURS PT_TRAVAILLEURS1 ON" + " ("
                + "    PT_POSTES_TRAVAILS1.ID_PT_TRAVAILLEURS=PT_TRAVAILLEURS1.ID" + " )" + " LEFT OUTER"
                + " JOIN SCHEMA.TIPAVSP TIPAVSP1 ON" + " (" + "    PT_TRAVAILLEURS1.ID_TITIERP=TIPAVSP1.HTITIE" + " )"
                + " LEFT OUTER" + " JOIN SCHEMA.TIPERSP TIPERSP1 ON" + " ("
                + "    PT_TRAVAILLEURS1.ID_TITIERP=TIPERSP1.HTITIE" + " )" + " LEFT OUTER"
                + " JOIN SCHEMA.TITIERP TITIERP1 ON" + " (" + "    PT_TRAVAILLEURS1.ID_TITIERP=TITIERP1.HTITIE" + " )"
                + " LEFT OUTER" + " JOIN SCHEMA.AFAFFIP AFAFFIP1 ON" + " ("
                + "    PT_POSTES_TRAVAILS1.ID_AFAFFIP=AFAFFIP1.MAIAFF" + " )" + " LEFT OUTER"
                + " JOIN SCHEMA.TITIERP TITIERP2 ON ( AFAFFIP1.HTITIE=TITIERP2.HTITIE ) LEFT OUTER"
                + " JOIN SCHEMA.TIADMIP TIADMIP1 ON ( AFAFFIP1.MACONV=TIADMIP1.HTITIE ) LEFT OUTER"
                + " JOIN SCHEMA.TITIERP TITIERP3 ON ( TIADMIP1.HTITIE=TITIERP3.HTITIE ) LEFT OUTER"
                + " JOIN SCHEMA.PT_EMPLOYEURS PT_EMPLOYEURS1 ON" + " ("
                + "    PT_POSTES_TRAVAILS1.ID_AFAFFIP=PT_EMPLOYEURS1.ID_AFAFFIP" + " )"
                + " INNER JOIN SCHEMA.PT_DECOMPTES PT_DECOMPTES1 ON" + " ("
                + "    PT_DECOMPTE_LIGNES1.ID_PT_DECOMPTES=PT_DECOMPTES1.ID" + " )" + " LEFT OUTER"
                + " JOIN SCHEMA.FAPASSP FAPASSP1 ON" + " ("
                + "    PT_DECOMPTES1.ID_PASSAGE_FACTURATION=FAPASSP1.IDPASSAGE" + " )"

                + " INNER JOIN SCHEMA.PT_COTISATION_DECOMPTES cotisations ON" + " ("
                + " cotisations.ID_PT_DECOMPTE_LIGNES = PT_DECOMPTE_LIGNES1.ID" + " )"
                + " INNER JOIN SCHEMA.AFCOTIP AFCOTIP1 ON" + " (" + " cotisations.ID_AFCOTIP=AFCOTIP1.MEICOT" + " )"
                + " INNER JOIN SCHEMA.AFASSUP AFASSUP1 ON" + " (" + " AFCOTIP1.MBIASS=AFASSUP1.MBIASS" + " AND"
                + " AFASSUP1.MBTTYP = 812001" + " )" + " WHERE" + " (" + "    PT_DECOMPTES1.CS_ETAT=68012007"

                + "    AND PT_DECOMPTES1.CS_TYPE IN (" + typeDecompte + ")" + "    AND" + "    ("
                + "       PT_DECOMPTE_LIGNES1.DATE_ANNONCE IS NULL OR PT_DECOMPTE_LIGNES1.DATE_ANNONCE=''" + "    )"
                + "    AND" + "    (" + "       (" + "          FAPASSP1.DATEFACTURATION>="
                + annee.getDateDebut().getValue() + "          AND FAPASSP1.DATEFACTURATION<="
                + annee.getDateFin().getValue() + "       )" + "    )" + " )";

        if (convention != null) {
            query = query + "    AND AFAFFIP1.MACONV=" + convention.getId();
        }

        query = query

                + " ORDER BY PT_DECOMPTE_LIGNES1_PERIODE_DEBUT ASC," + " PT_DECOMPTE_LIGNES1_PERIODE_FIN ASC,"
                + " PT_TRAVAILLEURS1_ID ASC," + " AFAFFIP1_MAIAFF ASC";
        try {
            ArrayList<HashMap<String, Object>> result = DBUtil.executeQuery(query, DecompteRepositoryJade.class);
            for (HashMap<String, Object> value : result) {
                DecompteSalaire decompteSalaire = new DecompteSalaire();
                Decompte decompte = new Decompte();
                PosteTravail posteTravail = new PosteTravail();
                Employeur employeur = new Employeur();
                Travailleur travailleur = new Travailleur();
                travailleur.setDesignation2(String.valueOf(value.get("TITIERP1_HTLDE2")));
                travailleur.setDesignation1(String.valueOf(value.get("TITIERP1_HTLDE1")));
                travailleur.setNumAvsActuel(String.valueOf(value.get("TIPAVSP1_HXNAVS")));
                employeur.setId(String.valueOf(value.get("PT_DECOMPTES1_ID_PT_EMPLOYEURS")));
                employeur.setAffilieNumero(String.valueOf(value.get("AFAFFIP1_MALNAF")));
                posteTravail.setId(String.valueOf(value.get("PT_POSTES_TRAVAILS1_ID")));
                decompteSalaire
                        .setSalaireTotal(new Montant(String.valueOf(value.get("PT_DECOMPTE_LIGNES1_SALAIRE_TOTAL"))));
                decompteSalaire.setId(String.valueOf(value.get("PT_DECOMPTE_LIGNES1_ID")));
                travailleur.setId(String.valueOf(value.get("PT_TRAVAILLEURS1_ID")));
                decompte.setId(String.valueOf(value.get("PT_DECOMPTES1_ID")));
                String dateDebut = String.valueOf(value.get("PT_DECOMPTE_LIGNES1_PERIODE_DEBUT"));
                String dateFin = String.valueOf(value.get("PT_DECOMPTE_LIGNES1_PERIODE_FIN"));
                decompteSalaire.setPeriode(new Periode(dateDebut, dateFin));
                String masseForcee = String.valueOf(value.get("MASSE_FORCEE"));
                if (masseForcee.equals("1")) {
                    decompteSalaire.setSalaireTotal(new Montant(String.valueOf(value.get("MASSE"))));
                }

                decompteSalaire.setDecompte(decompte);
                posteTravail.setEmployeur(employeur);
                posteTravail.setTravailleur(travailleur);
                decompteSalaire.setPosteTravail(posteTravail);
                decompteSalaires.add(decompteSalaire);
            }

        } catch (JadePersistenceException e) {
            logger.error(e.getMessage());
        }
        return decompteSalaires;
    }

    @Override
    public List<DecompteSalaire> findForYear(String idPosteTravail, Annee anneeDecompte) {
        LigneDecompteSearchComplexModel searchModel = new LigneDecompteSearchComplexModel();
        searchModel.setForPeriodeDebut(anneeDecompte.getFirstDayOfYear().getSwissValue());
        searchModel.setForPeriodeFin(anneeDecompte.getLastDayOfYear().getSwissValue());
        searchModel.setForIdPosteTravail(idPosteTravail);
        searchModel.setForEtatDecompte(EtatDecompte.COMPTABILISE);
        List<DecompteSalaire> decomptesSalaires = searchAndFetch(searchModel);
        loadCotisations(decomptesSalaires);
        return decomptesSalaires;
    }

    @Override
    public List<DecompteSalaire> findForYearComptaOuValide(String idPosteTravail, Annee anneeDecompte) {
        LigneDecompteSearchComplexModel searchModel = new LigneDecompteSearchComplexModel();
        searchModel.setForPeriodeDebut(anneeDecompte.getFirstDayOfYear().getSwissValue());
        searchModel.setForPeriodeFin(anneeDecompte.getLastDayOfYear().getSwissValue());
        searchModel.setForIdPosteTravail(idPosteTravail);
        searchModel.setForEtatDecompteIn(
                Arrays.asList(EtatDecompte.COMPTABILISE.getValue(), EtatDecompte.VALIDE.getValue()));
        List<DecompteSalaire> decomptesSalaires = searchAndFetch(searchModel);
        loadCotisations(decomptesSalaires);
        return decomptesSalaires;
    }

    @Override
    public List<DecompteSalaire> findAllForYear(Annee anneeDecompte, String idTravailleurGreater,
            String idTravailleurLess) {
        LigneDecompteSearchComplexModel searchModel = new LigneDecompteSearchComplexModel();
        searchModel.setForPeriodeDebut(anneeDecompte.getFirstDayOfYear().getSwissValue());
        searchModel.setForPeriodeFin(anneeDecompte.getLastDayOfYear().getSwissValue());
        searchModel.setForIdTravailleurLess(idTravailleurLess);
        searchModel.setForIdTravailleurGreater(idTravailleurGreater);
        // searchModel.setForEtatDecompte(EtatDecompte.COMPTABILISE);
        List<DecompteSalaire> decomptesSalaires = searchAndFetch(searchModel);
        loadCotisations(decomptesSalaires);
        return decomptesSalaires;
    }

    @Override
    public List<DecompteSalaire> findByIdAndPeriode(String idTravailleur, Date dateDebut, Date dateFin) {
        LigneDecompteSearchComplexModel searchComplexModel = new LigneDecompteSearchComplexModel();
        searchComplexModel.setForPeriodeDebut(dateDebut);
        if (dateFin != null) {
            searchComplexModel.setForPeriodeFin(dateFin);
        }
        searchComplexModel.setForEtatDecompte(EtatDecompte.COMPTABILISE);
        searchComplexModel.setForIdTravailleur(idTravailleur);
        return searchAndLoadDependencies(searchComplexModel);
    }

    @Override
    public List<DecompteSalaire> findByIdAndPeriodeWithCotisations(String idTravailleur, Date dateDebut, Date dateFin) {
        LigneDecompteSearchComplexModel searchComplexModel = new LigneDecompteSearchComplexModel();
        searchComplexModel.setForPeriodeDebut(dateDebut);
        if (dateFin != null) {
            searchComplexModel.setForPeriodeFin(dateFin);
        }
        searchComplexModel.setForEtatDecompte(EtatDecompte.COMPTABILISE);
        searchComplexModel.setForIdTravailleur(idTravailleur);
        List<DecompteSalaire> decomptes = searchAndFetch(searchComplexModel);
        loadCotisations(decomptes);
        return decomptes;
    }

    @Override
    public List<DecompteSalaire> findByIdPosteTravailAfterDateFin(String idPosteTravail, Date dateFin) {
        LigneDecompteSearchComplexModel searchComplexModel = new LigneDecompteSearchComplexModel();
        searchComplexModel.setForIdPosteTravail(idPosteTravail);
        searchComplexModel.setForPeriodeFinAfter(dateFin);
        return searchAndFetch(searchComplexModel);
    }

    @Override
    public List<DecompteSalaire> findDecomptesActifsHorsPeriode(String idPosteTravail, Date dateFin) {
        LigneDecompteSearchComplexModel searchComplexModel = new LigneDecompteSearchComplexModel();
        Date dateToCheck;
        // Add 1 mois pour ne pas supprimer les lignes si inactif en cours de mois
        if (dateFin.equals(dateFin.getLastDayOfMonth())) {
            dateToCheck = dateFin;
        } else {
            String date = JadeDateUtil.addMonths(dateFin.getSwissValue(), 1);
            dateToCheck = new Date(date);
        }

        searchComplexModel.setForPeriodeFinAfter(dateToCheck);
        searchComplexModel.setForTypeDecompteIn(TypeDecompte.PERIODIQUE);
        searchComplexModel.setForEtatDecompteIn(Arrays.asList(EtatDecompte.OUVERT.getValue(),
                EtatDecompte.GENERE.getValue(), EtatDecompte.A_TRAITER.getValue()));
        searchComplexModel.setForIdPosteTravail(idPosteTravail);
        return searchAndFetch(searchComplexModel);
    }

    @Override
    public List<CommunicationSalairesRetaval> findSalairesRetavalPourAnnee(Annee annee) {

        return QueryExecutor.execute(getQueryDecompteSalairesRetavalPourAnnee(new AnneeComptable(annee)),
                CommunicationSalairesRetaval.class);
    }

    private String getQueryDecompteSalairesRetavalPourAnnee(AnneeComptable anneeComptable) {
        return "SELECT TIADMIP.HBCADM AS CODE_CONVENTION, affilie.malnaf AS NUM_AFFILIE, affilie.MADESC AS RAISON_SOCIAL, "
                + "tiers.HTITIE AS ID_TIERS, poste.ID AS ID_POSTE_TRAVAIL, "
                + "tiers.HTLDE1 AS NOM, tiers.HTLDE2 AS PRENOM, pers.HPDNAI AS DATE_NAISSANCE, tiersAVS.HXNAVS AS NSS, "
                + "sum(ligne.SALAIRE_TOTAL) AS MASSE " + "FROM SCHEMA.PT_DECOMPTE_LIGNES ligne "
                + "inner join SCHEMA.PT_POSTES_TRAVAILS poste on poste.ID = ligne.ID_PT_POSTES_TRAVAIL "
                + "inner join SCHEMA.PT_TRAVAILLEURS trava on trava.ID = poste.ID_PT_TRAVAILLEURS "
                + "inner join SCHEMA.tipavsp tiersAVS on tiersAVS.HTITIE = trava.ID_TITIERP "
                + "inner join SCHEMA.TITIERP tiers on tiersAVS.HTITIE= tiers.HTITIE "
                + "LEFT OUTER join SCHEMA.TIPERSP pers on tiers.HTITIE=pers.HTITIE "
                + "inner join SCHEMA.PT_DECOMPTES decompte on decompte.ID = ligne.ID_PT_DECOMPTES "
                + "inner join SCHEMA.AFAFFIP affilie on poste.ID_AFAFFIP = affilie.MAIAFF "
                + "INNER JOIN SCHEMA.TIADMIP TIADMIP ON affilie.MACONV=TIADMIP.HTITIE "
                + "inner join SCHEMA.FAPASSP passage on passage.IDPASSAGE = decompte.ID_PASSAGE_FACTURATION "
                + "inner join SCHEMA.PT_COTISATION_DECOMPTES coti on coti.ID_PT_DECOMPTE_LIGNES = ligne.ID "
                + "inner join SCHEMA.afcotip cotiaf on coti.ID_AFCOTIP = cotiaf.MEICOT "
                + "inner join SCHEMA.afassup assurance on assurance.mbiass = cotiaf.mbiass "
                + "where assurance.MBIASS=238 and passage.DATEFACTURATION >= "
                + anneeComptable.getDateDebut().getValue() + " and passage.DATEFACTURATION <= "
                + anneeComptable.getDateFin().getValue()
                + " and ligne.SALAIRE_TOTAL <> 0 and decompte.CS_ETAT = 68012007 "
                + "group by TIADMIP.HBCADM, affilie.malnaf, affilie.MADESC, tiers.HTITIE, "
                + "poste.ID, tiers.HTLDE1, tiers.HTLDE2, pers.HPDNAI, tiersAVS.HXNAVS "
                + "ORDER by affilie.MALNAF, tiersAVS.HXNAVS";
    }

    @Override
    public List<DecompteSalaire> findDecompteOuvertGenereATraiterByIdPosteTravail(String idPosteTravail) {
        LigneDecompteSearchComplexModel searchModel = new LigneDecompteSearchComplexModel();
        List<String> etats = new ArrayList<String>();
        etats.add(EtatDecompte.OUVERT.getValue());
        etats.add(EtatDecompte.GENERE.getValue());
        etats.add(EtatDecompte.A_TRAITER.getValue());
        etats.add(EtatDecompte.SOMMATION.getValue());
        searchModel.setForEtatDecompteIn(etats);
        searchModel.setForIdPosteTravail(idPosteTravail);
        return searchAndLoadDependencies(searchModel);
    }

}