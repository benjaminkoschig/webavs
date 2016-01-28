package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuActiviteLucrativeDependanteException;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeDependante;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeDependanteSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

public interface RevenuActiviteLucrativeDependanteService extends JadeApplicationService,
        AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws RevenuActiviteLucrativeDependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(RevenuActiviteLucrativeDependanteSearch search) throws RevenuActiviteLucrativeDependanteException,
            JadePersistenceException;

    /**
     * Permet la création d'une entité RevenuActiviteLucrativeDependante
     * 
     * @param RevenuActiviteLucrativeDependante
     *            L'entité RevenuActiviteLucrativeDependante à créer
     * @return L'entité RevenuActiviteLucrativeDependante créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuActiviteLucrativeDependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public RevenuActiviteLucrativeDependante create(RevenuActiviteLucrativeDependante revenuActiviteLucrativeDependante)
            throws JadePersistenceException, RevenuActiviteLucrativeDependanteException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité RevenuActiviteLucrativeDependante
     * 
     * @param RevenuActiviteLucrativeDependante
     *            L'entité RevenuActiviteLucrativeDependante à supprimer
     * @return L'entité RevenuActiviteLucrativeDependante supprimé
     * @throws RevenuActiviteLucrativeDependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public RevenuActiviteLucrativeDependante delete(RevenuActiviteLucrativeDependante revenuActiviteLucrativeDependante)
            throws RevenuActiviteLucrativeDependanteException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> idsDonneFinanciere)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            RevenuActiviteLucrativeDependanteException;

    /**
     * Permet de charger en mémoire d'une entité RevenuActiviteLucrativeDependante
     * 
     * @param idRevenuActiviteLucrativeDependante
     *            L'identifiant de l'entité RevenuActiviteLucrativeDependante à charger en mémoire
     * @return L'entité RevenuActiviteLucrativeDependante chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuActiviteLucrativeDependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public RevenuActiviteLucrativeDependante read(String idRevenuActiviteLucrativeDependante)
            throws JadePersistenceException, RevenuActiviteLucrativeDependanteException;

    /**
     * Chargement d'une RevenuActiviteLucrativeDependante via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws RevenuActiviteLucrativeDependanteException
     * @throws JadePersistenceException
     */
    public RevenuActiviteLucrativeDependante readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws RevenuActiviteLucrativeDependanteException, JadePersistenceException;

    /**
     * Permet de chercher des RevenuActiviteLucrativeDependante selon un modèle de critères.
     * 
     * @param RevenuActiviteLucrativeDependanteSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuActiviteLucrativeDependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public RevenuActiviteLucrativeDependanteSearch search(
            RevenuActiviteLucrativeDependanteSearch revenuActiviteLucrativeDependanteSearch)
            throws JadePersistenceException, RevenuActiviteLucrativeDependanteException;

    /**
     * 
     * Permet la mise à jour d'une entité RevenuActiviteLucrativeDependante
     * 
     * @param RevenuActiviteLucrativeDependante
     *            L'entité RevenuActiviteLucrativeDependante à mettre à jour
     * @return L'entité RevenuActiviteLucrativeDependante mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuActiviteLucrativeDependanteException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public RevenuActiviteLucrativeDependante update(RevenuActiviteLucrativeDependante revenuActiviteLucrativeDependante)
            throws JadePersistenceException, RevenuActiviteLucrativeDependanteException, DonneeFinanciereException;
}