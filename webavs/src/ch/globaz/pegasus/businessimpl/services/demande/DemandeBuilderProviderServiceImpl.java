package ch.globaz.pegasus.businessimpl.services.demande;

import ch.globaz.pegasus.business.services.demande.DemandeBuilder;
import ch.globaz.pegasus.business.services.demande.DemandeBuilderProviderService;

public class DemandeBuilderProviderServiceImpl implements DemandeBuilderProviderService {

    @Override
    public DemandeBuilder getBillagBuilder() {
        return new SingleBillagBuilder();
    }

}
