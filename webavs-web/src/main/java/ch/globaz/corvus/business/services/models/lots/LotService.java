package ch.globaz.corvus.business.services.models.lots;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.corvus.business.exceptions.models.LotException;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.corvus.business.models.lots.SimpleLotSearch;

public interface LotService extends JadeApplicationService {

    /**
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws LotException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
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
     * Permet de charger en m�moire un lot
     * 
     * @param idSimpleLot
     *            L'identifiant du lot � charger en m�moire
     * @return Le lot charg� en m�moire
     * @throws LotException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleLot read(String idSimpleLot) throws JadePersistenceException, LotException;

    /**
     * Permet de chercher des lots selon un mod�le de crit�res.
     * 
     * @param search
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws LotException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
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
