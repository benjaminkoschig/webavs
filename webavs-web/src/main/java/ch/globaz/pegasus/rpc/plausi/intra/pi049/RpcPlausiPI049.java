package ch.globaz.pegasus.rpc.plausi.intra.pi049;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

public class RpcPlausiPI049 implements RpcPlausiMetier<RpcPlausiPI049Data> {

    @Override
    public RpcPlausiPI049Data buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        final RpcPlausiPI049Data dataPlausi = new RpcPlausiPI049Data(this);

        dataPlausi.idPca = decision.getPcaDecisionId();

        dataPlausi.FC32 = decision.getInterestFeesEligible();
        dataPlausi.FC21 = decision.getPropertyIncome();
        dataPlausi.FC22 = decision.getRentalValue();
        dataPlausi.FC23 = decision.getUsufructIncome();

        return dataPlausi;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTRA;
    }

    @Override
    public String getID() {
        return "PI-049";
    }

    @Override
    public String getReferance() {
        return "7232.0";
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
