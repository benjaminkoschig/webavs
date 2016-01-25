package ch.globaz.vulpecula.domain.repositories.association;

import java.util.List;
import ch.globaz.vulpecula.domain.models.association.AssociationCotisation;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface AssociationCotisationRepository extends Repository<AssociationCotisation> {
    List<AssociationCotisation> create(List<AssociationCotisation> associationsCotisations);

    List<AssociationCotisation> findByIdEmployeur(String idEmployeur);
}
