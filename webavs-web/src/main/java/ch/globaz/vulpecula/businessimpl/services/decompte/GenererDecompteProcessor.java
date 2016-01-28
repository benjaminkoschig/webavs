package ch.globaz.vulpecula.businessimpl.services.decompte;

import globaz.globall.util.JACalendar;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.JadePersistencePKProvider;
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
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.PeriodeMensuelle;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.CotisationDecompte;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.HistoriqueDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteRepository;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteSalaireRepository;
import ch.globaz.vulpecula.domain.repositories.decompte.HistoriqueDecompteRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.EmployeurRepository;
import ch.globaz.vulpecula.process.decompte.PTProcessDecompteInfo;

public class GenererDecompteProcessor {
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

    public GenererDecompteProcessor() {
        this(false);
    }

    public GenererDecompteProcessor(boolean optimized) {
        try {
            if (optimized) {
                pkProviderDecompte = JadePersistenceManager.getPrimaryKeyProvider(DecompteSimpleModel.class,
                        SMALL_RANGE);
                pkProviderHistoriqueDecompte = JadePersistenceManager.getPrimaryKeyProvider(
                        HistoriqueDecompteSimpleModel.class, SMALL_RANGE);
                pkProviderDecompteSalaire = JadePersistenceManager.getPrimaryKeyProvider(
                        LigneDecompteSimpleModel.class, MEDIUM_RANGE);
                pkProviderCotisation = JadePersistenceManager.getPrimaryKeyProvider(
                        CotisationDecompteSimpleModel.class, MEDIUM_RANGE);
            } else {
                pkProviderDecompte = JadePersistenceManager.getPrimaryKeyProvider(DecompteSimpleModel.class, ONE_RANGE);
                pkProviderHistoriqueDecompte = JadePersistenceManager.getPrimaryKeyProvider(
                        HistoriqueDecompteSimpleModel.class, ONE_RANGE);
                pkProviderDecompteSalaire = JadePersistenceManager.getPrimaryKeyProvider(
                        LigneDecompteSimpleModel.class, TINY_RANGE);
                pkProviderCotisation = JadePersistenceManager.getPrimaryKeyProvider(
                        CotisationDecompteSimpleModel.class, TINY_RANGE);
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

    public Decompte genererDecompteVideManuel(final Decompte decompte) throws UnsatisfiedSpecificationException,
            JadePersistenceException {
        // On vérifie les spécifications du décompte
        decompte.validate();
        decompte.setId(nextIdDecompte());
        decompte.setManuel(true);
        decompte.setEmployeur(employeurRepository.findByIdAffilie(decompte.getEmployeur().getId()));
        decompte.setEtat(EtatDecompte.OUVERT);
        // Si ce n'est pas un décompte de type contrôle employeur, on force alors la génération du numéro de décompte.
        if (!TypeDecompte.CONTROLE_EMPLOYEUR.equals(decompte.getType())) {
            decompte.setNumeroDecompte(decompteService.getNumeroDecompte(decompte.getEmployeur(), decompte.getType(),
                    decompte.getPeriodeDebut(), decompte.getPeriodiciteEmployeur()));
        }
        decompte.calculerAndSetRappel(VulpeculaServiceLocator.getPropertiesService().getRappelNombreJours());
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
        // decompteInfo

        if (TypeDecompte.COMPLEMENTAIRE.equals(decompteInfo.getType())) {
            decomptes.add(genererDecompteComplementaire(employeur, decompteInfo));
        } else if (TypeDecompte.PERIODIQUE.equals(decompteInfo.getType())) {
            decomptes.addAll(genererDecomptePeriodique(employeur, decompteInfo));
        }
        return decomptes;
    }

    private Decompte genererDecompteComplementaire(Employeur employeur, PTProcessDecompteInfo decompteInfo)
            throws JadePersistenceException {
        PeriodeMensuelle periodeMensuelle = decompteInfo.getPeriode();
        Date dateEtablissement = decompteInfo.getDateEtablissement();
        TypeDecompte typeComplementaire = TypeDecompte.COMPLEMENTAIRE;

        return genererDecompte(employeur, periodeMensuelle, dateEtablissement, typeComplementaire);
    }

    protected List<Decompte> genererDecomptePeriodique(Employeur employeur, PTProcessDecompteInfo decompteInfo)
            throws JadePersistenceException {
        List<Decompte> decomptes = new ArrayList<Decompte>();

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
        PeriodeMensuelle periode = decompteInfo.getPeriode();

        for (Date date : periode.getMois()) {

            PeriodeMensuelle periodeDecompte = new PeriodeMensuelle(date, date);
            Date dateEtablissement = decompteInfo.getDateEtablissement();
            TypeDecompte typeDecompte = TypeDecompte.PERIODIQUE;

            Decompte decompte = genererDecompte(employeur, periodeDecompte, dateEtablissement, typeDecompte);
            decomptes.add(decompte);
        }
        return decomptes;
    }

    private List<Decompte> genererDecomptePeriodiqueForEmployeurTrimestrielle(Employeur employeur,
            PTProcessDecompteInfo decompteInfo) throws JadePersistenceException {
        List<Decompte> decomptes = new ArrayList<Decompte>();
        PeriodeMensuelle periode = decompteInfo.getPeriode();

        if (periode.getPeriodeFin().isMoisTrimestriel()) {

            PeriodeMensuelle periodeDecompte = new PeriodeMensuelle(periode.getPeriodeDebut(), periode.getPeriodeFin());
            Date dateEtablissement = decompteInfo.getDateEtablissement();
            TypeDecompte typeDecompte = TypeDecompte.PERIODIQUE;

            Decompte decompte = genererDecompte(employeur, periodeDecompte, dateEtablissement, typeDecompte);
            decomptes.add(decompte);
        }
        return decomptes;
    }

    private List<Decompte> genererDecomptePeriodiqueForEmployeurAnnuelle(Employeur employeur,
            PTProcessDecompteInfo decompteInfo) throws JadePersistenceException {
        List<Decompte> decomptes = new ArrayList<Decompte>();
        PeriodeMensuelle periode = decompteInfo.getPeriode();
        if (periode.getPeriodeFin().isMoisAnnuel()) {

            PeriodeMensuelle periodeDecompte = new PeriodeMensuelle(periode.getPeriodeDebut(), periode.getPeriodeFin());
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
        decompte.setPeriode(periodeMensuelle);
        decompte.setType(type);
        decompte.setEtat(EtatDecompte.GENERE);
        decompte.setNumeroDecompte(decompteService.getNumeroDecompte(employeur, type,
                periodeMensuelle.getPeriodeDebut(), employeur.getPeriodicite()));
        decompte.setManuel(false);
        decompte.calculerAndSetRappel(propertiesService.getRappelNombreJours());
        decompteRepository.create(decompte);
        return decompte;
    }

    private void genererLignesDecompte(Decompte decompte) throws JadePersistenceException {
        // On ajoute les lignes de décomptes vides
        List<PosteTravail> listePoste = posteTravailService.findPostesActifsByIdAffilie(decompte.getIdEmployeur(),
                decompte.getPeriodeDebut(), decompte.getPeriodeFin());
        orderByNomPrenom(listePoste);

        for (int i = 0; i < listePoste.size(); i++) {
            Date debutActivite = decompte.getPeriodeDebut();
            Date finActivite = decompte.getPeriodeFin();

            PosteTravail posteTravail = listePoste.get(i);
            DecompteSalaire ligneDecompte = new DecompteSalaire();
            ligneDecompte.setId(nextIdDecompteSalaire());
            ligneDecompte.setPosteTravail(posteTravail);
            ligneDecompte.setDecompte(decompte);
            ligneDecompte.setSequence(String.valueOf(i + 1));
            if (decompte.getPeriodeDebut().before(posteTravail.getDebutActivite())) {
                debutActivite = posteTravail.getDebutActivite();
            }
            if (posteTravail.getFinActivite() != null && decompte.getPeriodeFin().after(posteTravail.getFinActivite())) {
                finActivite = posteTravail.getFinActivite();
            }

            ligneDecompte.setPeriode(new Periode(debutActivite, finActivite));

            if (posteTravail.isMensuel() && TypeDecompte.PERIODIQUE.equals(decompte.getType())) {
                ligneDecompte.setHeures(posteTravailService.getNombreHeuresParMois(posteTravail.getId(),
                        decompte.getPeriodeDebut(), decompte.getPeriodeFin()));
            }
            posteTravail.setAdhesionsCotisations(decompteService.findAdhesionsForDecompteSalaire(ligneDecompte));
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
        List<CotisationDecompte> listCotisations = new ArrayList<CotisationDecompte>();
        PosteTravail posteTravail = decompteSalaire.getPosteTravail();

        for (AdhesionCotisationPosteTravail adhesionCotisation : posteTravail.getAdhesionsCotisations()) {
            Taux tauxAssurance = tauxCotisationDecompteLoader.getOrLoadTauxAssurance(decompteSalaire,
                    adhesionCotisation);
            CotisationDecompte cotisationDecompte = decompteService.createCotisationDecompte(decompteSalaire,
                    adhesionCotisation, decompteSalaire.getPeriodeDebut(), tauxAssurance, nextIdCotisationDecompte());
            if (cotisationDecompte != null) {
                listCotisations.add(cotisationDecompte);
            }
        }
        return listCotisations;
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
