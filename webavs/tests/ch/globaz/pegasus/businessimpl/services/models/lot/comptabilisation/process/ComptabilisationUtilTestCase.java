package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ComptabilisationUtil;

public class ComptabilisationUtilTestCase {

    @Test
    public void testSplitMontantImpaire() {
        BigDecimal[] montants = ComptabilisationUtil.splitMontant(new BigDecimal(101));
        Assert.assertEquals(new BigDecimal(51), montants[0]);
        Assert.assertEquals(new BigDecimal(50), montants[1]);
    }

    @Test
    public void testSplitMontantPaire() {
        BigDecimal[] montants = ComptabilisationUtil.splitMontant(new BigDecimal(100));
        Assert.assertEquals(new BigDecimal(50), montants[0]);
        Assert.assertEquals(new BigDecimal(50), montants[1]);
    }

}
