package ch.globaz.pegasus.rpc.plausi.intra.pi014;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

public class RpcPlausiPI014 implements RpcPlausiMetier<RpcPlausiPI014Data> {

    @Override
    public RpcPlausiPI014Data buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        final RpcPlausiPI014Data dataPlausi = new RpcPlausiPI014Data(this);

        dataPlausi.idPca = decision.getPcaDecisionId();

        dataPlausi.FC19 = decision.getGrossRental();
        dataPlausi.FC27 = decision.getRentGrossTotal();

        return dataPlausi;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTRA;
    }

    @Override
    public String getID() {
        return "PI-014";
    }

    @Override
    public String getReferance() {
        return "2210.0";
    }

    @Override
    public RpcPlausiCategory getCategory() {
        return RpcPlausiCategory.ERROR;
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
