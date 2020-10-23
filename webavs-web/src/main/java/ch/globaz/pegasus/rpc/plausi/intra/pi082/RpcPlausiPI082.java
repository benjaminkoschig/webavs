package ch.globaz.pegasus.rpc.plausi.intra.pi082;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

import java.util.ArrayList;
import java.util.List;

public class RpcPlausiPI082 implements RpcPlausiMetier<RpcPlausiPI082Data> {


    @Override
    public RpcPlausiPI082Data buildPlausi(AnnonceDecision decision, AnnonceCase data) {

        final RpcPlausiPI082Data plausiData = new RpcPlausiPI082Data(this);
        plausiData.dateArrivee = decision.getRequestDateofReceipt();
        plausiData.decisionCause = decision.getDecisionCause();
        return plausiData;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTRA;
    }

    @Override
    public String getID() {
        return "PI-082";
    }

    @Override
    public String getReferance() {
        return "Registre PC - Réforme PC - 2020";
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
                add(RpcPlausiApplyToDecision.REJECT_FULL);
            }
        };
    }

}
