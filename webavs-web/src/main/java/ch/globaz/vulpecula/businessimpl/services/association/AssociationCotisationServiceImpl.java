package ch.globaz.vulpecula.businessimpl.services.association;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.models.registres.ParametreCotisationAssociationSearchComplexModel;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.association.AssociationCotisationService;
import ch.globaz.vulpecula.domain.models.association.AssociationCotisation;
import ch.globaz.vulpecula.domain.models.association.AssociationEmployeur;
import ch.globaz.vulpecula.domain.models.association.AssociationGenre;
import ch.globaz.vulpecula.domain.models.association.EtatFactureAP;
import ch.globaz.vulpecula.domain.models.association.LigneFactureAssociation;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.registre.CategorieFactureAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.repositories.association.AssociationCotisationRepository;
import ch.globaz.vulpecula.domain.repositories.association.AssociationEmployeurRepository;
import ch.globaz.vulpecula.domain.repositories.association.LigneFactureAssociationProfessionnelleRepository;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;
import com.google.common.base.Function;
import com.google.common.collect.Multimaps;

public class AssociationCotisationServiceImpl implements AssociationCotisationService {
    private AssociationCotisationRepository associationCotisationRepository;
    private AssociationEmployeurRepository associationEmployeurRepository;
    private LigneFactureAssociationProfessionnelleRepository ligneFactureAssociationProfessionnelleRepository;

    public AssociationCotisationServiceImpl(AssociationCotisationRepository associationCotisationRepository,
            LigneFactureAssociationProfessionnelleRepository ligneFactureAssociationProfessionnelleRepository,
            AssociationEmployeurRepository associationEmployeurRepository) {
        this.associationCotisationRepository = associationCotisationRepository;
        this.associationEmployeurRepository = associationEmployeurRepository;
        this.ligneFactureAssociationProfessionnelleRepository = ligneFactureAssociationProfessionnelleRepository;
    }

    @Override
    public Map<AssociationGenre, List<AssociationCotisation>> getCotisationByAssociation(String idEmployeur) {
        List<AssociationCotisation> cotisations = associationCotisationRepository.findByIdEmployeur(idEmployeur);

        List<AssociationEmployeur> associationsEmployeur = VulpeculaRepositoryLocator
                .getAssociationEmployeurRepository().findByIdEmployeur(idEmployeur);
        Map<String, AssociationEmployeur> mapAssocEmpl = createMap(associationsEmployeur);

        List<AssociationCotisation> cotisationsARetourner = new ArrayList<AssociationCotisation>();
        // Parcours de la liste pour ne pas retourner les cotisations inactives si elles ne sont pas désirées et
        // pour setter la variable qui définit si la coti est utilisée dans une facture
        for (AssociationCotisation associationCotisation : cotisations) {
            List<LigneFactureAssociation> listAssociationCotisation = VulpeculaRepositoryLocator
                    .getLigneFactureRepository().findByIdAssociationCotisation(associationCotisation.getId());

            boolean utiliseDansFacture = false;
            for (LigneFactureAssociation ligneFactureAssociation : listAssociationCotisation) {
                if (!EtatFactureAP.COMPTABILISE.equals(ligneFactureAssociation.getEnteteFacture().getEtat())
                        && !EtatFactureAP.SUPPRIME.equals(ligneFactureAssociation.getEnteteFacture().getEtat())) {
                    utiliseDansFacture = true;
                    break;
                }
            }

            associationCotisation.setUtiliseDansFacture(utiliseDansFacture);

            cotisationsARetourner.add(associationCotisation);
        }

        Map<AssociationGenre, List<AssociationCotisation>> listeCotisationsGroupees = AssociationCotisation
                .groupByAssociationGenre(cotisationsARetourner, mapAssocEmpl);

        return listeCotisationsGroupees;
    }

    private Map<String, AssociationEmployeur> createMap(List<AssociationEmployeur> associationsEmployeur) {
        Map<String, AssociationEmployeur> mapAssoc = new HashMap<String, AssociationEmployeur>();
        for (AssociationEmployeur associationEmployeur : associationsEmployeur) {
            mapAssoc.put(associationEmployeur.getIdAssociation(), associationEmployeur);
        }
        return mapAssoc;
    }

    @Override
    public void create(String idEmployeur, List<AssociationCotisation> associationsCotisations)
            throws UnsatisfiedSpecificationException {
        List<AssociationCotisation> associationsCotisationsExistantes = associationCotisationRepository
                .findByIdEmployeur(idEmployeur);

        Map<String, AssociationEmployeur> map = new HashMap<String, AssociationEmployeur>();
        for (AssociationCotisation ac : associationsCotisations) {
            map.put(ac.getIdAssociationProfessionnelle(), ac.getAssociationEmployeur());
        }
        create(new ArrayList<AssociationEmployeur>(map.values()), idEmployeur);

        Map<String, Collection<AssociationCotisation>> cotisationsGroupById = groupByIdCotisation(associationsCotisations);
        Map<AssociationGenre, List<AssociationCotisation>> associationsGroupByAssociationGenre = AssociationCotisation
                .groupByAssociationGenre(associationsCotisations, new HashMap<String, AssociationEmployeur>());

        List<AssociationCotisation> liste = associationsCotisations;
        List<AssociationCotisation> listeASupprimer = new ArrayList<AssociationCotisation>(
                associationsCotisationsExistantes);
        List<AssociationCotisation> listeAAjouter = new ArrayList<AssociationCotisation>(associationsCotisations);
        List<AssociationCotisation> listeAModifier = new ArrayList<AssociationCotisation>(associationsCotisations);

        listeASupprimer.removeAll(liste);
        listeAAjouter.removeAll(associationsCotisationsExistantes);
        listeAModifier.removeAll(listeAAjouter);
        listeAModifier.removeAll(listeASupprimer);

        UnsatisfiedSpecificationException exceptionChevauchement = checkChevauchementCotisations(cotisationsGroupById);
        UnsatisfiedSpecificationException exceptionMultiplesNM = checkMultipleCotisationsNM(associationsGroupByAssociationGenre);
        // UnsatisfiedSpecificationException exceptionMinimumMasseSalariale =
        // checkMinimumMasseSalariale(associationsGroupByAssociationGenre);
        UnsatisfiedSpecificationException exceptionCotisationASupprimerUtilisee = checkIfCotisationToDeleteIsUsed(listeASupprimer);
        UnsatisfiedSpecificationException exceptionCotisationAModifierUtilisee = checkIfCotisationToUpdateIsUsed(
                listeAModifier, associationsCotisationsExistantes);

        UnsatisfiedSpecificationException merge = exceptionChevauchement.merge(exceptionMultiplesNM)
                .merge(exceptionCotisationASupprimerUtilisee).merge(exceptionCotisationAModifierUtilisee);

        if (!merge.isEmpty()) {
            throw merge;
        }

        for (AssociationCotisation adhesion : listeASupprimer) {
            associationCotisationRepository.delete(adhesion);
        }

        for (AssociationCotisation adhesion : listeAAjouter) {
            associationCotisationRepository.create(adhesion);
        }

        for (AssociationCotisation adhesion : listeAModifier) {
            associationCotisationRepository.update(adhesion);
        }
    }

    private void create(List<AssociationEmployeur> listeAssociationEmployeur, String idEmployeur) {
        List<AssociationEmployeur> listeExistant = associationEmployeurRepository.findByIdEmployeur(idEmployeur);
        RepositoryJade.persistList(listeAssociationEmployeur, listeExistant, associationEmployeurRepository);

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

    private boolean canUpdate(AssociationCotisation associationCotisationAModifier,
            List<AssociationCotisation> associationsCotisationsExistantes) {

        int index = associationsCotisationsExistantes.indexOf(associationCotisationAModifier);

        if (index > -1 && associationsCotisationsExistantes.contains(associationCotisationAModifier)) {
            AssociationCotisation associationOriginal = associationsCotisationsExistantes.get(index);

            String idCotisationOriginal = associationOriginal.getIdCotisationAssociationProfessionnelle();
            String periodeDebutOriginal = associationOriginal.getPeriodeDebutAsValue();
            CategorieFactureAssociationProfessionnelle categorieOriginal = associationOriginal.getFacturer();
            Montant montantOriginal = associationOriginal.getForfait();

            String idCotisationModif = associationCotisationAModifier.getIdCotisationAssociationProfessionnelle();
            String periodeDebutModif = associationCotisationAModifier.getPeriodeDebutAsValue();
            CategorieFactureAssociationProfessionnelle categorieModif = associationCotisationAModifier.getFacturer();
            Montant montantModif = associationCotisationAModifier.getForfait();

            boolean idCotiChanged = !idCotisationOriginal.equals(idCotisationModif);
            boolean periodeDebutChanged = !periodeDebutOriginal.equals(periodeDebutModif);
            boolean categorieChanged = !categorieOriginal.equals(categorieModif);
            // boolean montantChanged = !montantOriginal.equals(montantModif);

            // Seul la date de fin peut être modifiée
            if (idCotiChanged || periodeDebutChanged || categorieChanged) {
                return false;
            }

        }

        return true;

    }

    private UnsatisfiedSpecificationException checkIfCotisationToUpdateIsUsed(
            List<AssociationCotisation> associationsCotisationsAChecker,
            List<AssociationCotisation> associationsCotisationsExistantes) {
        UnsatisfiedSpecificationException exception = new UnsatisfiedSpecificationException();

        for (AssociationCotisation associationCotisation : associationsCotisationsAChecker) {
            List<LigneFactureAssociation> listAssociationCotisation = ligneFactureAssociationProfessionnelleRepository
                    .findByIdAssociationCotisation(associationCotisation.getId());

            String idEnteteFacture = "";
            for (LigneFactureAssociation ligneFactureAssociation : listAssociationCotisation) {
                if (idEnteteFacture.isEmpty()
                        || !idEnteteFacture.equals(ligneFactureAssociation.getEnteteFacture().getId())) {

                    boolean canUpdate = canUpdate(associationCotisation, associationsCotisationsExistantes);

                    if (!canUpdate) {
                        exception.addMessage(
                                SpecificationMessage.ASSOCIATION_COTISATION_A_MODIFIER_UTILISEE_DANS_FACTURE,
                                associationCotisation.getCotisationAssociationProfessionnelle().getLibelle(),
                                ligneFactureAssociation.getAnnee().toString());
                    }
                    idEnteteFacture = ligneFactureAssociation.getEnteteFacture().getId();
                }
            }
        }

        return exception;
    }

    private UnsatisfiedSpecificationException checkIfCotisationToDeleteIsUsed(
            List<AssociationCotisation> associationsCotisationsAChecker) {
        UnsatisfiedSpecificationException exception = new UnsatisfiedSpecificationException();

        for (AssociationCotisation associationCotisation : associationsCotisationsAChecker) {
            List<LigneFactureAssociation> listAssociationCotisation = ligneFactureAssociationProfessionnelleRepository
                    .findByIdAssociationCotisation(associationCotisation.getId());

            String idEnteteFacture = "";
            for (LigneFactureAssociation ligneFactureAssociation : listAssociationCotisation) {
                if (idEnteteFacture.isEmpty()
                        || !idEnteteFacture.equals(ligneFactureAssociation.getEnteteFacture().getId())) {
                    exception.addMessage(SpecificationMessage.ASSOCIATION_COTISATION_A_SUPPRIMER_UTILISEE_DANS_FACTURE,
                            associationCotisation.getCotisationAssociationProfessionnelle().getLibelle(),
                            ligneFactureAssociation.getAnnee().toString());
                    idEnteteFacture = ligneFactureAssociation.getEnteteFacture().getId();
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
                // POBMS-73
                // if (tauxCumule.lessThan(AssociationCotisation.SOMME_MASSE_SALARIALE_IMPOSEE)) {
                // exception.addMessage(SpecificationMessage.ASSOCIATION_SOMME_MINIMUM_MASSE_SALARIALE_NON_ATTEINTE,
                // String.valueOf(AssociationCotisation.SOMME_MASSE_SALARIALE_IMPOSEE), entry.getKey()
                // .getLibelleAssociation(), getCodeLibelle(entry.getKey().getGenre().getValue()),
                // String.valueOf(anneeCourante.getValue()));
                // }
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

    @Override
    public boolean isDansLaFourchette(String idCotisationAssociationProfessionnelle, Montant masseSalariale)
            throws JadePersistenceException {
        ParametreCotisationAssociationSearchComplexModel searchModel = new ParametreCotisationAssociationSearchComplexModel();
        searchModel.setForIdCotisationAssociationProfessionnelle(idCotisationAssociationProfessionnelle);
        searchModel.setForFourchetteInferieure(masseSalariale.getValue());
        searchModel.setForFourchetteSuperieure(masseSalariale.getValue());
        int nombres = JadePersistenceManager.count(searchModel);
        if (nombres > 0) {
            return true;
        } else {
            return false;
        }
    }
}
