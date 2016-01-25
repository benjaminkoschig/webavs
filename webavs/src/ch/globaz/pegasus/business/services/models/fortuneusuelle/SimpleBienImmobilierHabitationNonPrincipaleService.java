package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierHabitationNonPrincipaleException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleBienImmobilierHabitationNonPrincipale;

public interface SimpleBienImmobilierHabitationNonPrincipaleService extends JadeApplicationService {

    /**
     * Permet la création d'une entité SimpleBienImmobilierHabitationNonPrincipale
     * 
     * @param simpleBienImmobilierHabitationNonPrincipale
     *            L'entité simpleBienImmobilierHabitationNonPrincipale à créer
     * @return L'entité simpleBienImmobilierHabitationNonPrincipale créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierHabitationNonPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleBienImmobilierHabitationNonPrincipale create(
            SimpleBienImmobilierHabitationNonPrincipale simpleBienImmobilierHabitationNonPrincipale)
            throws JadePersistenceException, BienImmobilierHabitationNonPrincipaleException;

    /**
     * Permet la suppression d'une entité SimpleBienImmobilierHabitationNonPrincipale
     * 
     * @param SimpleBienImmobilierHabitationNonPrincipale
     *            L'entité SimpleBienImmobilierHabitationNonPrincipale à supprimer
     * @return L'entité SimpleBienImmobilierHabitationNonPrincipale supprimé
     * @throws SimpleBienImmobilierHabitationNonPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleBienImmobilierHabitationNonPrincipale delete(
            SimpleBienImmobilierHabitationNonPrincipale simpleBienImmobilierHabitationNonPrincipale)
            throws BienImmobilierHabitationNonPrincipaleException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité SimpleBienImmobilierHabitationNonPrincipale
     * 
     * @param idBienImmobilierHabitationNonPrincipale
     *            L'identifiant de l'entité SimpleBienImmobilierHabitationNonPrincipale à charger en mémoire
     * @return L'entité SimpleBienImmobilierHabitationNonPrincipale chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierHabitationNonPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleBienImmobilierHabitationNonPrincipale read(String idBienImmobilierHabitationNonPrincipale)
            throws JadePersistenceException, BienImmobilierHabitationNonPrincipaleException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleBienImmobilierHabitationNonPrincipale
     * 
     * @param SimpleBienImmobilierHabitationNonPrincipale
     *            L'entité SimpleBienImmobilierHabitationNonPrincipale à mettre à jour
     * @return L'entité SimpleBienImmobilierHabitationNonPrincipale mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierHabitationNonPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleBienImmobilierHabitationNonPrincipale update(
            SimpleBienImmobilierHabitationNonPrincipale simpleBienImmobilierHabitationNonPrincipale)
            throws JadePersistenceException, BienImmobilierHabitationNonPrincipaleException;

}