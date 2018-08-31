package ch.globaz.vulpecula.domain.repositories.association;

import java.util.List;
import ch.globaz.vulpecula.domain.models.association.AssociationEmployeur;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface AssociationEmployeurRepository extends Repository<AssociationEmployeur> {
    List<AssociationEmployeur> findByIdEmployeur(String idEmployeur);

    List<AssociationEmployeur> findByIdEmployeurAndIdAssociation(String idEmployeur, String idAssociation);
}
