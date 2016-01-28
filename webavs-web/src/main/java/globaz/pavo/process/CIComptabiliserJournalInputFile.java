package globaz.pavo.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.jade.job.client.JadeJobServerFacade;
import globaz.jade.job.message.JadeJobInfo;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.inscriptions.CIJournal;

public class CIComptabiliserJournalInputFile extends BProcess {

    private static final long serialVersionUID = 177575795832197602L;

    public static void main(String[] args) {
        try {
            BSession session = (BSession) GlobazServer.getCurrentSystem()
                    .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO).newSession(args[0], args[1]);
            CIComptabiliserJournalInputFile process = new CIComptabiliserJournalInputFile();
            process.setEMailAddress(args[2]);
            process.setPath(args[3]);
            process.setSession(session);
            JadeJobInfo job = BProcessLauncher.start(process);

            while ((!job.isOut()) && (!job.isError())) {
                Thread.sleep(1000);
                job = JadeJobServerFacade.getJobInfo(job.getUID());
            }
            Thread.sleep(60000);
            if (job.isError()) {
                // erreurs critique, je retourne le code de retour not ok
                System.out.println("Process Compta Journaux not executed successfully !");
                System.out.println(job.getFatalErrorMessage());
                System.exit(0);
            } else {
                // pas d'erreurs critique, je retourne le code de retour ok
                System.out.println("Process Compta Journaux executed successfully !");
                System.exit(1);
            }

        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.out.println("Process Annonces Centrale CIs has error(s) !");

            // erreur critique, je retourne un code d'erreur 200
            System.exit(1);
        } finally {

        }
        System.exit(0);

    }

    String path = "";

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        java.io.BufferedReader fileIn = new java.io.BufferedReader(new java.io.FileReader(path));
        String line = null;
        while ((line = fileIn.readLine()) != null) {
            CIJournal journal = new CIJournal();
            journal.setSession(getSession());
            journal.setIdJournal(line.trim());
            journal.retrieve();
            if (!journal.isNew()) {
                try {
                    journal.comptabiliser("", "", getTransaction(), this);
                    getTransaction().commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    getTransaction().rollback();
                    getTransaction().clearErrorBuffer();
                }
                System.out.println(line);
            } else {
                System.out.println("journal non retrouvé no :" + line);
                abort();
                break;
            }
        }

        return false;
    }

    @Override
    protected String getEMailObject() {
        return "";
    }

    public String getPath() {
        return path;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
