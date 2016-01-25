package ch.globaz.vulpecula.domain.repositories.association;

import java.util.List;
import ch.globaz.vulpecula.domain.models.association.CotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface CotisationAssociationProfessionnelleRepository extends
        Repository<CotisationAssociationProfessionnelle> {
    List<CotisationAssociationProfessionnelle> findAll();

    List<CotisationAssociationProfessionnelle> findByAssociationGenre(String idAssociation,
            GenreCotisationAssociationProfessionnelle genre);
}
