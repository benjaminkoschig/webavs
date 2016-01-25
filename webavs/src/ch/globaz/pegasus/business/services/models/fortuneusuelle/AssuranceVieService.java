package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.AssuranceVieException;
import ch.globaz.pegasus.business.models.fortuneusuelle.AssuranceVie;
import ch.globaz.pegasus.business.models.fortuneusuelle.AssuranceVieSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface AssuranceVieService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws AssuranceVieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(AssuranceVieSearch search) throws AssuranceVieException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� AssuranceVie
     * 
     * @param AssuranceVie
     *            L'entit� AssuranceVie � cr�er
     * @return L'entit� AssuranceVie cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AssuranceVieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public AssuranceVie create(AssuranceVie assuranceVie) throws JadePersistenceException, AssuranceVieException,
            DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� AssuranceVie
     * 
     * @param AssuranceVie
     *            L'entit� AssuranceVie � supprimer
     * @return L'entit� AssuranceVie supprim�
     * @throws AssuranceVieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public AssuranceVie delete(AssuranceVie assuranceVie) throws AssuranceVieException, JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� AssuranceVie
     * 
     * @param idAssuranceVie
     *            L'identifiant de l'entit� AssuranceVie � charger en m�moire
     * @return L'entit� AssuranceVie charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AssuranceVieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public AssuranceVie read(String idAssuranceVie) throws JadePersistenceException, AssuranceVieException;

    /**
     * Chargement d'une AssuranceVie via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws AssuranceVieException
     * @throws JadePersistenceException
     */
    public AssuranceVie readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws AssuranceVieException,
            JadePersistenceException;

    /**
     * Permet de chercher des AssuranceVie selon un mod�le de crit�res.
     * 
     * @param AssuranceVieSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AssuranceVieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public AssuranceVieSearch search(AssuranceVieSearch assuranceVieSearch) throws JadePersistenceException,
            AssuranceVieException;

    /**
     * 
     * Permet la mise � jour d'une entit� AssuranceVie
     * 
     * @param AssuranceVie
     *            L'entit� AssuranceVie � mettre � jour
     * @return L'entit� AssuranceVie mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AssuranceVieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public AssuranceVie update(AssuranceVie assuranceVie) throws JadePersistenceException, AssuranceVieException,
            DonneeFinanciereException;
}