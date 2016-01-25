package ch.globaz.pegasus.businessimpl.services.models.fortuneparticuliere;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.AutreFortuneMobiliereException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AutreFortuneMobiliere;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AutreFortuneMobiliereSearch;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.AutreFortuneMobiliereService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * 
 * @author BSC
 * 
 */
public class AutreFortuneMobiliereServiceImpl extends PegasusAbstractServiceImpl implements
        AutreFortuneMobiliereService {
    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. AutreFortuneMobiliereService
     * #count(ch.globaz.pegasus.business.models.fortuneparticuliere .AutreFortuneMobiliereSearch)
     */
    @Override
    public int count(AutreFortuneMobiliereSearch search) throws AutreFortuneMobiliereException,
            JadePersistenceException {
        if (search == null) {
            throw new AutreFortuneMobiliereException(
                    "Unable to count autreFortuneMobiliere, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. AutreFortuneMobiliereService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere .AutreFortuneMobiliere)
     */
    @Override
    public AutreFortuneMobiliere create(AutreFortuneMobiliere autreFortuneMobiliere) throws JadePersistenceException,
            AutreFortuneMobiliereException, DonneeFinanciereException {
        if (autreFortuneMobiliere == null) {
            throw new AutreFortuneMobiliereException(
                    "Unable to create autreFortuneMobiliere, the model passed is null!");
        }

        try {
            autreFortuneMobiliere.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(
                            autreFortuneMobiliere.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimpleAutreFortuneMobiliereService().create(
                    autreFortuneMobiliere.getSimpleAutreFortuneMobiliere());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AutreFortuneMobiliereException("Service not available - " + e.getMessage());
        }

        return autreFortuneMobiliere;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. AutreFortuneMobiliereService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere .AutreFortuneMobiliere)
     */
    @Override
    public AutreFortuneMobiliere delete(AutreFortuneMobiliere autreFortuneMobiliere)
            throws AutreFortuneMobiliereException, JadePersistenceException {
        if (autreFortuneMobiliere == null) {
            throw new AutreFortuneMobiliereException(
                    "Unable to delete autreFortuneMobiliere, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleAutreFortuneMobiliereService().delete(
                    autreFortuneMobiliere.getSimpleAutreFortuneMobiliere());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AutreFortuneMobiliereException("Service not available - " + e.getMessage());
        }

        return autreFortuneMobiliere;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * AutreFortuneMobiliereService#read(java.lang.String)
     */
    @Override
    public AutreFortuneMobiliere read(String idAutreFortuneMobiliere) throws JadePersistenceException,
            AutreFortuneMobiliereException {
        if (JadeStringUtil.isEmpty(idAutreFortuneMobiliere)) {
            throw new AutreFortuneMobiliereException("Unable to read autreFortuneMobiliere, the id passed is null!");
        }
        AutreFortuneMobiliere autreFortuneMobiliere = new AutreFortuneMobiliere();
        autreFortuneMobiliere.setId(idAutreFortuneMobiliere);
        return (AutreFortuneMobiliere) JadePersistenceManager.read(autreFortuneMobiliere);
    }

    /**
     * Chargement d'un AutreFortuneMobiliere via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws AutreFortuneMobiliereException
     * @throws JadePersistenceException
     */
    @Override
    public AutreFortuneMobiliere readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws AutreFortuneMobiliereException, JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new AutreFortuneMobiliereException(
                    "Unable to find AutreFortuneMobiliere the idDonneeFinanciereHeader passed si null!");
        }

        AutreFortuneMobiliereSearch search = new AutreFortuneMobiliereSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (AutreFortuneMobiliereSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new AutreFortuneMobiliereException("More than one AutreFortuneMobiliere find, one was exepcted!");
        }

        return (AutreFortuneMobiliere) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((AutreFortuneMobiliereSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. AutreFortuneMobiliereService
     * #search(ch.globaz.pegasus.business.models.fortuneparticuliere .AutreFortuneMobiliereSearch)
     */
    @Override
    public AutreFortuneMobiliereSearch search(AutreFortuneMobiliereSearch autreFortuneMobiliereSearch)
            throws JadePersistenceException, AutreFortuneMobiliereException {
        if (autreFortuneMobiliereSearch == null) {
            throw new AutreFortuneMobiliereException(
                    "Unable to search autreFortuneMobiliere, the search model passed is null!");
        }
        return (AutreFortuneMobiliereSearch) JadePersistenceManager.search(autreFortuneMobiliereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. AutreFortuneMobiliereService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere .AutreFortuneMobiliere)
     */
    @Override
    public AutreFortuneMobiliere update(AutreFortuneMobiliere autreFortuneMobiliere) throws JadePersistenceException,
            AutreFortuneMobiliereException, DonneeFinanciereException {
        if (autreFortuneMobiliere == null) {
            throw new AutreFortuneMobiliereException(
                    "Unable to update autreFortuneMobiliere, the model passed is null!");
        }

        try {
            autreFortuneMobiliere.setSimpleAutreFortuneMobiliere(PegasusImplServiceLocator
                    .getSimpleAutreFortuneMobiliereService().update(
                            autreFortuneMobiliere.getSimpleAutreFortuneMobiliere()));
            autreFortuneMobiliere.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(
                            autreFortuneMobiliere.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AutreFortuneMobiliereException("Service not available - " + e.getMessage());
        }

        return autreFortuneMobiliere;
    }

}
