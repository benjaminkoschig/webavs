package ch.globaz.vulpecula.domain.specifications.is;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.is.TauxImposition;
import ch.globaz.vulpecula.domain.models.is.TauxImpositions;

public class TIPeriodesChevauchantes extends AbstractSpecification<TauxImposition> {
    private final TauxImpositions tauxImpositions;

    public TIPeriodesChevauchantes(TauxImpositions tauxImpositions) {
        this.tauxImpositions = tauxImpositions;
    }

    @Override
    public boolean isValid(TauxImposition tauxImposition) {
        if (tauxImpositions.chevauche(tauxImposition)) {
            addMessage(SpecificationMessage.TI_CHEVAUCHEMENT_PERIODES);
        }
        return true;
    }
}
