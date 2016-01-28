package ch.globaz.pegasus.business.services.models.habitat;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.exceptions.models.habitat.TaxeJournaliereHomeException;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.exceptions.models.home.PrixChambreException;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.habitat.Habitat;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHome;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHomeSearch;
import ch.globaz.pegasus.business.models.home.PrixChambre;
import ch.globaz.pegasus.business.models.home.TypeChambre;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface TaxeJournaliereHomeService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws LoyerException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(TaxeJournaliereHomeSearch search) throws TaxeJournaliereHomeException, JadePersistenceException;

    /**
     * Permet la création d'une entité simpleIndemniteJournaliere.
     * 
     * @param taxeJournaliereHome
     *            La renteAvsAi à créer
     * @return le taxeJournaliereHome créé
     * @throws TaxeJournaliereHomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public TaxeJournaliereHome create(TaxeJournaliereHome taxeJournaliereHome) throws TaxeJournaliereHomeException,
            DonneeFinanciereException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité simpleIndemniteJournaliere
     * 
     * @param taxeJournaliereHome
     *            le taxeJournaliereHome à supprimer
     * @return le taxeJournaliereHome supprimé
     * @throws TaxeJournaliereHomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DonneeFinanciereException
     */
    public TaxeJournaliereHome delete(TaxeJournaliereHome taxeJournaliereHome) throws TaxeJournaliereHomeException,
            JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet de trouver le prix d'une chambre en fonction d'une donné financière
     * 
     * @param SimpleDonneeFinanciereHeader
     *            donnée financière permet d'avoir la periode
     * @return le PrixChambre
     */
    public PrixChambre getPrixTypeChambre(SimpleDonneeFinanciereHeader donneeFinanciereHeader, TypeChambre typeChambre)
            throws PrixChambreException, HomeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException;

    /**
     * Retourne le prix de la chambre qui se trouve dans la map;
     * 
     * @param taxeJournaliereHome
     *            le taxeJournaliereHome
     */
    public String getPrixTypeChambreInMap(TaxeJournaliereHome taxeJournaliereHome);

    /**
     * Met le prix trouvé en fonction du paramétre habitat dans une map
     * 
     * @param Habitat
     *            le taxeJournaliereHome à mettre à jour
     */
    public void putPrixTypeChambreInMap(Habitat habitat) throws PrixChambreException, HomeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Permet de charger en mémoire un versionDroit
     * 
     * @param idTaxeJournaliereHome
     *            L'identifiant de le taxeJournaliereHomee à charger en mémoire
     * @return le taxeJournaliereHome chargé en mémoire
     * @throws TaxeJournaliereHomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public TaxeJournaliereHome read(String idTaxeJournaliereHome) throws TaxeJournaliereHomeException,
            JadePersistenceException;

    /**
     * Chargement d'une TaxeJournaliereHome via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws TaxeJournaliereHomeException
     * @throws JadePersistenceException
     */
    public TaxeJournaliereHome readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws TaxeJournaliereHomeException, JadePersistenceException;

    /**
     * Permet la recherche d'après les paramètres de recherche
     * 
     * @param taxeJournaliereHomeSearch
     * @return La recherche effectué
     * @throws TaxeJournaliereHomeException
     * @throws DonneeFinanciereException
     * @throws JadePersistenceException
     */

    public TaxeJournaliereHomeSearch search(TaxeJournaliereHomeSearch taxeJournaliereHomeSearch)
            throws TaxeJournaliereHomeException, DonneeFinanciereException, JadePersistenceException;

    /**
     * Permet la mise à jour d'une entité simpleIndemniteJournaliere
     * 
     * @param taxeJournaliereHome
     *            le taxeJournaliereHome à mettre à jour
     * @return le taxeJournaliereHome mis à jour
     * @throws TaxeJournaliereHomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public TaxeJournaliereHome update(TaxeJournaliereHome taxeJournaliereHome) throws TaxeJournaliereHomeException,
            DonneeFinanciereException, JadePersistenceException;

}
