package ch.globaz.vulpecula.business.services.servicemilitaire;

import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.servicemilitaire.ServiceMilitaire;

public interface ServiceMilitaireService {
    public ServiceMilitaire create(ServiceMilitaire serviceMilitaire) throws UnsatisfiedSpecificationException;
}
