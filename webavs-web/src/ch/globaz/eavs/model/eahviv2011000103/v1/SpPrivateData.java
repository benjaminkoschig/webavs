package ch.globaz.eavs.model.eahviv2011000103.v1;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractAnnuities;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractCash;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractChildAllowances;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractCompanyShares;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractDebts;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractInheritance;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractLifeInsurance;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractMaintenanceContribution;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractMilitaryInsurancePensions;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractMotorVehicle;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractOtherAssets;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractOtherPensions;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractPatentLicense;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractPensions;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractPensionsPillar1;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractPensionsPillar2;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractPensionsPillar3a;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractPensionsPillar3b;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractPerDiemAllowance;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractPrivateCash;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractPrivateDebts;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractPrivateRealEstateProperties;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractPrivateResidencyEntitlement;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractPrivateSecurities;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractRealEstateProperties;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractSecurities;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractSpPrivateData;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractTaxableIncomeDBG;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractTaxableIncomeExpenseTaxation;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractTaxableIncomeInAccordanceWithDBG;
import ch.globaz.eavs.model.eahviv2011000103.v0.TaxableIncomeDBG;

public class SpPrivateData extends AbstractSpPrivateData {

    private Annuities annuities = null;
    private Cash cash = null;
    private ChildAllowances childAllowances = null;
    private CompanyShares companyShares = null;
    private Debts debts = null;
    private Inheritance inheritance = null;
    private LifeInsurance lifeInsurance = null;
    private MaintenanceContribution maintenanceContribution = null;
    private MilitaryInsurancePensions militaryInsurancePensions = null;
    private MotorVehicle motorVehicle = null;
    private OtherAssets otherAssets = null;
    private OtherPensions otherPensions = null;
    private PatentLicense patentLicense = null;
    private Pensions pensions = null;
    private PensionsPillar1 pensionsPillar1 = null;
    private PensionsPillar2 pensionsPillar2 = null;
    private PensionsPillar3a pensionsPillar3a = null;
    private PensionsPillar3b pensionsPillar3b = null;
    private PerDiemAllowance perDiemAllowance = null;
    private PrivateCash privateCash = null;
    private PrivateDebts privateDebts = null;
    private PrivateRealEstateProperties privateRealEstateProperties = null;
    private PrivateSecurities privateSecurities = null;
    private RealEstateProperties realEstateProperties = null;
    private PrivateResidencyEntitlement residencyEntitlement = null;
    private Securities securities = null;
    private TaxableIncomeDBG taxableIncomeDBG = null;
    private TaxableIncomeInAccordanceWithDBG taxableIncomeInAccordanceWithDBG = null;
    private TaxableIncomeExpenseTaxation taxableIncomeInAccordanceWithExpenseTaxation = null;

    @Override
    public AbstractAnnuities getAnnuities() {
        if (annuities == null) {
            annuities = new Annuities();
        }
        return annuities;
    }

    @Override
    public AbstractCash getCash() {
        if (cash == null) {
            cash = new Cash();
        }
        return cash;
    }

    @Override
    public AbstractChildAllowances getChildAllowances() {
        if (childAllowances == null) {
            childAllowances = new ChildAllowances();
        }
        return childAllowances;
    }

    @Override
    public List getChildren() {
        List result = new ArrayList();
        result.add(pensions);
        result.add(otherPensions);
        result.add(motorVehicle);
        result.add(otherAssets);
        result.add(pensionsPillar1);
        result.add(pensionsPillar2);
        result.add(pensionsPillar3a);
        result.add(pensionsPillar3b);
        result.add(annuities);
        result.add(militaryInsurancePensions);
        result.add(perDiemAllowance);
        result.add(maintenanceContribution);
        result.add(childAllowances);
        result.add(patentLicense);
        result.add(residencyEntitlement);
        result.add(securities);
        result.add(cash);
        result.add(lifeInsurance);
        result.add(inheritance);
        result.add(realEstateProperties);
        result.add(companyShares);
        result.add(debts);
        result.add(taxableIncomeInAccordanceWithDBG);
        result.add(taxableIncomeInAccordanceWithExpenseTaxation);
        result.add(privateSecurities);
        result.add(privateCash);
        result.add(privateRealEstateProperties);
        result.add(privateDebts);
        return result;
    }

    @Override
    public AbstractCompanyShares getCompanyShares() {
        if (companyShares == null) {
            companyShares = new CompanyShares();
        }
        return companyShares;
    }

    @Override
    public AbstractDebts getDebts() {
        if (debts == null) {
            debts = new Debts();
        }
        return debts;
    }

    @Override
    public AbstractInheritance getInheritance() {
        if (inheritance == null) {
            inheritance = new Inheritance();
        }
        return inheritance;
    }

    @Override
    public AbstractLifeInsurance getLifeInsurance() {
        if (lifeInsurance == null) {
            lifeInsurance = new LifeInsurance();
        }
        return lifeInsurance;
    }

    @Override
    public AbstractMaintenanceContribution getMaintenanceContribution() {
        if (maintenanceContribution == null) {
            maintenanceContribution = new MaintenanceContribution();
        }
        return maintenanceContribution;
    }

    @Override
    public AbstractMilitaryInsurancePensions getMilitaryInsurancePensions() {
        if (militaryInsurancePensions == null) {
            militaryInsurancePensions = new MilitaryInsurancePensions();
        }
        return militaryInsurancePensions;
    }

    @Override
    public AbstractMotorVehicle getMotorVehicle() {
        if (motorVehicle == null) {
            motorVehicle = new MotorVehicle();
        }
        return motorVehicle;
    }

    @Override
    public AbstractOtherAssets getOtherAssets() {
        if (otherAssets == null) {
            otherAssets = new OtherAssets();
        }
        return otherAssets;
    }

    @Override
    public AbstractOtherPensions getOtherPensions() {
        if (otherPensions == null) {
            otherPensions = new OtherPensions();
        }
        return otherPensions;
    }

    @Override
    public AbstractPatentLicense getPatentLicense() {
        if (patentLicense == null) {
            patentLicense = new PatentLicense();
        }
        return patentLicense;
    }

    @Override
    public AbstractPensions getPensions() {
        if (pensions == null) {
            pensions = new Pensions();
        }
        return pensions;
    }

    @Override
    public AbstractPensionsPillar1 getPensionsPillar1() {
        if (pensionsPillar1 == null) {
            pensionsPillar1 = new PensionsPillar1();
        }
        return pensionsPillar1;
    }

    @Override
    public AbstractPensionsPillar2 getPensionsPillar2() {
        if (pensionsPillar2 == null) {
            pensionsPillar2 = new PensionsPillar2();
        }
        return pensionsPillar2;
    }

    @Override
    public AbstractPensionsPillar3a getPensionsPillar3a() {
        if (pensionsPillar3a == null) {
            pensionsPillar3a = new PensionsPillar3a();
        }
        return pensionsPillar3a;
    }

    @Override
    public AbstractPensionsPillar3b getPensionsPillar3b() {
        if (pensionsPillar3b == null) {
            pensionsPillar3b = new PensionsPillar3b();
        }
        return pensionsPillar3b;
    }

    @Override
    public AbstractPerDiemAllowance getPerDiemAllowance() {
        if (perDiemAllowance == null) {
            perDiemAllowance = new PerDiemAllowance();
        }
        return perDiemAllowance;
    }

    @Override
    public AbstractPrivateCash getPrivateCash() {
        if (privateCash == null) {
            privateCash = new PrivateCash();
        }
        return privateCash;
    }

    @Override
    public AbstractPrivateDebts getPrivateDebts() {
        if (privateDebts == null) {
            privateDebts = new PrivateDebts();
        }
        return privateDebts;
    }

    @Override
    public AbstractPrivateRealEstateProperties getPrivateRealEstateProperties() {
        if (privateRealEstateProperties == null) {
            privateRealEstateProperties = new PrivateRealEstateProperties();
        }
        return privateRealEstateProperties;
    }

    @Override
    public AbstractPrivateResidencyEntitlement getPrivateResidencyEntitlement() {
        if (residencyEntitlement == null) {
            residencyEntitlement = new PrivateResidencyEntitlement();
        }
        return residencyEntitlement;
    }

    @Override
    public AbstractPrivateSecurities getPrivateSecurities() {
        if (privateSecurities == null) {
            privateSecurities = new PrivateSecurities();
        }
        return privateSecurities;
    }

    @Override
    public AbstractRealEstateProperties getRealEstateProperties() {
        if (realEstateProperties == null) {
            realEstateProperties = new RealEstateProperties();
        }
        return realEstateProperties;
    }

    @Override
    public AbstractSecurities getSecurities() {
        if (securities == null) {
            securities = new Securities();
        }
        return securities;
    }

    @Override
    public AbstractTaxableIncomeDBG getTaxableIncomeDBG() {
        if (taxableIncomeDBG == null) {
            taxableIncomeDBG = new TaxableIncomeDBG();
        }
        return taxableIncomeDBG;
    }

    @Override
    public AbstractTaxableIncomeExpenseTaxation getTaxableIncomeExpenseTaxation() {
        if (taxableIncomeInAccordanceWithExpenseTaxation == null) {
            taxableIncomeInAccordanceWithExpenseTaxation = new TaxableIncomeExpenseTaxation();
        }
        return taxableIncomeInAccordanceWithExpenseTaxation;
    }

    @Override
    public AbstractTaxableIncomeInAccordanceWithDBG getTaxableIncomeInAccordanceWithDBG() {
        if (taxableIncomeInAccordanceWithDBG == null) {
            taxableIncomeInAccordanceWithDBG = new TaxableIncomeInAccordanceWithDBG();
        }
        return taxableIncomeInAccordanceWithDBG;
    }

    @Override
    public void setAnnuities(EAVSAbstractModel _annuities) {
        annuities = (Annuities) _annuities;

    }

    @Override
    public void setCash(EAVSAbstractModel _cash) {
        cash = (Cash) _cash;

    }

    @Override
    public void setChildAllowances(EAVSAbstractModel _childAllowances) {
        childAllowances = (ChildAllowances) _childAllowances;

    }

    @Override
    public void setCompanyShares(EAVSAbstractModel _companyShares) {
        companyShares = (CompanyShares) _companyShares;

    }

    @Override
    public void setDebts(EAVSAbstractModel _debts) {
        debts = (Debts) _debts;

    }

    @Override
    public void setInheritance(EAVSAbstractModel _inheritance) {
        inheritance = (Inheritance) _inheritance;

    }

    @Override
    public void setLifeInsurance(EAVSAbstractModel _lifeInsurance) {
        lifeInsurance = (LifeInsurance) _lifeInsurance;

    }

    @Override
    public void setMaintenanceContribution(EAVSAbstractModel _maintenanceContribution) {
        maintenanceContribution = (MaintenanceContribution) _maintenanceContribution;

    }

    @Override
    public void setMilitaryInsurancePensions(EAVSAbstractModel _militaryInsurancePensions) {
        militaryInsurancePensions = (MilitaryInsurancePensions) _militaryInsurancePensions;

    }

    @Override
    public void setMotorVehicle(EAVSAbstractModel _motorVehicle) {
        motorVehicle = (MotorVehicle) _motorVehicle;
    }

    @Override
    public void setOtherAssets(EAVSAbstractModel _otherAssets) {
        otherAssets = (OtherAssets) _otherAssets;
    }

    @Override
    public void setOtherPensions(EAVSAbstractModel _otherPensions) {
        otherPensions = (OtherPensions) _otherPensions;
    }

    @Override
    public void setPatentLicense(EAVSAbstractModel _patentLicense) {
        patentLicense = (PatentLicense) _patentLicense;

    }

    @Override
    public void setPensions(EAVSAbstractModel _pensions) {
        pensions = (Pensions) _pensions;
    }

    @Override
    public void setPensionsPillar1(EAVSAbstractModel _pensionsPillar1) {
        pensionsPillar1 = (PensionsPillar1) _pensionsPillar1;
    }

    @Override
    public void setPensionsPillar2(EAVSAbstractModel _pensionsPillar2) {
        pensionsPillar2 = (PensionsPillar2) _pensionsPillar2;
    }

    @Override
    public void setPensionsPillar3a(EAVSAbstractModel _pensionsPillar3a) {
        pensionsPillar3a = (PensionsPillar3a) _pensionsPillar3a;
    }

    @Override
    public void setPensionsPillar3b(EAVSAbstractModel _pensionsPillar3b) {
        pensionsPillar3b = (PensionsPillar3b) _pensionsPillar3b;
    }

    @Override
    public void setPerDiemAllowance(EAVSAbstractModel _perDiemAllowance) {
        perDiemAllowance = (PerDiemAllowance) _perDiemAllowance;

    }

    @Override
    public void setPrivateCash(EAVSAbstractModel _privateCash) {
        privateCash = (PrivateCash) _privateCash;
    }

    @Override
    public void setPrivateDebts(EAVSAbstractModel _privateDebts) {
        privateDebts = (PrivateDebts) _privateDebts;
    }

    @Override
    public void setPrivateRealEstateProperties(EAVSAbstractModel _privateRealEstateProperties) {
        privateRealEstateProperties = (PrivateRealEstateProperties) _privateRealEstateProperties;
    }

    @Override
    public void setPrivateResidencyEntitlement(EAVSAbstractModel _residencyEntitlement) {
        residencyEntitlement = (PrivateResidencyEntitlement) _residencyEntitlement;

    }

    @Override
    public void setPrivateSecurities(EAVSAbstractModel _privateSecurities) {
        privateSecurities = (PrivateSecurities) _privateSecurities;
    }

    @Override
    public void setRealEstateProperties(EAVSAbstractModel _realEstateProperties) {
        realEstateProperties = (RealEstateProperties) _realEstateProperties;

    }

    @Override
    public void setSecurities(EAVSAbstractModel _securities) {
        securities = (Securities) _securities;

    }

    @Override
    public void setTaxableIncomeDBG(EAVSAbstractModel _taxableIncomeDBG) {
        taxableIncomeDBG = (TaxableIncomeDBG) _taxableIncomeDBG;
    }

    @Override
    public void setTaxableIncomeExpenseTaxation(EAVSAbstractModel _taxableIncomeInAccordanceWithExpenseTaxation) {
        taxableIncomeInAccordanceWithExpenseTaxation = (TaxableIncomeExpenseTaxation) _taxableIncomeInAccordanceWithExpenseTaxation;

    }

    @Override
    public void setTaxableIncomeInAccordanceWithDBG(EAVSAbstractModel _taxableIncomeInAccordanceWithDBG) {
        taxableIncomeInAccordanceWithDBG = (TaxableIncomeInAccordanceWithDBG) _taxableIncomeInAccordanceWithDBG;

    }
}
