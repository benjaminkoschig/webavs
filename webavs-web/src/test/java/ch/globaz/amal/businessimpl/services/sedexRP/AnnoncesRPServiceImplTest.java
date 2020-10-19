package ch.globaz.amal.businessimpl.services.sedexRP;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class AnnoncesRPServiceImplTest extends TestCase {

    @Test
    public void testEstDansLaLimite_AucuneBorne() {
        String dateDecision = "01.01.2020";
        Assert.assertTrue(AnnoncesRPServiceImpl.estDansLaLimite(dateDecision, null, null));
    }

    @Test
    public void testEstDansLaLimite_BorneDebut() {
        String dateDecision = "01.01.2020";
        String dateDebut = "01.01.2020";
        Assert.assertTrue(AnnoncesRPServiceImpl.estDansLaLimite(dateDecision, dateDebut, null));

        dateDebut = "31.12.2019";
        Assert.assertTrue(AnnoncesRPServiceImpl.estDansLaLimite(dateDecision, dateDebut, null));

        dateDebut = "02.01.2020";
        Assert.assertFalse(AnnoncesRPServiceImpl.estDansLaLimite(dateDecision, dateDebut, null));
    }

    @Test
    public void testEstDansLaLimite_BorneFin() {
        String dateDecision = "01.01.2020";
        String dateFin = "01.01.2020";
        Assert.assertTrue(AnnoncesRPServiceImpl.estDansLaLimite(dateDecision, null, dateFin));

        dateFin = "31.12.2019";
        Assert.assertFalse(AnnoncesRPServiceImpl.estDansLaLimite(dateDecision, null, dateFin));

        dateFin = "02.01.2020";
        Assert.assertTrue(AnnoncesRPServiceImpl.estDansLaLimite(dateDecision, null, dateFin));
    }

    @Test
    public void testEstDansLaLimite_BorneDebut_BorneFin() {
        String dateDecision = "01.01.2020";
        String dateDebut = "01.01.2020";
        String dateFin = "01.01.2020";
        Assert.assertTrue(AnnoncesRPServiceImpl.estDansLaLimite(dateDecision, dateDebut, dateFin));

        dateDebut = "31.12.2019";
        dateFin = "01.01.2020";
        Assert.assertTrue(AnnoncesRPServiceImpl.estDansLaLimite(dateDecision, dateDebut, dateFin));

        dateDebut = "31.12.2019";
        dateFin = "02.01.2020";
        Assert.assertTrue(AnnoncesRPServiceImpl.estDansLaLimite(dateDecision, dateDebut, dateFin));

        dateDebut = "01.01.2020";
        dateFin = "02.01.2020";
        Assert.assertTrue(AnnoncesRPServiceImpl.estDansLaLimite(dateDecision, dateDebut, dateFin));

        dateDebut = "02.01.2020";
        dateFin = "02.01.2020";
        Assert.assertFalse(AnnoncesRPServiceImpl.estDansLaLimite(dateDecision, dateDebut, dateFin));

        dateDebut = "30.12.2019";
        dateFin = "31.12.2019";
        Assert.assertFalse(AnnoncesRPServiceImpl.estDansLaLimite(dateDecision, dateDebut, dateFin));
    }
}