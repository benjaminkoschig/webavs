package ch.globaz.vulpecula.domain.specifications.postetravail;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

/**
 * @author Arnaud Geiser (AGE) | Cr�� le 18 f�vr. 2014
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
