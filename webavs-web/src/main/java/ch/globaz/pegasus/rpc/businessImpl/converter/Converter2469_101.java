package ch.globaz.pegasus.rpc.businessImpl.converter;

import ch.globaz.naos.ree.tools.InfoCaisse;
import ch.globaz.pegasus.rpc.domaine.PersonElementsCalcul;
import ch.globaz.pegasus.rpc.domaine.RpcAddress;
import ch.globaz.pegasus.rpc.domaine.RpcData;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionAnnonceComplete;
import ch.globaz.pegasus.rpc.domaine.annonce.*;
import globaz.pegasus.enums.TypeDivestedWealth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.*;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_000101._1.CommonPersonalCalculationElementsType.PensionCategory;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_common._1.CommonDecisionType;
import rpc.ch.eahv_iv.xmlns.eahv_iv_2469_common._1.DeliveryOfficeType;
import rpc.ch.ech.xmlns.ech_0007._5.CantonAbbreviationType;
import rpc.ch.ech.xmlns.ech_0007._5.CantonFlAbbreviationType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

public class Converter2469_101 implements Converter<RpcData, ContentType> {
    private static final String XSD_RENTCATEGORY_ANNUAL_GROSS = "ANNUAL_GROSS";
    private static final String XSD_RENTCATEGORY_RENTAL_VALUE = "RENTAL_VALUE";
    private static final String XSD_HOUSINGMODE_RESIDENCE = "RESIDENCE";
    private static final String XSD_HOUSINGMODE_DOMICILE = "DOMICILE";
    private static final String XSD_PCC_PARTIE_DE_LA_TAXE_HOME = "INSIDE_RESIDENCE_COSTS";
    private static final String XSD_PCC_EN_SUS_DE_LA_TAXE_HOME = "IN_ADDITION_RESIDENCE_COSTS";
    private static final String XSD_PCC_NON_PRIS_EN_COMPTE = "IGNORED";

    private static final String XSD_TYPE_DIVESTED_WEALTH_SANS_CONTREPARTIE = "noEquivalentCompensation";
    private static final String XSD_TYPE_DIVESTED_WEALTH_CONSOMMATION_EXCESSIVE = "excessivelyConsume";

    public static final int XSD_ELLIMIT_PLAFONNEMENT_CAS_MINIMUM = 2;
    public static final int XSD_ELLIMIT_PAS_DE_PLAFONNEMENT = 0;
    public static final int XSD_ELLIMIT_PLAFONNEMENT = 1;

    private static final Logger LOG = LoggerFactory.getLogger(Converter2469_101.class);
    private final ObjectFactory factory = new ObjectFactory();
    private InfoCaisse currentCaisse;

    /**
     * @param infoCaisse
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
            CommonDecisionType xmlCommDeci = convertCommDecisionType(annonceDecision, annonceDecision.getAnnonce().getPcaDecision().getPca().getReformePC());
            if(xmlCommDeci instanceof DecisionTypeRef1){
                DecisionTypeRef1 xmlDeci = convertDecisionRef1(annonceDecision, xmlCommDeci);
                decisionsType.getDecisionRef1().add(xmlDeci);
            }else{
                DecisionTypeRef0 xmlDeci = convertDecisionRef0(annonceDecision, xmlCommDeci);
                decisionsType.getDecisionRef0().add(xmlDeci);
            }
        }
        caseType.setDecisions(decisionsType);
        masterData.setCase(caseType);
        return masterData;
    }

    private CommonDecisionType convertCommDecisionType(AnnonceDecision annonceDecision, boolean isReformePC) {
        rpc.ch.eahv_iv.xmlns.eahv_iv_2469_common._1.ObjectFactory commonFactory = new rpc.ch.eahv_iv.xmlns.eahv_iv_2469_common._1.ObjectFactory();
        CommonDecisionType xmlDeci;
        if(isReformePC) {
            xmlDeci = factory.createDecisionTypeRef1();
        }else{
            xmlDeci = factory.createDecisionTypeRef0();
        }
        xmlDeci.setDecisionId(annonceDecision.getDecisionId());
        xmlDeci.setDecisionDate(XmlConverters.convertDateToXMLGregorianCalendar(annonceDecision.getDecisionDate()));
        xmlDeci.setValidFrom(XmlConverters.convertDateToXMLGregorianCalendar(annonceDecision.getValidFrom()));

        DeliveryOfficeType dot = commonFactory.createDeliveryOfficeType();
        dot.setElOffice(annonceDecision.getDeliveryOffice().getElOffice());
        dot.setElAgency(annonceDecision.getDeliveryOffice().getElAgency());
        xmlDeci.setDeliveryOffice(dot);

        return xmlDeci;
    }

    private DecisionTypeRef1 convertDecisionRef1(AnnonceDecision annonceDecision, CommonDecisionType xmlCommDeci) {
        DecisionTypeRef1 xmlDeci = (DecisionTypeRef1) xmlCommDeci;

        xmlDeci.setDecisionKind(annonceDecision.getDecisionKind());
        xmlDeci.setDecisionCause(annonceDecision.getDecisionCause());
        if (annonceDecision.getValidTo() != null) {
            xmlDeci.setValidTo(XmlConverters.convertDateToXMLGregorianCalendar(annonceDecision.getValidTo()));
        }

        if (annonceDecision.getElAmounts() != null) {
            ElAmountsTypeRef1 elAmount = (ElAmountsTypeRef1) createCommElAmountsType(annonceDecision, true);
            elAmount.setElLimit(annonceDecision.getElAmounts().getElLimit());
            xmlDeci.setElAmounts(elAmount);
        } else {
            xmlDeci.setElAmounts((ElAmountsTypeRef1) createEmptyCommElAmountsType(true));
        }

        xmlDeci.setRequestDateOfReceipt(annonceDecision.getRequestDateofReceipt());

        CommonCalculationElementsType commCalculationElementsType = createCommCalculationElementsType(annonceDecision.getCalculationElements(), true);
        CalculationElementsTypeRef1 calcElmnt = convertCalculationElementRef1(annonceDecision.getCalculationElements(), commCalculationElementsType);
        xmlDeci.setCalculationElements(calcElmnt);

        PersonsTypeRef1 persons = convertPersonsRef1(annonceDecision.getPersons());
        xmlDeci.setPersons(persons);
        xmlDeci.setDecisionIdPartnerDecision(annonceDecision.getDecisionIdPartnerDecision());
        return xmlDeci;
    }

    private DecisionTypeRef0 convertDecisionRef0(AnnonceDecision annonceDecision, CommonDecisionType xmlCommDeci) {
        DecisionTypeRef0 xmlDeci = (DecisionTypeRef0) xmlCommDeci;

        xmlDeci.setDecisionKind(annonceDecision.getDecisionKind());
        xmlDeci.setDecisionCause(annonceDecision.getDecisionCause());
        if (annonceDecision.getValidTo() != null) {
            xmlDeci.setValidTo(XmlConverters.convertDateToXMLGregorianCalendar(annonceDecision.getValidTo()));
        }

        if (annonceDecision.getElAmounts() != null) {
            ElAmountsTypeRef0 elAmount = (ElAmountsTypeRef0) createCommElAmountsType(annonceDecision, false);
            elAmount.setElLimit(annonceDecision.getElAmounts().getElLimit());
            xmlDeci.setElAmounts(elAmount);
        } else {
            xmlDeci.setElAmounts((ElAmountsTypeRef0) createEmptyCommElAmountsType(false));
        }
        CommonCalculationElementsType commCalculationElementsType = createCommCalculationElementsType(annonceDecision.getCalculationElements(), false);
        CalculationElementsTypeRef0 calcElmnt = convertCalculationElementRef0(annonceDecision.getCalculationElements(), commCalculationElementsType);
        xmlDeci.setCalculationElements(calcElmnt);

        PersonsTypeRef0 persons = convertPersonsRef0(annonceDecision.getPersons());
        xmlDeci.setPersons(persons);
        xmlDeci.setDecisionIdPartnerDecision(annonceDecision.getDecisionIdPartnerDecision());
        return xmlDeci;
    }

    private CommonElAmountsType createCommElAmountsType(AnnonceDecision annonceDecision, boolean isReformePC) {
        CommonElAmountsType commElAmout;
        if(isReformePC){
            commElAmout = factory.createElAmountsTypeRef1();
        }else{
            commElAmout = factory.createElAmountsTypeRef0();
        }
        commElAmout.setAmountNoHC(annonceDecision.getElAmounts().getAmountNoHC().longValue());
        commElAmout.setAmountWithHC(annonceDecision.getElAmounts().getAmountWithHC().longValue());
        return commElAmout;
    }

    private CommonElAmountsType createEmptyCommElAmountsType(boolean isReformePC) {
        CommonElAmountsType commElAmout;
        if(isReformePC){
            commElAmout = factory.createElAmountsTypeRef1();
            ((ElAmountsTypeRef1) commElAmout).setElLimit(0);
        }else{
            commElAmout = factory.createElAmountsTypeRef0();
            ((ElAmountsTypeRef0) commElAmout).setElLimit(0);
        }
        commElAmout.setAmountNoHC(0);
        commElAmout.setAmountWithHC(0);
        return commElAmout;
    }

    private PersonsTypeRef1 convertPersonsRef1(List<AnnoncePerson> lPersons) {
        PersonsTypeRef1 persons = factory.createPersonsTypeRef1();
        for (AnnoncePerson annoncePerson : lPersons) {
            PersonTypeRef1 person = (PersonTypeRef1)createCommPersonType(annoncePerson, true);
            person.setVitalNeedsCategory(annoncePerson.getVitalNeedsCategory().name());

            PersonalCalculationElementsTypeRef1 pce = (PersonalCalculationElementsTypeRef1) createCommPersonalCalculationElementsType(annoncePerson, true);
            AnnoncePersonalCalculationElements apce = annoncePerson.getPersonalCalculationElements();
            if (apce.getHcEffectiveHelp() != null) { // n'existe pas encore
                pce.setHcEffectiveHelp(apce.getHcEffectiveHelp().longValue());
            }
            pce.setIndividualPremiumReduction(apce.getIndividualPremiumReduction().longValue());
            person.setPersonalCalculationElements(pce);
            persons.getPerson().add(person);
        }

        return persons;
    }

    private PersonsTypeRef0 convertPersonsRef0(List<AnnoncePerson> lPersons) {
        PersonsTypeRef0 persons = factory.createPersonsTypeRef0();
        for (AnnoncePerson annoncePerson : lPersons) {
            PersonTypeRef0 person = (PersonTypeRef0) createCommPersonType(annoncePerson, false);
            person.setVitalNeedsCategory(annoncePerson.getVitalNeedsCategory().name());

            PersonalCalculationElementsTypeRef0 pce = (PersonalCalculationElementsTypeRef0) createCommPersonalCalculationElementsType(annoncePerson, false);
            AnnoncePersonalCalculationElements apce = annoncePerson.getPersonalCalculationElements();
            if (apce.getHcEffectiveHelp() != null && !apce.getHcEffectiveHelp().isZero()) { // n'existe pas encore
                pce.setHcEffectiveHelp(apce.getHcEffectiveHelp().longValue());
            }
            person.setPersonalCalculationElements(pce);
            persons.getPerson().add(person);
        }

        return persons;
    }

    private CommonPersonalCalculationElementsType createCommPersonalCalculationElementsType(AnnoncePerson annoncePerson, boolean isReformePC) {
        CommonPersonalCalculationElementsType pce;
        if(isReformePC){
            pce = factory.createPersonalCalculationElementsTypeRef1();
        }else{
            pce = factory.createPersonalCalculationElementsTypeRef0();
        }
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
        pce.setOtherExpenses(apce.getOtherExpenses().longValue());
        if (apce.getResidenceCosts() != null) { // personData.hasResidenceCosts()
            pce.setResidenceCosts(resolveResidenceCosts(apce.getResidenceCosts()));
        }
        if(pce instanceof PersonalCalculationElementsTypeRef1) {
            PersonalCalculationElementsTypeRef1 pce1 = (PersonalCalculationElementsTypeRef1) pce;
            pce1.setChildrenCostsAssistanceNet(apce.getChildrenCostsAssitanceNet().longValue());
        }
        pce.setDisabledAllowanceRecipient(apce.getDisabledAllowanceRecipient());
        return pce;
    }

    private CommonPersonType createCommPersonType(AnnoncePerson annoncePerson, boolean isReformePC) {
        CommonPersonType person;
        if(isReformePC){
            person = factory.createPersonTypeRef1();
        }else{
            person = factory.createPersonTypeRef0();
        }
        person.setVn(annoncePerson.getVn());
        person.setRepresentative(annoncePerson.getRepresentative());
        person.setPensionKind(annoncePerson.getPensionKind());
        if(annoncePerson.getDegreeOfInvalidity() != null) {
            person.setDegreeOfInvalidity(new BigDecimal(annoncePerson.getDegreeOfInvalidity()));
        }
        person.setMaritalStatus(annoncePerson.getMaritalStatus());
        person.setHousingMode(annoncePerson.getHousingMode().isDomicile() ? XSD_HOUSINGMODE_DOMICILE
                : XSD_HOUSINGMODE_RESIDENCE);
        CommonPersonType.LegalAddress legalAdr = factory.createCommonPersonTypeLegalAddress();
        if (annoncePerson.getLegalAddress() != null) { // personData.isValidLegalAddress()
            legalAdr.setCanton(getValidCantonAbreviationType(annoncePerson.getLegalAddress().getAddress()));
            legalAdr.setMunicipality(annoncePerson.getLegalAddress().getMunicipality());
        }
        person.setLegalAddress(legalAdr);
        if (annoncePerson.getLivingAddress() != null) { // personData.isValidLivingAddress()
            CommonPersonType.LivingAddress livingAddr = factory.createCommonPersonTypeLivingAddress();

            livingAddr.setCanton(getValidCantonFIAbreviationType(annoncePerson.getLivingAddress().getAddress()));
            livingAddr.setMunicipality(annoncePerson.getLivingAddress().getMunicipality());
            person.setLivingAddress(livingAddr);
        }
        return person;
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
        PensionCategory pcepc = factory.createCommonPersonalCalculationElementsTypePensionCategory();
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

    private CantonFlAbbreviationType getValidCantonFIAbreviationType(RpcAddress address) {
        try {
            return CantonFlAbbreviationType.valueOf(address.getCanton().getAbreviation());
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

    private CommonCalculationElementsType createCommCalculationElementsType(AnnonceCalculationElements calculationElements, boolean isReformePC) {
        CommonCalculationElementsType commCalElmnt;
        if(isReformePC){
            commCalElmnt = factory.createCalculationElementsTypeRef1();
        }else{
            commCalElmnt = factory.createCalculationElementsTypeRef0();
        }
        commCalElmnt.setOtherWealth(calculationElements.getOtherWealth().longValue());
        commCalElmnt.setDivestedWealth(calculationElements.getDivestedWealth().longValue());
        commCalElmnt.setOtherDebts(calculationElements.getOtherDebts().longValue());
        commCalElmnt.setWealthDeductible(calculationElements.getWealthDeductible().longValue());
        commCalElmnt.setWealthConsidered(calculationElements.getWealthConsidered().longValue());
        commCalElmnt.setWealthIncome(calculationElements.getWealthIncome().longValue());
        commCalElmnt.setUsufructIncome(calculationElements.getUsufructIncome().longValue());
        commCalElmnt.setWealthIncomeConsidered(calculationElements.getWealthIncomeConsidered().longValue());
        commCalElmnt.setIncomeConsideredTotal(calculationElements.getIncomeConsideredTotal().longValue());
        commCalElmnt.setWealthIncomeRate(calculationElements.getWealthIncomeRate().movePointRight(2)
                .setScale(2, BigDecimal.ROUND_HALF_UP));
        commCalElmnt.setVitalNeeds(calculationElements.getVitalNeeds().longValue());
        commCalElmnt.setChildren(calculationElements.getChildren());
        commCalElmnt.setLivingSituation(calculationElements.getLivingSituationType().getType());

        return  commCalElmnt;
    }

    private CalculationElementsTypeRef1 convertCalculationElementRef1(AnnonceCalculationElements calculationElements, CommonCalculationElementsType commCalculationElementsType) {
        CalculationElementsTypeRef1 calcElmnt = (CalculationElementsTypeRef1)commCalculationElementsType;
        if (calculationElements.isProperty()) { //
            RealPropertyTypeRef1 realProp = (RealPropertyTypeRef1) createCommRealPropertyType(calculationElements, true);
            realProp.setMortgageDebtsRealProperty(calculationElements.getMortgageDebtsRealProperty().longValue());
            calcElmnt.setRealProperty(realProp);
        }
        if (calculationElements.isHousingOwner()) { // annonce.isProrietaire()
            HousingOwnerTypeRef1 housingOwner = (HousingOwnerTypeRef1)createHousingOwnerTypeRef(calculationElements, true);
            housingOwner.setMortgageDebtsSelfinhabited(calculationElements.getMortgageDebtsSelfinhabited().longValue());
            calcElmnt.setHousingOwner(housingOwner);
        }
        if (calculationElements.isRent()) {// annonce.hasLoyers()
            RentsTypeRef1 rents = (RentsTypeRef1)createRentsTypeRef(calculationElements, true);
            if(calculationElements.getRentRegion() != null) {
                rents.setRentRegion(calculationElements.getRentRegion());
                rents.setFamilySize(calculationElements.getFamilySize());
                calcElmnt.setRents(rents);
            }
        }

        if (calculationElements.isDivestedWealth()) { //si Dessaisissement fortune cochée
            // Alternative
//            calcElmnt.setTypeOfDivestedWealth(calculationElements.getTypeDivestedWealth().equals("1")? XSD_TYPE_DIVESTED_WEALTH_SANS_CONTREPARTIE : XSD_TYPE_DIVESTED_WEALTH_CONSOMMATION_EXCESSIVE);

            TypeDivestedWealth typeDivestedWealth = TypeDivestedWealth.fromCode(calculationElements.getTypeDivestedWealth());
            if (Objects.nonNull(typeDivestedWealth)) {
                calcElmnt.setTypeOfDivestedWealth(typeDivestedWealth.getXsdKey());
            }
        }

        return calcElmnt;
    }

    private HousingOwnerTypeRef0 createHousingOwnerTypeRef(AnnonceCalculationElements calculationElements, boolean isReformePC) {
        HousingOwnerTypeRef0 housingOwner;
        if(isReformePC){
            housingOwner = factory.createHousingOwnerTypeRef1();
        }else{
            housingOwner = factory.createHousingOwnerTypeRef0();
        }
        housingOwner.setSelfInhabitedProperty(calculationElements.getSelfInhabitedProperty().longValue());
        housingOwner.setSelfInhabitedPropertyDeductible(calculationElements.getSelfInhabitedPropertyDeductible()
                .longValue());
        housingOwner.setRentalValue(calculationElements.getRentalValue().longValue());
        return housingOwner;
    }

    private CalculationElementsTypeRef0 convertCalculationElementRef0(AnnonceCalculationElements calculationElements, CommonCalculationElementsType commCalculationElementsType) {
        CalculationElementsTypeRef0 calcElmnt = (CalculationElementsTypeRef0)commCalculationElementsType;
        if (calculationElements.isProperty()) { //
            RealPropertyTypeRef0 realProp = (RealPropertyTypeRef0)createCommRealPropertyType(calculationElements, false);
            realProp.setMortgageDebts(calculationElements.getMortgageDebts().longValue());
            calcElmnt.setRealProperty(realProp);
        }
        if (calculationElements.isHousingOwner()) { // annonce.isProrietaire()
            HousingOwnerTypeRef0 housingOwner = createHousingOwnerTypeRef(calculationElements, false);
            calcElmnt.setHousingOwner(housingOwner);
        }
        if (calculationElements.isRent()) {// annonce.hasLoyers()
            RentsTypeRef0 rents = createRentsTypeRef(calculationElements, false);
            calcElmnt.setRents(rents);
        }
        return calcElmnt;
    }

    private RentsTypeRef0 createRentsTypeRef(AnnonceCalculationElements calculationElements, boolean isReformePC) {
        RentsTypeRef0 rents;
        if(isReformePC){
            rents = factory.createRentsTypeRef1();
        }else{
            rents = factory.createRentsTypeRef0();
        }
        rents.setWheelchairSurcharge(calculationElements.isWheelchairSurcharge());
        rents.setGrossRental(calculationElements.getGrossRental().longValue());
        rents.setRentCategory(calculationElements.getRentCategory());
        rents.setRentGrossTotal(calculationElements.getRentGrossTotal().longValue());
        rents.setRentGrossTotalPart(calculationElements.getRentGrossTotalPart().longValue());
        rents.setMaxRent(calculationElements.getMaxRent().longValue());

        return rents;
    }

    private CommonRealPropertyType createCommRealPropertyType(AnnonceCalculationElements calculationElements, boolean isReformePC) {
        CommonRealPropertyType commRealProp;
        if(isReformePC){
            commRealProp = factory.createRealPropertyTypeRef1();
        }else{
            commRealProp = factory.createRealPropertyTypeRef0();
        }
        commRealProp.setRealProperty(calculationElements.getRealProperty().longValue());
        commRealProp.setPropertyIncome(calculationElements.getPropertyIncome().longValue());
        commRealProp.setMortgageInterest(calculationElements.getMortgageInterest().longValue());
        commRealProp.setMaintenanceFees(calculationElements.getMaintenanceFees().longValue());
        commRealProp.setInterestFeesEligible(calculationElements.getInterestFeesEligible().longValue());
        return commRealProp;
    }

}
