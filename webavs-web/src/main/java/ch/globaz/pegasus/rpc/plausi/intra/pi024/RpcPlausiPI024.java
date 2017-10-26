package ch.globaz.pegasus.rpc.plausi.intra.pi024;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnoncePerson;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

public class RpcPlausiPI024 implements RpcPlausiMetier<RpcPlausiPI024Data> {

    @Override
    public RpcPlausiPI024Data buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        final RpcPlausiPI024Data dataPlausi = new RpcPlausiPI024Data(this);
        dataPlausi.idPca = decision.getPcaDecisionId();
        dataPlausi.FC33 = decision.getVitalNeeds();
        dataPlausi.personsElement = new ArrayList<Boolean>();
        for (AnnoncePerson person : decision.getPersons()) {
            dataPlausi.personsElement.add(person.getHousingMode().isDomicile());
        }
        return dataPlausi;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTRA;
    }

    @Override
    public String getID() {
        return "PI-024";
    }

    @Override
    public String getReferance() {
        return "2222.0";
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
