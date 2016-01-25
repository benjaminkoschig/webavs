package ch.globaz.amal.business.services.models.detailfamille;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.exceptions.models.documents.DocumentException;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;

/**
 * @author CBU
 */
public interface SimpleDetailFamilleService extends JadeApplicationService {

    /**
     * Permet de compter le nombre de subsides
     * 
     * @param detailFamille
     *            le detailFamille a compter
     * @return le nombre de detailFamille
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DetailFamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public int count(SimpleDetailFamilleSearch detailFamilleSearch) throws JadePersistenceException,
            DetailFamilleException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet la création d'un detailFamille
     * 
     * @param detailFamille
     *            le detailFamille a créer
     * @return le detailFamille crée
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DetailFamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleDetailFamille create(SimpleDetailFamille detailFamille) throws JadePersistenceException,
            DetailFamilleException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'une entité detailFamille
     * 
     * @param detailFamille
     *            Le detailFamille à supprimer
     * @return Le detailFamille supprimé
     * @throws DetailFamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDetailFamille delete(SimpleDetailFamille detailFamille) throws DetailFamilleException,
            JadePersistenceException, DocumentException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en mémoire un detailFamille
     * 
     * @param idDetailFamille
     *            L'identifiant du detailFamille à charger en mémoire
     * @return Le detailFamille chargé en mémoire
     * @throws DetailFamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDetailFamille read(String idDetailFamille) throws DetailFamilleException, JadePersistenceException;

    /**
     * 
     * Permet la recherche de subsides (detailFamille) selon un modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return modèle de recherche renseigné
     * @throws JadePersistenceException
     * @throws DetailFamilleException
     */
    public SimpleDetailFamilleSearch search(SimpleDetailFamilleSearch search) throws JadePersistenceException,
            DetailFamilleException;

    /**
     * Permet la mise à jour d'une entité detailFamille
     * 
     * @param detailFamille
     *            Le detailFamille à mettre à jour
     * @return Le detailFamille mis à jour
     * @throws DetailFamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleDetailFamille update(SimpleDetailFamille detailFamille) throws DetailFamilleException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

}
