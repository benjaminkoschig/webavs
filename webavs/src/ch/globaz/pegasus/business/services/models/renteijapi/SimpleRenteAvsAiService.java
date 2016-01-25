package ch.globaz.pegasus.business.services.models.renteijapi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.RenteAvsAiException;
import ch.globaz.pegasus.business.models.renteijapi.SimpleRenteAvsAi;

/**
 * Interface pour le service des simple Rente avsai AI 6.2010
 * 
 * @author SCE
 * 
 */
public interface SimpleRenteAvsAiService extends JadeApplicationService {

    /**
     * Permet la création d'une entité renteAvsAi.
     * 
     * @param renteAvsAi
     *            La renteAvsAi à créer
     * @return La renteAvsAi créé
     * @throws RenteAvsAiException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleRenteAvsAi create(SimpleRenteAvsAi renteAvsAi) throws RenteAvsAiException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité renteAvsAi
     * 
     * @param renteAvsAi
     *            La renteAvsAi à supprimer
     * @return La renteAvsAi supprimé
     * @throws RenteAvsAiException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleRenteAvsAi delete(SimpleRenteAvsAi renteAvsAi) throws RenteAvsAiException, JadePersistenceException;

    /**
     * Permet la suppression réele de la donnée financiere
     * 
     * @param listeIDString
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire une renteAvsAi
     * 
     * @param idRenteAvsAi
     *            L'identifiant de la renteAvsAi à charger en mémoire
     * @return La renteAvsAi chargé en mémoire
     * @throws RenteAvsAiException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleRenteAvsAi read(String idRenteAvsAi) throws RenteAvsAiException, JadePersistenceException;

    /**
     * Permet la mise à jour d'une entité renteAvsAi
     * 
     * @param renteAvsAi
     *            La renteAvsAi à mettre à jour
     * @return La renteAvsAi mis à jour
     * @throws RenteAvsAiException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleRenteAvsAi update(SimpleRenteAvsAi renteAvsAi) throws RenteAvsAiException, JadePersistenceException;
}
