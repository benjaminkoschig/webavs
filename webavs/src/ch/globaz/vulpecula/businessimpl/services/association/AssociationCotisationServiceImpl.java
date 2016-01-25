package ch.globaz.vulpecula.businessimpl.services.association;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.association.AssociationCotisationService;
import ch.globaz.vulpecula.domain.models.association.AssociationCotisation;
import ch.globaz.vulpecula.domain.models.association.AssociationGenre;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.repositories.association.AssociationCotisationRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import com.google.common.base.Function;
import com.google.common.collect.Multimaps;

public class AssociationCotisationServiceImpl implements AssociationCotisationService {
    private AssociationCotisationRepository associationCotisationRepository;

    public AssociationCotisationServiceImpl(AssociationCotisationRepository associationCotisationRepository) {
        this.associationCotisationRepository = associationCotisationRepository;
    }

    @Override
    public Map<AssociationGenre, List<AssociationCotisation>> getCotisationByAssociation(String idEmployeur) {
        List<AssociationCotisation> cotisations = associationCotisationRepository.findByIdEmployeur(idEmployeur);
        return AssociationCotisation.groupByAssociationGenre(cotisations);
    }

    @Override
    public void create(String idEmployeur, List<AssociationCotisation> associationsCotisations)
            throws UnsatisfiedSpecificationException {
        List<AssociationCotisation> associationsCotisationsExistantes = associationCotisationRepository
                .findByIdEmployeur(idEmployeur);
        Map<String, Collection<AssociationCotisation>> cotisationsGroupById = groupByIdCotisation(associationsCotisations);
        Map<AssociationGenre, List<AssociationCotisation>> associationsGroupByAssociationGenre = AssociationCotisation
                .groupByAssociationGenre(associationsCotisations);

        UnsatisfiedSpecificationException exceptionChevauchement = checkChevauchementCotisations(cotisationsGroupById);
        UnsatisfiedSpecificationException exceptionMultiplesNM = checkMultipleCotisationsNM(associationsGroupByAssociationGenre);
        UnsatisfiedSpecificationException exceptionMinimumMasseSalariale = checkMinimumMasseSalariale(associationsGroupByAssociationGenre);

        UnsatisfiedSpecificationException merge = exceptionChevauchement.merge(exceptionMultiplesNM).merge(
                exceptionMinimumMasseSalariale);
        if (!merge.isEmpty()) {
            throw merge;
        }

        RepositoryJade.persistList(associationsCotisations, associationsCotisationsExistantes,
                associationCotisationRepository);
    }

    private Map<String, Collection<AssociationCotisation>> groupByIdCotisation(
            List<AssociationCotisation> associationsCotisations) {
        return Multimaps.index(associationsCotisations, new Function<AssociationCotisation, String>() {
            @Override
            public String apply(AssociationCotisation associationCotisation) {
                return associationCotisation.getIdCotisationAssociationProfessionnelle();
            }
        }).asMap();

    }

    private UnsatisfiedSpecificationException checkChevauchementCotisations(
            Map<String, Collection<AssociationCotisation>> cotisationsGroupById) {
        UnsatisfiedSpecificationException exception = new UnsatisfiedSpecificationException();

        for (Map.Entry<String, Collection<AssociationCotisation>> entry : cotisationsGroupById.entrySet()) {
            AssociationCotisation previous = null;
            AssociationCotisation current = null;
            List<AssociationCotisation> associationsCotisations = new ArrayList<AssociationCotisation>(entry.getValue());
            Collections.sort(associationsCotisations);
            for (AssociationCotisation associationCotisation : associationsCotisations) {
                if (current != null && previous == null) {
                    previous = current;
                }
                current = associationCotisation;

                if (previous != null) {
                    if (previous.getIdCotisationAssociationProfessionnelle().equals(
                            current.getIdCotisationAssociationProfessionnelle())
                            && (previous.sansPeriodeFin())
                            || previous.getPeriodeFin().afterOrEquals(current.getPeriodeDebut())) {
                        exception.addMessage(SpecificationMessage.ASSOCIATION_COTISATION_CHEVAUCHANTE, String
                                .valueOf(associationCotisation.getLibelleAssociationProfessionnelle()), current
                                .getPeriode().toString(), previous.getPeriode().toString());
                    }
                }
            }
        }
        return exception;
    }

    /**
     * Retourne une Exception lorsque deux cotisations non-membre sont actives en même temps.
     * 
     * @return
     */
    private UnsatisfiedSpecificationException checkMultipleCotisationsNM(
            Map<AssociationGenre, List<AssociationCotisation>> associationsGroupByAssociationGenre) {
        UnsatisfiedSpecificationException exception = new UnsatisfiedSpecificationException();
        for (Map.Entry<AssociationGenre, List<AssociationCotisation>> entry : associationsGroupByAssociationGenre
                .entrySet()) {
            // On ne traite que les cas non-membre
            if (!GenreCotisationAssociationProfessionnelle.MEMBRE.equals(entry.getKey().getGenre())) {
                AssociationCotisation previous = null;
                AssociationCotisation current = null;
                List<AssociationCotisation> associationsCotisations = new ArrayList<AssociationCotisation>(
                        entry.getValue());
                Collections.sort(associationsCotisations);
                for (AssociationCotisation associationCotisation : associationsCotisations) {
                    if (current != null && previous == null) {
                        previous = current;
                    }
                    current = associationCotisation;

                    if (previous != null) {
                        if (previous.getPeriodeFin() == null
                                || previous.getPeriodeFin().afterOrEquals(current.getPeriodeDebut())) {
                            exception.addMessage(SpecificationMessage.ASSOCIATION_PLUSIEURS_COTISATIONS_NM_ACTIVES,
                                    String.valueOf(associationCotisation.getLibelleAssociationProfessionnelle()),
                                    current.getPeriode().toString(), previous.getPeriode().toString());
                        }
                    }
                }
            }
        }
        return exception;
    }

    /**
     * Retourne une exception lorsque le total des cotisations par genre ne possèdent pas au minimum un taux de 100%
     * 
     */
    private UnsatisfiedSpecificationException checkMinimumMasseSalariale(
            Map<AssociationGenre, List<AssociationCotisation>> associationsGroupByAssociationGenre) {
        UnsatisfiedSpecificationException exception = new UnsatisfiedSpecificationException();
        for (Map.Entry<AssociationGenre, List<AssociationCotisation>> entry : associationsGroupByAssociationGenre
                .entrySet()) {
            // Dans le cas de non-taxé, on ne vérifie pas le taux
            if (GenreCotisationAssociationProfessionnelle.NON_TAXE.equals(entry.getKey().getGenre())) {
                continue;
            }

            List<AssociationCotisation> associationsCotisations = new ArrayList<AssociationCotisation>(entry.getValue());
            Collections.sort(associationsCotisations);

            // On vérifie que pour les années couvertes ont aient bien un minimum de masse salariale de la valeur
            // imposée
            Annee anneeDebut = associationsCotisations.get(0).getAnneeDebut();
            Annee anneeFin = chercherAnneeFin(associationsCotisations);
            Annee anneeCourante = anneeDebut;

            while (!anneeCourante.isAfter(anneeFin)) {
                Taux tauxCumule = Taux.ZERO();

                for (AssociationCotisation associationCotisation : associationsCotisations) {
                    if (associationCotisation.isActive(anneeCourante)) {
                        tauxCumule = tauxCumule.addTaux(associationCotisation.getMasseSalariale());
                    }
                }

                if (tauxCumule.lessThan(AssociationCotisation.SOMME_MASSE_SALARIALE_IMPOSEE)) {
                    exception.addMessage(SpecificationMessage.ASSOCIATION_SOMME_MINIMUM_MASSE_SALARIALE_NON_ATTEINTE,
                            String.valueOf(AssociationCotisation.SOMME_MASSE_SALARIALE_IMPOSEE), entry.getKey()
                                    .getLibelleAssociation(), getCodeLibelle(entry.getKey().getGenre().getValue()),
                            String.valueOf(anneeCourante.getValue()));
                }
                anneeCourante = anneeCourante.next();
            }

        }
        return exception;
    }

    /**
     * On recherche dans les cotisations une des cotisations qui possèdent une date de fin.
     * Dans le cas on ne retrouve aucune date de fin, on prend alors la date de début la plus grande.
     * 
     * @param associationsCotisations Liste de cotisations dont la date de fin doit être déterminé
     * @return Année de fin
     */
    private Annee chercherAnneeFin(List<AssociationCotisation> associationsCotisations) {
        Annee anneeFin = null;
        int i = associationsCotisations.size() - 1;
        while (i >= 0) {
            Annee annee = associationsCotisations.get(i).getAnneeFin();
            if (annee != null) {
                if (anneeFin == null) {
                    anneeFin = annee;
                } else if (annee.isAfter(anneeFin)) {
                    anneeFin = annee;
                }
            }
            i--;
        }

        if (anneeFin == null) {
            anneeFin = associationsCotisations.get(associationsCotisations.size() - 1).getAnneeDebut();
        }
        return anneeFin;
    }

    protected String getCodeLibelle(String valeur) {
        BSession session = BSessionUtil.getSessionFromThreadContext();
        return session.getCodeLibelle(valeur);
    }
}
