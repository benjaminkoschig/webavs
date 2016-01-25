package ch.globaz.hera.business.services.models.famille;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.hera.business.exceptions.models.PeriodeException;
import ch.globaz.hera.business.models.famille.SimplePeriode;
import ch.globaz.hera.business.models.famille.SimplePeriodeSearch;

public interface PeriodeService extends JadeApplicationService {

    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws PeriodeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimplePeriodeSearch search) throws PeriodeException, JadePersistenceException;

    /**
     * Permet de charger en mémoire une période
     * 
     * @param idSimplePeriode
     *            L'identifiant de la période à charger en mémoire
     * @return La période chargé en mémoire
     * @throws PeriodeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimplePeriode read(String idSimplePeriode) throws JadePersistenceException, PeriodeException;

    /**
     * Permet de chercher des périodes selon un modèle de critères.
     * 
     * @param search
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PeriodeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimplePeriodeSearch search(SimplePeriodeSearch search) throws JadePersistenceException, PeriodeException;

}
