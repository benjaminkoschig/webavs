package ch.globaz.pegasus.rpc.plausi.intra.pi015;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.plausi.common.RpcPlausiCommonCalculData;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;

class RpcPlausiPI015Data extends RpcPlausiCommonCalculData {
    String idPca;
    boolean isDomicile;
    Montant FC19;

    public RpcPlausiPI015Data(RpcPlausi<RpcPlausiCommonCalculData> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        return !isDomicile || !FC19.isZero();
    }
}
