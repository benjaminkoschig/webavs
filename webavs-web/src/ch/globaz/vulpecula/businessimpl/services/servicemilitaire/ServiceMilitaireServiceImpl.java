package ch.globaz.vulpecula.businessimpl.services.servicemilitaire;

import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.postetravail.PosteTravailService;
import ch.globaz.vulpecula.business.services.servicemilitaire.ServiceMilitaireService;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.prestations.Etat;
import ch.globaz.vulpecula.domain.models.prestations.TypePrestation;
import ch.globaz.vulpecula.domain.models.servicemilitaire.ServiceMilitaire;
import ch.globaz.vulpecula.domain.repositories.servicemilitaire.ServiceMilitaireRepository;

public class ServiceMilitaireServiceImpl implements ServiceMilitaireService {
    private ServiceMilitaireRepository serviceMilitaireRepository;
    private PosteTravailService posteTravailService;

    public ServiceMilitaireServiceImpl(ServiceMilitaireRepository serviceMilitaireRepository,
            PosteTravailService posteTravailService) {
        this.serviceMilitaireRepository = serviceMilitaireRepository;
        this.posteTravailService = posteTravailService;
    }

    @Override
    public ServiceMilitaire create(ServiceMilitaire serviceMilitaire) throws UnsatisfiedSpecificationException {
        PosteTravail posteTravail = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(
                serviceMilitaire.getIdPosteTravail());
        serviceMilitaire.setPosteTravail(posteTravail);
        serviceMilitaire.validate();
        // A désactiver pour la reprise
        if (!hasAssuranceActiveForAJ(serviceMilitaire)) {
            throw new UnsatisfiedSpecificationException(SpecificationMessage.PRESTATION_ASSURANCE_NON_ACTIVE);
        }
        if (serviceMilitaire.getEtat() == null) {
            serviceMilitaire.setEtat(Etat.SAISIE);
        }
        serviceMilitaire = serviceMilitaireRepository.create(serviceMilitaire);
        return serviceMilitaire;
    }

    private boolean hasAssuranceActiveForAJ(ServiceMilitaire serviceMilitaire) {
        return posteTravailService.hasAssuranceActiveForX(serviceMilitaire.getPosteTravail(), serviceMilitaire
                .getPeriode().getDateDebut(), serviceMilitaire.getPeriode().getDateFin(),
                TypePrestation.SERVICES_MILITAIRE);
    }
}
