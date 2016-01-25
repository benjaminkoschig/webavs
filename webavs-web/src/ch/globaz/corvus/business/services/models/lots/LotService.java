package ch.globaz.corvus.business.services.models.lots;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.corvus.business.exceptions.models.LotException;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.corvus.business.models.lots.SimpleLotSearch;

public interface LotService extends JadeApplicationService {

    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws LotException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimpleLotSearch search) throws LotException, JadePersistenceException;

    public SimpleLot create(SimpleLot simpleLot) throws JadePersistenceException, LotException;

    /**
     * Suppression d'un lot
     * 
     * @param simpleLot
     * @return
     * @throws JadePersistenceException
     * @throws LotException
     */
    public SimpleLot delete(SimpleLot simpleLot) throws JadePersistenceException, LotException;

    /**
     * Permet de charger en mémoire un lot
     * 
     * @param idSimpleLot
     *            L'identifiant du lot à charger en mémoire
     * @return Le lot chargé en mémoire
     * @throws LotException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleLot read(String idSimpleLot) throws JadePersistenceException, LotException;

    /**
     * Permet de chercher des lots selon un modèle de critères.
     * 
     * @param search
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws LotException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleLotSearch search(SimpleLotSearch search) throws JadePersistenceException, LotException;

    /**
     * @param simpleLot
     * @return
     * @throws JadePersistenceException
     * @throws LotException
     */
    public SimpleLot update(SimpleLot simpleLot) throws JadePersistenceException, LotException;

}
