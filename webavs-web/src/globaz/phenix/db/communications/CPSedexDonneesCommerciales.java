package globaz.phenix.db.communications;

import globaz.globall.db.BEntity;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;

public class CPSedexDonneesCommerciales extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String assets = "";
    private String assetsCjt = "";
    private String cash = "";
    private String cashCjt = "";
    private String commencementOfSelfEmployment = "";
    private String commencementOfSelfEmploymentCjt = "";
    private String cooperativeShares = "";
    private String cooperativeSharesCjt = "";
    private String debtInterest = "";
    private String debtInterestCjt = "";
    private String debts = "";
    private String debtsCjt = "";
    private String donations = "";
    private String donationsCjt = "";
    private String endOfSelfEmployment = "";
    private String endOfSelfEmploymentCjt = "";
    private String idDonneesCommerciales = "";
    private String idRetour = "";
    private String incomeRealEstate = "";
    private String incomeRealEstateCjt = "";
    private boolean isForBackup = false;
    private boolean isForRetourOriginale = false;
    private String mainIncome = "";
    private String mainIncomeCjt = "";
    private String mainIncomeInAgriculture = "";
    private String mainIncomeInAgricultureCjt = "";
    private String mainIncomeInRealEstateTrade = "";
    private String mainIncomeInRealEstateTradeCjt = "";
    private String otherIncome = "";
    private String otherIncomeCjt = "";

    private String partTimeEmployment = "";

    private String partTimeEmploymentCjt = "";

    private String partTimeEmploymentInAgriculture = "";

    private String partTimeEmploymentInAgricultureCjt = "";

    private String partTimeEmploymentInRealEstateTrade = "";

    private String partTimeEmploymentInRealEstateTradeCjt = "";

    private String realEstateProperties = "";

    private String realEstatePropertiesCjt = "";

    private String realisationProfit = "";

    private String realisationProfitCjt = "";

    private String residencyEntitlement = "";

    private String residencyEntitlementCjt = "";

    private String securities = "";

    private String securitiesCjt = "";

    private String securitiesIncome = "";

    private String securitiesIncomeCjt = "";

    private String totalAssets = "";

    private String totalAssetsCjt = "";

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        setIdDonneesCommerciales(this._incCounter(transaction, "0"));
    }

    @Override
    protected String _getTableName() {
        if (!isForBackup()) {
            return "CPDONCO";
        } else {
            return "CPDONCB";
        }
    }

    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idRetour = statement.dbReadNumeric("IKIRET");
        idDonneesCommerciales = statement.dbReadNumeric("SEDCOID");
        commencementOfSelfEmployment = statement.dbReadString("SEDEBAC");
        endOfSelfEmployment = statement.dbReadString("SEFINAC");
        mainIncome = statement.dbReadString("SEREVIN");
        mainIncomeInAgriculture = statement.dbReadString("SEREVAG");
        mainIncomeInRealEstateTrade = statement.dbReadString("SEREVIM");
        partTimeEmployment = statement.dbReadString("SEREVAC");
        partTimeEmploymentInAgriculture = statement.dbReadString("SEREVAA");
        partTimeEmploymentInRealEstateTrade = statement.dbReadString("SEREVCI");
        realisationProfit = statement.dbReadString("SEGAINL");
        cooperativeShares = statement.dbReadString("SEPARTR");
        securitiesIncome = statement.dbReadString("SETITRE");
        otherIncome = statement.dbReadString("SEAUTRE");
        residencyEntitlement = statement.dbReadString("SEDRHAB");
        incomeRealEstate = statement.dbReadString("SERIMMC");
        debtInterest = statement.dbReadString("SEINTER");
        donations = statement.dbReadString("SEALLOC");
        securities = statement.dbReadString("SETITRC");
        cash = statement.dbReadString("SEARGEN");
        assets = statement.dbReadString("SEPATRI");
        realEstateProperties = statement.dbReadString("SEVALRE");
        totalAssets = statement.dbReadString("SETOTAC");
        debts = statement.dbReadString("SEDETTE");
        // Champ conjoint
        commencementOfSelfEmploymentCjt = statement.dbReadString("SCDEBAC");
        endOfSelfEmploymentCjt = statement.dbReadString("SCFINAC");
        mainIncomeCjt = statement.dbReadString("SCREVIN");
        mainIncomeInAgricultureCjt = statement.dbReadString("SCREVAG");
        mainIncomeInRealEstateTradeCjt = statement.dbReadString("SCREVIM");
        partTimeEmploymentCjt = statement.dbReadString("SCREVAC");
        partTimeEmploymentInAgricultureCjt = statement.dbReadString("SCREVAA");
        partTimeEmploymentInRealEstateTradeCjt = statement.dbReadString("SCREVCI");
        realisationProfitCjt = statement.dbReadString("SCGAINL");
        cooperativeSharesCjt = statement.dbReadString("SCPARTR");
        securitiesIncomeCjt = statement.dbReadString("SCTITRE");
        otherIncomeCjt = statement.dbReadString("SCAUTRE");
        residencyEntitlementCjt = statement.dbReadString("SCDRHAB");
        incomeRealEstateCjt = statement.dbReadString("SCRIMMC");
        debtInterestCjt = statement.dbReadString("SCINTER");
        donationsCjt = statement.dbReadString("SCALLOC");
        securitiesCjt = statement.dbReadString("SCTITRC");
        cashCjt = statement.dbReadString("SCARGEN");
        assetsCjt = statement.dbReadString("SCPATRI");
        realEstatePropertiesCjt = statement.dbReadString("SCVALRE");
        totalAssetsCjt = statement.dbReadString("SCTOTAC");
        debtsCjt = statement.dbReadString("SCDETTE");
    }

    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IKIRET",
                this._dbWriteNumeric(statement.getTransaction(), getIdRetour(), "idCommunicationRetour"));
    }

    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IKIRET", this._dbWriteNumeric(statement.getTransaction(), idRetour, "idRetour"));
        if (isForRetourOriginale == false) {
            // Ne pas mettre à jour car cette zone a été défini par erreur comme clé primaire... sinon pb d'intégrité
            statement.writeField("SEDCOID",
                    this._dbWriteNumeric(statement.getTransaction(), idDonneesCommerciales, "idDonneesCommerciales"));
        }
        statement.writeField("SEDEBAC", this._dbWriteString(statement.getTransaction(), commencementOfSelfEmployment,
                "commencementOfSelfEmployment"));
        statement.writeField("SEFINAC",
                this._dbWriteString(statement.getTransaction(), endOfSelfEmployment, "endOfSelfEmployment"));
        statement.writeField("SEREVIN",
                this._dbWriteString(statement.getTransaction(), JANumberFormatter.deQuote(mainIncome), "mainIncome"));
        statement.writeField("SEREVAG", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(mainIncomeInAgriculture), "mainIncomeInAgriculture"));
        statement.writeField("SEREVIM", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(mainIncomeInRealEstateTrade), "mainIncomeInRealEstateTrade"));
        statement.writeField("SEREVAC", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(partTimeEmployment), "partTimeEmployment"));
        statement.writeField(
                "SEREVAA",
                this._dbWriteString(statement.getTransaction(),
                        JANumberFormatter.deQuote(partTimeEmploymentInAgriculture), "partTimeEmploymentInAgriculture"));
        statement.writeField("SEREVCI", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(partTimeEmploymentInRealEstateTrade), "partTimeEmploymentInRealEstateTrade"));
        statement.writeField("SEGAINL", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(realisationProfit), "realisationProfit"));
        statement.writeField("SEPARTR", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(cooperativeShares), "cooperativeShares"));
        statement.writeField("SETITRE", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(securitiesIncome), "securitiesIncome"));
        statement.writeField("SEAUTRE",
                this._dbWriteString(statement.getTransaction(), JANumberFormatter.deQuote(otherIncome), "otherIncome"));
        statement.writeField("SEDRHAB", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(residencyEntitlement), "residencyEntitlement"));
        statement.writeField("SERIMMC", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(incomeRealEstate), "incomeRealEstate"));
        statement.writeField("SEINTER", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(debtInterest), "debtInterest"));
        statement.writeField("SEALLOC",
                this._dbWriteString(statement.getTransaction(), JANumberFormatter.deQuote(donations), "donations"));
        statement.writeField("SETITRC",
                this._dbWriteString(statement.getTransaction(), JANumberFormatter.deQuote(securities), "securities"));
        statement.writeField("SEARGEN",
                this._dbWriteString(statement.getTransaction(), JANumberFormatter.deQuote(cash), "cash"));
        statement.writeField("SEPATRI",
                this._dbWriteString(statement.getTransaction(), JANumberFormatter.deQuote(assets), "assets"));
        statement.writeField("SEVALRE", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(realEstateProperties), "realEstateProperties"));
        statement.writeField("SETOTAC",
                this._dbWriteString(statement.getTransaction(), JANumberFormatter.deQuote(totalAssets), "totalAssets"));
        statement.writeField("SEDETTE",
                this._dbWriteString(statement.getTransaction(), JANumberFormatter.deQuote(debts), "debts"));
        // Champs conjoint
        statement.writeField("SCDEBAC", this._dbWriteString(statement.getTransaction(),
                commencementOfSelfEmploymentCjt, "commencementOfSelfEmploymentCjt"));
        statement.writeField("SCFINAC",
                this._dbWriteString(statement.getTransaction(), endOfSelfEmploymentCjt, "endOfSelfEmploymentCjt"));
        statement.writeField("SCREVIN", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(mainIncomeCjt), "mainIncomeCjt"));
        statement.writeField("SCREVAG", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(mainIncomeInAgricultureCjt), "mainIncomeInAgricultureCjt"));
        statement.writeField(
                "SCREVIM",
                this._dbWriteString(statement.getTransaction(),
                        JANumberFormatter.deQuote(mainIncomeInRealEstateTradeCjt), "mainIncomeInRealEstateTradeCjt"));
        statement.writeField("SCREVAC", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(partTimeEmploymentCjt), "partTimeEmploymentCjt"));
        statement.writeField("SCREVAA", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(partTimeEmploymentInAgricultureCjt), "partTimeEmploymentInAgricultureCjt"));
        statement.writeField("SCREVCI", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(partTimeEmploymentInRealEstateTradeCjt),
                "partTimeEmploymentInRealEstateTradeCjt"));
        statement.writeField("SCGAINL", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(realisationProfitCjt), "realisationProfitCjt"));
        statement.writeField("SCPARTR", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(cooperativeSharesCjt), "cooperativeSharesCjt"));
        statement.writeField("SCTITRE", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(securitiesIncomeCjt), "securitiesIncomeCjt"));
        statement.writeField("SCAUTRE", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(otherIncomeCjt), "otherIncomeCjt"));
        statement.writeField("SCDRHAB", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(residencyEntitlementCjt), "residencyEntitlementCjt"));
        statement.writeField("SCRIMMC", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(incomeRealEstateCjt), "incomeRealEstateCjt"));
        statement.writeField("SCINTER", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(debtInterestCjt), "debtInterestCjt"));
        statement.writeField("SCALLOC", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(donationsCjt), "donationsCjt"));
        statement.writeField("SCTITRC", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(securitiesCjt), "securitiesCjt"));
        statement.writeField("SCARGEN",
                this._dbWriteString(statement.getTransaction(), JANumberFormatter.deQuote(cashCjt), "cashCjt"));
        statement.writeField("SCPATRI",
                this._dbWriteString(statement.getTransaction(), JANumberFormatter.deQuote(assetsCjt), "assetsCjt"));
        statement.writeField("SCVALRE", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(realEstatePropertiesCjt), "realEstatePropertiesCjt"));
        statement.writeField("SCTOTAC", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(totalAssetsCjt), "totalAssetsCjt"));
        statement.writeField("SCDETTE",
                this._dbWriteString(statement.getTransaction(), JANumberFormatter.deQuote(debtsCjt), "debtsCjt"));
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

    public String getCash() {
        if (!JadeStringUtil.isBlankOrZero(cash)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(cash), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getCashCjt() {
        if (!JadeStringUtil.isBlankOrZero(cashCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(cashCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getCommencementOfSelfEmployment() {
        return commencementOfSelfEmployment;
    }

    public String getCommencementOfSelfEmploymentCjt() {
        return commencementOfSelfEmploymentCjt;
    }

    public String getCooperativeShares() {
        if (!JadeStringUtil.isBlankOrZero(cooperativeShares)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(cooperativeShares), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getCooperativeSharesCjt() {
        if (!JadeStringUtil.isBlankOrZero(cooperativeSharesCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(cooperativeSharesCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getDebtInterest() {
        if (!JadeStringUtil.isBlankOrZero(debtInterest)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(debtInterest), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getDebtInterestCjt() {
        if (!JadeStringUtil.isBlankOrZero(debtInterestCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(debtInterestCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getDebts() {
        if (!JadeStringUtil.isBlankOrZero(debts)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(debts), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getDebtsCjt() {
        if (!JadeStringUtil.isBlankOrZero(debtsCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(debtsCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getDonations() {
        if (!JadeStringUtil.isBlankOrZero(donations)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(donations), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getDonationsCjt() {
        if (!JadeStringUtil.isBlankOrZero(donationsCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(donationsCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getEndOfSelfEmployment() {
        return endOfSelfEmployment;
    }

    public String getEndOfSelfEmploymentCjt() {
        return endOfSelfEmploymentCjt;
    }

    public String getIdDonneesCommerciales() {
        return idDonneesCommerciales;
    }

    public String getIdRetour() {
        return idRetour;
    }

    public String getIncomeRealEstate() {
        if (!JadeStringUtil.isBlankOrZero(incomeRealEstate)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(incomeRealEstate), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getIncomeRealEstateCjt() {
        if (!JadeStringUtil.isBlankOrZero(incomeRealEstateCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(incomeRealEstateCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getMainIncome() {
        if (!JadeStringUtil.isBlankOrZero(mainIncome)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(mainIncome), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getMainIncomeCjt() {
        if (!JadeStringUtil.isBlankOrZero(mainIncomeCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(mainIncomeCjt), true, false, false, 0);
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

    public String getMainIncomeInRealEstateTrade() {
        if (!JadeStringUtil.isBlankOrZero(mainIncomeInRealEstateTrade)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(mainIncomeInRealEstateTrade), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getMainIncomeInRealEstateTradeCjt() {
        if (!JadeStringUtil.isBlankOrZero(mainIncomeInRealEstateTradeCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(mainIncomeInRealEstateTradeCjt), true, false, false,
                    0);
        } else {
            return "0";
        }
    }

    public String getOtherIncome() {
        if (!JadeStringUtil.isBlankOrZero(otherIncome)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(otherIncome), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getOtherIncomeCjt() {
        if (!JadeStringUtil.isBlankOrZero(otherIncomeCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(otherIncomeCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getPartTimeEmployment() {
        if (!JadeStringUtil.isBlankOrZero(partTimeEmployment)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(partTimeEmployment), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getPartTimeEmploymentCjt() {
        if (!JadeStringUtil.isBlankOrZero(partTimeEmploymentCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(partTimeEmploymentCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getPartTimeEmploymentInAgriculture() {
        if (!JadeStringUtil.isBlankOrZero(partTimeEmploymentInAgriculture)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(partTimeEmploymentInAgriculture), true, false,
                    false, 0);
        } else {
            return "0";
        }
    }

    public String getPartTimeEmploymentInAgricultureCjt() {
        if (!JadeStringUtil.isBlankOrZero(partTimeEmploymentInAgricultureCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(partTimeEmploymentInAgricultureCjt), true, false,
                    false, 0);
        } else {
            return "0";
        }
    }

    public String getPartTimeEmploymentInRealEstateTrade() {
        if (!JadeStringUtil.isBlankOrZero(partTimeEmploymentInRealEstateTrade)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(partTimeEmploymentInRealEstateTrade), true, false,
                    false, 0);
        } else {
            return "0";
        }
    }

    public String getPartTimeEmploymentInRealEstateTradeCjt() {
        if (!JadeStringUtil.isBlankOrZero(partTimeEmploymentInRealEstateTradeCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(partTimeEmploymentInRealEstateTradeCjt), true,
                    false, false, 0);
        } else {
            return "0";
        }
    }

    public String getRealEstateProperties() {
        if (!JadeStringUtil.isBlankOrZero(realEstateProperties)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(realEstateProperties), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getRealEstatePropertiesCjt() {
        if (!JadeStringUtil.isBlankOrZero(realEstatePropertiesCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(realEstatePropertiesCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getRealisationProfit() {
        if (!JadeStringUtil.isBlankOrZero(realisationProfit)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(realisationProfit), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getRealisationProfitCjt() {
        if (!JadeStringUtil.isBlankOrZero(realisationProfitCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(realisationProfitCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getResidencyEntitlement() {
        if (!JadeStringUtil.isBlankOrZero(residencyEntitlement)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(residencyEntitlement), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getResidencyEntitlementCjt() {
        if (!JadeStringUtil.isBlankOrZero(residencyEntitlementCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(residencyEntitlementCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getSecurities() {
        if (!JadeStringUtil.isBlankOrZero(securities)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(securities), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getSecuritiesCjt() {
        if (!JadeStringUtil.isBlankOrZero(securitiesCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(securitiesCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getSecuritiesIncome() {
        if (!JadeStringUtil.isBlankOrZero(securitiesIncome)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(securitiesIncome), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getSecuritiesIncomeCjt() {
        if (!JadeStringUtil.isBlankOrZero(securitiesIncomeCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(securitiesIncomeCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getTotalAssets() {
        if (!JadeStringUtil.isBlankOrZero(totalAssets)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(totalAssets), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getTotalAssetsCjt() {
        if (!JadeStringUtil.isBlankOrZero(totalAssetsCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(totalAssetsCjt), true, false, false, 0);
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

    public void setCash(String cash) {
        this.cash = cash;
    }

    public void setCashCjt(String cashCjt) {
        this.cashCjt = cashCjt;
    }

    public void setCommencementOfSelfEmployment(String commencementOfSelfEmployment) {
        this.commencementOfSelfEmployment = commencementOfSelfEmployment;
    }

    public void setCommencementOfSelfEmploymentCjt(String commencementOfSelfEmploymentCjt) {
        this.commencementOfSelfEmploymentCjt = commencementOfSelfEmploymentCjt;
    }

    public void setCooperativeShares(String cooperativeShares) {
        this.cooperativeShares = cooperativeShares;
    }

    public void setCooperativeSharesCjt(String cooperativeSharesCjt) {
        this.cooperativeSharesCjt = cooperativeSharesCjt;
    }

    public void setDebtInterest(String debtInterest) {
        this.debtInterest = debtInterest;
    }

    public void setDebtInterestCjt(String debtInterestCjt) {
        this.debtInterestCjt = debtInterestCjt;
    }

    public void setDebts(String debts) {
        this.debts = debts;
    }

    public void setDebtsCjt(String debtsCjt) {
        this.debtsCjt = debtsCjt;
    }

    public void setDonations(String donations) {
        this.donations = donations;
    }

    public void setDonationsCjt(String donationsCjt) {
        this.donationsCjt = donationsCjt;
    }

    public void setEndOfSelfEmployment(String endOfSelfEmployment) {
        this.endOfSelfEmployment = endOfSelfEmployment;
    }

    public void setEndOfSelfEmploymentCjt(String endOfSelfEmploymentCjt) {
        this.endOfSelfEmploymentCjt = endOfSelfEmploymentCjt;
    }

    public void setForBackup(boolean isForBackup) {
        this.isForBackup = isForBackup;
    }

    public void setForRetourOriginale(boolean isForRetourOriginale) {
        this.isForRetourOriginale = isForRetourOriginale;
    }

    public void setIdDonneesCommerciales(String idDonneesCommerciales) {
        this.idDonneesCommerciales = idDonneesCommerciales;
    }

    public void setIdRetour(String idRetour) {
        this.idRetour = idRetour;
    }

    public void setIncomeRealEstate(String incomeRealEstate) {
        this.incomeRealEstate = incomeRealEstate;
    }

    public void setIncomeRealEstateCjt(String incomeRealEstateCjt) {
        this.incomeRealEstateCjt = incomeRealEstateCjt;
    }

    public void setMainIncome(String mainIncome) {
        this.mainIncome = mainIncome;
    }

    public void setMainIncomeCjt(String mainIncomeCjt) {
        this.mainIncomeCjt = mainIncomeCjt;
    }

    public void setMainIncomeInAgriculture(String mainIncomeInAgriculture) {
        this.mainIncomeInAgriculture = mainIncomeInAgriculture;
    }

    public void setMainIncomeInAgricultureCjt(String mainIncomeInAgricultureCjt) {
        this.mainIncomeInAgricultureCjt = mainIncomeInAgricultureCjt;
    }

    public void setMainIncomeInRealEstateTrade(String mainIncomeInRealEstateTrade) {
        this.mainIncomeInRealEstateTrade = mainIncomeInRealEstateTrade;
    }

    public void setMainIncomeInRealEstateTradeCjt(String mainIncomeInRealEstateTradeCjt) {
        this.mainIncomeInRealEstateTradeCjt = mainIncomeInRealEstateTradeCjt;
    }

    public void setOtherIncome(String otherIncome) {
        this.otherIncome = otherIncome;
    }

    public void setOtherIncomeCjt(String otherIncomeCjt) {
        this.otherIncomeCjt = otherIncomeCjt;
    }

    public void setPartTimeEmployment(String partTimeEmployment) {
        this.partTimeEmployment = partTimeEmployment;
    }

    public void setPartTimeEmploymentCjt(String partTimeEmploymentCjt) {
        this.partTimeEmploymentCjt = partTimeEmploymentCjt;
    }

    public void setPartTimeEmploymentInAgriculture(String partTimeEmploymentInAgriculture) {
        this.partTimeEmploymentInAgriculture = partTimeEmploymentInAgriculture;
    }

    public void setPartTimeEmploymentInAgricultureCjt(String partTimeEmploymentInAgricultureCjt) {
        this.partTimeEmploymentInAgricultureCjt = partTimeEmploymentInAgricultureCjt;
    }

    public void setPartTimeEmploymentInRealEstateTrade(String partTimeEmploymentInRealEstateTrade) {
        this.partTimeEmploymentInRealEstateTrade = partTimeEmploymentInRealEstateTrade;
    }

    public void setPartTimeEmploymentInRealEstateTradeCjt(String partTimeEmploymentInRealEstateTradeCjt) {
        this.partTimeEmploymentInRealEstateTradeCjt = partTimeEmploymentInRealEstateTradeCjt;
    }

    public void setRealEstateProperties(String realEstateProperties) {
        this.realEstateProperties = realEstateProperties;
    }

    public void setRealEstatePropertiesCjt(String realEstatePropertiesCjt) {
        this.realEstatePropertiesCjt = realEstatePropertiesCjt;
    }

    public void setRealisationProfit(String realisationProfit) {
        this.realisationProfit = realisationProfit;
    }

    public void setRealisationProfitCjt(String realisationProfitCjt) {
        this.realisationProfitCjt = realisationProfitCjt;
    }

    public void setResidencyEntitlement(String residencyEntitlement) {
        this.residencyEntitlement = residencyEntitlement;
    }

    public void setResidencyEntitlementCjt(String residencyEntitlementCjt) {
        this.residencyEntitlementCjt = residencyEntitlementCjt;
    }

    public void setSecurities(String securities) {
        this.securities = securities;
    }

    public void setSecuritiesCjt(String securitiesCjt) {
        this.securitiesCjt = securitiesCjt;
    }

    public void setSecuritiesIncome(String securitiesIncome) {
        this.securitiesIncome = securitiesIncome;
    }

    public void setSecuritiesIncomeCjt(String securitiesIncomeCjt) {
        this.securitiesIncomeCjt = securitiesIncomeCjt;
    }

    public void setTotalAssets(String totalAssets) {
        this.totalAssets = totalAssets;
    }

    public void setTotalAssetsCjt(String totalAssetsCjt) {
        this.totalAssetsCjt = totalAssetsCjt;
    }

}
