package ch.globaz.orion.businessimpl.services.pucs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ch.globaz.common.dom.ElementsDomParser;
import ch.globaz.common.dom.Function;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.orion.business.domaine.pucs.Adresse;
import ch.globaz.orion.business.domaine.pucs.Contact;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaire;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.domaine.pucs.Employee;
import ch.globaz.orion.business.domaine.pucs.PeriodeSalary;
import ch.globaz.orion.business.domaine.pucs.SalariesAvs;
import ch.globaz.orion.business.domaine.pucs.SalariesCaf;
import ch.globaz.orion.business.domaine.pucs.SalaryAvs;
import ch.globaz.orion.business.domaine.pucs.SalaryCaf;
import ch.globaz.orion.ws.service.AppAffiliationService;
import globaz.globall.format.IFormatData;

// FIXME les parseurs maison (cf. ElementsDomParser) c'est LE MAL ABSOLU!!!
public class DeclarationSalaireBuilder {

    private DeclarationSalaireBuilder() {
        throw new UnsupportedOperationException("Build class");
    }

    public static DeclarationSalaire build(String path) {
        IFormatData formater = AppAffiliationService.resolveNumAffilieFormater();
        return build(path, formater);
    }

    public static DeclarationSalaire build(ElementsDomParser parser, DeclarationSalaireProvenance provenance) {
        IFormatData formater = AppAffiliationService.resolveNumAffilieFormater();
        return build(parser, provenance, formater);
    }

    public static DeclarationSalaire builOnlyHead(ElementsDomParser parser) {
        IFormatData formater = AppAffiliationService.resolveNumAffilieFormater();
        return builOnlyHead(parser, formater);
    }

    static DeclarationSalaire build(String path, IFormatData formater) {

        ElementsDomParser parser = new ElementsDomParser(path);
        DeclarationSalaire ds = builOnlyHead(parser, formater);
        ds.setEmployees(buildEmployees(parser));

        return ds;
    }

    static DeclarationSalaire build(ElementsDomParser parser, DeclarationSalaireProvenance provenance,
            IFormatData formater) {

        DeclarationSalaire ds = builOnlyHead(parser, formater);
        ds.setEmployees(buildEmployees(parser));
        ds.setProvenance(provenance);
        return ds;
    }

    static DeclarationSalaire builOnlyHead(ElementsDomParser parser, IFormatData formater) {
        boolean ifForAfSeul = false;
        DeclarationSalaire ds = new DeclarationSalaire();

        Integer annee = parser.find("GeneralSalaryDeclarationDescription AccountingPeriod").getValueAsInteger();
        Integer nbSalaire = parser.find("SalaryCounters NumberOf-AHV-AVS-Salary-Tags").getValueAsInteger();

        String name = parser.find("Company CompanyDescription Name HR-RC-Name").getFirstValue();
        Date transmissionDate = parser.find("TransmissionDate").getValueAsDateAndCorrectItIfNeed();
        String numeroAffilie = parser.find("Institutions AHV-AVS AK-CC-CustomerNumber").getFirstValue();

        if (numeroAffilie == null) {
            numeroAffilie = parser.find("AK-CC-CustomerNumber").getFirstValue();
        }

        if (numeroAffilie == null) {
            numeroAffilie = parser.find("FAK-CAF-CustomerNumber").getFirstValue();
            nbSalaire = parser.find("NumberOf-FAK-CAF-Salary-Tags").getValueAsInteger();
            ifForAfSeul = true;
        }

        ElementsDomParser totalParser = parser.find("SalaryTotals AHV-AVS-Totals");
        Montant montantAc1 = totalParser.find("Total-ALV-AC-Incomes").getValueAsMontant();
        Montant montantAc2 = totalParser.find("Total-ALVZ-ACS-Incomes").getValueAsMontant(); // to verify
        Montant montantAvs = totalParser.find("Total-AHV-AVS-Incomes").getValueAsMontant();
        Montant montantCaf = parser.find("Total-FAK-CAF-Incomes").getValueAsMontant();
        String montantCafCanton = parser.findValue("Canton");

        if (ifForAfSeul) {
            // FIXME ça risque pas de marcher en mode multi canton...
            montantCaf = parser.find("SalaryTotals Total-FAK-CAF-ContributorySalary").getValueAsMontant();
            montantCafCanton = parser.find("SalaryTotals Canton").getFirstValue();

            ds.setAfSeul(true);
        }
        ds.setTest(parser.find("TestCase").exist());
        ds.setAnnee(annee);
        ds.setMontantAc1(montantAc1);
        ds.setMontantAc2(montantAc2);
        ds.setMontantAvs(montantAvs);
        // FIXME SPI SCO checker le passage en multi canton!!!
        ds.setMontantCaf(montantCafCanton, montantCaf);

        ds.setNom(name);

        Adresse adresse = new Adresse(parser.findValue("Company CompanyDescription Address Street"),
                parser.findValue("Company CompanyDescription Address ZIP-Code"),
                parser.findValue("Company CompanyDescription Address City"));
        ds.setAdresse(adresse);

        Contact contact = new Contact(parser.findValue("ContactPerson Name"),
                parser.findValue("ContactPerson PhoneNumber"), parser.findValue("ContactPerson EmailAddress"));
        ds.setContact(contact);

        ds.setNumeroIde(parser.findValue("Company CompanyDescription UID-BFS"));
        ds.setNumeroAffilie(AppAffiliationService.formatNumAffilie(numeroAffilie, formater));
        ds.setTransmissionDate(transmissionDate);
        if (nbSalaire != null) {
            ds.setNbSalaire(nbSalaire);
        }

        // Si c'est une déclaration de substitution
        ds.setSubstitution(parser.find("SubstitutionMapping").exist());

        // si il a été déjà soumis
        ds.setDuplicate(parser.find("Duplicate").exist());

        return ds;
    }

    static List<SalaryForList> buildForList(DeclarationSalaire ds) {
        return buildForList(ds.getEmployees());
    }

    static List<SalaryForList> buildForList(List<Employee> employees) {
        List<SalaryForList> list = new ArrayList<SalaryForList>();
        for (Employee employee : employees) {
            List<SalaryCaf> cafs = new ArrayList<SalaryCaf>();
            for (SalaryAvs salaryAvs : employee.getSalariesAvs()) {
                SalaryForList salaryForList = new SalaryForList();
                salaryForList.setAc1(salaryAvs.getMontantAc1());
                salaryForList.setAc2(salaryAvs.getMontantAc2());
                SalaryCaf caf = employee.resolveAf(salaryAvs);
                if (caf != null) {
                    cafs.add(caf);
                    salaryForList.setAf(caf.getMontant());
                    salaryForList.setCantonAf(caf.getCanton());
                }
                salaryForList.setCanton(employee.getWorkPlaceCanton());
                salaryForList.setDateNaissance(employee.getDateNaissance());
                salaryForList.setNomPrenom(employee.getNom() + " " + employee.getPrenom());
                salaryForList.setNss(employee.getNss());
                salaryForList.setPeriode(salaryAvs.getPeriode());
                salaryForList.setSexe(employee.getSexe());
                salaryForList.setSlaire(salaryAvs.getMontantAvs());
                list.add(salaryForList);
            }

            for (SalaryCaf salaryCaf : employee.getSalariesCaf()) {
                if (!cafs.contains(salaryCaf)) {
                    SalaryForList salaryForList = new SalaryForList();
                    salaryForList.setAf(salaryCaf.getMontant());
                    salaryForList.setCanton(employee.getWorkPlaceCanton());
                    salaryForList.setCantonAf(salaryCaf.getCanton());
                    salaryForList.setDateNaissance(employee.getDateNaissance());
                    salaryForList.setNomPrenom(employee.getNom() + " " + employee.getPrenom());
                    salaryForList.setNss(employee.getNss());
                    salaryForList.setPeriode(salaryCaf.getPeriode());
                    salaryForList.setSexe(employee.getSexe());
                    list.add(salaryForList);
                }
            }

        }
        Collections.sort(list, new Comparator<SalaryForList>() {

            @Override
            public int compare(SalaryForList o1, SalaryForList o2) {
                return o1.getNomPrenom().compareTo(o2.getNomPrenom());
            }
        });
        return list;
    }

    private static List<Employee> buildEmployees(ElementsDomParser parser) {
        ElementsDomParser staffParser = parser.find("Person");

        return staffParser.createList(new Function<Employee>() {
            @Override
            public Employee convert(ElementsDomParser p) {
                Employee employee = new Employee();
                employee.setNom(p.find("Lastname").getFirstValue());
                employee.setNss(p.find("SV-AS-Number").getFirstValue());
                employee.setPrenom(p.find("Firstname").getFirstValue());
                employee.setWorkPlaceCanton(p.find("WorkplaceCanton").getFirstValue());
                if (employee.getWorkPlaceCanton() == null) {
                    employee.setWorkPlaceCanton(p.find("Canton").getFirstValue());
                }
                employee.setSexe(p.find("Sex").getFirstValue());
                employee.setDateNaissance(p.find("DateOfBirth").getValueAsDateAndCorrectItIfNeed());
                employee.setSalariesAvs(new SalariesAvs(buildSalariesAvs(p)));

                employee.setSalariesCaf(new SalariesCaf(buildSalariesCaf(p)));

                return employee;
            }
        });
    }

    private static List<SalaryAvs> buildSalariesAvs(ElementsDomParser parser) {
        ElementsDomParser salariesParser = parser.find("AHV-AVS-Salaries AHV-AVS-Salary");
        return salariesParser.createList(new Function<SalaryAvs>() {
            @Override
            public SalaryAvs convert(ElementsDomParser p) {
                return new SalaryAvs.SalaryAvsBuilder().montantAc1(p.findValueAsMontant("ALV-AC-Income"))
                        .montantAc2(p.findValueAsMontant("ALVZ-ACS-Income"))
                        .montantAvs(p.findValueAsMontant("AHV-AVS-Income"))
                        .periode(buildPeriode(p, "AccountingTime from", "AccountingTime until")).build();

            }
        });
    }

    private static List<SalaryCaf> buildSalariesCaf(ElementsDomParser parser) {
        ElementsDomParser salariesParser = parser.find("FAK-CAF-Salaries FAK-CAF-Salary");
        return salariesParser.createList(new Function<SalaryCaf>() {
            @Override
            public SalaryCaf convert(ElementsDomParser p) {
                PeriodeSalary periode = buildPeriode(p, "FAK-CAF-Period from", "FAK-CAF-Period until");
                Montant montant = p.findValueAsMontant("FAK-CAF-ContributorySalary");
                if (periode == null && montant.isZero()) {
                    return null;
                }

                String canton = p.findValue("FAK-CAF-WorkplaceCanton");

                return new SalaryCaf.SalaryCafBuilder().montant(montant).canton(canton).periode(periode).build();
            }
        });

    }

    private static PeriodeSalary buildPeriode(ElementsDomParser p, String pahtDebut, String pahtFin) {

        String dateDebut = p.findValue(pahtDebut);
        String dateFin = p.findValue(pahtFin);
        if (pahtDebut != null && dateFin != null) {
            return new PeriodeSalary.PeriodeSalaryBuilder().dateDebut(dateDebut).dateFin(dateFin).build();
        } else {
            return null;
        }
    }

}
