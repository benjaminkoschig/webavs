package globaz.cygnus.services.genererDecision;

import globaz.cygnus.db.decisions.RFDecision;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.demandes.RFDemandeManager;
import globaz.cygnus.db.qds.RFPeriodeValiditeQdPrincipale;
import globaz.cygnus.db.qds.RFPeriodeValiditeQdPrincipaleManager;
import globaz.cygnus.db.qds.RFQdHistoriquePeriodeValiditeQdPrincipaleJointSelf;
import globaz.cygnus.db.qds.RFQdHistoriquePeriodeValiditeQdPrincipaleJointSelfManager;
import globaz.cygnus.services.validerDecision.RFDecisionDocumentData;
import globaz.cygnus.services.validerDecision.RFDemandeValidationData;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class RFGenererDecisionRestitutionService {

    String dateDebut = null;
    String dateFin = null;
    List<String> listeDateDebutQds = null;
    List<String> listeDateFinQds = null;

    private void addRestit(List<RFDecisionDocumentData> decisionArrayAvecRestit,
            Map<String, RFDecisionDocumentData> mapDecisionsRestitution, String idTiers) {
        if (mapDecisionsRestitution.containsKey(idTiers)) {
            decisionArrayAvecRestit.add(mapDecisionsRestitution.get(idTiers));
        }
    }

    private List<RFDecisionDocumentData> buildDecisionsAvecRestitList(ArrayList<RFDecisionDocumentData> decisionArray,
            Map<String, RFDecisionDocumentData> mapDecisionsRestitution) {

        List<RFDecisionDocumentData> decisionArrayAvecRestit = new ArrayList<RFDecisionDocumentData>();
        String idTiersCourant = decisionArray.get(0).getIdTiers();
        boolean isFirstIter = true;
        for (RFDecisionDocumentData decisionCourante : decisionArray) {
            if (isFirstIter) {
                isFirstIter = false;
                addRestit(decisionArrayAvecRestit, mapDecisionsRestitution, decisionCourante.getIdTiers());
            }

            if (!idTiersCourant.equals(decisionCourante.getIdTiers())) {
                addRestit(decisionArrayAvecRestit, mapDecisionsRestitution, decisionCourante.getIdTiers());
            }

            idTiersCourant = decisionCourante.getIdTiers();
            decisionArrayAvecRestit.add(decisionCourante);
        }

        return decisionArrayAvecRestit;
    }

    /**
     * Methode pour parcourir les d�cisions et leurs demandes, et trouver si il y en a qui sont corrig�es
     */
    public List<RFDecisionDocumentData> createAndAddDecisionRestitution(ArrayList<RFDecisionDocumentData> decisionArray) {

        Map<String, RFDecisionDocumentData> mapDecisionsRestitution = new HashMap<String, RFDecisionDocumentData>();

        // Parcours de chaque d�cision existante
        for (RFDecisionDocumentData decision : decisionArray) {
            // Parcours de chaque demandes d'une d�cision
            for (RFDemandeValidationData demande : decision.getDecisionDemande()) {
                // Si demande parent existe, on ajoute la d�cision dans une map de restitution
                if (!JadeStringUtil.isBlankOrZero(demande.getIdDemandeParent())) {

                    // Si une demande corrig�e appartient � un idTiers d�j� pr�sent, on l'ajoute
                    if (mapDecisionsRestitution.containsKey(decision.getIdTiers())) {
                        mapDecisionsRestitution.get(decision.getIdTiers()).getDecisionDemande().add(demande);
                    }
                    // Sinon, on cr�e une nouvelle d�cision
                    else {
                        ArrayList<RFDemandeValidationData> listDemande = new ArrayList<RFDemandeValidationData>();
                        listDemande.add(demande);

                        RFDecisionDocumentData newDecisionRestit = new RFDecisionDocumentData("",
                                decision.getIdTiers(), decision.getGestionnaire(),
                                decision.getDateDecision_JourMoisAnnee(), decision.getAnneeQD(), listDemande,
                                decision.getTexteRemarque(), decision.getTexteAnnexe(), decision.getCopieDecision(),
                                decision.getIsDecompteRetour(), decision.getIsBulletinVersement(),
                                decision.getIsBordereauAccompagnement(), decision.getExcedentRevenu(),
                                decision.getDepassementQD(), decision.getMontantTotal(), decision.getTypePaiement(),
                                decision.getDateDebutRetro(), decision.getDateFinRetro(),
                                decision.getMontantCourantPartieRetroactive(),
                                decision.getMontantCourantPartieFuture(), "", decision.getGenrePrestation(),
                                decision.getIdTypeSoin(), decision.getReferencePaiement(),
                                decision.getMontantARembourserParLeDsas(), decision.getIdTiersAdressePaiement(),
                                decision.getIdQdPrincipale(), decision.getIsPhraseIncitationDepot(),
                                decision.getIsPhraseRetourBV(), decision.getDateDecision_JourMoisAnnee());
                        // Insert compl�ment d'info
                        newDecisionRestit.setIsRestitution(true);
                        newDecisionRestit.setAdresse(decision.getAdresse());
                        newDecisionRestit.setLangueTiers(decision.getLangueTiers());

                        mapDecisionsRestitution.put(decision.getIdTiers(), newDecisionRestit);

                    }
                }
            }
        }

        return buildDecisionsAvecRestitList(decisionArray, mapDecisionsRestitution);
    }

    public final String getDateDebut() {
        return dateDebut;
    }

    public final String getDateFin() {
        return dateFin;
    }

    /**
     * Methode qui retourne la premi�re date de d�but & la derni�re date de fin dans une String s�par�e par "&"
     * 
     * @return String dateDebut&dateFin
     */
    public void getDatesTries() throws Exception {
        try {
            // Tri de la liste des dates de d�but des Qds
            int nbrDatesDebutQds = listeDateDebutQds.size();
            TreeSet<String> triDatesDebutQds = new TreeSet<String>();
            for (int i = 0; i < nbrDatesDebutQds; i++) {
                triDatesDebutQds.add(listeDateDebutQds.get(i).toString());
            }

            // Tri de la liste des date de fin des Qds
            int nbrDatesFinQds = listeDateFinQds.size();
            TreeSet<String> triDatesFinQds = new TreeSet<String>();
            for (int i = 0; i < nbrDatesFinQds; i++) {
                triDatesFinQds.add(listeDateFinQds.get(i).toString());
            }

            // Set les dates retourn�es dans les variables
            dateDebut = triDatesDebutQds.first();
            dateFin = triDatesFinQds.last();

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e
                    + ": Erreur dans le tri des dates / RFGenererDecisionRestitutionService:getDatesTries");
        }
    }

    /**
     * Methode qui converti et retourne le genre de prestation
     * 
     * @param genrePrestation
     * @param langueTiers
     * @return
     * @throws Exception
     */
    public String getGenrePrestation(String genrePrestation, String langueTiers) throws Exception {
        String typeAssurance = null;
        try {
            if (genrePrestation.equals("64027003")) {
                if (langueTiers == "DE") {
                    typeAssurance = "IV";
                } else {
                    typeAssurance = "AI";
                }
            } else {
                if (langueTiers == "DE") {
                    typeAssurance = "AHV";
                } else {
                    typeAssurance = "AVS";
                }
            }
            return typeAssurance;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(
                    "Erreur dans la conversion du genre de prestation / RFGenererDecisionRestitutionService:getGenrePrestation");
        }
    }

    public void getHistoriquePeriodeValidite(BSession session, RFDemande demande) throws Exception {

        // Si aucune periode de validite dans la QD, recherche dans son historique
        RFQdHistoriquePeriodeValiditeQdPrincipaleJointSelfManager rfQdHistPeriodeValidQdPrinc = new RFQdHistoriquePeriodeValiditeQdPrincipaleJointSelfManager();
        rfQdHistPeriodeValidQdPrinc.setSession(session);
        rfQdHistPeriodeValidQdPrinc.setForIdQd(demande.getIdQdPrincipale());
        rfQdHistPeriodeValidQdPrinc.changeManagerSize(0);
        rfQdHistPeriodeValidQdPrinc.find();

        Iterator<RFQdHistoriquePeriodeValiditeQdPrincipaleJointSelf> itrQdHistPeriodeValidQdPrinc = rfQdHistPeriodeValidQdPrinc
                .iterator();

        while (itrQdHistPeriodeValidQdPrinc.hasNext()) {
            RFQdHistoriquePeriodeValiditeQdPrincipaleJointSelf historiquePeriodeValidite = itrQdHistPeriodeValidQdPrinc
                    .next();

            if (!JadeStringUtil.isBlankOrZero(historiquePeriodeValidite.getDateDebut())) {
                if (!listeDateDebutQds.contains(historiquePeriodeValidite.getDateDebut())) {
                    listeDateDebutQds.add(new String(historiquePeriodeValidite.getDateDebut()));
                }
            }
            if (!JadeStringUtil.isBlankOrZero(historiquePeriodeValidite.getDateFin())) {
                if (!listeDateDebutQds.contains(historiquePeriodeValidite.getDateFin())) {
                    listeDateFinQds.add(new String(historiquePeriodeValidite.getDateFin()));
                }
            }

        }

    }

    /**
     * Methode qui retourne une liste des nouvelles d�cisions. Traite les idDemande des demandes pr�sentes dans
     * decisiondocument
     * 
     * @param decisionDocument
     * @param session
     * @return
     */
    public ArrayList<RFDecision> getListeDecisionsDues(RFDecisionDocumentData decisionDocument, BSession session) {

        try {
            ArrayList<RFDecision> listeDecisionsDues = new ArrayList<RFDecision>();

            for (RFDemandeValidationData dataDemande : decisionDocument.getDecisionDemande()) {

                if (!JadeStringUtil.isEmpty(dataDemande.getIdDemande())) {
                    // Recherche de la demande initiale
                    RFDemande rfDemande = new RFDemande();
                    rfDemande.setSession(session);
                    rfDemande.setIdDemande(dataDemande.getIdDemande());
                    rfDemande.retrieve();

                    // Recherche de la d�cision li�e � la demande
                    if (!rfDemande.isNew()) {
                        RFDecision rfDecision = new RFDecision();
                        rfDecision.setSession(session);
                        rfDecision.setIdDecision(rfDemande.getIdDecision());
                        rfDecision.retrieve();

                        // Insertion de la d�cision dans la liste
                        if (!rfDecision.isNew()) {
                            if (!listeDecisionsDues.contains(rfDecision)) {
                                listeDecisionsDues.add(rfDecision);
                            }
                        }
                    }
                }
            }

            return listeDecisionsDues;

        } catch (Exception e) {
            throw new IllegalArgumentException(
                    e
                            + " : RFGenererDecisionRestitutionService_getNouvellesDecisions - Erreur dans le chargement des d�cisions dues");
        }
    }

    /**
     * Methode qui retourne une liste des anciennes d�cisions. Traite les idDemandeParent des demandes pr�sentes dans
     * decisiondocument
     * 
     * @param decisionDocument
     * @param session
     * @return
     */
    public ArrayList<RFDecision> getListeDecisionsVersees(RFDecisionDocumentData decisionDocument, BSession session) {
        try {
            ArrayList<RFDecision> listeDecisionsVersees = new ArrayList<RFDecision>();

            for (RFDemandeValidationData dataDemande : decisionDocument.getDecisionDemande()) {
                if (!JadeStringUtil.isEmpty(dataDemande.getIdDemandeParent())) {

                    // Recherche de la demande initiale
                    RFDemande rfDemande = new RFDemande();
                    rfDemande.setSession(session);
                    rfDemande.setIdDemande(dataDemande.getIdDemandeParent());
                    rfDemande.retrieve();

                    // Recherche de la d�cision li�e � la demande
                    if (!rfDemande.isNew()) {
                        RFDecision rfDecision = new RFDecision();
                        rfDecision.setSession(session);
                        rfDecision.setIdDecision(rfDemande.getIdDecision());
                        rfDecision.retrieve();

                        // Insertion de la d�cision dans la liste
                        if (!rfDecision.isNew()) {
                            if (!listeDecisionsVersees.contains(rfDecision)) {
                                listeDecisionsVersees.add(rfDecision);
                            }
                        }
                    }
                }
            }

            return listeDecisionsVersees;

        } catch (Exception e) {
            throw new IllegalArgumentException(
                    e
                            + " : RFGenererDecisionRestitutionService_getListeDecisionsVersees - Erreur dans le chargement des d�cisions vers�es.");
        }
    }

    /**
     * Methode qui va parcourir chaque dataDemande d'une d�cision et rechercher le d�tail de la RFDemande
     * 
     * @param decisionDocument
     * @param idDecisionCourante
     * @param session
     * @return
     */
    public ArrayList<RFDemande> getListeDemandesDues(RFDecisionDocumentData decisionDocument,
            String idDecisionCourante, BSession session) {
        try {
            ArrayList<RFDemande> listeDemandesDues = new ArrayList<RFDemande>();

            for (RFDemandeValidationData demandeData : decisionDocument.getDecisionDemande()) {

                // Recherche de la demande initiale
                if (!JadeStringUtil.isEmpty(demandeData.getIdDemande())) {
                    RFDemande rfDemande = new RFDemande();
                    rfDemande.setSession(session);
                    rfDemande.setIdDemande(demandeData.getIdDemande());
                    rfDemande.retrieve();

                    // Insertion de la demande, si elle correspond � la d�cision en cours de traitement
                    if (!rfDemande.isNew()) {
                        if (rfDemande.getIdDecision().equals(idDecisionCourante)) {
                            if (!listeDemandesDues.contains(rfDemande)) {
                                listeDemandesDues.add(rfDemande);
                            }
                        }
                    }
                }
            }

            return listeDemandesDues;

        } catch (Exception e) {
            throw new IllegalArgumentException(
                    e
                            + " : RFGenererDecisionRestitutionService_getNouvellesDemandes - Erreur dans la recherche de nouvelles demandes");
        }
    }

    /**
     * Methode qui retourne toutes les demandes li�es � la QD pass� en param�tre
     * 
     * @param idQdPrincipale
     * @param session
     * @return
     * @throws Exception
     */
    public Iterator getListeDemandesQd(String idQdPrincipale, BSession session) throws Exception {
        // R�cup�ration de toutes les demandes appartenant � la Qd
        RFDemandeManager demandeMgr = new RFDemandeManager();
        demandeMgr.setSession(session);
        demandeMgr.setForIdQdPrincipale(idQdPrincipale);
        demandeMgr.find();
        Iterator demandeItr = demandeMgr.iterator();

        return demandeItr;
    }

    /**
     * Methode qui retourne une liste des demandes parent li�es � la d�cision en traitement
     * 
     * @param decisionDocument
     * @param idDecisionCourante
     * @param session
     * @return
     */
    public ArrayList<RFDemande> getListeDemandesVersees(RFDecisionDocumentData decisionDocument,
            String idDecisionCourante, BSession session) {
        try {
            ArrayList<RFDemande> listeDemandesVersees = new ArrayList<RFDemande>();

            for (RFDemandeValidationData demandeData : decisionDocument.getDecisionDemande()) {

                // Recherche de la demande parent
                if (!JadeStringUtil.isEmpty(demandeData.getIdDemandeParent())) {
                    RFDemande rfDemande = new RFDemande();
                    rfDemande.setSession(session);
                    rfDemande.setIdDemande(demandeData.getIdDemandeParent());
                    rfDemande.retrieve();

                    // Insertion de la demande si elle correspond � la d�cision en cours de traitement
                    if (!rfDemande.isNew()) {
                        if (rfDemande.getIdDecision().equals(idDecisionCourante)) {
                            if (!listeDemandesVersees.contains(rfDemande)) {
                                listeDemandesVersees.add(rfDemande);
                            }
                        }
                    }
                }
            }

            return listeDemandesVersees;
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    e
                            + " : RFGenererDecisionRestitutionService_getNouvellesDemandes - Erreur dans la recherche de nouvelles demandes");
        }
    }

    /**
     * Methode pour retourner une liste des dates de d�but et des dates de fin
     * 
     * @throws Exception
     */
    public void getListePeriodesQdEtDemande(BSession session, Iterator demandeItr, Iterator periodesValiditesQdItr)
            throws Exception {
        // V�rifie si les liste sont d�j� instanci�s
        if (listeDateDebutQds == null) {
            listeDateDebutQds = new ArrayList<String>();
        }
        if (listeDateFinQds == null) {
            listeDateFinQds = new ArrayList<String>();
        }

        // Parcour chaque demande
        while (demandeItr.hasNext()) {

            RFDemande demande = (RFDemande) demandeItr.next();

            if (periodesValiditesQdItr.hasNext()) {
                while (periodesValiditesQdItr.hasNext()) {
                    String[] periodeValidite = new String[3];

                    RFPeriodeValiditeQdPrincipale rfPeriodeValiditeQdPrincipale = (RFPeriodeValiditeQdPrincipale) periodesValiditesQdItr
                            .next();

                    periodeValidite[0] = rfPeriodeValiditeQdPrincipale.getIdPeriodeValidite();
                    periodeValidite[1] = rfPeriodeValiditeQdPrincipale.getDateDebut();
                    periodeValidite[2] = rfPeriodeValiditeQdPrincipale.getDateFin();

                    JACalendar cal = new JACalendarGregorian();

                    // R�cup�ration de la date de d�but de traitement de la demande, si vide, r�cup�ration de la
                    // date de
                    // la
                    // facture
                    JADate dateDebutDemandeJd = new JADate();
                    if (!JadeStringUtil.isBlankOrZero(demande.getDateDebutTraitement())) {
                        dateDebutDemandeJd = new JADate(demande.getDateDebutTraitement());
                    } else {
                        dateDebutDemandeJd = new JADate(demande.getDateFacture());
                    }

                    // V�rifie si date de fin existe, sinon date de fin �gal au 31.12 de l'ann�e de d�but de la Qd
                    if (JadeStringUtil.isBlankOrZero(periodeValidite[2])) {
                        periodeValidite[2] = "31.12." + JADate.getYear(periodeValidite[1]);
                    }

                    // Retourne date au format jj.mm.aaaa
                    JADate dateDebutPeriodeQdJd = new JADate(periodeValidite[1]);
                    JADate dateFinPeriodeQdJd = new JADate(periodeValidite[2]);

                    // Contr�le si la Qd concerne la p�riode de la demande ou la m�me ann�e
                    if (((cal.compare(dateDebutPeriodeQdJd, dateDebutDemandeJd) == JACalendar.COMPARE_EQUALS) || (cal
                            .compare(dateDebutPeriodeQdJd, dateDebutDemandeJd) == JACalendar.COMPARE_FIRSTLOWER))
                            && ((cal.compare(dateFinPeriodeQdJd, dateDebutDemandeJd) == JACalendar.COMPARE_EQUALS) || (cal
                                    .compare(dateFinPeriodeQdJd, dateDebutDemandeJd) == JACalendar.COMPARE_FIRSTUPPER))) {

                        // Ajout unique dans la liste, chaque date trouv�e
                        if (!listeDateDebutQds.contains(periodeValidite[1])) {
                            listeDateDebutQds.add(new String(periodeValidite[1]));
                        }
                        if (!listeDateFinQds.contains(periodeValidite[2])) {
                            listeDateFinQds.add(new String(periodeValidite[2]));
                        }
                    } else {
                        getHistoriquePeriodeValidite(session, demande);
                    }
                }
            } else {
                // Si aucune periode de validite dans la QD, recherche dans son historique
                getHistoriquePeriodeValidite(session, demande);
            }
        }
    }

    /**
     * Methode qui va rechercher les p�riodes de validit�s pour chaque Qd pass�e en param�tre, et remplir les listes
     * concern�s.
     * 
     * @param idQdPrincipale
     * @param session
     * @throws Exception
     */
    private void getPeriodesDemandes(String idQdPrincipale, BSession session) throws Exception {

        try {

            // Recup�ration des demandes li�es � la QD
            Iterator demandeItr = getListeDemandesQd(idQdPrincipale, session);

            // Recuperation des p�riodes de validit�s de la QD
            Iterator<RFPeriodeValiditeQdPrincipale> periodesValiditesQdItr = getPeriodesValiditesQd(idQdPrincipale,
                    session);

            // Comparaison des dates de la demande et des dates des p�riodes de la Qd
            getListePeriodesQdEtDemande(session, demandeItr, periodesValiditesQdItr);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e + ": Erreur dans la recherche des periodes de validit� de la QD n�: "
                    + idQdPrincipale + " / RFGenererDecisionRestitutionService:getPeriodesDemandes");
        }
    }

    /**
     * Methode qui retourne les p�riodes de validit�s de la QD
     * 
     * @param idQdPrincipale
     * @param session
     * @return
     * @throws Exception
     */
    private Iterator getPeriodesValiditesQd(String idQdPrincipale, BSession session) throws Exception {
        RFPeriodeValiditeQdPrincipaleManager rfPeriodeValiditeQdPrincipaleMgr = new RFPeriodeValiditeQdPrincipaleManager();
        rfPeriodeValiditeQdPrincipaleMgr.setSession(session);
        rfPeriodeValiditeQdPrincipaleMgr.setForIdQd(idQdPrincipale);
        rfPeriodeValiditeQdPrincipaleMgr.setForDerniereVersion(true);
        rfPeriodeValiditeQdPrincipaleMgr.setOrderByDateDebutAsc(true);
        rfPeriodeValiditeQdPrincipaleMgr.changeManagerSize(0);
        rfPeriodeValiditeQdPrincipaleMgr.find();

        return rfPeriodeValiditeQdPrincipaleMgr.iterator();
    }

    /**
     * Methode pour retourner la date de d�but de la Qd la plus ant�rieur sur l'ensemble des Qds.
     * 
     * @param Map
     * @param session
     * 
     * @return String dateDebutEtDateFinPrestation
     */
    public void loadDateDebutQds(RFDecisionDocumentData decisionDocument, BSession session) throws Exception {

        try {

            List<String> listeQdsPrincipales = new ArrayList<String>();

            // R�cup�ration des Qd de chaque nouvelle demande
            for (RFDemandeValidationData demande : decisionDocument.getDecisionDemande()) {
                // Ajout de l'�l�ment de la demande dans la liste des Qds
                if (!listeQdsPrincipales.contains(demande.getIdQdPrincipale())) {
                    listeQdsPrincipales.add(demande.getIdQdPrincipale());
                }
            }

            // Parcours la liste des Qds
            int nbrQds = listeQdsPrincipales.size();
            for (int itrQd = 0; itrQd < nbrQds; itrQd++) {
                String idQdPrincipale = listeQdsPrincipales.get(itrQd).toString();

                // Appel d'une methode qui va remplir les listes "dateDebutQd" et "DateFinQd"
                getPeriodesDemandes(idQdPrincipale, session);
            }

            // Appel de la methode qui va trier les dates re�ues dans les listes respectives
            getDatesTries();

            // Suppression des �l�ments de la liste
            listeDateDebutQds.clear();
            listeDateFinQds.clear();

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e
                    + ": Erreur dans le tri des dates / RFGenererDecisionRestitutionService:getDateDebutEtDateFinQds");
        }
    }

    /**
     * Methode pour retourner la date de fin de la Qd la plus r�cente sur l'ensemble des Qds.
     * 
     * @param Map
     * @param session
     * 
     * @return String dateDebutEtDateFinPrestation
     */
    public void loadDateFinQds(RFDecisionDocumentData decisionDocument, BSession session) throws Exception {

        try {

            List<String> listeQdsPrincipales = new ArrayList<String>();

            // R�cup�ration des Qd de chaque nouvelle demande
            for (RFDemandeValidationData demande : decisionDocument.getDecisionDemande()) {

                if (!JadeStringUtil.isEmpty(demande.getIdDemandeParent())) {
                    RFDemande ancienneDemande = new RFDemande();
                    ancienneDemande.setSession(session);
                    ancienneDemande.setIdDemande(demande.getIdDemandeParent());
                    ancienneDemande.retrieve();

                    // Ajout de l'�l�ment de la demande dans la liste des Qds
                    if (!listeQdsPrincipales.contains(ancienneDemande.getIdQdPrincipale())) {
                        listeQdsPrincipales.add(ancienneDemande.getIdQdPrincipale());
                    }
                }
            }

            // Parcours la liste des Qds
            int nbrQds = listeQdsPrincipales.size();
            for (int itrQd = 0; itrQd < nbrQds; itrQd++) {
                String idQdPrincipale = listeQdsPrincipales.get(itrQd).toString();

                // Appel d'une methode qui va remplir les listes "dateDebutQd" et "DateFinQd"
                getPeriodesDemandes(idQdPrincipale, session);
            }

            // Appel de la methode qui va trier les dates re�ues dans les listes respectives
            getDatesTries();

            // Suppression des �l�ments de la liste
            listeDateDebutQds.clear();
            listeDateFinQds.clear();

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e
                    + ": Erreur dans le tri des dates / RFGenererDecisionRestitutionService:getDateDebutEtDateFinQds");
        }
    }

    /**
     * Methode pour supprimer les d�cisions de restitution apr�s qu'elles aient �t� trait�s
     * 
     * @param decisionArray
     * @return
     */
    public ArrayList<RFDecisionDocumentData> removeDecisionsRestitutionListe(
            ArrayList<RFDecisionDocumentData> decisionArray) {

        ArrayList<RFDecisionDocumentData> decisionArrayTemporaire = new ArrayList<RFDecisionDocumentData>();

        for (RFDecisionDocumentData decision : decisionArray) {
            if (!decision.getIsRestitution()) {
                decisionArrayTemporaire.add(decision);
            }
        }
        return decisionArrayTemporaire;
    }

    public final void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public final void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

}
