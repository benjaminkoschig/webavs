package ch.globaz.perseus.business.services.bvr;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.PerseusException;

public interface BvrService extends JadeApplicationService {

    public boolean validationCCP(String idAdressePaiement, String idApplication)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException;

    public String validationNumeroBVR(String numeroBVR) throws PerseusException;

}
