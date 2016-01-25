package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.pegasus.business.exceptions.models.blocage.BlocageException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;

public interface AfterPersisterOperations {

    void afterPersist(SimpleLot simpleLot) throws PrestationException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, BlocageException, JadeApplicationException;

}
