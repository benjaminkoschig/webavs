package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce;

import ch.globaz.pegasus.business.domaine.droit.VersionDroit;
import ch.globaz.pegasus.business.domaine.pca.PcaDecisions;

public class AnnonceData {
    // private final AnnonceRpc annonceRpc;
    private final PcaDecisions pcaDecisions;
    private final VersionDroit versionDroit;

    public AnnonceData(PcaDecisions pcaDecisions, VersionDroit versionDroit) {
        this.pcaDecisions = pcaDecisions;
        this.versionDroit = versionDroit;
    }

    public PcaDecisions getPcaDecisions() {
        return pcaDecisions;
    }

    public VersionDroit getVersionDroit() {
        return versionDroit;
    }

    // public boolean addPcaDecison(PcaDecision pcaDecision) {
    // return pcaDecisions.add(pcaDecision);
    // }
    //
    // public boolean addllPcaDecison(List<PcaDecision> pcaDecisions) {
    // return this.pcaDecisions.addAll(pcaDecisions);
    // }
}
