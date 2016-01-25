package ch.globaz.pegasus.businessimpl.services.models.mutation;

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;

public class AlloctionNoelGroupedTestCase {
    private AlloctionNoelGrouped createMapAllocationNoel() {
        Map<String, SimpleAllocationNoel> map = new HashMap<String, SimpleAllocationNoel>();
        for (int i = 0; i < 5; i++) {
            SimpleAllocationNoel simpleAllocationNoel = new SimpleAllocationNoel();
            simpleAllocationNoel.setIdDemande(String.valueOf((i * 2) + 100));
            simpleAllocationNoel.setMontantAllocation("100");
            simpleAllocationNoel.setIdPrestationAccordee(String.valueOf(i + 100));
            map.put(String.valueOf(i), simpleAllocationNoel);
        }
        return new AlloctionNoelGrouped(map);
    }

    @Test
    public void giveAllocactionWihtCoupleSepareeShoulBeReturn50() throws Exception {
        AlloctionNoelGrouped alloctionNoelGrouped = createMapAllocationNoel();
        Assert.assertEquals("50", alloctionNoelGrouped.getMontantRetoForIdDemande("1", true));
    }

    @Test
    public void giveAllocactionWihtOutCoupleSepareeShoulBeReturn100() throws Exception {
        AlloctionNoelGrouped alloctionNoelGrouped = createMapAllocationNoel();
        Assert.assertEquals("100", alloctionNoelGrouped.getMontantRetoForIdDemande("1", false));
    }

    @Test
    public void testGroupByIdDemandeAndSplitRetroAndPresation() throws Exception {
        AlloctionNoelGrouped alloctionNoelGrouped = createMapAllocationNoel();
        Assert.assertEquals(5, alloctionNoelGrouped.getRetro().size());
    }

}
