/**
 * 
 */
package ch.globaz.al.business.services.models.envoi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.exceptions.model.envoi.ALEnvoiParametresException;
import ch.globaz.al.business.models.envoi.EnvoiParametresSimpleModel;
import ch.globaz.al.business.models.envoi.EnvoiParametresSimpleModelSearch;

/**
 * service de gestion de la persitance des donn�es pour param�tres envoi
 * 
 * @author dhi
 * 
 */
public interface EnvoiParametresSimpleModelService extends JadeApplicationService {
    /**
     * Compte le nombre d'enregistrement disponibles pour un mod�le de recherche donn�
     * 
     * @param envoiParametresSearch
     *            mod�le de recherche renseign�
     * @return Le nombre d'enregistrement qui satisfont le mod�le de recherche
     * @throws ALEnvoiParametresException
     *             Soulev� en cas de probl�mes m�tier
     * @throws JadePersistenceException
     *             Soulev� en cas de probl�mes de persistence (data)
     */
    public int count(EnvoiParametresSimpleModelSearch envoiParametresSearch) throws ALEnvoiParametresException,
            JadePersistenceException;

    /**
     * Cr�ation d'un enregistrement de type EnvoiParametresSimpleModel en base
     * 
     * @param envoiParametres
     *            Le mod�le de donn�es � cr�er, renseign�
     * @return Le mod�le de donn�es cr��
     * @throws ALEnvoiParametresException
     *             Soulev� en cas de probl�mes m�tier
     * @throws JadePersistenceException
     *             Soulev� en cas de probl�mes de persistence
     */
    public EnvoiParametresSimpleModel create(EnvoiParametresSimpleModel envoiParametres)
            throws ALEnvoiParametresException, JadePersistenceException;

    /**
     * Effacement d'un enregistrement de type EnvoiParametresSimpleModel en base
     * 
     * @param envoiParametres
     *            Le mod�le de donn�es � supprimer
     * @return Le mod�le de donn�es supprim�
     * @throws ALEnvoiParametresException
     *             Soulev� en cas de probl�mes m�tier
     * @throws JadePersistenceException
     *             Soulev� en cas de probl�mes de persistence
     */
    public EnvoiParametresSimpleModel delete(EnvoiParametresSimpleModel envoiParametres)
            throws ALEnvoiParametresException, JadePersistenceException;

    /**
     * Lecture d'un enregistrement de type EnvoiParametresSimpleModel depuis la base
     * 
     * @param idEnvoiParametres
     *            L'id de l'enregistrement � lire
     * @return Le mod�le de donn�es renseign�
     * @throws ALEnvoiParametresException
     *             Soulev� en cas de probl�mes m�tier
     * @throws JadePersistenceException
     *             Soulev� en cas de probl�mes de persistence
     */
    public EnvoiParametresSimpleModel read(String idEnvoiParametres) throws ALEnvoiParametresException,
            JadePersistenceException;

    /**
     * Recherche d'�l�ments de type EnvoiJobSimpleModel en base
     * 
     * @param envoiParametresSearch
     *            Le mod�le de recherche de donn�es renseign�s
     * @return Le mod�le de recherche compl�t�
     * @throws ALEnvoiParametresException
     *             Soulev� en cas de probl�mes m�tier
     * @throws JadePersistenceException
     *             Soulev�e en cas de probl�mes de persistence
     */
    public EnvoiParametresSimpleModelSearch search(EnvoiParametresSimpleModelSearch envoiParametresSearch)
            throws ALEnvoiParametresException, JadePersistenceException;

    /**
     * Mise � jour d'un enregistrement de type EnvoiParametresSimpleModel en base
     * 
     * @param envoiParametres
     *            Le mod�le de donn�es � mettre � jour
     * @return Le mod�le de donn�es mis � jour
     * @throws ALEnvoiParametresException
     *             Soulev� en cas de probl�mes m�tier
     * @throws JadePersistenceException
     *             Soulev� en cas de probl�mes de persistence
     */
    public EnvoiParametresSimpleModel update(EnvoiParametresSimpleModel envoiParametres)
            throws ALEnvoiParametresException, JadePersistenceException;

}
