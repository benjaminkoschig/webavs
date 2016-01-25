package ch.globaz.pegasus.business.services.transfertDossier;

import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.transfertdossier.TransfertDossierException;

public interface TransfertDossierBuilderProviderService extends JadeApplicationService {

    public ITransfertDossierBuilder getBuilderFor(String typeBuilder) throws TransfertDossierException;
}
