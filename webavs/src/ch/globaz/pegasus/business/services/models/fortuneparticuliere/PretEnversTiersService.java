/**
 * 
 */
package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.PretEnversTiersException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.PretEnversTiers;
import ch.globaz.pegasus.business.models.fortuneparticuliere.PretEnversTiersSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

/**
 * @author BSC
 * 
 */
public interface PretEnversTiersService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws PretEnversTiersException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(PretEnversTiersSearch search) throws PretEnversTiersException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� pretEnversTiers
     * 
     * @param pretEnversTiers
     *            L'entit� pretEnversTiers � cr�er
     * @return L'entit� pretEnversTiers cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PretEnversTiersException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public PretEnversTiers create(PretEnversTiers pretEnversTiers) throws JadePersistenceException,
            PretEnversTiersException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� pretEnversTiers
     * 
     * @param pretEnversTiers
     *            L'entit� pretEnversTiers � supprimer
     * @return L'entit� pretEnversTiers supprim�
     * @throws PretEnversTiersException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public PretEnversTiers delete(PretEnversTiers pretEnversTiers) throws PretEnversTiersException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� pretEnversTiers
     * 
     * @param idPretEnversTiers
     *            L'identifiant de l'entit� pretEnversTiers � charger en m�moire
     * @return L'entit� pretEnversTiers charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PretEnversTiersException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public PretEnversTiers read(String idPretEnversTiers) throws JadePersistenceException, PretEnversTiersException;

    /**
     * Chargement d'un PretEnversTiers via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws PretEnversTiersException
     * @throws JadePersistenceException
     */
    public PretEnversTiers readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws PretEnversTiersException, JadePersistenceException;

    /**
     * Permet de chercher des pretEnversTiers selon un mod�le de crit�res.
     * 
     * @param pretEnversTiersSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PretEnversTiersException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public PretEnversTiersSearch search(PretEnversTiersSearch pretEnversTiersSearch) throws JadePersistenceException,
            PretEnversTiersException;

    /**
     * 
     * Permet la mise � jour d'une entit� pretEnversTiers
     * 
     * @param pretEnversTiers
     *            L'entit� pretEnversTiers � mettre � jour
     * @return L'entit� pretEnversTiers mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PretEnversTiersException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public PretEnversTiers update(PretEnversTiers pretEnversTiers) throws JadePersistenceException,
            PretEnversTiersException, DonneeFinanciereException;
}
