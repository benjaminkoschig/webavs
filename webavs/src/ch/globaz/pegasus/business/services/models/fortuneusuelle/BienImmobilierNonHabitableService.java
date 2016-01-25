package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierNonHabitableException;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierNonHabitable;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierNonHabitableSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface BienImmobilierNonHabitableService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws BienImmobilierNonHabitableException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(BienImmobilierNonHabitableSearch search) throws BienImmobilierNonHabitableException,
            JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� BienImmobilierNonHabitable
     * 
     * @param BienImmobilierNonHabitable
     *            L'entit� BienImmobilierNonHabitable � cr�er
     * @return L'entit� BienImmobilierNonHabitable cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BienImmobilierNonHabitableException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public BienImmobilierNonHabitable create(BienImmobilierNonHabitable bienImmobilierNonHabitable)
            throws JadePersistenceException, BienImmobilierNonHabitableException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� BienImmobilierNonHabitable
     * 
     * @param BienImmobilierNonHabitable
     *            L'entit� BienImmobilierNonHabitable � supprimer
     * @return L'entit� BienImmobilierNonHabitable supprim�
     * @throws BienImmobilierNonHabitableException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public BienImmobilierNonHabitable delete(BienImmobilierNonHabitable bienImmobilierNonHabitable)
            throws BienImmobilierNonHabitableException, JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� BienImmobilierNonHabitable
     * 
     * @param idBienImmobilierNonHabitable
     *            L'identifiant de l'entit� BienImmobilierNonHabitable � charger en m�moire
     * @return L'entit� BienImmobilierNonHabitable charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BienImmobilierNonHabitableException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public BienImmobilierNonHabitable read(String idBienImmobilierNonHabitable) throws JadePersistenceException,
            BienImmobilierNonHabitableException;

    /**
     * Chargement d'un BienImmobilierNonHabitable via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws BienImmobilierNonHabitableException
     * @throws JadePersistenceException
     */
    public BienImmobilierNonHabitable readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws BienImmobilierNonHabitableException, JadePersistenceException;

    /**
     * Permet de chercher des BienImmobilierNonHabitable selon un mod�le de crit�res.
     * 
     * @param BienImmobilierNonHabitableSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BienImmobilierNonHabitableException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public BienImmobilierNonHabitableSearch search(BienImmobilierNonHabitableSearch bienImmobilierNonHabitableSearch)
            throws JadePersistenceException, BienImmobilierNonHabitableException;

    /**
     * 
     * Permet la mise � jour d'une entit� BienImmobilierNonHabitable
     * 
     * @param BienImmobilierNonHabitable
     *            L'entit� BienImmobilierNonHabitable � mettre � jour
     * @return L'entit� BienImmobilierNonHabitable mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BienImmobilierNonHabitableException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public BienImmobilierNonHabitable update(BienImmobilierNonHabitable bienImmobilierNonHabitable)
            throws JadePersistenceException, BienImmobilierNonHabitableException, DonneeFinanciereException;
}