package ch.globaz.pegasus.business.services.models.renteijapi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AutreRenteException;
import ch.globaz.pegasus.business.models.renteijapi.SimpleAutreRente;

public interface SimpleAutreRenteService extends JadeApplicationService {

    /**
     * Permet la création d'une entité autreRente
     * 
     * @param simpleAutreRente
     *            L' simpleAutreRente à créer
     * @return simpleAutreRente créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutreRenteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleAutreRente create(SimpleAutreRente autreRente) throws AutreRenteException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité autreRente
     * 
     * @param simpleAutreRente
     *            simpleAutreRente à supprimer
     * @return simpleAutreRente supprimé
     * @throws AutreRenteException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleAutreRente delete(SimpleAutreRente autreRente) throws AutreRenteException, JadePersistenceException;

    /**
     * Permet la suppression réele de la donnée financiere
     * 
     * @param listeIDString
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire une autreRente PC
     * 
     * @param idAutreRente
     *            L'identifiant de la autreRente à charger en mémoire
     * @return L' autre rente chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutreRenteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleAutreRente read(String idSimpleAutreRente) throws AutreRenteException, JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité variableMetier
     * 
     * @param simpleAutreRente
     *            L'autre rente à mettre à jour
     * @return simpleAutreRente mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutreRenteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleAutreRente update(SimpleAutreRente autreRente) throws AutreRenteException, JadePersistenceException;
}
