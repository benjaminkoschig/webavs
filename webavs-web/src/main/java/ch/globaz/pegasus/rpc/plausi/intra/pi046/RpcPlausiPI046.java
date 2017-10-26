package ch.globaz.pegasus.rpc.plausi.intra.pi046;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

public class RpcPlausiPI046 implements RpcPlausiMetier<RpcPlausiPI046Data> {
    private final Montant tolerance;

    public RpcPlausiPI046(Montant tolerance) {
        this.tolerance = tolerance;
    }

    @Override
    public RpcPlausiPI046Data buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        final RpcPlausiPI046Data dataPlausi = new RpcPlausiPI046Data(this);

        dataPlausi.idPca = decision.getPcaDecisionId();
        dataPlausi.tolerance = tolerance;

        dataPlausi.FC24 = decision.getWealthIncomeConsidered();
        dataPlausi.FC25 = decision.getWealthIncomeRate().doubleValue();
        dataPlausi.FC18 = decision.getWealthConsidered();

        return dataPlausi;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTRA;
    }

    @Override
    public String getID() {
        return "PI-046";
    }

    @Override
    public String getReferance() {
        return "7229.0";
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
