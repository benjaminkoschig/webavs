package ch.globaz.al.business.services.models.processus;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.processus.TraitementPeriodiqueModel;
import ch.globaz.al.business.models.processus.TraitementPeriodiqueSearchModel;

/**
 * Services de persistence des traitements périodiques
 * 
 * @author GMO
 * 
 */
public interface TraitementPeriodiqueModelService extends JadeApplicationService {
    /**
     * Création d'un processus périodique selon le modèle passé en paramètre
     * 
     * @param traitementPeriodiqueModel
     *            le modèle à créér
     * @return le modèle du processus créé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TraitementPeriodiqueModel create(TraitementPeriodiqueModel traitementPeriodiqueModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Efface un processus périodique selon le modèle passé en paramètre
     * 
     * @param traitementPeriodiqueModel
     *            le modèle à effacer
     * @return le modèle effacé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TraitementPeriodiqueModel delete(TraitementPeriodiqueModel traitementPeriodiqueModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Lecture d'un processus périodique selon l'id passé en paramètre
     * 
     * @param idTraitementPeriodique
     *            l'id du processus à charger
     * @return le modèle de la configuration chargée
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TraitementPeriodiqueModel read(String idTraitementPeriodique) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche d'un traitement périodique selon les critères du searchModel
     * 
     * @param searchModel
     *            le modèle de recherche avec les critères
     * @return searchModel avec les résultats de recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TraitementPeriodiqueSearchModel search(TraitementPeriodiqueSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * @param traitementPeriodiqueModel
     *            le modèle à mettre à jour
     * @return le modèle mis à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TraitementPeriodiqueModel update(TraitementPeriodiqueModel traitementPeriodiqueModel)
            throws JadeApplicationException, JadePersistenceException;

}
