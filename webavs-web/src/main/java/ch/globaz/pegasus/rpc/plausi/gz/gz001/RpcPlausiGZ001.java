package ch.globaz.pegasus.rpc.plausi.gz.gz001;

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

public class RpcPlausiGZ001 implements RpcPlausiMetier<RpcPlausiGZ001Data> {

    @Override
    public RpcPlausiGZ001Data buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        final RpcPlausiGZ001Data plausiData = new RpcPlausiGZ001Data(this);

        plausiData.dateDecision = decision.getDecisionDate();

        plausiData.FC6 = decision.getValidTo();

        // TODO : DCLTODO - Est-juste comme j'ai fait ?
        List<PersonElementsCalcul> personElements = new ArrayList<PersonElementsCalcul>();
        for (AnnonceDecision decisionCase : data.getDecisions()) {
            if (decisionCase.getPersons() != null) {
                for (AnnoncePerson personCase : decisionCase.getPersons()) {
                    personElements.addAll(personCase.getPersonsElementsCalcul());
                }
            }
        }
        plausiData.loadDatas(personElements);

        return plausiData;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTERNAL;
    }

    @Override
    public String getID() {
        return "GZ-001";
    }

    @Override
    public String getReferance() {
        return "PR-011";
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
                add(RpcPlausiApplyToDecision.REJECT_SMALL);
            }
        };
    }
}
