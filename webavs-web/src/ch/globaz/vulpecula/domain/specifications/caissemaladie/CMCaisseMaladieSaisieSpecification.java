package ch.globaz.vulpecula.domain.specifications.caissemaladie;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;

public class CMCaisseMaladieSaisieSpecification extends AbstractSpecification<AffiliationCaisseMaladie> {

    @Override
    public boolean isValid(AffiliationCaisseMaladie affiliationCaisseMaladie) {
        if (affiliationCaisseMaladie.getCaisseMaladie() == null
                || affiliationCaisseMaladie.getIdCaisseMaladie() == null
                || affiliationCaisseMaladie.getIdCaisseMaladie() == "") {
            addMessage(SpecificationMessage.CM_CAISSE_MALADIE_NON_SELECTIONNEE);
        }
        return true;
    }
}
