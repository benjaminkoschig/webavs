package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;

public class CompensationInfoRestiutionTestCase {

    @Test
    public void testAddMontantDispoEricturePeriode() {
        CompensationInfoRestiution infoRestiution = new CompensationInfoRestiution();
        infoRestiution.addMontantDispo(EricturePeriodeFactory.generateEcriturePeriode(200, 150), false);
        Assert.assertEquals(new BigDecimal(200), infoRestiution.getMontantAdisposition());
        infoRestiution.addMontantDispo(EricturePeriodeFactory.generateEcriturePeriode(100, 150), false);
        Assert.assertEquals(new BigDecimal(300), infoRestiution.getMontantAdisposition());
        Assert.assertEquals(new BigDecimal(0), infoRestiution.getMontantAdispositionDom2R());
    }

    @Test
    public void testAddMontantDispoEricturePeriodeDom2R() {
        CompensationInfoRestiution infoRestiution = new CompensationInfoRestiution();
        infoRestiution.addMontantDispo(EricturePeriodeFactory.generateEcriturePeriode(200, 150), true);
        Assert.assertEquals(new BigDecimal(200), infoRestiution.getMontantAdispositionDom2R());
        infoRestiution.addMontantDispo(EricturePeriodeFactory.generateEcriturePeriode(100, 150), true);
        Assert.assertEquals(new BigDecimal(300), infoRestiution.getMontantAdispositionDom2R());
        Assert.assertEquals(new BigDecimal(0), infoRestiution.getMontantAdisposition());
    }

    public void testHasMonneyBothTrue() {
        CompensationInfoRestiution infoRestiution = new CompensationInfoRestiution();
        infoRestiution.addMontantDispo(EricturePeriodeFactory.generateEcriturePeriode(200, 150), false);
        infoRestiution.addMontantDispo(EricturePeriodeFactory.generateEcriturePeriode(200, 150), true);
        Assert.assertTrue(infoRestiution.hasMonney());
    }

    @Test
    public void testHasMonneyDom2RFalse() {
        CompensationInfoRestiution infoRestiution = new CompensationInfoRestiution();
        Assert.assertFalse(infoRestiution.hasMonney());
    }

    @Test
    public void testHasMonneyDom2RTrue() {
        CompensationInfoRestiution infoRestiution = new CompensationInfoRestiution();
        infoRestiution.addMontantDispo(EricturePeriodeFactory.generateEcriturePeriode(200, 150), true);
        Assert.assertTrue(infoRestiution.hasMonney());
    }

    @Test
    public void testHasMonneyNormalFalse() {
        CompensationInfoRestiution infoRestiution = new CompensationInfoRestiution();
        Assert.assertFalse(infoRestiution.hasMonney());
    }

    @Test
    public void testHasMonneyNormalTrue() {
        CompensationInfoRestiution infoRestiution = new CompensationInfoRestiution();
        infoRestiution.addMontantDispo(EricturePeriodeFactory.generateEcriturePeriode(200, 150), false);
        Assert.assertTrue(infoRestiution.hasMonney());
    }

    @Test
    public void testHasMontantAdispositionDom2RTrue() {
        CompensationInfoRestiution infoRestiution = new CompensationInfoRestiution();
        infoRestiution.addMontantDispo(EricturePeriodeFactory.generateEcriturePeriode(200, 150), true);
        Assert.assertFalse(infoRestiution.hasMontantAdisposition());
    }

    @Test
    public void testHasMontantAdispositionFalse() {
        CompensationInfoRestiution infoRestiution = new CompensationInfoRestiution();
        infoRestiution.addMontantDispo(EricturePeriodeFactory.generateEcriturePeriode(200, 150), false);
        infoRestiution.substractMontantAdisposition(new BigDecimal(200));
        Assert.assertFalse(infoRestiution.hasMontantAdisposition());
    }

    @Test
    public void testHasMontantAdispositionTrue() {
        CompensationInfoRestiution infoRestiution = new CompensationInfoRestiution();
        infoRestiution.addMontantDispo(EricturePeriodeFactory.generateEcriturePeriode(200, 150), false);
        Assert.assertTrue(infoRestiution.hasMontantAdisposition());
    }

    @Test
    public void testSubstractMontantAdisposition() {
        CompensationInfoRestiution infoRestiution = new CompensationInfoRestiution();
        infoRestiution.addMontantDispo(EricturePeriodeFactory.generateEcriturePeriode(200, 150), false);
        infoRestiution.substractMontantAdisposition(new BigDecimal(100));
        Assert.assertEquals(new BigDecimal(100), infoRestiution.getMontantAdisposition());
        Assert.assertEquals(new BigDecimal(0), infoRestiution.getMontantAdispositionDom2R());

    }

    @Test
    public void testSubstractMontantAdispositionDom2R() {
        CompensationInfoRestiution infoRestiution = new CompensationInfoRestiution();
        infoRestiution.addMontantDispo(EricturePeriodeFactory.generateEcriturePeriode(200, 150), true);
        infoRestiution.substractMontantAdispositionDom2R(new BigDecimal(100));
        Assert.assertEquals(new BigDecimal(100), infoRestiution.getMontantAdispositionDom2R());
        Assert.assertEquals(new BigDecimal(0), infoRestiution.getMontantAdisposition());

    }

}
