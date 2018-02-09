/**
 * 
 */
package ch.globaz.pegasus.business.services.models.decision;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadeCloneModelException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionRefus;
import ch.globaz.pegasus.business.models.decision.DecisionSuppression;
import ch.globaz.pegasus.business.models.decision.ValidationDecisionSearch;
import ch.globaz.pegasus.business.models.decision.ValidationDecisionSuppressionWithPcaSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PcaForDecompte;
import ch.globaz.pegasus.businessimpl.services.models.decision.validation.ValiderDecisionAcData;

/**
 * @author SCE
 * 
 *         13 sept. 2010
 */
public interface ValidationDecisionService extends JadeApplicationService {

    // public DecisionRefus dePreValidDecisionRefus(DecisionRefus decisionRefus) throws JadePersistenceException,
    // DecisionException, JadeApplicationServiceNotAvailableException;

    public void checkAdresses(DecisionApresCalcul decisionApresCalcul) throws JadePersistenceException,
            DecisionException;

    public int count(ValidationDecisionSearch search) throws DecisionException, JadePersistenceException;

    public boolean isPaymentDoneBetweenTheValidation(DecisionApresCalcul decisionApresCalcul)
            throws JadeApplicationServiceNotAvailableException, DecisionException;

    public DecisionApresCalcul preValidDecisionApresCalcul(DecisionApresCalcul decisionApresCalcul)
            throws JadePersistenceException, DecisionException, JadeApplicationServiceNotAvailableException;

    public DecisionRefus preValidDecisionRefus(DecisionRefus decisionRefus) throws JadePersistenceException,
            DecisionException, JadeApplicationServiceNotAvailableException;

    public ValidationDecisionSearch search(ValidationDecisionSearch validationDecisionSearch)
            throws JadePersistenceException, DecisionException, JadeApplicationServiceNotAvailableException;

    /**
     * Recherches des validations pour les decisions de suppressions (validation avec pca)
     * 
     * @param validationDecisionSearch
     * @return
     * @throws JadePersistenceException
     * @throws DecisionException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public ValidationDecisionSuppressionWithPcaSearch search(
            ValidationDecisionSuppressionWithPcaSearch validationDecisionSearch) throws JadePersistenceException,
            DecisionException, JadeApplicationServiceNotAvailableException;

    public DecisionRefus validDecisionRefus(DecisionRefus decisionRefus) throws JadePersistenceException,
            DecisionException, JadeApplicationServiceNotAvailableException, DemandeException, DossierException;

    public void validerDecisionSuppression(DecisionSuppression decisionSuppression, boolean isComptabilisationAuto,
            String mailProcessCompta) throws JadePersistenceException, DecisionException,
            JadeApplicationServiceNotAvailableException, DemandeException, DossierException, PCAccordeeException,
            JadeCloneModelException, JadeApplicationException;

    public void validerDecisionSuppression(DecisionSuppression decisionSuppression, boolean isComptabilisationAuto,
            String mailProcessCompta, boolean isAnnulation) throws JadePersistenceException, DecisionException,
            JadeCloneModelException, JadeApplicationException;

    /**
     * Permet une validation de toutes les décisions AC.
     * 
     * @param idVersionDroit Un id version de droit
     * @param forceCreationLotRestitution Boolean pour savoir si on veut la création de lot de restitution
     * @return les données utilisées et créés pour la validation des décisions
     * @throws DecisionException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public ValiderDecisionAcData validerToutesLesDesionsionApresCalcule(String idVersionDroit,
            boolean forceCreationLotRestitution) throws DecisionException, JadePersistenceException,
            JadeApplicationException;

    public void createAnnoncesLaprams(List<DecisionApresCalcul> list, List<PcaForDecompte> pcasReplaced)
            throws AnnonceException, DecisionException;

}
