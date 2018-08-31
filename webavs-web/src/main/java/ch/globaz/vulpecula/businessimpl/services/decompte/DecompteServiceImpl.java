/**
 *
 */
package ch.globaz.vulpecula.businessimpl.services.decompte;

import static ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator.*;
import static ch.globaz.vulpecula.business.services.VulpeculaServiceLocator.*;
import static ch.globaz.vulpecula.domain.models.decompte.DecompteReceptionEtat.*;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.util.JACalendar;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.JadePersistencePKProvider;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.models.decomptes.LigneDecompteSearchSimpleModel;
import ch.globaz.vulpecula.business.models.decomptes.LigneDecompteSimpleModel;
import ch.globaz.vulpecula.business.models.ebusiness.SynchronisationEbuSimpleModel;
import ch.globaz.vulpecula.business.models.is.EntetePrestationSearchComplexModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.decompte.DecompteService;
import ch.globaz.vulpecula.business.services.postetravail.PosteTravailService;
import ch.globaz.vulpecula.business.services.properties.PropertiesService;
import ch.globaz.vulpecula.business.services.taxationoffice.TaxationOfficeService;
import ch.globaz.vulpecula.businessimpl.services.properties.PropertiesServiceImpl;
import ch.globaz.vulpecula.documents.catalog.DocumentPrinter;
import ch.globaz.vulpecula.documents.decompte.CotisationsInfo;
import ch.globaz.vulpecula.documents.rappels.DocumentSommationPrinter;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.PeriodeMensuelle;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.CotisationDecompte;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteReceptionEtat;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.DifferenceControle;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.HistoriqueDecompte;
import ch.globaz.vulpecula.domain.models.decompte.NumeroDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeProvenance;
import ch.globaz.vulpecula.domain.models.ebusiness.Synchronisation;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteRepository;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteSalaireRepository;
import ch.globaz.vulpecula.domain.repositories.decompte.HistoriqueDecompteRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.AdhesionCotisationPosteTravailRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.EmployeurRepository;
import ch.globaz.vulpecula.domain.specifications.decompte.DecompteCTSPInteretsRequis;
import ch.globaz.vulpecula.domain.specifications.decompte.DecompteMotifProlongationRequisSpecification;
import ch.globaz.vulpecula.external.models.affiliation.Assurance;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;
import ch.globaz.vulpecula.external.services.CotisationService;
import ch.globaz.vulpecula.util.DBUtil;

/**
 * @author JPA
 * 
 */
public class DecompteServiceImpl implements DecompteService {
    private final DecompteRepository decompteRepository;
    private final HistoriqueDecompteRepository historiqueDecompteRepository;
    private final DecompteSalaireRepository decompteSalaireRepository;
    private final AdhesionCotisationPosteTravailRepository adhesionCotisationPosteTravailRepository;
    private final PropertiesService propertiesService;
    private PosteTravailService posteTravailService;

    private final Object lockDecompteSalaire = new Object();

    private static final Logger LOGGER = LoggerFactory.getLogger(DecompteServiceImpl.class);

    private static final int TINY_RANGE = 20;

    private static final Montant PLAFOND_APG = new Montant(148200);

    /**
     * Actuellement non utilisé car nous n'utilisons plus
     * la gestion des dépendances de Jade.
     */
    public DecompteServiceImpl() {
        this(getDecompteRepository(), getHistoriqueDecompteRepository(), getDecompteSalaireRepository(),
                getAdhesionCotisationPosteRepository(), getEmployeurRepository(), getPosteTravailService(),
                getPropertiesService(), getTaxationOfficeService());
    }

    public DecompteServiceImpl(final DecompteRepository decompteRepository,
            final HistoriqueDecompteRepository historiqueDecompteRepository,
            final DecompteSalaireRepository decompteSalaireRepository,
            final AdhesionCotisationPosteTravailRepository adhesionCotisationPosteTravailRepository,
            final EmployeurRepository employeurRepository, final PosteTravailService posteTravailService,
            final PropertiesService propertiesService, final TaxationOfficeService taxationOfficeService) {
        this.decompteRepository = decompteRepository;
        this.historiqueDecompteRepository = historiqueDecompteRepository;
        this.decompteSalaireRepository = decompteSalaireRepository;
        this.adhesionCotisationPosteTravailRepository = adhesionCotisationPosteTravailRepository;
        this.propertiesService = propertiesService;
        this.posteTravailService = posteTravailService;
    }

    @Override
    public Decompte update(Decompte decompte) throws UnsatisfiedSpecificationException, JadePersistenceException {
        new DecompteCTSPInteretsRequis().isSatisfiedBy(decompte);

        Decompte ancienDecompte = VulpeculaRepositoryLocator.getDecompteRepository().findById(decompte.getId());
        // On regarde si la date de rappel a changée et si le motif est bien renseigné
        final DecompteMotifProlongationRequisSpecification motifProlongationSpecification = new DecompteMotifProlongationRequisSpecification(
                ancienDecompte);
        if (motifProlongationSpecification.isSatisfiedBy(decompte)) {
            return VulpeculaRepositoryLocator.getDecompteRepository().update(decompte);
        }
        return decompte;
    }

    @Override
    public void devalider(final String idDecompte) {
        Decompte decompte = decompteRepository.findById(idDecompte);
        devaliderDecompte(decompte);
    }

    @Override
    public void devaliderDecompte(final Decompte decompte) {
        decompte.setEtat(EtatDecompte.OUVERT);
        decompte.setRectifie(false);
        HistoriqueDecompte historique = new HistoriqueDecompte();
        historique.setDecompte(decompte);
        historique.setDate(new Date(JACalendar.today().toStrAMJ()));
        historique.setEtat(EtatDecompte.OUVERT);

        devalider(decompte, historique);
    }

    private void devalider(final Decompte decompte, final HistoriqueDecompte historiqueDecompte) {
        decompteRepository.update(decompte);
        historiqueDecompteRepository.create(historiqueDecompte);
    }

    @Override
    public DifferenceControle controler(final Decompte decompte) {
        return controler(decompte.getId(), false);
    }

    @Override
    public DifferenceControle controler(final String idDecompte, boolean pasDeControle) {
        DifferenceControle differenceControle = new DifferenceControle();
        Decompte decompte = decompteRepository.findById(idDecompte);

        if (TypeProvenance.EBUSINESS.equals(decompte.getTypeProvenance())) {
            LigneDecompteSearchSimpleModel searchModel = new LigneDecompteSearchSimpleModel();
            searchModel.setForIdDecompte(idDecompte);
            searchModel.setForToTreat(true);
            try {
                searchModel = (LigneDecompteSearchSimpleModel) JadePersistenceManager.search(searchModel);
            } catch (JadePersistenceException e) {
                LOGGER.error(e.getMessage());
            }
            if (searchModel.getSize() == 0) {
                differenceControle = DifferenceControle.VALIDE;
                updateEtat(idDecompte, EtatDecompte.VALIDE);
            } else {
                differenceControle = DifferenceControle.ERREUR_EBUSINESS;
            }

        } else {
            Montant difference = propertiesService.getDifferenceAutoriseeControleDecompte();

            List<DecompteSalaire> decompteSalaire = decompteSalaireRepository
                    .findByIdDecompteWithDependencies(idDecompte);
            decompte.setLignes(decompteSalaire);

            if (pasDeControle) {
                differenceControle = DifferenceControle.VALIDE;
            } else {
                differenceControle = decompte.controler(difference, differenceControle);
                differenceControle.setRemarqueRectification(decompte.getRemarqueRectification());
            }

            if (differenceControle.isValid()) {
                updateEtat(idDecompte, EtatDecompte.VALIDE);
            }
        }
        return differenceControle;
    }

    @Override
    public boolean updateEtat(final String idDecompte, EtatDecompte etat) {
        return updateEtat(idDecompte, etat, null);
    }

    public boolean updateEtat(final String idDecompte, EtatDecompte etat, final String remarqueRectificatif) {
        Decompte decompte = decompteRepository.findById(idDecompte);

        decompte.setEtat(etat);
        if (EtatDecompte.RECTIFIE.equals(etat)) {
            decompte.setRectifie(true);
            decompte.setRemarqueRectification(remarqueRectificatif);
        }
        decompteRepository.update(decompte);
        // On set l'historique à l'état initial

        HistoriqueDecompte historique = new HistoriqueDecompte();
        historique.setDecompte(decompte);
        historique.setDate(new Date(JACalendar.today().toStrAMJ()));
        historique.setEtat(etat);
        historiqueDecompteRepository.create(historique);

        return true;
    }

    @Override
    public void ajoutCotisationsPourPoste(final DecompteSalaire decompteSalaire) {
        decompteSalaire.setCotisationsDecompte(getCotisationsPosteTravail(decompteSalaire));
    }

    @Override
    public void ajoutCotisationPourLigneAPG(DecompteSalaire decompteSalaire) {
        decompteSalaire.setCotisationsDecompte(getCotisationAVS(decompteSalaire));
    }

    private List<CotisationDecompte> getCotisationAVS(DecompteSalaire decompteSalaire) {
        List<CotisationDecompte> listCoti = getCotisationsPosteTravail(decompteSalaire);
        List<CotisationDecompte> returnList = new ArrayList<CotisationDecompte>();
        decompteSalaire.getPeriode();
        double nbMonthPeriode = (Periode.getNombreMois(decompteSalaire.getPeriodeDebut(),
                decompteSalaire.getPeriodeFin())) + 1.00;
        double salaireMensuel = decompteSalaire.getSalaireTotal().divide(nbMonthPeriode).doubleValue();
        if (decompteSalaire.getCotisationAC() != null) {
            Assurance assuranceAC = decompteSalaire.getCotisationAC().getAssurance();
            if (new Montant(salaireMensuel).greater(getPlafond(assuranceAC, new Date()).divide(12))) {
                for (CotisationDecompte cotisation : listCoti) {
                    if (cotisation.isForAPGHigh()) {
                        returnList.add(cotisation);
                    }
                }
            } else {
                for (CotisationDecompte cotisation : listCoti) {
                    if (cotisation.isForAPGLow()) {
                        returnList.add(cotisation);
                    }
                }
            }

        } else {
            if (new Montant(salaireMensuel).greater(PLAFOND_APG)) {
                for (CotisationDecompte cotisation : listCoti) {
                    if (cotisation.isForAPGHigh()) {
                        returnList.add(cotisation);
                    }
                }
            } else {
                for (CotisationDecompte cotisation : listCoti) {
                    if (cotisation.isForAPGLow()) {
                        returnList.add(cotisation);
                    }
                }
            }
        }

        return returnList;
    }

    @Override
    public void ajoutCotisationsPourPoste(final DecompteSalaire decompteSalaire, Montant masseAC2) {
        ajoutCotisationsPourPoste(decompteSalaire);
        CotisationDecompte cotisationAC2 = decompteSalaire.getCotisationAC2();
        if (cotisationAC2 != null) {
            cotisationAC2.setMasse(masseAC2);
            cotisationAC2.setMasseForcee(true);
        }
    }

    /**
     * Génération des cotisations pour un décompte selon le type de salaire.
     * <ul>
     * <li>Si le décompte est de type CPP : On prend les cotisations de l'affilié
     * <li>Si le décompte est d'un autre type : On prend les cotisations du poste de travail
     * 
     * @param decompteSalaire Décompte salaire pour lequel générer les cotisations
     * @return Liste de cotisations
     */
    private List<CotisationDecompte> genererTableCotisation(DecompteSalaire decompteSalaire) {
        return genererTableCotisation(decompteSalaire, new TauxCotisationDecompteLoader());
    }

    @Override
    public List<CotisationDecompte> genererTableCotisation(DecompteSalaire decompteSalaire,
            TauxCotisationDecompteLoader tauxCotisationDecompteLoader) {
        if (decompteSalaire.isCPP()) {
            return genererTableCotisationForCPP(decompteSalaire, tauxCotisationDecompteLoader);
        } else {
            return genererTableCotisationGenerique(decompteSalaire, tauxCotisationDecompteLoader);
        }
    }

    /**
     * Génération des cotisations pour un décompte selon les adhésions actives du poste de travail.
     * 
     * @param decompteSalaire Décompte salaire pour lequel générer les cotisations
     * @return Liste de cotisations
     */
    private List<CotisationDecompte> genererTableCotisationGenerique(DecompteSalaire decompteSalaire,
            TauxCotisationDecompteLoader tauxCotisationDecompteLoader) {
        PosteTravail posteTravail = decompteSalaire.getPosteTravail();
        List<CotisationDecompte> listCotisations = new ArrayList<CotisationDecompte>();
        for (AdhesionCotisationPosteTravail adhesionCotisation : posteTravail.getAdhesionsCotisations()) {
            CotisationDecompte cotisationDecompte = createCotisationDecompte(adhesionCotisation,
                    tauxCotisationDecompteLoader.getOrLoadTauxAssurance(decompteSalaire, adhesionCotisation));
            listCotisations.add(cotisationDecompte);
        }
        return listCotisations;
    }

    /**
     * Génération des cotisations pour un décompte selon les cotisations de l'affilié actives durant le début et la fin
     * de l'année de cotisation.
     * 
     * @param decompteSalaire Décompte salaire pour lequel générer les cotisations
     * @return Liste de cotisations
     */
    private List<CotisationDecompte> genererTableCotisationForCPP(DecompteSalaire decompteSalaire,
            TauxCotisationDecompteLoader tauxCotisationDecompteLoader) {
        Annee annee = decompteSalaire.getAnneeCotisations();
        List<Cotisation> cotisations = getCotisationService().findByIdAffilieForDate(decompteSalaire.getIdEmployeur(),
                annee.getFirstDayOfYear());
        List<CotisationDecompte> listCotisations = new ArrayList<CotisationDecompte>();
        for (Cotisation cotisation : cotisations) {
            Taux taux = tauxCotisationDecompteLoader.getOrLoadTauxAssurance(decompteSalaire.getIdEmployeur(),
                    cotisation, decompteSalaire.getDateCalculTaux());
            CotisationDecompte cotisationDecompte = createCotisationDecompte(cotisation, taux);
            listCotisations.add(cotisationDecompte);
        }
        return listCotisations;
    }

    private CotisationDecompte createCotisationDecompte(AdhesionCotisationPosteTravail adhesionCotisation, Taux taux) {
        return createCotisationDecompte(adhesionCotisation, taux, null);
    }

    /**
     * Création d'une cotisation d'un décompte si :
     * <ul>
     * <li>le travailleur est en âge de cotiser.
     * <li>le décompte n'est pas de type complémentaire et la cotisation pas congés payés
     * 
     * @param adhesionCotisation Adhésion à transformer en CotisationDecompte
     * @param nextId Id à affecter à l'objet
     * @param taux Taux à appliquer à la cotisation
     * @return CotisationDecompte si l'adhésion respecte les conditions ci-dessus, null dans les autres cas
     */
    @Override
    public CotisationDecompte createCotisationDecompte(AdhesionCotisationPosteTravail adhesionCotisation, Taux taux,
            String nextId) {
        return createCotisationDecompte(adhesionCotisation.getCotisation(), taux, nextId);
    }

    private CotisationDecompte createCotisationDecompte(Cotisation cotisation, Taux taux) {
        return createCotisationDecompte(cotisation, taux, null);
    }

    private CotisationDecompte createCotisationDecompte(Cotisation cotisation, Taux taux, String nextId) {
        CotisationDecompte cotisationDecompte = new CotisationDecompte();
        cotisationDecompte.setId(nextId);
        cotisationDecompte.setCotisation(cotisation);
        cotisationDecompte.setTaux(taux);
        return cotisationDecompte;
    }

    @Override
    public List<CotisationDecompte> getCotisationsPosteTravail(DecompteSalaire decompteSalaire) {
        PosteTravail posteTravail = decompteSalaire.getPosteTravail();
        if (posteTravail != null) {
            if (posteTravail.getAdhesionsCotisations().isEmpty()) {
                posteTravail.setAdhesionsCotisations(findAdhesionsForDecompteSalaire(decompteSalaire));
            }
        } else {
            throw new NullPointerException(
                    "Les nouveaux travailleurs doivent d'abord être ajoutés dans l'application !");
        }
        return genererTableCotisation(decompteSalaire);
    }

    @Override
    public Montant getPlafond(Assurance assurance, Date date) {
        Montant montant = Montant.ZERO;
        try {
            String plafond = AFUtil.giveParametreAssurance(CodeSystem.GEN_PARAM_ASS_PLAFOND, assurance.getId(),
                    date.getSwissValue(), BSessionUtil.getSessionFromThreadContext()).getValeur();
            montant = new Montant(plafond).divide(12);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return montant;
    }

    @Override
    public NumeroDecompte getNumeroDecompte(final Employeur employeur, final TypeDecompte typeDecompte,
            final Date date, final String periodicite) {
        NumeroDecompte numeroDecompte = getLastNoDecompte(employeur, typeDecompte, date, periodicite);
        if (numeroDecompte == null) {
            return NumeroDecompte.next(typeDecompte, date, periodicite, null);
        } else {
            return NumeroDecompte.next(typeDecompte, date, periodicite, numeroDecompte.getOffset());
        }
    }

    @Override
    public void modifierControleErrone(final String idDecompte) {
        updateEtat(idDecompte, EtatDecompte.ERREUR);
    }

    @Override
    public void rectifierControleErrone(final String idDecompte, final String remarqueRectificatif) {
        updateEtat(idDecompte, EtatDecompte.RECTIFIE, remarqueRectificatif);
    }

    @Override
    public DecompteReceptionEtat receptionner(final Decompte decompte, final Date date) {
        return receptionner(decompte.getId(), date);
    }

    @Override
    public DecompteReceptionEtat receptionner(final String idDecompte, final String date) {
        if (!Date.isValid(date)) {
            return NON_VALIDE;
        }

        return receptionner(idDecompte, new Date(date));
    }

    @Override
    public DecompteReceptionEtat receptionner(final String idDecompte, final Date date) {
        Decompte decompte = decompteRepository.findById(idDecompte);
        if (!decompte.isReceptionnable()) {
            return DEJA_RECEPTIONNE;
        } else if (!decompte.getEtat().equals(EtatDecompte.RECEPTIONNE)) {
            addReceptionneOnHistorique(decompte, date);
            updateEtatAndDateDecompte(decompte, date, EtatDecompte.RECEPTIONNE);
        }
        return VALIDE;
    }

    /**
     * Ajout de l'état réceptionné dans l'historique du décompte en base de données.
     * 
     * @param decompte Décompte à modifier
     * @param dateReception Date à laquelle le décompte a été réceptionné
     */
    private void addReceptionneOnHistorique(final Decompte decompte, final Date dateReception) {
        HistoriqueDecompte historiqueDecompte = new HistoriqueDecompte();
        historiqueDecompte.setDecompte(decompte);
        historiqueDecompte.setDate(dateReception);
        historiqueDecompte.setEtat(EtatDecompte.RECEPTIONNE);
        historiqueDecompteRepository.create(historiqueDecompte);
    }

    @Override
    public void reinitialiseDecompte(final Decompte decompte) {
        if (!EtatDecompte.COMPTABILISE.equals(decompte.getEtat())) {
            addOuvertOnHistorique(decompte);
            updateEtatAndDateDecompte(decompte, null, EtatDecompte.OUVERT);
        }
    }

    private void addOuvertOnHistorique(Decompte decompte) {
        HistoriqueDecompte historiqueDecompte = new HistoriqueDecompte();
        historiqueDecompte.setDecompte(decompte);
        historiqueDecompte.setDate(Date.now());
        historiqueDecompte.setEtat(EtatDecompte.OUVERT);
        historiqueDecompteRepository.create(historiqueDecompte);
    }

    /**
     * Mise à jour de l'état du décompte en base de données selon l'état passé en paramètre.
     * 
     * @param decompte Décompte à modifier
     * @param dateReception Date à laquelle le décompte a été réceptionné
     * @param etatDecompte le nouvel état
     */
    private void updateEtatAndDateDecompte(final Decompte decompte, final Date dateReception, EtatDecompte etatDecompte) {
        decompte.setDateReception(dateReception);
        decompte.setEtat(etatDecompte);
        decompteRepository.update(decompte);
    }

    @Override
    public NumeroDecompte getLastNoDecompte(final Employeur employeur, final TypeDecompte typeDecompte,
            final Date date, final String periodicite) {
        final String QUERY = "SELECT MAX(NUMERO_DECOMPTE) AS NUMERODECOMPTE FROM SCHEMA.PT_DECOMPTES decomptes WHERE ID_PT_EMPLOYEURS=? AND "
                + getWhereTypeSelonType(typeDecompte)
                + " AND SUBSTR(NUMERO_DECOMPTE,1,4)=? AND SUBSTR(NUMERO_DECOMPTE,5,2)=?";

        List<String> params = new ArrayList<String>();
        params.add(employeur.getId());
        params.add(date.getAnnee());
        params.add(NumeroDecompte.getCodeForTypeDecompte(typeDecompte, periodicite, date));

        try {
            List<HashMap<String, Object>> results = DBUtil.executeQuery(QUERY, params, getClass());
            if (results.size() == 0) {
                return null;
            } else {
                Map<String, Object> result = results.get(0);
                String value = (String) result.get("NUMERODECOMPTE");
                if (value == null) {
                    return null;
                }
                value = value.trim();
                return new NumeroDecompte(value);
            }
        } catch (JadePersistenceException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException("Impossible de retrouver le numéro de décompte actuel", e);
        }
    }

    private String getWhereTypeSelonType(TypeDecompte type) {
        if (type.isTraiterAsSpecial()) {
            return "CS_TYPE IN (" + TypeDecompte.SPECIAL_SALAIRE.getValue() + "," + TypeDecompte.CPP.getValue() + ","
                    + TypeDecompte.SPECIAL_CAISSE.getValue() + ")";
        } else {
            return "CS_TYPE=" + type.getValue();
        }
    }

    @Override
    public void annuler(final String idDecompte) {
        if (idDecompte == null) {
            throw new NullPointerException("idDecompte est null.");
        }
        Decompte decompte = decompteRepository.findById(idDecompte);

        if (decompte == null) {
            LOGGER.error("Impossible de trouver le décompte pour l'id : " + idDecompte);
            return;
        }
        annuler(decompte);
    }

    private void annuler(Decompte decompte) {
        decompte.setEtat(EtatDecompte.ANNULE);

        HistoriqueDecompte historique = new HistoriqueDecompte();
        historique.setDecompte(decompte);
        historique.setDate(new Date(JACalendar.today().toStrAMJ()));
        historique.setEtat(EtatDecompte.ANNULE);

        decompteRepository.update(decompte);
        historiqueDecompteRepository.create(historique);
    }

    @Override
    public void annulerDecompteForParticularite(String idAffilie, String dateDebut, String dateFin) {
        Date debut = new Date(dateDebut);
        Periode periode = new Periode(dateDebut, dateFin);
        List<Decompte> liste = VulpeculaRepositoryLocator.getDecompteRepository().findByIdEmployeur(idAffilie, debut);
        for (Decompte decompte : liste) {
            if (decompte.isSansSalaire() && !decompte.isComptabilise()) {

                Periode periodeDecompte = new Periode(decompte.getPeriodeDebut(), decompte.getPeriodeFin());
                if (periode.contains(periodeDecompte)) {
                    if (decompte.isTaxationOffice()) {
                        VulpeculaServiceLocator.getTaxationOfficeService().annulerByIdDecompte(decompte.getId());
                    }

                    annuler(decompte.getId());
                }
            }
        }
    }

    @Override
    public Collection<Decompte> rechercheDecomptesRectificatif() {
        List<Decompte> decomptesForRectification = VulpeculaRepositoryLocator.getDecompteRepository()
                .findRectificatifs();

        // for (Decompte decompte : decomptesForRectification) {
        // decompte.setLignes(VulpeculaRepositoryLocator.getDecompteSalaireRepository()
        // .findByIdDecompteWithDependencies(decompte.getId()));
        // decompte.setAdressePrincipale(VulpeculaRepositoryLocator.getAdresseRepository()
        // .findAdressePrioriteCourrierByIdTiers(decompte.getIdTiers()));
        // }
        return decomptesForRectification;
    }

    @Override
    public Decompte findDecomptePrecedent(Decompte decompte) {
        Decompte decomptePrecedent = VulpeculaRepositoryLocator.getDecompteRepository().findDecomptePrecedent(decompte);
        if (decomptePrecedent == null) {
            return null;
        }

        decomptePrecedent.setLignes(VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                .findByIdDecompteWithDependencies(decomptePrecedent.getId()));
        return decomptePrecedent;
    }

    @Override
    public void imprimerDecompteVideEtBVR(Decompte decompte, String email) throws Exception {
        ImprimerDecomptesProcess process = ImprimerDecomptesProcess.createWithDecomptes(Arrays.asList(decompte));
        process.setEMailAddress(email);

        // On imprime seulement le BVR pour les décomptes de type SP
        if (decompte.isTraiterAsSpecial()) {
            process.dontPrintDecompteVide();
        }
        process.start();
    }

    @Override
    /**
     * Recherche des différents taux pour les types de cotisations AVS, AC et
     * AC2 de l'employeur sous la forme d'un objet {@link CotisationsInfo}.
     *
     * @return {@link CotisationsInfo}
     */
    public CotisationsInfo retrieveCotisationsInfo(String idEmployeur, Date date) {
        Taux tauxAVS = getCotisationService().findTauxForEmployeurAndType(idEmployeur, TypeAssurance.COTISATION_AVS_AI,
                date);
        tauxAVS = tauxAVS.addTaux(getCotisationService().findTauxForEmployeurAndType(idEmployeur,
                TypeAssurance.FRAIS_ADMINISTRATION, date));
        Taux tauxAC = getCotisationService().findTauxForEmployeurAndType(idEmployeur, TypeAssurance.ASSURANCE_CHOMAGE,
                date);
        Taux tauxAC2 = getCotisationService().findTauxForEmployeurAndType(idEmployeur, TypeAssurance.COTISATION_AC2,
                date);

        return new CotisationsInfo(tauxAVS, tauxAC, tauxAC2);
    }

    private CotisationService getCotisationService() {
        return VulpeculaServiceLocator.getCotisationService();
    }

    /**
     * Recherche des éléments relatives aux décomptes, notammenent les postes de
     * travail, l'adresse de l'employeur ainsi que ses cotisations.
     * 
     * @param decompte
     *            décompte à compléter
     * @param date
     *            Date à laquelle l'activité doit être déterminée
     * @return Decompte complet
     */
    @Override
    public Decompte retrieveDecompteInfos(Decompte decompte) {
        Employeur employeur = decompte.getEmployeur();
        employeur.setCotisations(VulpeculaServiceLocator.getCotisationService().findByIdAffilieForDate(
                employeur.getId(), decompte.getPeriodeDebut()));
        employeur.setAdressePrincipale(VulpeculaRepositoryLocator.getAdresseRepository()
                .findAdressePrioriteCourrierByIdTiers(decompte.getEmployeurIdTiers()));
        return decompte;
    }

    /**
     * Retourne la liste des décomptes à passer en sommation.
     */
    private List<Decompte> getDecomptesForSommation() {
        return VulpeculaRepositoryLocator.getDecompteRepository().findDecomptesForSommation();
    }

    /**
     * Ajoute un historique "SOMMATION" à l'historique du décompte.
     */
    private void addSommationToHistorique(Decompte decompte) {
        HistoriqueDecompte historiqueDecompte = new HistoriqueDecompte();
        historiqueDecompte.setDate(Date.now());
        historiqueDecompte.setEtat(EtatDecompte.SOMMATION);
        historiqueDecompte.setDecompte(decompte);
        VulpeculaRepositoryLocator.getHistoriqueDecompteRepository().create(historiqueDecompte);
    }

    private void setNouvelleDateDeRappel(Decompte decompte) {
        int nbJoursSuperieurEtablissement = VulpeculaServiceLocator.getPropertiesService()
                .getTaxationNombreJoursSuperieureEtablissement();
        int nbJoursInferieurEtablissement = VulpeculaServiceLocator.getPropertiesService()
                .getTaxationNombreJoursInferieureEtablissement();
        Date nouvelleDateRappel = decompte.calculerTaxation(nbJoursSuperieurEtablissement,
                nbJoursInferieurEtablissement, Date.now());
        decompte.setDateRappel(nouvelleDateRappel);
    }

    @Override
    public String genererSommationManuel(String vide) throws Exception {
        List<Decompte> decomptesForSommation = getDecomptesForSommation();

        if (decomptesForSommation.isEmpty() == false) {
            for (Decompte decompte : decomptesForSommation) {
                addSommationToHistorique(decompte);
                decompte.setEtat(EtatDecompte.SOMMATION);
                setNouvelleDateDeRappel(decompte);
                VulpeculaRepositoryLocator.getDecompteRepository().update(decompte);
                decompte.getEmployeur().setAdressePrincipale(
                        VulpeculaRepositoryLocator.getAdresseRepository().findAdressePrioriteCourrierByIdTiers(
                                decompte.getEmployeur().getIdTiers()));
            }

            DocumentSommationPrinter documentSommation = new DocumentSommationPrinter(
                    DocumentPrinter.getIds(decomptesForSommation));
            BProcessLauncher.start(documentSommation);
        }
        return "";
    }

    @Override
    public List<AdhesionCotisationPosteTravail> findAdhesionsForDecompteSalaire(DecompteSalaire decompteSalaire) {
        List<AdhesionCotisationPosteTravail> adhesions = adhesionCotisationPosteTravailRepository
                .findByIdPosteTravailAndPeriode(decompteSalaire.getIdPosteTravail(), decompteSalaire.getPeriode());
        return aPrendreEnCompte(decompteSalaire, adhesions);
    }

    private List<AdhesionCotisationPosteTravail> aPrendreEnCompte(DecompteSalaire decompteSalaire,
            List<AdhesionCotisationPosteTravail> adhesions) {
        List<AdhesionCotisationPosteTravail> adhesionsAPrendre = new ArrayList<AdhesionCotisationPosteTravail>();
        for (AdhesionCotisationPosteTravail adhesion : adhesions) {
            if (aPrendreEnCompte(decompteSalaire, adhesion)) {
                adhesionsAPrendre.add(adhesion);
            }
        }
        return adhesionsAPrendre;
    }

    private boolean aPrendreEnCompte(DecompteSalaire decompteSalaire, AdhesionCotisationPosteTravail adhesion) {
        Decompte decompte = decompteSalaire.getDecompte();
        return !adhesion.aIgnorer(decompte.getType(), decompte.getConvention())
                && isEnAgeDeCotiser(adhesion, decompteSalaire);
    }

    @Override
    public List<AdhesionCotisationPosteTravail> findAdhesionsForDecompteSalaireCP(DecompteSalaire decompteSalaire) {
        List<AdhesionCotisationPosteTravail> adhesionsAPrendre = new ArrayList<AdhesionCotisationPosteTravail>();
        List<AdhesionCotisationPosteTravail> adhesions = adhesionCotisationPosteTravailRepository
                .findByIdPosteTravailForCP(decompteSalaire);
        for (AdhesionCotisationPosteTravail adhesion : adhesions) {
            if (aPrendreEnCompte(decompteSalaire, adhesion)) {
                adhesionsAPrendre.add(adhesion);
            }
        }
        return adhesionsAPrendre;
    }

    private boolean isEnAgeDeCotiser(AdhesionCotisationPosteTravail adhesion, DecompteSalaire decompteSalaire) {
        return isEnAgeDeCotiser(adhesion.getCotisation(), decompteSalaire);
    }

    private boolean isEnAgeDeCotiser(Cotisation cotisation, DecompteSalaire decompteSalaire) {
        return VulpeculaServiceLocator.getCotisationService().isEnAgeDeCotiser(decompteSalaire.getTravailleur(),
                cotisation.getId(), Date.getFirstDayOfYear(decompteSalaire.getAnneeFin()));
    }

    @Override
    public String findTextePersonneReference(Decompte decompte) {
        CodeLangue langue = decompte.getEmployeurLangue();
        if (CodeLangue.DE.equals(langue)) {
            // Property
            PropertiesServiceImpl propertiesServiceImpl = new PropertiesServiceImpl();

            String texteConcerneAllemand;
            String numeroAffilie = decompte.getEmployeurAffilieNumero();
            if (numeroAffilie.substring(numeroAffilie.length() - 2).equals("05")) {
                texteConcerneAllemand = propertiesServiceImpl.getTexteRectificatifAllemandElectricien();
            } else {
                texteConcerneAllemand = propertiesServiceImpl.getTexteRectificatifAllemand();
            }

            return texteConcerneAllemand;
        } else {
            // Récupération de l'historique du décompte pour trouver le dernier utilisateur actif dessus
            String idCurrentDecompte = decompte.getId();

            List<HistoriqueDecompte> listHistorique = VulpeculaRepositoryLocator.getHistoriqueDecompteRepository()
                    .findLastHistoriqueValidationDecompte(decompte);

            if (listHistorique.size() == 0) {
                LOGGER.error("Impossible de trouver un décompte validé pour le décompte : " + idCurrentDecompte);
            } else {
                HistoriqueDecompte dernierHistoriqueDecompte = listHistorique.get(0);

                BSpy lastSpy = new BSpy(dernierHistoriqueDecompte.getSpy());

                try {
                    JadeUser userCollaborateur = JadeAdminServiceLocatorProvider.getLocator().getUserService()
                            .load(lastSpy.getUser());
                    String nomPrenom = userCollaborateur.getFirstname() + " " + userCollaborateur.getLastname();
                    String telephone = userCollaborateur.getPhone();
                    return nomPrenom + " - " + telephone;
                } catch (Exception ex) {
                    LOGGER.error("Utilisateur de la dernière modification non trouvé : " + lastSpy.getUser());
                }
            }
        }
        return "";
    }

    @Override
    public void notifierSynchronisationEbu(String idDecompte, String idEmployeur, TypeDecompte type)
            throws JadePersistenceException {
        // On regarde si il s'agit d'un employeur eBusiness
        // et que le numéro de décompte ne soit pas dans la liste de synchronisation
        if (checkSyncDecompte(idEmployeur, idDecompte, type)) {
            SynchronisationEbuSimpleModel synchronisation = new SynchronisationEbuSimpleModel();
            synchronisation.setIdDecompte(idDecompte);
            synchronisation.setDateAjout(Date.now().getSwissValue());
            JadePersistenceManager.add(synchronisation);
        }
    }

    private boolean checkSyncDecompte(String idEmployeur, String idDecompte, TypeDecompte type) {
        boolean isEbusiness = VulpeculaRepositoryLocator.getSynchronisationRepository().mustBeSynchronized(idDecompte,
                type);
        if (isEbusiness) {
            List<Synchronisation> listSyncDecomptes = VulpeculaRepositoryLocator.getSynchronisationRepository()
                    .findDecompteToSynchronize(idEmployeur);
            for (Synchronisation sync : listSyncDecomptes) {
                if (idDecompte.equals(sync.getDecompte().getId())) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void actualiserTauxCotisations(DecompteSalaire decompteSalaire, Annee annee) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean hasPrestationsAFOuPasDossier(Decompte decompte) {
        DossierComplexSearchModel dsm = new DossierComplexSearchModel();
        dsm.setForNumeroAffilie(decompte.getEmployeurAffilieNumero());
        dsm.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        dsm.setForDebutValidite(decompte.getPeriodeDebutFormate());
        dsm.setForFinValidite(decompte.getPeriodeFinFormate());
        dsm.setWhereKey(DossierComplexSearchModel.SEARCH_DOSSIERS_ACTIF_COMPTA_VULPECULA);

        try {
            dsm = ALServiceLocator.getDossierComplexModelService().search(dsm);
            JadeAbstractModel[] searchResults = dsm.getSearchResults();
            if (searchResults.length == 0) {
                return true;
            }
            for (int i = 0; i < searchResults.length; i++) {
                EntetePrestationSearchComplexModel searchModel = new EntetePrestationSearchComplexModel();
                DossierComplexModel dossierComplexModel = (DossierComplexModel) searchResults[i];
                searchModel.setForIdDossier(dossierComplexModel.getId());
                searchModel.setForPeriodeDeAfterOrEquals(decompte.getPeriodeDebut());
                searchModel.setForPeriodeABeforeOrEquals(decompte.getPeriodeFin());
                if (JadePersistenceManager.count(searchModel) > 0) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<DecompteSalaire> removeLineFromControleEmployeur(List<DecompteSalaire> allLignes) {
        List<DecompteSalaire> sortedLignes = new ArrayList<DecompteSalaire>();
        for (DecompteSalaire ligne : allLignes) {
            if (!ligne.getDecompte().isControleEmployeur()) {
                sortedLignes.add(ligne);
            }
        }
        return sortedLignes;

    }

    private List<Decompte> removeControleEmployeur(List<Decompte> allDecomptes) {
        List<Decompte> sortedDecompte = new ArrayList<Decompte>();
        for (Decompte decompte : allDecomptes) {
            if (!decompte.isControleEmployeur()) {
                sortedDecompte.add(decompte);
            }
        }
        return sortedDecompte;

    }

    private boolean posteIsInDecompte(PosteTravail poste, Decompte decompte2) {
        Decompte decompte = VulpeculaRepositoryLocator.getDecompteRepository().findByIdWithDependencies(
                decompte2.getId());
        for (DecompteSalaire ligne : decompte.getLignes()) {
            if (poste.getId().equals(ligne.getPosteTravail().getId())
                    || (!poste.getPosteCorrelationId().isEmpty() && poste.getPosteCorrelationId().equals(
                            ligne.getPosteCorrelationId()))) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void actualiserDecompteForExternalServices(String idEmployeur, String dateATraiter)
            throws JadePersistenceException, UnsatisfiedSpecificationException {

        List<Decompte> listeDecomptes = VulpeculaRepositoryLocator.getDecompteRepository()
                .findOuvertGenereATraiterByIdEmployeur(idEmployeur, new Date(dateATraiter));
        listeDecomptes = removeControleEmployeur(listeDecomptes);
        for (Decompte decompte : listeDecomptes) {
            for (DecompteSalaire lineToModify : decompte.getLignes()) {
                PosteTravail posteTravail = lineToModify.getPosteTravail();
                if (lineToModify.getDecompte().getType().equals(TypeDecompte.COMPLEMENTAIRE)) {
                    posteTravail.setAdhesionsCotisations(findAdhesionsForDecompteSalaireCP(lineToModify));
                } else {
                    posteTravail.setAdhesionsCotisations(findAdhesionsForDecompteSalaire(lineToModify));
                }
                lineToModify.setPosteTravail(posteTravail);

                // Efface toutes les cotisations pour cette ligne de décompte
                VulpeculaServiceLocator.getCotisationDecompteService().deleteCotisationDecompteWithoutRecalcul(
                        lineToModify.getId());

                lineToModify.setCotisationsDecompte(genererTableCotisation(lineToModify));

                VulpeculaServiceLocator.getDecompteSalaireService().update(lineToModify);
            }
        }
    }

    @Override
    public void actualiserDecompteSelonPoste(PosteTravail posteTravailCreated) throws JadePersistenceException,
            UnsatisfiedSpecificationException {
        // retrieve les décomptes pour la période du poste à jour et toute les lignes concernant le poste de travail
        // existantes.
        List<Decompte> listeDecomptes = null;
        if (posteTravailCreated.getFinActivite() != null) {
            PeriodeMensuelle periode = new PeriodeMensuelle(posteTravailCreated.getDebutActivite(),
                    posteTravailCreated.getFinActivite());
            listeDecomptes = VulpeculaRepositoryLocator.getDecompteRepository()
                    .findOuvertGenereATraiterByIdEmployeurPeriode(posteTravailCreated.getIdEmployeur(), periode);

        } else {
            listeDecomptes = VulpeculaRepositoryLocator.getDecompteRepository().findOuvertGenereATraiterByIdEmployeur(
                    posteTravailCreated.getIdEmployeur(), posteTravailCreated.getDebutActivite());
        }

        listeDecomptes = removeControleEmployeur(listeDecomptes);

        List<Decompte> toBeAdded = new ArrayList<Decompte>();
        for (Decompte decompte : listeDecomptes) {
            if (!posteIsInDecompte(posteTravailCreated, decompte)) {
                toBeAdded.add(decompte);
            }
        }
        for (Decompte decompte : toBeAdded) {
            addLigne(posteTravailCreated, decompte);
        }

        List<DecompteSalaire> listLignes = VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                .findDecompteOuvertGenereATraiterByIdPosteTravail(posteTravailCreated.getId());

        listLignes = removeLineFromControleEmployeur(listLignes);
        for (DecompteSalaire ligne : listLignes) {
            updateLigne(posteTravailCreated, ligne.getDecompte(), ligne.getId(), ligne.getSequence());
        }
    }

    private void updateLigne(PosteTravail posteTravailCreated, Decompte decompte, String idLineToModify,
            String sequenceLineToModify) throws UnsatisfiedSpecificationException, JadePersistenceException {
        Date debutActivite = decompte.getPeriodeDebut();
        Date finActivite = decompte.getPeriodeFin();
        DecompteSalaire lineToModify = VulpeculaRepositoryLocator.getDecompteSalaireRepository().findById(
                idLineToModify);
        if (posteTravailCreated.getPosteCorrelationId() != null) {
            lineToModify.setPosteCorrelationId(posteTravailCreated.getPosteCorrelationId());
        }
        lineToModify.setDecompte(decompte);
        lineToModify.setSequence(sequenceLineToModify);
        if (decompte.getPeriodeDebut().before(posteTravailCreated.getDebutActivite())) {
            debutActivite = posteTravailCreated.getDebutActivite();
        }
        if (posteTravailCreated.getFinActivite() != null
                && decompte.getPeriodeFin().after(posteTravailCreated.getFinActivite())) {
            finActivite = posteTravailCreated.getFinActivite().getLastDayOfMonth();
        }

        lineToModify.setPeriode(new Periode(debutActivite.getFirstDayOfMonth(), finActivite.getLastDayOfMonth()));

        // Si mensuel alors on set le nbre d'h de la convention
        if (posteTravailCreated.isMensuel() && TypeDecompte.PERIODIQUE.equals(decompte.getType())) {
            lineToModify.setHeures(posteTravailService.getNombreHeuresParMois(posteTravailCreated.getId(),
                    decompte.getPeriodeDebut(), decompte.getPeriodeFin()));
        } else {
            lineToModify.setHeures(0.00);
        }

        if (lineToModify.getDecompte().getType().equals(TypeDecompte.COMPLEMENTAIRE)) {
            posteTravailCreated.setAdhesionsCotisations(findAdhesionsForDecompteSalaireCP(lineToModify));
        } else {
            posteTravailCreated.setAdhesionsCotisations(findAdhesionsForDecompteSalaire(lineToModify));
        }

        lineToModify.setPosteTravail(posteTravailCreated);

        for (CotisationDecompte cotisation : lineToModify.getCotisationsDecompte()) {
            VulpeculaServiceLocator.getCotisationDecompteService().deleteCotisationDecompteWithoutRecalcul(
                    lineToModify.getId(), cotisation.getId());
        }

        lineToModify.setCotisationsDecompte(genererTableCotisation(lineToModify));

        VulpeculaServiceLocator.getDecompteSalaireService().update(lineToModify);
    }

    private void addLigne(PosteTravail posteTravailCreated, Decompte decompte) throws JadePersistenceException {
        Date debutActivite = decompte.getPeriodeDebut();
        Date finActivite = decompte.getPeriodeFin();
        List<DecompteSalaire> listeLine = decompte.getLignes();
        DecompteSalaire lineToAdd = new DecompteSalaire();
        lineToAdd.setId(nextIdDecompteSalaire());
        lineToAdd.setPosteTravail(posteTravailCreated);
        if (posteTravailCreated.getPosteCorrelationId() != null) {
            lineToAdd.setPosteCorrelationId(posteTravailCreated.getPosteCorrelationId());
        }
        lineToAdd.setDecompte(decompte);
        lineToAdd.setSequence(String.valueOf(listeLine.size() + 1));
        if (decompte.getPeriodeDebut().before(posteTravailCreated.getDebutActivite())) {
            debutActivite = posteTravailCreated.getDebutActivite();
        }
        if (posteTravailCreated.getFinActivite() != null
                && decompte.getPeriodeFin().after(posteTravailCreated.getFinActivite())) {
            finActivite = posteTravailCreated.getFinActivite().getLastDayOfMonth();
        }
        lineToAdd.setPeriode(new Periode(debutActivite.getFirstDayOfMonth(), finActivite.getLastDayOfMonth()));

        // Si mensuel alors on set le nbre d'h de la convention
        if (posteTravailCreated.isMensuel() && TypeDecompte.PERIODIQUE.equals(decompte.getType())) {
            lineToAdd.setHeures(posteTravailService.getNombreHeuresParMois(posteTravailCreated.getId(),
                    decompte.getPeriodeDebut(), decompte.getPeriodeFin()));
        } else {
            lineToAdd.setHeures(0.00);
        }

        if (lineToAdd.getDecompte().getType().equals(TypeDecompte.COMPLEMENTAIRE)) {
            posteTravailCreated.setAdhesionsCotisations(findAdhesionsForDecompteSalaireCP(lineToAdd));
        } else {
            posteTravailCreated.setAdhesionsCotisations(findAdhesionsForDecompteSalaire(lineToAdd));
        }

        lineToAdd.setCotisationsDecompte(genererTableCotisation(lineToAdd));
        lineToAdd.setaTraiter(false);

        decompteSalaireRepository.create(lineToAdd);
    }

    private String nextIdDecompteSalaire() throws JadePersistenceException {
        JadePersistencePKProvider pkProviderDecompteSalaire = JadePersistenceManager.getPrimaryKeyProvider(
                LigneDecompteSimpleModel.class, TINY_RANGE);
        synchronized (lockDecompteSalaire) {
            return pkProviderDecompteSalaire.getNextPrimaryKey();
        }
    }

}
