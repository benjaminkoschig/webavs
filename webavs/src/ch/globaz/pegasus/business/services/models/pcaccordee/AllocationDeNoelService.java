package ch.globaz.pegasus.business.services.models.pcaccordee;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.AllocationDeNoelException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.AllocationNoel;
import ch.globaz.pegasus.business.models.pcaccordee.AllocationNoelSearch;
import ch.globaz.pegasus.business.models.process.allocationsNoel.PCAccordeePopulation;

public interface AllocationDeNoelService extends JadeApplicationService {

    public AllocationNoel create(PCAccordeePopulation pca, float montantAllocation, String anneeAllocation,
            String idAdressePaiementPostaleCreer, Boolean isCoupleSeparer) throws JadePersistenceException,
            JadeApplicationException;

    public boolean hasAlreadyRecivedAllocationNoelForTheYear(int year, String idDemande)
            throws JadePersistenceException;

    public AllocationNoel readAllocationNoelByIdPca(String idPca) throws PCAccordeeException,
            AllocationDeNoelException, JadeApplicationServiceNotAvailableException, JadePersistenceException;

    public AllocationNoelSearch search(AllocationNoelSearch search) throws JadePersistenceException;
}
