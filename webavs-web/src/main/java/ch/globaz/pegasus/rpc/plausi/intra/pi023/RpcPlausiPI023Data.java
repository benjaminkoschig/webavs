package ch.globaz.pegasus.rpc.plausi.intra.pi023;

import java.util.List;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;

class RpcPlausiPI023Data extends RpcPlausiHeader {
    String idPca;
    List<Boolean> personsElement;
    Montant FC33;

    public RpcPlausiPI023Data(RpcPlausiMetier<RpcPlausiPI023Data> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        boolean isDomicile = false;
        for (Boolean personDomicile : personsElement) {
            isDomicile = personDomicile ? true : isDomicile;
        }
        return !(FC33.isZero() && isDomicile);
    }
}
