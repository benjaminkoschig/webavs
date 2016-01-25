package globaz.phenix.documentsItext;

import globaz.docinfo.CADocumentInfoHelper;
import globaz.docinfo.CTDocumentInfoHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.ged.target.JadeGedTargetProperties;
import globaz.jade.log.JadeLogger;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourNEManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourNEViewBean;
import globaz.phenix.listes.itext.CPIListeCommunicationRetourDetailFiscNeParam;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.util.DocumentInfoPhenix;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * Insérez la description du type ici. Date de création : (26.02.2003 16:54:19)
 * 
 * @author: Administrator
 */
public class CPImpressionCommunicationRetourDetailFiscNE_Doc extends CPImpressionCommunicationRetourDetailFisc_Doc {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private CPCommunicationFiscaleRetourNEViewBean entity = null;

    private CPCommunicationFiscaleRetourNEManager manager = null;

    private long progressCounter = -1;

    private BStatement statement = null;

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:56:39)
     */
    public CPImpressionCommunicationRetourDetailFiscNE_Doc() throws Exception {
        super();
    }

    public CPImpressionCommunicationRetourDetailFiscNE_Doc(BProcess parent) throws FWIException {
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
    public CPImpressionCommunicationRetourDetailFiscNE_Doc(BSession session) throws FWIException {
        super(session);
    }

    @Override
    public void afterBuildReport() {
        try {
            setDocumentInfo();
        } catch (FWIException e) {
        }
        super.afterBuildReport();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        try {
            if (!JadeStringUtil.isEmpty(entity.getNumAffilie())) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_TITRE,
                        getSession().getLabel("DETAIL_FISC_TITRE") + " " + entity.getNumAffilie());
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_TITRE,
                        getSession().getLabel("DETAIL_FISC_TITRE_SANS_NUM") + " " + entity.getNumAffilie());
            }
            if (!JadeStringUtil.isBlankOrZero(entity.getIdTiers())) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_TIERS_NOMPRENOM, entity
                        .getTiers().getNomPrenom());
            }
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_TIERS_ADRESSE, entity.getLocalite());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_TIERS_IFD, entity.getIdIfd());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_TIERS_NUMCAISSE,
                    entity.getNeNumCaisse());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_TIERS_NUMCOMMUNE,
                    entity.getNeNumCommune());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_TIERS_NUMCLIENT,
                    entity.getNeNumClient());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_TIERS_NUMBDP, entity.getNeNumBDP());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_TIERS_AFFILIE,
                    entity.getNumAffilie());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_TIERS_NSS, entity.getNumeroAVS());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_TIERS_NUMCONTRI,
                    entity.getNumContribuable());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_TIERS_GENRE,
                    entity.getGenreAffilie());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_TIERS_NUMAVS, entity.getNeNumAvs());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_TIERS_NUMCONTRIRECU,
                    entity.getNeNumContribuable());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_IND_ANNEE1, entity.getAnnee1());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_IND_REVENU1, entity.getRevenu1());
            if (!JadeStringUtil.isEmpty(entity.getCapital())) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_IND_CAPITAL, entity.getCapital());
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_IND_CAPITAL, "0");
            }
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_IND_DEBEX1,
                    entity.getDebutExercice1());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_IND_FINEX1, entity.getFinExercice1());
            if (!JadeStringUtil.isIntegerEmpty(entity.getAnnee2())) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_IND_ANNEE2, entity.getAnnee2());
            }
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_IND_REVENU2, entity.getRevenu2());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_IND_DEBEX2,
                    entity.getDebutExercice2());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_IND_FINEX2, entity.getFinExercice2());

            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_NA_FORTUNE1,
                    entity.getNeFortuneAnnee1());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_NA_PENSION1,
                    entity.getNePensionAnnee1());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_NA_PENSION_ALIM1,
                    entity.getNePensionAlimentaire1());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_NA_RENTE_VIA1,
                    entity.getNeRenteViagere1());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_NA_INDEM_JOUR1,
                    entity.getNeIndemniteJour1());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_NA_RENTE_TOT1,
                    entity.getNeRenteTotale1());

            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_NA_PENSION2, entity.getNePension());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_NA_PENSION_ALIM2,
                    entity.getNePensionAlimentaire());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_NA_RENTE_VIA2,
                    entity.getNeRenteViagere());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_NA_INDEM_JOUR2,
                    entity.getNeIndemniteJour());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_NA_RENTE_TOT2,
                    entity.getNeRenteTotale());

            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_GENRE_TAX,
                    entity.getNeGenreTaxation());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_DOSSIER_TAXE, getSession()
                    .getCodeLibelle(entity.getNeDossierTaxe()));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_DOSSIER_TROUVE,
                    entity.getNeDossierTrouve());
            if (entity.getNeTaxationRectificative().booleanValue()) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_TAXATION_RECT, getSession()
                        .getLabel("DETAIL_FISC_NE_TAXATION_VALEUR_OUI"));
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_TAXATION_RECT, getSession()
                        .getLabel("DETAIL_FISC_NE_TAXATION_VALEUR_NON"));
            }
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_ASSUJETISSEMENT,
                    entity.getNeDateDebutAssuj());
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_DATE_RETOUR, entity.getDateRetour());
            // super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_COMMENTAIRES,
            // "");

            /*
             * CPApplication phenixApplication = (CPApplication) getSession().getApplication(); ICaisseReportHelper
             * caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper
             * (getSession().getApplication(), phenixApplication.getLangue2ISO(phenixApplication
             * .getLangueCantonISO(canton))); CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
             * headerBean.setNoAffilie(null); headerBean.setNoAvs(" "); headerBean.setAdresse(" ");
             * headerBean.setDate(" "); headerBean.setRecommandee(false); headerBean.setConfidentiel(false);
             * caisseReportHelper.addSignatureParameters(this, "");
             */

            if (!getTransaction().hasErrors()) {
                getTransaction().commit();
            } else {
                getTransaction().rollback();
            }
            // caisseReportHelper.addHeaderParameters(this, headerBean);
        } catch (Exception e) {
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.PARAM_TIERS_NOMPRENOM, "Tiers(Erreur): ");
            // super.setParametres(CPIListeCommunicationRetourParam.LABEL_CONTRIBUABLE,
            // "Contribuable");
        }
    }

    /**
     * Récupère les informations du décompte avant impression.
     */
    @Override
    public void beforeExecuteReport() {
        // Variable pour le comptage
        try {
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_TIERS,
                    getSession().getLabel("DETAIL_FISC_NE_TIERS"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_TIERS_DESCRIPTION, getSession()
                    .getLabel("DETAIL_FISC_NE_TIERS_DESCRIPTION"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_TIERS_IFD,
                    getSession().getLabel("DETAIL_FISC_NE_TIERS_IFD"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_TIERS_NUMCAISSE, getSession()
                    .getLabel("DETAIL_FISC_NE_TIERS_NUMCAISSE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_TIERS_NUMCOMMUNE, getSession()
                    .getLabel("DETAIL_FISC_NE_TIERS_NUMCOMMUNE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_TIERS_NUMCLIENT, getSession()
                    .getLabel("DETAIL_FISC_NE_TIERS_NUMCLIENT"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_TIERS_NUMBDP,
                    getSession().getLabel("DETAIL_FISC_NE_TIERS_NUMBDP"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_TIERS_AFFILIE, getSession()
                    .getLabel("DETAIL_FISC_NE_TIERS_AFFILIE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_TIERS_NSS,
                    getSession().getLabel("DETAIL_FISC_NE_TIERS_NSS"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_TIERS_NUMCONTRI, getSession()
                    .getLabel("DETAIL_FISC_NE_TIERS_NUMCONTRI"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_TIERS_GENRE,
                    getSession().getLabel("DETAIL_FISC_NE_TIERS_GENRE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_TIERS_NUMAVS,
                    getSession().getLabel("DETAIL_FISC_NE_TIERS_NUMAVS"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_TIERS_NUMCONTRIRECU, getSession()
                    .getLabel("DETAIL_FISC_NE_TIERS_NUMCONTRIRECU"));

            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_INDEPENDANT,
                    getSession().getLabel("DETAIL_FISC_NE_INDEPENDANT"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_AU,
                    getSession().getLabel("DETAIL_FISC_NE_AU"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_IND_ANNEE1,
                    getSession().getLabel("DETAIL_FISC_NE_IND_ANNEE1"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_IND_REVENU1,
                    getSession().getLabel("DETAIL_FISC_NE_IND_REVENU1"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_IND_CAPITAL,
                    getSession().getLabel("CAPITAL"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_IND_EX1,
                    getSession().getLabel("DETAIL_FISC_NE_IND_EX1"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_IND_ANNEE2,
                    getSession().getLabel("DETAIL_FISC_NE_IND_ANNEE2"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_IND_REVENU2,
                    getSession().getLabel("DETAIL_FISC_NE_IND_REVENU2"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_IND_EX2,
                    getSession().getLabel("DETAIL_FISC_NE_IND_EX2"));

            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_NA,
                    getSession().getLabel("DETAIL_FISC_NE_NA"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_NA_FORTUNE,
                    getSession().getLabel("DETAIL_FISC_NE_NA_FORTUNE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_NA_PENSION,
                    getSession().getLabel("DETAIL_FISC_NE_NA_PENSION"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_NA_PENSION_ALIM, getSession()
                    .getLabel("DETAIL_FISC_NE_NA_PENSION_ALIM"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_NA_RENTE_VIA,
                    getSession().getLabel("DETAIL_FISC_NE_NA_RENTE_VIA"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_NA_INDEM_JOUR, getSession()
                    .getLabel("DETAIL_FISC_NE_NA_INDEM_JOUR"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_NA_RENTE_TOT,
                    getSession().getLabel("DETAIL_FISC_NE_NA_RENTE_TOT"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_ANNEE1,
                    getSession().getLabel("DETAIL_FISC_NE_NA_ANNEE1"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_ANNEE2,
                    getSession().getLabel("DETAIL_FISC_NE_NA_ANNEE2"));

            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_INFO_SUPP,
                    getSession().getLabel("DETAIL_FISC_NE_INFO_SUPP"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_GENRE_TAX,
                    getSession().getLabel("DETAIL_FISC_NE_GENRE_TAX"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_DOSSIER_TAXE,
                    getSession().getLabel("DETAIL_FISC_NE_DOSSIER_TAXE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_DOSSIER_TROUVE, getSession()
                    .getLabel("DETAIL_FISC_NE_DOSSIER_TROUVE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_TAXATION_RECT, getSession()
                    .getLabel("DETAIL_FISC_NE_TAXATION_RECT"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_ASSUJETISSEMENT, getSession()
                    .getLabel("DETAIL_FISC_NE_ASSUJETISSEMENT"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_DATE_RETOUR,
                    getSession().getLabel("DETAIL_FISC_NE_DATE_RETOUR"));
            // super.setParametres(CPIListeCommunicationRetourDetailFiscNeParam.LABEL_COMMENTAIRES,
            // getSession().getLabel("DETAIL_FISC_NE_COMMENTAIRES"));

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
            // CPApplication phenixApplication =
            // (globaz.phenix.application.CPApplication)
            // getSession().getApplication();
            super.setTemplateFile("PHENIX_VALIDATION_RETOUR_FISC_DETAIL_NE");
            if ("ORDER_BY_CONTRIBUABLE".equals(manager.getTri())) {
                setDocumentTitle(entity.getNumContribuable() + " - " + entity.getAnnee1());
            } else if ("ORDER_BY_AFFILIE".equals(manager.getTri())) {
                setDocumentTitle(entity.getNumAffilie() + " - " + entity.getAnnee1());
            } else if ("ORDER_BY_IFD".equals(manager.getTri())) {
                setDocumentTitle(entity.getAnnee1() + " - " + entity.getNumContribuable());
            } else if ("ORDER_BY_AVS".equals(manager.getTri())) {
                setDocumentTitle(entity.getNumeroAVS() + " - " + entity.getAnnee1());
            } else { // Défaut
                setDocumentTitle(entity.getNumContribuable() + " - " + entity.getAnnee1());
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return;
        }
        return;
    }

    public CPCommunicationFiscaleRetourNEViewBean getEntity() {
        return entity;
    }

    public CPCommunicationFiscaleRetourNEManager getManager() {
        return manager;
    }

    @Override
    public boolean next() throws FWIException {
        try {
            if (((entity = (CPCommunicationFiscaleRetourNEViewBean) manager.cursorReadNext(statement)) != null)
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
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_FORMATTE, entity.getNumAvs());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE,
                JadeStringUtil.removeChar(entity.getNumAvs(), '.'));
    }

    public void setEntity(CPCommunicationFiscaleRetourNEViewBean entity) {
        this.entity = entity;
    }

    public void setManager(CPCommunicationFiscaleRetourNEManager manager) {
        this.manager = manager;
    }
}
