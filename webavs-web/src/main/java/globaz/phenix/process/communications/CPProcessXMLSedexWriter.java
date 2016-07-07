/*
 * Créé le 15 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.process.communications;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichage;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichageManager;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.listes.excel.CPListeCommunicationEnvoiCantonAZeroProcess;
import globaz.phenix.listes.excel.CPListeCommunicationEnvoiProcess;
import globaz.phenix.process.communications.envoiWritterImpl.CPSedexWriter;
import globaz.phenix.process.communications.envoiWritterImpl.SedexResult;
import globaz.phenix.util.Constante;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import java.util.ArrayList;

/**
 * <H1>Description</H1>
 * 
 * @author MAR
 */

public class CPProcessXMLSedexWriter extends BProcess {

    private static final long serialVersionUID = 7406014631735455424L;

    public static final int CS_ALLEMAND = 503002;
    public static final int CS_FRANCAIS = 503001;
    public static final int CS_ITALIEN = 503004;
    public static final String NUMERO_CAISSE = "NUMEROCAISSE";

    private String anneeDecision = "";
    private boolean auMoinsUneErreur = false;
    private CPCommunicationFiscaleAffichageManager communicationFiscaleManager = null;
    private String dateEnvoi = "";
    private Boolean dateEnvoiVide = new Boolean(false);
    private Boolean demandeAnnulee = new Boolean(false);
    private Boolean donneesCommerciales = new Boolean(false);
    private Boolean donneesPrivees = new Boolean(false);
    private Boolean envoiImmediat = new Boolean(false);
    private boolean envoiIndividuel = false;

    private String forCanton = "";
    private String forGenreAffilie = "";
    private String idCommunication = "";
    private String idIfd = "";
    private Boolean lifd = new Boolean(false);
    private int nbCommunication = 0;
    private String numAffilie = "";
    private boolean sendMail = true;
    private Boolean withAnneeEnCours = new Boolean(false);

    /**
     * Constructeur par defaut.
     */
    public CPProcessXMLSedexWriter() {
        super();
    }

    /**
     * Constructeur defini.
     * 
     * @param parent process.
     */
    public CPProcessXMLSedexWriter(BProcess parent) {
        super(parent);
    }

    /**
     * Constructeur defini..
     * 
     * @param session Session.
     */
    public CPProcessXMLSedexWriter(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
        setState(Constante.FWPROCESS_MGS_220);
    }

    @Override
    protected boolean _executeProcess() {

        // initialisation des variable du processus
        initializeVariableProcess();

        if (!JadeStringUtil.isEmpty(getIdCommunication())) {
            // Cas où on fait de l'individuel depuis la gestion des demandes, on va rechercher la communication
            try {
                CPCommunicationFiscaleAffichageManager commMng = new CPCommunicationFiscaleAffichageManager();
                commMng.setSession(getSession());
                commMng.setForIdCommunication(getIdCommunication());
                commMng.setWithAnneeEnCours(Boolean.TRUE);
                commMng.find();

                if (commMng.size() == 1) {
                    CPCommunicationFiscaleAffichage comm = (CPCommunicationFiscaleAffichage) commMng.getFirstEntity();
                    // Génération du fichier Sedex pour un envoi individuelle - Format XML
                    genererFichierXMLPourDemandeIndividuelle(comm, getTransaction());
                } else {
                    this._addError(getTransaction(), getSession().getLabel("CP_ERROR_COM_NON_TROUVEE") + " - "
                            + getIdCommunication());
                    return false;
                }
            } catch (Exception e) {
                this._addError(getTransaction(), getSession().getLabel("CP_ERROR_COM_NON_TROUVEE") + " - "
                        + getIdCommunication() + " : " + e.toString());
                return false;
            }
        } else {
            // Envoi en masse
            return genererFichierXMLPourDemandeMultiple();
        }

        if (getTransaction().hasErrors()) {
            setAuMoinsUneErreur(true);
        }
        return true;

    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isEmpty(getSession().getUserEMail()) && JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getTransaction(), getSession().getLabel("CP_EMAIL_OBLIGATOIRE"));
        }

        // Pour Sedex: Envoi non autorisé pour indépendant avec seulement données privées ou non actif avec seulement
        // données commerciales
        if ((getForGenreAffilie().equalsIgnoreCase(CPDecision.CS_INDEPENDANT)
                && getDonneesCommerciales().equals(Boolean.FALSE) && getDonneesPrivees().equals(Boolean.TRUE))
                || (getForGenreAffilie().equalsIgnoreCase(CPDecision.CS_NON_ACTIF)
                        && getDonneesCommerciales().equals(Boolean.TRUE) && getDonneesPrivees().equals(Boolean.FALSE))) {
            _addError(getTransaction(), getSession().getLabel("CP_ENVOI_SEDEX"));
        }

        // demande annulé, autorisé seulement pour GE et VD
        if (Boolean.TRUE.equals(getDemandeAnnulee())
                && !IConstantes.CS_LOCALITE_CANTON_GENEVE.equalsIgnoreCase(getForCanton())
                && !IConstantes.CS_LOCALITE_CANTON_VAUD.equalsIgnoreCase(getForCanton())) {
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0144"));
        }
    }

    private static TIAdministrationViewBean rechercheCaisse(BSession session) {
        try {
            TIAdministrationManager tiAdminCaisseMgr = new TIAdministrationManager();
            tiAdminCaisseMgr.setSession(session);
            tiAdminCaisseMgr.setForCodeAdministration(CaisseHelperFactory.getInstance().getNoCaisseFormatee(
                    session.getApplication()));
            tiAdminCaisseMgr.setForGenreAdministration(CaisseHelperFactory.CS_CAISSE_COMPENSATION);
            tiAdminCaisseMgr.find();

            TIAdministrationViewBean tiAdminCaisse = (TIAdministrationViewBean) tiAdminCaisseMgr.getFirstEntity();

            if (tiAdminCaisse != null) {
                return tiAdminCaisse;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    protected int caclulNumberOfFile(int nombreMaxParFichierEnvoi) {
        int nombreIterations = nbCommunication / nombreMaxParFichierEnvoi;
        if (nbCommunication % nombreMaxParFichierEnvoi > 0) {
            nombreIterations++;
        }
        return nombreIterations;
    }

    /* Nombre de cas maximum par fichier sedex */
    protected int findNumberMaxRecordPerFile() {
        try {
            return ((CPApplication) getSession().getApplication()).nombreMaxEnvoiSedex();
        } catch (Exception e1) {
            JadeLogger.warn(e1, e1.getMessage());
            return 250;
        }
    }

    protected void genarateListExcelLCommunicationEnvoye(ArrayList<String> communicationEnErreur) throws Exception {
        // On créé la liste des envois produit
        CPListeCommunicationEnvoiProcess process = new CPListeCommunicationEnvoiProcess();
        process.setMemoryLog(getMemoryLog());
        process.setSession(getSession());
        process.setAnnee(communicationFiscaleManager.getForAnneeDecision());
        process.setGenre(forGenreAffilie);
        process.setCanton(communicationFiscaleManager.getForCanton());
        process.setManager(communicationFiscaleManager);
        process.setEMailAddress(getEMailAddress());
        process.setParent(this);
        if (!communicationEnErreur.isEmpty()) {
            // On passe les communications en erreurs pour ne pas les lister
            process.setCommunicationEnErreur(communicationEnErreur);
        }
        process.executeProcess();
    }

    protected void generateFileAndListeByTownschip(CPProcessXMLSedexWriter cpProcessAsciiSedexWriter) throws Exception {

        int nombreMaxParFichierEnvoi = 0;
        CPSedexWriter fichier = new CPSedexWriter(cpProcessAsciiSedexWriter.getSession());
        fichier.setCaisse(CPProcessXMLSedexWriter.rechercheCaisse(cpProcessAsciiSedexWriter.getSession()));
        // Récupération du nombre du nombre de communication par fichier, 250 par défaut
        nombreMaxParFichierEnvoi = findNumberMaxRecordPerFile();
        // calcul du nombre de fichier à générer pour un canton
        int nombreIterations = caclulNumberOfFile(nombreMaxParFichierEnvoi);
        int debutATraiter = 0;
        int finATraiter = 0;

        /*
         * Les communications sont envoyées par lot. Si un problème survient dans un des lot (validation ou autre),
         * il ne faut pas insérer de date d'envoi dans les communications présentes dans ce lot.
         * Nous allons donc créer une transaction par lot.
         */
        BTransaction managedTransaction = null;
        SedexResult sedexResult = null;
        // Chaque itération représente un lot de communication à traiter
        for (int i = 0; (i < nombreIterations) && (!isAborted()); i++) {

            // Création de la nouvelle transaction pour le lot courant
            managedTransaction = (BTransaction) getSession().newTransaction();
            if (!managedTransaction.isOpened()) {
                managedTransaction.openTransaction();
            }

            if (nbCommunication >= (i + 1) * nombreMaxParFichierEnvoi) {
                debutATraiter = i * nombreMaxParFichierEnvoi;
                finATraiter = (i + 1) * nombreMaxParFichierEnvoi;
            } else {
                debutATraiter = i * nombreMaxParFichierEnvoi;
                finATraiter = nbCommunication;
            }

            sedexResult = null;
            try {
                // Création du fichier Sedex, validation - Format XML
                sedexResult = fichier.createFileSedexXMLEnvoiGroupe(communicationFiscaleManager, managedTransaction,
                        cpProcessAsciiSedexWriter, debutATraiter, finATraiter, donneesCommerciales, donneesPrivees);

            }
            // Bien évidement une RuntimeException peut survenir...
            catch (Exception exception) {
                // Si c'est le cas, sedexResult sera null
                if (sedexResult == null) {
                    sedexResult = new SedexResult();
                }
                // dans tous les cas, si une exception est survenue on ne veut pas commiter la transaction
                sedexResult.setDoRollback(true);
                managedTransaction.setRollbackOnly();
                // Logging de l'exception
                getMemoryLog().logMessage(exception.toString(), globaz.framework.util.FWMessage.ERREUR,
                        this.getClass().getSimpleName());
                JadeLogger.error(this, exception);
            }
            // Gestion des warnings/erreurs et de la transaction
            finally {

                // Information sur le lot traité
                String lotNumero = getSession().getLabel("LOT_NUMERO") + " [" + (i + 1) + "/" + nombreIterations + "]";

                // Gestion des warning/informations non bloquants dans le mail pour ce lot
                if (sedexResult.hasWarnings()) {
                    String msg = getSession().getLabel("WARNING_REMONTE_DANS_LOT_COMMUNICATION");
                    msg = msg.replace("{0}", String.valueOf(i + 1));
                    msg = msg.replace("{1}", String.valueOf(nombreIterations));
                    msg = msg.replace("{2}", String.valueOf(debutATraiter));
                    msg = msg.replace("{3}", String.valueOf(finATraiter));
                    getMemoryLog().logMessage(msg, globaz.framework.util.FWMessage.AVERTISSEMENT, "");

                    for (String warning : sedexResult.getWarnings()) {
                        getMemoryLog().logMessage(lotNumero + " : " + warning,
                                globaz.framework.util.FWMessage.AVERTISSEMENT, "");
                    }
                    getMemoryLog().logMessage("---------------------------------------------------------------",
                            globaz.framework.util.FWMessage.AVERTISSEMENT, "");
                }

                // Maintenant on contrôle si la transaction doit être rollbackée suite à des erreurs
                boolean errorFounded = sedexResult.getDoRollback() || managedTransaction.isRollbackOnly();

                // Gestion des erreur si besoin et remontées dans le mail
                if (errorFounded) {
                    setAuMoinsUneErreur(true);

                    String msg = lotNumero + ". " + getSession().getLabel("ERREUR_REMONTE_DANS_LOT_COMMUNICATION");
                    getMemoryLog().logMessage(msg, globaz.framework.util.FWMessage.ERREUR,
                            this.getClass().getSimpleName());

                    for (String error : sedexResult.getErrors()) {
                        getMemoryLog().logMessage(error, globaz.framework.util.FWMessage.ERREUR, "");
                    }

                    getMemoryLog().logMessage("---------------------------------------------------------------",
                            globaz.framework.util.FWMessage.ERREUR, "");
                }

                // fermeture de la transaction utilisée pour ce lot
                try {
                    if (errorFounded) {
                        managedTransaction.rollback();
                    } else {
                        managedTransaction.commit();
                    }
                } catch (Exception e) {
                    JadeLogger.error(this, e);
                } finally {
                    managedTransaction.closeTransaction();
                }
            }
        }

        if (sedexResult != null && sedexResult.hasWarnings()) {
            getMemoryLog().logMessage(getSession().getLabel("INFORMATION_WARNING_COMMUNICATION"),
                    globaz.framework.util.FWMessage.INFORMATION, "");
        }
        // Génération de la liste excel des communications envoyées
        genarateListExcelLCommunicationEnvoye(fichier.getCommunicationEnErreur());

        getMemoryLog().clear();
    }

    protected void genererFichierXMLPourDemandeIndividuelle(CPCommunicationFiscaleAffichage communication,
            BTransaction transaction) {
        numAffilie = communication.getNumAffilie();
        // Initialisation des données névessaire à la création du fichier
        CPSedexWriter fichier = new CPSedexWriter(getSession());
        fichier.setCaisse(CPProcessXMLSedexWriter.rechercheCaisse(getSession()));
        fichier.setEnvoiImmediat(getEnvoiImmediat().booleanValue());
        fichier.setDonneesCommerciales(getDonneesCommerciales().booleanValue());
        fichier.setDonneesPrivees(getDonneesPrivees().booleanValue());
        fichier.setLifd(getLifd().booleanValue());

        // Création du fichier XML
        String error;
        if (JadeStringUtil.isBlankOrZero(communication.getCanton())
                || IConstantes.CS_LOCALITE_ETRANGER.equalsIgnoreCase(communication.getCanton())) {
            error = getSession().getLabel("CP_MSG_0206");
        } else {
            error = fichier.createFileXMLSedex(transaction, communication).toString();
        }

        if (!JadeStringUtil.isEmpty(error)) {
            getMemoryLog().logMessage(error, globaz.framework.util.FWMessage.ERREUR, "");
            transaction.addErrors("");
            auMoinsUneErreur = true;
        }
    }

    private boolean genererFichierXMLPourDemandeMultiple() {
        try {
            // Lecture de toutes les communications
            initManagerCommunicationFiscale(getForCanton());
            // Lister les différents cantons qui ont des communications fiscales
            // afin de générer l'header Sedex selon le canton
            ArrayList<String> cantonsList = communicationFiscaleManager.getListeCantons();
            for (int iteratorCantons = 0; iteratorCantons < cantonsList.size(); iteratorCantons++) {
                String canton = cantonsList.get(iteratorCantons);
                if (!JadeStringUtil.isBlankOrZero(canton) && !canton.equals(IConstantes.CS_LOCALITE_ETRANGER)) {
                    // Lecture des communications du canton
                    initManagerCommunicationFiscale(canton);
                    communicationFiscaleManager.find(BManager.SIZE_NOLIMIT);
                    nbCommunication = communicationFiscaleManager.getSize();
                    setProgressScaleValue(nbCommunication);
                    if (nbCommunication == 0) {
                        getMemoryLog().logMessage(getSession().getLabel("SEDEX_AUCUNE_COM"),
                                globaz.framework.util.FWMessage.INFORMATION, this.getClass().getName());
                    } else if (nbCommunication > 1) {
                        // Génération du fichier XML et de la liste récapitulative pour le canton traité
                        generateFileAndListeByTownschip(this);
                    } else {
                        // La sélection pour un canton ne donne qu'un cas à envoyer
                        // => faire dans ce cas un envoi individuel
                        CPCommunicationFiscaleAffichage comm = (CPCommunicationFiscaleAffichage) communicationFiscaleManager
                                .getFirstEntity();

                        genererFichierXMLPourDemandeIndividuelle(comm, getTransaction());

                        if (getTransaction().hasErrors()) {
                            getMemoryLog().logMessage(getTransaction().getErrors().toString(),
                                    globaz.framework.util.FWMessage.ERREUR, "");
                            setAuMoinsUneErreur(true);
                        }

                    }

                }
                getTransaction().clearErrorBuffer();
            }
            // Impression de la liste pour les cantons non renseigné ou étranger
            printListeForTownschipEmpty();
            if (auMoinsUneErreur) {
                return false;
            }
            return true;
        } catch (Exception e) {
            JadeLogger.error(this, e);
            auMoinsUneErreur = true;
            getMemoryLog().logMessage(getTransaction().getErrors().toString(), globaz.framework.util.FWMessage.ERREUR,
                    "");
            return false;
        }
    }

    /**
     * @return
     */
    public String getAnneeDecision() {
        return anneeDecision;
    }

    /**
     * @return
     */
    public String getDateEnvoi() {
        return dateEnvoi;
    }

    /**
     * @return
     */
    public Boolean getDateEnvoiVide() {
        return dateEnvoiVide;
    }

    /**
     * @return
     */
    public Boolean getDemandeAnnulee() {
        return demandeAnnulee;
    }

    public Boolean getDonneesCommerciales() {
        return donneesCommerciales;
    }

    public Boolean getDonneesPrivees() {
        return donneesPrivees;
    }

    @Override
    protected String getEMailObject() {
        if (isAuMoinsUneErreur()) {
            return getSession().getLabel("SEDEX_ENVOI_KO");
        } else {
            return getSession().getLabel("SEDEX_ENVOI_OK");
        }
    }

    public Boolean getEnvoiImmediat() {
        return envoiImmediat;
    }

    public boolean getEnvoiIndividuel() {
        return envoiIndividuel;
    }

    /**
     * @return
     */
    public String getForCanton() {
        return forCanton;
    }

    /**
     * @return
     */
    public String getForGenreAffilie() {
        return forGenreAffilie;
    }

    /**
     * @return
     */
    public String getIdCommunication() {
        return idCommunication;
    }

    /**
     * @return
     */
    public String getIdIfd() {
        return idIfd;
    }

    public Boolean getLifd() {
        return lifd;
    }

    @Override
    public String getSubject() {
        if (JadeStringUtil.isEmpty(numAffilie)) {
            return super.getSubject();
        } else {
            return super.getSubject() + " : " + numAffilie;
        }
    }

    public Boolean getWithAnneeEnCours() {
        return withAnneeEnCours;
    }

    protected void initializeVariableProcess() {
        if (sendMail) {
            setSendCompletionMail(true);
        } else {
            setSendCompletionMail(false);
        }

        setSendMailOnError(true);
    }

    private CPCommunicationFiscaleAffichageManager initManagerCommunicationFiscale(String canton) {
        communicationFiscaleManager = new CPCommunicationFiscaleAffichageManager();
        communicationFiscaleManager.setSession(getSession());
        communicationFiscaleManager.setDemandeAnnulee(getDemandeAnnulee());
        if (getDemandeAnnulee().equals(Boolean.FALSE)) {
            communicationFiscaleManager.setExceptRetour(Boolean.TRUE);
            communicationFiscaleManager.setExceptComptabilise(Boolean.TRUE);
        }
        if (JadeStringUtil.isEmpty(getDateEnvoi())) {
            communicationFiscaleManager.setDateEnvoiVide(getDateEnvoiVide());
        } else {
            communicationFiscaleManager.setDateEnvoiVide(Boolean.FALSE);
            if (getDemandeAnnulee().equals(Boolean.FALSE)) {
                communicationFiscaleManager.setForDateEnvoi(getDateEnvoi());
            } else {
                communicationFiscaleManager.setForDateEnvoiAnnulation(getDateEnvoi());
            }
        }
        // Si Indépendant de sélectionné => IND, TSE, AGR, REN donc différent de
        // NON ACTIF...
        if (!JadeStringUtil.isEmpty(getForGenreAffilie())) {
            if (CPDecision.CS_INDEPENDANT.equalsIgnoreCase(getForGenreAffilie())) {
                communicationFiscaleManager.setNotInGenreAffilie(CPDecision.CS_NON_ACTIF + ", "
                        + CPDecision.CS_ETUDIANT);
            } else {
                communicationFiscaleManager.setInGenreAffilie(CPDecision.CS_NON_ACTIF + ", " + CPDecision.CS_ETUDIANT);
            }
        }
        if ("VIDE".equalsIgnoreCase(canton)) {
            communicationFiscaleManager.setEmptyOrEtranger(Boolean.TRUE);
        } else {
            communicationFiscaleManager.setForCanton(canton);
        }
        communicationFiscaleManager.setForAnneeDecision(getAnneeDecision());
        communicationFiscaleManager.setForIdCommunication(getIdCommunication());
        // Avec année encours
        if (JadeStringUtil.isEmpty(getAnneeDecision())) {
            communicationFiscaleManager.setWithAnneeEnCours(getWithAnneeEnCours());
        } else {
            communicationFiscaleManager.setWithAnneeEnCours(Boolean.FALSE);
        }
        communicationFiscaleManager.setSession(getSession());
        communicationFiscaleManager.setOrderBy("MALNAF, ICANDD");
        return communicationFiscaleManager;

    }

    public boolean isAuMoinsUneErreur() {
        return auMoinsUneErreur;
    }

    public boolean isEnvoiIndividuel() {
        return envoiIndividuel;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    protected void printListeForTownschipEmpty() throws Exception {
        // Impression des cas dont le canton est vide ou étranger
        initManagerCommunicationFiscale("VIDE");
        CPListeCommunicationEnvoiCantonAZeroProcess process = new CPListeCommunicationEnvoiCantonAZeroProcess();
        process.setSession(getSession());
        process.setAnnee(communicationFiscaleManager.getForAnneeDecision());
        process.setGenre(forGenreAffilie);
        process.setManager(communicationFiscaleManager);
        process.setEMailAddress(getEMailAddress());
        process.setParent(this);
        process.executeProcess();
    }

    /**
     * @param string
     */
    public void setAnneeDecision(String string) {
        anneeDecision = string;
    }

    public void setAuMoinsUneErreur(boolean auMoinsUneErreur) {
        this.auMoinsUneErreur = auMoinsUneErreur;
    }

    /**
     * @param string
     */
    public void setDateEnvoi(String string) {
        dateEnvoi = string;
    }

    /**
     * @param boolean1
     */
    public void setDateEnvoiVide(Boolean boolean1) {
        dateEnvoiVide = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setDemandeAnnulee(Boolean boolean1) {
        demandeAnnulee = boolean1;
    }

    public void setDonneesCommerciales(Boolean donneesCommerciales) {
        this.donneesCommerciales = donneesCommerciales;
    }

    public void setDonneesPrivees(Boolean donneesPrivees) {
        this.donneesPrivees = donneesPrivees;
    }

    public void setEnvoiImmediat(Boolean envoiImmediat) {
        this.envoiImmediat = envoiImmediat;
    }

    public void setEnvoiIndividuel(boolean envoiIndividuel) {
        this.envoiIndividuel = envoiIndividuel;
    }

    /**
     * @param string
     */
    public void setForCanton(String string) {
        forCanton = string;
    }

    /**
     * @param string
     */
    public void setForGenreAffilie(String string) {
        forGenreAffilie = string;
    }

    /**
     * @param string
     */
    public void setIdCommunication(String string) {
        idCommunication = string;
    }

    /**
     * @param string
     */
    public void setIdIfd(String string) {
        idIfd = string;
    }

    public void setLifd(Boolean lifd) {
        this.lifd = lifd;
    }

    public void setWithAnneeEnCours(Boolean withAnneeEnCours) {
        this.withAnneeEnCours = withAnneeEnCours;
    }

}
