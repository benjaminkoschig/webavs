package ch.globaz.pegasus.rpc.plausi.intra.pi044;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;

class RpcPlausiPI044Data extends RpcPlausiHeader {
    String idPca;
    Montant FC20;
    Montant FC12;

    public RpcPlausiPI044Data(RpcPlausi plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        return (FC12.divide(5)).greaterOrEquals(FC20);
    }

}
