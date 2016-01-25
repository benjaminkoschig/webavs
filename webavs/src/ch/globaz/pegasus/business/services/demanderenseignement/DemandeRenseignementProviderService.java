package ch.globaz.pegasus.business.services.demanderenseignement;

import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.demanderenseignement.AbstractDemandeRenseignementBuilder;

public interface DemandeRenseignementProviderService extends JadeApplicationService {

    public AbstractDemandeRenseignementBuilder getDemandeBuilder(String csTypeDemandeRenseignement);

}
