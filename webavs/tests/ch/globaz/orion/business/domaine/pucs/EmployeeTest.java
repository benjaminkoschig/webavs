package ch.globaz.orion.business.domaine.pucs;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Periode;

public class EmployeeTest {

    @Test
    public void testResolveAf() throws Exception {
        Employee e = buildEmployee();
        List<SalaryCaf> salaryCaf = new ArrayList<SalaryCaf>();
        SalaryAvs salaryAvs = buildSalarayAvs(1, "01.01.2015", "31.5.2015");
        SalaryCaf caf = buildSalarayCaf(1, "01.01.2015", "31.5.2015");
        salaryCaf.add(caf);
        salaryCaf.add(buildSalarayCaf(1, "01.06.2015", "31.8.2015"));
        e.setSalariesCaf(new SalariesCaf(salaryCaf));
        assertEquals(caf, e.resolveAf(salaryAvs));
    }

    @Test
    public void testResolveAfNotFound() throws Exception {
        Employee e = buildEmployee();
        List<SalaryCaf> salaryCaf = new ArrayList<SalaryCaf>();
        SalaryAvs salaryAvs = buildSalarayAvs(1, "01.01.2015", "31.5.2015");
        SalaryCaf caf = buildSalarayCaf(1, "01.02.2015", "31.5.2015");
        salaryCaf.add(caf);
        salaryCaf.add(buildSalarayCaf(1, "01.06.2015", "31.8.2015"));
        e.setSalariesCaf(new SalariesCaf(salaryCaf));
        assertNull(e.resolveAf(salaryAvs).getCanton());
    }

    private SalaryCaf buildSalarayCaf(int montant, String dateDebut, String dateFin) {
        SalaryCaf s = new SalaryCaf();
        s.setMontant(new Montant(montant));
        s.setPeriode(new Periode(dateDebut, dateFin));
        s.setCanton("canton");
        return s;
    }

    private SalaryAvs buildSalarayAvs(int montant, String dateDebut, String dateFin) {
        SalaryAvs s = new SalaryAvs();
        s.setMontantAc1(new Montant(montant / 2));
        s.setMontantAc2(new Montant(montant / 10));
        s.setMontantAvs(new Montant(montant));
        s.setPeriode(new Periode(dateDebut, dateFin));
        return s;
    }

    private Employee buildEmployee() {
        Employee e = new Employee();
        e.setDateNaissance(new Date());
        e.setNom("nom");
        e.setPrenom("prnome");
        e.setSexe("sexe");
        e.setWorkPlaceCanton("canton");
        return e;
    }

}
