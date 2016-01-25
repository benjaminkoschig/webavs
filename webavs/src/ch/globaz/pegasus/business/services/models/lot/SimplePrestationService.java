package ch.globaz.pegasus.business.services.models.lot;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.lot.SimplePrestation;
import ch.globaz.pegasus.business.models.lot.SimplePrestationSearch;

public interface SimplePrestationService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param SimplePrestationSearch
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws PrestationException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimplePrestationSearch search) throws PrestationException, JadePersistenceException;

    /**
     * Permet la création d'une entité Prestation
     * 
     * @param SimplePrestation
     *            La simplePresation à créer
     * @return simplePresation créé
     * @throws PrestationException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimplePrestation create(SimplePrestation simplePrestation) throws PrestationException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'une entité simplePresation
     * 
     * @param SimplePrestation
     *            La simplePresation à supprimer
     * @return supprimé
     * @throws PrestationException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimplePrestation delete(SimplePrestation simplePrestation) throws PrestationException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire une simplePresation PC
     * 
     * @param idSimplePrestation
     *            L'identifiant de simplePresation à charger en mémoire
     * @return simplePresation chargée en mémoire
     * @throws PrestationException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimplePrestation read(String idPrestation) throws PrestationException, JadePersistenceException;

    /**
     * Permet de chercher des Prestation selon un modèle de critères.
     * 
     * @param simplePresationSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws PrestationException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimplePrestationSearch search(SimplePrestationSearch simplePresationSearch) throws PrestationException,
            JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité Prestation
     * 
     * @param SimplePrestation
     *            Le modele à mettre à jour
     * @return simplePresation mis à jour
     * @throws PrestationException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimplePrestation update(SimplePrestation simplePrestation) throws PrestationException,
            JadePersistenceException;

    public abstract int delete(SimplePrestationSearch simplePrestationSearch) throws PrestationException,
            JadePersistenceException;

    ArrayList<String> getIdsPrestationsByLot(String idLot) throws PrestationException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

}