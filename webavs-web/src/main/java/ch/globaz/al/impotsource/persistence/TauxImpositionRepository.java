package ch.globaz.al.impotsource.persistence;

import ch.globaz.al.impotsource.domain.TauxImposition;
import ch.globaz.al.impotsource.domain.TauxImpositions;
import ch.globaz.al.impotsource.domain.TypeImposition;
import ch.globaz.common.domaine.repository.Repository;

public interface TauxImpositionRepository extends Repository<TauxImposition> {
    TauxImpositions findAll();

    TauxImpositions findAll(TypeImposition typeImposition);
}
