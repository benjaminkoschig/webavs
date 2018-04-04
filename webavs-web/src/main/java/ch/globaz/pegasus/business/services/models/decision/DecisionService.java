/**
 * 
 */
package ch.globaz.pegasus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.corvus.business.exceptions.models.LotException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.decision.ForDeleteDecisionSearch;
import ch.globaz.pegasus.business.models.decision.ListDecisions;
import ch.globaz.pegasus.business.models.decision.ListDecisionsSearch;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.vo.decision.DecisionPcVO;

/**
 * @author SCE 14 juil. 2010
 */
public interface DecisionService extends JadeApplicationService {

    /**
     * Dévalide les décisions d'une version de droit, c.a.d. les remet dans l'état avant leur validation et supprime les
     * prestations et ordres de versement liés.
     * 
     * @param idDroit
     *            id du droit à dévalider
     * @param idVersionDroit
     *            id de la version du droit à dévalider
     * @param noVersion
     *            Numéro de la version du droit à dévalider
     * @throws DecisionException
     *             en cas d'erreur
     */
    public void devalideDecisions(String idDroit, String idVersionDroit, String noVersion) throws DecisionException;

    /**
     * Dévalide les décisions d'une version de droit, c.a.d. les remet dans l'état avant leur validation et supprime les
     * prestations et ordres de versement liés.
     * 
     * @param idDroit
     *            id du droit à dévalider
     * @param idVersionDroit
     *            id de la version du droit à dévalider
     * @param noVersion
     *            Numéro de la version du droit à dévalider
     * @param forAnnulation
     *            process a excuter pour une dévalidation d'annulation
     * @throws DecisionException
     *             en cas d'erreur
     */
    public void devalideDecisions(String idDroit, String idVersionDroit, String noVersion, Boolean forAnnulation)
            throws DecisionException;

    /**
     * Permet de savoir si on peut dévalider une décision
     * 
     * @param simpleDecisionHeader
     * @param simpleVersionDroit
     * @return
     * @throws LotException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws PrestationException
     */
    public boolean isDecisionDevalidable(SimpleDecisionHeader simpleDecisionHeader,
            SimpleVersionDroit simpleVersionDroit) throws LotException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, PrestationException;

    /**
     * Retourne la decision chargé
     * 
     * @param idDecision
     * @return decsion, la decision chargé
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public ListDecisions readDecision(String idDecision) throws DecisionException, JadePersistenceException;

    /**
     * Retourne le resultat de recherches des décisions de tout types
     * 
     * @param decisionSearch
     * @return decisionSearch, le modèle de recherche
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public ListDecisionsSearch searchDecisions(ListDecisionsSearch listDecisionsSearch) throws DecisionException,
            JadePersistenceException;

    /**
     * Retourne une liste de décsion(s) validée(s) à une (des) dates données
     * 
     * @param datesValidations
     *            , List de date(s) de validation
     * @return une liste de décisions (DecisionPcVO)
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public List<DecisionPcVO> searchDecisionsByDateValidation(List<String> datesValidations) throws DecisionException,
            JadePersistenceException, DecisionException, JadeApplicationServiceNotAvailableException;

    /**
     * Retourne une liste de décsion(s) d'adaptation pour l'année courante
     * 
     * @return une liste de décisions (DecisionPcVO)
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public List<DecisionPcVO> searchDecisionsAdaptation() throws DecisionException, JadePersistenceException,
            DecisionException, JadeApplicationServiceNotAvailableException;

    public ForDeleteDecisionSearch searchForDelete(ForDeleteDecisionSearch search) throws DecisionException,
            JadePersistenceException;

    /**
     * Indique si la demande contient que des pca en refus;
     * 
     * @param idDemande
     * @return
     * @throws PCAccordeeException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public boolean hasOnlyRefus(String idDemande) throws PCAccordeeException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

    public boolean isAdaptationAnnuelleNotValidate(String nextDate) throws JadePersistenceException;
}
