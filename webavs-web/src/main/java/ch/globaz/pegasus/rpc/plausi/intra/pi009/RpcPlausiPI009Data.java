package ch.globaz.pegasus.rpc.plausi.intra.pi009;

import java.math.BigInteger;
import ch.globaz.pegasus.rpc.plausi.common.RpcPlausiCommonCalculData;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;

class RpcPlausiPI009Data extends RpcPlausiCommonCalculData {

    BigInteger FC2;

    public RpcPlausiPI009Data(RpcPlausi<RpcPlausiCommonCalculData> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        if (isReforme || !FC2.equals(BigInteger.valueOf(6))) {
            return true;
        } else {
            sumDepense = depense.sum();
            sumRevenu = revenu.sum();
            return sumDepense.greaterOrEquals(sumRevenu);
        }
    }

}
