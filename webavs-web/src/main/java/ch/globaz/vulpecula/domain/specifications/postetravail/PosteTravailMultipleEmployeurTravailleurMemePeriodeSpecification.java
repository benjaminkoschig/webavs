package ch.globaz.vulpecula.domain.specifications.postetravail;

import java.util.List;
import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

/**
 * Vérifie que le poste actuel n'est pas présent dans la liste des postes de
 * travail passé dans le constructeur. Les conditions sont que le poste ne
 * dispose pas du même employeur, travailleur et période
 * 
 * @author Arnaud Geiser (AGE) | Créé le 19 févr. 2014
 * 
 */
public class PosteTravailMultipleEmployeurTravailleurMemePeriodeSpecification extends
        AbstractSpecification<PosteTravail> {

    private List<PosteTravail> postesActuels;

    /**
     * Création de l'objet avec les postes du travailleur ou employeur
     * 
     * @param postes
     *            Liste de postes de travail actuellement présent
     */
    public PosteTravailMultipleEmployeurTravailleurMemePeriodeSpecification(final List<PosteTravail> postes) {
        postesActuels = postes;
    }

    @Override
    public boolean isValid(final PosteTravail poste) {
        checkPosteHasEmployeurAndTravailleur(poste);

        for (PosteTravail posteActuel : postesActuels) {
            if (hasSameEmployeurTravailleurAndPeriode(posteActuel, poste)) {
                addMessage(SpecificationMessage.POSTE_TRAVAIL_TRAVAILLEUR_EMPLOYEUR_CHEVAUCHE);
            }
        }
        return true;
    }

    private void checkPosteHasEmployeurAndTravailleur(final PosteTravail poste) {
        if (poste.getEmployeur() == null || poste.getEmployeur().getId() == null) {
            addMessage(SpecificationMessage.POSTE_TRAVAIL_EMPLOYEUR_REQUIS);
        } else if (poste.getTravailleur() == null || poste.getTravailleur().getId() == null) {
            addMessage(SpecificationMessage.POSTE_TRAVAIL_TRAVAILLEUR_REQUIS);
        }
    }

    private boolean hasSameEmployeurTravailleurAndPeriode(final PosteTravail poste1, final PosteTravail poste2) {
        return hasSameEmployeurAndTravailleur(poste1, poste2) && hasPeriodeSeChevauchant(poste1, poste2);
    }

    private boolean hasSameEmployeurAndTravailleur(final PosteTravail poste1, final PosteTravail poste2) {
        return hasSameEmployeur(poste1, poste2) && hasSameTravailleur(poste1, poste2);
    }

    private boolean hasSameEmployeur(final PosteTravail poste1, final PosteTravail poste2) {
        return poste1.getEmployeur().getId().equals(poste2.getEmployeur().getId());
    }

    private boolean hasSameTravailleur(final PosteTravail poste1, final PosteTravail poste2) {
        return poste1.getTravailleur().getId().equals(poste2.getTravailleur().getId());
    }

    private boolean hasPeriodeSeChevauchant(final PosteTravail poste1, final PosteTravail poste2) {
        poste1.getPeriodeActivite().chevauche(poste2.getPeriodeActivite());
        return false;
    }
}
