package ch.globaz.pegasus.rpc.plausi.intra.pi044;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

public class RpcPlausiPI044 implements RpcPlausiMetier<RpcPlausiPI044Data> {

    @Override
    public RpcPlausiPI044Data buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        final RpcPlausiPI044Data dataPlausi = new RpcPlausiPI044Data(this);

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
        return "PI-044";
    }

    @Override
    public String getReferance() {
        return "7228.0";
    }

    @Override
    public RpcPlausiCategory getCategory() {
        return RpcPlausiCategory.INFO;
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
