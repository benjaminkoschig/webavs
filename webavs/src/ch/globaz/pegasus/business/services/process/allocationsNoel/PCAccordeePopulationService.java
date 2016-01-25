package ch.globaz.pegasus.business.services.process.allocationsNoel;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.process.AdaptationException;
import ch.globaz.pegasus.business.models.process.allocationsNoel.PCAccordeePopulationSearch;

public interface PCAccordeePopulationService extends JadeApplicationService {

    public PCAccordeePopulationSearch findPcaForProcessAllocationNoel() throws AdaptationException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    public PCAccordeePopulationSearch search(PCAccordeePopulationSearch search) throws JadePersistenceException,
            AdaptationException;

}
