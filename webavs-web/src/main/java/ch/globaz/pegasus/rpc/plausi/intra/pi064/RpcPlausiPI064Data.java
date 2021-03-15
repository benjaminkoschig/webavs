package ch.globaz.pegasus.rpc.plausi.intra.pi064;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.plausi.common.RpcPlausiCommonCalculData;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;

public class RpcPlausiPI064Data extends RpcPlausiCommonCalculData {
    String idPca;
    Montant FC41;
    Montant E6;
    Montant E28;
    double par1;
    Montant par2;

    public RpcPlausiPI064Data(RpcPlausi<RpcPlausiCommonCalculData> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        if(isReforme){
            return true;
        }
        Montant minZero = ((E6.add(E28).substract(par2)).arrondiAUnIntierSupperior().multiply(par1));
        if (minZero.isNegative()) {
            minZero = Montant.ZERO;
        }
        return minZero.arrondiAUnIntierSupperior().greaterOrEquals(FC41);
    }
}
