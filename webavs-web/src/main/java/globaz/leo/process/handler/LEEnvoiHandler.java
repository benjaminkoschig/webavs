/*
 * Créé le 4 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.process.handler;

import globaz.envoi.constantes.ENConstantes;
import globaz.envoi.db.parametreEnvoi.access.ENRappel;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.journalisation.constantes.JOConstantes;
import globaz.journalisation.db.common.access.JOCommonJournalisationManager;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.db.data.LEEnvoiDataSource;
import globaz.leo.db.data.LEParamEnvoiDataSource;
import globaz.leo.db.envoi.LEChampUtilisateurListViewBean;
import globaz.leo.db.envoi.LEEnvoiListViewBean;
import globaz.leo.db.envoi.LEEnvoiOriginalViewBean;
import globaz.leo.db.envoi.LEEnvoiViewBean;
import globaz.leo.db.envoi.LELotsViewBean;
import globaz.leo.db.parametrage.LEDestinataireListViewBean;
import globaz.leo.db.parametrage.LEDestinataireViewBean;
import globaz.leo.db.parametrage.LEFormulePDFListViewBean;
import globaz.leo.db.parametrage.LEFormulePDFViewBean;
import globaz.leo.util.LEUtil;
import globaz.lupus.db.data.LUJournalDataSource;
import globaz.pyxis.db.tiers.TICompositionTiers;
import globaz.pyxis.db.tiers.TICompositionTiersManager;
import globaz.pyxis.db.tiers.TITiers;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LEEnvoiHandler {
    /**
     * @return
     */
    public static LEChampUtilisateurListViewBean getChampUt(BSession session, String idEnvoiParent) {
        LEChampUtilisateurListViewBean managerChamp = null;
        try {
            managerChamp = new LEChampUtilisateurListViewBean();
            managerChamp.setSession(session);
            managerChamp.setForIdJournalisation(idEnvoiParent);
            managerChamp.find();
        } catch (Exception e) {
            JadeLogger.info(LEEnvoiHandler.class, " Champ ut non trouvés pour l'id parent :" + idEnvoiParent);
        }
        return managerChamp;
    }

    private String _getAgenceCommunale(String idTiers, BSession session, BTransaction transaction, String TypeAgence)
            throws Exception {

        // recherche de l'agence communale en fonction du tiers dans la colonne
        // tiersParent
        TICompositionTiers agenceCommunale = null;
        String idAgenceCommunale = null;
        TICompositionTiersManager lienTiersManager = new TICompositionTiersManager();
        lienTiersManager.setSession(session);
        lienTiersManager.setForIdTiersParent(idTiers);
        lienTiersManager.setForTypeLien(TypeAgence);
        lienTiersManager.find(transaction);

        if (lienTiersManager.size() > 0) {
            agenceCommunale = (TICompositionTiers) lienTiersManager.getFirstEntity();
            idAgenceCommunale = agenceCommunale.getIdTiersEnfant();
        } else {
            // si on a pas de résultat on regarde dans la colonne tiersEnfant
            lienTiersManager = new TICompositionTiersManager();
            lienTiersManager.setSession(session);
            lienTiersManager.setForIdTiersEnfant(idTiers);
            lienTiersManager.setForTypeLien(TypeAgence);
            lienTiersManager.find(transaction);

            if (lienTiersManager.size() > 0) {
                agenceCommunale = (TICompositionTiers) lienTiersManager.getFirstEntity();
                idAgenceCommunale = agenceCommunale.getIdTiersParent();
            }
        }

        return idAgenceCommunale;
    }

    /**
     * @param session
     * @param transaction
     * @return
     */
    public String addNewLot(BSession session, BTransaction transaction, String description, String csTypeLot)
            throws Exception {
        LELotsViewBean lot = new LELotsViewBean();
        lot.setSession(session);
        lot.setHeure(new SimpleDateFormat("HHmmss").format(Calendar.getInstance().getTime()));
        lot.setDate(JACalendar.todayJJsMMsAAAA());
        lot.setCsTypeLot(csTypeLot);
        lot.setDescription(description);
        lot.setIdUtilisateur(session.getUserId());
        lot.add(transaction);
        return lot.getIdLot();
    }

    public String ajouterEnvoiOriginal(LEEnvoiDataSource envoiDS, BSession session, BTransaction transaction)
            throws Exception {
        LEEnvoiOriginalViewBean nouvelEnvoi = new LEEnvoiOriginalViewBean();
        nouvelEnvoi.setSession(session);
        nouvelEnvoi.setCsProvenance("1");
        nouvelEnvoi.setIdDocument(envoiDS.getField(LEEnvoiDataSource.CS_TYPE_DOCUMENT));
        nouvelEnvoi.setCsSelectionnne(ILEConstantes.CS_NON);
        nouvelEnvoi.add(transaction);
        return nouvelEnvoi.getIdEnvoi();
    }

    // public String getNomDoc(String csDocument, BSession session) throws
    // Exception{
    // LEFormuleViewBean formule = findFormule(csDocument, session);
    // return formule.getNomDoc();
    // }
    public String calculDateEnvoiRappel(String valeur, String csUnite) throws Exception {
        JACalendarGregorian calendar = new JACalendarGregorian();
        JADate nextWorkingDay = calendar.getNextWorkingDay(getDateEnvoiRappel(valeur, csUnite));
        return JACalendar.format(nextWorkingDay);
    }

    public LEEnvoiDataSource chargerDonnees(String idEnvoi, BSession session, BTransaction transaction)
            throws Exception {
        String idDernierJournal = getDernierEnvoi(idEnvoi, session, transaction);
        return this.getParamsEnvoi(idDernierJournal, session, transaction);
    }

    public LEEnvoiDataSource chargerDonnees(String idEnvoi, BSession session, BTransaction transaction,
            String dateRappel, String dateCreation) throws Exception {
        String idDernierJournal = getDernierEnvoi(idEnvoi, session, transaction);
        return this.getParamsEnvoi(idDernierJournal, session, transaction, dateRappel, dateCreation);
    }

    // utilisé lorsque l'on est sûr du dernier journal et qu'on connaît l'étape
    // suivante
    // typiquement lors du batch d'impression.
    public LEEnvoiDataSource chargerDonnees(String idDernierJournal, String csTypeEtapeSuivante, BSession session,
            BTransaction transaction) throws Exception {
        return this.getParamsEnvoi(idDernierJournal, csTypeEtapeSuivante, session, transaction);
    }
    
    public LEEnvoiDataSource chargerDonnees(String idDernierJournal, String csTypeEtapeSuivante, BSession session, BTransaction transaction,
           String dateCreation) throws Exception {
        return this.getParamsEnvoi(idDernierJournal, csTypeEtapeSuivante, session, transaction, dateCreation);
    }

    public LUJournalDataSource envoiToJournalisation(LEParamEnvoiDataSource envoiCrtDS) {
        LUJournalDataSource journalDataSource = new LUJournalDataSource();
        LEParamEnvoiDataSource.paramEnvoi paramCrt;
        for (int i = 0; i < envoiCrtDS.size(); i++) {
            paramCrt = envoiCrtDS.getParamEnvoi(i);
            if (ILEConstantes.CS_PARAM_GEN_ID_ENVOI_PRECEDENT.equals(paramCrt.getCsType())) {
                journalDataSource.setIdJournalParent(paramCrt.getValeur());
            } else if (ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE.equals(paramCrt.getCsType())) {
                journalDataSource.setDestination(JOConstantes.CS_JO_LIEN_TIERS_DESTINATAIRE, paramCrt.getValeur());
            } else {
                journalDataSource.addProvenance(paramCrt.getCsType(), paramCrt.getValeur());
                if (ILEConstantes.CS_PARAM_GEN_NUMERO.equals(paramCrt.getCsType())) {
                    journalDataSource.setLibelle(paramCrt.getValeur());
                }
            }
        }
        return journalDataSource;
    }

    public LEFormulePDFViewBean findFormule(String csDocument, BSession session) throws Exception {
        LEFormulePDFListViewBean formuleDef = new LEFormulePDFListViewBean();
        formuleDef.setSession(session);
        formuleDef.setForCsLangue(LEUtil.getCodeSystemeLangue(session));
        formuleDef.setForCsDocument(csDocument);
        formuleDef.find();
        if (formuleDef.size() > 0) {
            return (LEFormulePDFViewBean) formuleDef.getFirstEntity();
        } else {
            throw new Exception("Erreur : aucun document trouvé dans le paramétrage pour le type "
                    + session.getCode(csDocument) + " spécifié");
        }
    }

    public String genererEnvoi(LEEnvoiDataSource envoiDS, BSession session, BTransaction transaction) throws Exception {
        return this.genererEnvoi("", envoiDS, session, transaction);
    }

    public String genererEnvoi(LEEnvoiDataSource envoiDS, BSession session, BTransaction transaction,
            String idDestinataire) throws Exception {
        return this.genererEnvoi("", envoiDS, session, transaction, idDestinataire);
    }

    /**
     * @param idLot
     * @param envoiDS
     * @param session
     * @param transaction
     */
    public String genererEnvoi(String idLot, LEEnvoiDataSource envoiDS, BSession session, BTransaction transaction)
            throws Exception {
        return this.genererEnvoi(idLot, "", envoiDS, session, transaction);
    }

    public String genererEnvoi(String idLot, LEEnvoiDataSource envoiDS, BSession session, BTransaction transaction,
            String idDestinataire) throws Exception {
        return this.genererEnvoi(idLot, "", envoiDS, session, transaction, idDestinataire);
    }

    public String genererEnvoi(String idLot, String dateCreation, LEEnvoiDataSource envoiDS, BSession session,
            BTransaction transaction) throws Exception {
        return this.genererEnvoi(idLot, dateCreation, envoiDS, session, transaction, null);
    }

    public String genererEnvoi(String idLot, String dateCreation, LEEnvoiDataSource envoiDS, BSession session,
            BTransaction transaction, String idDestinataire) throws Exception {
        // on utilise la journalisation comme support de données
        LEJournalHandler jHandler = new LEJournalHandler();
        // on génère une journalisation
        LEParamEnvoiDataSource envoiCrt = envoiDS.getParamEnvoi();
        LUJournalDataSource jDS = envoiToJournalisation(envoiCrt);
        jDS.setDateRappel(envoiDS.getField(LEEnvoiDataSource.DATE_RAPPEL));
        if (!JadeStringUtil.isBlank(idDestinataire)) {
            jDS.setDestination(JOConstantes.CS_JO_LIEN_TIERS_DESTINATAIRE, idDestinataire);
        }
        return jHandler.genererJournalisationFormule(jDS, envoiDS.getField(LEEnvoiDataSource.CS_TYPE_DOCUMENT),
                envoiDS.getField(LEEnvoiDataSource.CS_CATEGORIE), idLot,
                envoiDS.getField(LEEnvoiDataSource.CS_ETAPE_SUIVANTE),
                envoiDS.getField(LEEnvoiDataSource.CS_MANU_AUTO), dateCreation, session, transaction);
    }

    /**
     * Récupère l'agence communale d'un tiers
     * 
     * @param idTiers
     *            : id du tiers dont on souhaite trouver l'agence communale
     * @param session
     * @param transaction
     * @return
     * @throws Exception
     */
    public String getAgenceCommunale(String idTiers, BSession session, BTransaction transaction) throws Exception {
        String idAgenceCommunale = null;
        // considère les agences communales standard
        idAgenceCommunale = _getAgenceCommunale(idTiers, session, transaction, TICompositionTiers.CS_AGENCE_COMMUNALE);
        // Si aucune agence communale standard trouvée, recherche des agences communales d'exploitation
        // BZ 6658
        if (JadeStringUtil.isBlankOrZero(idAgenceCommunale)) {
            idAgenceCommunale = _getAgenceCommunale(idTiers, session, transaction,
                    TICompositionTiers.CS_AGENCE_COMM_PRIVEE);
        }
        return idAgenceCommunale;
    }

    public String getClasseDocument(String csDocument, BSession session) throws Exception {
        LEFormulePDFViewBean formule = findFormule(csDocument, session);
        return formule.getClasseName();
    }

    /**
     * @param _journalId
     * @param session
     * @param transaction
     * @return
     */
    public String getCsDocument(String _journalId, BSession session, BTransaction transaction) throws Exception {
        LEJournalHandler journalHandler = new LEJournalHandler();
        String csDocument = journalHandler.getCsDocument(_journalId, session, transaction);
        LEParamEnvoiHandler paramEnvoi = new LEParamEnvoiHandler();
        return paramEnvoi.getFormuleRappel(csDocument, session, transaction);
    }

    public String getDateEnvoi(String idEnvoi, BSession session) throws Exception {
        LEEnvoiViewBean envoi = this.getEnvoi(idEnvoi, session);
        return envoi.getDate();
    }

    public String getDateEnvoiPrecedent(LEEnvoiViewBean envoi) throws Exception {
        if (envoi.isJournalInitial()) {
            throw new Exception("Erreur, impossible de trouver la date d'envoi précédent pour un envoi initial id="
                    + envoi.getIdEnvoi());
        }
        LEEnvoiViewBean envoiPrec = this.getEnvoi(envoi.getIdPrecedent(), envoi.getSession());
        return envoiPrec.getDate();
    }

    public JADate getDateEnvoiRappel(String valeur, String csUnite) throws Exception {
        JACalendarGregorian calendar = new JACalendarGregorian();
        JADate current = new JADate(JACalendar.todayJJsMMsAAAA());
        if (ENConstantes.CS_EN_UNITE_JOURS.equals(csUnite)) {
            current = calendar.addDays(current, Integer.parseInt(valeur));
        } else if (ENConstantes.CS_EN_UNITE_SEMAINES.equals(csUnite)) {
            current = calendar.addDays(current, Integer.parseInt(valeur) * 7);
        } else if (ENConstantes.CS_EN_UNITE_MOIS.equals(csUnite)) {
            current = calendar.addMonths(current, Integer.parseInt(valeur));
        } else {
            throw new Exception("Erreur : la valeur de l'unité n'est pas correct !");
        }
        return current;
    }

    public String getDernierEnvoi(String idEnvoi, BSession session, BTransaction transaction) throws Exception {
        LEJournalHandler journalHandler = new LEJournalHandler();
        return journalHandler.getDernierJournal(idEnvoi, session, transaction);
    }

    /**
     * @param csDocument
     * @param session
     * @param transaction
     * @return
     */
    public LEDestinataireListViewBean getDestinataires(String csDocument, BSession session, BTransaction transaction)
            throws Exception {
        LEDestinataireListViewBean list = new LEDestinataireListViewBean();
        list.setSession(session);
        list.setForCsDocument(csDocument);
        list.find(transaction);
        return list;

    }

    public LEEnvoiViewBean getEnvoi(String idEnvoi, BSession session) throws Exception {
        LEEnvoiViewBean envoiVb = new LEEnvoiViewBean();
        envoiVb.setSession(session);
        envoiVb.setIdEnvoi(idEnvoi);
        envoiVb.retrieve();
        if (envoiVb.isNew()) {
            throw new Exception("Erreur, impossible de trouver l'envoi avec un id = " + idEnvoi);
        }
        return envoiVb;
    }

    public LEEnvoiViewBean getEnvoi(String idEnvoi, BSession session, BTransaction transaction) throws Exception {
        LEEnvoiViewBean envoiVb = new LEEnvoiViewBean();
        envoiVb.setSession(session);
        envoiVb.setIdEnvoi(idEnvoi);
        envoiVb.retrieve(transaction);
        if (envoiVb.isNew()) {
            throw new Exception("Erreur, impossible de trouver l'envoi avec un id = " + idEnvoi);
        }
        return envoiVb;
    }

    /**
     * @param csFormule
     * @param session
     * @param object
     * @return
     */
    public String getFormulePrecedente(String csFormule, BSession session, BTransaction transaction) throws Exception {
        LEParamEnvoiHandler pEnvoiHandler = new LEParamEnvoiHandler();
        return pEnvoiHandler.getFormuleForCsRappel(csFormule, session, transaction);
    }

    /**
     * @param string
     * @param session
     * @return
     */
    public String getIdInitial(String idEnvoi, BSession session) throws Exception {
        return this.getEnvoi(idEnvoi, session).getIdInitial();
    }

    /**
     * @param csFormule
     * @param dateReference
     * @param session
     * @return
     */
    private LEEnvoiListViewBean getListeEnvoi(String csFormule, String dateReference, BSession session)
            throws Exception {
        LEEnvoiListViewBean res = new LEEnvoiListViewBean();
        res.setSession(session);
        res.setForCsTypeJournal(JOConstantes.CS_JO_FMT_FORMULE);
        res.setForIdSuivant("0");
        res.setUntilDate(dateReference);
        res.setJointureFichier(JOCommonJournalisationManager.TYPE_PAR_DATE_ENVOI);
        res.setForCsTypeCodeSysteme(ILEConstantes.CS_DEF_FORMULE_GROUPE);
        res.setForValeurCodeSysteme(csFormule);
        res.find(BManager.SIZE_NOLIMIT);
        return res;
    }

    private LEEnvoiDataSource getParamsEnvoi(String idJournal, BSession session, BTransaction transaction)
            throws Exception {
        return this.getParamsEnvoi(idJournal, session, transaction, null, null);
    }

    private LEEnvoiDataSource getParamsEnvoi(String idJournal, BSession session, BTransaction transaction,
            String dateRappel, String dateCreation) throws Exception {
        LEJournalHandler journalHandler = new LEJournalHandler();
        LEParamEnvoiHandler pHandler = new LEParamEnvoiHandler();
        LEParamEnvoiDataSource listParam = journalHandler.getParamsEnvoiDataSource(idJournal, session, transaction);
        String csDocument = journalHandler.getEtapeSuivanteByIdJournal(idJournal, session, transaction);
        // tester si pour le csDocument, le destinataire est spécifié
        LEDestinataireListViewBean dest = getDestinataires(csDocument, session, transaction);
        if (dest.size() > 0) {
            listParam.remove(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE);
            listParam.addParamEnvoi(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE,
                    ((LEDestinataireViewBean) dest.getFirstEntity()).getIdTiers());
        }

        // tester si le type de document nécessite un autre destinataire (envoi
        // du mandat à l'agence communale, rappel 1 ou rappel 2)
        if (ILEConstantes.CS_DEF_FORMULE_MANDAT_AGENT_DS.equals(csDocument)
                || ILEConstantes.CS_DEF_FORMULE_MANDAT_AGENT_RAPPEL1_DS.equals(csDocument)
                || ILEConstantes.CS_DEF_FORMULE_MANDAT_AGENT_RAPPEL2_DS.equals(csDocument)) {
            String idAgence = getAgenceCommunale(listParam.getParamEnvoi(ILEConstantes.CS_PARAM_GEN_ID_TIERS)
                    .getValeur(), session, transaction);
            if (!JadeStringUtil.isEmpty(idAgence)) {
                listParam.remove(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE);
                listParam.addParamEnvoi(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE, idAgence);
            } else {

                String theInfoTiers = "";
                try {
                    TITiers theDestinataire = new TITiers();
                    theDestinataire.setSession(session);
                    theDestinataire
                            .setIdTiers(listParam.getParamEnvoi(ILEConstantes.CS_PARAM_GEN_ID_TIERS).getValeur());
                    theDestinataire.retrieve();
                    theInfoTiers = theDestinataire.getNomEtNumero();
                } catch (Exception e) {
                    theInfoTiers = "";
                }

                throw new Exception(FWMessageFormat.format(session.getLabel("ERREUR_TIERS_SANS_AGENCE_COMMUNALE"),
                        theInfoTiers));
            }
        }
        return pHandler.loadParametreEnvoi(csDocument, listParam, session, transaction, dateRappel, dateCreation);
    }
    

    /**
     * 
     * 
     */
    private LEEnvoiDataSource getParamsEnvoi(String idJournal, String csDocument, BSession session,
            BTransaction transaction, String dateCreation) throws Exception {
        LEJournalHandler journalHandler = new LEJournalHandler();
        LEParamEnvoiHandler pHandler = new LEParamEnvoiHandler();
        LEParamEnvoiDataSource listParam = journalHandler.getParamsEnvoiDataSource(idJournal, session, transaction);
        LEDestinataireListViewBean dest = getDestinataires(csDocument, session, transaction);
        if (dest.size() > 0) {
            listParam.remove(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE);
            listParam.addParamEnvoi(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE,
                    ((LEDestinataireViewBean) dest.getFirstEntity()).getIdTiers());
        }

        // tester si le type de document nécessite un autre destinataire (envoi
        // du mandat à l'agence communale, rappel 1 ou rappel 2)
        if (ILEConstantes.CS_DEF_FORMULE_MANDAT_AGENT_DS.equals(csDocument)
                || ILEConstantes.CS_DEF_FORMULE_MANDAT_AGENT_RAPPEL1_DS.equals(csDocument)
                || ILEConstantes.CS_DEF_FORMULE_MANDAT_AGENT_RAPPEL2_DS.equals(csDocument)) {
            String idAgence = getAgenceCommunale(listParam.getParamEnvoi(ILEConstantes.CS_PARAM_GEN_ID_TIERS)
                    .getValeur(), session, transaction);
            if (!JadeStringUtil.isEmpty(idAgence)) {
                listParam.remove(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE);
                listParam.addParamEnvoi(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE, idAgence);
            } else {

                String theInfoTiers = "";
                try {
                    TITiers theDestinataire = new TITiers();
                    theDestinataire.setSession(session);
                    theDestinataire
                            .setIdTiers(listParam.getParamEnvoi(ILEConstantes.CS_PARAM_GEN_ID_TIERS).getValeur());
                    theDestinataire.retrieve();
                    theInfoTiers = theDestinataire.getNomEtNumero();
                } catch (Exception e) {
                    theInfoTiers = "";
                }

                throw new Exception(FWMessageFormat.format(session.getLabel("ERREUR_TIERS_SANS_AGENCE_COMMUNALE"),
                        theInfoTiers));
            }

        }

        return pHandler.loadParametreEnvoi(csDocument, listParam, session, transaction, "", dateCreation);
    }
    

    /**
     * @param idDernierJournal
     * @param session
     * @param transaction
     * @return
     */
    private LEEnvoiDataSource getParamsEnvoi(String idJournal, String csDocument, BSession session,
            BTransaction transaction) throws Exception {
        LEJournalHandler journalHandler = new LEJournalHandler();
        LEParamEnvoiHandler pHandler = new LEParamEnvoiHandler();
        LEParamEnvoiDataSource listParam = journalHandler.getParamsEnvoiDataSource(idJournal, session, transaction);
        LEDestinataireListViewBean dest = getDestinataires(csDocument, session, transaction);
        if (dest.size() > 0) {
            listParam.remove(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE);
            listParam.addParamEnvoi(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE,
                    ((LEDestinataireViewBean) dest.getFirstEntity()).getIdTiers());
        }

        // tester si le type de document nécessite un autre destinataire (envoi
        // du mandat à l'agence communale, rappel 1 ou rappel 2)
        if (ILEConstantes.CS_DEF_FORMULE_MANDAT_AGENT_DS.equals(csDocument)
                || ILEConstantes.CS_DEF_FORMULE_MANDAT_AGENT_RAPPEL1_DS.equals(csDocument)
                || ILEConstantes.CS_DEF_FORMULE_MANDAT_AGENT_RAPPEL2_DS.equals(csDocument)) {
            String idAgence = getAgenceCommunale(listParam.getParamEnvoi(ILEConstantes.CS_PARAM_GEN_ID_TIERS)
                    .getValeur(), session, transaction);
            if (!JadeStringUtil.isEmpty(idAgence)) {
                listParam.remove(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE);
                listParam.addParamEnvoi(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE, idAgence);
            } else {

                String theInfoTiers = "";
                try {
                    TITiers theDestinataire = new TITiers();
                    theDestinataire.setSession(session);
                    theDestinataire
                            .setIdTiers(listParam.getParamEnvoi(ILEConstantes.CS_PARAM_GEN_ID_TIERS).getValeur());
                    theDestinataire.retrieve();
                    theInfoTiers = theDestinataire.getNomEtNumero();
                } catch (Exception e) {
                    theInfoTiers = "";
                }

                throw new Exception(FWMessageFormat.format(session.getLabel("ERREUR_TIERS_SANS_AGENCE_COMMUNALE"),
                        theInfoTiers));
            }

        }

        return pHandler.loadParametreEnvoi(csDocument, listParam, session, transaction);
    }

    public String getTempsAttenteJours(LEEnvoiViewBean envoi) throws Exception {
        // recherche le csDocument
        LEJournalHandler journHandler = new LEJournalHandler();
        String csDoc = journHandler.getCsDocument(envoi.getIdEnvoi(), envoi.getSession(), null);
        return this.getTempsAttenteJours(csDoc, envoi.getSession());
    }

    /**
     * @param string
     * @return
     */
    public String getTempsAttenteJours(String csDoc, BSession session) throws Exception {
        // rechercher le rappel relatif au csDoc dans le paramétrage
        LEParamEnvoiHandler paramEnvoiHandler = new LEParamEnvoiHandler();
        ENRappel rappel = paramEnvoiHandler.getRappelForCsFormule(session, null, csDoc);
        String nbJours = "";
        if (rappel != null) {
            if (ENConstantes.CS_EN_UNITE_JOURS.equals(rappel.getCsUnite())) {
                nbJours = rappel.getTempsAttente();
            } else if (ENConstantes.CS_EN_UNITE_SEMAINES.equals(rappel.getCsUnite())) {
                nbJours = String.valueOf(Integer.parseInt(rappel.getTempsAttente()) * 7);
            } else {
                throw new Exception("Erreur : la valeur de l'unité n'est pas correct !");
            }
        }
        return nbJours;
    }

    // private LEEnvoiDataSource getParamsEnvoiList(String csDocument, String
    // dateReference ,BSession session, BTransaction transaction) throws
    // Exception {
    // // init le dataSource
    // LEJournalHandler journalHandler = new LEJournalHandler();
    // LEParamEnvoiHandler pHandler = new LEParamEnvoiHandler();
    // // String csDoc = pHandler.getFormuleRappel(csDocument, session,
    // transaction);
    // LEEnvoiDataSource envoiDS = pHandler.loadParametreEnvoi(csDocument,
    // session, transaction);
    // // initialiser le CS Document
    // envoiDS.put(LEEnvoiDataSource.CS_TYPE_DOCUMENT, csDocument);
    // // charger aussi la catégorie
    // envoiDS.put(LEEnvoiDataSource.CS_CATEGORIE,
    // pHandler.getCategorieByCsDoc(csDocument, session, transaction));
    // // on recherche tous les envoi concerne
    // String csFormPrec = getFormulePrecedente(csDocument, session,
    // transaction);
    // LEEnvoiListViewBean listeEnvoi = new LEEnvoiListViewBean();
    // listeEnvoi.setSession(session);
    // //listeEnvoi.setForCsTypeJournal(ILUConstantes.CS_JO_FMT_FORMULE);
    // listeEnvoi.setForIdSuivant("0");
    // listeEnvoi.setUntilDate(dateReference);
    // //listeEnvoi.setJointureFichier(LEEnvoiListViewBean.TYPE_PAR_DATE_ENVOI);
    // listeEnvoi.setForCsTypeCodeSysteme(ILEConstantes.CS_DEF_FORMULE_GROUPE);
    // listeEnvoi.setForValeurCodeSysteme(csFormPrec);
    // BStatement statement = listeEnvoi.cursorOpen(transaction);
    // LEEnvoiViewBean envoiCrt = null;
    // LEParamEnvoiDataSource params;
    // // on charge tous les paramètres pour chaque envois
    // while((envoiCrt = (LEEnvoiViewBean)
    // listeEnvoi.cursorReadNext(statement))!=null) {
    // params = journalHandler.getParamsEnvoiDataSource(envoiCrt.getIdEnvoi(),
    // session, transaction);
    // listParam.addEnvoi(params);
    // }
    // listeEnvoi.cursorClose(statement);
    // envoiDS.addListeParamEnvoi(listParam);
    // return envoiDS;
    // }
    /**
     * @param idDernierJournal
     * @param session
     * @param transaction
     * @return
     */
    private String getTypeDocEtapeSuivante(String idJournal, BSession session, BTransaction transaction)
            throws Exception {
        LEEnvoiHandler envoiHandler = new LEEnvoiHandler();
        return envoiHandler.getCsDocument(idJournal, session, transaction);
    }

}
