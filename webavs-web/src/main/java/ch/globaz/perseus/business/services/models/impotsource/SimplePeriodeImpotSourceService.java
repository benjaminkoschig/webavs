package ch.globaz.perseus.business.services.models.impotsource;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.impotsource.PeriodeImpotSourceException;
import ch.globaz.perseus.business.models.impotsource.SimplePeriodeImpotSource;
import ch.globaz.perseus.business.models.impotsource.SimplePeriodeImpotSourceSearchModel;

public interface SimplePeriodeImpotSourceService extends JadeApplicationService {

    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws PeriodeImpotSourceException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimplePeriodeImpotSourceSearchModel search) throws PeriodeImpotSourceException,
            JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� periodeImpotSource
     * 
     * @param periodeImpotSource
     *            La p�riode � cr�er
     * @return La p�riode cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PeriodeImpotSourceException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimplePeriodeImpotSource create(SimplePeriodeImpotSource periodeImpotSource)
            throws PeriodeImpotSourceException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� p�riode impot source
     * 
     * @param periodeImpotSource
     *            La p�riode � supprimer
     * @return La p�riode supprim�
     * @throws PeriodeImpotSourceException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimplePeriodeImpotSource delete(SimplePeriodeImpotSource periodeImpotSource)
            throws PeriodeImpotSourceException, JadePersistenceException;

    /**
     * Permet de charger en m�moire une p�riode d'impot � la source
     * 
     * @param idP�riodeImpotSource
     *            L'identifiant de la p�riode � charger en m�moire
     * @return La p�riode charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PeriodeImpotSourceException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimplePeriodeImpotSource read(String idPeriodeImpotSource) throws PeriodeImpotSourceException,
            JadePersistenceException;

    /**
     * Permet de chercher des p�riode d'impot � la source selon un mod�le de crit�res.
     * 
     * @param SimplePeriodeImpotSourceSearchModel
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimplePeriodeImpotSourceSearchModel search(
            SimplePeriodeImpotSourceSearchModel simplePeriodeImpotSourceSearchModel)
            throws PeriodeImpotSourceException, JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� p�riode d'impot � la source
     * 
     * @param periodeImpotSource
     *            La p�riode � mettre � jour
     * @return La p�riode mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PeriodeImpotSourceException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimplePeriodeImpotSource update(SimplePeriodeImpotSource periodeImpotSource)
            throws PeriodeImpotSourceException, JadePersistenceException;

}
