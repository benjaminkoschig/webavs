package ch.globaz.al.business.services.models.processus;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.processus.TraitementHistoriqueModel;
import ch.globaz.al.business.models.processus.TraitementHistoriqueSearchModel;

public interface TraitementHistoriqueModelService extends JadeApplicationService {

    /**
     * Création d'un historique de traitement selon le modèle passé en paramètre
     * 
     * @param model
     *            le modèle à créer
     * @return le modèle créé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TraitementHistoriqueModel create(TraitementHistoriqueModel model) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Effacement d'un historique de traitement selon le modèle passé en paramètre
     * 
     * @param model
     *            le modèle à mettre à jour
     * @return le modèle effacé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TraitementHistoriqueModel delete(TraitementHistoriqueModel model) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Lecture d'un modèle historique traitement selon l'id passé en paramètre
     * 
     * @param idModel
     *            l'identifiant du modèle à charger
     * @return le modèle chargé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TraitementHistoriqueModel read(String idModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche d'un modèle historique traitement selon le modèle de recherche passé en paramètre.
     * 
     * @param searchModel
     *            le modèle de recherche contenant les critères
     * @return searchModel le modèle de recherche contenant les résultats
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TraitementHistoriqueSearchModel search(TraitementHistoriqueSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Mise à jour d'un historique de traitement selon le modèle passé en paramètre
     * 
     * @param model
     *            le modèle à mettre à jour
     * @return le modèle modifié
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TraitementHistoriqueModel update(TraitementHistoriqueModel model) throws JadeApplicationException,
            JadePersistenceException;

}
