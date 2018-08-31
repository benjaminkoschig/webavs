package ch.globaz.vulpecula.businessimpl.services.qualification;

import java.util.List;
import org.apache.commons.lang.Validate;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.qualification.QualificationService;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.registre.ConventionQualification;
import ch.globaz.vulpecula.domain.models.registre.Personnel;

public class QualificationServiceImpl implements QualificationService {

    @Override
    public boolean isCCT(ConventionQualification qualification) throws UnsatisfiedSpecificationException {
        return qualification.getPersonnel().equals(Personnel.EXPLOITATION);
    }

    @Override
    public boolean isTravailleurCCT(PosteTravail poste) throws UnsatisfiedSpecificationException {
        Validate.notNull(poste);
        Validate.notNull(poste.getEmployeur());
        Validate.notNull(poste.getEmployeur().getConvention());
        List<ConventionQualification> listeQualif = VulpeculaRepositoryLocator.getConventionQualificationRepository()
                .findByIdConvention(poste.getEmployeur().getConvention().getId());
        for (ConventionQualification conventionQualification : listeQualif) {
            if (conventionQualification.getQualification().equals(poste.getQualification())) {
                return conventionQualification.getPersonnel().equals(Personnel.EXPLOITATION);
            }
        }
        return false;
    }

}
