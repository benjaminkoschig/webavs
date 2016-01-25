package globaz.musca.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.common.Jade;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAModulePassage;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.db.facturation.FAPassageManager;

/**
 * Génération de la facturation. Date de création : (25.11.2002 11:52:37)
 * 
 * @author: BTCBProcess
 */
public class FAPassageFacturationUnifierLesOperationsProcess extends FAGenericProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {

        System.out.println("Utilisateur : <[user]> \n Mot de passe : <[password]>");
        FAPassageManager passageMana = null;
        // String idPassage = null;
        String user = null;
        String password = null;

        if (args.length > 0) {
            user = args[0];
            if (args.length > 1) {
                password = args[1];
            }
        } else {
            return;
        }

        FAPassageFacturationUnifierLesOperationsProcess process = new FAPassageFacturationUnifierLesOperationsProcess();
        try {
            Jade.getInstance();
            // Connection à l'application
            BSession session = new BSession("MUSCA");
            session.connect(user, password);
            process.setSession(session);
            process.setTransaction(new BTransaction(process.getSession()));
            process.getTransaction().openTransaction();

            passageMana = new FAPassageManager();
            passageMana.setSession(session);
            passageMana.setForStatus(FAPassage.CS_ETAT_VALIDE);
            passageMana.find();
            if (passageMana.size() > 0) {
                for (int i = 0; i < passageMana.size(); i++) {
                    FAPassage passage = (FAPassage) passageMana.getEntity(i);
                    process._executeLancerProcess(passage);
                }
            }
            JadeSmtpClient.getInstance().sendMail(process.getEMailAddress(), process.getEMailObject(),
                    process.getMemoryLog().getMessagesInString(),
                    JadeConversionUtil.toStringArray(process.getAttachedDocuments()));
            System.out.println("Arrêt du programme lancé !");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ((process != null) && (process.getTransaction() != null)) {
                try {
                    process.getTransaction().closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.exit(0);
        }
    }

    // fermer le process de comptabilisation d'Osiris.
    private boolean comptabilisation = false;

    private Boolean executerDeSuite = new Boolean(false);

    /**
     * Commentaire relatif au constructeur CICompteIndividuelProcess.
     */
    public FAPassageFacturationUnifierLesOperationsProcess() {
        super();
    }

    /**
     * Commentaire relatif au constructeur Doc2_3006Batch.
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public FAPassageFacturationUnifierLesOperationsProcess(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructeur du type BProcess.
     * 
     * @param session
     *            la session utilisée par le process
     */
    public FAPassageFacturationUnifierLesOperationsProcess(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:26:51)
     * 
     * @param passage
     * @return boolean
     */
    public boolean _executeLancerProcess(IFAPassage passage) {

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

        try {
            FAPassageFacturationProcess comptabiliser = new FAPassageFacturationProcess();
            comptabiliser.setSession(getSession());
            comptabiliser.setParent(this);
            comptabiliser.setTransaction(getTransaction());
            comptabiliser.setIdPassage(passage.getIdPassage());
            comptabiliser.setEMailAddress(getEMailAddress());
            comptabiliser.setActionModulePassage(FAModulePassage.CS_ACTION_COMPTABILISE);
            comptabiliser.setSendCompletionMail(false);
            comptabilisation = comptabiliser._executeProcess();
            comptabiliser.publishDocumentBis();
            comptabiliser.getMemoryLog();
            if (comptabiliser.isOnError()) {
                comptabilisation = false;
            } else {
                comptabilisation = true;
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Impossible de comptabiliser le passage: " + passage.getIdPassage() + e.getMessage(),
                    FWViewBeanInterface.ERROR, passage.getClass().getName());
        }

        // Sujet de l'E-mail
        if (getActionModulePassage().equals(FAModulePassage.CS_ACTION_COMPTABILISE)) {
            if (comptabilisation) {
                // Succes
                setEMailObject(getSession().getLabel("5006") + passage.getLibelle() + " (" + getIdPassage() + ")");
            } else {
                // Echec
                setEMailObject(getSession().getLabel("5007") + passage.getLibelle() + " (" + getIdPassage() + ")");
            }
        } else {
            if (!getTransaction().hasErrors()) {
                setEMailObject(getSession().getLabel("OBJEMAIL_FA_SUBJECT_GENERATION_REUSSIE") + passage.getLibelle()
                        + " (" + getIdPassage() + ")");
            } else {
                setEMailObject(getSession().getLabel("OBJEMAIL_FA_SUBJECT_GENERATION_ECHOUEE") + passage.getLibelle()
                        + " (" + getIdPassage() + ")");
            }
        }

        return true;

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:26:51)
     * 
     * @auteur: BTC
     * @return boolean
     */
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
        passage.setStatus(FAPassage.CS_ETAT_VALIDE);
        try {
            passage.update();
        } catch (Exception e) {
            getMemoryLog().logMessage("Impossible de mettre le passage à l'état validé: " + e.getMessage(),
                    FWViewBeanInterface.ERROR, passage.getClass().getName());
        }

        if (executerDeSuite.booleanValue()) {
            boolean estComptabilise = _executeLancerProcess(passage);

            return estComptabilise;
        } else {
            passage.setEstVerrouille(new Boolean(false));
            try {
                passage.update();
            } catch (Exception e) {
                getMemoryLog().logMessage("Impossible de mettre le passage à l'état validé: " + e.getMessage(),
                        FWViewBeanInterface.ERROR, passage.getClass().getName());
            }
            // Sujet de l'E-mail

            if (!getTransaction().hasErrors()) {
                // Succes
                setEMailObject(getSession().getLabel("4004") + passage.getLibelle() + " (" + getIdPassage() + ")");
                return true;
            } else {
                // Echec
                setEMailObject(getSession().getLabel("4005") + passage.getLibelle() + " (" + getIdPassage() + ")");
                return false;
            }

        }

    }

    public Boolean getExecuterDeSuite() {
        return executerDeSuite;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setExecuterDeSuite(Boolean executerDeSuite) {
        this.executerDeSuite = executerDeSuite;
    }
}
