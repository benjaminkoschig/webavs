package ch.globaz.vulpecula.domain.specifications.postetravail;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

public class PosteTravailAdhesionAVSAvecFASpecification extends AbstractSpecification<PosteTravail> {
    @Override
    public boolean isValid(PosteTravail posteTravail) {
        if (posteTravail.hasCotisationAVS() && !posteTravail.hasCotisationFA()) {
            addMessage(SpecificationMessage.POSTE_TRAVAIL_COTISATION_FA_REQUISE);
        }
        if (!posteTravail.hasCotisationAVS() && posteTravail.hasCotisationFA()) {
            addMessage(SpecificationMessage.POSTE_TRAVAIL_COTISATION_AVS_REQUISE);
        }
        return true;
    }

}
