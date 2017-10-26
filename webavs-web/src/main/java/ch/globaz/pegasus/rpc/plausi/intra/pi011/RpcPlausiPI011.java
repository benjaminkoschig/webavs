package ch.globaz.pegasus.rpc.plausi.intra.pi011;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

public class RpcPlausiPI011 implements RpcPlausiMetier<RpcPlausiPI011Data> {
    private final Montant tolerance;

    public RpcPlausiPI011(Montant tolerance) {
        this.tolerance = tolerance;
    }

    @Override
    public RpcPlausiPI011Data buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        final RpcPlausiPI011Data dataPlausi = new RpcPlausiPI011Data(this);

        dataPlausi.tolerance = tolerance;
        dataPlausi.idPca = decision.getPcaDecisionId();

        dataPlausi.FC18 = decision.getWealthConsidered();
        dataPlausi.FC10 = decision.getRealProperty();
        dataPlausi.FC11 = decision.getSelfInhabitedProperty();
        dataPlausi.FC12 = decision.getOtherWealth();
        dataPlausi.FC13 = decision.getDivestedWealth();
        dataPlausi.FC14 = decision.getMortgageDebts();
        dataPlausi.FC15 = decision.getOtherDebts();
        dataPlausi.FC16 = decision.getWealthDeductible();
        dataPlausi.FC17 = decision.getSelfInhabitedPropertyDeductible();

        return dataPlausi;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTRA;
    }

    @Override
    public String getID() {
        return "PI-011";
    }

    @Override
    public String getReferance() {
        return "1233.0";
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
