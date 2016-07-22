package ch.globaz.orion.business.domaine.pucs;

import org.junit.Test;

public class PeriodeSalaryTest {

    @Test
    public void testCompareTo() throws Exception {

        PeriodeSalary periode1 = new PeriodeSalary.PeriodeSalaryBuilder().dateDebut("2015-01-01").dateFin("2015-12-31")
                .build();
        PeriodeSalary periode2 = new PeriodeSalary.PeriodeSalaryBuilder().dateDebut("2015-01-01").dateFin("2015-12-31")
                .build();

        // asseperiode1.compareTo(periode2)

    }

}