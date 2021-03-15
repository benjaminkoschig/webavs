package ch.globaz.pegasus.rpc.plausi.intra.pi003;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.plausi.common.RpcPlausiCommonCalculData;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;

public class RpcPlausiPI003Data extends RpcPlausiCommonCalculData {

    Montant X1;
    Montant FC7;
    int FC9;

    public RpcPlausiPI003Data(RpcPlausi<RpcPlausiCommonCalculData> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        if (!isReforme && (FC9 == 1 || FC9 == 2)) {
            resolveX1();
            // La valeur FC7 > X1 => invalid
            return FC7.less(X1);
        }
        return true;
    }

    private void resolveX1() {
        sumDepense = depense.sum();
        sumRevenu = revenu.sum();
        montantCalcule = sumDepense.substract(sumRevenu);
        X1 = montantCalcule.less(0) ? new Montant(0) : montantCalcule;
    }
}
