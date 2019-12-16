package ch.globaz.al.business.services.impotsource;

import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.al.impotsource.domain.TauxImposition;

public interface TauxImpositionService {
    TauxImposition create(TauxImposition tauxImposition) throws UnsatisfiedSpecificationException;

    TauxImposition update(TauxImposition tauxImposition) throws UnsatisfiedSpecificationException;
}
