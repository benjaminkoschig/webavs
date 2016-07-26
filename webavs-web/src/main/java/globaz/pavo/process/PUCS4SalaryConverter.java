package globaz.pavo.process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import ch.swissdec.schema.sd._20130514.salarydeclaration.FAKCAFTotalsType;
import ch.swissdec.schema.sd._20130514.salarydeclaration.ParticularsType;
import ch.swissdec.schema.sd._20130514.salarydeclaration.PersonType;
import ch.swissdec.schema.sd._20130514.salarydeclaration.PersonsType;
import ch.swissdec.schema.sd._20130514.salarydeclaration.SalaryCountersType;
import ch.swissdec.schema.sd._20130514.salarydeclaration.SalaryDeclarationType;
import ch.swissdec.schema.sd._20130514.salarydeclaration.TimePeriodType;
import ch.swissdec.schema.sd._20130514.salarydeclaration.TotalFAKCAFPerCantonType;
import ch.swissdec.schema.sd._20130514.salarydeclarationconsumercontainer.DeclareSalaryConsumerType;
import ch.swissdec.schema.sd._20130514.salarydeclarationcontainer.SalaryDeclarationRequestType;
import globaz.pavo.process.PUCS4SalaryConverter.PlausiResult.PlausiStatus;

/**
 * Convertit un objet issu du webservice PUCS v4 (cf. {@link DeclareSalaryConsumerType}) vers des objets métiers
 * internes à WebAVS.
 */
public class PUCS4SalaryConverter {
    private static final Logger LOG = LoggerFactory.getLogger(PUCS4SalaryConverter.class);
    private static final List<Plausi> ALL_CHECKS;
    static {
        List<Plausi> plausies = new ArrayList<Plausi>();
        plausies.add(new NoopPlausi());
        ALL_CHECKS = Collections.unmodifiableList(plausies);
    }

    public DeclarationSalaire convert(DeclareSalaryConsumerType param) {
        if (param == null) {
            return null;
        }

        DeclarationSalaire result = new DeclarationSalaire();
        result.setProvenance(DeclarationSalaireProvenance.SWISS_DEC);
        result.setTransmissionDate(
                new Date(param.getDistributorRequestContext().getTransmissionDate().toGregorianCalendar().getTime()));
        result.setAnnee(1234); // FIXME le year ne veut rien dire... on peut être à cheval sur plusieurs années...

        /*
         * FIXME comment mapper tout ça?
         * result.setNom(nom);
         * result.setNumeroAffilie(numeroAffilie);
         * result.setNumeroIde(numeroIde);
         * result.setContact(contact);
         * result.setNbSalaire(nbSalaire);
         * result.setDuplicate(duplicate);
         * result.setSubstitution(substitution);
         * result.setTest(test);
         *
         */

        SalaryDeclarationRequestType declareSalary = param.getDeclareSalary();
        SalaryDeclarationType salaryDeclaration = declareSalary.getSalaryDeclaration();
        CompanyType company = salaryDeclaration.getCompany();

        PersonsType staff = company.getStaff();

        boolean isAfSeul = true;

        for (PersonType person : staff.getPerson()) {
            ParticularsType particulars = person.getParticulars();

            Employee targetEmployee = new Employee();
            targetEmployee.setDateNaissance(new Date(particulars.getDateOfBirth().toGregorianCalendar().getTime()));
            targetEmployee.setNom(particulars.getLastname());
            targetEmployee.setPrenom(particulars.getFirstname());
            targetEmployee.setNss(particulars.getSocialInsuranceIdentification().getSVASNumber());
            targetEmployee.setSexe(particulars.getSex().value());
            targetEmployee.setWorkPlaceCanton(particulars.getResidenceCanton().value()); // FIXME workplace vs residence
                                                                                         // wtf?

            // AHVAVS -------------
            if (person.getAHVAVSSalaries() == null) {
                LOG.info("no AHVAVSSalaries node found in document... ignoring");
            } else {
                List<SalaryAvs> salaries = new ArrayList<SalaryAvs>();

                for (AHVAVSSalaryType salary : person.getAHVAVSSalaries().getAHVAVSSalary()) {
                    SalaryAvs targetSalary = new SalaryAvs.SalaryAvsBuilder()
                            .montantAc1(Montant.valueOf(salary.getALVACIncome()))
                            .montantAc2(Montant.valueOf(salary.getALVZACSIncome()))
                            .montantAvs(Montant.valueOf(salary.getAHVAVSIncome()))
                            .periode(buildPeriodeSalary(salary.getAccountingTime())).build();
                    salaries.add(targetSalary);
                }

                targetEmployee.setSalariesAvs(new SalariesAvs(salaries));
            }

            // FAKCAF -------------
            if (person.getFAKCAFSalaries() == null) {
                LOG.info("no FAKCAFSalaries node found in document... ignoring");
            } else {
                List<SalaryCaf> salaries = new ArrayList<SalaryCaf>();

                for (FAKCAFSalaryType salary : person.getFAKCAFSalaries().getFAKCAFSalary()) {
                    isAfSeul = false;

                    SalaryCaf targetSalary = new SalaryCaf.SalaryCafBuilder()
                            .montant(Montant.valueOf(salary.getFAKCAFContributorySalary()))
                            .canton(salary.getFAKCAFWorkplaceCanton().value())
                            .periode(buildPeriodeSalary(salary.getFAKCAFPeriod())).build();
                    salaries.add(targetSalary);
                }

                targetEmployee.setSalariesCaf(new SalariesCaf(salaries));
            }

            /*
             * if (person.getBVGLPPSalaries() != null) {
             * LOG.warn("je sais pas du tout quoi faire avec ce noeud: " + "getBVGLPPSalaries");
             * }
             * if (person.getKTGAMCSalaries() != null) {
             * LOG.warn("je sais pas du tout quoi faire avec ce noeud: " + "getKTGAMCSalaries");
             * }
             * if (person.getTaxSalaries() != null) {
             * LOG.warn("je sais pas du tout quoi faire avec ce noeud: " + "getTaxSalaries");
             * }
             * if (person.getTaxAtSourceSalaries() != null) {
             * LOG.warn("je sais pas du tout quoi faire avec ce noeud: " + "getTaxAtSourceSalaries");
             * }
             * if (person.getStatisticSalaries() != null) {
             * LOG.warn("je sais pas du tout quoi faire avec ce noeud: " + "getStatisticSalaries");
             * }
             * if (person.getUVGLAASalaries() != null) {
             * LOG.warn("je sais pas du tout quoi faire avec ce noeud: " + "getUVGLAASalaries");
             * }
             * if (person.getUVGZLAACSalaries() != null) {
             * LOG.warn("je sais pas du tout quoi faire avec ce noeud: " + "getUVGZLAACSalaries");
             * }
             */

            result.getEmployees().add(targetEmployee);
        }

        result.setAfSeul(isAfSeul);

        /*
         * result.setMontantAvs(x);
         * result.setMontantAc1(x);
         * result.setMontantAc2(x);
         * result.setMontantCaf(x);
         */
        // -----------------------
        List<AHVAVSTotalsType> ahvavs = company.getSalaryTotals().getAHVAVSTotals();
        if (ahvavs.size() > 1) {
            throw new UnsupportedOperationException("found " + ahvavs.size() + " nodes!");
        } else if (!ahvavs.isEmpty()) {
            // FIXME on fait quoi quand il y a plusieurs noeuds?
            AHVAVSTotalsType totalAvs = ahvavs.get(0);
            result.setMontantAvs(Montant.valueOf(totalAvs.getTotalAHVAVSIncomes()));
        }

        /*
         * FIXME
         * result.montantAc1(Montant.valueOf(salary.getALVACIncome()));
         * result.montantAc2(Montant.valueOf(salary.getALVZACSIncome()));
         */
        // -----------------------
        List<FAKCAFTotalsType> fakcaf = company.getSalaryTotals().getFAKCAFTotals();
        if (fakcaf.size() > 1) {
            throw new UnsupportedOperationException("found " + fakcaf.size() + " nodes!");
        } else if (!fakcaf.isEmpty()) {
            // FIXME on fait quoi quand il y a plusieurs noeuds?
            FAKCAFTotalsType totalCaf = fakcaf.get(0);

            for (TotalFAKCAFPerCantonType canton : totalCaf.getTotalFAKCAFPerCanton()) {
                result.setMontantCaf(canton.getCanton().value(),
                        Montant.valueOf(canton.getTotalFAKCAFContributorySalary()));
            }
        }

        boolean found = false;

        SalaryCountersType salaryCounters = company.getSalaryCounters();
        Long numberOfAHVAVSSalaryTags = salaryCounters.getNumberOfAHVAVSSalaryTags();
        if (numberOfAHVAVSSalaryTags != null) {
            if (found) {
                throw new IllegalStateException(
                        "nbSalaire has already been defined. This version of document is not supported (must define at max. 1 node with a number of salaries)");
            }
            result.setNbSalaire(numberOfAHVAVSSalaryTags.intValue());
            found = true;
        }

        Long numberOfFAKCAFSalaryTags = salaryCounters.getNumberOfFAKCAFSalaryTags();
        if (numberOfFAKCAFSalaryTags != null) {
            // FIXME on fait quoi dans ce cas là?
            if (found) {
                throw new IllegalStateException(
                        "nbSalaire has already been defined. This version of document is not supported (must define at max. 1 node with a number of salaries)");
            }
            result.setNbSalaire(numberOfFAKCAFSalaryTags.intValue());
        }

        CompanyDescriptionType companyDescription = company.getCompanyDescription();
        AddressType sourceAddress = companyDescription.getAddress();
        Adresse adresse = new Adresse(null /* FIXME ... */, sourceAddress.getZIPCode(), sourceAddress.getCity());
        result.setAdresse(adresse);

        checkAllPlausi(result);

        return result;
    }

    private PeriodeSalary buildPeriodeSalary(TimePeriodType period) {
        XMLGregorianCalendar from = period.getFrom();
        XMLGregorianCalendar until = period.getUntil();

        return new PeriodeSalary.PeriodeSalaryBuilder().dateDebut(from == null ? null : from.toXMLFormat())
                .dateFin(until == null ? null : until.toXMLFormat()).build();
    }

    private void checkAllPlausi(DeclarationSalaire salaire) {
        for (Plausi plausi : ALL_CHECKS) {
            PlausiResult result = plausi.checkPlausi(salaire);

            switch (result.status) {
                case OK:
                    break;
                case WARN:
                    break;
                case KO:
                    throw new IllegalArgumentException("could not validate plausi: " + result.message);
            }
        }
    }

    // ---------------------------------------------------------------

    public static interface Plausi {
        PlausiResult checkPlausi(DeclarationSalaire salaire);
    }

    public static final class PlausiResult {
        public enum PlausiStatus {
            OK,
            WARN,
            KO;
        }

        private PlausiStatus status;
        private String message;

        public String getMessage() {
            return message;
        }

        public PlausiStatus getStatus() {
            return status;
        }
    }

    static class NoopPlausi implements Plausi {
        @Override
        public PlausiResult checkPlausi(DeclarationSalaire salaire) {
            PlausiResult result = new PlausiResult();
            result.status = PlausiStatus.OK;
            result.message = "";
            return result;
        }
    }
}
