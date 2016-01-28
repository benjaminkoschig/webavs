package ch.globaz.pegasus.business.services.models.pcaccordee;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalculSearch;

public interface SimplePlanDeCalculService extends JadeApplicationService {

    public int count(SimplePlanDeCalculSearch search) throws PCAccordeeException, JadePersistenceException;

    public SimplePlanDeCalcul create(SimplePlanDeCalcul simplePlanDeCalcul) throws PCAccordeeException,
            JadePersistenceException;

    public SimplePlanDeCalcul delete(SimplePlanDeCalcul simplePlanDeCalcul) throws PCAccordeeException,
            JadePersistenceException;

    public int delete(SimplePlanDeCalculSearch simplePlanDeCalculSearch) throws PCAccordeeException,
            JadePersistenceException;

    public SimplePlanDeCalcul read(String idSimplePlanDeCalcul) throws PCAccordeeException, JadePersistenceException;

    public SimplePlanDeCalcul readPlanRetenuForIdPca(String idPca) throws JadePersistenceException, PCAccordeeException;

    public SimplePlanDeCalculSearch search(SimplePlanDeCalculSearch simplePlanDeCalculSearch)
            throws JadePersistenceException, PCAccordeeException;

    public SimplePlanDeCalcul update(SimplePlanDeCalcul simplePlanDeCalcul) throws PCAccordeeException,
            JadePersistenceException;
}
