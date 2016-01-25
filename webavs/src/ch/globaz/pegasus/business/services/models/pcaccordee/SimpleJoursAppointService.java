package ch.globaz.pegasus.business.services.models.pcaccordee;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppoint;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppointSearch;

public interface SimpleJoursAppointService extends JadeApplicationService {

    /**
     * Permet la création d'une entité simpleJoursAppoint
     * 
     * @param simpleJoursAppoint
     *            Le simpleJoursAppoint à créer
     * @return Le simpleJoursAppoint créé
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleJoursAppoint create(SimpleJoursAppoint simpleJoursAppoint) throws PCAccordeeException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entité simpleJoursAppoint
     * 
     * @param simpleJoursAppoint
     *            Le simpleJoursAppoint à supprimer
     * @return Le simpleJoursAppoint supprimé
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleJoursAppoint delete(SimpleJoursAppoint simpleJoursAppoint) throws PCAccordeeException,
            JadePersistenceException;

    public abstract int delete(SimpleJoursAppointSearch simpleJoursAppointSearch) throws PCAccordeeException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire un simpleJoursAppoint
     * 
     * @param idSimpleJoursAppoint
     *            L'identifiant du simpleJoursAppoint à charger en mémoire
     * @return Le simpleJoursAppoint chargé en mémoire
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleJoursAppoint read(String idSimpleJoursAppoint) throws PCAccordeeException, JadePersistenceException;

    /**
     * Permet de chercher des simpleJoursAppoint selon un modèle de critères.
     * 
     * @param simpleJoursAppointSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleJoursAppointSearch search(SimpleJoursAppointSearch simpleJoursAppointSearch)
            throws JadePersistenceException, PCAccordeeException;

    /**
     * Permet la mise à jour d'une entité simpleJoursAppoint
     * 
     * @param simpleJoursAppoint
     *            Le simpleJoursAppoint à mettre à jour
     * @return Le simpleJoursAppoint mis à jour
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleJoursAppoint update(SimpleJoursAppoint simpleJoursAppoint) throws PCAccordeeException,
            JadePersistenceException;
}
