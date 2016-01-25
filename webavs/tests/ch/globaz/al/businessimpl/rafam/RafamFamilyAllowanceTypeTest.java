package ch.globaz.al.businessimpl.rafam;

import globaz.jade.exception.JadeApplicationException;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;

@RunWith(Parameterized.class)
public class RafamFamilyAllowanceTypeTest {

    @Parameters
    public static List<Object[]> getParametres() {
        return Arrays.asList(new Object[][] { { "01", "al.enum.rafam.familyAllowanceType.code01" },
                { "02", "al.enum.rafam.familyAllowanceType.code02" },
                { "03", "al.enum.rafam.familyAllowanceType.code03" },
                { "04", "al.enum.rafam.familyAllowanceType.code04" },
                { "10", "al.enum.rafam.familyAllowanceType.code10" },
                { "11", "al.enum.rafam.familyAllowanceType.code11" },
                { "12", "al.enum.rafam.familyAllowanceType.code12" },
                { "13", "al.enum.rafam.familyAllowanceType.code13" },
                { "20", "al.enum.rafam.familyAllowanceType.code20" },
                { "21", "al.enum.rafam.familyAllowanceType.code21" },
                { "22", "al.enum.rafam.familyAllowanceType.code22" },
                { "23", "al.enum.rafam.familyAllowanceType.code23" },
                { "30", "al.enum.rafam.familyAllowanceType.code30" },
                { "31", "al.enum.rafam.familyAllowanceType.code31" },
                { "32", "al.enum.rafam.familyAllowanceType.code32" }

        });
    }

    private String pCodeCentrale;
    private String pCodeLibelle;

    public RafamFamilyAllowanceTypeTest(String pCodeCentrale, String pCodeLibelle) {
        this.pCodeCentrale = pCodeCentrale;
        this.pCodeLibelle = pCodeLibelle;
    }

    @Test
    public void testGetCodeCentrale() {

        RafamFamilyAllowanceType code = null;

        try {
            code = RafamFamilyAllowanceType.getFamilyAllowanceType(pCodeCentrale);
            Assert.assertEquals(pCodeCentrale, code.getCodeCentrale());
        } catch (NumberFormatException e1) {
            e1.printStackTrace();
            Assert.fail(e1.toString());
        } catch (JadeApplicationException e1) {
            Assert.assertNotNull(code);
        }
    }

    @Test
    public void testGetCodeLibelle() {
        RafamFamilyAllowanceType code = null;

        try {
            code = RafamFamilyAllowanceType.getFamilyAllowanceType(pCodeCentrale);
            Assert.assertEquals(pCodeLibelle, code.getCodeLibelle());
        } catch (NumberFormatException e1) {
            e1.printStackTrace();
            Assert.fail(e1.toString());
        } catch (JadeApplicationException e1) {
            Assert.assertNotNull(code);
        }
    }

}
