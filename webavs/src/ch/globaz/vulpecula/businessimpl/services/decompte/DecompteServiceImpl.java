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
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(DecompteServiceImpl.class);

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
    }

    @Override
    public Decompte update(Decompte decompte) throws UnsatisfiedSpecificationException {
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
        Montant difference = propertiesService.getDifferenceAutoriseeControleDecompte();

        Decompte decompte = decompteRepository.findById(idDecompte);
        List<DecompteSalaire> decompteSalaire = decompteSalaireRepository.findByIdDecompteWithDependencies(idDecompte);
        decompte.setLignes(decompteSalaire);

        if (pasDeControle) {
            differenceControle = DifferenceControle.VALIDE;
        } else {
            differenceControle = decompte.controler(difference, differenceControle);
            differenceControle.setRemarqueRectification(decompte.getRemarqueRectification());
        }

        if (differenceControle.isValid()) {
            decompte.setEtat(EtatDecompte.VALIDE);
            decompteRepository.update(decompte);
            // On set l'historique à l'état initial

            HistoriqueDecompte historique = new HistoriqueDecompte();
            historique.setDecompte(decompte);
            historique.setDate(new Date(JACalendar.today().toStrAMJ()));
            historique.setEtat(EtatDecompte.VALIDE);
            historiqueDecompteRepository.create(historique);
        }
        return differenceControle;
    }

    @Override
    public void ajoutCotisationsPourPoste(final DecompteSalaire decompteSalaire) {
        decompteSalaire.setCotisationsDecompte(getCotisationsPosteTravail(decompteSalaire));
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

    private List<CotisationDecompte> genererTableCotisation(DecompteSalaire decompteSalaire) {
        PosteTravail posteTravail = decompteSalaire.getPosteTravail();
        TauxCotisationDecompteLoader tauxCotisationDecompteLoader = new TauxCotisationDecompteLoader();
        List<CotisationDecompte> listCotisations = new ArrayList<CotisationDecompte>();
        for (AdhesionCotisationPosteTravail adhesionCotisation : posteTravail.getAdhesionsCotisations()) {
            CotisationDecompte cotisationDecompte = createCotisationDecompte(decompteSalaire, adhesionCotisation,
                    decompteSalaire.getPeriode().getDateFin(),
                    tauxCotisationDecompteLoader.getOrLoadTauxAssurance(decompteSalaire, adhesionCotisation), null);
            if (cotisationDecompte != null) {
                listCotisations.add(cotisationDecompte);
            }
        }
        return listCotisations;
    }

    @Override
    public CotisationDecompte createCotisationDecompte(DecompteSalaire decompteSalaire,
            AdhesionCotisationPosteTravail adhesionCotisation, Date dateReference, Taux taux, String nextId) {
        CotisationDecompte cotisationDecompte = new CotisationDecompte();
        cotisationDecompte.setId(nextId);
        cotisationDecompte.setCotisation(adhesionCotisation.getCotisation());
        cotisationDecompte.setTaux(taux);
        return cotisationDecompte;
    }

    @Override
    public List<CotisationDecompte> getCotisationsPosteTravail(DecompteSalaire decompteSalaire) {
        PosteTravail posteTravail = decompteSalaire.getPosteTravail();
        if (posteTravail.getAdhesionsCotisations().size() == 0) {
            posteTravail.setAdhesionsCotisations(findAdhesionsForDecompteSalaire(decompteSalaire));
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
        Decompte decompte = decompteRepository.findById(idDecompte);
        decompte.setEtat(EtatDecompte.ERREUR);
        decompteRepository.update(decompte);

        // On set l'historique
        HistoriqueDecompte historique = new HistoriqueDecompte();
        historique.setDecompte(decompte);
        historique.setDate(new Date(JACalendar.today().toStrAMJ()));
        historique.setEtat(EtatDecompte.ERREUR);

        historiqueDecompteRepository.create(historique);
    }

    @Override
    public void rectifierControleErrone(final String idDecompte, final String remarqueRectificatif) {
        Decompte decompte = decompteRepository.findById(idDecompte);
        decompte.setEtat(EtatDecompte.RECTIFIE);
        decompte.setRectifie(true);
        decompte.setRemarqueRectification(remarqueRectificatif);
        decompteRepository.update(decompte);

        // On set l'historique
        HistoriqueDecompte historique = new HistoriqueDecompte();
        historique.setDecompte(decompte);
        historique.setDate(new Date(JACalendar.today().toStrAMJ()));
        historique.setEtat(EtatDecompte.RECTIFIE);

        historiqueDecompteRepository.create(historique);
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
            updateEtatDecompte(decompte, date);
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

    /**
     * Mise à jour de l'état du décompte en réceptionné en base de données.
     * 
     * @param decompte Décompte à modifier
     * @param dateReception Date à laquelle le décompte a été réceptionné
     */
    private void updateEtatDecompte(final Decompte decompte, final Date dateReception) {
        decompte.setDateReception(dateReception);
        decompte.setEtat(EtatDecompte.RECEPTIONNE);
        decompteRepository.update(decompte);
    }

    @Override
    public NumeroDecompte getLastNoDecompte(final Employeur employeur, final TypeDecompte typeDecompte,
            final Date date, final String periodicite) {
        final String QUERY = "SELECT MAX(NUMERO_DECOMPTE) AS NUMERODECOMPTE FROM SCHEMA.PT_DECOMPTES decomptes WHERE ID_PT_EMPLOYEURS=? AND CS_TYPE=? AND SUBSTR(NUMERO_DECOMPTE,1,4)=? AND SUBSTR(NUMERO_DECOMPTE,5,2)=?";

        List<String> params = new ArrayList<String>();
        params.add(employeur.getId());
        params.add(typeDecompte.getValue());
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
            return null;
        }
    }

    @Override
    public void annuler(final String idDecompte) {
        if (idDecompte == null) {
            throw new NullPointerException("idDecompte est null.");
        }
        Decompte decompte = decompteRepository.findById(idDecompte);

        if (decompte == null) {
            // TODO Exception métier
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
        List<Decompte> decomptesComptabilises = new ArrayList<Decompte>();
        List<Decompte> liste = VulpeculaRepositoryLocator.getDecompteRepository().findByIdEmployeur(idAffilie, debut);
        for (Decompte decompte : liste) {
            Periode periodeDecompte = new Periode(decompte.getPeriodeDebut(), decompte.getPeriodeFin());
            if (periode.contains(periodeDecompte)) {
                annuler(decompte.getId());
                if (decompte.isComptabilise() || decompte.isTaxationOffice()) {
                    decomptesComptabilises.add(decompte);
                }
            }
        }

        if (decomptesComptabilises.size() > 0) {
            AnnulerDecompteComptabilise annuler = new AnnulerDecompteComptabilise(decomptesComptabilises);
            annuler.annulerSection("Annulation décomptes - "
                    + decomptesComptabilises.get(0).getEmployeurAffilieNumero());
        }

    }

    @Override
    public List<Decompte> rechercheDecomptesRectificatif() {
        List<Decompte> decomptesForRectification = VulpeculaRepositoryLocator.getDecompteRepository()
                .findRectificatifs();
        for (Decompte decompte : decomptesForRectification) {
            decompte.setLignes(VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                    .findByIdDecompteWithDependencies(decompte.getId()));
            decompte.setAdressePrincipale(VulpeculaRepositoryLocator.getAdresseRepository()
                    .findAdressePrioriteCourrierByIdTiers(decompte.getIdTiers()));
        }
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
        if (decompte.getType().equals(TypeDecompte.SPECIAL)) {
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
            // documentSommation.setEMailAddress(ge);
            // documentSommation.setSession(bsession);
            BProcessLauncher.start(documentSommation);
        }
        return "";
    }

    @Override
    public void genererDecomptesSansTravailleurs(Date dateEtablissement, PeriodeMensuelle periodeMensuelle, String email) {
        GenererDecompteSansTravailleurProcess process = new GenererDecompteSansTravailleurProcess(dateEtablissement,
                periodeMensuelle, email);
        process.start();
    }

    @Override
    public List<AdhesionCotisationPosteTravail> findAdhesionsForDecompteSalaire(DecompteSalaire decompteSalaire) {
        Decompte decompte = decompteSalaire.getDecompte();
        List<AdhesionCotisationPosteTravail> adhesionsAPrendre = new ArrayList<AdhesionCotisationPosteTravail>();
        List<AdhesionCotisationPosteTravail> adhesions = adhesionCotisationPosteTravailRepository.findByIdPosteTravail(
                decompteSalaire.getIdPosteTravail(), decompteSalaire.getPeriodeFin());
        for (AdhesionCotisationPosteTravail adhesion : adhesions) {
            if (!adhesion.aIgnorer(decompte.getType(), decompte.getConvention())
                    && isEnAgeDeCotiser(adhesion, decompteSalaire)) {
                adhesionsAPrendre.add(adhesion);
            }
        }
        return adhesionsAPrendre;
    }

    private boolean isEnAgeDeCotiser(AdhesionCotisationPosteTravail adhesion, DecompteSalaire decompteSalaire) {
        return VulpeculaServiceLocator.getCotisationService().isEnAgeDeCotiser(decompteSalaire.getTravailleur(),
                adhesion.getIdCotisation(), Date.getFirstDayOfYear(decompteSalaire.getAnneeFin()));
    }

    @Override
    public String findTextePersonneReference(Decompte decompte) {
        CodeLangue langue = decompte.getEmployeurLangue();
        if (CodeLangue.DE.equals(langue)) {
            // Property
            PropertiesServiceImpl propertiesServiceImpl = new PropertiesServiceImpl();
            String texteConcerneAllemand = propertiesServiceImpl.getTexteRectificatifAllemand();
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
}
