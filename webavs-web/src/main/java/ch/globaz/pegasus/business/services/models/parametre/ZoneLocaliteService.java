package ch.globaz.pegasus.business.services.models.parametre;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.models.parametre.ZoneLocalite;
import ch.globaz.pegasus.business.models.parametre.ZoneLocaliteSearch;

public interface ZoneLocaliteService extends JadeApplicationService {
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
    public int count(ZoneLocaliteSearch search) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException;

    /**
     * Permet la création d'une entité ZoneLocalite
     * 
     * @param ZoneLocalite
     *            La zoneLocalite à créer
     * @return zoneLocalite créé
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public ZoneLocalite create(ZoneLocalite zoneLocalite) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'une entité zoneLocalite
     * 
     * @param ZoneLocalite
     *            La zoneLocalite métier à supprimer
     * @return supprimé
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public ZoneLocalite delete(ZoneLocalite zoneLocalite) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en mémoire une zoneLocalite PC
     * 
     * @param idzoneLocalite
     *            L'identifiant de zoneLocalite à charger en mémoire
     * @return zoneLocalite chargée en mémoire
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public ZoneLocalite read(String idZoneLocalite) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException;

    /**
     * Permet de chercher des ZoneLocalite selon un modèle de critères.
     * 
     * @param zoneLocaliteSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public ZoneLocaliteSearch search(ZoneLocaliteSearch zoneLocaliteSearch)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité ZoneLocalite
     * 
     * @param ZoneLocalite
     *            Le modele à mettre à jour
     * @return zoneLocalite mis à jour
     * @throws ForfaitsPrimesAssuranceMaladieException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public ZoneLocalite update(ZoneLocalite zoneLocalite) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

}
