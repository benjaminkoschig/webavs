package ch.globaz.al.business.services.models.processus;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.processus.ProcessusPeriodiqueModel;
import ch.globaz.al.business.models.processus.ProcessusPeriodiqueSearchModel;

/**
 * Services de persistence des processus périodique
 * 
 * @author GMO
 * 
 */
public interface ProcessusPeriodiqueModelService extends JadeApplicationService {
    /**
     * Création d'un processus périodique selon le modèle passé en paramètre
     * 
     * @param processusPeriodiqueModel
     *            le modèle à créér
     * @return le modèle du processus créé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ProcessusPeriodiqueModel create(ProcessusPeriodiqueModel processusPeriodiqueModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Efface un processus périodique selon le modèle passé en paramètre
     * 
     * @param processusPeriodiqueModel
     *            le modèle à effacer
     * @return le modèle effacé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ProcessusPeriodiqueModel delete(ProcessusPeriodiqueModel processusPeriodiqueModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Lecture d'un processus périodique selon l'id passé en paramètre
     * 
     * @param idProcessusPeriodique
     *            l'id du processus à charger
     * @return le modèle de la configuration chargée
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ProcessusPeriodiqueModel read(String idProcessusPeriodique) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche les processus périodiques selon les critères du modèle de recherche passé en paramètre
     * 
     * @param searchModel
     *            le modèle de recherche avec les critères
     * @return le modèle de recherche avec les résultats
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ProcessusPeriodiqueSearchModel search(ProcessusPeriodiqueSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * @param processusPeriodiqueModel
     *            le modèle à mettre à jour
     * @return le modèle mis à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ProcessusPeriodiqueModel update(ProcessusPeriodiqueModel processusPeriodiqueModel)
            throws JadeApplicationException, JadePersistenceException;

}
