package ch.globaz.pegasus.business.services.transfertDossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.transfertdossier.TransfertDossierException;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.transfertDossier.DefaultTransfertRentePCBuilder;

public interface TransfertRentePCProviderService extends JadeApplicationService {

    public void checkProcessArguments(String idCaisseAgence) throws TransfertDossierException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException;

    public DefaultTransfertRentePCBuilder getTransfertBuilder();

}
