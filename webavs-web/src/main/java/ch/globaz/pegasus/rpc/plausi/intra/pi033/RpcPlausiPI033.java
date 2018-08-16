package ch.globaz.pegasus.rpc.plausi.intra.pi033;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.plausi.common.RpcPlausiCommonCalcul;
import ch.globaz.pegasus.rpc.plausi.common.RpcPlausiCommonCalculData;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

public class RpcPlausiPI033 extends RpcPlausiCommonCalcul {


    @Override
    public RpcPlausiCommonCalculData buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        final RpcPlausiPI033Data plausiData = new RpcPlausiPI033Data(this);
        plausiData.persons = decision.getPersons();
        return plausiData;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTRA;
    }

    @Override
    public String getID() {
        return "PI-033";
    }

    @Override
    public String getReferance() {
        return "2259.0";
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
