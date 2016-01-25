package ch.globaz.corvus.business.services.models.decisions;

import globaz.corvus.exceptions.RETechnicalException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.Set;
import ch.globaz.corvus.domaine.Decision;
import ch.globaz.corvus.domaine.SoldePourRestitution;

/**
 * Regroupement de m�thode utilitaire (faisant appelle � la persistance) concernant les d�cisions dans les rentes.
 */
public interface DecisionService extends JadeApplicationService {

    /**
     * <p>
     * Retourne la d�cision portant l'identifiant pass� en param�tre
     * </p>
     * 
     * @param idDecision
     *            l'ID de la d�cision que l'on veut
     * @return la d�cision
     */
    public Decision getDecision(Long idDecision);

    /**
     * Retourne la d�cision dont est issu l'ordre de versement
     * 
     * @param idOrdreVersement
     *            l'id de l'ordre de versement
     * @return la d�cision dont est issu cet ordre de versement
     * @throws RETechnicalException
     *             si aucune d�cision trouv�e
     */
    public Decision getDecisionPourIdOrdreVersement(Long idOrdreVersement);

    /**
     * Retourne la d�cision o� est utilis�e la rente vers�e � tort
     * 
     * @param idRenteVerseeATort
     *            l'id de la rente vers�e � tort
     * @return la d�cision dans laquelle est utilis�e cette rente vers�e � tort
     * @throws RETechnicalException
     *             si aucune d�cision trouv�e
     */
    public Decision getDecisionPourIdRenteVerseeATort(Long idRenteVerseeATort);

    /**
     * <p>
     * Retourne la liste des d�cisions de la famille ayant un solde positif et n'�tant pas encore valid�es.
     * </p>
     * <p>
     * La d�cision dont l'ID est pass� en param�tre sera ignor�e pour la recherche
     * </p>
     * 
     * @param idDecision
     *            l'ID de la d�cision sur laquelle on travaille
     * @return la liste des d�cisions correspondantes
     */
    public Set<Decision> getDecisionsAvecSoldePositifDeLaFamille(Long idDecision);

    /**
     * <p>
     * Retourne le solde pour restitution (s'il y en a un) de cette d�cision. Cette m�thode ne se pr�occupera que du
     * solde pour restitution li� � des dettes de types rente vers�e � tort et pas des dettes en comptabilit�.
     * </p>
     * <p>
     * Si aucune restitution n'est trouv�e (le solde de la d�cision est positif) retournera <code>null</code>
     * </p>
     * 
     * @param decision
     *            la d�cision dont on aimerait avoir le solde pour restitution
     * @return le solde pour restitution (ne traitant pas de dettes en compta) ou <code>null</code> s'il n'y a pas de
     *         restitution pour cette d�cision
     */
    public SoldePourRestitution getSoldePourRestitutionDuBeneficiairePrincipal(Decision decision);

    /**
     * Permet de savoir s'il y a d'autre d�cision, dans la famille du b�n�ficiaire principale de la d�cision pass�e en
     * param�tre, qui ne sont pas valid�es et qui ont un solde positif. Cela permettra de pouvoir faire des
     * compensations inter-d�cision si le solde de la d�cision pass�e en param�tre est n�gatif.
     * 
     * @param idDecision
     *            un ID d'une d�cision non valid�e
     * @return <code>true</code> s'il y a d'autres d�cisions non valid�es avec solde positif dans la famille du
     *         b�n�ficiaire principale de la d�cision pass�e en param�tre
     */
    public boolean isCompensationInterDecisionPossible(Long idDecision);

    /**
     * Re-calcul le solde de la d�cision, et selon si le solde est positif ou n�gatif, met � jour / cr�er / supprime le
     * solde pour restitution li� � cette d�cision.
     * 
     * @param idDecision
     *            l'ID de la d�cision sur laquelle est li� le solde pour restitution
     */
    public void recalculerSoldePourRestitution(Long idDecision);
}
