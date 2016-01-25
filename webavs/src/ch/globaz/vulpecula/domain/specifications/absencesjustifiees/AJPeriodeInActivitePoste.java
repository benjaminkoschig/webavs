package ch.globaz.vulpecula.domain.specifications.absencesjustifiees;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;

public class AJPeriodeInActivitePoste extends AbstractSpecification<AbsenceJustifiee> {

    @Override
    public boolean isValid(AbsenceJustifiee absenceJustifiee) {
        if (!absenceJustifiee.getPeriodeActivitePoste().contains(absenceJustifiee.getPeriode())) {
            addMessage(SpecificationMessage.PRESTATION_PERIODE_NON_CONTENUE_POSTE, absenceJustifiee.getPeriode()
                    .toString(), absenceJustifiee.getPeriodeActivitePoste().toString());
        }
        return true;
    }

}
