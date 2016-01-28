package ch.globaz.vulpecula.domain.specifications.postetravail;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

public class PosteTravailQualificationRequisSpecification extends AbstractSpecification<PosteTravail> {

    @Override
    public boolean isValid(PosteTravail posteTravail) {
        if (posteTravail.getQualification() == null) {
            addMessage(SpecificationMessage.POSTE_TRAVAIL_QUALIFICATION_REQUISE);
        }
        return true;
    }

}
