package ch.globaz.vulpecula.domain.repositories.is;

import ch.globaz.vulpecula.domain.models.is.TauxImposition;
import ch.globaz.vulpecula.domain.models.is.TauxImpositions;
import ch.globaz.vulpecula.domain.models.is.TypeImposition;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface TauxImpositionRepository extends Repository<TauxImposition> {
    TauxImpositions findAll();

    TauxImpositions findAll(TypeImposition typeImposition);
}
