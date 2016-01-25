package globaz.pavo.process;

import globaz.framework.process.FWProcess;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.jade.job.client.JadeJobServerFacade;
import globaz.jade.job.message.JadeJobInfo;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.db.inscriptions.CIJournalManager;

public class CIClotureJournalProcess extends BProcess {

    private static final long serialVersionUID = -3372101069982859871L;
    public static final int CODE_RETOUR_ERREUR = 200;
    public static final int CODE_RETOUR_OK = 0;

    public static void main(String[] args) {

        try {

            String emailAdresse = "";
            if (args.length > 0) {
                emailAdresse = args[0];
            }
            String user = "";
            if (args.length > 1) {
                user = args[1];
            }
            String pwd = "";
            if (args.length > 2) {
                pwd = args[2];
            }

            BSession session = (BSession) GlobazServer.getCurrentSystem()
                    .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO).newSession(user, pwd);

            CIClotureJournalProcess process = new CIClotureJournalProcess();
            process.setSession(session);
            process.setEMailAddress(emailAdresse);
            JadeJobInfo job = BProcessLauncher.start(process);

            while ((!job.isOut()) && (!job.isError())) {
                Thread.sleep(1000);
                job = JadeJobServerFacade.getJobInfo(job.getUID());
            }
            Thread.sleep(60000);
            if (job.isError()) {
                // erreurs critique, je retourne le code de retour not ok
                System.out.println("Process Cloture Journaux CIs not executed successfully !");
                System.out.println(job.getFatalErrorMessage());
                System.exit(CIClotureJournalProcess.CODE_RETOUR_ERREUR);
            } else {
                // pas d'erreurs critique, je retourne le code de retour ok
                System.out.println("Process Cloture Journaux CIsexecuted successfully !");
                System.exit(CIClotureJournalProcess.CODE_RETOUR_OK);
            }

        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.out.println("Process Cloture Journaux CIs has error(s) !");

            // erreur critique, je retourne un code d'erreur 200
            System.exit(CIClotureJournalProcess.CODE_RETOUR_ERREUR);
        } finally {

        }
        System.exit(0);
    }

    /**
     * Constructor for CIClotureJournalProcess.
     */
    public CIClotureJournalProcess() {
        super();
    }

    /**
     * Constructor for CIClotureJournalProcess.
     * 
     * @param session
     */
    public CIClotureJournalProcess(BSession session) {
        super(session);
    }

    /**
     * Constructor for CIClotureJournalProcess.
     * 
     * @param parent
     */
    public CIClotureJournalProcess(FWProcess parent) {
        super(parent);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            System.out.println("starting execution");
            boolean aCloturer = true;
            CIJournalManager mgr = new CIJournalManager();
            mgr.setSession(getSession());
            mgr.setForIdEtat(CIJournal.CS_PARTIEL);
            mgr.find(getTransaction(), BManager.SIZE_NOLIMIT);
            for (int i = 0; i < mgr.size(); i++) {
                BStatement statement = null;
                CIJournal journal = (CIJournal) mgr.getEntity(i);
                // ne pas comptabiliser les journaux de l'assurance facultative
                // par ce process (comptabilisé uniquement via l'écran)
                if (!CIJournal.CS_ASSURANCE_FACULTATIVE.equals(journal.getIdTypeInscription())) {

                    CIEcritureManager ecrMgr = new CIEcritureManager();
                    ecrMgr.setSession(getSession());
                    ecrMgr.setForIdJournal(journal.getIdJournal());
                    ecrMgr.changeManagerSize(BManager.SIZE_NOLIMIT);
                    statement = ecrMgr.cursorOpen(getTransaction());
                    CIEcriture ecriture = null;
                    System.out.println(journal.getIdJournal());
                    while ((ecriture = (CIEcriture) ecrMgr.cursorReadNext(statement)) != null) {
                        String etatEcriture = ecriture.getIdTypeCompte();
                        if (CIEcriture.CS_CI.equals(etatEcriture)
                                || CIEcriture.CS_CI_SUSPENS_SUPPRIMES.equals(etatEcriture)
                                || CIEcriture.CS_CORRECTION.equals(etatEcriture)
                                || CIEcriture.CS_GENRE_6.equals(etatEcriture)
                                || CIEcriture.CS_GENRE_7.equals(etatEcriture)) {

                        } else {
                            aCloturer = false;
                            break;
                        }
                    }
                    if (!"0.00".equals(journal.getTotalControle())) {
                        if (!journal.getTotalInscrit().equals(journal.getTotalControle())) {
                            aCloturer = false;
                        }
                    }
                    if (aCloturer) {
                        journal.setIdEtat(CIJournal.CS_COMPTABILISE);
                        journal.update(getTransaction());
                        System.out.println("cloturé");
                        getMemoryLog().logMessage(
                                getSession().getLabel("JOURNAL_CI_NUMERO") + " " + journal.getIdJournal() + " "
                                        + getSession().getLabel("JOURNAL_CI_NUMERO_SUCCES"), FWMessage.INFORMATION,
                                this.getClass().toString());
                    }
                    aCloturer = true;
                    ecrMgr.cursorClose(statement);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        System.out.println("process ended");

        return !isAborted();
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("UPDATE_JOURNAUX_CI_ERREUR");
        } else {
            return getSession().getLabel("UPDATE_JOURNAUX_CI_SUCCES");
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }
}
