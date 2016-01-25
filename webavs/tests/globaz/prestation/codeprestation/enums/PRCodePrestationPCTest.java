package globaz.prestation.codeprestation.enums;

import globaz.corvus.TestUtils;
import globaz.prestation.enums.codeprestation.IPRCodePrestationEnum;
import globaz.prestation.enums.codeprestation.PRCodePrestationPC;
import globaz.prestation.enums.codeprestation.PRDomainDePrestation;
import globaz.prestation.enums.codeprestation.PRTypeCodePrestation;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

public class PRCodePrestationPCTest {

    private PRTypeCodePrestation type = PRTypeCodePrestation.PC;

    private static final List<Integer> codePrestPrincipalInteger = new ArrayList<Integer>();
    private static final List<String> codePrestPrincipalString = new ArrayList<String>();
    private static final List<Integer> tousLesCodePrestationInteger = new ArrayList<Integer>();
    private static final List<String> tousLesCodePrestationString = new ArrayList<String>();

    static {
        PRCodePrestationPCTest.tousLesCodePrestationInteger.add(110);
        PRCodePrestationPCTest.tousLesCodePrestationInteger.add(113);
        PRCodePrestationPCTest.tousLesCodePrestationInteger.add(118);
        PRCodePrestationPCTest.tousLesCodePrestationInteger.add(119);
        PRCodePrestationPCTest.tousLesCodePrestationInteger.add(150);
        PRCodePrestationPCTest.tousLesCodePrestationInteger.add(158);
        PRCodePrestationPCTest.tousLesCodePrestationInteger.add(159);
        for (Integer codePrestation : PRCodePrestationPCTest.tousLesCodePrestationInteger) {
            PRCodePrestationPCTest.codePrestPrincipalInteger.add(codePrestation);
            PRCodePrestationPCTest.codePrestPrincipalString.add(String.valueOf(codePrestation));
            PRCodePrestationPCTest.tousLesCodePrestationString.add(String.valueOf(codePrestation));
        }
    }

    @Test
    public void testDefinitionDesCodePrestation() {
        Assert.assertTrue(PRCodePrestationPC.values().length == 7);
        TestUtils.testCodes(PRCodePrestationPC._110, type, PRDomainDePrestation.AVS, 110);
        TestUtils.testCodes(PRCodePrestationPC._113, type, PRDomainDePrestation.AVS, 113);
        TestUtils.testCodes(PRCodePrestationPC._118, type, PRDomainDePrestation.AVS, 118);
        TestUtils.testCodes(PRCodePrestationPC._119, type, PRDomainDePrestation.AVS, 119);
        TestUtils.testCodes(PRCodePrestationPC._150, type, PRDomainDePrestation.AI, 150);
        TestUtils.testCodes(PRCodePrestationPC._158, type, PRDomainDePrestation.AI, 158);
        TestUtils.testCodes(PRCodePrestationPC._159, type, PRDomainDePrestation.AI, 159);
    }

    @Test
    public void testMethodePublicIsPrestationPrincipale() {
        assertTrue(PRCodePrestationPC._110.isPrestationPrincipale());
        assertTrue(PRCodePrestationPC._113.isPrestationPrincipale());
        assertTrue(PRCodePrestationPC._118.isPrestationPrincipale());
        assertTrue(PRCodePrestationPC._119.isPrestationPrincipale());
        assertTrue(PRCodePrestationPC._150.isPrestationPrincipale());
        assertTrue(PRCodePrestationPC._158.isPrestationPrincipale());
        assertTrue(PRCodePrestationPC._159.isPrestationPrincipale());
    }

    @Test
    public void testMethodePublicIsPrestationOrdinaire() {
        assertTrue(PRCodePrestationPC._110.isPrestationOrdinaire());
        assertTrue(PRCodePrestationPC._113.isPrestationOrdinaire());
        assertTrue(PRCodePrestationPC._118.isPrestationOrdinaire());
        assertTrue(PRCodePrestationPC._119.isPrestationOrdinaire());
        assertTrue(PRCodePrestationPC._150.isPrestationOrdinaire());
        assertTrue(PRCodePrestationPC._158.isPrestationOrdinaire());
        assertTrue(PRCodePrestationPC._159.isPrestationOrdinaire());
    }

    @Test
    public void testMethodePublicIsPrestationPourEnfant() {
        assertTrue(!PRCodePrestationPC._110.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationPC._113.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationPC._118.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationPC._119.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationPC._150.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationPC._158.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationPC._159.isPrestationPourEnfant());
    }

    @Test
    public void testMethodeStaticIsPrestationPrincipaleInteger() {
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum code = PRCodePrestationPC.getCodePrestation(ctr);
            if (code != null) {
                if (code.isPrestationPrincipale()) {
                    assertTrue(PRCodePrestationPCTest.codePrestPrincipalInteger.contains(code.getCodePrestation()));
                    assertTrue(PRCodePrestationPC.isPrestationPrincipale(ctr));
                } else {
                    assertTrue(!PRCodePrestationPCTest.codePrestPrincipalInteger.contains(code.getCodePrestation()));
                    assertTrue(!PRCodePrestationPC.isPrestationPrincipale(ctr));
                }
            } else {
                assertTrue(!PRCodePrestationPC.isPrestationPrincipale(ctr));
            }
        }
    }

    @Test
    public void testMethodeStaticIsPrestationPrincipaleString() {
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum code = PRCodePrestationPC.getCodePrestation(String.valueOf(ctr));
            if (code != null) {
                if (code.isPrestationPrincipale()) {
                    assertTrue(PRCodePrestationPCTest.codePrestPrincipalString.contains(code
                            .getCodePrestationAsString()));
                    assertTrue(PRCodePrestationPC.isPrestationPrincipale(String.valueOf(ctr)));
                } else {
                    assertTrue(!PRCodePrestationPCTest.codePrestPrincipalString.contains(code
                            .getCodePrestationAsString()));
                    assertTrue(!PRCodePrestationPC.isPrestationPrincipale(String.valueOf(ctr)));
                }
            } else {
                assertTrue(!PRCodePrestationPC.isPrestationPrincipale(String.valueOf(ctr)));
            }
        }
    }

    @Test
    public void testMethodeStaticIsCodePrestationAPIInteger() {
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum code = PRCodePrestationPC.getCodePrestation(ctr);
            if (code != null) {
                assertTrue(PRCodePrestationPC.isCodePrestationPC(ctr));
                assertTrue(PRCodePrestationPCTest.tousLesCodePrestationInteger.contains(code.getCodePrestation()));
            } else {
                assertTrue(!PRCodePrestationPC.isCodePrestationPC(ctr));
                assertTrue(!PRCodePrestationPCTest.tousLesCodePrestationInteger.contains(ctr));
            }
        }
    }

    @Test
    public void testMethodeStaticIsCodePrestationAPIString() {
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum code = PRCodePrestationPC.getCodePrestation(String.valueOf(ctr));
            if (code != null) {
                assertTrue(PRCodePrestationPCTest.tousLesCodePrestationString
                        .contains(code.getCodePrestationAsString()));
                assertTrue(PRCodePrestationPC.isCodePrestationPC(String.valueOf(ctr)));
            } else {
                assertTrue(!PRCodePrestationPC.isCodePrestationPC(String.valueOf(ctr)));
                assertTrue(!PRCodePrestationPCTest.tousLesCodePrestationString.contains(String.valueOf(ctr)));
            }
        }
    }

    private void assertTrue(boolean exp) {
        Assert.assertTrue(exp);
    }

    @Test
    public void testAllocationDeNoel() {
        // isCodePrestationAllocationNoelAI
        Assert.assertFalse(PRCodePrestationPC.isCodePrestationAllocationNoelAI(PRCodePrestationPC._110
                .getCodePrestationAsString()));
        Assert.assertFalse(PRCodePrestationPC.isCodePrestationAllocationNoelAI(PRCodePrestationPC._113
                .getCodePrestationAsString()));
        Assert.assertFalse(PRCodePrestationPC.isCodePrestationAllocationNoelAI(PRCodePrestationPC._118
                .getCodePrestationAsString()));
        Assert.assertFalse(PRCodePrestationPC.isCodePrestationAllocationNoelAI(PRCodePrestationPC._119
                .getCodePrestationAsString()));
        Assert.assertFalse(PRCodePrestationPC.isCodePrestationAllocationNoelAI(PRCodePrestationPC._150
                .getCodePrestationAsString()));
        Assert.assertTrue(PRCodePrestationPC.isCodePrestationAllocationNoelAI(PRCodePrestationPC._158
                .getCodePrestationAsString()));
        Assert.assertTrue(PRCodePrestationPC.isCodePrestationAllocationNoelAI(PRCodePrestationPC._159
                .getCodePrestationAsString()));

        // isCodePrestationAllocationNoelAVS
        Assert.assertFalse(PRCodePrestationPC.isCodePrestationAllocationNoelAVS(PRCodePrestationPC._110
                .getCodePrestationAsString()));
        Assert.assertFalse(PRCodePrestationPC.isCodePrestationAllocationNoelAVS(PRCodePrestationPC._113
                .getCodePrestationAsString()));
        Assert.assertTrue(PRCodePrestationPC.isCodePrestationAllocationNoelAVS(PRCodePrestationPC._118
                .getCodePrestationAsString()));
        Assert.assertTrue(PRCodePrestationPC.isCodePrestationAllocationNoelAVS(PRCodePrestationPC._119
                .getCodePrestationAsString()));
        Assert.assertFalse(PRCodePrestationPC.isCodePrestationAllocationNoelAVS(PRCodePrestationPC._150
                .getCodePrestationAsString()));
        Assert.assertFalse(PRCodePrestationPC.isCodePrestationAllocationNoelAVS(PRCodePrestationPC._158
                .getCodePrestationAsString()));
        Assert.assertFalse(PRCodePrestationPC.isCodePrestationAllocationNoelAVS(PRCodePrestationPC._159
                .getCodePrestationAsString()));

        // isCodePrestationAllocationNoel
        Assert.assertFalse(PRCodePrestationPC.isCodePrestationAllocationNoel(PRCodePrestationPC._110
                .getCodePrestationAsString()));
        Assert.assertFalse(PRCodePrestationPC.isCodePrestationAllocationNoel(PRCodePrestationPC._113
                .getCodePrestationAsString()));
        Assert.assertTrue(PRCodePrestationPC.isCodePrestationAllocationNoel(PRCodePrestationPC._118
                .getCodePrestationAsString()));
        Assert.assertTrue(PRCodePrestationPC.isCodePrestationAllocationNoel(PRCodePrestationPC._119
                .getCodePrestationAsString()));
        Assert.assertFalse(PRCodePrestationPC.isCodePrestationAllocationNoel(PRCodePrestationPC._150
                .getCodePrestationAsString()));
        Assert.assertTrue(PRCodePrestationPC.isCodePrestationAllocationNoel(PRCodePrestationPC._158
                .getCodePrestationAsString()));
        Assert.assertTrue(PRCodePrestationPC.isCodePrestationAllocationNoel(PRCodePrestationPC._159
                .getCodePrestationAsString()));
    }

}
