package ch.globaz.pegasus.rpc.plausi.intra.pi002;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.plausi.common.RpcPlausiCommonCalcul;
import ch.globaz.pegasus.rpc.plausi.common.RpcPlausiCommonCalculData;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

public class RpcPlausiPI002 extends RpcPlausiCommonCalcul {

    private final Montant tolerance;

    public RpcPlausiPI002(Montant tolerance) {
        this.tolerance = tolerance;
    }

    @Override
    public RpcPlausiCommonCalculData buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        final RpcPlausiPI002Data plausiData = new RpcPlausiPI002Data(this);

        plausiData.tolerance = tolerance;

        plausiData.FC7 = decision.getAmountNoHC();
        plausiData.FC9 = decision.getElLimit();

        buildRevenu(decision, plausiData, data);
        buildDepense(decision, plausiData, false);

        return plausiData;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTRA;
    }

    @Override
    public String getID() {
        return "PI-002";
    }

    @Override
    public String getReferance() {
        return "301";
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
            }
        };
    }
}
