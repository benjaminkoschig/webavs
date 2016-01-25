package ch.globaz.vulpecula.domain.specifications.postetravail;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

/**
 * @author Arnaud Geiser (AGE) | Créé le 18 févr. 2014
 * 
 */
public class PosteTravailEmployeurRequisSpecification extends AbstractSpecification<PosteTravail> {

    @Override
    public boolean isValid(PosteTravail posteTravail) {
        if (posteTravail.getEmployeur() == null && posteTravail.getId() == null) {
            addMessage(SpecificationMessage.POSTE_TRAVAIL_EMPLOYEUR_REQUIS);
        }
        return true;
    }

}
