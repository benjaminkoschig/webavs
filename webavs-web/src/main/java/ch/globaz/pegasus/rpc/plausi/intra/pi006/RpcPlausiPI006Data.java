package ch.globaz.pegasus.rpc.plausi.intra.pi006;

import java.util.List;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;

class RpcPlausiPI006AddressContainer {
    // Municipality => livingAddress
    int P11;
    // Municipality => legalAddress
    int P6;
    // Housing mode == 1 => Domicile
    Boolean P12;
}

class RpcPlausiPI006Data extends RpcPlausiHeader {
    String idPca;
    List<RpcPlausiPI006AddressContainer> personsData;

    public RpcPlausiPI006Data(RpcPlausi<RpcPlausiPI006Data> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        boolean isOk = true;
        for (RpcPlausiPI006AddressContainer address : personsData) {
            if (isOk) {
                isOk = address.P12 ? address.P11 == address.P6 : true;
            }
        }
        return isOk;
    }
}
