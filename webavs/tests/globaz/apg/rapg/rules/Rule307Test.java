package globaz.apg.rapg.rules;

import globaz.apg.pojo.APChampsAnnonce;
import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;

public class Rule307Test {

    @Test
    public void testCheck() throws Exception {
        Rule307 rule = new Rule307("307");
        APChampsAnnonce champsAnnonce = new APChampsAnnonce();
        champsAnnonce.setServiceType("19");
        champsAnnonce.setNumberOfDays("60");
        champsAnnonce.setBasicDailyAmount("264");
        champsAnnonce.setAllowanceCareExpenses("1");
        champsAnnonce.setTotalAPG("1");

        Assert.assertTrue(rule.check(champsAnnonce, new BigDecimal("67")));
    }

}
