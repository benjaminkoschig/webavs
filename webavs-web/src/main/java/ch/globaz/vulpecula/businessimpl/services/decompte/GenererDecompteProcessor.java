package ch.globaz.vulpecula.businessimpl.services.decompte;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.models.decomptes.CotisationDecompteSimpleModel;
import ch.globaz.vulpecula.business.models.decomptes.DecompteSimpleModel;
import ch.globaz.vulpecula.business.models.decomptes.HistoriqueDecompteSimpleModel;
import ch.globaz.vulpecula.business.models.decomptes.LigneDecompteSimpleModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.decompte.DecompteService;
import ch.globaz.vulpecula.business.services.postetravail.PosteTravailService;
import ch.globaz.vulpecula.business.services.properties.PropertiesService;
import ch.globaz.vulpecula.business.services.taxationoffice.TaxationOfficeService;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.PeriodeMensuelle;
import ch.globaz.vulpecula.domain.models.decompte.CotisationDecompte;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.HistoriqueDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeProvenance;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteRepository;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteSalaireRepository;
import ch.globaz.vulpecula.domain.repositories.decompte.HistoriqueDecompteRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.EmployeurRepository;
import ch.globaz.vulpecula.external.models.affiliation.Adhesion;
import ch.globaz.vulpecula.external.models.affiliation.Particularite;
import ch.globaz.vulpecula.external.models.hercule.InteretsMoratoires;
import ch.globaz.vulpecula.process.decompte.PTProcessDecompteInfo;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.JadePersistencePKProvider;

public class GenererDecompteProcessor {
    private static final String ENTREPRISE_TRAVAIL_INTERIMAIRE = "133";
    private static final String CODE_PERIODICITE_ANNUELLE = "802004";
    private static final int ONE_RANGE = 1;
    private static final int TINY_RANGE = 20;
    private static final int SMALL_RANGE = 500;
    private static final int MEDIUM_RANGE = 1000;

    private JadePersistencePKProvider pkProviderDecompte;
    private JadePersistencePKProvider pkProviderHistoriqueDecompte;
    private JadePersistencePKProvider pkProviderDecompteSalaire;
    private JadePersistencePKProvider pkProviderCotisation;

    private final Object lockDecompte = new Object();
    private final Object lockHistoriqueDecompte = new Object();
    private final Object lockDecompteSalaire = new Object();
    private final Object lockCotisation = new Object();

    private boolean optimized;

    private TauxCotisationDecompteLoader tauxCotisationDecompteLoader = new TauxCotisationDecompteLoader();

    private DecompteRepository decompteRepository = VulpeculaRepositoryLocator.getDecompteRepository();
    private HistoriqueDecompteRepository historiqueDecompteRepository = VulpeculaRepositoryLocator
            .getHistoriqueDecompteRepository();
    private DecompteSalaireRepository decompteSalaireRepository = VulpeculaRepositoryLocator
            .getDecompteSalaireRepository();
    private PosteTravailService posteTravailService = VulpeculaServiceLocator.getPosteTravailService();
    private PropertiesService propertiesService = VulpeculaServiceLocator.getPropertiesService();
    private DecompteService decompteService = VulpeculaServiceLocator.getDecompteService();
    private TaxationOfficeService taxationOfficeService = VulpeculaServiceLocator.getTaxationOfficeService();
    private EmployeurRepository employeurRepository = VulpeculaRepositoryLocator.getEmployeurRepository();

    GenererDecompteProcessor(String test) {
        // Constructeur utilisé uniquement pour les tests
    }

    public GenererDecompteProcessor() {
        this(false);
    }

    public GenererDecompteProcessor(boolean optimized) {
        try {
            if (optimized) {
                pkProviderDecompte = JadePersistenceManager.getPrimaryKeyProvider(DecompteSimpleModel.class,
                        SMALL_RANGE);
                pkProviderHistoriqueDecompte = JadePersistenceManager
                        .getPrimaryKeyProvider(HistoriqueDecompteSimpleModel.class, SMALL_RANGE);
                pkProviderDecompteSalaire = JadePersistenceManager.getPrimaryKeyProvider(LigneDecompteSimpleModel.class,
                        MEDIUM_RANGE);
                pkProviderCotisation = JadePersistenceManager.getPrimaryKeyProvider(CotisationDecompteSimpleModel.class,
                        MEDIUM_RANGE);
            } else {
                pkProviderDecompte = JadePersistenceManager.getPrimaryKeyProvider(DecompteSimpleModel.class, ONE_RANGE);
                pkProviderHistoriqueDecompte = JadePersistenceManager
                        .getPrimaryKeyProvider(HistoriqueDecompteSimpleModel.class, ONE_RANGE);
                pkProviderDecompteSalaire = JadePersistenceManager.getPrimaryKeyProvider(LigneDecompteSimpleModel.class,
                        TINY_RANGE);
                pkProviderCotisation = JadePersistenceManager.getPrimaryKeyProvider(CotisationDecompteSimpleModel.class,
                        TINY_RANGE);
            }
        } catch (JadePersistenceException ex) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, ex);
        }
    }

    public Decompte genererDecompteVideManuel(Decompte decompte, boolean withPostesAndDocuments, String email)
            throws UnsatisfiedSpecificationException, JadePersistenceException {
        if (TypeDecompte.PERIODIQUE.equals(decompte.getType())
                && taxationOfficeService.hasTO(decompte.getEmployeur(), decompte.getPeriodeDebut())) {
            throw new UnsatisfiedSpecificationException(SpecificationMessage.EMPLOYEUR_POSSEDE_TO_ACTIVE);
        }

        decompte = genererDecompteVideManuel(decompte);
        if (withPostesAndDocuments) {
            genererLignesDecompte(decompte);
            try {
                decompteService.imprimerDecompteVideEtBVR(decompte, email);
            } catch (Exception e) {
                throw new UnsatisfiedSpecificationException(
                        SpecificationMessage.PROBLEME_IMPRESSION_DECOMPTE_BVR_OU_VIDE);
            }
        }
        return decompte;
    }

    public Decompte genererDecompteVideManuel(final Decompte decompte)
            throws UnsatisfiedSpecificationException, JadePersistenceException {
        // On vérifie les spécifications du décompte
        decompte.validate();
        decompte.setId(nextIdDecompte());
        decompte.setManuel(true);
        decompte.setTypeProvenance(TypeProvenance.WEBMETIER_MANUEL);
        decompte.setEmployeur(employeurRepository.findByIdAffilie(decompte.getEmployeur().getId()));
        decompte.setEtat(EtatDecompte.OUVERT);
        if (decompte.isCPP()) {
            decompte.setInteretsMoratoires(InteretsMoratoires.EXEMPTE);
        }
        // Dans tous les cas on génère un nouveau numéro de décompte
        decompte.setNumeroDecompte(decompteService.getNumeroDecompte(decompte.getEmployeur(), decompte.getType(),
                decompte.getPeriodeDebut(), decompte.getPeriodiciteEmployeur()));
        decompte.calculerAndSetRappel(VulpeculaServiceLocator.getPropertiesService().getRappelNombreJours());
        decompte.calculerAndSetControleAC2();
        decompteRepository.create(decompte);

        HistoriqueDecompte historique = new HistoriqueDecompte();
        historique.setId(nextIdHistoriqueDecompte());
        historique.setDecompte(decompte);
        historique.setDate(new Date(JACalendar.today().toStrAMJ()));
        historique.setEtat(EtatDecompte.OUVERT);

        historiqueDecompteRepository.create(historique);

        return decompte;
    }

    public List<Decompte> genererDecompte(final Employeur employeur, final PTProcessDecompteInfo decompteInfo)
            throws JadePersistenceException {
        List<Decompte> decomptes = new ArrayList<Decompte>();

        // On génère des décomptes pour chaque mois dans la période du
        if (TypeDecompte.COMPLEMENTAIRE.equals(decompteInfo.getType())) {
            Adhesion adhesion = VulpeculaRepositoryLocator.getAdhesionRepository().findCaisseMetier(employeur.getId());
            if (adhesion != null && ENTREPRISE_TRAVAIL_INTERIMAIRE.equals(adhesion.getCodeAdministrationPlanCaisse())
                    && !CODE_PERIODICITE_ANNUELLE.equals(employeur.getPeriodicite())) {
                return decomptes;
            }
            Decompte decompte = decompteRepository.findDecompteForAnnee(employeur, new Annee(decompteInfo.getAnnee()),
                    decompteInfo.getType());
            if (decompte != null) {
                return decomptes;
            }
            if (!hasParticulariteSurAnneeComplete(employeur, new Annee(decompteInfo.getAnnee()))) {
                decomptes.add(genererDecompteComplementaire(employeur, decompteInfo));
            }
        } else if (TypeDecompte.PERIODIQUE.equals(decompteInfo.getType())) {
            decomptes.addAll(genererDecomptePeriodique(employeur, decompteInfo));
        }
        return decomptes;
    }

    private Decompte genererDecompteComplementaire(Employeur employeur, PTProcessDecompteInfo decompteInfo)
            throws JadePersistenceException {

        Date dateDebutDecompte = decompteInfo.getPeriode().getPeriodeDebut();
        Date dateFinDecompte = decompteInfo.getPeriode().getPeriodeFin();
        Date dateDebutEmployeur = null;

        if (employeur.getDateDebut() != null && !JadeStringUtil.isBlankOrZero(employeur.getDateDebut())) {
            dateDebutEmployeur = new Date(employeur.getDateDebut());
        }
        Date dateFinEmployeur = null;

        if (employeur.getDateFin() != null && !JadeStringUtil.isBlankOrZero(employeur.getDateFin())) {
            dateFinEmployeur = new Date(employeur.getDateFin());
        }

        if (dateDebutDecompte != null && !JadeStringUtil.isBlankOrZero(dateDebutDecompte.getSwissValue())
                && dateDebutDecompte.before(dateDebutEmployeur)) {
            dateDebutDecompte = dateDebutEmployeur;
        }
        if (dateFinEmployeur != null && !JadeStringUtil.isBlankOrZero(dateFinEmployeur.getSwissValue())
                && dateFinDecompte.after(dateFinEmployeur)) {
            dateFinDecompte = dateFinEmployeur;
        }
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle(dateDebutDecompte, dateFinDecompte);

        Date dateEtablissement = decompteInfo.getDateEtablissement();
        TypeDecompte typeComplementaire = TypeDecompte.COMPLEMENTAIRE;

        return genererDecompte(employeur, periodeMensuelle, dateEtablissement, typeComplementaire);
    }

    /**
     * Recherche si l'employeur dispose d'une période d'affiliation complète sur l'année 2015.
     * Si l'employeur commence avant ou après le début de l'année, on regarde si l'ensemble des particularités couvrent
     * cette période.
     *
     * Le code n'est pas très propre et peut aisément être refactoré car il dispose d'une bonne couverture de tests.
     *
     * @param employeur Employeur pour lequel déterminer si il dispose de particularité sur l'année complète.
     * @param annee Année de la couverture
     * @return true si couverture complète des particularités
     */
    boolean hasParticulariteSurAnneeComplete(Employeur employeur, Annee annee) {
        Date minDate = null;
        Date maxDate = null;
        Periode previousPeriode = null;
        List<Particularite> particularites = findParticularites(employeur, annee);
        Collections.sort(particularites);
        for (Particularite particularite : particularites) {
            Periode periodeParticularite = new Periode(particularite.getDateDebut(), particularite.getDateFin());
            if (previousPeriode != null && !previousPeriode.chevauche(periodeParticularite)
                    && !periodeParticularite.getDateDebut().equals(previousPeriode.getDateFin())) {
                return false;
            }
            if (minDate == null || particularite.getDateDebut().before(minDate)) {
                minDate = particularite.getDateDebut();
            }
            if (maxDate == null || particularite.getDateFin().after(maxDate)) {
                maxDate = particularite.getDateFin();
            }
            previousPeriode = periodeParticularite;
        }
        if (minDate == null) {
            return false;
        }

        Date dateDebutEmployeur = new Date(employeur.getDateDebut());
        Date dateFinEmployeur = null;
        if (!JadeNumericUtil.isEmptyOrZero(employeur.getDateFin())) {
            dateFinEmployeur = new Date(employeur.getDateFin());
        }

        Periode periodeParticularite = new Periode(minDate, maxDate);
        if (dateDebutEmployeur.after(annee.getFirstDayOfYear())) {
            if (dateFinEmployeur != null && dateFinEmployeur.before(annee.getLastDayOfYear())) {
                return periodeParticularite.contains(new Periode(dateDebutEmployeur, dateFinEmployeur));
            } else {
                return periodeParticularite.contains(new Periode(dateDebutEmployeur, annee.getLastDayOfYear()));
            }
        } else {
            if (dateFinEmployeur != null && dateFinEmployeur.before(annee.getLastDayOfYear())) {
                return periodeParticularite.contains(new Periode(annee.getFirstDayOfYear(), dateFinEmployeur));
            } else {
                return periodeParticularite.contains(annee);
            }
        }
    }

    List<Particularite> findParticularites(Employeur employeur, Annee annee) {
        return VulpeculaServiceLocator.getEmployeurService().findParticularites(employeur.getId(),
                new Periode(annee.getFirstDayOfYear(), annee.getLastDayOfYear()));
    }

    protected List<Decompte> genererDecomptePeriodique(Employeur employeur, PTProcessDecompteInfo decompteInfo)
            throws JadePersistenceException {
        List<Decompte> decomptes = new ArrayList<Decompte>();

        Decompte decompte = decompteRepository.findDecompteIdentiqueEBusiness(employeur, decompteInfo.getPeriode(),
                TypeDecompte.PERIODIQUE);
        if (decompte != null) {
            return decomptes;
        }

        if (employeur.isMensuel()) {
            decomptes.addAll(genererDecomptePeriodiqueForEmployeurMensuelle(employeur, decompteInfo));
        } else if (employeur.isTrimestriel()) {
            decomptes.addAll(genererDecomptePeriodiqueForEmployeurTrimestrielle(employeur, decompteInfo));
        } else if (employeur.isAnnuel()) {
            decomptes.addAll(genererDecomptePeriodiqueForEmployeurAnnuelle(employeur, decompteInfo));
        }
        return decomptes;
    }

    private List<Decompte> genererDecomptePeriodiqueForEmployeurMensuelle(Employeur employeur,
            PTProcessDecompteInfo decompteInfo) throws JadePersistenceException {
        List<Decompte> decomptes = new ArrayList<Decompte>();
        PeriodeMensuelle periodeMensuelle = decompteInfo.getPeriode();

        Date debutAffiliation = new Date(employeur.getDateDebut());
        Date finAffiliation;

        /*
         * Si la date de fin d'adhésion est null, on prend la date de fin de trimestre
         */
        if (employeur.getDateFin().isEmpty()) {
            finAffiliation = periodeMensuelle.getPeriodeFin();
        } else {
            finAffiliation = new Date(employeur.getDateFin());
        }

        // créer que les décomptes nécessaire en fonction de la particularité "sans personnel"
        List<Particularite> particularites = VulpeculaServiceLocator.getEmployeurService().findParticularites(
                employeur.getId(), new Periode(periodeMensuelle.getPeriodeDebut(), periodeMensuelle.getPeriodeFin()));

        for (Date date : periodeMensuelle.getMois()) {
            if (new Integer(debutAffiliation.getAnneeMois()) <= new Integer(date.getAnneeMois())
                    && new Integer(finAffiliation.getAnneeMois()) >= new Integer(date.getAnneeMois())) {

                boolean creerDecompte = true;
                for (Particularite particularite : particularites) {
                    Periode periodeParticularite = new Periode(particularite.getDateDebut(),
                            particularite.getDateFin());
                    if (periodeParticularite.contains(date)) {
                        creerDecompte = false;
                    }
                }

                if (creerDecompte) {
                    PeriodeMensuelle periodeDecompte = new PeriodeMensuelle(date, date);
                    Date dateEtablissement = decompteInfo.getDateEtablissement();
                    TypeDecompte typeDecompte = TypeDecompte.PERIODIQUE;

                    Decompte decompte = genererDecompte(employeur, periodeDecompte, dateEtablissement, typeDecompte);
                    decomptes.add(decompte);
                }

            }
        }
        return decomptes;
    }

    private List<Decompte> genererDecomptePeriodiqueForEmployeurTrimestrielle(Employeur employeur,
            PTProcessDecompteInfo decompteInfo) throws JadePersistenceException {
        List<Decompte> decomptes = new ArrayList<Decompte>();
        PeriodeMensuelle periodeMensuelle = decompteInfo.getPeriode();

        // créer que les décomptes nécessaire en fonction de la particularité "sans personnel"
        List<Particularite> particularites = VulpeculaServiceLocator.getEmployeurService().findParticularites(
                employeur.getId(), new Periode(periodeMensuelle.getPeriodeDebut(), periodeMensuelle.getPeriodeFin()));

        if (periodeMensuelle.getPeriodeFin().isMoisTrimestriel()) {

            PeriodeMensuelle periodeDecompte = new PeriodeMensuelle(periodeMensuelle.getPeriodeDebut(),
                    periodeMensuelle.getPeriodeFin());
            Date dateEtablissement = decompteInfo.getDateEtablissement();
            TypeDecompte typeDecompte = TypeDecompte.PERIODIQUE;
            boolean creerDecompte = true;
            for (Particularite particularite : particularites) {
                Periode periodeParticularite = new Periode(particularite.getDateDebut(), particularite.getDateFin());
                if (periodeParticularite.contains(periodeMensuelle2Periode(periodeDecompte))) {
                    creerDecompte = false;
                }
            }
            if (creerDecompte) {
                Decompte decompte = genererDecompte(employeur, periodeDecompte, dateEtablissement, typeDecompte);
                decomptes.add(decompte);
            }
        }
        return decomptes;
    }

    /**
     * @param periodeDecompte
     * @return
     */
    private Periode periodeMensuelle2Periode(PeriodeMensuelle periodeDecompte) {
        return new Periode(periodeDecompte.getPeriodeDebutWithDay(), periodeDecompte.getPeriodeFinWithDay());
    }

    private List<Decompte> genererDecomptePeriodiqueForEmployeurAnnuelle(Employeur employeur,
            PTProcessDecompteInfo decompteInfo) throws JadePersistenceException {
        List<Decompte> decomptes = new ArrayList<Decompte>();
        PeriodeMensuelle periodeMensuelle = decompteInfo.getPeriode();
        if (periodeMensuelle.getPeriodeFin().isMoisAnnuel()) {

            PeriodeMensuelle periodeDecompte = new PeriodeMensuelle(periodeMensuelle.getPeriodeDebut(),
                    periodeMensuelle.getPeriodeFin());
            Date dateEtablissement = decompteInfo.getDateEtablissement();
            TypeDecompte typeDecompte = TypeDecompte.PERIODIQUE;

            Decompte decompte = genererDecompte(employeur, periodeDecompte, dateEtablissement, typeDecompte);
            decomptes.add(decompte);
        }
        return decomptes;
    }

    /**
     * Génération d'un décompte vide dans le cas du processus de génération.
     *
     * @param employeur Employeur sur lequel générer une prestation
     * @param periodeMensuelle Période mensuelle pour laquelle générer la prestation
     * @param dateEtablissement Date d'établissement de la prestation
     * @param type Type de la prestation
     * @return Nouveau décompte
     * @throws JadePersistenceException
     */
    private Decompte genererDecompte(final Employeur employeur, final PeriodeMensuelle periodeMensuelle,
            final Date dateEtablissement, final TypeDecompte type) throws JadePersistenceException {
        annuleAncienDecompte(employeur, periodeMensuelle, type);
        Decompte decompte = genererDecompteVide(employeur, periodeMensuelle, dateEtablissement, type);
        genererLignesDecompte(decompte);
        return decompte;
    }

    /**
     * Annulation d'un décompte existant à partir des informations concernant l'employeur, le type de décompte et la
     * période. Uniquement si le décompte est toujours à l'été généré.
     *
     * @param employeur
     * @param periodeMensuelle
     * @param dateEtablissement
     * @param type
     * @return
     */
    private void annuleAncienDecompte(final Employeur employeur, final PeriodeMensuelle periodeMensuelle,
            final TypeDecompte type) {
        Decompte decompte = decompteRepository.findDecompteIdentiqueGenere(employeur, periodeMensuelle, type);
        if (decompte != null) {
            decompte.setEtat(EtatDecompte.ANNULE);
            decompteRepository.update(decompte);
        }
    }

    private Decompte genererDecompteVide(final Employeur employeur, final PeriodeMensuelle periodeMensuelle,
            final Date dateEtablissement, final TypeDecompte type) throws JadePersistenceException {
        Decompte decompte = creationDecompte(employeur, periodeMensuelle, dateEtablissement, type);

        // On set l'historique à l'état initial
        HistoriqueDecompte historique = new HistoriqueDecompte();
        historique.setId(nextIdHistoriqueDecompte());
        historique.setDecompte(decompte);
        historique.setDate(new Date(JACalendar.today().toStrAMJ()));
        historique.setEtat(EtatDecompte.GENERE);

        historiqueDecompteRepository.create(historique);

        return decompte;
    }

    private Decompte creationDecompte(final Employeur employeur, final PeriodeMensuelle periodeMensuelle,
            final Date dateEtablissement, final TypeDecompte type) {
        // On ajoute le décomptes
        Decompte decompte = new Decompte();
        decompte.setDateEtablissement(dateEtablissement);
        decompte.setEmployeur(employeur);

        Date dateDebutAffiliation = new Date(employeur.getDateDebut());
        if (dateDebutAffiliation.after(periodeMensuelle.getPeriodeDebut())) {
            PeriodeMensuelle periodeMensuelle2 = new PeriodeMensuelle(dateDebutAffiliation,
                    periodeMensuelle.getPeriodeFin());
            decompte.setPeriode(periodeMensuelle2);
        } else {
            decompte.setPeriode(periodeMensuelle);
        }

        decompte.setType(type);
        decompte.setEtat(EtatDecompte.GENERE);
        decompte.setNumeroDecompte(decompteService.getNumeroDecompte(employeur, type,
                periodeMensuelle.getPeriodeDebut(), employeur.getPeriodicite()));
        decompte.setManuel(false);
        decompte.setTypeProvenance(TypeProvenance.WEBMETIER_GENERE);
        decompte.calculerAndSetRappel(propertiesService.getRappelNombreJours());
        decompte.calculerAndSetControleAC2();
        decompteRepository.create(decompte);
        return decompte;
    }

    private void genererLignesDecompte(Decompte decompte) throws JadePersistenceException {
        // On ajoute les lignes de décomptes vides
        List<PosteTravail> listePoste = posteTravailService.findPostesActifsByIdAffilie(decompte.getIdEmployeur(),
                decompte.getPeriodeDebut(), decompte.getPeriodeFin());
        orderByNomPrenom(listePoste);

        for (int i = 0; i < listePoste.size(); i++) {
            DecompteSalaire ligneDecompte = new DecompteSalaire();

            PosteTravail posteTravail = listePoste.get(i);

            ligneDecompte.setPosteTravail(posteTravail);
            ligneDecompte.setDecompte(decompte);
            ligneDecompte.setId(nextIdDecompteSalaire());
            ligneDecompte.setSequence(String.valueOf(i + 1));

            Date dateDebut = decompte.getPeriodeDebut();
            Date dateFin = decompte.getPeriodeFin();
            if (posteTravail.getDebutActivite() != null && posteTravail.getDebutActivite().after(dateDebut)) {
                dateDebut = posteTravail.getDebutActivite().getFirstDayOfMonth();
            }

            if (posteTravail.getFinActivite() != null && dateFin.after(posteTravail.getFinActivite())) {
                dateFin = posteTravail.getFinActivite().getLastDayOfMonth();
            }

            ligneDecompte.setPeriode(new Periode(dateDebut, dateFin));

            if (posteTravail.isMensuel() && TypeDecompte.PERIODIQUE.equals(decompte.getType())) {
                ligneDecompte.setHeures(posteTravailService.getNombreHeuresParMois(posteTravail.getId(),
                        decompte.getPeriodeDebut(), decompte.getPeriodeFin()));
            }

            if (decompte.getType().equals(TypeDecompte.COMPLEMENTAIRE)) {
                posteTravail.setAdhesionsCotisations(decompteService.findAdhesionsForDecompteSalaireCP(ligneDecompte));
            } else {
                posteTravail.setAdhesionsCotisations(decompteService.findAdhesionsForDecompteSalaire(ligneDecompte));
            }

            ligneDecompte.setCotisationsDecompte(genererTableCotisation(ligneDecompte));
            decompteSalaireRepository.create(ligneDecompte);
        }
    }

    private void orderByNomPrenom(List<PosteTravail> postes) {
        Collections.sort(postes, new Comparator<PosteTravail>() {
            @Override
            public int compare(PosteTravail o1, PosteTravail o2) {
                return o1.getNomPrenomTravailleur().compareTo(o2.getNomPrenomTravailleur());
            }
        });
    }

    private List<CotisationDecompte> genererTableCotisation(DecompteSalaire decompteSalaire)
            throws JadePersistenceException {
        List<CotisationDecompte> cotisations = decompteService.genererTableCotisation(decompteSalaire,
                tauxCotisationDecompteLoader);
        for (CotisationDecompte cotisationDecompte : cotisations) {
            cotisationDecompte.setId(nextIdCotisationDecompte());
        }
        return cotisations;
    }

    private String nextIdDecompte() throws JadePersistenceException {
        synchronized (lockDecompte) {
            return pkProviderDecompte.getNextPrimaryKey();
        }
    }

    private String nextIdDecompteSalaire() throws JadePersistenceException {
        synchronized (lockDecompteSalaire) {
            return pkProviderDecompteSalaire.getNextPrimaryKey();
        }
    }

    private String nextIdHistoriqueDecompte() throws JadePersistenceException {
        synchronized (lockHistoriqueDecompte) {
            return pkProviderHistoriqueDecompte.getNextPrimaryKey();
        }
    }

    private String nextIdCotisationDecompte() throws JadePersistenceException {
        synchronized (lockCotisation) {
            return pkProviderCotisation.getNextPrimaryKey();
        }
    }

    public final boolean isOptimized() {
        return optimized;
    }

    public final void setOptimized(boolean optimized) {
        this.optimized = optimized;
    }
}
