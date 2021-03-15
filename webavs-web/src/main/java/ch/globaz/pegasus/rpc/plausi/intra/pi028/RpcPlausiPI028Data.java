package ch.globaz.pegasus.rpc.plausi.intra.pi028;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.plausi.common.RpcPlausiCommonCalculData;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausi;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;

public class RpcPlausiPI028Data extends RpcPlausiCommonCalculData {

    Montant Par1;
    Montant Par2;
    Montant FC17;
    Montant FC11;
    Boolean isCoupleSepare;
    Boolean hasAPI = false; // FIXME

    public RpcPlausiPI028Data(RpcPlausi<RpcPlausiCommonCalculData> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        Boolean isOk = true;
        if (!isReforme && FC11.greater(new Montant(0))) {
            if (isCoupleSepare) {
                isOk = FC17.greaterOrEquals(Par2.divide(2));
            } else if (hasAPI) {
                isOk = FC17.greaterOrEquals(Par2);
            } else {
                isOk = FC17.greaterOrEquals(Par1);
            }
        }
        return isOk;
    }
    
//    public void resolveHasAPI(List<MembreFamilleWithDonneesFinanciere> membresFamille) {
//        hasAPI = false;
//        for (MembreFamilleWithDonneesFinanciere membre : membresFamille) {
//            if (!membre.getDonneesFinancieres().getApisAvsAi().isEmpty()) {
//                hasAPI = true;
//                break;
//            }
//        }
//    }
}
