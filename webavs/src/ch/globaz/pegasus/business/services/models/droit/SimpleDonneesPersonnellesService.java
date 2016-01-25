package ch.globaz.pegasus.business.services.models.droit;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneesPersonnellesException;
import ch.globaz.pegasus.business.models.droit.SimpleDonneesPersonnelles;
import ch.globaz.pegasus.business.models.droit.SimpleDonneesPersonnellesSearch;

public interface SimpleDonneesPersonnellesService extends JadeApplicationService {

    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws DonneesPersonnellesException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimpleDonneesPersonnellesSearch search) throws DonneesPersonnellesException,
            JadePersistenceException;

    /**
     * Permet la création d'une entité donneesPersonnelles.
     * 
     * @param donneesPersonnelles
     *            L'entité donneesPersonnelles à créer
     * @return L'entité donneesPersonnelles créé
     * @throws DonneesPersonnellesException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDonneesPersonnelles create(SimpleDonneesPersonnelles donneesPersonnelles)
            throws DonneesPersonnellesException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité donneesPersonnelles
     * 
     * @param donneesPersonnelles
     *            L'entité donneesPersonnelles à supprimer
     * @return L'entité donneesPersonnelles supprimé
     * @throws DonneesPersonnellesException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDonneesPersonnelles delete(SimpleDonneesPersonnelles donneesPersonnelles)
            throws DonneesPersonnellesException, JadePersistenceException;

    /**
     * Permet de charger en mémoire une entite donneesPersonnelles
     * 
     * @param idDonneesPersonnelles
     *            L'identifiant du donneesPersonnelles à charger en mémoire
     * @throws DonneesPersonnellesException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDonneesPersonnelles read(String idDonneesPersonnelles) throws DonneesPersonnellesException,
            JadePersistenceException;

    /**
     * Permet la mise à jour d'une entité donneesPersonnelles
     * 
     * @param donneesPersonnelles
     *            L'entité donneesPersonnelles à mettre à jour
     * @return L'entité donneesPersonnelles mis à jour
     * @throws DonneesPersonnellesException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDonneesPersonnelles update(SimpleDonneesPersonnelles donneesPersonnelles)
            throws DonneesPersonnellesException, JadePersistenceException;

}
