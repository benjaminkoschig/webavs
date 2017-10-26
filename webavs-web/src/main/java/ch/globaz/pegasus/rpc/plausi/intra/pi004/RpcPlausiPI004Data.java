package ch.globaz.pegasus.rpc.plausi.intra.pi004;

import java.util.List;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnoncePerson;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;

public class RpcPlausiPI004Data extends RpcPlausiHeader {

    String idPca;
    List<AnnoncePerson> mandataires;

    public RpcPlausiPI004Data(RpcPlausiMetier<RpcPlausiPI004Data> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        return mandataires.size() == 1;
    }

}
