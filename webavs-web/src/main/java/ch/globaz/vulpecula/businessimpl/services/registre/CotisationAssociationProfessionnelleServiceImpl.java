package ch.globaz.vulpecula.businessimpl.services.registre;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.registre.CotisationAssociationProfessionnelleService;
import ch.globaz.vulpecula.domain.models.association.AssociationGenre;
import ch.globaz.vulpecula.domain.models.association.CotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.registre.GenreCotisationAssociationProfessionnelle;
import ch.globaz.vulpecula.domain.models.registre.ParametreCotisationAssociation;
import ch.globaz.vulpecula.domain.repositories.association.CotisationAssociationProfessionnelleRepository;

public class CotisationAssociationProfessionnelleServiceImpl implements CotisationAssociationProfessionnelleService {
    private CotisationAssociationProfessionnelleRepository repository;

    public CotisationAssociationProfessionnelleServiceImpl(CotisationAssociationProfessionnelleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Map<AssociationGenre, Collection<CotisationAssociationProfessionnelle>> findAllCotisationsByAssociationGenre() {
        List<CotisationAssociationProfessionnelle> cotisations = repository.findAll();
        return CotisationAssociationProfessionnelle.groupByAssociationGenre(cotisations);
    }

    @Override
    public List<CotisationAssociationProfessionnelle> findCotisationsByAssociationGenre(String idAssociation,
            GenreCotisationAssociationProfessionnelle genre) {
        return repository.findByAssociationGenre(idAssociation, genre);
    }

    @Override
    public void validate(ParametreCotisationAssociation cotisationCM) throws UnsatisfiedSpecificationException {
        // On recherche toutes les cotisations identiques, avant de regarder la fourchette
        List<ParametreCotisationAssociation> cotisationList = VulpeculaRepositoryLocator
                .getParametreCotisationAssociationRepository().findCotisationsForFourchette(cotisationCM);
        cotisationCM.validate(cotisationList);
    }

}