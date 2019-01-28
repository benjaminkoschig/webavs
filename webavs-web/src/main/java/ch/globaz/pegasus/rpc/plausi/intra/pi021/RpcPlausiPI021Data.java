package ch.globaz.pegasus.rpc.plausi.intra.pi021;

import java.util.List;
import java.util.Map;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiHeader;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiMetier;

class RpcPlausiPI021Data extends RpcPlausiHeader {
    String idPca;
    Map<Long, Boolean> personsHousingMode;
    Map<Long, Montant> personsResidencePatientExpenses;

    public RpcPlausiPI021Data(RpcPlausiMetier<RpcPlausiPI021Data> plausi) {
        super(plausi);
    }

    @Override
    public boolean isValide() {
        for (Long vn : personsHousingMode.keySet()) {
            boolean isDomicile = personsHousingMode.get(vn);
            Montant patientExpenses = personsResidencePatientExpenses.get(vn);
            boolean isMontantSupZero = patientExpenses != null && patientExpenses.isPositive() && !patientExpenses.isZero();
            if(isDomicile && isMontantSupZero) {
                return false;
            }
        }
        return true;
    }
}
