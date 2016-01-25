package ch.globaz.al.business.services.models.attribut;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.attribut.AttributEntiteModel;
import ch.globaz.al.business.models.attribut.AttributEntiteSearchModel;

/**
 * Service de gestion de persistance des attributs liés à une entité précise
 * 
 * @author GMO
 * 
 */
public interface AttributEntiteModelService extends JadeApplicationService {
    /**
     * Création d'un attribut entité selon le modèle passée en paramètre
     * 
     * @param attributEntiteModel
     *            Le modèle attribut entité à créer
     * @return AttributEntiteModel Le modèle attribut entité créé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public AttributEntiteModel create(AttributEntiteModel attributEntiteModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Suppression d'un attribut entité selon le modèle passée en paramètre
     * 
     * @param attributEntiteModel
     *            Le modèle attribut entité à effacer
     * @return AttributEntiteModel Le modèle attribut entité effacé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public AttributEntiteModel delete(AttributEntiteModel attributEntiteModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche l'attribut voulu pour un affilié donné
     * 
     * @param nomAttr
     *            l'attribut recherché
     * @param idAffiliation
     *            l'id de l'affiliation
     * @return le modèle de l'attribut
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AttributEntiteModel getAttributAffilie(String nomAttr, String idAffiliation)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche l'attribut voulu pour un affilié donné
     * 
     * @param nomAttr
     *            l'attribut recherché
     * @param numeroAffilie
     *            le numéro de l'affilié dont on veut l'attribut
     * @return le modèle de l'attribut
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AttributEntiteModel getAttributAffilieByNumAffilie(String nomAttr, String numeroAffilie)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche d'un attribut selon les critères du modèle de recherche passé en paramètre
     * 
     * @param searchModel
     *            Le modèle de recherche contenant les critères
     * @return Le modèle de recherche contenant les résultats
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AttributEntiteSearchModel search(AttributEntiteSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Modification d'un attribut entité selon le modèle passée en paramètre
     * 
     * @param attributEntiteModel
     *            Le modèle attribut entité à modifier
     * @return AttributEntiteModel Le modèle attribut entité modifié
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public AttributEntiteModel update(AttributEntiteModel attributEntiteModel) throws JadeApplicationException,
            JadePersistenceException;

}
