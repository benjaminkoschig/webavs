package ch.globaz.pegasus.rpc.domaine.annonce;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.naos.ree.tools.InfoCaisse;
import ch.globaz.pegasus.business.constantes.EPCRegionLoyer;
import ch.globaz.pegasus.rpc.businessImpl.converter.ConverterRentRegion;
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
            AnnonceDecision annonce = new AnnonceDecision(pca.buildRpcDecisionAnnonceCompleteRequerant(rpcData.getVersionDroit()));
            if(annonce.getCalculationElements().getRentRegion() == null
                    && pca.getConjointDatas() != null
                    && pca.getConjointDatas().getCalcul().getLoyerRegion() != null){
                EPCRegionLoyer region = pca.getConjointDatas().getCalcul().getLoyerRegion();
                annonce.getCalculationElements().setRentRegion(ConverterRentRegion.convert(region));
            }
            decisions.add(annonce);
            if (pca.hasConjoint()) {
                AnnonceDecision annonceConjoint = new AnnonceDecision(pca.buildRpcDecisionAnnonceCompleteConjoint(rpcData.getVersionDroit()));
                if(annonceConjoint.getCalculationElements().getRentRegion() == null) {
                    annonceConjoint.getCalculationElements().setRentRegion(annonce.getCalculationElements().getRentRegion());
                }
                decisions.add(annonceConjoint);
            }
        }
        businessCaseIdRPC = rpcData.getDossier().getId();
    }

    private void rentRegionFromConjoint () {

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
