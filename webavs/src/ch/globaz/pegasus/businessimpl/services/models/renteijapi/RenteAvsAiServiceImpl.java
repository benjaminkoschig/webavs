package ch.globaz.pegasus.businessimpl.services.models.renteijapi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.RenteAvsAiException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.renteijapi.RenteAvsAi;
import ch.globaz.pegasus.business.models.renteijapi.RenteAvsAiSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.renteijapi.RenteAvsAiService;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class RenteAvsAiServiceImpl extends PegasusServiceLocator implements RenteAvsAiService {

    /**
     * Compte les entités en bd
     * 
     * @param serach
     *            le modèle de recherche
     * @throws RenteAvsAiException
     *             , JadePersistenceException
     * @return le nombre d'entité en bd
     */
    @Override
    public int count(RenteAvsAiSearch search) throws RenteAvsAiException, JadePersistenceException {
        if (search == null) {
            throw new RenteAvsAiException("Unable to count, the serach mdel passed is null!");
        }

        return JadePersistenceManager.count(search);
    }

    /**
     * Création d'une rente avs ai
     */
    @Override
    public RenteAvsAi create(RenteAvsAi renteAvsAi) throws RenteAvsAiException, DonneeFinanciereException,
            JadePersistenceException {
        if (renteAvsAi == null) {
            throw new RenteAvsAiException("Unable to create renteAvsAi, the model passed is null");
        }

        try {
            // Création donnefinancierHeader
            renteAvsAi.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(renteAvsAi.getSimpleDonneeFinanciereHeader()));
            // Creation simpleRenteAvsAi
            renteAvsAi.setSimpleRenteAvsAi(PegasusImplServiceLocator.getSimpleRenteAvsAiService().create(
                    renteAvsAi.getSimpleRenteAvsAi()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RenteAvsAiException("Service not available - " + e.getMessage());
        }
        return renteAvsAi;
    }

    /**
     * Suppression de la rente Avs Ai
     * 
     * @param renteAvsAi
     * @throws RenteAvsAiException
     *             , JadePersistenceException
     * @return RenteAvsAiException la rente supprimé
     */
    @Override
    public RenteAvsAi delete(RenteAvsAi renteAvsAi) throws RenteAvsAiException, JadePersistenceException {
        // check si pas null
        if (renteAvsAi == null) {
            throw new RenteAvsAiException("unable to delete renteavsAi, the model passed is null");
        }

        try {
            PegasusImplServiceLocator.getSimpleRenteAvsAiService().delete(renteAvsAi.getSimpleRenteAvsAi());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RenteAvsAiException("Service not available - " + e.getMessage());
        }
        return renteAvsAi;
    }

    /**
     * Chargement d'une rente avs ai en mémoire
     * 
     * @param idRenteAvsAi
     * @retun renteAvsAi la rente chargé
     * @throws RenteAvsAiException
     *             , JadePersistenceException
     */
    @Override
    public RenteAvsAi read(String idRenteAvsAi) throws RenteAvsAiException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idRenteAvsAi)) {
            throw new RenteAvsAiException("Unable to read the model, the id passed is null");
        }

        RenteAvsAi renteAvsAi = new RenteAvsAi();
        renteAvsAi.setId(idRenteAvsAi);
        return (RenteAvsAi) JadePersistenceManager.read(renteAvsAi);
    }

    /**
     * Chargement d'une rente avs ai via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws RenteAvsAiException
     * @throws JadePersistenceException
     */
    @Override
    public RenteAvsAi readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws RenteAvsAiException,
            JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new RenteAvsAiException("Unable to find RenteAvsAi the idDonneeFinanciereHeader passed si null!");
        }

        RenteAvsAiSearch search = new RenteAvsAiSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (RenteAvsAiSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new RenteAvsAiException("More than one RenteAvsAi find, one was exepcted!");
        }

        return (RenteAvsAi) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((RenteAvsAiSearch) donneeFinanciereSearch);
    }

    /**
     * Recherche en fonction des paramètres de recherche
     * 
     * @param crenteAvsAiSearch
     * @return search
     * @throws JadePersistenceException
     * @throws RenteAvsAiException
     * 
     */
    @Override
    public RenteAvsAiSearch search(RenteAvsAiSearch renteAvsAiSearch) throws JadePersistenceException,
            RenteAvsAiException {
        if (renteAvsAiSearch == null) {
            throw new RenteAvsAiException("Unable to search renteAvsAi, the search model passed is null!");
        }
        return (RenteAvsAiSearch) JadePersistenceManager.search(renteAvsAiSearch);
    }

    /**
     * Mise à jour d'une rente AvsAi
     * 
     * @param renteAvsAi
     * @throws RenteAvsAiException
     *             , JadePersistenceException
     * @return renteAvsAiException
     */
    @Override
    public RenteAvsAi update(RenteAvsAi renteAvsAi) throws RenteAvsAiException, DonneeFinanciereException,
            JadePersistenceException {
        if (renteAvsAi == null) {
            throw new RenteAvsAiException("Unable to update the renteAvsAi, the model passed is null");
        }

        try {
            // Mise à jour simpleDonneesFinancieresHeader
            renteAvsAi.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(renteAvsAi.getSimpleDonneeFinanciereHeader()));
            // Creation simpleIndemiteJournaliere
            renteAvsAi.setSimpleRenteAvsAi(PegasusImplServiceLocator.getSimpleRenteAvsAiService().update(
                    renteAvsAi.getSimpleRenteAvsAi()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RenteAvsAiException("Service not available - " + e.getMessage());
        }

        return renteAvsAi;
    }

}
