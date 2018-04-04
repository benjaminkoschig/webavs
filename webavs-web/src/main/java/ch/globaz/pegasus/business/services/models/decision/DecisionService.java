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
     * D�valide les d�cisions d'une version de droit, c.a.d. les remet dans l'�tat avant leur validation et supprime les
     * prestations et ordres de versement li�s.
     * 
     * @param idDroit
     *            id du droit � d�valider
     * @param idVersionDroit
     *            id de la version du droit � d�valider
     * @param noVersion
     *            Num�ro de la version du droit � d�valider
     * @throws DecisionException
     *             en cas d'erreur
     */
    public void devalideDecisions(String idDroit, String idVersionDroit, String noVersion) throws DecisionException;

    /**
     * D�valide les d�cisions d'une version de droit, c.a.d. les remet dans l'�tat avant leur validation et supprime les
     * prestations et ordres de versement li�s.
     * 
     * @param idDroit
     *            id du droit � d�valider
     * @param idVersionDroit
     *            id de la version du droit � d�valider
     * @param noVersion
     *            Num�ro de la version du droit � d�valider
     * @param forAnnulation
     *            process a excuter pour une d�validation d'annulation
     * @throws DecisionException
     *             en cas d'erreur
     */
    public void devalideDecisions(String idDroit, String idVersionDroit, String noVersion, Boolean forAnnulation)
            throws DecisionException;

    /**
     * Permet de savoir si on peut d�valider une d�cision
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
     * Retourne la decision charg�
     * 
     * @param idDecision
     * @return decsion, la decision charg�
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public ListDecisions readDecision(String idDecision) throws DecisionException, JadePersistenceException;

    /**
     * Retourne le resultat de recherches des d�cisions de tout types
     * 
     * @param decisionSearch
     * @return decisionSearch, le mod�le de recherche
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public ListDecisionsSearch searchDecisions(ListDecisionsSearch listDecisionsSearch) throws DecisionException,
            JadePersistenceException;

    /**
     * Retourne une liste de d�csion(s) valid�e(s) � une (des) dates donn�es
     * 
     * @param datesValidations
     *            , List de date(s) de validation
     * @return une liste de d�cisions (DecisionPcVO)
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public List<DecisionPcVO> searchDecisionsByDateValidation(List<String> datesValidations) throws DecisionException,
            JadePersistenceException, DecisionException, JadeApplicationServiceNotAvailableException;

    /**
     * Retourne une liste de d�csion(s) d'adaptation pour l'ann�e courante
     * 
     * @return une liste de d�cisions (DecisionPcVO)
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
