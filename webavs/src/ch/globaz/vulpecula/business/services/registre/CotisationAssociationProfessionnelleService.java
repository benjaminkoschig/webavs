package ch.globaz.vulpecula.business.services.registre;

import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.association.AssociationGenre;
import ch.globaz.vulpecula.domain.models.association.CotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.registre.ParametreCotisationAssociation;

public interface CotisationAssociationProfessionnelleService extends JadeApplicationService {
    Map<AssociationGenre, Collection<CotisationAssociationProfessionnelle>> findAllCotisationsByAssociationGenre();

    List<CotisationAssociationProfessionnelle> findCotisationsByAssociationGenre(String idAssociation,
            GenreCotisationAssociationProfessionnelle genre);

    void validate(ParametreCotisationAssociation cotisationCM) throws UnsatisfiedSpecificationException;

}
