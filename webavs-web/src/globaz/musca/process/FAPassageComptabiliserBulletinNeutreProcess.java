package globaz.musca.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.api.IFAPassage;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAAfactManager;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.db.facturation.FAPlanFacturation;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIGestionBulletinNeutre;
import globaz.osiris.api.APIOperationBulletinNeutre;
import globaz.osiris.api.APISection;
import globaz.pyxis.constantes.IConstantes;

/**
 * Génération de la facturation. Date de création : (25.11.2002 11:52:37)
 * 
 * @author: BTCBProcess
 */
public class FAPassageComptabiliserBulletinNeutreProcess extends FAPassageComptabiliserProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private APIGestionBulletinNeutre compta = null;

    /**
     * Method _comitterCompta.
     * 
     * @param compta
     * @param numeroJournal
     * @return true si la compta a été commitée
     */
    public boolean _committerCompta(APIGestionBulletinNeutre compta, int numeroJournal) {
        setState(getSession().getLabel("PROCESSSTATE_FIN_COMPTABILISATION") + "#" + numeroJournal);

        // vérifier si forcer la fermeture de la compta
        if (isDoCloseCompta()) {
            try {
                compta.comptabiliser(getSession(), this);
            } catch (Exception e) {
                getMemoryLog().logMessage(
                        "Erreur lors de la comptabilisation du journal Bulletin Neutre" + "#" + numeroJournal,
                        globaz.framework.util.FWMessage.ERREUR, this.getClass().getName());
            }
        }

        if ((comptaMemoryLog != null) && comptaMemoryLog.hasMessages()) {
            getMemoryLog().logMessage(comptaMemoryLog);
        }

        // commiter la création du journal ainsi que ses écritures
        if (!getTransaction().hasErrors() || !getTransaction().hasErrors()) {
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

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:26:51)
     * 
     * @param passage
     * @return boolean
     */
    @Override
    public boolean _executeComptabiliserProcess(IFAPassage passage) {

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

        // Si c'est un passage de facturation trimestrielle (gros job)
        // CS_PERIODIQUE
        // ne pas fermer la compta automatiquement (ordre à Osiris de
        // comptabilisation des écritures
        // du journal en cours.

        FAPlanFacturation myPlan = new FAPlanFacturation();
        myPlan.setSession(getSession());
        myPlan.setIdPlanFacturation(passage.getIdPlanFacturation());
        try {
            myPlan.retrieve(getTransaction());
        } catch (Exception e) {
            this._addError(getTransaction(),
                    getSession().getLabel("LABEL_PLAN_PASSAGE_PAS_TROUVE") + passage.getIdPassage());
        }

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

        // Charger les bornes des montants minimes
        try {
            montantMinimeNeg = getSession().getApplication().getProperty(FAApplication.MONTANT_MINIMENEG);
            montantMinimePos = getSession().getApplication().getProperty(FAApplication.MONTANT_MINIMEPOS);
            montantMinimeMax = getSession().getApplication().getProperty(FAApplication.MONTANT_MINIMEMAX);
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        // lancer la création d'écriture
        // **********************************************
        success = _executeCreerEcriture(passage.getIdTypeFacturation());

        // mettre l'état du passage à comptabilisé
        if (success) {
            _finalizePassageSetState((FAPassage) passage, FAPassage.CS_ETAT_COMPTABILISE);

        } else {
            _finalizePassageSetState((FAPassage) passage, FAPassage.CS_ETAT_TRAITEMENT);
        }

        // fermer la compta
        this._committerCompta(compta, dernierNumJournal);

        if (getTransaction().hasErrors() || getTransaction().hasErrors()) {
            getTransaction().addErrors("FAPassageComptabiliserProcess");
        }

        return (!isOnError() && success);

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.05.2003 16:37:33)
     * 
     * @return boolean
     * @param idType
     *            java.lang.String
     */
    @Override
    public boolean _executeCreerEcriture(String idType) {

        // BSession sessionOsiris =
        // (BSession)GlobazServer.getCurrentSystem().getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS).newSession(getSession());
        BSession sessionOsiris = (BSession) getSessionOsiris(getSession());
        FAPassageCompenserProcess passageCompenser = new FAPassageCompenserProcess();
        passageCompenser.setSession(getSession());
        // manager pour les décomptes du passage
        FAEnteteFactureManager entFactureManager = new FAEnteteFactureManager();
        entFactureManager.setSession(getSession());
        entFactureManager.setForIdPassage(passage.getIdPassage());

        // comptabiliser d'affilié en affilié
        entFactureManager.setFromIdExterneRole(getFromIdExterneRole());
        entFactureManager.setForTillIdExterneRole(getTillIdExterneRole());
        entFactureManager.setOrderBy(" IDEXTERNEROLE ASC, IDEXTERNEFACTURE ASC ");
        boolean success = true;
        try {
            shouldNbComptabilise = entFactureManager.getCount(getTransaction());
        } catch (Exception e) {
            getMemoryLog().logMessage("Ne peut pas obtenir le COUNT(*) du manager", FWViewBeanInterface.ERROR,
                    this.getClass().getName());

        }
        // le manager ne contient aucune entête de facture
        if (shouldNbComptabilise == 0) {
            getMemoryLog().logMessage(getSession().getLabel("OBJEMAIL_FAPRINT_NOFACTURE"),
                    globaz.framework.util.FWMessage.INFORMATION, this.getClass().getName());
            return true;
        }

        // Entrer les informations pour l' état du process
        setState("(" + super.getIdPassage() + ") " + getSession().getLabel("PROCESSSTATE_COMPTABILISATION"));
        if (shouldNbComptabilise > 0) {
            setProgressScaleValue(shouldNbComptabilise);
        } else {
            setProgressScaleValue(1);
        }

        BStatement statement = null;
        FAEnteteFacture entFacture = null;
        try {
            statement = entFactureManager.cursorOpen(getTransaction());

            APICompteAnnexe cpt = null;
            APISection sec = null;
            APIOperationBulletinNeutre ecr = null;
            int compt = 0;
            // itérer sur toutes les entêtes de factures
            while (((entFacture = (FAEnteteFacture) entFactureManager.cursorReadNext(statement)) != null)
                    && (!entFacture.isNew()) && !isAborted()) {
                compt++;
                setProgressDescription(entFacture.getIdExterneRole() + " <br>" + compt + "/" + entFactureManager.size()
                        + "<br>");
                if (isAborted()) {
                    setProgressDescription("Traitement interrompu<br> sur l'affilié : " + entFacture.getIdExterneRole()
                            + " <br>" + compt + "/" + entFactureManager.size() + "<br>");
                    if ((getParent() != null) && getParent().isAborted()) {
                        getParent().setProcessDescription(
                                "Traitement interrompu<br> sur l'affilié : " + entFacture.getIdExterneRole() + " <br>"
                                        + compt + "/" + entFactureManager.size() + "<br>");
                    }
                    break;
                } else {
                    // instantie le process de comptabilisation annexe
                    compta = _getComptaBulletinNeutre(progressCounter);

                    // Récupérer le compte annexe et la section
                    cpt = compta.getCompteAnnexeByRole(sessionOsiris, getTransaction(), entFacture.getIdTiers(),
                            entFacture.getIdRole(), entFacture.getIdExterneRole());

                    // Calcul de la section
                    // On renseigne le type de courrier et le domaine de
                    // courrier
                    // ces infos viennent de l'affiliation
                    String typeCourrier = "";
                    String domaineCourrier = "";
                    if (!JadeStringUtil.isIntegerEmpty(entFacture.getIdTypeCourrier())) {
                        typeCourrier = entFacture.getIdTypeCourrier();
                    } else {
                        typeCourrier = IConstantes.CS_AVOIR_ADRESSE_COURRIER;
                    }
                    if (!JadeStringUtil.isIntegerEmpty(entFacture.getIdDomaineCourrier())) {
                        domaineCourrier = entFacture.getIdDomaineCourrier();
                    } else {
                        domaineCourrier = IConstantes.CS_APPLICATION_FACTURATION;
                    }
                    sec = compta.createSection(sessionOsiris, getTransaction(), cpt.getIdCompteAnnexe(),
                            entFacture.getIdExterneFacture(), domaineCourrier, typeCourrier,
                            entFacture.isNonImprimable(), null);

                    FWCurrency totalFacture = new FWCurrency(entFacture.getTotalFacture());
                    if (((FAApplication) getSession().getApplication()).isModeReporterMontantMinimal()) {
                        if (!totalFacture.isZero() && (totalFacture.compareTo(new FWCurrency(montantMinimeMax)) < 0)
                                && (totalFacture.compareTo(new FWCurrency(montantMinimePos)) > 0)) {
                            sec.updateInfoCompensation(APISection.MODE_REPORT, sec.getIdPassageComp());
                        }
                    }
                    // Créer des écritures à partir des afacts du décompte
                    if (!getTransaction().hasErrors()) {
                        FAAfactManager afactManager = new FAAfactManager();
                        afactManager.setSession(getSession());
                        afactManager.setForIdPassage(passage.getIdPassage());
                        afactManager.setForIdEnteteFacture(entFacture.getIdEntete());
                        afactManager.setForAQuittancer(new Boolean(false));
                        afactManager.setForNonComptabilisable("false");
                        afactManager.setOrderBy(" IDENTETEFACTURE ASC, IDAFACT ASC ");
                        BStatement stmt = null;
                        try {
                            stmt = afactManager.cursorOpen(getTransaction());
                            FAAfact osiAfact = null;
                            // itérer sur tous les afacts
                            while (((osiAfact = (FAAfact) afactManager.cursorReadNext(stmt)) != null)
                                    && (!osiAfact.isNew())) {

                                ecr = compta.createOperationBulletinNeutre(sessionOsiris, getTransaction());

                                // remettre la session Osiris
                                ecr.setISession(getSessionOsiris(getSession()));

                                // attibuer les valeurs pour les écritures avec
                                // les données de l'afact
                                ecr.setIdCompteAnnexe(cpt.getIdCompteAnnexe());
                                ecr.setIdSection(sec.getIdSection());
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
                                    String libelleCompta = osiAfact.getLibelleCompta(entFacture, false);
                                    if (libelleCompta.length() > 39) {
                                        ecr.setLibelle(libelleCompta.substring(0, 39));
                                    } else {
                                        ecr.setLibelle(libelleCompta);
                                        // libelle est plus cours dans osiris
                                    }

                                } else {
                                    String libelleCompta = osiAfact.getLibelle();
                                    if (libelleCompta.length() > 39) {
                                        ecr.setLibelle(libelleCompta.substring(0, 39));
                                    } else {
                                        ecr.setLibelle(libelleCompta);
                                        // libelle est plus cours dans osiris
                                    }
                                }
                                ecr.setTaux(osiAfact.getTauxFacture());
                                compta.addOperationBulletinNeutre(sessionOsiris, getTransaction(), ecr);

                            }

                            // fermer le cursor
                            afactManager.cursorClose(stmt);
                            stmt = null;
                            if (!getTransaction().hasErrors() && !getTransaction().hasErrors()) {
                                try {
                                    getTransaction().commit();
                                    // incrémenter le compteur de décomptes
                                    // musca comptabilisés
                                    nbComptabilise++;
                                } catch (Exception e) {
                                    getMemoryLog().logMessage(
                                            "Erreur dans la création de l'écriture pour l'afact"
                                                    + osiAfact.getIdAfact() + " pour " + entFacture.getIdExterneRole(),
                                            globaz.framework.util.FWMessage.ERREUR, this.getClass().getName());
                                    try {
                                        getTransaction().rollback();
                                    } catch (Exception ee) {
                                        getMemoryLog().logMessage("Erreur lors du rollback: " + ee.getMessage(),
                                                globaz.framework.util.FWMessage.ERREUR, this.getClass().getName());
                                    }
                                }
                            }
                        } catch (Exception ee) {
                            getMemoryLog().logMessage(
                                    "Erreur dans la création de l'écriture pour l'afact "
                                            + entFacture.getIdExterneRole() + " pour "
                                            + entFacture.getIdExterneFacture(), globaz.framework.util.FWMessage.ERREUR,
                                    this.getClass().getName());
                            getTransaction().addErrors(
                                    "Erreur dans la création de l'écriture: " + ee.getMessage() + ". (" + "IdEntete: "
                                            + entFacture.getIdEntete() + ", IdExterneRole: "
                                            + entFacture.getIdExterneRole() + ")");
                            success = false;
                            break;
                        }
                    } else {
                        nbPasComptabilise++;
                    }
                    setProgressCounter(progressCounter++);
                }

            } // fin du While sur enteteFactureManager

            // fermer la compta
            int dernierNumJournal = (int) progressCounter % elementsParJournal;

        } catch (Exception e) {
            getMemoryLog().logMessage("Erreur dans la création de l'écriture: " + e.getMessage(),
                    globaz.framework.util.FWMessage.ERREUR, this.getClass().getName());
            if (entFacture != null) {
                getMemoryLog().logMessage(
                        "IdEntete: " + entFacture.getIdEntete() + ", IdExterneRole: " + entFacture.getIdExterneRole(),
                        globaz.framework.util.FWMessage.INFORMATION, this.getClass().getName());
            }
            getTransaction().addErrors(
                    "Erreur dans la création de l'écriture: " + e.getMessage() + ". (" + "IdEntete: "
                            + entFacture.getIdEntete() + ", IdExterneRole: " + entFacture.getIdExterneRole() + ")");
            success = false;

        } finally { // fermer le cursor du manager des entêtes de facture
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
    }

    /**
     * Method _getJournalFromCompta.
     * 
     * @param compta
     * @return APIJournal
     */
    public APIGestionBulletinNeutre _getComptaBulletinNeutre(long progressCounter) {
        boolean doNextCompta = false;
        // utilise la division intégrale pour calculer le numéro de série du
        // journal
        // calcule le numéro de journal à utiliser
        int numeroJournal = 1 + (int) java.lang.Math.floor(progressCounter / elementsParJournal);
        if (currentNumeroJournal != numeroJournal) {
            // fermer le process en cours avant d'en instancier un autre
            if (numeroJournal != 1) { // committer la compta
                if (this._committerCompta(compta, numeroJournal - 1)) {
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
                compta = (APIGestionBulletinNeutre) sessionOsiris.getAPIFor(APIGestionBulletinNeutre.class);
                comptaMemoryLog.setSession((BSession) sessionOsiris);
                // compta.setMessageLog(comptaMemoryLog);
                // compta.setLibelle(passage.getLibelle() + "#" +
                // numeroJournal);
                // compta.setDateValeur(passage.getDateFacturation());
                // compta.setEMailAddress(this.getEMailAddress());
                // compta.setSendCompletionMail(false);
                // compta.setTransaction(getTransaction());
                // compta.setProcess(this);
                compta.createJournal(getSession(), getTransaction(), passage.getLibelle() + "#" + numeroJournal,
                        passage.getDateFacturation());
                currentNumeroJournal = numeroJournal;

                // informer le numéro du journal Osiris dans le passage de
                // facturation
                passage.setIdJournal(compta.getJournal(getSession()).getIdJournal());
            } catch (Exception e) {
                getMemoryLog().logMessage(
                        "Problème dans la création du journal #" + numeroJournal + "de comptabilisation",
                        FWViewBeanInterface.ERROR, this.getClass().getName());
                compta = null;
            }
        }

        return compta;
    }
}
