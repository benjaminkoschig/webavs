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
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws MembreFamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
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
     * qui sont existants pour la date donn�e. Pour les relations entre conjoints seule la relation de mariage est
     * consid�r�.
     * 
     * Si la date de naissance est �gale � la date donn�e en param�tre la personnne ne sera pas filtre.
     * 
     * Si la date de d�c� est �gale � la date donn�e en param�tre la personne <b>sera filtre�</b>.
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
     * Retourne la famille compl�te d�pr�s l'id tiers d'un enfant pass� en param�tre, pour le domaine des rentes
     * 
     * @param idEnfant
     *            , l'id tiers de l'enfant
     * @param withoutParents
     *            , retourne les parents avec, ou pas. Si null, consid�rer comme false
     * @return, tableau de MembreFamilleVO
     * @throws MembreFamilleException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public ArrayList<MembreFamilleVO> getFamilleByIDEnfant(String idEnfant, Boolean withoutParents)
            throws MembreFamilleException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            Exception;

    /**
     * Retourne la famille compl�te d'apr�s l'id tiers d'un enfant et une date ceci pour le domaine des rentes
     * 
     * @param idEnfant
     *            , l'id tiers de l'enfant
     * @param date
     * @param withoutParents
     *            , retourne les parents avec, ou pas. Si null, consid�rer comme false
     * @return, tableau de MembreFamilleVO
     * @throws MembreFamilleException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public ArrayList<MembreFamilleVO> getFamilleByIDEnfant(String idEnfant, String date, Boolean withoutParents)
            throws MembreFamilleException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            Exception;

    /**
     * Permet de charger en m�moire un membre de famille
     * 
     * @param idMembreFamille
     *            L'identifiant du membre de famille � charger en m�moire
     * @return Le membre de famille charg� en m�moire
     * @throws MembreFamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public MembreFamille read(String idMembreFamille) throws JadePersistenceException, MembreFamilleException;

    /**
     * Permet de chercher des membres de famille selon un mod�le de crit�res.
     * 
     * @param search
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws MembreFamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
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
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
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
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public MembreFamilleVO[] searchMembresFamilleRequerantDomaineRentes(String idTiersRequerant)
            throws MembreFamilleException, JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Donne les membres de famille d'un requerant pour le domaine des rentes valable � la date sp�cifi�e
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
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public MembreFamilleVO[] searchMembresFamilleRequerantDomaineRentes(String idTiersRequerant, String date)
            throws MembreFamilleException, JadeApplicationServiceNotAvailableException, JadePersistenceException;

    MembreFamilleVO[] searchMembresFamilleRequerantDomaineStandard(String idTiersRequerant, String date)
            throws MembreFamilleException, JadeApplicationServiceNotAvailableException, JadePersistenceException;
}
