package ch.globaz.vulpecula.businessimpl.services.decompte;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.joda.time.DateTime;
import org.slf4j.LoggerFactory;
import com.google.common.base.Preconditions;
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
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.decompte.CodeErreur;
import ch.globaz.vulpecula.domain.models.decompte.CodeErreurDecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.CotisationDecompte;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Occupation;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.syndicat.ParametreSyndicat;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteRepository;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteSalaireRepository;
import ch.globaz.vulpecula.domain.repositories.postetravail.PosteTravailRepository;
import ch.globaz.vulpecula.external.models.affiliation.Adhesion;
import ch.globaz.vulpecula.external.models.affiliation.Assurance;
import ch.globaz.vulpecula.external.models.affiliation.Cotisation;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.web.views.decomptesalaire.DecompteSalaireDetailsAnnuelView;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;

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
    public DecompteSalaire createWithoutCotisations(final DecompteSalaire decompteSalaire)
            throws UnsatisfiedSpecificationException {
        return createWithoutCotisations(decompteSalaire, Montant.ZERO);
    }

    @Override
    public DecompteSalaire createWithoutPosteTravail(final DecompteSalaire decompteSalaire)
            throws UnsatisfiedSpecificationException {
        if (decompteSalaire.getDecompte().mustBeFetched()) {
            decompteSalaire.setDecompte(decompteRepository.findById(decompteSalaire.getIdDecompte()));
        }
        return decompteSalaireRepository.create(decompteSalaire);
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
    public DecompteSalaire createWithoutCotisations(final DecompteSalaire decompteSalaire, Montant montantAC2)
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

        return decompteSalaireRepository.createWithoutCotisations(decompteSalaire);
    }

    @Override
    public DecompteSalaire update(final DecompteSalaire decompteSalaire) throws UnsatisfiedSpecificationException,
            JadePersistenceException {
        return update(decompteSalaire, Montant.ZERO);
    }

    private boolean hasAbsences(DecompteSalaire decompteSalaire) {
        return !decompteSalaire.getAbsences().isEmpty();
    }

    private boolean isNouvelleLigne(DecompteSalaire decompte) {
        for (CodeErreurDecompteSalaire code : decompte.getListeCodeErreur()) {
            if (CodeErreur.NOUVELLE_LIGNE.equals(code.getCodeErreur())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public DecompteSalaire update(final DecompteSalaire decompteSalaire, Montant montantAC2)
            throws UnsatisfiedSpecificationException, JadePersistenceException {
        decompteSalaire.validate();
        if (isNouvelleLigne(decompteSalaire)
                || (hasAbsences(decompteSalaire) && decompteSalaire.getPosteTravail().isMensuel())) {
            decompteSalaire.setHeures(0.00);
            decompteSalaire.setSalaireHoraire(Montant.ZERO);
        } else {
            decompteSalaire.calculChampSalaire();
        }

        handleAC(decompteSalaire);
        plafonnementRentier(decompteSalaire, decompteSalaire.getMontantFranchise());

        if (!montantAC2.isZero()) {
            forceMasseAC2(decompteSalaire, montantAC2);
        }

        if (decompteSalaire.isMajFinPoste()) {
            PosteTravail poste = posteTravailRepository
                    .findByIdWithFullDependecies(decompteSalaire.getIdPosteTravail());
            Date dateDebutPeriode = poste.getPeriodeActivite().getDateDebut();
            poste.setPeriodeActivite(new Periode(dateDebutPeriode, decompteSalaire.getPeriodeFin()));
            cloturerCotisation(poste, decompteSalaire.getPeriodeFin());
            VulpeculaServiceLocator.getPosteTravailService().update(poste);
            decompteSalaire.getDecompte().getLignes().remove(decompteSalaire);
            decompteSalaireRepository.delete(decompteSalaire);
            // return null car la ligne de décompte à été supprimée
            return null;
        } else {
            return decompteSalaireRepository.update(decompteSalaire);
        }
    }

    private void cloturerCotisation(PosteTravail poste, Date date) {
        List<AdhesionCotisationPosteTravail> adhesions = poste.getAdhesionsCotisations();
        for (AdhesionCotisationPosteTravail adhesion : adhesions) {
            Periode periode = adhesion.getPeriode();
            if (periode.isActif()) {
                Periode newPeriode = new Periode(periode.getDateDebut(), date);
                adhesion.setPeriode(newPeriode);
            }
        }
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
        if (decompteSalaire.getPosteTravail().getTravailleur().getId() != null) {
            boolean isRentier = false;
            try {
                isRentier = VulpeculaServiceLocator.getTravailleurService().isRentier(
                        decompteSalaire.getIdTravailleur(), decompteSalaire.getPeriode().getDateFin());
            } catch (Exception e) {
                throw new GlobazTechnicalException(ExceptionMessage.ERREUR_INCONNUE, e);
            }
            if (!"12".equals(decompteSalaire.getDecompte().getPeriode().getMoisFin())
                    && !TypeDecompte.COMPLEMENTAIRE.equals(decompteSalaire.getDecompte().getType())) {

                Montant montantFranchise;
                if (masseFranchise != null && !masseFranchise.isZero()) {
                    montantFranchise = masseFranchise;
                } else {
                    montantFranchise = new Montant(VulpeculaServiceLocator.getPropertiesService().findProperties(
                            PropertiesService.MONTANT_MENSUEL_FRANCHISE_RENTIER));
                }

                if (decompteSalaire.isForcerFranchise0()) {
                    montantFranchise = Montant.ZERO;
                }

                List<CotisationDecompte> cotisationsDecompte = new ArrayList<CotisationDecompte>();
                for (CotisationDecompte cotisationDecompte : decompteSalaire.getCotisationsDecompte()) {
                    if (cotisationDecompte.isAssuranceWithReductionRentier() && isRentier) {
                        cotisationDecompte.setMasse(deductionFranchise(decompteSalaire.getSalaireTotal(),
                                montantFranchise));
                        cotisationDecompte.setMasseForcee(true);
                    }
                    cotisationsDecompte.add(cotisationDecompte);
                }
                decompteSalaire.setCotisationsDecompte(cotisationsDecompte);
            } else {
                // Trouver le montant de la franchise totale
                try {
                    Montant montantFranchise = new Montant(calculMontantFranchisePourNbMoisCPOr12(decompteSalaire));
                    Montant montantTotalAf = findMasseAFFor(decompteSalaire.getIdPosteTravail(),
                            decompteSalaire.getAnnee());
                    Montant montantTotalSalaire = findMasseSalaireFor(decompteSalaire.getIdPosteTravail(),
                            decompteSalaire.getAnnee());
                    Montant montantPlafonne = montantFranchise.substract(montantTotalSalaire.substract(montantTotalAf));
                    List<CotisationDecompte> cotisationsDecompte = new ArrayList<CotisationDecompte>();
                    for (CotisationDecompte cotisationDecompte : decompteSalaire.getCotisationsDecompte()) {
                        if (cotisationDecompte.isAssuranceWithReductionRentier() && isRentier) {
                            if (masseFranchise != null && !masseFranchise.isZero()) {
                                montantPlafonne = masseFranchise;
                            }
                            if (decompteSalaire.isForcerFranchise0()) {
                                montantPlafonne = Montant.ZERO;
                            }
                            cotisationDecompte.setMasse(deductionFranchise(decompteSalaire.getSalaireTotal(),
                                    montantPlafonne));
                            cotisationDecompte.setMasseForcee(true);
                        }
                        cotisationsDecompte.add(cotisationDecompte);
                    }
                    decompteSalaire.setCotisationsDecompte(cotisationsDecompte);
                } catch (Exception e) {
                    LoggerFactory.getLogger(this.getClass()).error(e.getMessage());
                }
            }
        }
    }

    private BigDecimal calculMontantFranchisePourNbMoisCPOr12(DecompteSalaire decompteSalaire) throws Exception {
        Montant franchiseMensuelle = new Montant(VulpeculaServiceLocator.getPropertiesService().findProperties(
                PropertiesService.MONTANT_MENSUEL_FRANCHISE_RENTIER));
        DateTime dateDebut = determineDateDebutForCPOr12(decompteSalaire);
        DateTime dateFin = determineDateFinForCPOr12(decompteSalaire);
        int nbMois = dateFin.getMonthOfYear() - (dateDebut.getMonthOfYear() - 1);
        if (nbMois < 0) {
            throw new Exception("Impossible d'avoir une différence négative");
        }
        return franchiseMensuelle.getBigDecimalValue().multiply(new BigDecimal(nbMois));
    }

    public DateTime determineDateFinForCPOr12(DecompteSalaire decompteSalaire) throws Exception {
        SimpleDateFormat dateFormatterInput = new SimpleDateFormat("dd.MM.yyyy");
        return new DateTime(dateFormatterInput.parse(decompteSalaire.getPeriodeFinDecompte().toString())).monthOfYear()
                .withMaximumValue().dayOfMonth().withMaximumValue();

    }

    public DateTime determineDateDebutForCPOr12(DecompteSalaire decompteSalaire) throws Exception {
        SimpleDateFormat dateFormatterInput = new SimpleDateFormat("dd.MM.yyyy");

        DateTime dateDebutAnnee = new DateTime(dateFormatterInput.parse(decompteSalaire.getPeriodeFinAsSwissValue()))
                .monthOfYear().withMinimumValue().monthOfYear().withMinimumValue().dayOfMonth().withMinimumValue();
        DateTime dateDebutPoste = new DateTime(dateFormatterInput.parseObject(decompteSalaire.getPosteTravail()
                .getDebutActiviteAsSwissValue()));

        Date dateRetraiteDo = VulpeculaServiceLocator.getTravailleurService().giveDateRentier(
                decompteSalaire.getIdTravailleur());
        DateTime dateRetraite = new DateTime(dateFormatterInput.parse(dateRetraiteDo.toString()));
        DateTime dateToReturn;
        if (dateDebutPoste.isAfter(dateDebutAnnee)) {
            dateToReturn = dateDebutPoste;
        } else {
            dateToReturn = dateDebutAnnee;
        }
        if (dateRetraite.isAfter(dateToReturn) && (dateRetraite.getYear() == dateDebutAnnee.getYear())) {
            return dateRetraite;
        } else {
            return dateToReturn;
        }
    }

    private Montant calculMontantFranchise(DecompteSalaire decompteSalaire, Montant masseFranchise) throws Exception {
        SimpleDateFormat dateFormatterInput = new SimpleDateFormat("dd.MM.yyyy");
        // Si le décompte est avant 2016, on ne prend que le contenu de la case
        if (decompteSalaire.getAnnee().isBefore(propertiesService.getAnneeProduction())) {
            return masseFranchise;
        } else if (masseFranchise != null && !masseFranchise.isZero()) {
            return masseFranchise;
        } else {
            // On retrouve le nombre de mois concernés par le décompte et on fait attention aux rentier en cours
            // d'année.
            DateTime dateDebutDecompte = new DateTime(dateFormatterInput.parse(decompteSalaire
                    .getPeriodeDebutAsSwissValue()));
            DateTime dateFinDecompte = new DateTime(dateFormatterInput.parse(decompteSalaire
                    .getPeriodeDebutAsSwissValue()));
            DateTime dateRetraite = new DateTime(dateFormatterInput.parse(VulpeculaServiceLocator
                    .getTravailleurService().giveDateRentier(decompteSalaire.getIdTravailleur()).toString()));
            if (dateRetraite.isAfter(dateDebutDecompte) && dateRetraite.isAfter(dateFinDecompte)) {
                dateDebutDecompte = dateRetraite;
            }
            int nbMois = dateFinDecompte.getMonthOfYear() - dateDebutDecompte.getMonthOfYear();
            BigDecimal franchiseMensuelle = new Montant(VulpeculaServiceLocator.getPropertiesService().findProperties(
                    PropertiesService.MONTANT_MENSUEL_FRANCHISE_RENTIER)).getBigDecimalValue();
            return new Montant(franchiseMensuelle.multiply(new BigDecimal(nbMois)));
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

    /**
     * Méthode qui définit si on doit calculer le chômage en prenant compte des inscriptions de l'année :
     * 
     * @param decompte
     * @return true si on doit caclculer sur l'année
     * @throws Exception
     */
    private boolean controleAC2SurLannee(Decompte decompte) {
        if (!TypeDecompte.PERIODIQUE.equals(decompte.getType())) {
            return true;
        }
        if ("12".equals(decompte.getPeriodeFin().getMois())) {
            return true;
        }
        if (JadeStringUtil.isBlankOrZero(decompte.getEmployeur().getDateFin())) {
            return false;
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            java.util.Date dateFinAffiliation = dateFormat.parse(decompte.getEmployeur().getDateFin());

            if (new Integer(decompte.getPeriodeFin().getMois()) == dateFinAffiliation.getMonth() + 1) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
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

        // SI pas PERIODIQUE
        if (decompteSalaire.getDecompte() != null && controleAC2SurLannee(decompteSalaire.getDecompte())) {
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
                    // Si le montantAC comptabilisé est plus grand que le montant maximum. On soustrait la différence et
                    // on
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
        } else {
            // Pour les PERIODIQUE
            int nbMonth = (Integer.valueOf(decompteSalaire.getPeriode().getDateFin().getMois()) - Integer
                    .valueOf(decompteSalaire.getPeriode().getDateDebut().getMois())) + 1;
            Montant plafondACForPeriode = getPlafondParMois(decompteSalaire.getCotisationAC().getAssurance(),
                    decompteSalaire.getPeriodeFinDecompte());
            plafondACForPeriode = plafondACForPeriode.multiply(nbMonth);

            Montant masseDecompteSalaire = decompteSalaire.getSalaireTotal();
            CotisationDecompte cotisationAC2 = findOrCreateCotisationAC2(decompteSalaire, masseDecompteSalaire);

            cotisationAC.setMasseForcee(true);

            Montant montantAC2 = masseDecompteSalaire.substract(plafondACForPeriode);
            if (montantAC2.isPositive()) {
                cotisationAC2.setMasse(montantAC2);
                cotisationAC.setMasse(plafondACForPeriode);
            } else {
                cotisationAC.setMasse(masseDecompteSalaire);
                cotisationAC2.setMasse(new Montant("0.00"));
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
        List<DecompteSalaire> decomptesSalaires = decompteSalaireRepository.findForYearComptaOuValide(idPosteTravail,
                anneeDecompte);
        Montant masseAC = Montant.ZERO;
        for (DecompteSalaire decompteSalaire : decomptesSalaires) {
            CotisationDecompte cotisationAC = decompteSalaire.getCotisationAC();
            if (cotisationAC != null && decompteSalaire.isValideOuComptabilise()) {
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

    /**
     * Retourne la masse AC2 pour l'année dont une ligne de décompte est passée en paramètre.
     * Seules les décomptes salaires comptabilisées sont prises en compte.
     * 
     * @param decompteSalaire Décompte salaire
     * @return Montant Franchise Totale
     */
    protected Montant findMasseAFFor(String idPosteTravail, Annee anneeDecompte) {
        List<DecompteSalaire> decomptesSalaires = decompteSalaireRepository.findForYearComptaOuValide(idPosteTravail,
                anneeDecompte);
        Montant masseAF = Montant.ZERO;
        for (DecompteSalaire decompteSalaire : decomptesSalaires) {
            CotisationDecompte cotisationAF = decompteSalaire.getCotisationAF();
            if (cotisationAF != null && decompteSalaire.isValideOuComptabilise()) {
                if (cotisationAF.getMasseForcee()) {
                    masseAF = masseAF.add(cotisationAF.getMasse());
                } else {
                    masseAF = masseAF.add(decompteSalaire.getSalaireTotal());
                }
            }
        }
        return masseAF;
    }

    /**
     * Retourne la masse AC2 pour l'année dont une ligne de décompte est passée en paramètre.
     * Seules les décomptes salaires comptabilisées sont prises en compte.
     * 
     * @param decompteSalaire Décompte salaire
     * @return Montant Franchise Totale
     */
    protected Montant findMasseSalaireFor(String idPosteTravail, Annee anneeDecompte) {
        List<DecompteSalaire> decomptesSalaires = decompteSalaireRepository.findForYearComptaOuValide(idPosteTravail,
                anneeDecompte);
        Montant masseSalaire = Montant.ZERO;
        for (DecompteSalaire decompteSalaire : decomptesSalaires) {
            Montant salaire = decompteSalaire.getSalaireTotal();
            if (salaire != null && decompteSalaire.isValideOuComptabilise()) {
                masseSalaire = masseSalaire.add(salaire);
            }
        }
        return masseSalaire;
    }

    protected Montant findMontantMaximumForAC(DecompteSalaire decompteSalaire, Annee annee) {
        return findMontantMaximumForAC(decompteSalaire, annee.getLastDayOfYear());
    }

    protected Montant findMontantMaximumForAC(DecompteSalaire decompteSalaire, Date date) {
        PosteTravail posteTravail = decompteSalaire.getPosteTravail();
        posteTravail.setOccupations(findOccupations(posteTravail));
        Assurance assuranceAC = decompteSalaire.getCotisationAC().getAssurance();

        int mois = date.getNumeroMois();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        java.util.Date dateDebut;
        int moisDebut = 1;
        try {
            dateDebut = format.parse(decompteSalaire.getEmployeur().getDateDebut());

            if (posteTravail.getPeriodeActivite().getDateDebut().getAnnee()
                    .equals(String.valueOf(1900 + dateDebut.getYear()))) {
                moisDebut = dateDebut.getMonth() + 1;
            }
        } catch (Exception e) {
            // ??
        }

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
    public DecompteSalaire findPrecedentValide(String idPosteTravail, Date dateRecherche) {
        return decompteSalaireRepository.findPrecedentValide(idPosteTravail, dateRecherche);
    }

    @Override
    public void majDateAnnonceSalaire(Deque<DecompteSalaire> listeSalaires) {
        String dateAnnonce = new BSpy(BSessionUtil.getSessionFromThreadContext()).getFullData();
        while (!listeSalaires.isEmpty()) {
            DecompteSalaire decompteSalaire = listeSalaires.removeFirst();

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
                    decompteSalaire.getPeriodeFin());
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

    @Override
    public Montant cumulSalaireSyndicatWithCotisationsCPR(String idTravailleur, Date dateDebut, Date dateFin) {
        // Récupérer tous les décomptes salaires du travailleur qui sont comptabilisés entre les périodes données
        List<DecompteSalaire> decomptesSalaires = decompteSalaireRepository.findByIdAndPeriode(idTravailleur,
                dateDebut, dateFin);

        // Récupération des décomptes avec cotisations
        List<DecompteSalaire> decomptesPourSalaireAnnuel = findDecomptesWithCotisationCPRInPeriode(decomptesSalaires,
                dateDebut, dateFin);

        return calculSomme(decomptesPourSalaireAnnuel);
    }

    /**
     * Retourne les détails salariales d'un travailleur pour une période données avec ses cotisations pour un syndicat
     * Seules les décomptes salaires comptabilisées sont prises en compte.
     * 
     * @param idTravailleur
     * @param idSyndicat
     * @param dateDebut Date de début de la période d'affiliation syndicat
     * @param dateFin Date de fin de la période d'affiliation syndicat (date défini ou date d'aujourd'hui)
     * @return List<DecompteSalaireDetailsAnnuelView> Liste contenant les détails salariales par année
     */
    @Override
    public List<DecompteSalaireDetailsAnnuelView> calculDetailsDecompteSalaire(String idTravailleur, String idSyndicat,
            Date dateDebut, Date dateFin) {
        // Récupérer tous les décomptes salaires du travailleur qui sont comptabilisés entre les périodes données
        List<DecompteSalaire> decomptesSalaires = decompteSalaireRepository.findByIdAndPeriodeWithCotisations(
                idTravailleur, dateDebut, dateFin);
        List<DecompteSalaireDetailsAnnuelView> detailsDecomptesSalairesParAnnee = new ArrayList<DecompteSalaireDetailsAnnuelView>();

        // On commence par l'année la plus récente, l'année en cours
        Date dateDebutEnCours = dateFin.getDateOfFirstDayOfYear();
        Date dateFinEnCours = dateFin.getDateOfLastDayOfYear();

        // Et on boucle jusqu'à l'année de la date début passé en paramètre, donc l'année la plus ancienne
        while (dateDebutEnCours.afterOrEquals(dateDebut)) {
            DecompteSalaireDetailsAnnuelView decSalaireDetAn = new DecompteSalaireDetailsAnnuelView();
            decSalaireDetAn.setTaux("0.00");
            decSalaireDetAn.setPourcentage("0.00");
            decSalaireDetAn.setFrais("0.00");

            // Récupération des décomptes avec cotisations
            List<DecompteSalaire> decomptesPourSalaireAnnuel = findDecomptesWithCotisationCPRInPeriode(
                    decomptesSalaires, dateDebutEnCours, dateFinEnCours);

            decSalaireDetAn.setSalaireAnnuel(calculSomme(decomptesPourSalaireAnnuel).toStringFormatTwoDecimales());

            if (!decomptesPourSalaireAnnuel.isEmpty()) {
                // Récupére le ou les ID de l'employeur et le décompte concerné
                // Afin de savoir quel décompte utilisé pour instancier le taux + le pourcentage et les frais du
                // syndicat
                Map<String, DecompteSalaire> idsEmployeurWithDecompteSalaire = getEmployeurAvecLeDecompteConcerne(decomptesPourSalaireAnnuel);

                // Décompte qui sera utilisé pour rechercher le taux d'assurance / le pourcentage et les frais du
                // syndicat
                DecompteSalaire decompteAUtilise = findDecompteAUtiliserPourTauxEtSyndicat(
                        idsEmployeurWithDecompteSalaire, dateFinEnCours);

                // Initiatlisation du taux / pourcentage / frais
                initTauxEtParametreSyndicat(decSalaireDetAn, decompteAUtilise, dateDebutEnCours, idSyndicat);
            }

            // Initialisation de l'année en cours, du salaire annuel et des cotisations brutes / nettes
            decSalaireDetAn = initDecompteSalaireDetailsAnnuelView(decSalaireDetAn, dateDebutEnCours);

            detailsDecomptesSalairesParAnnee.add(decSalaireDetAn);

            // Passage à l'année précédente
            dateDebutEnCours = dateDebutEnCours.addYear(-1);
            dateFinEnCours = dateFinEnCours.addYear(-1);
        }

        return detailsDecomptesSalairesParAnnee;
    }

    /**
     * Retourne les décomptes salaires qui possèdent des cotisations CPR basé sur la période des décomptes salaires
     * 
     * @param decomptes
     * @param dateDebut
     * @param dateFin
     * @return List<DecompteSalaire>
     */
    @Override
    public List<DecompteSalaire> findDecomptesInPeriodeWithCotisationCPR(List<DecompteSalaire> decomptesSalaire,
            Date dateDebut, Date dateFin) {
        List<DecompteSalaire> decomptesSalaireWithCotCPR = new ArrayList<DecompteSalaire>();
        for (DecompteSalaire decSalaire : decomptesSalaire) {
            if (isDecompteSalaireBetweenPeriode(decSalaire, dateDebut, dateFin) && hasCotisationsTypeCPR(decSalaire)) {
                decomptesSalaireWithCotCPR.add(decSalaire);
            }
        }
        return decomptesSalaireWithCotCPR;
    }

    /**
     * Retourne les décomptes salaires qui possèdent des cotisations CPR basé sur la période des cotisations
     * 
     * @param decomptes
     * @param dateDebut
     * @param dateFin
     * @return List<DecompteSalaire>
     */
    @Override
    public List<DecompteSalaire> findDecomptesWithCotisationCPRInPeriode(List<DecompteSalaire> decomptesSalaire,
            Date dateDebut, Date dateFin) {
        List<DecompteSalaire> decomptesSalaireWithCotCPR = new ArrayList<DecompteSalaire>();
        for (DecompteSalaire decSalaire : decomptesSalaire) {
            if (isDecompteSalaireBetweenPeriode(decSalaire, dateDebut, dateFin)
                    && hasCotisationsTypeCPRForAPeriode(decSalaire)) {
                decomptesSalaireWithCotCPR.add(decSalaire);
            }
        }
        return decomptesSalaireWithCotCPR;
    }

    /**
     * Set les adhésions cotisations au poste travail du décompte salaire. Test ensuite si une de ces adhésions
     * possèdent une cotisation de type CPR et que la période du décompte salaire se trouve dans la période de
     * l'adhésion
     * 
     * @param decSalaire
     * @return
     */
    private boolean hasCotisationsTypeCPRForAPeriode(DecompteSalaire decSalaire) {
        decSalaire.getPosteTravail().setAdhesionsCotisations(
                VulpeculaRepositoryLocator.getAdhesionCotisationPosteRepository().findByIdPosteTravail(
                        decSalaire.getPosteTravail().getId()));

        for (AdhesionCotisationPosteTravail adhesionCotisation : decSalaire.getPosteTravail().getAdhesionsCotisations()) {
            if (isDecSalaireInPeriodeCotisation(decSalaire, adhesionCotisation)) {
                for (CotisationDecompte coti : decSalaire.getCotisationsDecompte()) {
                    if (TypeAssurance.CPR_TRAVAILLEUR.equals(coti.getAssurance().getTypeAssurance())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Check si la période du décompte salaire se situe dans la période de l'adhésion cotisation
     * 
     * @param decSalaire
     * @param adhesionCotisation
     * @return vrai si la période du décompte se trouve dans celle de l'adhésion cotisation
     */
    private boolean isDecSalaireInPeriodeCotisation(DecompteSalaire decSalaire,
            AdhesionCotisationPosteTravail adhesionCotisation) {

        Boolean checkDateDebut = Integer.parseInt(decSalaire.getPeriode().getDateDebut().getAnneeMois()) >= Integer
                .parseInt(adhesionCotisation.getPeriode().getDateDebut().getAnneeMois());
        Boolean checkDateFin;
        if (adhesionCotisation.getPeriode().getDateFin() != null) {
            checkDateFin = Integer.parseInt(decSalaire.getPeriode().getDateFin().getAnneeMois()) <= Integer
                    .parseInt(adhesionCotisation.getPeriode().getDateFin().getAnneeMois());
        } else {
            checkDateFin = true;
        }
        return checkDateDebut && checkDateFin;
    }

    /**
     * Initialisation du taux de l'assurance de type CPR Travailleur
     * Initialisation du pourcentage et des frais des paramètres du syndicat
     * 
     * @param decSalaireDetAn
     * @param decompteAUtilise
     * @param dateDebutEnCours
     * @param idSyndicat
     */
    private void initTauxEtParametreSyndicat(DecompteSalaireDetailsAnnuelView decSalaireDetAn,
            DecompteSalaire decompteAUtilise, Date dateDebutEnCours, String idSyndicat) {
        if (JadeStringUtil.isBlankOrZero(decSalaireDetAn.getTaux())) {
            decSalaireDetAn.setTaux(getTauxAssuranceDeTypeCPRTravailleur(decompteAUtilise.getCotisationsDecompte(),
                    dateDebutEnCours));
        }
        if (JadeStringUtil.isBlankOrZero(decSalaireDetAn.getPourcentage())
                || JadeStringUtil.isBlankOrZero(decSalaireDetAn.getFrais())) {
            ParametreSyndicat parametreSyndicat = getParametreSyndicat(idSyndicat, decompteAUtilise.getEmployeur()
                    .getId());
            if (parametreSyndicat.getPourcentage() != null) {
                decSalaireDetAn.setPourcentage(parametreSyndicat.getPourcentage().getValue());
            }
            if (parametreSyndicat.getMontantParTravailleur() != null) {
                decSalaireDetAn.setFrais(parametreSyndicat.getMontantParTravailleur().getValueNormalisee());
            }
        }
    }

    /**
     * Retourne le décompte salaire qui sera utilisé pour rechercher le taux de l'assurance de type CPR Travailleur et
     * pour rechercher les paramètres d'un syndicat
     * 
     * @param idsEmployeurWithDecompteSalaire
     * @param date
     * @return DecompteSalaire Le décompte salaire qui sera utilisé pour retrouver le taux CPR et les paramètres du
     *         syndicat
     */
    private DecompteSalaire findDecompteAUtiliserPourTauxEtSyndicat(
            Map<String, DecompteSalaire> idsEmployeurWithDecompteSalaire, Date date) {
        DecompteSalaire decompteAUtilise = new DecompteSalaire();
        List<DecompteSalaire> decomptesSalaires = new ArrayList<DecompteSalaire>(
                idsEmployeurWithDecompteSalaire.values());
        // Si on a plus qu'un employeur
        if (idsEmployeurWithDecompteSalaire.size() > 1) {
            // On prend déjà le poste de travail qui est actif
            decompteAUtilise = findDecompteAvecPosteActif(decomptesSalaires, date);
            // Et si les postes sont tous actifs et aucun l'est, on prend le plus récent
            if (decompteAUtilise == null) {
                decompteAUtilise = findDecomptAvecPlusRecentPoste(decomptesSalaires);
            }
        } else if (idsEmployeurWithDecompteSalaire.size() == 1) {
            for (Entry<String, DecompteSalaire> entry : idsEmployeurWithDecompteSalaire.entrySet()) {
                decompteAUtilise = entry.getValue();
            }
        }
        return decompteAUtilise;
    }

    /**
     * Retourne le décompte salaire qui est actif parmis une liste de décomptes salaires
     * 
     * @param decomptesSalaire
     * @param date
     * @return DecompteSalaire Un décompte salaire s'il en existe un sinon null si 0 ou plusieurs
     */
    private DecompteSalaire findDecompteAvecPosteActif(List<DecompteSalaire> decomptesSalaire, Date date) {
        List<DecompteSalaire> decomptesAvecPosteTravailActifs = new ArrayList<DecompteSalaire>();

        // Récupération des décomptes avec postes actifs au dernier mois de l'année en cours
        for (DecompteSalaire decompteSalaire : decomptesSalaire) {
            if (decompteSalaire.getPosteTravail().isActif(date.getLastDayOfMonth())) {
                decomptesAvecPosteTravailActifs.add(decompteSalaire);
            }
        }

        // S'il y en a qu'un actif parmis tous, on le retourne
        if (decomptesAvecPosteTravailActifs.size() == 1) {
            return decomptesAvecPosteTravailActifs.get(0);
        } else {
            return null;
        }
    }

    /**
     * Retourne une map composé d'un ID unique d'employeur et le décompte salaire concerné
     * 
     * @param decomptesPourSalaireAnnuel Liste de décomptes salaires
     * @return Map<String, DecompteSalaire>
     */
    private Map<String, DecompteSalaire> getEmployeurAvecLeDecompteConcerne(
            List<DecompteSalaire> decomptesPourSalaireAnnuel) {
        Map<String, DecompteSalaire> idsEmployeurWithDecompteSalaire = new HashMap<String, DecompteSalaire>();
        for (DecompteSalaire decSalaire : decomptesPourSalaireAnnuel) {
            String idEmployeur = decSalaire.getEmployeur().getId();
            if (!idsEmployeurWithDecompteSalaire.containsKey(idEmployeur)) {
                idsEmployeurWithDecompteSalaire.put(idEmployeur, decSalaire);
            }
        }
        return idsEmployeurWithDecompteSalaire;
    }

    /**
     * Retourne le décompte salaire avec le plus récent poste de travail parmi une liste de décompte
     * 
     * @param decomptes
     * @return DecompteSalaire Le décompte salaire ayant le poste de travail avec la période d'activité la plus récente
     */
    public DecompteSalaire findDecomptAvecPlusRecentPoste(List<DecompteSalaire> decomptes) {
        Collections.sort(decomptes, new Comparator<DecompteSalaire>() {
            @Override
            public int compare(DecompteSalaire d1, DecompteSalaire d2) {
                return d2.getPosteTravail().getPeriodeActivite().compareTo(d1.getPosteTravail().getPeriodeActivite());
            }
        });
        if (!decomptes.isEmpty()) {
            return decomptes.get(0);
        }
        return null;
    }

    /**
     * Retourne vrai si le décompte salaire contient des cotisations de type CPR Travailleur
     * 
     * @param decSalaire
     * @return Boolean
     */
    private boolean hasCotisationsTypeCPR(DecompteSalaire decSalaire) {
        for (CotisationDecompte cotisation : decSalaire.getCotisationsDecompte()) {
            if (TypeAssurance.CPR_TRAVAILLEUR.equals(cotisation.getAssurance().getTypeAssurance())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retourne vrai si le décompte salaire se trouve entre deux date
     * 
     * @param decSalaire
     * @param dateDebutEnCours
     * @param dateFinEnCours
     * @return Boolean
     */
    private boolean isDecompteSalaireBetweenPeriode(DecompteSalaire decSalaire, Date dateDebutEnCours,
            Date dateFinEnCours) {
        if (dateFinEnCours == null) {
            dateFinEnCours = new Date();
        }
        return decSalaire.getPeriodeDebutDecompte().afterOrEquals(dateDebutEnCours)
                && decSalaire.getPeriodeFinDecompte().beforeOrEquals(dateFinEnCours);
    }

    /**
     * Calcul et rempli les attributs d'un objet DecompteSalaireDetailsAnnuelView (salaire annuel, cotisation
     * brute / nette)
     * 
     * @param decSalaireDetAn
     * @param dateDebutEnCours
     * @return DecompteSalaireDetailsAnnuelView
     */
    private DecompteSalaireDetailsAnnuelView initDecompteSalaireDetailsAnnuelView(
            DecompteSalaireDetailsAnnuelView decSalaireDetAn, Date dateDebutEnCours) {
        decSalaireDetAn.setAnnee(dateDebutEnCours.getAnnee());
        decSalaireDetAn.setCotisationBrute(Montant.valueOf(decSalaireDetAn.getSalaireAnnuel())
                .multiply(Montant.valueOf(decSalaireDetAn.getTaux())).divideBy100().toStringFormatTwoDecimales());
        decSalaireDetAn
                .setCotisationNette(new Montant(decSalaireDetAn.getCotisationBrute())
                        .multiply(Montant.valueOf(decSalaireDetAn.getPourcentage())).divideBy100()
                        .toStringFormatTwoDecimales());
        return decSalaireDetAn;
    }

    /**
     * Retourne les parametres du syndicat par rapport à la caisse métier de l'employeur
     * 
     * @param idSyndicat
     * @param idEmployeur
     * @return ParametreSyndicat
     */
    private ParametreSyndicat getParametreSyndicat(String idSyndicat, String idEmployeur) {
        Adhesion caisseMetier = VulpeculaRepositoryLocator.getAdhesionRepository().findCaisseMetier(idEmployeur);
        ParametreSyndicat parametreSyndicat = new ParametreSyndicat();
        List<ParametreSyndicat> param = VulpeculaRepositoryLocator.getParametreSyndicatRepository().findByIdSyndicat(
                idSyndicat, caisseMetier.getAdministrationPlanCaisse().getId());
        if (!param.isEmpty()) {
            parametreSyndicat = param.get(0);
        }
        return parametreSyndicat;
    }

    /**
     * Retourne le taux de l'assurance de type CPR Travailleur d'une cotisation pour une date donnée
     * 
     * @param cotisationsDecompte
     * @param date
     * @return
     */
    private String getTauxAssuranceDeTypeCPRTravailleur(List<CotisationDecompte> cotisationsDecompte, Date date) {
        String taux = "0";
        for (CotisationDecompte coti : cotisationsDecompte) {
            if (TypeAssurance.CPR_TRAVAILLEUR.equals(coti.getAssurance().getTypeAssurance())) {
                taux = VulpeculaServiceLocator.getCotisationService()
                        .findTauxForAssurance(coti.getAssurance().getId(), date).getValeurEmploye();
            }
        }
        return Montant.valueOf(taux).getValueNormalisee();
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
        // if (isListeTravailleurPaiementSyndicat) {
        decomptesSalaires = findDecomptesWithCotisationCPRInPeriode(decomptesSalaires, dateDebut, dateFin);
        // }
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
    public DifferenceCotisationSalaire checkCotisationDecompte(String idDecompteSalaire, Annee annee) {
        DecompteSalaire decompteSalaire = VulpeculaRepositoryLocator.getDecompteSalaireRepository().findById(
                idDecompteSalaire);
        // Si ce n'est pas la même année, on rechargera alors les cotisations
        if (!decompteSalaire.isSameAnneeCotisations(annee)) {
            return DifferenceCotisationSalaire.DIFFERENCE_ANNEE;
        }

        List<CotisationDecompte> cotisationsEffectiveDecompte = VulpeculaServiceLocator.getCotisationDecompteService()
                .getCotisationsDecompte(idDecompteSalaire);

        List<AdhesionCotisationPosteTravail> ahesionsPosteForDecompte = VulpeculaServiceLocator.getDecompteService()
                .findAdhesionsForDecompteSalaire(decompteSalaire);
        List<CotisationDecompte> cotisationsPossiblesDecompte = new ArrayList<CotisationDecompte>();
        for (AdhesionCotisationPosteTravail adhesion : ahesionsPosteForDecompte) {
            cotisationsPossiblesDecompte.add(new CotisationDecompte(adhesion));
        }

        if (listEquals(cotisationsEffectiveDecompte, cotisationsPossiblesDecompte)) {
            return DifferenceCotisationSalaire.NONE;
        } else {
            return DifferenceCotisationSalaire.DIFFERENCE_COTISATIONS;
        }
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
