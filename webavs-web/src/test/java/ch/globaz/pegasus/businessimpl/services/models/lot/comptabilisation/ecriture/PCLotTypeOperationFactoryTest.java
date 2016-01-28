package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.corvus.api.lots.IRELot;
import junit.framework.Assert;
import org.junit.Test;

public class PCLotTypeOperationFactoryTest {

    @Test
    public void testStandardCase() {

        String csTypeDeblocage = IRELot.CS_TYP_LOT_DEBLOCAGE_RA;
        String csTypeDecision = IRELot.CS_TYP_LOT_DECISION;

        PCLotTypeOperationFactory operationDelocage = PCLotTypeOperationFactory.csTypeOf(csTypeDeblocage);
        PCLotTypeOperationFactory operationDecision = PCLotTypeOperationFactory.csTypeOf(csTypeDecision);

        Assert.assertNotNull(operationDelocage);
        Assert.assertNotNull(operationDecision);

        Assert.assertEquals(operationDelocage, PCLotTypeOperationFactory.DEBLOCAGE);
        Assert.assertEquals(operationDecision, PCLotTypeOperationFactory.DECISION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullParameter() {
        String csTypeDeblocage = null;
        PCLotTypeOperationFactory operationDelocage = PCLotTypeOperationFactory.csTypeOf(csTypeDeblocage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidParameter() {
        String csTypeDeblocage = "213213123";
        PCLotTypeOperationFactory operationDelocage = PCLotTypeOperationFactory.csTypeOf(csTypeDeblocage);
    }

    @Test
    public void testNullParameterMsg() {
        String expectedMessage = "The csTypeLot to match the enum cannot be null [TypeOperation]";
        String csTypeDeblocage = null;
        try {
            PCLotTypeOperationFactory operationDelocage = PCLotTypeOperationFactory.csTypeOf(csTypeDeblocage);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testIllegalParameterMsg() {
        String expectedMessage = "The csTypeLot to match the enum is not present: [] [TypeOperation]";
        String csTypeDeblocage = "23412";

        try {
            PCLotTypeOperationFactory operationDelocage = PCLotTypeOperationFactory.csTypeOf(csTypeDeblocage);
        } catch (IllegalArgumentException e) {
            StringBuilder str = new StringBuilder(expectedMessage);
            str.insert(49, csTypeDeblocage);

            Assert.assertEquals(str.toString(), e.getMessage());
        }
    }

    @Test
    public void testRetourInterfaceImplemantation() {
        Assert.assertTrue(PCLotTypeOperationFactory.DEBLOCAGE.getTreatImplementation() instanceof GenerateOperationsBlocage);
        Assert.assertTrue(PCLotTypeOperationFactory.DEBLOCAGE.getTreatImplementation() instanceof GenerateOperations);
        Assert.assertTrue(PCLotTypeOperationFactory.DECISION.getTreatImplementation() instanceof GenerateOperationsApresCalcul);
        Assert.assertTrue(PCLotTypeOperationFactory.DECISION.getTreatImplementation() instanceof GenerateOperations);

    }

}
