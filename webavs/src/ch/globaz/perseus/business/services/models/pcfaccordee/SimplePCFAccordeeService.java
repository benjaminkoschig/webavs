package ch.globaz.perseus.business.services.models.pcfaccordee;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.pcfaccordee.PCFAccordeeException;
import ch.globaz.perseus.business.models.pcfaccordee.SimplePCFAccordee;
import ch.globaz.perseus.business.models.pcfaccordee.SimplePCFAccordeeSearchModel;

public interface SimplePCFAccordeeService extends JadeApplicationService {

    public SimplePCFAccordee create(SimplePCFAccordee simplePCFAccordee) throws JadePersistenceException,
            PCFAccordeeException;

    public SimplePCFAccordee delete(SimplePCFAccordee simplePCFAccordee) throws JadePersistenceException,
            PCFAccordeeException;

    public SimplePCFAccordee read(String idSimplePCFAccordee) throws JadePersistenceException, PCFAccordeeException;

    public SimplePCFAccordeeSearchModel search(SimplePCFAccordeeSearchModel simplePCFAccordeeSearchModel)
            throws JadePersistenceException, PCFAccordeeException;

    public SimplePCFAccordee update(SimplePCFAccordee simplePCFAccordee) throws JadePersistenceException,
            PCFAccordeeException;

}
