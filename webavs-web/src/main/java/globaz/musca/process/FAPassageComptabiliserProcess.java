package globaz.musca.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWPKProvider;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.api.IFAPassage;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAAfactManager;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAModulePassage;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.db.facturation.FAPlanFacturation;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIGestionComptabiliteExterne;
import globaz.osiris.api.APIJournal;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIOperationOrdreRecouvrement;
import globaz.osiris.api.APIOperationOrdreVersement;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import globaz.osiris.api.APISectionDescriptor;
import globaz.osiris.api.OsirisDef;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.utils.CAReferenceBVR;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressepaiement.TIAdressePaiement;
import globaz.pyxis.db.adressepaiement.TIAvoirPaiement;
import globaz.pyxis.db.adressepaiement.TIAvoirPaiementManager;
import globaz.pyxis.db.divers.TIApplication;
import java.math.BigDecimal;

/**
 * Génération de la facturation. Date de création : (25.11.2002 11:52:37)
 * 
 * @author: BTCBProcess
 */
public class FAPassageComptabiliserProcess extends FAGenericProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected FAApplication app = null;
    protected globaz.globall.api.BIApplication appOsiris = null;
    protected CAReferenceBVR bvr = null;
    private APIGestionComptabiliteExterne compta = null;
    protected FWMemoryLog comptaMemoryLog = new FWMemoryLog();
    protected int currentNumeroJournal = 0;
    protected int dernierNumJournal = 0;
    // fermer le process de comptabilisation d'Osiris.
    protected boolean doCloseCompta = true;
    // variable d'éléments à comptabiliser par journal
    protected int elementsParJournal = 0;
    protected String fromIdExterneRole = "";
    protected String montantMinimeMax = FAApplication.MONTANT_MINIMEMAX_DEFVAL;
    protected String montantMinimeNeg = FAApplication.MONTANT_MINIMENEG_DEFVAL;

    protected String montantMinimePos = FAApplication.MONTANT_MINIMEPOS_DEFVAL;
    // Variable pour le comptage
    protected long nbComptabilise = 0; // compteur d'entêtes de facture
    // nombre d'écritures non comptabilisées
    protected long nbPasComptabilise = 0;

    protected long progressCounter = 0;

    protected globaz.globall.api.BISession sessionOsiris = null;

    protected long shouldNbComptabilise = 0;

    protected String tillIdExterneRole = "";

    /**
     * Commentaire relatif au constructeur CICompteIndividuelProcess.
     */
    public FAPassageComptabiliserProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur Doc2_3006Batch.
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public FAPassageComptabiliserProcess(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructeur du type BProcess.
     * 
     * @param session
     *            la session utilisée par le process
     */
    public FAPassageComptabiliserProcess(globaz.globall.db.BSession session) {
        super(session);
    }

    /*
     * Annule les factures qui ont un solde en dessous du montant minimum sauf si on se trouve dans le mode
     * reporterMontantMinimal (MUSCA.properties) à true
     */
    public void _annulationMontantMin(FAEnteteFacture entFacture, APICompteAnnexe cpt, APISection sec) throws Exception {
        APIEcriture ecr = null;
        String montantMinimeNeg = FAApplication.MONTANT_MINIMENEG_DEFVAL;
        String montantMinimePos = FAApplication.MONTANT_MINIMEPOS_DEFVAL;
        String rubrique = "";
        CARubrique rub = null;
        montantMinimeNeg = getSession().getApplication().getProperty(FAApplication.MONTANT_MINIMENEG);
        montantMinimePos = getSession().getApplication().getProperty(FAApplication.MONTANT_MINIMEPOS);
        FWCurrency totalFacture = new FWCurrency(entFacture.getTotalFacture());
        if (totalFacture.compareTo(new FWCurrency(0)) != 0) {
            if ((totalFacture.compareTo(new FWCurrency(montantMinimeNeg)) >= 0)
                    && (totalFacture.compareTo(new FWCurrency(montantMinimePos)) <= 0)) {
                if ((cpt == null) || cpt.isNew()) {
                    return;
                }

                rubrique = getSession().getApplication().getProperty(FAApplication.RUBRIQUE_MONTANT_MINIMAL);
                if (JadeStringUtil.isBlank(rubrique)) {
                    throw new Exception(
                            "FAPassageComptabiliserProcess._annulationMontantMin:La rubrique est obligatoire");
                }
                rub = new CARubrique();
                rub.setSession(getSession());
                rub.setAlternateKey(APIRubrique.AK_IDEXTERNE);
                rub.setIdExterne(rubrique);
                rub.retrieve(getTransaction());
                if ((rub == null) || rub.isNew()) {
                    throw new Exception(
                            "FAPassageComptabiliserProcess._annulationMontantMin:Erreur lors de la recherche de l'id rubrique pour "
                                    + rubrique);
                }
                ecr = compta.createEcriture();
                // remettre la session Osiris
                ecr.setISession(getSessionOsiris(getSession()));
                ecr.setIdCompteAnnexe(cpt.getIdCompteAnnexe());
                ecr.setIdSection(sec.getIdSection());
                ecr.setIdCompte(rub.getIdRubrique());
                ecr.setDate(passage.getDateFacturation());
                ecr.setMontant((new BigDecimal(Math.abs(totalFacture.doubleValue())).toString()));
                if (totalFacture.isPositive()) {
                    ecr.setCodeDebitCredit(APIEcriture.CREDIT);
                } else {
                    ecr.setCodeDebitCredit(APIEcriture.DEBIT);
                }
                compta.addOperation(ecr);
            }
        }
    }

    /**
     * Method _comitterCompta.
     * 
     * @param compta
     * @param numeroJournal
     * @return true si la compta a été commitée
     */
    public boolean _committerCompta(APIGestionComptabiliteExterne compta, int numeroJournal) {
        setState(getSession().getLabel("PROCESSSTATE_FIN_COMPTABILISATION") + "#" + numeroJournal);

        // vérifier si forcer la fermeture de la compta
        if (isDoCloseCompta()) {
            compta.comptabiliser();
        }

        if ((comptaMemoryLog != null) && comptaMemoryLog.hasMessages()) {
            getMemoryLog().logMessage(comptaMemoryLog);
        }

        // commiter la création du journal ainsi que ses écritures
        if (!compta.hasFatalErrors() || !getTransaction().hasErrors()) {
            try {
                getTransaction().commit();
            } catch (Exception e) {
                getMemoryLog().logMessage("Erreur dans la transaction commit du journal " + "#" + numeroJournal,
                        globaz.framework.util.FWMessage.ERREUR, this.getClass().getName());
            }
        } else {
            try {
                getTransaction().rollback();
            } catch (Exception e) {
                getMemoryLog().logMessage("Erreur dans le rollback de la transaction journal " + "#" + numeroJournal,
                        globaz.framework.util.FWMessage.ERREUR, this.getClass().getName());
            }
        }
        if (!getTransaction().hasErrors()) {
            return true;
        } else {
            return false;
        }
    }

    // Ordre de recouvrement pour le LSV
    protected void _createOrdreRecouvrement(FAEnteteFacture entFacture, APICompteAnnexe cpt, APISection sec)
            throws Exception {
        APIOperationOrdreRecouvrement recouvrement = compta.createOperationOrdreRecouvrement();
        recouvrement.setISession(getSessionOsiris(getSession()));
        recouvrement.setIdCompteAnnexe(cpt.getIdCompteAnnexe());
        recouvrement.setIdSection(sec.getIdSection());
        recouvrement.setCodeISOMonnaieBonification("CHF");
        recouvrement.setCodeISOMonnaieDepot("CHF");
        recouvrement.setEstBloque(new Boolean(false));
        recouvrement.setEstRetire(new Boolean(false));

        /* Calcul de l'échéance du recouvrement */
        FAApplication applicationMusca = (FAApplication) GlobazServer.getCurrentSystem().getApplication(
                getSession().getApplicationId());
        APISectionDescriptor sectionDescriptor = applicationMusca.getSectionDescriptor(getSession());

        // initialiser le section descriptor avec les paramètres de l'entête de
        // facture
        sectionDescriptor.setSection(entFacture.getIdExterneFacture(), entFacture.getIdTypeFacture(),
                entFacture.getIdSousType(), getPassage().getDateFacturation(), "", "");
        String date = sectionDescriptor.getDateEcheanceLSV();
        recouvrement.setDate(date);

        // valeur absolue du montant, oter le signe -
        // on ne peut pas avoir de recouvrement negatif !
        recouvrement.setMontant(entFacture.getTotalFacture()); // substring(1));

        // id adresse de paiement
        recouvrement.setIdAdressePaiement(entFacture.getIdAdressePaiement());
        recouvrement.setNatureOrdre(CAOrdreGroupe.NATURE_VERSEMENT_RECOUVREMENT_COT);
        recouvrement.setTypeVirement(APIOperationOrdreVersement.VIREMENT);

        // motif = n° affilié + "/" + n° section externe+ " " + description
        // section
        recouvrement.setMotif(entFacture.getIdExterneRole() + "/"
                + entFacture.getDescriptionDecompte(cpt.getTiers().getLangueISO()));

        // référence
        getBvr().setSession(getSession());
        getBvr().setBVR(sec, entFacture.getTotalFacture());

        if (!getBvr().getRefNoSpace().equals(CAReferenceBVR.REFERENCE_NON_FACTURABLE)) {
            recouvrement.setReferenceBVR(getBvr().getRefNoSpace());
            compta.addOperation(recouvrement);
        }
    }

    /**
     * Method _createOrdreVersement. Poure toutes écritures mégatives, créer un ordre de versement à partir du montant
     * de l'écriture de signe opposé
     * 
     * @param ecr
     */
    public void _createOrdreVersement(FAEnteteFacture entFacture, APICompteAnnexe cpt, APISection sec) {
        APIOperationOrdreVersement versement = null;
        versement = compta.createOperationOrdreVersement();
        // remettre la session Osiris
        versement.setISession(getSessionOsiris(getSession()));
        // attibuer les valeurs pour les écritures avec les données de l'afact
        versement.setIdCompteAnnexe(cpt.getIdCompteAnnexe());
        versement.setIdSection(sec.getIdSection());
        if (JadeStringUtil.isBlank(passage.getDateEcheance())) {
            versement.setDate(passage.getDateFacturation());
        } else {
            versement.setDate(passage.getDateEcheance());
        }
        versement.setCodeISOMonnaieBonification("CHF");
        versement.setCodeISOMonnaieDepot("CHF");
        versement.setEstBloque(new Boolean(false));
        versement.setEstRetire(new Boolean(false));
        // valeur absolue du montant, oter le signe -
        versement.setMontant(entFacture.getTotalFacture().substring(1));
        // id adresse de paiement

        versement.setIdAdressePaiement(entFacture.getIdAdressePaiement());
        boolean isMotifSet;
        try {
            isMotifSet = false;

            TIAvoirPaiement lienAdresse = new TIAvoirPaiement();
            lienAdresse.setSession(getSession());
            lienAdresse.setIdAdrPmtIntUnique(entFacture.getIdAdressePaiement());
            lienAdresse.retrieve(getTransaction());

            // Récupérer l'adresse de paiement
            TIAdressePaiement adrPmt = new TIAdressePaiement();
            if (!lienAdresse.isNew()) {
                adrPmt.setSession(getSession());
                adrPmt.setIdAdressePmtUnique(lienAdresse.getIdAdressePaiement());
                adrPmt.retrieve(getTransaction());
            }
        } catch (Exception e) {
            getMemoryLog().logMessage("Erreur lors de la création de l'ordre de versement: " + e.getMessage(),
                    FWViewBeanInterface.ERROR, this.getClass().getName());
            isMotifSet = false;
        }

        if (!isMotifSet) {
            // le motif: le numéro d'affilié séparé par le numéro de facture
            versement.setMotif(cpt.getIdExterneRole() + "/" + sec.getIdExterne());
        }

        versement.setNatureOrdre(CAOrdreGroupe.NATURE_VERSEMENT_REMBOURSEMENT_COT);
        versement.setTypeVirement(APIOperationOrdreVersement.VIREMENT);
        compta.addOperation(versement);
    }

    public boolean _executeComptabiliserProcess(IFAPassage passage) {

        FAModulePassage modulePassage = ((FAPassage) passage).getModulePassageEnCours();
        if (modulePassage != null) {
            String etatModule = modulePassage.getIdAction();
            String idJournalCA = passage.getIdJournal();
            CAJournal journal = retrieveJournal(passage, idJournalCA);

            if (FAModulePassage.CS_ACTION_ERREUR_COMPTA.equals(etatModule)) {
                if (!JadeStringUtil.isBlank(idJournalCA) && !journal.isNew() && journal.isComptabilise()) {
                    resetModuleAction(modulePassage, FAModulePassage.CS_ACTION_COMPTABILISE);
                    return true;

                } else if (!JadeStringUtil.isBlank(idJournalCA) && !journal.isNew() && journal.isPartiel()) {
                    getMemoryLog().logMessage(
                            "Impossible de comptabiliser ce passage, il faut au préalable comptabiliser le journal "
                                    + idJournalCA + " en comptabilité auxiliaire pour pouvoir continuer.",
                            FWViewBeanInterface.ERROR, passage.getClass().getName());
                    return false;

                } else if (!JadeStringUtil.isBlank(idJournalCA) && !journal.isNew() && journal.isAnnule()) {

                } else {
                    getMemoryLog().logMessage(
                            "Impossible comptabiliser ce passage, " + "Le journal " + idJournalCA
                                    + " doit être soit comptabilisé soit annulé pour pouvoir continuer.",
                            FWViewBeanInterface.ERROR, passage.getClass().getName());
                    return false;
                }
            }
        }

        passage = testPassage(passage);

        // Si c'est un passage de facturation trimestrielle (gros job)
        // CS_PERIODIQUE
        // ne pas fermer la compta automatiquement (ordre à Osiris de
        // comptabilisation des écritures
        // du journal en cours.

        retrievePlanFacturation(passage);

        // instancier un nouveau processus de comptabilisation
        try {
            app = (FAApplication) getSession().getApplication();
            elementsParJournal = Integer.parseInt(app.getProperty(FAApplication.ELEMENTSPARJOURNAL));
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), globaz.framework.util.FWMessage.FATAL, this.getClass().getName());
            return false;
        }
        setState(getSession().getLabel("PROCESSSTATE_PASSAGE_COMPTABILISATION"));

        boolean success = false;

        // sinon exécution de la méthode pour créer les écritures
        getMemoryLog().logMessage("Création du journal de comptabilisation...",
                globaz.framework.util.FWMessage.INFORMATION, this.getClass().getName());

        loadMontantMinime();

        // lancer la création d'écriture
        // **********************************************
        success = _executeCreerEcriture(passage.getIdTypeFacturation());

        // fermer la compta
        _committerCompta(compta, dernierNumJournal);

        if (compta.hasFatalErrors() || getTransaction().hasErrors()) {
            getTransaction().addErrors("FAPassageComptabiliserProcess");
        }

        return (!isOnError() && success);

    }

    public boolean _executeCreerEcriture(String idType) {

        // manager pour les décomptes du passage
        FAEnteteFactureManager entFactureManager = initEnteteFactureManager();
        shouldNbComptabilise = countEntFacture(entFactureManager);

        // le manager ne contient aucune entête de facture
        if (shouldNbComptabilise == 0) {
            getMemoryLog().logMessage(getSession().getLabel("OBJEMAIL_FAPRINT_NOFACTURE"),
                    globaz.framework.util.FWMessage.INFORMATION, this.getClass().getName());
            return true;
        }

        initProgessValue();

        BStatement statement = null;
        FAEnteteFacture entFacture = null;
        try {
            statement = entFactureManager.cursorOpen(getTransaction());

            APICompteAnnexe cpt = null;
            APISection sec = null;

            int compt = 0;
            // itérer sur toutes les entêtes de factures
            while (((entFacture = (FAEnteteFacture) entFactureManager.cursorReadNext(statement)) != null)
                    && (!entFacture.isNew()) && !isAborted()) {
                compt++;
                setProgressDescription(entFacture.getIdExterneRole() + " <br>" + compt + "/" + entFactureManager.size()
                        + "<br>");
                if (isAborted()) {
                    progressDescription(entFactureManager, entFacture, compt);
                    break;
                } else {
                    // instantie le process de comptabilisation annexe
                    compta = _getCompta(progressCounter);

                    // Récupérer le compte annexe et la section
                    cpt = compta.getCompteAnnexeByRole(entFacture.getIdTiers(), entFacture.getIdRole(),
                            entFacture.getIdExterneRole());

                    sec = compta.getSectionByIdExterne(cpt.getIdCompteAnnexe(), computeIdTypeSection(entFacture),
                            entFacture.getIdExterneFacture(), giveDomaineCourrier(entFacture),
                            giveTypeCourrier(entFacture), entFacture.isNonImprimable(), givePkProviderForSection());

                    treatInfoCompensation(entFacture, sec);

                    // Créer des écritures à partir des afacts du décompte
                    if (!compta.hasFatalErrors()) {
                        try {
                            createEcrituresByAfacts(entFacture, cpt, sec);
                        } catch (Exception ee) {
                            logError(entFacture, ee);
                            break;
                        }
                    } else {
                        nbPasComptabilise++;
                    }

                    treatOrdresVersementRecouvrement(entFacture, cpt, sec);

                    // Annulation des montants minimum comptabilisé
                    _annulationMontantMin(entFacture, cpt, sec);
                    setProgressCounter(progressCounter++);
                }
            } // fin du While sur enteteFactureManager
        } catch (Exception e) {
            return logError(entFacture, e);
        } finally {
            // fermer le cursor du manager des entêtes de facture
            return closeAll(entFactureManager, statement);
        }
    }

    @Override
    protected boolean _executeProcess() {
        // prendre le passage en cours;
        passage = new FAPassage();
        passage.setIdPassage(getIdPassage());
        passage.setSession(getSession());
        try {
            passage.retrieve(getTransaction());
        } catch (Exception e) {
            getMemoryLog().logMessage("Impossible de retourner le passage: " + e.getMessage(),
                    FWViewBeanInterface.ERROR, passage.getClass().getName());
        }

        // Vérifier si le passage a les critères de validité pour une impression
        if (!_passageIsValid(passage)) {
            abort();
            return false;
        } // initialiser le passage
        if (!_initializePassage(passage)) {
            abort();
            return false;
        }
        // Exécuter l'impression
        // ----------------------------------------------------------------
        boolean estComptabilise = _executeComptabiliserProcess(passage);
        // finaliser le passage (le déverrouiller)
        if (!_finalizePassageSetState(passage, FAPassage.CS_ETAT_TRAITEMENT)) {
            // attention le mettre sur comptabilise
            abort();
            return false;
        }

        return estComptabilise;
    }

    /**
     * Method _getJournalFromCompta.
     * 
     * @param compta
     * @return APIJournal
     */
    public APIGestionComptabiliteExterne _getCompta(long progressCounter) {
        boolean doNextCompta = false;
        // utilise la division intégrale pour calculer le numéro de série du
        // journal
        // calcule le numéro de journal à utiliser
        int numeroJournal = 1 + (int) java.lang.Math.floor(progressCounter / elementsParJournal);
        if (currentNumeroJournal != numeroJournal) {
            // fermer le process en cours avant d'en instancier un autre
            if (numeroJournal != 1) { // committer la compta
                if (_committerCompta(compta, numeroJournal - 1)) {
                    // incrémenter le numéro de journal
                    currentNumeroJournal++;
                    doNextCompta = true;
                } else {
                    return null;
                } // sinon instancier un nouveau process de comptabilisation
            }
        }
        if ((currentNumeroJournal != numeroJournal) || (doNextCompta)) {
            try {
                BISession sessionOsiris = getSessionOsiris(getSession());
                compta = (APIGestionComptabiliteExterne) sessionOsiris.getAPIFor(APIGestionComptabiliteExterne.class);
                comptaMemoryLog.setSession((BSession) sessionOsiris);
                compta.setMessageLog(comptaMemoryLog);
                compta.setLibelle(passage.getLibelle() + "#" + numeroJournal);
                compta.setDateValeur(passage.getDateFacturation());
                compta.setEMailAddress(getEMailAddress());
                compta.setSendCompletionMail(false);
                compta.setTransaction(getTransaction());
                compta.setProcess(this);
                compta.createJournal();
                currentNumeroJournal = numeroJournal;

                // informer le numéro du journal Osiris dans le passage de
                // facturation
                passage.setIdJournal(compta.getJournal().getIdJournal());
            } catch (Exception e) {
                getMemoryLog().logMessage(
                        "Problème dans la création du journal #" + numeroJournal + "de comptabilisation",
                        FWViewBeanInterface.ERROR, this.getClass().getName());
                compta = null;
            }
        }

        return compta;
    }

    private void checkMontantMasse(FAAfact osiAfact, APIEcriture ecr) {
        FWCurrency cMontant = osiAfact.getMontantFactureToCurrency();
        FWCurrency cMasse = new FWCurrency(osiAfact.getMasseFacture());
        if ((cMasse.signum() != 0) && (cMasse.signum() != cMontant.signum())) {
            ecr.setUnsynchronizeSigneMasse(Boolean.TRUE);
        }
    }

    private boolean closeAll(FAEnteteFactureManager entFactureManager, BStatement statement) {
        try {
            entFactureManager.cursorClose(statement);
            statement = null;
        } catch (Exception e) {
            getMemoryLog().logMessage("Erreur dans la fermeture du curseur: " + e.getMessage(),
                    globaz.framework.util.FWMessage.ERREUR, this.getClass().getName());
        }
        if (getTransaction().hasErrors()) {
            return false;
        } else {
            return true;
        }
    }

    private void closeAndCommit(FAEnteteFacture entFacture, FAAfactManager afactManager, BStatement stmt,
            FAAfact osiAfact) throws Exception {
        // fermer le cursor
        afactManager.cursorClose(stmt);
        stmt = null;
        if (!compta.hasFatalErrors() && !getTransaction().hasErrors()) {
            commitTransaction(entFacture, osiAfact);
        }
    }

    private void commitTransaction(FAEnteteFacture entFacture, FAAfact osiAfact) {
        try {
            getTransaction().commit();
            // incrémenter le compteur de décomptes
            // musca comptabilisés
            nbComptabilise++;
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Erreur dans la création de l'écriture pour l'afact" + osiAfact.getIdAfact() + " pour "
                            + entFacture.getIdExterneRole(), globaz.framework.util.FWMessage.ERREUR,
                    this.getClass().getName());
            try {
                getTransaction().rollback();
            } catch (Exception ee) {
                getMemoryLog().logMessage("Erreur lors du rollback: " + ee.getMessage(),
                        globaz.framework.util.FWMessage.ERREUR, this.getClass().getName());
            }
        }
    }

    private String computeIdTypeSection(FAEnteteFacture entFacture) {
        String idSousTypeFacture = entFacture.getIdSousType();
        String theIdTypeFacture = entFacture.getIdTypeFacture();
        // Définir le type de section
        // 227015 AF --> mettre le type de section
        // APISection.ID_TYPE_SECTION_AF
        // 227016 APG --> mettre le type de section
        // APISection.ID_TYPE_SECTION_APG
        // 227021 IJAI --> mettre le type de section
        // APISection.ID_TYPE_SECTION_IJAI
        // 227026 RESTITUTION --> mettre le type de
        // section.APISection.ID_TYPE_SECTION_RESTITUTION
        String idTypeSection = "";
        if (!JadeStringUtil.isEmpty(idSousTypeFacture)) {
            if (APISection.ID_TYPE_SECTION_DECOMPTE_CAP_CGAS.equalsIgnoreCase(theIdTypeFacture)) {
                idTypeSection = APISection.ID_TYPE_SECTION_DECOMPTE_CAP_CGAS;
            } else if (idSousTypeFacture.equals(APISection.ID_CATEGORIE_SECTION_ALLOCATIONS_FAMILIALES)) {
                idTypeSection = APISection.ID_TYPE_SECTION_AF;
            } else if (idSousTypeFacture.equals(APISection.ID_CATEGORIE_SECTION_APG)) {
                idTypeSection = APISection.ID_TYPE_SECTION_APG;
            } else if (idSousTypeFacture.equals(APISection.ID_CATEGORIE_SECTION_IJAI)) {
                idTypeSection = APISection.ID_TYPE_SECTION_IJAI;
            } else if (idSousTypeFacture.equals(APISection.ID_CATEGORIE_SECTION_RESTITUTIONS)) {
                idTypeSection = APISection.ID_TYPE_SECTION_RESTITUTION;
            } else if (idSousTypeFacture.equals(APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS_ETUDIANT)) {
                idTypeSection = APISection.ID_TYPE_SECTION_ETUDIANTS;
            } else {
                idTypeSection = APISection.ID_TYPE_SECTION_DECOMPTE_COTISATION;
            }
        }
        if (!JadeStringUtil.isEmpty(entFacture.getIdTypeFacture())
                && APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(entFacture.getIdTypeFacture())) {
            idTypeSection = APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE;
        }
        return idTypeSection;
    }

    private long countEntFacture(FAEnteteFactureManager entFactureManager) {
        try {
            shouldNbComptabilise = entFactureManager.getCount(getTransaction());
        } catch (Exception e) {
            getMemoryLog().logMessage("Ne peut pas obtenir le COUNT(*) du manager", FWViewBeanInterface.ERROR,
                    this.getClass().getName());
        }
        return shouldNbComptabilise;
    }

    private APIEcriture createEcriture(FAEnteteFacture entFacture, APICompteAnnexe cpt, String idSection,
            FAAfact osiAfact) throws Exception {
        APIEcriture ecr = null;
        ecr = compta.createEcriture(givePkProviderForOperation().getNextPrimaryKey());

        // remettre la session Osiris
        ecr.setISession(getSessionOsiris(getSession()));

        // attibuer les valeurs pour les écritures
        // avec les données de l'afact
        ecr.setIdCompteAnnexe(cpt.getIdCompteAnnexe());
        ecr.setIdSection(idSection);
        ecr.setIdCompte(osiAfact.getIdRubrique());
        if (osiAfact.getDateValeur().equals("") || osiAfact.getDateValeur().equals(null)) {
            ecr.setDate(passage.getDateFacturation());
        } else {
            ecr.setDate(osiAfact.getDateValeur());
        }
        ecr.setAnneeCotisation(osiAfact.getAnneeCotisation());
        if (getRubrique(osiAfact.getIdRubrique()).isUseCaissesProf()) {
            ecr.setIdCaisseProfessionnelle(osiAfact.getNumCaisse());
        }

        if (osiAfact.getIdTypeAfact().equals(FAAfact.CS_AFACT_COMPENSATION)
                || osiAfact.getIdTypeAfact().equals(FAAfact.CS_AFACT_COMPENSATION_INTERNE)) {
            setLibelleEcriture(ecr, osiAfact.getLibelleCompta(entFacture, false));
            if (osiAfact.getIdTypeAfact().equals(FAAfact.CS_AFACT_COMPENSATION)) {
                ecr.setIdSectionCompensation(osiAfact.getIdSection());
                ecr.setSectionCompensationDeSur(APIOperation.SECTION_COMPENSATION_SUR);
                ecr.setIdTypeOperation(APIOperation.CAECRITURECOMPENSATION);
            }
        } else {
            setLibelleEcriture(ecr, osiAfact.getLibelle());
        }

        ecr.setMasse(osiAfact.getMasseFacture());
        ecr.setTaux(osiAfact.getTauxFacture());

        setMontantEcriture(osiAfact, ecr);
        checkMontantMasse(osiAfact, ecr);
        setIdjournalEcriture(cpt, ecr);
        return ecr;
    }

    private APIEcriture createEcritureCompensation(FAEnteteFacture entFacture, String idSectionCompensation,
            FAAfact osiAfact, APISection secCompensation) throws Exception {
        FWCurrency montant = osiAfact.getMontantFactureToCurrency();
        APIEcriture ecrComp = compta.createEcriture();
        ecrComp.setIdCompte(osiAfact.getIdRubrique());
        if (osiAfact.getLibelleCompta(entFacture, true).length() > 39) {
            ecrComp.setLibelle(osiAfact.getLibelleCompta(entFacture, true).substring(0, 39));
        } else {
            ecrComp.setLibelle(osiAfact.getLibelleCompta(entFacture, true));
        }
        ecrComp.setIdCompteAnnexe(secCompensation.getIdCompteAnnexe());
        ecrComp.setIdSection(secCompensation.getIdSection());
        ecrComp.setIdSectionCompensation(idSectionCompensation);
        ecrComp.setSectionCompensationDeSur(APIOperation.SECTION_COMPENSATION_DE);
        ecrComp.setDate(passage.getDateFacturation());
        ecrComp.setIdCaisseProfessionnelle(osiAfact.getNumCaisse());
        ecrComp.setIdTypeOperation(APIOperation.CAECRITURECOMPENSATION);
        ecrComp.setMontant((new BigDecimal(Math.abs(montant.doubleValue())).toString()));
        if (montant.isPositive()) {
            ecrComp.setCodeDebitCredit(APIEcriture.CREDIT);
        } else {
            ecrComp.setCodeDebitCredit(APIEcriture.DEBIT);
        }
        if (osiAfact.getDateValeur().equals("") || osiAfact.getDateValeur().equals(null)) {
            ecrComp.setDate(passage.getDateFacturation());
        } else {
            ecrComp.setDate(osiAfact.getDateValeur());
        }
        return ecrComp;
    }

    private FAAfact createEcrituresByAfacts(FAEnteteFacture entFacture, APICompteAnnexe cpt, APISection sec)
            throws Exception {
        FAAfactManager afactManager = initAfactManager(entFacture);
        BStatement stmt = afactManager.cursorOpen(getTransaction());
        FAAfact osiAfact = null;

        String numCaisse = "";
        // itérer sur tous les afacts
        while (((osiAfact = (FAAfact) afactManager.cursorReadNext(stmt)) != null) && (!osiAfact.isNew())) {
            // PO 7189 - Prendre les écritures à zéro pour mettre à jour les compteurs de masse (pb avec taux et masse
            // faible)
            if (!(new FWCurrency(osiAfact.getMontantFacture()).isZero())
                    || !(new FWCurrency(osiAfact.getMasseFacture()).isZero())) {
                APIEcriture ecr = createEcriture(entFacture, cpt, sec.getIdSection(), osiAfact);
                compta.addOperation(ecr);

                // Passer la compensation
                treatCompensation(entFacture, cpt, sec, osiAfact);
            }

            if (!JadeStringUtil.isBlankOrZero(osiAfact.getNumCaisse())) {
                numCaisse = osiAfact.getNumCaisse();
            }
        } // fin du traitement des écritures pour un décompte

        if (!JadeStringUtil.isBlank(numCaisse)) {
            sec.updateCaisseProf(numCaisse);
        }

        closeAndCommit(entFacture, afactManager, stmt, osiAfact);

        return osiAfact;
    }

    private void createOrdreVersementOrRecouvrement(FAEnteteFacture entFacture, APICompteAnnexe cpt, APISection sec)
            throws Exception {
        FWCurrency totalFacture = new FWCurrency(entFacture.getTotalFacture());
        FWCurrency montantMinNeg = new FWCurrency(montantMinimeNeg);
        FWCurrency montantMinPos = new FWCurrency(montantMinimePos);
        // créer un ordre de versement si écritures négatives
        if ((FAEnteteFacture.CS_MODE_REMBOURSEMENT.equalsIgnoreCase(entFacture.getIdModeRecouvrement()) || FAEnteteFacture.CS_MODE_FORCEE_REMBOURSEMENT
                .equalsIgnoreCase(entFacture.getIdModeRecouvrement()))
                && (totalFacture != null)
                && totalFacture.isNegative() && (totalFacture.doubleValue() < montantMinNeg.doubleValue())) {
            _createOrdreVersement(entFacture, cpt, sec);
            // créer un ordre de recouvrement si entete en mode
            // de recouvrement direct
        } else if (FAEnteteFacture.CS_MODE_RECOUVREMENT_DIRECT.equals(entFacture.getIdModeRecouvrement())
                && (totalFacture != null) && totalFacture.isPositive()
                && !JadeStringUtil.isIntegerEmpty(entFacture.getIdAdressePaiement())
                && (totalFacture.doubleValue() > montantMinPos.doubleValue())) {
            _createOrdreRecouvrement(entFacture, cpt, sec);
        }
    }

    private APISection createOrRetrieveSectionCompensation(APICompteAnnexe cpt, FAAfact osiAfact) {
        APISection secCompensation = null;
        if (!osiAfact.getIdExterneRole().equals(osiAfact.getIdExterneDebiteurCompensation())) {
            APICompteAnnexe cptCompensation = null;
            cptCompensation = compta.getCompteAnnexeByRole(osiAfact.getIdTiersDebiteurCompensation(),
                    osiAfact.getIdRoleDebiteurCompensation(), osiAfact.getIdExterneDebiteurCompensation());
            secCompensation = compta.getSectionByIdExterne(cptCompensation.getIdCompteAnnexe(),
                    osiAfact.getIdTypeFactureCompensation(),
                    // 1 ou 8 pour ancienne facture
                    osiAfact.getIdExterneFactureCompensation());
        } else {
            // Récupérer la section
            secCompensation = compta.getSectionByIdExterne(cpt.getIdCompteAnnexe(),
                    osiAfact.getIdTypeFactureCompensation(),
                    // 1 ou 8 pour ancienne
                    // facture
                    osiAfact.getIdExterneFactureCompensation());
        }
        return secCompensation;
    }

    private TIAvoirPaiementManager findAvoirPaiement(FAEnteteFacture entFacture) throws Exception {
        TIAvoirPaiementManager adresse = new TIAvoirPaiementManager();
        adresse.setSession(getSession());
        adresse.setForIdTiers(entFacture.getIdTiers());
        adresse.setForIdAdressePmtInterne(entFacture.getIdAdressePaiement());
        adresse.find();
        return adresse;
    }

    public BIApplication getApplicationOsiris() {
        // Si application pas ouverte
        if (appOsiris == null) {
            try {
                appOsiris = GlobazSystem.getApplication(OsirisDef.DEFAULT_APPLICATION_OSIRIS);
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return appOsiris;
    }

    /**
     * Renvoie la référence BVR.
     * 
     * @return la référence BVR.
     */
    public CAReferenceBVR getBvr() {
        if (bvr == null) {
            bvr = new CAReferenceBVR();
        }
        return bvr;
    }

    /**
     * Returns the fromIdExterneRole.
     * 
     * @return String
     */
    @Override
    public String getFromIdExterneRole() {
        return fromIdExterneRole;
    }

    protected CARubrique getRubrique(String idRubrique) {
        CARubrique rubrique = new CARubrique();
        rubrique.setISession(getSession());
        rubrique.setIdRubrique(idRubrique);
        try {
            rubrique.retrieve();
            if (rubrique.isNew()) {
                rubrique = null;
            }
        } catch (Exception e) {
            rubrique = null;
        }
        return rubrique;
    }

    public BISession getSessionOsiris(BISession session) {
        // Si session pas ouverte
        if (sessionOsiris == null) {
            try {
                sessionOsiris = getApplicationOsiris().newSession(session);
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return sessionOsiris;
    }

    /**
     * Returns the tillIdExterneRole.
     * 
     * @return String
     */
    @Override
    public String getTillIdExterneRole() {
        return tillIdExterneRole;
    }

    /**
     * @param entFacture
     * @return
     */
    private String giveDomaineCourrier(FAEnteteFacture entFacture) {
        String domaineCourrier = "";
        if (!JadeStringUtil.isIntegerEmpty(entFacture.getIdDomaineCourrier())) {
            domaineCourrier = entFacture.getIdDomaineCourrier();
        } else {
            domaineCourrier = TIApplication.CS_FACTURATION;
        }
        return domaineCourrier;
    }

    /**
     * @return
     */
    private FWPKProvider givePkProviderForOperation() {
        int tailleLotOperation = 100; // simplification... pourrait être le nombre d'afact...
        // Reservation d'une plage d'incrément pour gagner en performence
        CAOperation opForPk = new CAOperation();
        opForPk.setSession(getSession());
        FWPKProvider pkProviderForOperation = opForPk.getNewPrimaryKeyProvider(tailleLotOperation);
        return pkProviderForOperation;
    }

    /**
     * @return
     */
    private FWPKProvider givePkProviderForSection() {
        int tailleLotSection = 20;
        CASection sectionForPk = new CASection();
        sectionForPk.setSession(getSession());
        FWPKProvider pkProviderForSection = sectionForPk.getNewPrimaryKeyProvider(tailleLotSection);
        return pkProviderForSection;
    }

    /**
     * @param entFacture
     * @return
     */
    private String giveTypeCourrier(FAEnteteFacture entFacture) {
        String typeCourrier = "";
        if (!JadeStringUtil.isIntegerEmpty(entFacture.getIdTypeCourrier())) {
            typeCourrier = entFacture.getIdTypeCourrier();
        } else {
            typeCourrier = IConstantes.CS_AVOIR_ADRESSE_COURRIER;
        }
        return typeCourrier;
    }

    /**
     * @param entFacture
     * @return
     */
    private FAAfactManager initAfactManager(FAEnteteFacture entFacture) {
        FAAfactManager afactManager = new FAAfactManager();
        afactManager.setSession(getSession());
        afactManager.setForIdPassage(passage.getIdPassage());
        afactManager.setForIdEnteteFacture(entFacture.getIdEntete());
        afactManager.setForAQuittancer(new Boolean(false));
        afactManager.setForNonComptabilisable("false");
        afactManager.setOrderBy(" IDENTETEFACTURE ASC, IDAFACT ASC ");
        return afactManager;
    }

    private FAEnteteFactureManager initEnteteFactureManager() {
        FAEnteteFactureManager entFactureManager = new FAEnteteFactureManager();
        entFactureManager.setSession(getSession());
        entFactureManager.setForIdPassage(passage.getIdPassage());

        // comptabiliser d'affilié en affilié
        entFactureManager.setFromIdExterneRole(getFromIdExterneRole());
        entFactureManager.setForTillIdExterneRole(getTillIdExterneRole());
        entFactureManager.setOrderBy(" IDEXTERNEROLE ASC, IDEXTERNEFACTURE ASC ");
        return entFactureManager;
    }

    private void initProgessValue() {
        // Entrer les informations pour l'état du process
        setState("(" + super.getIdPassage() + ") " + getSession().getLabel("PROCESSSTATE_COMPTABILISATION"));
        if (shouldNbComptabilise > 0) {
            setProgressScaleValue(shouldNbComptabilise);
        } else {
            setProgressScaleValue(1);
        }
    }

    /**
     * Returns the doCloseComta.
     * 
     * @return boolean
     */
    public boolean isDoCloseCompta() {
        return doCloseCompta;
    }

    /**
     * @param passageCompenser
     * @param osiAfact
     * @return
     */
    private boolean isRubriqueCompensation(String idExterneRubrique) {
        FAPassageCompenserProcess passageCompenser = new FAPassageCompenserProcess();
        passageCompenser.setSession(getSession());
        return passageCompenser.getRubriqueCode(APIReferenceRubrique.RUBRIQUE_DE_LISSAGE).equalsIgnoreCase(
                idExterneRubrique)
                || passageCompenser.getRubriqueCode(APIReferenceRubrique.COMPENSATION_REPORT_DE_SOLDE)
                        .equalsIgnoreCase(idExterneRubrique)
                || passageCompenser.getRubriqueCode(APIReferenceRubrique.COMPENSATION_REPORT_DE_TAXE).equalsIgnoreCase(
                        idExterneRubrique)
                || passageCompenser.getRubriqueCode(APIReferenceRubrique.COMPENSATION_APG_MAT).equalsIgnoreCase(
                        idExterneRubrique)
                || passageCompenser.getRubriqueCode(APIReferenceRubrique.COMPENSATION_IJAI).equalsIgnoreCase(
                        idExterneRubrique)
                || passageCompenser.getRubriqueCode(APIReferenceRubrique.COMPENSATION_ALFA).equalsIgnoreCase(
                        idExterneRubrique)
                || passageCompenser.getRubriqueCode(APIReferenceRubrique.COMPENSATION_RENTES).equalsIgnoreCase(
                        idExterneRubrique);
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Charger les bornes des montants minimes
     */
    private void loadMontantMinime() {
        // Charger les bornes des montants minimes
        try {
            montantMinimeNeg = getSession().getApplication().getProperty(FAApplication.MONTANT_MINIMENEG);
            montantMinimePos = getSession().getApplication().getProperty(FAApplication.MONTANT_MINIMEPOS);
            montantMinimeMax = getSession().getApplication().getProperty(FAApplication.MONTANT_MINIMEMAX);
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    private boolean logError(FAEnteteFacture entFacture, Exception e) {
        getMemoryLog().logMessage("Erreur dans la création de l'écriture: " + e.getMessage(),
                globaz.framework.util.FWMessage.ERREUR, this.getClass().getName());
        if (entFacture != null) {
            getMemoryLog().logMessage(
                    "IdEntete: " + entFacture.getIdEntete() + ", IdExterneRole: " + entFacture.getIdExterneRole()
                            + ", idExterneFacture: " + entFacture.getIdExterneFacture(),
                    globaz.framework.util.FWMessage.INFORMATION, this.getClass().getName());
        }
        getTransaction().addErrors(
                "Erreur dans la création de l'écriture: " + e.getMessage() + ". (" + "IdEntete: "
                        + entFacture.getIdEntete() + ", IdExterneRole: " + entFacture.getIdExterneRole() + ")");
        return false;
    }

    private void logWarning(FAEnteteFacture entFacture) {
        FWCurrency totalFacture = new FWCurrency(entFacture.getTotalFacture());
        FWCurrency montantMinNeg = new FWCurrency(montantMinimeNeg);
        FWCurrency montantMinPos = new FWCurrency(montantMinimePos);
        if ((FAEnteteFacture.CS_MODE_REMBOURSEMENT.equalsIgnoreCase(entFacture.getIdModeRecouvrement()) || FAEnteteFacture.CS_MODE_FORCEE_REMBOURSEMENT
                .equalsIgnoreCase(entFacture.getIdModeRecouvrement()))
                && (totalFacture != null)
                && totalFacture.isNegative() && (totalFacture.doubleValue() < montantMinNeg.doubleValue())) {
            getMemoryLog().logMessage(
                    "Il n'y a pas d'adresse de paiement pour l'affilié : " + entFacture.getIdExterneRole()
                            + " Ou l'id d'addresse ne correspond à aucune adresse de paiement",
                    FWViewBeanInterface.WARNING, this.getClass().getName());
        } else if (FAEnteteFacture.CS_MODE_RECOUVREMENT_DIRECT.equals(entFacture.getIdModeRecouvrement())
                && (totalFacture != null) && totalFacture.isPositive()
                && !JadeStringUtil.isIntegerEmpty(entFacture.getIdAdressePaiement())
                && (totalFacture.doubleValue() > montantMinPos.doubleValue())) {
            getMemoryLog().logMessage(
                    "Il n'y a pas d'adresse de paiement pour l'affilié : " + entFacture.getIdExterneRole()
                            + " Ou l'id d'addresse ne correspond à aucune adresse de paiement",
                    FWViewBeanInterface.WARNING, this.getClass().getName());
        }
    }

    private void progressDescription(FAEnteteFactureManager entFactureManager, FAEnteteFacture entFacture, int compt) {
        setProgressDescription("Traitement interrompu<br> sur l'affilié : " + entFacture.getIdExterneRole() + " <br>"
                + compt + "/" + entFactureManager.size() + "<br>");
        if ((getParent() != null) && getParent().isAborted()) {
            getParent().setProcessDescription(
                    "Traitement interrompu<br> sur l'affilié : " + entFacture.getIdExterneRole() + " <br>" + compt
                            + "/" + entFactureManager.size() + "<br>");
        }
    }

    public void resetModuleAction(FAModulePassage modulePassage, String actionName) {
        // On crée une nouvelle transaction, car celle utilisée peut avoir une
        // erreur,
        // et il n'est donc pas possible de faire de commit dessus
        BTransaction transac = null;
        try {
            transac = new BTransaction(getSession());
            transac.openTransaction();
            modulePassage.setIdAction(actionName);
            modulePassage.update(transac);
            transac.commit();

        } catch (Exception e) {
            clearLogInfo4Process();
            this.logInfo4Process(false, false, "OBJEMAIL_FA_DOACTIONZERO_WARNING");
            getMemoryLog().logStringBuffer(getTransaction().getErrors(), this.getClass().getName());
            this._addError(getTransaction(), "Erreur lors de la mise à zéro des actions des modules du passage. ");
        } finally {
            if (transac != null) {
                if (transac.isOpened()) {
                    try {
                        transac.closeTransaction();
                    } catch (Exception e) {
                        clearLogInfo4Process();
                        this.logInfo4Process(false, false, "OBJEMAIL_FA_DOACTIONZERO_WARNING");
                        getMemoryLog().logStringBuffer(getTransaction().getErrors(), this.getClass().getName());
                        this._addError(getTransaction(),
                                "Erreur lors de la mise à zéro des actions des modules du passage, lors de la cloture de la transaction. "
                                        + e.toString());
                    }
                }
            }
        }

    }

    private CAJournal retrieveJournal(IFAPassage passage, String idJournalCA) {
        CAJournal journal = new CAJournal();
        journal.setSession(getSession());
        journal.setIdJournal(idJournalCA);
        try {
            journal.retrieve(getTransaction());
        } catch (Exception e) {
            getMemoryLog().logMessage("Impossible de retourner le journal  " + idJournalCA + e.getMessage(),
                    FWViewBeanInterface.ERROR, passage.getClass().getName());
        }
        return journal;
    }

    private void retrievePlanFacturation(IFAPassage passage) {
        FAPlanFacturation myPlan = new FAPlanFacturation();
        myPlan.setSession(getSession());
        myPlan.setIdPlanFacturation(passage.getIdPlanFacturation());
        try {
            myPlan.retrieve(getTransaction());
        } catch (Exception e) {
            this._addError(getTransaction(),
                    getSession().getLabel("LABEL_PLAN_PASSAGE_PAS_TROUVE") + passage.getIdPassage());
        }
    }

    /**
     * Sets the doCloseComta.
     * 
     * @param doCloseComta
     *            The doCloseComta to set
     */
    public void setDoCloseCompta(boolean doCloseCompta) {
        this.doCloseCompta = doCloseCompta;
    }

    /**
     * Sets the fromIdExterneRole.
     * 
     * @param fromIdExterneRole
     *            The fromIdExterneRole to set
     */
    @Override
    public void setFromIdExterneRole(String fromIdExterneRole) {
        this.fromIdExterneRole = fromIdExterneRole;
    }

    private void setIdjournalEcriture(APICompteAnnexe cpt, APIEcriture ecr) {
        if (ecr instanceof CAOperation) { // devrait tj être le cas ...
            CAOperation op = (CAOperation) ecr;

            APIJournal journal = compta.getJournal();
            if (journal instanceof CAJournal) { // devrait tj être le cas ...
                op.setJournal((CAJournal) journal);
            }
            if (cpt instanceof CACompteAnnexe) { // devrait tj être le cas ...
                op.setCompteAnnexe((CACompteAnnexe) cpt);
            }
        }
    }

    private void setLibelleEcriture(APIEcriture ecr, String libelleCompta) {
        if (libelleCompta.length() > 39) {
            ecr.setLibelle(libelleCompta.substring(0, 39));
        } else {
            ecr.setLibelle(libelleCompta);
            // libelle est plus cours dans osiris
        }
    }

    private FWCurrency setMontantEcriture(FAAfact osiAfact, APIEcriture ecr) {
        FWCurrency montant = osiAfact.getMontantFactureToCurrency();
        // le montant en valeur absolu
        ecr.setMontant((new BigDecimal(Math.abs(montant.doubleValue())).toString()));

        if (montant.isPositive()) {
            ecr.setCodeDebitCredit(APIEcriture.DEBIT);
        } else {
            ecr.setCodeDebitCredit(APIEcriture.CREDIT);
        }
        return montant;
    }

    /**
     * Sets the tillIdExterneRole.
     * 
     * @param tillIdExterneRole
     *            The tillIdExterneRole to set
     */
    @Override
    public void setTillIdExterneRole(String tillIdExterneRole) {
        this.tillIdExterneRole = tillIdExterneRole;
    }

    private IFAPassage testPassage(IFAPassage passage) {
        // test du passage
        if ((passage == null) || passage.isNew()) {
            passage = new FAPassage();
            passage.setIdPassage(getIdPassage());
            passage.setISession(getSession());
            try {
                this.passage.retrieve(getTransaction());
            } catch (Exception e) {
                getMemoryLog().logMessage("Impossible de retourner le passage: " + e.getMessage(),
                        FWViewBeanInterface.ERROR, passage.getClass().getName());
            }
        }
        if ((passage != null) && !passage.isNew()) {
            this.setPassage((FAPassage) passage);
        }
        return passage;
    }

    private void treatCompensation(FAEnteteFacture entFacture, APICompteAnnexe cpt, APISection sec, FAAfact osiAfact)
            throws Exception {

        if (!JadeStringUtil.isBlank(osiAfact.getIdExterneFactureCompensation())
                && !osiAfact.getIdTypeAfact().equals(FAAfact.CS_AFACT_COMPENSATION_INTERNE)) {
            if (isRubriqueCompensation(osiAfact.getIdExterneRubrique())) {
                // Créer une nouvelle écriture
                APISection secCompensation = createOrRetrieveSectionCompensation(cpt, osiAfact);
                APIEcriture ecrComp = createEcritureCompensation(entFacture, sec.getIdSection(), osiAfact,
                        secCompensation);
                compta.addOperation(ecrComp);

            } else {
                getTransaction().addErrors(
                        "La rubrique n'existe pas! " + osiAfact.getIdExterneRubrique() + " pour "
                                + entFacture.getIdExterneRole());
            }
        }
    }

    private void treatInfoCompensation(FAEnteteFacture entFacture, APISection sec) throws Exception {
        FWCurrency totalFacture = new FWCurrency(entFacture.getTotalFacture());
        if (((FAApplication) getSession().getApplication()).isModeReporterMontantMinimal()) {
            if (!totalFacture.isZero() && (totalFacture.compareTo(new FWCurrency(montantMinimeMax)) < 0)
                    && (totalFacture.compareTo(new FWCurrency(montantMinimePos)) > 0)) {
                sec.updateInfoCompensation(APISection.MODE_REPORT, sec.getIdPassageComp());
            }
        }
        // Si le montant de la facture est négatif et qu'il n'y a
        // pas d'adresse de paiement ni de contentieux
        // bloqué la compensation de la section.
        if (totalFacture.isNegative() && !FAEnteteFacture.CS_MODE_RETENU.equals(entFacture.getIdModeRecouvrement())
                && JadeStringUtil.isBlankOrZero(entFacture.getIdAdressePaiement())) {
            sec.updateInfoCompensation(APISection.MODE_BLOQUER_COMPENSATION, sec.getIdPassageComp());
        }
    }

    private TIAvoirPaiementManager treatOrdresVersementRecouvrement(FAEnteteFacture entFacture, APICompteAnnexe cpt,
            APISection sec) throws Exception {
        TIAvoirPaiementManager adresse = findAvoirPaiement(entFacture);
        if (adresse.size() > 0) {
            createOrdreVersementOrRecouvrement(entFacture, cpt, sec);
        } else {
            logWarning(entFacture);
        }
        return adresse;
    }
}
