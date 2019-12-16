package ch.globaz.al.impotsource.utils;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.al.impotsource.domain.TauxImposition;
import ch.globaz.al.impotsource.domain.TauxImpositions;

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
