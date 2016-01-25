package ch.globaz.pegasus.business.services.models.dossier;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.models.dossier.SimpleDossier;
import ch.globaz.pegasus.business.models.dossier.SimpleDossierSearch;

public interface SimpleDossierService extends JadeApplicationService {
    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws DossierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimpleDossierSearch search) throws DossierException, JadePersistenceException;

    /**
     * Permet la création d'une entité dossier. Le dossier doit avoir l'id d'une demande de prestation associée qui
     * existe et qui n'est pas déjà associée à un autre dossier, sinon une exception est levée.
     * 
     * @param dossier
     *            Le dossier à créer
     * @return Le dossier créé
     * @throws DossierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDossier create(SimpleDossier dossier) throws DossierException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité dossier
     * 
     * @param dossier
     *            Le dossier à supprimer
     * @return Le dossier supprimé
     * @throws DossierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDossier delete(SimpleDossier dossier) throws DossierException, JadePersistenceException;

    /**
     * Permet de charger en mémoire un dossier
     * 
     * @param idDossier
     *            L'identifiant du dossier à charger en mémoire
     * @return Le dossier chargé en mémoire
     * @throws DossierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDossier read(String idDossier) throws DossierException, JadePersistenceException;

    /**
     * Permet la mise à jour d'une entité dossier
     * 
     * @param dossier
     *            Le dossier à mettre à jour
     * @return Le dossier mis à jour
     * @throws DossierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDossier update(SimpleDossier dossier) throws DossierException, JadePersistenceException;
}
