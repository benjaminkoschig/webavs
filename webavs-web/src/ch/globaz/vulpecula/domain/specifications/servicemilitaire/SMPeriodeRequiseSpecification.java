package ch.globaz.vulpecula.domain.specifications.servicemilitaire;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.servicemilitaire.ServiceMilitaire;

public class SMPeriodeRequiseSpecification extends AbstractSpecification<ServiceMilitaire> {

    @Override
    public boolean isValid(ServiceMilitaire serviceMilitaire) {
        if (serviceMilitaire.getPeriode() == null) {
            addMessage(SpecificationMessage.SM_PERIODE_NON_VIDE);
        } else if (serviceMilitaire.getPeriode().getDateFin() == null) {
            addMessage(SpecificationMessage.SM_PERIODE_FIN_NON_SAISIE);
        }

        if (!isDateDebutIsNull(serviceMilitaire) && !isDateFinIsNull(serviceMilitaire)
                && !isDateFinAfterDateDebut(serviceMilitaire)) {
            addMessage(SpecificationMessage.SM_PERIODE_DEBUT_PLUS_GRANDE_PERIODE_FIN);
        }
        return true;
    }

    private boolean isDateDebutIsNull(ServiceMilitaire serviceMilitaire) {
        return serviceMilitaire.getPeriode().getDateDebut() == null;
    }

    private boolean isDateFinIsNull(ServiceMilitaire serviceMilitaire) {
        return serviceMilitaire.getPeriode().getDateFin() == null;
    }

    private boolean isDateFinAfterDateDebut(ServiceMilitaire serviceMilitaire) {
        return serviceMilitaire.getPeriode().getDateFin().afterOrEquals(serviceMilitaire.getPeriode().getDateDebut());
    }

}
