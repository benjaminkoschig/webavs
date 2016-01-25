/**
 * 
 */
package ch.globaz.al.business.services.models.envoi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.exceptions.model.envoi.ALEnvoiJobException;
import ch.globaz.al.business.models.envoi.EnvoiJobSimpleModel;
import ch.globaz.al.business.models.envoi.EnvoiJobSimpleModelSearch;

/**
 * service de gestion de la persitance des donn�es pour les jobs envoi AF
 * 
 * @author dhi
 */
public interface EnvoiJobSimpleModelService extends JadeApplicationService {
    /**
     * Compte le nombre d'enregistrement disponibles pour un mod�le de recherche donn�
     * 
     * @param envoiJobSearch
     *            mod�le de recherche renseign�
     * @return Le nombre d'enregistrement qui satisfont le mod�le de recherche
     * @throws ALEnvoiJobException
     *             Soulev� en cas de probl�mes m�tier
     * @throws JadePersistenceException
     *             Soulev� en cas de probl�mes de persistence (data)
     */
    public int count(EnvoiJobSimpleModelSearch envoiJobSearch) throws ALEnvoiJobException, JadePersistenceException;

    /**
     * Cr�ation d'un enregistrement de type EnvoiJobSimpleModel en base
     * 
     * @param envoiJob
     *            Le mod�le de donn�es � cr�er, renseign�
     * @return Le mod�le de donn�es cr��
     * @throws ALEnvoiJobException
     *             Soulev� en cas de probl�mes m�tier
     * @throws JadePersistenceException
     *             Soulev� en cas de probl�mes de persistence
     */
    public EnvoiJobSimpleModel create(EnvoiJobSimpleModel envoiJob) throws ALEnvoiJobException,
            JadePersistenceException;

    /**
     * Effacement d'un enregistrement de type EnvoiJobSimpleModel en base
     * 
     * @param envoiJob
     *            Le mod�le de donn�es � supprimer
     * @return Le mod�le de donn�es supprim�
     * @throws ALEnvoiJobException
     *             Soulev� en cas de probl�mes m�tier
     * @throws JadePersistenceException
     *             Soulev� en cas de probl�mes de persistence
     */
    public EnvoiJobSimpleModel delete(EnvoiJobSimpleModel envoiJob) throws ALEnvoiJobException,
            JadePersistenceException;

    /**
     * Lecture d'un enregistrement de type EnvoiJobSimpleModel depuis la base
     * 
     * @param idEnvoiJob
     *            L'id de l'enregistrement � lire
     * @return Le mod�le de donn�es renseign�
     * @throws ALEnvoiJobException
     *             Soulev� en cas de probl�mes m�tier
     * @throws JadePersistenceException
     *             Soulev� en cas de probl�mes de persistence
     */
    public EnvoiJobSimpleModel read(String idEnvoiJob) throws ALEnvoiJobException, JadePersistenceException;

    /**
     * Recherche d'�l�ments de type EnvoiJobSimpleModel en base
     * 
     * @param envoiJobSearch
     *            Le mod�le de recherche de donn�es renseign�s
     * @return Le mod�le de recherche compl�t�
     * @throws ALEnvoiJobException
     *             Soulev� en cas de probl�mes m�tier
     * @throws JadePersistenceException
     *             Soulev�e en cas de probl�mes de persistence
     */
    public EnvoiJobSimpleModelSearch search(EnvoiJobSimpleModelSearch envoiJobSearch) throws ALEnvoiJobException,
            JadePersistenceException;

    /**
     * Mise � jour d'un enregistrement de type EnvoiJobSimpleModel en base
     * 
     * @param envoiJob
     *            Le mod�le de donn�es � mettre � jour
     * @return Le mod�le de donn�es mis � jour
     * @throws ALEnvoiJobException
     *             Soulev� en cas de probl�mes m�tier
     * @throws JadePersistenceException
     *             Soulev� en cas de probl�mes de persistence
     */
    public EnvoiJobSimpleModel update(EnvoiJobSimpleModel envoiJob) throws ALEnvoiJobException,
            JadePersistenceException;

}
