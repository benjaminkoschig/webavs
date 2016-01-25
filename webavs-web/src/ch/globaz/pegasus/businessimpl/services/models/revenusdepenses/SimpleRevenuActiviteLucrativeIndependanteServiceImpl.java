package ch.globaz.pegasus.businessimpl.services.models.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuActiviteLucrativeIndependanteException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleRevenuActiviteLucrativeIndependante;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleRevenuActiviteLucrativeIndependanteSearch;
import ch.globaz.pegasus.business.services.models.revenusdepenses.SimpleRevenuActiviteLucrativeIndependanteService;
import ch.globaz.pegasus.businessimpl.checkers.revenusdepenses.SimpleRevenuActiviteLucrativeIndependanteChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleRevenuActiviteLucrativeIndependanteServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleRevenuActiviteLucrativeIndependanteService {
    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleRevenuActiviteLucrativeIndependanteService #create(ch.globaz.pegasus.business.models.fortuneparticuliere
     * .SimpleRevenuActiviteLucrativeIndependante)
     */
    @Override
    public SimpleRevenuActiviteLucrativeIndependante create(
            SimpleRevenuActiviteLucrativeIndependante simpleRevenuActiviteLucrativeIndependante)
            throws JadePersistenceException, RevenuActiviteLucrativeIndependanteException {
        if (simpleRevenuActiviteLucrativeIndependante == null) {
            throw new RevenuActiviteLucrativeIndependanteException(
                    "Unable to create simpleRevenuActiviteLucrativeIndependante, the model passed is null!");
        }
        SimpleRevenuActiviteLucrativeIndependanteChecker.checkForCreate(simpleRevenuActiviteLucrativeIndependante);
        return (SimpleRevenuActiviteLucrativeIndependante) JadePersistenceManager
                .add(simpleRevenuActiviteLucrativeIndependante);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleRevenuActiviteLucrativeIndependanteService #delete(ch.globaz.pegasus.business.models.fortuneparticuliere
     * .SimpleRevenuActiviteLucrativeIndependante)
     */
    @Override
    public SimpleRevenuActiviteLucrativeIndependante delete(
            SimpleRevenuActiviteLucrativeIndependante simpleRevenuActiviteLucrativeIndependante)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException {
        if (simpleRevenuActiviteLucrativeIndependante == null) {
            throw new RevenuActiviteLucrativeIndependanteException(
                    "Unable to delete simpleRevenuActiviteLucrativeIndependante, the model passed is null!");
        }
        if (simpleRevenuActiviteLucrativeIndependante.isNew()) {
            throw new RevenuActiviteLucrativeIndependanteException(
                    "Unable to delete simpleRevenuActiviteLucrativeIndependante, the model passed is new!");
        }
        SimpleRevenuActiviteLucrativeIndependanteChecker.checkForDelete(simpleRevenuActiviteLucrativeIndependante);
        return (SimpleRevenuActiviteLucrativeIndependante) JadePersistenceManager
                .delete(simpleRevenuActiviteLucrativeIndependante);
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleRevenuActiviteLucrativeIndependanteSearch search = new SimpleRevenuActiviteLucrativeIndependanteSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleRevenuActiviteLucrativeIndependanteService#read(java.lang.String)
     */
    @Override
    public SimpleRevenuActiviteLucrativeIndependante read(String idRevenuActiviteLucrativeIndependante)
            throws JadePersistenceException, RevenuActiviteLucrativeIndependanteException {
        if (JadeStringUtil.isEmpty(idRevenuActiviteLucrativeIndependante)) {
            throw new RevenuActiviteLucrativeIndependanteException(
                    "Unable to read simpleRevenuActiviteLucrativeIndependante, the id passed is not defined!");
        }
        SimpleRevenuActiviteLucrativeIndependante simpleRevenuActiviteLucrativeIndependante = new SimpleRevenuActiviteLucrativeIndependante();
        simpleRevenuActiviteLucrativeIndependante.setId(idRevenuActiviteLucrativeIndependante);
        return (SimpleRevenuActiviteLucrativeIndependante) JadePersistenceManager
                .read(simpleRevenuActiviteLucrativeIndependante);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleRevenuActiviteLucrativeIndependanteService #update(ch.globaz.pegasus.business.models.fortuneparticuliere
     * .SimpleRevenuActiviteLucrativeIndependante)
     */
    @Override
    public SimpleRevenuActiviteLucrativeIndependante update(
            SimpleRevenuActiviteLucrativeIndependante simpleRevenuActiviteLucrativeIndependante)
            throws JadePersistenceException, RevenuActiviteLucrativeIndependanteException {
        if (simpleRevenuActiviteLucrativeIndependante == null) {
            throw new RevenuActiviteLucrativeIndependanteException(
                    "Unable to update simpleRevenuActiviteLucrativeIndependante, the model passed is null!");
        }
        if (simpleRevenuActiviteLucrativeIndependante.isNew()) {
            throw new RevenuActiviteLucrativeIndependanteException(
                    "Unable to update simpleRevenuActiviteLucrativeIndependante, the model passed is new!");
        }
        SimpleRevenuActiviteLucrativeIndependanteChecker.checkForUpdate(simpleRevenuActiviteLucrativeIndependante);
        return (SimpleRevenuActiviteLucrativeIndependante) JadePersistenceManager
                .update(simpleRevenuActiviteLucrativeIndependante);
    }

}
