package ch.globaz.pegasus.business.services.models.annonce.communicationocc;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.annonce.SimpleCommunicationOCC;
import ch.globaz.pegasus.business.models.annonce.SimpleCommunicationOCCSearch;

public interface SimpleCommunicationOCCService extends JadeApplicationService {
    public SimpleCommunicationOCC create(SimpleCommunicationOCC simpleCommunicationOCC) throws JadePersistenceException;

    public SimpleCommunicationOCC delete(SimpleCommunicationOCC simpleCommunicationOCC)
            throws JadePersistenceException, PrestationException;

    public int delete(SimpleCommunicationOCCSearch search) throws JadePersistenceException, PrestationException;

    public SimpleCommunicationOCC read(String idSimpleCommunicationOCC) throws JadePersistenceException,
            PrestationException;

    public SimpleCommunicationOCC update(SimpleCommunicationOCC simpleCommunicationOCC)
            throws JadePersistenceException, PrestationException;

}