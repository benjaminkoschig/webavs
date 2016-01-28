package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierNonHabitableException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleBienImmobilierNonHabitable;

public interface SimpleBienImmobilierNonHabitableService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� SimpleBienImmobilierNonHabitable
     * 
     * @param simpleBienImmobilierNonHabitable
     *            L'entit� simpleBienImmobilierNonHabitable � cr�er
     * @return L'entit� simpleBienImmobilierNonHabitable cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BienImmobilierNonHabitableException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleBienImmobilierNonHabitable create(SimpleBienImmobilierNonHabitable simpleBienImmobilierNonHabitable)
            throws JadePersistenceException, BienImmobilierNonHabitableException;

    /**
     * Permet la suppression d'une entit� SimpleBienImmobilierNonHabitable
     * 
     * @param SimpleBienImmobilierNonHabitable
     *            L'entit� SimpleBienImmobilierNonHabitable � supprimer
     * @return L'entit� SimpleBienImmobilierNonHabitable supprim�
     * @throws SimpleBienImmobilierNonHabitableException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleBienImmobilierNonHabitable delete(SimpleBienImmobilierNonHabitable simpleBienImmobilierNonHabitable)
            throws BienImmobilierNonHabitableException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� SimpleBienImmobilierNonHabitable
     * 
     * @param idBienImmobilierNonHabitable
     *            L'identifiant de l'entit� SimpleBienImmobilierNonHabitable � charger en m�moire
     * @return L'entit� SimpleBienImmobilierNonHabitable charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BienImmobilierNonHabitableException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleBienImmobilierNonHabitable read(String idBienImmobilierNonHabitable) throws JadePersistenceException,
            BienImmobilierNonHabitableException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleBienImmobilierNonHabitable
     * 
     * @param SimpleBienImmobilierNonHabitable
     *            L'entit� SimpleBienImmobilierNonHabitable � mettre � jour
     * @return L'entit� SimpleBienImmobilierNonHabitable mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BienImmobilierNonHabitableException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleBienImmobilierNonHabitable update(SimpleBienImmobilierNonHabitable simpleBienImmobilierNonHabitable)
            throws JadePersistenceException, BienImmobilierNonHabitableException;

}