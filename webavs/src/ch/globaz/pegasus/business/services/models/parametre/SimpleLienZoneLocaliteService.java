package ch.globaz.pegasus.business.services.models.parametre;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.models.parametre.SimpleLienZoneLocalite;
import ch.globaz.pegasus.business.models.parametre.SimpleLienZoneLocaliteSearch;

public interface SimpleLienZoneLocaliteService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimpleLienZoneLocaliteSearch search) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException;

    /**
     * Permet la création d'une entité SimpleLienZoneLocalite
     * 
     * @param SimpleLienZoneLocalite
     *            La simpleLienZoneLocalite métier à créer
     * @return simpleLienZoneLocalite créé
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleLienZoneLocalite create(SimpleLienZoneLocalite simpleLienZoneLocalite)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'une entité simpleLienZoneLocalite
     * 
     * @param SimpleLienZoneLocalite
     *            La simpleLienZoneLocalite métier à supprimer
     * @return supprimé
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleLienZoneLocalite delete(SimpleLienZoneLocalite simpleLienZoneLocalite)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException;

    /**
     * Permet de charger en mémoire une simpleLienZoneLocalite PC
     * 
     * @param idsimpleLienZoneLocalite
     *            L'identifiant de simpleLienZoneLocalite à charger en mémoire
     * @return simpleLienZoneLocalite chargée en mémoire
     * @throws exeeForfaitsPrimesAssuranceMaladieExceptioneeee
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleLienZoneLocalite read(String idSimpleLienZoneLocalite) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException;

    /**
     * Permet de chercher des SimpleLienZoneLocalite selon un modèle de critères.
     * 
     * @param simpleLienZoneLocaliteSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleLienZoneLocaliteSearch search(SimpleLienZoneLocaliteSearch simpleLienZoneLocaliteSearch)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleLienZoneLocalite
     * 
     * @param SimpleLienZoneLocalite
     *            Le modele à mettre à jour
     * @return simpleLienZoneLocalite mis à jour
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleLienZoneLocalite update(SimpleLienZoneLocalite simpleLienZoneLocalite)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

}