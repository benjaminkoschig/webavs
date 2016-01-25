package ch.globaz.vulpecula.domain.specifications.absencesjustifiees;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;

/**
 * Specification obligeant l'absence justifiée de posséder une période valide (date de début et date de fin renseignée).
 * 
 */
public class AJPeriodeRequiseSpecification extends AbstractSpecification<AbsenceJustifiee> {

    @Override
    public boolean isValid(AbsenceJustifiee absenceJustifiee) {
        if (absenceJustifiee.getPeriode() == null) {
            addMessage(SpecificationMessage.AJ_PERIODE_NON_VIDE);
        } else if (absenceJustifiee.getPeriode().getDateFin() == null) {
            addMessage(SpecificationMessage.AJ_PERIODE_FIN_NON_SAISIE);
        }

        return true;
    }
}
