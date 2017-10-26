package ch.globaz.pegasus.rpc.plausi.intra.pi064;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

public class RpcPlausiPI064 implements RpcPlausiMetier<RpcPlausiPI064Data> {
    private final Map<Integer, Double> parameters1;
    private final Map<Integer, Montant> parameters2;

    public RpcPlausiPI064(Map<Integer, Double> par1, Map<Integer, Montant> par2) {
        parameters1 = par1;
        parameters2 = par2;
    }

    @Override
    public RpcPlausiPI064Data buildPlausi(AnnonceDecision decision, AnnonceCase data) {

        final RpcPlausiPI064Data dataPlausi = new RpcPlausiPI064Data(this);
        Integer pensionKind = 0;
        dataPlausi.FC41 = decision.getIncomeConsideredTotal();
        if (data.getDecisions().size() > 1) {
            dataPlausi.E28 = Montant.ZERO_ANNUEL;
            dataPlausi.E6 = Montant.ZERO_ANNUEL;

            for (AnnonceDecision deci : data.getDecisions()) {
                if (pensionKind == 0) {
                    pensionKind = deci.getPersonRequerant().getPensionKind();
                }
                dataPlausi.E6 = dataPlausi.E6.add(deci.getSumRevenuBruteActiviteLucrative());
                dataPlausi.E28 = dataPlausi.E28.add(deci.getSumRevenuBrutHypothetique());
            }
        } else {
            pensionKind = decision.getPersonRequerant().getPensionKind();
            dataPlausi.E6 = decision.getSumRevenuBruteActiviteLucrative();
            dataPlausi.E28 = decision.getSumRevenuBrutHypothetique();
        }

        dataPlausi.par1 = resolveParameters1(pensionKind);
        dataPlausi.par2 = resolveParameters2(pensionKind, decision.getPersons().size(), data.getDecisions().size());

        return dataPlausi;
    }

    private double resolveParameters1(Integer convert) {
        if (parameters1.containsKey(convert)) {
            return parameters1.get(convert);
        } else {
            return parameters1.get(0);
        }
    }

    private Montant resolveParameters2(Integer convert, int familySize, int nbDeci) {
        if (parameters2.containsKey(convert)) {
            return parameters2.get(convert);
        } else {
            // couple séapré
            if (nbDeci > 1) {
                return new Montant(750);
            } else if (familySize > 1) {
                return new Montant(1500);
            }
            return new Montant(1000);
        }
    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTRA;
    }

    @Override
    public String getID() {
        return "PI-064";
    }

    @Override
    public String getReferance() {
        return "Mail roe vom 6.1.2017";
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
