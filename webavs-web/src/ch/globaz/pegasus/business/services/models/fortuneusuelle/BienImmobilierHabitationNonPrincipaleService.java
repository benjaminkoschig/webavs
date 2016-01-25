package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierHabitationNonPrincipaleException;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierHabitationNonPrincipale;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierHabitationNonPrincipaleSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface BienImmobilierHabitationNonPrincipaleService extends JadeApplicationService,
        AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws BienImmobilierHabitationNonPrincipaleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(BienImmobilierHabitationNonPrincipaleSearch search)
            throws BienImmobilierHabitationNonPrincipaleException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� BienImmobilierHabitationNonPrincipale
     * 
     * @param BienImmobilierHabitationNonPrincipale
     *            L'entit� BienImmobilierHabitationNonPrincipale � cr�er
     * @return L'entit� BienImmobilierHabitationNonPrincipale cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BienImmobilierHabitationNonPrincipaleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public BienImmobilierHabitationNonPrincipale create(
            BienImmobilierHabitationNonPrincipale bienImmobilierHabitationNonPrincipale)
            throws JadePersistenceException, BienImmobilierHabitationNonPrincipaleException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� BienImmobilierHabitationNonPrincipale
     * 
     * @param BienImmobilierHabitationNonPrincipale
     *            L'entit� BienImmobilierHabitationNonPrincipale � supprimer
     * @return L'entit� BienImmobilierHabitationNonPrincipale supprim�
     * @throws BienImmobilierHabitationNonPrincipaleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public BienImmobilierHabitationNonPrincipale delete(
            BienImmobilierHabitationNonPrincipale bienImmobilierHabitationNonPrincipale)
            throws BienImmobilierHabitationNonPrincipaleException, JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� BienImmobilierHabitationNonPrincipale
     * 
     * @param idBienImmobilierHabitationNonPrincipale
     *            L'identifiant de l'entit� BienImmobilierHabitationNonPrincipale � charger en m�moire
     * @return L'entit� BienImmobilierHabitationNonPrincipale charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BienImmobilierHabitationNonPrincipaleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public BienImmobilierHabitationNonPrincipale read(String idBienImmobilierHabitationNonPrincipale)
            throws JadePersistenceException, BienImmobilierHabitationNonPrincipaleException;

    /**
     * Chargement d'une BienImmobilierHabitationNonPrincipale via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws BienImmobilierHabitationNonPrincipaleException
     * @throws JadePersistenceException
     */
    public BienImmobilierHabitationNonPrincipale readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws BienImmobilierHabitationNonPrincipaleException, JadePersistenceException;

    /**
     * Permet de chercher des BienImmobilierHabitationNonPrincipale selon un mod�le de crit�res.
     * 
     * @param BienImmobilierHabitationNonPrincipaleSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BienImmobilierHabitationNonPrincipaleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public BienImmobilierHabitationNonPrincipaleSearch search(
            BienImmobilierHabitationNonPrincipaleSearch bienImmobilierHabitationNonPrincipaleSearch)
            throws JadePersistenceException, BienImmobilierHabitationNonPrincipaleException;

    /**
     * 
     * Permet la mise � jour d'une entit� BienImmobilierHabitationNonPrincipale
     * 
     * @param BienImmobilierHabitationNonPrincipale
     *            L'entit� BienImmobilierHabitationNonPrincipale � mettre � jour
     * @return L'entit� BienImmobilierHabitationNonPrincipale mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BienImmobilierHabitationNonPrincipaleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public BienImmobilierHabitationNonPrincipale update(
            BienImmobilierHabitationNonPrincipale bienImmobilierHabitationNonPrincipale)
            throws JadePersistenceException, BienImmobilierHabitationNonPrincipaleException, DonneeFinanciereException;
}