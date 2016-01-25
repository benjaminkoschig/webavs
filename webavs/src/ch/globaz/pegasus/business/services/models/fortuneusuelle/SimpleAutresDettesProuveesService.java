package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.AutresDettesProuveesException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleAutresDettesProuvees;

public interface SimpleAutresDettesProuveesService extends JadeApplicationService {

    /**
     * Permet la création d'une entité SimpleAutresDettesProuvees
     * 
     * @param simpleAssuranceVie
     *            L'entité simpleAutresDettesProuvees à créer
     * @return L'entité simpleAutresDettesProuvees créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutresDettesProuveesException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleAutresDettesProuvees create(SimpleAutresDettesProuvees simpleAutresDettesProuvees)
            throws JadePersistenceException, AutresDettesProuveesException;

    /**
     * Permet la suppression d'une entité SimpleAutresDettesProuvees
     * 
     * @param SimpleAutresDettesProuvees
     *            L'entité SimpleAutresDettesProuvees à supprimer
     * @return L'entité SimpleAutresDettesProuvees supprimé
     * @throws SimpleAutresDettesProuveesException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleAutresDettesProuvees delete(SimpleAutresDettesProuvees simpleAutresDettesProuvees)
            throws AutresDettesProuveesException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité SimpleAutresDettesProuvees
     * 
     * @param idAutresDettesProuvees
     *            L'identifiant de l'entité SimpleAutresDettesProuvees à charger en mémoire
     * @return L'entité SimpleAutresDettesProuvees chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutresDettesProuveesException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleAutresDettesProuvees read(String idAutresDettesProuvees) throws JadePersistenceException,
            AutresDettesProuveesException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleAutresDettesProuvees
     * 
     * @param SimpleAutresDettesProuvees
     *            L'entité SimpleAutresDettesProuvees à mettre à jour
     * @return L'entité SimpleAutresDettesProuvees mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutresDettesProuveesException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleAutresDettesProuvees update(SimpleAutresDettesProuvees simpleAutresDettesProuvees)
            throws JadePersistenceException, AutresDettesProuveesException;

}
