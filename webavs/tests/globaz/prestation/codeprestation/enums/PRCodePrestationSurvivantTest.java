package globaz.prestation.codeprestation.enums;

import globaz.corvus.TestUtils;
import globaz.prestation.enums.codeprestation.IPRCodePrestationEnum;
import globaz.prestation.enums.codeprestation.PRDomainDePrestation;
import globaz.prestation.enums.codeprestation.PRTypeCodePrestation;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationSurvivant;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

public class PRCodePrestationSurvivantTest {

    private PRTypeCodePrestation type = PRTypeCodePrestation.SURVIVANT;
    private PRDomainDePrestation domain = PRDomainDePrestation.AVS;

    private static final List<Integer> codePrestPrincipalInteger = new ArrayList<Integer>();
    private static final List<String> codePrestPrincipalString = new ArrayList<String>();
    private static final List<Integer> tousLesCodePrestation = new ArrayList<Integer>();
    private static final List<String> codePrestComplementaireString = new ArrayList<String>();

    static {
        PRCodePrestationSurvivantTest.codePrestPrincipalInteger.add(13);
        PRCodePrestationSurvivantTest.codePrestPrincipalInteger.add(23);
        for (Integer codePrestation : PRCodePrestationSurvivantTest.codePrestPrincipalInteger) {
            PRCodePrestationSurvivantTest.codePrestPrincipalString.add(String.valueOf(codePrestation));
        }
        PRCodePrestationSurvivantTest.tousLesCodePrestation.add(13);
        PRCodePrestationSurvivantTest.tousLesCodePrestation.add(14);
        PRCodePrestationSurvivantTest.tousLesCodePrestation.add(15);
        PRCodePrestationSurvivantTest.tousLesCodePrestation.add(16);
        PRCodePrestationSurvivantTest.tousLesCodePrestation.add(23);
        PRCodePrestationSurvivantTest.tousLesCodePrestation.add(24);
        PRCodePrestationSurvivantTest.tousLesCodePrestation.add(25);
        PRCodePrestationSurvivantTest.tousLesCodePrestation.add(26);
        for (Integer codePrestation : PRCodePrestationSurvivantTest.tousLesCodePrestation) {
            PRCodePrestationSurvivantTest.codePrestComplementaireString.add(String.valueOf(codePrestation));
        }
    }

    @Test
    public void testDefinitionDesCodePrestation() {

        // Contrôle des rentes principales, il ne peut y en avoir que 2 + 2 anciens codes
        int ctr = 0;
        for (PRCodePrestationSurvivant code : PRCodePrestationSurvivant.values()) {
            if (code.isPrestationPrincipale()) {
                ctr++;
            }
        }
        Assert.assertTrue(ctr == 2);

        TestUtils.testCodes(PRCodePrestationSurvivant._13, type, domain, 13);
        TestUtils.testCodes(PRCodePrestationSurvivant._14, type, domain, 14);
        TestUtils.testCodes(PRCodePrestationSurvivant._15, type, domain, 15);
        TestUtils.testCodes(PRCodePrestationSurvivant._16, type, domain, 16);
        TestUtils.testCodes(PRCodePrestationSurvivant._23, type, domain, 23);
        TestUtils.testCodes(PRCodePrestationSurvivant._24, type, domain, 24);
        TestUtils.testCodes(PRCodePrestationSurvivant._25, type, domain, 25);
        TestUtils.testCodes(PRCodePrestationSurvivant._26, type, domain, 26);
    }

    @Test
    public void testMethodePublicIsPrestationPrincipale() {
        assertTrue(PRCodePrestationSurvivant._13.isPrestationPrincipale());
        assertTrue(!PRCodePrestationSurvivant._14.isPrestationPrincipale());
        assertTrue(!PRCodePrestationSurvivant._15.isPrestationPrincipale());
        assertTrue(!PRCodePrestationSurvivant._16.isPrestationPrincipale());
        assertTrue(PRCodePrestationSurvivant._23.isPrestationPrincipale());
        assertTrue(!PRCodePrestationSurvivant._24.isPrestationPrincipale());
        assertTrue(!PRCodePrestationSurvivant._25.isPrestationPrincipale());
        assertTrue(!PRCodePrestationSurvivant._26.isPrestationPrincipale());
    }

    @Test
    public void testMethodePublicIsPrestationOrdinaire() {
        assertTrue(PRCodePrestationSurvivant._13.isPrestationOrdinaire());
        assertTrue(PRCodePrestationSurvivant._14.isPrestationOrdinaire());
        assertTrue(PRCodePrestationSurvivant._15.isPrestationOrdinaire());
        assertTrue(PRCodePrestationSurvivant._16.isPrestationOrdinaire());
        assertTrue(!PRCodePrestationSurvivant._23.isPrestationOrdinaire());
        assertTrue(!PRCodePrestationSurvivant._24.isPrestationOrdinaire());
        assertTrue(!PRCodePrestationSurvivant._25.isPrestationOrdinaire());
        assertTrue(!PRCodePrestationSurvivant._26.isPrestationOrdinaire());
    }

    @Test
    public void testMethodePublicIsPrestationPourEnfant() {
        assertTrue(!PRCodePrestationSurvivant._13.isPrestationPourEnfant());
        assertTrue(PRCodePrestationSurvivant._14.isPrestationPourEnfant());
        assertTrue(PRCodePrestationSurvivant._15.isPrestationPourEnfant());
        assertTrue(PRCodePrestationSurvivant._16.isPrestationPourEnfant());
        assertTrue(!PRCodePrestationSurvivant._23.isPrestationPourEnfant());
        assertTrue(PRCodePrestationSurvivant._24.isPrestationPourEnfant());
        assertTrue(PRCodePrestationSurvivant._25.isPrestationPourEnfant());
        assertTrue(PRCodePrestationSurvivant._26.isPrestationPourEnfant());
    }

    @Test
    public void testMethodeStaticIsPrestationPrincipaleInteger() {
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum code = PRCodePrestationSurvivant.getCodePrestation(ctr);
            if (code != null) {
                if (code.isPrestationPrincipale()) {
                    assertTrue(PRCodePrestationSurvivantTest.codePrestPrincipalInteger.contains(code
                            .getCodePrestation()));
                    assertTrue(PRCodePrestationSurvivant.isPrestationPrincipale(ctr));
                } else {
                    assertTrue(!PRCodePrestationSurvivantTest.codePrestPrincipalInteger.contains(code
                            .getCodePrestation()));
                    assertTrue(!PRCodePrestationSurvivant.isPrestationPrincipale(ctr));
                }
            } else {
                assertTrue(!PRCodePrestationSurvivant.isPrestationPrincipale(ctr));
            }
        }
    }

    @Test
    public void testMethodeStaticIsPrestationPrincipaleString() {
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum code = PRCodePrestationSurvivant.getCodePrestation(String.valueOf(ctr));
            if (code != null) {
                if (code.isPrestationPrincipale()) {
                    assertTrue(PRCodePrestationSurvivantTest.codePrestPrincipalString.contains(code
                            .getCodePrestationAsString()));
                    assertTrue(PRCodePrestationSurvivant.isPrestationPrincipale(String.valueOf(ctr)));
                } else {
                    assertTrue(!PRCodePrestationSurvivantTest.codePrestPrincipalString.contains(code
                            .getCodePrestationAsString()));
                    assertTrue(!PRCodePrestationSurvivant.isPrestationPrincipale(String.valueOf(ctr)));
                }
            } else {
                assertTrue(!PRCodePrestationSurvivant.isPrestationPrincipale(String.valueOf(ctr)));
            }
        }
    }

    @Test
    public void testMethodeStaticIsCodePrestationSurvivantInteger() {
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum code = PRCodePrestationSurvivant.getCodePrestation(ctr);
            if (code != null) {
                assertTrue(PRCodePrestationSurvivant.isCodePrestationSurvivant(ctr));
                assertTrue(PRCodePrestationSurvivantTest.tousLesCodePrestation.contains(code.getCodePrestation()));
            } else {
                assertTrue(!PRCodePrestationSurvivant.isCodePrestationSurvivant(ctr));
                assertTrue(!PRCodePrestationSurvivantTest.tousLesCodePrestation.contains(ctr));
            }
        }
    }

    @Test
    public void testMethodeStaticIsCodePrestationSurvivantString() {
        for (int ctr = 0; ctr < 300; ctr++) {
            IPRCodePrestationEnum code = PRCodePrestationSurvivant.getCodePrestation(String.valueOf(ctr));
            if (code != null) {
                assertTrue(PRCodePrestationSurvivantTest.codePrestComplementaireString.contains(code
                        .getCodePrestationAsString()));
                assertTrue(PRCodePrestationSurvivant.isCodePrestationSurvivant(String.valueOf(ctr)));
            } else {
                assertTrue(!PRCodePrestationSurvivant.isCodePrestationSurvivant(String.valueOf(ctr)));
                assertTrue(!PRCodePrestationSurvivantTest.codePrestComplementaireString.contains(String.valueOf(ctr)));
            }
        }
    }

    private void assertTrue(boolean exp) {
        Assert.assertTrue(exp);
    }
}
