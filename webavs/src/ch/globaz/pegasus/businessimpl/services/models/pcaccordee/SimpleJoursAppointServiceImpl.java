package ch.globaz.pegasus.businessimpl.services.models.pcaccordee;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppoint;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppointSearch;
import ch.globaz.pegasus.business.services.models.pcaccordee.SimpleJoursAppointService;
import ch.globaz.pegasus.businessimpl.checkers.pcaccordee.SimpleJoursAppointChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleJoursAppointServiceImpl extends PegasusAbstractServiceImpl implements SimpleJoursAppointService {

    @Override
    public SimpleJoursAppoint create(SimpleJoursAppoint simpleJoursAppoint) throws PCAccordeeException,
            JadePersistenceException {
        if (simpleJoursAppoint == null) {
            throw new PCAccordeeException("Unable to create simpleJoursAppoint, the model passed is null!");
        }
        SimpleJoursAppointChecker.checkForCreate(simpleJoursAppoint);
        return (SimpleJoursAppoint) JadePersistenceManager.add(simpleJoursAppoint);
    }

    @Override
    public SimpleJoursAppoint delete(SimpleJoursAppoint simpleJoursAppoint) throws PCAccordeeException,
            JadePersistenceException {
        if (simpleJoursAppoint == null) {
            throw new PCAccordeeException("Unable to delete simpleJoursAppoint, the model passed is null!");
        }
        if (simpleJoursAppoint.isNew()) {
            throw new PCAccordeeException("Unable to delete simpleJoursAppoint, the model passed is new!");
        }
        SimpleJoursAppointChecker.checkForDelete(simpleJoursAppoint);
        return (SimpleJoursAppoint) JadePersistenceManager.delete(simpleJoursAppoint);
    }

    @Override
    public int delete(SimpleJoursAppointSearch simpleJoursAppointSearch) throws PCAccordeeException,
            JadePersistenceException {
        if (simpleJoursAppointSearch == null) {
            throw new PCAccordeeException("Unable to delete simpleJoursAppointSearch, the model passed is null!");
        }
        return JadePersistenceManager.delete(simpleJoursAppointSearch);
    }

    @Override
    public SimpleJoursAppoint read(String idSimpleJoursAppoint) throws PCAccordeeException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idSimpleJoursAppoint)) {
            throw new PCAccordeeException("Unable to read simpleJoursAppoint, the id passed is not defined!");
        }
        SimpleJoursAppoint simpleJoursAppoint = new SimpleJoursAppoint();
        simpleJoursAppoint.setId(idSimpleJoursAppoint);
        return (SimpleJoursAppoint) JadePersistenceManager.read(simpleJoursAppoint);
    }

    @Override
    public SimpleJoursAppointSearch search(SimpleJoursAppointSearch simpleJoursAppointSearch)
            throws JadePersistenceException, PCAccordeeException {
        if (simpleJoursAppointSearch == null) {
            throw new PCAccordeeException("Unable to search simpleJoursAppointSearch, the search model passed is null!");
        }
        return (SimpleJoursAppointSearch) JadePersistenceManager.search(simpleJoursAppointSearch);
    }

    @Override
    public SimpleJoursAppoint update(SimpleJoursAppoint simpleJoursAppoint) throws PCAccordeeException,
            JadePersistenceException {
        if (simpleJoursAppoint == null) {
            throw new PCAccordeeException("Unable to update simpleJoursAppoint, the model passed is null!");
        }
        if (simpleJoursAppoint.isNew()) {
            throw new PCAccordeeException("Unable to update simpleJoursAppoint, the model passed is new!");
        }
        SimpleJoursAppointChecker.checkForUpdate(simpleJoursAppoint);
        return (SimpleJoursAppoint) JadePersistenceManager.update(simpleJoursAppoint);
    }

}
