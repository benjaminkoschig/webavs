package globaz.apg.rapg.rules;

import globaz.apg.pojo.APChampsAnnonce;
import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;

public class Rule307Test {

    @Test
    public void testCheckService11() throws Exception {
        // service type 11, 13, 21, 41

        Rule307 rule = new Rule307("307");
        APChampsAnnonce champsAnnonce = new APChampsAnnonce();
        champsAnnonce.setServiceType("11");
        champsAnnonce.setNumberOfDays("10");
        champsAnnonce.setBasicDailyAmount("100");
        champsAnnonce.setAllowanceCareExpenses("0");

        // ---- DailyIndemnityGuaranteeAI = true (ne doit pas influencer ce test)
        // AllowanceFarm = false
        champsAnnonce.setAllowanceFarm(false);
        champsAnnonce.setDailyIndemnityGuaranteeAI(true);
        champsAnnonce.setTotalAPG("1000");
        Assert.assertTrue(rule.check(champsAnnonce, new BigDecimal("67")));
        champsAnnonce.setTotalAPG("999");
        Assert.assertFalse(rule.check(champsAnnonce, new BigDecimal("67")));
        champsAnnonce.setTotalAPG("1001");
        Assert.assertFalse(rule.check(champsAnnonce, new BigDecimal("67")));

        // AllowanceFarm = true
        champsAnnonce.setAllowanceFarm(true);
        champsAnnonce.setTotalAPG("1670");
        Assert.assertTrue(rule.check(champsAnnonce, new BigDecimal("67")));
        champsAnnonce.setTotalAPG("1669");
        Assert.assertTrue(rule.check(champsAnnonce, new BigDecimal("67")));
        champsAnnonce.setTotalAPG("1671");
        Assert.assertTrue(rule.check(champsAnnonce, new BigDecimal("67")));

        // ---- DailyIndemnityGuaranteeAI = false (ne doit pas influencer ce test)
        // AllowanceFarm = false
        champsAnnonce.setAllowanceFarm(false);
        champsAnnonce.setDailyIndemnityGuaranteeAI(false);
        champsAnnonce.setTotalAPG("1000");
        Assert.assertTrue(rule.check(champsAnnonce, new BigDecimal("67")));
        champsAnnonce.setTotalAPG("999");
        Assert.assertFalse(rule.check(champsAnnonce, new BigDecimal("67")));
        champsAnnonce.setTotalAPG("1001");
        Assert.assertFalse(rule.check(champsAnnonce, new BigDecimal("67")));

        // AllowanceFarm = true
        champsAnnonce.setAllowanceFarm(true);
        champsAnnonce.setTotalAPG("1670");
        Assert.assertTrue(rule.check(champsAnnonce, new BigDecimal("67")));
        champsAnnonce.setTotalAPG("1669");
        Assert.assertTrue(rule.check(champsAnnonce, new BigDecimal("67")));
        champsAnnonce.setTotalAPG("1671");
        Assert.assertTrue(rule.check(champsAnnonce, new BigDecimal("67")));
    }

    @Test
    public void testCheckServiceDifferent11() throws Exception {
        // service type différent de 11, 13, 21, 41

        Rule307 rule = new Rule307("307");
        APChampsAnnonce champsAnnonce = new APChampsAnnonce();
        champsAnnonce.setServiceType("10");
        champsAnnonce.setNumberOfDays("10");
        champsAnnonce.setBasicDailyAmount("100");
        champsAnnonce.setAllowanceCareExpenses("0");

        // ----- DailyIndemnityGuaranteeAI = false
        // AllowanceFarm = false
        champsAnnonce.setDailyIndemnityGuaranteeAI(false);
        champsAnnonce.setAllowanceFarm(false);
        champsAnnonce.setTotalAPG("1000");
        Assert.assertTrue(rule.check(champsAnnonce, new BigDecimal("67")));
        champsAnnonce.setTotalAPG("999");
        Assert.assertFalse(rule.check(champsAnnonce, new BigDecimal("67")));
        champsAnnonce.setTotalAPG("1001");
        Assert.assertFalse(rule.check(champsAnnonce, new BigDecimal("67")));

        // AllowanceFarm = true
        champsAnnonce.setAllowanceFarm(true);
        champsAnnonce.setTotalAPG("1670");
        Assert.assertTrue(rule.check(champsAnnonce, new BigDecimal("67")));
        champsAnnonce.setTotalAPG("1669");
        Assert.assertTrue(rule.check(champsAnnonce, new BigDecimal("67")));
        champsAnnonce.setTotalAPG("1671");
        Assert.assertTrue(rule.check(champsAnnonce, new BigDecimal("67")));

        // ----- DailyIndemnityGuaranteeAI = true
        // AllowanceFarm = false
        champsAnnonce.setDailyIndemnityGuaranteeAI(true);
        champsAnnonce.setAllowanceFarm(false);
        champsAnnonce.setTotalAPG("1000");
        Assert.assertTrue(rule.check(champsAnnonce, new BigDecimal("67")));
        champsAnnonce.setTotalAPG("999");
        Assert.assertTrue(rule.check(champsAnnonce, new BigDecimal("67")));
        champsAnnonce.setTotalAPG("1001");
        Assert.assertTrue(rule.check(champsAnnonce, new BigDecimal("67")));

        // AllowanceFarm = true
        champsAnnonce.setAllowanceFarm(true);
        champsAnnonce.setTotalAPG("1670");
        Assert.assertTrue(rule.check(champsAnnonce, new BigDecimal("67")));
        champsAnnonce.setTotalAPG("1669");
        Assert.assertTrue(rule.check(champsAnnonce, new BigDecimal("67")));
        champsAnnonce.setTotalAPG("1671");
        Assert.assertTrue(rule.check(champsAnnonce, new BigDecimal("67")));
    }

}
