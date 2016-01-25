package ch.globaz.vulpecula.domain.specifications.is;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.is.TauxImposition;

public class TICantonRequis extends AbstractSpecification<TauxImposition> {

    @Override
    public boolean isValid(TauxImposition tauxImposition) {
        if (tauxImposition.getCanton() == null || tauxImposition.getCanton().isEmpty()) {
            addMessage(SpecificationMessage.TI_CANTON_REQUIS);
        }
        return true;
    }
}
