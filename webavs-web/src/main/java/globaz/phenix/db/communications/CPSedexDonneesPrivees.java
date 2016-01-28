package globaz.phenix.db.communications;

import globaz.globall.db.BEntity;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;

public class CPSedexDonneesPrivees extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annuities = "";
    private String annuitiesCjt = "";
    private String cash = "";
    private String cashCjt = "";
    private String childAllowance = "";
    private String childAllowanceCjt = "";
    private String companyShares = "";
    private String companySharesCjt = "";
    private String debts = "";
    private String debtsCjt = "";
    private String idDonneesPrivees = "";
    private String idRetour = "";
    private String inheritance = "";
    private String inheritanceCjt = "";
    private boolean isForBackup = false;
    private boolean isForRetourOriginale = false;
    private String lifeInsurance = "";
    private String lifeInsuranceCjt = "";
    private String maintenanceContribution = "";
    private String maintenanceContributionCjt = "";
    private String militaryInsurancePensions = "";
    private String militaryInsurancePensionsCjt = "";
    private String motorVehicle = "";
    private String motorVehicleCjt = "";
    private String otherAssets = "";
    private String otherAssetsCjt = "";
    private String otherPensions = "";
    private String otherPensionsCjt = "";
    private String patentLicense = "";
    private String patentLicenseCjt = "";
    private String pensions = "";
    private String pensionsCjt = "";
    private String pensionsPillar1 = "";
    private String pensionsPillar1Cjt = "";
    private String pensionsPillar2 = "";
    private String pensionsPillar2Cjt = "";
    private String pensionsPillar3a = "";
    private String pensionsPillar3aCjt = "";
    private String pensionsPillar3b = "";
    private String pensionsPillar3bCjt = "";
    private String perDiemAllowance = "";
    private String perDiemAllowanceCjt = "";
    private String realEstateProperties = "";
    private String realEstatePropertiesCjt = "";
    private String residencyEntitlement = "";
    private String residencyEntitlementCjt = "";
    private String securities = "";
    private String securitiesCjt = "";
    private String taxableIncomeExpenseTaxation = "";
    private String taxableIncomeExpenseTaxationCjt = "";
    private String taxableIncomeInAccordanceWithDBG = "";
    private String taxableIncomeInAccordanceWithDBGCjt = "";

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {

        setIdDonneesPrivees(this._incCounter(transaction, "0"));

    }

    @Override
    protected String _getTableName() {
        if (!isForBackup()) {
            return "CPDOPRI";
        } else {
            return "CPDOPRB";
        }
    }

    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idRetour = statement.dbReadNumeric("IKIRET");
        idDonneesPrivees = statement.dbReadNumeric("SEDPRID");
        pensions = statement.dbReadString("SEPENSI");
        pensionsPillar1 = statement.dbReadString("SEPENS1");
        pensionsPillar2 = statement.dbReadString("SEPENS2");
        pensionsPillar3a = statement.dbReadString("SEPEN3A");
        pensionsPillar3b = statement.dbReadString("SEPEN3B");
        annuities = statement.dbReadString("SERENVI");
        otherPensions = statement.dbReadString("SEAUTRE");
        militaryInsurancePensions = statement.dbReadString("SEREMIL");
        perDiemAllowance = statement.dbReadString("SEINDEM");
        maintenanceContribution = statement.dbReadString("SEPENAL");
        childAllowance = statement.dbReadString("SEALLOC");
        patentLicense = statement.dbReadString("SEBREVE");
        residencyEntitlement = statement.dbReadString("SEDHABI");
        securities = statement.dbReadString("SETITRE");
        cash = statement.dbReadString("SEARGEN");
        lifeInsurance = statement.dbReadString("SEASSVI");
        motorVehicle = statement.dbReadString("SEVEHIC");
        inheritance = statement.dbReadString("SEHERIT");
        otherAssets = statement.dbReadString("SEPATRI");
        realEstateProperties = statement.dbReadString("SEIMMEU");
        companyShares = statement.dbReadString("SEPARTS");
        debts = statement.dbReadString("SEDETTE");
        taxableIncomeInAccordanceWithDBG = statement.dbReadString("SEREVIM");
        taxableIncomeExpenseTaxation = statement.dbReadString("SEIMPOS");
        // ChampS conjoint
        pensionsCjt = statement.dbReadString("SCPENSI");
        pensionsPillar1Cjt = statement.dbReadString("SCPENS1");
        pensionsPillar2Cjt = statement.dbReadString("SCPENS2");
        pensionsPillar3aCjt = statement.dbReadString("SCPEN3A");
        pensionsPillar3bCjt = statement.dbReadString("SCPEN3B");
        annuitiesCjt = statement.dbReadString("SCRENVI");
        otherPensionsCjt = statement.dbReadString("SCAUTRE");
        militaryInsurancePensionsCjt = statement.dbReadString("SCREMIL");
        perDiemAllowanceCjt = statement.dbReadString("SCINDEM");
        maintenanceContributionCjt = statement.dbReadString("SCPENAL");
        childAllowanceCjt = statement.dbReadString("SCALLOC");
        patentLicenseCjt = statement.dbReadString("ScBREVE");
        residencyEntitlementCjt = statement.dbReadString("SCDHABI");
        securitiesCjt = statement.dbReadString("SCTITRE");
        cashCjt = statement.dbReadString("SCARGEN");
        lifeInsuranceCjt = statement.dbReadString("SCASSVI");
        motorVehicleCjt = statement.dbReadString("SCVEHIC");
        inheritanceCjt = statement.dbReadString("SCHERIT");
        otherAssetsCjt = statement.dbReadString("SCPATRI");
        realEstatePropertiesCjt = statement.dbReadString("SCIMMEU");
        companySharesCjt = statement.dbReadString("SCPARTS");
        debtsCjt = statement.dbReadString("SCDETTE");
        taxableIncomeInAccordanceWithDBGCjt = statement.dbReadString("SCREVIM");
        taxableIncomeExpenseTaxationCjt = statement.dbReadString("SCIMPOS");

    }

    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IKIRET", this._dbWriteNumeric(statement.getTransaction(), idRetour, "idRetour"));
        // statement.writeKey("IKIRET",
        // _dbWriteNumeric(statement.getTransaction(), idRetour, "idRetour"));
    }

    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IKIRET", this._dbWriteNumeric(statement.getTransaction(), idRetour, "idRetour"));
        if (isForRetourOriginale == false) {
            // Ne pas mettre à jour car cette zone a été défini par erreur comme clé primaire... sinon pb d'intégrité
            statement.writeField("SEDPRID",
                    this._dbWriteNumeric(statement.getTransaction(), idDonneesPrivees, "idDonneesPrivees"));
        }
        statement.writeField("SEPENSI", this._dbWriteString(statement.getTransaction(), pensions, "pensions"));
        statement.writeField("SEPENS1", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(pensionsPillar1), "pensionsPillar1"));
        statement.writeField("SEPENS2", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(pensionsPillar2), "pensionsPillar2"));
        statement.writeField("SEPEN3A", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(pensionsPillar3a), "pensionsPillar3a"));
        statement.writeField("SEPEN3B", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(pensionsPillar3b), "pensionsPillar3b"));
        statement.writeField("SERENVI",
                this._dbWriteString(statement.getTransaction(), JANumberFormatter.deQuote(annuities), "annuities"));
        statement.writeField("SEAUTRE", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(otherPensions), "otherPensions"));
        statement.writeField("SEREMIL", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(militaryInsurancePensions), "militaryInsurancePensions"));
        statement.writeField("SEINDEM", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(perDiemAllowance), "perDiemAllowance"));
        statement.writeField("SEPENAL", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(maintenanceContribution), "maintenanceContribution"));
        statement.writeField("SEALLOC", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(childAllowance), "childAllowance"));
        statement.writeField("SEBREVE", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(patentLicense), "patentLicense"));
        statement.writeField("SEDHABI", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(residencyEntitlement), "residencyEntitlement"));
        statement.writeField("SETITRE",
                this._dbWriteString(statement.getTransaction(), JANumberFormatter.deQuote(securities), "securities"));
        statement.writeField("SEARGEN",
                this._dbWriteString(statement.getTransaction(), JANumberFormatter.deQuote(cash), "cash"));
        statement.writeField("SEASSVI", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(lifeInsurance), "lifeInsurance"));
        statement.writeField("SEVEHIC", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(motorVehicle), "motorVehicle"));
        statement.writeField("SEHERIT",
                this._dbWriteString(statement.getTransaction(), JANumberFormatter.deQuote(inheritance), "inheritance"));
        statement.writeField("SEPATRI",
                this._dbWriteString(statement.getTransaction(), JANumberFormatter.deQuote(otherAssets), "otherAssets"));
        statement.writeField("SEIMMEU", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(realEstateProperties), "realEstateProperties"));
        statement.writeField("SEPARTS", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(companyShares), "companyShares"));
        statement.writeField("SEDETTE",
                this._dbWriteString(statement.getTransaction(), JANumberFormatter.deQuote(debts), "debts"));
        statement.writeField("SEREVIM", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(taxableIncomeInAccordanceWithDBG), "taxableIncomeInAccordanceWithDBG"));
        statement.writeField("SEIMPOS", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(taxableIncomeExpenseTaxation), "taxableIncomeExpenseTaxation"));
        // Champs conjoint
        statement.writeField("SCPENSI",
                this._dbWriteString(statement.getTransaction(), JANumberFormatter.deQuote(pensionsCjt), "pensionsCjt"));
        statement.writeField("SCPENS1", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(pensionsPillar1Cjt), "pensionsPillar1Cjt"));
        statement.writeField("SCPENS2", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(pensionsPillar2Cjt), "pensionsPillar2Cjt"));
        statement.writeField("SCPEN3A", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(pensionsPillar3aCjt), "pensionsPillar3aCjt"));
        statement.writeField("SCPEN3B", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(pensionsPillar3bCjt), "pensionsPillar3bCjt"));
        statement.writeField("SCRENVI", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(annuitiesCjt), "annuitiesCjt"));
        statement.writeField("SCAUTRE", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(otherPensionsCjt), "otherPensionsCjt"));
        statement.writeField("SCREMIL", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(militaryInsurancePensionsCjt), "militaryInsurancePensionsCjt"));
        statement.writeField("SCINDEM", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(perDiemAllowanceCjt), "perDiemAllowanceCjt"));
        statement.writeField("SCPENAL", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(maintenanceContributionCjt), "maintenanceContributionCjt"));
        statement.writeField("SCALLOC", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(childAllowanceCjt), "childAllowanceCjt"));
        statement.writeField("SCBREVE", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(patentLicenseCjt), "patentLicenseCjt"));
        statement.writeField("SCDHABI", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(residencyEntitlementCjt), "residencyEntitlementCjt"));
        statement.writeField("SCTITRE", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(securitiesCjt), "securitiesCjt"));
        statement.writeField("SCARGEN",
                this._dbWriteString(statement.getTransaction(), JANumberFormatter.deQuote(cashCjt), "cashCjt"));
        statement.writeField("SCASSVI", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(lifeInsuranceCjt), "lifeInsuranceCjt"));
        statement.writeField("SCVEHIC", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(motorVehicleCjt), "motorVehicleCjt"));
        statement.writeField("SCHERIT", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(inheritanceCjt), "inheritanceCjt"));
        statement.writeField("SCPATRI", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(otherAssetsCjt), "otherAssetsCjt"));
        statement.writeField("SCIMMEU", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(realEstatePropertiesCjt), "realEstatePropertiesCjt"));
        statement.writeField("SCPARTS", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(companySharesCjt), "companySharesCjt"));
        statement.writeField("SCDETTE",
                this._dbWriteString(statement.getTransaction(), JANumberFormatter.deQuote(debtsCjt), "debtsCjt"));
        statement.writeField("SCREVIM", this._dbWriteString(statement.getTransaction(),
                JANumberFormatter.deQuote(taxableIncomeInAccordanceWithDBGCjt), "taxableIncomeInAccordanceWithDBGCjt"));
        statement.writeField(
                "SCIMPOS",
                this._dbWriteString(statement.getTransaction(),
                        JANumberFormatter.deQuote(taxableIncomeExpenseTaxationCjt), "taxableIncomeExpenseTaxationCjt"));

    }

    public String getAnnuities() {
        if (!JadeStringUtil.isBlankOrZero(annuities)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(annuities), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getAnnuitiesCjt() {
        if (!JadeStringUtil.isBlankOrZero(annuitiesCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(annuitiesCjt), true, false, false, 0);
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

    public String getChildAllowance() {
        if (!JadeStringUtil.isBlankOrZero(childAllowance)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(childAllowance), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getChildAllowanceCjt() {
        if (!JadeStringUtil.isBlankOrZero(childAllowanceCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(childAllowanceCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getCompanyShares() {
        if (!JadeStringUtil.isBlankOrZero(companyShares)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(companyShares), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getCompanySharesCjt() {
        if (!JadeStringUtil.isBlankOrZero(companySharesCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(companySharesCjt), true, false, false, 0);
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

    public String getIdDonneesPrivees() {
        return idDonneesPrivees;
    }

    public String getIdRetour() {
        return idRetour;
    }

    public String getInheritance() {
        if (!JadeStringUtil.isBlankOrZero(inheritance)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(inheritance), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getInheritanceCjt() {
        if (!JadeStringUtil.isBlankOrZero(inheritanceCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(inheritanceCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getLifeInsurance() {
        if (!JadeStringUtil.isBlankOrZero(lifeInsurance)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(lifeInsurance), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getLifeInsuranceCjt() {
        if (!JadeStringUtil.isBlankOrZero(lifeInsuranceCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(lifeInsuranceCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getMaintenanceContribution() {
        if (!JadeStringUtil.isBlankOrZero(maintenanceContribution)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(maintenanceContribution), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getMaintenanceContributionCjt() {
        if (!JadeStringUtil.isBlankOrZero(maintenanceContributionCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(maintenanceContributionCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getMilitaryInsurancePensions() {
        if (!JadeStringUtil.isBlankOrZero(militaryInsurancePensions)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(militaryInsurancePensions), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getMilitaryInsurancePensionsCjt() {
        if (!JadeStringUtil.isBlankOrZero(militaryInsurancePensionsCjt)) {
            return JANumberFormatter
                    .fmt(JANumberFormatter.deQuote(militaryInsurancePensionsCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getMotorVehicle() {
        if (!JadeStringUtil.isBlankOrZero(motorVehicle)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(motorVehicle), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getMotorVehicleCjt() {
        if (!JadeStringUtil.isBlankOrZero(motorVehicleCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(motorVehicleCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getOtherAssets() {
        if (!JadeStringUtil.isBlankOrZero(otherAssets)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(otherAssets), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getOtherAssetsCjt() {
        if (!JadeStringUtil.isBlankOrZero(otherAssetsCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(otherAssetsCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getOtherPensions() {
        if (!JadeStringUtil.isBlankOrZero(otherPensions)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(otherPensions), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getOtherPensionsCjt() {
        if (!JadeStringUtil.isBlankOrZero(otherPensionsCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(otherPensionsCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getPatentLicense() {
        if (!JadeStringUtil.isBlankOrZero(patentLicense)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(patentLicense), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getPatentLicenseCjt() {
        if (!JadeStringUtil.isBlankOrZero(patentLicenseCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(patentLicenseCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getPensions() {
        if (!JadeStringUtil.isBlankOrZero(pensions)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(pensions), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getPensionsCjt() {
        if (!JadeStringUtil.isBlankOrZero(pensionsCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(pensionsCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getPensionsPillar1() {
        if (!JadeStringUtil.isBlankOrZero(pensionsPillar1)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(pensionsPillar1), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getPensionsPillar1Cjt() {
        if (!JadeStringUtil.isBlankOrZero(pensionsPillar1Cjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(pensionsPillar1Cjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getPensionsPillar2() {
        if (!JadeStringUtil.isBlankOrZero(pensionsPillar2)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(pensionsPillar2), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getPensionsPillar2Cjt() {
        if (!JadeStringUtil.isBlankOrZero(pensionsPillar2Cjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(pensionsPillar2Cjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getPensionsPillar3a() {
        if (!JadeStringUtil.isBlankOrZero(pensionsPillar3a)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(pensionsPillar3a), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getPensionsPillar3aCjt() {
        if (!JadeStringUtil.isBlankOrZero(pensionsPillar3aCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(pensionsPillar3aCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getPensionsPillar3b() {
        if (!JadeStringUtil.isBlankOrZero(pensionsPillar3b)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(pensionsPillar3b), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getPensionsPillar3bCjt() {
        if (!JadeStringUtil.isBlankOrZero(pensionsPillar3bCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(pensionsPillar3bCjt), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getPerDiemAllowance() {
        if (!JadeStringUtil.isBlankOrZero(perDiemAllowance)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(perDiemAllowance), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getPerDiemAllowanceCjt() {
        if (!JadeStringUtil.isBlankOrZero(perDiemAllowanceCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(perDiemAllowanceCjt), true, false, false, 0);
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

    public String getTaxableIncomeExpenseTaxation() {
        if (!JadeStringUtil.isBlankOrZero(taxableIncomeExpenseTaxation)) {
            return JANumberFormatter
                    .fmt(JANumberFormatter.deQuote(taxableIncomeExpenseTaxation), true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getTaxableIncomeExpenseTaxationCjt() {
        if (!JadeStringUtil.isBlankOrZero(taxableIncomeExpenseTaxationCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(taxableIncomeExpenseTaxationCjt), true, false,
                    false, 0);
        } else {
            return "0";
        }
    }

    public String getTaxableIncomeInAccordanceWithDBG() {
        if (!JadeStringUtil.isBlankOrZero(taxableIncomeInAccordanceWithDBG)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(taxableIncomeInAccordanceWithDBG), true, false,
                    false, 0);
        } else {
            return "0";
        }
    }

    public String getTaxableIncomeInAccordanceWithDBGCjt() {
        if (!JadeStringUtil.isBlankOrZero(taxableIncomeInAccordanceWithDBGCjt)) {
            return JANumberFormatter.fmt(JANumberFormatter.deQuote(taxableIncomeInAccordanceWithDBGCjt), true, false,
                    false, 0);
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

    public void setAnnuities(String annuities) {
        this.annuities = annuities;
    }

    public void setAnnuitiesCjt(String annuitiesCjt) {
        this.annuitiesCjt = annuitiesCjt;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public void setCashCjt(String cashCjt) {
        this.cashCjt = cashCjt;
    }

    public void setChildAllowance(String childAllowance) {
        this.childAllowance = childAllowance;
    }

    public void setChildAllowanceCjt(String childAllowanceCjt) {
        this.childAllowanceCjt = childAllowanceCjt;
    }

    public void setCompanyShares(String companyShares) {
        this.companyShares = companyShares;
    }

    public void setCompanySharesCjt(String companySharesCjt) {
        this.companySharesCjt = companySharesCjt;
    }

    public void setDebts(String debts) {
        this.debts = debts;
    }

    public void setDebtsCjt(String debtsCjt) {
        this.debtsCjt = debtsCjt;
    }

    public void setForBackup(boolean isForBackup) {
        this.isForBackup = isForBackup;
    }

    public void setForRetourOriginale(boolean isForRetourOriginale) {
        this.isForRetourOriginale = isForRetourOriginale;
    }

    public void setIdDonneesPrivees(String idDonneesPrivees) {
        this.idDonneesPrivees = idDonneesPrivees;
    }

    public void setIdRetour(String idRetour) {
        this.idRetour = idRetour;
    }

    public void setInheritance(String inheritance) {
        this.inheritance = inheritance;
    }

    public void setInheritanceCjt(String inheritanceCjt) {
        this.inheritanceCjt = inheritanceCjt;
    }

    public void setLifeInsurance(String lifeInsurance) {
        this.lifeInsurance = lifeInsurance;
    }

    public void setLifeInsuranceCjt(String lifeInsuranceCjt) {
        this.lifeInsuranceCjt = lifeInsuranceCjt;
    }

    public void setMaintenanceContribution(String maintenanceContribution) {
        this.maintenanceContribution = maintenanceContribution;
    }

    public void setMaintenanceContributionCjt(String maintenanceContributionCjt) {
        this.maintenanceContributionCjt = maintenanceContributionCjt;
    }

    public void setMilitaryInsurancePensions(String militaryInsurancePensions) {
        this.militaryInsurancePensions = militaryInsurancePensions;
    }

    public void setMilitaryInsurancePensionsCjt(String militaryInsurancePensionsCjt) {
        this.militaryInsurancePensionsCjt = militaryInsurancePensionsCjt;
    }

    public void setMotorVehicle(String motorVehicle) {
        this.motorVehicle = motorVehicle;
    }

    public void setMotorVehicleCjt(String motorVehicleCjt) {
        this.motorVehicleCjt = motorVehicleCjt;
    }

    public void setOtherAssets(String otherAssets) {
        this.otherAssets = otherAssets;
    }

    public void setOtherAssetsCjt(String otherAssetsCjt) {
        this.otherAssetsCjt = otherAssetsCjt;
    }

    public void setOtherPensions(String otherPensions) {
        this.otherPensions = otherPensions;
    }

    public void setOtherPensionsCjt(String otherPensionsCjt) {
        this.otherPensionsCjt = otherPensionsCjt;
    }

    public void setPatentLicense(String patentLicense) {
        this.patentLicense = patentLicense;
    }

    public void setPatentLicenseCjt(String patentLicenseCjt) {
        this.patentLicenseCjt = patentLicenseCjt;
    }

    public void setPensions(String pensions) {
        this.pensions = pensions;
    }

    public void setPensionsCjt(String pensionsCjt) {
        this.pensionsCjt = pensionsCjt;
    }

    public void setPensionsPillar1(String pensionsPillar1) {
        this.pensionsPillar1 = pensionsPillar1;
    }

    public void setPensionsPillar1Cjt(String pensionsPillar1Cjt) {
        this.pensionsPillar1Cjt = pensionsPillar1Cjt;
    }

    public void setPensionsPillar2(String pensionsPillar2) {
        this.pensionsPillar2 = pensionsPillar2;
    }

    public void setPensionsPillar2Cjt(String pensionsPillar2Cjt) {
        this.pensionsPillar2Cjt = pensionsPillar2Cjt;
    }

    public void setPensionsPillar3a(String pensionsPillar3a) {
        this.pensionsPillar3a = pensionsPillar3a;
    }

    public void setPensionsPillar3aCjt(String pensionsPillar3aCjt) {
        this.pensionsPillar3aCjt = pensionsPillar3aCjt;
    }

    public void setPensionsPillar3b(String pensionsPillar3b) {
        this.pensionsPillar3b = pensionsPillar3b;
    }

    public void setPensionsPillar3bCjt(String pensionsPillar3bCjt) {
        this.pensionsPillar3bCjt = pensionsPillar3bCjt;
    }

    public void setPerDiemAllowance(String perDiemAllowance) {
        this.perDiemAllowance = perDiemAllowance;
    }

    public void setPerDiemAllowanceCjt(String perDiemAllowanceCjt) {
        this.perDiemAllowanceCjt = perDiemAllowanceCjt;
    }

    public void setRealEstateProperties(String realEstateProperties) {
        this.realEstateProperties = realEstateProperties;
    }

    public void setRealEstatePropertiesCjt(String realEstatePropertiesCjt) {
        this.realEstatePropertiesCjt = realEstatePropertiesCjt;
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

    public void setTaxableIncomeExpenseTaxation(String taxableIncomeInAccordanceWithExpenseTaxation) {
        taxableIncomeExpenseTaxation = taxableIncomeInAccordanceWithExpenseTaxation;
    }

    public void setTaxableIncomeExpenseTaxationCjt(String taxableIncomeInAccordanceWithExpenseTaxationCjt) {
        taxableIncomeExpenseTaxationCjt = taxableIncomeInAccordanceWithExpenseTaxationCjt;
    }

    public void setTaxableIncomeInAccordanceWithDBG(String taxableIncomeInAccordanceWithDBG) {
        this.taxableIncomeInAccordanceWithDBG = taxableIncomeInAccordanceWithDBG;
    }

    public void setTaxableIncomeInAccordanceWithDBGCjt(String taxableIncomeInAccordanceWithDBGCjt) {
        this.taxableIncomeInAccordanceWithDBGCjt = taxableIncomeInAccordanceWithDBGCjt;
    }

}
