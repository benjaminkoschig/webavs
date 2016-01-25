package globaz.prestation.codeprestation.enums;

import globaz.corvus.TestUtils;
import globaz.prestation.enums.codeprestation.IPRCodePrestationEnum;
import globaz.prestation.enums.codeprestation.PRDomainDePrestation;
import globaz.prestation.enums.codeprestation.PRTypeCodePrestation;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationAPI;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class PRCodePrestationAPITest {

    private PRTypeCodePrestation type = PRTypeCodePrestation.API;

    private static final List<Integer> codePrestPrincipalInteger = new ArrayList<Integer>();
    private static final List<String> codePrestPrincipalString = new ArrayList<String>();
    private static final List<Integer> tousLesCodePrestationInteger = new ArrayList<Integer>();
    private static final List<String> tousLesCodePrestationString = new ArrayList<String>();

    static {
        PRCodePrestationAPITest.tousLesCodePrestationInteger.add(81);
        PRCodePrestationAPITest.tousLesCodePrestationInteger.add(82);
        PRCodePrestationAPITest.tousLesCodePrestationInteger.add(83);
        PRCodePrestationAPITest.tousLesCodePrestationInteger.add(84);
        PRCodePrestationAPITest.tousLesCodePrestationInteger.add(85);
        PRCodePrestationAPITest.tousLesCodePrestationInteger.add(86);
        PRCodePrestationAPITest.tousLesCodePrestationInteger.add(87);
        PRCodePrestationAPITest.tousLesCodePrestationInteger.add(88);
        PRCodePrestationAPITest.tousLesCodePrestationInteger.add(89);
        PRCodePrestationAPITest.tousLesCodePrestationInteger.add(91);
        PRCodePrestationAPITest.tousLesCodePrestationInteger.add(92);
        PRCodePrestationAPITest.tousLesCodePrestationInteger.add(93);
        PRCodePrestationAPITest.tousLesCodePrestationInteger.add(95);
        PRCodePrestationAPITest.tousLesCodePrestationInteger.add(96);
        PRCodePrestationAPITest.tousLesCodePrestationInteger.add(97);
        for (Integer codePrestation : PRCodePrestationAPITest.tousLesCodePrestationInteger) {
            PRCodePrestationAPITest.codePrestPrincipalInteger.add(codePrestation);
            PRCodePrestationAPITest.codePrestPrincipalString.add(String.valueOf(codePrestation));
            PRCodePrestationAPITest.tousLesCodePrestationString.add(String.valueOf(codePrestation));
        }
    }

    @Test
    @Ignore
    public void testDefinitionDesCodePrestation() {

        // Contrôle des rentes principales
        int ctr = 0;
        for (PRCodePrestationAPI code : PRCodePrestationAPI.values()) {
            if (code.isPrestationPrincipale()) {
                ctr++;
            }
        }
        Assert.assertTrue(ctr == 15);

        TestUtils.testCodes(PRCodePrestationAPI._81, PRTypeCodePrestation.API, PRDomainDePrestation.AI, 81);
        TestUtils.testCodes(PRCodePrestationAPI._82, PRTypeCodePrestation.API, PRDomainDePrestation.AI, 82);
        TestUtils.testCodes(PRCodePrestationAPI._83, PRTypeCodePrestation.API, PRDomainDePrestation.AI, 83);
        TestUtils.testCodes(PRCodePrestationAPI._84, PRTypeCodePrestation.API, PRDomainDePrestation.AI, 84);
        TestUtils.testCodes(PRCodePrestationAPI._85, PRTypeCodePrestation.API, PRDomainDePrestation.AVS, 85);
        TestUtils.testCodes(PRCodePrestationAPI._86, PRTypeCodePrestation.API, PRDomainDePrestation.AVS, 86);
        TestUtils.testCodes(PRCodePrestationAPI._87, PRTypeCodePrestation.API, PRDomainDePrestation.AVS, 87);
        TestUtils.testCodes(PRCodePrestationAPI._88, PRTypeCodePrestation.API, PRDomainDePrestation.AI, 88);
        TestUtils.testCodes(PRCodePrestationAPI._89, PRTypeCodePrestation.API, PRDomainDePrestation.AVS, 89);
        TestUtils.testCodes(PRCodePrestationAPI._91, PRTypeCodePrestation.API, PRDomainDePrestation.AI, 91);
        TestUtils.testCodes(PRCodePrestationAPI._92, PRTypeCodePrestation.API, PRDomainDePrestation.AI, 92);
        TestUtils.testCodes(PRCodePrestationAPI._93, PRTypeCodePrestation.API, PRDomainDePrestation.AI, 93);
        TestUtils.testCodes(PRCodePrestationAPI._95, PRTypeCodePrestation.API, PRDomainDePrestation.AVS, 95);
        TestUtils.testCodes(PRCodePrestationAPI._96, PRTypeCodePrestation.API, PRDomainDePrestation.AVS, 96);
        TestUtils.testCodes(PRCodePrestationAPI._97, PRTypeCodePrestation.API, PRDomainDePrestation.AVS, 97);
    }

    @Test
    public void testMethodePublicIsPrestationPrincipale() {
        assertTrue(PRCodePrestationAPI._81.isPrestationPrincipale());
        assertTrue(PRCodePrestationAPI._82.isPrestationPrincipale());
        assertTrue(PRCodePrestationAPI._83.isPrestationPrincipale());
        assertTrue(PRCodePrestationAPI._84.isPrestationPrincipale());
        assertTrue(PRCodePrestationAPI._85.isPrestationPrincipale());
        assertTrue(PRCodePrestationAPI._86.isPrestationPrincipale());
        assertTrue(PRCodePrestationAPI._87.isPrestationPrincipale());
        assertTrue(PRCodePrestationAPI._88.isPrestationPrincipale());
        assertTrue(PRCodePrestationAPI._89.isPrestationPrincipale());
        assertTrue(PRCodePrestationAPI._91.isPrestationPrincipale());
        assertTrue(PRCodePrestationAPI._92.isPrestationPrincipale());
        assertTrue(PRCodePrestationAPI._93.isPrestationPrincipale());
        assertTrue(PRCodePrestationAPI._95.isPrestationPrincipale());
        assertTrue(PRCodePrestationAPI._96.isPrestationPrincipale());
        assertTrue(PRCodePrestationAPI._97.isPrestationPrincipale());
    }

    @Test
    public void testMethodePublicIsPrestationOrdinaire() {
        assertTrue(PRCodePrestationAPI._81.isPrestationOrdinaire());
        assertTrue(PRCodePrestationAPI._82.isPrestationOrdinaire());
        assertTrue(PRCodePrestationAPI._83.isPrestationOrdinaire());
        assertTrue(PRCodePrestationAPI._84.isPrestationOrdinaire());
        assertTrue(PRCodePrestationAPI._85.isPrestationOrdinaire());
        assertTrue(PRCodePrestationAPI._86.isPrestationOrdinaire());
        assertTrue(PRCodePrestationAPI._87.isPrestationOrdinaire());
        assertTrue(PRCodePrestationAPI._88.isPrestationOrdinaire());
        assertTrue(PRCodePrestationAPI._89.isPrestationOrdinaire());
        assertTrue(PRCodePrestationAPI._91.isPrestationOrdinaire());
        assertTrue(PRCodePrestationAPI._92.isPrestationOrdinaire());
        assertTrue(PRCodePrestationAPI._93.isPrestationOrdinaire());
        assertTrue(PRCodePrestationAPI._95.isPrestationOrdinaire());
        assertTrue(PRCodePrestationAPI._96.isPrestationOrdinaire());
        assertTrue(PRCodePrestationAPI._97.isPrestationOrdinaire());

    }

    @Test
    public void testMethodePublicIsPrestationPourEnfant() {
        assertTrue(!PRCodePrestationAPI._81.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationAPI._82.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationAPI._83.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationAPI._84.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationAPI._85.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationAPI._86.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationAPI._87.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationAPI._88.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationAPI._89.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationAPI._91.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationAPI._92.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationAPI._93.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationAPI._95.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationAPI._96.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationAPI._97.isPrestationPourEnfant());

    }

    @Test
    @Ignore
    public void testMethodeStaticIsPrestationPrincipaleInteger() {
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum code = PRCodePrestationAPI.getCodePrestation(ctr);
            if (code != null) {
                if (code.isPrestationPrincipale()) {
                    assertTrue(PRCodePrestationAPITest.codePrestPrincipalInteger.contains(code.getCodePrestation()));
                    assertTrue(PRCodePrestationAPI.isPrestationPrincipale(ctr));
                } else {
                    assertTrue(!PRCodePrestationAPITest.codePrestPrincipalInteger.contains(code.getCodePrestation()));
                    assertTrue(!PRCodePrestationAPI.isPrestationPrincipale(ctr));
                }
            } else {
                assertTrue(!PRCodePrestationAPI.isPrestationPrincipale(ctr));
            }
        }
    }

    @Test
    @Ignore
    public void testMethodeStaticIsPrestationPrincipaleString() {
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum code = PRCodePrestationAPI.getCodePrestation(String.valueOf(ctr));
            if (code != null) {
                if (code.isPrestationPrincipale()) {
                    assertTrue(PRCodePrestationAPITest.codePrestPrincipalString.contains(code
                            .getCodePrestationAsString()));
                    assertTrue(PRCodePrestationAPI.isPrestationPrincipale(String.valueOf(ctr)));
                } else {
                    assertTrue(!PRCodePrestationAPITest.codePrestPrincipalString.contains(code
                            .getCodePrestationAsString()));
                    assertTrue(!PRCodePrestationAPI.isPrestationPrincipale(String.valueOf(ctr)));
                }
            } else {
                assertTrue(!PRCodePrestationAPI.isPrestationPrincipale(String.valueOf(ctr)));
            }
        }
    }

    @Test
    @Ignore
    public void testMethodeStaticIsCodePrestationAPIInteger() {
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum code = PRCodePrestationAPI.getCodePrestation(ctr);
            if (code != null) {
                assertTrue(PRCodePrestationAPI.isCodePrestationAPI(ctr));
                assertTrue(PRCodePrestationAPITest.tousLesCodePrestationInteger.contains(code.getCodePrestation()));
            } else {
                assertTrue(!PRCodePrestationAPI.isCodePrestationAPI(ctr));
                assertTrue(!PRCodePrestationAPITest.tousLesCodePrestationInteger.contains(ctr));
            }
        }
    }

    @Test
    @Ignore
    public void testMethodeStaticIsCodePrestationAPIString() {
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum code = PRCodePrestationAPI.getCodePrestation(String.valueOf(ctr));
            if (code != null) {
                assertTrue(PRCodePrestationAPITest.tousLesCodePrestationString.contains(code
                        .getCodePrestationAsString()));
                assertTrue(PRCodePrestationAPI.isCodePrestationAPI(String.valueOf(ctr)));
            } else {
                assertTrue(!PRCodePrestationAPI.isCodePrestationAPI(String.valueOf(ctr)));
                assertTrue(!PRCodePrestationAPITest.tousLesCodePrestationString.contains(String.valueOf(ctr)));
            }
        }
    }

    private void assertTrue(boolean exp) {
        Assert.assertTrue(exp);
    }
}
