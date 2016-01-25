package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.CapitalLPPException;
import ch.globaz.pegasus.business.models.fortuneusuelle.CapitalLPP;
import ch.globaz.pegasus.business.models.fortuneusuelle.CapitalLPPSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface CapitalLPPService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws CapitalLPPException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(CapitalLPPSearch search) throws CapitalLPPException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� CapitalLPP
     * 
     * @param CapitalLPP
     *            L'entit� CapitalLPP � cr�er
     * @return L'entit� CapitalLPP cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws CapitalLPPException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public CapitalLPP create(CapitalLPP capitalLPP) throws JadePersistenceException, CapitalLPPException,
            DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� CapitalLPP
     * 
     * @param CapitalLPP
     *            L'entit� CapitalLPP � supprimer
     * @return L'entit� CapitalLPP supprim�
     * @throws CapitalLPPException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public CapitalLPP delete(CapitalLPP capitalLPP) throws CapitalLPPException, JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� CapitalLPP
     * 
     * @param idCapitalLPP
     *            L'identifiant de l'entit� CapitalLPP � charger en m�moire
     * @return L'entit� CapitalLPP charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws CapitalLPPException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public CapitalLPP read(String idCapitalLPP) throws JadePersistenceException, CapitalLPPException;

    /**
     * Chargement d'une CapitalLPP via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws CapitalLPPException
     * @throws JadePersistenceException
     */
    public CapitalLPP readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws CapitalLPPException,
            JadePersistenceException;

    /**
     * Permet de chercher des CapitalLPP selon un mod�le de crit�res.
     * 
     * @param CapitalLPPSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws CapitalLPPException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public CapitalLPPSearch search(CapitalLPPSearch capitalLPPSearch) throws JadePersistenceException,
            CapitalLPPException;

    /**
     * 
     * Permet la mise � jour d'une entit� CapitalLPP
     * 
     * @param CapitalLPP
     *            L'entit� CapitalLPP � mettre � jour
     * @return L'entit� CapitalLPP mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws CapitalLPPException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public CapitalLPP update(CapitalLPP capitalLPP) throws JadePersistenceException, CapitalLPPException,
            DonneeFinanciereException;
}