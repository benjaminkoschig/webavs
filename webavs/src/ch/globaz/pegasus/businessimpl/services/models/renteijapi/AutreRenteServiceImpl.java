package ch.globaz.pegasus.businessimpl.services.models.renteijapi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AutreRenteException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.renteijapi.AutreRente;
import ch.globaz.pegasus.business.models.renteijapi.AutreRenteSearch;
import ch.globaz.pegasus.business.services.models.renteijapi.AutreRenteService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * @author DMA
 * @date 24 juin 2010
 */
public class AutreRenteServiceImpl extends PegasusAbstractServiceImpl implements AutreRenteService {

    @Override
    public int count(AutreRenteSearch search) throws AutreRenteException, JadePersistenceException {
        if (search == null) {
            throw new AutreRenteException("Unable to count autreRenteException, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public AutreRente create(AutreRente autreRente) throws AutreRenteException, JadePersistenceException,
            DonneeFinanciereException {
        if (autreRente == null) {
            throw new AutreRenteException("Unable to create autreRente, the model passed is null!");
        }
        try {
            // creation du donneeFinanciereHeader
            autreRente.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(autreRente.getSimpleDonneeFinanciereHeader()));

            // creation du simpleAutreRente
            autreRente.setSimpleAutreRente(PegasusImplServiceLocator.getSimpleAutreRenteService().create(
                    autreRente.getSimpleAutreRente()));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AutreRenteException("Service not available - " + e.getMessage());
        }
        return autreRente;
    }

    @Override
    public AutreRente delete(AutreRente autreRente) throws AutreRenteException, JadePersistenceException,
            DonneeFinanciereException {
        if (autreRente == null) {
            throw new AutreRenteException("Unable to delete autreRente, the model passed is null!");
        }
        try {
            // effacement du donneeFinanciereHeader
            autreRente.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().delete(autreRente.getSimpleDonneeFinanciereHeader()));

            // effacement du simpleAureRente
            autreRente.setSimpleAutreRente(PegasusImplServiceLocator.getSimpleAutreRenteService().delete(
                    autreRente.getSimpleAutreRente()));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AutreRenteException("Service not available - " + e.getMessage());
        }
        return autreRente;
    }

    @Override
    public AutreRente read(String idAutreRente) throws AutreRenteException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idAutreRente)) {
            throw new AutreRenteException("Unable to read autreRente, the id passed is not defined!");
        }
        AutreRente autreRente = new AutreRente();
        autreRente.setId(idAutreRente);
        return (AutreRente) JadePersistenceManager.read(autreRente);
    }

    /**
     * Chargement d'une AutreRente via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws AutreRenteException
     * @throws JadePersistenceException
     */
    @Override
    public AutreRente readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws AutreRenteException,
            JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new AutreRenteException("Unable to find AutreRente the idDonneeFinanciereHeader passed si null!");
        }

        AutreRenteSearch search = new AutreRenteSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (AutreRenteSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new AutreRenteException("More than one AutreRente find, one was exepcted!");
        }

        return (AutreRente) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((AutreRenteSearch) donneeFinanciereSearch);
    }

    @Override
    public AutreRenteSearch search(AutreRenteSearch autreRenteSearch) throws JadePersistenceException,
            AutreRenteException {
        if (autreRenteSearch == null) {
            throw new AutreRenteException("Unable to search autreRenteSearch, the model passed is null!");
        }
        return (AutreRenteSearch) JadePersistenceManager.search(autreRenteSearch);
    }

    @Override
    public AutreRente update(AutreRente autreRente) throws AutreRenteException, JadePersistenceException,
            DonneeFinanciereException {
        if (autreRente == null) {
            throw new AutreRenteException("Unable to update autreRente, the model passed is null!");
        }
        try {
            // mise a jour du donneeFinanciereHeader
            autreRente.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(autreRente.getSimpleDonneeFinanciereHeader()));
            // mise a jour du simpleAureRente
            autreRente.setSimpleAutreRente(PegasusImplServiceLocator.getSimpleAutreRenteService().update(
                    autreRente.getSimpleAutreRente()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AutreRenteException("Service not available - " + e.getMessage());
        }
        return autreRente;
    }

}
