package ch.globaz.al.businessimpl.services.adi;

import globaz.framework.util.FWCurrency;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.Assert;
import org.junit.Test;
import org.safehaus.uuid.Logger;

public class CalculAdiBusinessServiceImplTest {
    @Test
    public void testFillMontantAllocEtrForOneSaisie() {

        double[] amountToDivide = { 379.20, 373.00, 300.00, 372.00, 371.90, 17.50 };
        double[] exceptedResult = { 63.20, 62.17, 50.00, 62.00, 61.99, 2.92 };

        for (int i = 0; i < amountToDivide.length; i++) {
            Integer cptPeriodeSaisie = 6;
            FWCurrency montantAdi = new FWCurrency(amountToDivide[i]);

            Double montantSaisieAlloc = (montantAdi.getBigDecimalValue()).divide(BigDecimal.valueOf(cptPeriodeSaisie),
                    2, RoundingMode.UP).doubleValue();

            Logger.logInfo("Montant attendu : " + exceptedResult[i] + " | " + montantSaisieAlloc);
            Assert.assertEquals(exceptedResult[i], montantSaisieAlloc, 0);
        }
    }
}
