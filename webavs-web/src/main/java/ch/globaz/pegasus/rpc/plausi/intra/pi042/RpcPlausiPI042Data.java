package ch.globaz.pegasus.rpc.plausi.intra.pi042;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;

class RpcPlausiPI042Data extends RpcPlausiHeader {
    String idPca;
    Montant FC19;
    Montant FC27;
    boolean isDomicile;

    public RpcPlausiPI042Data(RpcPlausi plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        return isDomicile || (FC19.isZero() && FC27.isZero());
    }

}
