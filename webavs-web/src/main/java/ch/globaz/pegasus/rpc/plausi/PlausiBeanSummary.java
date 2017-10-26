package ch.globaz.pegasus.rpc.plausi;

import ch.globaz.pegasus.rpc.domaine.RetourAnnonce;
import ch.globaz.simpleoutputlist.annotation.Column;

public class PlausiBeanSummary {
    private final RetourAnnonce plausis;
    private final int nb;

    public PlausiBeanSummary(RetourAnnonce plausis, int nb) {
        this.plausis = plausis;
        this.nb = nb;
    }

    @Column(name = "RPC_PLAUSI_CATEGORY")
    public String getCategory() {
        if (plausis.getCategorie() != null) {
            return plausis.getCategorie().toString();
        }
        return null;
    }

    @Column(name = "RPC_CODE_PLAUSI")
    public String getId() {
        return plausis.getCodePlausi();
    }

    @Column(name = "RPC_TYPE_PLAUSI")
    public String getType() {
        return plausis.getType().toString();
    }

    @Column(name = "RPC_NB_PLAUSI")
    public int getNb() {
        return nb;
    }

}
