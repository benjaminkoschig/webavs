/**
 * 
 */
package ch.globaz.pegasus.business.services.models.dessaisissement;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.conversionRente.ConversionRenteException;
import ch.globaz.pegasus.business.exceptions.models.dessaisissement.DessaisissementFortuneException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.dessaisissement.CalculContrePresation;
import ch.globaz.pegasus.business.models.dessaisissement.CalculContreprestationContainer;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortune;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortuneSearch;

/**
 * @author BSC
 * 
 */
public interface DessaisissementFortuneService extends JadeApplicationService {

    /**
     * Permet de caculer la contre prestation
     * 
     * @param DessaisissementFortune
     * 
     * @return calculContrePresation avec les valeurs setter
     * @throws DessaisissementFortuneException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public CalculContrePresation calculContrePresation(CalculContreprestationContainer calculCPContainer)
            throws ConversionRenteException, DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException;

    /**
     * Permet de caculer la contre prestation
     * 
     * @param DessaisissementFortune
     * 
     * @return calculContrePresation avec les valeurs setter
     * @throws DessaisissementFortuneException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public CalculContrePresation calculContrePresation(DessaisissementFortune dessaisissementFortune)
            throws ConversionRenteException, DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException;

    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws DessaisissementFortuneException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(DessaisissementFortuneSearch search) throws DessaisissementFortuneException,
            JadePersistenceException;

    /**
     * Permet la création d'une entité bétail
     * 
     * @param betail
     *            L'entité bétail à créer
     * @return L'entité bétail créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DessaisissementFortuneException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public DessaisissementFortune create(DessaisissementFortune betail) throws JadePersistenceException,
            DessaisissementFortuneException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité bétail
     * 
     * @param betail
     *            L'entité bétail à supprimer
     * @return L'entité bétail supprimé
     * @throws DessaisissementFortuneException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public DessaisissementFortune delete(DessaisissementFortune betail) throws DessaisissementFortuneException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité betail
     * 
     * @param idDessaisissementFortune
     *            L'identifiant de l'entité bétail à charger en mémoire
     * @return L'entité bétail chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DessaisissementFortuneException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public DessaisissementFortune read(String idDessaisissementFortune) throws JadePersistenceException,
            DessaisissementFortuneException;

    /**
     * Chargement d'une DessaisissementFortune via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws DessaisissementFortuneSearch
     *             Exception
     * @throws JadePersistenceException
     */
    public DessaisissementFortune readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws DessaisissementFortuneException, JadePersistenceException;

    /**
     * Permet de chercher des bétail selon un modèle de critères.
     * 
     * @param betailSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DessaisissementFortuneException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public DessaisissementFortuneSearch search(DessaisissementFortuneSearch betailSearch)
            throws JadePersistenceException, DessaisissementFortuneException;

    /**
     * 
     * Permet la mise à jour d'une entité bétail
     * 
     * @param betail
     *            L'entité bétail à mettre à jour
     * @return L'entité bétail mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DessaisissementFortuneException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public DessaisissementFortune update(DessaisissementFortune betail) throws JadePersistenceException,
            DessaisissementFortuneException, DonneeFinanciereException;
}
