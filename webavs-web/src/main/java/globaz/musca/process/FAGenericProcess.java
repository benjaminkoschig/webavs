package globaz.musca.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAAfactManager;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAModulePassage;
import globaz.musca.db.facturation.FAPassage;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.contentieux.CAMotifContentieux;
import globaz.osiris.db.contentieux.CAMotifContentieuxManager;
import globaz.osiris.translation.CACodeSystem;
import java.util.Iterator;
import java.util.List;

/**
 * Génération de la facturation. Date de création : (25.11.2002 11:52:37)
 * 
 * @author: BTC
 */
public abstract class FAGenericProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Recherche le compte annexe depuis une entete de facture.
     * 
     * @param enteteFacture
     * @param session
     * @param transaction
     * @return CACompteAnnexe
     * @throws Exception
     */
    @SuppressWarnings("finally")
    public static CACompteAnnexe getCompteAnnexe(FAEnteteFacture enteteFacture, BSession session,
            BTransaction transaction) throws Exception {

        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        FAApplication app = (FAApplication) GlobazServer.getCurrentSystem().getApplication(
                FAApplication.DEFAULT_APPLICATION_MUSCA);
        compteAnnexe.setISession(app.getSessionOsiris(session));
        compteAnnexe.setIdTiers(enteteFacture.getIdTiers());
        compteAnnexe.setIdRole(enteteFacture.getIdRole());
        compteAnnexe.setIdExterneRole(enteteFacture.getIdExterneRole());

        compteAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);

        try {
            compteAnnexe.retrieve(transaction);
        } catch (Exception e) {
            transaction
                    .addErrors("Erreur lors du retrieve du compte annexe dans la class : FAPassageRemboursementProcess "
                            + e.getMessage());
        } finally {
            if (!transaction.hasErrors()) {
                return compteAnnexe;
            } else {
                return null;
            }
        }
    }

    /**
     * On controle que la section à un sursis et que l'affililé n'est pas rentier.
     * 
     * @param APISection
     *            sec
     * @param BSession
     *            session
     * @return boolean
     */
    public static boolean hasSursisNotRentier(APISection sec, BSession session) {
        boolean sursisExistant = false;
        if (!sec.getCompteAnnexe().getContEstBloque().booleanValue()) {
            if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()) {
                if (!CACodeSystem.CS_RENTIER.equalsIgnoreCase(sec.getIdMotifContentieuxSuspendu())
                        && !JadeStringUtil.isEmpty(sec.getIdMotifContentieuxSuspendu())) {
                    try {
                        if (BSessionUtil.compareDateFirstGreaterOrEqual(session, sec.getDateSuspendu(),
                                JACalendar.todayJJsMMsAAAA())) {
                            sursisExistant = true;
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            } else {
                CAMotifContentieuxManager motifMgr = new CAMotifContentieuxManager();
                motifMgr.setSession(session);
                motifMgr.setForIdSection(sec.getIdSection());
                motifMgr.setFromDateBetweenDebutFin(JACalendar.todayJJsMMsAAAA());
                try {
                    motifMgr.find();
                    if (!motifMgr.isEmpty()) {
                        CAMotifContentieux motif = new CAMotifContentieux();
                        for (int i = 0; i < motifMgr.size(); i++) {
                            motif = (CAMotifContentieux) motifMgr.getEntity(i);
                            sursisExistant = true;
                            if (motif.getIdMotifBlocage().equals(CACodeSystem.CS_RENTIER)) {
                                sursisExistant = false;
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        return sursisExistant;
    }

    public static void jadeProfile(FAGenericProcess process) {
        try {

            globaz.jade.common.JadeInitProperties.setLogToSystemOut(true);
            globaz.jade.common.Jade.getInstance();
            globaz.jade.jdbc.JadeJdbcProfiler.startProfiler();
            globaz.jade.prof.JadeProfiler.startProfiler();

            BSession session = new BSession("MUSCA");
            session.connect("globazf", "ssiiadm");

            process.setSession(session);
            FAApplication application = (FAApplication) GlobazServer.getCurrentSystem().getApplication(
                    process.getSession().getApplicationId());
            application._setLocalPath("U:\\Musca");
            process.setEMailAddress("btc@globaz.ch");
            process.setTransaction(new BTransaction(process.getSession()));
            process.getTransaction().openTransaction();

            process.executeProcess();

        } catch (Throwable e) {
            JadeLogger.error(process, e);
        } finally {
            if ((process != null) && (process.getTransaction() != null)) {
                try {
                    process.getTransaction().closeTransaction();
                } catch (Exception e) {
                    JadeLogger.error(process, e);
                }
            }
            //
            System.out.println("----------------------------------------");
            System.out.println(globaz.jade.prof.JadeProfiler.getName() + ":");
            System.out.println("----------------------------------------");
            System.out.println("CLASS statistics by Average Time:");
            System.out.println("----------------------------------------");
            for (Iterator<?> iter = globaz.jade.prof.JadeProfiler.getInstance().classStatisticsIterator(); iter
                    .hasNext();) {
                globaz.jade.prof.JadeProfilerRecord record = (globaz.jade.prof.JadeProfilerRecord) iter.next();
                System.out.println(record.getId() + " called "
                        + java.text.NumberFormat.getNumberInstance().format(record.getCount()) + " times for "
                        + java.text.NumberFormat.getNumberInstance().format(record.getFullTime()) + " ms (min="
                        + java.text.NumberFormat.getNumberInstance().format(record.getMinTime()) + " ms,max="
                        + java.text.NumberFormat.getNumberInstance().format(record.getMaxTime()) + " ms,average="
                        + java.text.NumberFormat.getNumberInstance().format(record.getAverageTime()) + " ms)");
            }
            System.out.println("----------------------------------------");
            System.out.println("CLASS statistics by Count:");
            System.out.println("----------------------------------------");
            for (Iterator<?> iter = globaz.jade.prof.JadeProfiler.getInstance().classStatisticsIteratorByCount(); iter
                    .hasNext();) {
                globaz.jade.prof.JadeProfilerRecord record = (globaz.jade.prof.JadeProfilerRecord) iter.next();
                System.out.println(record.getId() + " called "
                        + java.text.NumberFormat.getNumberInstance().format(record.getCount()) + " times for "
                        + java.text.NumberFormat.getNumberInstance().format(record.getFullTime()) + " ms (min="
                        + java.text.NumberFormat.getNumberInstance().format(record.getMinTime()) + " ms,max="
                        + java.text.NumberFormat.getNumberInstance().format(record.getMaxTime()) + " ms,average="
                        + java.text.NumberFormat.getNumberInstance().format(record.getAverageTime()) + " ms)");
            }
            System.out.println("----------------------------------------");
            System.exit(0);
        }

    }

    public static void main(String[] args) {
        FAImpressionFactureProcess process = null;
        try {

            process = new FAImpressionFactureProcess((BSession) globaz.globall.db.GlobazServer.getCurrentSystem()
                    .getApplication(globaz.musca.application.FAApplication.DEFAULT_APPLICATION_MUSCA)
                    .newSession("globazf", "ssiiadm"));
            FAApplication application = (FAApplication) GlobazServer.getCurrentSystem().getApplication(
                    process.getSession().getApplicationId());
            application._setLocalPath("U:\\Musca");
            process.setIdPassage("32");
            process.setEMailAddress("acu@globaz.ch");
            process.setTransaction(new BTransaction(process.getSession()));
            process._executeImpressionProcess(null);
            System.out.println("Programme terminé ! Copier le fichier PDF avant de presser <Enter>....");
            System.in.read();
            System.out.println("Arrêt du programme lancé !");
        } catch (Exception e) {
            JadeLogger.error(process, e);
        } finally {
            if ((process != null) && (process.getTransaction() != null)) {
                try {
                    process.getTransaction().closeTransaction();
                } catch (Exception e) {
                    JadeLogger.error(process, e);
                }
            }
            System.exit(0);

        }
    }

    protected java.lang.String actionModulePassage = new String();
    protected Boolean callEcran = new Boolean(false);
    /**
     * Lancement de l'impression en mode batch Date de création : (05.05.2003 15:53:19)
     * 
     * @param args
     *            java.lang.String[]
     */

    protected boolean echoToConsole = false;
    protected java.lang.String eMailObject = new String();
    protected Boolean envoyerGed = new Boolean(false);
    protected StringBuffer facturationBuffer = new StringBuffer();
    protected String fromIdExterneRole = new String();
    protected String idEnteteFacture = new String();

    protected String idPassage;

    // public abstract String getNumAffilieEnCours();

    protected FAPassage passage;

    private boolean successful = true;

    protected String tillIdExterneRole = new String();

    /**
     * Commentaire relatif au constructeur FAGenericProcess
     */
    public FAGenericProcess() {
        super();
    }

    /**
     * Construit un nouveau process en lui associant un process parent. Le log, la transaction et la session sont
     * partagés.
     * 
     * @param parent
     *            le process parent
     */
    public FAGenericProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Constructeur du type BProcess.
     * 
     * @param session
     *            la session utilisée par le process
     */
    public FAGenericProcess(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Effacer les afacts générés par le module Date de création : (20.05.2003 12:12:57)
     * 
     * @return boolean
     */
    public boolean _deleteAfactDuModule(FAAfactManager afactManager, FAModulePassage modulePassage) {

        if (afactManager == null) {
            afactManager = new FAAfactManager();
        }

        // rapatrier les afacts liés à ce module
        afactManager.setSession(getSession());
        afactManager.setForIdModuleFacturation(modulePassage.getIdModuleFacturation());
        afactManager.setForIdPassage(modulePassage.getIdPassage());

        BStatement statement = null;
        try {
            statement = afactManager.cursorOpen(getTransaction());
            FAAfact afact = null;

            // parcourir les afacts liés aux modules
            while ((afact = (FAAfact) afactManager.cursorReadNext(statement)) != null) {
                // supprimer l'afact (non manuel) lié aux module si la méthode
                // avantRegeneration a échoué

                if (!afact.isNew()) {
                    afact.delete(getTransaction());
                    if (!afact.hasErrors()) {
                        try {
                            getTransaction().commit();
                        } catch (Exception e) {
                            getMemoryLog().logMessage("Impossible de supprimer l'afact:" + e.getMessage(),
                                    FWMessage.AVERTISSEMENT, this.getClass().getName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        } finally {
            if (getTransaction().hasErrors()) {
                // rollback en cas d'erreur
                try {
                    getTransaction().rollback();
                } catch (Exception e) {
                    JadeLogger.error(this, e);
                }
            }
            try {
                // dans tous les cas fermer le cursor
                afactManager.cursorClose(statement);
                statement = null;
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return !isAborted();
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Déverouiller le passage Date de création : (20.05.2003 17:11:38)
     * 
     * @param passage
     *            globaz.musca.db.facturation.FAPassage
     */
    @SuppressWarnings("finally")
    public boolean _finalizePassage(FAPassage passage) {

        getMemoryLog().logMessage(
                getSession().getLabel("OBJEMAIL_FA_DODEVERROUILLE_INFO") + " : passage: " + getIdPassage(),
                FWMessage.INFORMATION, "");
        try {
            // Déverrouiller le passage à la fin des traitements de tous les
            // modules du passage
            passage.setEstVerrouille(new Boolean(false));

            // libérer la transaction afin de pouvoir déverrouiller le passage
            // dans tous les cas
            // et de ne pas committer d'ancienne transaction

            getTransaction().freeTransaction();
            passage.update(getTransaction());
            getTransaction().commit();
            successful = true;
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    getSession().getLabel("OBJEMAIL_FA_DODEVERROUILLE_ERROR") + " : passage: " + getIdPassage(),
                    FWMessage.FATAL, "");
            this._addError(getTransaction(), "Erreur lors du deverrouillage du passage. ");
        } finally {
            // pas de rollback car le passage doit être déverouillé dans tous
            // les cas à la fin des traitements
            if (getTransaction().hasErrors()) { // unused
            }
            return successful;
        }
    }

    /**
     * Déverouiller le passage Date de création : (20.05.2003 17:11:38)
     * 
     * @param passage
     *            globaz.musca.db.facturation.FAPassage
     */
    @SuppressWarnings("finally")
    public boolean _finalizePassageSetState(FAPassage passage, String status) {

        boolean successful = false;
        getMemoryLog().logMessage(
                getSession().getLabel("OBJEMAIL_FA_DODEVERROUILLE_INFO") + " : passage: " + getIdPassage(),
                FWMessage.INFORMATION, this.getClass().getName());
        // Déverrouiller le passage à la fin des traitements de tous les modules
        // du passage
        try {
            passage.setEstVerrouille(new Boolean(false));
            // si le passage est comptabilisé, ne plus changer son état
            if (!passage.getStatus().equalsIgnoreCase(FAPassage.CS_ETAT_COMPTABILISE)
                    && !passage.getStatus().equalsIgnoreCase(FAPassage.CS_ETAT_ANNULE)) {
                passage.setStatus(status);
            }
            // mmu: on crée une nouvelle transaction pour mettre le passage à
            // jour
            // mais on garde la transaction utilisés par les modules pour faire
            // remonter les erreurs
            BTransaction newTransaction = new BTransaction(getSession());
            passage.update(newTransaction);
            newTransaction.commit();
            newTransaction.closeTransaction();
            if (!getTransaction().hasErrors()) {
                getTransaction().commit();
                successful = true;
            } else {
                throw (new Exception());
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    getSession().getLabel("OBJEMAIL_FA_DODEVERROUILLE_ERROR") + " : passage: " + getIdPassage(),
                    FWMessage.FATAL, this.getClass().getName());
            this._addError(getTransaction(), "Erreur lors du deverrouillage du passage. ");
        } finally {
            // pas de rollback car le passage doit être déverouillé dans tous
            // les cas à la fin des traitements
            if (getTransaction().hasErrors()) { // unused
            }
            return successful;
        }
    }

    /**
     * On initialise le passage en le verrouillant 02.03.2005: BTC : le passage n'est plus mis en état traitement dans
     * cette méthode mais seulement après que touts les modules aient été appelés. Date de création : (20.05.2003
     * 17:11:38)
     * 
     * @param passage
     *            globaz.musca.db.facturation.FAPassage
     */
    @SuppressWarnings("finally")
    public boolean _initializePassage(FAPassage passage) {

        boolean successful = false;
        getMemoryLog().logMessage(
                getSession().getLabel("OBJEMAIL_FA_DOVERROUILLE_INFO") + " : passage: " + getIdPassage(),
                FWMessage.INFORMATION, this.getClass().getName());

        // Verrouiller le passage
        passage.setEstVerrouille(new Boolean(true));

        try {
            passage.update(getTransaction());
            if (!getTransaction().hasErrors()) {
                getTransaction().commit(); // Commiter
                successful = true;
            } else {
                throw (new Exception());
            }
        } catch (Exception e) {
            this._addError(getTransaction(), "Facturation: la transaction de verrouillage a échoué");
            getMemoryLog().logMessage(
                    getSession().getLabel("OBJEMAIL_FA_DOVERROUILLE_ERROR") + " : passage: " + getIdPassage(),
                    FWViewBeanInterface.ERROR, this.getClass().getName());
        } finally {
            if (getTransaction().hasErrors()) {
                try { // Rollback de la transaction()
                    getTransaction().rollback();
                } catch (Exception e) {
                    this._addError(getTransaction(), "Facturation: Problème de rollback");
                }
            }
            return successful;
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.05.2003 11:02:21)
     * 
     * @return boolean
     * @param passage
     *            globaz.musca.db.facturation.FAPassage
     */
    public boolean _passageIsValid(FAPassage passage) {
        // si le passage n'existe pas, arrêter le process
        if ((passage == null) || passage.isNew()) {
            return false;
        }
        // quitter si l'état du passage est ouvert
        if (FAPassage.CS_ETAT_OUVERT.equalsIgnoreCase(passage.getStatus())) {

            getMemoryLog().logMessage(
                    getSession().getLabel("OBJEMAIL_FA_PRINTNONCOMPTA") + " : passage: " + getIdPassage(),
                    FWMessage.INFORMATION, this.getClass().getName());

            return false;
        }
        // quitter si le passage est déjà verrouillé
        if (passage.isEstVerrouille().booleanValue()) {
            getMemoryLog().logMessage(
                    getSession().getLabel("OBJEMAIL_FA_ISVERROUILLE_INFO") + " : passage: " + getIdPassage(),
                    FWMessage.INFORMATION, this.getClass().getName());
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void _validate() throws Exception {

        if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
            this._addError("Le champ email doit être renseigné.");
        }

        setControleTransaction(true);
        setSendCompletionMail(true);
    }

    public void clearLogInfo4Process() {
        facturationBuffer.delete(0, facturationBuffer.length());
    }

    /**
     * Returns the actionModulePassage.
     * 
     * @return java.lang.String
     */
    public java.lang.String getActionModulePassage() {
        return actionModulePassage;
    }

    public Boolean getCallEcran() {
        return callEcran;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.publish.client.JadePublishDocumentProducer#getDocumentList()
     */
    public List<?> getDocumentList() {
        // TODO Raccord de méthode auto-généré
        return null;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.04.2003 16:32:58)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getEMailObject() {
        return eMailObject;
    }

    public Boolean getEnvoyerGed() {
        return envoyerGed;
    }

    /**
     * Returns the fromIdExterneRole.
     * 
     * @return String
     */
    public String getFromIdExterneRole() {
        return fromIdExterneRole;
    }

    /**
     * @return
     */
    public String getIdEnteteFacture() {
        return idEnteteFacture;
    }

    /**
     * Retourne id du Passage Date de création : (13.03.2003 08:26:21)
     * 
     * @return java.lang.String
     */
    public String getIdPassage() {
        return idPassage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.03.2003 14:30:11)
     * 
     * @return globaz.musca.db.facturation.FAPassage
     */
    public globaz.musca.db.facturation.FAPassage getPassage() {
        return passage;
    }

    /**
     * Returns the tillIdExterneRole.
     * 
     * @return String
     */
    public String getTillIdExterneRole() {
        return tillIdExterneRole;
    }

    /**
     * @return
     */
    public boolean isSuccessful() {
        return successful;
    }

    public void logInfo4Process(boolean procInfo, boolean procSuccessful, String labelKey) {
        if (procInfo) {
            this.logInfo4Process(procSuccessful, labelKey);
            clearLogInfo4Process();
        } else {
            facturationBuffer.append(getSession().getLabel(labelKey));
            this.logInfo4Process(facturationBuffer.toString());
            clearLogInfo4Process();
        }

    }

    /**
     * Method logInfo4Process.
     * 
     * @param procSuccessful
     * @param labelKey
     */
    public void logInfo4Process(boolean procSuccessful, String labelKey) {

        if (procSuccessful) {
            // logger
            facturationBuffer.append(getSession().getLabel("OBJEMAIL_FA_OK"));
        } else {
            // logger
            facturationBuffer.append(getSession().getLabel("OBJEMAIL_FA_NOK"));
        }
        facturationBuffer.append("\n" + getSession().getLabel(labelKey));
        this.logInfo4Process(facturationBuffer.toString());

    }

    public void logInfo4Process(String message) {
        getMemoryLog().logMessage(message, FWMessage.INFORMATION, "");
    }

    protected final void publishDocumentBis() throws Exception {
        publishDocuments();
    }

    /**
     * Sets the actionModulePassage.
     * 
     * @param actionModulePassage
     *            The actionModulePassage to set
     */
    public void setActionModulePassage(java.lang.String actionModulePassage) {
        this.actionModulePassage = actionModulePassage;
    }

    public void setCallEcran(Boolean callEcran) {
        this.callEcran = callEcran;
    }

    /**
     * Ajoute ou non des infomations de traitement dans la console. Date de création : (25.11.2002 10:27:48)
     * 
     * @param newEchoToConsole
     *            mettre à true si ces informations doivent apparaître dans la console.
     */
    public void setEchoToConsole(boolean newEchoToConsole) {
        echoToConsole = newEchoToConsole;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.04.2003 16:32:58)
     * 
     * @param newEMailObject
     *            java.lang.String
     */
    public void setEMailObject(java.lang.String newEMailObject) {
        eMailObject = newEMailObject;
    }

    public void setEnvoyerGed(Boolean envoyerGed) {
        this.envoyerGed = envoyerGed;
    }

    /**
     * Sets the fromIdExterneRole.
     * 
     * @param fromIdExterneRole
     *            The fromIdExterneRole to set
     */
    public void setFromIdExterneRole(String fromIdExterneRole) {
        this.fromIdExterneRole = fromIdExterneRole;
    }

    /**
     * @param string
     */
    public void setIdEnteteFacture(String string) {
        idEnteteFacture = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.03.2003 13:53:08)
     * 
     * @param idJournal
     *            java.lang.String
     */
    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.03.2003 14:30:11)
     * 
     * @param newPassage
     *            globaz.musca.db.facturation.FAPassageViewBean
     */
    public void setPassage(globaz.musca.db.facturation.FAPassage newPassage) {
        passage = newPassage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.03.2003 14:30:11)
     * 
     * @param newPassage
     *            globaz.musca.db.facturation.FAPassageViewBean
     */
    public void setPassage(globaz.musca.db.facturation.FAPassageViewBean newPassage) {
        passage = newPassage;
    }

    /**
     * @param b
     */
    public void setSuccessful(boolean b) {
        successful = b;
    }

    /**
     * Sets the tillIdExterneRole.
     * 
     * @param tillIdExterneRole
     *            The tillIdExterneRole to set
     */
    public void setTillIdExterneRole(String tillIdExterneRole) {
        this.tillIdExterneRole = tillIdExterneRole;
    }

}
