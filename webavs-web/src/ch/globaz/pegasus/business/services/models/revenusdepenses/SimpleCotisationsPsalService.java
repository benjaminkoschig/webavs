package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.CotisationsPsalException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleCotisationsPsal;

public interface SimpleCotisationsPsalService extends JadeApplicationService {

    /**
     * Permet la création d'une entité SimpleCotisationsPsal
     * 
     * @param SimpleCotisationsPsal
     *            L'entité SimpleCotisationsPsal à créer
     * @return L'entité SimpleCotisationsPsal créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CotisationsPsalException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleCotisationsPsal create(SimpleCotisationsPsal simpleCotisationsPsal) throws JadePersistenceException,
            CotisationsPsalException;

    /**
     * Permet la suppression d'une entité SimpleCotisationsPsal
     * 
     * @param SimpleCotisationsPsal
     *            L'entité SimpleCotisationsPsal à supprimer
     * @return L'entité SimpleCotisationsPsal supprimé
     * @throws CotisationsPsalException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleCotisationsPsal delete(SimpleCotisationsPsal simpleCotisationsPsal) throws CotisationsPsalException,
            JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité SimpleCotisationsPsal
     * 
     * @param idCotisationsPsal
     *            L'identifiant de l'entité SimpleCotisationsPsal à charger en mémoire
     * @return L'entité SimpleCotisationsPsal chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CotisationsPsalException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleCotisationsPsal read(String idCotisationsPsal) throws JadePersistenceException,
            CotisationsPsalException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleCotisationsPsal
     * 
     * @param SimpleCotisationsPsal
     *            L'entité SimpleCotisationsPsal à mettre à jour
     * @return L'entité SimpleCotisationsPsal mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CotisationsPsalException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleCotisationsPsal update(SimpleCotisationsPsal simpleCotisationsPsal) throws JadePersistenceException,
            CotisationsPsalException;

}
