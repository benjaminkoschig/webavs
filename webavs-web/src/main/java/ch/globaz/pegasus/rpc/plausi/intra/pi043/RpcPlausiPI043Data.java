package ch.globaz.pegasus.rpc.plausi.intra.pi043;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;

class RpcPlausiPI043Data extends RpcPlausiHeader {
    String idPca;
    Montant FC20;
    Montant FC12;

    public RpcPlausiPI043Data(RpcPlausi plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        return !FC12.isZero() || FC20.isZero();
    }

}
