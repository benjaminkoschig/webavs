package ch.globaz.pegasus.businessimpl.services.annonce.communicationocc;

import ch.globaz.pegasus.business.services.annonce.communicationocc.CommunicationOCCBuilder;
import ch.globaz.pegasus.business.services.annonce.communicationocc.CommunicationOCCBuilderProviderService;

public class CommunicationOCCBuilderProviderServiceImpl implements CommunicationOCCBuilderProviderService {

    @Override
    public CommunicationOCCBuilder getCommunicationOCCBuilder() {
        return new CommunicationOCCDefaultBuilder();
    }

}
