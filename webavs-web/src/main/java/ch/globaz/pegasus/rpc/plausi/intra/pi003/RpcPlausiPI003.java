package ch.globaz.pegasus.rpc.plausi.intra.pi003;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.plausi.common.RpcPlausiCommonCalcul;
import ch.globaz.pegasus.rpc.plausi.common.RpcPlausiCommonCalculData;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

public class RpcPlausiPI003 extends RpcPlausiCommonCalcul {

    @Override
    public RpcPlausiCommonCalculData buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        final RpcPlausiPI003Data plausiData = new RpcPlausiPI003Data(this);

        plausiData.FC7 = decision.getAmountNoHC();
        plausiData.FC9 = decision.getElLimit();

        buildRevenu(decision, plausiData);
        buildDepense(decision, plausiData, false);

        return plausiData;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTRA;
    }

    @Override
    public String getID() {
        return "PI-003";
    }

    @Override
    public String getReferance() {
        return "301(bis)";
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
            }
        };
    }
}