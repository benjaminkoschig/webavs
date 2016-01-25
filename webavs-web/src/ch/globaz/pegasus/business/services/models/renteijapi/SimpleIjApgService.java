package ch.globaz.pegasus.business.services.models.renteijapi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.IjApgException;
import ch.globaz.pegasus.business.models.renteijapi.SimpleIjApg;

public interface SimpleIjApgService extends JadeApplicationService {

    /**
     * Permet la création d'une entité autreRente
     * 
     * @param SimpleIjApg
     *            SimpleIjApg à créer
     * @return SimpleIjApg créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws IjApgException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleIjApg create(SimpleIjApg simpleIjApg) throws IjApgException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité autreRente
     * 
     * @param SimpleIjApg
     *            SimpleIjApg à supprimer
     * @return simpleIjApg supprimé
     * @throws IjApgException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleIjApg delete(SimpleIjApg simpleIjApg) throws IjApgException, JadePersistenceException;

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
     * @param idSimpleIjApg
     *            L'identifiant de la simpleIjApg à charger en mémoire
     * @return simpleIjApg chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws IjApgException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleIjApg read(String idSimpleIjApg) throws IjApgException, JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité variableMetier
     * 
     * @param SimpleIjApg
     *            SimpleIjApg à mettre à jour
     * @return simpleAutreRente mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws IjApgException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleIjApg update(SimpleIjApg simpleIjApg) throws IjApgException, JadePersistenceException;
}
