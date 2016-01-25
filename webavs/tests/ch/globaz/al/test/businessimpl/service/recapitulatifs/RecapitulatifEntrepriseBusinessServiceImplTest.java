package ch.globaz.al.test.businessimpl.service.recapitulatifs;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import org.junit.Ignore;
import org.junit.Test;

// TODO : à terminer
public class RecapitulatifEntrepriseBusinessServiceImplTest {

    @Ignore
    @Test
    public void testGetRecapCompensation() {
        try {

            // RecapCompensationComplexSearchModel search = ALServiceLocator
            // .getRecapitulatifEntrepriseBusinessService()
            // .getRecapCompensation("02.2010", ALCSPrestation.ETAT_SA,
            // ALCSPrestation.BONI_INDIRECT, null);

            BigDecimal montant = new BigDecimal("0");

            // for (int i = 0; i < search.getSize(); i++) {

            // RecapCompensationComplexModel line =
            // (RecapCompensationComplexModel) (search
            // .getSearchResults()[i]);

            // montant = montant.add(new BigDecimal(line.getMontantTotal()));
            // }
            // TODO : à terminer
            // Assert.assertEquals(100, search.getSize());
            // Assert.assertEquals("10000.00", montant.toString());

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.toString());
        }
    }

}
