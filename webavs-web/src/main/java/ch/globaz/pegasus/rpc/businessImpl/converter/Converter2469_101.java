package ch.globaz.pegasus.rpc.businessImpl.converter;

import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.AddressType;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.CalculationElementsType;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.CaseType;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.CompensationOfficeType;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.ContentType;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.DecisionType;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.DecisionsType;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.ElAmountsType;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.HousingOwnerType;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.ObjectFactory;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.PensionType;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.PersonType;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.PersonalCalculationElementsType;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.PersonalCalculationElementsType.PensionCategory;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.PersonsType;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.RealPropertyType;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.RentsType;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.ResidenceCostsType;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_common._1.DeliveryOfficeType;
import rpc.ch.ech.xmlns.ech_0007._5.CantonAbbreviationType;
import ch.globaz.naos.ree.tools.InfoCaisse;
import ch.globaz.pegasus.rpc.domaine.PersonElementsCalcul;
import ch.globaz.pegasus.rpc.domaine.RpcAddress;
import ch.globaz.pegasus.rpc.domaine.RpcData;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionAnnonceComplete;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCalculationElements;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceHousingOwner;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnoncePensionCategory;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnoncePerson;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnoncePersonalCalculationElements;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceRealProperty;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceRents;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceResidenceCosts;

public class Converter2469_101 implements Converter<RpcData, ContentType> {
    private static final String XSD_RENTCATEGORY_ANNUAL_GROSS = "ANNUAL_GROSS";
    private static final String XSD_RENTCATEGORY_RENTAL_VALUE = "RENTAL_VALUE";
    private static final String XSD_HOUSINGMODE_RESIDENCE = "RESIDENCE";
    private static final String XSD_HOUSINGMODE_DOMICILE = "DOMICILE";
    private static final String XSD_PCC_PARTIE_DE_LA_TAXE_HOME = "INSIDE_RESIDENCE_COSTS";
    private static final String XSD_PCC_EN_SUS_DE_LA_TAXE_HOME = "IN_ADDITION_RESIDENCE_COSTS";
    private static final String XSD_PCC_NON_PRIS_EN_COMPTE = "IGNORED";
    public static final int XSD_ELLIMIT_PLAFONNEMENT_CAS_MINIMUM = 2;
    public static final int XSD_ELLIMIT_PAS_DE_PLAFONNEMENT = 0;
    public static final int XSD_ELLIMIT_PLAFONNEMENT = 1;

    private static final Logger LOG = LoggerFactory.getLogger(Converter2469_101.class);
    private final ObjectFactory factory = new ObjectFactory();
    private InfoCaisse currentCaisse;

    /**
     * @param infoCaisse
     * @param session
     */
    public Converter2469_101(InfoCaisse infoCaisse) {
        currentCaisse = infoCaisse;
    }

    @Override
    public ContentType convert(RpcData businessMessage) {
        return convertDomaine(businessMessage);
    }

    private ContentType convertDomaine(RpcData pojo) {
        AnnonceCase annonceCase = pojo.getAnnonce();
        annonceCase.setAnnonceDeliveryOffice(currentCaisse);
        ContentType masterData = factory.createContentType();
        CaseType caseType = factory.createCaseType();
        DecisionsType decisionsType = factory.createDecisionsType();

        caseType.setBusinessCaseIdRPC(annonceCase.getBusinessCaseIdRPC());

        for (AnnonceDecision annonceDecision : annonceCase.getDecisions()) {
            DecisionType xmlDeci = convertDecision(annonceDecision);
            decisionsType.getDecision().add(xmlDeci);
        }
        caseType.setDecisions(decisionsType);
        masterData.setCase(caseType);
        return masterData;
    }

    private DecisionType convertDecision(AnnonceDecision annonceDecision) {
        rpc.ch.eahv_iv.xmlns.eahv_iv_2469_common._1.ObjectFactory commonFactory = new rpc.ch.eahv_iv.xmlns.eahv_iv_2469_common._1.ObjectFactory();

        DecisionType xmlDeci = factory.createDecisionType();
        xmlDeci.setDecisionId(annonceDecision.getDecisionId());
        xmlDeci.setDecisionDate(XmlConverters.convertDateToXMLGregorianCalendar(annonceDecision.getDecisionDate()));
        xmlDeci.setValidFrom(XmlConverters.convertDateToXMLGregorianCalendar(annonceDecision.getValidFrom()));

        DeliveryOfficeType dot = commonFactory.createDeliveryOfficeType();
        dot.setElOffice(annonceDecision.getDeliveryOffice().getElOffice());
        dot.setElAgency(annonceDecision.getDeliveryOffice().getElAgency());
        xmlDeci.setDeliveryOffice(dot);

        xmlDeci.setDecisionKind(annonceDecision.getDecisionKind());
        xmlDeci.setDecisionCause(annonceDecision.getDecisionCause());
        if (annonceDecision.getValidTo() != null) {
            xmlDeci.setValidTo(XmlConverters.convertDateToXMLGregorianCalendar(annonceDecision.getValidTo()));
        }

        if (annonceDecision.getElAmounts() != null) {
            ElAmountsType elAmount = factory.createElAmountsType();
            elAmount.setAmountNoHC(annonceDecision.getElAmounts().getAmountNoHC().longValue());
            elAmount.setAmountWithHC(annonceDecision.getElAmounts().getAmountWithHC().longValue());
            elAmount.setElLimit(annonceDecision.getElAmounts().getElLimit());
            xmlDeci.setElAmounts(elAmount);
        }
        CalculationElementsType calcElmnt = convertCalculationElement(annonceDecision.getCalculationElements());
        xmlDeci.setCalculationElements(calcElmnt);
        PersonsType persons = convertPersons(annonceDecision.getPersons());
        xmlDeci.setPersons(persons);
        xmlDeci.setDecisionIdPartnerDecision(annonceDecision.getDecisionIdPartnerDecision());
        return xmlDeci;
    }

    private PersonsType convertPersons(List<AnnoncePerson> lPersons) {
        PersonsType persons = factory.createPersonsType();
        for (AnnoncePerson annoncePerson : lPersons) {
            PersonType person = factory.createPersonType();
            person.setVn(annoncePerson.getVn());
            person.setRepresentative(annoncePerson.getRepresentative());
            person.setPensionKind(annoncePerson.getPensionKind());
            person.setVitalNeedsCategory(annoncePerson.getVitalNeedsCategory().name());
            person.setMaritalStatus(annoncePerson.getMaritalStatus());
            person.setHousingMode(annoncePerson.getHousingMode().isDomicile() ? XSD_HOUSINGMODE_DOMICILE
                    : XSD_HOUSINGMODE_RESIDENCE);
            AddressType legalAdr = factory.createAddressType();
            if (annoncePerson.getLegalAddress() != null) { // personData.isValidLegalAddress()
                legalAdr.setCanton(getValidCantonAbreviationType(annoncePerson.getLegalAddress().getAddress()));
                legalAdr.setMunicipality(annoncePerson.getLegalAddress().getMunicipality());
            }
            person.setLegalAddress(legalAdr);
            if (annoncePerson.getLivingAddress() != null) { // personData.isValidLivingAddress()
                AddressType livingAddr = factory.createAddressType();

                livingAddr.setCanton(getValidCantonAbreviationType(annoncePerson.getLivingAddress().getAddress()));
                livingAddr.setMunicipality(annoncePerson.getLivingAddress().getMunicipality());
                person.setLivingAddress(livingAddr);
            }
            PersonalCalculationElementsType pce = factory.createPersonalCalculationElementsType();
            AnnoncePersonalCalculationElements apce = annoncePerson.getPersonalCalculationElements();
            PensionCategory pcepc = resolvePensionCategory(apce.getPensionCategory());
            pce.setPensionCategory(pcepc);
            pce.setHcLcaAllowance(apce.getHcLcaAllowance().longValue());
            pce.setLucrativeGrossIncome(apce.getLucrativeGrossIncome().longValue());
            pce.setHypotheticalGrossIncome(apce.getHypotheticalGrossIncome().longValue());
            pce.setTotalPension(apce.getTotalPension().longValue());
            if (apce.getLppPension() != null) { // personData.hasLppPension()
                pce.setLppPension(apce.getLppPension().longValue());
            }
            if (apce.getForeignPension() != null) { // personData.hasForeignPension()
                pce.setForeignPension(apce.getForeignPension().longValue());
            }
            pce.setOtherIncomes(apce.getOtherIncomes().longValue());
            // Non ne disposons pas de cette info
            pce.setLppWithdrawalAmount(apce.getLppWithdrawalAmount().longValue());
            pce.setPatientContributionCategory(apce.getPatientContributionCategory());
            pce.setHcFlatHelp(apce.getHcFlatHelp().longValue());
            if (apce.getHcEffectiveHelp() != null) { // n'existe pas encore
                pce.setHcEffectiveHelp(apce.getHcEffectiveHelp().longValue());
            }
            pce.setOtherExpenses(apce.getOtherExpenses().longValue());
            if (apce.getResidenceCosts() != null) { // personData.hasResidenceCosts()
                pce.setResidenceCosts(resolveResidenceCosts(apce.getResidenceCosts()));
            }
            person.setPersonalCalculationElements(pce);
            persons.getPerson().add(person);
        }

        return persons;
    }

    private String resolvePatientContributionCategory(RpcDecisionAnnonceComplete annonce,
            PersonElementsCalcul personData) {
        if (annonce.getPcaDecision().getPca().getGenre().isDomicile()) {
            return XSD_PCC_NON_PRIS_EN_COMPTE;
        } else {
            if (personData.hasResidenceContributions()) {
                return XSD_PCC_EN_SUS_DE_LA_TAXE_HOME;
            }
            return XSD_PCC_PARTIE_DE_LA_TAXE_HOME;
        }
    }

    private ResidenceCostsType resolveResidenceCosts(AnnonceResidenceCosts arc) {
        ResidenceCostsType residenceCosts = factory.createResidenceCostsType();
        if (arc.getResidenceCostsLodging() != null) {
            residenceCosts.setResidenceCostsLodging(arc.getResidenceCostsLodging().longValue());
        }
        if (arc.getResidenceCostsCare() != null) {
            residenceCosts.setResidenceCostsCare(arc.getResidenceCostsCare().longValue());
        }
        if (arc.getResidenceCostsAssistance() != null) {
            residenceCosts.setResidenceCostsAssistance(arc.getResidenceCostsAssistance().longValue());
        }
        if (arc.getResidenceCostsPatientContribution() != null) {
            residenceCosts.setResidenceCostsPatientContribution(arc.getResidenceCostsPatientContribution().longValue());
        }
        residenceCosts.setResidenceCostsTotal(arc.getResidenceCostsTotal().longValue());
        residenceCosts.setResidenceCostsConsidered(arc.getResidenceCostsConsidered().longValue());
        residenceCosts.setResidencePatientContribution(arc.getResidencePatientContribution().longValue());
        residenceCosts.setResidencePatientExpenses(arc.getResidencePatientExpenses().longValue());
        return residenceCosts;
    }

    private PensionCategory resolvePensionCategory(AnnoncePensionCategory pensionCat) {
        PensionCategory pcepc = factory.createPersonalCalculationElementsTypePensionCategory();
        if (pensionCat.getPension() != null) {// personData.hasPension()
            PensionType pension = factory.createPensionType();

            CompensationOfficeType co = factory.createCompensationOfficeType();
            co.setCompensationOffice(pensionCat.getPension().getCompensationOffice().getCompensationOffice());
            co.setCompensationAgency(pensionCat.getPension().getCompensationOffice().getCompensationAgency());
            pension.setCompensationOffice(co);

            pension.setAvsAipension(pensionCat.getPension().getAvsAipension().longValue());
            pension.setDisabledAllowance(pensionCat.getPension().getDisabledAllowance().longValue());
            pension.setDailyAllowance(pensionCat.getPension().getDailyAllowance().longValue());

            pcepc.setPension(pension);
        } else {
            // la string vide est necessaire à la validation de la xsd, n'accepte pas null
            pcepc.setNoPension("");
        }
        return pcepc;
    }

    private CantonAbbreviationType getValidCantonAbreviationType(RpcAddress address) {
        try {
            return CantonAbbreviationType.valueOf(address.getCanton().getAbreviation());
        } catch (IllegalArgumentException e) {
            LOG.debug("Le canton de l'adresse ne match pas à la liste reconnu par la xsd", e);
            throw new RpcBusinessException("pegasus.rpc.address.canton.introuvable", address.getCanton()
                    .getAbreviation(), address.toString());
        }
    }

    private boolean residenceCostsPatientContribution() {
        return false;
    }

    private boolean residenceCostsAssistance() {
        return false;
    }

    private boolean residenceCostsCare() {
        return false;
    }

    private boolean residenceCostsLodging() {
        return false;
    }

    private boolean hcEffectiveHelp() {
        // n'existe pas encore
        return false;
    }

    private CalculationElementsType convertCalculationElement(AnnonceCalculationElements calculationElements) {
        CalculationElementsType calcElmnt = factory.createCalculationElementsType();
        calcElmnt.setOtherWealth(calculationElements.getOtherWealth().longValue());
        calcElmnt.setDivestedWealth(calculationElements.getDivestedWealth().longValue());
        calcElmnt.setOtherDebts(calculationElements.getOtherDebts().longValue());
        calcElmnt.setWealthDeductible(calculationElements.getWealthDeductible().longValue());
        calcElmnt.setWealthConsidered(calculationElements.getWealthConsidered().longValue());
        calcElmnt.setWealthIncome(calculationElements.getWealthIncome().longValue());
        calcElmnt.setUsufructIncome(calculationElements.getUsufructIncome().longValue());
        calcElmnt.setWealthIncomeConsidered(calculationElements.getWealthIncomeConsidered().longValue());
        calcElmnt.setIncomeConsideredTotal(calculationElements.getIncomeConsideredTotal().longValue());
        calcElmnt.setWealthIncomeRate(calculationElements.getWealthIncomeRate().movePointRight(2)
                .setScale(2, BigDecimal.ROUND_HALF_UP));
        calcElmnt.setVitalNeeds(calculationElements.getVitalNeeds().longValue());
        calcElmnt.setChildren(calculationElements.getChildren());
        if (calculationElements.getRealProperty() != null) { //
            RealPropertyType realProp = factory.createRealPropertyType();
            AnnonceRealProperty arp = calculationElements.getRealProperty();
            realProp.setRealProperty(arp.getRealProperty().longValue());
            realProp.setMortgageDebts(arp.getMortgageDebts().longValue());
            realProp.setPropertyIncome(arp.getPropertyIncome().longValue());
            realProp.setMortgageInterest(arp.getMortgageInterest().longValue());
            realProp.setMaintenanceFees(arp.getMaintenanceFees().longValue());
            realProp.setInterestFeesEligible(arp.getInterestFeesEligible().longValue());
            calcElmnt.setRealProperty(realProp);
        }
        if (calculationElements.getHousingOwner() != null) { // annonce.isProrietaire()
            HousingOwnerType housingOwner = factory.createHousingOwnerType();
            AnnonceHousingOwner annonceHousingOwner = calculationElements.getHousingOwner();
            housingOwner.setSelfInhabitedProperty(annonceHousingOwner.getSelfInhabitedProperty().longValue());
            housingOwner.setSelfInhabitedPropertyDeductible(annonceHousingOwner.getSelfInhabitedPropertyDeductible()
                    .longValue());
            housingOwner.setRentalValue(annonceHousingOwner.getRentalValue().longValue());
            calcElmnt.setHousingOwner(housingOwner);
        }
        if (calculationElements.getRents() != null) {// annonce.hasLoyers()
            RentsType rents = factory.createRentsType();
            AnnonceRents annonceRents = calculationElements.getRents();
            rents.setGrossRental(annonceRents.getGrossRental().longValue());
            rents.setRentCategory(annonceRents.getRentCategory());
            rents.setRentGrossTotal(annonceRents.getRentGrossTotal().longValue());
            rents.setRentGrossTotalPart(annonceRents.getRentGrossTotalPart().longValue());
            rents.setMaxRent(annonceRents.getMaxRent().longValue());
            calcElmnt.setRents(rents);
        }
        return calcElmnt;
    }

}
