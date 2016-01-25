package ch.globaz.pegasus.business.services.models.dossier;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.models.dossier.Dossier;
import ch.globaz.pegasus.business.models.dossier.DossierRCListSearch;
import ch.globaz.pegasus.business.models.dossier.DossierSearch;
import ch.globaz.prestation.business.exceptions.models.DemandePrestationException;

public interface DossierService extends JadeApplicationService {

    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws DossierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(DossierSearch search) throws DossierException, JadePersistenceException;

    /**
     * Permet la création d'une entité dossier
     * 
     * @param dossier
     *            Le dossier à créer
     * @return Le dossier créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DossierException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Dossier create(Dossier dossier) throws JadePersistenceException, DossierException,
            DemandePrestationException;

    public Dossier delete(Dossier dossier) throws DossierException, JadePersistenceException;

    /**
     * Permet de charger en mémoire un dossier
     * 
     * @param idDossier
     *            L'identifiant du dossier à charger en mémoire
     * @return Le dossier chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DossierException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Dossier read(String idDossier) throws JadePersistenceException, DossierException;

    /**
     * Permet de chercher des dossiers selon un modèle de critères.
     * 
     * @param dossierSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DossierException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public DossierSearch search(DossierSearch dossierSearch) throws JadePersistenceException, DossierException;

    /**
     * Permet de chercher des dossiers selon un modèle de critères.
     * 
     * @param dossierSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats (DossierRcList)
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DossierException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public DossierRCListSearch searchRCList(DossierRCListSearch dossierSearch) throws JadePersistenceException,
            DossierException;

    /**
     * 
     * Permet la mise à jour d'une entité dossier
     * 
     * @param dossier
     *            Le dossier à mettre à jour
     * @return Le dossier mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DossierException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Dossier update(Dossier dossier) throws JadePersistenceException, DossierException;
}
