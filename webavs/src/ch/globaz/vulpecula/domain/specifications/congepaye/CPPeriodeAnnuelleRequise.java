package ch.globaz.vulpecula.domain.specifications.congepaye;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;

public class CPPeriodeAnnuelleRequise extends AbstractSpecification<CongePaye> {

    @Override
    public boolean isValid(CongePaye congePaye) {
        if (isAnneeDebutIsNull(congePaye)) {
            addMessage(SpecificationMessage.CP_ANNEE_DEBUT_NON_VIDE);
        }
        if (isAnneeFinIsNull(congePaye)) {
            addMessage(SpecificationMessage.CP_ANNEE_FIN_NON_VIDE);
        }

        if (!isAnneeDebutIsNull(congePaye) && !isAnneeFinIsNull(congePaye)
                && congePaye.getAnneeDebut().isAfter(congePaye.getAnneeFin())) {
            addMessage(SpecificationMessage.CP_ANNEE_FIN_PLUS_GRAND_ANNEE_DEBUT);
        }
        return true;
    }

    private boolean isAnneeDebutIsNull(CongePaye congePaye) {
        return congePaye.getAnneeDebut() == null;
    }

    private boolean isAnneeFinIsNull(CongePaye congePaye) {
        return congePaye.getAnneeFin() == null;
    }

}
