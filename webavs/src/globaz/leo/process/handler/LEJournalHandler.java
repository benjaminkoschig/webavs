/*
 * Créé le 4 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.process.handler;

import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWIncrementation;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.journalisation.constantes.JOConstantes;
import globaz.journalisation.db.common.access.IJOCommonComplementJournalDefTable;
import globaz.journalisation.db.common.access.IJOCommonJournalisationDefTable;
import globaz.journalisation.db.common.access.IJOCommonReferenceDestinationDefTable;
import globaz.journalisation.db.common.access.IJOCommonReferenceProvenanceDefTable;
import globaz.journalisation.db.journalisation.access.IJOComplementJournalDefTable;
import globaz.journalisation.db.journalisation.access.IJOJournalisationDefTable;
import globaz.journalisation.db.journalisation.access.IJOReferenceDestinationDefTable;
import globaz.journalisation.db.journalisation.access.IJOReferenceProvenanceDefTable;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.db.data.LEParamEnvoiDataSource;
import globaz.leo.db.envoi.LEEnvoiListViewBean;
import globaz.leo.db.envoi.LEEnvoiViewBean;
import globaz.leo.db.parametrage.LEFormulePDFViewBean;
import globaz.lupus.db.data.LUJournalDataSource;
import globaz.lupus.db.handler.LUJournalDefaulthandler;
import globaz.lupus.db.journalisation.LUComplementJournalListViewBean;
import globaz.lupus.db.journalisation.LUComplementJournalViewBean;
import globaz.lupus.db.journalisation.LUGroupeJournalViewBean;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.lupus.db.journalisation.LUJournalViewBean;
import globaz.lupus.db.journalisation.LULotsViewBean;
import globaz.lupus.db.journalisation.LUReferenceDestinationListViewBean;
import globaz.lupus.db.journalisation.LUReferenceDestinationViewBean;
import globaz.lupus.db.journalisation.LUReferenceProvenanceListViewBean;
import globaz.lupus.db.journalisation.LUReferenceProvenanceViewBean;
import javax.servlet.http.HttpServletRequest;

/**
 * @author ald Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LEJournalHandler {
    public void annulerDerniereEtape(String idJournal, BSession session) throws Exception {
        LEEnvoiHandler envoiHandler = new LEEnvoiHandler();
        // on trouve le dernier idJournal du suivi
        String idDernierJournal = envoiHandler.getDernierEnvoi(idJournal, session, null);
        // on efface le dernier Journal
        annulerEtape(idDernierJournal, session, null);
    }

    public void annulerEtape(String idEnvoi, BSession session, BTransaction transaction) throws Exception {
        LEParamEnvoiHandler lParamEnvHandler = new LEParamEnvoiHandler();
        LEEnvoiHandler envoiHandler = new LEEnvoiHandler();
        LUJournalDefaulthandler jDefHandler = new LUJournalDefaulthandler();
        LEEnvoiViewBean envoi = envoiHandler.getEnvoi(idEnvoi, session, transaction);

        if (!envoi.isJournalInitial()) {
            // il ne faut pas supprimer tous les journaux du groupe
            LEFormulePDFViewBean formulePrec = lParamEnvHandler.getFormule(jDefHandler.getComplementJournal(
                    envoi.getIdPrecedent(), ILEConstantes.CS_DEF_FORMULE_GROUPE, session, null), session, transaction);

            LEEnvoiViewBean envoiPre = envoiHandler.getEnvoi(envoi.getIdPrecedent(), session, transaction);
            String dateRappel = lParamEnvHandler.calculDateEnvoiRappel(
                    lParamEnvHandler.getRappel(session, transaction, formulePrec.getIdFormule()), envoiPre.getDate());
            envoi.setDateRappel(dateRappel);
        }
        envoi.delete(transaction);
    }

    public void annulerLot(String idLot, BSession session, BTransaction transaction) {
        try {
            // On va rechercher l'idJournal et l'idGroupeJournal pour l'idLot
            LEEnvoiListViewBean envoiManager = new LEEnvoiListViewBean();
            envoiManager.setSession(session);
            envoiManager.setForIdLot(idLot);
            envoiManager.wantCallMethodAfter(false);
            envoiManager.wantCallMethodBefore(false);
            envoiManager.wantCallMethodAfterFind(false);
            envoiManager.wantCallMethodBeforeFind(false);
            envoiManager.wantComplementJournal(false);
            envoiManager.wantGroupeJournal(false);
            envoiManager.setOrderby(IJOCommonJournalisationDefTable.IDJOURNALISATION);
            envoiManager.find(transaction);
            // on efface tous les complement,provenance, destination du lot
            effaceComplement(transaction, idLot, envoiManager.getCollection());
            effaceReferenceProvenance(transaction, idLot, envoiManager.getCollection());
            effaceReferenceDestination(transaction, idLot, envoiManager.getCollection());
            // on efface le journal
            for (int i = 0; i < envoiManager.getSize(); i++) {
                annulerEtape(((LUJournalViewBean) envoiManager.getEntity(i)).getIdJournalisation(), session,
                        transaction);
            }
            // on efface le / les lot(s)
            LULotsViewBean lotVb = new LULotsViewBean();
            lotVb.setIdLot(idLot);
            lotVb.retrieve(transaction);
            lotVb.delete(transaction);
        } catch (Exception e) {
            JadeLogger.error(this, "Erreur pendant la suppresion des envois pour l'idLot = " + idLot);
        }
    }

    public void annulerReceptionFormule(String idJournal, BSession session) throws Exception {
        LEEnvoiHandler envoiHandler = new LEEnvoiHandler();
        LEJournalHandler journalHandler = new LEJournalHandler();

        // on trouve le dernier idJournal du suivi
        String idDernierJournal = envoiHandler.getDernierEnvoi(idJournal, session, null);

        if (ILEConstantes.CS_DEF_FORMULE_RECEPTION
                .equals(journalHandler.getCsDocument(idDernierJournal, session, null))) {
            annulerEtape(idDernierJournal, session, null);
        }
    }

    /**
     * @param idJournal
     *            : n'importe quel id du groupe de journaux a effacer
     * @param session
     *            : la session
     */
    public void annulerTousJournaux(String idJournal, BSession session, BTransaction transaction) throws Exception {
        // On récupère le journal
        LUJournalDefaulthandler jDefHandler = new LUJournalDefaulthandler();
        LUJournalViewBean journal = jDefHandler.getJournal(idJournal, session, null);
        // si c'est le journal initial on peut tout supprimer sans autres
        if (journal.isJournalInitial()) {
            annulerEtape(idJournal, session, transaction);
        } else {
            // Sinon il faut retrouver l'idInitial avant de supprimer
            annulerEtape(journal.getIdInitial(), session, transaction);
        }
    }

    /**
     * @param transaction
     * @param id
     */
    public void effaceComplement(BTransaction transaction, String idLot, String collection) {
        // On efface les compléments journaux
        BStatement s = new BStatement(transaction);
        StringBuffer sql = new StringBuffer();
        StringBuffer sqlWhere = new StringBuffer();
        sql.append("DELETE FROM ").append(collection).append(IJOComplementJournalDefTable.TABLE_NAME).append(" WHERE ")
                .append(collection).append(IJOComplementJournalDefTable.TABLE_NAME).append(".")
                .append(IJOCommonComplementJournalDefTable.IDJOURNALISATION).append(" IN (SELECT ").append(collection)
                .append(IJOJournalisationDefTable.TABLE_NAME).append(".")
                .append(IJOCommonJournalisationDefTable.IDJOURNALISATION).append(" FROM  ").append(collection)
                .append(IJOJournalisationDefTable.TABLE_NAME).append(" WHERE ")
                .append(IJOCommonJournalisationDefTable.IDLOT).append(" = ").append(idLot).append(" )");
        try {
            s.createStatement();
            s.execute(sql.toString());
        } catch (Exception e) {
            JadeLogger.error(this, "Erreur pendant l'effacement du complément pour l'idLot " + idLot);
            e.printStackTrace();
        } finally {
            s.closeStatement();
        }
    }

    /*
     * public static void main(String[] argv) throws Exception{ BSession session = (BSession)
     * GlobazServer.getCurrentSystem().getApplication("HERMES").newSession ("globazf", "ssiiadm"); LEJournalHandler
     * jHandler = new LEJournalHandler(); jHandler.annulerTousJournaux("1", session,
     * session.getCurrentThreadTransaction()); }
     */

    /**
     * @param transaction
     * @param id
     * @param string
     */
    private void effaceReferenceDestination(BTransaction transaction, String idLot, String collection) {
        // On efface les destinations journaux
        BStatement s = new BStatement(transaction);
        StringBuffer sql = new StringBuffer();
        StringBuffer sqlWhere = new StringBuffer();
        sql.append("DELETE FROM ").append(collection).append(IJOReferenceDestinationDefTable.TABLE_NAME)
                .append(" WHERE ").append(collection).append(IJOReferenceDestinationDefTable.TABLE_NAME).append(".")
                .append(IJOCommonReferenceDestinationDefTable.IDJOURNALISATION).append(" IN (SELECT ")
                .append(collection).append(IJOJournalisationDefTable.TABLE_NAME).append(".")
                .append(IJOCommonJournalisationDefTable.IDJOURNALISATION).append(" FROM  ").append(collection)
                .append(IJOJournalisationDefTable.TABLE_NAME).append(" WHERE ")
                .append(IJOCommonJournalisationDefTable.IDLOT).append(" = ").append(idLot);
        sql.append(")");
        try {
            s.createStatement();
            s.execute(sql.toString());
        } catch (Exception e) {
            JadeLogger.error(this, "Erreur pendant l'effacement du destination pour l'idLot " + idLot);
            e.printStackTrace();
        } finally {
            s.closeStatement();
        }
    }

    // /**
    // * @param transaction
    // * @param id
    // * @param string
    // */
    // public void effaceGroupe(BTransaction transaction, String idLot, String
    // collection) {
    // //On efface les grtoupes journaux
    // BStatement s = new BStatement(transaction);
    // StringBuffer sql = new StringBuffer();
    // StringBuffer sqlWhere = new StringBuffer();
    // sql.append("DELETE FROM ")
    // .append(collection)
    // .append("JOJPGJO WHERE ")
    // .append(collection)
    // .append("JOJPGJO.JGJOID IN (SELECT ")
    // .append(collection)
    // .append("JOJPJOU.JGJOID FROM ")
    // .append(collection)
    // .append("JOJPJOU WHERE JLOTID = ")
    // .append(idLot)
    // .append(" ) )");
    // try {
    // s.createStatement();
    // s.execute(sql.toString());
    // } catch (Exception e) {
    // JadeLogger.error("Erreur pendant l'effacement du groupe pour l'idLot "+idLot);
    // e.printStackTrace();
    // }
    // s.closeStatement();
    //
    // }
    /**
     * @param transaction
     * @param id
     * @param string
     */
    public void effaceReferenceProvenance(BTransaction transaction, String idLot, String collection) {
        // On efface les provenances journaux
        BStatement s = new BStatement(transaction);
        StringBuffer sql = new StringBuffer();
        StringBuffer sqlWhere = new StringBuffer();
        sql.append("DELETE FROM ").append(collection).append(IJOReferenceProvenanceDefTable.TABLE_NAME)
                .append(" WHERE ").append(collection).append(IJOReferenceProvenanceDefTable.TABLE_NAME).append(".")
                .append(IJOCommonReferenceProvenanceDefTable.IDJOURNALISATION).append(" IN (SELECT ")
                .append(collection).append(IJOJournalisationDefTable.TABLE_NAME).append(".")
                .append(IJOCommonJournalisationDefTable.IDJOURNALISATION).append(" FROM  ").append(collection)
                .append(IJOJournalisationDefTable.TABLE_NAME).append(" WHERE ")
                .append(IJOCommonJournalisationDefTable.IDLOT).append("= ").append(idLot).append(")");
        try {
            s.createStatement();
            s.execute(sql.toString());
        } catch (Exception e) {
            JadeLogger.error(this, "Erreur pendant l'effacement du reference provenance pour l'idLot " + idLot);
            e.printStackTrace();
        } finally {
            s.closeStatement();
        }
    }

    public String genererJournalisationFormule(LUJournalDataSource source, String csDocument, String categorie,
            String etapeSuivante, String editionManuelle, BSession session, BTransaction transaction) throws Exception {
        return genererJournalisationFormule(source, csDocument, categorie, "", etapeSuivante, editionManuelle, session,
                transaction);
    }

    public String genererJournalisationFormule(LUJournalDataSource source, String csDocument, String categorie,
            String idLot, String etapeSuivante, String editionManuelle, BSession session, BTransaction transaction)
            throws Exception {
        return genererJournalisationFormule(source, csDocument, categorie, idLot, etapeSuivante, editionManuelle,
                session, transaction);
    }

    public String genererJournalisationFormule(LUJournalDataSource source, String csDocument, String categorie,
            String idLot, String etapeSuivante, String editionManuelle, String dateCreation, BSession session,
            BTransaction transaction) throws Exception {
        LUJournalDefaulthandler journalHandler = new LUJournalDefaulthandler();
        // ajouter une nouvelle journalisation
        String idJournal;
        if (source.getIdJournalParent() == null) {
            idJournal = journalHandler.addFirstJournalisation(idLot, source.getDateRappel(), source.getLibelle(),
                    dateCreation, session, transaction);
        } else {
            idJournal = journalHandler.addNewJournalisation(idLot, source.getIdJournalParent(), source.getDateRappel(),
                    "", dateCreation, session, transaction);
        }

        int size = 4;
        if (!JadeStringUtil.isEmpty(etapeSuivante)) {
            size = 5;
        }

        FWIncrementation fwInc = new FWIncrementation();
        fwInc.setIdIncrement("JOJPCJO");
        fwInc.setSession(session);
        long incr = fwInc.increment(transaction, size, 3);

        // ajouter un complément journal pour le formattage
        journalHandler.addComplementJournal(incr + "", idJournal, JOConstantes.GR_CS_JO_FMT_ID,
                JOConstantes.CS_JO_FMT_FORMULE, session, transaction);
        incr++;
        // ajouter un complément journal pour le libellé de la nouvelle
        // journalisation
        journalHandler.addComplementJournal(incr + "", idJournal, ILEConstantes.CS_DEF_FORMULE_GROUPE, csDocument,
                session, transaction);
        incr++;
        // ajouter un complément journal pour la catégorie de la journalisation
        journalHandler.addComplementJournal(incr + "", idJournal, ILEConstantes.CS_CATEGORIE_GROUPE, categorie,
                session, transaction);
        incr++;
        if (!JadeStringUtil.isEmpty(etapeSuivante)) {
            // ajouter un complément journal pour l'étape suivante de la
            // journalisation
            journalHandler.addComplementJournal(incr + "", idJournal, ILEConstantes.CS_ETAPE_SUIVANTE_GROUPE,
                    etapeSuivante, session, transaction);
            incr++;
        }
        // ajouter un complément journal pour le mode de génération du document
        journalHandler.addComplementJournal(incr + "", idJournal, ILEConstantes.CS_EDITION_MANUELLE_GROUPE,
                editionManuelle, session, transaction);

        // créer les propriétés du journal créé
        journalHandler.addProvenance(idJournal, source, session, transaction);
        // Ajouter encore une référence destination au journal
        journalHandler.addReferenceDestination(session, transaction, idJournal, source.getCsTypeDest(),
                source.getIdCleDest());
        return idJournal;
    }

    public String genererJournalisationReception(String idJournal, String dateReception, BSession session,
            BTransaction transaction) throws Exception {
        boolean transactionCommit = false;
        try {
            LUJournalDefaulthandler journalHandler = new LUJournalDefaulthandler();
            LEEnvoiHandler envoiHandler = new LEEnvoiHandler();
            LEJournalHandler journalEnvoiHandler = new LEJournalHandler();

            // on recherche le dernier journal du groupe
            String idDernierJournal = envoiHandler.getDernierEnvoi(idJournal, session, null);

            // Si le dernier journal est une réception, plus besoin de la
            // regénérer
            if (!ILEConstantes.CS_DEF_FORMULE_RECEPTION.equals(journalEnvoiHandler.getCsDocument(idDernierJournal,
                    session, null))) {

                if (transaction == null) {
                    transactionCommit = true;
                    transaction = new BTransaction(session);
                    transaction.openTransaction();
                }

                FWIncrementation fwInc = new FWIncrementation();
                fwInc.setIdIncrement("JOJPCJO");
                fwInc.setSession(session);
                long incr = fwInc.increment(transaction, 3, 3);

                //
                String categorie = getCategorieByIdJournal(idDernierJournal, session, transaction);
                // ajouter une nouvelle journalisation
                String idReceptionJourn = journalHandler.addNewJournalisation(idDernierJournal, "", dateReception,
                        session, transaction);
                // ajouter un complément journal pour la catégorie
                journalHandler.addComplementJournal(incr + "", idReceptionJourn, ILEConstantes.CS_CATEGORIE_GROUPE,
                        categorie, session, transaction);
                incr++;
                // ajouter un complément journal pour le formattage
                journalHandler.addComplementJournal(incr + "", idReceptionJourn, JOConstantes.GR_CS_JO_FMT_ID,
                        JOConstantes.CS_JO_FMT_FORMULE_RECEPTION, session, transaction);
                incr++;// ajouter un complément journal pour le libellé de la
                // journalisation de la réception
                journalHandler.addComplementJournal(incr + "", idReceptionJourn, ILEConstantes.CS_DEF_FORMULE_GROUPE,
                        ILEConstantes.CS_DEF_FORMULE_RECEPTION, session, transaction);
                // ajouter les même propriété journal que le parent
                journalHandler.addProvenance(idReceptionJourn,
                        journalHandler.getProvenanceList(idDernierJournal, session, transaction), session, transaction);
                if (transactionCommit) {
                    if (!transaction.hasErrors()) {
                        transaction.commit();
                    } else {
                        transaction.rollback();
                    }
                }
                return idReceptionJourn;
            } else {
                return idDernierJournal;
            }
        } finally {
            if (transactionCommit && (transaction != null)) {
                transaction.closeTransaction();
            }
        }
    }

    /**
     * @param idJournal
     * @param session
     * @param transaction
     * @return
     */
    private String getCategorieByIdJournal(String idJournal, BSession session, BTransaction transaction)
            throws Exception {
        LUComplementJournalListViewBean cplJournList = new LUComplementJournalListViewBean();
        cplJournList.setSession(session);
        cplJournList.setForIdJournalisation(idJournal);
        cplJournList.setForCsTypeCodeSysteme(ILEConstantes.CS_CATEGORIE_GROUPE);
        cplJournList.find(transaction);
        if (cplJournList.size() > 0) {
            return ((LUComplementJournalViewBean) cplJournList.getFirstEntity()).getValeurCodeSysteme();
        } else {
            throw new Exception("Erreur : impossible de trouver une catégorie pour l'id journal =" + idJournal);
        }
    }

    public String getComplementJournal(String idJournal, String csType, BSession session, BTransaction transaction)
            throws Exception {
        LUComplementJournalListViewBean cplJournList = new LUComplementJournalListViewBean();
        cplJournList.setSession(session);
        cplJournList.setForIdJournalisation(idJournal);
        cplJournList.setForCsTypeCodeSysteme(csType);
        cplJournList.find(transaction);
        if (cplJournList.size() > 0) {
            return ((LUComplementJournalViewBean) cplJournList.getFirstEntity()).getValeurCodeSysteme();
        } else {
            return null;
            // throw new
            // Exception("Erreur : impossible de trouver un complément pour l'id journal ="+idJournal);
        }
    }

    public String getCsDocument(String idJournal, BSession session, BTransaction transaction) throws Exception {
        // rechercher le type de document relaif à l'id journal
        LUJournalDefaulthandler journalHandler = new LUJournalDefaulthandler();
        return journalHandler
                .getComplementJournal(idJournal, ILEConstantes.CS_DEF_FORMULE_GROUPE, session, transaction);
    }

    /**
     * @param journalId
     * @param session
     * @param transaction
     * @return
     */
    public String getDernierJournal(String journalId, BSession session, BTransaction transaction) throws Exception {
        LUJournalViewBean j = getJournal(journalId, session, transaction);
        // on recherche le dernier journal du groupe
        LUJournalListViewBean journalListe = new LUJournalListViewBean();
        journalListe.setSession(session);
        journalListe.setForIdGroupeJournal(j.getIdGroupeJournal());
        journalListe.wantComplementJournal(false);
        journalListe.wantGroupeJournal(false);
        journalListe.wantCallMethodAfter(false);
        journalListe.wantCallMethodAfterFind(false);
        journalListe.wantCallMethodBefore(false);
        // journalListe.wantCallMethodBeforeFind(false);
        journalListe.setOrderby(IJOCommonJournalisationDefTable.IDJOURNALISATION);
        journalListe.find(transaction);
        if (journalListe.size() > 0) {
            return ((LUJournalViewBean) journalListe.getEntity(journalListe.size() - 1)).getIdJournalisation();
        } else {
            throw new Exception("Erreur : Impossible de retrouver le dernier journal pour idJournal=" + journalId);
        }

    }

    /**
     * @param idJournal
     * @param string
     * @param session
     * @param transaction
     * @return
     */
    private String getDestinataire(String idJournal, String typeRefDest, BSession session, BTransaction transaction)
            throws Exception {
        LUReferenceDestinationListViewBean listeDest = new LUReferenceDestinationListViewBean();
        listeDest.setSession(session);
        listeDest.setForTypeReferenceDestination(typeRefDest);
        listeDest.setForIdJournalisation(idJournal);
        listeDest.find(transaction);
        if (listeDest.size() > 0) {
            return ((LUReferenceDestinationViewBean) listeDest.getFirstEntity()).getIdCleReferenceDestination();
        } else {
            throw new Exception("Erreur : aucune destination trouvée pour l'idjournal =" + idJournal
                    + " et type destination=" + typeRefDest);
        }
    }

    public String getEtapeSuivanteByIdJournal(String idJournal, BSession session, BTransaction transaction)
            throws Exception {
        LUComplementJournalListViewBean cplJournList = new LUComplementJournalListViewBean();
        cplJournList.setSession(session);
        cplJournList.setForIdJournalisation(idJournal);
        cplJournList.setForCsTypeCodeSysteme(ILEConstantes.CS_ETAPE_SUIVANTE_GROUPE);
        cplJournList.find(transaction);
        if (cplJournList.size() > 0) {
            return ((LUComplementJournalViewBean) cplJournList.getFirstEntity()).getValeurCodeSysteme();
        } else {
            return null;
            // throw new
            // Exception("Erreur : impossible de trouver une etape suivante pour l'id journal ="+idJournal);
        }
    }

    /**
     * @param journal
     * @return
     */
    private LUGroupeJournalViewBean getGroupeJournal(LUJournalViewBean journal) throws Exception {
        LUGroupeJournalViewBean groupe = new LUGroupeJournalViewBean();
        groupe.setSession(journal.getSession());
        groupe.setIdGroupeJournal(journal.getIdGroupeJournal());
        groupe.retrieve();
        if (groupe.isNew()) {
            throw new Exception("Erreur : le groupe avec l'îd :" + journal.getIdGroupeJournal() + " n'existe pas");
        }
        return groupe;
    }

    /**
     * @param _journalId
     * @param session
     * @param transaction
     * @return
     */
    public LUJournalViewBean getJournal(String _journalId, BSession session, BTransaction transaction) throws Exception {
        LUJournalDefaulthandler journalHandler = new LUJournalDefaulthandler();
        return journalHandler.getJournal(_journalId, session, transaction);
    }

    public LEParamEnvoiDataSource getParamsEnvoiDataSource(String idJournal, BSession session, BTransaction transaction)
            throws Exception {
        LEParamEnvoiDataSource params = new LEParamEnvoiDataSource();
        LUReferenceProvenanceListViewBean provList = new LUReferenceProvenanceListViewBean();
        provList.setSession(session);
        provList.setForIdJournalisation(idJournal);
        provList.find(transaction);
        // les paramètres de provenance seront les paramètres de la prochaine
        // génération
        for (int i = 0; i < provList.size(); i++) {
            LUReferenceProvenanceViewBean prov = (LUReferenceProvenanceViewBean) provList.getEntity(i);
            params.addParamEnvoi(prov.getTypeReferenceProvenance(), prov.getIdCleReferenceProvenance());
        }
        // ajouter comme paramètre l'id de l'envoi
        params.addParamEnvoi(ILEConstantes.CS_PARAM_GEN_ID_ENVOI_PRECEDENT, idJournal);
        // charger aussi le même destinataire
        params.addParamEnvoi(ILEConstantes.CS_PARAM_GEN_ID_TIERS_DESTINAIRE,
                getDestinataire(idJournal, JOConstantes.CS_JO_LIEN_TIERS_DESTINATAIRE, session, transaction));
        return params;
    }

    public void receptionDocument(String idJournal, BSession session, HttpServletRequest request) throws Exception {
        JADate current = null;
        if (!JadeStringUtil.isEmpty(request.getParameter("dateReception"))) {
            current = new JADate(JAUtil.padInteger(request.getParameter("dateReception"),
                    request.getParameter("dateReception").length()));
        } else {
            current = new JADate(JACalendar.todayJJsMMsAAAA());
        }
        genererJournalisationReception(idJournal, JACalendar.format(current), session, null);
    }

    /**
     * @param journalId
     * @param session
     * @param transaction
     */
    public void updateDate(String journalId, BSession session, String dateRappel, String dateReception)
            throws Exception {
        LUJournalViewBean journal = getJournal(journalId, session, null);
        LUGroupeJournalViewBean groupe = getGroupeJournal(journal);
        if (dateRappel.length() > 0) {
            groupe.setDateRappel(dateRappel);
        }
        if (dateReception.length() > 0) {
            groupe.setDateReception(dateReception);
        }
        groupe.update();
        // journal.retrieve();
        // journal.update();
    }
}
