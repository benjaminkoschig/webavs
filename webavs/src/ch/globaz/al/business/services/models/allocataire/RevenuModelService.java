package ch.globaz.al.business.services.models.allocataire;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.allocataire.RevenuModel;
import ch.globaz.al.business.models.allocataire.RevenuSearchModel;

/**
 * Service de gestion de persistance des données des revenu de allocataire
 * 
 * @author PTA
 */
public interface RevenuModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'enregistrement retournés par le modèle de recherche passé en paramètre
     * 
     * @param revenuSearch
     *            Le modèle de recherche de revenus
     * @return Le nombre de revenus trouvés
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int count(RevenuSearchModel revenuSearch) throws JadePersistenceException;

    /**
     * Création d'un revenu selon le modèle passé en paramètre
     * 
     * @param revenuModel
     *            Le modèle du revenu à créer
     * @return RevenuModel Le modèle de revenu créé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RevenuModel create(RevenuModel revenuModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Suppression d'un revenu selon le modèle passée en paramètre
     * 
     * @param revenuModel
     *            Le modèle du revenu à effacer
     * @return RevenuModel Le modèle du revenu effacé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RevenuModel delete(RevenuModel revenuModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime les revenus liés à l'id de l'allocataire passé en paramètre
     * 
     * @param idAllocataire
     *            L'id de l'allocataire pour lequel il faut supprimer les revenus
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void deleteForIdAllocataire(String idAllocataire) throws JadePersistenceException, JadeApplicationException;

    /**
     * Initialise un revenuModel avec des valeurs par défaut
     * 
     * @param revenuModel
     *            Le modèle revenu à initialiser
     * @return Le modèle revenu initialisé
     * @throws JadeApplicationException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadePersistenceException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public RevenuModel initModel(RevenuModel revenuModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Lecture d'un revenu en fonction de son id passé en paramètre
     * 
     * @param idRevenuModel
     *            L'id du revenu à lire
     * @return RevenuModel Le modèle du revenu lu
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RevenuModel read(String idRevenuModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * 
     * Recherche d'un revenu selon le modèle de recherche passé en paramètre
     * 
     * @param revenuSearchModel
     *            Le modèle de recherche de revenu (contenant les critères)
     * @return Le modèle de recherche contenant les résultats
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RevenuSearchModel search(RevenuSearchModel revenuSearchModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche le dernier revenu de la personne en fonction de la date passée en paramètre
     * 
     * @param date
     *            La date à partir de laquelle chercher le dernier revenu
     * @param idAllocataire
     *            id de l'allocataire pour lequel rechercher le revenu
     * @param isConjoint
     *            indique s'il faut rechercher le revenu de l'allocataire ou celui de son conjoint
     * @return Le revenuModel représentant le résulat ou "null" si aucun revenu trouvé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RevenuModel searchDernierRevenu(String date, String idAllocataire, boolean isConjoint)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Mise à jour d'un revenu
     * 
     * @param revenuModel
     *            Le modèle revenu à mettre à jour
     * @return revenuModel Le modèle revenu mis à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public RevenuModel update(RevenuModel revenuModel) throws JadeApplicationException, JadePersistenceException;
}
