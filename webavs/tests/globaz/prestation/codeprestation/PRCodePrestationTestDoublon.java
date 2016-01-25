package globaz.prestation.codeprestation;

import globaz.corvus.TestUtils;
import globaz.prestation.enums.codeprestation.PRCodePrestationPC;
import globaz.prestation.enums.codeprestation.PRCodePrestationRFM;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationAPI;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationInvalidite;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationSurvivant;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationVieillesse;
import org.junit.Test;

/**
 * Le but de cette classe de test est de s'assurer qu'il n'y ai pas 2 même code prestation (ex : 22) déclaré dans 2
 * IPRCodePrestationEnum
 * 
 * @author lga
 */
public class PRCodePrestationTestDoublon {

    @Test
    public void testVieillesse() {
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationVieillesse.values(),
                PRCodePrestationInvalidite.values());
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationVieillesse.values(),
                PRCodePrestationSurvivant.values());
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationVieillesse.values(), PRCodePrestationAPI.values());
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationVieillesse.values(), PRCodePrestationPC.values());
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationVieillesse.values(), PRCodePrestationRFM.values());
    }

    @Test
    public void testSurvivant() {
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationSurvivant.values(),
                PRCodePrestationVieillesse.values());
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationSurvivant.values(),
                PRCodePrestationInvalidite.values());
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationSurvivant.values(), PRCodePrestationAPI.values());
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationSurvivant.values(), PRCodePrestationPC.values());
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationSurvivant.values(), PRCodePrestationRFM.values());
    }

    @Test
    public void testInvalidite() {
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationInvalidite.values(),
                PRCodePrestationVieillesse.values());
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationInvalidite.values(),
                PRCodePrestationSurvivant.values());
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationInvalidite.values(), PRCodePrestationAPI.values());
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationInvalidite.values(), PRCodePrestationPC.values());
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationInvalidite.values(), PRCodePrestationRFM.values());
    }

    @Test
    public void testAPI() {
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationAPI.values(), PRCodePrestationInvalidite.values());
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationAPI.values(), PRCodePrestationSurvivant.values());
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationAPI.values(), PRCodePrestationVieillesse.values());
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationAPI.values(), PRCodePrestationPC.values());
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationAPI.values(), PRCodePrestationRFM.values());
    }

    @Test
    public void testRFM() {
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationRFM.values(), PRCodePrestationInvalidite.values());
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationRFM.values(), PRCodePrestationSurvivant.values());
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationRFM.values(), PRCodePrestationVieillesse.values());
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationRFM.values(), PRCodePrestationPC.values());
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationRFM.values(), PRCodePrestationAPI.values());
    }

    @Test
    public void testPC() {
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationPC.values(), PRCodePrestationInvalidite.values());
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationPC.values(), PRCodePrestationSurvivant.values());
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationPC.values(), PRCodePrestationVieillesse.values());
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationPC.values(), PRCodePrestationAPI.values());
        TestUtils.compareTwoIPRCodePrestationEnum(PRCodePrestationPC.values(), PRCodePrestationRFM.values());
    }

}
