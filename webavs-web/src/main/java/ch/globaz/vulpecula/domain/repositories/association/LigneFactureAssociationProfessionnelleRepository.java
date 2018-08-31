package ch.globaz.vulpecula.domain.repositories.association;

import java.util.List;
import ch.globaz.vulpecula.domain.models.association.LigneFactureAssociation;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface LigneFactureAssociationProfessionnelleRepository extends Repository<LigneFactureAssociation> {
    List<LigneFactureAssociation> findByIdEntete(String idEntete);

    List<LigneFactureAssociation> findByIdAssociationCotisation(String idAssociationCotisation);
}
