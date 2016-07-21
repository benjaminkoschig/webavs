package ch.globaz.orion.business.domaine.pucs;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;

public class EmployeeTest {

    @Test
    public void testResolveAf() throws Exception {
        Employee e = buildEmployee();
        List<SalaryCaf> salariesCaf = new ArrayList<SalaryCaf>();
        SalaryAvs salaryAvs = buildSalarayAvs(1, "2015-01-01", "2015-05-31");
        SalaryCaf caf = buildSalarayCaf(1, "2015-01-01", "2015-05-31");
        salariesCaf.add(caf);
        salariesCaf.add(buildSalarayCaf(1, "2015-06-01", "2015-08-31"));
        e.setSalariesCaf(new SalariesCaf(salariesCaf));
        assertEquals(caf, e.resolveAf(salaryAvs));
    }

    @Test
    public void testResolveAfNotFound() throws Exception {

        SalaryAvs salaryAvs = buildSalarayAvs(1, "2015-01-01", "2015-05-31");

        List<SalaryCaf> salariesCaf = new ArrayList<SalaryCaf>();
        salariesCaf.add(buildSalarayCaf(1, "2015-02-01", "2015-05-31"));
        salariesCaf.add(buildSalarayCaf(1, "2015-06-01", "2015-08-31"));

        Employee e = buildEmployee();
        e.setSalariesCaf(new SalariesCaf(salariesCaf));

        assertNull(e.resolveAf(salaryAvs));
    }

    private PeriodeSalary buildPeriode(String dateDebut, String dateFin) {
        return new PeriodeSalary.PeriodeSalaryBuilder().dateDebut(dateDebut).dateFin(dateFin).build();
    }

    private SalaryCaf buildSalarayCaf(int montant, String dateDebut, String dateFin) {
        return new SalaryCaf.SalaryCafBuilder().canton("canton").periode(buildPeriode(dateDebut, dateFin))
                .montant(new Montant(montant)).build();
    }

    private SalaryAvs buildSalarayAvs(int montant, String dateDebut, String dateFin) {
        return new SalaryAvs.SalaryAvsBuilder().montantAc1(new Montant(montant / 2))
                .montantAc2(new Montant(montant / 10)).montantAvs(new Montant(montant))
                .periode(buildPeriode(dateDebut, dateFin)).build();
    }

    private Employee buildEmployee() {
        Employee e = new Employee();
        e.setDateNaissance(new Date());
        e.setNom("nom");
        e.setPrenom("prenom");
        e.setSexe("sexe");
        e.setWorkPlaceCanton("canton");
        return e;
    }
}
