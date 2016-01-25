/**
 * 
 */
package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.AutreFortuneMobiliereException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AutreFortuneMobiliere;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AutreFortuneMobiliereSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

/**
 * @author BSC
 * 
 */
public interface AutreFortuneMobiliereService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws AutreFortuneMobiliereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(AutreFortuneMobiliereSearch search) throws AutreFortuneMobiliereException,
            JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� autreFortuneMobiliere
     * 
     * @param autreFortuneMobiliere
     *            L'entit� autreFortuneMobiliere � cr�er
     * @return L'entit� autreFortuneMobiliere cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutreFortuneMobiliereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public AutreFortuneMobiliere create(AutreFortuneMobiliere autreFortuneMobiliere) throws JadePersistenceException,
            AutreFortuneMobiliereException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� autreFortuneMobiliere
     * 
     * @param autreFortuneMobiliere
     *            L'entit� autreFortuneMobiliere � supprimer
     * @return L'entit� autreFortuneMobiliere supprim�
     * @throws AutreFortuneMobiliereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public AutreFortuneMobiliere delete(AutreFortuneMobiliere autreFortuneMobiliere)
            throws AutreFortuneMobiliereException, JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� autreFortuneMobiliere
     * 
     * @param idAutreFortuneMobiliere
     *            L'identifiant de l'entit� autreFortuneMobiliere � charger en m�moire
     * @return L'entit� autreFortuneMobiliere charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutreFortuneMobiliereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public AutreFortuneMobiliere read(String idAutreFortuneMobiliere) throws JadePersistenceException,
            AutreFortuneMobiliereException;

    /**
     * Chargement d'un AutreFortuneMobiliere via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws AutreFortuneMobiliereException
     * @throws JadePersistenceException
     */
    public AutreFortuneMobiliere readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws AutreFortuneMobiliereException, JadePersistenceException;

    /**
     * Permet de chercher des autreFortuneMobiliere selon un mod�le de crit�res.
     * 
     * @param autreFortuneMobiliereSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutreFortuneMobiliereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public AutreFortuneMobiliereSearch search(AutreFortuneMobiliereSearch autreFortuneMobiliereSearch)
            throws JadePersistenceException, AutreFortuneMobiliereException;

    /**
     * 
     * Permet la mise � jour d'une entit� autreFortuneMobiliere
     * 
     * @param autreFortuneMobiliere
     *            L'entit� autreFortuneMobiliere � mettre � jour
     * @return L'entit� autreFortuneMobiliere mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutreFortuneMobiliereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public AutreFortuneMobiliere update(AutreFortuneMobiliere autreFortuneMobiliere) throws JadePersistenceException,
            AutreFortuneMobiliereException, DonneeFinanciereException;
}
