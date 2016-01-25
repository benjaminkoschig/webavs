package globaz.phenix.documentsItext;

import globaz.docinfo.CADocumentInfoHelper;
import globaz.docinfo.CTDocumentInfoHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.ged.target.JadeGedTargetProperties;
import globaz.jade.log.JadeLogger;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourVSManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourVSViewBean;
import globaz.phenix.db.communications.CPLienCommunicationsPlausi;
import globaz.phenix.db.communications.CPLienCommunicationsPlausiManager;
import globaz.phenix.db.communications.CPParametrePlausibilite;
import globaz.phenix.listes.itext.CPIListeCommunicationRetourDetailFiscVsParam;
import globaz.phenix.listes.itext.CPIListeCommunicationRetourParam;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.translation.CodeSystem;
import globaz.phenix.util.DocumentInfoPhenix;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * Insérez la description du type ici. Date de création : (26.02.2003 16:54:19)
 * 
 * @author: Administrator
 */
public class CPImpressionCommunicationRetourDetailFiscVS_Doc extends CPImpressionCommunicationRetourDetailFisc_Doc {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private CPCommunicationFiscaleRetourVSViewBean entity = null;

    private CPCommunicationFiscaleRetourVSManager manager = null;

    private long progressCounter = -1;

    private BStatement statement = null;

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:56:39)
     */
    public CPImpressionCommunicationRetourDetailFiscVS_Doc() throws Exception {
        super();
    }

    public CPImpressionCommunicationRetourDetailFiscVS_Doc(BProcess parent) throws FWIException {
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
    public CPImpressionCommunicationRetourDetailFiscVS_Doc(BSession session) throws FWIException {
        super(session);
    }

    @Override
    public void afterBuildReport() {
        super.afterBuildReport();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        try {
            super.setParametres("P_FISC_ANNEE", entity.getVsAnneeTaxation());
            // Informations communications
            super.setParametres("P_VS_NUM_FISC", entity.getDescription(1));
            super.setParametres("P_VS_TYPE", entity.getVsCodeTaxation2Code());
            super.setParametres("P_VS_CODE", entity.getVsTypeTaxation());
            super.setParametres("P_VS_NUM_CAISSE", entity.getVsNoCaisseAgenceAffilie(1));
            // Recherche description caisse
            String nomCaisse = "";
            String codeCaisse = entity.getVsNoCaisseAgenceAffilie(1);
            if (!JadeStringUtil.isBlankOrZero(codeCaisse)) {
                TIAdministrationManager admMng = new TIAdministrationManager();
                admMng.setSession(getSession());
                admMng.setForCodeAdministration(codeCaisse);
                admMng.setForGenreAdministration(TIAdministrationViewBean.CS_CAISSE_COMPENSATION);
                admMng.find();
                if (admMng.getSize() > 0) {
                    nomCaisse = ((TIAdministrationViewBean) admMng.getFirstEntity()).getNomPrenom();
                }
            }
            super.setParametres("P_VS_NOM_CAISSE", nomCaisse);
            super.setParametres("P_VS_DATE", entity.getVsDateTaxation());
            // // Informations contribuable
            super.setParametres("P_DESCRIPTION", entity.getDescription(4));
            if (JadeStringUtil.isBlankOrZero(entity.getVsNbJoursTaxation())) {
                super.setParametres("P_VS_NBJ", "");
            } else {
                super.setParametres("P_VS_NBJ", entity.getVsNbJoursTaxation());
            }
            super.setParametres("P_VS_DATE_DECES", entity.getVsDateDecesCtb());
            if (JadeStringUtil.isBlankOrZero(entity.getVsNumCtbSuivant())) {
                super.setParametres("P_VS_SUIVANT", "");
            } else {
                super.setParametres("P_VS_SUIVANT", entity.getVsNumCtbSuivant());
            }
            super.setParametres("P_VS_ETAT_CIVIL", entity.getVsEtatCivilCtb());
            super.setParametres("P_VS_SEXE", entity.getVsSexeCtb());
            super.setParametres("P_VS_NUMAVS", entity.getVsNumAvsCtb(1));
            super.setParametres("P_VS_DUREE", entity.getVsDebutActiviteCtb() + " " + entity.getVsFinActiviteCtb());
            // Définition des champs pour l'affiliation
            if (entity.getVsTypeTaxation().equalsIgnoreCase("SP") || entity.getVsTypeTaxation().equalsIgnoreCase("NA")) {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_ADRESSE_AFF, "CP / VK");
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_ADRESSE_CON, "CP / VK");
            } else {
                if (!JadeStringUtil.isIntegerEmpty(entity.getIdAffiliation())) {
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AFF, entity.getAffiliation()
                            .getAffilieNumero());
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_DEBUT_AFF, entity.getAffiliation()
                            .getDateDebut() + "-" + entity.getAffiliation().getDateFin());
                } else {
                    if (!JadeStringUtil.isBlankOrZero(entity.getVsNumAffilie())) {
                        super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AFF, entity.getVsNumAffilie());
                    } else {
                        super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AFF, "");
                    }
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_DEBUT_AFF, "");

                }
                if (!JadeStringUtil.isIntegerEmpty(entity.getIdTiers())) {
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_DATE_N_AFF, entity.getDateNaissance());
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AVS_AFF, entity.getTiers()
                            .getNumAvsActuel());
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_ADRESSE_AFF, entity.getTiers()
                            .getAdresseAsString());
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_ETAT_CIVIL_AFF,
                            CodeSystem.getLibelle(getSession(), entity.getEtatCivil()));
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_CODE_SEXE_AFF,
                            CodeSystem.getLibelle(getSession(), entity.getTiers().getSexe()));
                } else {
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_DATE_N_AFF, "");
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AVS_AFF, "");
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AFF, "");
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_ADRESSE_AFF, "");
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_ETAT_CIVIL_AFF, "");
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_CODE_SEXE_AFF, "");
                }
                // Définition des champs pour le conjoint
                if (!JadeStringUtil.isIntegerEmpty(entity.getIdAffiliationConjoint())) {
                    if (!JadeStringUtil.isBlankOrZero(entity.getVsNumAffilieConjoint(0))) {
                        super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AFF_CON, entity
                                .getAffiliationConjoint().getAffilieNumero());
                    } else {
                        super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AFF_CON, "");
                    }
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_PERIODE_CON, entity
                            .getAffiliationConjoint().getDateDebut()
                            + "-"
                            + entity.getAffiliationConjoint().getDateFin());
                } else {
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AFF_CON, " ");
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_PERIODE_CON, "");
                }
                if (!JadeStringUtil.isIntegerEmpty(entity.getIdConjoint())) {
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_DATE_N_CON, entity.getConjoint()
                            .getDateNaissance());
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AVS_CON, entity.getConjoint()
                            .getNumAvsActuel());
                    if (entity.getConjoint().getAdresseAsString().length() > 0) {
                        super.setParametres(CPIListeCommunicationRetourParam.PARAM_ADRESSE_CON, entity.getConjoint()
                                .getAdresseAsString());
                    } else {
                        super.setParametres(CPIListeCommunicationRetourParam.PARAM_ADRESSE_CON, "");
                    }
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_ETAT_CIVIL_CON,
                            CodeSystem.getLibelle(getSession(), entity.getConjoint().getEtatCivil()));
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_CODE_SEXE_CON,
                            CodeSystem.getLibelle(getSession(), entity.getConjoint().getSexe()));
                } else {
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_DATE_N_CON, "");
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AVS_CON, "");
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_ADRESSE_CON, "");
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_ETAT_CIVIL_CON, "");
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_CODE_SEXE_CON, "");
                }
            }
            // // Informations indépendant
            if (!JadeStringUtil.isBlankOrZero(entity.getVsRevenuNonAgricoleCtb())) {
                super.setParametres("P_VS_RNA", entity.getVsRevenuNonAgricoleCtb());
            } else {
                super.setParametres("P_VS_RNA", "0.00");
            }
            if (!JadeStringUtil.isBlankOrZero(entity.getVsRevenuAgricoleCtb())) {
                super.setParametres("P_VS_RA", entity.getVsRevenuAgricoleCtb());
            } else {
                super.setParametres("P_VS_RA", "0.00");
            }
            if (!JadeStringUtil.isBlankOrZero(entity.getVsRevenuNonAgricoleCtb())
                    || !JadeStringUtil.isBlankOrZero(entity.getVsRevenuAgricoleCtb())) {
                FWCurrency curr = new FWCurrency(JANumberFormatter.deQuote(entity.getVsRevenuAgricoleCtb()));
                curr.add(JANumberFormatter.deQuote(entity.getVsRevenuNonAgricoleCtb()));
                super.setParametres("P_VS_TOT_REV", JANumberFormatter.fmt(curr.toString(), true, false, true, 2));
            } else {
                super.setParametres("P_VS_TOT_REV", "0.00");
            }
            if (!JadeStringUtil.isBlankOrZero(entity.getVsCapitalPropreEngageEntrepriseCtb())) {
                super.setParametres("P_VS_CAP", entity.getVsCapitalPropreEngageEntrepriseCtb());
            } else {
                super.setParametres("P_VS_CAP", "0.00");
            }
            if (!JadeStringUtil.isBlankOrZero(entity.getVsRevenuNonAgricoleConjoint())) {
                super.setParametres("P_VS_RNA_CJT", entity.getVsRevenuNonAgricoleConjoint());
            } else {
                super.setParametres("P_VS_RNA_CJT", "0.00");
            }
            if (!JadeStringUtil.isBlankOrZero(entity.getVsRevenuAgricoleConjoint())) {
                super.setParametres("P_VS_RA_CJT", entity.getVsRevenuAgricoleConjoint());
            } else {
                super.setParametres("P_VS_RA_CJT", "0.00");
            }
            if (!JadeStringUtil.isBlankOrZero(entity.getVsRevenuNonAgricoleConjoint())
                    || !JadeStringUtil.isBlankOrZero(entity.getVsRevenuAgricoleConjoint())) {
                FWCurrency curr = new FWCurrency(JANumberFormatter.deQuote(entity.getVsRevenuAgricoleConjoint()));
                curr.add(JANumberFormatter.deQuote(entity.getVsRevenuNonAgricoleConjoint()));
                super.setParametres("P_VS_TOT_REV_CJT", JANumberFormatter.fmt(curr.toString(), true, false, true, 2));
            } else {
                super.setParametres("P_VS_TOT_REV_CJT", "0.00");
            }
            if (!JadeStringUtil.isBlankOrZero(entity.getVsCapitalPropreEngageEntrepriseConjoint())) {
                super.setParametres("P_VS_CAP_CJT", entity.getVsCapitalPropreEngageEntrepriseConjoint());
            } else {
                super.setParametres("P_VS_CAP_CJT", "0.00");
            }
            // Rachat LPP
            if (!JadeStringUtil.isBlankOrZero(entity.getVsRachatLpp())) {
                super.setParametres("P_VS_RACHAT_LPP", entity.getVsRachatLpp());
            } else {
                super.setParametres("P_VS_RACHAT_LPP", "0.00");
            }
            if (!JadeStringUtil.isBlankOrZero(entity.getVsRachatLppCjt())) {
                super.setParametres("P_VS_RACHAT_LPP_CJT", entity.getVsRachatLppCjt());
            } else {
                super.setParametres("P_VS_RACHAT_LPP_CJT", "0.00");
            }
            // // Informations non actif
            if (!JadeStringUtil.isBlankOrZero(entity.getVsRevenuRenteCtb())
                    || !JadeStringUtil.isBlankOrZero(entity.getVsRevenuRenteConjoint())) {
                FWCurrency curr = new FWCurrency(JANumberFormatter.deQuote(entity.getVsRevenuRenteCtb()));
                curr.add(JANumberFormatter.deQuote(entity.getVsRevenuRenteConjoint()));
                super.setParametres("P_VS_RR", JANumberFormatter.fmt(curr.toString(), true, false, true, 2));
            } else {
                super.setParametres("P_VS_RR", "0.00");
            }
            if (!JadeStringUtil.isBlankOrZero(entity.getVsFortunePriveeCtb())
                    || !JadeStringUtil.isBlankOrZero(entity.getVsFortunePriveeConjoint())) {
                FWCurrency curr = new FWCurrency(JANumberFormatter.deQuote(entity.getVsFortunePriveeCtb()));
                curr.add(JANumberFormatter.deQuote(entity.getVsFortunePriveeConjoint()));
                super.setParametres("P_VS_FORT", JANumberFormatter.fmt(curr.toString(), true, false, true, 2));
            } else {
                super.setParametres("P_VS_FORT", "0.00");
            }
            // // Autre
            if (!JadeStringUtil.isBlankOrZero(entity.getVsSalairesContribuable())) {
                super.setParametres("P_VS_SAL", entity.getVsSalairesContribuable());
            } else {
                super.setParametres("P_VS_SAL", "0.00");
            }
            if (!JadeStringUtil.isBlankOrZero(entity.getVsSalairesConjoint())) {
                super.setParametres("P_VS_SAL_CJT", entity.getVsSalairesConjoint());
            } else {
                super.setParametres("P_VS_SAL_CJT", "0.00");
            }
            if (!JadeStringUtil.isBlankOrZero(entity.getVsAutresRevenusCtb())) {
                super.setParametres("P_VS_AUTRE_REV", entity.getVsAutresRevenusCtb());
            } else {
                super.setParametres("P_VS_AUTRE_REV", "0.00");
            }
            if (!JadeStringUtil.isBlankOrZero(entity.getVsAutresRevenusConjoint())) {
                super.setParametres("P_VS_AUTRE_REV_CJT", entity.getVsAutresRevenusConjoint());
            } else {
                super.setParametres("P_VS_AUTRE_REV_CJT", "0.00");
            }
            super.setParametres("P_DATE_IMP", entity.getDateRetour());
            // Message si différent de spontanée
            int nbLogs = 2;
            String logs = "";
            CPLienCommunicationsPlausiManager manager = new CPLienCommunicationsPlausiManager();
            manager.setSession(getSession());
            manager.setForIdCommunication(entity.getIdRetour());
            manager.find();
            for (int i = 0; i < manager.size(); i++) {
                if (nbLogs-- > 0) {
                    CPLienCommunicationsPlausi lien = (CPLienCommunicationsPlausi) manager.get(i);
                    // On va rechercher le message
                    // si différent de spontanéé
                    if (!lien.getIdPlausibilite().equalsIgnoreCase("1")) {
                        CPParametrePlausibilite plausi = new CPParametrePlausibilite();
                        plausi.setSession(getSession());
                        plausi.setIdParametre(lien.getIdPlausibilite());
                        try {
                            plausi.retrieve();
                        } catch (Exception e) {
                        }
                        if (plausi.getDescription_fr().length() > 0) {
                            if (getSession().getIdLangueISO().equalsIgnoreCase("DE")) {
                                logs = logs + plausi.getId() + " - " + plausi.getDescription_de();
                            } else if (getSession().getIdLangueISO().equalsIgnoreCase("IT")) {
                                logs = logs + plausi.getId() + " - " + plausi.getDescription_it();
                            } else {
                                logs = logs + plausi.getId() + " - " + plausi.getDescription_fr();
                            }
                        }
                        logs = logs + "\n";
                    } else {
                        nbLogs++;
                    }
                }
            }
            super.setParametres("P_LOGS", logs);
        } catch (Exception e) {
            super.setParametres(CPIListeCommunicationRetourDetailFiscVsParam.PARAM_TIERS_NOMPRENOM, "Tiers(Erreur): ");
        }
    }

    /**
     * Récupère les informations du décompte avant impression.
     */
    @Override
    public void beforeExecuteReport() {
        // Variable pour le comptage
        try {
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_TITRE_IMPOT,
                    getSession().getLabel("VS_TITRE_IMPOT"));
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
            setDocumentInfo();
            super.setTemplateFile("PHENIX_VALIDATION_RETOUR_FISC_DETAIL_VS");
            if ("ORDER_BY_CONTRIBUABLE".equals(manager.getTri())) {
                setDocumentTitle(entity.getNumContribuable() + " - " + entity.getAnnee1());
            } else if ("ORDER_BY_AFFILIE".equals(manager.getTri())) {
                setDocumentTitle(entity.getTri() + " - " + entity.getAnnee1());
            } else if ("ORDER_BY_IFD".equals(manager.getTri())) {
                setDocumentTitle(entity.getAnnee1() + " - " + entity.getNumContribuable());
            } else if ("ORDER_BY_AVS".equals(manager.getTri())) {
                setDocumentTitle(entity.getNumeroAVS(0) + " - " + entity.getAnnee1());
            } else { // Défaut
                setDocumentTitle(entity.getNumContribuable() + " - " + entity.getAnnee1());
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return;
        }
        return;
    }

    public CPCommunicationFiscaleRetourVSViewBean getEntity() {
        return entity;
    }

    public CPCommunicationFiscaleRetourVSManager getManager() {
        return manager;
    }

    @Override
    public boolean next() throws FWIException {
        try {
            if (((entity = (CPCommunicationFiscaleRetourVSViewBean) manager.cursorReadNext(statement)) != null)
                    && (!entity.isNew()) && !super.isAborted()) {
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
        String langueIso = "";
        TITiersViewBean tierslu = entity.getTiers();
        if (tierslu != null) {
            langueIso = tierslu.getLangueIso();
        }
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_LANGUE_ISO, langueIso);
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NOM, entity.getNom());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_PRENOM, entity.getPrenom());
        /* Personne Avs */
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_DATE_NAISSANCE, entity.getDateNaissance());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_FORMATTE, entity.getNumAvs(0));
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE,
                JadeStringUtil.removeChar(entity.getNumAvs(0), '.'));
    }

    public void setEntity(CPCommunicationFiscaleRetourVSViewBean entity) {
        this.entity = entity;
    }

    public void setManager(CPCommunicationFiscaleRetourVSManager manager) {
        this.manager = manager;
    }
}
