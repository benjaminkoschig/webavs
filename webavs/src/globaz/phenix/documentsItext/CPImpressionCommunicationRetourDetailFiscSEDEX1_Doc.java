package globaz.phenix.documentsItext;

import globaz.docinfo.CADocumentInfoHelper;
import globaz.docinfo.CTDocumentInfoHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.ged.target.JadeGedTargetProperties;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourSEDEXViewBean;
import globaz.phenix.db.communications.CPSedexContribuable;
import globaz.phenix.db.communications.CPSedexDonneesBase;
import globaz.phenix.db.communications.CPSedexDonneesCommerciales;
import globaz.phenix.db.communications.CPSedexDonneesPrivees;
import globaz.phenix.listes.itext.CPIListeCommunicationRetourDetailFiscJuParam;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.util.DocumentInfoPhenix;

/**
 * Insérez la description du type ici. Date de création : (26.02.2003 16:54:19)
 * 
 * @author: Administrator
 */
public class CPImpressionCommunicationRetourDetailFiscSEDEX1_Doc extends CPImpressionCommunicationRetourDetailFisc_Doc {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CPSedexContribuable contribuable = null;
    private CPSedexDonneesBase donneesBase = null;
    private CPSedexDonneesCommerciales donneesCommerciales = null;
    private CPSedexDonneesPrivees donneesPrivees = null;
    private boolean genererDetail = true;
    private CPCommunicationFiscaleRetourSEDEXViewBean retour = null;

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:56:39)
     */
    public CPImpressionCommunicationRetourDetailFiscSEDEX1_Doc() throws Exception {
        super();
    }

    public CPImpressionCommunicationRetourDetailFiscSEDEX1_Doc(BProcess parent) throws FWIException {
        super(parent);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 17:00:08)
     * 
     * @param session
     *            globaz.globall.db.BSession
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public CPImpressionCommunicationRetourDetailFiscSEDEX1_Doc(BSession session) throws FWIException {
        super(session);
    }

    @Override
    public void afterBuildReport() {
        try {
            setDocumentInfo();
        } catch (Exception e) {
            this._addError("Error detail: " + getRetour().getIdRetour());
        }
        super.afterBuildReport();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        try {
            genererDetail = false;
            donneesPrivees = new CPSedexDonneesPrivees();
            donneesPrivees.setSession(getSession());
            donneesPrivees.setIdRetour(getDonneesBase().getIdRetour());
            donneesPrivees.retrieve();
            donneesCommerciales = new CPSedexDonneesCommerciales();
            donneesCommerciales.setSession(getSession());
            donneesCommerciales.setIdRetour(getDonneesBase().getIdRetour());
            donneesCommerciales.retrieve();
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_DATE_IMPRESSION,
                    JACalendar.todayJJsMMsAAAA());
            super.setParametres("P_ANNEE", getRetour().getAnnee1());
            super.setParametres("P_REF_INTERNE", contribuable.getOurBusinessReferenceID());
            super.setParametres("P_REF_EXTERNE", contribuable.getYourBusinessReferenceId());

            if (JadeStringUtil.isBlankOrZero(getDonneesPrivees().getPensions())) {
                super.setParametres("P_PENSION", "0");
            } else {
                super.setParametres("P_PENSION", getDonneesPrivees().getPensions());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesPrivees().getPensionsPillar1())) {
                super.setParametres("P_RENTE_PILIER1", "0");
            } else {
                super.setParametres("P_RENTE_PILIER1", getDonneesPrivees().getPensionsPillar1());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesPrivees().getPensionsPillar2())) {
                super.setParametres("P_RENTE_PILIER2", "0");
            } else {
                super.setParametres("P_RENTE_PILIER2", getDonneesPrivees().getPensionsPillar2());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesPrivees().getPensionsPillar3a())) {
                super.setParametres("P_RENTE_PILIER3A", "0");
            } else {
                super.setParametres("P_RENTE_PILIER3A", getDonneesPrivees().getPensionsPillar3a());
            }

            if (JadeStringUtil.isBlankOrZero(getDonneesPrivees().getPensionsPillar3b())) {
                super.setParametres("P_RENTE_PILIER3B", "0");
            } else {
                super.setParametres("P_RENTE_PILIER3B", getDonneesPrivees().getPensionsPillar3b());
            }

            if (JadeStringUtil.isBlankOrZero(getDonneesPrivees().getAnnuities())) {
                super.setParametres("P_RENTE_VIAGERE", "0");
            } else {
                super.setParametres("P_RENTE_VIAGERE", getDonneesPrivees().getAnnuities());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesPrivees().getOtherPensions())) {
                super.setParametres("P_AUTRE_RENTE", "0");
            } else {
                super.setParametres("P_AUTRE_RENTE", getDonneesPrivees().getOtherPensions());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesPrivees().getMilitaryInsurancePensions())) {
                super.setParametres("P_ASS_MILITAIRE", "0");
            } else {
                super.setParametres("P_ASS_MILITAIRE", getDonneesPrivees().getMilitaryInsurancePensions());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesPrivees().getPerDiemAllowance())) {
                super.setParametres("P_IJ", "0");
            } else {
                super.setParametres("P_IJ", getDonneesPrivees().getPerDiemAllowance());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesPrivees().getMaintenanceContribution())) {
                super.setParametres("P_PENSION_ALIMENTAIRE", "0");
            } else {
                super.setParametres("P_PENSION_ALIMENTAIRE", getDonneesPrivees().getMaintenanceContribution());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesPrivees().getChildAllowance())) {
                super.setParametres("P_ALLOC_ENFANT", "0");
            } else {
                super.setParametres("P_ALLOC_ENFANT", getDonneesPrivees().getChildAllowance());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesPrivees().getPatentLicense())) {
                super.setParametres("P_BREVET", "0");
            } else {
                super.setParametres("P_BREVET", getDonneesPrivees().getPatentLicense());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesPrivees().getSecurities())) {
                super.setParametres("P_TITRE_ET_AVOIR", "0");
            } else {
                super.setParametres("P_TITRE_ET_AVOIR", getDonneesPrivees().getSecurities());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesPrivees().getResidencyEntitlement())) {
                super.setParametres("P_DROIT_HABITATION", "0");
            } else {
                super.setParametres("P_DROIT_HABITATION", getDonneesPrivees().getResidencyEntitlement());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesPrivees().getCash())) {
                super.setParametres("P_ARGENT_LIQUIDE", "0");
            } else {
                super.setParametres("P_ARGENT_LIQUIDE", getDonneesPrivees().getCash());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesPrivees().getLifeInsurance())) {
                super.setParametres("P_ASSURANCE_VIE", "0");
            } else {
                super.setParametres("P_ASSURANCE_VIE", getDonneesPrivees().getLifeInsurance());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesPrivees().getMotorVehicle())) {
                super.setParametres("P_VEHICULE", "0");
            } else {
                super.setParametres("P_VEHICULE", getDonneesPrivees().getMotorVehicle());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesPrivees().getInheritance())) {
                super.setParametres("P_PART_HERITAGE", "0");
            } else {
                super.setParametres("P_PART_HERITAGE", getDonneesPrivees().getInheritance());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesPrivees().getOtherAssets())) {
                super.setParametres("P_AUTRE_PATRIMOINE", "0");
            } else {
                super.setParametres("P_AUTRE_PATRIMOINE", getDonneesPrivees().getOtherAssets());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesPrivees().getRealEstateProperties())) {
                super.setParametres("P_IMMEUBLE", "0");
            } else {
                super.setParametres("P_IMMEUBLE", getDonneesPrivees().getRealEstateProperties());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesPrivees().getCompanyShares())) {
                super.setParametres("P_PART_PATRIMOINE", "0");
            } else {
                super.setParametres("P_PART_PATRIMOINE", getDonneesPrivees().getCompanyShares());
            }

            if (JadeStringUtil.isBlankOrZero(getDonneesPrivees().getDebts())) {
                super.setParametres("P_DETTE_PRIVEE", "0");
            } else {
                super.setParametres("P_DETTE_PRIVEE", getDonneesPrivees().getDebts());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesPrivees().getTaxableIncomeInAccordanceWithDBG())) {
                super.setParametres("P_LIFD", "0");
            } else {
                super.setParametres("P_LIFD", getDonneesPrivees().getTaxableIncomeInAccordanceWithDBG());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesPrivees().getTaxableIncomeExpenseTaxation())) {
                super.setParametres("P_BASE_CALCUL", "0");
            } else {
                super.setParametres("P_BASE_CALCUL", getDonneesPrivees().getTaxableIncomeExpenseTaxation());
            }
            // Données commerciales
            super.setParametres("P_DEBUT_ACTIVITE", getDonneesCommerciales().getCommencementOfSelfEmployment());
            super.setParametres("P_FIN_ACTIVITE", getDonneesCommerciales().getEndOfSelfEmployment());
            if (JadeStringUtil.isBlankOrZero(getDonneesCommerciales().getMainIncome())) {
                super.setParametres("P_REVENU_IND", "0");
            } else {
                super.setParametres("P_REVENU_IND", getDonneesCommerciales().getMainIncome());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesCommerciales().getMainIncomeInAgriculture())) {
                super.setParametres("P_REVENU_AGRICOLE", "0");
            } else {
                super.setParametres("P_REVENU_AGRICOLE", getDonneesCommerciales().getMainIncomeInAgriculture());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesCommerciales().getMainIncomeInRealEstateTrade())) {
                super.setParametres("P_IMMOBILIER", "0");
            } else {
                super.setParametres("P_IMMOBILIER", getDonneesCommerciales().getMainIncomeInRealEstateTrade());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesCommerciales().getPartTimeEmployment())) {
                super.setParametres("P_REVENU_ACC_IND", "0");
            } else {
                super.setParametres("P_REVENU_ACC_IND", getDonneesCommerciales().getPartTimeEmployment());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesCommerciales().getPartTimeEmploymentInAgriculture())) {
                super.setParametres("P_REVENU_ACC_AGR", "0");
            } else {
                super.setParametres("P_REVENU_ACC_AGR", getDonneesCommerciales().getPartTimeEmploymentInAgriculture());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesCommerciales().getPartTimeEmploymentInRealEstateTrade())) {
                super.setParametres("P_REVENU_ACC_IMM", "0");
            } else {
                super.setParametres("P_REVENU_ACC_IMM", getDonneesCommerciales()
                        .getPartTimeEmploymentInRealEstateTrade());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesCommerciales().getRealisationProfit())) {
                super.setParametres("P_GAIN_LIQUIDATION", "0");
            } else {
                super.setParametres("P_GAIN_LIQUIDATION", getDonneesCommerciales().getRealisationProfit());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesCommerciales().getCooperativeShares())) {
                super.setParametres("P_PART_REV_SOCIETE", "0");
            } else {
                super.setParametres("P_PART_REV_SOCIETE", getDonneesCommerciales().getCooperativeShares());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesCommerciales().getSecuritiesIncome())) {
                super.setParametres("P_TITRE_SOCIETE", "0");
            } else {
                super.setParametres("P_TITRE_SOCIETE", getDonneesCommerciales().getSecuritiesIncome());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesCommerciales().getOtherIncome())) {
                super.setParametres("P_AUTRE_REV_COMM", "0");
            } else {
                super.setParametres("P_AUTRE_REV_COMM", getDonneesCommerciales().getOtherIncome());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesCommerciales().getResidencyEntitlement())) {
                super.setParametres("P_DROIT_HABIT_COMM", "0");
            } else {
                super.setParametres("P_DROIT_HABIT_COMM", getDonneesCommerciales().getResidencyEntitlement());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesCommerciales().getIncomeRealEstate())) {
                super.setParametres("P_REVE_IMME_COMM", "0");
            } else {
                super.setParametres("P_REVE_IMME_COMM", getDonneesCommerciales().getIncomeRealEstate());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesCommerciales().getDebtInterest())) {
                super.setParametres("P_INT_DETTE_COMM", "0");
            } else {
                super.setParametres("P_INT_DETTE_COMM", getDonneesCommerciales().getDebtInterest());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesCommerciales().getDonations())) {
                super.setParametres("P_ALLOC_VOLONTAIRE", "0");
            } else {
                super.setParametres("P_ALLOC_VOLONTAIRE", getDonneesCommerciales().getDonations());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesCommerciales().getSecurities())) {
                super.setParametres("P_TITRE_COMM", "0");
            } else {
                super.setParametres("P_TITRE_COMM", getDonneesCommerciales().getSecurities());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesCommerciales().getCash())) {
                super.setParametres("P_LIQUIDE_COMM", "0");
            } else {
                super.setParametres("P_LIQUIDE_COMM", getDonneesCommerciales().getCash());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesCommerciales().getAssets())) {
                super.setParametres("P_PATRIMOINE_COMM", "0");
            } else {
                super.setParametres("P_PATRIMOINE_COMM", getDonneesCommerciales().getAssets());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesCommerciales().getRealEstateProperties())) {
                super.setParametres("P_IMMEUBLE_COMM", "0");
            } else {
                super.setParametres("P_IMMEUBLE_COMM", getDonneesCommerciales().getRealEstateProperties());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesCommerciales().getTotalAssets())) {
                super.setParametres("P_TOTAL_ACTIF", "0");
            } else {
                super.setParametres("P_TOTAL_ACTIF", getDonneesCommerciales().getTotalAssets());
            }
            if (JadeStringUtil.isBlankOrZero(getDonneesCommerciales().getDebts())) {
                super.setParametres("P_DETTE_COMMERCIAL", "0");
            } else {
                super.setParametres("P_DETTE_COMMERCIAL", getDonneesCommerciales().getDebts());
            }

        } catch (Exception e) {
            this._addError("Error detail: " + getRetour().getIdRetour());
        }
    }

    /**
     * Récupère les informations du décompte avant impression.
     */
    @Override
    public void beforeExecuteReport() {
        try {
            // Données privées
            super.setParametres("L_ANNEE", getSession().getLabel("ANNEE"));
            super.setParametres("L_REF_INTERNE", getSession().getLabel("DETAIL_FISC_SE_OURID"));
            super.setParametres("L_DATERECEPTION", getSession().getLabel("DETAIL_FISC_DATE_RECEPTION"));
            super.setParametres("L_REF_EXTERNE", getSession().getLabel("DETAIL_FISC_SE_YOURID"));
            super.setParametres("L_DONNEE_PRIVEE", getSession().getLabel("DONNEE_PRIVEE"));
            super.setParametres("L_PENSION", getSession().getLabel("PENSIONS"));
            super.setParametres("L_RENTE_PILIER1", getSession().getLabel("PENSIONSPILLAR1"));
            super.setParametres("L_RENTE_PILIER2", getSession().getLabel("PENSIONSPILLAR2"));
            super.setParametres("L_RENTE_PILIER3A", getSession().getLabel("PENSIONSPILLAR3A"));
            super.setParametres("L_RENTE_PILIER3B", getSession().getLabel("PENSIONSPILLAR3B"));
            super.setParametres("L_RENTE_VIAGERE", getSession().getLabel("ANNUITIES"));
            super.setParametres("L_AUTRE_RENTE", getSession().getLabel("OTHERPENSIONS"));
            super.setParametres("L_ASS_MILITAIRE", getSession().getLabel("MILITARYINSURANCEPENSIONS"));
            super.setParametres("L_IJ", getSession().getLabel("PERDIEMALLOWANCE"));
            super.setParametres("L_PENSION_ALIMENTAIRE", getSession().getLabel("MAINTENANCECONTRIBUTION"));
            super.setParametres("L_ALLOC_ENFANT", getSession().getLabel("CHILDALLOWANCE"));
            super.setParametres("L_BREVET", getSession().getLabel("PATENTLICENSE"));
            super.setParametres("L_TITRE_ET_AVOIR", getSession().getLabel("SECURITIES"));
            super.setParametres("L_DROIT_HABITATION", getSession().getLabel("RESIDENCYENTITLEMENT"));
            super.setParametres("L_ARGENT_LIQUIDE", getSession().getLabel("CASH"));
            super.setParametres("L_ASSURANCE_VIE", getSession().getLabel("LIFEINSURANCE"));
            super.setParametres("L_VEHICULE", getSession().getLabel("MOTORVEHICLE"));
            super.setParametres("L_PART_HERITAGE", getSession().getLabel("INHERITANCE"));
            super.setParametres("L_AUTRE_PATRIMOINE", getSession().getLabel("OTHERASSETS"));
            super.setParametres("L_PART_PATRIMOINE", getSession().getLabel("COMPANYSHARES"));
            super.setParametres("L_IMMEUBLE", getSession().getLabel("REALESTATEPROPERTIES"));
            super.setParametres("L_DETTE_PRIVEE", getSession().getLabel("DEBTS"));
            super.setParametres("L_LIFD", getSession().getLabel("TAXABLEINCOMEINACCORDANCEWITHDBG"));
            super.setParametres("L_BASE_CALCUL", getSession().getLabel("TAXABLEINCOMEINACCORDANCEWITHEXPENSETAXATION"));
            // Données commerciales
            super.setParametres("L_DONNEE_COMMERCIALE", getSession().getLabel("DONNEE_COMM"));
            super.setParametres("L_DEBUT_ACTIVITE", getSession().getLabel("COMMENCEMENTOFSELFEMPLOYMENT"));
            super.setParametres("L_FIN_ACTIVITE", getSession().getLabel("ENDOFSELFEMPLOYMENT"));
            super.setParametres("L_REVENU_IND", getSession().getLabel("MAININCOME"));
            super.setParametres("L_REVENU_AGRICOLE", getSession().getLabel("MAININCOMEINAGRICULTURE"));
            super.setParametres("L_IMMOBILIER", getSession().getLabel("MAININCOMEINREALESTATETRADE"));
            super.setParametres("L_REVENU_ACC_IND", getSession().getLabel("PARTTIMEEMPLOYMENT"));
            super.setParametres("L_REVENU_ACC_AGR", getSession().getLabel("PARTTIMEEMPLOYMENTINAGRICULTURE"));
            super.setParametres("L_REVENU_ACC_IMM", getSession().getLabel("PARTTIMEEMPLOYMENTINREALESTATETRADE"));
            super.setParametres("L_GAIN_LIQUIDATION", getSession().getLabel("REALISATIONPROFIT"));
            super.setParametres("L_PART_REV_SOCIETE", getSession().getLabel("COOPERATIVESHARES"));
            super.setParametres("L_TITRE_SOCIETE", getSession().getLabel("SECURITIESINCOME"));
            super.setParametres("L_AUTRE_REV_COMM", getSession().getLabel("OTHERINCOME"));
            super.setParametres("L_DROIT_HABITATION", getSession().getLabel("RESIDENCYENTITLEMENT"));
            super.setParametres("L_REVE_IMME_COMM", getSession().getLabel("INCOMEREALESTATE"));
            super.setParametres("L_INT_DETTE_COMM", getSession().getLabel("DEBTINTEREST"));
            super.setParametres("L_ALLOC_VOLONTAIRE", getSession().getLabel("DONATIONS"));
            super.setParametres("L_TITRE_COMM", getSession().getLabel("SECURITIESC"));
            super.setParametres("L_LIQUIDE_COMM", getSession().getLabel("CASHC"));
            super.setParametres("L_PATRIMOINE_COMM", getSession().getLabel("ASSETSC"));
            super.setParametres("L_IMMEUBLE_COMM", getSession().getLabel("REALESTATEPROPERTIES"));
            super.setParametres("L_TOTAL_ACTIF", getSession().getLabel("TOTALASSETS"));
            super.setParametres("L_DETTE_COMMERCIAL", getSession().getLabel("DEBTSC"));
            super.setTailleLot(0);
        } catch (Exception e) {
            this._addError("false");
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 14:07:16)
     */
    @Override
    public void createDataSource() {
        try {
            super.setTemplateFile("PHENIX_DETAIL_SEDEX1");
            setDocumentTitle(getContribuable().getYourBusinessReferenceId() + " - " + getRetour().getAnnee1());
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return;
        }
        return;
    }

    public CPSedexContribuable getContribuable() {
        return contribuable;
    }

    public CPSedexDonneesBase getDonneesBase() {
        return donneesBase;
    }

    public CPSedexDonneesCommerciales getDonneesCommerciales() {
        return donneesCommerciales;
    }

    public CPSedexDonneesPrivees getDonneesPrivees() {
        return donneesPrivees;
    }

    public CPCommunicationFiscaleRetourSEDEXViewBean getRetour() {
        return retour;
    }

    @Override
    public boolean next() throws FWIException {
        return genererDetail;
    }

    public void setContribuable(CPSedexContribuable contribuable) {
        this.contribuable = contribuable;
    }

    public void setDocumentInfo() throws FWIException {
        try {
            getDocumentInfo().setDocumentProperty(CTDocumentInfoHelper.TYPE_DOCUMENT_ID,
                    ((CPApplication) getSession().getApplication()).getGedTypeDossier());
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty(CTDocumentInfoHelper.TYPE_DOCUMENT_ID, "");
        }
        try {
            getDocumentInfo().setDocumentProperty(JadeGedTargetProperties.SERVICE,
                    ((CPApplication) getSession().getApplication()).getGedService());
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty(JadeGedTargetProperties.SERVICE, "");
        }

        getDocumentInfo().setDocumentTypeNumber("0132CCP");
        getDocumentInfo().setDocumentType("0132CCP");

        getDocumentInfo().setDocumentProperty(CADocumentInfoHelper.SECTION_ID_EXTERNE, getRetour().getAnnee1());
        getDocumentInfo().setDocumentProperty(DocumentInfoPhenix.DECISION_GENRE, getRetour().getGenreAffilie());
        try {
            getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_NON_FORMATTE,
                    CPToolBox.unFormat(getRetour().getNumAffilie()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_NON_FORMATTE, "");
        }
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_FORMATTE, getRetour().getNumAffilie());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_ID, getRetour().getIdTiers());

        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NOM, getRetour().getNom());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_PRENOM, getRetour().getPrenom());
        /* Personne Avs */
        getDocumentInfo()
                .setDocumentProperty(TIDocumentInfoHelper.TIERS_DATE_NAISSANCE, getRetour().getDateNaissance());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_FORMATTE, getRetour().getNumAvs(1));
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE,
                JadeStringUtil.removeChar(getRetour().getNumAvs(0), '.'));
    }

    public void setDonneesBase(CPSedexDonneesBase donneesBase) {
        this.donneesBase = donneesBase;
    }

    public void setDonneesCommerciales(CPSedexDonneesCommerciales donneesCommerciales) {
        this.donneesCommerciales = donneesCommerciales;
    }

    public void setDonneesPrivees(CPSedexDonneesPrivees donneesPrivees) {
        this.donneesPrivees = donneesPrivees;
    }

    public void setRetour(CPCommunicationFiscaleRetourSEDEXViewBean retour) {
        this.retour = retour;
    }
}
