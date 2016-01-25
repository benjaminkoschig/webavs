package ch.globaz.al.businessimpl.services.rafam;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALTestCaseJU4;

public class AnnoncesRafamSearchServiceTest extends ALTestCaseJU4 {

    @Ignore
    @Test
    public void testLoadAnnoncesToSend() {
        try {
            ALImplServiceLocator.getAnnoncesRafamSearchService().loadAnnoncesToSend();

            Assert.assertEquals(1, 1);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.toString());
        } finally {
            doFinally();
        }
    }

}
