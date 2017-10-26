package ch.globaz.pegasus.rpc.domaine.annonce;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.naos.ree.tools.InfoCaisse;
import ch.globaz.pegasus.rpc.domaine.RpcData;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionRequerantConjoint;

public class AnnonceCase {

    protected RpcData rpcData;

    protected String businessCaseIdRPC;
    protected List<AnnonceDecision> decisions;

    public AnnonceCase(RpcData rpcData) {
        this.rpcData = rpcData;
        decisions = new ArrayList<AnnonceDecision>();
        initDecisions();
    }

    private void initDecisions() {
        for (RpcDecisionRequerantConjoint pca : rpcData.getRpcDecisionRequerantConjoints()) {
            decisions.add(new AnnonceDecision(pca.buildRpcDecisionAnnonceCompleteRequerant(rpcData.getVersionDroit())));
            if (pca.hasConjoint()) {
                decisions.add(new AnnonceDecision(
                        pca.buildRpcDecisionAnnonceCompleteConjoint(rpcData.getVersionDroit())));
            }
        }
        businessCaseIdRPC = rpcData.getDossier().getId();
    }

    public String getBusinessCaseIdRPC() {
        return businessCaseIdRPC;
    }

    public List<AnnonceDecision> getDecisions() {
        return decisions;
    }

    public boolean isRequerantDomicile() {
        boolean isDomicile = false;

        for (AnnonceDecision decisionCase : getDecisions()) {
            if (decisionCase.getPersonRequerant() != null) {
                isDomicile = decisionCase.getPersonRequerant().getHousingMode().isDomicile();
            }
        }

        return isDomicile;
    }

    public void setAnnonceDeliveryOffice(InfoCaisse infoCaisse) {
        for (AnnonceDecision decision : decisions) {
            decision.setDeliveryOffice(infoCaisse);
        }

    }

}
