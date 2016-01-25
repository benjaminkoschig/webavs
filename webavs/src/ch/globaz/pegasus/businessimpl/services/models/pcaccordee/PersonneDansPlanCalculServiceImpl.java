package ch.globaz.pegasus.businessimpl.services.models.pcaccordee;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PersonneDansPlanCalculException;
import ch.globaz.pegasus.business.models.pcaccordee.PersonneDansPlanCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.PersonneDansPlanCalculSearch;
import ch.globaz.pegasus.business.services.models.pcaccordee.PersonneDansPlanCalculService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class PersonneDansPlanCalculServiceImpl extends PegasusAbstractServiceImpl implements
        PersonneDansPlanCalculService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.pcaccordee.EnfantDansCalculService #
     * count(ch.globaz.pegasus.business.models.pcaccordee.EnfantDansCalculSearch )
     */
    @Override
    public int count(PersonneDansPlanCalculSearch search) throws PCAccordeeException, JadePersistenceException {
        if (search == null) {
            throw new PersonneDansPlanCalculException(
                    "Unable to count enfantDansCalcul, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.pcaccordee.EnfantDansCalculService
     * #create(ch.globaz.pegasus.business.models.pcaccordee.EnfantDansCalcul)
     */
    @Override
    public PersonneDansPlanCalcul create(PersonneDansPlanCalcul enfantDansCalcul) throws PCAccordeeException,
            JadePersistenceException {
        if (enfantDansCalcul == null) {
            throw new PersonneDansPlanCalculException("Unable to create enfantDansCalcul, the model passed is null!");
        }
        try {
            enfantDansCalcul.setSimplePersonneDansPlanCalcul(PegasusImplServiceLocator
                    .getSimplePersonneDansPlanCalculService()
                    .create(enfantDansCalcul.getSimplePersonneDansPlanCalcul()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PersonneDansPlanCalculException("Service not available - " + e.getMessage());
        }
        return enfantDansCalcul;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.pcaccordee.EnfantDansCalculService
     * #delete(ch.globaz.pegasus.business.models.pcaccordee.EnfantDansCalcul)
     */
    @Override
    public PersonneDansPlanCalcul delete(PersonneDansPlanCalcul enfantDansCalcul) throws PCAccordeeException,
            JadePersistenceException {
        if (enfantDansCalcul == null) {
            throw new PersonneDansPlanCalculException("Unable to delete enfantDansCalcul, the model passed is null!");
        }
        try {
            enfantDansCalcul.setSimplePersonneDansPlanCalcul(PegasusImplServiceLocator
                    .getSimplePersonneDansPlanCalculService()
                    .delete(enfantDansCalcul.getSimplePersonneDansPlanCalcul()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PersonneDansPlanCalculException("Service not available - " + e.getMessage());
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.pcaccordee.EnfantDansCalculService #read(java.lang.String)
     */
    @Override
    public PersonneDansPlanCalcul read(String idEnfantDansCalcul) throws PCAccordeeException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idEnfantDansCalcul)) {
            throw new PersonneDansPlanCalculException("Unable to read enfantDansCalcul, the id passed is null!");
        }
        PersonneDansPlanCalcul enfantDansCalcul = new PersonneDansPlanCalcul(idEnfantDansCalcul);
        enfantDansCalcul.setId(idEnfantDansCalcul);
        return (PersonneDansPlanCalcul) JadePersistenceManager.read(enfantDansCalcul);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.pcaccordee.EnfantDansCalculService #
     * search(ch.globaz.pegasus.business.models.pcaccordee.EnfantDansCalculSearch )
     */

    @Override
    public PersonneDansPlanCalculSearch search(PersonneDansPlanCalculSearch enfantDansCalculSearch)
            throws JadePersistenceException, PCAccordeeException {
        if (enfantDansCalculSearch == null) {
            throw new PersonneDansPlanCalculException(
                    "Unable to search enfantDansCalcul, the search model passed is null!");
        }
        return (PersonneDansPlanCalculSearch) JadePersistenceManager.search(enfantDansCalculSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.pcaccordee.EnfantDansCalculService
     * #update(ch.globaz.pegasus.business.models.pcaccordee.EnfantDansCalcul)
     */
    @Override
    public PersonneDansPlanCalcul update(PersonneDansPlanCalcul enfantDansCalcul) throws PCAccordeeException,
            JadePersistenceException {
        if (enfantDansCalcul == null) {
            throw new PersonneDansPlanCalculException("Unable to update enfantDansCalcul, the model passed is null!");
        }

        try {
            enfantDansCalcul.setSimplePersonneDansPlanCalcul(PegasusImplServiceLocator
                    .getSimplePersonneDansPlanCalculService()
                    .update(enfantDansCalcul.getSimplePersonneDansPlanCalcul()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PersonneDansPlanCalculException("Service not available - " + e.getMessage());
        }

        return enfantDansCalcul;
    }
}
