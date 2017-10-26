package ch.globaz.pegasus.rpc.plausi.gz.gz003;

import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;

public class RpcPlausiGZ003Data extends RpcPlausiHeader {
    String idPca;
    Integer FC35;
    Integer FC37;

    public RpcPlausiGZ003Data(RpcPlausi<RpcPlausiGZ003Data> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        boolean isOk = true;
        if (FC35 == 0) {
            isOk = false;
        }
        return isOk;
    }
}
