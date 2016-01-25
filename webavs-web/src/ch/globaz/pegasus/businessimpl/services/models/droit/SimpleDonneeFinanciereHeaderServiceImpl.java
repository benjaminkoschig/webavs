package ch.globaz.pegasus.businessimpl.services.models.droit;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.constantes.DroitsSaveAction;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeaderSearch;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.services.models.droit.SimpleDonneeFinanciereHeaderService;
import ch.globaz.pegasus.businessimpl.checkers.droit.SimpleDonneeFinanciereHeaderChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleDonneeFinanciereHeaderServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleDonneeFinanciereHeaderService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.droit.DroitMembreFamilleService
     * #count(ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch)
     */
    @Override
    public int count(SimpleDonneeFinanciereHeaderSearch search) throws DroitException, JadePersistenceException {
        if (search == null) {
            throw new DroitException("Unable to count donneFinanciereHeader, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.droit. SimpleDonneeFinanciereHeaderService
     * #create(ch.globaz.pegasus.business.models .droit.SimpleDonneeFinanciereHeader)
     */
    @Override
    public SimpleDonneeFinanciereHeader create(SimpleDonneeFinanciereHeader donneeFinanciereHeader)
            throws DonneeFinanciereException, JadePersistenceException {
        if (donneeFinanciereHeader == null) {
            throw new DonneeFinanciereException("Unable to create donneeFinanciereHeader, the model passed is null!");
        }
        SimpleDonneeFinanciereHeaderChecker.checkForCreate(donneeFinanciereHeader);
        return (SimpleDonneeFinanciereHeader) JadePersistenceManager.add(donneeFinanciereHeader);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.droit. SimpleDonneeFinanciereHeaderService
     * #delete(ch.globaz.pegasus.business.models .droit.SimpleDonneeFinanciereHeader)
     */
    @Override
    public SimpleDonneeFinanciereHeader delete(SimpleDonneeFinanciereHeader donneeFinanciereHeader)
            throws DonneeFinanciereException, JadePersistenceException {
        if (donneeFinanciereHeader == null) {
            throw new DonneeFinanciereException("Unable to delete donneeFinanciereHeader, the model passed is null!");
        }
        if (donneeFinanciereHeader.isNew()) {
            throw new DonneeFinanciereException("Unable to delete donneeFinanciereHeader, the model passed is new!");
        }
        SimpleDonneeFinanciereHeaderChecker.checkForDelete(donneeFinanciereHeader);
        return (SimpleDonneeFinanciereHeader) JadePersistenceManager.delete(donneeFinanciereHeader);
    }

    @Override
    public SimpleDonneeFinanciereHeaderSearch delete(SimpleDonneeFinanciereHeaderSearch search)
            throws DonneeFinanciereException, JadePersistenceException {
        // supprime les données finanieres header
        JadePersistenceManager.delete(search);
        return search;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.droit. SimpleDonneeFinanciereHeaderService
     * #getSaveAction(ch.globaz.pegasus.business .models.droit.SimpleDonneeFinanciereHeader,
     * ch.globaz.pegasus.business.models.droit.SimpleVersionDroit)
     */
    @Override
    public Integer getSaveAction(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader,
            SimpleVersionDroit simpleVersionDroit) throws DonneeFinanciereException {
        if (simpleDonneeFinanciereHeader == null) {
            throw new DonneeFinanciereException(
                    "Unable to get saveAction, the passed simpleDonneeFinanciereHeader is null!");
        }
        if (simpleVersionDroit == null) {
            throw new DonneeFinanciereException("Unable to get saveAction, the passed simpleVersionDroit is null!");
        }

        return simpleDonneeFinanciereHeader.getIdVersionDroit().equals(simpleVersionDroit.getIdVersionDroit()) ? DroitsSaveAction.UPDATE
                : DroitsSaveAction.CREATE;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.droit. SimpleDonneeFinanciereHeaderService#read(java.lang.String)
     */
    @Override
    public SimpleDonneeFinanciereHeader read(String idDonneeFinanciereHeader) throws DonneeFinanciereException,
            JadePersistenceException {
        if (JadeStringUtil.isEmpty(idDonneeFinanciereHeader)) {
            throw new DonneeFinanciereException("Unable to read donneeFinanciereHeader, the id passed is not defined!");
        }
        SimpleDonneeFinanciereHeader donneeFinanciereHeader = new SimpleDonneeFinanciereHeader();
        donneeFinanciereHeader.setId(idDonneeFinanciereHeader);
        return (SimpleDonneeFinanciereHeader) JadePersistenceManager.read(donneeFinanciereHeader);
    }

    @Override
    public SimpleDonneeFinanciereHeaderSearch search(
            SimpleDonneeFinanciereHeaderSearch simpleDonneeFinanciereHeaderSearch) throws JadePersistenceException,
            DonneeFinanciereException {
        if (simpleDonneeFinanciereHeaderSearch == null) {
            throw new DonneeFinanciereException("Unable to search pretEnversTiers, the search model passed is null!");
        }
        return (SimpleDonneeFinanciereHeaderSearch) JadePersistenceManager.search(simpleDonneeFinanciereHeaderSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.droit. SimpleDonneeFinanciereHeaderService
     * #update(ch.globaz.pegasus.business.models .droit.SimpleDonneeFinanciereHeader)
     */
    @Override
    public SimpleDonneeFinanciereHeader update(SimpleDonneeFinanciereHeader donneeFinanciereHeader)
            throws DonneeFinanciereException, JadePersistenceException {
        if (donneeFinanciereHeader == null) {
            throw new DonneeFinanciereException("Unable to update donneeFinanciereHeader, the model passed is null!");
        }
        if (donneeFinanciereHeader.isNew()) {
            throw new DonneeFinanciereException("Unable to update donneeFinanciereHeader, the model passed is new!");
        }
        SimpleDonneeFinanciereHeaderChecker.checkForUpdate(donneeFinanciereHeader);
        return (SimpleDonneeFinanciereHeader) JadePersistenceManager.update(donneeFinanciereHeader);
    }

}
