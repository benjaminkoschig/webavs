package globaz.prestation.codeprestation.enums;

import globaz.corvus.TestUtils;
import globaz.prestation.enums.codeprestation.IPRCodePrestationEnum;
import globaz.prestation.enums.codeprestation.PRDomainDePrestation;
import globaz.prestation.enums.codeprestation.PRTypeCodePrestation;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationVieillesse;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

public class PRCodePrestationVieillesseTest {

    private PRTypeCodePrestation type = PRTypeCodePrestation.VIEILLESSE;
    private PRDomainDePrestation domain = PRDomainDePrestation.AVS;

    private static final List<Integer> codePrestPrincipalInteger = new ArrayList<Integer>();
    private static final List<String> codePrestPrincipalString = new ArrayList<String>();
    private static final List<Integer> tousLesCodePrestation = new ArrayList<Integer>();
    private static final List<String> codePrestVieillesseString = new ArrayList<String>();

    static {
        PRCodePrestationVieillesseTest.codePrestPrincipalInteger.add(10);
        PRCodePrestationVieillesseTest.codePrestPrincipalInteger.add(12);
        PRCodePrestationVieillesseTest.codePrestPrincipalInteger.add(20);
        PRCodePrestationVieillesseTest.codePrestPrincipalInteger.add(22);
        for (Integer codePrestation : PRCodePrestationVieillesseTest.codePrestPrincipalInteger) {
            PRCodePrestationVieillesseTest.codePrestPrincipalString.add(String.valueOf(codePrestation));
        }
        PRCodePrestationVieillesseTest.tousLesCodePrestation.add(10);
        PRCodePrestationVieillesseTest.tousLesCodePrestation.add(12);
        PRCodePrestationVieillesseTest.tousLesCodePrestation.add(20);
        PRCodePrestationVieillesseTest.tousLesCodePrestation.add(22);
        PRCodePrestationVieillesseTest.tousLesCodePrestation.add(33);
        PRCodePrestationVieillesseTest.tousLesCodePrestation.add(34);
        PRCodePrestationVieillesseTest.tousLesCodePrestation.add(35);
        PRCodePrestationVieillesseTest.tousLesCodePrestation.add(36);
        PRCodePrestationVieillesseTest.tousLesCodePrestation.add(43);
        PRCodePrestationVieillesseTest.tousLesCodePrestation.add(44);
        PRCodePrestationVieillesseTest.tousLesCodePrestation.add(45);
        PRCodePrestationVieillesseTest.tousLesCodePrestation.add(46);
        for (Integer codePrestation : PRCodePrestationVieillesseTest.tousLesCodePrestation) {
            PRCodePrestationVieillesseTest.codePrestVieillesseString.add(String.valueOf(codePrestation));
        }
    }

    @Test
    public void testDefinitionDesCodePrestation() {

        // Contrôle des rentes principales, il ne peut y en avoir que 2 + 2 anciens codes
        int ctr = 0;
        for (PRCodePrestationVieillesse code : PRCodePrestationVieillesse.values()) {
            if (code.isPrestationPrincipale()) {
                ctr++;
            }
        }
        Assert.assertTrue(ctr == 4);

        TestUtils.testCodes(PRCodePrestationVieillesse._10, type, domain, 10);
        TestUtils.testCodes(PRCodePrestationVieillesse._12, type, domain, 12);
        TestUtils.testCodes(PRCodePrestationVieillesse._20, type, domain, 20);
        TestUtils.testCodes(PRCodePrestationVieillesse._22, type, domain, 22);
        TestUtils.testCodes(PRCodePrestationVieillesse._33, type, domain, 33);
        TestUtils.testCodes(PRCodePrestationVieillesse._34, type, domain, 34);
        TestUtils.testCodes(PRCodePrestationVieillesse._35, type, domain, 35);
        TestUtils.testCodes(PRCodePrestationVieillesse._36, type, domain, 36);
        TestUtils.testCodes(PRCodePrestationVieillesse._43, type, domain, 43);
        TestUtils.testCodes(PRCodePrestationVieillesse._44, type, domain, 44);
        TestUtils.testCodes(PRCodePrestationVieillesse._45, type, domain, 45);
        TestUtils.testCodes(PRCodePrestationVieillesse._46, type, domain, 46);

    }

    @Test
    public void testMethodePublicIsPrestationPrincipale() {
        assertTrue(PRCodePrestationVieillesse._10.isPrestationPrincipale());
        assertTrue(PRCodePrestationVieillesse._12.isPrestationPrincipale());
        assertTrue(PRCodePrestationVieillesse._20.isPrestationPrincipale());
        assertTrue(PRCodePrestationVieillesse._22.isPrestationPrincipale());
        assertTrue(!PRCodePrestationVieillesse._33.isPrestationPrincipale());
        assertTrue(!PRCodePrestationVieillesse._34.isPrestationPrincipale());
        assertTrue(!PRCodePrestationVieillesse._35.isPrestationPrincipale());
        assertTrue(!PRCodePrestationVieillesse._36.isPrestationPrincipale());
        assertTrue(!PRCodePrestationVieillesse._43.isPrestationPrincipale());
        assertTrue(!PRCodePrestationVieillesse._44.isPrestationPrincipale());
        assertTrue(!PRCodePrestationVieillesse._45.isPrestationPrincipale());
        assertTrue(!PRCodePrestationVieillesse._46.isPrestationPrincipale());
    }

    @Test
    public void testMethodePublicIsPrestationOrdinaire() {
        assertTrue(PRCodePrestationVieillesse._10.isPrestationOrdinaire());
        assertTrue(PRCodePrestationVieillesse._12.isPrestationOrdinaire());
        assertTrue(!PRCodePrestationVieillesse._20.isPrestationOrdinaire());
        assertTrue(!PRCodePrestationVieillesse._22.isPrestationOrdinaire());
        assertTrue(PRCodePrestationVieillesse._33.isPrestationOrdinaire());
        assertTrue(PRCodePrestationVieillesse._34.isPrestationOrdinaire());
        assertTrue(PRCodePrestationVieillesse._35.isPrestationOrdinaire());
        assertTrue(PRCodePrestationVieillesse._36.isPrestationOrdinaire());
        assertTrue(!PRCodePrestationVieillesse._43.isPrestationOrdinaire());
        assertTrue(!PRCodePrestationVieillesse._44.isPrestationOrdinaire());
        assertTrue(!PRCodePrestationVieillesse._45.isPrestationOrdinaire());
        assertTrue(!PRCodePrestationVieillesse._46.isPrestationOrdinaire());
    }

    @Test
    public void testMethodePublicIsPrestationPourEnfant() {
        assertTrue(!PRCodePrestationVieillesse._10.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationVieillesse._12.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationVieillesse._20.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationVieillesse._22.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationVieillesse._33.isPrestationPourEnfant());
        assertTrue(PRCodePrestationVieillesse._34.isPrestationPourEnfant());
        assertTrue(PRCodePrestationVieillesse._35.isPrestationPourEnfant());
        assertTrue(PRCodePrestationVieillesse._36.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationVieillesse._43.isPrestationPourEnfant());
        assertTrue(PRCodePrestationVieillesse._44.isPrestationPourEnfant());
        assertTrue(PRCodePrestationVieillesse._45.isPrestationPourEnfant());
        assertTrue(PRCodePrestationVieillesse._46.isPrestationPourEnfant());
    }

    @Test
    public void testMethodeStaticIsPrestationPrincipaleInteger() {
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum code = PRCodePrestationVieillesse.getCodePrestation(ctr);
            if (code != null) {
                if (code.isPrestationPrincipale()) {
                    assertTrue(PRCodePrestationVieillesseTest.codePrestPrincipalInteger.contains(code
                            .getCodePrestation()));
                    assertTrue(PRCodePrestationVieillesse.isPrestationPrincipale(ctr));
                } else {
                    assertTrue(!PRCodePrestationVieillesseTest.codePrestPrincipalInteger.contains(code
                            .getCodePrestation()));
                    assertTrue(!PRCodePrestationVieillesse.isPrestationPrincipale(ctr));
                }
            } else {
                assertTrue(!PRCodePrestationVieillesse.isPrestationPrincipale(ctr));
            }
        }
    }

    @Test
    public void testMethodeStaticIsPrestationPrincipaleString() {
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum code = PRCodePrestationVieillesse.getCodePrestation(String.valueOf(ctr));
            if (code != null) {
                if (code.isPrestationPrincipale()) {
                    assertTrue(PRCodePrestationVieillesseTest.codePrestPrincipalString.contains(code
                            .getCodePrestationAsString()));
                    assertTrue(PRCodePrestationVieillesse.isPrestationPrincipale(String.valueOf(ctr)));
                } else {
                    assertTrue(!PRCodePrestationVieillesseTest.codePrestPrincipalString.contains(code
                            .getCodePrestationAsString()));
                    assertTrue(!PRCodePrestationVieillesse.isPrestationPrincipale(String.valueOf(ctr)));
                }
            } else {
                assertTrue(!PRCodePrestationVieillesse.isPrestationPrincipale(String.valueOf(ctr)));
            }
        }
    }

    @Test
    public void testMethodeStaticIsCodePrestationVieillesseInteger() {
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum code = PRCodePrestationVieillesse.getCodePrestation(ctr);
            if (code != null) {
                assertTrue(PRCodePrestationVieillesse.isCodePrestationVieillesse(ctr));
                assertTrue(PRCodePrestationVieillesseTest.tousLesCodePrestation.contains(code.getCodePrestation()));
            } else {
                assertTrue(!PRCodePrestationVieillesse.isCodePrestationVieillesse(ctr));
                assertTrue(!PRCodePrestationVieillesseTest.tousLesCodePrestation.contains(ctr));
            }
        }
    }

    @Test
    public void testMethodeStaticIsCodePrestationVieillesseString() {
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum code = PRCodePrestationVieillesse.getCodePrestation(String.valueOf(ctr));
            if (code != null) {
                assertTrue(PRCodePrestationVieillesseTest.codePrestVieillesseString.contains(code
                        .getCodePrestationAsString()));
                assertTrue(PRCodePrestationVieillesse.isCodePrestationVieillesse(String.valueOf(ctr)));
            } else {
                assertTrue(!PRCodePrestationVieillesse.isCodePrestationVieillesse(String.valueOf(ctr)));
                assertTrue(!PRCodePrestationVieillesseTest.codePrestVieillesseString.contains(String.valueOf(ctr)));
            }
        }
    }

    private void assertTrue(boolean exp) {
        Assert.assertTrue(exp);
    }
}
