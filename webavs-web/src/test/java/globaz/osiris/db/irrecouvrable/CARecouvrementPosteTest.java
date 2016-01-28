package globaz.osiris.db.irrecouvrable;

import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;

public class CARecouvrementPosteTest {
    @Test
    public void checkCalculSoldeDisponibleCorrectTest() {
        CARecouvrementPoste recouvrementPoste = new CARecouvrementPoste(2012, "11", "12", "13", "2110.3300.0000",
                "2110.4060.0000", "Amortissement Cot. pers.", new BigDecimal("1000"), new BigDecimal("1"),
                new BigDecimal("500"), new BigDecimal("2"), new BigDecimal("100"), "1", new BigDecimal("200"),
                new BigDecimal("300"), new BigDecimal("300"), CATypeDeRecouvrementPoste.getEnumFromCodeSystem("239003"));

        recouvrementPoste.getSoldeDisponible();
        Assert.assertTrue(recouvrementPoste.getSoldeDisponible().compareTo(new BigDecimal("500")) == 0);
        System.out.println(recouvrementPoste.toString());
    }

}
