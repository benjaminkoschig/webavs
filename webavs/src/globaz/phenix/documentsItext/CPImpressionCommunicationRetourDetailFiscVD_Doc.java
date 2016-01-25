package globaz.phenix.documentsItext;

import globaz.docinfo.CADocumentInfoHelper;
import globaz.docinfo.CTDocumentInfoHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.ged.target.JadeGedTargetProperties;
import globaz.jade.log.JadeLogger;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourVDManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourVDViewBean;
import globaz.phenix.listes.itext.CPIListeCommunicationRetourDetailFiscGeParam;
import globaz.phenix.listes.itext.CPIListeCommunicationRetourDetailFiscJuParam;
import globaz.phenix.listes.itext.CPIListeCommunicationRetourDetailFiscVdParam;
import globaz.phenix.listes.itext.CPIListeCommunicationRetourParam;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.util.DocumentInfoPhenix;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.Hashtable;

/**
 * Insérez la description du type ici. Date de création : (26.02.2003 16:54:19)
 * 
 * @author: Administrator
 */
public class CPImpressionCommunicationRetourDetailFiscVD_Doc extends CPImpressionCommunicationRetourDetailFisc_Doc {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private CPCommunicationFiscaleRetourVDViewBean entity = null;

    private CPCommunicationFiscaleRetourVDManager manager = null;

    private long progressCounter = -1;

    private BStatement statement = null;

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:56:39)
     */
    public CPImpressionCommunicationRetourDetailFiscVD_Doc() throws Exception {
        super();
    }

    public CPImpressionCommunicationRetourDetailFiscVD_Doc(BProcess parent) throws FWIException {
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
    public CPImpressionCommunicationRetourDetailFiscVD_Doc(BSession session) throws FWIException {
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
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_DATE_RECEPTION,
                    entity.getDateRetour());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_ANNEE, entity.getAnnee1());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_REV_SUI_PSAAVS,
                    JANumberFormatter.fmt(entity.getVdImpositionDepense(), true, false, true, 2));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_DEP_PSAAVS,
                    entity.getDepensesTrainVie());

            String adresse = "";
            try {
                if (entity.getAdresse() != null) {
                    Hashtable<String, String> data = entity.getAdresse().getData();
                    // String npa =
                    // (String)data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA);
                    /*
                     * String npasup = (String)data.get(TIAbstractAdresseDataSource .ADRESSE_VAR_NPA_SUP); if
                     * (Integer.parseInt(npasup)<10){ npa = npa + "0" + npasup; } else { npa = npa + npasup; }
                     */
                    // adresse += npa;
                    // adresse += npa +
                    // (String)data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
                    adresse += data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE) + " "
                            + data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO);
                    adresse += "\n" + data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA) + " "
                            + data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
                }
            } catch (Exception e) {
                adresse = "";
            }
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_TIERS_ADRESSE, adresse);
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_TIERS_DATE_NAISSANCE,
                    entity.getDateNaissance());
            if (entity.getTiers() != null) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_TIERS_NOM_PRENOM, entity
                        .getTiers().getNomPrenom());
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_TIERS_NOM_PRENOM,
                        entity.getVdNomPrenom());
            }
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_TIERS_NSS, entity.getNumAvs());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_NUM_CAISSE, entity.getVdNumCaisse());
            if (!JadeStringUtil.isEmpty(entity.getVdNumAffilie())) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_TITRE,
                        getSession().getLabel("DETAIL_FISC_TITRE") + " " + entity.getVdNumAffilie());
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_TITRE,
                        getSession().getLabel("DETAIL_FISC_TITRE_SANS_NUM") + " " + entity.getNumAffilie());
            }
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_NOMPRENOM, entity.getVdNomPrenom());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_NSS, entity.getNumAvs());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_ADRESSE, entity.getVdAddChez()
                    + entity.getVdAddRue() + "\n" + entity.getVdNumLocalite());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_IFD, entity.getNumIfd());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_NUMCAISSE, entity.getVdNumCaisse());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_AFFILIE, entity.getVdNumAffilie());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_NSS_RECU, entity.getVdNumAvs());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_NUM_CONTRIVD,
                    entity.getVdNumContribuable());
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_CAPITAL, entity.getCapital());
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_FORTUNE, entity.getFortune());
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_REVENU1, entity.getRevenu1());
            // super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_REVENU2,
            // entity.getRevenu2());

            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_DEB_PERIODE,
                    entity.getVdDebPeriode());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_FIN_PERIODE,
                    entity.getVdFinPeriode());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_DAT_NAI, entity.getVdDatNaissance());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_ASS_CON, entity.getVdAssujCtb());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_PEN_ALIM,
                    entity.getVdPenAliObtenue());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_DRO_HAB,
                    entity.getVdDroitHabitation());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_TYP_TAX, entity.getVdTypeTax());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_DEB_ASS, entity.getVdDebAssuj());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_FIN_ASS, entity.getVdFinAssuj());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_CAP_INVESTI,
                    entity.getVdCapInvesti());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_DAT_DETCAPINVESTI,
                    entity.getVdDatDetCapInvesti());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_REV_ACT_IND, entity.getVdRevActInd());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_GI_PROF, entity.getVdGIprof());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_EXCES_LIQUID,
                    entity.getVdExcesLiquidite());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_SAL_VERSE_CJT,
                    entity.getVdSalVerseCjt());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_NAT_COM, entity.getVdNatureComm());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_SAL_COTISANT,
                    entity.getVdSalaireCotisant());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_FOR_IMPO,
                    entity.getVdFortuneImposable());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_DAT_DET_FOR,
                    entity.getVdDateDetFortune());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_MON_RENTE,
                    entity.getVdMontantRente());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_MON_ACT_LUC,
                    entity.getVdMontantActLuc());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_COD_NATRENTE,
                    entity.getVdCodeNatureRente());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_REV_NET, entity.getVdRevenuNet());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_REV_ACT_LUC,
                    entity.getVdRevenuActivitesLucratives());

            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_GEN_AFFTRANSMIS,
                    entity.getVdGenreAffilie());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_DAT_RECEPT, entity.getDateRetour());
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.PARAM_COMMENTAIRES,
                    entity.getVdCommentaire());
            if (!getTransaction().hasErrors()) {
                getTransaction().commit();
            } else {
                getTransaction().rollback();
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
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_DATE_RECEPTION, getSession()
                    .getLabel("DETAIL_FISC_VD_DATE_RECEPTION"));
            // super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_DATE_ENVOI,
            // getSession().getLabel("DETAIL_FISC_VD_DATE_ENVOI"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_DONNEES_INTERNES, getSession()
                    .getLabel("DETAIL_FISC_VD_DONNEES_INTERNES"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_REV_SUI_PSAAVS, getSession()
                    .getLabel("DETAIL_FISC_VD_REV_SUI"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_DEP_PSAAVS,
                    getSession().getLabel("DETAIL_FISC_VD_DEPENSES_TRAIN_VIE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_CAISSE_AVS,
                    getSession().getLabel("DETAIL_FISC_VD_CAISSE_AVS"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_DONNEES_TIERS, getSession()
                    .getLabel("DETAIL_FISC_VD_DONNEES_TIERS"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_DESCRIPTION,
                    getSession().getLabel("DETAIL_FISC_VD_TIERS_DESCRIPTION"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_TIERS,
                    getSession().getLabel("DETAIL_FISC_VD_TIERS"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_IFD,
                    getSession().getLabel("DETAIL_FISC_VD_IFD"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_NUMCAISSE,
                    getSession().getLabel("DETAIL_FISC_VD_NUMCAISSE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_AFFILIE,
                    getSession().getLabel("DETAIL_FISC_AFFILIE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_NSS,
                    getSession().getLabel("DETAIL_FISC_VD_NSS"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_NSS_RECU,
                    getSession().getLabel("DETAIL_FISC_VD_NSSRECU"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_NUM_CONTRIVD,
                    getSession().getLabel("NUM_CONTRIBUABLE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_NUM_DEMANDE,
                    getSession().getLabel("DETAIL_FISC_GE_NUM_DEMANDE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.LABEL_CAPITAL,
                    getSession().getLabel("DETAIL_FISC_JU_CAPITAL"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.LABEL_FORTUNE,
                    getSession().getLabel("DETAIL_FISC_JU_FORTUNE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.LABEL_REVENU1,
                    getSession().getLabel("REVENU_ANNUEL"));
            // super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.LABEL_REVENU2,
            // getSession().getLabel("DETAIL_FISC_JU_REVENU2"));

            super.setParametres(CPIListeCommunicationRetourParam.LABEL_INFO_GENERAL,
                    getSession().getLabel("DETAIL_FISC_INFO_GENERAL_VD"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_DEB_PERIODE,
                    getSession().getLabel("DETAIL_FISC_VD_DEB_PERIODE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_FIN_PERIODE,
                    getSession().getLabel("DETAIL_FISC_VD_FIN_PERIODE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_DAT_NAI,
                    getSession().getLabel("DETAIL_FISC_VD_DAT_NAI"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_ASS_CON,
                    getSession().getLabel("DETAIL_FISC_VD_ASS_CON"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_PEN_ALIM,
                    getSession().getLabel("DETAIL_FISC_VD_PEN_ALIM"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_DRO_HAB,
                    getSession().getLabel("DETAIL_FISC_VD_DRO_HAB"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_TYP_TAX,
                    getSession().getLabel("DETAIL_FISC_VD_TYP_TAX"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_DEB_ASS,
                    getSession().getLabel("DETAIL_FISC_VD_DEB_ASS"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_FIN_ASS,
                    getSession().getLabel("DETAIL_FISC_VD_FIN_ASS"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_CAP_INVESTI,
                    getSession().getLabel("DETAIL_FISC_VD_CAP_INVESTI"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_DAT_DETCAPINVESTI, getSession()
                    .getLabel("DETAIL_FISC_VD_DAT_DETCAPINVESTI"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_REV_ACT_IND,
                    getSession().getLabel("DETAIL_FISC_VD_REV_ACT_IND"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_GI_PROF,
                    getSession().getLabel("DETAIL_FISC_VD_GI_PROF"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_EXCES_LIQUID,
                    getSession().getLabel("DETAIL_FISC_VD_EXCES_LIQUID"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_SAL_VERSE_CJT, getSession()
                    .getLabel("DETAIL_FISC_VD_SAL_VERSE_CJT"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_NAT_COM,
                    getSession().getLabel("DETAIL_FISC_VD_NAT_COM"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_SAL_COTISANT,
                    getSession().getLabel("DETAIL_FISC_VD_SAL_COTISANT"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_FOR_IMPO,
                    getSession().getLabel("DETAIL_FISC_VD_FOR_IMPO"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_DAT_DET_FOR,
                    getSession().getLabel("DETAIL_FISC_VD_DAT_DET_FOR"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_MON_RENTE,
                    getSession().getLabel("DETAIL_FISC_VD_MON_RENTE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_MON_ACT_LUC,
                    getSession().getLabel("DETAIL_FISC_VD_MON_ACT_LUC"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_COD_NATRENTE,
                    getSession().getLabel("DETAIL_FISC_VD_COD_NATRENTE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_REV_NET,
                    getSession().getLabel("DETAIL_FISC_VD_REV_NET"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_REV_ACT_LUC,
                    getSession().getLabel("DETAIL_FISC_VD_REV_ACT_LUC"));

            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_INFO_SUPP,
                    getSession().getLabel("DETAIL_FISC_VD_INFO_SUPP"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_GEN_AFFTRANSMIS, getSession()
                    .getLabel("DETAIL_FISC_VD_GEN_AFFTRANSMIS"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_DAT_RECEPT,
                    getSession().getLabel("DETAIL_FISC_VD_DAT_RECPT"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscVdParam.LABEL_COMMENTAIRES,
                    getSession().getLabel("DETAIL_FISC_VD_COMMENTAIRES"));

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
            getDocumentInfo();
            super.setTemplateFile("PHENIX_VALIDATION_RETOUR_FISC_DETAIL_VD");
            if ("ORDER_BY_CONTRIBUABLE".equals(manager.getTri())) {
                setDocumentTitle(entity.getNumContribuable() + " - " + entity.getAnnee1());
            } else if ("ORDER_BY_AFFILIE".equals(manager.getTri())) {
                setDocumentTitle(entity.getNumAffilie() + " - " + entity.getAnnee1());
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

    public CPCommunicationFiscaleRetourVDViewBean getEntity() {
        return entity;
    }

    public CPCommunicationFiscaleRetourVDManager getManager() {
        return manager;
    }

    @Override
    public boolean next() throws FWIException {
        try {
            if (((entity = (CPCommunicationFiscaleRetourVDViewBean) manager.cursorReadNext(statement)) != null)
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

    public void setEntity(CPCommunicationFiscaleRetourVDViewBean entity) {
        this.entity = entity;
    }

    public void setManager(CPCommunicationFiscaleRetourVDManager manager) {
        this.manager = manager;
    }
}
