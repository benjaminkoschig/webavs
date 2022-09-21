package globaz.pavo.process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;

import ch.swissdec.schema.common._20200220.common.AddressType;
import ch.swissdec.schema.common._20200220.common.CantonAddressType;
import ch.swissdec.schema.common._20200220.common.CompanyDescriptionType;
import ch.swissdec.schema.common._20200220.common.ParticularsType;
import ch.swissdec.schema.common._20200220.common.TimePeriodType;
import ch.swissdec.schema.sd._20130514.salarydeclaration.CantonAndEXType;
import ch.swissdec.schema.sd._20200220.salarydeclaration.AHVAVSCustomerIdentificationType;
import ch.swissdec.schema.sd._20200220.salarydeclaration.AHVAVSSalaryType;
import ch.swissdec.schema.sd._20200220.salarydeclaration.AHVAVSTotalsType;
import ch.swissdec.schema.sd._20200220.salarydeclaration.CompanyType;
import ch.swissdec.schema.sd._20200220.salarydeclaration.ContactPersonType;
import ch.swissdec.schema.sd._20200220.salarydeclaration.CustomerIdentificationType;
import ch.swissdec.schema.sd._20200220.salarydeclaration.FAKCAFCustomerIdentificationType;
import ch.swissdec.schema.sd._20200220.salarydeclaration.FAKCAFSalaryType;
import ch.swissdec.schema.sd._20200220.salarydeclaration.FAKCAFTotalsType;
import ch.swissdec.schema.sd._20200220.salarydeclaration.PersonType;
import ch.swissdec.schema.sd._20200220.salarydeclaration.PersonsType;
import ch.swissdec.schema.sd._20200220.salarydeclaration.SalaryCountersType;
import ch.swissdec.schema.sd._20200220.salarydeclaration.SalaryDeclarationType;
import ch.swissdec.schema.sd._20200220.salarydeclaration.TotalFAKCAFPerCantonType;
import ch.swissdec.schema.sd._20200220.salarydeclarationconsumercontainer.DeclareSalaryConsumerType;
import ch.swissdec.schema.sd._20200220.salarydeclarationconsumercontainer.DistributorRequestContextType;
import ch.swissdec.schema.sd._20200220.salarydeclarationcontainer.SalaryDeclarationRequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * Convertit un objet issu du webservice PUCS v4 (cf. {@link DeclareSalaryConsumerType}) vers des objets m�tiers
 * internes � WebAVS.
 */
public class PUCS5SalaryConverter {
    private static final Logger LOG = LoggerFactory.getLogger(PUCS5SalaryConverter.class);
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
        if(param.getDeclareSalary() == null || param.getDeclareSalary().getSalaryDeclaration() == null){
            throw new NullPointerException("Aucune d�claration de salaire correctement form�e dans le fichier XML fourni.");
        }
        result.setAnnee(param.getDeclareSalary().getSalaryDeclaration().getGeneralSalaryDeclarationDescription()
                .getAccountingPeriod().getYear());

        DistributorRequestContextType distributorRequestContext = param.getDistributorRequestContext();

        // Si distributorRequestContext est null �a veut dire qu'on � pas le node
        // DistributorRequestContext dans le fichier xml
        if (distributorRequestContext != null) {
            result.setTransmissionDate(new Date(distributorRequestContext.getTransmissionDate().toGregorianCalendar()
                    .getTime()));
            result.setTest(distributorRequestContext.getTestCase() != null);
            result.setDuplicate(distributorRequestContext.getDuplicate() != null);
            result.setSubstitution(distributorRequestContext.getSubstitutionMapping() != null);
        } else {
            result.setTransmissionDate(new Date(param.getDeclareSalary().getRequestContext().getTransmissionDate()
                    .toGregorianCalendar().getTime()));
            result.setTest(param.getDeclareSalary().getRequestContext().getTestCase() != null);
            result.setDuplicate(false);
            result.setSubstitution(false);
        }

        /*
         * FIXME comment mapper tout �a?
         * result.setNumeroIde(numeroIde);
         */

        SalaryDeclarationRequestType declareSalary = param.getDeclareSalary();
        SalaryDeclarationType salaryDeclaration = declareSalary.getSalaryDeclaration();
        CompanyType company = salaryDeclaration.getCompany();

        convertEmployeesAndSetIsAfSeulFlag(result, company.getStaff());

        String numAff = null;
        CustomerIdentificationType institutions = company.getInstitutions();
        List<AHVAVSCustomerIdentificationType> institAhvavs = institutions.getAHVAVS();
        if (institAhvavs != null && !institAhvavs.isEmpty()) {
            // FIXME on fait quoi quand il y a plusieurs AHVAVSCustomerIdentificationType?
            numAff = institAhvavs.get(0).getAKCCCustomerNumber();
        } else {
            List<FAKCAFCustomerIdentificationType> institFakcaf = institutions.getFAKCAF();
            if (institFakcaf != null && !institFakcaf.isEmpty()) {
                // FIXME on fait quoi quand il y a plusieurs FAKCAFCustomerIdentificationType?
                numAff = institFakcaf.get(0).getFAKCAFCustomerNumber();
            }
        }

        if (numAff != null) {
            numAff = numAff.trim();
        }

        result.setNumeroAffilie(numAff);

        /*
         * result.setMontantAvs(x);
         * result.setMontantCaf(x);
         */
        // -----------------------
        List<AHVAVSTotalsType> ahvavs = company.getSalaryTotals().getAHVAVSTotals();
        if (ahvavs.size() > 1) {
            AHVAVSTotalsType first = ahvavs.get(0);
            result.setMontantAvs(Montant.valueOf(first.getTotalAHVAVSIncomes()));
            for (int i = 1; i < ahvavs.size(); i++) {
                AHVAVSTotalsType avs = ahvavs.get(i);
                if (first.getTotalAHVAVSIncomes().compareTo(avs.getTotalAHVAVSIncomes()) != 0) {
                    result.getMontantAVSDiff().add(avs.getTotalAHVAVSIncomes().toString());
                }
            }
        } else if (!ahvavs.isEmpty()) {
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
        for (FAKCAFTotalsType compare : fakcaf) {
            for (TotalFAKCAFPerCantonType canton : compare.getTotalFAKCAFPerCanton()) {
                updateCAF(canton, result);
            }
        }

        boolean foundNbSalaire = false;

        SalaryCountersType salaryCounters = company.getSalaryCounters();
        Long numberOfAHVAVSSalaryTags = salaryCounters.getNumberOfAHVAVSSalaryTags();
        if (numberOfAHVAVSSalaryTags != null) {
            if (foundNbSalaire) {
                throw new IllegalStateException(
                        "nbSalaire has already been defined. This version of document is not supported (must define at max. 1 node with a number of salaries)");
            }
            result.setNbSalaire(numberOfAHVAVSSalaryTags.intValue());
            foundNbSalaire = true;
        }

        Long numberOfFAKCAFSalaryTags = salaryCounters.getNumberOfFAKCAFSalaryTags();
        if (!foundNbSalaire && numberOfFAKCAFSalaryTags != null) {

            result.setNbSalaire(numberOfFAKCAFSalaryTags.intValue());
        }

        CompanyDescriptionType companyDescription = company.getCompanyDescription();

        result.setNom(companyDescription.getName().getHRRCName());

        AddressType sourceAddress = companyDescription.getAddress();
        Adresse adresse = new Adresse(sourceAddress.getStreet(), sourceAddress.getZIPCode(), sourceAddress.getCity());
        result.setAdresse(adresse);

        ContactPersonType contact = salaryDeclaration.getGeneralSalaryDeclarationDescription().getContactPerson();

        if (contact != null) {
            result.setContact(new Contact(contact.getName(), contact.getPhoneNumber(), contact.getEmailAddress()));
        } else {
            result.setContact(new Contact("", "", ""));
        }

        checkAllPlausi(result);

        return result;
    }

    private void updateCAF(TotalFAKCAFPerCantonType canton, DeclarationSalaire result) {
        if (result.getMontantCaf(canton.getCanton().value()) != null) {
            // montant d�j� pr�sent pour ce canton
            if (!result.getMontantCaf(canton.getCanton().value())
                    .substract(Montant.valueOf(canton.getTotalFAKCAFContributorySalary())).isZero()) {
                // valeur diff�rente pour un m�me canton
                if (result.getMontantAFDiff().get(canton.getCanton()) == null) {
                    result.getMontantAFDiff().put(convertELM5CantonToELM4Canton(canton.getCanton()), new ArrayList<String>());
                }
                result.getMontantAFDiff().get(canton.getCanton())
                        .add(canton.getTotalFAKCAFContributorySalary().toString());
            }
        } else {
            result.setMontantCaf(canton.getCanton().value(), Montant.valueOf(canton.getTotalFAKCAFContributorySalary()));
        }
    }

    private CantonAndEXType convertELM5CantonToELM4Canton(CantonAddressType canton){
        HashMap<CantonAddressType, CantonAndEXType> mapper = new HashMap<>();
        mapper.put(CantonAddressType.AG, CantonAndEXType.AG);
        mapper.put(CantonAddressType.AI, CantonAndEXType.AI);
        mapper.put(CantonAddressType.AR, CantonAndEXType.AR);
        mapper.put(CantonAddressType.BE, CantonAndEXType.BE);
        mapper.put(CantonAddressType.BL, CantonAndEXType.BL);
        mapper.put(CantonAddressType.BS, CantonAndEXType.BS);
        mapper.put(CantonAddressType.FR, CantonAndEXType.FR);
        mapper.put(CantonAddressType.GE, CantonAndEXType.GE);
        mapper.put(CantonAddressType.GL, CantonAndEXType.GL);
        mapper.put(CantonAddressType.GR, CantonAndEXType.GR);
        mapper.put(CantonAddressType.JU, CantonAndEXType.JU);
        mapper.put(CantonAddressType.LU, CantonAndEXType.LU);
        mapper.put(CantonAddressType.NE, CantonAndEXType.NE);
        mapper.put(CantonAddressType.NW, CantonAndEXType.NW);
        mapper.put(CantonAddressType.OW, CantonAndEXType.OW);
        mapper.put(CantonAddressType.SG, CantonAndEXType.SG);
        mapper.put(CantonAddressType.SH, CantonAndEXType.SH);
        mapper.put(CantonAddressType.SO, CantonAndEXType.SO);
        mapper.put(CantonAddressType.SZ, CantonAndEXType.SZ);
        mapper.put(CantonAddressType.TG, CantonAndEXType.TG);
        mapper.put(CantonAddressType.TI, CantonAndEXType.TI);
        mapper.put(CantonAddressType.UR, CantonAndEXType.UR);
        mapper.put(CantonAddressType.VD, CantonAndEXType.VD);
        mapper.put(CantonAddressType.VS, CantonAndEXType.VS);
        mapper.put(CantonAddressType.ZG, CantonAndEXType.ZG);
        mapper.put(CantonAddressType.ZH, CantonAndEXType.ZH);
        return mapper.get(canton);
    }

    private void convertEmployeesAndSetIsAfSeulFlag(DeclarationSalaire result, PersonsType staff) {

        int nombreCAFInscriptionInFile = 0;
        int nombreAVSInscriptionInFile = 0;

        for (PersonType person : staff.getPerson()) {
            ParticularsType particulars = person.getParticulars();

            Employee targetEmployee = new Employee();
            if (particulars.getDateOfBirth() != null) {
                targetEmployee.setDateNaissance(new Date(particulars.getDateOfBirth().toGregorianCalendar().getTime()));
            }
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
                    nombreAVSInscriptionInFile++;
                }

                targetEmployee.setSalariesAvs(new SalariesAvs(salaries));
            }

            // FAKCAF -------------
            if (person.getFAKCAFSalaries() == null) {
                LOG.info("no FAKCAFSalaries node found in document... ignoring");
            } else {
                List<SalaryCaf> salaries = new ArrayList<SalaryCaf>();

                for (FAKCAFSalaryType salary : person.getFAKCAFSalaries().getFAKCAFSalary()) {

                    SalaryCaf targetSalary = new SalaryCaf.SalaryCafBuilder()
                            .montant(Montant.valueOf(salary.getFAKCAFContributorySalary()))
                            .canton(salary.getFAKCAFWorkplaceCanton().value())
                            .periode(buildPeriodeSalary(salary.getFAKCAFPeriod())).build();
                    salaries.add(targetSalary);
                    nombreCAFInscriptionInFile++;
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

        result.setAfSeul(nombreAVSInscriptionInFile == 0 && nombreCAFInscriptionInFile >= 1);

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
            result.status = PlausiResult.PlausiStatus.OK;
            result.message = "";
            return result;
        }
    }
}
