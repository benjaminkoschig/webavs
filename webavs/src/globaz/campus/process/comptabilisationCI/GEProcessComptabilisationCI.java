package globaz.campus.process.comptabilisationCI;

import globaz.campus.db.annonces.GEAnnonces;
import globaz.campus.db.annonces.GEAnnoncesManager;
import globaz.campus.db.lots.GELots;
import globaz.campus.util.GEUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteur;
import globaz.osiris.db.comptes.CACompteurManager;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.util.CIUtil;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDonneesCalcul;
import globaz.phenix.toolbox.CPToolBox;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TIRole;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.math.BigDecimal;

public class GEProcessComptabilisationCI extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String idLot = "";
    public String libelle = "";
    private BSession sessionPavo = null;
    private BSession sessionPhenix = null;

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        boolean result = true;
        GEAnnoncesManager annoncesMng = null;
        GEAnnonces annonce = null;
        GELots lot = null;
        String idLotLocal = "";
        int anneeLot = 0;
        BStatement statement = null;
        BTransaction transactionCurseur = null;
        BigDecimal solde = null;
        int annee = JACalendar.getYear(JACalendar.todayJJsMMsAAAA());
        BigDecimal montantParAnnee = null;
        CIJournal journalCi = null;
        int nbAnnonces = 0;
        int nbAnnoncesTraites = 0;
        try {
            // Création des sessions
            creationSession();
            // Recherche des annonces à l'état validé et qui n'ont pas le code
            // postgrade ou doctorant
            annoncesMng = new GEAnnoncesManager();
            annoncesMng.setSession(getSession());
            annoncesMng.setForCsEtatAnnonce(GEAnnonces.CS_ETAT_VALIDE);
            // Annonce du lot si l'id lot est renseigné
            if (!JadeStringUtil.isBlankOrZero(getIdLot())) {
                annoncesMng.setForIdLot(getIdLot());
            }
            // traiter les annonces avant les imputations
            annoncesMng.orderByAnnoncesImputations();
            // count sur les annonces
            nbAnnonces = annoncesMng.getCount(getTransaction());
            // Ouverture de la transaction du curseur
            transactionCurseur = _getTransactionCurseur();
            // ouverture du curseur
            statement = annoncesMng.cursorOpen(transactionCurseur);
            // Progression du traitement
            setProgressScaleValue(nbAnnonces);
            if (nbAnnonces > 0) {
                // Création du journal CI seulement s'il y a des annonces à
                // traiter
                journalCi = creationJournalCi();
            }
            while (((annonce = (GEAnnonces) annoncesMng.cursorReadNext(statement)) != null) && (!annonce.isNew())) {
                try {
                    // Recherche du lot et mis à jour de l'état du lot précédent
                    if (!idLotLocal.equals(annonce.getIdLot())) {
                        // Mise à jour du lot
                        majLot(lot);
                        // Recherche du lot
                        lot = _getLot(annonce.getIdLot());
                        // Stockage de l'idLot
                        idLotLocal = lot.getIdLot();
                        // Stockage de l'année du lot en int
                        anneeLot = Integer.parseInt(lot.getAnnee());
                        // L'année du lot doit être supérieure à 0
                        if (anneeLot <= 0) {
                            throw new Exception(getSession().getLabel("ANNEE_LOT"));
                        }
                    }
                    boolean inscriptionCi = false;
                    // Recherche de la décision
                    CPDecision decision = _getDecision(annonce.getIdDecision());
                    // Recherche de l'affiliation correspondant à la décision
                    AFAffiliation affiliation = _getAffiliation(decision.getIdAffiliation());
                    // Recherche du Tiers
                    TITiersViewBean tiers = _getTiers(affiliation.getIdTiers());
                    // Si la date de début est égale à la date de fin, ne rien
                    // inscrire aux CI's
                    // car dans ce cas l'étudiant est exonéré (radiation de
                    // l'affiliation)
                    if (!affiliation.getDateDebut().equals(affiliation.getDateFin())) {
                        // Traitement pour les décisions qui ne sont pas des
                        // imputations
                        if (!CPDecision.CS_IMPUTATION.equals(decision.getTypeDecision())) {
                            // Recherche du compte annexe
                            CACompteAnnexe compteAnnexe = _getCompteAnnexe(affiliation.getAffilieNumero());
                            // Stokage du solde du compte annexe
                            solde = new BigDecimal(JANumberFormatter.deQuote(compteAnnexe.getSolde()));
                            // Si le solde du compte annexe est négatif ou égal
                            // à 0, il faut inscrire au CI
                            if (solde.compareTo(new BigDecimal(0)) != 1) {
                                inscriptionCi = true;
                            } else {
                                // Si le solde est positif, inscrire au CI la
                                // section est payé complètement ou
                                // partiellement
                                while ((solde.compareTo(new BigDecimal(0)) != -1) && (annee >= anneeLot)) {
                                    montantParAnnee = _getMontantComptabilise(compteAnnexe.getIdCompteAnnexe(), annee
                                            + "");
                                    if (annee > anneeLot) {
                                        solde.subtract(montantParAnnee);
                                        // Si le solde est plus petit ou égale à
                                        // 0, inscritpion aux Ci's
                                        if (solde.compareTo(new BigDecimal(0)) != 1) {
                                            inscriptionCi = true;
                                        }
                                    } else if (annee == anneeLot) {
                                        // Si le solde est plus petit ou égal au
                                        // montant du compteur, inscrire au CI
                                        if (solde.compareTo(montantParAnnee) == -1) {
                                            inscriptionCi = true;
                                        }
                                    }
                                    annee--;
                                }
                            }
                        } else {
                            // Traitement des imputations
                            // Recherche de l'annonce parent et inscription au
                            // Ci seulement si elle est comptabilsée
                            GEAnnonces annonceParent = _getAnnonceParent(annonce.getIdAnnonceParent());
                            if (GEAnnonces.CS_ETAT_COMPTABILISE.equals(annonceParent.getCsEtatAnnonce())) {
                                inscriptionCi = true;
                            }
                        }
                        // insciption au CI
                        if (inscriptionCi) {
                            inscriptionCi(decision, journalCi, tiers, affiliation);
                            // Mettre l'annonce à l'état comptabilisé
                            majAnnonces(annonce, GEAnnonces.CS_ETAT_COMPTABILISE);
                            nbAnnoncesTraites++;
                        }
                    } else {
                        // Mettre l'annonce à l'état exempté pour les
                        // affiliations radiées
                        majAnnonces(annonce, GEAnnonces.CS_ETAT_EXEMPTE);
                        nbAnnoncesTraites++;
                    }
                } catch (Exception e) {
                    getTransaction().clearErrorBuffer();
                    if (annonce != null) {
                        getMemoryLog().logMessage(
                                "(" + annonce.getNumImmatriculationTransmis() + ", " + annonce.getNom() + ", "
                                        + annonce.getPrenom() + ")" + e.getMessage().toString(),
                                FWViewBeanInterface.WARNING, "ProcessComptabilisationCI");
                    } else {
                        getTransaction().rollback();
                    }
                }
                incProgressCounter();
                getTransaction().commit();
            }
            // Mise à jour du dernier lot traité
            majLot(lot);
            // Suppression ou comptabilisation du journal CI
            majJournalCi(journalCi);
            // Information dans l'email pour le nombre d'annonces / imputations
            // validées
            if (nbAnnoncesTraites == 0) {
                getMemoryLog().logMessage("Aucune annonce / imputation mise aux CI.", FWViewBeanInterface.OK,
                        "ProcessComptabilisationCI");
            } else {
                getMemoryLog().logMessage("Numréro du journal CI: " + journalCi.getIdJournal(), FWViewBeanInterface.OK,
                        "ProcessComptabilisationCI");
                getMemoryLog().logMessage("Nombre d'annonces / imputations mises aux CI: " + nbAnnoncesTraites,
                        FWViewBeanInterface.OK, "ProcessComptabilisationCI");
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage().toString(), FWViewBeanInterface.WARNING,
                    "ProcessValidationAnnonces");
            result = false;
        } finally {
            try {
                if (annoncesMng != null) {
                    annoncesMng.cursorClose(statement);
                }
            } finally {
                if ((transactionCurseur != null) && transactionCurseur.isOpened()) {
                    transactionCurseur.closeTransaction();
                }
            }
        }
        return result;
    }

    private AFAffiliation _getAffiliation(String idAffiliation) throws Exception {
        AFAffiliation affiliation = new AFAffiliation();
        affiliation.setSession(getSession());
        affiliation.setAffiliationId(idAffiliation);
        affiliation.retrieve(getTransaction());
        if (affiliation.isNew() || (affiliation == null)) {
            throw new Exception(getSession().getLabel("AUCUNE_AFFILIATION"));
        }
        return affiliation;
    }

    private GEAnnonces _getAnnonceParent(String idAnnonceParent) throws Exception {
        GEAnnonces annonceParent = new GEAnnonces();
        annonceParent.setSession(getSession());
        annonceParent.setIdAnnonce(idAnnonceParent);
        annonceParent.retrieve(getTransaction());
        if (annonceParent.isNew() || (annonceParent == null)) {
            throw new Exception(getSession().getLabel("AUCUNE_ANNONCE"));
        }
        if (GEAnnonces.CS_ETAT_ERREUR.equals(annonceParent.getCsEtatAnnonce())) {
            throw new Exception(getSession().getLabel("ANNONCE_PARENT_EN_ERREUR"));
        }
        return annonceParent;
    }

    private CACompteAnnexe _getCompteAnnexe(String numAffilie) throws Exception {
        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(getSession());
        compteAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
        compteAnnexe.setIdExterneRole(numAffilie);
        compteAnnexe.setIdRole(TIRole.CS_AFFILIE);
        compteAnnexe.retrieve(getTransaction());
        if (compteAnnexe.isNew() || (compteAnnexe == null)) {
            throw new Exception(getSession().getLabel("COMPTE_ANNEXE"));
        }
        return compteAnnexe;
    }

    private CPDecision _getDecision(String idDecision) throws Exception {
        CPDecision decision = new CPDecision();
        decision.setSession(getSession());
        decision.setIdDecision(idDecision);
        decision.retrieve(getTransaction());
        if (decision.isNew() || (decision == null)) {
            throw new Exception(getSession().getLabel("AUCUNE_DECISION"));
        }
        if (!CPDecision.CS_FACTURATION.equals(decision.getDernierEtat())
                && !CPDecision.CS_PB_COMPTABILISATION.equals(decision.getDernierEtat())) {
            throw new Exception(getSession().getLabel("ETAT_DECISION_NON_COMPTABILISE"));
        }
        return decision;
    }

    private GELots _getLot(String idLot) throws Exception {
        GELots lot = new GELots();
        lot.setSession(getSession());
        lot.setIdLot(idLot);
        lot.retrieve(getTransaction());
        if (lot.isNew() || (lot == null)) {
            throw new Exception(getSession().getLabel("AUCUN_LOT"));
        }
        return lot;
    }

    private BigDecimal _getMontantComptabilise(String idCompteAnnexe, String annee) throws Exception {
        BigDecimal montant = new BigDecimal(0);
        try {
            CACompteurManager compteurMng = new CACompteurManager();
            compteurMng.setSession(getSession());
            compteurMng.setForIdCompteAnnexe(idCompteAnnexe);
            compteurMng.setForAnnee(annee);
            compteurMng.find(getTransaction());
            for (int i = 0; i < compteurMng.size(); i++) {
                CACompteur compteur = (CACompteur) compteurMng.getEntity(i);
                montant = montant.add(new BigDecimal(JANumberFormatter.deQuote(compteur.getCumulCotisation())));
            }
        } catch (Exception e) {
            throw new Exception("_getMontantComptabilise: " + e.getMessage());
        }
        return montant;
    }

    private TITiersViewBean _getTiers(String idTiers) throws Exception {
        TITiersViewBean tiers = new TITiersViewBean();
        tiers.setSession(getSession());
        tiers.setIdTiers(idTiers);
        tiers.retrieve(getTransaction());
        if (tiers.isNew() || (tiers == null)) {
            throw new Exception(getSession().getLabel("AUCUN_TIERS"));
        }
        return tiers;
    }

    private BTransaction _getTransactionCurseur() throws Exception {
        try {
            BTransaction transactionCurseur = new BTransaction(getSession());
            transactionCurseur.getSession().newTransaction();
            transactionCurseur.openTransaction();
            return transactionCurseur;
        } catch (Exception e) {
            throw new Exception("_getTransactionCurseur: " + e.getMessage());
        }
    }

    @Override
    protected void _validate() throws Exception {
        // L'adresse email est obligatoire
        if (JadeStringUtil.isBlank(getEMailAddress())) {
            this._addError(getSession().getLabel("EMAIL_OBLIGATOIRE"));
        }
        // Le libellé est obligatoire
        if (JadeStringUtil.isBlank(getLibelle())) {
            this._addError(getSession().getLabel("LIBELLE_JOURNAL_OBLIGATOIRE"));
        }
        // divers :
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        // Les erreurs sont ajoutées à la session,
        // abort permet l'arrêt du process
        if (getSession().hasErrors()) {
            abort();
        }
    }

    private CIJournal creationJournalCi() throws Exception {
        try {
            CIJournal journalCi = new CIJournal();
            journalCi.setSession(getSessionPavo());
            journalCi.setIdTypeInscription(CIJournal.CS_DECISION_COT_PERS);
            journalCi.setLibelle(getLibelle());
            journalCi.add(getTransaction());
            return journalCi;
        } catch (Exception e) {
            throw new Exception("creationJournalCi: " + e.getMessage());
        }
    }

    // Création des sessions
    private void creationSession() throws Exception {
        // Création de la session pavo
        sessionPavo = GEUtil.creationSessionPavo(getSession());
        // Création de la session phenix
        sessionPhenix = GEUtil.creationSessionPhenix(getSession());
        ;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError() || isAborted() || getMemoryLog().getErrorLevel().equals(FWMessage.ERREUR)) {
            return getSession().getLabel("RESULTAT_PROCESS_COMPTABILISATION_KO");
        } else {
            return getSession().getLabel("RESULTAT_PROCESS_COMPTABILISATION_OK");
        }
    }

    public String getIdLot() {
        return idLot;
    }

    public String getLibelle() {
        return libelle;
    }

    public BSession getSessionPavo() {
        return sessionPavo;
    }

    public BSession getSessionPhenix() {
        return sessionPhenix;
    }

    private void inscriptionCi(CPDecision decision, CIJournal journalCI, TITiersViewBean tiers,
            AFAffiliation affiliation) throws Exception {
        try {
            // Recherche du n° avs correspondant à l'année de décision
            // Mandat 236 : Si période de décision < date NNSS
            // => prendre n° avs correspondant à la période décision dans
            // l'historique n° avs
            // sinon si aucun n° avs trouvé ou période de décision >= date NNSS
            // => prendre dernier n° valable
            // Récupération de la date NNSS
            String dateNNSS = ((CPApplication) getSessionPhenix().getApplication()).getDateNNSS();
            String numAvs = "";
            if (BSessionUtil.compareDateFirstLower(getSession(), decision.getFinDecision(), dateNNSS)) {
                TIHistoriqueAvs hist = new TIHistoriqueAvs();
                hist.setSession(getSession());
                try {
                    numAvs = hist.findPrevKnownNumAvs(decision.getIdTiers(), decision.getFinDecision());
                    if (JadeStringUtil.isEmpty(numAvs)) {
                        numAvs = hist.findNextKnownNumAvs(decision.getIdTiers(), decision.getDebutDecision());
                    }
                } catch (Exception e) {
                    numAvs = "";
                }
            }
            // Si aucun n° trouvé dans historique ou NNSS => prendre l'actuel n°
            // avs
            if (JadeStringUtil.isEmpty(numAvs)) {
                numAvs = tiers.getNumAvsActuel();
            }

            String montantCI = "";
            CPDonneesCalcul donnee = new CPDonneesCalcul();
            donnee.setSession(getSessionPhenix());
            montantCI = donnee.getMontant(decision.getIdDecision(), CPDonneesCalcul.CS_REV_CI);
            // Création de l'écriture
            CIEcriture ecriture = new CIEcriture();
            ecriture.setSession(getSessionPavo());
            ecriture.setAvs(numAvs);
            ecriture.setForAffiliePersonnel(true);
            String nomPrenom = tiers.getDesignation1().toUpperCase() + "," + tiers.getDesignation2().toUpperCase();
            if (nomPrenom.length() < 40) {
                ecriture.setNomPrenom(nomPrenom);
            } else {
                ecriture.setNomPrenom(nomPrenom.substring(0, 40));
            }
            if (CIUtil.isNNSSlengthOrNegate(numAvs)) {
                ecriture.setNumeroavsNNSS("true");
                ecriture.setAvsNNSS("true");
            }
            ecriture.getWrapperUtil().rechercheCI(getTransaction());
            if (CPDecision.CS_IMPUTATION.equals(decision.getTypeDecision())) {
                ecriture.setCode(CIEcriture.CS_CODE_MIS_EN_COMTE);
                ecriture.setExtourne(CIEcriture.CS_EXTOURNE_1);
            }
            ecriture.setMontant(montantCI);
            ecriture.setBrancheEconomique(affiliation.getBrancheEconomique());
            ecriture.setEmployeur(affiliation.getAffiliationId());
            if (decision.isNonActif()) {
                if (tiers.isRentier(Integer.parseInt(decision.getAnneeDecision()))) {
                    ecriture.setGenreEcriture(CIEcriture.CS_CIGENRE_7);
                } else {
                    ecriture.setGenreEcriture(CIEcriture.CS_CIGENRE_4);
                }
            }
            ecriture.setMoisDebut(Integer.toString(JACalendar.getMonth(decision.getDebutDecision())));
            ecriture.setMoisFin(Integer.toString(JACalendar.getMonth(decision.getFinDecision())));
            ecriture.setAnnee(decision.getAnneeDecision());
            ecriture.setIdJournal(journalCI.getIdJournal());
            ecriture.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE);
            ecriture.setCodeSpecial(CPToolBox._returnCodeSpecial(decision));
            ecriture.setNoSumNeeded(true);
            ecriture.add(getTransaction());
            if (!getTransaction().hasErrors()) {
                CICompteIndividuel ci = ecriture.getCI(getTransaction(), true);
                if (!CICompteIndividuel.CS_REGISTRE_ASSURES.equals(ci.getRegistre())) {
                    ci.setDateNaissance(tiers.getDateNaissance());
                    ci.update(getTransaction());
                }
            }
            if (getTransaction().hasErrors()) {
                throw new Exception(getTransaction().getErrors().toString());
            }
        } catch (Exception e) {
            throw new Exception("inscriptionCi: " + e.getMessage());
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    private void majAnnonces(GEAnnonces annonce, String etat) throws Exception {
        try {
            if ((annonce != null) && !annonce.isNew()) {
                annonce.wantCallValidate(false);
                annonce.wantCallMethodBefore(false);
                annonce.wantCallMethodAfter(false);
                annonce.setCsEtatAnnonce(etat);
                annonce.update(getTransaction());
            }
        } catch (Exception e) {
            throw new Exception("majAnnonces: " + e.getMessage());
        }
    }

    private void majJournalCi(CIJournal journalCi) throws Exception {
        try {
            if ((journalCi != null) && !journalCi.isNew()) {
                CIEcritureManager ecritureMng = new CIEcritureManager();
                ecritureMng.setSession(getSessionPavo());
                ecritureMng.setForIdJournal(journalCi.getIdJournal());
                if (ecritureMng.getCount(getTransaction()) == 0) {
                    // Suppression du journal CI si pas d'écriture
                    journalCi.delete(getTransaction());
                } else {
                    // Comptabilisation si écriture
                    journalCi.updateInscription(getTransaction());
                    // Comptabilisation du journal (dépend paramétrage de la
                    // caisse)
                    if (((CPApplication) getSessionPhenix().getApplication()).isComptabiliseJournalCi().booleanValue()) {
                        StringBuffer errors = journalCi.comptabiliser("", "", getTransaction(), this);
                        if (errors.length() > 0) {
                            throw new Exception(errors.toString());
                        }
                    }

                }
            }
        } catch (Exception e) {
            throw new Exception("majJournalCi: " + e.getMessage());
        }
    }

    private void majLot(GELots lot) throws Exception {
        try {
            if ((lot != null) && !lot.isNew()) {
                if (!GELots.CS_ETAT_COMPTABILISE.equals(lot.getCsEtatLot())) {
                    GEAnnoncesManager annoncesComptabilisees = new GEAnnoncesManager();
                    annoncesComptabilisees.setSession(getSession());
                    annoncesComptabilisees.setForCsEtatAnnonce(GEAnnonces.CS_ETAT_COMPTABILISE);
                    annoncesComptabilisees.setForIdLot(lot.getIdLot());
                    if (annoncesComptabilisees.getCount(getTransaction()) > 0) {
                        lot.setCsEtatLot(GELots.CS_ETAT_COMPTABILISE);
                        lot.wantCallMethodBefore(false);
                        lot.update(getTransaction());
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("majLot: " + e.getMessage());
        }
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setSessionPavo(BSession sessionPavo) {
        this.sessionPavo = sessionPavo;
    }

    public void setSessionPhenix(BSession sessionPhenix) {
        this.sessionPhenix = sessionPhenix;
    }
}
