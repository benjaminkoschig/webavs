package ch.globaz.pegasus.rpc.plausi.simple.ps012;

import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;


public class RpcPlausiPS012Data extends RpcPlausiHeader {

    public RpcPlausiPS012Data(RpcPlausiMetier<RpcPlausiPS012Data> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {

        // TODO A modifier pour PS-012
        return true;
    }

}
