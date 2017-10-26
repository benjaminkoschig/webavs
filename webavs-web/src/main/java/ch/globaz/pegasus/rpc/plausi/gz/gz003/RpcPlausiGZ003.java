package ch.globaz.pegasus.rpc.plausi.gz.gz003;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.naos.ree.tools.InfoCaisse;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;
import ch.globaz.pegasus.utils.RpcUtil;

public class RpcPlausiGZ003 implements RpcPlausiMetier<RpcPlausiGZ003Data> {

    @Override
    public RpcPlausiGZ003Data buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        final RpcPlausiGZ003Data dataPlausi = new RpcPlausiGZ003Data(this);

        dataPlausi.idPca = decision.getPcaDecisionId();

        final InfoCaisse infoCaisse = RpcUtil.createInfosCaisse();

        dataPlausi.FC35 = infoCaisse.getNumeroCaisse();
        dataPlausi.FC37 = infoCaisse.getNumeroAgence();

        return dataPlausi;
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTERNAL;
    }

    @Override
    public String getID() {
        return "GZ-003";
    }

    @Override
    public String getReferance() {
        return "PR-030";
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
                add(RpcPlausiApplyToDecision.CANCEL);
                add(RpcPlausiApplyToDecision.REJECT_SMALL);
            }
        };
    }
}
