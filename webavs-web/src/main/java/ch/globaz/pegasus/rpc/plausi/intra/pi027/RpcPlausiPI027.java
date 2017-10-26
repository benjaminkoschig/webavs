package ch.globaz.pegasus.rpc.plausi.intra.pi027;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.domaine.RpcVitalNeedsCategory;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnoncePerson;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiApplyToDecision;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;
import ch.globaz.pegasus.rpc.plausi.intra.pi027.RpcPlausiPI027Data.ParSituationCouple;

public class RpcPlausiPI027 implements RpcPlausiMetier<RpcPlausiPI027Data> {

    private Montant par1;
    private Montant par2;
    private Montant par3;

    public RpcPlausiPI027(Montant par1, Montant par2, Montant par3) {
        this.par1 = par1;
        this.par2 = par2;
        this.par3 = par3;
    }

    @Override
    public RpcPlausiPI027Data buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        final RpcPlausiPI027Data dataPlausi = new RpcPlausiPI027Data(this);
        dataPlausi.par1 = par1;
        dataPlausi.par2 = par2;
        dataPlausi.par3 = par3;
        dataPlausi.idPca = decision.getDecisionId();
        dataPlausi.FC16 = decision.getWealthDeductible();
        dataPlausi.nbTotalEnfants = decision.getChildren();
        for (AnnoncePerson person : decision.getPersons()) {
            resolvePar(person.getVitalNeedsCategory(), dataPlausi);
        }
        return dataPlausi;
    }

    private void resolvePar(RpcVitalNeedsCategory category, RpcPlausiPI027Data dataPlausi) {

        if (RpcVitalNeedsCategory.NO_NEEDS.equals(category)) {
            dataPlausi.par = ParSituationCouple.PAR1;
        } else if (RpcVitalNeedsCategory.ALONE.equals(category)) {
            dataPlausi.par = ParSituationCouple.PAR1;
        } else if (RpcVitalNeedsCategory.COUPLE.equals(category)) {
            dataPlausi.par = ParSituationCouple.PAR2;
        }

    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTRA;
    }

    @Override
    public String getID() {
        return "PI-027";
    }

    @Override
    public String getReferance() {
        return "2235.4, 3235.0, 3235.1";
    }

    @Override
    public RpcPlausiCategory getCategory() {
        return RpcPlausiCategory.WARNING;
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
