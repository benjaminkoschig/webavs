package ch.globaz.pegasus.rpc.plausi.intra.pi043;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

public class RpcPlausiPI043 implements RpcPlausiMetier<RpcPlausiPI043Data> {

    @Override
    public RpcPlausiPI043Data buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        final RpcPlausiPI043Data dataPlausi = new RpcPlausiPI043Data(this);

        dataPlausi.idPca = decision.getPcaDecisionId();

        dataPlausi.FC20 = decision.getWealthIncome();
        dataPlausi.FC12 = decision.getOtherWealth();

        return dataPlausi;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTRA;
    }

    @Override
    public String getID() {
        return "PI-043";
    }

    @Override
    public String getReferance() {
        return "7227.0";
    }

    @Override
    public RpcPlausiCategory getCategory() {
        return RpcPlausiCategory.WARNING;
    }

    @Override
    public List<RpcPlausiApplyToDecision> getApplyTo() {
        return new ArrayList<RpcPlausiApplyToDecision>() {
            {
                add(RpcPlausiApplyToDecision.POSITIVE);
                add(RpcPlausiApplyToDecision.REJECT_FULL);
            }
        };
    }
}
