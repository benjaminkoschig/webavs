package globaz.leo.util;

import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.job.client.JadeJobServerFacade;
import globaz.jade.job.message.JadeJobInfo;
import globaz.leo.application.LEApplication;
import globaz.leo.process.handler.LEJournalHandler;
import java.math.BigDecimal;

public class LEGenReception extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        try {
            if (args.length < 4) {
                System.out.println("java globaz.leo.util.LEGenReception <user> <pass> <email> <list path>");
                throw new Exception("Wrong number of arguments");
            }

            String user = args[0];
            String pwd = args[1];

            Jade.getInstance();
            BISession session = globaz.globall.db.GlobazServer.getCurrentSystem()
                    .getApplication(LEApplication.DEFAULT_APPLICATION_LEO).newSession(user, pwd);

            LEGenReception process = new LEGenReception((BSession) session);

            process.setEMailAddress(args[2]);
            process.setListPath(args[3]);

            JadeJobInfo job = BProcessLauncher.start(process);

            while ((!job.isOut()) && (!job.isError())) {
                Thread.sleep(1000);
                job = JadeJobServerFacade.getJobInfo(job.getUID());
            }
            Thread.sleep(60000);
            if (job.isError()) {
                // erreurs critique, je retourne le code de retour not ok
                System.out.println("Erreur dans la génération automatique des réceptions !");
                System.out.println(job.getFatalErrorMessage());
                System.exit(200);
            } else {
                // pas d'erreurs critique, je retourne le code de retour ok
                System.out.println("Aucun problème dans la génération automatique des réceptions !");
                System.exit(0);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Erreur dans la génération automatique des réceptions !");
            // erreur critique, je retourne un code d'erreur 200
            System.exit(200);

        } finally {
            Jade.getInstance().endProfiling();
        }

        System.exit(0);
    }

    String listPath = "";

    public LEGenReception() {
        super();
    }

    public LEGenReception(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        LEJournalHandler j = new LEJournalHandler();

        java.io.BufferedReader fileIn = new java.io.BufferedReader(new java.io.FileReader(listPath));
        String line = "";
        String crtIdJournal = "";
        String dateReception = "";
        java.util.StringTokenizer tokens;
        while ((line = fileIn.readLine()) != null) {
            try {
                tokens = new java.util.StringTokenizer(line, ";");
                crtIdJournal = tokens.nextToken();
                try {
                    dateReception = tokens.nextToken();
                } catch (Exception e) {
                    dateReception = JACalendar.format(JACalendar.todayJJsMMsAAAA(), JACalendar.FORMAT_YYYYMMDD);
                }
                dateReception = JACalendar.format(new JADate(new BigDecimal(dateReception)));
                if (!JadeStringUtil.isIntegerEmpty(crtIdJournal)) {
                    j.genererJournalisationReception(crtIdJournal, dateReception, getSession(), null);
                } else {
                    getMemoryLog().logMessage("idJournal " + crtIdJournal + " n'est pas une valeur correcte",
                            FWMessage.ERREUR, "LEGenReception");
                }
            } catch (Exception e) {
                getMemoryLog().logMessage(
                        "Erreur constatée :" + e.getMessage() + " dernier idJournal: " + crtIdJournal,
                        FWMessage.ERREUR, "LEGenReception");
            }
        }
        return false;
    }

    @Override
    protected String getEMailObject() {
        return "Le traitement de la génération automatique des réceptions est terminé";
    }

    public String getListPath() {
        return listPath;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setListPath(String listPath) {
        this.listPath = listPath;
    }

}
