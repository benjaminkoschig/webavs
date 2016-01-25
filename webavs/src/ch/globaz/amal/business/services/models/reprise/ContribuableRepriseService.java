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
     * Compte les éléments selon un modèle de recherche particulier
     * 
     * @param search
     *            le modèle de recherche à renseigner
     * @return
     * @throws Exception
     * @throws JadePersistenceException
     */
    public int count(ContribuableRepriseSearch search) throws Exception, JadePersistenceException;

    /**
     * Compte les éléments selon un modèle de recherche particulier
     * 
     * @param search
     *            le modèle de recherche à renseigner
     * @return
     * @throws Exception
     * @throws JadePersistenceException
     */
    public int count(SimpleContribuableInfoRepriseSearch search) throws Exception, JadePersistenceException;

    /**
     * Compte les éléments selon un modèle de recherche particulier
     * 
     * @param search
     *            le modèle de recherche à renseigner
     * @return
     * @throws Exception
     * @throws JadePersistenceException
     */
    public int count(SimpleContribuableRepriseSearch search) throws Exception, JadePersistenceException;

    /**
     * Compte les éléments selon un modèle de recherche particulier
     * 
     * @param search
     *            le modèle de recherche à renseigner
     * @return
     * @throws Exception
     * @throws JadePersistenceException
     */
    public int count(SimpleFamilleRepriseSearch search) throws Exception, JadePersistenceException;

    /**
     * Compte les éléments selon un modèle de recherche particulier
     * 
     * @param search
     *            le modèle de recherche à renseigner
     * @return
     * @throws Exception
     * @throws JadePersistenceException
     */
    public int count(SimpleInnerMFRepriseSearch search) throws Exception, JadePersistenceException;

    /**
     * Création d'un enregistrement RP_INNER_MF
     * 
     * @param simpleInner
     *            modèle à renseigner
     * @return le modèle créé
     * @throws JadePersistenceException
     * @throws Exception
     */
    public SimpleInnerMFReprise create(SimpleInnerMFReprise simpleInner) throws JadePersistenceException, Exception;

    /**
     * Suppression d'un enregistrement SimpleInnerMFReprise, selon un modèle de données
     * 
     * @param innerMF
     *            le modèle à supprimer
     * @return le modèle supprimé
     * @throws JadePersistenceException
     * @throws Exception
     */
    public SimpleInnerMFReprise deleteSimpleInnerMF(SimpleInnerMFReprise innerMF) throws JadePersistenceException,
            Exception;

    /**
     * Récupération du nombre d'enregistrement traités
     * 
     * @return
     */
    public String getNbEnCoursEnregistrements();

    /**
     * Récupération du nombre total d'enregistrement à traiter
     * 
     * @return
     */
    public String getNbTotalEnregistrements();

    /**
     * Récupération du status de la reprise (in progress, stopped)
     * 
     * @return
     */
    public String getRepriseStatus();

    /**
     * Lecture d'un enregistrement Contribuable Reprise, selon son id
     * 
     * @param idContribuable
     *            l'id du contribuable
     * @return le modèle de données renseigné
     * @throws JadePersistenceException
     * @throws Exception
     */
    public ContribuableReprise readContribuable(String idContribuable) throws JadePersistenceException, Exception;

    /**
     * Lecture d'un enregistrement de famille Reprise, selon son id
     * 
     * @param idFamille
     *            l'id du membre famille
     * @return le modèle renseigné
     * @throws JadePersistenceException
     * @throws Exception
     */
    public SimpleFamilleReprise readFamille(String idFamille) throws JadePersistenceException, Exception;

    /**
     * Lecture d'un enregistrement RP_INNER_MF, selon son id
     * 
     * @param idInnerMF
     *            id de l'enregistrement souhaité
     * @return le modèle renseigné
     * @throws JadePersistenceException
     * @throws Exception
     */
    public SimpleInnerMFReprise readInner(String idInnerMF) throws JadePersistenceException, Exception;

    /**
     * Lecture d'un enregistrement SimpleContribuableReprise, selon son id
     * 
     * @param idContribuable
     *            id de l'enregistrement souhaité
     * @return le modèle renseigné
     * @throws JadePersistenceException
     * @throws Exception
     */
    public SimpleContribuableReprise readSimpleContribuable(String idContribuable) throws JadePersistenceException,
            Exception;

    /**
     * Lecture d'un enregistrement SimpleContribuableInfoReprise, selon son id
     * 
     * @param idContribuable
     *            id de l'enregistrement souhaité
     * @return le modèle renseigné
     * @throws JadePersistenceException
     * @throws Exception
     */
    public SimpleContribuableInfoReprise readSimpleContribuableInfo(String idContribuable)
            throws JadePersistenceException, Exception;

    /**
     * Recherche d'enregistrements de Contribuable Reprise, selon un modèle de recherche
     * 
     * @param search
     *            le modèle de recherche
     * @return le modèle de recherche renseigné
     * @throws Exception
     * @throws JadePersistenceException
     */
    public ContribuableRepriseSearch search(ContribuableRepriseSearch search) throws Exception,
            JadePersistenceException;

    /**
     * Recherche d'enregistrements de SimpleContribuableInfoReprise, selon un modèle de recherche
     * 
     * @param search
     *            le modèle de recherche
     * @return le modèle de recherche renseigné
     * @throws Exception
     * @throws JadePersistenceException
     */
    public SimpleContribuableInfoRepriseSearch search(SimpleContribuableInfoRepriseSearch search) throws Exception,
            JadePersistenceException;

    /**
     * Recherche d'enregistrements de SimpleContribuableReprise, selon un modèle de recherche
     * 
     * @param search
     *            le modèle de recherche
     * @return le modèle de recherche renseigné
     * @throws Exception
     * @throws JadePersistenceException
     */
    public SimpleContribuableRepriseSearch search(SimpleContribuableRepriseSearch search) throws Exception,
            JadePersistenceException;

    /**
     * Recherche d'enregistrements de SimpleFamilleReprise, selon un modèle de recherche
     * 
     * @param search
     *            le modèle de recherche
     * @return le modèle de recherche renseigné
     * @throws Exception
     * @throws JadePersistenceException
     */
    public SimpleFamilleRepriseSearch search(SimpleFamilleRepriseSearch search) throws Exception,
            JadePersistenceException;

    /**
     * Recherche d'enregistrements de SimpleInnerMFReprise, selon un modèle de recherche
     * 
     * @param search
     *            le modèle de recherche
     * @return le modèle de recherche renseigné
     * @throws Exception
     * @throws JadePersistenceException
     */
    public SimpleInnerMFRepriseSearch search(SimpleInnerMFRepriseSearch search) throws Exception,
            JadePersistenceException;

    /**
     * Mise à jour d'un enregistrement SimpleContribuable Reprise, selon un modèle de donnée
     * 
     * @param contribuable
     *            le modèle à mettre à jour
     * @return le modèle mis à jour
     * @throws JadePersistenceException
     * @throws Exception
     */
    public SimpleContribuableReprise updateSimpleContribuable(SimpleContribuableReprise contribuable)
            throws JadePersistenceException, Exception;

    /**
     * Mise à jour d'un enregistrement SimpleFamilleReprise, selon un modèle de données
     * 
     * @param famille
     *            le modèle à mettre à jour
     * @return le modèle mis à jour
     * @throws JadePersistenceException
     * @throws Exception
     */
    public SimpleFamilleReprise updateSimpleFamille(SimpleFamilleReprise famille) throws JadePersistenceException,
            Exception;

    /**
     * Mise à jour d'un enregistrement SimpleInnerMFReprise, selon un modèle de données
     * 
     * @param innerMF
     *            le modèle à mettre à jour
     * @return le modèle mis à jour
     * @throws JadePersistenceException
     * @throws Exception
     */
    public SimpleInnerMFReprise updateSimpleInnerMF(SimpleInnerMFReprise innerMF) throws JadePersistenceException,
            Exception;
}
