package globaz.ij.helpers.acor;

import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.prestations.IJFpiCalculee;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.helpers.prononces.IJCorrigeDepuisPrononceTest;
import globaz.ij.helpers.prononces.IJCorrigerDepuisPrononce;
import junit.framework.Assert;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class IJCalculFpiStandardTest {

    /**
     * Test CalculMontantsPrestation
     */
    @Test
    public void testCalcul1MontantsPrestationOk() {
            Assert.assertEquals("500.00", calcul("01.02.2021","28.02.2021","0","16.70","0","500"));
    }

    @Test
    public void testCalcul2MontantsPrestationOk() {
        Assert.assertEquals("232.80", calcul("01.02.2021","14.02.2021","0","16.70","0","500"));
    }

    @Test
    public void testCalcul3MontantsPrestationOk() {
        Assert.assertEquals("15.70", calcul("28.02.2021","28.02.2021","0","16.70","0","500"));
    }

    @Test
    public void testCalcul4MontantsPrestationOk() {
        Assert.assertEquals("166.00", calcul("05.02.2021","14.02.2021","0","16.70","0","500"));
    }

    @Test
    public void testCalcul5MontantsPrestationOk() {
        Assert.assertEquals("99.20", calcul("05.02.2024","28.02.2024","18","16.70","0","500"));
    }

    @Test
    public void testCalcul6MontantsPrestationOk() {
        Assert.assertEquals("258.00", calcul("05.02.2021","14.02.2021","0","16.70","9.2","500"));
    }

    @Test
    public void testCalcul7MontantsPrestationOk() {
        Assert.assertEquals("198.40", calcul("05.02.2024","28.02.2024","18","33.40","0","1000"));
    }

    @Test
    public void testCalcul8MontantsPrestationOk() {
        Assert.assertEquals("154.40", calcul("05.02.2021","14.02.2021","4","16.70","9.2","500"));
    }

    // Test FPV
    @Test
    public void testCalcul10aMontantsPrestationOk() {
        Assert.assertEquals("266.20", calcul("15.07.2022","31.07.2022","0","16.70","0","500"));
    }

    @Test
    public void testCalcul10bMontantsPrestationOk() {
        Assert.assertEquals("249.50", calcul("01.08.2022","15.08.2022","0","16.70","0","500"));
    }

    @Test
    public void testCalcul10cMontantsPrestationOk() {
        Assert.assertEquals("1194.50", calcul("16.08.2022","31.08.2022","0","79.70","0","2390"));
    }


    @Test
    public void testCalcul11aMontantsPrestationOk() {
        Assert.assertEquals("15.70", calcul("15.08.2022","15.08.2022","0","16.70","0","500"));
    }


    @Test
    public void testCalcul12aMontantsPrestationOk() {
        Assert.assertEquals("266.20", calcul("15.07.2022","31.07.2022","0","16.70","0","500"));
    }
//
    @Test
    public void testCalcul12bMontantsPrestationOk() {
        Assert.assertEquals("15.70", calcul("01.08.2022","01.08.2022","0","16.70","0","500"));
    }

    @Test
    public void testCalcul12cMontantsPrestationOk() {
        Assert.assertEquals("2310.30", calcul("02.08.2022","31.08.2022","0","79.70","0","2390"));
    }



    @Test
    public void testCalcul13MontantsPrestationOk() {
        Assert.assertEquals("1274.20", calcul("15.08.2022","31.08.2022","0","79.70","0","2390"));
    }



    @Test
    public void testCalcul14aMontantsPrestationOk() {
        Assert.assertEquals("266.20", calcul("15.07.2022","31.07.2022","0","16.70","0","500"));
    }

    @Test
    public void testCalcul14bMontantsPrestationOk() {
        Assert.assertEquals("500.00", calcul("01.08.2022","31.08.2022","0","16.70","0","500"));
    }

    @Test
    public void testCalcul14cMontantsPrestationOk() {
        Assert.assertEquals("2390.00", calcul("01.09.2022","30.09.2022","0","79.70","0","2390"));
    }




    @Test
    public void testCalcul15aMontantsPrestationOk() {
        Assert.assertEquals("266.20", calcul("15.08.2022","31.08.2022","0","16.70","0","500"));
    }

    @Test
    public void testCalcul15bMontantsPrestationOk() {
        Assert.assertEquals("2390.00", calcul("01.09.2022","30.09.2022","0","79.70","0","2390"));
    }



    class TestCalcul {
        IJBaseIndemnisation baseIndemnisation;
        IJFpiCalculee ijFpiCalculee;

        public TestCalcul (String dateDebut, String dateFin, String joursNonCouvert, String montantBase, String montantEnfant, String salaireMensuel) {
            baseIndemnisation = new IJBaseIndemnisation();
            baseIndemnisation.setDateDebutPeriode(dateDebut);
            baseIndemnisation.setDateFinPeriode(dateFin);
            baseIndemnisation.setNombreJoursNonCouverts(joursNonCouvert);

            ijFpiCalculee = new IJFpiCalculee();
            ijFpiCalculee.setMontantBase(montantBase);
            ijFpiCalculee.setMontantEnfants(montantEnfant);
            ijFpiCalculee.setSalaireMensuel(salaireMensuel);
        }
    }

    private String calcul(String dateDebut, String dateFin, String joursNonCouvert, String montantBase, String montantEnfant, String salaireMensuel) {
        TestCalcul testCaalcul = new TestCalcul(dateDebut, dateFin, joursNonCouvert, montantBase, montantEnfant, salaireMensuel);
        IJPrestation prestation = new IJPrestation();
        try {
            IJCalculFpiStandard.calculMontantsPrestation(testCaalcul.baseIndemnisation, testCaalcul.ijFpiCalculee, prestation);
        } catch (IJCalculException e) {
            e.printStackTrace();
        }
        return prestation.getMontantBrut();
    }

}
