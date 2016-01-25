package globaz.phenix.db.communications;

import globaz.globall.db.BEntity;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;

public class CPSedexDonneesBase extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String assets = "";
    private String assetsCjt = "";
    private String capital = "";
    private String capitalCjt = "";
    private String employmentIncome = "";
    private String employmentIncomeCjt = "";
    private String idDonneesBase = "";
    private String idRetour = "";
    private String incomeFromSelfEmployment = "";
    private String incomeFromSelfEmploymentCjt = "";
    private boolean isForBackup = false;
    private boolean isForRetourOriginale = false;
    private String mainIncomeInAgriculture = "";
    private String mainIncomeInAgricultureCjt = "";
    private String nonDomesticIncomePresent = "";
    private String nonDomesticIncomePresentCjt = "";
    private String OASIBridgingPension = "";
    private String OASIBridgingPensionCjt = "";
    private String pensionIncome = "";
    private String pensionIncomeCjt = "";
    private String purchasingLPP = "";
    private String purchasingLPPCjt = "";

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        setIdDonneesBase(this._incCounter(transaction, "0"));
    }

    @Override
    protected String _getTableName() {
        if (!isForBackup()) {
            return "CPDONBA";
        } else {
            return "CPDONBB";
        }
    }

    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idRetour = statement.dbReadNumeric("IKIRET");
        idDonneesBase = statement.dbReadNumeric("SEDBAID");
        employmentIncome = statement.dbReadString("SEREVNT");
        incomeFromSelfEmployment = statement.dbReadString("SEMONTA");
        pensionIncome = statement.dbReadString("SERENTE");
        mainIncomeInAgriculture = statement.dbReadString("SEREVAC");
        capital = statement.dbReadString("SECAPIN");
        assets = statement.dbReadString("SEASSET");
        nonDomesticIncomePresent = statement.dbReadString("SEREVET");
        purchasingLPP = statement.dbReadString("SEACLPP");
        OASIBridgingPension = statement.dbReadString("SEOASIB");
        // Champs conjoint
        employmentIncomeCjt = statement.dbReadString("SCREVNT");
        incomeFromSelfEmploymentCjt = statement.dbReadString("SCMONTA");
        pensionIncomeCjt = statement.dbReadString("SCRENTE");
        mainIncomeInAgricultureCjt = statement.dbReadString("SCREVAC");
        capitalCjt = statement.dbReadString("SCCAPIN");
        assetsCjt = statement.dbReadString("SCASSET");
        nonDomesticIncomePresentCjt = statement.dbReadString("SCREVET");
        purchasingLPPCjt = statement.dbReadString("SCACLPP");
        OASIBridgingPensionCjt = statement.dbReadString("SCOASIB");
    }

    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IKIRET", this._dbWriteNumeric(statement.getTransaction(), idRetour, "idRetour"));
    }

    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IKIRET",
                this._dbWriteNumeric(statement.getTransaction(), idRetour, "idCommunicationRetour"));
        if (isForRetourOriginale == false) {
            // Ne pas mettre à jour car cette zone a été défini par erreur comme clé primaire... sinon pb d'intégrité
            statement.writeField("SEDBAID",
                    this._dbWriteNumeric(statement.getTransaction(), idDonneesBase, "idDonneesBase"));
        }
        statement.writeField("SEREVNT", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(employmentIncome), "employmentIncome"));
        statement.writeField("SEMONTA", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(incomeFromSelfEmployment), "incomeFromSelfEmployment"));
        statement.writeField("SERENTE", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(pensionIncome), "pensionIncome"));
        statement.writeField("SEREVAC", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(mainIncomeInAgriculture), "mainIncomeInAgriculture"));
        statement.writeField("SECAPIN",
                this._dbWriteString(statement.getTransaction(), JANumberFormatter.deQuote(capital), "capital"));
        statement.writeField("SEASSET",
                this._dbWriteString(statement.getTransaction(), JANumberFormatter.deQuote(assets), "assets"));
        statement.writeField("SEREVET",
                this._dbWriteString(statement.getTransaction(), nonDomesticIncomePresent, "nonDomesticIncomePresent"));
        statement.writeField("SEACLPP", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(purchasingLPP), "purchasingLPP"));
        statement.writeField("SEOASIB", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(OASIBridgingPension), "OASIBridgingPension"));
        // Champs conjoint
        statement.writeField("SCREVNT", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(employmentIncomeCjt), "employmentIncomeCjt"));
        statement.writeField("SCMONTA", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(incomeFromSelfEmploymentCjt), "incomeFromSelfEmploymentCjt"));
        statement.writeField("SCRENTE", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(pensionIncomeCjt), "pensionIncomeCjt"));
        statement.writeField("SCREVAC", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(mainIncomeInAgricultureCjt), "mainIncomeInAgricultureCjt"));
        statement.writeField("SCCAPIN",
                this._dbWriteString(statement.getTransaction(), JANumberFormatter.deQuote(capitalCjt), "capitalCjt"));
        statement.writeField("SCASSET",
                this._dbWriteString(statement.getTransaction(), JANumberFormatter.deQuote(assetsCjt), "assetsCjt"));
        statement.writeField("SCREVET", this._dbWriteString(statement.getTransaction(), nonDomesticIncomePresentCjt,
                "nonDomesticIncomePresentCjt"));
        statement.writeField("SCACLPP", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(purchasingLPPCjt), "purchasingLPPCjt"));
        statement.writeField("SCOASIB", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(OASIBridgingPensionCjt), "OASIBridgingPensionCjt"));
    }

    public String getAssets() {
        if (!JadeStringUtil.isBlankOrZero(assets)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(assets), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getAssetsCjt() {
        if (!JadeStringUtil.isBlankOrZero(assetsCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(assetsCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getCapital() {
        if (!JadeStringUtil.isBlankOrZero(capital)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(capital), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getCapitalCjt() {
        if (!JadeStringUtil.isBlankOrZero(capitalCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(capitalCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getEmploymentIncome() {
        if (!JadeStringUtil.isBlankOrZero(employmentIncome)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(employmentIncome), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getEmploymentIncomeCjt() {
        if (!JadeStringUtil.isBlankOrZero(employmentIncomeCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(employmentIncomeCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getIdDonneesBase() {
        return idDonneesBase;
    }

    public String getIdRetour() {
        return idRetour;
    }

    public String getIncomeFromSelfEmployment() {
        if (!JadeStringUtil.isBlankOrZero(incomeFromSelfEmployment)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(incomeFromSelfEmployment), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getIncomeFromSelfEmploymentCjt() {
        if (!JadeStringUtil.isBlankOrZero(incomeFromSelfEmploymentCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(incomeFromSelfEmploymentCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getMainIncomeInAgriculture() {
        if (!JadeStringUtil.isBlankOrZero(mainIncomeInAgriculture)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(mainIncomeInAgriculture), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getMainIncomeInAgricultureCjt() {
        if (!JadeStringUtil.isBlankOrZero(mainIncomeInAgricultureCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(mainIncomeInAgricultureCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getNonDomesticIncomePresent() {
        return nonDomesticIncomePresent;
    }

    public String getNonDomesticIncomePresentCjt() {
        return nonDomesticIncomePresentCjt;
    }

    public String getOASIBridgingPension() {
        if (!JadeStringUtil.isBlankOrZero(OASIBridgingPension)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(OASIBridgingPension), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getOASIBridgingPensionCjt() {
        if (!JadeStringUtil.isBlankOrZero(OASIBridgingPensionCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(OASIBridgingPensionCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getPensionIncome() {
        if (!JadeStringUtil.isBlankOrZero(pensionIncome)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(pensionIncome), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getPensionIncomeCjt() {
        if (!JadeStringUtil.isBlankOrZero(pensionIncomeCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(pensionIncomeCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getPurchasingLPP() {
        if (!JadeStringUtil.isBlankOrZero(purchasingLPP)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(purchasingLPP), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getPurchasingLPPCjt() {
        if (!JadeStringUtil.isBlankOrZero(purchasingLPPCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(purchasingLPPCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    @Override
    public boolean hasSpy() {
        if (isForRetourOriginale()) {
            return false;
        } else {
            return super.hasSpy();
        }
    }

    public boolean isForBackup() {
        return isForBackup;
    }

    public boolean isForRetourOriginale() {
        return isForRetourOriginale;
    }

    public void setAssets(String assets) {
        this.assets = assets;
    }

    public void setAssetsCjt(String assetsCjt) {
        this.assetsCjt = assetsCjt;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public void setCapitalCjt(String capitalCjt) {
        this.capitalCjt = capitalCjt;
    }

    public void setEmploymentIncome(String employmentIncome) {
        this.employmentIncome = employmentIncome;
    }

    public void setEmploymentIncomeCjt(String employmentIncomeCjt) {
        this.employmentIncomeCjt = employmentIncomeCjt;
    }

    public void setForBackup(boolean isForBackup) {
        this.isForBackup = isForBackup;
    }

    public void setForRetourOriginale(boolean isForRetourOriginale) {
        this.isForRetourOriginale = isForRetourOriginale;
    }

    public void setIdDonneesBase(String idDonneesBase) {
        this.idDonneesBase = idDonneesBase;
    }

    public void setIdRetour(String idRetour) {
        this.idRetour = idRetour;
    }

    public void setIncomeFromSelfEmployment(String incomeFromSelfEmployment) {
        this.incomeFromSelfEmployment = incomeFromSelfEmployment;
    }

    public void setIncomeFromSelfEmploymentCjt(String incomeFromSelfEmploymentCjt) {
        this.incomeFromSelfEmploymentCjt = incomeFromSelfEmploymentCjt;
    }

    public void setMainIncomeInAgriculture(String mainIncomeInAgriculture) {
        this.mainIncomeInAgriculture = mainIncomeInAgriculture;
    }

    public void setMainIncomeInAgricultureCjt(String mainIncomeInAgricultureCjt) {
        this.mainIncomeInAgricultureCjt = mainIncomeInAgricultureCjt;
    }

    public void setNonDomesticIncomePresent(String nonDomesticIncomePresent) {
        this.nonDomesticIncomePresent = nonDomesticIncomePresent;
    }

    public void setNonDomesticIncomePresentCjt(String nonDomesticIncomePresentCjt) {
        this.nonDomesticIncomePresentCjt = nonDomesticIncomePresentCjt;
    }

    public void setOASIBridgingPension(String OASIBridgingPension) {
        this.OASIBridgingPension = OASIBridgingPension;
    }

    public void setOASIBridgingPensionCjt(String oASIBridgingPensionCjt) {
        OASIBridgingPensionCjt = oASIBridgingPensionCjt;
    }

    public void setPensionIncome(String pensionIncome) {
        this.pensionIncome = pensionIncome;
    }

    public void setPensionIncomeCjt(String pensionIncomeCjt) {
        this.pensionIncomeCjt = pensionIncomeCjt;
    }

    public void setPurchasingLPP(String purchasingLPP) {
        this.purchasingLPP = purchasingLPP;
    }

    public void setPurchasingLPPCjt(String purchasingLPPCjt) {
        this.purchasingLPPCjt = purchasingLPPCjt;
    }
}
