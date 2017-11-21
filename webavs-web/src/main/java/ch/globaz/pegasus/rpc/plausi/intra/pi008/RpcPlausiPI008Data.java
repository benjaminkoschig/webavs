package ch.globaz.pegasus.rpc.plausi.intra.pi008;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.plausi.common.RpcPlausiCommonCalculData;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;

class RpcPlausiPI008Data extends RpcPlausiCommonCalculData {

    Montant X2;
    Montant tolerance;
    Montant FC8;
    int FC9;

    public RpcPlausiPI008Data(RpcPlausi<RpcPlausiCommonCalculData> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        if (FC9 != 0) {
            return true;
        } else {
            resolveX2();
            diff = FC8.substract(X2).abs();
            return diff.less(tolerance);
        }
    }

    private void resolveX2() {
        sumDepense = depense.sum();
        sumRevenu = revenu.sum();
        montantCalcule = sumDepense.substract(sumRevenu);
        X2 = montantCalcule.less(depense.E24) ? depense.E24 : montantCalcule;
    }
}
