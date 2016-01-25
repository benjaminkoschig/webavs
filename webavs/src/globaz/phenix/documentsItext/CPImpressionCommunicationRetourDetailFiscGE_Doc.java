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
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourGEManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourGEViewBean;
import globaz.phenix.listes.itext.CPIListeCommunicationRetourDetailFiscGeParam;
import globaz.phenix.listes.itext.CPIListeCommunicationRetourDetailFiscNeParam;
import globaz.phenix.listes.itext.CPIListeCommunicationRetourParam;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.util.DocumentInfoPhenix;
import globaz.pyxis.db.tiers.TITiersViewBean;

/**
 * Insérez la description du type ici. Date de création : (26.02.2003 16:54:19)
 * 
 * @author: Administrator
 */
public class CPImpressionCommunicationRetourDetailFiscGE_Doc extends CPImpressionCommunicationRetourDetailFisc_Doc {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private CPCommunicationFiscaleRetourGEViewBean entity = null;

    private CPCommunicationFiscaleRetourGEManager manager = null;

    private long progressCounter = -1;

    private BStatement statement = null;

    // initialisation pour CS langue tiers (TITIERS)
    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:56:39)
     */
    public CPImpressionCommunicationRetourDetailFiscGE_Doc() throws Exception {
        super();
    }

    public CPImpressionCommunicationRetourDetailFiscGE_Doc(BProcess parent) throws FWIException {
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
    public CPImpressionCommunicationRetourDetailFiscGE_Doc(BSession session) throws FWIException {
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
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_TIERS_NOMPRENOM, entity
                        .getTiers().getNomPrenom());
            }
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_TIERS_ADRESSE, entity.getLocalite());
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_IFD, entity.getNumIfd());
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_NUMCONTRI_RECU,
                    entity.getGeNumContribuable());
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_NUMAFF_RECU,
                    entity.getGeNumAffilie());
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_NUMAVS_CON,
                    entity.getGeNumAvsConjoint());
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_GENRE_AFF,
                    entity.getGeGenreAffilie());
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_NUM_CAISSE, entity.getGeNumCaisse());
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_NUM_DEMANDE,
                    entity.getGeNumDemande());
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_NUM_COMM,
                    entity.getGeNumCommunication());
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_AFFILIE, entity.getNumAffilie());
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_NSS, entity.getGeNumAvs());
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_NUM_CONTRI,
                    entity.getGeNumContribuable());
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_NOM, entity.getGeNom());
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_PRENOM, entity.getGePrenom());
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_NOM_CON, entity.getGeNomConjoint());
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_PRENOM_CON,
                    entity.getGePrenomConjoint());
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_NOM_AFC, entity.getGeNomAFC());
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_PRENOM_AFC, entity.getGePrenomAFC());
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_GENRE_TAX, entity.getGenreTaxation());
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_ANNEE, entity.getAnnee1());
            if (!JadeStringUtil.isEmpty(entity.getRevenu1())) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_REVENU, entity.getRevenu1());
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_REVENU, "0.0");
            }
            if (!JadeStringUtil.isEmpty(entity.getCapital())) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_CAPITAL, entity.getCapital());
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_CAPITAL, "0.0");
            }
            if (entity.getGeImpotSource().booleanValue()) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_IMPOSITION, getSession()
                        .getLabel("DETAIL_FISC_GE_OUI"));
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_IMPOSITION, getSession()
                        .getLabel("DETAIL_FISC_GE_NON"));
            }
            if (entity.getGeTaxationOffice().booleanValue()) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_TAX_OFFICE, getSession()
                        .getLabel("DETAIL_FISC_GE_OUI"));
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_TAX_OFFICE, getSession()
                        .getLabel("DETAIL_FISC_GE_NON"));
            }
            if (entity.getGeImpositionSelonDepense().booleanValue()) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_IMPO_DEPENSE, getSession()
                        .getLabel("DETAIL_FISC_GE_OUI"));
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_IMPO_DEPENSE, getSession()
                        .getLabel("DETAIL_FISC_GE_NON"));
            }
            if (entity.getGePension().booleanValue()) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_PENSION,
                        getSession().getLabel("DETAIL_FISC_GE_OUI"));
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_PENSION,
                        getSession().getLabel("DETAIL_FISC_GE_NON"));
            }
            if (entity.getGeRenteVieillesse().booleanValue()) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_RENTE_VIEIL, getSession()
                        .getLabel("DETAIL_FISC_GE_OUI"));
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_RENTE_VIEIL, getSession()
                        .getLabel("DETAIL_FISC_GE_NON"));
            }
            if (entity.getGeRenteInvalidite().booleanValue()) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_RENTE_INVALID, getSession()
                        .getLabel("DETAIL_FISC_GE_OUI"));
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_RENTE_INVALID, getSession()
                        .getLabel("DETAIL_FISC_GE_NON"));
            }
            if (entity.getGeRetraite().booleanValue()) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_RETRAITE,
                        getSession().getLabel("DETAIL_FISC_GE_OUI"));
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_RETRAITE,
                        getSession().getLabel("DETAIL_FISC_GE_NON"));
            }
            if (entity.getGeDivers().booleanValue()) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_DIVERS,
                        getSession().getLabel("DETAIL_FISC_GE_OUI"));
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_DIVERS,
                        getSession().getLabel("DETAIL_FISC_GE_NON"));
            }
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_EXERCICE, entity.getDebutExercice1()
                    + " " + getSession().getLabel("DETAIL_FISC_GE_AU") + " " + entity.getFinExercice1());
            if (!JadeStringUtil.isEmpty(entity.getFortune())) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_FORTUNE, entity.getFortune());
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_FORTUNE, "0.0");
            }
            if (entity.getGePersonneNonIdentifiee().booleanValue()) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_PERS_NI,
                        getSession().getLabel("DETAIL_FISC_GE_OUI"));
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_PERS_NI,
                        getSession().getLabel("DETAIL_FISC_GE_NON"));
            }
            if (entity.getGeNonAssujettiIBO().booleanValue()) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_ASSUJETI_IBO, getSession()
                        .getLabel("DETAIL_FISC_GE_OUI"));
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_ASSUJETI_IBO, getSession()
                        .getLabel("DETAIL_FISC_GE_NON"));
            }
            if (entity.getGeNonAssujettiIFD().booleanValue()) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_ASSUJETI_IFD, getSession()
                        .getLabel("DETAIL_FISC_GE_OUI"));
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_ASSUJETI_IFD, getSession()
                        .getLabel("DETAIL_FISC_GE_NON"));
            }
            if (entity.getGePasActiviteDeclaree().booleanValue()) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_ACTIVITE_DECLARE, getSession()
                        .getLabel("DETAIL_FISC_GE_OUI"));
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_ACTIVITE_DECLARE, getSession()
                        .getLabel("DETAIL_FISC_GE_NON"));
            }
            if (entity.getGePensionAlimentaire().booleanValue()) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_PENSION_ALIM, getSession()
                        .getLabel("DETAIL_FISC_GE_OUI"));
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_PENSION_ALIM, getSession()
                        .getLabel("DETAIL_FISC_GE_NON"));
            }
            if (entity.getGeRenteViagere().booleanValue()) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_RENTE_VIAGERE, getSession()
                        .getLabel("DETAIL_FISC_GE_OUI"));
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_RENTE_VIAGERE, getSession()
                        .getLabel("DETAIL_FISC_GE_NON"));
            }
            if (entity.getGeIndemniteJournaliere().booleanValue()) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_INDEM_JOUR, getSession()
                        .getLabel("DETAIL_FISC_GE_OUI"));
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_INDEM_JOUR, getSession()
                        .getLabel("DETAIL_FISC_GE_NON"));
            }
            if (entity.getGeBourses().booleanValue()) {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_BOURSES,
                        getSession().getLabel("DETAIL_FISC_GE_OUI"));
            } else {
                super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_BOURSES,
                        getSession().getLabel("DETAIL_FISC_GE_NON"));
            }
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_EXPLICATIONS,
                    entity.getGeExplicationsDivers());
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_DATE_MAD,
                    entity.getGeDateTransfertMAD());
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_DATE_RECEPTION,
                    entity.getDateRetour());
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.PARAM_OBSERVATIONS,
                    entity.getGeObservations());

            if (!getTransaction().hasErrors()) {
                getTransaction().commit();
            } else {
                getTransaction().rollback();
            }
        } catch (Exception e) {
        }
    }

    /**
     * Récupère les informations du décompte avant impression.
     */
    @Override
    public void beforeExecuteReport() {
        // Variable pour le comptage
        try {
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_TIERS,
                    getSession().getLabel("DETAIL_FISC_GE_TIERS"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_TIERS_DESCRIPTION, getSession()
                    .getLabel("DETAIL_FISC_GE_TIERS_DESCRIPTION"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_IFD,
                    getSession().getLabel("DETAIL_FISC_GE_IFD"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_NUMCONTRI_RECU, getSession()
                    .getLabel("DETAIL_FISC_GE_NUMCONTRI_RECU"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_NUMAFF_RECU,
                    getSession().getLabel("DETAIL_FISC_GE_NUMAFF_RECU"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_NUMAVS_CON,
                    getSession().getLabel("DETAIL_FISC_GE_NUMAVS_CON"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_GENRE_AFF,
                    getSession().getLabel("DETAIL_FISC_GE_GENRE_AFF"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_NUM_CAISSE,
                    getSession().getLabel("DETAIL_FISC_GE_NUM_CAISSE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_NUM_DEMANDE,
                    getSession().getLabel("DETAIL_FISC_GE_NUM_DEMANDE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_NUM_COMM,
                    getSession().getLabel("DETAIL_FISC_GE_NUM_COMM"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_AFFILIE,
                    getSession().getLabel("DETAIL_FISC_AFFILIE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_NSS,
                    getSession().getLabel("DETAIL_FISC_GE_NSS"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_NUM_CONTRI,
                    getSession().getLabel("DETAIL_FISC_GE_NUM_CONTRI"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_NOM,
                    getSession().getLabel("DETAIL_FISC_GE_NOM"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_PRENOM,
                    getSession().getLabel("DETAIL_FISC_GE_PRENOM"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_NOM_CON,
                    getSession().getLabel("DETAIL_FISC_GE_NOM_CON"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_PRENOM_CON,
                    getSession().getLabel("DETAIL_FISC_GE_PRENOM_CON"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_NOM_AFC,
                    getSession().getLabel("DETAIL_FISC_GE_NOM_AFC"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_PRENOM_AFC,
                    getSession().getLabel("DETAIL_FISC_GE_PRENOM_AFC"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_GENRE_TAX,
                    getSession().getLabel("DETAIL_FISC_GE_GENRE_TAX"));
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_INFO_GENERAL,
                    getSession().getLabel("DETAIL_FISC_INFO_GENERAL"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_ANNEE,
                    getSession().getLabel("DETAIL_FISC_GE_ANNEE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_REVENU,
                    getSession().getLabel("DETAIL_FISC_GE_REVENU"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_CAPITAL,
                    getSession().getLabel("CAPITAL"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_IMPOSITION,
                    getSession().getLabel("DETAIL_FISC_GE_IMPOSITION"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_TAX_OFFICE,
                    getSession().getLabel("DETAIL_FISC_GE_TAX_OFFICE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_IMPO_DEPENSE,
                    getSession().getLabel("DETAIL_FISC_GE_IMPO_DEPENSE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_PENSION,
                    getSession().getLabel("DETAIL_FISC_GE_PENSION"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_RENTE_VIEIL,
                    getSession().getLabel("DETAIL_FISC_GE_RENTE_VIEIL"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_RENTE_INVALID, getSession()
                    .getLabel("DETAIL_FISC_GE_RENTE_INVALID"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_RETRAITE,
                    getSession().getLabel("DETAIL_FISC_GE_RETRAITE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_DIVERS,
                    getSession().getLabel("DETAIL_FISC_GE_DIVERS"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_EXERCICE,
                    getSession().getLabel("DETAIL_FISC_GE_EXERCICE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_FORTUNE,
                    getSession().getLabel("DETAIL_FISC_GE_FORTUNE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_PERS_NI,
                    getSession().getLabel("DETAIL_FISC_GE_PERS_NI"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_ASSUJETI_IBO,
                    getSession().getLabel("DETAIL_FISC_GE_ASSUJETI_IBO"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_ASSUJETI_IFD,
                    getSession().getLabel("DETAIL_FISC_GE_ASSUJETI_IFD"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_ACTIVITE_DECLARE, getSession()
                    .getLabel("DETAIL_FISC_GE_ACTIVITE_DECLARE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_PENSION_ALIM,
                    getSession().getLabel("DETAIL_FISC_GE_PENSION_ALIM"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_RENTE_VIAGERE, getSession()
                    .getLabel("DETAIL_FISC_GE_RENTE_VIAGERE"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_INDEM_JOUR,
                    getSession().getLabel("DETAIL_FISC_GE_INDEM_JOUR"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_BOURSES,
                    getSession().getLabel("DETAIL_FISC_GE_BOURSES"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_EXPLICATIONS,
                    getSession().getLabel("DETAIL_FISC_GE_EXPLICATIONS"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_INFO_SUPP,
                    getSession().getLabel("DETAIL_FISC_GE_INFO_SUPP"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_DATE_MAD,
                    getSession().getLabel("DETAIL_FISC_GE_DATE_MAD"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_DATE_RECEPTION, getSession()
                    .getLabel("DETAIL_FISC_DATE_RECEPTION"));
            super.setParametres(CPIListeCommunicationRetourDetailFiscGeParam.LABEL_OBSERVATIONS,
                    getSession().getLabel("DETAIL_FISC_GE_OBSERVATIONS"));
            // Un doc info par lot
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
            super.setTemplateFile("PHENIX_VALIDATION_RETOUR_FISC_DETAIL_GE");
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

    public CPCommunicationFiscaleRetourGEViewBean getEntity() {
        return entity;
    }

    public CPCommunicationFiscaleRetourGEManager getManager() {
        return manager;
    }

    @Override
    public boolean next() throws FWIException {
        try {
            if (((entity = (CPCommunicationFiscaleRetourGEViewBean) manager.cursorReadNext(statement)) != null)
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

    /*
     * Insertion des infos pour la publication (GED)
     */
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

    public void setEntity(CPCommunicationFiscaleRetourGEViewBean entity) {
        this.entity = entity;
    }

    public void setManager(CPCommunicationFiscaleRetourGEManager manager) {
        this.manager = manager;
    }

}
