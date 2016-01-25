package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierHabitationNonPrincipaleException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleBienImmobilierHabitationNonPrincipale;

public interface SimpleBienImmobilierHabitationNonPrincipaleService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� SimpleBienImmobilierHabitationNonPrincipale
     * 
     * @param simpleBienImmobilierHabitationNonPrincipale
     *            L'entit� simpleBienImmobilierHabitationNonPrincipale � cr�er
     * @return L'entit� simpleBienImmobilierHabitationNonPrincipale cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BienImmobilierHabitationNonPrincipaleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleBienImmobilierHabitationNonPrincipale create(
            SimpleBienImmobilierHabitationNonPrincipale simpleBienImmobilierHabitationNonPrincipale)
            throws JadePersistenceException, BienImmobilierHabitationNonPrincipaleException;

    /**
     * Permet la suppression d'une entit� SimpleBienImmobilierHabitationNonPrincipale
     * 
     * @param SimpleBienImmobilierHabitationNonPrincipale
     *            L'entit� SimpleBienImmobilierHabitationNonPrincipale � supprimer
     * @return L'entit� SimpleBienImmobilierHabitationNonPrincipale supprim�
     * @throws SimpleBienImmobilierHabitationNonPrincipaleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleBienImmobilierHabitationNonPrincipale delete(
            SimpleBienImmobilierHabitationNonPrincipale simpleBienImmobilierHabitationNonPrincipale)
            throws BienImmobilierHabitationNonPrincipaleException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� SimpleBienImmobilierHabitationNonPrincipale
     * 
     * @param idBienImmobilierHabitationNonPrincipale
     *            L'identifiant de l'entit� SimpleBienImmobilierHabitationNonPrincipale � charger en m�moire
     * @return L'entit� SimpleBienImmobilierHabitationNonPrincipale charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BienImmobilierHabitationNonPrincipaleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleBienImmobilierHabitationNonPrincipale read(String idBienImmobilierHabitationNonPrincipale)
            throws JadePersistenceException, BienImmobilierHabitationNonPrincipaleException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleBienImmobilierHabitationNonPrincipale
     * 
     * @param SimpleBienImmobilierHabitationNonPrincipale
     *            L'entit� SimpleBienImmobilierHabitationNonPrincipale � mettre � jour
     * @return L'entit� SimpleBienImmobilierHabitationNonPrincipale mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BienImmobilierHabitationNonPrincipaleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleBienImmobilierHabitationNonPrincipale update(
            SimpleBienImmobilierHabitationNonPrincipale simpleBienImmobilierHabitationNonPrincipale)
            throws JadePersistenceException, BienImmobilierHabitationNonPrincipaleException;

}