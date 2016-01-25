package ch.globaz.pegasus.business.services.models.home;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.home.PeriodeServiceEtatException;
import ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtat;
import ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtatSearch;

public interface SimplePeriodeServiceEtatService extends JadeApplicationService {
    /**
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws PeriodeServiceEtatException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimplePeriodeServiceEtatSearch search) throws PeriodeServiceEtatException,
            JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� periodeServiceEtat
     * 
     * @param periodeServiceEtat
     *            Le periodeServiceEtat � cr�er
     * @return Le periodeServiceEtat cr��
     * @throws PeriodeServiceEtatException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimplePeriodeServiceEtat create(SimplePeriodeServiceEtat simplePeriodeServiceEtat)
            throws PeriodeServiceEtatException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� periodeServiceEtat
     * 
     * @param periodeServiceEtat
     *            La periodeServiceEtat � supprimer
     * @return La periodeServiceEtat supprim�
     * @throws PeriodeServiceEtatException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimplePeriodeServiceEtat delete(SimplePeriodeServiceEtat simplePeriodeServiceEtat)
            throws PeriodeServiceEtatException, JadePersistenceException;

    /**
     * Permet de charger en m�moire une periodeServiceEtat
     * 
     * @param idSimplePeriodeServiceEtat
     *            L'identifiant de la periodeServiceEtat � charger en m�moire
     * @return La periodeServiceEtat charg� en m�moire
     * @throws PeriodeServiceEtatException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimplePeriodeServiceEtat read(String idSimplePeriodeServiceEtat) throws PeriodeServiceEtatException,
            JadePersistenceException;

    /**
     * Permet de chercher des periodeServiceEtat selon un mod�le de crit�res.
     * 
     * @param simplePeriodeServiceEtatSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PeriodeServiceEtatException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimplePeriodeServiceEtatSearch search(SimplePeriodeServiceEtatSearch simplePeriodeServiceEtatSearch)
            throws JadePersistenceException, PeriodeServiceEtatException;

    /**
     * Permet la mise � jour d'une entit� periodeServiceEtat
     * 
     * @param simplePeriodeServiceEtat
     *            La periodeServiceEtat � mettre � jour
     * @return La periodeServiceEtat mis � jour
     * @throws PeriodeServiceEtatException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimplePeriodeServiceEtat update(SimplePeriodeServiceEtat simplePeriodeServiceEtat)
            throws PeriodeServiceEtatException, JadePersistenceException;
}
