package ch.globaz.pegasus.rpc.plausi.simple.ps012;

import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;

public class RpcPlausiPS012Data extends RpcPlausiHeader {

    String idPca;
    Date validFrom;

    public RpcPlausiPS012Data(RpcPlausiMetier<RpcPlausiPS012Data> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {

        // TODO A modifier pour PS-012

        if (validFrom == null) {
            return false;
        }
        Date tmpvalidF = new Date(validFrom.getMois() + "." + validFrom.getYear());
        Date tmpAnnonce = new Date(new Date().getMois() + "." + new Date().getYear());
        return tmpvalidF.before(tmpAnnonce);
    }

}
