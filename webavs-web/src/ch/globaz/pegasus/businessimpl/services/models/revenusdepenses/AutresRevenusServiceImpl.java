package ch.globaz.pegasus.businessimpl.services.models.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.AutresRevenusException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.revenusdepenses.AutresRevenus;
import ch.globaz.pegasus.business.models.revenusdepenses.AutresRevenusSearch;
import ch.globaz.pegasus.business.services.models.revenusdepenses.AutresRevenusService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class AutresRevenusServiceImpl extends PegasusAbstractServiceImpl implements AutresRevenusService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. AutresRevenusService
     * #count(ch.globaz.pegasus.business.models.revenusdepenses .AutresRevenusSearch)
     */
    @Override
    public int count(AutresRevenusSearch search) throws AutresRevenusException, JadePersistenceException {
        if (search == null) {
            throw new AutresRevenusException("Unable to count AutresRevenus, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. AutresRevenusService
     * #create(ch.globaz.pegasus.business.models.revenusdepenses .AutresRevenus)
     */
    @Override
    public AutresRevenus create(AutresRevenus autresRevenus) throws JadePersistenceException, AutresRevenusException,
            DonneeFinanciereException {
        if (autresRevenus == null) {
            throw new AutresRevenusException("Unable to create AutresRevenus, the model passed is null!");
        }

        try {
            autresRevenus.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(autresRevenus.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimpleAutresRevenusService().create(autresRevenus.getSimpleAutresRevenus());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AutresRevenusException("Service not available - " + e.getMessage());
        }

        return autresRevenus;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. AutresRevenusService
     * #delete(ch.globaz.pegasus.business.models.revenusdepenses .AutresRevenus)
     */
    @Override
    public AutresRevenus delete(AutresRevenus autresRevenus) throws AutresRevenusException, JadePersistenceException {
        if (autresRevenus == null) {
            throw new AutresRevenusException("Unable to delete AutresRevenus, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleAutresRevenusService().delete(autresRevenus.getSimpleAutresRevenus());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AutresRevenusException("Service not available - " + e.getMessage());
        }

        return autresRevenus;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. AutresRevenusService#read(java.lang.String)
     */
    @Override
    public AutresRevenus read(String idAutresRevenus) throws JadePersistenceException, AutresRevenusException {
        if (JadeStringUtil.isEmpty(idAutresRevenus)) {
            throw new AutresRevenusException("Unable to read AutresRevenus, the id passed is null!");
        }
        AutresRevenus AutresRevenus = new AutresRevenus();
        AutresRevenus.setId(idAutresRevenus);
        return (AutresRevenus) JadePersistenceManager.read(AutresRevenus);
    }

    /**
     * Chargement d'un AutresRevenus via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws AutresRevenusException
     * @throws JadePersistenceException
     */
    @Override
    public AutresRevenus readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws AutresRevenusException,
            JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new AutresRevenusException(
                    "Unable to find AutresRevenus the idDonneeFinanciereHeader passed si null!");
        }

        AutresRevenusSearch search = new AutresRevenusSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (AutresRevenusSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new AutresRevenusException("More than one AutresRevenus find, one was exepcted!");
        }

        return (AutresRevenus) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((AutresRevenusSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. AutresRevenusService
     * #search(ch.globaz.pegasus.business.models.revenusdepenses .AutresRevenusSearch)
     */
    @Override
    public AutresRevenusSearch search(AutresRevenusSearch autresRevenusSearch) throws JadePersistenceException,
            AutresRevenusException {
        if (autresRevenusSearch == null) {
            throw new AutresRevenusException("Unable to search AutresRevenus, the search model passed is null!");
        }
        return (AutresRevenusSearch) JadePersistenceManager.search(autresRevenusSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. AutresRevenusService
     * #update(ch.globaz.pegasus.business.models.revenusdepenses .AutresRevenus)
     */
    @Override
    public AutresRevenus update(AutresRevenus autresRevenus) throws JadePersistenceException, AutresRevenusException,
            DonneeFinanciereException {
        if (autresRevenus == null) {
            throw new AutresRevenusException("Unable to update AutresRevenus, the model passed is null!");
        }

        try {
            autresRevenus.setSimpleAutresRevenus(PegasusImplServiceLocator.getSimpleAutresRevenusService().update(
                    autresRevenus.getSimpleAutresRevenus()));
            autresRevenus.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(autresRevenus.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AutresRevenusException("Service not available - " + e.getMessage());
        }

        return autresRevenus;
    }

}
