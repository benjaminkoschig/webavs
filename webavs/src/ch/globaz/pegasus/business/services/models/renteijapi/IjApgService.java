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
     * Permet la création d'une entité autreRente
     * 
     * @param IjApg
     *            IjApg à créer
     * @return IjApg créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws IjApgException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public IjApg create(IjApg ijApg) throws IjApgException, JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité autreRente
     * 
     * @param IjApg
     *            IjApg à supprimer
     * @return ijApg supprimé
     * @throws IjApgException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public IjApg delete(IjApg ijApg) throws IjApgException, JadePersistenceException, DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en mémoire une autreRente PC
     * 
     * @param idIjApg
     *            L'identifiant de la ijApg à charger en mémoire
     * @return ijApg chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws IjApgException
     *             Levée en cas de problème métier dans l'exécution du service
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
     * Permet la mise à jour d'une entité variableMetier
     * 
     * @param IjApg
     *            IjApg à mettre à jour
     * @return simpleAutreRente mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws IjApgException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public IjApg update(IjApg ijApg) throws IjApgException, JadePersistenceException, DonneeFinanciereException;

}
