package ch.globaz.pegasus.businessimpl.services.demanderenseignement;

import ch.globaz.pegasus.business.services.demanderenseignement.DemandeRenseignementProviderService;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.AbstractPegasusBuilder;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.demanderenseignement.AbstractDemandeRenseignementBuilder;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.demanderenseignement.DemandeRenseignementAgenceCommunaleAVSBuilder;

public class DemandeRenseignementProviderServiceImpl extends AbstractPegasusBuilder implements
        DemandeRenseignementProviderService {

    @Override
    public AbstractDemandeRenseignementBuilder getDemandeBuilder(String csTypeDemandeRenseignement) {
        return new DemandeRenseignementAgenceCommunaleAVSBuilder();
    }

}
