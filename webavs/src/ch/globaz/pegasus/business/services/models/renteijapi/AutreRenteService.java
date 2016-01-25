package ch.globaz.pegasus.business.services.models.renteijapi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AutreRenteException;
import ch.globaz.pegasus.business.models.renteijapi.AutreRente;
import ch.globaz.pegasus.business.models.renteijapi.AutreRenteSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface AutreRenteService extends JadeApplicationService, AbstractDonneeFinanciereService {

    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws AutreRenteException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(AutreRenteSearch search) throws AutreRenteException, JadePersistenceException;

    /**
     * Permet la création d'une entité autreRente
     * 
     * @param autreRente
     *            L' autre rente à créer
     * @return L' autre rente créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutreRenteException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public AutreRente create(AutreRente autreRente) throws AutreRenteException, JadePersistenceException,
            DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité autreRente
     * 
     * @param autreRente
     *            L' autre rente à supprimer
     * @return L' autre rente supprimé
     * @throws AutreRenteException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DonneeFinanciereException
     */
    public AutreRente delete(AutreRente autreRente) throws AutreRenteException, JadePersistenceException,
            DonneeFinanciereException;

    /**
     * Permet de charger en mémoire une autreRente PC
     * 
     * @param idAutreRente
     *            L'identifiant de la autreRente à charger en mémoire
     * @return L' autre rente chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutreRenteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AutreRente read(String idAutreRente) throws AutreRenteException, JadePersistenceException;

    /**
     * Chargement d'une AutreRente via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws AutreRenteException
     * @throws JadePersistenceException
     */
    public AutreRente readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws AutreRenteException,
            JadePersistenceException;

    public AutreRenteSearch search(AutreRenteSearch autreRenteSearch) throws JadePersistenceException,
            AutreRenteException;

    /**
     * 
     * Permet la mise à jour d'une entité variableMetier
     * 
     * @param autreRente
     *            L'autre rente à mettre à jour
     * @return L'autre rente mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutreRenteException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public AutreRente update(AutreRente autreRente) throws AutreRenteException, JadePersistenceException,
            DonneeFinanciereException;
}
