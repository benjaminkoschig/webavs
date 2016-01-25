package ch.globaz.pegasus.business.services.models.pcaccordee;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.PersonneDansPlanCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.PersonneDansPlanCalculSearch;

public interface PersonneDansPlanCalculService extends JadeApplicationService {
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
    public int count(PersonneDansPlanCalculSearch search) throws PCAccordeeException, JadePersistenceException;

    /**
     * Permet la création d'une entité EnfantDansCalcul
     * 
     * @param enfantDansCalcul
     *            L'enfantDansCalcul à créer
     * @return L'enfantDansCalcul créé
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public PersonneDansPlanCalcul create(PersonneDansPlanCalcul enfantDansCalcul) throws PCAccordeeException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entité enfantDansCalcul
     * 
     * @param enfantDansCalcul
     *            L'enfantDansCalcul à supprimer
     * @return L'enfantDansCalcul supprimé
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public PersonneDansPlanCalcul delete(PersonneDansPlanCalcul enfantDansCalcul) throws PCAccordeeException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire un enfantDansCalcul
     * 
     * @param idEnfantDansCalcul
     *            L'identifiant de l'enfantDansCalcul à charger en mémoire
     * @return L'enfantDansCalcul chargé en mémoire
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public PersonneDansPlanCalcul read(String idEnfantDansCalcul) throws PCAccordeeException, JadePersistenceException;

    /**
     * Permet de chercher des enfantDansCalcul selon un modèle de critères.
     * 
     * @param enfantDansCalculSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PersonneDansPlanCalculSearch search(PersonneDansPlanCalculSearch enfantDansCalculSearch)
            throws JadePersistenceException, PCAccordeeException;

    /**
     * Permet la mise à jour d'une entité enfantDansCalcul
     * 
     * @param enfantDansCalcul
     *            L'enfantDansCalcul à mettre à jour
     * @return L'enfantDansCalcul mis à jour
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public PersonneDansPlanCalcul update(PersonneDansPlanCalcul enfantDansCalcul) throws PCAccordeeException,
            JadePersistenceException;

}
