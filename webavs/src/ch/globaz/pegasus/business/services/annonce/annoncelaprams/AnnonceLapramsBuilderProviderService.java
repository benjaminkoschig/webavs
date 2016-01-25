package ch.globaz.pegasus.business.services.annonce.annoncelaprams;

import globaz.jade.service.provider.application.JadeApplicationService;

public interface AnnonceLapramsBuilderProviderService extends JadeApplicationService {

    public AnnonceLapramsBuilder getAnnonceLapramsDefaultBuilder();
}
