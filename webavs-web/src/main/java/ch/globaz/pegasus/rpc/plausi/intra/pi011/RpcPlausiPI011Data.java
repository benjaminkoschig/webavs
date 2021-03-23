package ch.globaz.pegasus.rpc.plausi.intra.pi011;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.plausi.common.RpcPlausiCommonCalculData;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;

class RpcPlausiPI011Data extends RpcPlausiHeader {
    String idPca;
    Montant FC18;
    Montant X3;
    Montant FC11;
    Montant FC17;
    Montant FC10;
    Montant FC12;
    Montant FC13;
    Montant FC14;
    Montant FC15;
    Montant FC16;
    Montant tolerance;
    Boolean isReforme;

    public RpcPlausiPI011Data(RpcPlausi<RpcPlausiPI011Data> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        if(isReforme) {
            return true;
        }
        resolveX3();
        return FC18.substract(X3).abs().less(tolerance);
    }

    private void resolveX3() {
        Montant minZero = FC11.substract(FC17);
        if (minZero.isNegative()) {
            minZero = Montant.ZERO;
        }
        minZero = minZero.add(FC10).add(FC12).add(FC13).substract(FC14).substract(FC15).substract(FC16);
        if (minZero.isNegative()) {
            minZero = Montant.ZERO;
        }
        X3 = minZero;
    }

}
