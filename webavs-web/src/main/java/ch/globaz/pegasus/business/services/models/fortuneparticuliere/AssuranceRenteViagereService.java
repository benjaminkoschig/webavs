/**
 * 
 */
package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.AssuranceRenteViagereException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AssuranceRenteViagere;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AssuranceRenteViagereSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

/**
 * @author BSC
 * 
 */
public interface AssuranceRenteViagereService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws AssuranceRenteViagereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(AssuranceRenteViagereSearch search) throws AssuranceRenteViagereException,
            JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� assuranceRenteViagere
     * 
     * @param assuranceRenteViagere
     *            L'entit� assuranceRenteViagere � cr�er
     * @return L'entit� assuranceRenteViagere cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AssuranceRenteViagereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public AssuranceRenteViagere create(AssuranceRenteViagere assuranceRenteViagere) throws JadePersistenceException,
            AssuranceRenteViagereException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� assuranceRenteViagere
     * 
     * @param assuranceRenteViagere
     *            L'entit� assuranceRenteViagere � supprimer
     * @return L'entit� assuranceRenteViagere supprim�
     * @throws AssuranceRenteViagereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public AssuranceRenteViagere delete(AssuranceRenteViagere assuranceRenteViagere)
            throws AssuranceRenteViagereException, JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� assuranceRenteViagere
     * 
     * @param idAssuranceRenteViagere
     *            L'identifiant de l'entit� assuranceRenteViagere � charger en m�moire
     * @return L'entit� assuranceRenteViagere charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AssuranceRenteViagereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public AssuranceRenteViagere read(String idAssuranceRenteViagere) throws JadePersistenceException,
            AssuranceRenteViagereException;

    /**
     * Chargement d'un AssuranceRenteViagere via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws AssuranceRenteViagereException
     * @throws JadePersistenceException
     */
    public AssuranceRenteViagere readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws AssuranceRenteViagereException, JadePersistenceException;

    /**
     * Permet de chercher des assuranceRenteViagere selon un mod�le de crit�res.
     * 
     * @param assuranceRenteViagereSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AssuranceRenteViagereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public AssuranceRenteViagereSearch search(AssuranceRenteViagereSearch assuranceRenteViagereSearch)
            throws JadePersistenceException, AssuranceRenteViagereException;

    /**
     * 
     * Permet la mise � jour d'une entit� assuranceRenteViagere
     * 
     * @param assuranceRenteViagere
     *            L'entit� assuranceRenteViagere � mettre � jour
     * @return L'entit� assuranceRenteViagere mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AssuranceRenteViagereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public AssuranceRenteViagere update(AssuranceRenteViagere assuranceRenteViagere) throws JadePersistenceException,
            AssuranceRenteViagereException, DonneeFinanciereException;
}
