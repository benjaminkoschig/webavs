package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.CotisationsPsalException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuActiviteLucrativeDependanteException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleRevenuActiviteLucrativeDependante;

public interface SimpleRevenuActiviteLucrativeDependanteService extends JadeApplicationService {

    /**
     * Permet la création d'une entité SimpleRevenuActiviteLucrativeDependante
     * 
     * @param SimpleRevenuActiviteLucrativeDependante
     *            L'entité SimpleRevenuActiviteLucrativeDependante à créer
     * @return L'entité SimpleRevenuActiviteLucrativeDependante créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuActiviteLucrativeDependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuActiviteLucrativeDependante create(
            SimpleRevenuActiviteLucrativeDependante simpleRevenuActiviteLucrativeDependante)
            throws JadePersistenceException, RevenuActiviteLucrativeDependanteException;

    /**
     * Permet la suppression d'une entité SimpleRevenuActiviteLucrativeDependante
     * 
     * @param SimpleRevenuActiviteLucrativeDependante
     *            L'entité SimpleRevenuActiviteLucrativeDependante à supprimer
     * @return L'entité SimpleRevenuActiviteLucrativeDependante supprimé
     * @throws CotisationsPsalException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws RevenuActiviteLucrativeDependanteException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleRevenuActiviteLucrativeDependante delete(
            SimpleRevenuActiviteLucrativeDependante simpleRevenuActiviteLucrativeDependante)
            throws RevenuActiviteLucrativeDependanteException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité SimpleRevenuActiviteLucrativeDependante
     * 
     * @param idRevenuActiviteLucrativeDependante
     *            L'identifiant de l'entité SimpleRevenuActiviteLucrativeDependante à charger en mémoire
     * @return L'entité SimpleRevenuActiviteLucrativeDependante chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuActiviteLucrativeDependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuActiviteLucrativeDependante read(String idRevenuActiviteLucrativeDependante)
            throws JadePersistenceException, RevenuActiviteLucrativeDependanteException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleRevenuActiviteLucrativeDependante
     * 
     * @param SimpleRevenuActiviteLucrativeDependante
     *            L'entité SimpleRevenuActiviteLucrativeDependante à mettre à jour
     * @return L'entité SimpleRevenuActiviteLucrativeDependante mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuActiviteLucrativeDependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuActiviteLucrativeDependante update(
            SimpleRevenuActiviteLucrativeDependante simpleRevenuActiviteLucrativeDependante)
            throws JadePersistenceException, RevenuActiviteLucrativeDependanteException;

}
