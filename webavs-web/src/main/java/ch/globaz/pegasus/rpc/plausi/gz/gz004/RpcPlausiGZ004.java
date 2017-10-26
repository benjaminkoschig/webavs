package ch.globaz.pegasus.rpc.plausi.gz.gz004;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

public class RpcPlausiGZ004 implements RpcPlausiMetier<RpcPlausiGZ004Data> {

    @Override
    public RpcPlausiGZ004Data buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        final RpcPlausiGZ004Data dataPlausi = new RpcPlausiGZ004Data(this);
        dataPlausi.idPca = decision.getDecisionId();
        List<String> list = new ArrayList<String>();
        for (AnnonceDecision dec : data.getDecisions()) {
            list.add(dec.getDecisionId());
        }
        dataPlausi.loadList(list);
        return dataPlausi;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTERNAL;
    }

    @Override
    public String getID() {
        return "GZ-004";
    }

    @Override
    public String getReferance() {
        return "PR-025";
    }

    @Override
    public RpcPlausiCategory getCategory() {
        return RpcPlausiCategory.DATA_INTEGRITY;
    }

    @Override
    public List<RpcPlausiApplyToDecision> getApplyTo() {
        return new ArrayList<RpcPlausiApplyToDecision>() {
            {
                add(RpcPlausiApplyToDecision.POSITIVE);
                add(RpcPlausiApplyToDecision.REJECT_FULL);
                add(RpcPlausiApplyToDecision.CANCEL);
            }
        };
    }

}
