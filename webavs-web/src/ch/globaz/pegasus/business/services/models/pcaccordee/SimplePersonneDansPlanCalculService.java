package ch.globaz.pegasus.business.services.models.pcaccordee;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PersonneDansPlanCalculException;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePersonneDansPlanCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePersonneDansPlanCalculSearch;

public interface SimplePersonneDansPlanCalculService extends JadeApplicationService {
    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws PersonneDansPlanCalculException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PCAccordeeException
     */
    public int count(SimplePersonneDansPlanCalculSearch search) throws JadePersistenceException, PCAccordeeException;

    /**
     * Permet la création d'une entité simpleEnfantDansCalcul
     * 
     * @param simplePersonneDansPlanCalcul
     *            Le simpleEnfantDansCalcul à créer
     * @return Le simpleEnfantDansCalcul créé
     * @throws PersonneDansPlanCalculException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PCAccordeeException
     */
    public SimplePersonneDansPlanCalcul create(SimplePersonneDansPlanCalcul simplePersonneDansPlanCalcul)
            throws JadePersistenceException, PCAccordeeException;

    /**
     * Permet la suppression d'une entité simpleEnfantDansCalcul
     * 
     * @param simplePersonneDansPlanCalcul
     *            Le simpleEnfantDansCalcul à supprimer
     * @return Le simpleEnfantDansCalcul supprimé
     * @throws PersonneDansPlanCalculException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PCAccordeeException
     */
    public SimplePersonneDansPlanCalcul delete(SimplePersonneDansPlanCalcul simplePersonneDansPlanCalcul)
            throws JadePersistenceException, PCAccordeeException;

    public void delete(SimplePersonneDansPlanCalculSearch personneSearch) throws JadePersistenceException,
            PCAccordeeException;

    /**
     * Permet de charger en mémoire un simpleEnfantDansCalcul
     * 
     * @param idSimplePersonneDansPlanCalcul
     *            L'identifiant du simpleEnfantDansCalcul à charger en mémoire
     * @return Le simpleEnfantDansCalcul chargé en mémoire
     * @throws PersonneDansPlanCalculException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PCAccordeeException
     */
    public SimplePersonneDansPlanCalcul read(String idSimplePersonneDansPlanCalcul) throws JadePersistenceException,
            PCAccordeeException;

    /**
     * Permet de chercher des simpleEnfantDansCalcul selon un modèle de critères.
     * 
     * @param simplePersonneDansPlanCalculSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PersonneDansPlanCalculException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws PCAccordeeException
     */
    public SimplePersonneDansPlanCalculSearch search(
            SimplePersonneDansPlanCalculSearch simplePersonneDansPlanCalculSearch) throws JadePersistenceException,
            PCAccordeeException;

    /**
     * Permet la mise à jour d'une entité simpleEnfantDansCalcul
     * 
     * @param simplePersonneDansPlanCalcul
     *            Le simpleEnfantDansCalcul à mettre à jour
     * @return Le simpleEnfantDansCalcul mis à jour
     * @throws PersonneDansPlanCalculException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PCAccordeeException
     */
    public SimplePersonneDansPlanCalcul update(SimplePersonneDansPlanCalcul simplePersonneDansPlanCalcul)
            throws JadePersistenceException, PCAccordeeException;

}
