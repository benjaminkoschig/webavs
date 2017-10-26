package ch.globaz.pegasus.rpc.plausi.intra.pi025;

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
import ch.globaz.pegasus.rpc.plausi.intra.pi025.RpcPlausiPI025Data.ParSituationCouple;

public class RpcPlausiPI025 implements RpcPlausiMetier<RpcPlausiPI025Data> {

    private Montant par1;
    private Montant par2;
    private Montant par3;
    private Montant par4;
    private Montant par5;
    private Montant par6;

    public RpcPlausiPI025(Montant par1, Montant par2, Montant par3, Montant par4, Montant par5, Montant par6) {
        this.par1 = par1;
        this.par2 = par2;
        this.par3 = par3;
        this.par4 = par4;
        this.par5 = par5;
        this.par6 = par6;
    }

    @Override
    public RpcPlausiPI025Data buildPlausi(AnnonceDecision decision, AnnonceCase data) {
        final RpcPlausiPI025Data dataPlausi = new RpcPlausiPI025Data(this);
        dataPlausi.par1 = par1;
        dataPlausi.par2 = par2;
        dataPlausi.par3 = par3;
        dataPlausi.par4 = par4;
        dataPlausi.par5 = par5;
        dataPlausi.par6 = par6;
        dataPlausi.idPca = decision.getPcaDecisionId();
        dataPlausi.FC33 = decision.getVitalNeeds();
        dataPlausi.isDomicile = decision.getPersons().get(0).getHousingMode().isDomicile();
        dataPlausi.nbTotalEnfants = decision.getChildren();
        for (AnnoncePerson person : decision.getPersons()) {
            resolvePar(person.getVitalNeedsCategory(), dataPlausi);
        }
        return dataPlausi;
    }

    private void resolvePar(RpcVitalNeedsCategory category, RpcPlausiPI025Data dataPlausi) {

        if (category.isNoNeed()) {
            if (ParSituationCouple.PAR1.equals(dataPlausi.par)) {
                dataPlausi.par = ParSituationCouple.PAR3;
            } else {
                dataPlausi.par = ParSituationCouple.PAR0;
            }
        } else if (category.isAlone()) {
            if (ParSituationCouple.PAR0.equals(dataPlausi.par)) {
                dataPlausi.par = ParSituationCouple.PAR3;
            } else {
                dataPlausi.par = ParSituationCouple.PAR1;
            }
        } else if (category.isCouple()) {
            dataPlausi.par = ParSituationCouple.PAR2;
        } else if (category.isChild() && dataPlausi.par == null) {
            dataPlausi.par = ParSituationCouple.PAR4;
        }

    }

    @Override
    public RpcPlausiType getType() {
        return RpcPlausiType.INTRA;
    }

    @Override
    public String getID() {
        return "PI-025";
    }

    @Override
    public String getReferance() {
        return "2223.0";
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
