package ch.globaz.vulpecula.business.services.is;

import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.is.TauxImposition;

public interface TauxImpositionService {
    TauxImposition create(TauxImposition tauxImposition) throws UnsatisfiedSpecificationException;

    TauxImposition update(TauxImposition tauxImposition) throws UnsatisfiedSpecificationException;
}
