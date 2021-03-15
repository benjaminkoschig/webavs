package ch.globaz.pegasus.rpc.plausi.intra.pi028;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.plausi.common.RpcPlausiCommonCalcul;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

public class RpcPlausiPI028 extends RpcPlausiCommonCalcul {

    Montant maxPar1;
    Montant maxPar2;

    public RpcPlausiPI028(Montant maxPar1, Montant maxPar2) {
        this.maxPar1 = maxPar1;
        this.maxPar2 = maxPar2;
    }

    @Override
    public RpcPlausiPI028Data buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        final RpcPlausiPI028Data plausiData = new RpcPlausiPI028Data(this);

        plausiData.setReforme(decision.getAnnonce().getPcaDecision().getPca().getReformePC());

        plausiData.Par1 = maxPar1;
        plausiData.Par2 = maxPar2;

        plausiData.FC17 = decision.getSelfInhabitedPropertyDeductible();
        plausiData.FC11 = decision.getSelfInhabitedProperty();

        plausiData.isCoupleSepare = data.getDecisions().size() > 1;
        // plausiData.resolveHasAPI(membresFamilleWithDonneesFinanciere.getMembresFamilleWithDonneesFinanciere());

        return plausiData;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTRA;
    }

    @Override
    public String getID() {
        return "PI-028";
    }

    @Override
    public String getReferance() {
        return "Empty";
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
