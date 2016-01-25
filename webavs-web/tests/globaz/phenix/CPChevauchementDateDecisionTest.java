package globaz.phenix;

import globaz.corvus.application.REApplication;
import globaz.globall.db.BSession;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.toolbox.CPDataDecision;
import globaz.utils.SessionForTestBuilder;
import java.math.BigDecimal;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class CPChevauchementDateDecisionTest {

    private CPDataDecision dataDecisionBase;
    private CPDataDecision dataDecisionComparaison;
    private BSession session;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Ignore
    @Test
    public void DataDecisionChevauchementDateDebutDansPeriodeDateFinHorsPeriodeComplementaire() throws Exception {
        dataDecisionComparaison = new CPDataDecision(new BigDecimal("1000"), "15.12.2013", CPDecision.CS_NON_ACTIF,
                new Boolean(true), new BigDecimal("1000"), "30.06.2013", "31.12.2013");
        Assert.assertTrue(dataDecisionBase.checkChevauchementDateDecision(session, dataDecisionComparaison));
    }

    @Ignore
    @Test
    public void DataDecisionChevauchementDateDebutDansPeriodeDateFinHorsPeriodeNonComplementaire() throws Exception {
        dataDecisionComparaison = new CPDataDecision(new BigDecimal("1000"), "15.12.2013", CPDecision.CS_NON_ACTIF,
                new Boolean(false), new BigDecimal("1000"), "30.06.2013", "31.12.2013");
        Assert.assertTrue(dataDecisionBase.checkChevauchementDateDecision(session, dataDecisionComparaison));
    }

    @Ignore
    @Test
    public void DataDecisionChevauchementDateDebutEtDateFinIdentiqueADatePeriodeComplementaire() throws Exception {
        dataDecisionComparaison = new CPDataDecision(new BigDecimal("1000"), "15.12.2013", CPDecision.CS_NON_ACTIF,
                new Boolean(true), new BigDecimal("1000"), "01.01.2013", "30.06.2013");
        Assert.assertTrue(dataDecisionBase.checkChevauchementDateDecision(session, dataDecisionComparaison));
    }

    @Ignore
    @Test
    public void DataDecisionChevauchementDateDebutEtDateFinIdentiqueADatePeriodeNonComplementaire() throws Exception {
        dataDecisionComparaison = new CPDataDecision(new BigDecimal("1000"), "15.12.2013", CPDecision.CS_NON_ACTIF,
                new Boolean(false), new BigDecimal("1000"), "01.01.2013", "30.06.2013");
        Assert.assertTrue(dataDecisionBase.checkChevauchementDateDecision(session, dataDecisionComparaison));
    }

    @Ignore
    @Test
    public void DataDecisionChevauchementDateDebutHorsPeriodeDateFinDansPeriodeComplementaire() throws Exception {
        dataDecisionComparaison = new CPDataDecision(new BigDecimal("1000"), "15.12.2013", CPDecision.CS_NON_ACTIF,
                new Boolean(true), new BigDecimal("1000"), "01.01.2012", "15.06.2013");
        Assert.assertTrue(dataDecisionBase.checkChevauchementDateDecision(session, dataDecisionComparaison));
    }

    @Ignore
    @Test
    public void DataDecisionChevauchementDateDebutHorsPeriodeDateFinDansPeriodeNonComplementaire() throws Exception {
        dataDecisionComparaison = new CPDataDecision(new BigDecimal("1000"), "15.12.2013", CPDecision.CS_NON_ACTIF,
                new Boolean(false), new BigDecimal("1000"), "01.01.2012", "15.06.2013");
        Assert.assertTrue(dataDecisionBase.checkChevauchementDateDecision(session, dataDecisionComparaison));
    }

    @Ignore
    @Test
    public void DataDecisionChevauchementDateDebutHorsPeriodeDateFinVideNonComplementaire() throws Exception {
        dataDecisionComparaison = new CPDataDecision(new BigDecimal("1000"), "15.12.2013", CPDecision.CS_NON_ACTIF,
                new Boolean(false), new BigDecimal("1000"), "01.01.2012", "");

        try {
            dataDecisionBase.checkChevauchementDateDecision(session, dataDecisionComparaison);
            Assert.assertFalse("Les dates des décisions sont correctes, alors qu'au moins une devrait être vide", true);
        } catch (Exception e) {
            Assert.assertTrue("Les dates des décisions ne sont pas correctes".equals(e.getMessage()));
        }

    }

    @Ignore
    @Test
    public void DataDecisionChevauchementDateDebutVideDateFinHorsPeriodeNonComplementaire() throws Exception {
        dataDecisionComparaison = new CPDataDecision(new BigDecimal("1000"), "15.12.2013", CPDecision.CS_NON_ACTIF,
                new Boolean(false), new BigDecimal("1000"), "", "12.12.2015");

        try {
            dataDecisionBase.checkChevauchementDateDecision(session, dataDecisionComparaison);
            Assert.assertFalse("Les dates des décisions sont correctes, alors qu'au moins une devrait être vide", true);
        } catch (Exception e) {
            Assert.assertTrue("Les dates des décisions ne sont pas correctes".equals(e.getMessage()));
        }

    }

    // @Before
    public void setUp() throws Exception {
        session = SessionForTestBuilder.getSession(REApplication.DEFAULT_APPLICATION_CORVUS, "ccjuglo", "glob4az");
        if (session == null) {
            throw new NullPointerException("Session null");
        }
        dataDecisionBase = new CPDataDecision(new BigDecimal("1000"), "15.12.2013", CPDecision.CS_NON_ACTIF,
                new Boolean(false), new BigDecimal("1000"), "01.01.2013", "30.06.2013");
    }

    @After
    public void tearDown() throws Exception {
    }

}
