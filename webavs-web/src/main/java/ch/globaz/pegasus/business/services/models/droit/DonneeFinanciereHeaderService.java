/**
 * 
 */
package ch.globaz.pegasus.business.services.models.droit;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.DonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.droit.DonneeFinanciereHeaderSearch;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;

/**
 * @author SCE
 * 
 *         8 juil. 2010
 */
public interface DonneeFinanciereHeaderService extends JadeApplicationService {
    /**
     * Copy toute les données fiancières sur le droit donnée en paramètre a condition qu'il avait un encien droit pour
     * le requérant
     * 
     * @param newDroit
     * @throws DonneeFinanciereException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public void copyDonneeFinanciere(Droit newDroit, Droit oldDroit,
            DroitMembreFamilleSearch newdroitMembreFamilleSearch) throws DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param sdfSearch
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(DonneeFinanciereHeaderSearch sdfSearch) throws DroitException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité donneeFinanciereHeader
     * 
     * @param donneeFinanciereHeader
     *            L'entité donneeFinanciereHeader à supprimer
     * @return L'entité donneeFinanciereHeader supprimé
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public DonneeFinanciereHeader delete(DonneeFinanciereHeader donneeFinanciereHeader) throws DroitException,
            JadePersistenceException;

    /**
     * Supprimer les données fiancière pour une version de droit
     * 
     * @param idVersionDroit
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     * @throws JadeApplicationServiceNotAvailableException
     */

    public void deleteDonneFinancierByIdVersionDroit(String idVersionDroit) throws JadePersistenceException,
            DonneeFinanciereException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en mémoire une entité donneeFinanciereHeader
     * 
     * @param iddonneeFinanciereHeader
     *            L'identifiant de la donneeFinanciereHeader à charger en mémoire
     * @return La donneeFinanciereHeader chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroutException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public DonneeFinanciereHeader read(String idDonneeFinanciereHeader) throws JadePersistenceException, DroitException;

    public DonneeFinanciereHeaderSearch search(DonneeFinanciereHeaderSearch search) throws DonneeFinanciereException,
            JadePersistenceException;

    /**
     * Permet de setter les valeurs génériques pour les données financière lors de la création.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param simpleDonneeFinanciereHeader
     * @return SimpleDonneeFinanciereHeader
     * @throws JadePersistenceException
     */
    public SimpleDonneeFinanciereHeader setDonneeFinanciereHeaderForCreation(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, SimpleDonneeFinanciereHeader simpleDonneeFinanciereHeader)
            throws JadePersistenceException;

    /**
     * Permet de définir si on veut que la donnée financières soit prise oui ou non dans le calcule On passe l'idVersion
     * droit pour vérifié que l'on trait sur le bon droit dans un bon état.
     * 
     * @param idDonneeFinanciereHeader
     * @param idVersionDroit
     * @return
     * @throws DonneeFinanciereException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws DroitException
     */
    public SimpleDonneeFinanciereHeader toggleTookInCalculating(String idDonneeFinanciereHeader, String idVersionDroit)
            throws DonneeFinanciereException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            DroitException;

    /**
     * 
     * Permet la mise à jour d'une entité donneeFinanciereHeader
     * 
     * @param donneeFinanciereHeader
     *            La donneeFinanciereHeader à mettre à jour
     * @return La ddonneeFinanciereHeader mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DroitException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DossierException
     */
    public DonneeFinanciereHeader update(DonneeFinanciereHeader donneeFinanciereHeader)
            throws JadePersistenceException, DroitException;

}
