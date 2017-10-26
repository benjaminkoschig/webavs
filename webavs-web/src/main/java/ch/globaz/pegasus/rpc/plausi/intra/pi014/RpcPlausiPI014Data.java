package ch.globaz.pegasus.rpc.plausi.intra.pi014;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;

class RpcPlausiPI014Data extends RpcPlausiHeader {
    String idPca;
    Montant FC19;
    Montant FC27;

    public RpcPlausiPI014Data(RpcPlausi<RpcPlausiPI014Data> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        return FC27.greaterOrEquals(FC19);
    }

}
