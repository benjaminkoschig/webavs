package ch.globaz.pegasus.businessimpl.services.models.pcaccordee;

import globaz.cygnus.api.paiement.IRFPrestations;
import globaz.cygnus.db.paiement.RFPrestationAccordeeJointREPrestationAccordee;
import globaz.cygnus.db.paiement.RFPrestationAccordeeJointREPrestationAccordeeManager;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Iterator;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeAdaptationImpression;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeAdaptationImpressionAncienneSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeAdaptationImpressionSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalculSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.pcaccordee.PCAccordeeAdaptationImpressionService;
import ch.globaz.pegasus.business.vo.pcaccordee.Regimes02RFMVo;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class PCAccordeeAdaptationImpressionServiceImpl extends PegasusAbstractServiceImpl implements
        PCAccordeeAdaptationImpressionService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.pcaccordee.PCAccordeeService
     * #count(ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch)
     */
    @Override
    public int count(PCAccordeeAdaptationImpressionSearch search) throws PCAccordeeException, JadePersistenceException {
        if (search == null) {
            throw new PCAccordeeException("Unable to count pcAccordee, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    private Regimes02RFMVo fillRegimeVoObject(RFPrestationAccordeeJointREPrestationAccordee regimeRFM) {
        Regimes02RFMVo regimeVo = new Regimes02RFMVo();
        regimeVo.setCsGenrePrestationAccordee(regimeRFM.getCsGenrePrestationAccordee());
        regimeVo.setDateDebutDroit(regimeRFM.getDateDebutDroit());
        regimeVo.setDateEcheance(regimeRFM.getDateEcheance());
        regimeVo.setDateFinDroit(regimeRFM.getDateFinDroit());
        regimeVo.setDateValidationDecision(regimeRFM.getDateValidationDecision());
        regimeVo.setIdInfoCompta(regimeRFM.getIdInfoCompta());
        regimeVo.setIdTiers(regimeRFM.getIdTiers());
        regimeVo.setMontantPrestation(regimeRFM.getMontantPrestation());

        return regimeVo;
    }

    private void loadPlanCalculs(PCAccordeeAdaptationImpression pca) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimplePlanDeCalculSearch planSearch = new SimplePlanDeCalculSearch();
        planSearch.setForIdPCAccordee(pca.getIdPCAccordee());
        planSearch.setForIsPlanRetenu(true);
        ArrayList<SimplePlanDeCalcul> listePlanCalculs = new ArrayList<SimplePlanDeCalcul>();

        planSearch = (SimplePlanDeCalculSearch) JadePersistenceManager.search(planSearch, true);
        for (JadeAbstractModel absPlan : planSearch.getSearchResults()) {
            SimplePlanDeCalcul plan = (SimplePlanDeCalcul) absPlan;
            plan = PegasusServiceLocator.getSimplePlanDeCalculService().read(plan.getIdPlanDeCalcul());
            // plan = PegasusImplServiceLocator.getSimplePlanDeCalculService().read(plan.getIdPlanDeCalcul());
            listePlanCalculs.add(plan);
        }
        pca.setPlanCalculs(listePlanCalculs);
    }

    @Override
    public PCAccordeeAdaptationImpression read(String idDecisionHeader) throws JadePersistenceException,
            PCAccordeeException, JadeApplicationServiceNotAvailableException {

        if (JadeStringUtil.isEmpty(idDecisionHeader)) {
            throw new PCAccordeeException("Unable to find PCAccordeeAdaptationImpression, the id passed is null");
        }

        PCAccordeeAdaptationImpression decision = new PCAccordeeAdaptationImpression();
        decision.setId(idDecisionHeader);
        PCAccordeeAdaptationImpression result = (PCAccordeeAdaptationImpression) JadePersistenceManager.read(decision);

        // recherche du plan de calcul
        loadPlanCalculs(result);

        return result;
    }

    /**
     * Recherche de pca pour impression adaptation On va rechercher ici les anciennes pca
     */
    @Override
    public PCAccordeeAdaptationImpressionAncienneSearch search(
            PCAccordeeAdaptationImpressionAncienneSearch pcAccordeeSearch) throws JadePersistenceException,
            PCAccordeeException {
        if (pcAccordeeSearch == null) {
            throw new PCAccordeeException("Unable to search pcaccordee, the search model passed is null!");
        }
        return (PCAccordeeAdaptationImpressionAncienneSearch) JadePersistenceManager.search(pcAccordeeSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.pcaccordee.PCAccordeeService
     * #search(ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch)
     */
    @Override
    public PCAccordeeAdaptationImpressionSearch search(PCAccordeeAdaptationImpressionSearch pcAccordeeSearch)
            throws JadePersistenceException, PCAccordeeException {
        if (pcAccordeeSearch == null) {
            throw new PCAccordeeException("Unable to search pcaccordee, the search model passed is null!");
        }
        return (PCAccordeeAdaptationImpressionSearch) JadePersistenceManager.search(pcAccordeeSearch);
    }

    @Override
    public PCAccordeeAdaptationImpression search(String idDecisionHeader) throws JadePersistenceException,
            PCAccordeeException, JadeApplicationServiceNotAvailableException {
        PCAccordeeAdaptationImpressionSearch searchModel = new PCAccordeeAdaptationImpressionSearch();
        searchModel.setForIdDecisionHeader(idDecisionHeader);
        searchModel.setDefinedSearchSize(1);
        searchModel = this.search(searchModel);
        PCAccordeeAdaptationImpression pca = (PCAccordeeAdaptationImpression) searchModel.getSearchResults()[0];
        loadPlanCalculs(pca);
        return pca;
    }

    @Override
    public ArrayList<Regimes02RFMVo> searchRegimeRFM(ArrayList<String> idTiersBeneficiaires, String dateAdaptation)
            throws Exception {

        // Si liste de tiers vide
        if ((null == idTiersBeneficiaires) || (idTiersBeneficiaires.size() == 0)) {
            throw new PCAccordeeException(
                    "Unable to search regimes RFM, thge idtiers list passed passed is null or empty!");
        }

        // Si date adaptation vide
        if (dateAdaptation == null) {
            throw new PCAccordeeException(
                    "Unable to search regimes RFM, thge dateAdaptation passed passed is null or empty!");
        }

        ArrayList<Regimes02RFMVo> listeRetourResults = new ArrayList<Regimes02RFMVo>();

        // liste des types
        String[] listeTypeRegime = new String[2];
        listeTypeRegime[0] = IRFPrestations.CS_SOURCE_RFACCORDEES_REGIME;
        listeTypeRegime[1] = IRFPrestations.CS_SOURCE_RFACCORDEES_REGIME_DIABETIQUE;

        // Instanciation du manager
        RFPrestationAccordeeJointREPrestationAccordeeManager rfPreAccMgr = new RFPrestationAccordeeJointREPrestationAccordeeManager();
        rfPreAccMgr.setForIdTiersBeneFiciairesIn(idTiersBeneficiaires);
        rfPreAccMgr.setForDateAdaptation(dateAdaptation);
        rfPreAccMgr.setSession(BSessionUtil.getSessionFromThreadContext());
        rfPreAccMgr.setForCsSourceRfmAccordee(listeTypeRegime);
        // rfPreAccMgr.setForEnCoursAtMois(forEnCoursAtMois);
        rfPreAccMgr.changeManagerSize(0);
        rfPreAccMgr.find();

        // récupération de l'itérateur et retour de la liste
        Iterator<RFPrestationAccordeeJointREPrestationAccordee> rfPreAccItr = rfPreAccMgr.iterator();
        while (rfPreAccItr.hasNext()) {
            listeRetourResults.add(fillRegimeVoObject(rfPreAccItr.next()));
        }

        return listeRetourResults;

    }

}
