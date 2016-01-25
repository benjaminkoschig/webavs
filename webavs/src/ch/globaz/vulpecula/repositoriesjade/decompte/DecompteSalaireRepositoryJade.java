package ch.globaz.vulpecula.repositoriesjade.decompte;

import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.vulpecula.business.models.decomptes.AbsenceComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.AbsenceSearchComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.AbsenceSearchSimpleModel;
import ch.globaz.vulpecula.business.models.decomptes.AbsenceSimpleModel;
import ch.globaz.vulpecula.business.models.decomptes.CotisationDecompteComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.CotisationDecompteSearchComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.CotisationDecompteSimpleModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteSalaireAAnnoncerComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteSalaireAAnnoncerSearchComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.LigneDecompteComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.LigneDecompteSearchComplexModel;
import ch.globaz.vulpecula.business.models.decomptes.LigneDecompteSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.Absence;
import ch.globaz.vulpecula.domain.models.decompte.CotisationDecompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteSalaireRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import ch.globaz.vulpecula.repositoriesjade.decompte.converters.AbsenceConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.converters.CotisationDecompteConverter;
import ch.globaz.vulpecula.repositoriesjade.decompte.converters.DecompteSalaireConverter;

/***
 * Implémentation Jade de {@link DecompteSalaireRepository}
 * 
 */
public class DecompteSalaireRepositoryJade extends
        RepositoryJade<DecompteSalaire, LigneDecompteComplexModel, LigneDecompteSimpleModel> implements
        DecompteSalaireRepository {

    private final Logger LOGGER = LoggerFactory.getLogger(DecompteSalaireRepositoryJade.class);

    static enum LoadOptions {
        ABSENCES,
        COTISATIONS,
        NONE
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
        decompteSalaire = super.create(decompteSalaire);
        saveCotisationsDecompte(decompteSalaire);
        saveAbsences(decompteSalaire, false);
        return decompteSalaire;
    }

    @Override
    public DecompteSalaire update(DecompteSalaire decompteSalaire) {
        decompteSalaire = super.update(decompteSalaire);
        saveCotisationsDecompte(decompteSalaire);
        saveAbsences(decompteSalaire);
        return decompteSalaire;
    }

    @Override
    public DecompteSalaire updateDateAnnonce(DecompteSalaire decompteSalaire) {
        return super.update(decompteSalaire);
    }

    @Override
    public void delete(final DecompteSalaire entity) {
        // On efface les cotisations par cascade SQL
        super.delete(entity);
    }

    @Override
    public List<DecompteSalaire> findByIdDecompte(final String idDecompte) {
        LigneDecompteSearchComplexModel searchModel = new LigneDecompteSearchComplexModel();
        searchModel.setForIdDecompte(idDecompte);
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
                if (JadeStringUtil.isBlankOrZero(absenceSimpleModel.getSpy())) {
                    JadePersistenceManager.add(absenceSimpleModel);
                } else {
                    JadePersistenceManager.update(absenceSimpleModel);
                }
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
                List<CotisationDecompte> cotisationDecomptes = CotisationDecompteConverter.convertToDomain(
                        cotisationDecompteComplexModels, decompteSalaire);
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

        DecompteSalaireAAnnoncerSearchComplexModel searchModel = new DecompteSalaireAAnnoncerSearchComplexModel();

        searchModel.setForAnneeDecompte(annee.toString());

        if (convention != null) {
            searchModel.setForIdConvention(convention.getId());
        }
        searchModel.setForEtatDecompte(EtatDecompte.COMPTABILISE);
        searchModel.setOrderKey(LigneDecompteSearchComplexModel.ORDER_BY_EMPLOYEUR);
        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        searchModel.setForDateNonAnnoncee("true");
        if (JadeStringUtil.isEmpty(typeDecompte)) {
            List<String> listeDecompte = new ArrayList<String>();
            listeDecompte.add(TypeDecompte.COMPLEMENTAIRE.getValue());
            listeDecompte.add(TypeDecompte.PERIODIQUE.getValue());
            listeDecompte.add(TypeDecompte.SPECIAL.getValue());
            searchModel.setForTypeDecompteIn(listeDecompte);
        } else {
            List<String> listeDecompte = new ArrayList<String>();
            listeDecompte.add(typeDecompte);
            searchModel.setForTypeDecompteIn(listeDecompte);
        }

        try {
            JadePersistenceManager.search(searchModel);
        } catch (JadePersistenceException e) {
            logger.error(e.getMessage());
        }

        Deque<DecompteSalaire> salaires = new ArrayDeque<DecompteSalaire>();
        for (int i = 0; i < searchModel.getSearchResults().length; i++) {
            DecompteSalaireConverter converter = DecompteSalaireConverter.getInstance();
            DecompteSalaireAAnnoncerComplexModel complexModel = (DecompteSalaireAAnnoncerComplexModel) searchModel
                    .getSearchResults()[i];
            DecompteSalaire decompteSalaire = converter.convertToDomain(complexModel);

            // Il faut checker que le travailleur cotise à l'AVS pour la date donnée, au lieu de regarder
            // l'adhésion caisse de l'employeur.
            // ------------------------------------
            boolean isCaisseForAnnonceSalaire = false;

            PosteTravail posteTravail = decompteSalaire.getPosteTravail();
            decompteSalaire.getIdPosteTravail();

            List<AdhesionCotisationPosteTravail> adhesionCotisationPosteTravails = VulpeculaRepositoryLocator
                    .getAdhesionCotisationPosteRepository().findByIdPosteTravail(posteTravail.getId(),
                            new Date("01.01." + annee));

            for (AdhesionCotisationPosteTravail adhesionCotisationPosteTravail : adhesionCotisationPosteTravails) {
                if (adhesionCotisationPosteTravail.getTypeAssurance().equals(TypeAssurance.COTISATION_AVS_AI)) {
                    isCaisseForAnnonceSalaire = true;
                    break;
                }
            }

            if (isCaisseForAnnonceSalaire) {
                salaires.add(decompteSalaire);
            }

        }

        return salaires;
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
    public List<DecompteSalaire> findByIdAndPeriode(String idTravailleur, Date dateDebut, Date dateFin) {
        LigneDecompteSearchComplexModel searchComplexModel = new LigneDecompteSearchComplexModel();
        searchComplexModel.setForPeriodeDebut(dateDebut);
        if (dateFin != null) {
            searchComplexModel.setForPeriodeFin(dateFin);
        }
        searchComplexModel.setForEtatDecompte(EtatDecompte.COMPTABILISE);
        searchComplexModel.setForIdTravailleur(idTravailleur);
        return searchAndFetch(searchComplexModel);
    }
}