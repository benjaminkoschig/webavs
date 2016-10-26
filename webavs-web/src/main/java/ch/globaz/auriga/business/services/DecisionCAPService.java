package ch.globaz.auriga.business.services;

import globaz.jade.client.util.JadeProgressBarModel;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.parametreAssurance.AFParametreAssurance;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import java.util.List;
import ch.globaz.auriga.business.beans.decisioncap.DecisionCAPBean;
import ch.globaz.auriga.business.constantes.AUDecisionEtat;
import ch.globaz.auriga.business.models.ComplexEnfantDecisionCapWidgetSearchModel;
import ch.globaz.auriga.business.models.DecisionCAPSearchModel;
import ch.globaz.auriga.business.models.SimpleDecisionCAP;
import ch.globaz.common.business.models.ResultatTraitementMasseCsvJournal;
import ch.globaz.musca.business.models.PassageModel;
import ch.globaz.naos.business.model.AdhesionCotisationAssuranceSearchComplexModel;

public interface DecisionCAPService extends JadeApplicationService {

    /**
     * ATTENTION !!! Code spécifique à la CCVD
     */
    public static final String TYPE_COMPOSITION_TIERS_ENFANT_CODE_SYSTEM = "19170013";

    public SimpleDecisionCAP changeEtatDecision(String idDecision, AUDecisionEtat nouvelEtat)
            throws JadePersistenceException;

    public SimpleDecisionCAP create(SimpleDecisionCAP decisionCap, List<String> listIdEnfants)
            throws JadePersistenceException, Exception;

    public SimpleDecisionCAP delete(SimpleDecisionCAP decisionCap) throws JadePersistenceException, Exception;

    public void extournerDecisions(String idAffiliation, String fromDate) throws Exception;

    public void extournerDecisionsRollback(String idAffiliation) throws Exception;

    public AFAssurance findAssurance(String idAffiliation) throws Exception;

    public AdhesionCotisationAssuranceSearchComplexModel findAssuranceWidget(
            AdhesionCotisationAssuranceSearchComplexModel searchModel) throws Exception;

    public AFParametreAssurance findParamAssurance(String idAssurance, String genre, String annee) throws Exception;

    public AFTauxAssurance findTauxAssurance(String idAssurance, String annee) throws Exception;

    public double getMontantAFAFacturer(SimpleDecisionCAP decisionCap, int nbMoisAFacturer);

    public double getMontantAFAFacturerForSortieRectificative(SimpleDecisionCAP decisionCapRectificative,
            SimpleDecisionCAP decisionCapRectifiee);

    public String printDecision(String idDecision) throws Exception;

    public List<JadePublishDocument> printDecisionPassage(String idPassage) throws Exception;

    public SimpleDecisionCAP read(String idDecisionCap) throws JadePersistenceException;

    public DecisionCAPBean renouvelerDecisionCAP(DecisionCAPBean decisionCAPBeanARenouveler,
            PassageModel thePassageFacturation, boolean hasPassageModuleFacturationDecisionCAP,
            String theTypeAssurance, List<SimpleDecisionCAP> theListDecisionCAPDejaExistanteDansAnneeDuRenouvellement,
            List<String> theListAffiliePlusieursCotisationDansAnneeDuRenouvellement, String theIdAssurance)
            throws Exception;

    public ResultatTraitementMasseCsvJournal renouvelerDecisionCAPMasse(JadeProgressBarModel jadeProgressBarModel,
            boolean simulation, String annee, String numeroAffilieDebut, String numeroAffilieFin,
            String numeroPassageFacturation) throws Exception;

    public DecisionCAPSearchModel search(DecisionCAPSearchModel searchModel) throws JadePersistenceException;

    public ComplexEnfantDecisionCapWidgetSearchModel searchEnfantDecisionCapWidget(
            ComplexEnfantDecisionCapWidgetSearchModel searchModel) throws Exception;

    public SimpleDecisionCAP update(SimpleDecisionCAP decisionCap, List<String> listIdEnfants)
            throws JadePersistenceException, Exception;

    public SimpleDecisionCAP updateWithoutCalculCotis(SimpleDecisionCAP decisionCap) throws JadePersistenceException,
            Exception;

    public String getLibelleCategorieDecisionPrintedInRenouvellement() throws Exception;
}
