package ch.globaz.pegasus.businessimpl.services.models.mutation;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;
import ch.globaz.pegasus.business.services.models.pcaccordee.SimpleAllocationDeNoelService;

public class AllocationNoelForMutationTestCase {

    @Ignore
    @Test
    public void testGroupByIdDemandeAndSplitRetroAndPresation() throws Exception {

        SimpleAllocationDeNoelService service = Mockito.mock(SimpleAllocationDeNoelService.class);
        AllocationNoelForMutation allocationNoelForMutation = new AllocationNoelForMutation(service);
        List<SimpleAllocationNoel> list = new ArrayList<SimpleAllocationNoel>();

        for (int i = 0; i < 5; i++) {
            SimpleAllocationNoel simpleAllocationNoel = new SimpleAllocationNoel();
            simpleAllocationNoel.setIdDemande(String.valueOf((i * 2) + 100));
            simpleAllocationNoel.setMontantAllocation("100");
            simpleAllocationNoel.setIdPrestationAccordee(String.valueOf(i + 100));
            list.add(simpleAllocationNoel);
        }

        AlloctionNoelGrouped alloctionNoelGrouped = allocationNoelForMutation
                .groupByIdDemandeAndSplitRetroAndPresation(list);

        Assert.assertEquals(5, alloctionNoelGrouped.getRetro().size());

    }

}
