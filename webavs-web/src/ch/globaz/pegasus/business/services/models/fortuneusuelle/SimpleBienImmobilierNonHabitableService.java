package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierNonHabitableException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleBienImmobilierNonHabitable;

public interface SimpleBienImmobilierNonHabitableService extends JadeApplicationService {

    /**
     * Permet la création d'une entité SimpleBienImmobilierNonHabitable
     * 
     * @param simpleBienImmobilierNonHabitable
     *            L'entité simpleBienImmobilierNonHabitable à créer
     * @return L'entité simpleBienImmobilierNonHabitable créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierNonHabitableException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleBienImmobilierNonHabitable create(SimpleBienImmobilierNonHabitable simpleBienImmobilierNonHabitable)
            throws JadePersistenceException, BienImmobilierNonHabitableException;

    /**
     * Permet la suppression d'une entité SimpleBienImmobilierNonHabitable
     * 
     * @param SimpleBienImmobilierNonHabitable
     *            L'entité SimpleBienImmobilierNonHabitable à supprimer
     * @return L'entité SimpleBienImmobilierNonHabitable supprimé
     * @throws SimpleBienImmobilierNonHabitableException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleBienImmobilierNonHabitable delete(SimpleBienImmobilierNonHabitable simpleBienImmobilierNonHabitable)
            throws BienImmobilierNonHabitableException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité SimpleBienImmobilierNonHabitable
     * 
     * @param idBienImmobilierNonHabitable
     *            L'identifiant de l'entité SimpleBienImmobilierNonHabitable à charger en mémoire
     * @return L'entité SimpleBienImmobilierNonHabitable chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierNonHabitableException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleBienImmobilierNonHabitable read(String idBienImmobilierNonHabitable) throws JadePersistenceException,
            BienImmobilierNonHabitableException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleBienImmobilierNonHabitable
     * 
     * @param SimpleBienImmobilierNonHabitable
     *            L'entité SimpleBienImmobilierNonHabitable à mettre à jour
     * @return L'entité SimpleBienImmobilierNonHabitable mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierNonHabitableException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleBienImmobilierNonHabitable update(SimpleBienImmobilierNonHabitable simpleBienImmobilierNonHabitable)
            throws JadePersistenceException, BienImmobilierNonHabitableException;

}