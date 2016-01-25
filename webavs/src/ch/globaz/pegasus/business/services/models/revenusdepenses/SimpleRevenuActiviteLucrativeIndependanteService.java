package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.CotisationsPsalException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuActiviteLucrativeIndependanteException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleRevenuActiviteLucrativeIndependante;

public interface SimpleRevenuActiviteLucrativeIndependanteService extends JadeApplicationService {

    /**
     * Permet la création d'une entité SimpleRevenuActiviteLucrativeIndependante
     * 
     * @param SimpleRevenuActiviteLucrativeIndependante
     *            L'entité SimpleRevenuActiviteLucrativeIndependante à créer
     * @return L'entité SimpleRevenuActiviteLucrativeIndependante créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuActiviteLucrativeIndependante create(
            SimpleRevenuActiviteLucrativeIndependante simpleRevenuActiviteLucrativeIndependante)
            throws JadePersistenceException, RevenuActiviteLucrativeIndependanteException;

    /**
     * Permet la suppression d'une entité SimpleRevenuActiviteLucrativeIndependante
     * 
     * @param SimpleRevenuActiviteLucrativeIndependante
     *            L'entité SimpleRevenuActiviteLucrativeIndependante à supprimer
     * @return L'entité SimpleRevenuActiviteLucrativeIndependante supprimé
     * @throws CotisationsPsalException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleRevenuActiviteLucrativeIndependante delete(
            SimpleRevenuActiviteLucrativeIndependante simpleRevenuActiviteLucrativeIndependante)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité SimpleRevenuActiviteLucrativeIndependante
     * 
     * @param idRevenuActiviteLucrativeIndependante
     *            L'identifiant de l'entité SimpleRevenuActiviteLucrativeIndependante à charger en mémoire
     * @return L'entité SimpleRevenuActiviteLucrativeIndependante chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuActiviteLucrativeIndependante read(String idRevenuActiviteLucrativeIndependante)
            throws JadePersistenceException, RevenuActiviteLucrativeIndependanteException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleRevenuActiviteLucrativeIndependante
     * 
     * @param SimpleRevenuActiviteLucrativeIndependante
     *            L'entité SimpleRevenuActiviteLucrativeIndependante à mettre à jour
     * @return L'entité SimpleRevenuActiviteLucrativeIndependante mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuActiviteLucrativeIndependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleRevenuActiviteLucrativeIndependante update(
            SimpleRevenuActiviteLucrativeIndependante simpleRevenuActiviteLucrativeIndependante)
            throws JadePersistenceException, RevenuActiviteLucrativeIndependanteException;
}
