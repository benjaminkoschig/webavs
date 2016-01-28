package ch.globaz.orion.business.domaine.pucs;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.Ignore;
import org.junit.Test;

public class DeclarationSalaireTest {

    @Ignore
    @Test
    public void testResolveDistinctContantEmployees() throws Exception {
        DeclarationSalaire declarationSalaire = new DeclarationSalaire();
        List<Employee> employees = new ArrayList<Employee>();
        for (int i = 0; i < 10; i++) {
            Employee employee = new Employee();
            employee.setWorkPlaceCanton("FR");
            employees.add(employee);
        }

        for (int i = 0; i < 10; i++) {
            Employee employee = new Employee();
            employee.setWorkPlaceCanton("NE");
            employees.add(employee);
        }

        declarationSalaire.setEmployees(employees);
        Set<String> cantons = declarationSalaire.resolveDistinctContant();
        assertEquals(2, cantons.size());
        String s = "";
        for (String canton : cantons) {
            s = s + canton;
        }
        assertEquals("FRNE", s);

    }

}
