/**
 * 
 */
package ch.globaz.amal.business.services.models.reprise;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.amal.business.models.reprise.ContribuableReprise;
import ch.globaz.amal.business.models.reprise.ContribuableRepriseSearch;
import ch.globaz.amal.business.models.reprise.SimpleContribuableInfoReprise;
import ch.globaz.amal.business.models.reprise.SimpleContribuableInfoRepriseSearch;
import ch.globaz.amal.business.models.reprise.SimpleContribuableReprise;
import ch.globaz.amal.business.models.reprise.SimpleContribuableRepriseSearch;
import ch.globaz.amal.business.models.reprise.SimpleFamilleReprise;
import ch.globaz.amal.business.models.reprise.SimpleFamilleRepriseSearch;
import ch.globaz.amal.business.models.reprise.SimpleInnerMFReprise;
import ch.globaz.amal.business.models.reprise.SimpleInnerMFRepriseSearch;

/**
 * @author DHI
 * 
 */
public interface ContribuableRepriseService extends JadeApplicationService {

    /**
     * Compte les �l�ments selon un mod�le de recherche particulier
     * 
     * @param search
     *            le mod�le de recherche � renseigner
     * @return
     * @throws Exception
     * @throws JadePersistenceException
     */
    public int count(ContribuableRepriseSearch search) throws Exception, JadePersistenceException;

    /**
     * Compte les �l�ments selon un mod�le de recherche particulier
     * 
     * @param search
     *            le mod�le de recherche � renseigner
     * @return
     * @throws Exception
     * @throws JadePersistenceException
     */
    public int count(SimpleContribuableInfoRepriseSearch search) throws Exception, JadePersistenceException;

    /**
     * Compte les �l�ments selon un mod�le de recherche particulier
     * 
     * @param search
     *            le mod�le de recherche � renseigner
     * @return
     * @throws Exception
     * @throws JadePersistenceException
     */
    public int count(SimpleContribuableRepriseSearch search) throws Exception, JadePersistenceException;

    /**
     * Compte les �l�ments selon un mod�le de recherche particulier
     * 
     * @param search
     *            le mod�le de recherche � renseigner
     * @return
     * @throws Exception
     * @throws JadePersistenceException
     */
    public int count(SimpleFamilleRepriseSearch search) throws Exception, JadePersistenceException;

    /**
     * Compte les �l�ments selon un mod�le de recherche particulier
     * 
     * @param search
     *            le mod�le de recherche � renseigner
     * @return
     * @throws Exception
     * @throws JadePersistenceException
     */
    public int count(SimpleInnerMFRepriseSearch search) throws Exception, JadePersistenceException;

    /**
     * Cr�ation d'un enregistrement RP_INNER_MF
     * 
     * @param simpleInner
     *            mod�le � renseigner
     * @return le mod�le cr��
     * @throws JadePersistenceException
     * @throws Exception
     */
    public SimpleInnerMFReprise create(SimpleInnerMFReprise simpleInner) throws JadePersistenceException, Exception;

    /**
     * Suppression d'un enregistrement SimpleInnerMFReprise, selon un mod�le de donn�es
     * 
     * @param innerMF
     *            le mod�le � supprimer
     * @return le mod�le supprim�
     * @throws JadePersistenceException
     * @throws Exception
     */
    public SimpleInnerMFReprise deleteSimpleInnerMF(SimpleInnerMFReprise innerMF) throws JadePersistenceException,
            Exception;

    /**
     * R�cup�ration du nombre d'enregistrement trait�s
     * 
     * @return
     */
    public String getNbEnCoursEnregistrements();

    /**
     * R�cup�ration du nombre total d'enregistrement � traiter
     * 
     * @return
     */
    public String getNbTotalEnregistrements();

    /**
     * R�cup�ration du status de la reprise (in progress, stopped)
     * 
     * @return
     */
    public String getRepriseStatus();

    /**
     * Lecture d'un enregistrement Contribuable Reprise, selon son id
     * 
     * @param idContribuable
     *            l'id du contribuable
     * @return le mod�le de donn�es renseign�
     * @throws JadePersistenceException
     * @throws Exception
     */
    public ContribuableReprise readContribuable(String idContribuable) throws JadePersistenceException, Exception;

    /**
     * Lecture d'un enregistrement de famille Reprise, selon son id
     * 
     * @param idFamille
     *            l'id du membre famille
     * @return le mod�le renseign�
     * @throws JadePersistenceException
     * @throws Exception
     */
    public SimpleFamilleReprise readFamille(String idFamille) throws JadePersistenceException, Exception;

    /**
     * Lecture d'un enregistrement RP_INNER_MF, selon son id
     * 
     * @param idInnerMF
     *            id de l'enregistrement souhait�
     * @return le mod�le renseign�
     * @throws JadePersistenceException
     * @throws Exception
     */
    public SimpleInnerMFReprise readInner(String idInnerMF) throws JadePersistenceException, Exception;

    /**
     * Lecture d'un enregistrement SimpleContribuableReprise, selon son id
     * 
     * @param idContribuable
     *            id de l'enregistrement souhait�
     * @return le mod�le renseign�
     * @throws JadePersistenceException
     * @throws Exception
     */
    public SimpleContribuableReprise readSimpleContribuable(String idContribuable) throws JadePersistenceException,
            Exception;

    /**
     * Lecture d'un enregistrement SimpleContribuableInfoReprise, selon son id
     * 
     * @param idContribuable
     *            id de l'enregistrement souhait�
     * @return le mod�le renseign�
     * @throws JadePersistenceException
     * @throws Exception
     */
    public SimpleContribuableInfoReprise readSimpleContribuableInfo(String idContribuable)
            throws JadePersistenceException, Exception;

    /**
     * Recherche d'enregistrements de Contribuable Reprise, selon un mod�le de recherche
     * 
     * @param search
     *            le mod�le de recherche
     * @return le mod�le de recherche renseign�
     * @throws Exception
     * @throws JadePersistenceException
     */
    public ContribuableRepriseSearch search(ContribuableRepriseSearch search) throws Exception,
            JadePersistenceException;

    /**
     * Recherche d'enregistrements de SimpleContribuableInfoReprise, selon un mod�le de recherche
     * 
     * @param search
     *            le mod�le de recherche
     * @return le mod�le de recherche renseign�
     * @throws Exception
     * @throws JadePersistenceException
     */
    public SimpleContribuableInfoRepriseSearch search(SimpleContribuableInfoRepriseSearch search) throws Exception,
            JadePersistenceException;

    /**
     * Recherche d'enregistrements de SimpleContribuableReprise, selon un mod�le de recherche
     * 
     * @param search
     *            le mod�le de recherche
     * @return le mod�le de recherche renseign�
     * @throws Exception
     * @throws JadePersistenceException
     */
    public SimpleContribuableRepriseSearch search(SimpleContribuableRepriseSearch search) throws Exception,
            JadePersistenceException;

    /**
     * Recherche d'enregistrements de SimpleFamilleReprise, selon un mod�le de recherche
     * 
     * @param search
     *            le mod�le de recherche
     * @return le mod�le de recherche renseign�
     * @throws Exception
     * @throws JadePersistenceException
     */
    public SimpleFamilleRepriseSearch search(SimpleFamilleRepriseSearch search) throws Exception,
            JadePersistenceException;

    /**
     * Recherche d'enregistrements de SimpleInnerMFReprise, selon un mod�le de recherche
     * 
     * @param search
     *            le mod�le de recherche
     * @return le mod�le de recherche renseign�
     * @throws Exception
     * @throws JadePersistenceException
     */
    public SimpleInnerMFRepriseSearch search(SimpleInnerMFRepriseSearch search) throws Exception,
            JadePersistenceException;

    /**
     * Mise � jour d'un enregistrement SimpleContribuable Reprise, selon un mod�le de donn�e
     * 
     * @param contribuable
     *            le mod�le � mettre � jour
     * @return le mod�le mis � jour
     * @throws JadePersistenceException
     * @throws Exception
     */
    public SimpleContribuableReprise updateSimpleContribuable(SimpleContribuableReprise contribuable)
            throws JadePersistenceException, Exception;

    /**
     * Mise � jour d'un enregistrement SimpleFamilleReprise, selon un mod�le de donn�es
     * 
     * @param famille
     *            le mod�le � mettre � jour
     * @return le mod�le mis � jour
     * @throws JadePersistenceException
     * @throws Exception
     */
    public SimpleFamilleReprise updateSimpleFamille(SimpleFamilleReprise famille) throws JadePersistenceException,
            Exception;

    /**
     * Mise � jour d'un enregistrement SimpleInnerMFReprise, selon un mod�le de donn�es
     * 
     * @param innerMF
     *            le mod�le � mettre � jour
     * @return le mod�le mis � jour
     * @throws JadePersistenceException
     * @throws Exception
     */
    public SimpleInnerMFReprise updateSimpleInnerMF(SimpleInnerMFReprise innerMF) throws JadePersistenceException,
            Exception;
}
