/**
 * 
 */
package ch.globaz.pegasus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import java.util.Map;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.babel.business.exception.CatalogueTexteException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalculOO;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalculSearch;
import ch.globaz.pegasus.business.models.decision.ForDeleteDecisionSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * @author SCE
 * 
 *         26 juil. 2010
 */
public interface DecisionApresCalculService extends JadeApplicationService {
    /**
     * Retourne un documentData contenant l'entier du plan de calcul
     * 
     * @param idDecision
     * @return
     * @throws CatalogueTexteException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws Exception
     */
    public DocumentData buildPlanCalculDocumentData(String idDecisionApresCalcul, boolean isWithMemmbreFamilles)
            throws CatalogueTexteException, JadeApplicationServiceNotAvailableException, Exception;

    /**
     * Compte le nombre d'occurence en base de donn�es
     * 
     * @param search
     * @return int, le nombre d'occurence
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public int count(DecisionApresCalculSearch search) throws DecisionException, JadePersistenceException;

    /**
     * @param decision
     * @return
     */
    public DecisionApresCalcul create(DecisionApresCalcul decisionApresCalcul) throws DecisionException,
            JadePersistenceException;

    /**
     * Processus de pr�parationde la d�cision aprs calcul de type COURANT
     * 
     * @param decision
     * @throws PCAccordeeException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws DecisionException
     * @throws Exception
     */
    public void createCourantDecision(DecisionApresCalcul decision, PCAccordeeSearch pcaSearchForDac,
            Map<String, Map<String, List<SimpleDetailFamille>>> listeAmal) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DecisionException, Exception;

    /**
     * Processus de pr�paration de la (ou des) d�cisions apres calcul de type RETRO
     * 
     * @param decision
     * @throws PCAccordeeException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public void createRetroDecision(DecisionApresCalcul decision, PCAccordeeSearch pcaSearchForDac,
            Map<String, Map<String, List<SimpleDetailFamille>>> listeAmal) throws Exception;

    /**
     * Cr�ation de la d�cision de type Standard, la recherche des pca est lanc�, et ensuite la fonction eponyme, avec
     * les recherches est appel�
     * 
     * @param decision
     * @throws Exception
     */
    public void createStandardDecision(DecisionApresCalcul decision) throws Exception;

    /**
     * Processus de pr�paration de ( ou des) d�cision apres calcul de type STANDARD
     * 
     * @param decision
     * @throws PCAccordeeException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws DecisionException
     * @throws Exception
     */
    public void createStandardDecision(DecisionApresCalcul decision, PCAccordeeSearch pcaSearchForDac,
            Map<String, Map<String, List<SimpleDetailFamille>>> listeAmal) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DecisionException, Exception;

    /**
     * @param decision
     * @return
     */
    public void delete(ForDeleteDecisionSearch search) throws JadePersistenceException, DecisionException;

    /**
     * Charge l'entit� en base de donn�es
     * 
     * @param idDecision
     * @return l'entit� charg�
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public DecisionApresCalcul read(String idDecision) throws JadePersistenceException, DecisionException;

    /**
     * Chargement de l'entit� decisionAresCalcul etendu pour le modele OO
     * 
     * @param idDecision
     * @return instance de DecisionAprescalculOO
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public DecisionApresCalculOO readForOO(String idDecision) throws JadePersistenceException, DecisionException;

    /**
     * Recherche de l'entit� en base de donn�es
     * 
     * @param simpleDecisionSearch
     * @return serach, modele de recherche
     * @throws JadePersistenceException
     * @throws DecisionException
     */
    public DecisionApresCalculSearch search(DecisionApresCalculSearch decisionApresCalculSearch)
            throws JadePersistenceException, DecisionException;

    /**
     * Recherche les decisions pour ne version de droit de type courant
     * 
     * @param decisionApresCalculSearch
     * @return
     * @throws JadePersistenceException
     * @throws DecisionException
     *             , lev� si plus de une entit� est retourn�
     */
    public DecisionApresCalculSearch searchForDecisionCourant(DecisionApresCalculSearch decisionApresCalculSearch)
            throws JadePersistenceException, DecisionException;

    /**
     * @param decision
     * @return
     */
    public DecisionApresCalcul update(DecisionApresCalcul decision) throws JadePersistenceException, DecisionException;

    public DecisionApresCalcul updateForPrevalidation(DecisionApresCalcul decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;
}
