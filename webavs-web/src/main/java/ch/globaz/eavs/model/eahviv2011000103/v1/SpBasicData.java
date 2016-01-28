package ch.globaz.eavs.model.eahviv2011000103.v1;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.eahviv2011000101.common.AbstractSpouse;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractAssets;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractCapital;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractEmploymentIncome;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractIncomeFromParticipations;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractIncomeFromSelfEmployment;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractIncomeInAgriculture;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractNonDomesticIncomePresent;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractOASIBridgingPension;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractOASIContribution;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractPensionIncome;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractPurchasingLPP;
import ch.globaz.eavs.model.eahviv2011000103.common.AbstractSpBasicData;

public class SpBasicData extends AbstractSpBasicData {
    private Assets assets = null;
    private OASIBridgingPension bridgingPension = null;
    private Capital capital = null;
    private EmploymentIncome employmentIncome = null;
    private IncomeFromParticipations incomeFromParticipations = null;
    private IncomeFromSelfEmployment incomeFromSelfEmployment = null;
    private IncomeInAgriculture incomeInAgriculture = null;
    private NonDomesticIncomePresent nonDomesticIncomePresent = null;
    private OASIContribution oasiContribution = null;
    private PensionIncome pensionIncome = null;
    private PurchasingLPP purchasingLPP = null;
    private Spouse spouse = null;

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
        result.add(pensionIncome);
        result.add(capital);
        result.add(assets);
        result.add(oasiContribution);
        result.add(nonDomesticIncomePresent);
        result.add(incomeFromParticipations);
        result.add(spouse);
        result.add(employmentIncome);
        result.add(incomeInAgriculture);
        result.add(purchasingLPP);
        result.add(bridgingPension);
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
    public AbstractIncomeFromParticipations getIncomeFromParticipations() {
        if (incomeFromParticipations == null) {
            incomeFromParticipations = new IncomeFromParticipations();
        }
        return incomeFromParticipations;
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
        if (bridgingPension == null) {
            bridgingPension = new OASIBridgingPension();
        }
        return bridgingPension;
    }

    @Override
    public AbstractOASIContribution getOASIContribution() {
        if (oasiContribution == null) {
            oasiContribution = new OASIContribution();
        }
        return oasiContribution;
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

    public AbstractSpouse getSpouse() {
        if (spouse == null) {
            spouse = new Spouse();
        }
        return spouse;
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
    public void setIncomeFromParticipations(EAVSAbstractModel _incomeFromParticipations) {
        incomeFromParticipations = (IncomeFromParticipations) _incomeFromParticipations;
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
    public void setOASIBridgingPension(EAVSAbstractModel _bridgingPension) {
        bridgingPension = (OASIBridgingPension) _bridgingPension;
    }

    @Override
    public void setOASIContribution(EAVSAbstractModel _oasiContribution) {
        oasiContribution = (OASIContribution) _oasiContribution;
    }

    @Override
    public void setPensionIncome(EAVSAbstractModel _pensionIncome) {
        pensionIncome = (PensionIncome) _pensionIncome;
    }

    @Override
    public void setPurchasingLPP(EAVSAbstractModel _purchasingLPP) {
        purchasingLPP = (PurchasingLPP) _purchasingLPP;
    }

    public void setSpouse(EAVSAbstractModel _spouse) {
        spouse = (Spouse) _spouse;
    }

}
