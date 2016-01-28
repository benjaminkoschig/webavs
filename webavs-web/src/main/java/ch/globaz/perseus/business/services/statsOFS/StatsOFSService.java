package ch.globaz.perseus.business.services.statsOFS;

import globaz.globall.db.BSession;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessLogSession;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.impotsource.ImpotSourceException;

public interface StatsOFSService extends JadeApplicationService {

    public String genererFichierXML(BSession session, JadeBusinessLogSession logSession, String anneeEnquete)
            throws ImpotSourceException, JadePersistenceException, Exception;
}
