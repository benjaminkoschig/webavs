package globaz.phenix.documentsItext;

import globaz.commons.nss.NSUtil;
import globaz.docinfo.CADocumentInfoHelper;
import globaz.docinfo.CTDocumentInfoHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAStringFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.ged.target.JadeGedTargetProperties;
import globaz.jade.log.JadeLogger;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourSEDEXManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourSEDEXViewBean;
import globaz.phenix.db.communications.CPSedexConjoint;
import globaz.phenix.db.communications.CPSedexContribuable;
import globaz.phenix.db.communications.CPSedexDonneesBase;
import globaz.phenix.listes.itext.CPIListeCommunicationRetourDetailFiscJuParam;
import globaz.phenix.listes.itext.CPIListeCommunicationRetourDetailFiscVdParam;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.util.DocumentInfoPhenix;
import globaz.pyxis.db.tiers.TITiersViewBean;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * Insérez la description du type ici. Date de création : (26.02.2003 16:54:19)
 * 
 * @author: Administrator
 */
public class CPImpressionCommunicationRetourDetailFiscSEDEX_Doc extends CPImpressionCommunicationRetourDetailFisc_Doc {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CPSedexConjoint conjoint = null;
    private CPSedexContribuable contribuable = null;
    private CPSedexDonneesBase donneesBase = null;

    private CPCommunicationFiscaleRetourSEDEXViewBean entity = null;

    String langueIso = "";
    private CPCommunicationFiscaleRetourSEDEXManager manager = null;
    private long progressCounter = -1;

    private BStatement statement = null;
    private Boolean wantDetail = null;

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:56:39)
     */
    public CPImpressionCommunicationRetourDetailFiscSEDEX_Doc() throws Exception {
        super();
    }

    public CPImpressionCommunicationRetourDetailFiscSEDEX_Doc(BProcess parent) throws FWIException {
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
    public CPImpressionCommunicationRetourDetailFiscSEDEX_Doc(BSession session) throws FWIException {
        super(session);
    }

    @Override
    public void afterBuildReport() {
        try {
            setDocumentInfo();
            if (Boolean.TRUE.equals(getWantDetail())) {
                JasperPrint detailSedex = getDetailSedex();
                if (detailSedex != null) {
                    detailSedex.setName("DETAIL");
                    super.getDocumentList().add(detailSedex);
                }
            }
        } catch (Exception e) {
            this._addError("Error detail: " + entity.getIdRetour());
        }
        super.afterBuildReport();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        try {
            super.setParametres("P_SENDER_ID", contribuable.getSenderId());
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_DATE_IMPRESSION,
                    JACalendar.todayJJsMMsAAAA());
            TITiersViewBean tierslu = entity.getTiers();
            if (tierslu != null) {
                langueIso = tierslu.getLangueIso();
            }
            if (!JadeStringUtil.isBlank(contribuable.getYourBusinessReferenceId())) {
                super.setParametres("P_SEDEX_TITRE",
                        getSession().getLabel("DETAIL_SEDEX") + " : " + contribuable.getYourBusinessReferenceId());
            } else {
                super.setParametres("P_SEDEX_TITRE", getSession().getLabel("DETAIL_SEDEX"));
            }

            super.setParametres("P_GENREAFFILIE", getSession().getCodeLibelle(entity.getGenreAffilie()));
            super.setParametres("P_ANNEE", entity.getAnnee1());
            super.setParametres("P_REF_INTERNE", contribuable.getOurBusinessReferenceID());
            super.setParametres("P_DATERECEPTION",
                    JACalendar.format(entity.getDateRetour(), JACalendar.FORMAT_DDsMMsYYYY));
            super.setParametres("P_REF_EXTERNE", contribuable.getYourBusinessReferenceId());

            super.setParametres("P_GENRE_TAXATION", getSession().getCodeLibelle(entity.getGenreTaxation()));
            if (contribuable.getAssessmentType().equals("1") || contribuable.getAssessmentType().equals("2")
                    || contribuable.getAssessmentType().equals("3") || contribuable.getAssessmentType().equals("4")
                    || contribuable.getAssessmentType().equals("5") || contribuable.getAssessmentType().equals("11")) {
                super.setParametres("P_TYPE_EVALUATION",
                        getSession().getLabel("ASSESSMENTTYPE_" + contribuable.getAssessmentType()));
            }
            if (contribuable.getReportType().equals("1") || contribuable.getReportType().equals("2")
                    || contribuable.getReportType().equals("4")) {
                super.setParametres("P_TYPE_REPONSE",
                        getSession().getLabel("REPORTTYPE_" + contribuable.getReportType()));
            }
            super.setParametres("P_DATE_ASSIETTE", CPToolBox.formatDate(contribuable.getAssessmentDate(), 2));
            // Informations sur le contribuable
            String nss = "";
            if (!JadeStringUtil.isBlankOrZero(contribuable.getVn())) {
                if (NSUtil.unFormatAVS(contribuable.getVn().trim()).length() == 13) {
                    nss = NSUtil.formatAVSNewNum(contribuable.getVn().trim());
                } else if (NSUtil.unFormatAVS(contribuable.getVn().trim()).length() == 11) {
                    nss = JAStringFormatter.formatAVS(contribuable.getVn().trim());
                } else {
                    nss = contribuable.getOurBusinessReferenceID().trim();
                }
            }
            super.setParametres("P_NSS_CONTRIBUABLE", nss);
            String adresse = contribuable.getOfficialName() + " " + contribuable.getFirstName();
            if (JadeStringUtil.isBlank(adresse) && (entity.getTiers() != null)
                    && (!JadeStringUtil.isEmpty(entity.getTiers().getNomPrenom()))) {
                adresse = entity.getTiers().getNomPrenom() + "\n";
            } else {
                adresse = adresse + "\n";
            }
            if (!JadeStringUtil.isBlank(contribuable.getAddressLine1())
                    && !contribuable
                            .getAddressLine1()
                            .trim()
                            .equalsIgnoreCase(
                                    contribuable.getOfficialName().trim() + " " + contribuable.getFirstName().trim())) {
                adresse = adresse + contribuable.getAddressLine1() + "\n";
            }
            if (!JadeStringUtil.isBlank(contribuable.getAddressLine2())
                    && !contribuable.getAddressLine2().trim().equalsIgnoreCase(contribuable.getOfficialName().trim())) {
                adresse = adresse + contribuable.getAddressLine2() + "\n";
            }
            if (!JadeStringUtil.isBlank(contribuable.getStreet())
                    || !JadeStringUtil.isBlank(contribuable.getHouseNumber())) {
                adresse = adresse + contribuable.getStreet() + contribuable.getHouseNumber() + "\n";
            }
            /*
             * if (!JadeStringUtil.isBlank(this.contribuable.getLocality())) { adresse = adresse +
             * this.contribuable.getLocality() + "\n"; }
             */
            if (!JadeStringUtil.isBlank(contribuable.getTown())) {
                adresse = adresse + contribuable.getSwissZipCode() + "  " + contribuable.getTown() + "\n";
            }
            if (!JadeStringUtil.isBlank(contribuable.getCountry())) {
                adresse = adresse + contribuable.getCountry();
            }
            super.setParametres("P_DESC_CONTRIBUABLE", adresse);
            super.setParametres("P_SEXE_CONTRIBUABLE", CPToolBox.getLibSexe(getSession(), contribuable.getSex()));
            super.setParametres("P_DATE_NAISSANCE_CONTRIBUABLE",
                    CPToolBox.formatDate(contribuable.getYearMonthDay(), 2));
            super.setParametres("P_ETATCIVIL_CONTRIBUABLE",
                    CPToolBox.getLibEtatCivil(getSession(), contribuable.getMaritalStatus()));
            super.setParametres("P_DATE_MARIAGE_CONTRIBUABLE",
                    CPToolBox.formatDate(contribuable.getDateOfMaritalStatus(), 2));
            if (contribuable.getDateOfEntry().length() > 10) {
                super.setParametres("P_DATE_ENTREE_CONTRIBUABLE",
                        CPToolBox.formatDate(contribuable.getDateOfEntry().substring(0, 10), 2));
            } else {
                super.setParametres("P_DATE_ENTREE_CONTRIBUABLE",
                        CPToolBox.formatDate(contribuable.getDateOfEntry(), 2));
                ;
            }
            // Informations sur le conjoint
            nss = "";
            if (!JadeStringUtil.isBlankOrZero(conjoint.getVn())) {
                if (NSUtil.unFormatAVS(conjoint.getVn().trim()).length() == 13) {
                    nss = NSUtil.formatAVSNewNum(conjoint.getVn().trim());
                } else if (NSUtil.unFormatAVS(conjoint.getVn().trim()).length() == 11) {
                    nss = JAStringFormatter.formatAVS(conjoint.getVn().trim());
                } else {
                    nss = contribuable.getOurBusinessReferenceID().trim();
                }
            }
            if (!JadeStringUtil.isEmpty(conjoint.getOfficialName())
                    || !JadeStringUtil.isBlankOrZero((conjoint.getVn()))) {
                super.setParametres("L_CONJOINT", getSession().getLabel("CONJOINT"));
                super.setParametres("L_DONNEE_CONJOINT", getSession().getLabel("CONJOINT"));
                super.setParametres("P_NSS_CONJOINT", nss);
                // Adresse conjoint
                adresse = conjoint.getOfficialName() + " " + conjoint.getFirstName() + "\n";
                if (!JadeStringUtil.isBlank(conjoint.getStreet()) || !JadeStringUtil.isBlank(conjoint.getHouseNumber())) {
                    adresse = adresse + conjoint.getStreet() + conjoint.getHouseNumber() + "\n";
                }
                if (!JadeStringUtil.isBlank(conjoint.getTown())) {
                    adresse = adresse + conjoint.getSwissZipCode() + " " + conjoint.getTown() + "\n";
                }
                if (!JadeStringUtil.isBlank(conjoint.getCountry())) {
                    adresse = adresse + conjoint.getCountry();
                }
                super.setParametres("P_DESC_CONJOINT", adresse);
                super.setParametres("P_SEXE_CONJOINT", CPToolBox.getLibSexe(getSession(), conjoint.getSex()));
                super.setParametres("P_DATE_NAISSANCE_CONJOINT", CPToolBox.formatDate(conjoint.getYearMonthDay(), 2));
                super.setParametres("P_ETATCIVIL_CONJOINT",
                        CPToolBox.getLibEtatCivil(getSession(), conjoint.getMaritalStatus()));
                super.setParametres("P_DATE_MARIAGE_CONJOINT",
                        CPToolBox.formatDate(conjoint.getDateOfMaritalStatus(), 2));
                if (conjoint.getDateOfEntry().length() > 10) {
                    super.setParametres("P_DATE_ENTREE_CONJOINT",
                            CPToolBox.formatDate(conjoint.getDateOfEntry().substring(0, 10), 2));
                } else {
                    super.setParametres("P_DATE_ENTREE_CONJOINT", CPToolBox.formatDate(conjoint.getDateOfEntry(), 2));
                    ;
                }
            } else {
                super.setParametres("L_CONJOINT", "");
                super.setParametres("P_NSS_CONJOINT", "");
                // Adresse conjoint
                super.setParametres("P_DESC_CONJOINT", "");
                super.setParametres("P_SEXE_CONJOINT", "");
                super.setParametres("P_DATE_NAISSANCE_CONJOINT", "");
                super.setParametres("P_ETATCIVIL_CONJOINT", "");
                super.setParametres("P_DATE_MARIAGE_CONJOINT", "");
                super.setParametres("P_DATE_ENTREE_CONJOINT", "");
            }
            super.setParametres("P_REMARQUE", contribuable.getRemark());
            super.setParametres("P_SALAIRE", donneesBase.getEmploymentIncome());
            super.setParametres("P_REVENU_IND", donneesBase.getIncomeFromSelfEmployment());
            super.setParametres("P_RENTE_NE", donneesBase.getPensionIncome());
            super.setParametres("P_REVENU_AGRICOLE", donneesBase.getMainIncomeInAgriculture());
            super.setParametres("P_CAPITAL", donneesBase.getCapital());
            super.setParametres("P_PATRIMOINE_COMMERCIAL", donneesBase.getAssets());
            if (JadeStringUtil.isBlank(donneesBase.getNonDomesticIncomePresent())) {
                super.setParametres("P_REVENU_ETRANGER", "");
            } else {
                int revEtranger = Integer.parseInt(donneesBase.getNonDomesticIncomePresent());
                if (revEtranger == 0) {
                    super.setParametres("P_REVENU_ETRANGER", getSession().getLabel("NON"));
                } else if (revEtranger == 1) {
                    super.setParametres("P_REVENU_ETRANGER", getSession().getLabel("OUI"));
                } else if (revEtranger == -1) {
                    super.setParametres("P_REVENU_ETRANGER", getSession().getLabel("INCONNU"));
                } else {
                    super.setParametres("P_REVENU_ETRANGER", "");
                }
            }
            super.setParametres("P_RACHAT_LPP", donneesBase.getPurchasingLPP());
            super.setParametres("P_RENTE_PONT", donneesBase.getOASIBridgingPension());
            // Données du conjoint
            if (!JadeStringUtil.isEmpty(conjoint.getOfficialName())
                    || !JadeStringUtil.isBlankOrZero((conjoint.getVn()))) {
                super.setParametres("P_SALAIRE_CJT", donneesBase.getEmploymentIncomeCjt());
                super.setParametres("P_REVENU_IND_CJT", donneesBase.getIncomeFromSelfEmploymentCjt());
                super.setParametres("P_RENTE_NE_CJT", donneesBase.getPensionIncomeCjt());
                super.setParametres("P_REVENU_AGRICOLE_CJT", donneesBase.getMainIncomeInAgricultureCjt());
                super.setParametres("P_CAPITAL_CJT", donneesBase.getCapitalCjt());
                super.setParametres("P_PATRIMOINE_COMMERCIAL_CJT", donneesBase.getAssetsCjt());
                if (JadeStringUtil.isBlank(donneesBase.getNonDomesticIncomePresentCjt())) {
                    super.setParametres("P_REVENU_ETRANGER_CJT", "");
                } else {
                    int revEtranger = Integer.parseInt(donneesBase.getNonDomesticIncomePresentCjt());
                    if (revEtranger == 0) {
                        super.setParametres("P_REVENU_ETRANGER_CJT", getSession().getLabel("NON"));
                    } else if (revEtranger == 1) {
                        super.setParametres("P_REVENU_ETRANGER_CJT", getSession().getLabel("OUI"));
                    } else if (revEtranger == -1) {
                        super.setParametres("P_REVENU_ETRANGER_CJT", getSession().getLabel("INCONNU"));
                    } else {
                        super.setParametres("P_REVENU_ETRANGER_CJT", "");
                    }
                }
                super.setParametres("P_RACHAT_LPP_CJT", donneesBase.getPurchasingLPPCjt());
                super.setParametres("P_RENTE_PONT_CJT", donneesBase.getOASIBridgingPensionCjt());
            } else {
                super.setParametres("P_SALAIRE_CJT", "");
                super.setParametres("P_REVENU_IND_CJT", "");
                super.setParametres("P_RENTE_NE_CJT", "");
                super.setParametres("P_REVENU_AGRICOLE_CJT", "");
                super.setParametres("P_CAPITAL_CJT", "");
                super.setParametres("P_PATRIMOINE_COMMERCIAL_CJT", "");
                super.setParametres("P_REVENU_ETRANGER_CJT", "");
                super.setParametres("P_RACHAT_LPP_CJT", "");
                super.setParametres("P_RENTE_PONT_CJT", "");
            }
        } catch (Exception e) {
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_NOMPRENOM, "Tiers(Erreur): ");
        }
    }

    /**
     * Récupère les informations du décompte avant impression.
     */
    @Override
    public void beforeExecuteReport() {
        // Variable pour le comptage
        try {
            super.setParametres("L_GENREAFFILIE", getSession().getLabel("DETAIL_FISC_SE_GENRE_AFFILIE"));
            super.setParametres("L_ANNEE", getSession().getLabel("ANNEE"));
            super.setParametres("L_REF_INTERNE", getSession().getLabel("DETAIL_FISC_SE_OURID"));
            super.setParametres("L_DATERECEPTION", getSession().getLabel("DETAIL_FISC_DATE_RECEPTION"));
            super.setParametres("L_REF_EXTERNE", getSession().getLabel("DETAIL_FISC_SE_YOURID"));
            super.setParametres("L_DONNEE_RECAP", getSession().getLabel("DETAIL_FISC_SE_DONNEE_RECAP"));
            super.setParametres("L_GENRE_TAXATION", getSession().getLabel("DETAIL_FISC_GENRE_TAX"));
            super.setParametres("L_TYPE_EVALUATION", getSession().getLabel("ASSESSMENTTYPE"));
            super.setParametres("L_TYPE_REPONSE", getSession().getLabel("REPORTTYPE"));
            super.setParametres("L_DATE_ASSIETTE", getSession().getLabel("ASSESSMENTDATE"));
            super.setParametres("L_GENRE_TAXATION", getSession().getLabel("GENRE_TAXATION"));
            super.setParametres("L_CONTRIBUABLE", getSession().getLabel("DETAIL_FISC_SE_TIERS"));
            super.setParametres("L_DATE_NAISSANCE", getSession().getLabel("YEARMONTHDAY"));
            super.setParametres("L_DATE_MARIAGE", getSession().getLabel("DATEOFMARITALSTATUS"));
            super.setParametres("L_DATE_ENTREE", getSession().getLabel("DATEOFENTRY"));
            super.setParametres("L_REMARQUE", getSession().getLabel("REMARK"));
            super.setParametres("L_DONNEE_BASE", getSession().getLabel("DONNEE_BASE"));
            super.setParametres("L_DONNEE_CONTRIBUABLE", getSession().getLabel("CONTRIBUABLE"));
            super.setParametres("L_SALAIRE", getSession().getLabel("EMPLOYMENTINCOME"));
            super.setParametres("L_REVENU_IND", getSession().getLabel("INCOMEFROMSELFEMPLOYMENT"));
            super.setParametres("L_RENTE_NE", getSession().getLabel("PENSIONINCOME"));
            super.setParametres("L_CAPITAL", getSession().getLabel("DETAIL_FISC_SE_CAP_I"));
            super.setParametres("L_RENTE_PONT", getSession().getLabel("OASIBRIDGINGPENSION"));
            super.setParametres("L_PATRIMOINE_COMMERCIAL", getSession().getLabel("ASSETS"));
            super.setParametres("L_REVENU_AGRICOLE", getSession().getLabel("MAININCOMEINAGRICULTURE"));
            super.setParametres("L_REVENU_ETRANGER", getSession().getLabel("NONDOMESTICINCOMEPRESENT"));
            super.setParametres("L_RACHAT_LPP", getSession().getLabel("PURCHASINGLPP"));
            super.setParametres("L_ETATCIVIL", getSession().getLabel("MARITALSTATUS"));
            super.setParametres("L_SEXE", getSession().getLabel("SEX"));
            super.setTailleLot(0);
            super.setImpressionParLot(true);
            // ---------------------
            // Chargement des labels
            // ---------------------
            manager.setForIdPlausibilite(getForIdPlausibilite());
            nbCommunication = manager.getCount(getTransaction());
            // nombre de communication fiscale à traiter
            // Entrer les informations pour l' état du process
            setState(getSession().getLabel("OBJEMAIL_FAPRINT_IMPRESSSIONCOMMUNICATION"));
            if (nbCommunication > 0) {
                setProgressScaleValue(nbCommunication);
            } else {
                setProgressScaleValue(1);
            }
            statement = manager.cursorOpen(getTransaction());
            if (nbCommunication == 0) {
                // le manager ne contient aucune police
                // getMemoryLog().logMessage(getSession().getLabel("CP_MSG_0151"),
                // globaz.framework.util.FWMessage.INFORMATION,
                // getClass().getName());
            }
        } catch (Exception e) {
            this._addError("false");
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            try {
                getTransaction().rollback();
            } catch (Exception f) {
                getMemoryLog().logMessage(f.getMessage(), FWMessage.FATAL, this.getClass().getName());

            } finally {
                try {
                    if (statement != null) {
                        manager.cursorClose(statement);
                    }
                } catch (Exception g) {
                    getMemoryLog().logMessage(g.getMessage(), FWMessage.FATAL, this.getClass().getName());
                }
            }
        } finally {
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 14:07:16)
     */
    @Override
    public void createDataSource() {
        try {
            super.setTemplateFile("PHENIX_DETAIL_SEDEX");
            if ("ORDER_BY_CONTRIBUABLE".equals(manager.getTri())) {
                setDocumentTitle(entity.getNumContribuable() + " - " + entity.getAnnee1());
            } else if ("ORDER_BY_AFFILIE".equals(manager.getTri())) {
                setDocumentTitle(entity.getNumAffilie() + " - " + entity.getAnnee1());
            } else if ("ORDER_BY_IFD".equals(manager.getTri())) {
                setDocumentTitle(entity.getAnnee1() + " - " + entity.getNumContribuable());
            } else if ("ORDER_BY_AVS".equals(manager.getTri())) {
                setDocumentTitle(entity.getNumeroAVS(0) + " - " + entity.getAnnee1());
            } else { // Défaut
                setDocumentTitle(entity.getNumAffilie() + " - " + entity.getAnnee1());
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return;
        }
        return;
    }

    protected JasperPrint getDetailSedex() throws Exception {
        java.util.List<?> docGenere = null;
        CPImpressionCommunicationRetourDetailFiscSEDEX1_Doc doc = new CPImpressionCommunicationRetourDetailFiscSEDEX1_Doc();
        doc.setSession(getSession());
        doc.setSendCompletionMail(false);
        doc.setDonneesBase(donneesBase);
        doc.setRetour(entity);
        doc.setContribuable(contribuable);
        doc.setImpressionParLot(false);
        doc.setFileTitle("DetailSedex");
        doc.executeProcess();
        docGenere = doc.getDocumentList();
        if (docGenere.isEmpty()) {
            return null;
        } else {
            return (JasperPrint) docGenere.get(0);
        }
    }

    public CPCommunicationFiscaleRetourSEDEXViewBean getEntity() {
        return entity;
    }

    public CPCommunicationFiscaleRetourSEDEXManager getManager() {
        return manager;
    }

    public Boolean getWantDetail() {
        return wantDetail;
    }

    @Override
    public boolean next() throws FWIException {
        try {
            if (((entity = (CPCommunicationFiscaleRetourSEDEXViewBean) manager.cursorReadNext(statement)) != null)
                    && (!entity.isNew()) && !super.isAborted()) {
                // On va récupérer les infos dans les tables sedex
                contribuable = new CPSedexContribuable();
                contribuable.setSession(getSession());
                contribuable.setIdRetour(entity.getIdRetour());
                contribuable.retrieve();
                conjoint = new CPSedexConjoint();
                conjoint.setSession(getSession());
                conjoint.setIdRetour(entity.getIdRetour());
                conjoint.retrieve();
                donneesBase = new CPSedexDonneesBase();
                donneesBase.setSession(getSession());
                donneesBase.setIdRetour(entity.getIdRetour());
                donneesBase.retrieve();
                // ---------------------------------------------------------------------
                setProgressCounter(progressCounter++);
                // ---------------------------------------------------------------------
                return true;
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            this._addError(getTransaction(), e.toString());
            throw new FWIException(e);
        }
        return false;
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

        getDocumentInfo().setDocumentProperty(CADocumentInfoHelper.SECTION_ID_EXTERNE, entity.getAnnee1());
        getDocumentInfo().setDocumentProperty(DocumentInfoPhenix.DECISION_GENRE, entity.getGenreAffilie());
        try {
            getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_NON_FORMATTE,
                    CPToolBox.unFormat(entity.getNumAffilie()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_NON_FORMATTE, "");
        }
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_FORMATTE, entity.getNumAffilie());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_ID, entity.getIdTiers());

        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_LANGUE_ISO, langueIso);
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NOM, entity.getNom());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_PRENOM, entity.getPrenom());
        /* Personne Avs */
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_DATE_NAISSANCE, entity.getDateNaissance());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_FORMATTE, entity.getNumAvs(1));
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE,
                JadeStringUtil.removeChar(entity.getNumAvs(0), '.'));
    }

    public void setEntity(CPCommunicationFiscaleRetourSEDEXViewBean entity) {
        this.entity = entity;
    }

    public void setManager(CPCommunicationFiscaleRetourSEDEXManager manager) {
        this.manager = manager;
    }

    public void setWantDetail(Boolean wantDetail) {
        this.wantDetail = wantDetail;
    }
}
