package ch.globaz.pegasus.business.models.home;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TypeChambreTest {

    TypeChambre tchambre;

    @Before
    public void init() {
        tchambre = new TypeChambre();
    }

    @Test
    public void testGetTiersDesignation() {
        tchambre.getPersonneEtendue().getTiers().setDesignation1("S�bastien");
        tchambre.getPersonneEtendue().getTiers().setDesignation2("Ch�vre");
        tchambre.getSimpleTypeChambre().setIsParticularite(Boolean.TRUE);
        Assert.assertTrue(tchambre.getTierDesignation().equals("S�bastien, Ch�vre"));
    }

    @Test
    public void testGetTiersDesignationNullSimpleTypeChambre() {
        Assert.assertTrue(null == tchambre.getTierDesignation());
    }

    @Test
    public void testGetTiersDesignationNullIsParticularite() {
        Assert.assertTrue(null == tchambre.getTierDesignation());
    }

    @Test
    public void testGetTiersDesignationNullDesignation1() {
        tchambre.getSimpleTypeChambre().setIsParticularite(Boolean.TRUE);
        Assert.assertTrue(null == tchambre.getTierDesignation());
    }

    @Test
    public void testDesignationTypeChambre() {
        tchambre.getPersonneEtendue().getTiers().setDesignation1("S�bastien");
        tchambre.getPersonneEtendue().getTiers().setDesignation2("Ch�vre");
        tchambre.getSimpleTypeChambre().setDesignation("Home test");
        tchambre.getSimpleTypeChambre().setIsParticularite(Boolean.TRUE);
        Assert.assertTrue(tchambre.getDesignationTypeChambre().equals("Home test - S�bastien, Ch�vre"));
    }

    @Test
    public void testDesignationTypeChambreNullDesignation() {
        tchambre.getPersonneEtendue().getTiers().setDesignation1("S�bastien");
        tchambre.getPersonneEtendue().getTiers().setDesignation2("Ch�vre");
        tchambre.getSimpleTypeChambre().setIsParticularite(Boolean.TRUE);
        Assert.assertTrue(tchambre.getDesignationTypeChambre().equals(
                "[TypeChambre designation not found] - S�bastien, Ch�vre"));
    }

    @Test
    public void testDesignationTypeChambreNullDesignation1And2AndDesignation() {
        tchambre.getSimpleTypeChambre().setIsParticularite(Boolean.TRUE);
        Assert.assertTrue(tchambre.getDesignationTypeChambre().equals("[TypeChambre designation not found]"));
    }

    @Test
    public void testDesignationTypeChambreNullDesignation1And2And() {
        tchambre.getSimpleTypeChambre().setDesignation("Home test");
        tchambre.getSimpleTypeChambre().setIsParticularite(Boolean.TRUE);
        Assert.assertTrue(tchambre.getDesignationTypeChambre().equals("Home test"));
    }

}
