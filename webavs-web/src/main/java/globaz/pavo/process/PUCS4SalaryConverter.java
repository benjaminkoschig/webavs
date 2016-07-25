package globaz.pavo.process;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.datatype.XMLGregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.orion.business.domaine.pucs.Adresse;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaire;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.domaine.pucs.Employee;
import ch.globaz.orion.business.domaine.pucs.PeriodeSalary;
import ch.globaz.orion.business.domaine.pucs.SalariesAvs;
import ch.globaz.orion.business.domaine.pucs.SalariesCaf;
import ch.globaz.orion.business.domaine.pucs.SalaryAvs;
import ch.globaz.orion.business.domaine.pucs.SalaryCaf;
import ch.swissdec.schema.sd._20130514.salarydeclaration.AHVAVSSalaryType;
import ch.swissdec.schema.sd._20130514.salarydeclaration.AHVAVSTotalsType;
import ch.swissdec.schema.sd._20130514.salarydeclaration.AddressType;
import ch.swissdec.schema.sd._20130514.salarydeclaration.CompanyDescriptionType;
import ch.swissdec.schema.sd._20130514.salarydeclaration.CompanyType;
import ch.swissdec.schema.sd._20130514.salarydeclaration.FAKCAFSalaryType;
import ch.swissdec.schema.sd._20130514.salarydeclaration.ParticularsType;
import ch.swissdec.schema.sd._20130514.salarydeclaration.PersonType;
import ch.swissdec.schema.sd._20130514.salarydeclaration.PersonsType;
import ch.swissdec.schema.sd._20130514.salarydeclaration.SalaryCountersType;
import ch.swissdec.schema.sd._20130514.salarydeclaration.SalaryDeclarationType;
import ch.swissdec.schema.sd._20130514.salarydeclaration.TimePeriodType;
import ch.swissdec.schema.sd._20130514.salarydeclarationconsumercontainer.DeclareSalaryConsumerType;
import ch.swissdec.schema.sd._20130514.salarydeclarationcontainer.SalaryDeclarationRequestType;

/**
 * Convertit un objet issu du webservice PUCS v4 (cf. {@link DeclareSalaryConsumerType}) vers des objets métiers
 * internes à WebAVS.
 */
public class PUCS4SalaryConverter {
    private static final Logger LOG = LoggerFactory.getLogger(PUCS4SalaryConverter.class);

    public DeclarationSalaire convert(DeclareSalaryConsumerType param) {
        if (param == null) {
            return null;
        }

        DeclarationSalaire result = new DeclarationSalaire();
        result.setProvenance(DeclarationSalaireProvenance.SWISS_DEC);
        result.setTransmissionDate(
                new Date(param.getDistributorRequestContext().getTransmissionDate().toGregorianCalendar().getTime()));

        /*
         * FIXME comment mapper tout ça?
         * result.setNom(nom);
         * result.setNumeroAffilie(numeroAffilie);
         * result.setNumeroIde(numeroIde);
         * result.setContact(contact);
         * result.setAnnee(annee);
         * result.setNbSalaire(nbSalaire);
         * result.setDuplicate(duplicate);
         * result.setSubstitution(substitution);
         * result.setTest(test);
         * result.setAfSeul(isAfSeul);
         */

        SalaryDeclarationRequestType declareSalary = param.getDeclareSalary();
        SalaryDeclarationType salaryDeclaration = declareSalary.getSalaryDeclaration();
        CompanyType company = salaryDeclaration.getCompany();

        PersonsType staff = company.getStaff();
        for (PersonType person : staff.getPerson()) {
            ParticularsType particulars = person.getParticulars();

            Employee e = new Employee();
            e.setDateNaissance(new Date(particulars.getDateOfBirth().toGregorianCalendar().getTime()));
            e.setNom(particulars.getLastname());
            e.setPrenom(particulars.getFirstname());
            e.setNss(particulars.getSocialInsuranceIdentification().getSVASNumber());
            e.setSexe(particulars.getSex().value());
            e.setWorkPlaceCanton(particulars.getResidenceCanton().value()); // FIXME workplace vs residence wtf?

            // AHVAVS -------------
            if (person.getAHVAVSSalaries() == null) {
                LOG.info("no AHVAVSSalaries node found in document... ignoring");
            } else {
                List<SalaryAvs> salaries = new ArrayList<SalaryAvs>();

                for (AHVAVSSalaryType salary : person.getAHVAVSSalaries().getAHVAVSSalary()) {
                    SalaryAvs targetSalary = new SalaryAvs.SalaryAvsBuilder()
                            .montantAc1(Montant.valueOf(salary.getALVACIncome()))
                            .montantAvs(Montant.valueOf(salary.getAHVAVSIncome()))
                            .periode(buildPeriodeSalary(salary.getAccountingTime())).build();

                    // FIXME et le canton?
                    salaries.add(targetSalary);
                }

                e.setSalariesAvs(new SalariesAvs(salaries));
            }

            // FAKCAF -------------
            if (person.getFAKCAFSalaries() == null) {
                LOG.info("no FAKCAFSalaries node found in document... ignoring");
            } else {
                List<SalaryCaf> salaries = new ArrayList<SalaryCaf>();

                for (FAKCAFSalaryType salary : person.getFAKCAFSalaries().getFAKCAFSalary()) {
                    SalaryCaf targetSalary = new SalaryCaf.SalaryCafBuilder()
                            .montant(Montant.valueOf(salary.getFAKCAFContributorySalary())) // FIXME ?
                            .canton(salary.getFAKCAFWorkplaceCanton().value())
                            .periode(buildPeriodeSalary(salary.getFAKCAFPeriod())).build();
                    salaries.add(targetSalary);
                }

                e.setSalariesCaf(new SalariesCaf(salaries));
            }

            // FIXME on fout quoi de ces infos???
            if (person.getBVGLPPSalaries() != null) {
                LOG.warn("je sais pas du tout quoi faire avec ce noeud: " + "getBVGLPPSalaries");
            }
            if (person.getKTGAMCSalaries() != null) {
                LOG.warn("je sais pas du tout quoi faire avec ce noeud: " + "getKTGAMCSalaries");
            }
            if (person.getTaxSalaries() != null) {
                LOG.warn("je sais pas du tout quoi faire avec ce noeud: " + "getTaxSalaries");
            }
            if (person.getTaxAtSourceSalaries() != null) {
                LOG.warn("je sais pas du tout quoi faire avec ce noeud: " + "getTaxAtSourceSalaries");
            }
            if (person.getStatisticSalaries() != null) {
                LOG.warn("je sais pas du tout quoi faire avec ce noeud: " + "getStatisticSalaries");
            }
            if (person.getUVGLAASalaries() != null) {
                LOG.warn("je sais pas du tout quoi faire avec ce noeud: " + "getUVGLAASalaries");
            }
            if (person.getUVGZLAACSalaries() != null) {
                LOG.warn("je sais pas du tout quoi faire avec ce noeud: " + "getUVGZLAACSalaries");
            }

            result.getEmployees().add(e);
        }

        /*
         * result.setMontantAvs(x);
         * result.setMontantAc1(x);
         * result.setMontantAc2(x);
         * result.setMontantCaf(x);
         */
        Map<String, BigDecimal> allTotals = new TreeMap<String, BigDecimal>();
        for (AHVAVSTotalsType total : company.getSalaryTotals().getAHVAVSTotals()) {
            allTotals.put(total.getInstitutionIDRef(), total.getTotalAHVAVSIncomes());
            result.setMontantAvs(Montant.valueOf(total.getTotalAHVAVSIncomes()));
        }

        // allTotals.

        SalaryCountersType salaryCounters = company.getSalaryCounters();
        Long numberOfAHVAVSSalaryTags = salaryCounters.getNumberOfAHVAVSSalaryTags();
        if (numberOfAHVAVSSalaryTags != null) {
            result.setNbSalaire(numberOfAHVAVSSalaryTags.intValue());
        }

        CompanyDescriptionType companyDescription = company.getCompanyDescription();
        AddressType sourceAddress = companyDescription.getAddress();
        Adresse adresse = new Adresse(null /* FIXME ... */, sourceAddress.getZIPCode(), sourceAddress.getCity());
        result.setAdresse(adresse);

        return result;
    }

    private PeriodeSalary buildPeriodeSalary(TimePeriodType period) {
        XMLGregorianCalendar from = period.getFrom();
        XMLGregorianCalendar until = period.getUntil();

        return new PeriodeSalary.PeriodeSalaryBuilder().dateDebut(from == null ? null : from.toXMLFormat())
                .dateFin(until == null ? null : until.toXMLFormat()).build();
    }
}
