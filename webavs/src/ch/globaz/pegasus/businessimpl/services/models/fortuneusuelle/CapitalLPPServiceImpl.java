package ch.globaz.pegasus.businessimpl.services.models.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.CapitalLPPException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.fortuneusuelle.CapitalLPP;
import ch.globaz.pegasus.business.models.fortuneusuelle.CapitalLPPSearch;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.CapitalLPPService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class CapitalLPPServiceImpl extends PegasusAbstractServiceImpl implements CapitalLPPService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. CapitalLPPService
     * #count(ch.globaz.pegasus.business.models.revenusdepenses .CapitalLPPSearch)
     */
    @Override
    public int count(CapitalLPPSearch search) throws CapitalLPPException, JadePersistenceException {
        if (search == null) {
            throw new CapitalLPPException("Unable to count CapitalLPP, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. CapitalLPPService
     * #create(ch.globaz.pegasus.business.models.revenusdepenses .CapitalLPP)
     */
    @Override
    public CapitalLPP create(CapitalLPP capitalLPP) throws JadePersistenceException, CapitalLPPException,
            DonneeFinanciereException {
        if (capitalLPP == null) {
            throw new CapitalLPPException("Unable to create CapitalLPP, the model passed is null!");
        }

        try {
            capitalLPP.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(capitalLPP.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimpleCapitalLPPService().create(capitalLPP.getSimpleCapitalLPP());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CapitalLPPException("Service not available - " + e.getMessage());
        }

        return capitalLPP;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. CapitalLPPService
     * #delete(ch.globaz.pegasus.business.models.revenusdepenses .CapitalLPP)
     */
    @Override
    public CapitalLPP delete(CapitalLPP capitalLPP) throws CapitalLPPException, JadePersistenceException {
        if (capitalLPP == null) {
            throw new CapitalLPPException("Unable to delete CapitalLPP, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleCapitalLPPService().delete(capitalLPP.getSimpleCapitalLPP());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CapitalLPPException("Service not available - " + e.getMessage());
        }

        return capitalLPP;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. CapitalLPPService#read(java.lang.String)
     */
    @Override
    public CapitalLPP read(String idCapitalLPP) throws JadePersistenceException, CapitalLPPException {
        if (JadeStringUtil.isEmpty(idCapitalLPP)) {
            throw new CapitalLPPException("Unable to read CapitalLPP, the id passed is null!");
        }
        CapitalLPP CapitalLPP = new CapitalLPP();
        CapitalLPP.setId(idCapitalLPP);
        return (CapitalLPP) JadePersistenceManager.read(CapitalLPP);
    }

    /**
     * Chargement d'une CapitalLPP via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws CapitalLPPException
     * @throws JadePersistenceException
     */
    @Override
    public CapitalLPP readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws CapitalLPPException,
            JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new CapitalLPPException("Unable to find CapitalLPP the idDonneeFinanciereHeader passed si null!");
        }

        CapitalLPPSearch search = new CapitalLPPSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (CapitalLPPSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new CapitalLPPException("More than one CapitalLPP find, one was exepcted!");
        }

        return (CapitalLPP) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((CapitalLPPSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. CapitalLPPService
     * #search(ch.globaz.pegasus.business.models.revenusdepenses .CapitalLPPSearch)
     */
    @Override
    public CapitalLPPSearch search(CapitalLPPSearch capitalLPPSearch) throws JadePersistenceException,
            CapitalLPPException {
        if (capitalLPPSearch == null) {
            throw new CapitalLPPException("Unable to search CapitalLPP, the search model passed is null!");
        }
        return (CapitalLPPSearch) JadePersistenceManager.search(capitalLPPSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. CapitalLPPService
     * #update(ch.globaz.pegasus.business.models.revenusdepenses .CapitalLPP)
     */
    @Override
    public CapitalLPP update(CapitalLPP capitalLPP) throws JadePersistenceException, CapitalLPPException,
            DonneeFinanciereException {
        if (capitalLPP == null) {
            throw new CapitalLPPException("Unable to update CapitalLPP, the model passed is null!");
        }

        try {
            capitalLPP.setSimpleCapitalLPP(PegasusImplServiceLocator.getSimpleCapitalLPPService().update(
                    capitalLPP.getSimpleCapitalLPP()));
            capitalLPP.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(capitalLPP.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CapitalLPPException("Service not available - " + e.getMessage());
        }

        return capitalLPP;
    }

}
