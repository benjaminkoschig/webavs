package ch.globaz.al.business.services.models.processus;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.processus.TraitementPeriodiqueModel;
import ch.globaz.al.business.models.processus.TraitementPeriodiqueSearchModel;

/**
 * Services de persistence des traitements p�riodiques
 * 
 * @author GMO
 * 
 */
public interface TraitementPeriodiqueModelService extends JadeApplicationService {
    /**
     * Cr�ation d'un processus p�riodique selon le mod�le pass� en param�tre
     * 
     * @param traitementPeriodiqueModel
     *            le mod�le � cr��r
     * @return le mod�le du processus cr��
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TraitementPeriodiqueModel create(TraitementPeriodiqueModel traitementPeriodiqueModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Efface un processus p�riodique selon le mod�le pass� en param�tre
     * 
     * @param traitementPeriodiqueModel
     *            le mod�le � effacer
     * @return le mod�le effac�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TraitementPeriodiqueModel delete(TraitementPeriodiqueModel traitementPeriodiqueModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Lecture d'un processus p�riodique selon l'id pass� en param�tre
     * 
     * @param idTraitementPeriodique
     *            l'id du processus � charger
     * @return le mod�le de la configuration charg�e
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TraitementPeriodiqueModel read(String idTraitementPeriodique) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche d'un traitement p�riodique selon les crit�res du searchModel
     * 
     * @param searchModel
     *            le mod�le de recherche avec les crit�res
     * @return searchModel avec les r�sultats de recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TraitementPeriodiqueSearchModel search(TraitementPeriodiqueSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * @param traitementPeriodiqueModel
     *            le mod�le � mettre � jour
     * @return le mod�le mis � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TraitementPeriodiqueModel update(TraitementPeriodiqueModel traitementPeriodiqueModel)
            throws JadeApplicationException, JadePersistenceException;

}
