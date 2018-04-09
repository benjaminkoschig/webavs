package ch.globaz.pegasus.rpc.plausi.simple.ps011;

import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;

public class RpcPlausiPS011Data extends RpcPlausiHeader {

    String idPca;
    Date validFrom;

    public RpcPlausiPS011Data(RpcPlausiMetier<RpcPlausiPS011Data> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        if (validFrom == null) {
            return false;
        }
        Date tmpvalidF = new Date(validFrom.getMois() + "." + validFrom.getYear());
        Date tmpAnnonce = new Date(new Date().getMois() + "." + new Date().getYear());
        return tmpvalidF.before(tmpAnnonce);
    }

}
