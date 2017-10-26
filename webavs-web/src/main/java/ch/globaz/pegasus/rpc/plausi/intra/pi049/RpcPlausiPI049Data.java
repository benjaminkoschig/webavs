package ch.globaz.pegasus.rpc.plausi.intra.pi049;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;

public class RpcPlausiPI049Data extends RpcPlausiHeader {
    String idPca;
    Montant FC32;
    Montant FC21;
    Montant FC22;
    Montant FC23;

    public RpcPlausiPI049Data(RpcPlausi<RpcPlausiPI049Data> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        return FC21.add(FC22).add(FC23).greaterOrEquals(FC32);
    }
}
