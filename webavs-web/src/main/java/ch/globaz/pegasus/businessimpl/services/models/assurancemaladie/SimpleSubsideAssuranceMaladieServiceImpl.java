package ch.globaz.pegasus.businessimpl.services.models.assurancemaladie;

import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.SubsideAssuranceMaladieException;
import ch.globaz.pegasus.business.models.assurancemaladie.SimpleSubsideAssuranceMaladie;
import ch.globaz.pegasus.business.models.assurancemaladie.SimpleSubsideAssuranceMaladieSearch;
import ch.globaz.pegasus.business.services.models.assurancemaladie.SimpleSubsideAssuranceMaladieService;
import ch.globaz.pegasus.businessimpl.checkers.assurancemaladie.SimpleSubsideAssuranceMaladieChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;

import java.util.List;

public class SimpleSubsideAssuranceMaladieServiceImpl extends PegasusAbstractServiceImpl implements SimpleSubsideAssuranceMaladieService {
    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.assurancemaladie. simpleSubsideAssuranceMaladie
     * #create(ch.globaz.pegasus.business.models.assurancemaladie .simpleSubsideAssuranceMaladie)
     */
    @Override
    public SimpleSubsideAssuranceMaladie create(SimpleSubsideAssuranceMaladie simpleSubsideAssuranceMaladie) throws JadePersistenceException,
            SubsideAssuranceMaladieException {
        if (simpleSubsideAssuranceMaladie == null) {
            throw new SubsideAssuranceMaladieException("Unable to create simpleSubsideAssuranceMaladie, the model passed is null!");
        }
        SimpleSubsideAssuranceMaladieChecker.checkForCreate(simpleSubsideAssuranceMaladie);
        return (SimpleSubsideAssuranceMaladie) JadePersistenceManager.add(simpleSubsideAssuranceMaladie);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.assurancemaladie. simpleSubsideAssuranceMaladie
     * #delete(ch.globaz.pegasus.business.models.assurancemaladie .simpleSubsideAssuranceMaladie)
     */
    @Override
    public SimpleSubsideAssuranceMaladie delete(SimpleSubsideAssuranceMaladie simpleSubsideAssuranceMaladie) throws SubsideAssuranceMaladieException,
            JadePersistenceException {
        if (simpleSubsideAssuranceMaladie == null) {
            throw new SubsideAssuranceMaladieException("Unable to delete simpleSubsideAssuranceMaladie, the model passed is null!");
        }
        if (simpleSubsideAssuranceMaladie.isNew()) {
            throw new SubsideAssuranceMaladieException("Unable to delete simpleSubsideAssuranceMaladie, the model passed is new!");
        }
        SimpleSubsideAssuranceMaladieChecker.checkForDelete(simpleSubsideAssuranceMaladie);
        return (SimpleSubsideAssuranceMaladie) JadePersistenceManager.delete(simpleSubsideAssuranceMaladie);
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleSubsideAssuranceMaladieSearch search = new SimpleSubsideAssuranceMaladieSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleSubsideAssuranceMaladie#read(java.lang.String)
     */
    @Override
    public SimpleSubsideAssuranceMaladie read(String idSubsideAssuranceMaladie) throws JadePersistenceException, SubsideAssuranceMaladieException {
        if (JadeStringUtil.isEmpty(idSubsideAssuranceMaladie)) {
            throw new SubsideAssuranceMaladieException("Unable to read simpleSubsideAssuranceMaladie, the id passed is not defined!");
        }
        SimpleSubsideAssuranceMaladie simpleSubsideAssuranceMaladie = new SimpleSubsideAssuranceMaladie();
        simpleSubsideAssuranceMaladie.setId(idSubsideAssuranceMaladie);
        return (SimpleSubsideAssuranceMaladie) JadePersistenceManager.read(simpleSubsideAssuranceMaladie);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.assurancemaladie. simpleSubsideAssuranceMaladieService
     * #update(ch.globaz.pegasus.business.models.assurancemaladie .simpleSubsideAssuranceMaladie)
     */
    @Override
    public SimpleSubsideAssuranceMaladie update(SimpleSubsideAssuranceMaladie simpleSubsideAssuranceMaladie) throws JadePersistenceException,
            SubsideAssuranceMaladieException {
        if (simpleSubsideAssuranceMaladie == null) {
            throw new SubsideAssuranceMaladieException("Unable to update simpleSubsideAssuranceMaladie, the model passed is null!");
        }
        if (simpleSubsideAssuranceMaladie.isNew()) {
            throw new SubsideAssuranceMaladieException("Unable to update simpleSubsideAssuranceMaladie, the model passed is new!");
        }
        SimpleSubsideAssuranceMaladieChecker.checkForUpdate(simpleSubsideAssuranceMaladie);
        return (SimpleSubsideAssuranceMaladie) JadePersistenceManager.update(simpleSubsideAssuranceMaladie);
    }

}
