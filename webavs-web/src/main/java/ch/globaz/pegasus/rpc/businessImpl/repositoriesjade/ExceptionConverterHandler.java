package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.pegasus.business.models.decision.DecisionRefus;
import ch.globaz.pegasus.rpc.business.models.RPCDecionsPriseDansLeMois;
import com.google.common.base.Throwables;

public class ExceptionConverterHandler {
    private Map<Object, Exception> mapException = new HashMap<Object, Exception>();
    private Map<Object, Boolean> mapIsAc = new HashMap<Object, Boolean>();

    public void put(Object object, Exception e, Boolean hasDroit) {
        if (mapException.size() > 10) {
            throw new ch.globaz.pegasus.rpc.businessImpl.RpcTechnicalException("Too munch excptions append");
        }
        mapException.put(object, e);
        mapIsAc.put(object, hasDroit);
    }

    public String print() {
        StringWriter writer = new StringWriter();
        for (Entry<Object, Exception> entry : mapException.entrySet()) {
            String identifactionDuCas;

            if (mapIsAc.get(entry.getKey())) {
                List<RPCDecionsPriseDansLeMois> decisions = (List) entry.getKey();
                identifactionDuCas = decisions.get(0).getNssTiersBeneficiaire() + "  (idDemande: "
                        + decisions.get(0).getIdDemande() + ", idVersionDroit:"
                        + decisions.get(0).getSimpleVersionDroit().getIdVersionDroit() + ")";
            } else {
                DecisionRefus decision = (DecisionRefus) entry.getKey();

                identifactionDuCas = decision.getDecisionHeader().getPersonneEtendue().getPersonneEtendue()
                        .getNumAvsActuel()
                        + " (idDemande: " + decision.getDemande().getSimpleDemande().getIdDemande() + ")";
            }

            writer.append(" " + identifactionDuCas + "\n " + Throwables.getStackTraceAsString(entry.getValue())
                    + "\n\n");
        }
        return writer.toString();
    }

    public boolean hasErrors() {
        return !mapIsAc.isEmpty();
    }
}
