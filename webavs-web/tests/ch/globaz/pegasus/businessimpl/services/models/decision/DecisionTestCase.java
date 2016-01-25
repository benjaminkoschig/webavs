package ch.globaz.pegasus.businessimpl.services.models.decision;

import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.vo.decision.DecisionPcVO;
import ch.globaz.pegasus.tests.util.BaseTestCase;

public class DecisionTestCase extends BaseTestCase {

    /**
     * Test le calcule pour trouve la fotune d'une personne lié à un décision
     * 
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DecisionException
     */
    public final void testFortune() throws JadeApplicationServiceNotAvailableException, DecisionException {
        // (Assert.assertEquals(new BigDecimal(-34500),
        // PegasusServiceLocator.getFortuneService().calculeFortune("490"));
        // 756.1002.1736.71 / Pressacco Italico / 23.04.1960 / Homme / Italie
        // Assert.assertEquals(new BigDecimal(-123569),
        // PegasusServiceLocator.getFortuneService().calculeFortune("437"));
        // 438
    }

    /**
     * Test doit retourner 2 décisions (28.4.2011)
     * 
     * @throws Exception
     */
    public final void testSearch_DecisionPcVO() throws Exception {
        List<DecisionPcVO> list = new ArrayList<DecisionPcVO>();
        ArrayList dates = new ArrayList();
        dates.add("28.04.2011");

        list = PegasusServiceLocator.getDecisionService().searchDecisionsByDateValidation(dates);
        Assert.assertEquals(list.size(), 2);
    }
}
