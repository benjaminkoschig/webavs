/**
 * 
 */
package ch.globaz.al.business.services.models.envoi;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.envoi.EnvoiTemplateComplexModel;
import ch.globaz.al.business.models.envoi.EnvoiTemplateComplexModelSearch;

/**
 * @author dhi
 * 
 */
public interface EnvoiTemplateComplexModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'�l�ment correspondant au mod�le de recherche
     * 
     * @param envoiTemplateSearch
     *            Le mod�le de recherche renseign�
     * @return Le mod�le de recherche renseign� et compl�t�
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me de persistence des donn�es
     * @throws JadeApplicationException
     *             Lev�e en cas de probl�me m�tier
     */
    public int count(EnvoiTemplateComplexModelSearch envoiTemplateSearch) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Cr�ation d'un �l�ment de type EnvoiTemplateComplexModel. Touche FormuleList et EnvoiTemplateSimpleModel
     * 
     * @param envoiTemplate
     *            Le mod�le de donn�es renseign�, � cr�er
     * @return Le mod�le de donn�es EnvoiTemplateComplexModel cr��
     * @throws JadeApplicationException
     *             Lev�e en cas de probl�me m�tier
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me de persistence
     */
    public EnvoiTemplateComplexModel create(EnvoiTemplateComplexModel envoiTemplate) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Effacement de type EnvoiTemplateComplexModel en db
     * 
     * @param envoiTemplate
     *            Le mod�le de donn�es renseign�, � supprimer
     * @return Le mod�le de donn�es supprim�
     * @throws JadeApplicationException
     *             Lev�e en cas de probl�me m�tier
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me de persistence
     */
    public EnvoiTemplateComplexModel delete(EnvoiTemplateComplexModel envoiTemplate) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Lecture d'un enregistrement de type EnvoiTemplateComplexModel
     * 
     * @param idEnvoiTemplateComplexModel
     *            L'id de l'enregistrement � lire
     * @return Le mod�le de donn�es renseign�
     * @throws JadeApplicationException
     *             Lev�e en cas de probl�me m�tier
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me de persistence
     */
    public EnvoiTemplateComplexModel read(String idEnvoiTemplateComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche de donn�es de type EnvoiTemplateComplexModel en fonction d'un mod�le de recherche
     * 
     * @param envoiTemplateSearch
     *            Le mod�le de recherche renseign�
     * @return Le mod�le de recherche renseign� et compl�t�
     * @throws JadeApplicationException
     *             Lev�e en cas de probl�me m�tier
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me de persistence
     */
    public EnvoiTemplateComplexModelSearch search(EnvoiTemplateComplexModelSearch envoiTemplateSearch)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Mise � jour d'un enregistrement de type EnvoiTemplateComplexModel
     * 
     * @param envoiTemplate
     *            Le mod�le de donn�es � jour, � inscrire en db
     * @return Le mod�le de donn�es mis � jour
     * @throws JadeApplicationException
     *             Lev�e en cas de probl�me m�tier
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me de persistence
     */
    public EnvoiTemplateComplexModel update(EnvoiTemplateComplexModel envoiTemplate) throws JadeApplicationException,
            JadePersistenceException;

}
