package ch.globaz.pegasus.business.services.models.droit;

import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;

public interface AbstractDonneeFinanciereService {

    /**
     * Permet la recherche d'après les paramètres de recherche
     * 
     * @param renteAvsAiSearch
     * @return La recherche effectué
     * @throws LoyerException
     * @throws DonneeFinanciereException
     * @throws JadePersistenceException
     */

    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException;

}