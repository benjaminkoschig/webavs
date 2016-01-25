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
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourJUManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourJUViewBean;
import globaz.phenix.listes.itext.CPIListeCommunicationRetourDetailFiscJuParam;
import globaz.phenix.listes.itext.CPIListeCommunicationRetourParam;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.util.DocumentInfoPhenix;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * Insérez la description du type ici. Date de création : (26.02.2003 16:54:19)
 * 
 * @author: Administrator
 */
public class CPImpressionCommunicationRetourDetailFiscJU_Doc extends CPImpressionCommunicationRetourDetailFisc_Doc {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private CPCommunicationFiscaleRetourJUViewBean entity = null;

    private CPCommunicationFiscaleRetourJUManager manager = null;

    private long progressCounter = -1;

    private BStatement statement = null;

    // initialisation pour CS langue tiers (TITIERS)
    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:56:39)
     */
    public CPImpressionCommunicationRetourDetailFiscJU_Doc() throws Exception {
        super();
    }

    public CPImpressionCommunicationRetourDetailFiscJU_Doc(BProcess parent) throws FWIException {
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
    public CPImpressionCommunicationRetourDetailFiscJU_Doc(BSession session) throws FWIException {
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
                super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_TITRE,
                        getSession().getLabel("DETAIL_FISC_TITRE") + " " + entity.getNumAffilie());
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_TITRE,
                        getSession().getLabel("DETAIL_FISC_TITRE_SANS_NUM") + " " + entity.getNumAffilie());
            }
            if (!JadeStringUtil.isBlankOrZero(entity.getIdTiers())) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_TIERS_NOMPRENOM, entity
                        .getTiers().getNomPrenom());
            }
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_TIERS_ADRESSE, entity.getLocalite());
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_IFD, entity.getNumIfd());
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_LOT, entity.getJuLot());
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_NBRE_JOUR, entity.getJuNbrJour1());
            if (!JadeStringUtil.isEmpty(entity.getRevenu1())) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_REVENU1, entity.getRevenu1());
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_REVENU1, "0.0");
            }
            if (!JadeStringUtil.isEmpty(entity.getCapital())) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_CAPITAL, entity.getCapital());
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_CAPITAL, "0.0");
            }
            if (!JadeStringUtil.isEmpty(entity.getFortune())) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_FORTUNE, entity.getFortune());
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_FORTUNE, "0.0");
            }
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_AFFILIE, entity.getNumAffilie());
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_NSS, entity.getNumAvs(0));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_NUM_CONTRI,
                    entity.getNumContribuable());
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_NUM_CONTRI_RECU,
                    entity.getJuNumContribuable());
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_GENRE_AFF, getSession()
                    .getCodeLibelle(entity.getGenreAffilie()));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_NBRE_JOUR2, entity.getJuNbrJour2());
            if (!JadeStringUtil.isEmpty(entity.getRevenu2())) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_REVENU2, entity.getRevenu2());
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_REVENU2, "0.0");
            }
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_GENRE_AFF_TRANSMIS,
                    entity.getJuGenreAffilie());
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_DATE_NAI,
                    entity.getJuDateNaissance());
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_NEW_CONTRI,
                    entity.getJuNewNumContribuable());
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_GENRE_TAX,
                    entity.getJuGenreTaxation());
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_DATE_RECEPT, entity.getDateRetour());
            if (entity.getJuTaxeMan().booleanValue()) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_TAX_MANQUANTE, getSession()
                        .getLabel("DETAIL_FISC_JU_TAXATION_VALEUR_OUI"));
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_TAX_MANQUANTE, getSession()
                        .getLabel("DETAIL_FISC_JU_TAXATION_VALEUR_NON"));
            }

            if (!getTransaction().hasErrors()) {
                getTransaction().commit();
            } else {
                getTransaction().rollback();
            }
        } catch (Exception e) {
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.PARAM_TIERS_NOMPRENOM, "Tiers(Erreur): ");
        }
    }

    /**
     * Récupère les informations du décompte avant impression.
     */
    @Override
    public void beforeExecuteReport() {
        // Variable pour le comptage
        try {
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_INFO_GENERAL,
                    getSession().getLabel("DETAIL_FISC_INFO_GENERAL"));
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_INFO_SUPP,
                    getSession().getLabel("DETAIL_FISC_INFO_SUPP"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.LABEL_TIERS,
                    getSession().getLabel("DETAIL_FISC_JU_TIERS"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.LABEL_IFD,
                    getSession().getLabel("DETAIL_FISC_JU_IFD"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.LABEL_LOT,
                    getSession().getLabel("DETAIL_FISC_JU_LOT"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.LABEL_NBRE_JOUR,
                    getSession().getLabel("DETAIL_FISC_JU_NBRE_JOUR"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.LABEL_REVENU1,
                    getSession().getLabel("DETAIL_FISC_JU_REVENU1"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.LABEL_CAPITAL,
                    getSession().getLabel("CAPITAL"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.LABEL_FORTUNE,
                    getSession().getLabel("DETAIL_FISC_JU_FORTUNE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.LABEL_AFFILIE,
                    getSession().getLabel("DETAIL_FISC_AFFILIE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.LABEL_NSS,
                    getSession().getLabel("DETAIL_FISC_JU_NSS"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.LABEL_NUM_CONTRI,
                    getSession().getLabel("DETAIL_FISC_JU_NUM_CONTRI"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.LABEL_NUM_CONTRI_RECU, getSession()
                    .getLabel("DETAIL_FISC_JU_NUM_CONTRI_RECU"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.LABEL_GENRE_AFF,
                    getSession().getLabel("DETAIL_FISC_JU_GENRE_AFF"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.LABEL_NBRE_JOUR2,
                    getSession().getLabel("DETAIL_FISC_JU_NBRE_JOUR2"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.LABEL_REVENU2,
                    getSession().getLabel("DETAIL_FISC_JU_REVENU2"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.LABEL_GENRE_AFF_TRANSMIS, getSession()
                    .getLabel("DETAIL_FISC_JU_GENRE_AFF_TRANSMIS"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.LABEL_DATE_NAI,
                    getSession().getLabel("DETAIL_FISC_JU_DATE_NAI"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.LABEL_NEW_CONTRI,
                    getSession().getLabel("DETAIL_FISC_JU_NEW_CONTRI"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.LABEL_GENRE_TAX,
                    getSession().getLabel("DETAIL_FISC_JU_GENRE_TAX"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.LABEL_DATE_RECEPT,
                    getSession().getLabel("DETAIL_FISC_JU_DATE_RECEPT"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscJuParam.LABEL_TAX_MANQUANTE, getSession()
                    .getLabel("DETAIL_FISC_JU_TAX_MANQUANTE"));

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
            super.setTemplateFile("PHENIX_VALIDATION_RETOUR_FISC_DETAIL_JU");
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

    public CPCommunicationFiscaleRetourJUViewBean getEntity() {
        return entity;
    }

    public CPCommunicationFiscaleRetourJUManager getManager() {
        return manager;
    }

    @Override
    public boolean next() throws FWIException {
        try {
            if (((entity = (CPCommunicationFiscaleRetourJUViewBean) manager.cursorReadNext(statement)) != null)
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

    public void setEntity(CPCommunicationFiscaleRetourJUViewBean entity) {
        this.entity = entity;
    }

    public void setManager(CPCommunicationFiscaleRetourJUManager manager) {
        this.manager = manager;
    }
}
