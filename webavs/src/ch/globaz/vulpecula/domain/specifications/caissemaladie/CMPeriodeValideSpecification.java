package ch.globaz.vulpecula.domain.specifications.caissemaladie;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;
import ch.globaz.vulpecula.domain.models.common.Periode;

public class CMPeriodeValideSpecification extends AbstractSpecification<AffiliationCaisseMaladie> {

    @Override
    public boolean isValid(AffiliationCaisseMaladie affiliationCaisseMaladie) {
        if (!Periode.isValid(affiliationCaisseMaladie.getMoisDebut(), affiliationCaisseMaladie.getMoisFin())) {
            addMessage(SpecificationMessage.CM_PERIODE_INVALIDE);
        }
        return true;
    }

}
