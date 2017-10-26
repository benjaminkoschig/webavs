package ch.globaz.pegasus.rpc.plausi.simple.ps010;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

public class RpcPlausiPS010 implements RpcPlausiMetier<RpcPlausiPS010Data> {

    @Override
    public RpcPlausiPS010Data buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        RpcPlausiPS010Data plausiData = new RpcPlausiPS010Data(this);
        plausiData.dateDecision = decision.getDecisionDate();
        plausiData.FC6 = decision.getValidTo();
        return plausiData;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.SIMPLE;
    }

    @Override
    public String getID() {
        return "PS-010";
    }

    @Override
    public String getReferance() {
        return "Mail Lur vom 28.4.2017";
    }

    @Override
    public RpcPlausiCategory getCategory() {
        return RpcPlausiCategory.BLOCKING;
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
