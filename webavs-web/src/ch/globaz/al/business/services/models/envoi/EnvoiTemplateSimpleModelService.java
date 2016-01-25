/**
 * 
 */
package ch.globaz.al.business.services.models.envoi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.exceptions.model.envoi.ALEnvoiTemplateException;
import ch.globaz.al.business.models.envoi.EnvoiTemplateSimpleModel;
import ch.globaz.al.business.models.envoi.EnvoiTemplateSimpleModelSearch;

/**
 * service de gestion de la persitance des donn�es pour les templates envoi AF
 * 
 * @author dhi
 */
public interface EnvoiTemplateSimpleModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'enregistrement disponibles pour un mod�le de recherche donn�
     * 
     * @param envoiTemplateSearch
     *            mod�le de recherche renseign�
     * @return Le nombre d'enregistrement qui satisfont le mod�le de recherche
     * @throws ALEnvoiTemplateException
     *             Soulev� en cas de probl�mes m�tier
     * @throws JadePersistenceException
     *             Soulev� en cas de probl�mes de persistence (data)
     */
    public int count(EnvoiTemplateSimpleModelSearch envoiTemplateSearch) throws ALEnvoiTemplateException,
            JadePersistenceException;

    /**
     * Cr�ation d'un enregistrement de type EnvoiTemplateSimpleModel en base
     * 
     * @param envoiTemplate
     *            Le mod�le de donn�es � cr�er, renseign�
     * @return Le mod�le de donn�es cr��
     * @throws ALEnvoiTemplateException
     *             Soulev� en cas de probl�mes m�tier
     * @throws JadePersistenceException
     *             Soulev� en cas de probl�mes de persistence
     */
    public EnvoiTemplateSimpleModel create(EnvoiTemplateSimpleModel envoiTemplate) throws ALEnvoiTemplateException,
            JadePersistenceException;

    /**
     * Effacement d'un enregistrement de type EnvoiTemplateSimpleModel en base
     * 
     * @param envoiTemplate
     *            Le mod�le de donn�es � supprimer
     * @return Le mod�le de donn�es supprim�
     * @throws ALEnvoiTemplateException
     *             Soulev� en cas de probl�mes m�tier
     * @throws JadePersistenceException
     *             Soulev� en cas de probl�mes de persistence
     */
    public EnvoiTemplateSimpleModel delete(EnvoiTemplateSimpleModel envoiTemplate) throws ALEnvoiTemplateException,
            JadePersistenceException;

    /**
     * Lecture d'un enregistrement de type EnvoiTemplateSimpleModel depuis la base
     * 
     * @param idEnvoiTemplate
     *            L'id de l'enregistrement � lire
     * @return Le mod�le de donn�es renseign�
     * @throws ALEnvoiTemplateException
     *             Soulev� en cas de probl�mes m�tier
     * @throws JadePersistenceException
     *             Soulev� en cas de probl�mes de persistence
     */
    public EnvoiTemplateSimpleModel read(String idEnvoiTemplate) throws ALEnvoiTemplateException,
            JadePersistenceException;

    /**
     * Recherche d'�l�ments de type EnvoiTemplateSimpleModel en base
     * 
     * @param envoiTemplateSearch
     *            Le mod�le de recherche de donn�es renseign�s
     * @return Le mod�le de recherche compl�t�
     * @throws ALEnvoiTemplateException
     *             Soulev� en cas de probl�mes m�tier
     * @throws JadePersistenceException
     *             Soulev�e en cas de probl�mes de persistence
     */
    public EnvoiTemplateSimpleModelSearch search(EnvoiTemplateSimpleModelSearch envoiTemplateSearch)
            throws ALEnvoiTemplateException, JadePersistenceException;

    /**
     * Mise � jour d'un enregistrement de type EnvoiTemplateSimpleModel en base
     * 
     * @param envoiTemplate
     *            Le mod�le de donn�es � mettre � jour
     * @return Le mod�le de donn�es mis � jour
     * @throws ALEnvoiTemplateException
     *             Soulev� en cas de probl�mes m�tier
     * @throws JadePersistenceException
     *             Soulev� en cas de probl�mes de persistence
     */
    public EnvoiTemplateSimpleModel update(EnvoiTemplateSimpleModel envoiTemplate) throws ALEnvoiTemplateException,
            JadePersistenceException;

}
