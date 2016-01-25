package ch.globaz.perseus.business.services.models.impotsource;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.impotsource.PeriodeImpotSourceException;
import ch.globaz.perseus.business.models.impotsource.SimplePeriodeImpotSource;
import ch.globaz.perseus.business.models.impotsource.SimplePeriodeImpotSourceSearchModel;

public interface SimplePeriodeImpotSourceService extends JadeApplicationService {

    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws PeriodeImpotSourceException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimplePeriodeImpotSourceSearchModel search) throws PeriodeImpotSourceException,
            JadePersistenceException;

    /**
     * Permet la création d'une entité periodeImpotSource
     * 
     * @param periodeImpotSource
     *            La période à créer
     * @return La période créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PeriodeImpotSourceException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimplePeriodeImpotSource create(SimplePeriodeImpotSource periodeImpotSource)
            throws PeriodeImpotSourceException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité période impot source
     * 
     * @param periodeImpotSource
     *            La période à supprimer
     * @return La période supprimé
     * @throws PeriodeImpotSourceException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimplePeriodeImpotSource delete(SimplePeriodeImpotSource periodeImpotSource)
            throws PeriodeImpotSourceException, JadePersistenceException;

    /**
     * Permet de charger en mémoire une période d'impot à la source
     * 
     * @param idPériodeImpotSource
     *            L'identifiant de la période à charger en mémoire
     * @return La période chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PeriodeImpotSourceException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimplePeriodeImpotSource read(String idPeriodeImpotSource) throws PeriodeImpotSourceException,
            JadePersistenceException;

    /**
     * Permet de chercher des période d'impot à la source selon un modèle de critères.
     * 
     * @param SimplePeriodeImpotSourceSearchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimplePeriodeImpotSourceSearchModel search(
            SimplePeriodeImpotSourceSearchModel simplePeriodeImpotSourceSearchModel)
            throws PeriodeImpotSourceException, JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité période d'impot à la source
     * 
     * @param periodeImpotSource
     *            La période à mettre à jour
     * @return La période mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PeriodeImpotSourceException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimplePeriodeImpotSource update(SimplePeriodeImpotSource periodeImpotSource)
            throws PeriodeImpotSourceException, JadePersistenceException;

}
