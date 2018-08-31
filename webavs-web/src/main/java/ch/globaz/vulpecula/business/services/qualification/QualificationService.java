package ch.globaz.vulpecula.business.services.qualification;

import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.registre.ConventionQualification;

public interface QualificationService {
    public boolean isCCT(ConventionQualification qualification) throws UnsatisfiedSpecificationException;

    public boolean isTravailleurCCT(PosteTravail poste) throws UnsatisfiedSpecificationException;
}
