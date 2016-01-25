package ch.globaz.pegasus.business.services.models.droit;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeaderSearch;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;

public interface SimpleDonneeFinanciereHeaderService extends JadeApplicationService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.droit.DroitMembreFamilleService
     * #count(ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch)
     */
    public int count(SimpleDonneeFinanciereHeaderSearch search) throws DroitException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� donneeFinanciereHeader.
     * 
     * @param donneeFinanciereHeader
     *            L'entit� donneeFinanciereHeader � cr�er
     * @return L'entit� donneeFinanciereHeader cr��
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDonneeFinanciereHeader create(SimpleDonneeFinanciereHeader donneeFinanciereHeader)
            throws DonneeFinanciereException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� donneeFinanciereHeader
     * 
     * @param donneeFinanciereHeader
     *            L'entit� donneeFinanciereHeader � supprimer
     * @return L'entit� donneeFinanciereHeader supprim�
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDonneeFinanciereHeader delete(SimpleDonneeFinanciereHeader donneeFinanciereHeader)
            throws DonneeFinanciereException, JadePersistenceException;

    SimpleDonneeFinanciereHeaderSearch delete(SimpleDonneeFinanciereHeaderSearch search)
            throws DonneeFinanciereException, JadePersistenceException;

    /**
     * Donne l'action a executer lors d'un save: CREATE, UPDATE
     * 
     * si simpleDonneeFinanciereHeader.isVersion = simpleVersionDroit.idVersion -> UPDATE
     * 
     * sinon -> CREATE
     * 
     * @param simpleDonneeFinanciereHeader
     *            le header de la donnee financiere pour laquelle l'action save est demandee
     * @param simpleVersionDroit
     *            la version pour laquelle la donnee financiere doit etre enregistree
     * @return la constante correspondant a l'action CREATE ou UPDATE
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public Integer getSaveAction(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader,
            SimpleVersionDroit simpleVersionDroit) throws DonneeFinanciereException;

    /**
     * Permet de charger en m�moire une entite donneeFinanciereHeader
     * 
     * @param idDonneeFinanciereHeader
     *            L'identifiant du donneeFinanciereHeader � charger en m�moire
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDonneeFinanciereHeader read(String idDonneeFinanciereHeader) throws DonneeFinanciereException,
            JadePersistenceException;

    SimpleDonneeFinanciereHeaderSearch search(SimpleDonneeFinanciereHeaderSearch simpleDonneeFinanciereHeaderSearch)
            throws JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet la mise � jour d'une entit� donneeFinanciereHeader
     * 
     * @param donneeFinanciereHeader
     *            L'entit� donneeFinanciereHeader � mettre � jour
     * @return L'entit� donneeFinanciereHeader mis � jour
     * @throws DonneeFinanciereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDonneeFinanciereHeader update(SimpleDonneeFinanciereHeader donneeFinanciereHeader)
            throws DonneeFinanciereException, JadePersistenceException;

}
