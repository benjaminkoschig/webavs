package ch.globaz.pegasus.business.services.models.pcaccordee;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordeesSearch;

public interface SimplePrestationAccordeeService extends JadeApplicationService {

    int count(SimplePrestationsAccordeesSearch model) throws JadePersistenceException, JadeApplicationException;

    SimplePrestationsAccordees create(SimplePrestationsAccordees simplePrestationAccordee)
            throws JadePersistenceException, JadeApplicationException;

    SimplePrestationsAccordees delete(SimplePrestationsAccordees simplePrestationAccordee)
            throws JadePersistenceException, JadeApplicationException;

    public int delete(SimplePrestationsAccordeesSearch simplePrestationAccordeeSearch) throws JadePersistenceException,
            JadeApplicationException;

    SimplePrestationsAccordeesSearch find(SimplePrestationsAccordeesSearch searchModel)
            throws JadePersistenceException, JadeApplicationException;

    SimplePrestationsAccordees read(String idPrestation) throws JadePersistenceException, JadeApplicationException;

    SimplePrestationsAccordees update(SimplePrestationsAccordees simplePrestationAccordee)
            throws JadePersistenceException, JadeApplicationException;
}
