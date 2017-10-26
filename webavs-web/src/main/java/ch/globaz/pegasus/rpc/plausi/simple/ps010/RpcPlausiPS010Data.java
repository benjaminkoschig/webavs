package ch.globaz.pegasus.rpc.plausi.simple.ps010;

import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;

public class RpcPlausiPS010Data extends RpcPlausiHeader {

    String idPca;
    Date dateDecision;
    Date FC6;

    public RpcPlausiPS010Data(RpcPlausiMetier<RpcPlausiPS010Data> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        if (FC6 == null) {
            return true;
        }
        Date tmpFC6 = new Date(FC6.getMois() + "." + FC6.getYear());
        Date tmpDecision = new Date(dateDecision.getMois() + "." + dateDecision.getYear());
        return !tmpFC6.after(tmpDecision);

    }

}
