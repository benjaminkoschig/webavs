package ch.globaz.corvus.business.services.models.decisions;

import globaz.corvus.exceptions.RETechnicalException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.Set;
import ch.globaz.corvus.domaine.Decision;
import ch.globaz.corvus.domaine.SoldePourRestitution;

/**
 * Regroupement de méthode utilitaire (faisant appelle à la persistance) concernant les décisions dans les rentes.
 */
public interface DecisionService extends JadeApplicationService {

    /**
     * <p>
     * Retourne la décision portant l'identifiant passé en paramètre
     * </p>
     * 
     * @param idDecision
     *            l'ID de la décision que l'on veut
     * @return la décision
     */
    public Decision getDecision(Long idDecision);

    /**
     * Retourne la décision dont est issu l'ordre de versement
     * 
     * @param idOrdreVersement
     *            l'id de l'ordre de versement
     * @return la décision dont est issu cet ordre de versement
     * @throws RETechnicalException
     *             si aucune décision trouvée
     */
    public Decision getDecisionPourIdOrdreVersement(Long idOrdreVersement);

    /**
     * Retourne la décision où est utilisée la rente versée à tort
     * 
     * @param idRenteVerseeATort
     *            l'id de la rente versée à tort
     * @return la décision dans laquelle est utilisée cette rente versée à tort
     * @throws RETechnicalException
     *             si aucune décision trouvée
     */
    public Decision getDecisionPourIdRenteVerseeATort(Long idRenteVerseeATort);

    /**
     * <p>
     * Retourne la liste des décisions de la famille ayant un solde positif et n'étant pas encore validées.
     * </p>
     * <p>
     * La décision dont l'ID est passé en paramètre sera ignorée pour la recherche
     * </p>
     * 
     * @param idDecision
     *            l'ID de la décision sur laquelle on travaille
     * @return la liste des décisions correspondantes
     */
    public Set<Decision> getDecisionsAvecSoldePositifDeLaFamille(Long idDecision);

    /**
     * <p>
     * Retourne le solde pour restitution (s'il y en a un) de cette décision. Cette méthode ne se préoccupera que du
     * solde pour restitution lié à des dettes de types rente versée à tort et pas des dettes en comptabilité.
     * </p>
     * <p>
     * Si aucune restitution n'est trouvée (le solde de la décision est positif) retournera <code>null</code>
     * </p>
     * 
     * @param decision
     *            la décision dont on aimerait avoir le solde pour restitution
     * @return le solde pour restitution (ne traitant pas de dettes en compta) ou <code>null</code> s'il n'y a pas de
     *         restitution pour cette décision
     */
    public SoldePourRestitution getSoldePourRestitutionDuBeneficiairePrincipal(Decision decision);

    /**
     * Permet de savoir s'il y a d'autre décision, dans la famille du bénéficiaire principale de la décision passée en
     * paramètre, qui ne sont pas validées et qui ont un solde positif. Cela permettra de pouvoir faire des
     * compensations inter-décision si le solde de la décision passée en paramètre est négatif.
     * 
     * @param idDecision
     *            un ID d'une décision non validée
     * @return <code>true</code> s'il y a d'autres décisions non validées avec solde positif dans la famille du
     *         bénéficiaire principale de la décision passée en paramètre
     */
    public boolean isCompensationInterDecisionPossible(Long idDecision);

    /**
     * Re-calcul le solde de la décision, et selon si le solde est positif ou négatif, met à jour / créer / supprime le
     * solde pour restitution lié à cette décision.
     * 
     * @param idDecision
     *            l'ID de la décision sur laquelle est lié le solde pour restitution
     */
    public void recalculerSoldePourRestitution(Long idDecision);
}
