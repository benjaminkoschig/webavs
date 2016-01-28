package ch.globaz.pegasus.businessimpl.services.models.renteijapi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.IndemniteJournaliereAiException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.RenteAvsAiException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.renteijapi.IndemniteJournaliereAi;
import ch.globaz.pegasus.business.models.renteijapi.IndemniteJournaliereAiSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.renteijapi.IndemniteJournaliereAiService;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class IndemniteJournaliereAiServiceImpl extends PegasusServiceLocator implements IndemniteJournaliereAiService {

    /**
     * Compte le nombre d'entité en fonction des paramètres de recherches
     */
    @Override
    public int count(IndemniteJournaliereAiSearch search) throws IndemniteJournaliereAiException,
            JadePersistenceException {
        if (search == null) {
            throw new IndemniteJournaliereAiException("Unable to count, the search mdel passed is null!");
        }

        return JadePersistenceManager.count(search);
    }

    /**
     * Création d'une indemnité jouurnaliereAi
     * 
     * @throws DonneeFinanciereException
     */
    @Override
    public IndemniteJournaliereAi create(IndemniteJournaliereAi indemniteJournaliereAi)
            throws IndemniteJournaliereAiException, JadePersistenceException, DonneeFinanciereException {

        if (indemniteJournaliereAi == null) {
            throw new IndemniteJournaliereAiException(
                    "Unable to create indemniteJournaliereAi, the model passed is null");
        }

        try {
            // Création simpleDonneesFinancieresHeader
            indemniteJournaliereAi.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(
                            indemniteJournaliereAi.getSimpleDonneeFinanciereHeader()));
            // Creation simpleIndemiteJournaliere
            indemniteJournaliereAi.setSimpleIndemniteJournaliereAi(PegasusImplServiceLocator
                    .getSimpleIndemniteJournaliereAiService().create(
                            indemniteJournaliereAi.getSimpleIndemniteJournaliereAi()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new IndemniteJournaliereAiException("Service not available - " + e.getMessage());
        }
        return indemniteJournaliereAi;
    }

    /**
     * Suppression de l'indemniteJOurnalierAi, Avoir avec update
     */
    @Override
    public IndemniteJournaliereAi delete(IndemniteJournaliereAi indemniteJournaliereAi)
            throws IndemniteJournaliereAiException, JadePersistenceException {

        // check si pas null
        if (indemniteJournaliereAi == null) {
            throw new IndemniteJournaliereAiException(
                    "unable to delete indemniteJournaliereAi, the model passed is null");
        }

        try {
            PegasusImplServiceLocator.getSimpleIndemniteJournaliereAiService().delete(
                    indemniteJournaliereAi.getSimpleIndemniteJournaliereAi());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new IndemniteJournaliereAiException("Service not available - " + e.getMessage());
        }
        return indemniteJournaliereAi;
    }

    /**
     * Lecture du modele complexe
     * 
     */
    @Override
    public IndemniteJournaliereAi read(String idIndemniteJournaliereAi) throws IndemniteJournaliereAiException,
            JadePersistenceException {

        if (JadeStringUtil.isEmpty(idIndemniteJournaliereAi)) {
            throw new IndemniteJournaliereAiException("Unable to read the model, the id passed is null");
        }
        IndemniteJournaliereAi indemniteJournaliereAi = new IndemniteJournaliereAi();
        indemniteJournaliereAi.setId(idIndemniteJournaliereAi);
        return (IndemniteJournaliereAi) JadePersistenceManager.read(indemniteJournaliereAi);

    }

    /**
     * Chargement d'une ijai via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws RenteAvsAiException
     * @throws JadePersistenceException
     */
    @Override
    public IndemniteJournaliereAi readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws IndemniteJournaliereAiException, JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new IndemniteJournaliereAiException(
                    "Unable to find IndeminieJournaliereAI the idDonneeFinanciereHeader passed si null!");
        }

        IndemniteJournaliereAiSearch search = new IndemniteJournaliereAiSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (IndemniteJournaliereAiSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new IndemniteJournaliereAiException("More than one IndeminieJournaliereAI find, one was expected!");
        }

        return (IndemniteJournaliereAi) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((IndemniteJournaliereAiSearch) donneeFinanciereSearch);
    }

    /**
     * Recherche en fonction des paramètres de recherche
     * 
     * @param indemniteJournaliereAiSearch
     * @return search
     * @throws JadePersistenceException
     * @throws IndemniteJournaliereException
     */
    @Override
    public IndemniteJournaliereAiSearch search(IndemniteJournaliereAiSearch indemniteJournaliereAiSearch)
            throws JadePersistenceException, IndemniteJournaliereAiException {
        if (indemniteJournaliereAiSearch == null) {
            throw new IndemniteJournaliereAiException(
                    "Unable to search indemniteJournaliere, the search model passed is null!");
        }
        return (IndemniteJournaliereAiSearch) JadePersistenceManager.search(indemniteJournaliereAiSearch);
    }

    /**
     * Mise à jour du modele complexe
     */
    @Override
    public IndemniteJournaliereAi update(IndemniteJournaliereAi indemniteJournaliereAi)
            throws IndemniteJournaliereAiException, DonneeFinanciereException, JadePersistenceException {

        if (indemniteJournaliereAi == null) {
            throw new IndemniteJournaliereAiException(
                    "Unable to update the indemniteJournalierAi, the model passed is null");
        }

        try {
            // Mise à jour simpleDonneesFinancieresHeader
            indemniteJournaliereAi.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(
                            indemniteJournaliereAi.getSimpleDonneeFinanciereHeader()));
            // Creation simpleIndemiteJournaliere
            indemniteJournaliereAi.setSimpleIndemniteJournaliereAi(PegasusImplServiceLocator
                    .getSimpleIndemniteJournaliereAiService().update(
                            indemniteJournaliereAi.getSimpleIndemniteJournaliereAi()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new IndemniteJournaliereAiException("Service not available - " + e.getMessage());
        }

        return indemniteJournaliereAi;
    }

}
