package ch.globaz.vulpecula.domain.repositories.registre;

import java.util.List;
import ch.globaz.vulpecula.domain.models.registre.ParametreCotisationAssociation;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface ParametreCotisationAssociationRepository extends Repository<ParametreCotisationAssociation> {
    List<ParametreCotisationAssociation> findAll();

    List<ParametreCotisationAssociation> findCotisationsForFourchette(ParametreCotisationAssociation cotisationCM);
}
