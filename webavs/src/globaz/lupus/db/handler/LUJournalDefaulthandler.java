/*
 * Créé le 5 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.lupus.db.handler;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWIncrementation;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.journalisation.constantes.JOConstantes;
import globaz.lupus.db.data.LUJournalDataSource;
import globaz.lupus.db.journalisation.LUComplementJournalListViewBean;
import globaz.lupus.db.journalisation.LUComplementJournalViewBean;
import globaz.lupus.db.journalisation.LUGroupeJournalViewBean;
import globaz.lupus.db.journalisation.LUJournalViewBean;
import globaz.lupus.db.journalisation.LUReferenceDestinationViewBean;
import globaz.lupus.db.journalisation.LUReferenceProvenanceListViewBean;
import globaz.lupus.db.journalisation.LUReferenceProvenanceViewBean;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LUJournalDefaulthandler {
    /**
     * @param idJournal
     * @param type
     * @param valeur
     * @param session
     * @param transaction
     */
    public void addComplementJournal(String idComplementJournal, String idJournal, String type, String valeur,
            BSession session, BTransaction transaction) throws Exception {
        LUComplementJournalViewBean complJournal = new LUComplementJournalViewBean();
        complJournal.setIdComplementJournal(idComplementJournal);
        complJournal.setSession(session);
        complJournal.setIdJournalisation(idJournal);
        complJournal.setCsTypeCodeSysteme(type);
        complJournal.setValeurCodeSysteme(valeur);
        complJournal.add(transaction);
    }

    /**
     * @param string
     * @param string2
     * @param session
     * @param transaction
     * @return
     */
    public String addFirstJournalisation(String dateRappel, String libelle, BSession session, BTransaction transaction)
            throws Exception {
        return addFirstJournalisation("", dateRappel, libelle, session, transaction);
    }

    public String addFirstJournalisation(String idLot, String dateRappel, String libelle, BSession session,
            BTransaction transaction) throws Exception {
        return addFirstJournalisation(idLot, dateRappel, libelle, "", session, transaction);
    }

    public String addFirstJournalisation(String idLot, String dateRappel, String libelle, String dateCreation,
            BSession session, BTransaction transaction) throws Exception {
        String idGroupeJournal = addNewGroup(dateRappel, session, transaction);
        // ajouter une nouvelle journalisation dans le groupe précédemment créé
        return addJournalisationWithGroupe(idLot, idGroupeJournal, session, transaction, libelle, dateCreation);
    }

    /**
     * @param idLot
     * @param idGroupeJournal
     * @param session
     * @param transaction
     * @param libelle
     * @param dateCreation
     * @return
     */
    private String addJournalisationWithGroupe(String idLot, String idGroupeJournal, BSession session,
            BTransaction transaction, String libelle, String dateCreation) throws Exception {
        LUJournalViewBean nouvelJourn = new LUJournalViewBean();
        nouvelJourn.setSession(session);
        nouvelJourn.setIdGroupeJournal(idGroupeJournal);
        nouvelJourn.setIdLot(idLot);
        nouvelJourn.setDate(JAUtil.isStringEmpty(dateCreation) ? JACalendar.format(JACalendar.today()) : dateCreation);
        nouvelJourn.setCsTypeJournal(JOConstantes.CS_JO_JOURNALISATION);
        nouvelJourn.setIdUtilisateur(session.getUserId());
        nouvelJourn.setLibelle(libelle);
        nouvelJourn.setIdPrecedent("0");
        nouvelJourn.setIdInitial("0");
        nouvelJourn.add(transaction);
        return nouvelJourn.getIdJournalisation();
    }

    public String addNewGroup(BSession session, BTransaction transaction) throws Exception {
        LUGroupeJournalViewBean groupeJournal = new LUGroupeJournalViewBean();
        groupeJournal.setSession(session);
        groupeJournal.add(transaction);
        return groupeJournal.getIdGroupeJournal();
    }

    /**
     * @param dateRappel
     * @param session
     * @param transaction
     * @return
     */
    private String addNewGroup(String dateRappel, BSession session, BTransaction transaction) throws Exception {
        LUGroupeJournalViewBean groupeJournal = new LUGroupeJournalViewBean();
        groupeJournal.setSession(session);
        groupeJournal.setDateRappel(dateRappel);
        groupeJournal.add(transaction);
        return groupeJournal.getIdGroupeJournal();
    }

    public String addNewJournalisation(BSession session, BTransaction transaction) throws Exception {
        return addNewJournalisation(session, transaction, "", "");
    }

    public String addNewJournalisation(BSession session, BTransaction transaction, String libelle, String dateCreation)
            throws Exception {
        return addNewJournalisation("", session, transaction, libelle, dateCreation);
    }

    /**
     * @param jo
     * @param libelle
     * @param session
     * @param transaction
     */
    private LUJournalViewBean addNewJournalisation(LUJournalViewBean jo, String libelle, BSession session,
            BTransaction transaction) throws Exception {
        return addNewJournalisation("", jo, libelle, session, transaction);
    }

    public String addNewJournalisation(String idLot, BSession session, BTransaction transaction, String libelle,
            String dateCreation) throws Exception {
        // ajouter un nouveau groupe
        String idGroupeJournal = addNewGroup(session, transaction);
        // ajouter une nouvelle journalisation dans le groupe précédemment créé
        LUJournalViewBean nouvelJourn = addNewJournalisation(idLot, idGroupeJournal, dateCreation, session,
                transaction, libelle);
        return nouvelJourn.getIdJournalisation();
    }

    private LUJournalViewBean addNewJournalisation(String idLot, LUJournalViewBean jo, String libelle,
            BSession session, BTransaction transaction) throws Exception {
        return addNewJournalisation(idLot, jo, libelle, "", session, transaction);
    }

    private LUJournalViewBean addNewJournalisation(String idLot, LUJournalViewBean jo, String libelle,
            String dateCreation, BSession session, BTransaction transaction) throws Exception {
        LUJournalViewBean nouvelJourn = new LUJournalViewBean();
        nouvelJourn.setSession(session);
        nouvelJourn.setIdGroupeJournal(jo.getIdGroupeJournal());
        nouvelJourn.setIdLot(idLot);
        nouvelJourn.setDate(JAUtil.isStringEmpty(dateCreation) ? JACalendar.format(JACalendar.today()) : dateCreation);
        nouvelJourn.setCsTypeJournal(JOConstantes.CS_JO_JOURNALISATION);
        nouvelJourn.setIdUtilisateur(session.getUserId());
        nouvelJourn.setLibelle(libelle);
        nouvelJourn.setIdPrecedent(jo.getIdJournalisation());
        if (jo.isJournalInitial()) {
            nouvelJourn.setIdInitial(jo.getIdJournalisation());
            // nouvelJourn.setIdSuivant(jo.getIdJournalisation());
        } else {
            nouvelJourn.setIdInitial(jo.getIdInitial());
            // nouvelJourn.setIdSuivant(jo.getIdInitial());
        }
        nouvelJourn.add(transaction);
        return nouvelJourn;
    }

    public LUJournalViewBean addNewJournalisation(String idLot, String idGroupeJournal, BSession session,
            BTransaction transaction, String libelle) throws Exception {
        return addNewJournalisation(idLot, idGroupeJournal, "", session, transaction, libelle);
    }

    public String addNewJournalisation(String idJournalParent, String dateRappel, String dateReception,
            BSession session, BTransaction transaction) throws Exception {
        return addNewJournalisation("", idJournalParent, dateRappel, dateReception, session, transaction);
    }

    public LUJournalViewBean addNewJournalisation(String idLot, String idGroupeJournal, String dateCreation,
            BSession session, BTransaction transaction, String libelle) throws Exception {
        // ajouter une nouvelle journalisation dans le groupe précédemment créé
        LUJournalViewBean nouvelJourn = new LUJournalViewBean();
        nouvelJourn.setSession(session);
        nouvelJourn.setIdGroupeJournal(idGroupeJournal);
        nouvelJourn.setIdLot(idLot);
        nouvelJourn.setDate(JAUtil.isStringEmpty(dateCreation) ? JACalendar.format(JACalendar.today()) : dateCreation);
        nouvelJourn.setCsTypeJournal(JOConstantes.CS_JO_JOURNALISATION);
        nouvelJourn.setIdUtilisateur(session.getUserId());
        nouvelJourn.setLibelle(libelle);
        nouvelJourn.add(transaction);
        return nouvelJourn;
    }

    /**
     * @param string
     * @param string2
     * @param string3
     * @param session
     * @param transaction
     * @return
     */
    public String addNewJournalisation(String idLot, String idJournalParent, String dateRappel, String dateReception,
            BSession session, BTransaction transaction) throws Exception {
        return addNewJournalisation(idLot, idJournalParent, dateRappel, dateReception, "", session, transaction);
    }

    public String addNewJournalisation(String idLot, String idJournalParent, String dateRappel, String dateReception,
            String dateCreation, BSession session, BTransaction transaction) throws Exception {
        LUJournalViewBean ancienneJo = getJournal(idJournalParent, session, transaction);
        // update la date de rappel du groupe
        updateGroupeJournal(dateRappel, dateReception, session, transaction, ancienneJo);
        // ajoute une nouvelle journalisation en fonction d'une journalisation
        // pré-insérée
        LUJournalViewBean nouvelleJo = addNewJournalisation(idLot, ancienneJo, ancienneJo.getLibelle(), dateCreation,
                session, transaction);
        // met a jour la journalisation parente
        updateLienJournalisation(ancienneJo, nouvelleJo, session, transaction);
        return nouvelleJo.getIdJournalisation();
    }

    /**
     * @param idJournal
     * @param provenance
     * @param session
     * @param transaction
     */
    public void addProvenance(String idJournal, LUJournalDataSource provenanceList, BSession session,
            BTransaction transaction) throws Exception {
        if (provenanceList != null) {
            LUReferenceProvenanceViewBean refProv = new LUReferenceProvenanceViewBean();
            refProv.setSession(session);
            refProv.setIdJournalisation(idJournal);
            LUJournalDataSource.provenance p;
            int size = provenanceList.size();
            FWIncrementation fwInc = new FWIncrementation();
            fwInc.setIdIncrement("JOJPREP");
            fwInc.setSession(session);

            long incr = fwInc.increment(transaction, size, 3);
            for (int i = 0; i < size; i++) {
                p = provenanceList.getProvenance(i);
                refProv.setIdReferenceProvenance(incr + i + "");
                refProv.setTypeReferenceProvenance(p.getCsType());
                refProv.setIdCleReferenceProvenance(p.getValeur());
                refProv.add(transaction);
            }
        }
    }

    /**
     * @param idJournal
     * @param idCle
     * @param valeur
     * @param session
     * @param transaction
     */
    public void addProvenance(String idJournal, String type, String valeur, BSession session, BTransaction transaction)
            throws Exception {
        LUReferenceProvenanceViewBean refProv = new LUReferenceProvenanceViewBean();
        refProv.setSession(session);
        refProv.setIdJournalisation(idJournal);
        refProv.setIdCleReferenceProvenance(valeur);
        refProv.setTypeReferenceProvenance(type);
        refProv.add(transaction);
    }

    public String addReferenceDestination(BSession session, BTransaction transaction, String idJournal,
            String typeRefDest, String idCleRefDest) throws Exception {
        LUReferenceDestinationViewBean refDest = new LUReferenceDestinationViewBean();
        refDest.setSession(session);
        refDest.setIdJournalisation(idJournal);
        refDest.setIdCleReferenceDestination(idCleRefDest);
        refDest.setTypeReferenceDestination(typeRefDest);
        refDest.add(transaction);
        return refDest.getIdReferenceDestination();
    }

    public String getComplementJournal(String idJournal, String csType, BSession session, BTransaction transaction)
            throws Exception {
        LUComplementJournalListViewBean cplJournListe = new LUComplementJournalListViewBean();
        cplJournListe.setSession(session);
        cplJournListe.setForIdJournalisation(idJournal);
        cplJournListe.setForCsTypeCodeSysteme(csType);
        cplJournListe.find(transaction);
        if (cplJournListe.size() > 0) {
            return ((LUComplementJournalViewBean) cplJournListe.getFirstEntity()).getValeurCodeSysteme();
        } else {
            throw new Exception("Impossible de trouver un complément journal pour idJournal=" + idJournal
                    + "type code système=" + csType);
        }
    }

    public LUGroupeJournalViewBean getGroupeJournal(String idGroupeJournal, BSession session, BTransaction transaction)
            throws Exception {
        LUGroupeJournalViewBean groupe = new LUGroupeJournalViewBean();
        groupe.setSession(session);
        groupe.setIdGroupeJournal(idGroupeJournal);
        groupe.retrieve(transaction);
        if (groupe.isNew()) {
            throw new Exception("Erreur: le groupe journal id=" + idGroupeJournal
                    + "n'existe pas, impossible de retrouver ce groupe");
        }
        return groupe;
    }

    public LUJournalViewBean getJournal(String idJournal, BSession session, BTransaction transaction) throws Exception {
        LUJournalViewBean jo = new LUJournalViewBean();
        jo.setSession(session);
        jo.setIdJournalisation(idJournal);
        jo.retrieve(transaction);
        if (jo.isNew()) {
            throw new Exception("Erreur: la journalisation id=" + idJournal + "n'existe pas, retrouver ce journal");
        }
        return jo;
    }

    public LUJournalDataSource getProvenanceList(String idJournal, BSession session, BTransaction transaction)
            throws Exception {
        LUJournalDataSource res = new LUJournalDataSource();
        LUReferenceProvenanceListViewBean provenanceList = new LUReferenceProvenanceListViewBean();
        provenanceList.setSession(session);
        provenanceList.setForIdJournalisation(idJournal);
        provenanceList.find(transaction);
        for (int i = 0; i < provenanceList.size(); i++) {
            LUReferenceProvenanceViewBean provVb = (LUReferenceProvenanceViewBean) provenanceList.getEntity(i);
            res.addProvenance(provVb.getTypeReferenceProvenance(), provVb.getIdCleReferenceProvenance());
        }
        return res;
    }

    public void updateGroupeJournal(String dateRappel, String dateReception, BSession session,
            BTransaction transaction, LUJournalViewBean jo) throws Exception {
        // mettre a jour la date de rappel du groupe
        LUGroupeJournalViewBean groupe = new LUGroupeJournalViewBean();
        groupe.setSession(session);
        groupe.setIdGroupeJournal(jo.getIdGroupeJournal());
        groupe.retrieve(transaction);
        if (groupe.isNew()) {
            throw new Exception("Erreur: le groupe journal id=" + jo.getIdGroupeJournal()
                    + "n'existe pas, impossible de générer l'étape suivante");
        }
        groupe.setDateRappel(dateRappel);
        groupe.setDateReception(dateReception);
        groupe.update(transaction);
    }

    /**
     * @param bean
     * @param string
     */
    public void updateIdPrecedent(LUJournalViewBean jo, String idPrecedent, BTransaction transaction) throws Exception {
        jo.retrieve(transaction);
        jo.setIdPrecedent(idPrecedent);
        jo.update(transaction);
    }

    /**
     * @param ancienneJo
     * @param string
     */
    private void updateIdSuivant(LUJournalViewBean jo, String idSuivant, BTransaction transaction) throws Exception {
        jo.retrieve(transaction);
        jo.setIdSuivant(idSuivant);
        jo.update(transaction);
    }

    /**
     * @param ancienneJo
     * @param nouvelleJo
     */
    private void updateLienJournalisation(LUJournalViewBean ancienneJo, LUJournalViewBean nouvelleJo, BSession session,
            BTransaction transaction) throws Exception {
        // updateIdPrecedent(getJournal(ancienneJo.isJournalInitial()?ancienneJo.getIdJournalisation():ancienneJo.getIdInitial(),session,transaction),nouvelleJo.getIdJournalisation(),transaction);
        updateIdSuivant(ancienneJo, nouvelleJo.getIdJournalisation(), transaction);
    }
}
