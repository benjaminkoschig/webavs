package ch.globaz.pegasus.rpc.plausi.intra.pi024;

import java.util.List;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;

class RpcPlausiPI024Data extends RpcPlausiHeader {
    String idPca;
    List<Boolean> personsElement;
    Montant FC33;

    public RpcPlausiPI024Data(RpcPlausiMetier<RpcPlausiPI024Data> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        boolean isDomicile = true;
        for (Boolean personDomicile : personsElement) {
            isDomicile = personDomicile ? isDomicile : false;
        }
        return !(!FC33.isZero() && !isDomicile);
    }

}
