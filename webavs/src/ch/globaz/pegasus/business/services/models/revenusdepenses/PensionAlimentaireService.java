package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.PensionAlimentaireException;
import ch.globaz.pegasus.business.models.revenusdepenses.PensionAlimentaire;
import ch.globaz.pegasus.business.models.revenusdepenses.PensionAlimentaireSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface PensionAlimentaireService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws PensionAlimentaireException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(PensionAlimentaireSearch search) throws PensionAlimentaireException, JadePersistenceException;

    /**
     * Permet la création d'une entité PensionAlimentaire
     * 
     * @param PensionAlimentaire
     *            L'entité PensionAlimentaire à créer
     * @return L'entité PensionAlimentaire créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PensionAlimentaireException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PensionAlimentaire create(PensionAlimentaire pensionAlimentaire) throws JadePersistenceException,
            PensionAlimentaireException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité PensionAlimentaire
     * 
     * @param PensionAlimentaire
     *            L'entité PensionAlimentaire à supprimer
     * @return L'entité PensionAlimentaire supprimé
     * @throws PensionAlimentaireException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public PensionAlimentaire delete(PensionAlimentaire pensionAlimentaire) throws PensionAlimentaireException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité PensionAlimentaire
     * 
     * @param idPensionAlimentaire
     *            L'identifiant de l'entité PensionAlimentaire à charger en mémoire
     * @return L'entité PensionAlimentaire chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PensionAlimentaireException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PensionAlimentaire read(String idPensionAlimentaire) throws JadePersistenceException,
            PensionAlimentaireException;

    /**
     * Chargement d'une PensionAlimentaireSearch via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws PensionAlimentaireSearch
     *             Exception
     * @throws JadePersistenceException
     */
    public PensionAlimentaire readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws PensionAlimentaireException, JadePersistenceException;

    /**
     * Permet de chercher des PensionAlimentaire selon un modèle de critères.
     * 
     * @param PensionAlimentaireSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PensionAlimentaireException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PensionAlimentaireSearch search(PensionAlimentaireSearch pensionAlimentaireSearch)
            throws JadePersistenceException, PensionAlimentaireException;

    /**
     * 
     * Permet la mise à jour d'une entité PensionAlimentaire
     * 
     * @param PensionAlimentaire
     *            L'entité PensionAlimentaire à mettre à jour
     * @return L'entité PensionAlimentaire mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PensionAlimentaireException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public PensionAlimentaire update(PensionAlimentaire pensionAlimentaire) throws JadePersistenceException,
            PensionAlimentaireException, DonneeFinanciereException;
}