package ch.globaz.vulpecula.businessimpl.services.decompte;

import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.decompte.DecompteSalaireService;
import ch.globaz.vulpecula.business.services.properties.PropertiesService;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.decompte.CotisationDecompte;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Occupation;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteRepository;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteSalaireRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.PosteTravailRepository;
import ch.globaz.vulpecula.external.models.affiliation.Assurance;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import com.google.common.base.Preconditions;

public class DecompteSalaireServiceImpl implements DecompteSalaireService {
    private final DecompteSalaireRepository decompteSalaireRepository;
    private final DecompteRepository decompteRepository;
    private final PosteTravailRepository posteTravailRepository;
    private final PropertiesService propertiesService;

    public DecompteSalaireServiceImpl(final DecompteSalaireRepository decompteSalaireRepository,
            final DecompteRepository decompteRepository, final PosteTravailRepository posteTravailRepository,
            final PropertiesService propertiesService) {
        this.decompteSalaireRepository = decompteSalaireRepository;
        this.decompteRepository = decompteRepository;
        this.posteTravailRepository = posteTravailRepository;
        this.propertiesService = propertiesService;
    }

    @Override
    public DecompteSalaire create(final DecompteSalaire decompteSalaire) throws UnsatisfiedSpecificationException {
        return create(decompteSalaire, Montant.ZERO);
    }

    @Override
    public DecompteSalaire create(final DecompteSalaire decompteSalaire, Montant montantAC2)
            throws UnsatisfiedSpecificationException {
        if (decompteSalaire.getPosteTravail().mustBeFetched()) {
            decompteSalaire.setPosteTravail(posteTravailRepository.findById(decompteSalaire.getIdPosteTravail()));
        }
        if (decompteSalaire.getDecompte().mustBeFetched()) {
            decompteSalaire.setDecompte(decompteRepository.findById(decompteSalaire.getIdDecompte()));
        }
        decompteSalaire.validate();
        handleAC(decompteSalaire);
        plafonnementRentier(decompteSalaire, decompteSalaire.getMontantFranchise());

        if (montantAC2 != null) {
            forceMasseAC2(decompteSalaire, montantAC2);
        }

        return decompteSalaireRepository.create(decompteSalaire);
    }

    @Override
    public DecompteSalaire update(final DecompteSalaire decompteSalaire) throws UnsatisfiedSpecificationException {
        return update(decompteSalaire, Montant.ZERO);
    }

    @Override
    public DecompteSalaire update(final DecompteSalaire decompteSalaire, Montant montantAC2)
            throws UnsatisfiedSpecificationException {
        decompteSalaire.validate();
        decompteSalaire.calculSalaireTotalSiNecessaire();
        handleAC(decompteSalaire);
        plafonnementRentier(decompteSalaire, decompteSalaire.getMontantFranchise());

        if (!montantAC2.isZero()) {
            forceMasseAC2(decompteSalaire, montantAC2);
        }

        return decompteSalaireRepository.update(decompteSalaire);
    }

    @Override
    public DecompteSalaire findById(String idDecompteSalaire) {
        return decompteSalaireRepository.findById(idDecompteSalaire);
    }

    @Override
    public DecompteSalaire findByIdForSimulation(String idDecompteSalaire, boolean handleAC, Montant masseSalariale,
            Montant masseAC2, Montant masseFranchise) {
        DecompteSalaire decompteSalaire = decompteSalaireRepository.findById(idDecompteSalaire);
        decompteSalaire.setSalaireTotal(masseSalariale);

        if (handleAC) {
            handleAC(decompteSalaire);
        }

        if (!masseAC2.isZero()) {
            forceMasseAC2(decompteSalaire, masseAC2);
        }

        // if (!masseFranchise.isZero()) {
        plafonnementRentier(decompteSalaire, masseFranchise);
        // }

        return decompteSalaire;
    }

    private void forceMasseAC2(DecompteSalaire decompteSalaire, Montant masseAC2) {
        CotisationDecompte cotisationAC2 = decompteSalaire.getCotisationAC2();
        if (cotisationAC2 != null) {
            cotisationAC2.setMasse(masseAC2);
            cotisationAC2.setMasseForcee(true);
        }

        CotisationDecompte cotisationAC = decompteSalaire.getCotisationAC();
        if (cotisationAC != null) {
            cotisationAC.setMasse(decompteSalaire.getSalaireTotal().substract(masseAC2));
            cotisationAC.setMasseForcee(true);
        }
    }

    private void plafonnementRentier(DecompteSalaire decompteSalaire, Montant masseFranchise) {
        boolean isRentier = false;
        try {
            isRentier = VulpeculaServiceLocator.getTravailleurService().isRentier(decompteSalaire.getIdTravailleur(),
                    decompteSalaire.getPeriode().getDateFin());
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_INCONNUE, e);
        }
        Montant montantFranchise = calculMontantFranchise(decompteSalaire, masseFranchise);
        List<CotisationDecompte> cotisationsDecompte = new ArrayList<CotisationDecompte>();
        for (CotisationDecompte cotisationDecompte : decompteSalaire.getCotisationsDecompte()) {
            if (cotisationDecompte.isAssuranceWithReductionRentier() && isRentier) {
                cotisationDecompte.setMasse(deductionFranchise(decompteSalaire.getSalaireTotal(), montantFranchise));
                cotisationDecompte.setMasseForcee(true);
            }
            cotisationsDecompte.add(cotisationDecompte);
        }
        decompteSalaire.setCotisationsDecompte(cotisationsDecompte);
    }

    private Montant calculMontantFranchise(DecompteSalaire decompteSalaire, Montant masseFranchise) {
        // Si le décompte est avant 2016, on ne prend que le contenu de la case
        if (decompteSalaire.getAnnee().isBefore(propertiesService.getAnneeProduction())) {
            return masseFranchise;
        } else if (!masseFranchise.isZero()) {
            return masseFranchise;
        } else {
            // Sinon, on prend le contenu de la case si renseigné, sinon montant de la properties
            return new Montant(VulpeculaServiceLocator.getPropertiesService().findProperties(
                    PropertiesService.MONTANT_MENSUEL_FRANCHISE_RENTIER));
        }
    }

    Montant deductionFranchise(Montant salaireTotal, Montant montantFranchise) {
        if (salaireTotal.isNegative()) {
            return salaireTotal;
        }
        if (montantFranchise != null && !montantFranchise.isZero()) {
            salaireTotal = salaireTotal.substract(montantFranchise);
            if (salaireTotal.isNegative()) {
                return Montant.ZERO;
            }
        }
        return salaireTotal;
    }

    void handleAC(DecompteSalaire decompteSalaire) {
        CotisationDecompte cotisationAC = decompteSalaire.getCotisationAC();
        if (!isGererAC2(decompteSalaire, cotisationAC)) {
            decompteSalaire.resetCotisationAC2();
            // Si la cotisation AC est présente, on force alors son montant à 0.
            if (cotisationAC != null) {
                cotisationAC.setMasseForcee(false);
                cotisationAC.setMasse(Montant.ZERO);
            }
            return;
        }

        Decompte decompte = findDecompte(decompteSalaire);
        if (decompteSalaire.controleAC2() || !decompteSalaire.isMemeAnnee(decompte.getAnnee())) {
            // Montant AC total comptabilisé pour l'année en cours
            Montant montantACComptabiliseForCurrentYear = findMasseACFor(decompteSalaire.getIdPosteTravail(),
                    decompteSalaire.getAnneeDecompte());

            // On va rechercher les lignes de décompte (décompte salaire) pour le même poste de travail dans le même
            // décompte afin de calculer l'AC qui a déjà été calculé.
            findLignesMemeDecompte(decompteSalaire);

            Montant plafondACForPeriode = findPlafondAcForPeriode(decompteSalaire,
                    decompte.getMontantAC(decompteSalaire));

            Montant masseDecompteSalaire = decompteSalaire.getSalaireTotal();
            CotisationDecompte cotisationAC2 = findOrCreateCotisationAC2(decompteSalaire, masseDecompteSalaire);

            cotisationAC.setMasseForcee(true);
            cotisationAC.setMasse(Montant.ZERO);
            if (montantACComptabiliseForCurrentYear.greater(plafondACForPeriode)) {
                // Si le montantAC comptabilisé est plus grand que le montant maximum. On soustrait la différence et on
                // l'ajoute dans l'AC.
                Montant difference = montantACComptabiliseForCurrentYear.substract(plafondACForPeriode);
                cotisationAC.setMasse(difference.negate());
                cotisationAC2.setMasse(difference.add(masseDecompteSalaire));
            } else if (montantACComptabiliseForCurrentYear.less(plafondACForPeriode)) {
                Montant montantPossibleAC = plafondACForPeriode.substract(montantACComptabiliseForCurrentYear);
                if (montantPossibleAC.greater(masseDecompteSalaire)) {
                    Montant montantAC2DeduirePossible = montantPossibleAC.substract(masseDecompteSalaire);
                    // Rechercher les cotisations AC2 pour l'année
                    Montant montantAC2Precedent = findMasseAC2For(decompteSalaire);
                    if (montantAC2DeduirePossible.greater(montantAC2Precedent)) {
                        cotisationAC.setMasse(masseDecompteSalaire.add(montantAC2Precedent));
                        cotisationAC2.setMasse(montantAC2Precedent.negate());
                    } else {
                        cotisationAC.setMasse(masseDecompteSalaire.add(montantAC2DeduirePossible));
                        cotisationAC2.setMasse(montantAC2DeduirePossible.negate());
                    }
                } else {
                    cotisationAC.setMasse(montantPossibleAC);
                    // Montant AC2, compensation
                    Montant montantAC2 = masseDecompteSalaire.substract(montantPossibleAC);
                    cotisationAC2.setMasse(montantAC2);
                }
            }
        }
    }

    /**
     * Retourne si l'AC2 doit être gérer sur le décompte.
     */
    private boolean isGererAC2(DecompteSalaire decompteSalaire, CotisationDecompte cotisationAC) {
        // On ne gère que les decomptes salaires à partir de l'année saisie dans les properties.
        if (decompteSalaire.getAnnee().isBefore(propertiesService.getAnneeProduction())) {
            return false;
        }

        // Si le décompte salaire ne possède pas de cotisationAC, alors on ne gère pas l'AC2
        if (cotisationAC == null) {
            return false;
        }

        Decompte decompte = findDecompte(decompteSalaire);

        // Si pas de contrôle AC2 et que le décompte salaire est dans la même année que le décompte, on force le montant
        // de l'AC2 à 0.
        if (!decompteSalaire.controleAC2() && decompteSalaire.isMemeAnnee(decompte.getAnnee())) {
            return false;
        }
        return true;
    }

    /**
     * @param decompteSalaire
     * @param decompte
     * @return
     */
    private Montant findPlafondAcForPeriode(DecompteSalaire decompteSalaire, Montant ACDejaPresent) {
        Montant plafondACForPeriode = findMontantMaximumForAC(decompteSalaire);
        // On soustrait le montant des autres lignes de décomptes si plusieurs fois pour le même poste de travail.
        plafondACForPeriode = plafondACForPeriode.substract(ACDejaPresent);
        return plafondACForPeriode;
    }

    /**
     * Recherche du montant AC maximum
     * 
     * @param decompteSalaire
     * @return
     */
    private Montant findMontantMaximumForAC(DecompteSalaire decompteSalaire) {
        Montant plafondACForPeriode = null;
        if (decompteSalaire.isMemeAnneeDecompte()) {
            // Si dans la même année, on recherche le montant de l'AC comptabilisé jusqu'à la période de fin.
            plafondACForPeriode = findMontantMaximumForAC(decompteSalaire, decompteSalaire.getPeriodeFinDecompte());
        } else {
            // Si dans une autre année, on recherche le montant AC pour toute l'année.
            plafondACForPeriode = findMontantMaximumForAC(decompteSalaire, new Annee(decompteSalaire
                    .getPeriodeDebutDecompte().getAnnee()));
        }
        return plafondACForPeriode;
    }

    /**
     * @param decompteSalaire
     * @return
     */
    private Decompte findDecompte(DecompteSalaire decompteSalaire) {
        Decompte decompte = decompteSalaire.getDecompte();
        // Si le décompte n'est pas dans le décompte salaire, alors on recherche le décompte.
        if (decompte == null) {
            decompte = findDecompte(decompteSalaire.getIdDecompte());
            decompteSalaire.setDecompte(decompte);
        }
        return decompte;
    }

    /**
     * @param decompteSalaire
     * @return
     */
    private CotisationDecompte findOrCreateCotisationAC2(DecompteSalaire decompteSalaire, Montant masseDecompteSalaire) {
        CotisationDecompte cotisationAC2 = decompteSalaire.getCotisationAC2();

        // Si pas de cotisation AC2, on va alors la créer
        if (cotisationAC2 == null) {
            decompteSalaire.addCotisationDecompte(creerCotisationAC2(decompteSalaire.getIdEmployeur(),
                    decompteSalaire.getPeriodeDebutDecompte()));

            cotisationAC2 = decompteSalaire.getCotisationAC2();

            // Si pas de cotisation AC2 alors qu'on a tenté de la créer, on renvoie une exception.
            if (cotisationAC2 == null) {
                throw new GlobazTechnicalException(ExceptionMessage.DECOMPTE_ERREUR_PARAMETRAGE_AC2);
            }
        }
        cotisationAC2.setMasseForcee(true);
        cotisationAC2.setMasse(masseDecompteSalaire);

        return cotisationAC2;
    }

    /**
     * On va rechercher les lignes de décompte (décompte salaire) pour le même poste de travail dans le même
     * décompte afin de calculer l'AC qui a déjà été calculé.
     */
    protected void findLignesMemeDecompte(DecompteSalaire decompteSalaire) {
        Decompte decompte = decompteSalaire.getDecompte();
        decompte.setLignes(VulpeculaRepositoryLocator.getDecompteSalaireRepository().findByIdWithCotisations(
                decompte.getId(), decompteSalaire.getIdPosteTravail()));
    }

    protected Decompte findDecompte(String idDecompte) {
        return VulpeculaRepositoryLocator.getDecompteRepository().findByIdWithDependencies(idDecompte);
    }

    /**
     * Retourne la masse AC pour l'année et l'idPosteTravail passés en paramètre.
     * Seuls les décomptes salaires comptabilisés sont pris en compte.
     * 
     * @param idPosteTravail
     * @param anneeDecompte
     * @return Montant AC
     */
    protected Montant findMasseACFor(String idPosteTravail, Annee anneeDecompte) {
        List<DecompteSalaire> decomptesSalaires = decompteSalaireRepository.findForYear(idPosteTravail, anneeDecompte);
        Montant masseAC = Montant.ZERO;
        for (DecompteSalaire decompteSalaire : decomptesSalaires) {
            CotisationDecompte cotisationAC = decompteSalaire.getCotisationAC();
            if (cotisationAC != null && decompteSalaire.isComptabilise()) {
                if (cotisationAC.getMasse().isZero()) {
                    masseAC = masseAC.add(decompteSalaire.getSalaireTotal());
                } else {
                    masseAC = masseAC.add(cotisationAC.getMasse());
                }
            }
        }
        return masseAC;
    }

    /**
     * Retourne la masse AC2 pour l'année dont une ligne de décompte est passée en paramètre.
     * Seules les décomptes salaires comptabilisées sont prises en compte.
     * 
     * @param decompteSalaire Décompte salaire
     * @return Montant AC
     */
    protected Montant findMasseAC2For(DecompteSalaire referenceDecompteSalaire) {
        List<DecompteSalaire> decomptesSalaires = decompteSalaireRepository.findForYear(
                referenceDecompteSalaire.getIdPosteTravail(), referenceDecompteSalaire.getAnneeDecompte());
        Montant masseAC2 = Montant.ZERO;
        for (DecompteSalaire decompteSalaire : decomptesSalaires) {
            CotisationDecompte cotisationAC2 = decompteSalaire.getCotisationAC2();
            if (cotisationAC2 == null) {
                continue;
            }

            if (cotisationAC2.getMasse() != null && !cotisationAC2.getMasse().isZero()) {
                masseAC2 = masseAC2.add(cotisationAC2.getMasse());
            }
        }
        return masseAC2;
    }

    protected Montant findMontantMaximumForAC(DecompteSalaire decompteSalaire, Annee annee) {
        return findMontantMaximumForAC(decompteSalaire, annee.getLastDayOfYear());
    }

    protected Montant findMontantMaximumForAC(DecompteSalaire decompteSalaire, Date date) {
        PosteTravail posteTravail = decompteSalaire.getPosteTravail();
        posteTravail.setOccupations(findOccupations(posteTravail));
        Assurance assuranceAC = decompteSalaire.getCotisationAC().getAssurance();

        int mois = date.getNumeroMois();
        int moisDebut = 1;

        Montant plafondPourXMois = Montant.ZERO;
        for (int i = moisDebut; i <= mois; i++) {
            Date moisCourant = date.getMois(i);
            plafondPourXMois = plafondPourXMois.add(getPlafondParMois(assuranceAC, moisCourant));
        }
        return plafondPourXMois;
    }

    protected List<Occupation> findOccupations(PosteTravail posteTravail) {
        return VulpeculaRepositoryLocator.getOccupationRepository().findOccupationsByPosteTravail(posteTravail);
    }

    protected Montant getPlafondParMois(Assurance assurance, Date date) {
        return VulpeculaServiceLocator.getDecompteService().getPlafond(assurance, date);
    }

    /**
     * Retourne une cotisation AC2 à partir d'une cotisation de la cotisation de l'employeur.
     * 
     * @param decompteSalaire
     */
    protected CotisationDecompte creerCotisationAC2(String idEmployeur, Date periodeDebutDecompte) {
        List<Cotisation> cotisationsEmployeur = VulpeculaServiceLocator.getCotisationService().findByIdAffilieForDate(
                idEmployeur, periodeDebutDecompte);

        CotisationDecompte cotisationDecompte = null;
        for (Cotisation cotisation : cotisationsEmployeur) {
            if (TypeAssurance.COTISATION_AC2.equals(cotisation.getTypeAssurance())) {
                cotisationDecompte = new CotisationDecompte();
                cotisationDecompte.setCotisation(cotisation);
                cotisationDecompte.setTaux(VulpeculaServiceLocator.getCotisationService().findTaux(cotisation.getId(),
                        periodeDebutDecompte));
            }
        }

        return cotisationDecompte;
    }

    @Override
    public void delete(final DecompteSalaire decompteSalaire) {
        decompteSalaireRepository.delete(decompteSalaire);
    }

    @Override
    public DecompteSalaire findPrecedentComptabilise(String idPosteTravail, Date dateRecherche) {
        return decompteSalaireRepository.findPrecedentComptabilise(idPosteTravail, dateRecherche);
    }

    @Override
    public void majDateAnnonceSalaire(Deque<DecompteSalaire> listeDecompteSalaire) {
        String dateAnnonce = new BSpy(BSessionUtil.getSessionFromThreadContext()).getFullData();
        while (!listeDecompteSalaire.isEmpty()) {
            DecompteSalaire decompteSalaire = listeDecompteSalaire.removeFirst();

            DecompteSalaire decompteToUpd = VulpeculaRepositoryLocator.getDecompteSalaireRepository().findById(
                    decompteSalaire.getId());
            decompteToUpd.setDateAnnonce(dateAnnonce);
            VulpeculaRepositoryLocator.getDecompteSalaireRepository().update(decompteToUpd);
        }

    }

    @Override
    public boolean isPosteTravailRentier(String idDecompteLigne, Date date) throws Exception {
        DecompteSalaire decompteSalaire = VulpeculaRepositoryLocator.getDecompteSalaireRepository().findById(
                idDecompteLigne);
        return VulpeculaServiceLocator.getTravailleurService().isRentier(
                decompteSalaire.getPosteTravail().getTravailleur(), date);
    }

    @Override
    public boolean isPosteTravailRentier(DecompteSalaire decompteSalaire) {
        Preconditions.checkNotNull(decompteSalaire.getId());
        Preconditions.checkNotNull(decompteSalaire.getTravailleur());

        try {
            return VulpeculaServiceLocator.getTravailleurService().isRentier(decompteSalaire.getTravailleur(),
                    decompteSalaire.getPeriodeDebut());
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    }

    @Override
    public Montant cumulSalaire(String idTravailleur, String caisseMetier, Date dateDebut, Date dateFin) {
        Montant montant = Montant.ZERO;
        List<DecompteSalaire> deecomptesSalaires = decompteSalaireRepository.findByIdAndPeriode(idTravailleur,
                dateDebut, dateFin);
        for (DecompteSalaire decompteSalaire : deecomptesSalaires) {
            String idCaisseMetier = findCaisseMetierForDecompteSalaire(decompteSalaire);
            if (idCaisseMetier.equals(caisseMetier)) {
                montant = montant.add(decompteSalaire.getSalaireTotal());
            }
        }
        return montant;
    }

    @Override
    public Montant cumulSalaire(String idTravailleur, Date dateDebut, Date dateFin) {
        List<DecompteSalaire> decomptesSalaires = decompteSalaireRepository.findByIdAndPeriode(idTravailleur,
                dateDebut, dateFin);
        return calculSomme(decomptesSalaires);
    }

    protected String findCaisseMetierForDecompteSalaire(DecompteSalaire decompteSalaire) {
        return String.valueOf(VulpeculaServiceLocator.getPosteTravailService().getIdTiersCaissePrincipale(
                decompteSalaire.getIdPosteTravail()));
    }

    private Montant calculSomme(Collection<DecompteSalaire> decomptesSalaires) {
        Montant montant = Montant.ZERO;
        for (DecompteSalaire decompteSalaire : decomptesSalaires) {
            montant = montant.add(decompteSalaire.getSalaireTotal());
        }
        return montant;
    }

    @Override
    public Map<Administration, Montant> findMontantsByCaisseMetier(String idTravailleur, Date dateDebut, Date dateFin) {
        Map<String, Administration> mappingEmployeursCaisseMetier = new HashMap<String, Administration>();
        Map<Administration, Montant> montantsByCaisseMetier = new HashMap<Administration, Montant>();

        List<DecompteSalaire> decomptesSalaires = decompteSalaireRepository.findByIdAndPeriode(idTravailleur,
                dateDebut, dateFin);
        for (DecompteSalaire decompteSalaire : decomptesSalaires) {
            String idEmployeur = decompteSalaire.getIdEmployeur();
            if (!mappingEmployeursCaisseMetier.containsKey(idEmployeur)) {
                Administration caisseMetier = getCaisseMetier(idEmployeur);
                mappingEmployeursCaisseMetier.put(idEmployeur, caisseMetier);
            }
            Administration caisseMetier = mappingEmployeursCaisseMetier.get(idEmployeur);
            if (!montantsByCaisseMetier.containsKey(caisseMetier)) {
                montantsByCaisseMetier.put(caisseMetier, Montant.ZERO);
            }
            Montant montantActuel = montantsByCaisseMetier.get(caisseMetier);
            Montant nouveauMontant = montantActuel.add(decompteSalaire.getSalaireTotal());
            montantsByCaisseMetier.put(caisseMetier, nouveauMontant);
        }
        return montantsByCaisseMetier;
    }

    private Administration getCaisseMetier(String idAffilie) {
        return VulpeculaRepositoryLocator.getAdhesionRepository().findCaisseMetier(idAffilie)
                .getAdministrationPlanCaisse();
    }

    @Override
    public String findFirstRowOfLigneForDecompte(String idDecompte) {
        DecompteSalaire decompteSalaire = VulpeculaRepositoryLocator.getDecompteSalaireRepository()
                .findAndfetchFirstByIdDecompte(idDecompte);
        if (decompteSalaire != null) {
            return decompteSalaire.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean checkCotisationDecompte(String idDecompteSalaire) {
        DecompteSalaire decompteSalaire = VulpeculaRepositoryLocator.getDecompteSalaireRepository().findById(
                idDecompteSalaire);

        List<CotisationDecompte> cotisationsEffectiveDecompte = VulpeculaServiceLocator.getCotisationDecompteService()
                .getCotisationsDecompte(idDecompteSalaire);

        List<AdhesionCotisationPosteTravail> ahesionsPosteForDecompte = VulpeculaServiceLocator.getDecompteService()
                .findAdhesionsForDecompteSalaire(decompteSalaire);
        List<CotisationDecompte> cotisationsPossiblesDecompte = new ArrayList<CotisationDecompte>();
        for (AdhesionCotisationPosteTravail adhesion : ahesionsPosteForDecompte) {
            cotisationsPossiblesDecompte.add(new CotisationDecompte(adhesion));
        }

        return listEquals(cotisationsEffectiveDecompte, cotisationsPossiblesDecompte);
    }

    /**
     * @param listToCheck
     * @return
     */
    private boolean listEquals(List<CotisationDecompte> list1, List<CotisationDecompte> list2) {
        if (list1 == null || list2 == null) {
            return false;
        }

        boolean result = list1.size() == list2.size();
        for (CotisationDecompte cotisationDecompte : list1) {
            if (!contain(list2, cotisationDecompte)) {
                return false;
            }
        }

        return result;
    }

    private boolean contain(List<CotisationDecompte> list, CotisationDecompte cotisationDecompte) {
        if (cotisationDecompte == null) {
            return false;
        }
        if (list == null) {
            return false;
        }

        for (CotisationDecompte cotisationDecompteRef : list) {
            if (cotisationDecompteRef.getCotisation().getId().equals(cotisationDecompte.getCotisation().getId())) {
                return true;
            }
        }

        return false;
    }
}
