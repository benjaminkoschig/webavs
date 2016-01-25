/**
 * 
 */
package ch.globaz.al.business.services.models.envoi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.exceptions.model.envoi.ALEnvoiItemException;
import ch.globaz.al.business.models.envoi.EnvoiItemSimpleModel;
import ch.globaz.al.business.models.envoi.EnvoiItemSimpleModelSearch;

/**
 * service de gestion de la persitance des donn�es pour les envoi AF
 * 
 * @author dhi
 */
public interface EnvoiItemSimpleModelService extends JadeApplicationService {
    /**
     * Compte le nombre d'enregistrement disponibles pour un mod�le de recherche donn�
     * 
     * @param envoiItemSearch
     *            mod�le de recherche renseign�
     * @return Le nombre d'enregistrement qui satisfont le mod�le de recherche
     * @throws ALEnvoiItemException
     *             Soulev� en cas de probl�mes m�tier
     * @throws JadePersistenceException
     *             Soulev� en cas de probl�mes de persistence (data)
     */
    public int count(EnvoiItemSimpleModelSearch envoiItemSearch) throws ALEnvoiItemException, JadePersistenceException;

    /**
     * Cr�ation d'un enregistrement de type EnvoiItemSimpleModel en base
     * 
     * @param envoiItem
     *            Le mod�le de donn�es � cr�er, renseign�
     * @return Le mod�le de donn�es cr��
     * @throws ALEnvoiItemException
     *             Soulev� en cas de probl�mes m�tier
     * @throws JadePersistenceException
     *             Soulev� en cas de probl�mes de persistence
     */
    public EnvoiItemSimpleModel create(EnvoiItemSimpleModel envoiItem) throws ALEnvoiItemException,
            JadePersistenceException;

    /**
     * Effacement d'un enregistrement de type EnvoiItemSimpleModel en base
     * 
     * @param envoiItem
     *            Le mod�le de donn�es � supprimer
     * @return Le mod�le de donn�es supprim�
     * @throws ALEnvoiItemException
     *             Soulev� en cas de probl�mes m�tier
     * @throws JadePersistenceException
     *             Soulev� en cas de probl�mes de persistence
     */
    public EnvoiItemSimpleModel delete(EnvoiItemSimpleModel envoiItem) throws ALEnvoiItemException,
            JadePersistenceException;

    /**
     * Lecture d'un enregistrement de type EnvoiItemSimpleModel depuis la base
     * 
     * @param idEnvoiItem
     *            L'id de l'enregistrement � lire
     * @return Le mod�le de donn�es renseign�
     * @throws ALEnvoiItemException
     *             Soulev� en cas de probl�mes m�tier
     * @throws JadePersistenceException
     *             Soulev� en cas de probl�mes de persistence
     */
    public EnvoiItemSimpleModel read(String idEnvoiItem) throws ALEnvoiItemException, JadePersistenceException;

    /**
     * Recherche d'�l�ments de type EnvoiItemSimpleModel en base
     * 
     * @param envoiItemSearch
     *            Le mod�le de recherche de donn�es renseign�s
     * @return Le mod�le de recherche compl�t�
     * @throws ALEnvoiItemException
     *             Soulev� en cas de probl�mes m�tier
     * @throws JadePersistenceException
     *             Soulev�e en cas de probl�mes de persistence
     */
    public EnvoiItemSimpleModelSearch search(EnvoiItemSimpleModelSearch envoiItemSearch) throws ALEnvoiItemException,
            JadePersistenceException;

    /**
     * Mise � jour d'un enregistrement de type EnvoiItemSimpleModel en base
     * 
     * @param envoiItem
     *            Le mod�le de donn�es � mettre � jour
     * @return Le mod�le de donn�es mis � jour
     * @throws ALEnvoiItemException
     *             Soulev� en cas de probl�mes m�tier
     * @throws JadePersistenceException
     *             Soulev� en cas de probl�mes de persistence
     */
    public EnvoiItemSimpleModel update(EnvoiItemSimpleModel envoiItem) throws ALEnvoiItemException,
            JadePersistenceException;

}
