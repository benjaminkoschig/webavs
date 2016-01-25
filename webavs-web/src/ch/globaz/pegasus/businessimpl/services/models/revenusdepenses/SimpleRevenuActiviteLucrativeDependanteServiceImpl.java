package ch.globaz.pegasus.businessimpl.services.models.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuActiviteLucrativeDependanteException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleRevenuActiviteLucrativeDependante;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleRevenuActiviteLucrativeDependanteSearch;
import ch.globaz.pegasus.business.services.models.revenusdepenses.SimpleRevenuActiviteLucrativeDependanteService;
import ch.globaz.pegasus.businessimpl.checkers.revenusdepenses.SimpleRevenuActiviteLucrativeDependanteChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleRevenuActiviteLucrativeDependanteServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleRevenuActiviteLucrativeDependanteService {
    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleRevenuActiviteLucrativeDependanteService #create(ch.globaz.pegasus.business.models.fortuneparticuliere
     * .SimpleRevenuActiviteLucrativeDependante)
     */
    @Override
    public SimpleRevenuActiviteLucrativeDependante create(
            SimpleRevenuActiviteLucrativeDependante simpleRevenuActiviteLucrativeDependante)
            throws JadePersistenceException, RevenuActiviteLucrativeDependanteException {
        if (simpleRevenuActiviteLucrativeDependante == null) {
            throw new RevenuActiviteLucrativeDependanteException(
                    "Unable to create simpleRevenuActiviteLucrativeDependante, the model passed is null!");
        }
        SimpleRevenuActiviteLucrativeDependanteChecker.checkForCreate(simpleRevenuActiviteLucrativeDependante);
        return (SimpleRevenuActiviteLucrativeDependante) JadePersistenceManager
                .add(simpleRevenuActiviteLucrativeDependante);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleRevenuActiviteLucrativeDependanteService #delete(ch.globaz.pegasus.business.models.fortuneparticuliere
     * .SimpleRevenuActiviteLucrativeDependante)
     */
    @Override
    public SimpleRevenuActiviteLucrativeDependante delete(
            SimpleRevenuActiviteLucrativeDependante simpleRevenuActiviteLucrativeDependante)
            throws RevenuActiviteLucrativeDependanteException, JadePersistenceException {
        if (simpleRevenuActiviteLucrativeDependante == null) {
            throw new RevenuActiviteLucrativeDependanteException(
                    "Unable to delete simpleRevenuActiviteLucrativeDependante, the model passed is null!");
        }
        if (simpleRevenuActiviteLucrativeDependante.isNew()) {
            throw new RevenuActiviteLucrativeDependanteException(
                    "Unable to delete simpleRevenuActiviteLucrativeDependante, the model passed is new!");
        }
        SimpleRevenuActiviteLucrativeDependanteChecker.checkForDelete(simpleRevenuActiviteLucrativeDependante);
        return (SimpleRevenuActiviteLucrativeDependante) JadePersistenceManager
                .delete(simpleRevenuActiviteLucrativeDependante);
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleRevenuActiviteLucrativeDependanteSearch search = new SimpleRevenuActiviteLucrativeDependanteSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleRevenuActiviteLucrativeDependanteService#read(java.lang.String)
     */
    @Override
    public SimpleRevenuActiviteLucrativeDependante read(String idRevenuActiviteLucrativeDependante)
            throws JadePersistenceException, RevenuActiviteLucrativeDependanteException {
        if (JadeStringUtil.isEmpty(idRevenuActiviteLucrativeDependante)) {
            throw new RevenuActiviteLucrativeDependanteException(
                    "Unable to read simpleRevenuActiviteLucrativeDependante, the id passed is not defined!");
        }
        SimpleRevenuActiviteLucrativeDependante simpleRevenuActiviteLucrativeDependante = new SimpleRevenuActiviteLucrativeDependante();
        simpleRevenuActiviteLucrativeDependante.setId(idRevenuActiviteLucrativeDependante);
        return (SimpleRevenuActiviteLucrativeDependante) JadePersistenceManager
                .read(simpleRevenuActiviteLucrativeDependante);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleRevenuActiviteLucrativeDependanteService #update(ch.globaz.pegasus.business.models.fortuneparticuliere
     * .SimpleRevenuActiviteLucrativeDependante)
     */
    @Override
    public SimpleRevenuActiviteLucrativeDependante update(
            SimpleRevenuActiviteLucrativeDependante simpleRevenuActiviteLucrativeDependante)
            throws JadePersistenceException, RevenuActiviteLucrativeDependanteException {
        if (simpleRevenuActiviteLucrativeDependante == null) {
            throw new RevenuActiviteLucrativeDependanteException(
                    "Unable to update simpleRevenuActiviteLucrativeDependante, the model passed is null!");
        }
        if (simpleRevenuActiviteLucrativeDependante.isNew()) {
            throw new RevenuActiviteLucrativeDependanteException(
                    "Unable to update simpleRevenuActiviteLucrativeDependante, the model passed is new!");
        }
        SimpleRevenuActiviteLucrativeDependanteChecker.checkForUpdate(simpleRevenuActiviteLucrativeDependante);
        return (SimpleRevenuActiviteLucrativeDependante) JadePersistenceManager
                .update(simpleRevenuActiviteLucrativeDependante);
    }

}
