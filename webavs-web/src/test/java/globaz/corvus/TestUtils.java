package globaz.corvus;

import globaz.prestation.enums.codeprestation.IPRCodePrestationEnum;
import globaz.prestation.enums.codeprestation.PRDomainDePrestation;
import globaz.prestation.enums.codeprestation.PRTypeCodePrestation;
import junit.framework.Assert;

/**
 * Classes avec des méthodes utilitaires pour l'éxecution des tests Corvus
 * 
 * @author lga
 */
public class TestUtils {

    /**
     * Test que les valeurs possible de deux IPRCodePrestationEnum n'aient pas deux codes identique
     * 
     * @param codePrestationEnum1
     *            Enum1
     * @param codePrestationEnum2
     *            Enum2
     */

    public static void compareTwoIPRCodePrestationEnum(IPRCodePrestationEnum[] codePrestationEnum1,
            IPRCodePrestationEnum[] codePrestationEnum2) {
        for (IPRCodePrestationEnum code1 : codePrestationEnum1) {
            for (IPRCodePrestationEnum code2 : codePrestationEnum2) {
                Assert.assertFalse(code1.equals(code2));
                Assert.assertFalse(code1.getCodePrestation() == code2.getCodePrestation());
                Assert.assertFalse(code1.getCodePrestationAsString().equals(code2.getCodePrestationAsString()));
            }
        }
    }

    public static String indent(StringBuilder sb, int indentValue) {
        if (sb == null) {
            sb = new StringBuilder();
        }
        while (sb.length() < indentValue) {
            sb.append(" ");
        }
        return sb.toString();
    }

    public static void printSpacer() {
        System.out.println("-------------------------------------------------------------------------------");
    }

    /**
     * Test les valeurs définies d'un IPRCodePrestationEnum
     * 
     * @param code
     *            Le code à tester
     * @param typeDePrest
     *            Le type de prestation attendu
     * @param codePrest
     *            Le code prestation attendu
     */
    public static void testCodes(IPRCodePrestationEnum code, PRTypeCodePrestation type, PRDomainDePrestation domain,
            int codePrest) {
        Assert.assertTrue(code.getCodePrestation() == codePrest);
        Assert.assertTrue(code.getCodePrestationAsString().equals(String.valueOf(codePrest)));
        Assert.assertTrue(code.getTypeDePrestation().equals(type));
        Assert.assertTrue(code.getDomainDePrestation().equals(domain));
    }
}
