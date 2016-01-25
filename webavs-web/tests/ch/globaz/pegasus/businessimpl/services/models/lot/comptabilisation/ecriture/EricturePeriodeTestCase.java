package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;

public class EricturePeriodeTestCase {

    @Test
    public void testIsBenficaireDom2IsNull() {
        EricturePeriode ericturePeriode = new EricturePeriode();
        Assert.assertFalse(ericturePeriode.isBenficaireDom2R());
    }

    @Test
    public void testIsBenficaireDom2R() {
        EricturePeriode ericturePeriode = EricturePeriodeFactory.generateEcriturePeriode();
        Assert.assertFalse(ericturePeriode.isBenficaireDom2R());
    }

    @Test
    public void testIsBenficaireDom2RTrue() {
        EricturePeriode ericturePeriode = EricturePeriodeFactory.generateEcriturePeriode();
        ericturePeriode.getBeneficiaire().getOrdreVersement().setIdTiersAdressePaiementConjoint("1");
        Assert.assertTrue(ericturePeriode.isBenficaireDom2R());
    }

    @Test
    public void testResolveMontantMinBeneficiareGrand() {
        EricturePeriode ericturePeriode = EricturePeriodeFactory.generateEcriturePeriode(200, 100);
        Assert.assertEquals(new BigDecimal(100), ericturePeriode.resolveMontantMin());
    }

    @Test
    public void testResolveMontantMinBeneficiarePetiti() {
        EricturePeriode ericturePeriode = EricturePeriodeFactory.generateEcriturePeriode(100, 100);
        Assert.assertEquals(new BigDecimal(100), ericturePeriode.resolveMontantMin());
    }

    @Test
    public void testResolveMontantMinEquals() {
        EricturePeriode ericturePeriode = EricturePeriodeFactory.generateEcriturePeriode(200, 200);
        Assert.assertEquals(new BigDecimal(200), ericturePeriode.resolveMontantMin());
    }

}
