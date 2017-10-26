package ch.globaz.pegasus.rpc.plausi.core;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.domaine.RpcData;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionAnnonceComplete;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.plausi.gz.gz001.RpcPlausiGZ001;
import ch.globaz.pegasus.rpc.plausi.gz.gz002.RpcPlausiGZ002;
import ch.globaz.pegasus.rpc.plausi.gz.gz003.RpcPlausiGZ003;
import ch.globaz.pegasus.rpc.plausi.gz.gz004.RpcPlausiGZ004;
import ch.globaz.pegasus.rpc.plausi.intra.pi002.RpcPlausiPI002;
import ch.globaz.pegasus.rpc.plausi.intra.pi003.RpcPlausiPI003;
import ch.globaz.pegasus.rpc.plausi.intra.pi004.RpcPlausiPI004;
import ch.globaz.pegasus.rpc.plausi.intra.pi008.RpcPlausiPI008;
import ch.globaz.pegasus.rpc.plausi.intra.pi009.RpcPlausiPI009;
import ch.globaz.pegasus.rpc.plausi.intra.pi011.RpcPlausiPI011;
import ch.globaz.pegasus.rpc.plausi.intra.pi013.RpcPlausiPI013;
import ch.globaz.pegasus.rpc.plausi.intra.pi014.RpcPlausiPI014;
import ch.globaz.pegasus.rpc.plausi.intra.pi015.RpcPlausiPI015;
import ch.globaz.pegasus.rpc.plausi.intra.pi023.RpcPlausiPI023;
import ch.globaz.pegasus.rpc.plausi.intra.pi024.RpcPlausiPI024;
import ch.globaz.pegasus.rpc.plausi.intra.pi025.RpcPlausiPI025;
import ch.globaz.pegasus.rpc.plausi.intra.pi028.RpcPlausiPI028;
import ch.globaz.pegasus.rpc.plausi.intra.pi042.RpcPlausiPI042;
import ch.globaz.pegasus.rpc.plausi.intra.pi043.RpcPlausiPI043;
import ch.globaz.pegasus.rpc.plausi.intra.pi044.RpcPlausiPI044;
import ch.globaz.pegasus.rpc.plausi.intra.pi046.RpcPlausiPI046;
import ch.globaz.pegasus.rpc.plausi.intra.pi049.RpcPlausiPI049;
import ch.globaz.pegasus.rpc.plausi.intra.pi064.RpcPlausiPI064;
import ch.globaz.pegasus.rpc.plausi.simple.ps010.RpcPlausiPS010;

public class PlausiContainer {

    private static final List<RpcPlausiMetier<? extends PlausiResult>> listMetier = new ArrayList<RpcPlausiMetier<? extends PlausiResult>>();

    static {
        listMetier.add(new RpcPlausiPI002(new Montant(300)));
        listMetier.add(new RpcPlausiPI003());
        listMetier.add(new RpcPlausiPI008(new Montant(300)));
        listMetier.add(new RpcPlausiPI009());
        listMetier.add(new RpcPlausiPI011(new Montant(300)));
        listMetier.add(new RpcPlausiPI013());
        listMetier.add(new RpcPlausiPI014());
        listMetier.add(new RpcPlausiPI015());
        listMetier.add(new RpcPlausiPI028(new Montant(112500), new Montant(300000))); // Par1 & Par2
        listMetier.add(new RpcPlausiPI042());
        listMetier.add(new RpcPlausiPI043());
        listMetier.add(new RpcPlausiPI044());
        listMetier.add(new RpcPlausiPI046(new Montant(300)));
        listMetier.add(new RpcPlausiPI049());
        listMetier.add(new RpcPlausiGZ001());
        listMetier.add(new RpcPlausiGZ002());
        listMetier.add(new RpcPlausiGZ003());
        listMetier.add(new RpcPlausiGZ004());

        listMetier.add(initPlausi064());

        listMetier.add(new RpcPlausiPI004());
        listMetier.add(new RpcPlausiPS010());
        listMetier.add(new RpcPlausiPI023());
        listMetier.add(new RpcPlausiPI024());
        listMetier.add(new RpcPlausiPI025(new Montant(19290), new Montant(28935), new Montant(19290),
                new Montant(10080), new Montant(6720), new Montant(3360)));
        // listMetier.add(new RpcPlausiPI027(new Montant(37500), new Montant(60000), new Montant(15000)));
    }

    public static PlausisResults buildPlausis(RpcData rpcData) {
        Set<RpcPlausiCategory> inCategory = EnumSet.allOf(RpcPlausiCategory.class);
        return buildPlausisInCategory(rpcData, inCategory);
    }

    public static PlausisResults buildPlausisInCategory(RpcData rpcData, Set<RpcPlausiCategory> inCategory) {

        PlausisResults plausisResults = new PlausisResults();

        for (RpcPlausiMetier<? extends PlausiResult> plausi : listMetier) {
            if (inCategory.contains(plausi.getCategory())) {
                plausisResults.addAll(buildPlausisMetier(plausi, rpcData));
            }
        }

        return plausisResults;
    }

    static List<PlausiResult> buildPlausisMetier(RpcPlausiMetier<?> plausi, RpcData rpcData) {
        List<PlausiResult> plausisRsults = new ArrayList<PlausiResult>();

        AnnonceCase annonceCase = rpcData.getAnnonce();

        for (AnnonceDecision decision : annonceCase.getDecisions()) {
            RpcPlausiApplyToDecision applyTo = resolveApplyTo(decision.getAnnonce(), rpcData.hasVersionDroit());
            if (plausi.getApplyTo().contains(applyTo)) {
                plausisRsults.add(plausi.buildPlausi(decision, annonceCase));
            }
        }

        return plausisRsults;
    }

    static RpcPlausiApplyToDecision resolveApplyTo(RpcDecisionAnnonceComplete data, boolean hasVersionDroit) {
        if (hasVersionDroit) {
            if (data.getPcaDecision().getPca().getEtatCalcul().isRefus()) {
                return RpcPlausiApplyToDecision.REJECT_FULL;
            } else {
                return RpcPlausiApplyToDecision.POSITIVE;
            }
        } else {
            return RpcPlausiApplyToDecision.REJECT_SMALL;
        }
    }

    private static RpcPlausiPI064 initPlausi064() {
        return new RpcPlausiPI064(new HashMap<Integer, Double>() {
            {
                put(994, (double) 1);
                put(0, ((double) 2 / (double) 3));
            }
        }, new HashMap<Integer, Montant>() {
            {
                put(994, Montant.ZERO);
            }
        });
    }
}
