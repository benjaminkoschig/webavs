package ch.globaz.pegasus.businessimpl.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.decision.DecisionPCAPlanCalcule;
import ch.globaz.pegasus.business.models.decision.DecisionPCAPlanCalculeSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalculSearch;
import ch.globaz.pegasus.business.services.models.decision.FortuneService;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

public class FortuneServiceImpl implements FortuneService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.pegasus.business.services.models.decision.FortuneService#calculeFortuneFromDecision(java.lang.String)
     */
    @Override
    public BigDecimal calculeFortuneFromDecision(String idDecision) throws JadeApplicationServiceNotAvailableException,
            DecisionException {

        if (idDecision == null) {
            throw new DecisionException("Unable to search , the idDecision passed is null!");
        }
        DecisionPCAPlanCalculeSearch decisionPCAPlanCalculeSearch = new DecisionPCAPlanCalculeSearch();
        decisionPCAPlanCalculeSearch.setForIsPlanRetenu(true);
        decisionPCAPlanCalculeSearch.setForIdDecisionHeader(idDecision);
        try {
            decisionPCAPlanCalculeSearch = (DecisionPCAPlanCalculeSearch) JadePersistenceManager.search(
                    decisionPCAPlanCalculeSearch, true);
        } catch (JadePersistenceException e) {
            e.printStackTrace();
        }
        if (decisionPCAPlanCalculeSearch.getSize() != 1) {
            throw new DecisionException("Error in select too many value or not. Restult query size: "
                    + decisionPCAPlanCalculeSearch.getSize());
        }

        SimplePlanDeCalcul simplePlanDeCalcul = ((DecisionPCAPlanCalcule) decisionPCAPlanCalculeSearch
                .getSearchResults()[0]).getSimplePlanDeCalcul();

        String byteArrayToString = new String(simplePlanDeCalcul.getResultatCalcul());
        TupleDonneeRapport tupleDonneeRapport = PegasusImplServiceLocator.getCalculPersistanceService()
                .deserialiseDonneesCcXML(byteArrayToString);

        BigDecimal fortune = new BigDecimal(
                tupleDonneeRapport.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_SOUS_TOTAL));
        BigDecimal detteProuvee = new BigDecimal(
                tupleDonneeRapport.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_AUT_DETT_TOTAL));
        BigDecimal dettHyp = new BigDecimal(
                tupleDonneeRapport.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_TOTAL));

        BigDecimal total = (fortune.subtract(detteProuvee)).subtract(dettHyp);
        return total;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.pegasus.business.services.models.decision.FortuneService#calculeFortuneFromPcAccordee(java.lang.String)
     */
    @Override
    public BigDecimal calculeFortuneFromPcAccordee(String idPcAccordee)
            throws JadeApplicationServiceNotAvailableException, PCAccordeeException {

        SimplePlanDeCalculSearch planCalculSearch = new SimplePlanDeCalculSearch();
        planCalculSearch.setForIdPCAccordee(idPcAccordee);
        planCalculSearch.setForIsPlanRetenu(Boolean.TRUE);

        try {
            planCalculSearch = (SimplePlanDeCalculSearch) JadePersistenceManager.search(planCalculSearch, true);
        } catch (JadePersistenceException e) {
            e.printStackTrace();
        }
        if (planCalculSearch.getSize() != 1) {
            throw new PCAccordeeException("Error in select too many value or not. Restult query size: "
                    + planCalculSearch.getSize());
        }

        SimplePlanDeCalcul simplePlanCalucl = (SimplePlanDeCalcul) planCalculSearch.getSearchResults()[0];

        String byteArrayToString = new String(simplePlanCalucl.getResultatCalcul());

        TupleDonneeRapport tupleDonneeRapport = PegasusImplServiceLocator.getCalculPersistanceService()
                .deserialiseDonneesCcXML(byteArrayToString);

        BigDecimal fortune = new BigDecimal(
                tupleDonneeRapport.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_SOUS_TOTAL));
        BigDecimal detteProuvee = new BigDecimal(
                tupleDonneeRapport.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_AUT_DETT_TOTAL));
        BigDecimal dettHyp = new BigDecimal(
                tupleDonneeRapport.getValeurEnfant(IPCValeursPlanCalcul.CLE_FORTU_DETE_HYP_TOTAL));

        BigDecimal total = (fortune.subtract(detteProuvee)).subtract(dettHyp);
        return total;
    }
}
