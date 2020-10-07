package ch.globaz.pegasus.business.services.models.creancier;

import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.models.creancier.*;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.math.BigDecimal;

public interface SimpleCreancierHystoriqueService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     *
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimpleCreancierHystoriqueSearch search) throws CreancierException, JadePersistenceException;

    /**
     * Permet la création d'une entité SimpleCreancierHystorique
     *
     * @param SimpleCreancierHystorique
     *            La SimpleCreancierHystorique à créer
     * @return SimpleCreancierHystorique créé
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleCreancierHystorique create(SimpleCreancier simpleCreancier, SimpleCreanceAccordee simpleCreanceAccordee) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'une entité creanceAccordeeHyst
     *
     * @param SimpleCreancierHystorique
     *            La creanceAccordeeHyst à supprimer
     * @return supprimé
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleCreancierHystorique delete(SimpleCreancierHystorique creanceAccordeeHyst) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;




    /**
     * Permet de charger en mémoire une creanceAccordeeHyst PC
     *
     * @param idcreanceAccordeeHyst
     *            L'identifiant de creanceAccordeeHyst à charger en mémoire
     * @return creanceAccordeeHyst chargée en mémoire
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleCreancierHystorique read(String idSimpleCreancierHystorique) throws CreancierException, JadePersistenceException;

    /**
     * Permet de chercher des SimpleCreancierHystorique selon un modèle de critères.
     *
     * @param SimpleCreancierHystorique
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleCreancierHystoriqueSearch search(SimpleCreancierHystoriqueSearch simpleCreancierHystoriqueSearch) throws CreancierException,
            JadePersistenceException;

    /**
     *
     * Permet la mise à jour d'une entité SimpleCreancierHystorique
     *
     * @param SimpleCreancierHystorique
     *            Le modele à mettre à jour
     * @return creanceAccordeeHyst mis à jour
     * @throws CreancierException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistences
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleCreancierHystorique update(SimpleCreancierHystorique creanceAccordeeHyst) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;
}
