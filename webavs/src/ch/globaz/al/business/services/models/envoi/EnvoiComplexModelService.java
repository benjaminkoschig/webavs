/**
 * 
 */
package ch.globaz.al.business.services.models.envoi;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.envoi.EnvoiComplexModel;
import ch.globaz.al.business.models.envoi.EnvoiComplexModelSearch;

/**
 * @author dhi
 * 
 */
public interface EnvoiComplexModelService extends JadeApplicationService {

    /**
     * Permet de compter le nombre de r�sultats suivant le mod�le de recherche
     * 
     * @param envoiComplexSearch
     *            mod�le de recherche renseign�
     * @return mod�le de recherche renseign� et compl�t�
     * @throws JadePersistenceException
     *             lev�e en cas de probl�me de persistence
     * @throws JadeApplicationException
     *             lev�e ne cas de probl�me m�tier
     */
    public int count(EnvoiComplexModelSearch envoiComplexSearch) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Permet d'effacer un enregistrement envoi particulier
     * 
     * @param envoiComplex
     *            mod�le de donn�es renseign�
     * @return
     * @throws JadeApplicationException
     *             lev�e en cas de probl�me m�tier
     * @throws JadePersistenceException
     *             lev�e en cas de probl�me de persistence
     */
    public EnvoiComplexModel delete(EnvoiComplexModel envoiComplex) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Lecture et mise en m�moire des informations relatives � un envoi
     * 
     * @param idEnvoiComplexModel
     *            id de l'envoi � lire
     * @return un mod�le de donn�es renseign�
     * @throws JadeApplicationException
     *             lev�e en cas de probl�me m�tier
     * @throws JadePersistenceException
     *             lev�e en cas de probl�me de persistence
     */
    public EnvoiComplexModel read(String idEnvoiComplexModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche d'envois en fonction d'un mod�le de recherche renseign�
     * 
     * @param envoiComplexSearch
     *            le mod�le de recherche renseign�
     * @return le mod�le de recherche renseign� et compl�t�
     * @throws JadeApplicationException
     *             lev�e en cas de probl�me m�tier
     * @throws JadePersistenceException
     *             lev�e en cas de probl�me de persistence
     */
    public EnvoiComplexModelSearch search(EnvoiComplexModelSearch envoiComplexSearch) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Mise � jour d'un envoi complex
     * 
     * @param envoiComplex
     *            le mod�le � mettre � jour
     * @return le mod�le mis � jour
     * @throws JadeApplicationException
     *             lev�e en cas de probl�me m�tier
     * @throws JadePersistenceException
     *             lev�e en cas de probl�me de persistence
     */
    public EnvoiComplexModel update(EnvoiComplexModel envoiComplex) throws JadeApplicationException,
            JadePersistenceException;

}
