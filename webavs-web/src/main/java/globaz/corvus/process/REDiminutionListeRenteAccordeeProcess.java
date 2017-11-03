package globaz.corvus.process;

import globaz.commons.nss.NSUtil;
import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.annonces.REAnnonceRente;
import globaz.corvus.db.annonces.REAnnoncesDiminution10Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution9Eme;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculDixiemeRevision;
import globaz.corvus.db.basescalcul.REBasesCalculNeuviemeRevision;
import globaz.corvus.db.rentesaccordees.REEnteteBlocage;
import globaz.corvus.db.rentesaccordees.REListerEcheanceRenteJoinMembresFamille;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.module.compta.diminution.REModuleComptableDiminution;
import globaz.corvus.utils.REDiminutionRenteUtils;
import globaz.corvus.utils.REEcheancesUtils;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.enums.CommunePolitique;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import ch.globaz.common.properties.CommonProperties;

public class REDiminutionListeRenteAccordeeProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csCodeMutation = "";
    private String csCodeTraitement = "";
    private String dateDebutDroit = "";
    private String dateFinDroit = "";
    private REListerEcheanceRenteJoinMembresFamille echeanceCourrante = null;
    private Iterator echeancesIterator = null;
    private String genreDiminution = "";
    private String idRenteAccordee = "";
    private String idTiersBeneficiaire = "";
    private List<REListerEcheanceRenteJoinMembresFamille> listeEcheances = null;
    private String montant = "";
    private int nbDiminution = 0;
    private String tiersBeneficiaireInfo = "";

    public REDiminutionListeRenteAccordeeProcess() throws Exception {
        this(new BSession(REApplication.DEFAULT_APPLICATION_CORVUS));
    }

    public REDiminutionListeRenteAccordeeProcess(BProcess parent) throws Exception {
        super(parent);
    }

    public REDiminutionListeRenteAccordeeProcess(BSession session) throws Exception {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        boolean isError = false;
        BITransaction transaction = getSession().newTransaction();

        if (!transaction.isOpened()) {
            transaction.openTransaction();
        }

        try {

            if (getListeEcheances().isEmpty()) {
                getMemoryLog().logMessage(
                        getSession().getLabel("ERREUR_LISTEECHEANCEVIDE_REDIMINUTIONLISTERENTEACCORDEE"),
                        FWMessage.ERREUR, getClass().getSimpleName());
            } else {

                echeancesIterator = getListeEcheances().iterator();

                while (echeancesIterator.hasNext()) {
                    echeanceCourrante = (REListerEcheanceRenteJoinMembresFamille) echeancesIterator.next();

                    RERenteAccordee renteAcc = new RERenteAccordee();
                    renteAcc.setSession(getSession());
                    renteAcc.setId(echeanceCourrante.getIdRenteAccordee());
                    renteAcc.setIdTiersBeneficiaire(echeanceCourrante.getIdTiers());
                    renteAcc.retrieve();

                    // Inforom 483 : Ne pas diminuer la rente automatiquement si :
                    // la RA contient un code cas spécial 02 OU 05
                    // la RA possède un montant d'ajournement
                    // la RA possède un montant d'anticipation

                    boolean doDiminuerLaRente = true;
                    String message = "";

                    // Si la RA est New, elle sera détectée dans le process du coup on la laisse passer
                    if (!renteAcc.isNew()) {
                        if (renteAcc.contientCodeCasSpecial("02")) {// On ne traite pas la ra
                            message = ajouteNomPrenomNSS(message);
                            doDiminuerLaRente = false;
                            message += getSession().getLabel("DIMINUTION_RENTE_ENFANT_CODE_CAS_SPECIAL_02");
                            message = "<strong>" + message + "</strong>";
                        }
                        if (renteAcc.contientCodeCasSpecial("05")) {
                            message = ajouteNomPrenomNSS(message);
                            doDiminuerLaRente = false;
                            message += getSession().getLabel("DIMINUTION_RENTE_ENFANT_CODE_CAS_SPECIAL_05");
                            message = "<strong>" + message + "</strong>";
                        }
                        if (REDiminutionRenteUtils.hasMontantAjournement(renteAcc)) {
                            message = ajouteNomPrenomNSS(message);
                            doDiminuerLaRente = false;
                            message += getSession().getLabel("DIMINUTION_RENTE_ENFANT_POSSEDE_MONTANT_AJOURNEMENT");
                            message = "<strong>" + message + "</strong>";
                        }
                        if (REDiminutionRenteUtils.hasMontantAnticipation(renteAcc)) {
                            message = ajouteNomPrenomNSS(message);
                            doDiminuerLaRente = false;
                            message += getSession().getLabel("DIMINUTION_RENTE_ENFANT_POSSEDE_MONTANT_ANTICIPATION");
                            message = "<strong>" + message + "</strong>";
                        }
                    }

                    // On ne traite pas la ra
                    if (!doDiminuerLaRente) {
                        getMemoryLog().logMessage(
                                message + "\r\n" + " R" + echeanceCourrante.getCodePrestation()
                                        + " ID Rente accordée : " + echeanceCourrante.getIdRenteAccordee() + "\r\n",
                                FWMessage.INFORMATION, this.getClass().getSimpleName());
                    }

                    // Traitement normal
                    else {
                        // Récupération de l'entête de blocage (si existant) pour tester si la prestation est bloquée
                        REEnteteBlocage enteteBlocage = null;
                        if (!renteAcc.isNew() && !JadeStringUtil.isBlankOrZero(renteAcc.getIdEnteteBlocage())) {
                            enteteBlocage = new REEnteteBlocage();
                            enteteBlocage.setSession(getSession());
                            enteteBlocage.setIdEnteteBlocage(renteAcc.getIdEnteteBlocage());
                            enteteBlocage.retrieve(transaction);
                            if (enteteBlocage.isNew()) {
                                enteteBlocage = null;
                            }
                        }

                        if (renteAcc.isNew()) {
                            getMemoryLog().logMessage(
                                    getSession().getLabel("ERREUR_AUCUNERENTE_RELISTERECHEANCESPROCESS") + " "
                                            + echeanceCourrante.getNss() + " " + echeanceCourrante.getNom() + " "
                                            + echeanceCourrante.getPrenom(), FWMessage.ERREUR,
                                    getClass().getSimpleName());
                        } else if (!JadeStringUtil.isBlankOrZero(renteAcc.getDateFinDroit())) {
                            // BZ 5708 : les RA ayant déjà une date de fin de droit ne sont pas (re-)diminuées
                            getMemoryLog().logMessage(
                                    getSession().getLabel("ERREUR_RENTE_DEJA_DIMINUEE") + " R"
                                            + echeanceCourrante.getCodePrestation() + " ID:"
                                            + echeanceCourrante.getIdRenteAccordee() + " / "
                                            + echeanceCourrante.getNss() + " - " + echeanceCourrante.getNom() + " "
                                            + echeanceCourrante.getPrenom(), FWMessage.INFORMATION,
                                    this.getClass().getSimpleName());
                        } else if (REEcheancesUtils.isPrestationBloquee(renteAcc, enteteBlocage)) {
                            // Si la prestation est bloquée, elle doit être diminuée manuellement
                            StringBuilder messageErreur = new StringBuilder("\n");
                            messageErreur.append("<strong>" + getSession().getLabel("ERREUR_RENTE_BLOQUEE")
                                    + "</strong>");
                            messageErreur.append("\n&nbsp;R").append(echeanceCourrante.getCodePrestation());
                            messageErreur.append("\n&nbsp;ID: ").append(echeanceCourrante.getIdRenteAccordee());
                            messageErreur.append("\n&nbsp;NSS: ").append(echeanceCourrante.getNss());
                            messageErreur.append("\n&nbsp;Nom - Prénom : ").append(echeanceCourrante.getNom())
                                    .append("&nbsp").append(echeanceCourrante.getPrenom());

                            getMemoryLog().logMessage(messageErreur.toString(), FWMessage.INFORMATION,
                                    this.getClass().getSimpleName());
                        } else {

                            setIdRenteAccordee(echeanceCourrante.getIdRenteAccordee());
                            setDateDebutDroit(echeanceCourrante.getDateDebutDroit());
                            setGenreDiminution(renteAcc.getCsGenre());
                            setIdTiersBeneficiaire(echeanceCourrante.getIdTiers());
                            setMontant(renteAcc.getMontantPrestation());

                            // Calcul du mois de rapport de l'annonce de diminution
                            // et la récap.
                            String dateMoisRapport = "";
                            JADate date = new JADate(REPmtMensuel.getDateDernierPmt(getSession()));
                            JACalendar cal = new JACalendarGregorian();
                            date = cal.addMonths(date, 1);
                            dateMoisRapport = PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(date.toStrAMJ());

                            transaction.openTransaction();
                            RERenteAccordee ra = new RERenteAccordee();
                            ra.setSession(getSession());
                            ra.setIdPrestationAccordee(getIdRenteAccordee());
                            ra.retrieve(transaction);
                            PRAssert.notIsNew(ra, null);

                            FWMemoryLog log = REModuleComptableDiminution.getInstance(getSession())
                                    .diminuerRenteAccordee(this, getDateFinDroit(), ra, getCsCodeTraitement(),
                                            dateMoisRapport);

                            setNbDiminution(getNbDiminution() + 1);
                            if (getNbDiminution() == 1) {

                                if (getListeEcheances().size() > 1) {
                                    getMemoryLog().logMessage(
                                            getSession().getLabel("MAIL_DIMINUTIONS_SUPPRESSIONRENTE25ANSPROCESS")
                                                    + "\n", FWMessage.INFORMATION, getClass().getSimpleName());
                                } else {
                                    getMemoryLog().logMessage(
                                            getSession().getLabel("MAIL_DIMINUTION_SUPPRESSIONRENTE25ANSPROCESS")
                                                    + "\n", FWMessage.INFORMATION, getClass().getSimpleName());
                                }
                            }
                            getMemoryLog().logMessage(log);

                            // Si toute les RA de la demande sont diminuées, mettre
                            // une date de fin à cette demande, avec la plus grande
                            // date
                            // de fin de toute les RA.

                            // Génération de l'annonce de diminution

                            REBasesCalculDixiemeRevision bc10 = new REBasesCalculDixiemeRevision();
                            REBasesCalculNeuviemeRevision bc9 = new REBasesCalculNeuviemeRevision();
                            REBasesCalcul bc = new REBasesCalcul();

                            bc.setIdBasesCalcul(ra.getIdBaseCalcul());
                            bc.setSession(getSession());
                            bc.retrieve(transaction);

                            String noRevision;
                            if (bc.isNew()) {
                                throw new Exception(getSession().getLabel(
                                        "EXCEPTION_RETRIEVE_BASECALCUL_REDIMINUTIONLISTERENTEACCORDEE")
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

                            PRTiersWrapper tier = PRTiersHelper
                                    .getTiersParId(getSession(), ra.getIdTiersBeneficiaire());

                            if (null == tier) {
                                throw new Exception(getSession().getLabel(
                                        "EXCEPTION_TIERS_REDIMINUTIONLISTERENTEACCORDEE")
                                        + " (REDiminutionListeRenteAccordeeProcess)");
                            }

                            String idAnnonce = "";

                            if (noRevision.equals("10")) {
                                // Créer annonce pour 10 ème révision si bc = 10ème
                                // révision

                                REAnnoncesDiminution10Eme annonce10Eme = new REAnnoncesDiminution10Eme();
                                annonce10Eme.setSession(getSession());
                                annonce10Eme.setCodeApplication("45");
                                annonce10Eme.setCodeEnregistrement01("01");
                                annonce10Eme.setNumeroCaisse(getSession().getApplication().getProperty("noCaisse"));
                                annonce10Eme.setNumeroAgence(getSession().getApplication().getProperty("noAgence"));
                                annonce10Eme.setNumeroAnnonce("");
                                annonce10Eme.setReferenceCaisseInterne("DIM" + getSession().getUserId().toUpperCase());
                                annonce10Eme.setNoAssAyantDroit(NSUtil.unFormatAVS(tier
                                        .getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
                                annonce10Eme.setGenrePrestation(ra.getCodePrestation());

                                int lengthMontant = String
                                        .valueOf(new FWCurrency(ra.getMontantPrestation()).intValue()).length();
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
                                annonce10Eme.setCodeMutation(getSession().getCode(getCsCodeMutation()));
                                annonce10Eme.setEtat(IREAnnonces.CS_ETAT_OUVERT);
                                annonce10Eme.setIdTiers(tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                                annonce10Eme.add(transaction);

                                idAnnonce = annonce10Eme.getIdAnnonce();

                            } else if (noRevision.equals("9")) {
                                // Créer annonce pour 9 ème révision si bc = 9ème
                                // révision

                                REAnnoncesDiminution9Eme annonce9Eme = new REAnnoncesDiminution9Eme();
                                annonce9Eme.setSession(getSession());
                                annonce9Eme.setCodeApplication("42");
                                annonce9Eme.setCodeEnregistrement01("01");
                                annonce9Eme.setNumeroCaisse(getSession().getApplication().getProperty("noCaisse"));
                                annonce9Eme.setNumeroAgence(getSession().getApplication().getProperty("noAgence"));
                                annonce9Eme.setNumeroAnnonce("");
                                annonce9Eme.setReferenceCaisseInterne("DIM" + getSession().getUserId().toUpperCase());
                                annonce9Eme.setNoAssAyantDroit(NSUtil.unFormatAVS(tier
                                        .getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
                                annonce9Eme.setGenrePrestation(ra.getCodePrestation());

                                int lengthMontant = String
                                        .valueOf(new FWCurrency(ra.getMontantPrestation()).intValue()).length();
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
                                annonce9Eme.setCodeMutation(getSession().getCode(getCsCodeMutation()));
                                annonce9Eme.setIdTiers(tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                                annonce9Eme.setEtat(IREAnnonces.CS_ETAT_OUVERT);

                                annonce9Eme.add(transaction);

                                idAnnonce = annonce9Eme.getIdAnnonce();

                            } else {
                                getMemoryLog().logMessage(
                                        getSession().getLabel("ERREUR_DIMINUTION_RA") + " No révision = " + noRevision,
                                        FWMessage.ERREUR, "REDiminutionRenteAccordeeProcess");
                            }

                            // Création de l'annonce de rente pour la liaison entre
                            // les ra et les annonces
                            REAnnonceRente annonceRente = new REAnnonceRente();
                            annonceRente.setSession(getSession());
                            annonceRente.setIdAnnonceHeader(idAnnonce);
                            annonceRente.setCsEtat(IREAnnonces.CS_ETAT_OUVERT);
                            annonceRente.setCsTraitement(IREAnnonces.CS_CODE_EN_COURS);
                            annonceRente.setIdRenteAccordee(ra.getIdPrestationAccordee());
                            annonceRente.add(transaction);

                            // Mise à jour de la rente accordée (Etat = Diminué +
                            // nouvelle date de fin + code mutation)
                            if (!transaction.hasErrors()) {
                                ra.retrieve(transaction);
                                ra.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);
                                ra.setCodeMutation(getSession().getCode(getCsCodeMutation()));
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

                            // MAJ de la date de fin de la demande, si toutes les RA
                            // ont été diminuées
                            REDiminutionRenteUtils.majDemandePourDiminution(getSession(), (BTransaction) transaction,
                                    ra);

                            // Création de l'email envoyé à l'utilisateur
                            if (CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue()) {

                                String communePolitique = PRTiersHelper.getCommunePolitique(
                                        echeanceCourrante.getIdTiers(), new Date(), getSession());
                                getMemoryLog().logMessage(
                                        getSession().getLabel(
                                                CommunePolitique.LABEL_COMMUNE_POLITIQUE_TITRE_COLONNE.getKey())
                                                + " : " + communePolitique, FWMessage.INFORMATION,
                                        getClass().getSimpleName());
                            }

                            getMemoryLog().logMessage(
                                    getSession().getLabel("NSS_SUPPRESSIONRENTES25ANSPROCESS") + " "
                                            + echeanceCourrante.getNss(), FWMessage.INFORMATION,
                                    getClass().getSimpleName());
                            getMemoryLog().logMessage(
                                    getSession().getLabel("NOM_SUPPRESSIONRENTES25ANSPROCESS") + " "
                                            + echeanceCourrante.getNom(), FWMessage.INFORMATION,
                                    getClass().getSimpleName());
                            getMemoryLog().logMessage(
                                    getSession().getLabel("PRENOM_SUPPRESSIONRENTES25ANSPROCESS") + " "
                                            + echeanceCourrante.getPrenom(), FWMessage.INFORMATION,
                                    getClass().getSimpleName());

                            if (JadeStringUtil.isEmpty(echeanceCourrante.getCs2())
                                    && JadeStringUtil.isEmpty(echeanceCourrante.getCs5())) {
                                getMemoryLog().logMessage(
                                        getSession().getLabel("GENRE_PRESTATION_SUPPRESSIONRENTES25ANSPROCESS") + " "
                                                + echeanceCourrante.getCodePrestation() + "\n", FWMessage.INFORMATION,
                                        getClass().getSimpleName());
                            } else {
                                // Si code cas spéciaux 02 ou 05, un mail est envoyé
                                // à l'utilisateur
                                getMemoryLog().logMessage(
                                        getSession().getLabel("GENRE_PRESTATION_SUPPRESSIONRENTES25ANSPROCESS") + " "
                                                + echeanceCourrante.getCodePrestation(), FWMessage.INFORMATION,
                                        getClass().getSimpleName());
                                if (!JadeStringUtil.isEmpty(echeanceCourrante.getCs2())
                                        && !JadeStringUtil.isEmpty(echeanceCourrante.getCs5())) {
                                    getMemoryLog().logMessage(
                                            getSession().getLabel("MAIL_CODECASSPECIAUX_SUPPRESSIONRENTE25ANSPROCESS")
                                                    + " " + echeanceCourrante.getCs2() + ", "
                                                    + echeanceCourrante.getCs5() + "\n", FWViewBeanInterface.WARNING,
                                            getClass().getSimpleName());
                                } else if (!JadeStringUtil.isEmpty(echeanceCourrante.getCs2())) {
                                    getMemoryLog().logMessage(
                                            getSession().getLabel("MAIL_CODECASSPECIAL_SUPPRESSIONRENTE25ANSPROCESS")
                                                    + " " + echeanceCourrante.getCs2() + "\n", FWMessage.INFORMATION,
                                            getClass().getSimpleName());
                                } else if (!JadeStringUtil.isEmpty(echeanceCourrante.getCs5())) {
                                    getMemoryLog().logMessage(
                                            getSession().getLabel("MAIL_CODECASSPECIAL_SUPPRESSIONRENTE25ANSPROCESS")
                                                    + " " + echeanceCourrante.getCs5() + "\n", FWMessage.INFORMATION,
                                            getClass().getSimpleName());
                                }
                            }
                        }
                    }
                    // Fin traitement normal
                }
            }
        } catch (Exception e) {
            isError = true;
            JadeLogger.error(this, e);
            this._addError(getSession().getLabel("ERREUR_DIMINUTION_RA") + " : " + e.toString());
            getMemoryLog().logMessage(getSession().getLabel("ERREUR_DIMINUTION_RA") + " : " + e.toString(),
                    FWMessage.ERREUR, "REDiminutionRenteAccordeeProcess");
            return false;
        } finally {
            if (transaction != null) {
                try {
                    if (isError || transaction.hasErrors() || transaction.isRollbackOnly()) {
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

    private String ajouteNomPrenomNSS(String message) {
        message += echeanceCourrante.getNss() + " - " + echeanceCourrante.getNom() + " "
                + echeanceCourrante.getPrenom() + "\r\n";
        return message;
    }

    @Override
    protected void _validate() throws Exception {
        setSendCompletionMail(true);
        setSendMailOnError(true);
        setControleTransaction(true);
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
     * Récupère la date de début du droit à la rente
     * 
     * @return
     */
    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    /**
     * Récuppère la date de fin du droit à la rente
     * 
     * @return
     */
    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public REListerEcheanceRenteJoinMembresFamille getEcheanceCourrante() {
        return echeanceCourrante;
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("MAIL_TITRE_SUPPRESSIONRENTE25ANSPROCESS");
    }

    /**
     * Récupère le code de genre de diminution
     * 
     * @return
     */
    public String getGenreDiminution() {
        return genreDiminution;
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
     * Récupère l'id du tiers bénéficiaire
     * 
     * @return
     */
    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public List<REListerEcheanceRenteJoinMembresFamille> getListeEcheances() {
        return listeEcheances;
    }

    /**
     * Récupère le montant de la rente accordée
     * 
     * @return
     */
    public String getMontant() {
        return montant;
    }

    private int getNbDiminution() {
        return nbDiminution;
    }

    /**
     * @return
     */
    public BSpy getSpy() {
        return null;
    }

    /**
     * Récupération de la ligne info sur le tiers bénéficaire
     * 
     * @return
     */
    public String getTiersBeneficiaireInfo() {
        return tiersBeneficiaireInfo;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * Modifie le code de mutation
     * 
     * @param newCodeMutation
     */
    public void setCsCodeMutation(String newCodeMutation) {
        csCodeMutation = newCodeMutation;
    }

    /**
     * Modifie le cote de traitement
     * 
     * @param newCodeTraitement
     */
    public void setCsCodeTraitement(String newCodeTraitement) {
        csCodeTraitement = newCodeTraitement;
    }

    /**
     * Modifie la date de début du droit à la rente
     * 
     * @param newDateDebutDroit
     */
    public void setDateDebutDroit(String newDateDebutDroit) {
        dateDebutDroit = newDateDebutDroit;
    }

    /**
     * Modifie la date de fin du droit à la rente
     * 
     * @param newDateFinDroit
     */
    public void setDateFinDroit(String newDateFinDroit) {
        dateFinDroit = newDateFinDroit;
    }

    public void setEcheanceCourrante(REListerEcheanceRenteJoinMembresFamille echeanceCourrante) {
        this.echeanceCourrante = echeanceCourrante;
    }

    /**
     * Modification du code de genre de diminution
     * 
     * @param newGenreDiminution
     */
    public void setGenreDiminution(String newGenreDiminution) {
        genreDiminution = newGenreDiminution;
    }

    /**
     * Modification de l'id rente accordée
     * 
     * @param newIdRenteAccordee
     */
    public void setIdRenteAccordee(String newIdRenteAccordee) {
        idRenteAccordee = newIdRenteAccordee;
    }

    /**
     * Modifie l'id du tiers bénéficiaire
     * 
     * @param newIdTiersBeneficiaire
     */
    public void setIdTiersBeneficiaire(String newIdTiersBeneficiaire) {
        idTiersBeneficiaire = newIdTiersBeneficiaire;
    }

    public void setListeEcheances(List<REListerEcheanceRenteJoinMembresFamille> listeEcheances) {
        this.listeEcheances = listeEcheances;
    }

    /**
     * Modifie le montant de la rente accordée
     * 
     * @param newMontant
     */
    public void setMontant(String newMontant) {
        montant = newMontant;
    }

    private void setNbDiminution(int nbDiminution) {
        this.nbDiminution = nbDiminution;
    }

    /**
     * Modification de la ligne info sur le tiers bénéficiaire
     * 
     * @param newTiersBeneficiaireInfo
     */
    public void setTiersBeneficiaireInfo(String newTiersBeneficiaireInfo) {
        tiersBeneficiaireInfo = newTiersBeneficiaireInfo;
    }

}
