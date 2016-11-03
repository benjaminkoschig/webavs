package ch.globaz.orion.businessimpl.services.pucs;

import static org.junit.Assert.*;
import globaz.globall.format.IFormatData;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Test;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaire;
import ch.globaz.orion.business.domaine.pucs.Employee;
import ch.globaz.orion.business.domaine.pucs.PeriodeSalary;
import ch.globaz.orion.business.domaine.pucs.SalariesAvs;
import ch.globaz.orion.business.domaine.pucs.SalariesCaf;
import ch.globaz.orion.business.domaine.pucs.SalaryAvs;
import ch.globaz.orion.business.domaine.pucs.SalaryCaf;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.core.Details;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;

public class DeclarationSalaireBuilderTest {

    @Test
    public void testBuild() throws Exception {

        DeclarationSalaire ds = DeclarationSalaireBuilder.build(
                "src/test/resources/ch/globaz/orion/businessimpl/services/pucs/pucs1.xml", buildFormater());
        assertEquals(new Integer(2012), ds.getAnnee());
        assertEquals(null, ds.getNbSalaire());
        assertEquals(0, null);
        assertEquals(new Montant(208067.2), ds.getMontantAc1());
        assertEquals(new Montant(11784), ds.getMontantAc2());
        assertEquals(new Montant(280851.2), ds.getMontantAvs());
        assertEquals(new Montant(280851.20), ds.getMontantCaf());
        assertEquals(buildDate("2013-01-02"), ds.getTransmissionDate());
        assertEquals(buildDate("2013-01-02"), ds.getTransmissionDate());
        assertEquals("072.631-00", ds.getNumeroAffilie());
        assertEquals("JLG SARL", ds.getNom());
        assertEquals(4, ds.getEmployees().size());
        Employee employee = ds.getEmployees().get(0);
        assertEquals("MORALES", employee.getNom());
        assertEquals("JEAN-BERNARD", employee.getPrenom());
        assertEquals("7565177696249", employee.getNss());
        assertEquals("M", employee.getSexe());
        assertEquals(buildDate("1947-10-23"), employee.getDateNaissance());
        assertEquals(2, employee.getSalariesAvs().size());
        SalaryAvs salaryAvs = employee.getSalariesAvs().get(0);
        assertEquals(new Montant(81000), salaryAvs.getMontantAc1());
        assertEquals(new Montant(0), salaryAvs.getMontantAc2());
        assertEquals(new Montant(81000), salaryAvs.getMontantAvs());
        assertEquals(buildPeriode("2012-02-01", "2012-10-30"), salaryAvs.getPeriode());
        assertFalse(ds.isAfSeul());
        assertFalse(ds.isTest());
    }

    private IFormatData buildFormater() {
        IFormatData dataFormater = new IFormatData() {
            @Override
            public String check(Object value) throws Exception {
                return (String) value;
            }

            @Override
            public String format(String value) throws Exception {
                return value;
            }

            @Override
            public String unformat(String value) throws Exception {
                return value;
            }

        };

        return dataFormater;
    }

    @Test
    public void testBuildAfSeul() throws Exception {
        DeclarationSalaire ds = DeclarationSalaireBuilder.build(
                "src/test/resources/ch/globaz/orion/businessimpl/services/pucs/124.1195-AF_SEULE.xml", buildFormater());
        assertEquals(new Integer(2014), ds.getAnnee());
        assertEquals(new Integer(1), ds.getNbSalaire());
        assertEquals(new Montant(0), ds.getMontantAc1());
        assertEquals(new Montant(0), ds.getMontantAc2());
        assertEquals(new Montant(0), ds.getMontantAvs());
        assertEquals(new Montant(32500.00), ds.getMontantCaf());
        assertEquals(buildDate("2014-10-02"), ds.getTransmissionDate());
        assertEquals("124.1195", ds.getNumeroAffilie());
        assertEquals("ICHAG - AF SEULE", ds.getNom());
        assertEquals(1, ds.getEmployees().size());
        Employee employee = ds.getEmployees().get(0);
        assertEquals("Balmer", employee.getNom());
        assertEquals("Fredi", employee.getPrenom());
        assertEquals("756.1848.4786.64", employee.getNss());
        assertEquals("M", employee.getSexe());
        assertEquals(buildDate("1945-03-16"), employee.getDateNaissance());
        assertEquals(0, employee.getSalariesAvs().size());
        assertEquals(1, employee.getSalariesCaf().size());
        SalaryCaf salaryAvs = employee.getSalariesCaf().get(0);
        assertTrue(ds.isAfSeul());
        assertEquals(new Montant(65000), salaryAvs.getMontant());
        assertEquals(buildPeriode("2014-01-01", "2014-06-30"), salaryAvs.getPeriode());
        assertTrue(ds.isTest());
    }

    @Test
    public void testBuildwithTestBalise() throws Exception {
        DeclarationSalaire ds = DeclarationSalaireBuilder.build(
                "src/test/resources/ch/globaz/orion/businessimpl/services/pucs/swissDec1WithBaliseTest.xml",
                buildFormater());
        assertTrue(ds.isTest());
    }

    @Test
    public void testBuildForList() throws Exception {
        List<Employee> employees = new ArrayList<Employee>();
        Employee e = buildEmployee();
        employees.add(e);
        List<SalaryForList> l = DeclarationSalaireBuilder.buildForList(employees);
        assertEquals(0, l.size());
    }

    @Test
    public void testBuildForListWithMultiplSalary() throws Exception {
        List<Employee> employees = new ArrayList<Employee>();
        Employee e = buildEmployee();
        List<SalaryAvs> salaryAvs = new ArrayList<SalaryAvs>();
        salaryAvs.add(buildSalarayAvs(1, "2015-01-01", "2015-05-31"));
        salaryAvs.add(buildSalarayAvs(2, "2015-06-01", "2015-10-31"));
        salaryAvs.add(buildSalarayAvs(3, "2015-11-01", "2015-12-31"));

        SalariesAvs salariesAvs = new SalariesAvs(salaryAvs);
        e.setSalariesAvs(salariesAvs);
        employees.add(e);
        List<SalaryForList> l = DeclarationSalaireBuilder.buildForList(employees);
        assertEquals(3, l.size());
    }

    @Test
    public void testBuildForListWithAfSeul() throws Exception {
        List<Employee> employees = new ArrayList<Employee>();
        Employee e = buildEmployee();
        List<SalaryCaf> salaryCaf = new ArrayList<SalaryCaf>();
        salaryCaf.add(buildSalarayCaf(1, "2015-01-01", "2015-05-31"));
        salaryCaf.add(buildSalarayCaf(2, "2015-06-01", "2015-10-31"));
        salaryCaf.add(buildSalarayCaf(3, "2015-11-01", "2015-12-31"));

        e.setSalariesAvs(new SalariesAvs(new ArrayList<SalaryAvs>()));
        e.setSalariesCaf(new SalariesCaf(salaryCaf));
        employees.add(e);
        List<SalaryForList> l = DeclarationSalaireBuilder.buildForList(employees);
        assertEquals(3, l.size());
    }

    // @Test
    public void testBuildForListWithMultiplSalaryWithAfNotSamePeriode() throws Exception {
        List<Employee> employees = new ArrayList<Employee>();
        Employee e = buildEmployee();
        List<SalaryAvs> salaryAvs = new ArrayList<SalaryAvs>();
        salaryAvs.add(buildSalarayAvs(1, "2015-01-01", "2015-05-31"));
        salaryAvs.add(buildSalarayAvs(2, "2015-06-01", "2015-10-31"));
        salaryAvs.add(buildSalarayAvs(3, "2015-11-01", "2015-12-31"));

        SalariesAvs salariesAvs = new SalariesAvs(salaryAvs);
        List<SalaryCaf> salaryCaf = new ArrayList<SalaryCaf>();
        salaryCaf.add(buildSalarayCaf(3, "2015-01-01", "2015-03-31"));
        salaryCaf.add(buildSalarayCaf(2, "2015-04-01", "2015-05-31"));
        salaryCaf.add(buildSalarayCaf(1, "2015-06-01", "2015-12-31"));
        SalariesCaf salariesCaf = new SalariesCaf(salaryCaf);
        e.setSalariesAvs(salariesAvs);
        e.setSalariesCaf(salariesCaf);
        employees.add(e);
        List<SalaryForList> l = DeclarationSalaireBuilder.buildForList(employees);
        assertEquals(6, l.size());
        assertEquals(new Montant(0), l.get(0).getAf());
        assertEquals(new Montant(0), l.get(1).getAf());
        assertEquals(new Montant(0), l.get(2).getAf());
        assertEquals(new Montant(3), l.get(3).getAf());
        assertEquals(new Montant(2), l.get(4).getAf());
        assertEquals(new Montant(1), l.get(5).getAf());
    }

    @Test
    public void testBuildForListWithMultiplSalaryWithAfSamePeriode() throws Exception {
        List<Employee> employees = new ArrayList<Employee>();
        Employee e = buildEmployee();
        List<SalaryAvs> salaryAvs = new ArrayList<SalaryAvs>();
        salaryAvs.add(buildSalarayAvs(1, "2015-01-01", "2015-05-31"));
        salaryAvs.add(buildSalarayAvs(2, "2015-06-01", "2015-10-31"));
        salaryAvs.add(buildSalarayAvs(3, "2015-11-01", "2015-12-31"));

        SalariesAvs salariesAvs = new SalariesAvs(salaryAvs);
        List<SalaryCaf> salaryCaf = new ArrayList<SalaryCaf>();
        salaryCaf.add(buildSalarayCaf(3, "2015-01-01", "2015-05-31"));
        salaryCaf.add(buildSalarayCaf(2, "2015-06-01", "2015-10-31"));
        salaryCaf.add(buildSalarayCaf(1, "2015-11-01", "2015-12-31"));
        SalariesCaf salariesCaf = new SalariesCaf(salaryCaf);
        e.setSalariesAvs(salariesAvs);
        e.setSalariesCaf(salariesCaf);
        employees.add(e);
        List<SalaryForList> l = DeclarationSalaireBuilder.buildForList(employees);
        assertEquals(3, l.size());
        assertEquals(new Montant(3), l.get(0).getAf());
        assertEquals(new Montant(2), l.get(1).getAf());
        assertEquals(new Montant(1), l.get(2).getAf());
    }

    private SalaryCaf buildSalarayCaf(int montant, String dateDebut, String dateFin) {
        return new SalaryCaf.SalaryCafBuilder().canton("canton").periode(buildPeriode(dateDebut, dateFin))
                .montant(new Montant(montant)).build();
    }

    private PeriodeSalary buildPeriode(String dateDebut, String dateFin) {
        return new PeriodeSalary.PeriodeSalaryBuilder().dateDebut(dateDebut).dateFin(dateFin).build();
    }

    private SalaryAvs buildSalarayAvs(int montant, String dateDebut, String dateFin) {
        return new SalaryAvs.SalaryAvsBuilder().montantAc1(new Montant(montant / 2))
                .montantAc2(new Montant(montant / 10)).montantAvs(new Montant(montant))
                .periode(buildPeriode(dateDebut, dateFin)).build();

    }

    private Date buildDate(String date) {

        if (date != null && date.trim().length() > 0) {
            try {
                XMLGregorianCalendar dateXml = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
                return new Date(dateXml.toGregorianCalendar().getTime());
            } catch (DatatypeConfigurationException e) {
                throw new RuntimeException("Error parasing date", e);
            }
        }
        return null;
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

    public void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                System.out.println(fileEntry.getName());
            }
        }
    }

    @Test
    public void testPdf() throws Exception {

        // pucsGros.xml;
        List<String> files = Arrays
                .asList(new String[] { "pucs1", "swissDec1", "swissDec2", "swissDecMix", "swissDec3" });

        final File folder = new File("src/test/resources/ch/globaz/orion/businessimpl/services/pucs");

        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                try {

                    Locale locale = Locale.FRENCH;

                    DeclarationSalaire ds = DeclarationSalaireBuilder.build(file.getAbsolutePath(), buildFormater());

                    List<SalaryForList> list = DeclarationSalaireBuilder.buildForList(ds.getEmployees());

                    Details paramsData = new Details();
                    paramsData.add("Numéro d'affilié", ds.getNumeroAffilie());
                    paramsData.add("Raison sociale", ds.getNom());
                    paramsData.add("Déclaration", String.valueOf(ds.getAnnee()));
                    paramsData.newLigne();
                    paramsData.add("Nb salaire", String.valueOf(ds.getEmployees().size()));
                    paramsData.add("MontantAvs", ds.getMontantAvs().toStringFormat());
                    paramsData.add("MontantAc1", ds.getMontantAc1().toStringFormat());

                    paramsData.add("MontantAc1", "1'000'000'000'.00");
                    paramsData.add("MontantAc1", ds.getMontantAc2().toStringFormat());
                    paramsData.add("MontantCaf", ds.getMontantCaf().toStringFormat());

                    paramsData.newLigne();

                    List<SalaryForListInterface> forListInterface = new ArrayList<SalaryForListInterface>();

                    String path = "src/test/resources/ch/globaz/orion/businessimpl/services/pdf/"
                            + file.getName().replace(".xml", "");

                    for (SalaryForList salaryForList : list) {
                        forListInterface.add(salaryForList);
                    }

                    File f = SimpleOutputListBuilder.newInstance().local(locale).asPdf().addList(forListInterface)
                            .addTitle("Liste des déclaration de salaire", Align.RIGHT)
                            .classElementList(SalaryForListInterface.class).addHeaderDetails(paramsData)
                            .outputName(path).build();

                } catch (Throwable e) {
                    System.out.println(file.getName());
                    e.printStackTrace();
                    fail();
                }
            }
        }
    }
}
