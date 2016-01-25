package ch.globaz.perseus.business.services.models.impotsource;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.impotsource.TauxException;
import ch.globaz.perseus.business.models.impotsource.SimpleBareme;
import ch.globaz.perseus.business.models.impotsource.SimpleBaremeSearchModel;

public interface SimpleBaremeService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� simpleBareme
     * 
     * @param simpleBareme
     *            simpleBareme cr�er
     * @return simpleBareme cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws TauxException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleBareme create(SimpleBareme simpleBareme) throws JadePersistenceException, TauxException;

    /**
     * Permet la suppression d'une entit� simpleBareme
     * 
     * @param simpleBareme
     *            simpleBareme � supprimer
     * @return simpleBareme supprim�
     * @throws TauxException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleBareme delete(SimpleBareme simpleBareme) throws JadePersistenceException, TauxException;

    /**
     * Permet de charger en m�moire une SimpleBareme
     * 
     * @param idBareme
     *            L'identifiant SimpleBareme � charger en m�moire
     * @return SimpleBareme charg� en m�moire
     * @throws TauxException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleBareme read(String idBareme) throws JadePersistenceException, TauxException;

    /**
     * Permet de chercher des SimpleBareme selon un mod�le de crit�res.
     * 
     * @param searchModel
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws TauxException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleBaremeSearchModel search(SimpleBaremeSearchModel searchModel) throws JadePersistenceException,
            TauxException;

    /**
     * Permet la mise � jour d'une entit� SimpleBareme
     * 
     * @param simpleBareme
     *            simpleBareme � mettre � jour
     * @return simpleBareme mis � jour
     * @throws TauxException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleBareme update(SimpleBareme simpleBareme) throws JadePersistenceException, TauxException;

}
