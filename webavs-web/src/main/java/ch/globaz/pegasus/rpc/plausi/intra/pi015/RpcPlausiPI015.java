package ch.globaz.pegasus.rpc.plausi.intra.pi015;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.plausi.common.RpcPlausiCommonCalcul;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

public class RpcPlausiPI015 extends RpcPlausiCommonCalcul {

    @Override
    public RpcPlausiPI015Data buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        final RpcPlausiPI015Data dataPlausi = new RpcPlausiPI015Data(this);

        dataPlausi.setReforme(decision.getAnnonce().getPcaDecision().getPca().getReformePC());

        dataPlausi.idPca = decision.getPcaDecisionId();

        dataPlausi.isDomicile = decision.getPersonRepresentative().getHousingMode().isDomicile();

        dataPlausi.FC19 = decision.getGrossRental();

        return dataPlausi;
    }
    
    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTRA;
    }

    @Override
    public String getID() {
        return "PI-015";
    }

    @Override
    public String getReferance() {
        return "2211.0";
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
