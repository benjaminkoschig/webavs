package ch.globaz.pegasus.rpc.plausi.intra.pi004;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnoncePerson;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

public class RpcPlausiPI004 implements RpcPlausiMetier<RpcPlausiPI004Data> {

    @Override
    public RpcPlausiPI004Data buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        final RpcPlausiPI004Data dataPlausi = new RpcPlausiPI004Data(this);
        dataPlausi.mandataires = new ArrayList<AnnoncePerson>();
        dataPlausi.idPca = decision.getPcaDecisionId();

        // Load address
        if (decision.getPersons() != null) {
            for (AnnoncePerson personData : decision.getPersons()) {
                if (personData.getRepresentative()) {
                    dataPlausi.mandataires.add(personData);
                }
            }
        }
        return dataPlausi;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTRA;
    }

    @Override
    public String getID() {
        return "PI-004";
    }

    @Override
    public String getReferance() {
        return "403";
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
                add(RpcPlausiApplyToDecision.REJECT_SMALL);
            }
        };
    }

}
