package ch.globaz.al.business.services.models.processus;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.processus.ProcessusPeriodiqueModel;
import ch.globaz.al.business.models.processus.ProcessusPeriodiqueSearchModel;

/**
 * Services de persistence des processus p�riodique
 * 
 * @author GMO
 * 
 */
public interface ProcessusPeriodiqueModelService extends JadeApplicationService {
    /**
     * Cr�ation d'un processus p�riodique selon le mod�le pass� en param�tre
     * 
     * @param processusPeriodiqueModel
     *            le mod�le � cr��r
     * @return le mod�le du processus cr��
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ProcessusPeriodiqueModel create(ProcessusPeriodiqueModel processusPeriodiqueModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Efface un processus p�riodique selon le mod�le pass� en param�tre
     * 
     * @param processusPeriodiqueModel
     *            le mod�le � effacer
     * @return le mod�le effac�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ProcessusPeriodiqueModel delete(ProcessusPeriodiqueModel processusPeriodiqueModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Lecture d'un processus p�riodique selon l'id pass� en param�tre
     * 
     * @param idProcessusPeriodique
     *            l'id du processus � charger
     * @return le mod�le de la configuration charg�e
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ProcessusPeriodiqueModel read(String idProcessusPeriodique) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche les processus p�riodiques selon les crit�res du mod�le de recherche pass� en param�tre
     * 
     * @param searchModel
     *            le mod�le de recherche avec les crit�res
     * @return le mod�le de recherche avec les r�sultats
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ProcessusPeriodiqueSearchModel search(ProcessusPeriodiqueSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * @param processusPeriodiqueModel
     *            le mod�le � mettre � jour
     * @return le mod�le mis � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ProcessusPeriodiqueModel update(ProcessusPeriodiqueModel processusPeriodiqueModel)
            throws JadeApplicationException, JadePersistenceException;

}
