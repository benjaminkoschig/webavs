package ch.globaz.pegasus.businessimpl.services.models.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.AssuranceVieException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.fortuneusuelle.AssuranceVie;
import ch.globaz.pegasus.business.models.fortuneusuelle.AssuranceVieSearch;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.AssuranceVieService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class AssuranceVieServiceImpl extends PegasusAbstractServiceImpl implements AssuranceVieService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. AssuranceVieService
     * #count(ch.globaz.pegasus.business.models.revenusdepenses .AssuranceVieSearch)
     */
    @Override
    public int count(AssuranceVieSearch search) throws AssuranceVieException, JadePersistenceException {
        if (search == null) {
            throw new AssuranceVieException("Unable to count AssuranceVie, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. AssuranceVieService
     * #create(ch.globaz.pegasus.business.models.revenusdepenses .AssuranceVie)
     */
    @Override
    public AssuranceVie create(AssuranceVie assuranceVie) throws JadePersistenceException, AssuranceVieException,
            DonneeFinanciereException {
        if (assuranceVie == null) {
            throw new AssuranceVieException("Unable to create AssuranceVie, the model passed is null!");
        }

        try {
            assuranceVie.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(assuranceVie.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimpleAssuranceVieService().create(assuranceVie.getSimpleAssuranceVie());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AssuranceVieException("Service not available - " + e.getMessage());
        }

        return assuranceVie;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. AssuranceVieService
     * #delete(ch.globaz.pegasus.business.models.revenusdepenses .AssuranceVie)
     */
    @Override
    public AssuranceVie delete(AssuranceVie assuranceVie) throws AssuranceVieException, JadePersistenceException {
        if (assuranceVie == null) {
            throw new AssuranceVieException("Unable to delete AssuranceVie, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleAssuranceVieService().delete(assuranceVie.getSimpleAssuranceVie());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AssuranceVieException("Service not available - " + e.getMessage());
        }

        return assuranceVie;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. AssuranceVieService#read(java.lang.String)
     */
    @Override
    public AssuranceVie read(String idAssuranceVie) throws JadePersistenceException, AssuranceVieException {
        if (JadeStringUtil.isEmpty(idAssuranceVie)) {
            throw new AssuranceVieException("Unable to read AssuranceVie, the id passed is null!");
        }
        AssuranceVie AssuranceVie = new AssuranceVie();
        AssuranceVie.setId(idAssuranceVie);
        return (AssuranceVie) JadePersistenceManager.read(AssuranceVie);
    }

    /**
     * Chargement d'une AssuranceVie via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws AssuranceVieException
     * @throws JadePersistenceException
     */
    @Override
    public AssuranceVie readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws AssuranceVieException,
            JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new AssuranceVieException("Unable to find AssuranceVie the idDonneeFinanciereHeader passed si null!");
        }

        AssuranceVieSearch search = new AssuranceVieSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (AssuranceVieSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new AssuranceVieException("More than one AssuranceVie find, one was exepcted!");
        }

        return (AssuranceVie) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((AssuranceVieSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. AssuranceVieService
     * #search(ch.globaz.pegasus.business.models.revenusdepenses .AssuranceVieSearch)
     */
    @Override
    public AssuranceVieSearch search(AssuranceVieSearch assuranceVieSearch) throws JadePersistenceException,
            AssuranceVieException {
        if (assuranceVieSearch == null) {
            throw new AssuranceVieException("Unable to search AssuranceVie, the search model passed is null!");
        }
        return (AssuranceVieSearch) JadePersistenceManager.search(assuranceVieSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. AssuranceVieService
     * #update(ch.globaz.pegasus.business.models.revenusdepenses .AssuranceVie)
     */
    @Override
    public AssuranceVie update(AssuranceVie assuranceVie) throws JadePersistenceException, AssuranceVieException,
            DonneeFinanciereException {
        if (assuranceVie == null) {
            throw new AssuranceVieException("Unable to update AssuranceVie, the model passed is null!");
        }

        try {
            assuranceVie.setSimpleAssuranceVie(PegasusImplServiceLocator.getSimpleAssuranceVieService().update(
                    assuranceVie.getSimpleAssuranceVie()));
            assuranceVie.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(assuranceVie.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AssuranceVieException("Service not available - " + e.getMessage());
        }

        return assuranceVie;
    }

}
