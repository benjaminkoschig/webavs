package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.TitreException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleTitre;

public interface SimpleTitreService extends JadeApplicationService {

    /**
     * Permet la création d'une entité SimpleTitre
     * 
     * @param simpleTitre
     *            L'entité simpleTitre à créer
     * @return L'entité simpleTitre créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TitreException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleTitre create(SimpleTitre simpleTitre) throws JadePersistenceException, TitreException;

    /**
     * Permet la suppression d'une entité SimpleTitre
     * 
     * @param SimpleTitre
     *            L'entité SimpleTitre à supprimer
     * @return L'entité SimpleTitre supprimé
     * @throws SimpleTitreException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleTitre delete(SimpleTitre simpleTitre) throws TitreException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité SimpleTitre
     * 
     * @param idTitre
     *            L'identifiant de l'entité SimpleTitre à charger en mémoire
     * @return L'entité SimpleTitre chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TitreException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleTitre read(String idTitre) throws JadePersistenceException, TitreException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleTitre
     * 
     * @param SimpleTitre
     *            L'entité SimpleTitre à mettre à jour
     * @return L'entité SimpleTitre mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws TitreException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleTitre update(SimpleTitre simpleTitre) throws JadePersistenceException, TitreException;

}