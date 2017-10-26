package ch.globaz.pegasus.rpc.plausi.intra.pi002;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.plausi.common.RpcPlausiCommonCalculData;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;

public class RpcPlausiPI002Data extends RpcPlausiCommonCalculData {

    Montant X1;
    Montant tolerance;
    Montant FC7;
    int FC9;

    public RpcPlausiPI002Data(RpcPlausi<RpcPlausiCommonCalculData> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        if (FC9 != 0) {
            return true;
        } else {
            resolveX1();
            diff = FC7.substract(X1).abs();
            return diff.less(tolerance);
        }
    }

    private void resolveX1() {
        sumDepense = depense.sum();
        sumRevenu = revenu.sum();
        montantCalcule = sumDepense.substract(sumRevenu);
        X1 = montantCalcule.less(0) ? new Montant(0) : montantCalcule;
    }
}
