package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierServantHabitationPrincipaleException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleBienImmobilierServantHabitationPrincipale;

public interface SimpleBienImmobilierServantHabitationPrincipaleService extends JadeApplicationService {

    /**
     * Permet la création d'une entité SimpleBienImmobilierServantHabitation
     * 
     * @param simpleBienImmobilierServantHabitation
     *            L'entité simpleBienImmobilierServantHabitation à créer
     * @return L'entité simpleBienImmobilierServantHabitation créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierServantHabitationException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleBienImmobilierServantHabitationPrincipale create(
            SimpleBienImmobilierServantHabitationPrincipale simpleBienImmobilierServantHabitationPrincipale)
            throws JadePersistenceException, BienImmobilierServantHabitationPrincipaleException;

    /**
     * Permet la suppression d'une entité SimpleBienImmobilierServantHabitationPrincipale
     * 
     * @param SimpleBienImmobilierServantHabitationPrincipale
     *            L'entité SimpleBienImmobilierServantHabitationPrincipale à supprimer
     * @return L'entité SimpleBienImmobilierServantHabitationPrincipale supprimé
     * @throws SimpleBienImmobilierServantHabitationPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleBienImmobilierServantHabitationPrincipale delete(
            SimpleBienImmobilierServantHabitationPrincipale simpleBienImmobilierServantHabitationPrincipale)
            throws BienImmobilierServantHabitationPrincipaleException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité SimpleBienImmobilierServantHabitationPrincipale
     * 
     * @param idBienImmobilierServantHabitationPrincipale
     *            L'identifiant de l'entité SimpleBienImmobilierServantHabitationPrincipale à charger en mémoire
     * @return L'entité SimpleBienImmobilierServantHabitationPrincipale chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierServantHabitationPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleBienImmobilierServantHabitationPrincipale read(String idBienImmobilierServantHabitationPrincipale)
            throws JadePersistenceException, BienImmobilierServantHabitationPrincipaleException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleBienImmobilierServantHabitationPrincipale
     * 
     * @param SimpleBienImmobilierServantHabitationPrincipale
     *            L'entité SimpleBienImmobilierServantHabitationPrincipale à mettre à jour
     * @return L'entité SimpleBienImmobilierServantHabitationPrincipale mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws BienImmobilierServantHabitationPrincipaleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleBienImmobilierServantHabitationPrincipale update(
            SimpleBienImmobilierServantHabitationPrincipale simpleBienImmobilierServantHabitationPrincipale)
            throws JadePersistenceException, BienImmobilierServantHabitationPrincipaleException;

}