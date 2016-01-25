package ch.globaz.pegasus.businessimpl.services.models.mutation;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;

public class AlloctionNoelGrouped {
    private Map<String, SimpleAllocationNoel> allocation = new HashMap<String, SimpleAllocationNoel>();

    public AlloctionNoelGrouped(Map<String, SimpleAllocationNoel> allocation) {
        this.allocation = allocation;
    }

    public String getMontantRetoForIdDemande(String idDemande, boolean isCoupleSepareParLaMaladie) {
        return resolveMontant(allocation, idDemande, isCoupleSepareParLaMaladie);
    }

    public Map<String, SimpleAllocationNoel> getRetro() {
        return allocation;
    }

    private String resolveMontant(Map<String, SimpleAllocationNoel> map, String idDemande,
            boolean isCoupleSepareParLaMaladie) {
        if (map.containsKey(idDemande)) {
            String montant = map.get(idDemande).getMontantAllocation();
            if (isCoupleSepareParLaMaladie) {
                montant = new BigDecimal(montant).divide(new BigDecimal(2)).toString();
            }
            return montant;
        }
        return null;
    }

    @Override
    public String toString() {
        return "AlloctionNoelGrouped [allocation=" + allocation + "]";
    }
}
