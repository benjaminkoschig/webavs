package ch.globaz.pegasus.businessimpl.services.models;

import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.pegasus.businessimpl.services.models.decision.validation.ValiderAcTestDeRecette;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.UtilsLotForTestRetroAndOV;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.InitTestForBd;

public class TestDeRecetteValidationComptablication extends InitTestForBd {

    // @Test
    // public void testAll() throws Exception {
    // ValiderAcTestDeRecette test = new ValiderAcTestDeRecette();
    // test.testCasSimpleMultipleCorrectionDroit();
    // // test.testCoupleSepare();
    // // test.testDom2R();
    // // test.testDom2RToSepMalRequerantHomeDeuxOctrois();
    // // test.testDomToDom2R();
    // // test.testDom2RToDom();
    // // test.testAvsAi();
    // UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    // // System.out.println(ComptabilisationProcess.displayTime() + "\r");
    // // 0072.9735.18 avec dette
    // }
    // @Test
    // public void testAvsAi() throws Exception {
    // ValiderAcTestDeRecette test = new ValiderAcTestDeRecette();
    // test.testAvsAi();
    // UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    // }

    // @Test
    // public void testCasSimpleAvecCreancierAuxConjoint() throws Exception {
    // ValiderAcTestDeRecette test = new ValiderAcTestDeRecette();
    // test.testCasSimpleAvecCreancierAuxConjoint();
    // UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    // }

    //
    // @Test
    // public void testCasSimpleAvecCreancierAuxDeux() throws Exception {
    // ValiderAcTestDeRecette test = new ValiderAcTestDeRecette();
    // test.testCasSimpleAvecCreancierAuxDeux();
    // UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    // }
    //
    // @Test
    // public void testCasSimpleAvecCreancierAuxRequerant() throws Exception {
    // ValiderAcTestDeRecette test = new ValiderAcTestDeRecette();
    // test.testCasSimpleAvecCreancierAuxRequerant();
    // UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    // }
    //
    // @Test
    // public void testCasSimpleMultipleCorrectionDroit() throws Exception {
    // ValiderAcTestDeRecette test = new ValiderAcTestDeRecette();
    // test.testCasSimpleMultipleCorrectionDroit();
    // UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    // }
    //
    // @Test
    public void testCoupleSepare() throws Exception {
        ValiderAcTestDeRecette test = new ValiderAcTestDeRecette();
        test.testCoupleSepare();
        UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    }

    //
    // @Test
    // public void testDom2R() throws Exception {
    // ValiderAcTestDeRecette test = new ValiderAcTestDeRecette();
    // test.testDom2R();
    // UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    // }
    //
    // @Test
    // public void testDom2RToDom() throws Exception {
    // ValiderAcTestDeRecette test = new ValiderAcTestDeRecette();
    // test.testDom2RToDom();
    // UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    // }
    //
    // @Test
    public void testDom2RToSepMalConjointHomeDeuxOctrois() throws Exception {
        ValiderAcTestDeRecette test = new ValiderAcTestDeRecette();
        test.testDom2RToSepMalConjointHomeDeuxOctrois();
        UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    }

    //
    // @Test
    // public void testDom2RToSepMalConjointHomeRefusRequerant() throws Exception {
    // ValiderAcTestDeRecette test = new ValiderAcTestDeRecette();
    // test.testDom2RToSepMalConjointHomeRefusRequerant();
    // UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    // }
    //
    // @Test
    // public void testDom2RToSepMalRequerantHomeDeuxOctrois() throws Exception {
    // ValiderAcTestDeRecette test = new ValiderAcTestDeRecette();
    // test.testDom2RToSepMalRequerantHomeDeuxOctrois();
    // UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    // }
    //
    // @Test
    // public void testDom2RToSepMalRequerantHomeRefusConjoint() throws Exception {
    // ValiderAcTestDeRecette test = new ValiderAcTestDeRecette();
    // test.testDom2RToSepMalRequerantHomeRefusConjoint();
    // UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    // }
    //
    // @Test
    // public void testDomToDom2R() throws Exception {
    // ValiderAcTestDeRecette test = new ValiderAcTestDeRecette();
    // test.testDomToDom2R();
    // UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    // }
    //
    @Ignore
    @Test
    public void testSepMalToDom2RConjointEnHomeRequerantRefus() throws Exception {
        ValiderAcTestDeRecette test = new ValiderAcTestDeRecette();
        test.testSepMalToDom2RConjointEnHomeRequerantRefus();
        UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    }

    //
    // @Test
    // public void testSepMalToDom2RConjointHomeOctrois() throws Exception {
    // ValiderAcTestDeRecette test = new ValiderAcTestDeRecette();
    // test.testSepMalToDom2RConjointHomeOctrois();
    // UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    // }
    //
    // @Test
    // public void testSepMalToDom2RRequerantHomeConjointRefus() throws Exception {
    // ValiderAcTestDeRecette test = new ValiderAcTestDeRecette();
    // test.testSepMalToDom2RRequerantHomeRefus();
    // UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    // }
    //
    // @Test
    // public void testSepMalToDom2RRequerantHomeOctrois() throws Exception {
    // ValiderAcTestDeRecette test = new ValiderAcTestDeRecette();
    // test.testSepMalToDom2RRequerantHomeOctrois();
    // UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    // }

    // @Test
    public void testSepMalToDom2RRequerantHomeOctrois() throws Exception {
        ValiderAcTestDeRecette test = new ValiderAcTestDeRecette();
        test.testVersionInitialePcaEnRefus();
        UtilsLotForTestRetroAndOV.comptabliserCurrentLot();
    }
}
