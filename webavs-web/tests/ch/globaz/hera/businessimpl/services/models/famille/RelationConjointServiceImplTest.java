package ch.globaz.hera.businessimpl.services.models.famille;

import globaz.corvus.application.REApplication;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.utils.SessionForTestBuilder;
import java.util.Date;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.hera.business.exceptions.models.RelationConjointException;
import ch.globaz.hera.business.services.HeraServiceLocator;
import ch.globaz.pegasus.tests.util.Init;

public class RelationConjointServiceImplTest {

    private static final String NSS_CAS_BARBIER = "756.3306.0397.14";
    private static final String NSS_CAS_FACTICE = "121212";
    private static final String NSS_SANS_RELATION_1 = "756.5266.3585.84";
    private static final String NSS_SANS_RELATION_2 = "756.4105.8770.71";
    private static final String NSS_TEST_FAIL = "756.0124.2717.15";

    @Test
    @Ignore
    public void getDateNaissanceTestWithCasOk() throws Exception {
        String dateNaissanceConjoint = HeraServiceLocator.getRelationConjointService().getDateNaissanceConjointForDate(
                RelationConjointServiceImplTest.NSS_CAS_BARBIER, JadeDateUtil.getGlobazFormattedDate(new Date()));
        Assert.assertEquals("23.04.1960", dateNaissanceConjoint);
    }

    // @Ignore("A executer avec jade")
    @Test(expected = RelationConjointException.class)
    public void getDateNaissanceTestWithNSSfail() throws Exception {
        String dateNaissanceConjoint = HeraServiceLocator.getRelationConjointService().getDateNaissanceConjointForDate(
                RelationConjointServiceImplTest.NSS_CAS_FACTICE, JadeDateUtil.getGlobazFormattedDate(new Date()));
        Assert.assertEquals("23.04.1960", dateNaissanceConjoint);
    }

    @Test
    @Ignore
    public void getDateNaissanceTestWithRelationFail() throws Exception {
        String dateNaissanceConjoint = HeraServiceLocator.getRelationConjointService().getDateNaissanceConjointForDate(
                RelationConjointServiceImplTest.NSS_SANS_RELATION_1, JadeDateUtil.getGlobazFormattedDate(new Date()));
        Assert.assertEquals("0", dateNaissanceConjoint);
    }

    @Test
    @Ignore
    public void getDateNaissanceTestWithTestFail() throws Exception {

        try {
            String dateNaissanceConjoint = HeraServiceLocator.getRelationConjointService()
                    .getDateNaissanceConjointForDate(RelationConjointServiceImplTest.NSS_TEST_FAIL,
                            JadeDateUtil.getGlobazFormattedDate(new Date()));
            Assert.fail();
        } catch (Exception e) {
            if (e instanceof RelationConjointException) {
                Assert.assertTrue(true);
            }
        }

    }

    // @Ignore("A executer avec jade")
    @Ignore
    @Before
    public void launchFramework() throws Exception {
        BSession session = SessionForTestBuilder.getSession(REApplication.DEFAULT_APPLICATION_CORVUS, "ccjuglo",
                "glob4az");

        Object ctx = JadeThread.currentContext();

        if (ctx == null) {

            Jade.getInstance();
            // BSession session = (BSession) GlobazServer.getCurrentSystem().getApplication("FRAMEWORK")
            // .newSession("pctest", "pctest");
            JadeThreadActivator.startUsingJdbcContext(this, Init.initContext(session).getContext());
            JadeThread.currentContext().storeTemporaryObject("bsession", session);
        }
    }

}
