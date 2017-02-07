package ch.globaz.hera.business.services.models.famille;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import ch.globaz.hera.business.exceptions.models.MembreFamilleException;
import ch.globaz.hera.business.models.famille.DateNaissanceConjoint;
import ch.globaz.hera.business.models.famille.MembreFamille;
import ch.globaz.hera.business.models.famille.MembreFamilleSearch;
import ch.globaz.hera.business.vo.famille.MembreFamilleVO;

public interface MembreFamilleService extends JadeApplicationService {

    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws MembreFamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(MembreFamilleSearch search) throws MembreFamilleException, JadePersistenceException;

    public DateNaissanceConjoint readDateNaissanceConjoint(String idMbrFamConjoint) throws JadePersistenceException,
            MembreFamilleException;

    /**
     * Code repris de puis cette class SFApercuRelationFamilialeAction.actionEntrerApplication();
     */
    public void createRequerant(String idTiersRequerant) throws MembreFamilleException;

    /**
     * Permet de filtre une liste de membre de famille en fonction d'une date. Retourne seulement les membres famille
     * qui sont existants pour la date donnée. Pour les relations entre conjoints seule la relation de mariage est
     * considéré.
     * 
     * Si la date de naissance est égale à la date donnée en paramétre la personnne ne sera pas filtre.
     * 
     * Si la date de décè est égale à la date donnée en paramétre la personne <b>sera filtreé</b>.
     * 
     * @param arrayMembreFamilleVO
     * @param date
     * @return array de membre famille
     * @throws MembreFamilleException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public MembreFamilleVO[] filtreMembreFamilleWithDate(MembreFamilleVO[] arrayMembreFamilleVO, String date)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, MembreFamilleException;

    /**
     * Retourne la famille complète dâprès l'id tiers d'un enfant passé en paramètre, pour le domaine des rentes
     * 
     * @param idEnfant
     *            , l'id tiers de l'enfant
     * @param withoutParents
     *            , retourne les parents avec, ou pas. Si null, considérer comme false
     * @return, tableau de MembreFamilleVO
     * @throws MembreFamilleException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public ArrayList<MembreFamilleVO> getFamilleByIDEnfant(String idEnfant, Boolean withoutParents)
            throws MembreFamilleException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            Exception;

    /**
     * Retourne la famille complète d'après l'id tiers d'un enfant et une date ceci pour le domaine des rentes
     * 
     * @param idEnfant
     *            , l'id tiers de l'enfant
     * @param date
     * @param withoutParents
     *            , retourne les parents avec, ou pas. Si null, considérer comme false
     * @return, tableau de MembreFamilleVO
     * @throws MembreFamilleException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public ArrayList<MembreFamilleVO> getFamilleByIDEnfant(String idEnfant, String date, Boolean withoutParents)
            throws MembreFamilleException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            Exception;

    /**
     * Permet de charger en mémoire un membre de famille
     * 
     * @param idMembreFamille
     *            L'identifiant du membre de famille à charger en mémoire
     * @return Le membre de famille chargé en mémoire
     * @throws MembreFamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public MembreFamille read(String idMembreFamille) throws JadePersistenceException, MembreFamilleException;

    /**
     * Permet de chercher des membres de famille selon un modèle de critères.
     * 
     * @param search
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws MembreFamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public MembreFamilleSearch search(MembreFamilleSearch search) throws JadePersistenceException,
            MembreFamilleException;

    /**
     * Donne les membre de famille d'un requerant pour un domaine
     * 
     * Doit remplacer:
     * 
     * globaz.hera.api.ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
     * ISFSituationFamiliale.CS_DOMAINE_RENTES, demandePc.getIdTiers());
     * 
     * ISFMembreFamilleRequerant[] mf = sf.getMembresFamilleRequerant(demandePc.getIdTiers());
     * 
     * @param csDomaine
     *            Le domaine pour lequel la famille est cherchee
     * @param idTiersRequerant
     *            L'id tiers du requerant
     * @param date
     *            La date de la situation familliale
     * 
     * @return Les membres de famille
     * @throws MembreFamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public MembreFamilleVO[] searchMembresFamilleRequerant(String csDomaine, String idTiersRequerant, String date)
            throws MembreFamilleException, JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Donne les membre de famille d'un requerant pour le domaine des rentes
     * 
     * Doit remplacer:
     * 
     * globaz.hera.api.ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
     * ISFSituationFamiliale.CS_DOMAINE_RENTES, demandePc.getIdTiers());
     * 
     * ISFMembreFamilleRequerant[] mf = sf.getMembresFamilleRequerant(demandePc.getIdTiers());
     * 
     * @param idTiersRequerant
     *            L'id tiers du requerant
     * @return Les membres de famille
     * @throws MembreFamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public MembreFamilleVO[] searchMembresFamilleRequerantDomaineRentes(String idTiersRequerant)
            throws MembreFamilleException, JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Donne les membres de famille d'un requerant pour le domaine des rentes valable à la date spécifiée
     * 
     * Doit remplacer:
     * 
     * globaz.hera.api.ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
     * ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiers);
     * 
     * ISFMembreFamilleRequerant[] mf = sf.getMembresFamilleRequerant(IdTiers, date);
     * 
     * @param idTiersRequerant
     *            L'id tiers du requerant
     * @param date
     *            La date de la situation familliale
     * 
     * @return Les membres de famille
     * @throws MembreFamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public MembreFamilleVO[] searchMembresFamilleRequerantDomaineRentes(String idTiersRequerant, String date)
            throws MembreFamilleException, JadeApplicationServiceNotAvailableException, JadePersistenceException;

    MembreFamilleVO[] searchMembresFamilleRequerantDomaineStandard(String idTiersRequerant, String date)
            throws MembreFamilleException, JadeApplicationServiceNotAvailableException, JadePersistenceException;
}
