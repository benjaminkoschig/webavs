package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

import java.util.List;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.pca.Pca;
import ch.globaz.pegasus.rpc.domaine.RpcData;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionRequerantConjoint;

public class PcaSummer {
    private Montant pcaAi = Montant.ZERO;
    private Montant pcaAvs = Montant.ZERO;
    private int nbPcaCourante;
    private int nbRpcData = 0;

    public static PcaSummer sum(List<RpcData> rpcDatas) {
        PcaSummer pcaSummer = new PcaSummer();
        pcaSummer.sumCurrent(rpcDatas);
        return pcaSummer;
    }

    public Montant getTotalPcaAi() {
        return pcaAi;
    }

    public Montant getTotalPcaAvs() {
        return pcaAvs;
    }

    public int getNbPca() {
        return nbPcaCourante;
    }

    public Montant getPcaAi() {
        return pcaAi;
    }

    public Montant getPcaAvs() {
        return pcaAvs;
    }

    public int getNbPcaCourante() {
        return nbPcaCourante;
    }

    public int getNbRpcData() {
        return nbRpcData;
    }

    public void sum(RpcData rpcData) {
        nbRpcData++;
        for (RpcDecisionRequerantConjoint pca : rpcData.getRpcDecisionRequerantConjoints()) {
            if (pca.isCurrent()) {
                sumCurrenteAndCountPca(pca.getRequerant().getPca());
                if (pca.getSituation().isCoupleSepare()) {
                    sumCurrenteAndCountPca(pca.getConjoint().getPca());
                }
            }
        }
    }

    private void sumCurrent(List<RpcData> rpcDatas) {
        for (RpcData rpcData : rpcDatas) {
            this.sum(rpcData);
        }
    }

    private void sumCurrenteAndCountPca(Pca pca) {
        if (pca.hasCurrent()) {
            if (pca.getType().isAi()) {
                pcaAi = pcaAi.add(pca.getMontant());
            } else if (pca.getType().isAvs()) {
                pcaAvs = pcaAvs.add(pca.getMontant());
            }
            nbPcaCourante++;
        }
    }

}
