package globaz.cygnus.process.adaptationAnnuelle.octroi;

import globaz.cygnus.api.adaptationsJournalieres.IRFAdaptationJournaliere;
import globaz.cygnus.services.adaptationJournaliere.RFAdaptationJournaliereContext;
import globaz.cygnus.services.adaptationJournaliere.RFAdaptationJournaliereProvider;
import globaz.cygnus.utils.RFUtils;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSessionUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.List;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties;
import ch.globaz.pegasus.business.vo.decision.DecisionPcVO;
import com.google.gson.Gson;

public class RFProcessAdaptationAnnuelleOctroiHandlerEntity implements JadeProcessEntityInterface,
        JadeProcessEntityNeedProperties {

    private List<String[]> logsList = null;
    DecisionPcVO pcVOs = null;
    private Map<Enum<?>, String> properties = null;

    public RFProcessAdaptationAnnuelleOctroiHandlerEntity(List<String[]> logsList) {
        this.logsList = logsList;
    }

    public List<String[]> getLogsList() {
        return logsList;
    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {

        RFAdaptationJournaliereContext contextCourant = RFUtils.initContextAdaptation(pcVOs, BSessionUtil
                .getSessionFromThreadContext().getUserId());

        try {

            if (null != contextCourant) {

                contextCourant = RFAdaptationJournaliereProvider.getHandler(contextCourant,
                        BSessionUtil.getSessionFromThreadContext(),
                        BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction(), logsList,
                        BSessionUtil.getSessionFromThreadContext().getUserId(), true).executerAdaptation();

                if (contextCourant.getEtat().equals(IRFAdaptationJournaliere.ETAT_ECHEC)) {

                    contextCourant.setEtat(IRFAdaptationJournaliere.ETAT_ECHEC);

                    RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR,
                            contextCourant.getIdAdaptationJournaliere(), contextCourant.getIdTiersBeneficiaire(),
                            contextCourant.getNssTiersBeneficiaire(), contextCourant.getIdDecisionPc(),
                            contextCourant.getNumeroDecisionPc(),
                            "Impossible d'effectuer l'adaptation relative à la décision", getLogsList());

                } else {

                    RFUtils.ajouterLogAdaptation(FWViewBeanInterface.OK, contextCourant.getIdAdaptationJournaliere(),
                            contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                            contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(),
                            "l'adaptation c'est effectuée avec succés ", getLogsList());

                }

            } else {
                RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, "", "", "", "", "", "Context null",
                        getLogsList());
            }

        } catch (Exception e) {
            RFUtils.ajouterLogAdaptation(FWViewBeanInterface.ERROR, contextCourant.getIdAdaptationJournaliere(),
                    contextCourant.getIdTiersBeneficiaire(), contextCourant.getNssTiersBeneficiaire(),
                    contextCourant.getIdDecisionPc(), contextCourant.getNumeroDecisionPc(), e.getMessage(),
                    getLogsList());

            throw new JadePersistenceException(e.getMessage());
        }
    }

    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {
        Gson gson = new Gson();
        pcVOs = gson.fromJson(entity.getValue1(), DecisionPcVO.class);
    }

    public void setLogsList(List<String[]> logsList) {
        this.logsList = logsList;
    }

    @Override
    public void setProperties(Map<Enum<?>, String> map) {
        properties = map;
    }

}
