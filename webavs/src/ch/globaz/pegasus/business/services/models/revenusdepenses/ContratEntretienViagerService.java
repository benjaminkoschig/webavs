package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.ContratEntretienViagerException;
import ch.globaz.pegasus.business.models.revenusdepenses.ContratEntretienViager;
import ch.globaz.pegasus.business.models.revenusdepenses.ContratEntretienViagerSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface ContratEntretienViagerService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws ContratEntretienViagerException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(ContratEntretienViagerSearch search) throws ContratEntretienViagerException,
            JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� ContratEntretienViager
     * 
     * @param ContratEntretienViager
     *            L'entit� ContratEntretienViager � cr�er
     * @return L'entit� ContratEntretienViager cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ContratEntretienViagerException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public ContratEntretienViager create(ContratEntretienViager contratEntretienViager)
            throws JadePersistenceException, ContratEntretienViagerException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� ContratEntretienViager
     * 
     * @param ContratEntretienViager
     *            L'entit� ContratEntretienViager � supprimer
     * @return L'entit� ContratEntretienViager supprim�
     * @throws ContratEntretienViagerException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public ContratEntretienViager delete(ContratEntretienViager contratEntretienViager)
            throws ContratEntretienViagerException, JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� ContratEntretienViager
     * 
     * @param idContratEntretienViager
     *            L'identifiant de l'entit� ContratEntretienViager � charger en m�moire
     * @return L'entit� ContratEntretienViager charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ContratEntretienViagerException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public ContratEntretienViager read(String idContratEntretienViager) throws JadePersistenceException,
            ContratEntretienViagerException;

    /**
     * Chargement d'un ContratEntretienViager via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws ContratEntretienViagerException
     * @throws JadePersistenceException
     */
    public ContratEntretienViager readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws ContratEntretienViagerException, JadePersistenceException;

    /**
     * Permet de chercher des ContratEntretienViager selon un mod�le de crit�res.
     * 
     * @param ContratEntretienViagerSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ContratEntretienViagerException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public ContratEntretienViagerSearch search(ContratEntretienViagerSearch contratEntretienViagerSearch)
            throws JadePersistenceException, ContratEntretienViagerException;

    /**
     * 
     * Permet la mise � jour d'une entit� ContratEntretienViager
     * 
     * @param ContratEntretienViager
     *            L'entit� ContratEntretienViager � mettre � jour
     * @return L'entit� ContratEntretienViager mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ContratEntretienViagerException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public ContratEntretienViager update(ContratEntretienViager contratEntretienViager)
            throws JadePersistenceException, ContratEntretienViagerException, DonneeFinanciereException;
}