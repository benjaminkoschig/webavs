package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuActiviteLucrativeIndependanteException;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeIndependante;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeIndependanteSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface RevenuActiviteLucrativeIndependanteService extends JadeApplicationService,
        AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(RevenuActiviteLucrativeIndependanteSearch search)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� RevenuActiviteLucrativeIndependante
     * 
     * @param RevenuActiviteLucrativeIndependante
     *            L'entit� RevenuActiviteLucrativeIndependante � cr�er
     * @return L'entit� RevenuActiviteLucrativeIndependante cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public RevenuActiviteLucrativeIndependante create(
            RevenuActiviteLucrativeIndependante revenuActiviteLucrativeIndependante) throws JadePersistenceException,
            RevenuActiviteLucrativeIndependanteException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� RevenuActiviteLucrativeIndependante
     * 
     * @param RevenuActiviteLucrativeIndependante
     *            L'entit� RevenuActiviteLucrativeIndependante � supprimer
     * @return L'entit� RevenuActiviteLucrativeIndependante supprim�
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public RevenuActiviteLucrativeIndependante delete(
            RevenuActiviteLucrativeIndependante revenuActiviteLucrativeIndependante)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� RevenuActiviteLucrativeIndependante
     * 
     * @param idRevenuActiviteLucrativeIndependante
     *            L'identifiant de l'entit� RevenuActiviteLucrativeIndependante � charger en m�moire
     * @return L'entit� RevenuActiviteLucrativeIndependante charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public RevenuActiviteLucrativeIndependante read(String idRevenuActiviteLucrativeIndependante)
            throws JadePersistenceException, RevenuActiviteLucrativeIndependanteException;

    /**
     * Chargement d'une RevenuActiviteLucrativeIndependante via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws RevenuActiviteLucrativeIndependanteException
     * @throws JadePersistenceException
     */
    public RevenuActiviteLucrativeIndependante readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException;

    /**
     * Permet de chercher des RevenuActiviteLucrativeIndependante selon un mod�le de crit�res.
     * 
     * @param RevenuActiviteLucrativeIndependanteSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public RevenuActiviteLucrativeIndependanteSearch search(
            RevenuActiviteLucrativeIndependanteSearch revenuActiviteLucrativeIndependanteSearch)
            throws JadePersistenceException, RevenuActiviteLucrativeIndependanteException;

    /**
     * 
     * Permet la mise � jour d'une entit� RevenuActiviteLucrativeIndependante
     * 
     * @param RevenuActiviteLucrativeIndependante
     *            L'entit� RevenuActiviteLucrativeIndependante � mettre � jour
     * @return L'entit� RevenuActiviteLucrativeIndependante mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public RevenuActiviteLucrativeIndependante update(
            RevenuActiviteLucrativeIndependante revenuActiviteLucrativeIndependante) throws JadePersistenceException,
            RevenuActiviteLucrativeIndependanteException, DonneeFinanciereException;
}