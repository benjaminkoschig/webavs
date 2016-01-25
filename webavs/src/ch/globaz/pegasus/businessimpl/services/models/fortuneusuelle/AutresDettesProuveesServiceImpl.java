package ch.globaz.pegasus.businessimpl.services.models.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.AutresDettesProuveesException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.fortuneusuelle.AutresDettesProuvees;
import ch.globaz.pegasus.business.models.fortuneusuelle.AutresDettesProuveesSearch;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.AutresDettesProuveesService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class AutresDettesProuveesServiceImpl extends PegasusAbstractServiceImpl implements AutresDettesProuveesService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. AutresDettesProuveesService
     * #count(ch.globaz.pegasus.business.models.revenusdepenses .AutresDettesProuveesSearch)
     */
    @Override
    public int count(AutresDettesProuveesSearch search) throws AutresDettesProuveesException, JadePersistenceException {
        if (search == null) {
            throw new AutresDettesProuveesException(
                    "Unable to count AutresDettesProuvees, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. AutresDettesProuveesService
     * #create(ch.globaz.pegasus.business.models.revenusdepenses .AutresDettesProuvees)
     */
    @Override
    public AutresDettesProuvees create(AutresDettesProuvees autresDettesProuvees) throws JadePersistenceException,
            AutresDettesProuveesException, DonneeFinanciereException {
        if (autresDettesProuvees == null) {
            throw new AutresDettesProuveesException("Unable to create AutresDettesProuvees, the model passed is null!");
        }

        try {
            autresDettesProuvees.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(
                            autresDettesProuvees.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimpleAutresDettesProuveesService().create(
                    autresDettesProuvees.getSimpleAutresDettesProuvees());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AutresDettesProuveesException("Service not available - " + e.getMessage());
        }

        return autresDettesProuvees;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. AutresDettesProuveesService
     * #delete(ch.globaz.pegasus.business.models.revenusdepenses .AutresDettesProuvees)
     */
    @Override
    public AutresDettesProuvees delete(AutresDettesProuvees autresDettesProuvees) throws AutresDettesProuveesException,
            JadePersistenceException {
        if (autresDettesProuvees == null) {
            throw new AutresDettesProuveesException("Unable to delete AutresDettesProuvees, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleAutresDettesProuveesService().delete(
                    autresDettesProuvees.getSimpleAutresDettesProuvees());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AutresDettesProuveesException("Service not available - " + e.getMessage());
        }

        return autresDettesProuvees;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses.
     * AutresDettesProuveesService#read(java.lang.String)
     */
    @Override
    public AutresDettesProuvees read(String idAutresDettesProuvees) throws JadePersistenceException,
            AutresDettesProuveesException {
        if (JadeStringUtil.isEmpty(idAutresDettesProuvees)) {
            throw new AutresDettesProuveesException("Unable to read AutresDettesProuvees, the id passed is null!");
        }
        AutresDettesProuvees AutresDettesProuvees = new AutresDettesProuvees();
        AutresDettesProuvees.setId(idAutresDettesProuvees);
        return (AutresDettesProuvees) JadePersistenceManager.read(AutresDettesProuvees);
    }

    /**
     * Chargement d'une AutresDettesProuvees via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws AutresDettesProuveesException
     * @throws JadePersistenceException
     */
    @Override
    public AutresDettesProuvees readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws AutresDettesProuveesException, JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new AutresDettesProuveesException(
                    "Unable to find AutresDettesProuvees the idDonneeFinanciereHeader passed si null!");
        }

        AutresDettesProuveesSearch search = new AutresDettesProuveesSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (AutresDettesProuveesSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new AutresDettesProuveesException("More than one AutresDettesProuvees find, one was exepcted!");
        }

        return (AutresDettesProuvees) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((AutresDettesProuveesSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. AutresDettesProuveesService
     * #search(ch.globaz.pegasus.business.models.revenusdepenses .AutresDettesProuveesSearch)
     */
    @Override
    public AutresDettesProuveesSearch search(AutresDettesProuveesSearch autresDettesProuveesSearch)
            throws JadePersistenceException, AutresDettesProuveesException {
        if (autresDettesProuveesSearch == null) {
            throw new AutresDettesProuveesException(
                    "Unable to search AutresDettesProuvees, the search model passed is null!");
        }
        return (AutresDettesProuveesSearch) JadePersistenceManager.search(autresDettesProuveesSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. AutresDettesProuveesService
     * #update(ch.globaz.pegasus.business.models.revenusdepenses .AutresDettesProuvees)
     */
    @Override
    public AutresDettesProuvees update(AutresDettesProuvees autresDettesProuvees) throws JadePersistenceException,
            AutresDettesProuveesException, DonneeFinanciereException {
        if (autresDettesProuvees == null) {
            throw new AutresDettesProuveesException("Unable to update AutresDettesProuvees, the model passed is null!");
        }

        try {
            autresDettesProuvees.setSimpleAutresDettesProuvees(PegasusImplServiceLocator
                    .getSimpleAutresDettesProuveesService()
                    .update(autresDettesProuvees.getSimpleAutresDettesProuvees()));
            autresDettesProuvees.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(
                            autresDettesProuvees.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AutresDettesProuveesException("Service not available - " + e.getMessage());
        }

        return autresDettesProuvees;
    }

}
