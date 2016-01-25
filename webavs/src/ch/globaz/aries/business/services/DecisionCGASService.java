package ch.globaz.aries.business.services;

import globaz.jade.client.util.JadeProgressBarModel;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.aries.business.beans.decisioncgas.DecisionCGASBean;
import ch.globaz.aries.business.models.ComplexDecisionCGASAffiliationCotisationSearchModel;
import ch.globaz.aries.business.models.DecisionCGASSearchModel;
import ch.globaz.aries.business.models.SimpleDecisionCGAS;
import ch.globaz.aries.business.models.SimpleDetailDecisionCGAS;
import ch.globaz.common.business.models.ResultatTraitementMasseCsvJournal;
import ch.globaz.musca.business.models.PassageModel;

public interface DecisionCGASService extends JadeApplicationService {

    public List<SimpleDetailDecisionCGAS> beanToListDetailDecisionCGAS(DecisionCGASBean entity);

    public DecisionCGASBean create(DecisionCGASBean entity) throws Exception;

    public DecisionCGASBean createDecisionCGASBeanFromDecisionCGAS(SimpleDecisionCGAS decisionCGAS)
            throws JadePersistenceException;

    public DecisionCGASBean delete(DecisionCGASBean entity) throws JadeApplicationException, JadePersistenceException,
            Exception;

    public void extournerDecisions(String idAffiliation, String fromDate) throws Exception;

    public void extournerDecisionsRollback(String idAffiliation) throws Exception;

    public String printDecision(String idDecision) throws Exception;

    public List<JadePublishDocument> printDecisionPassage(String idPassage) throws Exception;

    public DecisionCGASBean read(String idEntity) throws JadeApplicationException, JadePersistenceException;

    public DecisionCGASBean renouvelerDecisionCGAS(DecisionCGASBean decisionCGASBeanARenouveler,
            PassageModel thePassageFacturation, boolean hasPassageModuleFacturationDecisionCGAS,
            List<SimpleDecisionCGAS> theListDecisionCGASDejaExistanteDansAnneeDuRenouvellement,
            List<String> theListAffiliePlusieursCotisationDansAnneeDuRenouvellement) throws Exception;

    public ResultatTraitementMasseCsvJournal renouvelerDecisionCGASMasse(JadeProgressBarModel jadeProgressBarModel,
            boolean simulation, String annee, String numeroAffilieDebut, String numeroAffilieFin,
            String numeroPassageFacturation) throws Exception;

    public ComplexDecisionCGASAffiliationCotisationSearchModel search(
            ComplexDecisionCGASAffiliationCotisationSearchModel searchModel) throws JadePersistenceException;

    public DecisionCGASSearchModel search(DecisionCGASSearchModel searchModel) throws JadePersistenceException;

    public DecisionCGASBean update(DecisionCGASBean entity) throws Exception;

    public SimpleDecisionCGAS updateWithoutCalculCotis(SimpleDecisionCGAS decisionCgas)
            throws JadePersistenceException, Exception;

}
