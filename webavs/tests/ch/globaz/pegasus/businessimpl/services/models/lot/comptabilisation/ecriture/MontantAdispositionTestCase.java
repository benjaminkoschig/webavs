package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;

public class MontantAdispositionTestCase {

    @Test
    public void testAddMontantDispo() {
        MontantAdisposition<Object> montantAdisposition = new MontantAdisposition<Object>();
        montantAdisposition.addMontantDispo(new BigDecimal(10));
        Assert.assertEquals(new BigDecimal(10), montantAdisposition.getMontantAdisposition());
    }

    @Test
    public void testAddMontantDispoDom2R() {
        MontantAdisposition<Object> montantAdisposition = new MontantAdisposition<Object>();
        montantAdisposition.addMontantDispoDom2R(new BigDecimal(10));
        Assert.assertEquals(new BigDecimal(10), montantAdisposition.getMontantAdispositionDom2R());
    }

    @Test
    public void testGetTotalMontantAdisposition() {
        MontantAdisposition<Object> montantAdisposition = new MontantAdisposition<Object>();
        montantAdisposition.addMontantDispo(new BigDecimal(10));
        montantAdisposition.addMontantDispoDom2R(new BigDecimal(20));
        Assert.assertEquals(new BigDecimal(30), montantAdisposition.getTotalMontantAdisposition());
    }

    @Test
    public void testHasMonneyFalse() {
        MontantAdisposition<Object> montantAdisposition = new MontantAdisposition<Object>();
        montantAdisposition.addMontantDispo(new BigDecimal(0));
        montantAdisposition.addMontantDispoDom2R(new BigDecimal(0));
        Assert.assertFalse(montantAdisposition.hasMonney());
    }

    @Test
    public void testHasMonneyTrue() {
        MontantAdisposition<Object> montantAdisposition = new MontantAdisposition<Object>();
        montantAdisposition.addMontantDispo(new BigDecimal(10));
        montantAdisposition.addMontantDispoDom2R(new BigDecimal(20));
        Assert.assertTrue(montantAdisposition.hasMonney());
    }

    @Test
    public void testHasMonneyTrue1() {
        MontantAdisposition<Object> montantAdisposition = new MontantAdisposition<Object>();
        montantAdisposition.addMontantDispo(new BigDecimal(10));
        Assert.assertTrue(montantAdisposition.hasMonney());
    }

    @Test
    public void testHasMontantAdisposition() {
        MontantAdisposition<Object> montantAdisposition = new MontantAdisposition<Object>();
        montantAdisposition.addMontantDispo(new BigDecimal(10));
        Assert.assertTrue(montantAdisposition.hasMonney());
    }

    @Test
    public void testHasMontantAdispositionDom2R() {
        MontantAdisposition<Object> montantAdisposition = new MontantAdisposition<Object>();
        montantAdisposition.addMontantDispoDom2R(new BigDecimal(20));
        Assert.assertTrue(montantAdisposition.hasMonney());
    }

    @Test
    public void testSubstractMontantAdisposition() {
        MontantAdisposition<Object> montantAdisposition = new MontantAdisposition<Object>();
        montantAdisposition.addMontantDispo(new BigDecimal(10));
        montantAdisposition.substractMontantAdisposition(new BigDecimal(5));
        Assert.assertEquals(new BigDecimal(5), montantAdisposition.getMontantAdisposition());
    }

    @Test
    public void testSubstractMontantAdispositionDom2R() {
        MontantAdisposition<Object> montantAdisposition = new MontantAdisposition<Object>();
        montantAdisposition.addMontantDispoDom2R(new BigDecimal(10));
        montantAdisposition.substractMontantAdispositionDom2R(new BigDecimal(5));
        Assert.assertEquals(new BigDecimal(5), montantAdisposition.getMontantAdispositionDom2R());
    }

}
