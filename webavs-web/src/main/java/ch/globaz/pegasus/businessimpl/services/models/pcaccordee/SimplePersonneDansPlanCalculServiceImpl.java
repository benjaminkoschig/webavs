package ch.globaz.pegasus.businessimpl.services.models.pcaccordee;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PersonneDansPlanCalculException;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePersonneDansPlanCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePersonneDansPlanCalculSearch;
import ch.globaz.pegasus.business.services.models.pcaccordee.SimplePersonneDansPlanCalculService;
import ch.globaz.pegasus.businessimpl.checkers.pcaccordee.SimplePersonneDansPlanCalculChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimplePersonneDansPlanCalculServiceImpl extends PegasusAbstractServiceImpl implements
        SimplePersonneDansPlanCalculService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.pcaccordee. SimplePersonneDansPlanCalculService
     * #count(ch.globaz.pegasus.business.models.pcaccordee .SimplePersonneDansPlanCalculSearch)
     */
    @Override
    public int count(SimplePersonneDansPlanCalculSearch search) throws PersonneDansPlanCalculException,
            JadePersistenceException {
        if (search == null) {
            throw new PersonneDansPlanCalculException(
                    "Unable to count simplePersonneDansPlanCalcul, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.pcaccordee. SimplePersonneDansPlanCalculService
     * #create(ch.globaz.pegasus.business.models.pcaccordee .SimplePersonneDansPlanCalcul)
     */
    @Override
    public SimplePersonneDansPlanCalcul create(SimplePersonneDansPlanCalcul simplePersonneDansPlanCalcul)
            throws PersonneDansPlanCalculException, JadePersistenceException {
        if (simplePersonneDansPlanCalcul == null) {
            throw new PersonneDansPlanCalculException(
                    "Unable to create simplePersonneDansPlanCalcul, the model passed is null!");
        }
        // SimplePersonneDansPlanCalculChecker.checkForCreate(simplePersonneDansPlanCalcul);
        return (SimplePersonneDansPlanCalcul) JadePersistenceManager.add(simplePersonneDansPlanCalcul);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.pcaccordee. SimplePersonneDansPlanCalculService
     * #delete(ch.globaz.pegasus.business.models.pcaccordee .SimplePersonneDansPlanCalcul)
     */
    @Override
    public SimplePersonneDansPlanCalcul delete(SimplePersonneDansPlanCalcul simplePersonneDansPlanCalcul)
            throws PersonneDansPlanCalculException, JadePersistenceException {
        if (simplePersonneDansPlanCalcul == null) {
            throw new PersonneDansPlanCalculException(
                    "Unable to delete simplePersonneDansPlanCalcul, the model passed is null!");
        }
        if (simplePersonneDansPlanCalcul.isNew()) {
            throw new PersonneDansPlanCalculException(
                    "Unable to delete simplePersonneDansPlanCalcul, the model passed is new!");
        }
        SimplePersonneDansPlanCalculChecker.checkForDelete(simplePersonneDansPlanCalcul);
        return (SimplePersonneDansPlanCalcul) JadePersistenceManager.delete(simplePersonneDansPlanCalcul);
    }

    @Override
    public void delete(SimplePersonneDansPlanCalculSearch personneSearch) throws JadePersistenceException,
            PCAccordeeException {
        if (personneSearch == null) {
            throw new PersonneDansPlanCalculException("Unable to delete from search model, the model passed is null!");
        }

        JadePersistenceManager.delete(personneSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.pcaccordee.
     * SimplePersonneDansPlanCalculService#read(java.lang.String)
     */
    @Override
    public SimplePersonneDansPlanCalcul read(String idSimplePersonneDansPlanCalcul)
            throws PersonneDansPlanCalculException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idSimplePersonneDansPlanCalcul)) {
            throw new PersonneDansPlanCalculException(
                    "Unable to read simplePersonneDansPlanCalcul, the id passed is not defined!");
        }
        SimplePersonneDansPlanCalcul simplePersonneDansPlanCalcul = new SimplePersonneDansPlanCalcul();
        simplePersonneDansPlanCalcul.setId(idSimplePersonneDansPlanCalcul);
        return (SimplePersonneDansPlanCalcul) JadePersistenceManager.read(simplePersonneDansPlanCalcul);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.pcaccordee. SimplePersonneDansPlanCalculService
     * #search(ch.globaz.pegasus.business.models.pcaccordee .SimplePersonneDansPlanCalculSearch)
     */
    @Override
    public SimplePersonneDansPlanCalculSearch search(
            SimplePersonneDansPlanCalculSearch simplePersonneDansPlanCalculSearch) throws JadePersistenceException,
            PersonneDansPlanCalculException {
        if (simplePersonneDansPlanCalculSearch == null) {
            throw new PersonneDansPlanCalculException(
                    "Unable to search simplePersonneDansPlanCalcul, the search model passed is null!");
        }
        return (SimplePersonneDansPlanCalculSearch) JadePersistenceManager.search(simplePersonneDansPlanCalculSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.pcaccordee. SimplePersonneDansPlanCalculService
     * #update(ch.globaz.pegasus.business.models.pcaccordee .SimplePersonneDansPlanCalcul)
     */
    @Override
    public SimplePersonneDansPlanCalcul update(SimplePersonneDansPlanCalcul simplePersonneDansPlanCalcul)
            throws PersonneDansPlanCalculException, JadePersistenceException {
        if (simplePersonneDansPlanCalcul == null) {
            throw new PersonneDansPlanCalculException(
                    "Unable to update simplePersonneDansPlanCalcul, the model passed is null!");
        }
        if (simplePersonneDansPlanCalcul.isNew()) {
            throw new PersonneDansPlanCalculException(
                    "Unable to update simplePersonneDansPlanCalcul, the model passed is new!");
        }
        SimplePersonneDansPlanCalculChecker.checkForUpdate(simplePersonneDansPlanCalcul);
        return (SimplePersonneDansPlanCalcul) JadePersistenceManager.update(simplePersonneDansPlanCalcul);
    }

}
