package ch.globaz.pegasus.business.services.models.pcaccordee;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordeeSearch;

public interface SimplePCAccordeeService extends JadeApplicationService {
    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimplePCAccordeeSearch search) throws PCAccordeeException, JadePersistenceException;

    /**
     * Permet la création d'une entité simplePCAccordees
     * 
     * @param simplePCAccordees
     *            Le simplePCAccordees à créer
     * @return Le simplePCAccordees créé
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimplePCAccordee create(SimplePCAccordee simplePCAccordees) throws PCAccordeeException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entité simplePCAccordees
     * 
     * @param simplePCAccordees
     *            Le simplePCAccordees à supprimer
     * @return Le simplePCAccordees supprimé
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimplePCAccordee delete(SimplePCAccordee simplePCAccordees) throws PCAccordeeException,
            JadePersistenceException;

    /**
     * Permet la suppression de plusieurs simplePCAccordees
     * 
     * @param simplePCAccordees
     *            Le modele de recherche simplePCAccordees à supprimer
     * @return Le nombre d'elements supprimés
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int delete(SimplePCAccordeeSearch simplePCAccordeesSearch) throws JadePersistenceException,
            PCAccordeeException;

    /**
     * Permet de charger en mémoire un simplePCAccordees
     * 
     * @param idSimplePCAccordee
     *            L'identifiant du simplePCAccordees à charger en mémoire
     * @return Le simplePCAccordees chargé en mémoire
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimplePCAccordee read(String idSimplePCAccordee) throws PCAccordeeException, JadePersistenceException;

    /**
     * Permet de chercher des simplePCAccordees selon un modèle de critères.
     * 
     * @param simplePCAccordeesSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimplePCAccordeeSearch search(SimplePCAccordeeSearch simplePCAccordeesSearch)
            throws JadePersistenceException, PCAccordeeException;

    /**
     * Permet la mise à jour d'une entité simplePCAccordees
     * 
     * @param simplePCAccordees
     *            Le simplePCAccordees à mettre à jour
     * @return Le simplePCAccordees mis à jour
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimplePCAccordee update(SimplePCAccordee simplePCAccordees) throws PCAccordeeException,
            JadePersistenceException;

}
