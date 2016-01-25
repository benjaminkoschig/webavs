package ch.globaz.pegasus.businessimpl.services.annonce.annoncelaprams;

import ch.globaz.pegasus.business.services.annonce.annoncelaprams.AnnonceLapramsBuilder;
import ch.globaz.pegasus.business.services.annonce.annoncelaprams.AnnonceLapramsBuilderProviderService;

public class AnnonceLapramsBuilderProviderServiceImpl implements AnnonceLapramsBuilderProviderService {

    @Override
    public AnnonceLapramsBuilder getAnnonceLapramsDefaultBuilder() {
        return new AnnonceLapramsDefaultBuilder();
    }

}
