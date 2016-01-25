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
     * Permet la création d'une entité donneeFinanciereHeader.
     * 
     * @param donneeFinanciereHeader
     *            L'entité donneeFinanciereHeader à créer
     * @return L'entité donneeFinanciereHeader créé
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDonneeFinanciereHeader create(SimpleDonneeFinanciereHeader donneeFinanciereHeader)
            throws DonneeFinanciereException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité donneeFinanciereHeader
     * 
     * @param donneeFinanciereHeader
     *            L'entité donneeFinanciereHeader à supprimer
     * @return L'entité donneeFinanciereHeader supprimé
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
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
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Integer getSaveAction(SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader,
            SimpleVersionDroit simpleVersionDroit) throws DonneeFinanciereException;

    /**
     * Permet de charger en mémoire une entite donneeFinanciereHeader
     * 
     * @param idDonneeFinanciereHeader
     *            L'identifiant du donneeFinanciereHeader à charger en mémoire
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDonneeFinanciereHeader read(String idDonneeFinanciereHeader) throws DonneeFinanciereException,
            JadePersistenceException;

    SimpleDonneeFinanciereHeaderSearch search(SimpleDonneeFinanciereHeaderSearch simpleDonneeFinanciereHeaderSearch)
            throws JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet la mise à jour d'une entité donneeFinanciereHeader
     * 
     * @param donneeFinanciereHeader
     *            L'entité donneeFinanciereHeader à mettre à jour
     * @return L'entité donneeFinanciereHeader mis à jour
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDonneeFinanciereHeader update(SimpleDonneeFinanciereHeader donneeFinanciereHeader)
            throws DonneeFinanciereException, JadePersistenceException;

}
