package ch.globaz.pegasus.rpc.plausi.gz.gz004;

import java.util.Collections;
import java.util.List;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;

public class RpcPlausiGZ004Data extends RpcPlausiHeader {
    String idPca;

    List<String> lFC36;

    public RpcPlausiGZ004Data(RpcPlausiMetier<RpcPlausiGZ004Data> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        for (String id : lFC36) {
            if (Collections.frequency(lFC36, id) > 1) {
                return false;
            }
        }
        return true;
    }

    public void loadList(List<String> dicisions) {
        lFC36 = dicisions;
    }

}
