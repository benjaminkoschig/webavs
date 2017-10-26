package ch.globaz.pegasus.rpc.plausi.intra.pi009;

import ch.globaz.pegasus.rpc.plausi.common.RpcPlausiCommonCalculData;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;

class RpcPlausiPI009Data extends RpcPlausiCommonCalculData {

    public RpcPlausiPI009Data(RpcPlausi<RpcPlausiCommonCalculData> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        sumDepense = depense.sum();
        sumRevenu = revenu.sum();
        return sumDepense.greaterOrEquals(sumRevenu);
    }

}
