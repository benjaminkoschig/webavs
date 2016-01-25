package ch.globaz.pegasus.businessimpl.services.models.fortuneparticuliere;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.AutreFortuneMobiliereException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleAutreFortuneMobiliere;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleAutreFortuneMobiliereSearch;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.SimpleAutreFortuneMobiliereService;
import ch.globaz.pegasus.businessimpl.checkers.fortuneparticuliere.SimpleAutreFortuneMobiliereChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleAutreFortuneMobiliereServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleAutreFortuneMobiliereService {
    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleAutreFortuneMobiliereService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleAutreFortuneMobiliere)
     */
    @Override
    public SimpleAutreFortuneMobiliere create(SimpleAutreFortuneMobiliere simpleAutreFortuneMobiliere)
            throws JadePersistenceException, AutreFortuneMobiliereException {
        if (simpleAutreFortuneMobiliere == null) {
            throw new AutreFortuneMobiliereException(
                    "Unable to create simpleAutreFortuneMobiliere, the model passed is null!");
        }
        SimpleAutreFortuneMobiliereChecker.checkForCreate(simpleAutreFortuneMobiliere);
        return (SimpleAutreFortuneMobiliere) JadePersistenceManager.add(simpleAutreFortuneMobiliere);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleAutreFortuneMobiliereService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleAutreFortuneMobiliere)
     */
    @Override
    public SimpleAutreFortuneMobiliere delete(SimpleAutreFortuneMobiliere simpleAutreFortuneMobiliere)
            throws AutreFortuneMobiliereException, JadePersistenceException {
        if (simpleAutreFortuneMobiliere == null) {
            throw new AutreFortuneMobiliereException(
                    "Unable to delete simpleAutreFortuneMobiliere, the model passed is null!");
        }
        if (simpleAutreFortuneMobiliere.isNew()) {
            throw new AutreFortuneMobiliereException(
                    "Unable to delete simpleAutreFortuneMobiliere, the model passed is new!");
        }
        SimpleAutreFortuneMobiliereChecker.checkForDelete(simpleAutreFortuneMobiliere);
        return (SimpleAutreFortuneMobiliere) JadePersistenceManager.delete(simpleAutreFortuneMobiliere);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleAutreFortuneMobiliereService#deleteParListeIdDoFinH(java.util.List)
     */
    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleAutreFortuneMobiliereSearch search = new SimpleAutreFortuneMobiliereSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleAutreFortuneMobiliereService#read(java.lang.String)
     */
    @Override
    public SimpleAutreFortuneMobiliere read(String idAutreFortuneMobiliere) throws JadePersistenceException,
            AutreFortuneMobiliereException {
        if (JadeStringUtil.isEmpty(idAutreFortuneMobiliere)) {
            throw new AutreFortuneMobiliereException(
                    "Unable to read simpleAutreFortuneMobiliere, the id passed is not defined!");
        }
        SimpleAutreFortuneMobiliere simpleAutreFortuneMobiliere = new SimpleAutreFortuneMobiliere();
        simpleAutreFortuneMobiliere.setId(idAutreFortuneMobiliere);
        return (SimpleAutreFortuneMobiliere) JadePersistenceManager.read(simpleAutreFortuneMobiliere);
    }

    @Override
    public SimpleAutreFortuneMobiliereSearch search(SimpleAutreFortuneMobiliereSearch searchModel)
            throws JadePersistenceException, AutreFortuneMobiliereException {
        if (searchModel == null) {
            throw new AutreFortuneMobiliereException(
                    "Unable to search simpleAutreFortuneMobiliere, the search model passed is null!");
        }
        return (SimpleAutreFortuneMobiliereSearch) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleAutreFortuneMobiliereService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleAutreFortuneMobiliere)
     */
    @Override
    public SimpleAutreFortuneMobiliere update(SimpleAutreFortuneMobiliere simpleAutreFortuneMobiliere)
            throws JadePersistenceException, AutreFortuneMobiliereException {
        if (simpleAutreFortuneMobiliere == null) {
            throw new AutreFortuneMobiliereException(
                    "Unable to update simpleAutreFortuneMobiliere, the model passed is null!");
        }
        if (simpleAutreFortuneMobiliere.isNew()) {
            throw new AutreFortuneMobiliereException(
                    "Unable to update simpleAutreFortuneMobiliere, the model passed is new!");
        }
        SimpleAutreFortuneMobiliereChecker.checkForUpdate(simpleAutreFortuneMobiliere);
        return (SimpleAutreFortuneMobiliere) JadePersistenceManager.update(simpleAutreFortuneMobiliere);
    }

}
