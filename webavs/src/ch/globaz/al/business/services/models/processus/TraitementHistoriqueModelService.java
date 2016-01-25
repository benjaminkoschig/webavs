package ch.globaz.al.business.services.models.processus;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.processus.TraitementHistoriqueModel;
import ch.globaz.al.business.models.processus.TraitementHistoriqueSearchModel;

public interface TraitementHistoriqueModelService extends JadeApplicationService {

    /**
     * Cr�ation d'un historique de traitement selon le mod�le pass� en param�tre
     * 
     * @param model
     *            le mod�le � cr�er
     * @return le mod�le cr��
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TraitementHistoriqueModel create(TraitementHistoriqueModel model) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Effacement d'un historique de traitement selon le mod�le pass� en param�tre
     * 
     * @param model
     *            le mod�le � mettre � jour
     * @return le mod�le effac�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TraitementHistoriqueModel delete(TraitementHistoriqueModel model) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Lecture d'un mod�le historique traitement selon l'id pass� en param�tre
     * 
     * @param idModel
     *            l'identifiant du mod�le � charger
     * @return le mod�le charg�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TraitementHistoriqueModel read(String idModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche d'un mod�le historique traitement selon le mod�le de recherche pass� en param�tre.
     * 
     * @param searchModel
     *            le mod�le de recherche contenant les crit�res
     * @return searchModel le mod�le de recherche contenant les r�sultats
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TraitementHistoriqueSearchModel search(TraitementHistoriqueSearchModel searchModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Mise � jour d'un historique de traitement selon le mod�le pass� en param�tre
     * 
     * @param model
     *            le mod�le � mettre � jour
     * @return le mod�le modifi�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public TraitementHistoriqueModel update(TraitementHistoriqueModel model) throws JadeApplicationException,
            JadePersistenceException;

}
