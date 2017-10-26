package ch.globaz.pegasus.rpc.plausi.intra.pi042;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

public class RpcPlausiPI042 implements RpcPlausiMetier<RpcPlausiPI042Data> {

    @Override
    public RpcPlausiPI042Data buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        final RpcPlausiPI042Data dataPlausi = new RpcPlausiPI042Data(this);

        dataPlausi.idPca = decision.getPcaDecisionId();
        dataPlausi.FC19 = decision.getGrossRental();
        dataPlausi.FC27 = decision.getRentGrossTotal();
        dataPlausi.isDomicile = data.isRequerantDomicile();

        return dataPlausi;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTRA;
    }

    @Override
    public String getID() {
        return "PI-042";
    }

    @Override
    public String getReferance() {
        return "7212.0";
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
