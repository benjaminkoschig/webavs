package ch.globaz.eavs.model.eahviv2011000103.v1;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractAssets;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractBusinessAssets;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractBusinessCash;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractBusinessDebts;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractBusinessRealEstateProperties;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractBusinessResidencyEntitlement;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractBusinessSecurities;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractCash;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractCommencementOfSelfEmployment;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractCooperativeShares;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractDebtInterest;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractDonations;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractEndOfSelfEmployment;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractIncomeRealEstate;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractMainIncome;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractMainIncomeInAgriculture;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractMainIncomeInRealEstateTrade;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractOtherIncome;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractPartTimeEmployment;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractPartTimeEmploymentInAgriculture;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractPartTimeEmploymentInRealEstateTrade;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractPrivateResidencyEntitlement;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractRealEstateProperties;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractRealisationProfit;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractSecurities;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractSecuritiesIncome;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractSpBusinessData;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractTotalAssets;

public class SpBusinessData extends AbstractSpBusinessData {
    private Assets assets = null;
    private BusinessAssets businessAssets = null;
    private BusinessCash businessCash = null;
    private BusinessDebts businessDebts = null;
    private BusinessRealEstateProperties businessRealEstateProperties = null;
    private BusinessResidencyEntitlement businessResidencyEntitlement = null;
    private BusinessSecurities businessSecurities = null;
    private Cash cash = null;
    private CommencementOfSelfEmployment commencementOfSelfEmployment = null;
    private CooperativeShares cooperativeShares = null;
    private DebtInterest debtInterest = null;
    private Donations donations = null;
    private EndOfSelfEmployment endOfSelfEmployment = null;
    private IncomeRealEstate incomeRealEstate = null;
    private MainIncome mainIncome = null;
    private MainIncomeInAgriculture mainIncomeInAgriculture = null;
    private MainIncomeInRealEstateTrade mainIncomeInRealEstateTrade = null;
    private OtherIncome otherIncome = null;
    private PartTimeEmployment partTimeEmployment = null;
    private PartTimeEmploymentInAgriculture partTimeEmploymentInAgriculture = null;
    private PartTimeEmploymentInRealEstateTrade partTimeEmploymentInRealEstateTrade = null;
    private RealEstateProperties realEstateProperties = null;
    private RealisationProfit realisationProfit = null;
    private PrivateResidencyEntitlement residencyEntitlement = null;
    private Securities securities = null;
    private SecuritiesIncome securitiesIncome = null;
    private TotalAssets totalAssets = null;

    @Override
    public AbstractAssets getAssets() {
        if (assets == null) {
            assets = new Assets();
        }
        return assets;
    }

    @Override
    public AbstractBusinessAssets getBusinessAssets() {
        if (businessAssets == null) {
            businessAssets = new BusinessAssets();
        }
        return businessAssets;
    }

    @Override
    public AbstractBusinessCash getBusinessCash() {
        if (businessCash == null) {
            businessCash = new BusinessCash();
        }
        return businessCash;
    }

    @Override
    public AbstractBusinessDebts getBusinessDebts() {
        if (businessDebts == null) {
            businessDebts = new BusinessDebts();
        }
        return businessDebts;
    }

    @Override
    public AbstractBusinessRealEstateProperties getBusinessRealEstateProperties() {
        if (businessRealEstateProperties == null) {
            businessRealEstateProperties = new BusinessRealEstateProperties();
        }
        return businessRealEstateProperties;
    }

    @Override
    public AbstractBusinessResidencyEntitlement getBusinessResidencyEntitlement() {
        if (businessResidencyEntitlement == null) {
            businessResidencyEntitlement = new BusinessResidencyEntitlement();
        }
        return businessResidencyEntitlement;
    }

    @Override
    public AbstractBusinessSecurities getBusinessSecurities() {
        if (businessSecurities == null) {
            businessSecurities = new BusinessSecurities();
        }
        return businessSecurities;
    }

    @Override
    public AbstractCash getCash() {
        if (cash == null) {
            cash = new Cash();
        }
        return cash;
    }

    @Override
    public List getChildren() {
        List result = new ArrayList();
        result.add(commencementOfSelfEmployment);
        result.add(mainIncome);
        result.add(securitiesIncome);
        result.add(otherIncome);
        result.add(debtInterest);
        result.add(donations);
        result.add(businessDebts);
        result.add(mainIncomeInAgriculture);
        result.add(mainIncomeInRealEstateTrade);
        result.add(partTimeEmployment);
        result.add(partTimeEmploymentInAgriculture);
        result.add(partTimeEmploymentInRealEstateTrade);
        result.add(realisationProfit);
        result.add(cooperativeShares);
        result.add(residencyEntitlement);
        result.add(incomeRealEstate);
        result.add(securities);
        result.add(cash);
        result.add(assets);
        result.add(realEstateProperties);
        result.add(totalAssets);
        result.add(businessResidencyEntitlement);
        result.add(businessSecurities);
        result.add(businessCash);
        result.add(businessAssets);
        result.add(businessRealEstateProperties);
        return result;
    }

    @Override
    public AbstractCommencementOfSelfEmployment getCommencementOfSelfEmployment() {
        if (commencementOfSelfEmployment == null) {
            commencementOfSelfEmployment = new CommencementOfSelfEmployment();
        }
        return commencementOfSelfEmployment;
    }

    @Override
    public AbstractCooperativeShares getCooperativeShares() {
        if (cooperativeShares == null) {
            cooperativeShares = new CooperativeShares();
        }
        return cooperativeShares;
    }

    @Override
    public AbstractDebtInterest getDebtInterest() {
        if (debtInterest == null) {
            debtInterest = new DebtInterest();
        }
        return debtInterest;
    }

    @Override
    public AbstractDonations getDonations() {
        if (donations == null) {
            donations = new Donations();
        }
        return donations;
    }

    @Override
    public AbstractEndOfSelfEmployment getEndOfSelfEmployment() {
        if (endOfSelfEmployment == null) {
            endOfSelfEmployment = new EndOfSelfEmployment();
        }
        return endOfSelfEmployment;
    }

    @Override
    public AbstractIncomeRealEstate getIncomeRealEstate() {
        if (incomeRealEstate == null) {
            incomeRealEstate = new IncomeRealEstate();
        }
        return incomeRealEstate;
    }

    @Override
    public AbstractMainIncome getMainIncome() {
        if (mainIncome == null) {
            mainIncome = new MainIncome();
        }
        return mainIncome;
    }

    @Override
    public AbstractMainIncomeInAgriculture getMainIncomeInAgriculture() {
        if (mainIncomeInAgriculture == null) {
            mainIncomeInAgriculture = new MainIncomeInAgriculture();
        }
        return mainIncomeInAgriculture;
    }

    @Override
    public AbstractMainIncomeInRealEstateTrade getMainIncomeInRealEstateTrade() {
        if (mainIncomeInRealEstateTrade == null) {
            mainIncomeInRealEstateTrade = new MainIncomeInRealEstateTrade();
        }
        return mainIncomeInRealEstateTrade;
    }

    @Override
    public AbstractOtherIncome getOtherIncome() {
        if (otherIncome == null) {
            otherIncome = new OtherIncome();
        }
        return otherIncome;
    }

    @Override
    public AbstractPartTimeEmployment getPartTimeEmployment() {
        if (partTimeEmployment == null) {
            partTimeEmployment = new PartTimeEmployment();
        }
        return partTimeEmployment;
    }

    @Override
    public AbstractPartTimeEmploymentInAgriculture getPartTimeEmploymentInAgriculture() {
        if (partTimeEmploymentInAgriculture == null) {
            partTimeEmploymentInAgriculture = new PartTimeEmploymentInAgriculture();
        }
        return partTimeEmploymentInAgriculture;
    }

    @Override
    public AbstractPartTimeEmploymentInRealEstateTrade getPartTimeEmploymentInRealEstateTrade() {
        if (partTimeEmploymentInRealEstateTrade == null) {
            partTimeEmploymentInRealEstateTrade = new PartTimeEmploymentInRealEstateTrade();
        }
        return partTimeEmploymentInRealEstateTrade;
    }

    @Override
    public AbstractRealEstateProperties getRealEstateProperties() {
        if (realEstateProperties == null) {
            realEstateProperties = new RealEstateProperties();
        }
        return realEstateProperties;
    }

    @Override
    public AbstractRealisationProfit getRealisationProfit() {
        if (realisationProfit == null) {
            realisationProfit = new RealisationProfit();
        }
        return realisationProfit;
    }

    @Override
    public AbstractPrivateResidencyEntitlement getResidencyEntitlement() {
        if (residencyEntitlement == null) {
            residencyEntitlement = new PrivateResidencyEntitlement();
        }
        return residencyEntitlement;
    }

    @Override
    public AbstractSecurities getSecurities() {
        if (securities == null) {
            securities = new Securities();
        }
        return securities;
    }

    @Override
    public AbstractSecuritiesIncome getSecuritiesIncome() {
        if (securitiesIncome == null) {
            securitiesIncome = new SecuritiesIncome();
        }
        return securitiesIncome;
    }

    @Override
    public AbstractTotalAssets getTotalAssets() {
        if (totalAssets == null) {
            totalAssets = new TotalAssets();
        }
        return totalAssets;
    }

    @Override
    public void setAssets(EAVSAbstractModel _assets) {
        assets = (Assets) _assets;
    }

    @Override
    public void setBusinessAssets(EAVSAbstractModel _businessAssets) {
        businessAssets = (BusinessAssets) _businessAssets;
    }

    @Override
    public void setBusinessCash(EAVSAbstractModel _businessCash) {
        businessCash = (BusinessCash) _businessCash;
    }

    @Override
    public void setBusinessDebts(EAVSAbstractModel _businessDebts) {
        businessDebts = (BusinessDebts) _businessDebts;
    }

    @Override
    public void setBusinessRealEstateProperties(EAVSAbstractModel _businessRealEstateProperties) {
        businessRealEstateProperties = (BusinessRealEstateProperties) _businessRealEstateProperties;
    }

    @Override
    public void setBusinessResidencyEntitlement(EAVSAbstractModel _businessResidencyEntitlement) {
        businessResidencyEntitlement = (BusinessResidencyEntitlement) _businessResidencyEntitlement;
    }

    @Override
    public void setBusinessSecurities(EAVSAbstractModel _businessSecurities) {
        businessSecurities = (BusinessSecurities) _businessSecurities;
    }

    @Override
    public void setCash(EAVSAbstractModel _cash) {
        cash = (Cash) _cash;
    }

    @Override
    public void setCommencementOfSelfEmployment(EAVSAbstractModel _commencementOfSelfEmployment) {
        commencementOfSelfEmployment = (CommencementOfSelfEmployment) _commencementOfSelfEmployment;
    }

    @Override
    public void setCooperativeShares(EAVSAbstractModel _cooperativeShares) {
        cooperativeShares = (CooperativeShares) _cooperativeShares;
    }

    @Override
    public void setDebtInterest(EAVSAbstractModel _debtInterest) {
        debtInterest = (DebtInterest) _debtInterest;
    }

    @Override
    public void setDonations(EAVSAbstractModel _donations) {
        donations = (Donations) _donations;
    }

    @Override
    public void setEndOfSelfEmployment(EAVSAbstractModel _endOfSelfEmployment) {
        endOfSelfEmployment = (EndOfSelfEmployment) _endOfSelfEmployment;
    }

    @Override
    public void setIncomeRealEstate(EAVSAbstractModel _incomeRealEstate) {
        incomeRealEstate = (IncomeRealEstate) _incomeRealEstate;
    }

    @Override
    public void setMainIncome(EAVSAbstractModel _mainIncome) {
        mainIncome = (MainIncome) _mainIncome;
    }

    @Override
    public void setMainIncomeInAgriculture(EAVSAbstractModel _mainIncomeInAgriculture) {
        mainIncomeInAgriculture = (MainIncomeInAgriculture) _mainIncomeInAgriculture;
    }

    @Override
    public void setMainIncomeInRealEstateTrade(EAVSAbstractModel _mainIncomeInRealEstateTrade) {
        mainIncomeInRealEstateTrade = (MainIncomeInRealEstateTrade) _mainIncomeInRealEstateTrade;
    }

    @Override
    public void setOtherIncome(EAVSAbstractModel _otherIncome) {
        otherIncome = (OtherIncome) _otherIncome;
    }

    @Override
    public void setPartTimeEmployment(EAVSAbstractModel _partTimeEmployment) {
        partTimeEmployment = (PartTimeEmployment) _partTimeEmployment;
    }

    @Override
    public void setPartTimeEmploymentInAgriculture(EAVSAbstractModel _partTimeEmploymentInAgriculture) {
        partTimeEmploymentInAgriculture = (PartTimeEmploymentInAgriculture) _partTimeEmploymentInAgriculture;
    }

    @Override
    public void setPartTimeEmploymentInRealEstateTrade(EAVSAbstractModel _partTimeEmploymentInRealEstateTrade) {
        partTimeEmploymentInRealEstateTrade = (PartTimeEmploymentInRealEstateTrade) _partTimeEmploymentInRealEstateTrade;
    }

    @Override
    public void setRealEstateProperties(EAVSAbstractModel _realEstateProperties) {
        realEstateProperties = (RealEstateProperties) _realEstateProperties;
    }

    @Override
    public void setRealisationProfit(EAVSAbstractModel _realisationProfit) {
        realisationProfit = (RealisationProfit) _realisationProfit;
    }

    @Override
    public void setResidencyEntitlement(EAVSAbstractModel _residencyEntitlement) {
        residencyEntitlement = (PrivateResidencyEntitlement) _residencyEntitlement;
    }

    @Override
    public void setSecurities(EAVSAbstractModel _securities) {
        securities = (Securities) _securities;
    }

    @Override
    public void setSecuritiesIncome(EAVSAbstractModel _securitiesIncome) {
        securitiesIncome = (SecuritiesIncome) _securitiesIncome;
    }

    @Override
    public void setTotalAssets(EAVSAbstractModel _totalAssets) {
        totalAssets = (TotalAssets) _totalAssets;
    }

}
