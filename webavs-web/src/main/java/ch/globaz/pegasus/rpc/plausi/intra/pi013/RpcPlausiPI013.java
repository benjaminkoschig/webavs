package ch.globaz.pegasus.rpc.plausi.intra.pi013;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.rpc.domaine.PersonElementsCalcul;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnoncePerson;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

public class RpcPlausiPI013 implements RpcPlausiMetier<RpcPlausiPI013Data> {

    @Override
    public RpcPlausiPI013Data buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        final RpcPlausiPI013Data dataPlausi = new RpcPlausiPI013Data(this);

        dataPlausi.idPca = decision.getPcaDecisionId();

        // TODO : DCLTODO - Est-ce juste comme j ai fait?
        dataPlausi.loadDatas(new ArrayList<PersonElementsCalcul>());
        List<PersonElementsCalcul> personElements = new ArrayList<PersonElementsCalcul>();
        for (AnnonceDecision decisionCase : data.getDecisions()) {
            if (decisionCase.getPersons() != null) {
                for (AnnoncePerson personCase : decisionCase.getPersons()) {
                    personElements.addAll(personCase.getPersonsElementsCalcul());
                }
            }
        }
        dataPlausi.loadDatas(personElements);

        return dataPlausi;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTRA;
    }

    @Override
    public String getID() {
        return "PI-013";
    }

    @Override
    public String getReferance() {
        return "2201.3";
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
