package ch.globaz.pegasus.business.services.models.renteijapi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.IjApgException;
import ch.globaz.pegasus.business.models.renteijapi.IjApg;
import ch.globaz.pegasus.business.models.renteijapi.IjApgSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface IjApgService extends JadeApplicationService, AbstractDonneeFinanciereService {

    public int count(IjApgSearch search) throws JadePersistenceException, IjApgException;

    /**
     * Permet la cr�ation d'une entit� autreRente
     * 
     * @param IjApg
     *            IjApg � cr�er
     * @return IjApg cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws IjApgException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public IjApg create(IjApg ijApg) throws IjApgException, JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entit� autreRente
     * 
     * @param IjApg
     *            IjApg � supprimer
     * @return ijApg supprim�
     * @throws IjApgException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public IjApg delete(IjApg ijApg) throws IjApgException, JadePersistenceException, DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en m�moire une autreRente PC
     * 
     * @param idIjApg
     *            L'identifiant de la ijApg � charger en m�moire
     * @return ijApg charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws IjApgException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public IjApg read(String idIjApg) throws IjApgException, JadePersistenceException;

    /**
     * Chargement d'une IjAPG via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws IjApgException
     * @throws JadePersistenceException
     */
    public IjApg readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws IjApgException,
            JadePersistenceException;

    public IjApgSearch search(IjApgSearch ijApgSearch) throws JadePersistenceException, IjApgException;

    /**
     * 
     * Permet la mise � jour d'une entit� variableMetier
     * 
     * @param IjApg
     *            IjApg � mettre � jour
     * @return simpleAutreRente mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws IjApgException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DonneeFinanciereException
     */
    public IjApg update(IjApg ijApg) throws IjApgException, JadePersistenceException, DonneeFinanciereException;

}
