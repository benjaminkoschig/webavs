package ch.globaz.vulpecula.domain.specifications.postetravail;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

/**
 * @author Arnaud Geiser (AGE) | Créé le 18 févr. 2014
 * 
 */
public class PosteTravailTravailleurRequisSpecification extends AbstractSpecification<PosteTravail> {

    @Override
    public boolean isValid(PosteTravail posteTravail) {
        if (posteTravail.getTravailleur() == null || posteTravail.getTravailleur().getId() == null) {
            addMessage(SpecificationMessage.POSTE_TRAVAIL_TRAVAILLEUR_REQUIS);
        }
        return true;
    }

}
