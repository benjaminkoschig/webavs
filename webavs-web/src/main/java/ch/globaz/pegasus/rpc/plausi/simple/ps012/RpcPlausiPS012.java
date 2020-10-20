package ch.globaz.pegasus.rpc.plausi.simple.ps012;

import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

import java.util.ArrayList;
import java.util.List;

public class RpcPlausiPS012 implements RpcPlausiMetier<RpcPlausiPS012Data> {

    @Override
    public RpcPlausiPS012Data buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        RpcPlausiPS012Data plausiData = new RpcPlausiPS012Data(this);
        plausiData.validFrom = decision.getValidFrom();
        return plausiData;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.SIMPLE;
    }

    @Override
    public String getID() {
        return "PS-012";
    }

    @Override
    public String getReferance() {
        return " ";
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
                add(RpcPlausiApplyToDecision.REJECT_FULL);
                add(RpcPlausiApplyToDecision.REJECT_SMALL);
            }
        };
    }

}
