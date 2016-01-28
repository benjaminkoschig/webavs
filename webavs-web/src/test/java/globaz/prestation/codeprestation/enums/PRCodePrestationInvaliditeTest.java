package globaz.prestation.codeprestation.enums;

import globaz.corvus.TestUtils;
import globaz.prestation.enums.codeprestation.IPRCodePrestationEnum;
import globaz.prestation.enums.codeprestation.PRDomainDePrestation;
import globaz.prestation.enums.codeprestation.PRTypeCodePrestation;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationInvalidite;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

public class PRCodePrestationInvaliditeTest {

    private PRTypeCodePrestation type = PRTypeCodePrestation.INVALIDITE;
    private PRDomainDePrestation domain = PRDomainDePrestation.AI;

    private static final List<Integer> codePrestPrincipalInteger = new ArrayList<Integer>();
    private static final List<String> codePrestPrincipalString = new ArrayList<String>();
    private static final List<Integer> tousLesCodePrestation = new ArrayList<Integer>();
    private static final List<String> codePrestVieillesseString = new ArrayList<String>();

    static {
        PRCodePrestationInvaliditeTest.codePrestPrincipalInteger.add(50);
        PRCodePrestationInvaliditeTest.codePrestPrincipalInteger.add(52);
        PRCodePrestationInvaliditeTest.codePrestPrincipalInteger.add(70);
        PRCodePrestationInvaliditeTest.codePrestPrincipalInteger.add(72);
        for (Integer codePrestation : PRCodePrestationInvaliditeTest.codePrestPrincipalInteger) {
            PRCodePrestationInvaliditeTest.codePrestPrincipalString.add(String.valueOf(codePrestation));
        }
        PRCodePrestationInvaliditeTest.tousLesCodePrestation.add(50);
        PRCodePrestationInvaliditeTest.tousLesCodePrestation.add(52);
        PRCodePrestationInvaliditeTest.tousLesCodePrestation.add(53);
        PRCodePrestationInvaliditeTest.tousLesCodePrestation.add(54);
        PRCodePrestationInvaliditeTest.tousLesCodePrestation.add(55);
        PRCodePrestationInvaliditeTest.tousLesCodePrestation.add(56);
        PRCodePrestationInvaliditeTest.tousLesCodePrestation.add(70);
        PRCodePrestationInvaliditeTest.tousLesCodePrestation.add(72);
        PRCodePrestationInvaliditeTest.tousLesCodePrestation.add(73);
        PRCodePrestationInvaliditeTest.tousLesCodePrestation.add(74);
        PRCodePrestationInvaliditeTest.tousLesCodePrestation.add(75);
        PRCodePrestationInvaliditeTest.tousLesCodePrestation.add(76);
        for (Integer codePrestation : PRCodePrestationInvaliditeTest.tousLesCodePrestation) {
            PRCodePrestationInvaliditeTest.codePrestVieillesseString.add(String.valueOf(codePrestation));
        }
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testDefinitionDesCodePrestation() {

        // Contrôle des rentes principales, il ne peut y en avoir que 2 + 2 anciens codes
        int ctr = 0;
        for (PRCodePrestationInvalidite code : PRCodePrestationInvalidite.values()) {
            if (code.isPrestationPrincipale()) {
                ctr++;
            }
        }
        Assert.assertTrue(ctr == 4);

        TestUtils.testCodes(PRCodePrestationInvalidite._50, type, domain, 50);
        TestUtils.testCodes(PRCodePrestationInvalidite._52, type, domain, 52);
        TestUtils.testCodes(PRCodePrestationInvalidite._53, type, domain, 53);
        TestUtils.testCodes(PRCodePrestationInvalidite._54, type, domain, 54);
        TestUtils.testCodes(PRCodePrestationInvalidite._55, type, domain, 55);
        TestUtils.testCodes(PRCodePrestationInvalidite._56, type, domain, 56);
        TestUtils.testCodes(PRCodePrestationInvalidite._70, type, domain, 70);
        TestUtils.testCodes(PRCodePrestationInvalidite._72, type, domain, 72);
        TestUtils.testCodes(PRCodePrestationInvalidite._73, type, domain, 73);
        TestUtils.testCodes(PRCodePrestationInvalidite._74, type, domain, 74);
        TestUtils.testCodes(PRCodePrestationInvalidite._75, type, domain, 75);
        TestUtils.testCodes(PRCodePrestationInvalidite._76, type, domain, 76);

    }

    @SuppressWarnings("deprecation")
    @Test
    public void testMethodePublicIsPrestationPrincipale() {
        assertTrue(PRCodePrestationInvalidite._50.isPrestationPrincipale());
        assertTrue(PRCodePrestationInvalidite._52.isPrestationPrincipale());
        assertTrue(!PRCodePrestationInvalidite._53.isPrestationPrincipale());
        assertTrue(!PRCodePrestationInvalidite._54.isPrestationPrincipale());
        assertTrue(!PRCodePrestationInvalidite._55.isPrestationPrincipale());
        assertTrue(!PRCodePrestationInvalidite._56.isPrestationPrincipale());
        assertTrue(PRCodePrestationInvalidite._70.isPrestationPrincipale());
        assertTrue(PRCodePrestationInvalidite._72.isPrestationPrincipale());
        assertTrue(!PRCodePrestationInvalidite._73.isPrestationPrincipale());
        assertTrue(!PRCodePrestationInvalidite._74.isPrestationPrincipale());
        assertTrue(!PRCodePrestationInvalidite._75.isPrestationPrincipale());
        assertTrue(!PRCodePrestationInvalidite._76.isPrestationPrincipale());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testMethodePublicIsPrestationOrdinaire() {
        assertTrue(PRCodePrestationInvalidite._50.isPrestationOrdinaire());
        assertTrue(PRCodePrestationInvalidite._52.isPrestationOrdinaire());
        assertTrue(PRCodePrestationInvalidite._53.isPrestationOrdinaire());
        assertTrue(PRCodePrestationInvalidite._54.isPrestationOrdinaire());
        assertTrue(PRCodePrestationInvalidite._55.isPrestationOrdinaire());
        assertTrue(PRCodePrestationInvalidite._56.isPrestationOrdinaire());
        assertTrue(!PRCodePrestationInvalidite._70.isPrestationOrdinaire());
        assertTrue(!PRCodePrestationInvalidite._72.isPrestationOrdinaire());
        assertTrue(!PRCodePrestationInvalidite._73.isPrestationOrdinaire());
        assertTrue(!PRCodePrestationInvalidite._74.isPrestationOrdinaire());
        assertTrue(!PRCodePrestationInvalidite._75.isPrestationOrdinaire());
        assertTrue(!PRCodePrestationInvalidite._76.isPrestationOrdinaire());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testMethodePublicIsPrestationPourEnfant() {
        assertTrue(!PRCodePrestationInvalidite._50.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationInvalidite._52.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationInvalidite._53.isPrestationPourEnfant());
        assertTrue(PRCodePrestationInvalidite._54.isPrestationPourEnfant());
        assertTrue(PRCodePrestationInvalidite._55.isPrestationPourEnfant());
        assertTrue(PRCodePrestationInvalidite._56.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationInvalidite._70.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationInvalidite._72.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationInvalidite._73.isPrestationPourEnfant());
        assertTrue(PRCodePrestationInvalidite._74.isPrestationPourEnfant());
        assertTrue(PRCodePrestationInvalidite._75.isPrestationPourEnfant());
        assertTrue(PRCodePrestationInvalidite._76.isPrestationPourEnfant());
    }

    @Test
    public void testMethodeStaticIsPrestationPrincipaleInteger() {
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum code = PRCodePrestationInvalidite.getCodePrestation(ctr);
            if (code != null) {
                if (code.isPrestationPrincipale()) {
                    assertTrue(PRCodePrestationInvaliditeTest.codePrestPrincipalInteger.contains(code
                            .getCodePrestation()));
                    assertTrue(PRCodePrestationInvalidite.isPrestationPrincipale(ctr));
                } else {
                    assertTrue(!PRCodePrestationInvaliditeTest.codePrestPrincipalInteger.contains(code
                            .getCodePrestation()));
                    assertTrue(!PRCodePrestationInvalidite.isPrestationPrincipale(ctr));
                }
            } else {
                assertTrue(!PRCodePrestationInvalidite.isPrestationPrincipale(ctr));
            }
        }
    }

    @Test
    public void testMethodeStaticIsPrestationPrincipaleString() {
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum code = PRCodePrestationInvalidite.getCodePrestation(String.valueOf(ctr));
            if (code != null) {
                if (code.isPrestationPrincipale()) {
                    assertTrue(PRCodePrestationInvaliditeTest.codePrestPrincipalString.contains(code
                            .getCodePrestationAsString()));
                    assertTrue(PRCodePrestationInvalidite.isPrestationPrincipale(String.valueOf(ctr)));
                } else {
                    assertTrue(!PRCodePrestationInvaliditeTest.codePrestPrincipalString.contains(code
                            .getCodePrestationAsString()));
                    assertTrue(!PRCodePrestationInvalidite.isPrestationPrincipale(String.valueOf(ctr)));
                }
            } else {
                assertTrue(!PRCodePrestationInvalidite.isPrestationPrincipale(String.valueOf(ctr)));
            }
        }
    }

    @Test
    public void testMethodeStaticIsCodePrestationInvaliditeInteger() {
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum code = PRCodePrestationInvalidite.getCodePrestation(ctr);
            if (code != null) {
                assertTrue(PRCodePrestationInvalidite.isCodePrestationInvalidite(ctr));
                assertTrue(PRCodePrestationInvaliditeTest.tousLesCodePrestation.contains(code.getCodePrestation()));
            } else {
                assertTrue(!PRCodePrestationInvalidite.isCodePrestationInvalidite(ctr));
                assertTrue(!PRCodePrestationInvaliditeTest.tousLesCodePrestation.contains(ctr));
            }
        }
    }

    @Test
    public void testMethodeStaticIsCodePrestationInvaliditeString() {
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum code = PRCodePrestationInvalidite.getCodePrestation(String.valueOf(ctr));
            if (code != null) {
                assertTrue(PRCodePrestationInvaliditeTest.codePrestVieillesseString.contains(code
                        .getCodePrestationAsString()));
                assertTrue(PRCodePrestationInvalidite.isCodePrestationInvalidite(String.valueOf(ctr)));
            } else {
                assertTrue(!PRCodePrestationInvalidite.isCodePrestationInvalidite(String.valueOf(ctr)));
                assertTrue(!PRCodePrestationInvaliditeTest.codePrestVieillesseString.contains(String.valueOf(ctr)));
            }
        }
    }

    private void assertTrue(boolean exp) {
        Assert.assertTrue(exp);
    }
}
