package ch.globaz.pegasus.rpc.process;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.process.byitem.ProcessItem;
import ch.globaz.pegasus.business.domaine.pca.PcaDecision;
import ch.globaz.pegasus.businessimpl.services.rpc.RpcAnnonceGenerator;
import ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader.RpcDataConverter;
import ch.globaz.pegasus.rpc.domaine.Annonce;
import ch.globaz.pegasus.rpc.domaine.AnnonceRpc;
import ch.globaz.pegasus.rpc.domaine.EtatAnnonce;
import ch.globaz.pegasus.rpc.domaine.LotAnnonceRpc;
import ch.globaz.pegasus.rpc.domaine.RpcData;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionWithIdPlanCal;
import ch.globaz.pegasus.rpc.domaine.TypeAnnonce;
import ch.globaz.pegasus.rpc.plausi.core.PlausiContainer;
import ch.globaz.pegasus.rpc.plausi.core.PlausisResults;
import ch.globaz.pegasus.utils.RpcUtil;

class AnnonceItem extends ProcessItem {
    private static final Logger LOG = LoggerFactory.getLogger(AnnonceItem.class);

    private final LotAnnonceRpc lotAnnonce;
    private final RpcDataConverter rpcDataConverter;
    private final RpcAnnonceGenerator annonceGenerator;
    private RpcData rpcData;
    private File file;
    private PlausisResults results = new PlausisResults();
    private AnnonceRpc annonce = new Annonce();

    public AnnonceItem(RpcDataConverter rpcDataConverter, LotAnnonceRpc lotAnnonce, RpcAnnonceGenerator annonceGenerator) {
        this.rpcDataConverter = rpcDataConverter;
        this.lotAnnonce = lotAnnonce;
        this.annonceGenerator = annonceGenerator;
    }

    @Override
    public String getDescription() {
        if (rpcData != null) {
            return rpcData.description();
        }
        return rpcDataConverter.description();
    }

    public AnnonceRpc getAnnonce() {
        return annonce;
    }

    public void setAnnonce(AnnonceRpc annonce) {
        this.annonce = annonce;
    }

    public PlausisResults checkPlausis() {
        return PlausiContainer.buildPlausis(rpcData);
    }

    protected AnnonceRpc createAnnonce(TypeAnnonce type) {
        List<RpcDecisionWithIdPlanCal> decisions = new ArrayList<RpcDecisionWithIdPlanCal>();

        for (PcaDecision pcaDecision : rpcData.resolveDecisionsRequerant().list()) {
            if (pcaDecision.getPca() != null) {
                decisions.add(new RpcDecisionWithIdPlanCal(pcaDecision.getDecision(), pcaDecision.getPca().getCalcul()
                        .getId(), pcaDecision.getPca().getRoleBeneficiaire()));
            } else {
                decisions.add(new RpcDecisionWithIdPlanCal(pcaDecision.getDecision()));
            }
        }
        for (PcaDecision pcaDecision : rpcData.resolveDecisionsConjoint().list()) {
            decisions.add(new RpcDecisionWithIdPlanCal(pcaDecision.getDecision(), pcaDecision.getPca().getCalcul()
                    .getId(), pcaDecision.getPca().getRoleBeneficiaire()));
        }
        annonce.setDossier(rpcData.getDossier());
        annonce.setDemande(rpcData.getDemande());
        annonce.setVersionDroit(rpcData.getVersionDroit());
        annonce.setDecisions(decisions);
        annonce.setLot(lotAnnonce);
        annonce.setEtat(EtatAnnonce.OUVERT);
        annonce.setType(type);
        return annonce;
    }

    @Override
    public void treat() throws Exception {
        try {
            rpcData = rpcDataConverter.convert();
            results = PlausiContainer.buildPlausis(rpcData);

            if (rpcDataConverter.isRefus()) {
                annonce = createAnnonce(TypeAnnonce.PARTIEL);
                file = annonceGenerator.getSedexConvertor201().generateMessageFileWithValidation(rpcData);
            } else if (rpcData.isSuppressionDecesRequerant()) {
                annonce = createAnnonce(TypeAnnonce.ANNULATION);
                RpcUtil.deleteSuffixDecisionId(rpcData.getRpcDecisionRequerantConjoints().get(0).getRequerant()
                        .getDecision(), rpcData.getRpcDecisionRequerantConjoints().get(0).getConjoint().getDecision());
                file = annonceGenerator.getSedexConvertor301().generateMessageFileWithValidation(rpcData);
            } else {
                annonce = createAnnonce(TypeAnnonce.COMPLET);
                file = annonceGenerator.getSedexConvertor101().generateMessageFileWithValidation(rpcData);
            }

            if (results.hasPlausiKo()) {
                annonce.setEtat(EtatAnnonce.PLAUSI_KO);
            }
        } catch (Exception e) {
            catchException(e);
            annonce = createErrorAnnonce(TypeAnnonce.COMPLET);
        } finally {
            // create();
        }
    }

    private AnnonceRpc createErrorAnnonce(TypeAnnonce type) {
        try {
            annonce.setDossier(rpcDataConverter.getDossier());
            annonce.setDemande(rpcDataConverter.getDemande());
            annonce.setVersionDroit(rpcDataConverter.getVersionDroit());
            annonce.setDecisions(rpcDataConverter.getDecisions());
        } catch (Exception e) {

        }
        annonce.setLot(lotAnnonce);
        annonce.setEtat(EtatAnnonce.ERROR);
        annonce.setType(type);
        lotAnnonce.addAnnonce(annonce);
        return annonce;
    }

    public PlausisResults getPlausisResults() {
        return results;
    }

    public File getFile() {
        return file;
    }

    public RpcData getRpcData() {
        return rpcData;
    }

}
