package ch.globaz.eavs.model.eahviv2011000103.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;

public abstract class AbstractEpBasicData extends CommonModel implements EAVSNonFinalNode {
    public abstract AbstractAssets getAssets();

    public abstract AbstractCapital getCapital();

    public abstract AbstractEmploymentIncome getEmploymentIncome();

    public abstract AbstractIncomeFromSelfEmployment getIncomeFromSelfEmployment();

    public abstract AbstractIncomeInAgriculture getIncomeInAgriculture();

    public abstract AbstractNonDomesticIncomePresent getNonDomesticIncomePresent();

    public abstract AbstractOASIBridgingPension getOASIBridgingPension();

    public abstract AbstractPensionIncome getPensionIncome();

    public abstract AbstractPurchasingLPP getPurchasingLPP();

    public abstract void setAssets(EAVSAbstractModel _assets);

    public abstract void setCapital(EAVSAbstractModel _capital);

    public abstract void setEmploymentIncome(EAVSAbstractModel _employmentIncome);

    public abstract void setIncomeFromSelfEmployment(EAVSAbstractModel _incomeFromSelfEmployment);

    public abstract void setIncomeInAgriculture(EAVSAbstractModel _incomeInAgriculture);

    public abstract void setNonDomesticIncomePresent(EAVSAbstractModel _nonDomesticIncomePresent);

    public abstract void setOASIBridgingPension(EAVSAbstractModel _oasiBridgingPension);

    public abstract void setPensionIncome(EAVSAbstractModel _pensionIncome);

    public abstract void setPurchasingLPP(EAVSAbstractModel _purchasingLPP);

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }
}
