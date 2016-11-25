package globaz.corvus.process;

import globaz.commons.nss.NSUtil;
import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.annonces.REAnnonceRente;
import globaz.corvus.db.annonces.REAnnoncesDiminution10Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution9Eme;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculDixiemeRevision;
import globaz.corvus.db.basescalcul.REBasesCalculNeuviemeRevision;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteManager;
import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.db.ordresversements.REOrdresVersementsManager;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.rentesaccordees.REPrestationAccordeeManager;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.module.compta.diminution.REModuleComptableDiminution;
import globaz.corvus.utils.REDiminutionRenteUtils;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.utils.SFFamilleUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.db.demandes.PRDemandeManager;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationInvalidite;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationSurvivant;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationVieillesse;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.zip.DataFormatException;

public class REDiminutionRenteAccordeeProcess extends BProcess {

    private static final long serialVersionUID = 8016386030894621119L;

    private String csCodeMutation = "";
    private String csCodeTraitement = "";
    private String dateFinDroit = "";
    private String emailObject = "";
    private String idRenteAccordee = "";
    private final String montant = "";
    private boolean noCommit = false;
    private boolean diminuerAutomatiquementLesRentesVieillesseComplementaires = true;

    public REDiminutionRenteAccordeeProcess() throws Exception {
        this(new BSession(REApplication.DEFAULT_APPLICATION_CORVUS));
        setSendCompletionMail(false);
    }

    public REDiminutionRenteAccordeeProcess(final BProcess parent) throws Exception {
        super(parent);
        setSendCompletionMail(false);
    }

    public REDiminutionRenteAccordeeProcess(final BSession session) throws Exception {
        super(session);
        setSendCompletionMail(false);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        final BTransaction transaction = getTransaction();
        if (!transaction.isOpened()) {
            transaction.openTransaction();
        }

        try {
            checkErrorAndThrowException();

            if (!REPmtMensuel.isValidationDecisionAuthorise(getSession())) {
                String message = getSession().getLabel("ERREUR_PAS_DE_PAIEMENT_MENSUEL_POUR_MOIS_COURANT");
                throw new Exception(message);
            }

            String dateDernierPaiement = REPmtMensuel.getDateDernierPmt(getSession());
            if (REPmtMensuel.DATE_NON_TROUVEE_POUR_DERNIER_PAIEMENT.equals(dateDernierPaiement)) {
                String message = getSession().getLabel("ERREUR_IMPOSSIBLE_RETROUVER_DATE_DERNIER_PAIEMENT");
                throw new RETechnicalException(message);
            }

            JADate date = new JADate(dateDernierPaiement);
            JADate dateFinDroit = new JADate(getDateFinDroit());
            JACalendar cal = new JACalendarGregorian();

            // Calcul du mois de rapport de l'annonce de diminution et la récap.
            String dateMoisRapport = "";
            if (JACalendar.COMPARE_SECONDUPPER == cal.compare(date, dateFinDroit)) {
                dateFinDroit = cal.addMonths(dateFinDroit, 1);
                dateMoisRapport = PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dateFinDroit.toStrAMJ());
            } else {
                date = cal.addMonths(date, 1);
                dateMoisRapport = PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(date.toStrAMJ());
            }

            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession(getSession());
            ra.setIdPrestationAccordee(getIdRenteAccordee());
            ra.retrieve(transaction);

            PRTiersWrapper tiersBeneficiaire = PRTiersHelper.getTiersParId(getSession(), ra.getIdTiersBeneficiaire());
            if (tiersBeneficiaire != null) {
                emailObject = getSession().getLabel("MAIL_TITRE_DIMINUTION_RENTE_PROCESS_TIERS");
                emailObject = emailObject.replace("{0}", tiersBeneficiaire.getDescription(getSession()));
            } else {
                emailObject = getSession().getLabel("MAIL_TITRE_DIMINUTION_RENTE_PROCESS_ID");
                emailObject = emailObject.replace("{0}", ra.getIdPrestationAccordee());
            }

            FWMemoryLog log = new FWMemoryLog();

            // bz-4369 et isNew
            if (!transaction.hasErrors()) {
                checkReferenceSurRA(getSession(), transaction, ra);

                // Diminution de la rente accordée et création des annonces de récap
                log = REModuleComptableDiminution.getInstance(getSession()).diminuerRenteAccordee(this,
                        getDateFinDroit(), ra, getCsCodeTraitement(), dateMoisRapport);

            }

            setMemoryLog(log);
            checkErrorAndThrowException();

            // BZ 4667
            // Si le conjoint (marié) reçoit également une rente 10, 13, 20, 23, 50 ou 70
            // l'utilisateur sera notifié qu'il y a une incohérence
            PRTiersWrapper conjoint = SFFamilleUtils.getConjointActuel(getSession(),
                    ISFSituationFamiliale.CS_DOMAINE_RENTES, ra.getIdTiersBeneficiaire());

            // TODO : remplacer le teste pour les API par quelque chose de plus propre
            if ((conjoint != null) && (Integer.parseInt(ra.getCodePrestation()) < 80)) {
                // récupération des prestations en cours pour le conjoint
                REPrestationAccordeeManager prestationManager = new REPrestationAccordeeManager();
                prestationManager.setSession(getSession());
                prestationManager.setForIdTiersBeneficiaire(conjoint.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                StringBuilder currentMonthBuilder = new StringBuilder();
                Calendar calendar = Calendar.getInstance();
                int month = calendar.get(Calendar.MONTH) + 1;
                if (month < 10) {
                    currentMonthBuilder.append("0");
                }
                currentMonthBuilder.append(month).append(".").append(calendar.get(Calendar.YEAR));
                prestationManager.setForEnCoursAtMois(currentMonthBuilder.toString());

                prestationManager.find(getTransaction());

                // on parcours les prestations (en cours) accordées au conjoint du tiers
                for (Iterator<Object> iterator = prestationManager.iterator(); iterator.hasNext();) {
                    REPrestationsAccordees prestationsAccordees = (REPrestationsAccordees) iterator.next();

                    String codePrestation = prestationsAccordees.getCodePrestation();
                    if (JadeStringUtil.isIntegerEmpty(codePrestation)) {
                        String message = getSession().getLabel("ERREUR_ID_RENTE_ACCORDEE_EST_VIDE");
                        throw new RETechnicalException(message);
                    }

                    /*
                     * si code prestation 10, 13, 20, 23, 50 ou 70, notification de l'utilisateur qu'il y a une rente à
                     * recalculer
                     */
                    List<String> codesPrestations = new ArrayList<String>();
                    codesPrestations.add(PRCodePrestationVieillesse._10.getCodePrestationAsString());
                    codesPrestations.add(PRCodePrestationVieillesse._20.getCodePrestationAsString());
                    codesPrestations.add(PRCodePrestationSurvivant._13.getCodePrestationAsString());
                    codesPrestations.add(PRCodePrestationSurvivant._23.getCodePrestationAsString());
                    codesPrestations.add(PRCodePrestationInvalidite._50.getCodePrestationAsString());
                    codesPrestations.add(PRCodePrestationInvalidite._70.getCodePrestationAsString());

                    if (codesPrestations.contains(codePrestation)) {
                        StringBuilder infoMessage = new StringBuilder();
                        infoMessage.append(getSession().getLabel("WARNING_RA_CONJOINT_RENTE_DIMINUEE_DEBUT"));
                        infoMessage.append(conjoint.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                        infoMessage.append(" ").append(conjoint.getProperty(PRTiersWrapper.PROPERTY_NOM));
                        infoMessage.append(" ").append(conjoint.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
                        infoMessage.append(" ").append(getSession().getLabel("WARNING_RA_CONJOINT_RENTE_DIMINUEE_FIN"));
                        getMemoryLog().logMessage(infoMessage.toString(), FWViewBeanInterface.WARNING,
                                this.getClass().getSimpleName());
                        // Pas d'ajout d'erreur car ce n'est qu'un warning pour l'utilisateur
                    }
                }
            }

            // On contrôle si aucune erreur ne s'est glisée à quelque part !
            checkErrorAndThrowException();

            // Génération de l'annonce de diminution et maj de la RA
            // (Si toute les RA de la demande sont diminuées, mettre une date de
            // fin à cette demande, avec la plus grande date
            // de fin de toute les RA.)
            doTraitementAnnonceDiminution(transaction, ra, dateMoisRapport, getCsCodeMutation());

            // On contrôle si aucune erreur ne s'est glisée à quelque part !
            checkErrorAndThrowException();

            // MAJ de la date de fin de la demande, si toutes les RA ont été
            // diminuées
            REDiminutionRenteUtils.majDemandePourDiminution(getSession(), transaction, ra);

            // On contrôle si aucune erreur ne s'est glissée quelque part !
            checkErrorAndThrowException();

            // bz-4434
            RERenteAccordee raDiminuee = new RERenteAccordee();
            raDiminuee.setSession(getSession());
            raDiminuee.setIdPrestationAccordee(ra.getIdPrestationAccordee());
            raDiminuee.retrieve(transaction);

            if (diminuerAutomatiquementLesRentesVieillesseComplementaires
                    && (REGenresPrestations.GENRE_10.equals(ra.getCodePrestation()) || REGenresPrestations.GENRE_20
                            .equals(ra.getCodePrestation()))) {

                PRDemandeManager demPrestMgr = new PRDemandeManager();
                demPrestMgr.setSession(getSession());
                demPrestMgr.setForIdTiers(ra.getIdTiersBeneficiaire());
                demPrestMgr.setForTypeDemande(IPRDemande.CS_TYPE_RENTE);
                demPrestMgr.find(transaction);

                /*
                 * Pour chacune des demande de prestations trouvée, récupérer les rente complémentaire active des
                 * demandes de rentes du tiersBeneficiaire de la rente principale.
                 */

                if (!demPrestMgr.isEmpty()) {
                    REDemandeRenteManager demMgr = new REDemandeRenteManager();
                    demMgr.setSession(getSession());
                    demMgr.setForIdDemandePrestation(((PRDemande) demPrestMgr.getFirstEntity()).getIdDemande());
                    demMgr.find(transaction);

                    for (int i = 0; i < demMgr.size(); i++) {
                        REDemandeRente dem = (REDemandeRente) demMgr.getEntity(i);

                        RERenteAccJoinTblTiersJoinDemRenteManager mgr2 = new RERenteAccJoinTblTiersJoinDemRenteManager();
                        mgr2.setSession(getSession());
                        mgr2.setForNoDemandeRente(dem.getIdDemandeRente());
                        mgr2.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE + ", "
                                + IREPrestationAccordee.CS_ETAT_PARTIEL);
                        mgr2.find(transaction);
                        for (int j = 0; j < mgr2.size(); j++) {
                            RERenteAccJoinTblTiersJoinDemandeRente elm = (RERenteAccJoinTblTiersJoinDemandeRente) mgr2
                                    .getEntity(j);
                            if (!JadeStringUtil.isBlankOrZero(elm.getDateFinDroit())) {
                                continue;
                            } else if (REGenresPrestations.GENRE_33.equals(elm.getCodePrestation())
                                    || REGenresPrestations.GENRE_34.equals(elm.getCodePrestation())
                                    || REGenresPrestations.GENRE_35.equals(elm.getCodePrestation())
                                    || REGenresPrestations.GENRE_36.equals(elm.getCodePrestation())
                                    || REGenresPrestations.GENRE_45.equals(elm.getCodePrestation())) {

                                RERenteAccordee racc = new RERenteAccordee();
                                racc.setSession(getSession());
                                racc.setIdPrestationAccordee(elm.getIdPrestationAccordee());
                                racc.retrieve(transaction);

                                // Do traitement
                                checkReferenceSurRA(getSession(), transaction, racc);

                                REModuleComptableDiminution moduleComptableDiminution = REModuleComptableDiminution
                                        .getInstance(getSession());
                                FWMemoryLog log2 = moduleComptableDiminution.diminuerRenteAccordee(this,
                                        getDateFinDroit(), racc, getCsCodeTraitement(), dateMoisRapport);

                                int s = log2.getMessagesToVector().size();
                                for (int k = 0; k < s; k++) {
                                    getMemoryLog().logMessage(log2.getMessage(k));
                                }

                                /*
                                 * Bon, si des erreurs sont remontées dans le memoryLog -> on lance une exception Ce qui
                                 * est cool c'est qu'on ne sait pas quel est le message correspondant donc on balance
                                 * tout
                                 */
                                if (log2.hasErrors()) {
                                    StringBuilder message = new StringBuilder();
                                    message.append(getSession().getLabel("ERREUR_REMONTEE_MODULE_COMPTABLE_DIMINUTION"));
                                    for (int ctr = 0; ctr < s; ctr++) {
                                        message.append(log2.getMessage(ctr));
                                        if (ctr < (s - 1)) {
                                            message.append(" - ");
                                        }
                                    }
                                }

                                PRTiersWrapper tw = PRTiersHelper.getTiersParId(getSession(),
                                        racc.getIdTiersBeneficiaire());

                                // OK, ce n'est pas un warning mais bon...
                                String msg = getSession().getLabel("DIMINUTION_AUTO_RENTE_COMPL")
                                        + tw.getDescription(getSession()) + " - " + racc.getCodePrestation() + "/"
                                        + racc.getMontantPrestation();
                                getMemoryLog().logMessage(msg, FWViewBeanInterface.WARNING,
                                        "REDiminutionRenteAccordeeProcess");

                                // On contrôle si aucune erreur ne s'est glissée quelque part !
                                checkErrorAndThrowException();

                                /*
                                 * Génération de l'annonce de diminution et maj de la RA Si toute les RA de la demande
                                 * sont diminuées --> mettre une date de fin à cette demande avec la plus grande date de
                                 * fin de toute les RA.
                                 */
                                doTraitementAnnonceDiminution(transaction, racc, dateMoisRapport,
                                        IREAnnonces.CS_CODE_MUTATION_EVENEMENT_PROCHE_FAM);

                                // On contrôle si aucune erreur ne s'est glissée quelque part !
                                checkErrorAndThrowException();

                                // MAJ de la date de fin de la demande, si
                                // toutes les RA ont été diminuées
                                REDiminutionRenteUtils.majDemandePourDiminution(getSession(), transaction, racc);
                            }
                        }
                    }
                }
            }

            if (ra.contientCodeCasSpecial("02")) {
                String message = getSession().getLabel("DIMINUTION_CODE_CAS_SPECIAL_02");
                message = message.replace("{0}", ra.getIdPrestationAccordee());
                message = "<strong>" + message + "</strong>";
                getMemoryLog().logMessage(message, FWViewBeanInterface.WARNING,
                        REDiminutionRenteAccordeeProcess.class.getSimpleName());
            }

            if (ra.contientCodeCasSpecial("05")) {
                String message = getSession().getLabel("DIMINUTION_CODE_CAS_SPECIAL_05");
                message = message.replace("{0}", ra.getIdPrestationAccordee());
                message = "<strong>" + message + "</strong>";
                getMemoryLog().logMessage(message, FWViewBeanInterface.WARNING,
                        REDiminutionRenteAccordeeProcess.class.getSimpleName());
            }

            if (REDiminutionRenteUtils.hasMontantAjournement(ra)) {
                String message = getSession().getLabel("DIMINUTION_RENTE_MONTANT_AJOURNEMENT");
                message = "<strong>" + message.replace("{0}", ra.getIdPrestationAccordee()) + "</strong>";
                getMemoryLog().logMessage(message, FWViewBeanInterface.WARNING,
                        REDiminutionRenteAccordeeProcess.class.getSimpleName());
            }

            if (REDiminutionRenteUtils.hasMontantAnticipation(ra)) {
                String message = getSession().getLabel("DIMINUTION_RENTE_MONTANT_ANTICIPATION");
                message = "<strong>" + message.replace("{0}", ra.getIdPrestationAccordee()) + "</strong>";
                getMemoryLog().logMessage(message, FWViewBeanInterface.WARNING,
                        REDiminutionRenteAccordeeProcess.class.getSimpleName());
            }

            if (hasError()) {
                emailObject = getSession().getCodeLibelle(FWMessage.AVERTISSEMENT) + " : " + emailObject;
                setSendCompletionMail(true);
                getMemoryLog().logMessage(getErrorMessages(), FWViewBeanInterface.WARNING,
                        "REDiminutionRenteAccordeeProcess");
            }
        } catch (Exception e) {
            setSendCompletionMail(true);
            emailObject = getSession().getCodeLibelle(FWMessage.ERREUR) + " : " + emailObject;
            addErrorMessage(getSession().getLabel("ERREUR_DIMINUTION_RA") + " : " + e.toString(), true);
            JadeLogger.error(this, e);
            return false;
        } finally {
            if ((transaction != null) && !isNoCommit()) {
                try {
                    if (hasError() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
        return true;
    }

    @Override
    protected void _validate() throws Exception {

        setSendMailOnError(true);
        setControleTransaction(getTransaction() == null);

        // On contrôle que la RA n'est pas déjà diminuée;
        // Evite le lancement simultané de ce process pour la même RA au cas ou
        // l'utilisateur serait atteint
        // d'une parkinsonite aïgue !!!

        RERenteAccordee ra = new RERenteAccordee();
        ra.setSession(getSession());
        ra.setIdPrestationAccordee(getIdRenteAccordee());
        ra.retrieve();
        if (ra.isNew()) {
            String errorMessage = getSession().getLabel("ERREUR_IMPOSSIBLE_RETROUVER_RENTE_ACCORDEE_AVEC_ID");
            errorMessage = errorMessage.replace("{id}", getIdRenteAccordee());
            addErrorMessage(errorMessage, true);
        }

        if (IREPrestationAccordee.CS_ETAT_DIMINUE.equals(ra.getCsEtat())) {
            String errorMessage = getSession().getLabel("ERREUR_RA_DEJA_DIMINUEE");
            addErrorMessage(errorMessage, true);
        }

        // bz7784
        if (getSession().hasErrors()) {
            throw new DataFormatException(getSession().getErrors().toString());
        }
    }

    /**
     * Ajoute un message d'erreur dans la session et la transaction Le message sera ajouté tel quel, ausun traduction
     * sera réalisée
     * 
     * @param errorMessage
     *            Le message traduit à insérer
     * @param addToMemoryLogToo
     *            Si le message doit également être inséré dans le MemoryLog en tant qu'erreur
     */
    private void addErrorMessage(final String errorMessage, final boolean addToMemoryLogToo) {
        getSession().addError(errorMessage);
        getTransaction().addErrors(errorMessage);
        if (addToMemoryLogToo) {
            getMemoryLog().logMessage(errorMessage, FWMessage.ERREUR, this.getClass().getSimpleName());
        }
    }

    /**
     * Contrôle si des erreurs sont présentes dans la session, dans la transaction et dans le MemoryLog et lance une
     * exception si des erreurs sont détectées
     * 
     * @throws Exception
     */
    private void checkErrorAndThrowException() throws Exception {
        if (hasError()) {
            String message = getSession().getLabel(
                    "ERREUR_IMPOSSIBLE_DIMINUER_RENTE_ACCORDEE_AVEC_ID_A_CAUSE_DES_ERREURS_SUIVANTES");
            message = message.replace("{id}", getIdRenteAccordee());
            message += getErrorMessages();
            throw new Exception(message);
        }
    }

    /**
     * Avant de lancer la diminution, on s'assure que cette RA n'appartient à aucune Décision non validée, c'est à dire
     * en cours de traitement. Le lot n'est pas encore comptabilisé.
     * 
     * @throws Exception
     *             en cas d'erreur
     */
    private void checkReferenceSurRA(final BSession session, final BTransaction transaction, final RERenteAccordee ra)
            throws Exception {
        REOrdresVersementsManager mgr = new REOrdresVersementsManager();
        mgr.setSession(getSession());
        mgr.setForIdRAD(ra.getIdPrestationAccordee());
        mgr.find(transaction, BManager.SIZE_NOLIMIT);

        for (int i = 0; i < mgr.size(); i++) {
            REOrdresVersements ov = (REOrdresVersements) mgr.getEntity(i);

            REPrestations prestation = new REPrestations();
            prestation.setSession(getSession());
            prestation.setIdPrestation(ov.getIdPrestation());
            prestation.retrieve(transaction);

            REDecisionEntity decision = new REDecisionEntity();
            decision.setSession(getSession());
            decision.setIdDecision(prestation.getIdDecision());
            decision.retrieve(transaction);

            if (!decision.isNew()
                    && (IREDecision.CS_ETAT_ATTENTE.equals(decision.getCsEtat()) || IREDecision.CS_ETAT_PREVALIDE
                            .equals(decision.getCsEtat()))) {

                throw new Exception(
                        "Cette rente est ratachée à une décision non validée. Veuillez la comptabiliser ou la supprimer avant de diminuer cette rente.");
            }
        }
    }

    private void doTraitementAnnonceDiminution(final BITransaction transaction, final RERenteAccordee ra,
            final String dateMoisRapport, final String codeMutation) throws Exception {

        REBasesCalculDixiemeRevision bc10 = new REBasesCalculDixiemeRevision();
        REBasesCalculNeuviemeRevision bc9 = new REBasesCalculNeuviemeRevision();
        REBasesCalcul bc = new REBasesCalcul();

        bc.setIdBasesCalcul(ra.getIdBaseCalcul());
        bc.setSession(getSession());
        bc.retrieve(transaction);

        String noRevision;
        if (bc.isNew()) {
            throw new Exception(getSession().getLabel("EXCEPTION_RETRIEVE_BASECALCUL_REDIMINUTIONLISTERENTEACCORDEE")
                    + " (REDiminutionListeRenteAccordeeProcess)");
        } else {
            bc10.setSession(getSession());
            bc10.setIdBasesCalcul(bc.getIdBasesCalcul());
            bc10.retrieve(transaction);
            if (bc10.isNew()) {
                bc9.setSession(getSession());
                bc9.setIdBasesCalcul(bc.getIdBasesCalcul());
                bc9.retrieve(transaction);
                if (bc9.isNew()) {
                    throw new Exception(getSession().getLabel(
                            "EXCEPTION_RETRIEVE_BASECALCUL_REDIMINUTIONLISTERENTEACCORDEE")
                            + " (REDiminutionListeRenteAccordeeProcess)");
                } else {
                    noRevision = "9";
                }
            } else {
                noRevision = "10";
            }
        }

        PRTiersWrapper tier = PRTiersHelper.getTiersParId(getSession(), ra.getIdTiersBeneficiaire());

        if (null == tier) {
            throw new Exception(getSession().getLabel("EXCEPTION_TIERS_REDIMINUTIONLISTERENTEACCORDEE")
                    + " (REDiminutionListeRenteAccordeeProcess)");
        }

        String idAnnonce = "";

        if (noRevision.equals("10")) {
            // Créer annonce pour 10 ème révision si bc = 10ème révision

            REAnnoncesDiminution10Eme annonce10Eme = new REAnnoncesDiminution10Eme();
            annonce10Eme.setSession(getSession());
            annonce10Eme.setCodeApplication("45");
            annonce10Eme.setCodeEnregistrement01("01");
            annonce10Eme.setNumeroCaisse(getSession().getApplication().getProperty("noCaisse"));
            annonce10Eme.setNumeroAgence(getSession().getApplication().getProperty("noAgence"));
            annonce10Eme.setNumeroAnnonce("");
            annonce10Eme.setReferenceCaisseInterne("DIM" + getSession().getUserId().toUpperCase());
            annonce10Eme
                    .setNoAssAyantDroit(NSUtil.unFormatAVS(tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
            annonce10Eme.setGenrePrestation(ra.getCodePrestation());

            int lengthMontant = String.valueOf(new FWCurrency(ra.getMontantPrestation()).intValue()).length();
            int nbZeroAajouter = 5 - lengthMontant;

            String montant = String.valueOf(new FWCurrency(ra.getMontantPrestation()).intValue());

            for (int i = 0; i < nbZeroAajouter; i++) {
                montant = "0" + montant;
            }

            annonce10Eme.setMensualitePrestationsFrancs(montant);
            annonce10Eme.setFinDroit(PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater
                    .convertDate_MMxAAAA_to_AAAAMM(getDateFinDroit())));
            annonce10Eme.setMoisRapport(PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater
                    .convertDate_MMxAAAA_to_AAAAMM(dateMoisRapport)));
            annonce10Eme.setCodeMutation(getSession().getCode(codeMutation));
            annonce10Eme.setEtat(IREAnnonces.CS_ETAT_OUVERT);
            annonce10Eme.setIdTiers(tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            annonce10Eme.add(transaction);

            idAnnonce = annonce10Eme.getIdAnnonce();

        } else if (noRevision.equals("9")) {
            // Créer annonce pour 9 ème révision si bc = 9ème révision

            REAnnoncesDiminution9Eme annonce9Eme = new REAnnoncesDiminution9Eme();
            annonce9Eme.setSession(getSession());
            annonce9Eme.setCodeApplication("42");
            annonce9Eme.setCodeEnregistrement01("01");
            annonce9Eme.setNumeroCaisse(getSession().getApplication().getProperty("noCaisse"));
            annonce9Eme.setNumeroAgence(getSession().getApplication().getProperty("noAgence"));
            annonce9Eme.setNumeroAnnonce("");
            annonce9Eme.setReferenceCaisseInterne("DIM" + getSession().getUserId().toUpperCase());
            annonce9Eme
                    .setNoAssAyantDroit(NSUtil.unFormatAVS(tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
            annonce9Eme.setGenrePrestation(ra.getCodePrestation());

            int lengthMontant = String.valueOf(new FWCurrency(ra.getMontantPrestation()).intValue()).length();
            int nbZeroAajouter = 5 - lengthMontant;

            String montant = String.valueOf(new FWCurrency(ra.getMontantPrestation()).intValue());

            for (int i = 0; i < nbZeroAajouter; i++) {
                montant = "0" + montant;
            }

            annonce9Eme.setMensualitePrestationsFrancs(montant);
            annonce9Eme.setFinDroit(PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater
                    .convertDate_MMxAAAA_to_AAAAMM(getDateFinDroit())));
            annonce9Eme.setMoisRapport(PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater
                    .convertDate_MMxAAAA_to_AAAAMM(dateMoisRapport)));
            annonce9Eme.setCodeMutation(getSession().getCode(codeMutation));
            annonce9Eme.setIdTiers(tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            annonce9Eme.setEtat(IREAnnonces.CS_ETAT_OUVERT);

            annonce9Eme.add(transaction);

            idAnnonce = annonce9Eme.getIdAnnonce();

        } else {
            String messageError = getSession().getLabel("ERREUR_DIMINUTION_RA") + " No révision = " + noRevision;
            transaction.addErrors(messageError);
            getMemoryLog().logMessage(messageError, FWMessage.ERREUR, "REDiminutionRenteAccordeeProcess");
        }

        // Création de l'annonce de rente pour la liaison entre les ra et les
        // annonces
        REAnnonceRente annonceRente = new REAnnonceRente();
        annonceRente.setSession(getSession());
        annonceRente.setIdAnnonceHeader(idAnnonce);
        annonceRente.setCsEtat(IREAnnonces.CS_ETAT_OUVERT);
        annonceRente.setCsTraitement(IREAnnonces.CS_CODE_EN_COURS);
        annonceRente.setIdRenteAccordee(ra.getIdPrestationAccordee());
        annonceRente.add(transaction);

        // Mise à jour de la rente accordée (Etat = Diminué + nouvelle date de
        // fin + code mutation)
        if (!transaction.hasErrors()) {
            ra.retrieve(transaction);
            ra.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
            ra.setCodeMutation(getSession().getCode(codeMutation));
            ra.setDateFinDroit(getDateFinDroit());
            // Pourrait l'être, donc on la reset à non bloquée.
            ra.setIsPrestationBloquee(Boolean.FALSE);
            ra.setTypeDeMiseAJours("0");
            ra.setDateEcheance("");
            ra.update(transaction);
        }

        if (transaction.hasErrors()) {
            throw new Exception(transaction.getErrors().toString());
        }
    }

    /**
     * Récupère le code de mutation
     * 
     * @return
     */
    public String getCsCodeMutation() {
        return csCodeMutation;
    }

    /**
     * Récupère le code de traitement
     * 
     * @return
     */
    public String getCsCodeTraitement() {
        return csCodeTraitement;
    }

    /**
     * Récuppère la date de fin du droit à la rente
     * 
     * @return
     */
    public String getDateFinDroit() {
        return dateFinDroit;
    }

    @Override
    public String getEMailObject() {
        return emailObject;
    }

    /**
     * Recherche les messages d'erreurs existants dans la session et la transaction
     */
    private String getErrorMessages() {
        /**
         * Pour info, ce code n'est pas merveilleux... Mais bon, on ne fait pas une orange avec une pomme de terre !!
         */
        String transactionErrors = getTransaction().getErrors().toString();
        String sessionErrors = getTransaction().getErrors().toString();

        if (transactionErrors == null) {
            if (sessionErrors == null) {
                return "";
            } else {
                return sessionErrors;
            }
        } else if (sessionErrors == null) {
            return transactionErrors;
        }

        if (transactionErrors.equals(sessionErrors)) {
            return transactionErrors;
        } else {
            return "Session errors : " + sessionErrors + " Transaction errors : " + transactionErrors;
        }
    }

    /**
     * Récupère l'id de la rente accordée
     * 
     * @return
     */
    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    /**
     * Récupère le montant de la rente accordée
     * 
     * @return
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @return
     */
    public BSpy getSpy() {
        return null;
    }

    /**
     * Contrôle si des erreurs sont présentes dans la session, dans la transaction et dans le MemoryLog
     * 
     * @return <code>true</code> si des erreurs sont présentes dans la session ou dans la transaction
     */
    private boolean hasError() {
        return getSession().hasErrors() || getTransaction().hasErrors() || getMemoryLog().hasErrors();
    }

    /**
     * Définit si la transaction est gérée (commit/rollback) dans ce process ou dans le process parent
     * Si la méthode retourne 'true' cela veux dire que la transaction est gèrée dans le process parent
     * 
     * @return
     */
    public boolean isNoCommit() {
        return noCommit;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Modifie le code de mutation
     * 
     * @param newCodeMutation
     */
    public void setCsCodeMutation(final String newCodeMutation) {
        csCodeMutation = newCodeMutation;
    }

    /**
     * Modifie le cote de traitement
     * 
     * @param newCodeTraitement
     */
    public void setCsCodeTraitement(final String newCodeTraitement) {
        csCodeTraitement = newCodeTraitement;
    }

    /**
     * Modifie la date de fin du droit à la rente
     * 
     * @param newDateFinDroit
     */
    public void setDateFinDroit(final String newDateFinDroit) {
        dateFinDroit = newDateFinDroit;
    }

    /**
     * Modification de l'id rente accordée
     * 
     * @param newIdRenteAccordee
     */
    public void setIdRenteAccordee(final String newIdRenteAccordee) {
        idRenteAccordee = newIdRenteAccordee;
    }

    public void setNoCommit(final boolean noCommit) {
        this.noCommit = noCommit;
    }

    public void setIsDiminuerAutomatiquementLesRentesVieillesseComplementaires(
            Boolean diminuerAutomatiquementLesRentesVieillesseComplementaires) {
        this.diminuerAutomatiquementLesRentesVieillesseComplementaires = diminuerAutomatiquementLesRentesVieillesseComplementaires;
    }

    public Boolean getIsDiminuerAutomatiquementLesRentesVieillesseComplementaires() {
        return diminuerAutomatiquementLesRentesVieillesseComplementaires;
    }
}
