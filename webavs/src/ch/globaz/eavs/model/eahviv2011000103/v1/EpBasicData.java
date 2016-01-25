package ch.globaz.eavs.model.eahviv2011000103.v1;

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
    private IncomeFromSelfEmployment incomeFromSelfEmployment = null;
    private NonDomesticIncomePresent nonDomesticIncomePresent = null;

    @Override
    public AbstractAssets getAssets() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AbstractCapital getCapital() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List getChildren() {
        List result = new ArrayList();
        result.add(incomeFromSelfEmployment);
        result.add(nonDomesticIncomePresent);
        return result;
    }

    @Override
    public AbstractEmploymentIncome getEmploymentIncome() {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AbstractPensionIncome getPensionIncome() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AbstractPurchasingLPP getPurchasingLPP() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAssets(EAVSAbstractModel _assets) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setCapital(EAVSAbstractModel _capital) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setEmploymentIncome(EAVSAbstractModel _employmentIncome) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setIncomeFromSelfEmployment(EAVSAbstractModel _incomeFromSelfEmployment) {
        incomeFromSelfEmployment = (IncomeFromSelfEmployment) _incomeFromSelfEmployment;
    }

    @Override
    public void setIncomeInAgriculture(EAVSAbstractModel _incomeInAgriculture) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setNonDomesticIncomePresent(EAVSAbstractModel _nonDomesticIncomePresent) {
        nonDomesticIncomePresent = (NonDomesticIncomePresent) _nonDomesticIncomePresent;
    }

    @Override
    public void setOASIBridgingPension(EAVSAbstractModel _oasiBridgingPension) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPensionIncome(EAVSAbstractModel _pensionIncome) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPurchasingLPP(EAVSAbstractModel _purchasingLPP) {
        // TODO Auto-generated method stub

    }

}
