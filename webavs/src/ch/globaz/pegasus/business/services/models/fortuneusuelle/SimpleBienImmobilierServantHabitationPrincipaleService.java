package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierServantHabitationPrincipaleException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleBienImmobilierServantHabitationPrincipale;

public interface SimpleBienImmobilierServantHabitationPrincipaleService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� SimpleBienImmobilierServantHabitation
     * 
     * @param simpleBienImmobilierServantHabitation
     *            L'entit� simpleBienImmobilierServantHabitation � cr�er
     * @return L'entit� simpleBienImmobilierServantHabitation cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BienImmobilierServantHabitationException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleBienImmobilierServantHabitationPrincipale create(
            SimpleBienImmobilierServantHabitationPrincipale simpleBienImmobilierServantHabitationPrincipale)
            throws JadePersistenceException, BienImmobilierServantHabitationPrincipaleException;

    /**
     * Permet la suppression d'une entit� SimpleBienImmobilierServantHabitationPrincipale
     * 
     * @param SimpleBienImmobilierServantHabitationPrincipale
     *            L'entit� SimpleBienImmobilierServantHabitationPrincipale � supprimer
     * @return L'entit� SimpleBienImmobilierServantHabitationPrincipale supprim�
     * @throws SimpleBienImmobilierServantHabitationPrincipaleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleBienImmobilierServantHabitationPrincipale delete(
            SimpleBienImmobilierServantHabitationPrincipale simpleBienImmobilierServantHabitationPrincipale)
            throws BienImmobilierServantHabitationPrincipaleException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� SimpleBienImmobilierServantHabitationPrincipale
     * 
     * @param idBienImmobilierServantHabitationPrincipale
     *            L'identifiant de l'entit� SimpleBienImmobilierServantHabitationPrincipale � charger en m�moire
     * @return L'entit� SimpleBienImmobilierServantHabitationPrincipale charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BienImmobilierServantHabitationPrincipaleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleBienImmobilierServantHabitationPrincipale read(String idBienImmobilierServantHabitationPrincipale)
            throws JadePersistenceException, BienImmobilierServantHabitationPrincipaleException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleBienImmobilierServantHabitationPrincipale
     * 
     * @param SimpleBienImmobilierServantHabitationPrincipale
     *            L'entit� SimpleBienImmobilierServantHabitationPrincipale � mettre � jour
     * @return L'entit� SimpleBienImmobilierServantHabitationPrincipale mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws BienImmobilierServantHabitationPrincipaleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleBienImmobilierServantHabitationPrincipale update(
            SimpleBienImmobilierServantHabitationPrincipale simpleBienImmobilierServantHabitationPrincipale)
            throws JadePersistenceException, BienImmobilierServantHabitationPrincipaleException;

}