package ch.globaz.eavs.model.eahviv2011000103.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractSpBusinessData extends CommonModel implements EAVSNonFinalNode {
    public abstract AbstractAssets getAssets();

    public abstract AbstractBusinessAssets getBusinessAssets();

    public abstract AbstractBusinessCash getBusinessCash();

    public abstract AbstractBusinessDebts getBusinessDebts();

    public abstract AbstractBusinessRealEstateProperties getBusinessRealEstateProperties();

    public abstract AbstractBusinessResidencyEntitlement getBusinessResidencyEntitlement();

    public abstract AbstractBusinessSecurities getBusinessSecurities();

    public abstract AbstractCash getCash();

    public abstract AbstractCommencementOfSelfEmployment getCommencementOfSelfEmployment();

    public abstract AbstractCooperativeShares getCooperativeShares();

    public abstract AbstractDebtInterest getDebtInterest();

    public abstract AbstractDonations getDonations();

    public abstract AbstractEndOfSelfEmployment getEndOfSelfEmployment();

    public abstract AbstractIncomeRealEstate getIncomeRealEstate();

    public abstract AbstractMainIncome getMainIncome();

    public abstract AbstractMainIncomeInAgriculture getMainIncomeInAgriculture();

    public abstract AbstractMainIncomeInRealEstateTrade getMainIncomeInRealEstateTrade();

    public abstract AbstractOtherIncome getOtherIncome();

    public abstract AbstractPartTimeEmployment getPartTimeEmployment();

    public abstract AbstractPartTimeEmploymentInAgriculture getPartTimeEmploymentInAgriculture();

    public abstract AbstractPartTimeEmploymentInRealEstateTrade getPartTimeEmploymentInRealEstateTrade();

    public abstract AbstractRealEstateProperties getRealEstateProperties();

    public abstract AbstractRealisationProfit getRealisationProfit();

    public abstract AbstractPrivateResidencyEntitlement getResidencyEntitlement();

    public abstract AbstractSecurities getSecurities();

    public abstract AbstractSecuritiesIncome getSecuritiesIncome();

    public abstract AbstractTotalAssets getTotalAssets();

    public abstract void setAssets(EAVSAbstractModel _assets);

    public abstract void setBusinessAssets(EAVSAbstractModel _businessAssets);

    public abstract void setBusinessCash(EAVSAbstractModel _businessCash);

    public abstract void setBusinessDebts(EAVSAbstractModel _businessDebts);

    public abstract void setBusinessRealEstateProperties(EAVSAbstractModel _businessRealEstateProperties);

    public abstract void setBusinessResidencyEntitlement(EAVSAbstractModel _businessResidencyEntitlement);

    public abstract void setBusinessSecurities(EAVSAbstractModel _businessSecurities);

    public abstract void setCash(EAVSAbstractModel _cash);

    public abstract void setCommencementOfSelfEmployment(EAVSAbstractModel _commencementOfSelfEmployment);

    public abstract void setCooperativeShares(EAVSAbstractModel _cooperativeShares);

    public abstract void setDebtInterest(EAVSAbstractModel _debtInterest);

    public abstract void setDonations(EAVSAbstractModel _donations);

    public abstract void setEndOfSelfEmployment(EAVSAbstractModel _endOfSelfEmployment);

    public abstract void setIncomeRealEstate(EAVSAbstractModel _incomeRealEstate);

    public abstract void setMainIncome(EAVSAbstractModel _mainIncome);

    public abstract void setMainIncomeInAgriculture(EAVSAbstractModel _mainIncomeInAgriculture);

    public abstract void setMainIncomeInRealEstateTrade(EAVSAbstractModel _mainIncomeInRealEstateTrade);

    public abstract void setOtherIncome(EAVSAbstractModel _otherIncome);

    public abstract void setPartTimeEmployment(EAVSAbstractModel _partTimeEmployment);

    public abstract void setPartTimeEmploymentInAgriculture(EAVSAbstractModel _partTimeEmploymentInAgriculture);

    public abstract void setPartTimeEmploymentInRealEstateTrade(EAVSAbstractModel _partTimeEmploymentInRealEstateTrade);

    public abstract void setRealEstateProperties(EAVSAbstractModel _realEstateProperties);

    public abstract void setRealisationProfit(EAVSAbstractModel _realisationProfit);

    public abstract void setResidencyEntitlement(EAVSAbstractModel _residencyEntitlement);

    public abstract void setSecurities(EAVSAbstractModel _securities);

    public abstract void setSecuritiesIncome(EAVSAbstractModel _securitiesIncome);

    public abstract void setTotalAssets(EAVSAbstractModel _totalAssets);

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }
}
