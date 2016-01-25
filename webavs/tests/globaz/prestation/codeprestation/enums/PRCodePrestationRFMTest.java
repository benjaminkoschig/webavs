package globaz.prestation.codeprestation.enums;

import globaz.corvus.TestUtils;
import globaz.prestation.enums.codeprestation.IPRCodePrestationEnum;
import globaz.prestation.enums.codeprestation.PRCodePrestationRFM;
import globaz.prestation.enums.codeprestation.PRDomainDePrestation;
import globaz.prestation.enums.codeprestation.PRTypeCodePrestation;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

public class PRCodePrestationRFMTest {

    private PRTypeCodePrestation type = PRTypeCodePrestation.RFM;

    private static final List<Integer> codePrestPrincipalInteger = new ArrayList<Integer>();
    private static final List<String> codePrestPrincipalString = new ArrayList<String>();
    private static final List<Integer> tousLesCodePrestationInteger = new ArrayList<Integer>();
    private static final List<String> tousLesCodePrestationString = new ArrayList<String>();

    static {
        PRCodePrestationRFMTest.tousLesCodePrestationInteger.add(210);
        PRCodePrestationRFMTest.tousLesCodePrestationInteger.add(213);
        PRCodePrestationRFMTest.tousLesCodePrestationInteger.add(250);
        for (Integer codePrestation : PRCodePrestationRFMTest.tousLesCodePrestationInteger) {
            PRCodePrestationRFMTest.codePrestPrincipalInteger.add(codePrestation);
            PRCodePrestationRFMTest.codePrestPrincipalString.add(String.valueOf(codePrestation));
            PRCodePrestationRFMTest.tousLesCodePrestationString.add(String.valueOf(codePrestation));
        }
    }

    @Test
    public void testDefinitionDesCodePrestation() {
        Assert.assertTrue(PRCodePrestationRFM.values().length == 3);
        TestUtils.testCodes(PRCodePrestationRFM._210, type, PRDomainDePrestation.AVS, 210);
        TestUtils.testCodes(PRCodePrestationRFM._213, type, PRDomainDePrestation.AVS, 213);
        TestUtils.testCodes(PRCodePrestationRFM._250, type, PRDomainDePrestation.AI, 250);
    }

    @Test
    public void testMethodePublicIsPrestationPrincipale() {
        assertTrue(PRCodePrestationRFM._210.isPrestationPrincipale());
        assertTrue(PRCodePrestationRFM._213.isPrestationPrincipale());
        assertTrue(PRCodePrestationRFM._250.isPrestationPrincipale());
    }

    @Test
    public void testMethodePublicIsPrestationOrdinaire() {
        assertTrue(PRCodePrestationRFM._210.isPrestationOrdinaire());
        assertTrue(PRCodePrestationRFM._213.isPrestationOrdinaire());
        assertTrue(PRCodePrestationRFM._250.isPrestationOrdinaire());
    }

    @Test
    public void testMethodePublicIsPrestationPourEnfant() {
        assertTrue(!PRCodePrestationRFM._210.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationRFM._213.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationRFM._250.isPrestationPourEnfant());
    }

    @Test
    public void testMethodeStaticIsPrestationPrincipaleInteger() {
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum code = PRCodePrestationRFM.getCodePrestation(ctr);
            if (code != null) {
                if (code.isPrestationPrincipale()) {
                    assertTrue(PRCodePrestationRFMTest.codePrestPrincipalInteger.contains(code.getCodePrestation()));
                    assertTrue(PRCodePrestationRFM.isPrestationPrincipale(ctr));
                } else {
                    assertTrue(!PRCodePrestationRFMTest.codePrestPrincipalInteger.contains(code.getCodePrestation()));
                    assertTrue(!PRCodePrestationRFM.isPrestationPrincipale(ctr));
                }
            } else {
                assertTrue(!PRCodePrestationRFM.isPrestationPrincipale(ctr));
            }
        }
    }

    @Test
    public void testMethodeStaticIsPrestationPrincipaleString() {
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum code = PRCodePrestationRFM.getCodePrestation(String.valueOf(ctr));
            if (code != null) {
                if (code.isPrestationPrincipale()) {
                    assertTrue(PRCodePrestationRFMTest.codePrestPrincipalString.contains(code
                            .getCodePrestationAsString()));
                    assertTrue(PRCodePrestationRFM.isPrestationPrincipale(String.valueOf(ctr)));
                } else {
                    assertTrue(!PRCodePrestationRFMTest.codePrestPrincipalString.contains(code
                            .getCodePrestationAsString()));
                    assertTrue(!PRCodePrestationRFM.isPrestationPrincipale(String.valueOf(ctr)));
                }
            } else {
                assertTrue(!PRCodePrestationRFM.isPrestationPrincipale(String.valueOf(ctr)));
            }
        }
    }

    @Test
    public void testMethodeStaticIsCodePrestationAPIInteger() {
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum code = PRCodePrestationRFM.getCodePrestation(ctr);
            if (code != null) {
                assertTrue(PRCodePrestationRFM.isCodePrestationRFM(ctr));
                assertTrue(PRCodePrestationRFMTest.tousLesCodePrestationInteger.contains(code.getCodePrestation()));
            } else {
                assertTrue(!PRCodePrestationRFM.isCodePrestationRFM(ctr));
                assertTrue(!PRCodePrestationRFMTest.tousLesCodePrestationInteger.contains(ctr));
            }
        }
    }

    @Test
    public void testMethodeStaticIsCodePrestationAPIString() {
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum code = PRCodePrestationRFM.getCodePrestation(String.valueOf(ctr));
            if (code != null) {
                assertTrue(PRCodePrestationRFMTest.tousLesCodePrestationString.contains(code
                        .getCodePrestationAsString()));
                assertTrue(PRCodePrestationRFM.isCodePrestationRFM(String.valueOf(ctr)));
            } else {
                assertTrue(!PRCodePrestationRFM.isCodePrestationRFM(String.valueOf(ctr)));
                assertTrue(!PRCodePrestationRFMTest.tousLesCodePrestationString.contains(String.valueOf(ctr)));
            }
        }
    }

    private void assertTrue(boolean exp) {
        Assert.assertTrue(exp);
    }

}
