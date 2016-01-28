package ch.globaz.pegasus.businessimpl.tests.pcAccordee;

import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import ch.globaz.pegasus.business.models.pcaccordee.PCAWithCalculMembreFamilleAndPrestationSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.vo.donneeFinanciere.RenteAvsAiVO;
import ch.globaz.pegasus.business.vo.pcaccordee.PCAAccordeePlanClaculeAndMembreFamilleVO;

public class PcAccordeeTestCase {
    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.tests.util.BaseTestCase#tearDown()
     * //
     */
    // @Override
    // public void tearDown() {
    // // genere erreur pour provoquer un rollback de la db
    // try {
    // JadeThread.rollbackSession();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // super.tearDown();
    // }

    public final void test_PCAWithCalculMembreFamilleAndPrestationSearch() throws Exception {
        PCAWithCalculMembreFamilleAndPrestationSearch search = new PCAWithCalculMembreFamilleAndPrestationSearch();
        search.setForIdTiersMembreFamille("1667529");
        search = PegasusServiceLocator.getPCAccordeeService().search(search);
    }

    public final void testSearch_PCAWithCalculMembreFamilleAndPrestationSearchVO() throws Exception {
        List<PCAAccordeePlanClaculeAndMembreFamilleVO> list = new ArrayList<PCAAccordeePlanClaculeAndMembreFamilleVO>();
        list = PegasusServiceLocator.getPCAccordeeService()
                .searchPCAccordeeWithCalculeRetenuVO("1667535", "02.01.2011");
        Assert.assertEquals(list.size(), 1);
        // 20100101

        List<PCAAccordeePlanClaculeAndMembreFamilleVO> list2 = new ArrayList<PCAAccordeePlanClaculeAndMembreFamilleVO>();
        list2 = PegasusServiceLocator.getPCAccordeeService().searchPCAccordeeWithCalculeRetenuVO("1000022",
                "01.01.2010");
        Assert.assertEquals(list2.size(), 1);

        // Assert.assertEquals(list.get(0).getListMembreFamilleVO().size(), 6);
    }

    public final void testSearchRenteAvsAIByIdPcAccordee() throws Exception {
        List<RenteAvsAiVO> list = PegasusServiceLocator.getRenteIjApiService().searchRenteAvsAiByIdPCAccordee("1221",
                "02.01.2011");
        Assert.assertEquals(list.size(), 5);

    }
}
