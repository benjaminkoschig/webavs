package ch.globaz.vulpecula.domain.specifications.servicemilitaire;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.servicemilitaire.ServiceMilitaire;

public class SMPeriodeInActivitePoste extends AbstractSpecification<ServiceMilitaire> {

    @Override
    public boolean isValid(ServiceMilitaire serviceMiltiaire) {
        if (!serviceMiltiaire.getPeriodeActivitePoste().contains(serviceMiltiaire.getPeriode())) {
            addMessage(SpecificationMessage.PRESTATION_PERIODE_NON_CONTENUE_POSTE, serviceMiltiaire.getPeriode()
                    .toString(), serviceMiltiaire.getPeriodeActivitePoste().toString());
        }
        return true;
    }

}
