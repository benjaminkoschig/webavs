package ch.globaz.eavs.model.eahviv2011000103.v2;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractAssets;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractCapital;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractEmploymentIncome;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractEpBasicData;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractIncomeFromSelfEmployment;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractIncomeInAgriculture;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractNonDomesticIncomePresent;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractOASIBridgingPension;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractPensionIncome;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractPurchasingLPP;

public class EpBasicData extends AbstractEpBasicData {
    private Assets assets = null;
    private Capital capital = null;
    private EmploymentIncome employmentIncome = null;
    private IncomeFromSelfEmployment incomeFromSelfEmployment = null;
    private IncomeInAgriculture incomeInAgriculture = null;
    private NonDomesticIncomePresent nonDomesticIncomePresent = null;
    private OASIBridgingPension OASIBridgingPension = null;
    private PensionIncome pensionIncome = null;
    private PurchasingLPP purchasingLPP = null;

    @Override
    public AbstractAssets getAssets() {
        if (assets == null) {
            assets = new Assets();
        }
        return assets;
    }

    @Override
    public AbstractCapital getCapital() {
        if (capital == null) {
            capital = new Capital();
        }
        return capital;
    }

    @Override
    public List getChildren() {
        List result = new ArrayList();
        result.add(incomeFromSelfEmployment);
        result.add(nonDomesticIncomePresent);
        result.add(employmentIncome);
        result.add(pensionIncome);
        result.add(incomeInAgriculture);
        result.add(capital);
        result.add(assets);
        result.add(purchasingLPP);
        result.add(OASIBridgingPension);
        return result;
    }

    @Override
    public AbstractEmploymentIncome getEmploymentIncome() {
        if (employmentIncome == null) {
            employmentIncome = new EmploymentIncome();
        }
        return employmentIncome;
    }

    @Override
    public AbstractIncomeFromSelfEmployment getIncomeFromSelfEmployment() {
        if (incomeFromSelfEmployment == null) {
            incomeFromSelfEmployment = new IncomeFromSelfEmployment();
        }
        return incomeFromSelfEmployment;
    }

    @Override
    public AbstractIncomeInAgriculture getIncomeInAgriculture() {
        if (incomeInAgriculture == null) {
            incomeInAgriculture = new IncomeInAgriculture();
        }
        return incomeInAgriculture;
    }

    @Override
    public AbstractNonDomesticIncomePresent getNonDomesticIncomePresent() {
        if (nonDomesticIncomePresent == null) {
            nonDomesticIncomePresent = new NonDomesticIncomePresent();
        }
        return nonDomesticIncomePresent;
    }

    @Override
    public AbstractOASIBridgingPension getOASIBridgingPension() {
        if (OASIBridgingPension == null) {
            OASIBridgingPension = new OASIBridgingPension();
        }
        return OASIBridgingPension;
    }

    @Override
    public AbstractPensionIncome getPensionIncome() {
        if (pensionIncome == null) {
            pensionIncome = new PensionIncome();
        }
        return pensionIncome;
    }

    @Override
    public AbstractPurchasingLPP getPurchasingLPP() {
        if (purchasingLPP == null) {
            purchasingLPP = new PurchasingLPP();
        }
        return purchasingLPP;
    }

    @Override
    public void setAssets(EAVSAbstractModel _assets) {
        assets = (Assets) _assets;
    }

    @Override
    public void setCapital(EAVSAbstractModel _capital) {
        capital = (Capital) _capital;
    }

    @Override
    public void setEmploymentIncome(EAVSAbstractModel _employmentIncome) {
        employmentIncome = (EmploymentIncome) _employmentIncome;
    }

    @Override
    public void setIncomeFromSelfEmployment(EAVSAbstractModel _incomeFromSelfEmployment) {
        incomeFromSelfEmployment = (IncomeFromSelfEmployment) _incomeFromSelfEmployment;
    }

    @Override
    public void setIncomeInAgriculture(EAVSAbstractModel _incomeInAgriculture) {
        incomeInAgriculture = (IncomeInAgriculture) _incomeInAgriculture;
    }

    @Override
    public void setNonDomesticIncomePresent(EAVSAbstractModel _nonDomesticIncomePresent) {
        nonDomesticIncomePresent = (NonDomesticIncomePresent) _nonDomesticIncomePresent;
    }

    @Override
    public void setOASIBridgingPension(EAVSAbstractModel _OASIBridgingPension) {
        OASIBridgingPension = (OASIBridgingPension) _OASIBridgingPension;
    }

    @Override
    public void setPensionIncome(EAVSAbstractModel _pensionIncome) {
        pensionIncome = (PensionIncome) _pensionIncome;
    }

    @Override
    public void setPurchasingLPP(EAVSAbstractModel _purchasingLPP) {
        purchasingLPP = (PurchasingLPP) _purchasingLPP;
    }
}
