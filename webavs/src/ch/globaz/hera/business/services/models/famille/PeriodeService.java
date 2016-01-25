package ch.globaz.hera.business.services.models.famille;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.hera.business.exceptions.models.PeriodeException;
import ch.globaz.hera.business.models.famille.SimplePeriode;
import ch.globaz.hera.business.models.famille.SimplePeriodeSearch;

public interface PeriodeService extends JadeApplicationService {

    /**
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws PeriodeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimplePeriodeSearch search) throws PeriodeException, JadePersistenceException;

    /**
     * Permet de charger en m�moire une p�riode
     * 
     * @param idSimplePeriode
     *            L'identifiant de la p�riode � charger en m�moire
     * @return La p�riode charg� en m�moire
     * @throws PeriodeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimplePeriode read(String idSimplePeriode) throws JadePersistenceException, PeriodeException;

    /**
     * Permet de chercher des p�riodes selon un mod�le de crit�res.
     * 
     * @param search
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PeriodeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimplePeriodeSearch search(SimplePeriodeSearch search) throws JadePersistenceException, PeriodeException;

}
