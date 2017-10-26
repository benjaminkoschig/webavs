package ch.globaz.pegasus.rpc.plausi.gz.gz002;

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

public class RpcPlausiGZ002 implements RpcPlausiMetier<RpcPlausiGZ002Data> {

    @Override
    public RpcPlausiGZ002Data buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        final RpcPlausiGZ002Data dataPlausi = new RpcPlausiGZ002Data(this);

        dataPlausi.idPca = decision.getPcaDecisionId();

        // TODO : DCLTODO - Est-ce juste comme j'ai fait ?
        List<PersonElementsCalcul> personElements = new ArrayList<PersonElementsCalcul>();
        for (AnnonceDecision decisionCase : data.getDecisions()) {
            for (AnnoncePerson personCase : decisionCase.getPersons()) {
                personElements.addAll(personCase.getPersonsElementsCalcul());
            }
        }
        dataPlausi.loadList(personElements);

        return dataPlausi;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTERNAL;
    }

    @Override
    public String getID() {
        return "GZ-002";
    }

    @Override
    public String getReferance() {
        return "PR-029";
    }

    @Override
    public RpcPlausiCategory getCategory() {
        return RpcPlausiCategory.DATA_INTEGRITY;
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
