package ch.globaz.pegasus.businessimpl.services.models.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.CotisationsPsalException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.revenusdepenses.CotisationsPsal;
import ch.globaz.pegasus.business.models.revenusdepenses.CotisationsPsalSearch;
import ch.globaz.pegasus.business.services.models.revenusdepenses.CotisationsPsalService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class CotisationsPsalServiceImpl extends PegasusAbstractServiceImpl implements CotisationsPsalService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. CotisationsPsalService
     * #count(ch.globaz.pegasus.business.models.revenusdepenses .CotisationsPsalSearch)
     */
    @Override
    public int count(CotisationsPsalSearch search) throws CotisationsPsalException, JadePersistenceException {
        if (search == null) {
            throw new CotisationsPsalException("Unable to count CotisationsPsal, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. CotisationsPsalService
     * #create(ch.globaz.pegasus.business.models.revenusdepenses .CotisationsPsal)
     */
    @Override
    public CotisationsPsal create(CotisationsPsal cotisationsPsal) throws JadePersistenceException,
            CotisationsPsalException, DonneeFinanciereException {
        if (cotisationsPsal == null) {
            throw new CotisationsPsalException("Unable to create CotisationsPsal, the model passed is null!");
        }

        try {
            cotisationsPsal
                    .setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                            .create(cotisationsPsal.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimpleCotisationsPsalService().create(
                    cotisationsPsal.getSimpleCotisationsPsal());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CotisationsPsalException("Service not available - " + e.getMessage());
        }

        return cotisationsPsal;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. CotisationsPsalService
     * #delete(ch.globaz.pegasus.business.models.revenusdepenses .CotisationsPsal)
     */
    @Override
    public CotisationsPsal delete(CotisationsPsal cotisationsPsal) throws CotisationsPsalException,
            JadePersistenceException {
        if (cotisationsPsal == null) {
            throw new CotisationsPsalException("Unable to delete CotisationsPsal, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleCotisationsPsalService().delete(
                    cotisationsPsal.getSimpleCotisationsPsal());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CotisationsPsalException("Service not available - " + e.getMessage());
        }

        return cotisationsPsal;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. CotisationsPsalService#read(java.lang.String)
     */
    @Override
    public CotisationsPsal read(String idCotisationsPsal) throws JadePersistenceException, CotisationsPsalException {
        if (JadeStringUtil.isEmpty(idCotisationsPsal)) {
            throw new CotisationsPsalException("Unable to read CotisationsPsal, the id passed is null!");
        }
        CotisationsPsal CotisationsPsal = new CotisationsPsal();
        CotisationsPsal.setId(idCotisationsPsal);
        return (CotisationsPsal) JadePersistenceManager.read(CotisationsPsal);
    }

    /**
     * Chargement d'un CotisationsPsal via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws CotisationsPsalException
     * @throws JadePersistenceException
     */
    @Override
    public CotisationsPsal readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws CotisationsPsalException, JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new CotisationsPsalException(
                    "Unable to find CotisationsPsal the idDonneeFinanciereHeader passed si null!");
        }

        CotisationsPsalSearch search = new CotisationsPsalSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (CotisationsPsalSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new CotisationsPsalException("More than one CotisationsPsal find, one was exepcted!");
        }

        return (CotisationsPsal) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((CotisationsPsalSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. CotisationsPsalService
     * #search(ch.globaz.pegasus.business.models.revenusdepenses .CotisationsPsalSearch)
     */
    @Override
    public CotisationsPsalSearch search(CotisationsPsalSearch cotisationsPsalSearch) throws JadePersistenceException,
            CotisationsPsalException {
        if (cotisationsPsalSearch == null) {
            throw new CotisationsPsalException("Unable to search CotisationsPsal, the search model passed is null!");
        }
        return (CotisationsPsalSearch) JadePersistenceManager.search(cotisationsPsalSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. CotisationsPsalService
     * #update(ch.globaz.pegasus.business.models.revenusdepenses .CotisationsPsal)
     */
    @Override
    public CotisationsPsal update(CotisationsPsal cotisationsPsal) throws JadePersistenceException,
            CotisationsPsalException, DonneeFinanciereException {
        if (cotisationsPsal == null) {
            throw new CotisationsPsalException("Unable to update CotisationsPsal, the model passed is null!");
        }

        try {
            cotisationsPsal.setSimpleCotisationsPsal(PegasusImplServiceLocator.getSimpleCotisationsPsalService()
                    .update(cotisationsPsal.getSimpleCotisationsPsal()));
            cotisationsPsal
                    .setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                            .update(cotisationsPsal.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CotisationsPsalException("Service not available - " + e.getMessage());
        }

        return cotisationsPsal;
    }

}
