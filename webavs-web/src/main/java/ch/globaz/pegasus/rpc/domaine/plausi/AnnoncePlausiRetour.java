package ch.globaz.pegasus.rpc.domaine.plausi;

import java.util.List;

public class AnnoncePlausiRetour {
    private final String businessCaseIdRPC;
    private final List<PlausiRetour> plausiRetours;

    public AnnoncePlausiRetour(String businessCaseIdRPC, List<PlausiRetour> plausiRetours) {
        this.businessCaseIdRPC = businessCaseIdRPC;
        this.plausiRetours = plausiRetours;
    }

    public String getBusinessCaseIdRPC() {
        return businessCaseIdRPC;
    }

    public List<PlausiRetour> getPlausiRetours() {
        return plausiRetours;
    }

}
