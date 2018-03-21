package ch.globaz.pegasus.rpc.plausi.simple.ps011;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

public class RpcPlausiPS011 implements RpcPlausiMetier<RpcPlausiPS011Data> {

    @Override
    public RpcPlausiPS011Data buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        RpcPlausiPS011Data plausiData = new RpcPlausiPS011Data(this);
        plausiData.validFrom = decision.getValidFrom();
        return plausiData;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.SIMPLE;
    }

    @Override
    public String getID() {
        return "PS-011";
    }

    @Override
    public String getReferance() {
        //TODO completer ces rubriques
        return "Ajout futur";
    }

    @Override
    public RpcPlausiCategory getCategory() {
      //TODO completer ces rubriques
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
