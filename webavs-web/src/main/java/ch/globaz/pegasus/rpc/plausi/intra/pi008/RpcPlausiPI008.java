package ch.globaz.pegasus.rpc.plausi.intra.pi008;

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

public class RpcPlausiPI008 extends RpcPlausiCommonCalcul {

    private final Montant tolerance;

    public RpcPlausiPI008(Montant tolerance) {
        this.tolerance = tolerance;
    }

    @Override
    public RpcPlausiCommonCalculData buildPlausi(AnnonceDecision decision, AnnonceCase data) {
            final RpcPlausiPI008Data plausiData = new RpcPlausiPI008Data(this);

            plausiData.setReforme(decision.getAnnonce().getPcaDecision().getPca().getReformePC());

            plausiData.tolerance = tolerance;
            plausiData.FC2 = decision.getDecisionKind();
            plausiData.FC8 = decision.getAmountWithHC();
            plausiData.FC9 = decision.getElLimit();

            buildRevenu(decision, plausiData, data);
            buildDepense(decision, plausiData, true);

            return plausiData;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTRA;
    }

    @Override
    public String getID() {
        return "PI-008";
    }

    @Override
    public String getReferance() {
        return "1202.0";
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
