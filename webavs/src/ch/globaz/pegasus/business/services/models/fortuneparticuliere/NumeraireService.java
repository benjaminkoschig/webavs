/**
 * 
 */
package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.NumeraireException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.Numeraire;
import ch.globaz.pegasus.business.models.fortuneparticuliere.NumeraireSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

/**
 * @author BSC
 * 
 */
public interface NumeraireService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws NumeraireException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(NumeraireSearch search) throws NumeraireException, JadePersistenceException;

    /**
     * Permet la création d'une entité numéraire
     * 
     * @param numeraire
     *            L'entité numéraire à créer
     * @return L'entité numéraire créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws NumeraireException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Numeraire create(Numeraire numeraire) throws JadePersistenceException, NumeraireException,
            DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité numéraire
     * 
     * @param numeraire
     *            L'entité numéraire à supprimer
     * @return L'entité numéraire supprimé
     * @throws NumeraireException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public Numeraire delete(Numeraire numeraire) throws NumeraireException, JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité numeraire
     * 
     * @param idNumeraire
     *            L'identifiant de l'entité numéraire à charger en mémoire
     * @return L'entité numéraire chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws NumeraireException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Numeraire read(String idNumeraire) throws JadePersistenceException, NumeraireException;

    /**
     * Chargement d'un Numeraire via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws NumeraireException
     * @throws JadePersistenceException
     */
    public Numeraire readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws NumeraireException,
            JadePersistenceException;

    /**
     * Permet de chercher des numéraire selon un modèle de critères.
     * 
     * @param numeraireSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws NumeraireException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public NumeraireSearch search(NumeraireSearch numeraireSearch) throws JadePersistenceException, NumeraireException;

    /**
     * 
     * Permet la mise à jour d'une entité numéraire
     * 
     * @param numeraire
     *            L'entité numéraire à mettre à jour
     * @return L'entité numéraire mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws NumeraireException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public Numeraire update(Numeraire numeraire) throws JadePersistenceException, NumeraireException,
            DonneeFinanciereException;
}
