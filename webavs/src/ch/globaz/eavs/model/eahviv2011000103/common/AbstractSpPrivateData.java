package ch.globaz.eavs.model.eahviv2011000103.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractSpPrivateData extends CommonModel implements EAVSNonFinalNode {
    public abstract AbstractAnnuities getAnnuities();

    public abstract AbstractCash getCash();

    public abstract AbstractChildAllowances getChildAllowances();

    public abstract AbstractCompanyShares getCompanyShares();

    public abstract AbstractDebts getDebts();

    public abstract AbstractInheritance getInheritance();

    public abstract AbstractLifeInsurance getLifeInsurance();

    public abstract AbstractMaintenanceContribution getMaintenanceContribution();

    public abstract AbstractMilitaryInsurancePensions getMilitaryInsurancePensions();

    public abstract AbstractMotorVehicle getMotorVehicle();

    public abstract AbstractOtherAssets getOtherAssets();

    public abstract AbstractOtherPensions getOtherPensions();

    public abstract AbstractPatentLicense getPatentLicense();

    public abstract AbstractPensions getPensions();

    public abstract AbstractPensionsPillar1 getPensionsPillar1();

    public abstract AbstractPensionsPillar2 getPensionsPillar2();

    public abstract AbstractPensionsPillar3a getPensionsPillar3a();

    public abstract AbstractPensionsPillar3b getPensionsPillar3b();

    public abstract AbstractPerDiemAllowance getPerDiemAllowance();

    public abstract AbstractPrivateCash getPrivateCash();

    public abstract AbstractPrivateDebts getPrivateDebts();

    public abstract AbstractPrivateRealEstateProperties getPrivateRealEstateProperties();

    public abstract AbstractPrivateResidencyEntitlement getPrivateResidencyEntitlement();

    public abstract AbstractPrivateSecurities getPrivateSecurities();

    public abstract AbstractRealEstateProperties getRealEstateProperties();

    public abstract AbstractSecurities getSecurities();

    public abstract AbstractTaxableIncomeDBG getTaxableIncomeDBG();

    public abstract AbstractTaxableIncomeExpenseTaxation getTaxableIncomeExpenseTaxation();

    public abstract AbstractTaxableIncomeInAccordanceWithDBG getTaxableIncomeInAccordanceWithDBG();

    public abstract void setAnnuities(EAVSAbstractModel _annuities);

    public abstract void setCash(EAVSAbstractModel _cash);

    public abstract void setChildAllowances(EAVSAbstractModel _childAllowances);

    public abstract void setCompanyShares(EAVSAbstractModel _companyShares);

    public abstract void setDebts(EAVSAbstractModel _debts);

    public abstract void setInheritance(EAVSAbstractModel _inheritance);

    public abstract void setLifeInsurance(EAVSAbstractModel _lifeInsurance);

    public abstract void setMaintenanceContribution(EAVSAbstractModel _maintenanceContribution);

    public abstract void setMilitaryInsurancePensions(EAVSAbstractModel _militaryInsurancePensions);

    public abstract void setMotorVehicle(EAVSAbstractModel _motorVehicle);

    public abstract void setOtherAssets(EAVSAbstractModel _otherAssets);

    public abstract void setOtherPensions(EAVSAbstractModel _otherPensions);

    public abstract void setPatentLicense(EAVSAbstractModel _PatentLicense);

    public abstract void setPensions(EAVSAbstractModel _pensions);

    public abstract void setPensionsPillar1(EAVSAbstractModel _pensionsPillar1);

    public abstract void setPensionsPillar2(EAVSAbstractModel _pensionsPillar2);

    public abstract void setPensionsPillar3a(EAVSAbstractModel _pensionsPillar3a);

    public abstract void setPensionsPillar3b(EAVSAbstractModel _pensionsPillar3b);

    public abstract void setPerDiemAllowance(EAVSAbstractModel _perDiemAllowance);

    public abstract void setPrivateCash(EAVSAbstractModel _privateCash);

    public abstract void setPrivateDebts(EAVSAbstractModel _privateDebts);

    public abstract void setPrivateRealEstateProperties(EAVSAbstractModel _privateRealEstateProperties);

    public abstract void setPrivateResidencyEntitlement(EAVSAbstractModel _residencyEntitlement);

    public abstract void setPrivateSecurities(EAVSAbstractModel _privateSecurities);

    public abstract void setRealEstateProperties(EAVSAbstractModel _realEstateProperties);

    public abstract void setSecurities(EAVSAbstractModel _securities);

    public abstract void setTaxableIncomeDBG(EAVSAbstractModel _taxableIncomeDBG);

    public abstract void setTaxableIncomeExpenseTaxation(EAVSAbstractModel _taxableIncomeInAccordanceWithExpenseTaxation);

    public abstract void setTaxableIncomeInAccordanceWithDBG(EAVSAbstractModel _axableIncomeInAccordanceWithDBG);

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }

}
