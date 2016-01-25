package ch.globaz.pegasus.business.services.models.home;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.home.PeriodeServiceEtatException;
import ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtat;
import ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtatSearch;

public interface SimplePeriodeServiceEtatService extends JadeApplicationService {
    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws PeriodeServiceEtatException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimplePeriodeServiceEtatSearch search) throws PeriodeServiceEtatException,
            JadePersistenceException;

    /**
     * Permet la création d'une entité periodeServiceEtat
     * 
     * @param periodeServiceEtat
     *            Le periodeServiceEtat à créer
     * @return Le periodeServiceEtat créé
     * @throws PeriodeServiceEtatException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimplePeriodeServiceEtat create(SimplePeriodeServiceEtat simplePeriodeServiceEtat)
            throws PeriodeServiceEtatException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité periodeServiceEtat
     * 
     * @param periodeServiceEtat
     *            La periodeServiceEtat à supprimer
     * @return La periodeServiceEtat supprimé
     * @throws PeriodeServiceEtatException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimplePeriodeServiceEtat delete(SimplePeriodeServiceEtat simplePeriodeServiceEtat)
            throws PeriodeServiceEtatException, JadePersistenceException;

    /**
     * Permet de charger en mémoire une periodeServiceEtat
     * 
     * @param idSimplePeriodeServiceEtat
     *            L'identifiant de la periodeServiceEtat à charger en mémoire
     * @return La periodeServiceEtat chargé en mémoire
     * @throws PeriodeServiceEtatException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimplePeriodeServiceEtat read(String idSimplePeriodeServiceEtat) throws PeriodeServiceEtatException,
            JadePersistenceException;

    /**
     * Permet de chercher des periodeServiceEtat selon un modèle de critères.
     * 
     * @param simplePeriodeServiceEtatSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PeriodeServiceEtatException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimplePeriodeServiceEtatSearch search(SimplePeriodeServiceEtatSearch simplePeriodeServiceEtatSearch)
            throws JadePersistenceException, PeriodeServiceEtatException;

    /**
     * Permet la mise à jour d'une entité periodeServiceEtat
     * 
     * @param simplePeriodeServiceEtat
     *            La periodeServiceEtat à mettre à jour
     * @return La periodeServiceEtat mis à jour
     * @throws PeriodeServiceEtatException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimplePeriodeServiceEtat update(SimplePeriodeServiceEtat simplePeriodeServiceEtat)
            throws PeriodeServiceEtatException, JadePersistenceException;
}
