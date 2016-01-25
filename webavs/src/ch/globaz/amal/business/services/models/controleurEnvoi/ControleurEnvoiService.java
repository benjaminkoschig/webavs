/**
 * 
 */
package ch.globaz.amal.business.services.models.controleurEnvoi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import ch.globaz.amal.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.amal.business.exceptions.models.controleurEnvoi.ControleurEnvoiException;
import ch.globaz.amal.business.exceptions.models.controleurJob.ControleurJobException;
import ch.globaz.amal.business.exceptions.models.documents.DocumentException;
import ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurAnnonceDetailSearch;
import ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoi;
import ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiDetail;
import ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiDetailSearch;
import ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiSearch;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatus;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatusSearch;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJob;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJobSearch;

/**
 * @author DHI
 * 
 */
public interface ControleurEnvoiService extends JadeApplicationService {

    /**
     * Permet la mise � jour du status d'une entit� ComplexControleurEnvoi
     * 
     * @param idJob
     *            Le Job � mettre � jour avec un nouveau status
     * @return Le job supprimer
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     * @throws ControleurEnvoiException
     * @throws DocumentException
     * @throws AnnonceException
     * @throws ControleurJobException
     */
    public ComplexControleurEnvoi changeStatus(String idJob, String newStatus) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, ControleurEnvoiException, AnnonceException, DocumentException,
            ControleurJobException;

    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(ComplexControleurAnnonceDetailSearch search) throws JadePersistenceException;

    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(ComplexControleurEnvoiDetailSearch search) throws JadePersistenceException;

    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(ComplexControleurEnvoiSearch search) throws JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� ComplexControleurEnvoi
     * 
     * @param controleurEnvoi
     *            Le Job � Cr�er
     * @return Le job cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public ComplexControleurEnvoi create(ComplexControleurEnvoi controleurEnvoi) throws JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� ComplexControleurEnvoi
     * 
     * @param controleurEnvoi
     *            Le Job � Cr�er
     * @return Le job cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public ComplexControleurEnvoiDetail create(ComplexControleurEnvoiDetail controleurEnvoi)
            throws JadePersistenceException;

    /**
     * Permet la suppression d'une entit� ComplexControleurEnvoi
     * 
     * @param controleurEnvoi
     *            Le Job � supprimer
     * @return Le job supprimer
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public ComplexControleurEnvoi delete(ComplexControleurEnvoi controleurEnvoi) throws JadePersistenceException;

    /**
     * Permet la suppression d'une entit� ComplexControleurEnvoi
     * 
     * @param controleurEnvoi
     *            Le Job � supprimer
     * @return Le job supprimer
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public ComplexControleurEnvoiDetail delete(ComplexControleurEnvoiDetail controleurEnvoi)
            throws JadePersistenceException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'une entit� ComplexControleurEnvoi
     * 
     * @param idJob
     *            Le Job � supprimer
     * @return Le job supprimer
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     * @throws ControleurEnvoiException
     * @throws ControleurJobException
     */
    public ComplexControleurEnvoi delete(String idJob) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, ControleurEnvoiException, ControleurJobException;

    /**
     * 
     * G�n�ration du status global du job
     * 
     * @param idJob
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public String generateStatus(String idJob) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException;

    /**
     * Retourne le nom du document en fonction de son envoi, avec path complet, depuis le client
     * 
     * @param idStatusEnvoi
     *            id du status envoi attach�
     * @return filename complet : \\chemin_complet\tif_21.10.2011_11-20-35_669424_42000013.doc
     */
    public String getInteractivDocumentFullFileName(String idStatusEnvoi);

    /**
     * Retourne le nom du document en fonction de son envoi, avec path complet, depuis le serveur de job
     * 
     * @param idStatusEnvoi
     *            id du status envoi attach�
     * @return filename complet : \\chemin_complet\tif_21.10.2011_11-20-35_669424_42000013.doc
     */
    public String getInteractivDocumentFullFileNameFromJobServer(String idStatusEnvoi);

    /**
     * R�cup�ration de la liste des complexcontroleurenvoidetail d'un job en particulier
     * 
     * @param idJob
     * @return
     */
    public ArrayList<ComplexControleurEnvoiDetail> getListComplexControleurEnvoiDetail(String idJob);

    /**
     * R�cup�ration des informations de publication GED pour le document demand�
     * 
     * @param statusEnvoi
     * @return
     */
    public JadePublishDocumentInfo getPublishInfoGEDDocument(SimpleControleurEnvoiStatus statusEnvoi);

    /**
     * R�cup�ration des informations de publication GED pour le document demand�
     * 
     * @param idStatusEnvoi
     * @return
     */
    public JadePublishDocumentInfo getPublishInfoGEDDocument(String idStatusEnvoi);

    /**
     * R�cup�ration des informations de publication print pour le document demand�
     * 
     * @param statusEnvoi
     * @return
     */
    public JadePublishDocumentInfo getPublishInfoPrintDocument(SimpleControleurEnvoiStatus statusEnvoi,
            boolean bSendEMail);

    /**
     * R�cup�ration des informations de publication print pour le document demand�
     * 
     * @param idStatusEnvoi
     * @return
     */
    public JadePublishDocumentInfo getPublishInfoPrintDocument(String idStatusEnvoi, boolean bSendEMail);

    /**
     * R�cup�ration des informations de publication print pour le job demand�
     * 
     * @param job
     * @return
     */
    public JadePublishDocumentInfo getPublishInfoPrintJob(SimpleControleurJob job);

    /**
     * R�cup�ration des informations de publication print pour le job demand�
     * 
     * @param idJob
     * @return
     */
    public JadePublishDocumentInfo getPublishInfoPrintJob(String idJob);

    /**
     * Permet de charger en m�moire un ComplexControleurEnvoi (Job)
     * 
     * @param idControleurEnvoi
     *            L'identifiant du job � charger en m�moire
     * @return Le controleur envoi charg� en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public ComplexControleurEnvoi read(String idControleurEnvoi) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en m�moire un simplecontroleurJob
     * 
     * @param idControleurJob
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws ControleurJobException
     */
    public SimpleControleurJob readSimpleControleurJob(String idControleurJob) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, ControleurJobException;

    /**
     * Permet de charger en m�moire un simplecontroleurenvoistatus
     * 
     * @param idControleurEnvoiStatus
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleControleurEnvoiStatus readSimpleEnvoiStatus(String idControleurEnvoiStatus)
            throws JadePersistenceException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet de chercher des jobs en fonction de crit�res de recherche
     * 
     * @param controleurAnnonceSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public ComplexControleurAnnonceDetailSearch search(ComplexControleurAnnonceDetailSearch controleurAnnonceSearch)
            throws JadePersistenceException;

    /**
     * Permet de chercher des jobs en fonction de crit�res de recherche
     * 
     * @param controleurEnvoiSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public ComplexControleurEnvoiDetailSearch search(ComplexControleurEnvoiDetailSearch controleurEnvoiSearch)
            throws JadePersistenceException;

    /**
     * Permet de chercher des jobs en fonction de crit�res de recherche
     * 
     * @param controleurEnvoiSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public ComplexControleurEnvoiSearch search(ComplexControleurEnvoiSearch controleurEnvoiSearch)
            throws JadePersistenceException;

    /**
     * Recherche de simplecontroleurenvoistatus
     * 
     * @param simpleControleurEnvoiSearch
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws ControleurEnvoiException
     */
    public SimpleControleurEnvoiStatusSearch search(SimpleControleurEnvoiStatusSearch simpleControleurEnvoiSearch)
            throws JadePersistenceException, ControleurEnvoiException, JadeApplicationServiceNotAvailableException;

    /**
     * Recherche de simplecontroleurjob
     * 
     * @param simpleControleurJobSearch
     * @return
     * @throws JadePersistenceException
     * @throws ControleurEnvoiException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimpleControleurJobSearch search(SimpleControleurJobSearch simpleControleurJobSearch)
            throws JadePersistenceException, ControleurJobException, JadeApplicationServiceNotAvailableException;

}
