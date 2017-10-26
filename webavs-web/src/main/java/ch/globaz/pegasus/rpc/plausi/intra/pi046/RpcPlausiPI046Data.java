package ch.globaz.pegasus.rpc.plausi.intra.pi046;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;

public class RpcPlausiPI046Data extends RpcPlausiHeader {
    String idPca;
    Montant FC24;
    double FC25;
    Montant FC18;
    Montant tolerance;

    public RpcPlausiPI046Data(RpcPlausi<RpcPlausiPI046Data> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        return FC24.substract(FC18.multiply(FC25)).abs().less(tolerance);
    }
}
