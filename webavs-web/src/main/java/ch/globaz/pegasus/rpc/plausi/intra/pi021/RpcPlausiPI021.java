package ch.globaz.pegasus.rpc.plausi.intra.pi021;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnoncePerson;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

public class RpcPlausiPI021 implements RpcPlausiMetier<RpcPlausiPI021Data> {

    @Override
    public RpcPlausiPI021Data buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        final RpcPlausiPI021Data dataPlausi = new RpcPlausiPI021Data(this);
        dataPlausi.idPca = decision.getPcaDecisionId();
        dataPlausi.personsHousingMode = new HashMap<>();
        dataPlausi.personsResidencePatientExpenses = new HashMap<>();
        for (AnnoncePerson person : decision.getPersons()) {
            Long vn = person.getVn();
            dataPlausi.personsHousingMode.put(vn, person.getHousingMode().isDomicile());
            dataPlausi.personsResidencePatientExpenses.put(vn, person.getResidencePatientExpenses());
        }

        return dataPlausi;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTRA;
    }

    @Override
    public String getID() {
        return "PI-021";
    }

    @Override
    public String getReferance() {
        return "2216.0";
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
