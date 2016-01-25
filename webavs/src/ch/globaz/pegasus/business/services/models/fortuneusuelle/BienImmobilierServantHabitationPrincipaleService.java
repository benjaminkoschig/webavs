package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierServantHabitationPrincipaleException;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierServantHabitationPrincipale;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierServantHabitationPrincipaleSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface BienImmobilierServantHabitationPrincipaleService extends JadeApplicationService,
        AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws BienImmobilierServantHabitationPrincipaleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(BienImmobilierServantHabitationPrincipaleSearch search)
            throws BienImmobilierServantHabitationPrincipaleException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� BienImmobilierServantHabitationPrincipale
     * 
     * @param BienImmobilierServantHabitationPrincipale
     *            L'entit� BienImmobilierServantHabitationPrincipale � cr�er
     * @return L'entit� BienImmobilierServantHabitationPrincipale cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BienImmobilierServantHabitationPrincipaleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public BienImmobilierServantHabitationPrincipale create(
            BienImmobilierServantHabitationPrincipale bienImmobilierServantHabitationPrincipale)
            throws JadePersistenceException, BienImmobilierServantHabitationPrincipaleException,
            DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� BienImmobilierServantHabitationPrincipale
     * 
     * @param BienImmobilierServantHabitationPrincipale
     *            L'entit� BienImmobilierServantHabitationPrincipale � supprimer
     * @return L'entit� BienImmobilierServantHabitationPrincipale supprim�
     * @throws BienImmobilierServantHabitationPrincipaleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public BienImmobilierServantHabitationPrincipale delete(
            BienImmobilierServantHabitationPrincipale bienImmobilierServantHabitationPrincipale)
            throws BienImmobilierServantHabitationPrincipaleException, JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� BienImmobilierServantHabitationPrincipale
     * 
     * @param idBienImmobilierServantHabitationPrincipale
     *            L'identifiant de l'entit� BienImmobilierServantHabitationPrincipale � charger en m�moire
     * @return L'entit� BienImmobilierServantHabitationPrincipale charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BienImmobilierServantHabitationPrincipaleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public BienImmobilierServantHabitationPrincipale read(String idBienImmobilierServantHabitationPrincipale)
            throws JadePersistenceException, BienImmobilierServantHabitationPrincipaleException;

    /**
     * Chargement d'une BienImmobilierServantHabitationPrincipale via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws BienImmobilierServantHabitationPrincipaleException
     * @throws JadePersistenceException
     */
    public BienImmobilierServantHabitationPrincipale readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws BienImmobilierServantHabitationPrincipaleException, JadePersistenceException;

    /**
     * Permet de chercher des BienImmobilierServantHabitationPrincipale selon un mod�le de crit�res.
     * 
     * @param BienImmobilierServantHabitationPrincipaleSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BienImmobilierServantHabitationPrincipaleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public BienImmobilierServantHabitationPrincipaleSearch search(
            BienImmobilierServantHabitationPrincipaleSearch bienImmobilierServantHabitationPrincipaleSearch)
            throws JadePersistenceException, BienImmobilierServantHabitationPrincipaleException;

    /**
     * 
     * Permet la mise � jour d'une entit� BienImmobilierServantHabitationPrincipale
     * 
     * @param BienImmobilierServantHabitationPrincipale
     *            L'entit� BienImmobilierServantHabitationPrincipale � mettre � jour
     * @return L'entit� BienImmobilierServantHabitationPrincipale mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BienImmobilierServantHabitationPrincipaleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public BienImmobilierServantHabitationPrincipale update(
            BienImmobilierServantHabitationPrincipale bienImmobilierServantHabitationPrincipale)
            throws JadePersistenceException, BienImmobilierServantHabitationPrincipaleException,
            DonneeFinanciereException;
}